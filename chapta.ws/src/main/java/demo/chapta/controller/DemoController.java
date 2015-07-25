package demo.chapta.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import demo.chapta.Config;
import demo.chapta.ImageTool;

@RequestMapping(value = "/receive")
@Controller
public class DemoController {
	
	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
	
	class Element{
		
		private int position;
    	private int percent;
    	private String value;
    	
    	public Element(int position, int percent, String value){
    		this.position = position;
    		this.percent = percent;
    		this.value = value;
    	}
    	
    	public int getPosition(){ return this.position; }
    	public int getPercent(){ return this.percent; }
    	public String getValue(){ return this.value; }
	}
	
	private Config captchaConfig;
	public void setCaptchaConfig(Config config){ this.captchaConfig = config; }
	
	private Config priceConfig;
	public void setPriceConfig(Config config){ this.priceConfig = config; }
	
	@RequestMapping(value = "/captcha/red.do")
	@ResponseBody
	public String acceptActiveCaptcha(@RequestParam(value = "file", required = true) MultipartFile file) {

		long start = new java.util.Date().getTime();
		logger.debug("start process ActiveCaptcha : binary stream");
		StringBuilder sbActive = new StringBuilder();
		try {

			String strDir = this.captchaConfig.getTmpPath() + "/" + new Date().getTime();
			File dir = new File(strDir);
			dir.mkdir();

			ImageTool it = new ImageTool();
			it.setImage(it.getBufferedImage(file.getInputStream()));
			it.saveToFile(strDir + "/before.jpg");
	        it = it.midddleValueFilter(20, true).holdRed();//中值过滤//保留红色和白色背景
	        it.saveToFile(strDir + "/after.jpg");
	        
	        List<Element> list = new ArrayList<Element>();
	        BufferedImage img = it.getImage();
	        for(int i=0; i<captchaConfig.getOffsetX().length; i++){
	        	
	        	ImageTool itx = new ImageTool();
	        	BufferedImage subImg = img.getSubimage(captchaConfig.getOffsetX()[i], captchaConfig.getOffsetY(),
	        			captchaConfig.getWidth(), captchaConfig.getHeight());
	        	itx.setImage(subImg);
	        	int redPercent = itx.getActivePercent();
	        	
	        	itx = itx.changeToGrayImage().changeToBlackWhiteImage().removeBadBlock(1, 1, captchaConfig.getMinNearSpots());//灰度处理//黑白//去噪
	        	//ImageIO.write(subImg, "JPG", new File(String.format(this.captchaConfig.getTmpPath() + "/captcha-item-%d.jpg", i)));
	            String s = getSingleCharOcr(subImg, captchaConfig.getDict());
	            
	            list.add(new Element(i, redPercent, s));
	        }
	        
	        Collections.sort(list, new Comparator<Element>() {//按红色百分比倒序排序
				public int compare(Element o1, Element o2) {
					return o2.getPercent()-o1.getPercent();
				}
	        });
	        
	        List<Element> top4 = list.subList(0, 4);//获取top4
	        Collections.sort(top4, new Comparator<Element>(){//按出现顺序升序
				public int compare(Element o1, Element o2) {
					return o1.getPosition()-o2.getPosition();
				}
	        });
	        for(Element e:top4)
	        	sbActive.append(e.getValue());
	        
		} catch (IOException e) {
			
			e.printStackTrace();
			return "++++++";
		}
		
		long end = new java.util.Date().getTime();
		logger.info("CAPTCHA:[{}]", sbActive.toString());
		logger.debug("end process Captcha : cost {}ms", end-start);
		return sbActive.toString();
	}
	
	@RequestMapping(value = "/captcha/detail.do")
	@ResponseBody
	public String[] detailCaptcha(@RequestParam(value = "file", required = true) MultipartFile file) {
		
		List<String> rtn = new ArrayList<String>();
		long start = new java.util.Date().getTime();
		logger.debug("start process Captcha : binary stream");
		StringBuilder sb = new StringBuilder();
		try{
			
			String strDir = this.captchaConfig.getTmpPath();
			File dir = new File(strDir);
			
			ImageTool it = new ImageTool();
	    	BufferedImage bi = it.getBufferedImage(file.getInputStream());
	    	it.setImage(bi);
	    	it.saveToFile(strDir + "/captchaBefore.jpg");
	        it = it.changeToGrayImage().changeToBlackWhiteImage().removeBadBlock(1, 1, this.captchaConfig.getMinNearSpots());//灰度处理,黑白,去噪
	        it.saveToFile(strDir + "/captchaAfter.jpg");
	        
	        BufferedImage img = it.getImage();
	        for(int i=0; i<captchaConfig.getOffsetX().length; i++){
	        	
	        	BufferedImage item = img.getSubimage(captchaConfig.getOffsetX()[i], captchaConfig.getOffsetY(),
	        			captchaConfig.getWidth(), captchaConfig.getHeight());
	        	java.io.ByteArrayOutputStream arrayOS = new java.io.ByteArrayOutputStream();
	        	ImageIO.write(item, "JPG", arrayOS);
	        	rtn.add(new String(org.apache.commons.codec.binary.Base64.encodeBase64(arrayOS.toByteArray())));
	        	ImageIO.write(item, "JPG", new File(String.format(strDir + "/item-%d.jpg", i)));
	            String s = getSingleCharOcr(item, captchaConfig.getDict());
	            sb.append(s);
	        }
			
		} catch (IOException ex){
			
			ex.printStackTrace();
			return (String[])rtn.toArray();
		}
		
		long end = new java.util.Date().getTime();
		logger.info("CAPTCHA:[{}]", sb.toString());
		logger.debug("end process Captcha : cost {}ms", end-start);
		String[] result;
		rtn.add(sb.toString());
		result = rtn.toArray(new String[0]); 
		return result;
	}
	
	@RequestMapping(value = "/captcha.do")
	@ResponseBody
	public String acceptCaptcha(@RequestParam(value = "file", required = true) MultipartFile file) {
		
		long start = new java.util.Date().getTime();
		logger.debug("start process Captcha : binary stream");
		StringBuilder sb = new StringBuilder();
		try{
			String strDir = this.captchaConfig.getTmpPath() + "/" + new Date().getTime();
			File dir = new File(strDir);
			dir.mkdir();
			
			ImageTool it = new ImageTool();
	    	BufferedImage bi = it.getBufferedImage(file.getInputStream());
	    	it.setImage(bi);
	    	it.saveToFile(strDir + "/captchaBefore.jpg");
	        it = it.changeToGrayImage().changeToBlackWhiteImage().removeBadBlock(1, 1, captchaConfig.getMinNearSpots());//灰度处理,黑白,去噪
	        it.saveToFile(strDir + "/captchaAfter.jpg");
	        
	        BufferedImage img = it.getImage();
	        for(int i=0; i<captchaConfig.getOffsetX().length; i++){
	        	
	        	BufferedImage item = img.getSubimage(captchaConfig.getOffsetX()[i], captchaConfig.getOffsetY(),
	        			captchaConfig.getWidth(), captchaConfig.getHeight());
	        	//ImageIO.write(item, "JPG", new File(String.format(this.captchaConfig.getTmpPath() + "/captcha-item-%d.jpg", i)));
	            String s = getSingleCharOcr(item, captchaConfig.getDict());
	            sb.append(s);
	        }
			
		} catch (IOException ex){
			
			ex.printStackTrace();
			return "++++++";
		}
		
		long end = new java.util.Date().getTime();
		logger.info("CAPTCHA:[{}]", sb.toString());
		logger.debug("end process Captcha : cost {}ms", end-start);
		return sb.toString();
	}
	
	@RequestMapping(value = "/price.do")
	@ResponseBody
	public String acceptPrice(@RequestParam(value = "file", required = true) MultipartFile file){
		
		StringBuilder sb = new StringBuilder();
		try{
			
			ImageTool it = new ImageTool();
	    	BufferedImage bi = it.getBufferedImage(file.getInputStream());
	    	it.setImage(bi);
	    	it.saveToFile(this.priceConfig.getTmpPath() + "/priceBefore.bmp");
	        it = it.changeToGrayImage();//灰度处理
	        it = it.changeToBlackWhiteImage();//黑白
	        it = it.removeBadBlock(1, 1, priceConfig.getMinNearSpots());//去噪
	        it.saveToFile(this.priceConfig.getTmpPath() + "/priceAfter.bmp");
	        
	        BufferedImage img = it.getImage();
	        for(int i=0; i<priceConfig.getOffsetX().length; i++){
	        	
	        	BufferedImage item = img.getSubimage(priceConfig.getOffsetX()[i], priceConfig.getOffsetY(),
	        			priceConfig.getWidth(), priceConfig.getHeight());
	        	ImageIO.write(item, "JPG", new File(String.format(this.priceConfig.getTmpPath() + "/price-item-%d.jpg", i)));
	            String s = getSingleCharOcr(item, priceConfig.getDict());
	            sb.append(s);
	        }
	        //System.out.println("Price : " + sb.toString());
			
		} catch (IOException ex){
			return "++++++";
		}
		
		return sb.toString();
	}
	
	private static String getSingleCharOcr(BufferedImage img,  
            Map<BufferedImage, String> map) {  
        String result = "";  
        int width = img.getWidth();  
        int height = img.getHeight();  
        int min = width * height;  
        for (BufferedImage bi : map.keySet()) {  
            int count = 0;  
            Label1: for (int x = 0; x < width; ++x) {  
                for (int y = 0; y < height; ++y) {  
                    if (isWhite(img.getRGB(x, y)) != isWhite(bi.getRGB(x, y))) {  
                        count++;  
                        if (count >= min)  
                            break Label1;  
                    }
                }
            }
            if (count < min) {
                min = count;  
                result = map.get(bi);  
            }  
        }  
        return result;  
    }
	
	private static int isWhite(int colorInt) {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() > 100) {  
            return 1;  
        }  
        return 0;  
    }  

}

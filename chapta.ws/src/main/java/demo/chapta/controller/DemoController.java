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

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import demo.chapta.Config;
import demo.chapta.EhOrc;
import demo.chapta.IOrc;
import demo.chapta.ImageTool;
import demo.chapta.Orc;

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
	
	/***
	 * 识别图片红色验证码
	 * 1. 中值过滤
	 * 2. 保留白色和红色
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/captcha/red.do")
	@ResponseBody
	public String acceptActiveCaptcha(@RequestParam(value = "file", required = true) MultipartFile file) {

		long start = new java.util.Date().getTime();
		logger.info("accept process ActiveCaptcha : binary stream");
		StringBuilder sbActive = new StringBuilder();
		try {

			String strDir = this.captchaConfig.getTmpPath() + "/" + new Date().getTime();
			File dir = new File(strDir);
			dir.mkdir();

			ImageTool it = new ImageTool();
			it.setImage(ImageTool.getBufferedImage(file.getInputStream()));
			it.saveToFile(strDir + "/before.bmp");
	        it = it.midddleValueFilter(20, true).holdRed();//中值过滤//保留红色和白色背景
	        it.saveToFile(strDir + "/after.bmp");
	        
	        List<Element> list = new ArrayList<Element>();
	        BufferedImage img = it.getImage();
	        for(int i=0; i<captchaConfig.getOffsetX().length; i++){
	        	
	        	ImageTool itx = new ImageTool();
	        	BufferedImage subImg = img.getSubimage(captchaConfig.getOffsetX()[i], captchaConfig.getOffsetY(),
	        			captchaConfig.getWidth(), captchaConfig.getHeight());
	        	itx.setImage(subImg);
	        	int redPercent = itx.getActivePercent();	        	
	        	itx = itx.changeToGrayImage().changeToBlackWhiteImage().removeBadBlock(1, 1, captchaConfig.getMinNearSpots());//灰度处理//黑白//去噪
	        	itx.saveToFile(String.format("%s/%d.bmp", strDir, i));
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
		logger.info("end process ActiveCaptcha : cost {}ms", end-start);
		return sbActive.toString();
	}
	
	/***
	 * 1.灰度处理
	 * 2.二值处理（黑白）
	 * 3.去噪点
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/captcha/detail.do")
	@ResponseBody
	public String[] detailCaptcha(@RequestParam(value = "file", required = true) MultipartFile file) {
		
		List<String> rtn = new ArrayList<String>();
		long start = new java.util.Date().getTime();
		logger.debug("accept process detail Captcha : binary stream");
		StringBuilder sb = new StringBuilder();
		try{

			//文字识别
			IOrc orc = Orc.getInstance(this.captchaConfig);
			BufferedImage image = ImageTool.getBufferedImage(file.getInputStream());
			sb.append(orc.scanStringFromPic(image, 0, 0));
			
			//获取字图片
			for(int i=0; i<orc.getSubImages().length; i++){
				
				java.io.ByteArrayOutputStream arrayOS = new java.io.ByteArrayOutputStream();
	        	ImageIO.write(orc.getSubImages()[i], "JPG", arrayOS);
	        	rtn.add(new String(Base64.encodeBase64(arrayOS.toByteArray())));//BASE64 Encode
			}
			
		} catch (IOException ex){
			
			ex.printStackTrace();
			return (String[])rtn.toArray();
		}
		
		long end = new java.util.Date().getTime();
		logger.info("CAPTCHA:[{}], cost {}ms", sb.toString(), end-start);
		logger.debug("end process detail Captcha");
		
		String[] result;
		rtn.add(sb.toString());
		result = rtn.toArray(new String[0]); 
		return result;
	}
	/*@RequestMapping(value = "/captcha/detail.do")
	@ResponseBody
	public String[] detailCaptcha(@RequestParam(value = "file", required = true) MultipartFile file) {
		
		List<String> rtn = new ArrayList<String>();
		long start = new java.util.Date().getTime();
		logger.debug("accept process detail Captcha : binary stream");
		StringBuilder sb = new StringBuilder();
		try{
			
			String strDir = this.captchaConfig.getTmpPath();
			ImageTool it = new ImageTool();
	    	BufferedImage bi = it.getBufferedImage(file.getInputStream());
	    	it.setImage(bi);
	    	it.saveToFile(strDir + "/captchaBefore.bmp");
	    	it = it.changeToGrayImage().changeToBlackWhiteImage();//灰度处理,黑白,去噪
	        it.saveToFile(strDir + "/captchaAfter.bmp");
	        
	        BufferedImage img = it.getImage();
	        for(int i=0; i<captchaConfig.getOffsetX().length; i++){
	        	
	        	BufferedImage item = img.getSubimage(captchaConfig.getOffsetX()[i], captchaConfig.getOffsetY(),
	        			captchaConfig.getWidth(), captchaConfig.getHeight());
	        	ImageTool subIt = new ImageTool();
	        	subIt.setImage(item);
	        	subIt = subIt.removeBadBlock(1, 1, this.captchaConfig.getMinNearSpots());
	        	item = subIt.getImage();
	        	
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
		logger.debug("end process detail Captcha : cost {}ms", end-start);
		String[] result;
		rtn.add(sb.toString());
		result = rtn.toArray(new String[0]); 
		return result;
	}*/
	
	@RequestMapping(value = "/dynamic/captcha/detail.do")
	@ResponseBody
	public String[] detailDynamicCaptcha(@RequestParam(value = "file", required = true) MultipartFile file) {
		
		List<String> rtn = new ArrayList<String>();
		long start = new java.util.Date().getTime();
		logger.debug("accept process detail Captcha : binary stream");
		StringBuilder sb = new StringBuilder();
		try{

			//文字识别
			IOrc orc = Orc.getInstance(this.captchaConfig);
			EhOrc enhanceOrc = new EhOrc(this.captchaConfig, orc);
			BufferedImage image = ImageTool.getBufferedImage(file.getInputStream());
			sb.append(enhanceOrc.scanStringFromPic(image, 0, 0));
			
			//获取字图片
			for(int i=0; i<orc.getSubImages().length; i++){
				
				java.io.ByteArrayOutputStream arrayOS = new java.io.ByteArrayOutputStream();
	        	ImageIO.write(orc.getSubImages()[i], "JPG", arrayOS);
	        	rtn.add(new String(Base64.encodeBase64(arrayOS.toByteArray())));//BASE64 Encode
			}
			
		} catch (IOException ex){
			
			ex.printStackTrace();
			return (String[])rtn.toArray();
		}
		
		long end = new java.util.Date().getTime();
		logger.info("CAPTCHA:[{}], cost {}ms", sb.toString(), end-start);
		logger.debug("end process detail Captcha");
		
		String[] result;
		rtn.add(sb.toString());
		result = rtn.toArray(new String[0]); 
		return result;
	}
	
	@RequestMapping(value = "/dynamic/captcha.do")
	@ResponseBody
	public String acceptDynamicCaptcha(@RequestParam(value = "file", required = true) MultipartFile file) {
		
		long start = new java.util.Date().getTime();
		logger.debug("accept process Captcha : binary stream");
		StringBuilder sb = new StringBuilder();
		try{
			
			BufferedImage image = ImageTool.getBufferedImage(file.getInputStream());
			IOrc orc = Orc.getInstance(this.captchaConfig);
			EhOrc enhanceOrc = new EhOrc(this.captchaConfig, orc);
			sb.append(enhanceOrc.scanStringFromPic(image, "/" + new Date().getTime(), 0, 0));
			
		} catch (IOException ex){
			
			ex.printStackTrace();
			return "++++++";
		}
		
		long end = new java.util.Date().getTime();
		logger.info("CAPTCHA:[{}], cost : {}ms", sb.toString(), end-start);
		logger.debug("end process Captcha");
		return sb.toString();
	}
	
	/***
	 * 1.灰度处理
	 * 2.二值处理（黑白）
	 * 3.去噪点
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/captcha.do")
	@ResponseBody
	public String acceptCaptcha(@RequestParam(value = "file", required = true) MultipartFile file) {
		
		long start = new java.util.Date().getTime();
		logger.debug("accept process Captcha : binary stream");
		StringBuilder sb = new StringBuilder();
		try{
			
			BufferedImage image = ImageTool.getBufferedImage(file.getInputStream());
			IOrc orc = Orc.getInstance(this.captchaConfig);
			sb.append(orc.scanStringFromPic(image, "/" + new Date().getTime(), 0, 0));
		} catch (IOException ex){
			
			ex.printStackTrace();
			return "++++++";
		}
		
		long end = new java.util.Date().getTime();
		logger.info("CAPTCHA:[{}], cost {}ms", sb.toString(), end-start);
		logger.debug("end process Captcha");
		return sb.toString();
	}
	/*public String acceptCaptcha(@RequestParam(value = "file", required = true) MultipartFile file) {
		
		long start = new java.util.Date().getTime();
		logger.debug("accept process Captcha : binary stream");
		StringBuilder sb = new StringBuilder();
		try{
			String strDir = this.captchaConfig.getTmpPath() + "/" + new Date().getTime();
			File dir = new File(strDir);
			dir.mkdir();
			
			ImageTool it = new ImageTool();
	    	BufferedImage bi = it.getBufferedImage(file.getInputStream());
	    	it.setImage(bi);
	    	it.saveToFile(strDir + "/captchaBefore.bmp");
	    	it = it.changeToGrayImage().changeToBlackWhiteImage();//
	        it.saveToFile(strDir + "/captchaAfter.bmp");
	        
	        BufferedImage img = it.getImage();
	        for(int i=0; i<captchaConfig.getOffsetX().length; i++){
	        	
	        	BufferedImage item = img.getSubimage(captchaConfig.getOffsetX()[i], captchaConfig.getOffsetY(),
	        			captchaConfig.getWidth(), captchaConfig.getHeight());
	        	ImageTool itSub = new ImageTool();
	        	itSub.setImage(item);
	        	itSub = itSub.removeBadBlock(1, 1, captchaConfig.getMinNearSpots());//灰度处理,黑白,去噪
	        	item = itSub.getImage();
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
	}*/
	
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

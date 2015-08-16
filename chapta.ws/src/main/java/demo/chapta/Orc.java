package demo.chapta;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

public class Orc implements IOrc {
	
	private Orc(Config config){
		this.orcConfig = config;
	}
	
	static private Orc instance;
	static public IOrc getInstance(Config config){
		
		if(null == instance)
			instance = new Orc(config);
		return instance;
	}
	
	private Config orcConfig;
	private BufferedImage[] subImages;
	
	public BufferedImage[] getSubImages(){ return this.subImages; }
	/***
	 * 识别图片中的文字
	 * @param image
	 * @return
	 */
	public String scanStringFromPic(BufferedImage img, String subDir, int offsetX, int offsetY){
		
		String strDir = this.orcConfig.getTmpPath();
		if(null != subDir){
			strDir += subDir;
			File dir = new File(strDir);
			dir.mkdir();
		}
		
		StringBuilder sb = new StringBuilder();
		ImageTool it = new ImageTool(img);
		it.saveToFile(strDir + "/before.bmp");
    	it = it.changeToGrayImage().changeToBlackWhiteImage();//灰度//二值
    	it.saveToFile(strDir + "/after.bmp");
    	
    	this.subImages = new BufferedImage[this.orcConfig.getOffsetX().length];
        for(int i=0; i<this.orcConfig.getOffsetX().length; i++){//分割
        	
        	BufferedImage subImg = img.getSubimage(//子图片
        			this.orcConfig.getOffsetX()[i] + offsetX, this.orcConfig.getOffsetY() + offsetY,
        			this.orcConfig.getWidth(), this.orcConfig.getHeight());
        	
        	ImageTool itSub = new ImageTool(subImg);
        	itSub = itSub.removeBadBlock(1, 1, this.orcConfig.getMinNearSpots());//去噪
        	itSub.saveToFile(strDir + String.format("/sub-%d.bmp", i));
        	
        	this.subImages[i] = itSub.getImage();
            String s = getSingleCharFromPic(this.subImages[i], this.orcConfig.getDict());
            sb.append(s);
        }
        return sb.toString();
	}
	
	public String scanStringFromPic(BufferedImage img, int offsetX, int offsetY){
		
		return this.scanStringFromPic(img, null, offsetX, offsetY);
	}
	
	/***
	 * 图片中识别单字符
	 * @param img
	 * @param map
	 * @return
	 */
	private static String getSingleCharFromPic(BufferedImage img, Map<BufferedImage, String> map){
		
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
	
	private static boolean isWhite(int colorInt) {
		
		Color color = new Color(colorInt);
        return color.getRed() + color.getGreen() + color.getBlue() > 100;  
    }
}

package demo.chapta;

import java.awt.Color;  
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  

import javax.imageio.ImageIO;
  
public class ImagePreProcess {  
  
    public static int isWhite(int colorInt) {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() > 100) {  
            return 1;  
        }  
        return 0;  
    }  
  
    public static int isBlack(int colorInt) {  
        Color color = new Color(colorInt);  
        if (color.getRed() + color.getGreen() + color.getBlue() <= 100) {  
            return 1;  
        }

        return 0;  
    }  
  
    private static int isRed(int colorInt){
    	
    	Color color = new Color(colorInt);
    	if(color.getRed()>160 && color.getRed()<250)
    		return 1;
    	return 0;
    }
    private static int isGray(int colorInt){
    	Color color = new Color(colorInt);
    	if((color.getRed()>=122 && color.getRed()<=192) 
    			&& (color.getGreen()>=123 && color.getGreen() <= 187) 
    			&& (color.getBlue()>=123 && color.getBlue()<=180))
    		return 1;
    	return 0;
    }
    
    public static BufferedImage removeBackgroud(String picFile)  
            throws Exception {  
        BufferedImage img = ImageIO.read(new File(picFile));  
        int width = img.getWidth();  
        int height = img.getHeight();  
        for (int x = 0; x < width; ++x) {  
            for (int y = 0; y < height; ++y) {  
                if (isWhite(img.getRGB(x, y)) == 1) {  
                    img.setRGB(x, y, Color.WHITE.getRGB());  
                } else {  
                    img.setRGB(x, y, Color.BLACK.getRGB());  
                }  
            	//if(isBlack(img.getRGB(x, y)) == 1){
            	//	img.setRGB(x, y, Color.BLACK.getRGB());
            	//} else {
            	//	img.setRGB(x, y, Color.WHITE.getRGB());
            	//}
            }  
        }  
        return img;  
    }  
  
    public static List<BufferedImage> splitImage(BufferedImage img)  
            throws Exception {  
        List<BufferedImage> subImgs = new ArrayList<BufferedImage>();  
        subImgs.add(img.getSubimage(10, 6, 8, 10));  
        subImgs.add(img.getSubimage(19, 6, 8, 10));  
        subImgs.add(img.getSubimage(28, 6, 8, 10));  
        subImgs.add(img.getSubimage(37, 6, 8, 10));  
        return subImgs;  
    }  
  
    public static Map<BufferedImage, String> loadTrainData() throws Exception {  
        Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();  
        File dir = new File("/home/martin/dicts");  
        File[] files = dir.listFiles();  
        for (File file : files) {  
            map.put(ImageIO.read(file), file.getName().charAt(0) + "");  
        }  
        return map;  
    }  
  
    public static String getSingleCharOcr(BufferedImage img,  
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
  
    public static String getAllOcr(String file) throws Exception {  
        BufferedImage img = removeBackgroud(file);  
        List<BufferedImage> listImg = splitImage(img);  
        Map<BufferedImage, String> map = loadTrainData();  
        String result = "";  
        for (BufferedImage bi : listImg) {  
            result += getSingleCharOcr(bi, map);  
        }  
        ImageIO.write(img, "JPG", new File("/home/martin/img/result/"+result+".jpg"));  
        return result;  
    }  
  
  
    private Config config;
    public void setConfig(Config config){
    	this.config = config;
    }
    /** 
     * @param args 
     * @throws Exception 
     */  
    public static void main(String[] args) throws Exception {
    	
		ImageTool it = new ImageTool();
		it.setImage(ImageIO.read(new File("/home/martin/tmp/1439455106501/captchaBefore.bmp")));
		it.changeToGrayImage().changeToBlackWhiteImage().removeBadBlock(1, 1, 4);
		java.awt.Point init = it.findPoint();
		System.out.println(init.getX());
		System.out.println(init.getY());
    }
    
    public static Map<BufferedImage, String> load(String directory) throws Exception {
    	
    	Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();
    	File dir = new File(directory);
        File[] files = dir.listFiles();
        for (File file : files)
            map.put(ImageIO.read(file), file.getName().charAt(0) + "");
    	return map;
    }
}  
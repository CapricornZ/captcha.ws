package demo.chapta;

import java.awt.Color;  
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
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

    	Config config = Config.load();
    	
    	ImageTool it = new ImageTool();
    	BufferedImage bi = it.getBufferedImage(String.format("%s/captcha.jpg", args[0]));
        it.setImage(bi);
        it = it.changeToGrayImage();//灰度处理
        it = it.changeToBlackWhiteImage();//黑白
        it = it.removeBadBlock(1, 1, config.getMinNearSpots());//去噪
        it.saveToFile(String.format("%s/temp.jpg", args[0]));
        
        BufferedImage img = it.getImage();
        Map<BufferedImage, String> map = load(args[1]);
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<config.getOffsetX().length; i++){
        	
        	BufferedImage item = img.getSubimage(config.getOffsetX()[i], config.getOffsetY(),
        			config.getWidth(), config.getHeight());
            String s = getSingleCharOcr(item, map);
            sb.append(s);
        }
        System.out.println(sb.toString());
        
        FileOutputStream fo = new FileOutputStream(String.format("%s/captcha.txt", args[0]));
        fo.write(sb.toString().getBytes());
        fo.close();
        
        /*BufferedImage img = ImageIO.read(new File("/home/martin/xxx-0.jpg"));
        ImageIO.write(img.getSubimage(2, 4, 15, 20), "JPG", new File("/home/martin/x0.jpg"));
        ImageIO.write(img.getSubimage(18, 4, 15, 20), "JPG", new File("/home/martin/x1.jpg"));
        ImageIO.write(img.getSubimage(36, 4, 15, 20), "JPG", new File("/home/martin/x2.jpg"));
        ImageIO.write(img.getSubimage(54, 4, 15, 20), "JPG", new File("/home/martin/x3.jpg"));
        ImageIO.write(img.getSubimage(72, 4, 15, 20), "JPG", new File("/home/martin/x4.jpg"));
        ImageIO.write(img.getSubimage(90, 4, 15, 20), "JPG", new File("/home/martin/x5.jpg"));
        
        Map<BufferedImage, String> map = load();
        String s = getSingleCharOcr(img.getSubimage(2, 4, 15, 20), map);System.out.print(s);
        s = getSingleCharOcr(img.getSubimage(18, 4, 15, 20), map);System.out.print(s);
        s = getSingleCharOcr(img.getSubimage(36, 4, 15, 20), map);System.out.print(s);
        s = getSingleCharOcr(img.getSubimage(54, 4, 15, 20), map);System.out.print(s);
        s = getSingleCharOcr(img.getSubimage(72, 4, 15, 20), map);System.out.print(s);
        s = getSingleCharOcr(img.getSubimage(90, 4, 15, 20), map);System.out.print(s);*/
    	
    	/*BufferedImage img = ImageIO.read(new File("/home/martin/xxx.jpg"));
        ImageIO.write(img.getSubimage(2, 4, 15, 20), "JPG", new File("/home/martin/x0.jpg"));
        ImageIO.write(img.getSubimage(21, 4, 15, 20), "JPG", new File("/home/martin/x1.jpg"));
        ImageIO.write(img.getSubimage(41, 4, 15, 20), "JPG", new File("/home/martin/x2.jpg"));
        ImageIO.write(img.getSubimage(60, 4, 15, 20), "JPG", new File("/home/martin/x3.jpg"));
        ImageIO.write(img.getSubimage(78, 4, 15, 20), "JPG", new File("/home/martin/x4.jpg"));
        ImageIO.write(img.getSubimage(98, 4, 15, 20), "JPG", new File("/home/martin/x5.jpg"));
        
        Map<BufferedImage, String> map = load();
        String s = getSingleCharOcr(img.getSubimage(2, 4, 15, 20), map);System.out.print(s);
        s = getSingleCharOcr(img.getSubimage(21, 4, 15, 20), map);System.out.print(s);
        s = getSingleCharOcr(img.getSubimage(41, 4, 15, 20), map);System.out.print(s);
        s = getSingleCharOcr(img.getSubimage(60, 4, 15, 20), map);System.out.print(s);
        s = getSingleCharOcr(img.getSubimage(78, 4, 15, 20), map);System.out.print(s);
        s = getSingleCharOcr(img.getSubimage(98, 4, 15, 20), map);System.out.print(s);*/
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
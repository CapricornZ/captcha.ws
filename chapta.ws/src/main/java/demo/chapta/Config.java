package demo.chapta;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;

public class Config {
	
	public static Config load() throws FileNotFoundException, IOException{

		Properties prop = new Properties();
		//prop.load(Config.class.getResourceAsStream("application.properties"));
		prop.load(new FileInputStream("application.properties"));
		
		Config rtn = new Config();
		rtn.setHeight(Integer.parseInt(prop.getProperty("height")));
		rtn.setWidth(Integer.parseInt(prop.getProperty("width")));
		
		rtn.setMinNearSpots(Integer.parseInt(prop.getProperty("minNearSpots")));
		
		String offsetX = prop.getProperty("offsetX");
		String[] array = offsetX.split(",");
		int[] arrayInt = new int[array.length];
		for(int i=0; i<array.length; i++)
			arrayInt[i] = Integer.parseInt(array[i]);
		rtn.setOffsetX(arrayInt);
		rtn.setOffsetY(Integer.parseInt(prop.getProperty("offsetY")));
		return rtn;
	}
	
	private int minNearSpots;
	private int[] offsetX;
	private int offsetY;
	
	private int width;
	private int height;

	private Map<BufferedImage, String> dict;
	
	public Map<BufferedImage, String> getDict(){ return dict; }
	public void setDictPath(String path) throws IOException{
		
		Map<BufferedImage, String> map = new HashMap<BufferedImage, String>();
    	File dir = new File(path);
        File[] files = dir.listFiles();
        for (File file : files)
        	if(file.isFile())
        		map.put(ImageIO.read(file), file.getName().charAt(0) + "");
    	this.dict = map;
	}
	
	public int getMinNearSpots() { return minNearSpots; }
	public void setMinNearSpots(int minNearSpots) { this.minNearSpots = minNearSpots; }
	
	public int[] getOffsetX() { return offsetX; }
	public void setOffsetX(int[] offsetX) { this.offsetX = offsetX; }
	
	public int getOffsetY() { return offsetY; }
	public void setOffsetY(int offsetY) { this.offsetY = offsetY; }
	
	public int getWidth() { return width; }
	public void setWidth(int width) { this.width = width; }
	
	public int getHeight() { return height; }
	public void setHeight(int height) { this.height = height; }
	
	private String tmpPath;
	public void setTmpPath(String path){ this.tmpPath = path; }
	public String getTmpPath(){ return this.tmpPath; }
}

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

import demo.chapta.controller.rest.OrcConfig;

public class Config {
	
	public static Config load() throws FileNotFoundException, IOException{

		Properties prop = new Properties();
		prop.load(new FileInputStream("application.properties"));
		
		Config rtn = new Config();
		String offsetX = prop.getProperty("offsetX");
		String[] array = offsetX.split(",");
		int[] arrayInt = new int[array.length];
		for(int i=0; i<array.length; i++)
			arrayInt[i] = Integer.parseInt(array[i]);
		return rtn;
	}

	private String tmpPath;
	private OrcConfig orcConfig;
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
	
	public void setOrcConfig(OrcConfig config){ this.orcConfig = config; }
	
	public int getMinNearSpots() { return orcConfig.getMinNearSpots(); }
	
	public int[] getOffsetX() { return orcConfig.getOffsetX(); }
	
	public int getOffsetY() { return orcConfig.getOffsetY(); }
	
	public int getWidth() { return orcConfig.getWidth(); }
	
	public int getHeight() { return orcConfig.getHeight(); }

	public void setTmpPath(String path){ this.tmpPath = path; }
	public String getTmpPath(){ return this.tmpPath; }
}

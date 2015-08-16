package demo.chapta;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageScan {
	
	private BufferedImage image;
    private int width;
    private int height;

	public void setImage(BufferedImage image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }
	
	public void save(String file) throws IOException{
		ImageIO.write(image, "JPG", new File(file));
	}
	
	public ImageScan resize(int w, int h) throws IOException {

		ImageScan rtn = new ImageScan();
        //构建图片对象
        BufferedImage _image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);  
        //绘制缩小后的图  
        _image.getGraphics().drawImage(this.image, 0, 0, w, h, null);  
        rtn.setImage(_image);
        return rtn;
    }  
	
	public boolean isWhite(Color point){
		
		int r = point.getRed();
		return (r>=200);
	}
	
	public ImageScan subImageByColumn(int x0, int x1){
		
		BufferedImage _image = image.getSubimage(x0, 0, x1-x0, this.height);
		ImageScan rtn = new ImageScan();
		rtn.setImage(_image);
		return rtn;
	}
	
	public ImageScan subImageByRow(int y0, int y1){
		
		BufferedImage _image = image.getSubimage(0, y0, this.width, y1-y0);
		ImageScan rtn = new ImageScan();
		rtn.setImage(_image);
		return rtn;
	}
	
	public List<Integer> splitByRow(){
		
		List<Integer> result = this.scanRow();
		List<Integer> arrayY = new ArrayList<Integer>();
		
		boolean bFound = false;
		for(int i=0; !bFound && i<result.size(); i++)
			if(result.get(i) != 0){
				
				arrayY.add(i);
				bFound = true;
			}
		
		bFound = false;
		for(int i=arrayY.get(0); !bFound && i<result.size(); i++)
			if(result.get(i) == 0){
				
				arrayY.add(i);
				bFound = true;
			}
		
		return arrayY;
	}
	
	public List<Integer> splitByColumn(){
		
		List<Integer> result = this.scanColumn();
		List<Integer> arrayX = new ArrayList<Integer>();
		int tag = 0;//偶数表示找启示，奇数找中指;
		for(int i=1; i<result.size(); i++){
			if(tag % 2 == 0){
				
				if(result.get(i)-result.get(i-1) > 0 && result.get(i-1)<=2){
					arrayX.add(i);
					tag++;
				}
			} else {
				
				if(result.get(i)-result.get(i-1) < -4 && result.get(i)<=2){
					arrayX.add(i);
					tag++;
				}
			}
		}
		return arrayX;
	}
	
	public List<Integer> scanRow(){

		List<Integer> list = new ArrayList<Integer>();
		for(int y=0; y<height; y++){
			
			int countOfBlack = 0;
			for(int x=0; x<width; x++){
				
				Color point = new Color(this.image.getRGB(x, y));
				if(!isWhite(point))
					countOfBlack++;
			}
			System.out.println(String.format("ROW[%d]:%d", y, countOfBlack));
			list.add(countOfBlack);
		}
		return list;
	}
	
	public List<Integer> scanColumn(){
		
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0; i<width; i++){
			int countOfBlack = 0;
			for(int j=0; j<height; j++){
				
				Color point = new Color(this.image.getRGB(i, j));
				//System.out.println(String.format("r:%d, g:%d, b%d",point.getRed(), point.getGreen(), point.getBlue()));
				if(!isWhite(point))
					countOfBlack++;
			}
			System.out.println(String.format("COLUMN[%d]:%d", i, countOfBlack));
			list.add(countOfBlack);
		}
		return list;
	}
}

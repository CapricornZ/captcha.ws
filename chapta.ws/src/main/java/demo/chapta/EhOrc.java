package demo.chapta;

import java.awt.Point;
import java.awt.image.BufferedImage;

/***
 * 直方图找到左上角起点
 * @author martin
 */
public class EhOrc implements IOrc {

	private Config orcConfig;
	private IOrc instance;
	
	private Point findSourcePoint(BufferedImage image){
		
		ImageTool it = new ImageTool(image);
		it = it.changeToGrayImage().changeToBlackWhiteImage().removeBadBlock(1, 1, this.orcConfig.getMinNearSpots());//灰度处理,黑白,去噪
		Point initPos = it.findPoint();
		return initPos;
	}
	
	public EhOrc(Config config, IOrc instance){
		this.orcConfig = config;
		this.instance = instance;
	}
	
	@Override
	public String scanStringFromPic(BufferedImage img, String subDir, int offsetX, int offsetY) {
		
		Point initPos = this.findSourcePoint(img);
		BufferedImage activeImg = img.getSubimage((int)initPos.getX()-1, (int)initPos.getY()-1, 
				img.getWidth()-(int)initPos.getX()+1, img.getHeight()-(int)initPos.getY()+1);
		return this.instance.scanStringFromPic(activeImg, subDir, 
				offsetX-this.orcConfig.getOffsetX()[0], offsetY);
	}

	@Override
	public String scanStringFromPic(BufferedImage img, int offsetX, int offsetY) {
		return this.scanStringFromPic(img, null, offsetX, offsetY);
	}

	@Override
	public BufferedImage[] getSubImages() { return this.instance.getSubImages(); }
}

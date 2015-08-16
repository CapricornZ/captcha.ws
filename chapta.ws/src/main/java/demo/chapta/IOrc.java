package demo.chapta;

import java.awt.image.BufferedImage;

public interface IOrc {
	
	String scanStringFromPic(BufferedImage img, String subDir, int offsetX, int offsetY);
	String scanStringFromPic(BufferedImage img, int offsetX, int offsetY);
	
	BufferedImage[] getSubImages();
}

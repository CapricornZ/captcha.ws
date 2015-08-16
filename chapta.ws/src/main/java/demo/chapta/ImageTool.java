package demo.chapta;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

/**
 *
 * @author Administrator
 */
public class ImageTool {

    private BufferedImage image;
    private int width;
    private int height;
    
    public ImageTool(){}
    public ImageTool(BufferedImage img){
    	this.setImage(img);
    }

    /***
     * 利用直方图找左上角起点
     * @return
     */
    public Point findPoint(){
    
    	boolean bFound = false;
    	int offsetX;
    	for(offsetX=0; !bFound && offsetX < 20; offsetX++){
    		
    		List<Integer> rows = new ArrayList<Integer>();
    		for(int x=0; x<15; x++){
    			int black = 0;
    			for(int y=0; y<this.image.getHeight(); y++)
    				if(isBlack(new Color(this.image.getRGB(x+offsetX, y))))
    					black++;
    			rows.add(black);
    		}
    		
    		if(rows.get(0) == 0 && rows.get(1) != 0 && rows.get(2) != 0 && rows.get(3) != 0)
    			bFound = true;
    		
    		if(rows.get(0) == 0 && rows.get(1) == 0 && rows.get(2) == 0)
    			bFound = (rows.get(12) == 0 && rows.get(13) == 0 && rows.get(14) == 0);
    	}
    	
    	bFound = false;
    	int offsetY = 0;
    	for(int y=0; !bFound && y<this.image.getHeight(); y++){
    		
    		int black = 0;
    		for(int x=0; x<15; x++)
    			if(isBlack(new Color(this.image.getRGB(x+offsetX, y))))
    				black++;
    		if(black != 0){
    			offsetY = y;
    			bFound = true;
    		}
    	}
    	
    	return new Point(offsetX, offsetY);
    }
    
    /**
     * 变图像为黑白色 提示: 黑白化之前最好灰色化以便得到好的灰度平均值,利于获得好的黑白效果
     * @return
     */
    public ImageTool changeToBlackWhiteImage() {
        int avgGrayValue = getAvgValue();
        int whitePoint = getWhitePoint(), blackPoint = getBlackPoint();

        Color point;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                point = new Color(image.getRGB(j, i));
                image.setRGB(j, i, (point.getRed() < avgGrayValue ? blackPoint : whitePoint));
            }
        }
        return this;
    }

    public ImageTool clearNoise(int dgGrayValue, int maxNearPoints){
    	
    	ImageTool rtn = new ImageTool();
    	
    	BufferedImage _image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
    	Color[] points = new Color[8];
    	int white = this.getWhitePoint();
    	for(int x=0; x<this.width; x++)
    		for(int y=0; y<this.height; y++){
    			
    			Color color = new Color(this.image.getRGB(x, y));
    			if(color.getRed() < dgGrayValue){
    				
    				int nearDots = 0;
    				if(x==0 || x==width-1 || y==0 || y==height-1)
    					_image.setRGB(x, y, white);
    				else{

    					//points[0] = new Color(this.image.getRGB(x-1, y-1));
    	    			//points[1] = new Color(this.image.getRGB(x, y-1));
    	    			//points[2] = new Color(this.image.getRGB(x+1, y-1));
    	    			//points[3] = new Color(this.image.getRGB(x-1, y));
    	    			//points[4] = new Color(this.image.getRGB(x+1, y));
    	    			//points[5] = new Color(this.image.getRGB(x-1, y+1));
    	    			//points[6] = new Color(this.image.getRGB(x, y+1));
    	    			//points[7] = new Color(this.image.getRGB(x+1, y+1));
    					
    	    			points[0] = new Color(this.image.getRGB(x, y-1));
    	    			points[1] = new Color(this.image.getRGB(x-1, y));
    	    			points[2] = new Color(this.image.getRGB(x+1, y));
    	    			points[3] = new Color(this.image.getRGB(x, y+1));
    	    			
    	    			for(int i=0; i<4; i++)
    	    				if(points[i].getRed() < dgGrayValue)
    	    					nearDots ++ ;
    				}

    				if(nearDots < maxNearPoints)
    					_image.setRGB(x, y, white);
    			}
    			else
    				_image.setRGB(x, y, white);
    		}
    	
    	rtn.setImage(_image);
    	return rtn;
    }
    /***
     * 3*3中值滤波
     * @param dgGrayValue
     * @return
     */
    public ImageTool clearNoise(int dgGrayValue){
    	
    	Color[] points = new Color[9];
    	for(int y=1; y<this.height-1; y++){
    		for(int x=1; x<this.width-1; x++){
    			points[0] = new Color(this.image.getRGB(x-1, y-1));
    			points[1] = new Color(this.image.getRGB(x, y-1));
    			points[2] = new Color(this.image.getRGB(x+1, y-1));
    			points[3] = new Color(this.image.getRGB(x-1, y));
    			points[4] = new Color(this.image.getRGB(x, y));
    			points[5] = new Color(this.image.getRGB(x+1, y));
    			points[6] = new Color(this.image.getRGB(x-1, y+1));
    			points[7] = new Color(this.image.getRGB(x, y+1));
    			points[8] = new Color(this.image.getRGB(x+1, y+1));
    			
    			for(int j=0; j<5; j++)
    				for(int i=j+1;i<9;i++){

    					if(points[j].getRGB() < points[i].getRGB()){
    						Color tmp = points[j];
    						points[j] = points[i];
    						points[i] = tmp;
    					}
    				}
    			image.setRGB(x, y, new Color(points[4].getRed(), points[4].getRed(), points[4].getRed()).getRGB());
    			//image.setRGB(x, y, points[4].getRGB());
    		}
    	}
    	return this;
    }
    /**
     *
     *
     * @param whiteAreaPercent 过滤之后白色区域面积占整个图片面积的最小百分比
     * @param removeLighter true:过滤比中值颜色轻的,false:过滤比中值颜色重的,一般都是true
     * @return
     */
    public ImageTool midddleValueFilter(int whiteAreaMinPercent, boolean removeLighter) {
        int modify = 0;
        int avg = getAvgValue();
        Color point;
        while (getWhitePercent() < whiteAreaMinPercent) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    point = new Color(image.getRGB(j, i));
                    if (removeLighter) {
                        if (((point.getRed() + point.getGreen() + point.getBlue()) / 3) > avg - modify) {
//                         System.out.println(((point.getRed() + point.getGreen() + point.getBlue()) / 3)+"--"+(avg - modify));
                            image.setRGB(j, i, getWhitePoint());
                        }
                    } else {
                        if (((point.getRed() + point.getGreen() + point.getBlue()) / 3) < avg + modify) {
//                         System.out.println(((point.getRed() + point.getGreen() + point.getBlue()) / 3)+"--"+(avg - modify));
                            image.setRGB(j, i, getWhitePoint());
                        }
                    }

                }
            }
            modify++;
        }
//        System.out.println(getWhitePercent());
        return this;
    }
    
    /***
     * Active指的是红色
     * @return
     */
    public int getActivePercent() {
    	
        int active = 0;
        int height = image.getHeight();
        int width = image.getWidth();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                
            	Color point = new Color(image.getRGB(j, i));
                if(isRed(point))
                	active++;
            }
        }
        return (int) Math.ceil(((float) active * 100 / (width * height)));
    }
    
    private static boolean isRed(Color point){
    	
    	int g = point.getGreen();
    	int r = point.getRed();
    	int b = point.getBlue();
    	return (r>=150 && r>=b && r>=g);
    	//return (r>=145 && r>=b && r-g > 100);
    }
    
    private static boolean isBlack(Color point){
    	return point.getRed() == 0;
    }
    
    private static boolean isGray(Color point){
    	
    	int g = point.getGreen();
    	int r = point.getRed();
    	int b = point.getBlue();
    	
    	return ((g-50<r && g+50>r) && (g-50<b && g+50>r) && r > 100);
    }

    private int getWhitePercent() {
        Color point;
        int white = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                point = new Color(image.getRGB(j, i));
                if (((point.getRed() + point.getGreen() + point.getBlue()) / 3) == 255) {
                    white++;
                }
            }
        }
        return (int) Math.ceil(((float) white * 100 / (width * height)));
    }

    /**
     * @param 变图像为灰色 取像素点的rgb三色平均值作为灰度值
     *
     * @return
     */
    public ImageTool changeToGrayImage() {
        int gray;
        Color point;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                point = new Color(image.getRGB(j, i));
                gray = (point.getRed() + point.getGreen() + point.getBlue()) / 3;
                image.setRGB(j, i, new Color(gray, gray, gray).getRGB());
            }
        }
        return this;
    }

    /**
     *
     * 去除噪点和单点组成的干扰线 注意: 去除噪点之前应该对图像黑白化
     *
     * @param neighborhoodMinCount 每个点最少的邻居数
     * @return
     */
    public ImageTool removeBadBlock(int blockWidth, int blockHeight, int neighborhoodMinCount) {
        int val;
        int whitePoint = getWhitePoint();
        int counter, topLeftXIndex, topLeftYIndex;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //初始化邻居数为0
                counter = 0;
                topLeftXIndex = x - 1;
                topLeftYIndex = y - 1;
                //x1 y1是以x,y左上角点为顶点的矩形,该矩形包围在传入的矩形的外围,计算传入的矩形的有效邻居数目
                if (isBlackBlock(x, y, blockWidth, blockHeight)) {//只有当块是全黑色才计算
                    for (int x1 = topLeftXIndex; x1 <= topLeftXIndex + blockWidth + 1; x1++) {
                        for (int y1 = topLeftYIndex; y1 <= topLeftYIndex + blockHeight + 1; y1++) {
                            //判断这个点是否存在
                            if (x1 < width && x1 >= 0 && y1 < height && y1 >= 0) {
                                //判断这个点是否是传入矩形的外围点
                                if (x1 == topLeftXIndex || x1 == topLeftXIndex + blockWidth + 1
                                        || y1 == topLeftYIndex || y1 == topLeftYIndex + blockHeight + 1) {
                                    //这里假定图像已经被黑白化,取Red值认为不是0就是255 
                                    val = new Color(image.getRGB(x1, y1)).getRed();
//                                System.out.println(val + "--" + (centerVal));
                                    //如果这个邻居是黑色,就把中心点的有效邻居数目加一
                                    if (val == 0) {
                                        counter++;
                                    }
                                }
                            }
                        }
                    }
//                    System.out.println("-------------------");
//                System.out.println(x+"-"+y+"-"+counter);
                    if (counter < neighborhoodMinCount) {
                        image.setRGB(x, y, whitePoint);
                    }
                }
            }
        }
        return this;
    }

    /**
     * 如果点周围的黑点数达到补偿值就把这个点变为黑色
     *
     * @param addFlag 补偿阀值,通过观察处理过的图像确定,一般为2即可
     * @return
     */
    public ImageTool modifyBlank(int addFlag) {
        int val, counter = 0, topLeftXIndex, topLeftYIndex, blackPoint = getBlackPoint();
        Color point;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //初始化邻居数为0
                counter = 0;
                topLeftXIndex = x - 1;
                topLeftYIndex = y - 1;
                point = new Color(image.getRGB(x, y));
                //这里假定图像已经被黑白化,取Red值认为不是0就是255 
                val = point.getRed();
                //只有白点才进行补偿
                if (val == 255) {
                    for (int x1 = topLeftXIndex; x1 <= topLeftXIndex + 2; x1++) {
                        for (int y1 = topLeftYIndex; y1 <= topLeftYIndex + 2; y1++) {
                            //判断这个点是否存在
                            if (x1 < width && x1 >= 0 && y1 < height && y1 >= 0) {
                                //判断这个点是否是传入点的外围点
                                if (x1 == topLeftXIndex || x1 == topLeftXIndex + 2
                                        || y1 == topLeftYIndex || y1 == topLeftYIndex + 2) {
                                    //这里假定图像已经被黑白化,取Red值认为不是0就是255 
                                    val = new Color(image.getRGB(x1, y1)).getRed();
//                                System.out.println(val + "--" + (centerVal));
                                    //如果这个邻居是黑色,就把中心点的补偿数目加一
                                    if (val == 0) {
                                        counter++;
                                    }
                                }
                            }
                        }
                    }
                    //如果这个点周围的黑点数达到补偿值就把这个点变为黑色
                    if (counter >= addFlag) {
                        image.setRGB(x, y, blackPoint);
                    }
                }
            }
        }
        return this;
    }

    static public BufferedImage getBufferedImage(InputStream is){
    	try{
    		return ImageIO.read(is);
    	} catch (IOException ex){
    		Logger.getLogger(ImageTool.class.getName()).log(Level.SEVERE, null, ex);
            return null;
    	}
    }
    
    static public BufferedImage getBufferedImage(String filename) {
        File file = new File(filename);
        try {
            return ImageIO.read(file);
        } catch (IOException ex) {
            Logger.getLogger(ImageTool.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private boolean isBlackBlock(int startX, int startY, int blockWidth, int blockHeight) {
        int counter = 0;//统计黑色像素点的个数
        int total = 0;//统计有效像素点的个数
        int val;
        for (int x1 = startX; x1 <= startX + blockWidth - 1; x1++) {
            for (int y1 = startY; y1 <= startY + blockHeight - 1; y1++) {
                //判断这个点是否存在
                if (x1 < width && x1 >= 0 && y1 < height && y1 >= 0) {
                    total++;//有效像素点的个数
                    //这里假定图像已经被黑白化,取Red值认为不是0就是255 
                    val = new Color(image.getRGB(x1, y1)).getRed();
                    //如果这个点是黑色,就把黑色像素点的数目加一
                    if (val == 0) {
                        counter++;
                    }
                }
            }
        }
//        System.out.println(startX + "--" + startY + "" + (counter == total&&total!=0));
        return counter == total && total != 0;
    }
    
    public ImageTool holdActive(){
    	
    	int white = this.getWhitePoint();
        Color point;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                point = new Color(image.getRGB(j, i));
                if(!isGray(point) && !isRed(point))
                	image.setRGB(j,i, white);
            }
        }
        return this;
    }
    
    public ImageTool holdGray(){
    	
    	int white = this.getWhitePoint();
        Color point;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                point = new Color(image.getRGB(j, i));
                if(!isGray(point))
                	image.setRGB(j,i, white);
            }
        }
        return this;
    }
    
    public ImageTool holdRed(){
    	
    	int white = this.getWhitePoint();
        Color point;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                point = new Color(image.getRGB(j, i));
                if(!isRed(point))
                	image.setRGB(j,i, white);
            }
        }
        return this;
    }

    private int getWhitePoint() {
        return (new Color(255, 255, 255).getRGB() & 0xffffffff);
    }

    private int getBlackPoint() {
        return (new Color(0, 0, 0).getRGB() & 0xffffffff);
    }

    private int getAvgValue() {
        Color point;
        int total = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                point = new Color(image.getRGB(j, i));
                total += (point.getRed() + point.getGreen() + point.getBlue()) / 3;
            }
        }
        return total / (width * height);
    }

    public void saveToFile(String filePath) {
        try {
            String ext = filePath.substring(filePath.lastIndexOf(".") + 1);
            File newFile = new File(filePath);
            ImageIO.write(image, ext, newFile);
        } catch (IOException ex) {
            Logger.getLogger(ImageTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }
}
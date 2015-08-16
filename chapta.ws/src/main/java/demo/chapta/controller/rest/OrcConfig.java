package demo.chapta.controller.rest;

public class OrcConfig implements IOrcConfig {
	
	private int[] offsetX;
	private int offsetY;
	private int width, height;
	private int minNearSpots;
	
	public int[] getOffsetX() { return offsetX; }
	public void setOffsetX(int[] offsetX) { this.offsetX = offsetX; }
	public String getOffsetXText(){
		
		StringBuilder sb = new StringBuilder();
		sb.append(this.offsetX[0]);
		for(int i=1; i<this.offsetX.length; i++)
			sb.append(String.format(",%d", this.offsetX[i]));
		return sb.toString();
	}
	
	public int getOffsetY() { return offsetY; }
	public void setOffsetY(int offsetY) { this.offsetY = offsetY; }
	
	public int getWidth() { return width; }
	public void setWidth(int width) { this.width = width; }
	
	public int getHeight() { return height; }
	public void setHeight(int height) { this.height = height; }
	
	public int getMinNearSpots() { return minNearSpots; }
	public void setMinNearSpots(int minNearSpots) { this.minNearSpots = minNearSpots; }
	
	@Override
	public String getCategory() {
		return "OrcConfig";
	}
}

package demo.chapta.controller.rest;

public class OrcTipConfig implements IOrcConfig{

	private OrcConfig configTip;
	private OrcConfig configNo;
	
	public OrcConfig getConfigTip() { return configTip; }
	public void setConfigTip(OrcConfig config0) { this.configTip = config0; }

	public OrcConfig getConfigNo() { return configNo; }
	public void setConfigNo(OrcConfig config1) { this.configNo = config1; }

	@Override
	public String getCategory() {
		return "OrcTipConfig";
	}
}
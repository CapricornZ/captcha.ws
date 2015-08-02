package demo.chapta.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "SubmitPrice")
public class SubmitPrice {

	/**
	 * Point[0]:校验码
	 * Point[1]:正在获取校验码
	 * Point[2]:有效校验码：请输入第2到5位……
	 */
	private Point[] captcha;
	private Point inputBox;
	/***
	 * buttons[0]:确定
	 * buttons[1]:取消
	 * buttons[2]:
	 */
	private Point[] buttons;
	
	public Point[] getCaptcha() {
		return captcha;
	}
	public void setCaptcha(Point[] captcha) {
		this.captcha = captcha;
	}
	public Point getInputBox() {
		return inputBox;
	}
	public void setInputBox(Point inputBox) {
		this.inputBox = inputBox;
	}
	public Point[] getButtons() {
		return buttons;
	}
	public void setButtons(Point[] buttons) {
		this.buttons = buttons;
	}
}

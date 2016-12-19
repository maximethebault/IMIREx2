package org.tud.imir.ex2.core.descriptor;

enum valueType {
	RGB, YCaCb
}

public class ColorValues {

	private int red;
	private int green;
	private int blue;
	
	private float y;
	private float cb;
	private float cr;
	
	public ColorValues(int colorType1, int colorType2, int colorType3, valueType colorSpace) {
		
		switch(colorSpace) {
			case RGB:
				this.red = colorType1;
				this.green = colorType2;
				this.blue = colorType3;
				break;
				
			case YCaCb:
				this.y = colorType1;
				this.cb = colorType2;
				this.cr = colorType3;
		}
		
		this.convertToYCbCr();
	}
	
	public ColorValues() {
	}
	
	
	public double getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public double getCb() {
		return cb;
	}

	public void setCb(float cb) {
		this.cb = cb;
	}

	public double getCr() {
		return cr;
	}

	public void setCr(float ca) {
		this.cr = ca;
	}
	
	public int getRed() {
		return red;
	}
	public void setRed(int red) {
		this.red = red;
	}
	public int getGreen() {
		return green;
	}
	public void setGreen(int green) {
		this.green = green;
	}
	public int getBlue() {
		return blue;
	}
	public void setBlue(int blue) {
		this.blue = blue;
	}
	
	public void convertToYCbCr() {
		this.y = (float) ((0.299 * this.red) + (0.587 * this.green) + ( 0.114 * this.blue));
		this.cb = (float) (128 - (0.168736 * this.red) - (0.331264 * this.green) + ( 0.5 * this.blue));
		this.cr = (float) (128 + (0.5 * this.red) - (0.418688 * this.green) - ( 0.081312 * this.blue));
	}
}

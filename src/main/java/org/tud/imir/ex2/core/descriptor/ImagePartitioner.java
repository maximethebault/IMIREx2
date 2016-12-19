package org.tud.imir.ex2.core.descriptor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImagePartitioner {

	private BufferedImage img;
	private ArrayList<ArrayList<ColorValues>> colorMatrix;
	
	public void setImage(BufferedImage img) {
		this.img = img;
	}
	
	public ArrayList<ArrayList<ColorValues>> getColorMatrix () {
		// holds 8x8 average-color values of image
		colorMatrix = new ArrayList<ArrayList<ColorValues>>();
		int imageBlockHeight = img.getHeight()/8;
		int imageBlockWidth = img.getWidth()/8;
		
		// y
		for (int y = 0; y < 8; y++) {
			
			// x
			for (int x = 0; x < 8; x++) {
				
				int[] colorValuesBlock = img.getRGB(y*imageBlockWidth, x*imageBlockHeight, imageBlockWidth, imageBlockHeight, null, 0, imageBlockWidth);
				int[] currentRGB = {0,0,0};
				
				for (int colorValue:colorValuesBlock) {
					
					Color currentColor = new Color(colorValue);
					
					currentRGB[0] += currentColor.getRed();
					currentRGB[1] += currentColor.getGreen();
					currentRGB[2] += currentColor.getBlue();
					
				}
				// get average
				currentRGB[0] /= imageBlockHeight*imageBlockWidth;
				currentRGB[1] /= imageBlockHeight*imageBlockWidth;
				currentRGB[2] /= imageBlockHeight*imageBlockWidth;
				
				// for first new colorset, add new list/matrix entry
				if (x == 0) {
					ArrayList<ColorValues> firstColor = new ArrayList<ColorValues>();
					firstColor.add(new ColorValues(currentRGB[0],currentRGB[1],currentRGB[2], valueType.RGB));
					colorMatrix.add(firstColor);
				} else {
					colorMatrix.get(y).add(new ColorValues(currentRGB[0],currentRGB[1],currentRGB[2], valueType.RGB));
				}
				
			}
		}
		
		return colorMatrix;
	}
}

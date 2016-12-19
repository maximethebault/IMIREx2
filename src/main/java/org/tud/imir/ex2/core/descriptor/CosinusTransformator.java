package org.tud.imir.ex2.core.descriptor;

import java.util.ArrayList;

enum colorValue {
	Y, Cb, Cr
}

public class CosinusTransformator {

	private float N = 8;
	private ArrayList<ArrayList<ColorValues>> blockColorMatrix;
	
	
	public ArrayList<ArrayList<ColorValues>> getBlockColorValue() {
		return blockColorMatrix;
	}


	public void setBlockColorMatrix(ArrayList<ArrayList<ColorValues>> blockColorMatrix) {
		this.blockColorMatrix = blockColorMatrix;
	}


	public float getN() {
		return N;
	}


	public void setN(float n) {
		N = n;
	}


	public ArrayList<ArrayList<Integer>> getValue(colorValue value) {
		
		float alphaU = 0;
		float alphaV = 0;

		ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
			
		// iterate over matrix line by line
		for (int v = 0; v < 8; v++) {
			for (int u = 0; u < 8; u++) {
				
				float result = 0;
				
				// build sum
				for (int y = 0; y <= this.N-1; y++) {
					for (int x = 0; x <= this.N-1; x++) {
						
						float colorValueFloat = 0;
						
						switch(value) {
						case Y:
							colorValueFloat = (float) this.blockColorMatrix.get(y).get(x).getY();
							break;
						case Cb:
							colorValueFloat = (float) this.blockColorMatrix.get(y).get(x).getCb();
							break;
						case Cr:
							colorValueFloat = (float) this.blockColorMatrix.get(y).get(x).getCr();
							break;
						}
						
						result += colorValueFloat * (Math.cos( (Math.PI * (2.0*x+1) * u)/(2.0*this.N) ) ) * (Math.cos( (Math.PI * (2.0*y+1) * v)/(2.0*this.N) ));
					}
					
				}
				
				if (u == 0) {
					alphaU = (float) Math.sqrt(1.0/this.N);
				} else {
					alphaU = (float) Math.sqrt(2.0/this.N);
				}
				
				if (v == 0) {
					alphaV = (float) Math.sqrt(1.0/this.N);
				} else {
					alphaV = (float) Math.sqrt(2.0/this.N);
				}
				
				result *= alphaU * alphaV;
				
				// for first new colorset, add new list/matrix entry
				if (v == 0) {
					ArrayList<Integer> firstValue = new ArrayList<Integer>();
					firstValue.add(Math.round(result));
					matrix.add(firstValue);
				} else {
					matrix.get(u).add(Math.round(result));
				}
				
			}
		}
		
		return matrix;
	}
	
}

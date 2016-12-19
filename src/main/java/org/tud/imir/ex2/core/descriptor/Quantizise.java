package org.tud.imir.ex2.core.descriptor;

import java.util.ArrayList;

public class Quantizise {

	public ArrayList<ArrayList<Integer>> quantisizeMatrix(ArrayList<ArrayList<Integer>> matrix, Integer quantization) {
		
		
		for (Integer i = 0; i < matrix.size(); i++) {
			for (Integer j = 0; j < matrix.get(i).size(); j++) {
				Integer quantisizeValue = matrix.get(i).get(j) / quantization;
				matrix.get(i).set(j, quantisizeValue);
			}
		}
		return matrix;
	}
	
}

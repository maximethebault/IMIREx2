package org.tud.imir.ex2.core.descriptor;

import java.util.ArrayList;
import java.util.List;

public class ZickZackReader {

	private List<ArrayList<Integer>> matrix;
	
	public List<ArrayList<Integer>> getMatrix() {
		return matrix;
	}

	public void setMatrix(ArrayList<ArrayList<Integer>> matrix) {
		this.matrix = matrix;
	}

	public Integer getDCValue() {
		return matrix.get(0).get(0); 
	}
	
	public List<Integer> get2ACValues() {
		
		List<Integer> outputList = new ArrayList<Integer>();
		
		outputList.add(matrix.get(0).get(1));
		outputList.add(matrix.get(1).get(0));
		
		return outputList;
	}
	
	public List<Integer> get5ACValues() {
		
		List<Integer> outputList = new ArrayList<Integer>();
		
		outputList.add(matrix.get(0).get(1));
		outputList.add(matrix.get(1).get(0));
		outputList.add(matrix.get(2).get(0));
		outputList.add(matrix.get(1).get(1));
		outputList.add(matrix.get(0).get(2));
		
		return outputList;
	}
}

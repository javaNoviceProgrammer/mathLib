package mathLib.utils;

import java.util.ArrayList;

import net.objecthunter.exp4j.ExpressionBuilder;

public class StringUtils {

	public static boolean isNumeric(String s) {
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}

	public static double toDouble(String s) {
		if (isNumeric(s)) {
			return Double.parseDouble(s);
		} else {
			return Double.NaN;
		}
	}

	public static double[] toDoubleArray(String s) {
		String str = s.replaceAll("[!?,;]", "");
		String[] words = str.split("\\s+");
		int n = words.length;
		double[] numbers = new double[n];
		for (int i = 0; i < n; i++) {
			numbers[i] = toDouble(words[i]);
		}
		return numbers;
	}

	public static double evaluate(String expression){
		double result = new ExpressionBuilder(expression).build().evaluate() ;
		return result ;
	}
	
	public static ArrayList<String> toArrayList(String[] st) {
		ArrayList<String> list = new ArrayList<>() ;
		for(String s : st) {
			list.add(s) ;
		}
		return list ;
	}
	
	
	

}

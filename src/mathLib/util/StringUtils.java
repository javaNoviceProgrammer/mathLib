package mathLib.util;

import java.text.DecimalFormat;
import java.util.ArrayList;

import mathLib.numbers.Complex;
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

	public static String fixedWidthDoubletoString(double var0, int var2, int var3) {
		DecimalFormat var4 = new DecimalFormat();
		var4.setMaximumFractionDigits(var3);
		var4.setMinimumFractionDigits(var3);
		var4.setGroupingUsed(false);

		String var5;
		for (var5 = var4.format(var0); var5.length() < var2; var5 = " " + var5) {
			;
		}

		return var5;
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

	public static double evaluate(String expression) {
		double result = new ExpressionBuilder(expression).build().evaluate();
		return result;
	}

	public static ArrayList<String> toArrayList(String[] st) {
		ArrayList<String> list = new ArrayList<>();
		for (String s : st) {
			list.add(s);
		}
		return list;
	}

	public static ArrayList<Complex> toArrayList(Complex[] st) {
		ArrayList<Complex> list = new ArrayList<>();
		for (Complex s : st) {
			list.add(s);
		}
		return list;
	}

	public static ArrayList<Double> toArrayList(Double[] st) {
		ArrayList<Double> list = new ArrayList<>();
		for (double s : st) {
			list.add(s);
		}
		return list;
	}

}

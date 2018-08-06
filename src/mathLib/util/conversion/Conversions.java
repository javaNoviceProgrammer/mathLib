package mathLib.util.conversion;

public class Conversions {
	// convert degree to radian
	public static class Angles {
		public static double toRadian(double degree) {
			return degree * Math.PI / 180;
		}

		public static double toDegree(double radian) {
			return radian * 180 / Math.PI;
		}
	}

	// convert lenght units
	public static class Length {
		public static double meters_to_feet(double length_meters) {
			return (length_meters * 3.28084);
		}

		public static double feet_to_meters(double length_feet) {
			return (length_feet / 3.28084);
		}
	}

	// convert weight units
	public static class Weight {
		public static double kg_to_lb(double weight_kg) {
			return (weight_kg * 2.20462);
		}

		public static double lb_to_kg(double weight_lb) {
			return (weight_lb / 2.20462);
		}
	}

	// this class for converting strings of numbers to arrays of numbers
	public static class Strings {
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
	}
}

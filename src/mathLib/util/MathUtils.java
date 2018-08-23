package mathLib.util;

import flanagan.integration.IntegralFunction;
import flanagan.math.ArrayMaths;
import mathLib.integral.Integral1D;
import mathLib.numbers.Complex;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.util.ArrayList;

public class MathUtils {

	public static double evaluate(String expression){
		double result = new ExpressionBuilder(expression).build().evaluate() ;
		return result ;
	}

	public static double[] linspace(double start, double end, int numPoints){
		double[] Values = new double[numPoints] ;
		double Delta ;
		if(numPoints == 1){
			Delta = 0 ;
		}
		else{
			Delta = (end - start)/(numPoints-1) ;
		}
		for(int i=0; i<numPoints; i++){
			Values[i] = start + i*Delta ;
		}
		return Values ;
	}

	public static double[] linspace(double start, double end, double stepSize){
		ArrayList<Double> values = new ArrayList<Double>() ;
		for(double x = start; x<=end; x+=stepSize){
			values.add(x) ;
		}
		double[] Values = new double[values.size()] ;
		for(int i=0; i<values.size(); i++){
			Values[i] = values.get(i) ;
		}
		return Values ;
	}
	
	public static double deltaKronecker(int i, int j) {
		if(i == j)
			return 1 ;
		else 
			return 0 ;
	}
	
	public static double deltaKronecker(int i, int j, int k) {
		if(i == j && i == k)
			return 1 ;
		else 
			return 0 ;
	}
	
	public static double deltaKronecker(int i, int j, int k, int l) {
		if(i == j && i == k && i == l)
			return 1 ;
		else 
			return 0 ;
	}
	
	public static class Functions{
		public static int factorial (int m) {
			if(m==0) return 1 ;
			else return m*factorial(m-1) ;
		}
		// adding the hyperbolic inverse functions
		public static double asinh(double x) {
			return Math.log(x + Math.sqrt(x*x + 1.0));
			}
	
		public static double acosh(double x) {
			return Math.log(x + Math.sqrt(x*x - 1.0));
			}
	
		public static double atanh(double x) {
			return 0.5*Math.log( (x + 1.0) / (-x + 1.0) );
			}
	
		public static double acoth(double x){
			return 1/atanh(x) ; // note that acoth(x) has a singularity at x=0
		}
	
		// adding sinc function
		public static double sinc(double x){
			if(x==0){return 1 ; }
			else{return Math.sin(Math.PI*x)/(Math.PI*x) ; }
		}
	
		public static double sign(double x){
			if(x>0) {return +1 ;}
			else if(x<0) {return -1 ;}
			else {return 0 ;}
		}
	
		public static double step(double stepPoint, double x){
			if(x>=stepPoint){return 1 ;}
			else{return 0;}
		}
	
		public static double unitStep(double stepPoint, double x){
			if(x>=stepPoint){return 1 ;}
			else{return 0;}
		}
	
		public static double sinIntegral(double x){
			IntegralFunction func = new IntegralFunction() {
	
				@Override
				public double function(double t) {
					double y = MathUtils.Functions.sinc(t/Math.PI);
					return y;
				}
			};
			Integral1D result = new Integral1D(func, 0, x) ;
			return result.getIntegral() ;
		}
	
	}

	public static class Conversions{
		// convert degree to radian
		public static class Angles{
			public static double toRadian(double degree){
				return degree*Math.PI/180 ;
			}

			public static double toDegree(double radian){
				return radian*180/Math.PI ;
			}
		}
		// convert lenght units
		public static class Length{
			public static double meters_to_feet(double length_meters){
				return (length_meters * 3.28084) ;
			}
			public static double feet_to_meters(double length_feet){
				return (length_feet / 3.28084) ;
			}
		}
		// convert weight units
		public static class Weight{
			public static double kg_to_lb(double weight_kg){
				return (weight_kg * 2.20462) ;
			}
			public static double lb_to_kg(double weight_lb){
				return (weight_lb / 2.20462) ;
			}
		}

		// this class for converting strings of numbers to arrays of numbers
		public static class Strings{
			public static boolean isNumeric(String s){
				return s != null && s.matches("[-+]?\\d*\\.?\\d+");
			}
			public static double toDouble(String s){
				if(isNumeric(s)) {return Double.parseDouble(s);}
				else{return Double.NaN ;}
			}
			public static double[] toDoubleArray(String s){
				String str = s.replaceAll("[!?,;]", "");
				String[] words = str.split("\\s+");
				int n = words.length ;
				double[] numbers = new double[n] ;
				for(int i=0; i<n; i++) {numbers[i] = toDouble(words[i]) ;}
				return numbers ;
			}
		}

		// converting to dB
		public static double todB(double x){
			return checkNaN(10*Math.log10(x)) ;
		}

		private static double checkNaN(double x){
			if (x<0){
				if(Double.isInfinite(x)||Double.isNaN(x)){
					return -300 ;
				}
				else{
					return x ;
				}
			}
			else if(x>0){
				if(Double.isInfinite(x)||Double.isNaN(x)){
					return 300 ;
				}
				else{
					return x ;
				}
			}
			else{
				return 0 ;
			}
		}

		// converting from dB
		public static double fromdB(double xdB){
			return Math.pow(10, xdB/10) ;
		}

	}

	//******************************************************************************

	/** adding array operations just like MATLAB.
	 *  This part includes all the array-based MATLAB functions.
	 *
	 * @author Meisam
	 *
	 */

	public static class Arrays{

		// creating an array of zero elements
		public static double[] setZero(int length){
			double[] x = new double[length] ;
			for(int i=0; i<length; i++){
				x[i] = 0 ;
			}
			return x ;
		}

		// creating an array of specific value
		public static double[] setValue(double value, int length){
			int n = length ;
			double[] x = new double[n] ;
			for(int i=0; i<n; i++){
				x[i] = value ;
			}
			return x ;
		}

		public static double[] replaceValues(double[] x, int indexArray[], double[] values){
			int n = indexArray.length ;
			for(int i=0; i<n; i++){
				x[indexArray[i]] = values[i] ;
			}
			return x ;
		}

		// summing two arrays (z = y + x)
		public static double[] plus(double[] y, double[] x){
			int n = x.length ;
			double[] z = new double[n] ;
			for(int i=0; i<n; i++){
				z[i] = x[i] + y[i] ;
			}
			return z ;
		}

		public static int[] plus(int[] y, int[] x){
			int n = x.length ;
			int[] z = new int[n] ;
			for(int i=0; i<n; i++){
				z[i] = x[i] + y[i] ;
			}
			return z ;
		}

		public static String[] plus(String[] y, String[] x){
			int n = x.length ;
			String[] z = new String[n] ;
			for(int i=0; i<n; i++){
				z[i] = Double.toString(Double.parseDouble(x[i]) + Double.parseDouble(y[i])) ;
			}
			return z ;
		}

		// adding a constant number to all the elements of an array (y = x + alpha)
		public static double[] plus(double[] x, double alpha){
			int n = x.length ;
			double[] y = new double[n] ;
			for(int i=0; i<n; i++){
				y[i] = x[i] + alpha ;
			}
			return y ;
		}

		public static double[] plus(double alpha, double[] x){
			int n = x.length ;
			double[] y = new double[n] ;
			for(int i=0; i<n; i++){
				y[i] = x[i] + alpha ;
			}
			return y ;
		}

		// subtracting two arrays (z = y - x)
		public static double[] minus(double[] y, double[] x){
			int n = y.length ;
			double[] z = new double[n] ;
			for(int i=0; i<n; i++){
				z[i] = y[i] - x[i] ;
			}
			return z ;
		}

		// multiplying an array by a number
		public static double[] times(double[] x, double alpha){
			int n = x.length ;
			double[] y = new double[n] ;
			for(int i=0; i<n; i++){
				y[i] = x[i] * alpha ;
			}
			return y ;
		}

		public static double[] times(double alpha, double[] x){
			int n = x.length ;
			double[] y = new double[n] ;
			for(int i=0; i<n; i++){
				y[i] = x[i] * alpha ;
			}
			return y ;
		}

		// multiplying element-wise of two arrays (z = x.*y)
		public static double[] times(double[] x, double[] y){
			int n = x.length ;
			double[] z = new double[n] ;
			for(int i=0; i<n; i++){
				z[i] = x[i]*y[i] ;
			}
			return z ;
		}

		// element-wise division of two arrays (z = y./x)
		public static double[] divides(double[] y, double[] x){
			int n = y.length ;
			double[] z = new double[n] ;
			for(int i=0; i<n; i++){
				z[i] = y[i]/x[i] ;
			}
			return z ;
		}

		public static int[] concat(int[] A, int[] B){
			int nA = A.length ;
			int nB = B.length ;
			int[] AB = new int[nA+nB] ;
			for(int i=0; i<nA; i++){
				AB[i] = A[i] ;
			}
			for(int i=0; i<nB; i++){
				AB[i+nA] = B[i] ;
			}
			return AB ;
		}

		public static double[] concat(double[] A, double[] B){
			int nA = A.length ;
			int nB = B.length ;
			double[] AB = new double[nA+nB] ;
			for(int i=0; i<nA; i++){
				AB[i] = A[i] ;
			}
			for(int i=0; i<nB; i++){
				AB[i+nA] = B[i] ;
			}
			return AB ;
		}

		public static String[] concat(String[] A, String[] B){
			int nA = A.length ;
			int nB = B.length ;
			String[] AB = new String[nA+nB] ;
			for(int i=0; i<nA; i++){
				AB[i] = A[i] ;
			}
			for(int i=0; i<nB; i++){
				AB[i+nA] = B[i] ;
			}
			return AB ;
		}

		// appending a new element to the end of array
		public static double[] append(double[] x, double x0){
			return MathUtils.Arrays.concat(x, new double[] {x0}) ;
		}

		public static int[] append(int[] x, int x0){
			return MathUtils.Arrays.concat(x, new int[] {x0}) ;
		}

		public static String[] append(String[] x, String x0){
			return MathUtils.Arrays.concat(x, new String[] {x0}) ;
		}

		public static double[] getReal(Complex[] u){
			double[] x = new double[u.length] ;
			for(int i=0; i<u.length; i++){
				x[i] = u[i].re() ;
			}
			return x ;
		}

		public static double[] getImag(Complex[] u){
			double[] x = new double[u.length] ;
			for(int i=0; i<u.length; i++){
				x[i] = u[i].im() ;
			}
			return x ;
		}

//		public static double[] getReal(ComplexMatrix[] u){
//			double[][] x = new double[][] ;
//			for(int i=0; i<u.length; i++){
//				x[i] = u[i].re() ;
//			}
//			return x ;
//		}

		// converting to string
		public static String toString(double[] x){
			String s = "[ " ;
			int n = x.length ;
			for(int i=0; i<n-1; i++){
				s += x[i] + ", " ;
			}
			s = s + x[n-1]+" ]" ;
			return s ;
		}
		
		public static String toString(Complex[] x){
			String s = "[ " ;
			int n = x.length ;
			for(int i=0; i<n-1; i++){
				s += x[i] + ", " ;
			}
			s = s + x[n-1]+" ]" ;
			return s ;
		}

		// printing the array on the screen
		public static void show(double[] x){
			System.out.println(toString(x));
		}
		
		public static void show(Complex[] x){
			System.out.println(toString(x));
		}

		// finding the minimum value of an array and its index
		public static class FindMinimum{
			public static double getValue(double[] x){
				ArrayMaths amath = new ArrayMaths(x) ;
				double minVal = amath.getMinimum() ;
				return minVal ;
			}

			public static int getIndex(double[] x){
				ArrayMaths amath = new ArrayMaths(x) ;
				int minIndex = amath.getMinimumIndex() ;
				return minIndex ;
			}
		}

		// finding the maximum value of an array and its index
		public static class FindMaximum{
			public static double getValue(double[] x){
				ArrayMaths amath = new ArrayMaths(x) ;
				double maxVal = amath.getMaximum() ;
				return maxVal ;
			}

			public static int getIndex(double[] x){
				ArrayMaths amath = new ArrayMaths(x) ;
				int maxIndex = amath.getMaximumIndex() ;
				return maxIndex ;
			}

		}

		// ********* Conversions on arrays
		public static class Conversions{
			// convert degree to radian
			public static class Angles{
				public static double[] toRadian(double[] degree){
					double[] x = new double[degree.length] ;
					for(int i=0; i<x.length; i++){
						x[i] = degree[i]*Math.PI/180 ;
					}
					return x;
				}

				public static double[] toDegree(double[] radian){
					double[] x = new double[radian.length] ;
					for(int i=0; i<x.length; i++){
						x[i] = radian[i]*180/Math.PI  ;
					}
					return x;
				}
			}
			// convert lenght units
			public static class Length{
				public static double[] meters_to_feet(double[] length_meters){
					double[] x = {} ;
					for(int i=0; i<length_meters.length; i++){
						double y = MathUtils.Conversions.Length.meters_to_feet(length_meters[i])  ;
						x = MathUtils.Arrays.append(x, y) ;
					}
					return x;
				}
				public static double[] feet_to_meters(double[] length_feet){
					double[] x = {} ;
					for(int i=0; i<length_feet.length; i++){
						double y = MathUtils.Conversions.Length.feet_to_meters(length_feet[i])  ;
						x = MathUtils.Arrays.append(x, y) ;
					}
					return x;
				}
			}
			// convert weight units
			public static class Weight{
				public static double[] kg_to_lb(double[] weight_kg){
					double[] x = {} ;
					for(int i=0; i<weight_kg.length; i++){
						double y = MathUtils.Conversions.Weight.kg_to_lb(weight_kg[i]) ;
						x = MathUtils.Arrays.append(x, y) ;
					}
					return x;
				}
				public static double[] lb_to_kg(double[] weight_lb){
					double[] x = {} ;
					for(int i=0; i<weight_lb.length; i++){
						double y = MathUtils.Conversions.Weight.lb_to_kg(weight_lb[i]) ;
						x = MathUtils.Arrays.append(x, y) ;
					}
					return x;
				}
			}

			// converting to dB
			public static double[] todB(double[] x){
				double[] t = {} ;
				for(int i=0; i<x.length; i++){
					double y = MathUtils.Conversions.todB(x[i])  ;
					t = MathUtils.Arrays.append(t, y) ;
				}
				return t;
			}

			// converting from dB
			public static double[] fromdB(double[] xdB){
				double[] t = {} ;
				for(int i=0; i<xdB.length; i++){
					double y = MathUtils.Conversions.fromdB(xdB[i])  ;
					t = MathUtils.Arrays.append(t, y) ;
				}
				return t;
			}

		}

		//************** Functions on Arrays *****************
		public static class Functions{

			public static double[] pow(double[] x, double p){
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = Math.pow(x[i], p) ;
				}
				return y ;
			}

			public static double[] exp(double[] x){
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = Math.exp(x[i]) ;
				}
				return y ;
			}

			public static double[] sin(double[] x){
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = Math.sin(x[i]) ;
				}
				return y ;
			}

			public static double[] cos(double[] x){
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = Math.cos(x[i]) ;
				}
				return y ;
			}

			public static double[] tan(double[] x){
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = Math.tan(x[i]) ;
				}
				return y ;
			}

			public static double[] cot(double[] x){
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = 1/Math.tan(x[i]) ;
				}
				return y ;
			}

			public static double[] asinh(double[] x) {
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = MathUtils.Functions.asinh(x[i]) ;
				}
				return y ;
				}

			public static double[] acosh(double[] x) {
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = MathUtils.Functions.acosh(x[i]) ;
				}
				return y ;
				}

			public static double[] atanh(double[] x) {
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = MathUtils.Functions.atanh(x[i]) ;
				}
				return y ;
				}

			public static double[] acoth(double[] x){
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = MathUtils.Functions.acoth(x[i]) ;
				}
				return y ;
			}

			public static double[] sinc(double[] x){
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					if(x[i]==0){
						y[i] = 1 ;
					}
					else{
						y[i] = Math.sin(Math.PI * x[i])/(Math.PI * x[i]) ;
					}
				}
				return y ;
			}

			public static double[] sinIntegral(double[] x){
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = MathUtils.Functions.sinIntegral(x[i]) ;
				}
				return y ;
			}

			public static double[] abs(double[] x){
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = Math.abs(x[i]) ;
				}
				return y ;
			}

			public static double[] absSquared(double[] x){
				int n = x.length ;
				double[] y = new double[n] ;
				for(int i=0; i<n; i++){
					y[i] = x[i]*x[i] ;
				}
				return y ;
			}


		}

	}

	// for test
	public static void main(String[] args) {
		System.out.println(Functions.factorial(10));
	}
}

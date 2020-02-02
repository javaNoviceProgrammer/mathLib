package mathLib.root;

import java.util.ArrayList;
import java.util.List;
import static mathLib.optimize.pso.ParticleSwarmOptimization.interval;

import mathLib.optimize.pso.ParticleSwarmOptimization;


public class RealRoot {

	RealRootFunction func ;
	RealRootDerivFunction derivFunc ;

	double absError = 1e-15 ;
	double relError = 1e-15 ;

	public RealRoot(RealRootFunction func) {
		this.func = func ;
		this.derivFunc = null ;
	}

	public RealRoot(RealRootDerivFunction derivFunc) {
		this.derivFunc = derivFunc ;
		this.func = null ;
	}

	// bi-section method
	public double bisection(double start, double end) {
		double root = Double.NaN ;
		double guess = 0.5*(start+end) ;
		if(Math.abs(func.value(start))<absError)
			return start ;
		else if(Math.abs(func.value(end))<absError)
			return end ;
		else if(Math.abs((end-start)/end)<relError || Math.abs((end-start)/start)<relError
				|| Math.abs(end-start)<absError){
			return guess ;
		}
		if(func.value(start)*func.value(guess)<=0){
			return bisection(start, guess) ;
		}
		else if(func.value(guess)*func.value(end)<=0) {
			return bisection(guess, end) ;
		}
		return root ;
	}

	// bi-section method with the initial guess --> method overloading technique
	public double bisection(double start, double end, double guess) {
		double root = Double.NaN ;
		if(Math.abs(func.value(start))<absError)
			return start ;
		else if(Math.abs(func.value(end))<absError)
			return end ;
		else if(Math.abs((end-start)/end)<relError || Math.abs((end-start)/start)<relError
				|| Math.abs(end-start)<absError){
			return 0.5*(start+end) ;
		}
		if(func.value(start)*func.value(guess)<=0){
			return bisection(start, guess) ;
		}
		else if(func.value(guess)*func.value(end)<=0) {
			return bisection(guess, end) ;
		}
		return root ;
	}

	public List<Double> bisection(double start, double end, int subIntervals) {
		List<Double> roots = new ArrayList<>() ;
		double[] points = new double[subIntervals+1] ;
		double dx = (end-start)/subIntervals ;
		for(int i=0; i<points.length; i++) {
			points[i] = start + i*dx ;
		}
		for(int i=0; i<subIntervals; i++) {
			double potentialRoot = bisection(points[i], points[i+1]) ;
			if(!Double.isNaN(potentialRoot) && !roots.contains(potentialRoot)){
				roots.add(potentialRoot) ;
			}
		}
		return roots ;
	}

	public double trisection(double start, double end) {
		double root = Double.NaN ;
		double delta = (end-start)/3.0 ;
		double guess1 = start + delta ;
		double guess2 = start + 2*delta ;
		if(Math.abs(func.value(start))<absError)
			return start ;
		else if(Math.abs(func.value(end))<absError)
			return end ;
		else if(Math.abs((end-start)/end)<relError || Math.abs((end-start)/start)<relError
				|| Math.abs(end-start)<absError){
			return 0.5*(start+end) ;
		}
		if(func.value(start)*func.value(guess1)<=0){
			return trisection(start, guess1) ;
		}
		else if(func.value(guess1)*func.value(guess2)<=0) {
			return trisection(guess1, guess2) ;
		}
		else if(func.value(guess2)*func.value(end)<=0) {
			return trisection(guess2, end) ;
		}
		return root ;
	}

	public List<Double> trisection(double start, double end, int subIntervals) {
		List<Double> roots = new ArrayList<>() ;
		double[] points = new double[subIntervals+1] ;
		double dx = (end-start)/subIntervals ;
		for(int i=0; i<points.length; i++) {
			points[i] = start + i*dx ;
		}
		for(int i=0; i<subIntervals; i++) {
			double potentialRoot = trisection(points[i], points[i+1]) ;
			if(!Double.isNaN(potentialRoot) && !roots.contains(potentialRoot)){
				roots.add(potentialRoot) ;
			}
		}
		return roots ;
	}

	public double secant(double start, double end) {
		double root = Double.NaN ;
		// check for the initial bracket
		if(func.value(start)*func.value(end)>0)
			return Double.NaN ;
//		 check for start and end points & stop condition
		if(Math.abs(func.value(start))<absError)
			return start ;
		else if(Math.abs(func.value(end))<absError)
			return end ;
		else if(Math.abs((end-start)/end)<relError || Math.abs((end-start)/start)<relError
				|| Math.abs(end-start)<absError){
			return 0.5*(start+end) ;
		}
		// implement secant method
		double x1 = start, y1 = func.value(x1) ;
		double x2 = end, y2 = func.value(x2) ;
		// coeff: the inverse of the slope of the secant line
		double coeff = (x2-x1)/(y2-y1) ;
		double x3 = x2 - y2*coeff ;
		// recursion
		// first look for the valid bracket
		if(func.value(x1)*func.value(x3)<=0)
			root = secant(x1, x3) ;
		else if(func.value(x2)*func.value(x3)<=0)
			root = secant(x2, x3) ;
		return root ;
	}

	public List<Double> secant(double start, double end, int subIntervals) {
		List<Double> roots = new ArrayList<>() ;
		double[] points = new double[subIntervals+1] ;
		double dx = (end-start)/subIntervals ;
		for(int i=0; i<points.length; i++) {
			points[i] = start + i*dx ; // inclusive of start and end points
		}
		// run the bisection on each sub-interval
		for(int i=0; i<subIntervals; i++) {
			double potentialRoot = secant(points[i], points[i+1]) ;
			// test for NaN and duplicates
//			if(!Double.isNaN(potentialRoot) && start <= potentialRoot && potentialRoot <= end){
			if(!Double.isNaN(potentialRoot)){
				boolean repeated = false ;
				for(int j=0;j<roots.size(); j++){
					if(Math.abs(potentialRoot-roots.get(j))<1e-7)
						repeated = true ;
				}
				if(!repeated)
					roots.add(potentialRoot) ;
			}
		}
		return roots ;
	}

	public double brent(double start, double end, double guess) {
		double root = Double.NaN ;
		// check for start and end points & stop condition
		if(Math.abs(func.value(start))<absError)
			return start ;
		else if(Math.abs(func.value(end))<absError)
			return end ;
		else if(Math.abs((end-start)/end)<relError || Math.abs((end-start)/start)<relError
				|| Math.abs(end-start)<absError){
			return 0.5*(start+end) ;
		}
		// implement brent's method
		double x1 = start, y1 = func.value(x1) ;
		double x2 = end, y2 = func.value(x2) ;
		double x3 = guess, y3 = func.value(guess) ;
		double coeff1 = (y2*y3)/((y1-y2)*(y1-y3)) ;
		double coeff2 = (y1*y3)/((y2-y1)*(y2-y3)) ;
		double coeff3 = (y1*y2)/((y3-y1)*(y3-y2)) ;
		// crossing point with the horizontal axis y = 0
		double x4 = coeff1*x1 + coeff2*x2 + coeff3*x3 ;
		// recursion
		root = brent(x2, x3, x4) ;
		return root ;
	}

	// using --> method overloading
	public double brent(double start, double end) {
		double root = Double.NaN ;
		double guess = 0.5*(start+end) ;
		// check for start and end points & stop condition
		if(Math.abs(func.value(start))<absError)
			return start ;
		else if(Math.abs(func.value(end))<absError)
			return end ;
		else if(Math.abs((end-start)/end)<relError || Math.abs((end-start)/start)<relError
				|| Math.abs(end-start)<absError){
			return 0.5*(start+end) ;
		}
		// implement brent's method
		double x1 = start, y1 = func.value(x1) ;
		double x2 = end, y2 = func.value(x2) ;
		double x3 = guess, y3 = func.value(guess) ;
		double coeff1 = (y2*y3)/((y1-y2)*(y1-y3)) ;
		double coeff2 = (y1*y3)/((y2-y1)*(y2-y3)) ;
		double coeff3 = (y1*y2)/((y3-y1)*(y3-y2)) ;
		// crossing point with the horizontal axis y = 0
		double x4 = coeff1*x1 + coeff2*x2 + coeff3*x3 ;
		// recursion
		root = brent(x2, x3, x4) ;
		return root ;
	}

	public List<Double> brent(double start, double end, int subIntervals) {
		List<Double> roots = new ArrayList<>() ;
		double[] points = new double[subIntervals+1] ;
		double dx = (end-start)/subIntervals ;
		for(int i=0; i<points.length; i++) {
			points[i] = start + i*dx ; // inclusive of start and end points
		}
		// run the bisection on each sub-interval
		for(int i=0; i<subIntervals; i++) {
			double potentialRoot = brent(points[i], points[i+1]) ;
			// test for NaN and duplicates
			if(!Double.isNaN(potentialRoot) && start <= potentialRoot && potentialRoot <= end){
				boolean repeated = false ;
				for(int j=0;j<roots.size(); j++){
					if(Math.abs(potentialRoot-roots.get(j))<1e-7)
						repeated = true ;
				}
				if(!repeated)
					roots.add(potentialRoot) ;
			}
		}
		return roots ;
	}

	public double newton(double guess, int maxIterations) {
		double root = Double.NaN ;
		// stop criteria
		if(maxIterations==0) {
			System.out.println("Max iterations reached") ;
			return Double.NaN ;
		}
		if(derivFunc == null) {
			// find the derivative
			double h = 1e-2 ;
			double x1 = guess ;
			double y1 = func.value(guess) ;
			double coeff = (func.value(x1+h)-func.value(x1-h))/(2.0*h) ; // center expression ~ O(h^2)
			double x2 = x1 - y1/coeff ;
			// set the convergence criteria based on relative error
			double relErr1 = Math.abs((x2-guess)/guess) ;
			double relErr2 = Math.abs((x2-guess)/x2) ;
			double absErr = Math.abs(y1) ;
			if(relErr1<=relError || relErr2<=relError || absErr<=absError)
				return guess ;
			// recursion
			root = newton(x2, maxIterations-1) ; // being trapped
		}
		else if(func == null) {
			double x1 = guess ;
			double[] vals = derivFunc.values(x1) ;
			double y1 = vals[0] ;
			double coeff = vals[1] ;
			double x2 = x1 - y1/coeff ;
			// set the convergence criteria based on relative error
			double relErr1 = Math.abs((x2-guess)/guess) ;
			double relErr2 = Math.abs((x2-guess)/x2) ;
			double absErr = Math.abs(y1) ;
			if(relErr1<=relError || relErr2<=relError || absErr<=absError)
				return guess ;
			// recursion
			root = newton(x2, maxIterations-1) ; // being trapped
		}
		return root ;
	}

	public List<Double> newton(double start, double end, int subIntervals, int maxIterations) {
		List<Double> roots = new ArrayList<>() ;
		double[] points = new double[subIntervals+1] ;
		double dx = (end-start)/subIntervals ;
		for(int i=0; i<points.length; i++) {
			points[i] = start + i*dx ;
		}
		// run the bisection on each sub-interval
		for(int i=0; i<subIntervals; i++) {
			double guess = 0.5*(points[i]+points[i+1]) ;
			double potentialRoot = newton(guess, maxIterations) ;
			// test for NaN and duplicates
			if(!Double.isNaN(potentialRoot) && start <= potentialRoot && potentialRoot <= end){
				boolean repeated = false ;
				for(int j=0;j<roots.size(); j++){
					if(Math.abs(potentialRoot-roots.get(j))<1e-7)
						repeated = true ;
				}
				if(!repeated)
					roots.add(potentialRoot) ;
			}
		}
		return roots ;
	}

	// PSO method for finding roots is not recursive
	public double pso(double start, double end) {
		double root = Double.NaN ;
		if(Math.abs(func.value(start))<absError)
			return start ;
		else if(Math.abs(func.value(end))<absError)
			return end ;
		// create pso (1-D), remember: fitness function = abs(root func)
		ParticleSwarmOptimization pso = new ParticleSwarmOptimization(10, t -> Math.abs(func.value(t[0])), interval(start, end)) ;
		// set the goal of pso
		pso.setMinimize(true) ;
		// iterate
		pso.solve(100) ;
		// get the min candidate (position)
		root = pso.bestPosition().at(0) ;
		// test the min
		if(Math.abs(func.value(root))>1e-9 || root<start || root>end)
			return Double.NaN ;
		return root ;
	}

	public List<Double> pso(double start, double end, int subIntervals) {
		List<Double> roots = new ArrayList<>() ;
		double[] points = new double[subIntervals+1] ;
		double dx = (end-start)/subIntervals ;
		for(int i=0; i<points.length; i++) {
			points[i] = start + i*dx ;
		}
		for(int i=0; i<subIntervals; i++) {
			double potentialRoot = pso(points[i], points[i+1]) ;
			if(!Double.isNaN(potentialRoot) && points[i] <= potentialRoot && potentialRoot <= points[i+1]){
				boolean repeated = false ;
				for(int j=0;j<roots.size(); j++){
					if(Math.abs(potentialRoot-roots.get(j))<1e-7)
						repeated = true ;
				}
				if(!repeated)
					roots.add(potentialRoot) ;
			}
		}
		return roots ;
	}


}

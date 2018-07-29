package mathLib.root;

import java.util.ArrayList;

import flanagan.roots.RealRoot;
import flanagan.roots.RealRootFunction;
import mathLib.utils.MathUtils;

public class RealRootFinder {
	
	RealRootFunction func;
	RealRootFunction funcNormalized ;
	double a, b  ;
	double stepSize ;
	double accuracy ;
	ArrayList<Double> roots ;
	
	public RealRootFinder(
			final RealRootFunction func_,
			double xStart,
			double xEnd,
			double accuracy
			){
		this.func = func_ ;
		this.a = xStart ;
		this.b = xEnd ;
		this.accuracy = accuracy ;
		this.stepSize = accuracy/(b-a) ; 
		roots = new ArrayList<Double>() ;
		funcNormalized = new RealRootFunction() {	
			@Override
			public double function(double y) {
				return func_.function(getX(y));
			}
		};
	}
	
	public RealRootFinder(
			final RealRootFunction func_,
			double xStart,
			double xEnd
			){
		this.func = func_ ;
		this.a = xStart ;
		this.b = xEnd ;
		this.accuracy = 1e-2 ;
		this.stepSize = 1e-2/(b-a) ; // default accuracy = 0.01
		roots = new ArrayList<Double>() ;
		funcNormalized = new RealRootFunction() {	
			@Override
			public double function(double y) {
				return func_.function(getX(y));
			}
		};
	}
	
	public RealRootFinder(){
		funcNormalized = new RealRootFunction() {	
			@Override
			public double function(double y) {
				return func.function(getX(y));
			}
		};
	}
	
	// setters
	public void setAccuracy(double accuracy){
		this.accuracy = accuracy ;
		stepSize = accuracy/(b-a) ;
	}
	
	public void setStartPoint(double a){
		this.a = a ;
	}
	
	public void setEndPoint(double b){
		this.b = b ;
	}
	
	public void setRootFunction(final RealRootFunction func_){
		this.func = func_ ;
		funcNormalized = new RealRootFunction() {	
			@Override
			public double function(double y) {
				return func_.function(getX(y));
			}
		};
	}
	
	// getters
	public double getStepY(){
		return stepSize ;
	}
	
	public double getStepX(){
		return (b-a)*stepSize ;
	}
	
	// y = (x-a)/(b-a) ;
	private double getY(double x){
		return (x-a)/(b-a) ;
	}
	
	// x = (b-a)*y + a
	private double getX(double y){
		return (b-a)*y+a ;
	}
	
	// 0<y<1 --> linear mapping to a<x<b
	
	private void findRootOverInterval(double yStart, double yEnd){
		RealRoot rootSolver = new RealRoot() ;
		double estimate = (funcNormalized.function(yStart)+funcNormalized.function(yEnd))/2 ;
		rootSolver.setEstimate(estimate);
		
		rootSolver.suppressLimitReachedMessage();
		rootSolver.suppressNaNmessage();
		
//		double r = rootSolver.bisect(funcNormalized, yStart, yEnd) ;
		double r = rootSolver.brent(funcNormalized, yStart, yEnd) ;
		if(!Double.isNaN(r) && Double.isFinite(func.function(getX(r))) && isContinuousAt(r) && Math.abs(funcNormalized.function(r))<accuracy){
			roots.add(getX(r)) ;
		}
	}
	
	private double getRootOverInterval(double yStart, double yEnd){
		RealRoot rootSolver = new RealRoot() ;
		double estimate = funcNormalized.function(yStart) ;
		rootSolver.setEstimate(estimate);
		
		rootSolver.suppressLimitReachedMessage();
		rootSolver.suppressNaNmessage();
		
//		double r = rootSolver.bisect(funcNormalized, yStart, yEnd) ;
		double r = rootSolver.brent(funcNormalized, yStart, yEnd) ;
		if(!Double.isNaN(r) && Double.isFinite(func.function(getX(r))) && isContinuousAt(r) && Math.abs(funcNormalized.function(r))<accuracy){
			return r ;
		}
		else{
			return Double.NaN ;
		}
	}
	
	public void findAllRoots(){
		double[] points = MathUtils.linspace(0, 1, stepSize) ;
		int M = points.length ;
		for(int i=0; i<M-1; i++){
			if(funcNormalized.function(points[i])*funcNormalized.function(points[i+1]) < 0){
				findRootOverInterval(points[i], points[i+1]);
			}
		}
//		applyAccuracyRule();
	}

	public double getFirstRoot_greater_than_or_equal_to(double x){
		double y = getY(x) ; // mapping to (0,1) --> y < 1
		double yStart = y ;
		double yEnd = yStart + stepSize ;
		double root = Double.NaN ;
		while(Double.isNaN(root)){
			yStart += stepSize ;
			yEnd += stepSize ;
			root = getRootOverInterval(yStart, yEnd) ;
		}
		return getX(root) ;
	}
	
	public double getFirstRoot_smaller_than_or_equal_to(double x){
		double y = getY(x) ; // mapping to (0,1) --> y > 0
		double yEnd = y - stepSize ;
		double yStart = yEnd - stepSize ;
		double root = Double.NaN ;
		while(Double.isNaN(root)){
			yStart -= stepSize ;
			yEnd -= stepSize ;
			root = getRootOverInterval(yStart, yEnd) ;
		}
		return getX(root) ;
	}
	
	public double getSpecificRoot(int rootNumber){
		// first partitioning the [0,1] interval
		double[] points = MathUtils.linspace(0, 1, stepSize) ;
		int M = points.length ;
		int k = 0 ;
		double yStart = 0, yEnd = 0 ;
		for(int i=0; i<M-1; i++){
			if(funcNormalized.function(points[i])*funcNormalized.function(points[i+1]) < 0 && rootNumber > k){
				k++ ;
				yStart = points[i] ;
				yEnd = points[i+1] ;
			}
		}

		RealRoot rootSolver = new RealRoot() ;
		double estimate = (funcNormalized.function(yStart)+funcNormalized.function(yEnd))/2 ;
		rootSolver.setEstimate(estimate);
		double r = rootSolver.brent(funcNormalized, yStart, yEnd) ;
		return getX(r) ;
	}
	
	public double getSpecificRoot_from_end(int rootNumber){
		// first partitioning the [0,1] interval
		double[] points = MathUtils.linspace(0, 1, stepSize) ;
		int M = points.length ;
		int k = 0 ;
		double yStart = 0, yEnd = 0 ;
		for(int i=M-1; i>0; i--){
			if(funcNormalized.function(points[i])*funcNormalized.function(points[i-1]) < 0 && rootNumber > k){
				k++ ;
				yStart = points[i-1] ;
				yEnd = points[i] ;
			}
		}
		
		RealRoot rootSolver = new RealRoot() ;
		double estimate = (funcNormalized.function(yStart)+funcNormalized.function(yEnd))/2 ;
		rootSolver.setEstimate(estimate);
		double r = rootSolver.brent(funcNormalized, yStart, yEnd) ;
		return getX(r) ;
	}
	
	public double[] getAllRoots(){
		int M = roots.size() ;
		double[] r = new double[M] ;
		for(int i=0; i<M; i++){
			r[i] = roots.get(i) ;
		}
		return r ;
	}
	
	public void showAllRoots(){
		MathUtils.Arrays.show(getAllRoots());
	}
	
	public String toString(){
		return MathUtils.Arrays.toString(getAllRoots()) ;
	}
	
	public int getNumberOfRoots(){
		return roots.size() ;
	}
	
	@SuppressWarnings("unused")
	private ArrayList<Double> removeNaNs(ArrayList<Double> list){
		ArrayList<Double> list_copy = new ArrayList<Double>() ;
		for(double x : list){
			if(!Double.isNaN(x)){
				list_copy.add(x) ;
			}
		}
		return list_copy ;
	}
	
	@SuppressWarnings("unused")
	private void applyAccuracyRule(){
		for(int i=0; i<roots.size()-1; i++){
			double x = roots.get(i) ;
			double y = roots.get(i+1) ;
			if(y-x < accuracy){
				roots.remove(y) ;
				applyAccuracyRule();
			}
		}
	}
	
	private boolean isContinuousAt(double x){
		double dx = 1e-2 ;
		double epsilon = 1e-2 ;
		double x_right, x_left ;
		boolean isContinuous = true ;
		int counter = 0 ;
		while(counter<10){
			x_right = x + dx ;
			x_left = x - dx ;
			if(Math.abs(funcNormalized.function(x_right)-funcNormalized.function(x_left))>epsilon){
				isContinuous = false ;
				dx = dx/10 ;
				counter++ ;
			}
			else{
				isContinuous = true ;
				break ;
			}
		}
		return isContinuous ;
	}
	
	//******************** for test ******************
	public static void main(String[] args){
		RealRootFunction func = new RealRootFunction(){
			@Override
			public double function(double x) {
				return Math.sin(x) ;
			}
		} ;
		
		double xStart = Math.PI*(-6) ;
		double xEnd = Math.PI*6 ;
		
		double[] x = MathUtils.linspace(xStart, xEnd, 1000) ;
		double[] y = new double[x.length] ;
		for(int i=0; i<x.length; i++){
			y[i] = func.function(x[i]) ;
		}
		
		RealRootFinder rootFinder = new RealRootFinder(func, xStart, xEnd) ;
		rootFinder.findAllRoots();
		rootFinder.showAllRoots();
		System.out.println(rootFinder.getFirstRoot_greater_than_or_equal_to(-2));
		System.out.println(rootFinder.getFirstRoot_smaller_than_or_equal_to(-7));
	}
	
	
	

}

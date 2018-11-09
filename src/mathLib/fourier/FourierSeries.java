package mathLib.fourier;

import static mathLib.numbers.Complex.j;

import java.util.HashMap;
import java.util.Map;

import edu.uta.futureye.function.intf.MathFunc;
import mathLib.func.ArrayFunc;
import mathLib.func.intf.RealFunction;
import mathLib.integral.Integral1D;
import mathLib.integral.intf.IntegralFunction1D;
import mathLib.numbers.Complex;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;
import mathLib.util.Timer;

public class FourierSeries {
	
	IntegralFunction1D func ;
	double start, end, period ;
	
	// caching vastly improves the speed in certain cases
	Map<Integer, Double> cacheAn = new HashMap<>() ;
	Map<Integer, Double> cacheBn = new HashMap<>() ;
	
	public FourierSeries(RealFunction func, double periodStart, double periodEnd) {
		this.start = periodStart ;
		this.end = periodEnd ;
		this.period = end-start ;
		this.func = x -> func.evaluate(x*period) ;
	}
	
	public FourierSeries(MathFunc func, double periodStart, double periodEnd) {
		this.start = periodStart ;
		this.end = periodEnd ;
		this.period = end-start ;
		this.func = x -> func.apply(x*period) ;
	}
	
	public FourierSeries(String func, double periodStart, double periodEnd) {
		this.start = periodStart ;
		this.end = periodEnd ;
		this.period = end-start ;
		this.func = new IntegralFunction1D() {
			@Override
			public double function(double arg0) {
				Map<String, Double> map = new HashMap<String, Double>() ;
				map.put("x", arg0*period) ;
				return MathUtils.evaluate(func, map);
			}
		} ;
	}
	
	public double An(int n) {
		if(!cacheAn.isEmpty() && cacheAn.containsKey(n))
			return cacheAn.get(n) ;
		IntegralFunction1D funcFourier = null ;
		if(n==0) {
			funcFourier = func ;
			double an = (new Integral1D(funcFourier, start/period, end/period)).getIntegral() ;
			cacheAn.put(n, an) ;
			return an ;
		}
		if(n<0) {
			cacheAn.put(n, An(-n)) ;
			return An(-n) ;
		}
		
		funcFourier = x -> 2*func.function(x)*Math.cos(2*Math.PI*n*x) ;
		double an = (new Integral1D(funcFourier, start/period, end/period)).getIntegral() ;
		cacheAn.put(n, an) ;
		return an ;	
	}
	
	public double Bn(int n) {
		if(!cacheBn.isEmpty() && cacheBn.containsKey(n))
			return cacheBn.get(n) ;
		IntegralFunction1D funcFourier = null ;
		if(n==0) {
			cacheBn.put(n, 0.0) ;
			return 0.0 ;
		}
		if(n<0) {
			cacheBn.put(n, -Bn(-n)) ;
			return -Bn(-n) ;
		}

		funcFourier = x -> 2*func.function(x)*Math.sin(2*Math.PI*n*x) ;
		double bn = (new Integral1D(funcFourier, start/period, end/period)).getIntegral() ;
		cacheBn.put(n, bn) ;
		return bn ;	
	}
	
	public Complex Cn(int n) {
		if(n==0)
			return An(0)-j*0 ;
		
		return (An(n)-j*Bn(n))/2.0 ;
	}
	
	public RealFunction getTerm(int n) {
		
		RealFunction func = new RealFunction() {	
			@Override
			public double evaluate(double t) {
				return An(n)*Math.cos(2*Math.PI/period*n*t) + Bn(n)*Math.sin(2*Math.PI/period*n*t);
			}
		};
		
		return func ;
	}
	
	public RealFunction getSeries(int n) {
		
		RealFunction func = new RealFunction() {	
			@Override
			public double evaluate(double t) {
				double sum = 0 ;
				for(int i=0; i<=n; i++)
					sum += An(i)*Math.cos(2*Math.PI/period*i*t) + Bn(i)*Math.sin(2*Math.PI/period*i*t);
				return sum ;
			}
		};
		
		return func ;
	}
	
	
	// for test
	public static void main(String[] args) {
		double start = -1.0 ;
		double end = 1.0 ;
		
		RealFunction func = new RealFunction() {
			@Override
			public double evaluate(double x) {
				if(x>= -0.5 && x <= 0.5)
					return x*x ;
				else
					return 0.0 ;
			}
		};
		
		FourierSeries series = new FourierSeries(func, start, end) ;
	
		System.out.println(series.Cn(0));
		System.out.println(series.Cn(1));
		System.out.println(series.Cn(2));
		System.out.println(series.Cn(3));
		System.out.println(series.Cn(4));
		System.out.println(series.Cn(5));
		System.out.println(series.Cn(6));
		System.out.println(series.Cn(7));
		System.out.println(series.Cn(8));
		System.out.println(series.Cn(9));
		
		Timer timer = new Timer() ;
		timer.start();
		
		double[] x = MathUtils.linspace(start, end, 1000) ;
		double[] y1 = ArrayFunc.apply(series.getSeries(200), x) ;
		double[] y2 = ArrayFunc.apply(func, x) ;
		
		timer.stop(); 
		System.out.println(timer);
		
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y2, "r", 2f, "Exact");
		fig.plot(x, y1, "b", 2f, "Series");
		fig.renderPlot();
		fig.run(true);
		
	}
	

}

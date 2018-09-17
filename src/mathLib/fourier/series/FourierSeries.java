package mathLib.fourier.series;

import static mathLib.numbers.Complex.j;

import java.util.HashMap;
import java.util.Map;

import edu.uta.futureye.function.intf.MathFunc;
import mathLib.func.ArrayFunc;
import mathLib.func.intf.RealFunction;
import mathLib.integral.Integral1D;
import mathLib.integral.intf.IntegralFunction1D;
import mathLib.numbers.Complex;
import mathLib.util.MathUtils;
import plotter.chart.MatlabChart;

public class FourierSeries {
	
	IntegralFunction1D func ;
	double period ;
	
	public FourierSeries(RealFunction func, double period) {
		this.func = x -> func.evaluate(x*period) ;
		this.period = period ;
	}
	
	public FourierSeries(MathFunc func, double period) {
		this.func = x -> func.apply(x*period) ;
		this.period = period ;
	}
	
	public FourierSeries(String func, double period) {

		this.func = new IntegralFunction1D() {
			@Override
			public double function(double arg0) {
				Map<String, Double> map = new HashMap<String, Double>() ;
				map.put("x", arg0*period) ;
				return MathUtils.evaluate(func, map);
			}
		} ;
		
		this.period = period ;
	}
	
	public double An(int n) {
		IntegralFunction1D funcFourier = null ;
		if(n==0) {
			funcFourier = func ;
			return (new Integral1D(funcFourier, 0.0, 1.0)).getIntegral()*2 ;
		}
		if(n<0)
			return An(-n) ;
			
		funcFourier = x -> 2*func.function(x)*Math.cos(2*Math.PI*n*x) ;
		return (new Integral1D(funcFourier, 0.0, 1.0)).getIntegral() ;	
	}
	
	public double Bn(int n) {
		IntegralFunction1D funcFourier = null ;
		if(n==0) {
			return 0.0 ;
		}
		if(n<0)
			return -Bn(-n) ;
			
		funcFourier = x -> 2*func.function(x)*Math.sin(2*Math.PI*n*x) ;
		return (new Integral1D(funcFourier, 0.0, 1.0)).getIntegral() ;	
	}
	
	public Complex Cn(int n) {
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
					sum += An(n)*Math.cos(2*Math.PI/period*n*t) + Bn(n)*Math.sin(2*Math.PI/period*n*t);
				return sum ;
			}
		};
		
		return func ;
	}
	
	
	// for test
	public static void main(String[] args) {
		RealFunction func = x -> Math.cos(x*x) ;
		FourierSeries series = new FourierSeries(func, 2*Math.PI) ;
		
//		System.out.println(series.An(0));
//		System.out.println(series.An(1));
//		System.out.println(series.An(2));
//		System.out.println(series.An(3));
//		
//		System.out.println(series.Bn(0));
//		System.out.println(series.Bn(1));
//		System.out.println(series.Bn(2));
//		System.out.println(series.Bn(3));
		
		System.out.println(series.Cn(0));
		System.out.println(series.Cn(1));
		System.out.println(series.Cn(2));
		System.out.println(series.Cn(3));
		System.out.println(series.Cn(-1));
		System.out.println(series.Cn(-2));
		System.out.println(series.Cn(-3));
		
		double[] x = MathUtils.linspace(-Math.PI, Math.PI, 100) ;
		double[] y1 = ArrayFunc.apply(series.getTerm(0), x) ;
		double[] y2 = ArrayFunc.apply(series.getTerm(1), x) ;
		double[] y3 = ArrayFunc.apply(series.getTerm(2), x) ;
		double[] y4 = ArrayFunc.apply(series.getTerm(3), x) ;
		
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y1, "b");
		fig.plot(x, y2, "r");
		fig.plot(x, y3, "g");
		fig.plot(x, y4, "k");
		fig.RenderPlot();
		fig.run(true);
	}
	

}

package mathLib.fitting.interpol;

import flanagan.interpolation.LinearInterpolation;
import mathLib.func.ArrayFunc;
import mathLib.func.intf.RealFunction;
import mathLib.func.richardson.Richardson;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class LinearInterpolation1D extends LinearInterpolation {
	
	private RealFunction func ;
	private RealFunction deriv ;
	private RealFunction deriv2 ;

	public LinearInterpolation1D(double[] var1, double[] var2) {
		super(var1, var2);
		func = t -> this.interpolate(t) ;
		deriv = t -> Richardson.deriv(func, t, 2) ;
		deriv2 = t -> Richardson.deriv2(func, t, 2) ;
	}
	
	public double value(double x) {
		return func.evaluate(x) ;
	}
	
	public RealFunction function() {
		return func ;
	}
	
	public RealFunction derivFunc() {
		return deriv ;
	}
	
	public double deriv2Value(double x) {
		return deriv2.evaluate(x) ;
	}
	
	public RealFunction deriv2Func() {
		return deriv2 ;
	}
	
	public double derivValue(double x) {
		return deriv.evaluate(x) ;
	}
	
	// for test
	public static void main(String[] args) {
		double[] x = MathUtils.linspace(0, 2*Math.PI, 50) ;
		double[] y1 = ArrayFunc.apply(t -> Math.sin(t*t), x) ;
		double[] y1deriv = ArrayFunc.apply(t -> Math.cos(t*t)*2*t, x) ;
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y1deriv);
		// interpolate
		LinearInterpolation1D interpolate = new LinearInterpolation1D(x, y1) ;
		double[] y2deriv = ArrayFunc.apply(interpolate.derivFunc(), x) ;
		fig.plot(x, y2deriv, "r");
		fig.renderPlot();
		fig.markerON();
		fig.run(true);
	}

}

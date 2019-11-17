package mathLib.integral.methods;

import flanagan.integration.IntegralFunction;
import mathLib.integral.Integral1D;
import mathLib.integral.intf.IntegralFunction1D;
import mathLib.sequence.Sequence;
import mathLib.sequence.Series;


public class GaussLegendreQuadrature {

	// 20 points

	double[] points = { -0.9931285991850949247861, -0.9639719272779137912677, -0.9122344282513259058678,
			-0.8391169718222188233945, -0.7463319064601507926143, -0.6360536807265150254528, -0.5108670019508270980044,
			-0.3737060887154195606726, -0.2277858511416450780805, -0.07652652113349733375464, 0.0765265211334973337546,
			0.2277858511416450780805, 0.3737060887154195606726, 0.5108670019508270980044, 0.6360536807265150254528,
			0.7463319064601507926143, 0.8391169718222188233945, 0.9122344282513259058678, 0.9639719272779137912677,
			0.9931285991850949247861 };

	double[] weights = { 0.0176140071391521183119, 0.04060142980038694133104, 0.0626720483341090635695,
			0.0832767415767047487248, 0.1019301198172404350368, 0.1181945319615184173124, 0.1316886384491766268985,
			0.1420961093183820513293, 0.1491729864726037467878, 0.1527533871307258506981, 0.152753387130725850698,
			0.149172986472603746788, 0.142096109318382051329, 0.1316886384491766268985, 0.1181945319615184173124,
			0.101930119817240435037, 0.083276741576704748725, 0.0626720483341090635695, 0.040601429800386941331,
			0.0176140071391521183119 };


	// 5 points

//	double[] points = {} ;
//	double[] weights = {} ;

	IntegralFunction func;
	double start, end;

	public GaussLegendreQuadrature(IntegralFunction func, double xStart, double xEnd) {
		this.func = func;
		this.start = xStart;
		this.end = xEnd;
	}

	public double getIntegral() {
		Sequence sFunc = k -> weights[(int) k] * func.function(getTransform(points[(int) k]));
		Series integral = new Series(sFunc);
		return integral.sum(0, points.length - 1) * (end - start) / 2.0;
	}

	private double getTransform(double u) {
		return (end - start) / 2.0 * u + (end + start) / 2.0;
	}

	// for test
	public static void main(String[] args) {
		IntegralFunction1D func1d = x -> x * Math.sin(x);
		Integral1D integral1d = new Integral1D(func1d, 0, 10);
		System.out.println(integral1d.getIntegral());

		GaussLegendreQuadrature glQuad = new GaussLegendreQuadrature(func1d, 0, 10);
		System.out.println(glQuad.getIntegral());
	}

}

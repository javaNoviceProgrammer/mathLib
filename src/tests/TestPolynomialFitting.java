package tests;

import mathLib.fitting.poly.PolynomialFittingLMSE;
import mathLib.plot.MatlabChart;

public class TestPolynomialFitting {

	public static void main(String[] args) {
		double[] x = { 1.228e3, 1.274e3, 1.32e3, 1.366e3, 1.412e3, 1.458e3, 1.504e3, 1.55e3, 1.596e3, 1.642e3, 1.688e3,
				1.734e3, 1.78e3, 1.826e3, 1.872e3 };
		double[] ne = { 2.223511, 2.221478, 2.219559, 2.217736, 2.215992, 2.214314, 2.212690, 2.211111, 2.209568,
				2.208054, 2.206562, 2.205088, 2.203626, 2.202173, 2.200725 };
//		double[] no = { 2.148157, 2.146414, 2.144772, 2.143212, 2.141722, 2.140290, 2.138905, 2.137560, 2.136246,
//				2.134959, 2.133691, 2.132440, 2.131200, 2.129968, 2.128742 };
		
		PolynomialFittingLMSE polyFit = new PolynomialFittingLMSE(1) ;
		polyFit.setData(x, ne);
		polyFit.fit();
		System.out.println(polyFit);
		
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, ne, "b");
		
		double[] neFit = new double[ne.length] ;
//		double[] noFit = new double[no.length] ;
		for(int i=0; i<ne.length; i++) {
			neFit[i] = polyFit.interpolate(x[i]) ;
		}
		
		fig.plot(x, neFit, "r");
		fig.renderPlot();
		fig.markerON();
		fig.run(true);
		
	}

}

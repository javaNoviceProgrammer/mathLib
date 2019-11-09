package mathLib.root;

import java.util.ArrayList;
import java.util.List;
import static mathLib.optimize.pso.ParticleSwarmOptimization.interval;
import flanagan.roots.RealRootFunction;
import mathLib.func.ArrayFunc;
import mathLib.optimize.pso.ParticleSwarmOptimization;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class RealRootPSO {

	public static double rootPSO(RealRootFunction func, double start, double end) {
		double root = Double.NaN ;
		RealRootFunction absFunc = t -> Math.abs(func.function(t)) ;
		ParticleSwarmOptimization pso = new ParticleSwarmOptimization(10, t -> absFunc.function(t[0]), interval(start, end)) ;
		pso.setMinimize(true) ;
		pso.solve(100) ;
		root = pso.bestPosition().at(0) ;
		if(Math.abs(func.function(root))>1e-9 || root>end || root<start) {
			return Double.NaN ;
		}
		return root ;
	}

	public static List<Double> rootPSO(RealRootFunction func, double start, double end, int subIntervals) {
		List<Double> roots = new ArrayList<>() ;
		double[] points = new double[subIntervals+1] ;
		double dx = (end-start)/subIntervals ;
		for(int i=0; i<points.length; i++) {
			points[i] = start + i*dx ;
		}
		for(int i=0; i<subIntervals; i++) {
			double potentialRoot = rootPSO(func, points[i], points[i+1]) ;
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

	private static void test1() {
		RealRootFunction func = t -> Math.tan(t);
//		double root = rootPSO(func, -5, 5) ;
		List<Double> root = rootPSO(func, -5.0, 5.0, 100) ;
		System.out.println(root);

		MatlabChart fig = new MatlabChart() ;
		double[] x = MathUtils.linspace(-5, 5, 1000) ;
		double[] y = ArrayFunc.apply(t -> func.function(t), x) ;
		fig.plot(x, y);
		fig.renderPlot();
		fig.font(13);
		fig.show(true);
	}

	public static void main(String[] args) {
		test1() ;
	}

}

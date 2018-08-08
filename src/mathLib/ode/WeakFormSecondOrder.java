package mathLib.ode;

import java.util.HashMap;
import java.util.Map;

import edu.uta.futureye.algebra.intf.Matrix;
import edu.uta.futureye.algebra.intf.Vector;
import edu.uta.futureye.algebra.solver.Solver;
import edu.uta.futureye.core.Mesh;
import edu.uta.futureye.core.NodeType;
import edu.uta.futureye.core.intf.FiniteElement;
import edu.uta.futureye.function.SingleVarFunc;
import edu.uta.futureye.function.intf.MathFunc;
import edu.uta.futureye.lib.assembler.Assembler;
import edu.uta.futureye.lib.element.FELinearLine1D;
import edu.uta.futureye.lib.weakform.WeakForm;
import edu.uta.futureye.util.MeshGenerator;
import edu.uta.futureye.util.Utils;
import edu.uta.futureye.util.container.NodeList;
import flanagan.interpolation.LinearInterpolation;
import mathLib.util.Timer;
import plotter.chart.MatlabChart;

public class WeakFormSecondOrder {

	// diff equation: a*y'' + b*y' + c*y = f , over (x0, x1) , Boundary value:  y(x0) = y0 and y(x1) = y1

	Timer timer ;
	double x0, x1, y0, y1 ;
	final MathFunc a, b, c, f ;
	int numPoints ;
	double[] x, y ;
	boolean debug = true ;

	public WeakFormSecondOrder(MathFunc a, MathFunc b, MathFunc c, MathFunc f){
		this.a = a ;
		this.b = b ;
		this.c = c ;
		this.f = f ;
	}

	public void setDegub(boolean debug) {
		this.debug = debug ;
	}

	public void setBoundary(double min, double max) {
		this.x0 = min ;
		this.x1 = max ;
	}

	public void setBoundaryValue(double y0, double y1) {
		this.y0 = y0 ;
		this.y1 = y1 ;
	}

	public void setGridSize(int numPoints) {
		this.numPoints = numPoints ;
	}

	public double[] getGrid() {
		return x ;
	}

	public double[] getSolution() {
		return y ;
	}

	public LinearInterpolation getInterpolator() {
		return new LinearInterpolation(x, y) ;
	}

	public void solve() {
		timer = new Timer() ;
		timer.start();

		// step1: create 1D mesh
		Mesh mesh = MeshGenerator.linear1D(x0, x1, numPoints) ;
		mesh.computeNodeBelongsToElements();

		// step2: mark Dirichlet boundaries
		Map<NodeType, MathFunc> mapNTF = new HashMap<NodeType, MathFunc>() ;
		mapNTF.put(NodeType.Dirichlet, null) ;
		mesh.markBorderNode(mapNTF);

		// step3: create finite element
		FiniteElement fe = new FELinearLine1D() ;

		// step4: create weakform
		WeakForm wf = new WeakForm(fe,
				(u,v) -> -u.diff("x")* (v*a).diff("x") - u * (v*b).diff("x") + c*u*v ,
				(v) -> f*v) ;

		wf.compile();
		// step4: Create assembler
		Assembler assembler = new Assembler(mesh, wf) ;
		assembler.assembleGlobal();

		// step5: generate load and stiffness
		Matrix stiffness = assembler.getGlobalStiffMatrix() ;
		Vector load = assembler.getGlobalLoadVector() ;

		// step6: impose Dirichlet to the global assembly
		Utils.imposeDirichletCondition(stiffness, load, fe, mesh, new SingleVarFunc("boundary", "x") {
			@Override
			public double apply(double... var1) {
				double x = var1[0] ;
				double mid = (x0+x1)/2.0 ;
				if (x<mid)
					return y0 ;
				else
					return y1 ;
			}
		});

		// step7: solve
		Solver solver = new Solver() ;
		Vector u = solver.solveAuto(stiffness, load) ;
		x = new double[u.getDim()] ;
		y = new double[u.getDim()] ;
		NodeList nodes = mesh.getNodeList() ;
		for(int i=1; i<=u.getDim(); i++){
			x[i-1] = nodes.at(i).coord(1);
			y[i-1] = u.get(i) ;
		}
		timer.end();
		if(debug)
			printDebugInfo();
	}

	public void plotSolution() {
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y);
		fig.RenderPlot();
		fig.xlabel("grid");
		fig.ylabel("solution");
		fig.setFigLineWidth(0, 2.0f);
		fig.run(true);
	}

	private void printDebugInfo() {
		System.out.println("xMin = " + x0);
		System.out.println("xMax = " + x1);
		System.out.println(timer);
	}

}

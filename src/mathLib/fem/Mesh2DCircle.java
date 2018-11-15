package mathLib.fem;

import java.util.ArrayList;
import java.util.List;

import mathLib.fem.core.Mesh;
import mathLib.fem.core.Node;
import mathLib.fem.triangulation.DelaunayTriangulator;
import mathLib.fem.triangulation.NotEnoughPointsException;
import mathLib.fem.triangulation.Triangle2D;
import mathLib.fem.triangulation.Vector2D;
import mathLib.geometry.algebra.Point;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;
import mathLib.util.Timer;

public class Mesh2DCircle {

	double centerX, centerY, radius ;
	int numRadius, numAngle ;
	Mesh mesh ;

	public Mesh2DCircle(
			double centerX,
			double centerY,
			double radius
			) {
		this.centerX = centerX ;
		this.centerY = centerY ;
		this.radius = radius ;

		numRadius = 10 ;
		numAngle = 50 ;

		createMesh();
	}

	public Mesh2DCircle(
			Point center,
			double radius
			) {
		this.centerX = center.getX() ;
		this.centerY = center.getY() ;
		this.radius = radius ;

		numRadius = 10 ;
		numAngle = 50 ;

		createMesh();
	}

	public void setCenter(double x, double y) {
		this.centerX = x ;
		this.centerY = y ;
	}

	public void setRadius(double radius) {
		this.radius = radius ;
	}

	public void setNumberofPoints(int numRadius, int numAngle) {
		this.numRadius = numRadius ;
		this.numAngle = numAngle ;
	}

	public void createMesh() {
		mesh = new Mesh() ;

		double[] r = MathUtils.linspace(0.05*radius, radius, numRadius) ;
		double[] theta = MathUtils.linspace(0, 2*Math.PI, numAngle) ;

		List<Vector2D> pointSet = new ArrayList<>() ;
		double x, y ;
		for(int i=0; i<r.length; i++)
			for(int j=0; j<theta.length; j++) {
				x = r[i]*Math.cos(theta[j]) ;
				y = r[i]*Math.sin(theta[j]) ;
				pointSet.add(new Vector2D(x, y)) ;
			}
		pointSet.add(new Vector2D(0, 0)) ;

		Timer timer = new Timer() ;
		timer.start();

		DelaunayTriangulator triangles = new DelaunayTriangulator(pointSet) ;
		try {
			triangles.triangulate();
		} catch (NotEnoughPointsException e) {
			e.printStackTrace();
		}

		timer.stop();
		System.out.println(timer);

		// need to create nodes
		int numNodes = pointSet.size() ;
		for(int i=0; i<numNodes; i++) {
			Node node = new Node(i, pointSet.get(i).x, pointSet.get(i).y) ;
			mesh.addNode(node);
		}
		// need to create elements
		List<Triangle2D> elems = triangles.getTriangles() ;
		int numElements = elems.size() ;
		for(int i=0; i<numElements; i++) {
			// ????
		}
	}

	public void plotNodes() {
		MatlabChart fig = new MatlabChart() ;
		int nodeSize = mesh.getNodeList().size() ;
		double[] x = new double[nodeSize] ;
		double[] y = new double[nodeSize] ;
		for(int i=1; i<=nodeSize; i++) {
			x[i-1] = mesh.getNodeList().at(i).coord(1) ;
			y[i-1] = mesh.getNodeList().at(i).coord(2) ;
		}
		fig.plot(x, y);
		fig.renderPlot();
		fig.markerON();
		fig.setFigLineWidth(0, 0f);
		fig.run(true);

	}

	public Mesh getMesh() {
		return mesh ;
	}


	// for test
	public static void main(String[] args) {
		Mesh2DCircle mesh1 = new Mesh2DCircle(0, 0, 5) ;
		System.out.println(mesh1.getMesh().getNodeList());
		mesh1.plotNodes();
	}

}

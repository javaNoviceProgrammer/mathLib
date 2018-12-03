package mathLib.fem.mesh.mesh2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;

import mathLib.fem.triangulation.Vector2D;
import mathLib.geometry.algebra.Point;
import mathLib.util.MathUtils;

public class Mesh2DRectangleElement extends AbstractMesh2DElement {

	/**
	 * p2-------p3
	 * |		|
	 * p1-------p4
	 */
	
	int defaultDensity = 10;
	double x1, y1, x2, y2, x3, y3, x4, y4  ;
	Point p1, p2, p3, p4;

	ArrayList<Point> points;
	ArrayList<XYAnnotation> annotations;

	public Mesh2DRectangleElement(String name, double xLL, double yLL, double xUR, double yUR) {
		this.elemName = name;
		this.x1 = xLL;
		this.y1 = yLL;
		this.x3 = xUR;
		this.y3 = yUR;
		this.x2 = xLL ;
		this.y2 = yUR ;
		this.x4 = xUR ;
		this.y4 = yLL ;
		this.p1 = Point.getInstance(x1, y1);
		this.p2 = Point.getInstance(x2, y2);
		this.p3 = Point.getInstance(x3, y3);
		this.p4 = Point.getInstance(x4, y4);
		this.points = new ArrayList<>();
		this.annotations = new ArrayList<>();
		createMeshElement();
		createAnnotations();
		
		// default priority
		meshPriority = "0" ;
	}
	
	public void setMeshPriority(String priority) {
		this.meshPriority = priority ;
	}

	@Override
	public boolean isInside(Vector2D point) {
		Mesh2DTriangleElement tri1 = new Mesh2DTriangleElement("tri1", x1, y1, x2, y2, x3, y3) ;
		Mesh2DTriangleElement tri2 = new Mesh2DTriangleElement("tri1", x1, y1, x3, y3, x4, y4) ;
		if(tri1.isInside(point) || tri2.isInside(point))
			return true ;
		else
			return false ;
	}

	private void createMeshElement() {
		double[] r = MathUtils.linspace(0, 1, defaultDensity);
		double[] s = MathUtils.linspace(0, 1, defaultDensity);
		for (int i = 0; i < r.length; i++) {
			for (int j = 0; j < s.length; j++) {
					if (r[i] + s[j] < 1) {
						Point q1 = r[i] * p1 + s[j] * p3 + (1-r[i]-s[j]) * p2 ;
						Point q2 = r[i] * p1 + s[j] * p3 + (1-r[i]-s[j]) * p4 ;
							points.add(q1) ;
							points.add(q2) ;
					}
					else if (r[i] + s[j] == 1) {
						Point q1 = r[i] * p1 + s[j] * p3 + (1-r[i]-s[j]) * p2 ;
						points.add(q1) ;
					}
			}
		}

	}

	private void createAnnotations() {
		XYAnnotation ab = new XYLineAnnotation(p1.getX(), p1.getY(), p2.getX(), p2.getY(), new BasicStroke(3f),
				Color.BLACK);
		XYAnnotation bc = new XYLineAnnotation(p2.getX(), p2.getY(), p3.getX(), p3.getY(), new BasicStroke(3f),
				Color.BLACK);
		XYAnnotation cd = new XYLineAnnotation(p3.getX(), p3.getY(), p4.getX(), p4.getY(), new BasicStroke(3f),
				Color.BLACK);
		XYAnnotation da = new XYLineAnnotation(p4.getX(), p4.getY(), p1.getX(), p1.getY(), new BasicStroke(3f),
				Color.BLACK);
		annotations.add(ab) ;
		annotations.add(bc) ;
		annotations.add(cd) ;
		annotations.add(da) ;
	}

	@Override
	public ArrayList<Vector2D> getNodes() {
		ArrayList<Vector2D> vecs = new ArrayList<>();
		for (Point p : points)
			vecs.add(new Vector2D(p.getX(), p.getY()));
		return vecs ;
	}

	@Override
	public ArrayList<XYAnnotation> getDrawings() {
		return annotations ;
	}

	@Override
	public void refine() {
		defaultDensity = (int) (2 * defaultDensity); // multiply by 2
		// clear previous points
		this.points = new ArrayList<>();
		createMeshElement();
		createAnnotations();
	}

	@Override
	public void refine(int order) {
		defaultDensity = (int) (order * defaultDensity); // multiply by 2
		// clear previous points
		this.points = new ArrayList<>();
		createMeshElement();
		createAnnotations();
	}

}

package mathLib.fem.mesh.mesh2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;

import mathLib.fem.triangulation.Vector2D;
import mathLib.geometry.algebra.Point;
import mathLib.util.MathUtils;

public class Mesh2DTriangleElement extends AbstractMesh2DElement {

	int defaultDensity = 10;
	double x1, y1, x2, y2, x3, y3;
	Point p1, p2, p3;

	ArrayList<Point> points;
	ArrayList<XYAnnotation> annotations;

	public Mesh2DTriangleElement(String name, double x1, double y1, double x2, double y2, double x3, double y3) {
		this.elemName = name;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.x3 = x3;
		this.y3 = y3;
		this.p1 = Point.getInstance(x1, y1);
		this.p2 = Point.getInstance(x2, y2);
		this.p3 = Point.getInstance(x3, y3);
		this.points = new ArrayList<>();
		this.annotations = new ArrayList<>();
		createMeshElement();
		createAnnotations();
	}

	private void createMeshElement() {
		ArrayList<Point> rstVal = new ArrayList<>();
		double[] r = MathUtils.linspace(0, 1, defaultDensity);
		double[] s = MathUtils.linspace(0, 1, defaultDensity);
		double[] t = MathUtils.linspace(0, 1, defaultDensity);
		for (int i = 0; i < r.length; i++) {
			for (int j = 0; j < s.length; j++) {
				for (int k = 0; k < t.length; k++) {
					if (r[i] + s[j] + t[k] == 1)
						rstVal.add(Point.getInstance(r[i], s[j], t[k]));
				}
			}
		}

		for (Point rst : rstVal) {
			Point q = rst.getX() * p1 + rst.getY() * p2 + rst.getZ() * p3;
			points.add(q);
		}

	}

	private void createAnnotations() {
		XYAnnotation ab = new XYLineAnnotation(p1.getX(), p1.getY(), p2.getX(), p2.getY(), new BasicStroke(3f),
				Color.BLACK);
		XYAnnotation ac = new XYLineAnnotation(p1.getX(), p1.getY(), p3.getX(), p3.getY(), new BasicStroke(3f),
				Color.BLACK);
		XYAnnotation bc = new XYLineAnnotation(p2.getX(), p2.getY(), p3.getX(), p3.getY(), new BasicStroke(3f),
				Color.BLACK);
		annotations.add(ab) ;
		annotations.add(ac) ;
		annotations.add(bc) ;
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

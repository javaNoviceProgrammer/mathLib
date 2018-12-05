package mathLib.fem.mesh.mesh2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;

import mathLib.fem.core.Mesh;
import mathLib.fem.core.Node;
import mathLib.fem.triangulation.DelaunayTriangulator;
import mathLib.fem.triangulation.NotEnoughPointsException;
import mathLib.fem.triangulation.Triangle2D;
import mathLib.fem.triangulation.Vector2D;
import mathLib.plot.MatlabChart;

/*
 * how to handle mesh priority to remove overlaps?
 */

public class Mesher2D {
	
	ArrayList<Vector2D> nodes ;
	ArrayList<Triangle2D> elems ;
	ArrayList<AbstractMesh2DElement> mesh2dElements ;
	Map<String, ArrayList<AbstractMesh2DElement>> meshPriorities ;
	
	Mesh mesh ;
	
	MatlabChart fig = null ; // canvas for drawing
	
	public Mesher2D() {
		nodes = new ArrayList<>() ;
		elems = new ArrayList<>() ;
		mesh2dElements = new ArrayList<>() ;
		meshPriorities = new HashMap<>() ;
	}
	
	public void addElement(AbstractMesh2DElement element) {
		mesh2dElements.add(element) ;
	}
	
	public void triangulate() {
		
		for(AbstractMesh2DElement e : mesh2dElements) {
			nodes.addAll(e.getNodes()) ;
		}
			DelaunayTriangulator triangulator = new DelaunayTriangulator(nodes) ;
			try {
				triangulator.triangulate();
			} catch (NotEnoughPointsException e1) {
				e1.printStackTrace();
			}
			elems.addAll(triangulator.getTriangles()) ;
		
		// updating canvas
		updateCanvas();
	}
	
	public Mesh getMesh() {
		mesh = new Mesh() ;
		for(int i=0; i<nodes.size(); i++) {
			Node node = new Node(i, nodes.get(i).x, nodes.get(i).y) ;
			mesh.addNode(node);
		}
		
//		for(Triangle2D tri : elems) {
//			NodeList nodes = new NodeList() ;
//			nodes.add(new Node(globalIndex, x, coords)) ??
//			Element e = new Element(nodes) ;
//		}
		
		return mesh ;
	}
	
	public void updateCanvas() {
		fig = new MatlabChart() ;
		fig.plot(new double[] {}, new double[] {});
		fig.renderPlot();
		// drawing the edges of each element
		for(AbstractMesh2DElement e : mesh2dElements) {
			for(XYAnnotation ann : e.getDrawings())
				fig.getRawXYPlot().addAnnotation(ann);
		}
		// drawing all nodes
		for(Vector2D vec : nodes)
			fig.append(0, vec.x, vec.y);
		fig.markerOFF();
		fig.setFigLineWidth(0, 0f);
		fig.grid("off", "off");
		fig.setFigColor(0, Color.RED);
		// drawing all elements
		for(Triangle2D tri : elems) {
			XYAnnotation ann1 = new XYLineAnnotation(tri.a.x, tri.a.y, tri.b.x, tri.b.y, new BasicStroke(1f), Color.BLACK) ;
			XYAnnotation ann2 = new XYLineAnnotation(tri.b.x, tri.b.y, tri.c.x, tri.c.y, new BasicStroke(1f), Color.BLACK) ;
			XYAnnotation ann3 = new XYLineAnnotation(tri.c.x, tri.c.y, tri.a.x, tri.a.y, new BasicStroke(1f), Color.BLACK) ;
			fig.getRawXYPlot().addAnnotation(ann1);
			fig.getRawXYPlot().addAnnotation(ann2);
			fig.getRawXYPlot().addAnnotation(ann3);
		}
	}
	
	public MatlabChart getCanvas() {
		return fig ;
	}
	
	
	// for test
	public static void main(String[] args) {
//		Mesh2DTriangleElement triangleElement1 = new Mesh2DTriangleElement("tri1", 0, 1, 5, 0, 2, 5) ;
//		Mesh2DTriangleElement triangleElement2 = new Mesh2DTriangleElement("tri2", 1, 1, -1, 0, 2, 5) ;
//		triangleElement1.refine(4);
//		triangleElement2.refine(4);
		
		Mesh2DRectangleElement rect1 = new Mesh2DRectangleElement("rect1", -2, -2, 2, 2) ;
		Mesh2DRectangleElement rect2 = new Mesh2DRectangleElement("rect2", -0.5, -0.5, 0.7, 0.7) ;
//		Mesh2DRectangleElement rect3 = new Mesh2DRectangleElement("rect3", -0.5, -0.5, 0.7, 0.7) ;
		rect1.refine(3);
		rect2.refine(2);
		Mesher2D mesher = new Mesher2D() ;
//		mesher.addElement(triangleElement1);
//		mesher.addElement(triangleElement2);
		mesher.addElement(rect1);
		mesher.addElement(rect2);
		mesher.triangulate();
//		mesher.getCanvas().markerON();
		mesher.getCanvas().run(true);
		
//		System.out.println(triangleElement1.isInside(new Vector2D(-2, 2)));
		System.out.println(rect1.isInside(new Vector2D(0, 0)));
		System.out.println(rect1.isInside(new Vector2D(2, 3)));
		
		System.out.println(mesher.getMesh().getNodeList().size());
	}
}

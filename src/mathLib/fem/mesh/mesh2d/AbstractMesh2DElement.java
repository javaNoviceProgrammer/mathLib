package mathLib.fem.mesh.mesh2d;

import java.util.ArrayList;

import org.jfree.chart.annotations.XYAnnotation;

import mathLib.fem.triangulation.Vector2D;

public abstract class AbstractMesh2DElement {
	
	String elemName ;
	String meshPriority ;
	
	public abstract ArrayList<Vector2D> getNodes() ;
	public abstract ArrayList<XYAnnotation> getDrawings() ;
	public abstract void refine() ;
	public abstract void refine(int order) ;
	public abstract boolean isInside(Vector2D point) ;
	
}

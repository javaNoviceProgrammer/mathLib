package mathLib.fem.mesh.mesh2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import org.jfree.chart.annotations.XYLineAnnotation;

import mathLib.geometry.algebra.Point;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class TestBarycentric {
	public static void main(String[] args) {
		Point A = Point.getInstance(0, 1) ;
		Point B = Point.getInstance(10, 0) ;
		Point C = Point.getInstance(5, 5) ;
		
		ArrayList<Point> rstVal = new ArrayList<>() ;
		
		double[] r = MathUtils.linspace(0, 1, 30) ;
		double[] s = MathUtils.linspace(0, 1, 30) ;
		double[] t = MathUtils.linspace(0, 1, 30) ;
		for(int i=0; i<r.length; i++) {
			for(int j=0; j<s.length; j++) {
				for(int k=0; k<t.length; k++) {
					if(r[i] + s[j] + t[k] == 1)
						rstVal.add(Point.getInstance(r[i], s[j], t[k])) ;
				}
			}
		}
		
		MatlabChart fig = new MatlabChart() ;
		fig.plot(new double[] {}, new double[] {});
		fig.renderPlot();
		fig.append(0, A.getX(), A.getY());
		fig.append(0, B.getX(), B.getY());
		fig.append(0, C.getX(), C.getY());
		fig.markerON();
		fig.setFigLineWidth(0, 0f);
		fig.grid("off", "off");
		fig.setFigColor(0, Color.RED);
		
		XYLineAnnotation ab = new XYLineAnnotation(A.getX(), A.getY(), B.getX(), B.getY(), 
													new BasicStroke(3f), Color.BLACK) ;
		XYLineAnnotation ac = new XYLineAnnotation(A.getX(), A.getY(), C.getX(), C.getY(), 
				new BasicStroke(3f), Color.BLACK) ;
		XYLineAnnotation bc = new XYLineAnnotation(B.getX(), B.getY(), C.getX(), C.getY(), 
				new BasicStroke(3f), Color.BLACK) ;
		fig.getRawXYPlot().addAnnotation(ab);
		fig.getRawXYPlot().addAnnotation(ac);
		fig.getRawXYPlot().addAnnotation(bc);
		
//		XYShapeAnnotation circ = new XYShapeAnnotation(new Ellipse2D.Double(0, 0, 2, 1), new BasicStroke(3f), Color.BLACK) ;
//		fig.getRawXYPlot().addAnnotation(circ);
		
		for(Point p : rstVal) {
			Point q = p.getX() * A + p.getY() * B + p.getZ() * C ;
			fig.append(0, q.getX(), q.getY());
		}
		
		
		fig.show(true);
	}

}

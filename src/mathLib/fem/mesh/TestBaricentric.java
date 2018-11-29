package mathLib.fem.mesh;

import java.util.ArrayList;

import mathLib.geometry.algebra.Point;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class TestBaricentric {
	public static void main(String[] args) {
		Point A = Point.getInstance(0, 0) ;
		Point B = Point.getInstance(10, 0) ;
		Point C = Point.getInstance(5, 5) ;
		
		ArrayList<Point> rstVal = new ArrayList<>() ;
		
		double[] r = MathUtils.linspace(0, 1, 20) ;
		double[] s = MathUtils.linspace(0, 1, 20) ;
		double[] t = MathUtils.linspace(0, 1, 20) ;
		for(int i=0; i<r.length; i++) {
			for(int j=0; j<s.length; j++) {
				for(int k=0; k<t.length; k++) {
					if(r[i] + s[j] + t[k] <= 1)
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
		
		for(Point p : rstVal) {
			Point q = p.getX() * A + p.getY() * B + p.getZ() * C ;
			fig.append(0, q.getX(), q.getY());
		}
		
		
		
		
		
		fig.show(true);
	}

}

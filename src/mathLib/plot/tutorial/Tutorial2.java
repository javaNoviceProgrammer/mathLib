package mathLib.plot.tutorial;

import java.awt.EventQueue;

import mathLib.plot.ColorMapPlot;
import mathLib.plot.util.ColorMap.ColorMapName;
import mathLib.plot.util.MeshGrid;
import mathLib.util.MathUtils;


public class Tutorial2 {
    public static void main(String[] args) {
    	double[] x = MathUtils.linspace(1.0, 10.0, 500) ;
    	double[] y = MathUtils.linspace(10.0, 20.0, 1000) ;
    	MeshGrid mesh = new MeshGrid(x, y) ;
    	double[][] func = new double[x.length][y.length] ;
    	for(int i=0; i<x.length; i++){
    		for(int j=0; j<y.length; j++){
    			func[i][j] = 2*Math.sin(mesh.getX(i, j))*Math.sin(Math.PI*mesh.getY(i, j)) ;
    		}
    	}
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ColorMapPlot(mesh, func, ColorMapName.Rainbow).run(true);;
            }
        });
    }
}

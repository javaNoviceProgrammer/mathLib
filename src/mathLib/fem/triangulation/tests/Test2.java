package mathLib.fem.triangulation.tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import mathLib.fem.triangulation.DelaunayTriangulator;
import mathLib.fem.triangulation.NotEnoughPointsException;
import mathLib.fem.triangulation.Triangle2D;
import mathLib.fem.triangulation.Vector2D;
import mathLib.util.MathUtils;

public class Test2 extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		double[] x = MathUtils.linspace(0, 500, 20) ;
		double[] y = MathUtils.linspace(0, 500, 20) ;

		List<Vector2D> pointSet = new ArrayList<>() ;
		for(int i=0; i<x.length; i++)
			for(int j=0; j<y.length; j++)
				pointSet.add(new Vector2D(x[i], y[j])) ;

		DelaunayTriangulator mesh = new DelaunayTriangulator(pointSet) ;
		try {
			mesh.triangulate();
		} catch (NotEnoughPointsException e) {
			e.printStackTrace();
		}
		
		List<Triangle2D> triangles = mesh.getTriangles() ;
//		for(int i=0; i<triangles.size(); i++)
//			System.out.println(triangles.get(i));

				
		Set<Line> edges = new HashSet<>() ;
		for(Triangle2D t : triangles) {
			Line l1 = new Line(t.a.x*5+100, t.a.y*5+100, t.b.x*5+100, t.b.y*5+100) ;
			l1.setStroke(Color.RED);
			edges.add(l1) ;
			
			Line l2 = new Line(t.b.x*5+100, t.b.y*5+100, t.c.x*5+100, t.c.y*5+100) ;
			l2.setStroke(Color.RED);
			edges.add(l2) ;
			
			Line l3 = new Line(t.c.x*5+100, t.c.y*5+100, t.a.x*5+100, t.a.y*5+100) ;
			l3.setStroke(Color.RED);
			edges.add(l3) ;
			
		}
		
		List<Circle> points = new ArrayList<>() ;
		for(Vector2D v : pointSet) {
			Circle c = new Circle(v.x*5+100, v.y*5+100, 2) ;
			c.setFill(Color.BLUE);
			points.add(c) ;
		}
		
		
		
		AnchorPane pane = new AnchorPane() ;
		pane.getChildren().addAll(points) ;
		pane.getChildren().addAll(edges) ;
		ScrollPane pane1 = new ScrollPane(pane) ;
		Scene scene = new Scene(pane1, 600, 400) ;
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
}

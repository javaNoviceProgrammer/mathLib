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

public class Test3 extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		double radius = 100 ;
		double[] r = MathUtils.linspace(0.1*radius, radius, 10) ;
		double[] theta = MathUtils.linspace(0, 2*Math.PI, 10) ;

		List<Vector2D> pointSet = new ArrayList<>() ;
		double x, y ;
		for(int i=0; i<r.length; i++)
			for(int j=0; j<theta.length; j++) {
				x = r[i]*Math.cos(theta[j]) ;
				y = r[i]*Math.sin(theta[j]) ;
				pointSet.add(new Vector2D(x, y)) ;
			}
		pointSet.add(new Vector2D(0, 0)) ;

		DelaunayTriangulator mesh = new DelaunayTriangulator(pointSet) ;
		try {
			mesh.triangulate();
		} catch (NotEnoughPointsException e) {
			e.printStackTrace();
		}
		
		List<Triangle2D> triangles = mesh.getTriangles() ;
//		for(int i=0; i<triangles.size(); i++)
//			System.out.println(triangles.get(i));

		double offset = 200 ;
		
		Set<Line> edges = new HashSet<>() ;
		for(Triangle2D t : triangles) {
			Line l1 = new Line(t.a.x+offset, t.a.y+offset, t.b.x+offset, t.b.y+offset) ;
			l1.setStroke(Color.RED);
			edges.add(l1) ;
			
			Line l2 = new Line(t.b.x+offset, t.b.y+offset, t.c.x+offset, t.c.y+offset) ;
			l2.setStroke(Color.RED);
			edges.add(l2) ;
			
			Line l3 = new Line(t.c.x+offset, t.c.y+offset, t.a.x+offset, t.a.y+offset) ;
			l3.setStroke(Color.RED);
			edges.add(l3) ;
			
		}
		
		List<Circle> points = new ArrayList<>() ;
		for(Vector2D v : pointSet) {
			Circle c = new Circle(v.x+offset, v.y+offset, 2) ;
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

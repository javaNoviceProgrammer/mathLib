package mathLib.fem.triangulation.tests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import mathLib.fem.core.Node;
import mathLib.fem.triangulation.DelaunayTriangulator;
import mathLib.fem.triangulation.NotEnoughPointsException;
import mathLib.fem.triangulation.Triangle2D;
import mathLib.fem.triangulation.Vector2D;
import mathLib.util.MathUtils;
import mathLib.util.Timer;

public class Test3 extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		double radius = 500 ;
		double[] r = MathUtils.linspace(0.05*radius, radius, 20) ;
		double[] theta = MathUtils.linspace(0, 2*Math.PI, 50) ;

		List<Vector2D> pointSet = new ArrayList<>() ;
		double x, y ;
		for(int i=0; i<r.length; i++)
			for(int j=0; j<theta.length; j++) {
				x = r[i]*Math.cos(theta[j]) ;
				y = r[i]*Math.sin(theta[j]) ;
				pointSet.add(new Node((j+1)+i*r.length, x, y)) ;
			}
		pointSet.add(new Node(r.length*theta.length+1, 0, 0)) ;

		Timer timer = new Timer() ;
		timer.start();
		
		DelaunayTriangulator mesh = new DelaunayTriangulator(pointSet) ;
		try {
			mesh.triangulate();
		} catch (NotEnoughPointsException e) {
			e.printStackTrace();
		}
		
		timer.stop();
		System.out.println(timer);
		
		List<Triangle2D> triangles = mesh.getTriangles() ;
		for(int i=0; i<triangles.size(); i++)
			System.out.println(triangles.get(i));

		double offset = 1000 ;
		
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
//		for(Vector2D v : pointSet) {
//			Circle c = new Circle(v.x+offset, v.y+offset, 2) ;
//			c.setFill(Color.BLUE);
//			c.toBack();
//			points.add(c) ;
//		}
		
		Circle c1 = new Circle(0+offset, 0+offset, radius, Color.BLACK) ;
		Circle c2 = new Circle(0+offset, 0+offset, 0.99*radius, Color.BLACK) ;
		Shape border = Shape.subtract(c1, c2) ;
		
		AnchorPane pane = new AnchorPane() ;
		pane.getChildren().addAll(points) ;
		pane.getChildren().addAll(edges) ;
		pane.getChildren().add(border) ;
		pane.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		ScrollPane pane1 = new ScrollPane(pane) ;
		Scene scene = new Scene(pane1, 600, 400) ;
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
}

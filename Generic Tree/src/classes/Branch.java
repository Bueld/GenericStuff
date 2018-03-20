package classes;

import java.awt.Point;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Branch extends Polygon {
	
	private Point end;
	private Point start;
	private int stage;

	public Branch(Point start, double thickness, Color c, double length, double startRange, double endRange, int stage) {
		this.start = new Point();
		this.start = start;
		
		double range = Math.random()*(endRange-startRange);
		double endX = Math.cos(range+startRange)*length;
		double endY = Math.sin(range+startRange)*length;
		
		end = new Point((int)(endX+this.start.getX()),(int) (endY+this.start.getY()));
		
		Point vec = new Point();
		vec.setLocation(Math.round(endX/length),Math.round(endY/length));
		
		
		Double[] points = {
				-start.getX()+vec.getY()*thickness,-start.getY()-vec.getX()*thickness,
				-start.getX()-vec.getY()*thickness,-start.getY()+vec.getX()*thickness,
				-end.getX()-vec.getY()*thickness,-end.getY()+vec.getX()*thickness,
				-end.getX()+vec.getY()*thickness,-end.getY()-vec.getX()*thickness,
				-start.getX()+vec.getY()*thickness,-start.getY()-vec.getX()*thickness,
		};
		
		this.getPoints().addAll(points);
		this.setFill(c);
		
		this.stage = stage;
	}
	
	public int getStage() {
		return stage;
	}
	
	public Point getEnd() {
		return end;
	}
	
}

package classes;

import java.awt.Point;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Branch extends Polygon {
	
	private Point end;
	private Point start;
	private int stage;

	public Branch(Point start, double thickness, Color c, double length, double range, int stage) {
		this.start = new Point();
		this.start = start;
		
		double range2 = Math.random()*(range);
		
		double endX = (Math.cos(range2));
		double endY = (Math.sin(range2));
		
		Point vec = new Point();
		vec.setLocation(Math.round(endX*thickness),Math.round(endY*thickness));
		
		end = new Point((int)(endX*length+this.start.getX()),(int) (endY*length+this.start.getY()));
		
		
		Double[] points = {
				-start.getX()+vec.getY(),-start.getY()-vec.getX(),
				-start.getX()-vec.getY(),-start.getY()+vec.getX(),
				-end.getX()-vec.getY(),-end.getY()+vec.getX(),
				-end.getX()+vec.getY(),-end.getY()-vec.getX(),
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

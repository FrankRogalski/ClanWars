package main;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Clanmitglied {
	private GraphicsContext gc;
	private Physics phys;
	private Boden[][] terrain;
	private ArrayList<Clanmitglied> menschen = new ArrayList<Clanmitglied>(); 
	private int leben;
	private double[] desires = new double[3]; // desired Essen, Sand, Wasser

	public Clanmitglied(GraphicsContext gc, Boden[][] terrain, ArrayList<Clanmitglied> menschen, int x, int y) {
		this.gc = gc;
		this.terrain = terrain;
		this.menschen = menschen;

		Random r = new Random();
		leben = r.nextInt(60 * 30);
		for (int i = 0; i < desires.length; i++) {
			desires[i] = r.nextInt(11) - 5;
		}

		phys = new Physics(x, y, r.nextDouble() * 5, 5);
	}

	public Clanmitglied(GraphicsContext gc, Boden[][] terrain, ArrayList<Clanmitglied> menschen, int x, int y, int leben, double[] desires,
			double speed) {
		this.gc = gc;
		this.terrain = terrain;
		this.menschen = menschen;
		this.leben = leben;
		this.desires = desires;

		phys = new Physics(x, y, speed, 0.05);
	}

	public void update() {
		leben--;
		if(leben <= 0) {
			die();
		}
		
		for (int i = 0; i < desires.length; i++) {
			Point2D p = findClosestBoden((byte) i);
			if (p != null) {
				phys.seek(p, desires[i]);
			}
		}
		phys.updatePosition();
	}
	
	private void die() {
		menschen.remove(this);
	}

	private Point2D findClosestBoden(byte type) {
		for (int i = (int) Math.round(phys.getLocation().getX() - 30); i < phys.getLocation().getX() + 30; i++) {
			for (int n = (int) Math.round(phys.getLocation().getY() - 30); n < phys.getLocation().getY() + 30; n++) {
				try {
					if (terrain[i][n].getIndex() == type) {
						return new Point2D(i, n);
					}
				} catch (Exception ex) {
				}
			}
		}
		return null;
	}

	public void show() {
		double s = ClanWars.s;
		gc.setFill(Color.RED);
		gc.fillOval(phys.getX() * s - s * 0.5, phys.getY() * s - s * 0.5, s, s);
	}
}
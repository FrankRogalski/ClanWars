package main;

import java.util.ArrayList;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ClanWars extends Application{
	private Canvas can;
	private GraphicsContext gc;
	private Timeline tl_draw;
	
	public final static double px = 800, res = 100, s = px / res;
	
	private Boden terrain[][] = new Boden[(int) res][(int) res];
	private double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
	
	ArrayList<Clanmitglied> menschen = new ArrayList<Clanmitglied>();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void init() throws Exception {
		tl_draw = new Timeline(new KeyFrame(Duration.millis(1000.0 / 60.0), e -> {
			draw();
		}));
		tl_draw.setCycleCount(Timeline.INDEFINITE);
		tl_draw.play();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Pane root = new Pane();
		Scene scene = new Scene(root, px, px);
		
		stage.setTitle("Sample Text");
		
		can = new Canvas(scene.getWidth(), scene.getHeight());
		gc = can.getGraphicsContext2D();
		
		root.getChildren().add(can);
		
		stage.setScene(scene);
		stage.show();
		
		setup();
	}
	
	private void setup() {
		PerlinNoise pn = new PerlinNoise();
		Random r = new Random();
		double mult = r.nextDouble() * 0.1;
		double[][] terrain = new double[(int) res][(int) res];
		
		for (int i = 0; i < terrain.length; i++) {
			for (int n = 0; n < terrain[0].length; n++) {
				terrain[i][n] = pn.noise(i * mult, n * mult, 1);
				if (terrain[i][n] > max) {
					max = terrain[i][n];
				}
				if (terrain[i][n] < min) {
					min = terrain[i][n];
				}
			}
		}
		
		for (int i = 0; i < terrain.length; i++) {
			for (int n = 0; n < terrain[0].length; n++) {
				terrain[i][n] = map(terrain[i][n], min, max, 0, 10);
				if (terrain[i][n] < 3) {
					this.terrain[i][n] = new Boden((byte) 0);
				} else if (terrain[i][n] < 5) {
					this.terrain[i][n] = new Boden((byte) 1);
				} else {
					this.terrain[i][n] = new Boden((byte) 2);
				}
			}
		}
		
		for (int i = 0; i < 20; i++) {
			menschen.add(new Clanmitglied(gc, this.terrain, menschen, 45 + r.nextInt(11), 45 + r.nextInt(11)));
		}
	}
	
	private void draw() {
		gc.clearRect(0, 0, can.getWidth(), can.getHeight());
		drawMap();
		drawMenschen();
	}
	
	private void drawMenschen() {
		for (int i = menschen.size() - 1; i >= 0; i--) {
			Clanmitglied cm = menschen.get(i);
			cm.show();
			cm.update();
		}
	}
	
	private void drawMap() {
		for (int i = 0; i < terrain.length; i++) {
			for (int n = 0; n < terrain[0].length; n++) {
				gc.setFill(terrain[i][n].getColor());
				gc.fillRect(i * s, n * s, s, s);
			}
		}
	}
	
	public final static double map(double value, double min, double max, double nMin, double nMax) {
		return ((value - min) / (max - min)) * (nMax - nMin) + nMin;
	}
}
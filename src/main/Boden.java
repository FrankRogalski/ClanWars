package main;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Boden {
	private byte index;
	
	public Boden(byte index) {
		this.index = index;
	}
	
	public Paint getColor() {
		if (index == 0) {
			return Color.AQUA;
		} else if (index == 1) {
			return Color.GREEN;
		} // index == 2
		return Color.ORANGE;
	}
	
	public byte getIndex() {
		return index;
	}
	
	public void setIndex(byte index) {
		this.index = index;
	}
}

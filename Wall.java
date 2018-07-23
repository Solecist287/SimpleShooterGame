package gamepackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Wall {
	private int x,y;
	private int width, height;
	private int outline_width;
	public Color color;

	public Wall(int x, int y, int width, int height, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.color = color;
		outline_width = 1;
	}
	
	public int getx() { 
		return x; 
	}
	public int gety() { 
		return y;
	}
	public int getWidth() { 
		return width;
	}
	public int getHeight() { 
		return height;
	}
	public void update() {
		//wall stays still for now
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);//x,y,width,height
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(outline_width));
		g.drawRect(x, y, width, height);
	}
}

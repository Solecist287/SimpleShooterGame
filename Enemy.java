package gamepackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Enemy {
	private int x,y;//coords
	private int r;//radius
	private int dx,dy;
	private int speed;
	private int hp;//health points
	private boolean hit;
	private boolean dead;
	private Color color;
	private int outline_width;
	
	//CONSTRUCTOR
	public Enemy(int tier) {
		hit=false;
		dead=false;
		outline_width = 1;
		if (tier==1) {
			color = new Color ((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
			this.r = 20;
			speed = 2;
			hp = 1;
		}
		//randomize coordinates
		Random ran = new Random();
		int xmin,xmax,ymin,ymax;
		xmin = GamePanel.border_thickness + r;
		xmax = GamePanel.WIDTH - 1 - GamePanel.border_thickness - r;
		ymin = GamePanel.border_thickness + r;
		ymax = GamePanel.HEIGHT - 1 - GamePanel.border_thickness - r;
		
		x = xmin + ran.nextInt(xmax - xmin);
		y = ymin + ran.nextInt(ymax - ymin);
		
		//distance stuff
		dx = speed;//not sure, maybe randomize?
		dy = speed;
	}
	
	//GETTERS/SETTERS
	public Color getColor() {
		return this.color;
	}
	public int getOutline_width() { 
		return this.outline_width; 
	}
	public int getdx() { 
		return dx; 
	}
	public int getdy() { 
		return dy;
	}
	public void setdx(int dx) { 
		this.dx = dx; 
	}
	public void setdy(int dy) { 
		this.dy = dy;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	//coordinates stuff
	public int getx() { 
		return x; 
	}
	public int gety() { 
		return y;
	}
	public int getr() { 
		return this.r; 
	}
	public void setx(int x) { 
		this.x = x; 
	}
	public void sety(int y) { 
		this.y = y;
	}
	public void setr(int r) {
		this.r = r;
	}
	//speed stuff
	public int getSpeed() { 
		return this.speed; 
	}
	public void setSpeed(int speed) { 
		this.speed = speed; 
	}
	
	//health stuff
	public boolean isDead() { 
		return hp <= 0; 
	}
	public void hurt(int damage) {
		hp-=damage;
		if (isDead()) {
			dead = true;
		}
		//hit=true;
		//set hit timer
	}
	//UPDATE
	public void update() {
		x+=dx;
		y+=dy;
		//do hit timer checks
	}
	//DRAW
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
		
		g.setStroke(new BasicStroke(outline_width));
		g.setColor(Color.BLACK);
		g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
		//g.setStroke(new BasicStroke(1));
	}
}

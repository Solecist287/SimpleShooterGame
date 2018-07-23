package gamepackage;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Projectile {
	public int x,y,dx,dy;
	public int speed;
	public int damage;
	public int outline_width;
	public Color color;
	public Ownership owner;
	public Projectile(){
		outline_width = 1;
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
	public void setx(int x) { 
		this.x = x; 
	}
	public void sety(int y) { 
		this.y = y;
	}
	//speed stuff
	public int getSpeed() { 
		return this.speed; 
	}
	public void setSpeed(int speed) { 
		this.speed = speed; 
	}
	
	
	public abstract void update();
	public abstract void draw(Graphics2D g);
	

}

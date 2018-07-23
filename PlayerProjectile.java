package gamepackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

public class PlayerProjectile extends Projectile {
	private int side;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	
	public PlayerProjectile(int x, int y, Color color) {
		super();
		this.x = x;
		this.y = y;
		
		left = false;
		right = false;
		up = false;
		down = false;
		
		owner = Ownership.PLAYER;
		dx = 0;
		dy = 0;
		speed = 10;
		damage = 1;
		this.color = color;
		side = 5;//match player firinghandler pside
	}
	public int getSide() {
		return this.side;
	}
	//shoot direction stuff
	public boolean getLeft() {
		return left;
	}
	public boolean getRight() {
		return right;
	}
	public boolean getUp() {
		return up;
	}
	public boolean getDown() {
		return down;
	}
	public void setLeft(boolean b) { 
		left = b; 
	}
	public void setRight(boolean b) { 
		right = b; 
	}
	public void setUp(boolean b) { 
		up = b; 
	}
	public void setDown(boolean b) { 
		down = b; 
	}
	@Override
	public void update() {
		if (up==true) {
			dx = 0;
			dy = -speed;
		}else if (down==true) {
			dx = 0;
			dy = speed;
		}else if (right==true) {
			dx = speed;
			dy = 0;
		}else if (left==true) {
			dx = -speed;
			dy = 0;
		}
		
		x+=dx;
		y+=dy;
		
		dx = 0;
		dy = 0;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		//GradientPaint colortowhite = new GradientPaint(x,y,color,x+side,y+side,Color.WHITE);
		//g.setPaint(colortowhite);
		g.fillRect(x, y, side, side);
		g.setStroke(new BasicStroke(outline_width));
		g.setColor(Color.BLACK);
		g.drawRect(x, y, side, side);
	}

}

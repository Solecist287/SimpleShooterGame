package gamepackage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
public class Player {
	private int score;
	
	private int lives;
	
	private int ammo;
	private int fullammo;
	
	private int x,y,dx,dy;
	private int speed;
	private int side;
	private int outline_width;
	private Color color;
	//movement vars
	private char travel_direction;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	//shoot vars
	private boolean shootleft;
	private boolean shootright;
	private boolean shootup;
	private boolean shootdown;
	//reloading vars
	private boolean isreloading;
	private long reloadtimer;
	private long reloaddelay;
	//firing vars
	private boolean isfiring;
	private long firingtimer;
	private long firingdelay;
	
	public Player() {
		x = GamePanel.WIDTH/2;
		y = GamePanel.HEIGHT/2;
		speed = 3;
		dx = 0;
		dy = 0;
		score = 0;
		lives = 3;
		//color = new Color(27,227,250);
		color = new Color(45,193,193);
		side = 28;
		outline_width=1;
		
		fullammo = 4;
		ammo = fullammo;//ammo and fullammo should be multiple of 4
		firingtimer = System.nanoTime();
		isfiring = false;
		firingdelay = 500;
		reloaddelay = 1000;
	}
	
	//life stuff
	public void gainLife() {
			lives++;
	}
	public void loseLife() {
			lives--;
	}
	public boolean isDead() { 
			return lives <= 0; 
	}
	
	//ammo stuff
	public boolean shouldReload() {
		return ammo<=0;
	}
	public void reload() {
		this.ammo = fullammo;
	}
	//GETTERS
	public Color getColor() {
		return this.color;
	}
	public int getScore() {
		return score;
	}
	public int getSide() { 
		return this.side; 
	}
	public int getOutline_width() { 
		return this.outline_width; 
	}
	//coordinates stuff
	public int getx() { 
		return x; 
	}
	public int gety() { 
		return y;
	}
	//SETTERS
	public void setx(int x) { 
		this.x = x; 
	}
	public void sety(int y) { 
		this.y = y;
	}
	//move direction stuff
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
	public char getDir() { 
		return this.travel_direction;
	}
	//shoot direction stuff
	public void shootLeft(boolean b) { 
		shootleft = b; 
	}
	public void shootRight(boolean b) { 
		shootright = b; 
	}
	public void shootUp(boolean b) { 
		shootup = b; 
	}
	public void shootDown(boolean b) { 
		shootdown = b; 
	}
	public void setisfiring(boolean b) { 
		isfiring = b; 
	}
	//UPDATE
	public void setFiringHandler(boolean firing, char direction) {
		//shoot one direction at a time
		if (firing==true && shootup == false && shootdown == false && shootleft == false && shootright == false) {
			if (direction == 'u') {//up
				shootup = true;
				isfiring=true;
			}else if (direction == 'd') {//down
				shootdown = true;
				isfiring=true;
			}else if (direction == 'l') {//left
				shootleft = true;
				isfiring=true;
			}else if (direction == 'r') {//right
				shootright = true;
				isfiring=true;
			}
		}else if (firing==false) {
			if (direction == 'u') {//up
				shootup = false;
				isfiring=false;
			}else if (direction == 'd') {//down
				shootdown = false;
				isfiring=false;
			}else if (direction == 'l') {//left
				shootleft = false;
				isfiring=false;
			}else if (direction == 'r') {//right
				shootright = false;
				isfiring=false;
			}
		}
	}
	
	private void firingHandler() {
		int px,py;
		int pside = 5;//match PlayerProjectile side
		if (isfiring && shouldReload()==false && isreloading==false) {
			long elapsed = (System.nanoTime()-firingtimer)/1000000;
			if (elapsed>=firingdelay) {//time passed is sufficient
				firingtimer = System.nanoTime();
				if (shootup==true) {
					px = x+(int)(side/2)- (int)(pside/2);
					py = y + (int)(side/2) - (int)(pside/2);
					PlayerProjectile pp = new PlayerProjectile(px,py,color);
					GamePanel.p_projectiles.add(pp);
					pp.setUp(true);
					ammo--;
				}else if (shootdown==true) {//agh
					px = x+(int)(side/2) - (int)(pside/2);
					py = y + (int)(side/2) - (int)(pside/2);
					PlayerProjectile pp = new PlayerProjectile(px,py,color);
					GamePanel.p_projectiles.add(pp);
					pp.setDown(true);
					ammo--;
				}else if (shootleft==true) {
					px = x + (int)(side/2) - (int)(pside/2);
					py = y+(int)(side/2) - (int)(pside/2);
					PlayerProjectile pp = new PlayerProjectile(px,py,color);
					GamePanel.p_projectiles.add(pp);
					pp.setLeft(true);
					ammo--;
				}else if (shootright==true) {//agh
					px = x + (int)(side/2) - (int)(pside/2);
					py = y + (int)(side/2) - (int)(pside/2);
					PlayerProjectile pp = new PlayerProjectile(px,py,color);
					GamePanel.p_projectiles.add(pp);
					pp.setRight(true);
					ammo--;
				}
			}
		}
	}
	
	public void setReloadingHandler() {
		if (ammo<fullammo && isreloading==false) {
			isreloading = true;
			reloadtimer = System.nanoTime();
		}
	}
	private void reloadingHandler() {
		if (isreloading) {
			long elapsed = (System.nanoTime()-reloadtimer)/1000000;
			if (elapsed>=reloaddelay) {//reload time is sufficient
				ammo = fullammo;
				isreloading = false;
			}
		}
	}
	
	private void emptyAnimation(Graphics2D g) {//animation for when square fires a shot. Empties in fourths
		if (ammo==fullammo || ammo>(int)((fullammo*3)/4)) {//full ammo
			g.fillRect(x, y, side, side);//full square
		}else if (ammo==(int)((fullammo*3)/4) || ammo>(int)((fullammo*2)/4)) {//shot once
			g.fillRect(x+(side/2), y, (int)(side/2), (int)(side/2));//topright square quadrant
			g.fillRect(x, y+(int)(side/2), (int)(side/2), (int)(side/2));//bottomleft square quadrant
			g.fillRect(x+(int)(side/2), y+(int)(side/2), (int)(side/2), (int)(side/2));//bottomright square quadrant
		}else if (ammo==(int)((fullammo*2)/4)|| ammo>(int)((fullammo*1)/4)) {//shot twice
			g.fillRect(x, y+(int)(side/2), (int)(side/2), (int)(side/2));//bottomleft square quadrant
			g.fillRect(x+(int)(side/2), y+(int)(side/2), (int)(side/2), (int)(side/2));//bottomright square quadrant
		}else if (ammo==(int)((fullammo*1)/4) || ammo>0) {//shot thrice
			g.fillRect(x, y+(int)(side/2), (int)(side/2), (int)(side/2));//bottomleft square quadrant
		}
	}
	
	private void reloadAnimation(Graphics2D g) {//animation for reloading square (reverse order of empyting animation)
		long elapsed = (System.nanoTime()-reloadtimer)/1000000;
		if (elapsed>=(int)((reloaddelay*1)/4) && elapsed<(int)((reloaddelay*2)/4)) {//reloading 1/4 done
			g.fillRect(x, y+(int)(side/2), (int)(side/2), (int)(side/2));//bottomleft square quadrant
		}else if (elapsed>=(int)((reloaddelay*2)/4) && elapsed<(int)((reloaddelay*3)/4)) {//reloading 2/4 done
			g.fillRect(x, y+(int)(side/2), (int)(side/2), (int)(side/2));//bottomleft square quadrant
			g.fillRect(x+(int)(side/2), y+(int)(side/2), (int)(side/2), (int)(side/2));//bottomright square quadrant
		}else if (elapsed>=(int)((reloaddelay*3)/4) && elapsed<(int)((reloaddelay*4)/4)) {//reloading 3/4 done
			g.fillRect(x, y+(int)(side/2), (int)(side/2), (int)(side/2));//bottomleft square quadrant
			g.fillRect(x+(int)(side/2), y+(int)(side/2), (int)(side/2), (int)(side/2));//bottomright square quadrant
			g.fillRect(x+(side/2), y, (int)(side/2), (int)(side/2));//topright square quadrant
		}
	}
	
	public void update() {
		
		if (up==true) {
			dy=-speed;
			travel_direction = 'u';
		}
		else if (down==true) {
			dy=speed;
			travel_direction = 'd';
		}
		else if (right==true) {
			dx=speed;
			travel_direction = 'r';
		}
		else if (left==true) {
			dx=-speed;
			travel_direction = 'l';
		}
		x+=dx;
		y+=dy;
		//controls firing directions and delays
		firingHandler();
		
		//handle ammo
		reloadingHandler();
		
		dx = 0;
		dy = 0;
	}
	public void draw(Graphics2D g) {
		g.setColor(color);
		//square dynamically changes with amount of ammo it has
		if (!isreloading) {
			emptyAnimation(g);
		}else if (isreloading) {
			reloadAnimation(g);
		}
		
		//outline
		g.setStroke(new BasicStroke(outline_width));
		g.setColor(Color.BLACK);
		g.drawRect(x, y, side, side);
	}
}

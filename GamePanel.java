package gamepackage;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.util.ArrayList;
import java.awt.event.*;


public class GamePanel extends Canvas implements Runnable,KeyListener{
	
	private static final long serialVersionUID = 5776264079234158789L;
	public static final int WIDTH=640;
	public static final int HEIGHT=480;
	
	public static int border_thickness = 20;
	
	private boolean init = true;
	private int level;
	
	private BufferedImage image;
	private Graphics2D g;
	
	public Player player;
	public static ArrayList <Enemy> enemies;
	public static ArrayList <EnemyProjectile> e_projectiles;
	public static ArrayList <PlayerProjectile> p_projectiles;
	public static ArrayList <Wall> walls;

	private int FPS = 60;
	private double averageFPS;
	
	private Thread thread;
	private boolean running=false;
	
	public GamePanel() {
		setSize(WIDTH,HEIGHT);
		addKeyListener(this);
		setFocusable(true);
		requestFocus();
		new Window(WIDTH,HEIGHT,"Squares and Circles",this);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running=true;
	}
	public synchronized void stop() {
		try {
			thread.join();
			running=false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void run() {
		//game loop vars
		long startTime;
		long URDTimeMillis;
		long waitTime;
		long totalTime = 0;
		int frameCount = 0;
		int maxFrameCount = 60;
		long targetTime = 1000 / FPS;
		
		level = 1;
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		player = new Player();
		
		enemies = new ArrayList <Enemy> ();
		p_projectiles = new ArrayList <PlayerProjectile> ();
		e_projectiles = new ArrayList <EnemyProjectile> ();
		walls = new ArrayList <Wall> ();
		// GAME LOOP
		while(running){
			startTime = System.nanoTime();
			game_update();
			game_render();
			game_draw();
			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
			waitTime = targetTime - URDTimeMillis;
			try {
				Thread.sleep(Math.max(0,waitTime));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			frameCount++;
			if(frameCount == maxFrameCount) {
				averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
				frameCount = 0;
				totalTime = 0;
			}	
		}
		stop();
	}
	private void game_update() {
		
		spawnSystem();//spawning
		
		player.update();//updating player
		
		//updating enemies
		for (int i=0; i<enemies.size();i++) {
			enemies.get(i).update();
		}
		
		//updating projectiles
		for (int i=0; i<p_projectiles.size();i++) {
			p_projectiles.get(i).update();
		}
		
		for (int i=0; i<e_projectiles.size();i++) {
			e_projectiles.get(i).update();
		}
		
		//not update walls?
		
		//collision
		projectiles_enemies_collision();
		walls_collision_handler();
		
		
		//if (enemies.size()==0) {level++;} //update level
	}
	private void game_render() {
		//draw background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		//draw player	
		player.draw(g);
		//draw enemies
		for (int i=0; i<enemies.size();i++) {
			enemies.get(i).draw(g);
		}
		//draw projectiles
		for (int i=0; i<p_projectiles.size();i++) {
			p_projectiles.get(i).draw(g);
		}
		for (int i=0; i<e_projectiles.size();i++) {
			e_projectiles.get(i).draw(g);
		}
		//draw walls
		for (int i=0; i<walls.size();i++) {
			walls.get(i).draw(g);
		}
	}
	private void game_draw() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
	public static void main(String[] args) {
		new GamePanel();
	}

	@Override
	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();
		
		if (keyCode == KeyEvent.VK_UP) {
			player.setUp(true);
		}else if (keyCode == KeyEvent.VK_DOWN) {
			player.setDown(true);
		}else if (keyCode == KeyEvent.VK_RIGHT) {
			player.setRight(true);
		}else if (keyCode == KeyEvent.VK_LEFT) {
			player.setLeft(true);
		}
		
		//shooting directions
		if (keyCode == KeyEvent.VK_W) {//shoot up
			player.setFiringHandler(true, 'u');
		}else if (keyCode == KeyEvent.VK_A) {//shoot left
			player.setFiringHandler(true, 'l');
		}else if (keyCode == KeyEvent.VK_S) {//shoot down
			player.setFiringHandler(true, 'd');
		}else if (keyCode == KeyEvent.VK_D) {//shoot right
			player.setFiringHandler(true, 'r');
		}
		//reloading
		if (keyCode == KeyEvent.VK_R) {//shoot up
			player.setReloadingHandler();//handler in player class should take care of setting to false
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {
		int keyCode = key.getKeyCode();
		if (keyCode == KeyEvent.VK_UP) {
			player.setUp(false);
		}else if (keyCode == KeyEvent.VK_DOWN) {
			player.setDown(false);
		}else if (keyCode == KeyEvent.VK_RIGHT) {
			player.setRight(false);
		}else if (keyCode == KeyEvent.VK_LEFT) {
			player.setLeft(false);
		}
		
		//shooting directions
		if (keyCode == KeyEvent.VK_W) {//shoot up
			player.setFiringHandler(false, 'u');
		}else if (keyCode == KeyEvent.VK_A) {//shoot left
			player.setFiringHandler(false, 'l');
		}else if (keyCode == KeyEvent.VK_S) {//shoot down
			player.setFiringHandler(false, 'd');
		}else if (keyCode == KeyEvent.VK_D) {//shoot right
			player.setFiringHandler(false, 'r');
		}
	}

	@Override
	public void keyTyped(KeyEvent key) {
		
	}	
	
	public boolean projectile_enemy_collision(PlayerProjectile p, Enemy e) {
		double rx = e.getx();
		double ry = e.gety();
		
		boolean up = false;
		boolean down = false;
		boolean left = false;
		boolean right = false;
		
		if (rx<p.getx()) {
			rx = p.getx();
			left = true;
		}else if (rx>(p.getx()+p.getSide())){
			rx = p.getx()+p.getSide();
			right = true;
		}
		if (ry<p.gety()) {
			ry = p.gety();
			up = true;
		}else if (ry>(p.gety()+p.getSide())){
			ry = p.gety()+p.getSide();
			down = true;
		}
		if (Math.hypot(rx-e.getx(), ry-e.gety())<e.getr()){
			if (up==true) {
				//e.setdy(e.getdy()*-1);
				return true;
			}else if (down==true) {
				//e.setdy(e.getdy()*-1);
				return true;
			}
			if (left==true) {
				//e.setdx(e.getdx()*-1);
				return true;
			}else if (right==true) {
				//e.setdx(e.getdx()*-1);
				return true;
			}
		}
		return false;
	}
	public void projectiles_enemies_collision() {
		int psize = p_projectiles.size();
		int esize = enemies.size();
		int i=0;
		int j=0;
		
		while(i<psize && psize>0) {
			PlayerProjectile p = p_projectiles.get(i);
			esize = enemies.size();
			j=0;
			while (j<esize && esize>0 && psize>0) {
				Enemy e = enemies.get(j);
				boolean collision = projectile_enemy_collision(p,e);
				if (collision) {
					e.hurt(p.damage);
					if (e.isDead()) {
						enemies.remove(j);
						esize--;
						j--;
					}
					p_projectiles.remove(i);
					psize--;
					i--;
				}
				j++;
			}
			i++;
		}
	}
	
	//should return true if enemy touches wall, false otherwise
	//also, corrects enemies coordinates/speed if touches wall
	public boolean enemy_wall_collision(Wall w, Enemy e) {
		double rx = e.getx();
		double ry = e.gety();
		
		boolean up = false;
		boolean down = false;
		boolean left = false;
		boolean right = false;
		
		boolean retval = false;
		
		if (rx<w.getx()) {
			rx = w.getx();
			left = true;
		}else if (rx>(w.getx()+w.getWidth())){
			rx = w.getx()+w.getWidth();
			right = true;
		}
		if (ry<w.gety()) {
			ry = w.gety();
			up = true;
		}else if (ry>(w.gety()+w.getHeight())){
			ry = w.gety()+w.getHeight();
			down = true;
		}
		if (Math.hypot(rx-e.getx(), ry-e.gety())<e.getr()){//collides
			if (up==true) {
				e.setdy(e.getdy()*-1);
			}else if (down==true) {
				e.setdy(e.getdy()*-1);
			}
			if (left==true) {
				e.setdx(e.getdx()*-1);
			}else if (right==true) {
				e.setdx(e.getdx()*-1);
				
			}
			retval = true;
		}
		return retval;
	}
	public void player_wall_collision(Wall w, char p_dir) {
		int p_left = player.getx();
		int p_right = player.getx() + player.getSide() ;
		int p_top = player.gety() ;
		int p_bottom = player.gety() + player.getSide() ;
		
		int w_left = w.getx() ;
		int w_right = w.getx() + w.getWidth() ;
		int w_top = w.gety() ;
		int w_bottom = w.gety() + w.getHeight() ;

		if ((p_left<w_right && p_left>w_left)||(p_right<w_right && p_right>w_left)||(p_left<w_left && p_right>w_right)) {
			//top or bottom collisions
			if (p_dir=='d' && p_bottom>w_top && p_top<w_top) {//top collision
				player.sety(w_top - player.getSide());//bound player on wall
			}else if (p_dir=='u' && p_top<w_bottom && p_bottom>w_bottom) {//bottom collision
				player.sety(w_bottom);//bound player under wall
			}
		}
		if ((p_top>w_top && p_top<w_bottom)||(p_bottom>w_top && p_bottom<w_bottom)||(p_bottom>w_bottom && p_top<w_top)) {
			//left or right collisions
			if (p_dir=='r' && p_right>w_left && p_left<w_left) {//left collision
				player.setx(w_left - player.getSide());//bound player on left of wall
			}else if (p_dir=='l' && p_left<w_right && p_right>w_right) {//right collision
				player.setx(w_right);//bound player on right of wall
			}
		}
	}
	public boolean p_projectile_wall_collision(Wall w, PlayerProjectile p) {
		//square projectile collide with rectangle
		int p_left = p.getx();
		int p_right = p.getx() + p.getSide() ;
		int p_top = p.gety() ;
		int p_bottom = p.gety() + p.getSide() ;
		
		int w_left = w.getx() ;
		int w_right = w.getx() + w.getWidth() ;
		int w_top = w.gety() ;
		int w_bottom = w.gety() + w.getHeight() ;

		return (p_left<w_right && p_right>w_left && p_top<w_bottom && p_bottom>w_top);
	}
	public boolean e_projectile_wall_collision(Wall w, EnemyProjectile ep) {
		double rx = ep.getx();
		double ry = ep.gety();
		
		boolean up = false;
		boolean down = false;
		boolean left = false;
		boolean right = false;
		
		if (rx<w.getx()) {
			rx = w.getx();
			left = true;
		}else if (rx>(w.getx()+w.getWidth())){
			rx = w.getx()+w.getWidth();
			right = true;
		}
		if (ry<w.gety()) {
			ry = w.gety();
			up = true;
		}else if (ry>(w.gety()+w.getHeight())){
			ry = w.gety()+w.getHeight();
			down = true;
		}
		if (Math.hypot(rx-ep.getx(), ry-ep.gety())<ep.getr()){
			if (up==true) {
				//ep.setdy(ep.getdy()*-1);
				return true;
			}else if (down==true) {
				//ep.setdy(ep.getdy()*-1);
				return true;
			}
			if (left==true) {
				//ep.setdx(ep.getdx()*-1);
				return true;
			}else if (right==true) {
				//ep.setdx(ep.getdx()*-1);
				return true;
			}
		}
		return false;
	}
	public void walls_collision_handler() {
		//compare player with all walls
		for (int windex=0;windex<walls.size();windex++) {
			player_wall_collision(walls.get(windex),player.getDir());
		}
		//compare each enemy to each wall
		for (int windex=0;windex<walls.size();windex++){
			for (int eindex=0;eindex<enemies.size();eindex++){
				enemy_wall_collision(walls.get(windex),enemies.get(eindex));
			}
		}
		//player projectile wall collisions
		int ppindex = 0;
		int ppsize = p_projectiles.size();
		while(ppindex<ppsize && ppsize>0) {
			PlayerProjectile p = p_projectiles.get(ppindex);
			for (int windex=0;windex<walls.size();windex++){
				Wall w = walls.get(windex);
				boolean collision = p_projectile_wall_collision(w,p);
				if (collision) {
					p_projectiles.remove(ppindex);
					ppindex--;
					ppsize--;
					break;
				}
			}
			ppindex++;
		}
		//enemy projectile wall collisions
		int epindex = 0;
		int epsize = e_projectiles.size();
		while(epindex<epsize && epsize>0) {
			EnemyProjectile ep = e_projectiles.get(ppindex);
			for (int windex=0;windex<walls.size();windex++){
				Wall w = walls.get(windex);
				boolean collision = e_projectile_wall_collision(w,ep);
				if (collision) {
					e_projectiles.remove(epindex);
					epindex--;
					epsize--;
					break;
				}
			}
			epindex++;
		}	
	}

	public void spawnSystem() {
		if (init == true) {
			init = false;
			
			//level border [ESSENTIAL]
			Color levelcolor = new Color ((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
			walls.add(new Wall(0,0,WIDTH,border_thickness,levelcolor));//top horizontal wall
			walls.add(new Wall(0,(HEIGHT-1)-border_thickness,WIDTH,border_thickness,levelcolor));//bottom horizontal wall
			walls.add(new Wall(0,0,border_thickness,(HEIGHT-1),levelcolor));//left vertical wall
			walls.add(new Wall((WIDTH-1)-border_thickness,0,border_thickness,(HEIGHT-1),levelcolor));//right vertical wall
		
			//other walls
			walls.add(new Wall(100,100,200,10,levelcolor));
			walls.add(new Wall(100,300,250,50,levelcolor));
			walls.add(new Wall(100,378,250,50,levelcolor));
			
			int num_enemies = 5;
			for (int i=0;i<num_enemies;i++) {
				Enemy e;
				boolean retry;
				do {
					retry = false;
					e = new Enemy(1);
					//compare each enemy to each wall (first 4 walls is border..prechecked so can ignore)
					for (int windex=4;windex<walls.size();windex++){
						Wall w = walls.get(windex);
						retry = enemy_wall_collision(w,e);
						if (retry) {//collided true...retry is true!
							break;
						}
					}
				}while(retry==true);
				enemies.add(e);
				//System.out.println(numiter+"enemy color"+e.getColor());
				//System.out.println("enemy at: ("+e.getx()+", "+e.gety()+")");
			}
		}
	}
}

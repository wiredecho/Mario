package com.tutorial.mario;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Input.KeyInput;

import com.tutorial.mario.entity.Entity;
import com.tutorial.mario.gfx.Sprite;
import com.tutorial.mario.gfx.SpriteSheet;

public class Game extends Canvas implements Runnable {

	public static final int WIDTH = 270;
	public static final int HEIGHT = WIDTH/14*10;
	public static final int SCALE = 4;
	public static final String TITLE = "Mario";
	
	private Thread thread;
	private boolean running = false;
	private BufferedImage image;
	
	public static Handler handler;
	public static SpriteSheet sheet;
	public static Camera cam;
	
	public static Sprite grass;
	public static Sprite powerUp;
	public static Sprite usedPowerUp;
	public static Sprite player[] = new Sprite[10];
	public static Sprite mushroom;
	public static Sprite goomba[] = new Sprite[10];
	
	public Game() {
		Dimension size = new Dimension(WIDTH*SCALE, HEIGHT*SCALE);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
	}
	
	private void init(){
		handler = new Handler();
		sheet = new SpriteSheet("/spritesheet.png");
		cam = new Camera();
		
		addKeyListener(new KeyInput());
		
		grass = new Sprite(sheet, 1, 1);
		powerUp=new Sprite(sheet,1,14);
		usedPowerUp = new Sprite (sheet, 2,14);
		mushroom = new Sprite (sheet, 1, 3);
		player = new Sprite[10];
		goomba = new Sprite[10];
		for(int i=0;i<player.length;i++){
			player[i]=new Sprite(sheet,i+1,16);
		}
		
		for(int i=0;i<goomba.length;i++){
			goomba[i]=new Sprite(sheet,i+1,7);
		}
		
		
		
		try {
			image = ImageIO.read(getClass().getResource("/level.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		handler.createLevel(image);

	}
	
	
	private synchronized void start(){
		if(running) return;
		running = true;
		thread = new Thread(this, "Thread");
		thread.start();
	}
	
	private synchronized void stop(){
		if(!running) return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		init();
		requestFocus();
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();double delta = 0;
		double ns = 1000000000.0/60.0;
		int frames = 0;
		int ticks = 0;
		while(running){
			long now = System.nanoTime();
			delta+=(now-lastTime)/ns;
			lastTime = now;
			while(delta>=1){
				tick();
				ticks++;
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis()-timer>1000){
				timer+=1000;
				System.out.println(frames + " Frames Per Second " + ticks + " Updates Per Second ");
				frames = 0;
				ticks = 0;
			}
		}
		stop();
	}
	
	public int getFrameWidth(){
		return WIDTH*SCALE;
	}
	
	public int getFrameHeight(){
		return HEIGHT*SCALE;
	}
	
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs==null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0,  0,  getWidth(), getHeight());
		g.translate(cam.getX(),cam.getY());
		handler.render(g);
		g.dispose();
		bs.show();
	}
	
	public void tick(){ //update
		handler.tick();
		
		for(Entity e:handler.entity){
			if(e.getId()==Id.player){
				cam.tick(e);
			}
		}
	}
	

	
	public static void main(String[] args){
		Game game = new Game();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		game.start();
	}

	
	

}

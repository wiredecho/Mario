package com.tutorial.mario.entity.mob;

import java.awt.Color;
import java.awt.Graphics;

import com.tutorial.mario.Game;
import com.tutorial.mario.Handler;
import com.tutorial.mario.Id;
import com.tutorial.mario.entity.Entity;
import com.tutorial.mario.states.PlayerState;
import com.tutorial.mario.tile.Tile;

public class Player extends Entity {
	
	private PlayerState state;
	private int pixelsTravelled = 0;
	
	private int frame = 0;
	private int frameDelay = 0;
	
	private boolean animate = false;

	public Player(int x, int y, int width, int height, Id id,
			Handler handler) {
		super(x, y, width, height, id, handler);
		
		state= PlayerState.SMALL;
	
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		if(facing==0){
			g.drawImage(Game.player[frame+5].getBufferedImage(), x, y, width, height, null);
		}else if(facing==1){
			g.drawImage(Game.player[frame].getBufferedImage(), x, y, width, height, null);
		}
	}

	public void tick() {
		x+=velX;
		y+=velY; 
		if(goingDownPipe){
			pixelsTravelled+=velY;
		}
		if(y+height >= 771) y=771-height;
		if(velX!=0) animate=true;
		else animate = false;
		for(int i=0;i<handler.tile.size();i++){
			Tile t = handler.tile.get(i);
			if(t.isSolid()&&!goingDownPipe){
				if(getBoundsTop().intersects(t.getBounds())){
					setVelY(0);
//					y = t.getY()+t.height; removed for platform game purposes
					if(jumping) {
						jumping =false;
						gravity=0.8;
						falling=true;
					}
					if(t.getId()==Id.powerUp){
						if(getBoundsTop().intersects(t.getBounds())) t.activated=true;
					}
				}
				if(getBoundsBottom().intersects(t.getBounds())){
					setVelY(0);
//					y = t.getY()-t.height; removed for platform game purposes
					if(falling) falling=false;
				}else if(!falling && !jumping){
						falling = true;
						gravity = 0.8;
				}
				if(getBoundsLeft().intersects(t.getBounds())){
					setVelX(0);
					x = t.getX()+t.width;
				}
				if(getBoundsRight().intersects(t.getBounds())){
					setVelX(0);
					x = t.getX()-t.width;
				}
			}
		}
		
		for(int i=0;i<handler.entity.size();i++){
			Entity e = handler.entity.get(i);
			
			if(e.getId()==Id.mushroom){
				if(getBounds().intersects(e.getBounds())){
					int tpX = getX();
					int tpY = getY();
					if(state==PlayerState.SMALL){
						width*=2;
						height*=2;
					}
					setX(tpX-width);
					setY(tpY-height);
					if(state==PlayerState.SMALL) state = PlayerState.BIG;
					e.die();
				}
			}else if(e.getId()==Id.goomba){
				if(getBoundsBottom().intersects(e.getBoundsTop())){
					e.die();
				}else if(getBounds().intersects(e.getBounds())){
					if(state==PlayerState.BIG) {
						state = PlayerState.SMALL;
						width/=2;
						height/=2;
						
					}
					else if(state==PlayerState.SMALL){
						die();
					}
				
				}
			}
		}
		
		
		if(jumping&&!goingDownPipe){
			gravity-=0.1;
			setVelY((int)-gravity);
			if(gravity<=0.0){
				jumping = false;
				falling = true;
			}
		}
		if(falling&&!goingDownPipe){
			gravity+=0.1;
			setVelY((int)gravity);
		}
		if(animate){
			frameDelay++;
			if(frameDelay>=3){
				frame++;
				if(frame>=5){
					frame = 0;
				}
				frameDelay=0;			
			}
		}
		
		if(goingDownPipe){
			for(int i=0;i<Game.handler.tile.size();i++){
				Tile t = Game.handler.tile.get(i);
				if(t.getId()==Id.pipe){
					switch(t.facing){
					case 0:
						setVelY(-5);
						break;
					case 2:
						setVelY(5);
						break;
					}
					if(pixelsTravelled>t.height+height) goingDownPipe=false;
				}
			}
		}

	}
}

package com.tutorial.mario;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import com.tutorial.mario.entity.Entity;
import com.tutorial.mario.entity.mob.Goomba;
import com.tutorial.mario.entity.mob.Player;
import com.tutorial.mario.entity.powerup.Mushroom;
import com.tutorial.mario.tile.PowerUpBlock;
import com.tutorial.mario.tile.Tile;
import com.tutorial.mario.tile.Wall;

public class Handler {

	public LinkedList<Entity> entity = new LinkedList<Entity>();
	public LinkedList<Tile> tile = new LinkedList<Tile>();
	

	
	public void render(Graphics g){
		for(int i=0;i<entity.size();i++){
			Entity e = entity.get(i);
			e.render(g);
		}
		for(int i=0;i<tile.size();i++){
			Tile t = tile.get(i);
			t.render(g);
		}
	}
	
	public void tick(){
		for(int i=0; i<entity.size();i++){
			entity.get(i).tick();
		}
		for(Tile ti:tile){
			ti.tick();
		}
	}
	
	public void addEntity(Entity e){
		entity.add(e);
		
	}
	public void removeEntity(Entity e){
		entity.remove(e);
	}
	
	public void addTile(Tile t){
		tile.add(t);
		
	}
	public void removeTile(Tile t){
		entity.remove(t);
	}

	public void createLevel(BufferedImage level){
		int width = level.getWidth();
		int height = level.getHeight();
		
		for(int y=0; y<height; y++){
			for(int x = 0; x<width; x++){
				int pixel = level.getRGB(x, y);
				
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				//a brick will be placed if black
				if(red==0&&green==0&&blue==0) addTile(new Wall(x*64, y*64, 64,64,true,Id.wall,this));
				//player will be where a blue box is
				if(red==0&&green==0&&blue==255) addEntity(new Player(x*64, y*64, 64,64,Id.player,this));
				if(red==255&&green==0&&blue==0) addEntity(new Mushroom(x*64,y*64,64,64,Id.mushroom,this));
				if(red==0&&green==255&&blue==0) addEntity(new Goomba(x*64,y*64,64,64,Id.goomba,this));
				if(red==255&&green==255&&blue==0) addTile(new PowerUpBlock(x*64,y*64, 64,64,true,Id.powerUp,this,Game.mushroom));
			}
		}
	}
}
package com.tutorial.mario.tile;

import java.awt.Color;
import java.awt.Graphics;

import com.tutorial.mario.Handler;
import com.tutorial.mario.Id;

public class Pipe extends Tile{

	public Pipe(int x, int y, int width, int height, boolean solid, Id id, Handler handler, int facing) {
		super(x, y, width, height, solid, id, handler);
		this.facing = facing;
		// TODO Auto-generated constructor stub
	}



	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(new Color(128,128,128));
		g.fillRect(x, y, width, height);
		
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}}

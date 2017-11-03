package com.bpalomino.entities;

import org.newdawn.slick.geom.Rectangle;

public class MapNode{
	private Rectangle rectangle;
	private boolean blocked;
	private boolean dot;
	private boolean powerpellet;
	private boolean teleporter;

	public MapNode(int x, int y, boolean blocked, boolean dot, boolean powerpellet, boolean teleporter){
		rectangle = new Rectangle(x* 24, y* 24,24,24);
		this.blocked=blocked;
		this.dot=dot;
		this.powerpellet=powerpellet;
		this.teleporter=teleporter;
	}

	public Rectangle getRectangle(){ return rectangle; }

	public void setBlocked(boolean setting){ blocked = true; }
	public boolean isBlocked(){ return blocked; }

	public boolean isDot() { return dot; }
	public void eatDot() { dot=false; }

	public boolean isPowerPellet(){ return powerpellet; }
	public void eatPowerPellet(){ powerpellet=false; }

	public boolean isTeleporter(){ return teleporter; };
}
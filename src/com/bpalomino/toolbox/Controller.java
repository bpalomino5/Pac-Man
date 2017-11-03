package com.bpalomino.toolbox;

import com.bpalomino.managers.JukeBox;
import com.bpalomino.game.PacGame;
import com.bpalomino.entities.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Input;

public class Controller{
	private static final int SIZE= 24;
	private float x;
	private float y;
	private Animation sprite;
	private Image [] movement;
	private int score;
	private boolean finished=true;
	private boolean movable=false;

	//update method vars
	private int dir;
	private int nextdir;
	private float fdelta = 2f;

	public Controller(int x, int y, SpriteSheet ss){
	//init position
		this.x = x*SIZE;
		this.y = y*SIZE;
	//init Images
		movement = new Image[]{ss.getSprite(0,0) , ss.getSprite(1,0), ss.getSprite(2,0), ss.getSprite(3,0)};
	//init Animation
		sprite = new Animation(movement,30,false);
	//ping pong animation for nice display of pacman
		sprite.setPingPong(true);
	}

	public int getX() { return (int) x; }
	public int getY() { return (int) y; }

	public void update(Input input, int delta){
		if(movable){
		//Check for teleporter
			if(Map.getNode(Math.round(x/SIZE), Math.round(y/SIZE)).isTeleporter()){
				if(x+SIZE==SIZE && dir==Input.KEY_LEFT) x=(PacGame.width-1)*SIZE;
				else if(x==((PacGame.width-1)*SIZE) && dir==Input.KEY_RIGHT) x=0;
			}
		//Handle buffering
			if(dir!=0){
				if(dir==Input.KEY_LEFT) bufferLeft(delta);
				else if(dir==Input.KEY_RIGHT) bufferRight(delta);
				else if(dir==Input.KEY_UP) bufferUp(delta);
				else if(dir==Input.KEY_DOWN) bufferDown(delta);
			}
			if(nextdir!=0){
				if(nextdir==Input.KEY_UP) tryUp();
				else if(nextdir==Input.KEY_DOWN) tryDown();
				else if(nextdir==Input.KEY_RIGHT) tryRight();
				else if(nextdir==Input.KEY_LEFT) tryLeft();
			}
		//Check and set directions
			if(input.isKeyDown(Input.KEY_UP)){
			//if going left or right already, then (try) up
				if(dir==Input.KEY_LEFT || dir==Input.KEY_RIGHT) nextdir=Input.KEY_UP;
			//if not going up, then go up
				if(dir==0 || dir==Input.KEY_DOWN) dir=Input.KEY_UP;
			}
			else if(input.isKeyDown(Input.KEY_DOWN)){
				if(dir==Input.KEY_LEFT || dir==Input.KEY_RIGHT) nextdir=Input.KEY_DOWN;
				if(dir==0 || dir==Input.KEY_UP) dir=Input.KEY_DOWN;
			}
			else if(input.isKeyDown(Input.KEY_LEFT)){
				if(dir==Input.KEY_UP || dir==Input.KEY_DOWN) nextdir=Input.KEY_LEFT;
				if(dir==0 || dir==Input.KEY_RIGHT) dir=Input.KEY_LEFT;
			}
			else if(input.isKeyDown(Input.KEY_RIGHT)){
				if(dir==Input.KEY_UP || dir==Input.KEY_DOWN) nextdir=Input.KEY_RIGHT;
				if(dir==0 || dir==Input.KEY_LEFT) dir=Input.KEY_RIGHT;
			}
		//Eat Pac-Dots
			if(Map.getNode(Math.round(x/SIZE), Math.round(y/SIZE)).isDot()){
				Map.getNode(Math.round(x/SIZE), Math.round(y/SIZE)).eatDot();
				score+=10;
				if(finished){ 
					JukeBox.play("part1");
					finished=false;
				}
				else{
					JukeBox.play("part2");
					finished=true;
				}
			}
		//Eat Power Pellets
			if(Map.getNode(Math.round(x/SIZE), Math.round(y/SIZE)).isPowerPellet()){ 
				Map.getNode(Math.round(x/SIZE), Math.round(y/SIZE)).eatPowerPellet();
				score+=50;
			}
		}
	}

	public void draw(){
		sprite.draw((int)x, (int) y);
	}

	public int getScore() { return score; }
	public void setMovable(boolean status) { movable=status; }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////HELPER METHODS FOR MOVEMENT///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void tryUp(){
		if(!(isBlocked(x,y-fdelta) || isBlocked(x+SIZE-1, y-fdelta))){
			dir=Input.KEY_UP;
			nextdir=0;
		}
	}
	private void tryDown(){
		if(!(isBlocked(x,y+SIZE) || isBlocked(x+SIZE-1, y + SIZE))){
			dir=Input.KEY_DOWN;
			nextdir=0;
		}
	}
	private void tryLeft(){
		if(!(isBlocked(x - fdelta, y) || isBlocked(x - fdelta, y +SIZE-1))){
			dir=Input.KEY_LEFT;
			nextdir=0;		
		}
	}
	private void tryRight(){
		if(!(isBlocked(x+SIZE, y) || isBlocked(x+SIZE, y +SIZE-1))){
			dir=Input.KEY_RIGHT;
			nextdir=0;			
		}
	}
	
	private void bufferLeft(int delta){
			for(Image im : movement) im.setRotation(-90);
			if(!(isBlocked(x - fdelta, y) || isBlocked(x - fdelta, y +SIZE-1))){
				sprite.update(delta);
				x -= fdelta;
			}
			else dir=0;
	}
	private void bufferRight(int delta){
			for(Image im : movement) im.setRotation(90);
			if(!(isBlocked(x+SIZE, y) || isBlocked(x+SIZE, y +SIZE-1))){
				sprite.update(delta);
				x += fdelta;
			}
			else dir=0;
	}
	private void bufferUp(int delta){
			for(Image im : movement) im.setRotation(0);
			if(!(isBlocked(x,y-fdelta) || isBlocked(x+SIZE-1, y-fdelta))){
				sprite.update(delta);
				y -= fdelta;
			}
			else dir=0;
	}
	private void bufferDown(int delta){
			for(Image im : movement) im.setRotation(180);
			if(!(isBlocked(x,y+SIZE) || isBlocked(x+SIZE-1, y + SIZE))){
				sprite.update(delta);
				y += fdelta;
			}
			else dir=0;
	}

	private boolean isBlocked(float x, float y){
		int xBlock = (int) x / SIZE;
		int yBlock = (int) y / SIZE;
		return Map.getNode(xBlock,yBlock).isBlocked();
	}

}
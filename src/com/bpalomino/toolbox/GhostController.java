package com.bpalomino.toolbox;

import com.bpalomino.entities.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Input;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.geom.Vector2f;
import java.util.ArrayList;

public class GhostController{
	private static final int SIZE= 24;
	private static final int HALFSIZE=SIZE/2;
	private float x;
	private float y;
	private Animation sprite, upAnimation, rightAnimation, downAnimation, leftAnimation;
	private Image [] up,right,down,left;
	private ArrayList<Vector2f> points;
	private Vector2f target,start;

	private boolean movable=false;
	private int pathTimer;
	private int limit;
	private AStarPathFinder pathFinder;
	private Path path;
	private int type;

	public GhostController(int x, int y, SpriteSheet ss, int type, Map map){
	//init position
		this.x = x*SIZE+12;
		this.y = y*SIZE;
	//init Images
		up = new Image[]{ss.getSprite(0,type), ss.getSprite(1,type)};
		right = new Image[]{ss.getSprite(2,type), ss.getSprite(3,type)};
		down = new Image[]{ss.getSprite(4,type), ss.getSprite(5,type)};
		left = new Image[]{ss.getSprite(6,type), ss.getSprite(7,type)};
	//init Animation
		upAnimation = new Animation(up,130,true);
		rightAnimation = new Animation(right,130,true);
		downAnimation = new Animation(down,130,true);
		leftAnimation = new Animation(left,130,true);
	//set sprite to right animation
		sprite = rightAnimation;

	//init arraylist
		points = new ArrayList<>();

		this.type=type;
	//setup timer limits based on ghost (type)
		if(type==4) limit = 15;
		else if(type==5) limit = 20;
		else if(type==6) limit = 25;
		else if(type==7) limit = 30;

	//startpath
		pathFinder = new AStarPathFinder(map,100,false);
		path = pathFinder.findPath(null,x,9,11,19);

	//init start vector
		start = new Vector2f(x+.5f,y);
	}

	public void update(GameContainer gc, int delta,int px, int py){
	//timer for handling when to recalculate pathfinding
		pathTimer+=delta;
		if(movable){
			if(pathTimer/100>=limit){
				pathTimer=0;
				if(isStartPosition()) startPath(path,start.getX());
				else getPath(pathFinder.findPath(null,Math.round(x/SIZE),Math.round(y/SIZE),px/SIZE,py/SIZE));
			}
		}

	//check to see if ghost finished moving down current path
		if(!isFinished()) gotoTarget(target.getX(),target.getY());

	//check to see if a ghost hit pacman
		if((x+HALFSIZE==px && y==py) || (x==px+HALFSIZE && y==py) || (x==px && y+HALFSIZE==py) || (x==px && y==py+HALFSIZE)){ 
			System.out.println("YOU LOST!");
			gc.exit();
		}
	}
	public void setMovable(boolean status) { movable=status; }

	private boolean isStartPosition(){
		return (x==start.getX()*SIZE && y==start.getY()*SIZE);
	}

//checks if there is a current target to go to and if ghost is already made it to the target, then gets next target if available
	private boolean isFinished(){
		if(target==null || target.getX()*SIZE==x && target.getY()*SIZE==y){
			if(!points.isEmpty()) target=points.remove(0);
			return true;
		}
		else return false; 
	}

//moves ghost pixel position by two pixels based on target coordinates everytime called
	public void gotoTarget(float xtarget, float ytarget){
		if(x<xtarget*SIZE){ 
			sprite=rightAnimation; 
			x+=2;
		}
		else if(xtarget*SIZE < x){
			sprite=leftAnimation; 
			x-=2;
		}
		if(y<ytarget*SIZE){ 
			sprite=downAnimation; 
			y+=2;
		}
		else if(ytarget*SIZE < y){
			sprite=upAnimation; 
			y-=2;
		}
	}

//recalculates path by clearing current path, then create new array of path points
	public void getPath(Path path){
		if(!points.isEmpty()) points.clear();
		for(int i = 0; i < path.getLength(); i++) points.add(new Vector2f(path.getX(i), path.getY(i)));
	}

//starter path for when game is created and to help ghost move smoothly out of ghost box
	public void startPath(Path path,float x){
		points.add(new Vector2f(x,9));
		for(int i = 0; i < path.getLength(); i++) points.add(new Vector2f(path.getX(i), path.getY(i)));
	}

	public int getX() { return Math.round(x); }
	public int getY() { return Math.round(y); }

	public void draw(){
		sprite.draw((int) x, (int) y);
	}
}
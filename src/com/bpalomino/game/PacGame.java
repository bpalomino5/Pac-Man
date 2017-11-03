package com.bpalomino.game;

import com.bpalomino.managers.JukeBox;
import com.bpalomino.toolbox.GhostController;
import com.bpalomino.toolbox.Controller;
import com.bpalomino.entities.Map;
import com.bpalomino.entities.MapNode;
import com.bpalomino.managers.FileManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;


public class PacGame extends BasicGameState
{
	public static final int ID = 1;

	private static SpriteSheet ss;
	private Image [] powerPellet;
	private Image pacdot;

	private Map map;

	private static Rectangle rect;

	public static int width;
	public static int height;
	private static final int SIZE  = 24;

	private Animation pellet;

	private UnicodeFont font;

	private int gameTimer;
	
	//Controllers
	private Controller pacController;
	private GhostController blinkyController;
	private GhostController pinkyController;
	private GhostController inkyController;
	private GhostController clydeController;


	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
	//setup bounds vars
		width = FileManager.getWidth();
		height = FileManager.getHeight();
	//setup static rectangle
		rect = new Rectangle(0,0,SIZE,SIZE);
	//turn off fps counter
		gc.setShowFPS(false);
	//init spritesheet with pacman png at 24x24 pixels each
		ss= new SpriteSheet("res/pacman_characters.png",24,24);
	//init images
		pacdot = ss.getSprite(4,0);
		powerPellet = new Image[]{ss.getSprite(6,0), new Image(0,0)};
	
	//init animation
		pellet = new Animation(powerPellet,200,true);
	//setup Map
		map = new Map();
	//init Controllers
		pacController = new Controller(11,19,ss);
		blinkyController = new GhostController(9,11,ss,4,map);
		pinkyController = new GhostController(10,11,ss,5,map);
		inkyController = new GhostController(11,11,ss,6,map);
		clydeController = new GhostController(12,11,ss,7,map);

	//init font
		font = new UnicodeFont("res/animeace.ttf",24,false,false);
		font.addAsciiGlyphs();
		font.getEffects().add(new ColorEffect(java.awt.Color.white));
		font.loadGlyphs();

	//init JukeBox
		JukeBox.load("res/sounds/part1.ogg","part1");
		JukeBox.load("res/sounds/part2.ogg","part2");
		JukeBox.load("res/sounds/start.ogg","start");
	}

	@Override
	public void update(GameContainer gc,StateBasedGame sbg, int delta) throws SlickException {
		gameTimer+=delta;
		if(gameTimer/1000==5){
			blinkyController.setMovable(true);
			pacController.setMovable(true);
		}
		else if (gameTimer/1000==13) pinkyController.setMovable(true);
		else if(gameTimer/1000==25) inkyController.setMovable(true);
		else if(gameTimer/1000==40) clydeController.setMovable(true);

		Input input = gc.getInput();

		pacController.update(input,delta);
		blinkyController.update(gc,delta,pacController.getX(),pacController.getY());
		pinkyController.update(gc,delta,pacController.getX(),pacController.getY());
		inkyController.update(gc,delta,pacController.getX(),pacController.getY());
		clydeController.update(gc,delta,pacController.getX(),pacController.getY());

		
		if(input.isKeyDown(Input.KEY_ESCAPE)) gc.exit();
		if(pacController.getScore()==2540){
			System.out.println("YOU WIN!");
			gc.exit();
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg,Graphics g) throws SlickException{
	//draw sprite
		pacController.draw();
	//draw map
		for(int x=0; x<width;x++){
			for(int y=0; y < height; y++){
				if(Map.getNode(x,y).isBlocked()){
					g.setColor(Color.white);
					g.fill(Map.getNode(x,y).getRectangle());
					g.setLineWidth(2F);
					g.setColor(Color.black);
					g.draw(Map.getNode(x,y).getRectangle());
					g.resetLineWidth();
				}
				else if(Map.getNode(x,y).isDot()) g.drawImage(pacdot,x*SIZE,y*SIZE);
				else if(Map.getNode(x,y).isPowerPellet()) pellet.draw(x*SIZE,y*SIZE);
				else if(Map.getNode(x,y).isTeleporter()){
					g.setColor(Color.red); 
					g.fill(Map.getNode(x,y).getRectangle());
					g.setLineWidth(2F);
					g.setColor(Color.black);
					g.draw(Map.getNode(x,y).getRectangle());
				}
			}
		}
	//draw ghosts
		blinkyController.draw();
		pinkyController.draw();
		inkyController.draw();
		clydeController.draw();
	//draw score
		font.drawString(5,height*SIZE,"Score: "+pacController.getScore());
	}

	@Override
	public int getID(){
		return PacGame.ID;
	}

	@Override
	public void enter(GameContainer gc, StateBasedGame sbg){
		try{
			AppGameContainer appgc = (AppGameContainer) gc;
			appgc.setDisplayMode(FileManager.getWidth()*24, (FileManager.getHeight()*24)+30, false);
		} catch(SlickException e){
			e.printStackTrace();
		}

		//play starting music
		JukeBox.play("start");
	}

}
package com.bpalomino.game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import com.bpalomino.managers.FileManager;
import java.io.IOException;


public class Application extends StateBasedGame{
	// Game state identifiers 
	public static final int MAINMENU	= 0;
	public static final int GAME 		= 1;

	// Application Properities
	public static final int WIDTH		= 640;
	public static final int HEIGHT 		= 480;
	public static final int FPS			= 70;
	public static final double VERSION	= 1.0;

	// Class Constructor
	public Application(String appName){
		super(appName);
	}

	// Initialize your game states (calls init method of each gamestate, and set's the state ID)
	public void initStatesList(GameContainer gc) throws SlickException{
		// The first state added will be the one that is loaded first, when the application is launched 
		this.addState(new MainMenu());
		this.addState(new PacGame());
	}

	// Main Method
 	public static void main(String[] args) {
 		//First thing to happen at compilation, load map file for app configuration based off data
		try{
			FileManager.read("res/level4.txt");
		} catch(IOException e){ e.printStackTrace(); }

 		try{
 			AppGameContainer app = new AppGameContainer(new Application("Pac-Man"));
 			app.setDisplayMode(WIDTH, HEIGHT, false);
 			app.setTargetFrameRate(FPS);
 			app.setShowFPS(false);
 			app.start();
 		}
 		catch(SlickException e){
 			e.printStackTrace();
 		}
 	}
}
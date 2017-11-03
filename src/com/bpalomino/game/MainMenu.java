package com.bpalomino.game;

import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Input;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;


public class MainMenu extends BasicGameState
{
	private String[] menuItems;
	private int currentItem = 1;
	private Image image;

	// ID we return to class 'Application'
	public static final int ID = 0;

	// init-method for initializing all resources
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		menuItems = new String[]{
			"Quit",
			"Play"
		};
		image = new Image("res/title.png");
	}

	// render-method for all the things happening on-screen
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		for(int i =0; i < menuItems.length; i++){
			if(currentItem == i) g.setColor(Color.cyan);
			else g.setColor(Color.white);
			g.drawString(menuItems[i], 280, 300 - 30 * i);
		}
		image.draw(100,70,0.5f);

	}

	@Override
	public void keyPressed(int key, char c){
		if(key==Input.KEY_UP){
			if(currentItem < menuItems.length - 1) currentItem++;
		}

		if(key==Input.KEY_DOWN){
			if(currentItem > 0) currentItem--;
		}
	}

	// update-method with all the magic happening in it
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException{
		if(gc.getInput().isKeyDown(Input.KEY_ESCAPE)) gc.exit();
		if(gc.getInput().isKeyDown(Input.KEY_ENTER)){
			if(sbg.getState(currentItem).getID()==this.getID())
				gc.exit();
			sbg.enterState(currentItem);
		}
	}

	// Returning 'ID' from class 'MainMenu'
	@Override
	public int getID(){
		return MainMenu.ID;
	}
}
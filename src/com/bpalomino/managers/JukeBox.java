package com.bpalomino.managers;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import java.util.HashMap;

public class JukeBox{
	private static HashMap<String, Sound> sounds;

	static{
		sounds = new HashMap<String, Sound>();
	}

	public static void load(String path, String name) throws SlickException{
		Sound sound = new Sound(path);
		sounds.put(name, sound);
	}

	public static void play(String name){
		sounds.get(name).play();
	}

	public static void loop(String name){
		sounds.get(name).loop();
	}

	public static void stop(String name){
		sounds.get(name).stop();
	}

	// public static void pause(String name){
	// 	sounds.get(name).pause();
	// }

	// public static void resume(String name){
	// 	sounds.get(name).resume();
	// }

	public static void stopAll(){
		for(Sound s : sounds.values()){
			s.stop();
		}
	}

	public static boolean isPlaying(String name){
		return sounds.get(name).playing();
	}
}
package com.bpalomino.entities;

import com.bpalomino.game.PacGame;
import com.bpalomino.entities.MapNode;
import com.bpalomino.managers.FileManager;
import org.newdawn.slick.util.pathfinding.TileBasedMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;

public class Map implements TileBasedMap{
	private static MapNode[][] nodes;
	private int width, height;

	public Map(){
		this.width=PacGame.width;
		this.height=PacGame.height;

		//setup Map Nodes using FileManager to parse map file
		int type;
		nodes = new MapNode[width][height];		//20 cols, 25 rows
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				type = FileManager.getMapData(x,y);
				if(type==1) nodes[x][y] = new MapNode(x,y,true,false,false,false);
				else if(type==0) nodes[x][y] = new MapNode(x,y,false,true,false,false);
				else if(type==2) nodes[x][y] = new MapNode(x,y,false,false,true,false);
				else if(type==3) nodes[x][y] = new MapNode(x,y,false,false,false,true);
				else if(type==4) nodes[x][y] = new MapNode(x,y,false,false,false,false);
			}
		}
	}
	public static MapNode getNode(int x, int y) { return nodes[x][y]; }

	@Override
	public boolean blocked(PathFindingContext ctx, int x, int y){
		return nodes[x][y].isBlocked();
	}

	@Override
	public float getCost(PathFindingContext ctx, int x, int y){
		return 1.0f;
	}

	@Override
	public int getHeightInTiles(){
		return height;
	}

	@Override
	public int getWidthInTiles(){
		return width;
	}

	@Override
	public void pathFinderVisited(int x, int y){}
}
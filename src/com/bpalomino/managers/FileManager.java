package com.bpalomino.managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class FileManager{
	private String filePath;
	private static BufferedReader reader;
	private static int width,height;
	private static int[][] data;

	public FileManager(String file){
		width=0;
		height=0;
		filePath=file;
	}

	public static void read(String file) throws IOException{
		String line;
		InputStream in = FileManager.class.getResourceAsStream("/"+file);
		// reader = new BufferedReader(new FileReader(file));
		reader = new BufferedReader(new InputStreamReader(in));

		line = reader.readLine();
		String[] parts = line.split(",",2);
		width = Integer.parseInt(parts[0]);
		height = Integer.parseInt(parts[1]);

		data = new int[width][height];		//25 col, 20 rows
		int row=0;
		while((line = reader.readLine()) != null){		//loops 20 times - i.e. going downwards on map
			for(int col = 0; col < width; col++){
				data[col][row] = line.charAt(col) -'0';
			}
			row++;
		}
	}

	public static int getWidth() { return width; }
	public static int getHeight() { return height; }
	public static int getMapData(int x, int y){
		return data[x][y];
	}

	public static void print(){
		for(int j=0; j<height;j++){
			for(int i = 0; i < width; i++){
				System.out.print(data[i][j]);
			}
			System.out.println();
		}
	}
}
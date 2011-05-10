package com.dyndns.kebree.dames.controller;

import java.util.ArrayList;

import android.util.Log;

import com.dyndns.kebree.dames.model.Grid;
import com.dyndns.kebree.dames.model.Piece.Color;
import com.dyndns.kebree.dames.model.Player;
import com.dyndns.kebree.dames.view.GridView;

/**
 * Controler of the game.
 * 
 * @author Kal
 *
 */
public class DamesControl {
	private ArrayList<GridView> views = new ArrayList<GridView>();
	private Grid model;
	private Player[] players;
	private int currentPlayer;
	
	/**
	 * Constructor
	 * 
	 * @param model the model linked with the controller
	 */
	public DamesControl(Grid model, Player p1, Player p2){
		players = new Player[2];
		this.players[0] = p1;
		this.players[1] = p2;
		currentPlayer = 0;
		this.model=model;
	}
	
	/**
	 * Adding a view linked with the controller
	 * 
	 * @param v the new view
	 */
	public void addView(GridView v){
		views.add(v);
	}

	/**
	 * This method says if a piece is selectable by one of the players
	 * 
	 * @param idTail the id of the square
	 * @param color the color of the player who try to select the piece on the square
	 * @return if the square is selectable
	 */
	public boolean selectable(int idSquare, Color color) {
		if(model.getPiece(idSquare).getColor() == color){
			return true;
		}
		return false;
	}
	
	public boolean ended(int newSquare){
		Color color = model.getPiece(newSquare).getColor();
		Log.i("i", "color : "+color+", square : " + newSquare);
		if(color == Color.black){
			int[] diagBG = model.diagBG(newSquare);
			if(model.canEat(newSquare, diagBG[1]))
				return false;
			int[] diagBD = model.diagBD(newSquare);
			if(model.canEat(newSquare, diagBD[1]))
				return false;
		}
		if(color == Color.white){
			int[] diagHG = model.diagHG(newSquare);
			if(model.canEat(newSquare, diagHG[1]))
				return false;
			int[] diagHD = model.diagHD(newSquare);
			if(model.canEat(newSquare, diagHD[1]))
				return false;
		}
		Log.i("i", "ended");
		return true;
	}

	/**
	 * move a piece from a square to another if possible
	 * 
	 * @param select the selected piece
	 * @param newSquare the new square of the piece 
	 */
	public void movePiece(int select, int newSquare) {
		if(model.canMove(select, newSquare)){
			model.movePiece(select, newSquare);
			currentPlayer = (currentPlayer+1)%2;
		}else if(model.canEat(select, newSquare)){
			Log.i("i", "canEat ? : "+model.canEat(select, newSquare));
			model.eatPiece(select, newSquare);
			if(ended(newSquare))
				currentPlayer = (currentPlayer+1)%2;
		}
		
	}

	/**
	 * Generate an event if the grid is modified by the model. The controller tells to all his view that the grid changed
	 */
	public void gridChanged() {
		for(GridView gv : views){
			gv.gridChanged();
		}
	}
	
	/**
	 * determine what are the square available for a movement
	 * 
	 * @param square initial position of the piece
	 * @return an array with all available squares 
	 */
	public int[] getMovable(int square) {
		return model.getMovable(square);		
	}
	
	public Player getPlayer(){
		return players[currentPlayer];
	}

	public void gameEnded() {
		for(GridView gv : views){
			gv.gameEnded();
		}		
	}
}

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
	private boolean played = false;

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
		int[] diagBG = model.diagBG(newSquare);
		if(model.canEat(newSquare, diagBG[1]))
			return false;
		int[] diagBD = model.diagBD(newSquare);
		if(model.canEat(newSquare, diagBD[1]))
			return false;
		int[] diagHG = model.diagHG(newSquare);
		if(model.canEat(newSquare, diagHG[1]))
			return false;
		int[] diagHD = model.diagHD(newSquare);
		if(model.canEat(newSquare, diagHD[1]))
			return false;
		if(model.canPromote(newSquare)){
			Log.i("i","Promote called "+newSquare+" "+model.getPiece(newSquare).getColor());
			model.promote(newSquare);
		}
		return true;
	}

	/**
	 * move a piece from a square to another if possible
	 * 
	 * @param select the selected piece
	 * @param newSquare the new square of the piece 
	 */
	public void movePiece(int select, int newSquare) {
		if(model.canEat(select, newSquare)){
			model.eatPiece(select, newSquare);
			played = true;
			if(ended(newSquare)){
				currentPlayer = (currentPlayer+1)%2;
				played = false;
			}
		} else if(model.canMove(select, newSquare) && !played){
			model.movePiece(select, newSquare);
			currentPlayer = (currentPlayer+1)%2;
			if(model.canPromote(newSquare)){
				Log.i("i","Promote called "+newSquare+" "+model.getPiece(newSquare).getColor());
				model.promote(newSquare);
			}
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
	public ArrayList<Integer> getMovable(int square) {
		return model.getMovable(square, played);		
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

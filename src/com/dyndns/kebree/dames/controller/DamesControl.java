package com.dyndns.kebree.dames.controller;

import java.util.ArrayList;

import com.dyndns.kebree.dames.model.Grid;
import com.dyndns.kebree.dames.model.Piece.Color;
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
	
	/**
	 * Constructor
	 * 
	 * @param model the model linked with the controller
	 */
	public DamesControl(Grid model){
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

	/**
	 * move a piece from a square to another if possible
	 * 
	 * @param select the selected piece
	 * @param newSquare the new square of the piece 
	 */
	public void movePiece(int select, int newSquare) {
		if(model.canMove(select, newSquare))
			model.movePiece(select, newSquare);
		
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
}

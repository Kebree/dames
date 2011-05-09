package com.dyndns.kebree.dames.model;

import java.util.ArrayList;

import android.util.Log;

import com.dyndns.kebree.dames.controller.DamesControl;
import com.dyndns.kebree.dames.model.Piece.Color;
import com.dyndns.kebree.dames.model.Piece.PieceType;

/**
 * Model part of the game. 
 * 
 * @author Kal
 *
 */
public class Grid {
	private Piece [] grid;
	private ArrayList<DamesControl> listControl;
	
	/**
	 * Default constructor. 
	 */
	public Grid(){
		listControl = new ArrayList<DamesControl>();
		grid = new Piece[50];
		for(int i=0; i<20; i++){
			grid[i] = new Piece(Color.black, PieceType.pawn);
		}
		for(int i=20; i<30; i++){
			grid[i] = new Piece();
		}
		for(int i=49; i>=30; i--){
			grid[i] = new Piece(Color.white, PieceType.pawn);
		}
	}
	
	/**
	 * Generate an event to notify to all controllers that the model changed
	 */
	public void gridChanged(){
		for(DamesControl dc : listControl){
			dc.gridChanged();
		}
	}
	
	/**
	 * Link a controller to the model
	 * 
	 * @param dc controller that will be notified when model will change
	 */
	public void addController(DamesControl dc){
		listControl.add(dc);
	}
	
	/**
	 * 
	 * @param square the square id where is located the piece
	 * @return the piece at this location
	 */
	public Piece getPiece(int square){
		return grid[square];
	}
	
	/**
	 * 
	 * @return the complete grid at this instant
	 */
	public Piece[] getGrid(){
		return grid;
	}
	
	/**
	 * 
	 * @param square the square 
	 * @return if the square is located on a add or even line
	 */
	private boolean isLPaire(int square){
		if(((square%10)>5)||(square%10)==0){
			Log.i("i",square+"Pair");
			return true;
		}
		Log.i("i",square+"ImPair");
		return false;
	}

	/**
	 * 
	 * @param selectedSquare the square where the piece is at the moment
	 * @param newSquare the square where the piece will be located
	 * @return if the piece cando the move
	 */
	public boolean canMove(int selectedSquare, int newSquare) {
		selectedSquare++;
		newSquare++;
		int res = selectedSquare%10;
		Log.i("i","selected : "+selectedSquare+", Target : "+newSquare +", result : "+res);
		if(grid[newSquare-1].getColor() != Color.none){
			Log.i("i","False 1");
			return false;
		}
		if(grid[selectedSquare-1].getColor() == Color.black){
			if(!isLPaire(selectedSquare)){
				Log.i("i","ImPair");
				//odd lines (lignes impaires)
				if(!(newSquare == selectedSquare+5 || newSquare == selectedSquare+6)){
					// this is not a direct diagonal
					Log.i("i","False 2");
					return false;
				}
				if(selectedSquare % 10 == 5 && newSquare != selectedSquare+5){
					// border overflow
					return false;
				}
			}else{
				Log.i("i","Pair");
				//even lines (lignes paires)
				if(!(newSquare == selectedSquare+4 || newSquare == selectedSquare+5)){
					// this is not a direct diagonal
					Log.i("i","False 3");
					return false;
				}
				if(selectedSquare % 10 == 6 && newSquare != selectedSquare+5){
					// border overflow
					return false;
				}
			}
		} else {
			if(!isLPaire(selectedSquare)){
				Log.i("i","ImPair");
				//odd lines (lignes impaires)
				if(!(newSquare == selectedSquare-5 || newSquare == selectedSquare-4)){
					// this is not a direct diagonal
					Log.i("i","False 2");
					return false;
				}
				if(selectedSquare % 10 == 5 && newSquare != selectedSquare-5){
					// border overflow
					return false;
				}
			}else{
				Log.i("i","Pair");
				//even lines (lignes paires)
				if(!(newSquare == selectedSquare-6 || newSquare == selectedSquare-5)){
					// this is not a direct diagonal
					Log.i("i","False 3");
					return false;
				}
				if(selectedSquare % 10 == 6 && newSquare != selectedSquare-5){
					// border overflow
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Move a piece from a square to another. 
	 * @param oldSquare the square where the piece is at the moment
	 * @param newSquare the square where the piece will be located
	 */
	public void movePiece(int oldSquare, int newSquare) {
		grid[newSquare] = new Piece(grid[oldSquare].getColor(), grid[oldSquare].getPtype());
		grid[oldSquare] = new Piece();
		gridChanged();
	}

	/**
	 * 
	 * @param square the origin square
	 * @return all the square where the piece can move
	 */
	public int[] getMovable(int square) {
		int ret[] = new int[2];
		if(grid[square].getColor() == Color.black){
			if(isLPaire(square+1)){
				ret[0] = (canMove(square, square+5))?square+5:-1;
				ret[1] = (canMove(square, square+4))?square+4:-1;
			} else {
				ret[0] = (canMove(square, square+5))?square+5:-1;
				ret[1] = (canMove(square, square+6))?square+6:-1;
			}
		} else if(grid[square].getColor() == Color.white){
			if(isLPaire(square+1)){
				ret[0] = (canMove(square, square-5))?square-5:-1;
				ret[1] = (canMove(square, square-6))?square-6:-1;
			} else {
				ret[0] = (canMove(square, square-5))?square-5:-1;
				ret[1] = (canMove(square, square-4))?square-4:-1;
			}
		} else{
			ret[0]=-1;
			ret[1]=-1;			
		}
		return ret;		
	}

}

package com.dyndns.kebree.dames.model;

import java.util.ArrayList;
import java.util.HashMap;

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
	private HashMap<Color, Integer> pieces;

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
		pieces = new HashMap<Color, Integer>();
		pieces.put(Color.white, 20);
		pieces.put(Color.black, 20);
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
		if(((square%10)>4)){
			return true;
		}
		return false;
	}

	public int[] diagHG(int square){
		int[] ret = new int[8];
		for(int i=0; i<8; i++)
			ret[i] = -1;
		int i=0;
		diagHGRec(square, i, ret);
		return ret;
	}

	private void diagHGRec(int square, int i, int[] ret){
		if(i==8)
			return;
		if(square%10 == 5)
			return;
		if(square<0){
			ret[i-1]=-1;
			return;
		}
		if(isLPaire(square)){
			ret[i]=square-6;
			diagHGRec(square-6, ++i, ret);
		}else{
			ret[i]=square-5;
			diagHGRec(square-5, ++i, ret);
		}
	}

	public int[] diagBD(int square){
		int[] ret = new int[8];
		for(int i=0; i<8; i++)
			ret[i] = -1;
		int i=0;
		diagBDRec(square, i, ret);
		return ret;
	}

	private void diagBDRec(int square, int i, int[] ret){
		if(i==8)
			return;
		if(square%10 == 4)
			return;
		if(square>=50){
			ret[i-1]=-1;
			return;
		}
		if(isLPaire(square)){
			ret[i]=square+5;
			diagBDRec(square+5, ++i, ret);
		}else{
			ret[i]=square+6;
			diagBDRec(square+6, ++i, ret);
		}
	}

	public int[] diagHD(int square){
		int[] ret = new int[10];
		for(int i=0; i<10; i++)
			ret[i] = -1;
		int i=0;
		diagHDRec(square, i, ret);
		return ret;
	}

	private void diagHDRec(int square, int i, int[] ret){
		if(i==10)
			return;
		if(square%10 == 4)
			return;
		if(square<0){
			ret[i-1]=-1;
			if(i>3)
				if(ret[i-3]==4)
					ret[i-2]=-1;
			return;
		}
		if(isLPaire(square)){
			ret[i]=square-5;
			diagHDRec(square-5, ++i, ret);
		}else{
			ret[i]=square-4;
			diagHDRec(square-4, ++i, ret);
		}
	}

	public int[] diagBG(int square){
		int[] ret = new int[10];
		for(int i=0; i<10; i++)
			ret[i] = -1;
		int i=0;
		diagBGRec(square, i, ret);
		return ret;
	}

	private void diagBGRec(int square, int i, int[] ret){
		if(i==10)
			return;
		if(square%10 == 5)
			return;
		if(square>=50){
			ret[i-1]=-1;
			if(i>3)
				if(ret[i-3]==45)
					ret[i-2]=-1;
			return;
		}
		if(isLPaire(square)){
			ret[i]=square+4;
			diagBGRec(square+4, ++i, ret);
		}else{
			ret[i]=square+5;
			diagBGRec(square+5, ++i, ret);
		}
	}

	private boolean isIn(int value, int[] tab){
		for(int v : tab){
			if(value==v)
				return true;
		}
		return false;
	}

	private int[] getDiag(int initSquare, int square){
		int[] diag = diagHG(initSquare);
		if(isIn(square, diag))
			return diag;

		diag = diagBG(initSquare);
		if(isIn(square, diag))
			return diag;

		diag = diagBD(initSquare);
		if(isIn(square, diag))
			return diag;

		diag = diagHD(initSquare);
		if(isIn(square, diag))
			return diag;

		return null;		
	}


	public boolean canMove(int oldSquare, int newSquare){
		if(canMovePiece(oldSquare, newSquare))
			return true;
		return false;
	}

	public boolean canEat(int oldSquare, int newSquare){
		if(canEatPiece(oldSquare, newSquare))
			return true;
		return false;
	}

	/**
	 * 
	 * @param oldSquare the square where the piece is at the moment
	 * @param newSquare the square where the piece will be located
	 * @return if the piece cando the move
	 */
	public boolean canMovePiece(int oldSquare, int newSquare) {
		int[] diag = getDiag(oldSquare, newSquare);
		if(diag == null)
			return false;
		if(diag[0] == -1 || grid[diag[0]].getColor() != Color.none)
			return false;
		if(newSquare != diag[0])
			return false;

		return true;
	}

	public boolean canEatPiece(int oldSquare, int newSquare){		
		int[] diag = getDiag(oldSquare, newSquare);
		if(diag == null)
			return false;

		if(diag[0] == -1 || diag[1] == -1)
			return false;

		if(grid[diag[0]].getColor() == Color.none)
			return false;

		if(grid[oldSquare].getColor() == grid[diag[0]].getColor())
			return false;

		if(grid[diag[1]].getColor() != Color.none)
			return false;

		if(newSquare != diag[1])
			return false;


		return true;
	}

	/**
	 * Move a piece from a square to another. 
	 * @param oldSquare the square where the piece is at the moment
	 * @param newSquare the square where the piece will be located
	 */
	public void movePiece(int oldSquare, int newSquare) {
		Color color = grid[oldSquare].getColor();
		grid[newSquare] = new Piece(grid[oldSquare].getColor(), grid[oldSquare].getPtype());
		grid[oldSquare] = new Piece();
		gridChanged();
	}

	/**
	 * 
	 * @param square the origin square
	 * @return all the square where the piece can move
	 */
	public ArrayList<Integer> getMovable(int square) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		int[] diagHG = diagHG(square);
		int[] diagHD = diagHD(square);
		int[] diagBG = diagBG(square);
		int[] diagBD = diagBD(square);
		if(canEat(square, diagHG[1]))
			ret.add(diagHG[1]);
		if(canEat(square, diagHD[1]))
			ret.add(diagHD[1]);
		if(canEat(square, diagBG[1]))
			ret.add(diagBG[1]);
		if(canEat(square, diagBD[1]))
			ret.add(diagBD[1]);
		if(!ret.isEmpty())
			return ret;
		if(grid[square].getColor() == Color.black){
			if(canMove(square, diagBG[0]))
				ret.add(diagBG[0]);
			if(canMove(square, diagBD[0]))
				ret.add(diagBD[0]);
		} else if(grid[square].getColor() == Color.white){
			if(canMove(square, diagHG[0]))
				ret.add(diagHG[0]);
			if(canMove(square, diagHD[0]))
				ret.add(diagHD[0]);
		}
		
		return ret;		
	}

	public void eatPiece(int select, int newSquare) {
		Color color = grid[select].getColor();
		int[] diag = diagBG(select);
		if(isIn(newSquare, diag))
			grid[diag[0]]=new Piece();
		else {
			diag = diagBD(select);
			if(isIn(newSquare, diag))
				grid[diag[0]]=new Piece();
			else {
				diag = diagHG(select);
				if(isIn(newSquare, diag))
					grid[diag[0]]=new Piece();
				else{
					diag = diagHD(select);
				}
			}
		}

		grid[diag[0]]=new Piece();
		if(color != Color.none){
			int nbPieces = pieces.get(color);
			nbPieces--;
			if (nbPieces == 0){
				for(DamesControl dc : listControl){
					dc.gameEnded();
				}
			}
			pieces.put(color, nbPieces);
		}


		if(isIn(newSquare, diag))
			grid[diag[0]]=new Piece();

		//grid[diag[0]] = new Piece();
		PieceType type = grid[select].getPtype();
		grid[select] = new Piece();
		grid[newSquare] = new Piece(color, type);	
		gridChanged();	

	}

}

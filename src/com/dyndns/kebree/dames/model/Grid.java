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
	 * @param square the square 
	 * @return if the square is located on a add or even line
	 */
	private boolean isLPaire(int square){
		if(((square%10)>5)||(square%10)==0){
			return true;
		}
		return false;
	}


	/**
	 * 
	 * @param oldSquare the square where the piece is at the moment
	 * @param newSquare the square where the piece will be located
	 * @return if the piece cando the move
	 */
	public boolean canMovePiece(int oldSquare, int newSquare) {
		if(newSquare>=0 && newSquare<50){
			oldSquare++;
			newSquare++;
			if(grid[newSquare-1].getColor() != Color.none){
				return false;
			}
			if(grid[oldSquare-1].getColor() == Color.black){
				if(!isLPaire(oldSquare)){
					//odd lines (lignes impaires)
					if(!(newSquare == oldSquare+5 || newSquare == oldSquare+6)){
						// this is not a direct diagonal
						return false;
					}
					if(oldSquare % 10 == 5 && newSquare != oldSquare+5){
						// border overflow
						return false;
					}
				}else{
					//even lines (lignes paires)
					if(!(newSquare == oldSquare+4 || newSquare == oldSquare+5)){
						// this is not a direct diagonal
						return false;
					}
					if(oldSquare % 10 == 6 && newSquare != oldSquare+5){
						// border overflow
						return false;
					}
				}
			} else {
				if(!isLPaire(oldSquare)){
					//odd lines (lignes impaires)
					if(!(newSquare == oldSquare-5 || newSquare == oldSquare-4)){
						// this is not a direct diagonal
						return false;
					}
					if(oldSquare % 10 == 5 && newSquare != oldSquare-5){
						// border overflow
						return false;
					}
				}else{
					//even lines (lignes paires)
					if(!(newSquare == oldSquare-6 || newSquare == oldSquare-5)){
						// this is not a direct diagonal
						return false;
					}
					if(oldSquare % 10 == 6 && newSquare != oldSquare-5){
						// border overflow
						return false;
					}
				}
			}
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
		if(square<0){
			ret[i-1]=-1;
			return;
		}
		if(isLPaire(square+1)){
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
		if(square>=50){
			ret[i-1]=-1;
			return;
		}
		if(isLPaire(square+1)){
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
		if(square<0){
			ret[i-1]=-1;
			if(i>3)
				if(ret[i-3]==4)
					ret[i-2]=-1;
			return;
		}
		if(isLPaire(square+1)){
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
		if(square>=50){
			ret[i-1]=-1;
			if(i>3)
				if(ret[i-3]==45)
					ret[i-2]=-1;
			return;
		}
		if(isLPaire(square+1)){
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

	public boolean canEatPiece(int oldSquare, int newSquare){
		if(newSquare >= 0 && newSquare < 50){
			if(grid[newSquare].getColor() != Color.none){
				return false;
			}
			newSquare++;
			oldSquare++;
			if(grid[oldSquare-1].getColor()==Color.black){
				int[] diag = diagBG(oldSquare-1);
				if(!isIn(newSquare-1, diag))
					diag=diagBD(oldSquare-1);		
				if(grid[diag[0]].getColor() != Color.white){
					return false;
				}
				if(newSquare != diag[1]+1)
					//lateral move
					return false;
				if(isLPaire(oldSquare)){
					// left grid edge 
					if(oldSquare%10 == 6 && newSquare%10==5)
						return false;
					// right grid edge 
					if(oldSquare%10==0 && newSquare%10 == 1)
						return false;
					// free arrival square
					if(grid[newSquare-1].getColor() != Color.none)
						return false;
				}
				else {
					// ligne impaire (odd line)
					// left grid edge 
					if(oldSquare%10 == 1 && newSquare%10==0)
						return false;
					// right grid edge 
					if(oldSquare%10==5 && newSquare%10 == 6)
						return false;
					// free arrival square
					if(grid[newSquare-1].getColor() != Color.none)
						return false;
					//OK
				}		
			}
			else if(grid[oldSquare-1].getColor()==Color.white){
				int[] diag = diagHG(oldSquare-1);
				if(!isIn(newSquare-1, diag))
					diag=diagHD(oldSquare-1);		
				if(grid[diag[0]].getColor() != Color.black) {
					return false;
				}
				if(newSquare != diag[1]+1)
					//lateral move
					return false;

				if(isLPaire(oldSquare)){
					// left grid edge 
					if(oldSquare%10 == 6 && newSquare%10==5)
						return false;
					// right grid edge 
					if(oldSquare%10==0 && newSquare%10 == 1)
						return false;
					// free arrival square
					if(grid[newSquare-1].getColor() != Color.none)
						return false;
				}
				else {
					// ligne impaire (odd line)
					// left grid edge 
					if(oldSquare%10 == 1 && newSquare%10==0)
						return false;
					// right grid edge 
					if(oldSquare%10==5 && newSquare%10 == 6)
						return false;
					// free arrival square
					if(grid[newSquare-1].getColor() != Color.none)
						return false;
					//OK
				}		
			}

			return true;
		}
		return false;
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
	public int[] getMovable(int square) {
		int ret[] = new int[2];
		if(grid[square].getColor() == Color.black){
			if(isLPaire(square+1)){
				ret[0] = (canMove(square, square+5))?square+5:-1;
				ret[1] = (canMove(square, square+4))?square+4:-1;
				if(square < 45){
					if(ret[0] == -1 && grid[square+5].getColor() == Color.white){
						ret[0] = (canEat(square, square+11))?square+11:-1;
					}
					if(ret[1] == -1 && grid[square+4].getColor() == Color.white){
						ret[1] = (canEat(square, square+9))?square+9:-1;
					}
				}
			} else {
				ret[0] = (canMove(square, square+5))?square+5:-1;
				ret[1] = (canMove(square, square+6))?square+6:-1;
				if(square < 45){
					if(ret[0] == -1 && grid[square+5].getColor() == Color.white){
						ret[0] = (canEat(square, square+9))?square+9:-1;
					}
					if(ret[1] == -1 && grid[square+6].getColor() == Color.white){
						ret[1] = (canEat(square, square+11))?square+11:-1;
					}
				}
			}	
		} else if(grid[square].getColor() == Color.white){
			if(isLPaire(square+1)){
				ret[0] = (canMove(square, square-5))?square-5:-1;
				ret[1] = (canMove(square, square-6))?square-6:-1;
				if(square > 5){
					if(ret[0] == -1 && grid[square-5].getColor() == Color.black){
						ret[0] = (canEat(square, square-9))?square-9:-1;
					}
					if(ret[1] == -1 && grid[square-6].getColor() == Color.black){
						ret[1] = (canEat(square, square-11))?square-11:-1;
					}
				}
			} else {
				ret[0] = (canMove(square, square-5))?square-5:-1;
				ret[1] = (canMove(square, square-4))?square-4:-1;
				if(square > 5){
					if(ret[0] == -1 && grid[square-5].getColor() == Color.black){
						ret[0] = (canEat(square, square-11))?square-11:-1;
					}
					if(ret[1] == -1 && grid[square-4].getColor() == Color.black){
						ret[1] = (canEat(square, square-9))?square-9:-1;
					}
				}
			}
		} else{
			ret[0]=-1;
			ret[1]=-1;			
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

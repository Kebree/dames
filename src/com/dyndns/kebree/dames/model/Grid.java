package com.dyndns.kebree.dames.model;

import java.util.ArrayList;

import android.util.Log;

import com.dyndns.kebree.dames.controller.DamesControl;
import com.dyndns.kebree.dames.model.Piece.Color;
import com.dyndns.kebree.dames.model.Piece.PieceType;

public class Grid {
	private Piece [] grid;
	private ArrayList<DamesControl> listControl;
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
	
	public void gridChanged(){
		for(DamesControl dc : listControl){
			dc.gridChanged();
		}
	}
	
	public void addController(DamesControl dc){
		listControl.add(dc);
	}
	
	public Piece getPiece(int tail){
		return grid[tail];
	}
	
	public Piece[] getGrid(){
		return grid;
	}
	
	private boolean isLPaire(int tail){
		return (tail%10>5)?true:false;
	}

	public boolean canMove(int select, int idTail) {
		Log.i("i","selected : "+select+", Target : "+idTail);
		if(grid[idTail].getColor() != Color.none)
			return false;
		if(grid[select].getColor() == Color.black){
			if(!isLPaire(select)){
				if(idTail != select+5 || idTail != select+6)
					return false;
				//if(select % 10 == 5 && idTail == select+6)
				//	return false;
			}else{
				if(idTail != select+4 || idTail != select+5)
					return false;
				//if(select % 10 == 5 && idTail == select+4)
				//	return false;
			}
		}
		return true;
	}

	public void movePiece(int select, int idTail) {
		grid[idTail] = new Piece(grid[select].getColor(), grid[select].getPtype());
		grid[select] = new Piece();
		gridChanged();
	}

	public int[] getMovable(int idTail) {
		int ret[] = new int[2];
		if(grid[idTail].getColor() == Color.black){
			ret[0] = (canMove(idTail, idTail+5))?idTail+5:-1;
			ret[1] = (canMove(idTail, idTail+6))?idTail+6:-1;
		} else if(grid[idTail].getColor() == Color.white){
			ret[0] = (canMove(idTail, idTail-5))?idTail-5:-1;
			ret[1] = (canMove(idTail, idTail-6))?idTail-6:-1;
		} else{
			ret[0]=-1;
			ret[1]=-1;			
		}
		return ret;
		
	}

}

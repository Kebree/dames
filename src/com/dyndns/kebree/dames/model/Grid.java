package com.dyndns.kebree.dames.model;

import java.util.ArrayList;

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

	public boolean canMove(int select, int idTail) {
		// TODO Auto-generated method stub
		return true;
	}

	public void movePiece(int select, int idTail) {
		grid[idTail] = new Piece(grid[select].getColor(), grid[select].getPtype());
		grid[select] = new Piece();
		gridChanged();
	}

}

package com.dyndns.kebree.dames.controller;

import java.util.ArrayList;

import com.dyndns.kebree.dames.model.Grid;
import com.dyndns.kebree.dames.model.Piece;
import com.dyndns.kebree.dames.model.Piece.Color;
import com.dyndns.kebree.dames.view.GridView;

public class DamesControl {
	private ArrayList<GridView> views = new ArrayList<GridView>();
	private Grid model;
	
	
	public DamesControl(Grid model){
		this.model=model;
	}
	public void addView(GridView v){
		views.add(v);
	}

	public boolean selectable(int idTail, Color color) {
		if(model.getPiece(idTail).getColor() == color){
			return true;
		}
		return false;
	}

	public void movePiece(int select, int idTail) {
		if(model.canMove(select, idTail))
			model.movePiece(select, idTail);
		
	}

	public void gridChanged() {
		for(GridView gv : views){
			gv.gridChanged();
		}
	}
	public int[] getMovable(int idTail) {
		return model.getMovable(idTail);		
	}
}

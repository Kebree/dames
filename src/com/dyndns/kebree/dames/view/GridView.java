package com.dyndns.kebree.dames.view;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dyndns.kebree.dames.R;
import com.dyndns.kebree.dames.controller.DamesControl;
import com.dyndns.kebree.dames.model.Grid;
import com.dyndns.kebree.dames.model.Piece;

public class GridView extends View{

	private Activity act;
	private DamesControl dcontrol;
	private Grid model;
	ArrayList<Integer> colored;
	private int select=-1;
	Button [] tabBut = new Button[50];

	public GridView(Activity act, DamesControl dc, Grid model) {
		super(act);
		this.act=act;
		this.model = model;
		dcontrol = dc;
	}

	public View init(){
		TableLayout tlayout = new TableLayout(act);
		tlayout.setBackgroundColor(Color.WHITE);		
		for(int i=0; i<50; i++){
			tabBut[i] = new Button(act); 
			tabBut[i].setHeight(32);
			tabBut[i].setWidth(32);
			tabBut[i].setBackgroundColor(Color.BLACK);
			tabBut[i].setId(i);
			if(dcontrol.selectable(i, com.dyndns.kebree.dames.model.Piece.Color.black))
				tabBut[i].setBackgroundResource(R.drawable.black);
			else if (dcontrol.selectable(i, com.dyndns.kebree.dames.model.Piece.Color.white))
				tabBut[i].setBackgroundResource(R.drawable.white);
			OnClickListener butClicked = new OnClickListener() {
				public void onClick(View v) {

					int idTail = v.getId();
					if(dcontrol.selectable(idTail, dcontrol.getPlayer().getColor())){
						// a piece that belongs to the player
						if(select == idTail){
							if(dcontrol.getPlayer().getColor() == com.dyndns.kebree.dames.model.Piece.Color.white)
								tabBut[select].setBackgroundResource(R.drawable.white);
							else 
								tabBut[select].setBackgroundResource(R.drawable.black);
							select = -1;
						}else{
							if(select != -1){
								//redraw
								if(dcontrol.selectable(select, com.dyndns.kebree.dames.model.Piece.Color.white))
									tabBut[select].setBackgroundResource(R.drawable.white);
								else if(dcontrol.selectable(select, com.dyndns.kebree.dames.model.Piece.Color.black))
									tabBut[select].setBackgroundResource(R.drawable.black);
							}
							v.setBackgroundColor(Color.RED);
							select=idTail;
							colored = dcontrol.getMovable(idTail);
							for(int value : colored){
								tabBut[value].setBackgroundColor(Color.YELLOW);
							}
						}
					} else if(dcontrol.selectable(idTail,com.dyndns.kebree.dames.model.Piece.Color.none) && select != -1){
						dcontrol.movePiece(select,idTail);
						select = -1;
					}

				}
			};
			tabBut[i].setOnClickListener(butClicked);
		}

		for(int i=0; i<10; i++){
			TableRow rowi = new TableRow(act);
			for(int j=0; j<10; j++){
				if((estPair(i) && !estPair(j)) || (!estPair(i) && estPair(j))){
					rowi.addView(tabBut[i*5+(j/2)]); 
				}
				else{
					TextView t = new TextView(act);
					rowi.addView(t);
				}
			}

			tlayout.addView(rowi);
		}
		return tlayout;
	}

	private boolean estPair(int i){
		return (i%2==0)?true:false;
	}

	public void gridChanged() {
		Piece[] grid = model.getGrid();
		for(int i=0; i<grid.length; i++){
			if(grid[i].getColor()==com.dyndns.kebree.dames.model.Piece.Color.black)
				tabBut[i].setBackgroundResource(R.drawable.black);
			else if(grid[i].getColor()==com.dyndns.kebree.dames.model.Piece.Color.white)
				tabBut[i].setBackgroundResource(R.drawable.white);
			else 
				tabBut[i].setBackgroundColor(Color.BLACK);
		}

	}

	public void gameEnded() {
		TextView t = new TextView(act);
		t.setText("Partie Terminée");
		act.setContentView(t);

	}
}

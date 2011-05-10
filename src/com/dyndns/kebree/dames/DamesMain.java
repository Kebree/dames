package com.dyndns.kebree.dames;

import android.app.Activity;
import android.os.Bundle;

import com.dyndns.kebree.dames.controller.DamesControl;
import com.dyndns.kebree.dames.model.Grid;
import com.dyndns.kebree.dames.model.Player;
import com.dyndns.kebree.dames.model.Piece.Color;
import com.dyndns.kebree.dames.view.GridView;

/**
 * Main classe. Create the main objects. May be modifiable
 * 
 * @author Kal
 *
 */
public class DamesMain extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        Grid model = new Grid();
        Player player1 = new Player(Color.white);
        Player player2 = new Player(Color.black);
        DamesControl dc = new DamesControl(model, player1, player2);
        GridView gv = new GridView(this, dc, model);
        model.addController(dc);
        dc.addView(gv);
        setContentView(gv.init());
    }
}
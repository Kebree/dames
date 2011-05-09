package com.dyndns.kebree.dames;

import android.app.Activity;
import android.os.Bundle;

import com.dyndns.kebree.dames.controller.DamesControl;
import com.dyndns.kebree.dames.model.Grid;
import com.dyndns.kebree.dames.model.Piece.Color;
import com.dyndns.kebree.dames.view.GridView;
import com.dyndns.kebree.dames.view.Player;

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
        DamesControl dc = new DamesControl(model);
        Player player = new Player(Color.white);
        GridView gv = new GridView(this, dc, player, model);
        model.addController(dc);
        dc.addView(gv);
        setContentView(gv.init());
    }
}
/**
 * @author zyang36
 * @date 07/31/2013
 */
package com.example.searchablenotepad;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class handwriting extends Activity {
	
	private WriteView view;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.handwriting);
	    view = (WriteView)findViewById(R.id.writeview);
	    view.requestFocus();

	    Button clear = (Button)findViewById(R.id.clear);
	    Button undo = (Button)findViewById(R.id.writepad_undo);
	    Button redo = (Button)findViewById(R.id.writepad_redo);
	    
	    clear.setOnClickListener(
	    		new OnClickListener() {

					@Override
					public void onClick(View v) {
						view.clear();
						Toast.makeText(getApplicationContext(), "Clear", Toast.LENGTH_SHORT).show();
					}
				});
	    view.activeUndo(undo);
	    view.activeRedo(redo);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.writepad_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		return true;
	}
}

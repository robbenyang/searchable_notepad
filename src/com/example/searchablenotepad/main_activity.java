/**
 * @author zyang36
 * @date 07/31/2013
 */
package com.example.searchablenotepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class main_activity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
//		ListView sv = new ListView(this);
//		LinearLayout ll = new LinearLayout(this);
//
//		Button b = new Button(this);
//		b.setText("Create");
//		b.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				goToDrawpad(this);
//				System.exit(0);
//			}
//		});
//		ll.addView(b);
//		EditText et = new EditText(this);
//		et.setHint("test for LinearLayout");
//		ll.addView(et);
//		this.setContentView(ll);
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.create_note:
			goToDrawpad();
			break;
		default:
			break;
		}
		return true;
	}
	
	//Function to go to creating activity
	public void goToDrawpad(){
		Intent intent = new Intent(this, handwriting.class);
		startActivity(intent);
	}

}

/**
 * @author zyang36
 * @date 07/31/2013
 */
package com.example.searchablenotepad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
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
		switch (item.getItemId()) {
		case R.id.Save:
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			saveToFile(timeStamp);
			break;
			
		case R.id.Back:
			backDialog();
			break;

		default:
			break;
		}
		return true;
	}
	
	private Dialog backDialog(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("Confirm Going Back?");
		builder.setTitle("Alert");
		
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}
		});
		
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
		return builder.create();
		
		
	}
	
	public void saveToFile(String file_name){
		OutputStream fout = null;
		File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"snote/" + file_name + ".png");
		f.getParentFile().mkdirs();
		try{
			f.createNewFile();
		}catch(Exception e){
			
		}
		try{
			fout = new FileOutputStream(f);
		}catch(Exception e){
			
		}
		
		view.getDrawingCache().compress(Bitmap.CompressFormat.PNG,90,fout);		
		try{
			fout.flush();
			fout.close();
		}catch(IOException e){
			
		}
		Toast.makeText(getApplicationContext(), "Saved " + file_name, Toast.LENGTH_SHORT).show();
		
	}
}

/**
 * @author zyang36
 * @date 07/31/2013
 */
package com.example.searchablenotepad;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class main_activity extends FragmentActivity {

	SimpleCursorAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_scrollview);
		LinearLayout ll = (LinearLayout) findViewById(R.id.main_linearlayout);
		List<File> files = getFiles();
		Collections.sort(files);
		
		for (int i = 0; i < files.size(); i++) {
			
			//List all files as textview, and click to see the picture with another app
			TextView tv = new TextView(this);
			tv.setText(files.get(i).getName());
			tv.setTextSize(30);
			LayoutParams p = new LayoutParams();
			p.width = LayoutParams.WRAP_CONTENT;
			tv.setLayoutParams(p);
			OnClickListener l = new ParamsClickListener(files.get(i).getAbsolutePath());
			tv.setOnClickListener(l);
			
			//Set up two pieces of white spaces so that the UI looks better
			View whiteSpace = new View(this);
			LayoutParams whiteParams = new LayoutParams();
			whiteParams.height = 10;
			whiteSpace.setLayoutParams(whiteParams);
			whiteSpace.setBackgroundColor(Color.TRANSPARENT);
			View whiteSpace1 = new View(this);
			whiteSpace1.setLayoutParams(whiteParams);
			whiteSpace1.setBackgroundColor(Color.TRANSPARENT);
			
			//add a divider
			View divider = new View(this);
			LayoutParams params = new LayoutParams();
			params.height=1;
			divider.setLayoutParams(params);
			divider.setBackgroundColor(Color.GRAY);
			
			//add all views to the activity page
			ll.addView(whiteSpace);
			ll.addView(tv);
			ll.addView(whiteSpace1);
			ll.addView(divider);
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.create_note:
			goToDrawpad();
			break;
		case R.id.search:
			goToSearchAct();
			break;
		case R.id.Exit:
			exitDialog();

		default:
			break;
		}
		return true;
	}

	private boolean exitDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("Confirm Exit?");
		builder.setTitle("Alert");

		builder.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.exit(0);
					}
				});

		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
		return true;
	}
	
	//Get all files in this app's folder
	private List<File> getFiles() {
		if(!isExternalAvailable()){
			Log.e("External Storage", "External Storage is not avaiable! Loading files failed");
			return new ArrayList<File>();
		}
		List<File> fileList = new ArrayList<File>();
		File path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File myDir = new File(path, "snote/");
		for (File myFile : myDir.listFiles()) {
			fileList.add(myFile);
			Log.i("add files", myFile.getName());
		}
		return fileList;
	}

	// Function to go to creating activity
	public void goToDrawpad() {
		Intent intent = new Intent(this, handwriting.class);
		startActivity(intent);
	}
	
	//Function to go to search activity
	public void goToSearchAct(){
		Intent intent = new Intent(this, Search.class);
		startActivity(intent);
	}

	// Check external storage availability
	public boolean isExternalAvailable() {
		boolean externalStorageAvailable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			externalStorageAvailable = true;
			Log.i("isExternalAvailable", "External Storage is Writable");
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			externalStorageAvailable = true;
			Log.i("isExternalAvailable", "External Storage is Readable");
		} else {
			externalStorageAvailable = false;
			Log.e("isExternalAvailable", "External Storage is not available");
		}
		return externalStorageAvailable;
	}
	
	/**
	 * Customized OnClickListener, which can accept a string as parameter, and then open the image with given name
	 *
	 */
	private class ParamsClickListener implements OnClickListener{
		private String filePath;
		
		public ParamsClickListener(String _filePath){
			filePath = _filePath;
		}
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://"+filePath), "image/*");
			startActivity(intent);
		}		
	}
}
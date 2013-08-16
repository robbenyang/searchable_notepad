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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class handwriting extends Activity {

	private WriteView view;
	private DatabaseAdapter db = new DatabaseAdapter(this);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.handwriting);
		view = (WriteView) findViewById(R.id.writeview);
		view.requestFocus();

		Button clear = (Button) findViewById(R.id.clear);
		Button undo = (Button) findViewById(R.id.writepad_undo);
		Button redo = (Button) findViewById(R.id.writepad_redo);

		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearDialog();
			}
		});
		view.activeUndo(undo);
		view.activeRedo(redo);
	}

	private boolean clearDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("Confirm Clear? All strokes will be deleted and can't be restored");
		builder.setTitle("Alert");

		builder.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						view.clear();
						Toast.makeText(getApplicationContext(), "Cleared",
								Toast.LENGTH_SHORT).show();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.writepad_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.Save:
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(Calendar.getInstance().getTime());
			saveToFile(timeStamp);
			recognize();
			break;

		case R.id.Back:
			backDialog();
			break;

		default:
			break;
		}
		return true;
	}

	private void recognize() {
		// Try to merge the arrays of points into one array of integers, which
		// makes variable passing much easier.
		List<Integer> div = view.getDivision();
		List<Segment> seg = view.getSegments();
		List<Integer> segDiv = view.getSegDiv();
		// for(int i = 0;i<seg.size();i++){
		// Log.w("seg values from canvas",""+seg.get(i));
		// }
		Iterator<Integer> it = div.iterator();
		int[] division = new int[div.size()];
		/*
		 * for(int i = 0; i < segDiv.size();i++){ Log.w("segment Div",
		 * ""+segDiv.get(i)); }
		 */

		for (int i = 0; i < div.size(); i++) {
			division[i] = it.next().intValue();
			// Log.w("Div position","" + division[i]);
		}
		List<Integer> intList = new ArrayList<Integer>();
		int stroke = 0;
		int divCounter = 1;
		Log.w("segDiv size", "" + segDiv.size());
		for (int k = 0; k < seg.size(); k++) {
			List<Point> points = seg.get(k).points;
			for (int j = 0; j < points.size(); j++) {
				intList.add(stroke);
				intList.add((int) points.get(j).x);
				intList.add((int) points.get(j).y);
			}
			stroke++;
			if (divCounter < segDiv.size()) {
				if (stroke == segDiv.get(divCounter)) {
					stroke = 0;
					divCounter++;
				}
			}
		}
		Log.w("intList size", "" + intList.size());
		// for(int i = 0; i< 300 && i < intList.size();i++){
		// Log.w("values of intList", ""+intList.get(i));
		// }
		int[] data = new int[intList.size()];
		Iterator<Integer> iterator = intList.iterator();
		for (int i = 0; i < intList.size(); i++) {
			data[i] = iterator.next().intValue();
		}
		// for(int i = 0 ;i < data.length;i++){
		// Log.w("orginal data",""+data[i]);
		// }
		String[] result = OCR(data, division);
		if (result == null) {
			Log.e("No characters recognized", "Recognize program stops");
			return;
		}
		for (int i = 0; i < result.length; i++) {
			Log.w("String array messge", result[i]);
		}
		/**
		 * for(int i = 0; i < div.size()-1 ;i++){ List<Segment> temp =
		 * seg.subList(div.get(i), div.get(i+1)); List<Integer> intList = new
		 * ArrayList<Integer>(); for(int k = 0; k < temp.size();k++){
		 * List<Point> points = temp.get(k).points; for(int j = 0; j <
		 * points.size();j++){ intList.add(k);
		 * intList.add((int)points.get(j).x); intList.add((int)points.get(j).y);
		 * } } int[] data = new int[intList.size()]; Iterator<Integer> iterator
		 * = intList.iterator(); for(int j = 0; j < intList.size();j++){ data[j]
		 * = iterator.next().intValue(); } String s = OCR(data); ret.add(s); }
		 */
	}

	static {
		System.loadLibrary("CharRecognizer");
	}

	private native String[] OCR(int[] data, int[] division);

	private boolean backDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("Confirm Going Back?");
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

	public void saveToFile(String file_name) {
		OutputStream fout = null;
		File f = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"snote/" + file_name + ".png");
		f.getParentFile().mkdirs();
		try {
			f.createNewFile();
		} catch (Exception e) {

		}
		try {
			fout = new FileOutputStream(f);
		} catch (Exception e) {

		}

		view.getDrawingCache().compress(Bitmap.CompressFormat.PNG, 100, fout);
		try {
			fout.flush();
			fout.close();
		} catch (IOException e) {

		}
		Toast.makeText(getApplicationContext(), "Saved " + file_name,
				Toast.LENGTH_SHORT).show();

	}
}

/**
 * @author zyang36
 * @date 07/31/2013
 */
package com.example.searchablenotepad;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class WriteView extends View {

	private Canvas c = new Canvas();

	OnTouchListener draw = new WriteListener();
	// OnTouchListener erase = new EraseListener();

	private Paint paint;
	List<Segment> segments = new ArrayList<Segment>();
	Stack<Segment> main = new Stack<Segment>();

	Segment eraser = new Segment();
	Button undo = null;
	Button redo = null;

	public WriteView(Context context) {
		super(context);
		init();
	}

	public WriteView(Context context, AttributeSet att) {
		super(context, att);
		init();
	}

	private void init() {
		c.drawColor(Color.WHITE);
		setDrawingCacheEnabled(true);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setOnTouchListener(draw);

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setStrokeWidth(4);
	}

	// Override onDraw function. Implementation of writing on the screen
	@Override
	public void onDraw(Canvas c) {
		for (Segment segment : segments) {
			List<Point> points = segment.points;
			for (int i = 0; i < points.size() - 1;) {
				Point s = points.get(i);
				Point e = points.get(++i);
				c.drawLine(s.x, s.y, e.x, e.y, paint);
			}
		}

		if (undo != null) {
			undo.setEnabled(segments.size() > 0);
		}
		
		if(redo != null){
			redo.setEnabled(!main.empty());
		}
	}

	
	public boolean activeUndo(Button _undo){
		undo = _undo;
		undo.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						undo();
					}
				});
		return true;
	}
	
	public void undo(){
		if(segments.size() > 0){
			try {
				Segment temp = segments.remove(segments.size()-1);
				main.push(temp);
			}catch(NullPointerException e){
				System.out.println("Null Pointer!");
			}
		}
		invalidate();
	}
	
	public boolean activeRedo(Button _redo){
		redo = _redo;
		redo.setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						redo();
					}
				});
		return true;
	}
	
	public void redo(){
		if(!main.empty()){
			try{
				Segment temp = main.pop();
				segments.add(temp);
			}catch(NullPointerException e){
				System.out.println("Null Pointer");
			}
		}
		invalidate();
	}

	public void clear() {
		segments = new ArrayList<Segment>();
		c.drawColor(Color.WHITE);
		invalidate();
	}

	private class WriteListener implements OnTouchListener {

		public boolean onTouch(View view, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (segments.size() == 0
						|| segments.get(segments.size() - 1).points.size() != 0) {
					segments.add(new Segment());
					main.push(segments.get(segments.size() - 1));
				}
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				Point point = new Point(event.getX(), event.getY());
				segments.get(segments.size() - 1).points.add(point);
				invalidate();
			}
			return true;
		}
	}
}

/**
 * Helper classes
 */
class Point {
	float x, y;

	public Point() {

	}

	public Point(float _x, float _y) {
		x = _x;
		y = _y;
	}

	// Override the default toString function to print the coordinates for
	// debugging
	@Override
	public String toString() {
		return "( " + x + "," + y + " )";
	}
}

class Segment {
	List<Point> points;

	public Segment() {
		points = new ArrayList<Point>();
	}

	// Override the default toString function to print a list of coordinates for
	// debugging
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (Point p : points) {
			buffer.append("\n");
			buffer.append(p);
		}
		return buffer.toString();
	}
}
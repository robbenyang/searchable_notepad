package com.example.searchablenotepad;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter{
	private Context c;
	
	public ImageAdapter(Context context){
		c = context;
	}
	
	public int getCount(){
		return 0;
	}
	
	public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    
    public View getView(int position, View convertView, ViewGroup parent){
    	ImageView imageView = (ImageView)convertView;
    	if(imageView == null){
    		imageView = new ImageView(c);
    		imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
    	}
        return imageView;
    }

}

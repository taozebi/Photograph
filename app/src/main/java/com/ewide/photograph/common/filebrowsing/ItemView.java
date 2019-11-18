package com.ewide.photograph.common.filebrowsing;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewide.photograph.R;

/**
 * 文件浏览器头部子View
 * @author Taozebi
 * @date 2016年10月12日
 */
public class ItemView extends LinearLayout	{
	
	private Context mContext;
	private LayoutInflater mInflater;
	
	private TextView pathName;
	private ImageView home;
	
	private String name = "";
	private boolean isRoot = false;
	private String path = "";

	public ItemView(Context context) {
		super(context);
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		initView();
	}
	
	private void initView(){
		setOrientation(LinearLayout.HORIZONTAL);
		View v = mInflater.inflate(R.layout.layout_item_view, null);
		pathName = (TextView)v.findViewById(R.id.pathName);
		home = (ImageView)v.findViewById(R.id.home);
		this.addView(v);
//		this.setOnClickListener(mOnClickListaner);
	}
	
	public void setHomeVisibility(int visiablity){
		home.setVisibility(visiablity);
	}
	
	public void setName(String name){
		if(!TextUtils.isEmpty(name)){
			this.name = name;
			pathName.setText(name);
		}
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setRoot(boolean isRoot){
		this.isRoot = isRoot;
	}
	
	public boolean isRoot(){
		return isRoot;
	}
}

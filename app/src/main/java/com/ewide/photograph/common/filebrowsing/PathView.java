package com.ewide.photograph.common.filebrowsing;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ewide.photograph.common.util.Utils;


/**
 * 文件浏览器头部View
 * @author Taozebi
 * @date 2016年10月12日
 */
public class PathView extends LinearLayout {
	
	private Context mContext;
	private String path = "";
	private String rootPath = "";
	private String currentPath = "/";
	
	private List<String> names = new ArrayList<String>();
	private List<String> paths = new ArrayList<String>();
	private ItemView rootView;
	private ItemView firstView;
	private ItemView secondView;
	private ItemView thirdView;
	
	private OnPathItemClickListener listener = null;
	
	public PathView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PathView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		rootPath = Utils.getSDPath();
		initPathView();
		init(rootPath);
	}

	private void initPathView() {
		setOrientation(LinearLayout.HORIZONTAL);
		rootView = new ItemView(mContext);
		rootView.setName("Home");
		rootView.setRoot(true);
		rootView.setPath(rootPath);
		rootView.setHomeVisibility(View.VISIBLE);
		firstView = new ItemView(mContext);
		firstView.setHomeVisibility(View.GONE);
		firstView.setRoot(false);
		secondView = new ItemView(mContext);
		secondView.setHomeVisibility(View.GONE);
		secondView.setRoot(false);
		thirdView = new ItemView(mContext);
		thirdView.setHomeVisibility(View.GONE);
		thirdView.setRoot(false);
		
		rootView.setClickable(true);
		firstView.setClickable(true);
		secondView.setClickable(true);
		thirdView.setClickable(true);
		
		rootView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dealWithClick(arg0);
			}
		});
		firstView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dealWithClick(arg0);
			}
		});
		secondView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dealWithClick(arg0);
			}
		});
		thirdView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dealWithClick(arg0);
			}
		});
		
		this.addView(rootView);
		this.addView(firstView);
		this.addView(secondView);
		this.addView(thirdView);
		firstView.setVisibility(View.GONE);
		secondView.setVisibility(View.GONE);
		thirdView.setVisibility(View.GONE);
	}

	public PathView(Context context, String path) {
		super(context);
		this.mContext = context;
		this.path = path;
		rootPath = Utils.getSDPath();
		initPathView();
		init(path);
	}
	
	public void init(String path) {
		if(TextUtils.isEmpty(path)){
			dealWithPath(rootPath, true);
		}else if((path+"/").equals(rootPath)){
			dealWithPath(path, true);
		}else{
			dealWithPath(path,false);
		}
		
	}

	public void dealWithPath(String path,boolean isRoot){
		names.clear();
		paths.clear();
		firstView.setVisibility(View.GONE);
		secondView.setVisibility(View.GONE);
		thirdView.setVisibility(View.GONE);
		if(!isRoot){
			addItemView(path);
		}
		
		if(names.size() == 1){
			firstView.setVisibility(View.VISIBLE);
			firstView.setName(names.get(0));
			firstView.setPath(paths.get(0));
		}else if(names.size() == 2){
			firstView.setVisibility(View.VISIBLE);
			secondView.setVisibility(View.VISIBLE);
			firstView.setName(names.get(0));
			firstView.setPath(paths.get(0));
			secondView.setName(names.get(1));
			secondView.setPath(paths.get(1));
		}else if(names.size() >= 3){
			firstView.setVisibility(View.VISIBLE);
			secondView.setVisibility(View.VISIBLE);
			thirdView.setVisibility(View.VISIBLE);
			
			firstView.setName(names.get(names.size()-3));
			firstView.setPath(paths.get(paths.size()-3));
			secondView.setName(names.get(names.size()-2));
			secondView.setPath(paths.get(paths.size()-2));
			thirdView.setName(names.get(names.size()-1));
			thirdView.setPath(paths.get(paths.size()-1));
		}
	}
	
	
	private void addItemView(String path){
		String[] childNames = path.split(rootPath);
		currentPath = rootPath;
		if(childNames.length < 1)return;
		String[] strs = childNames[1].split("/");
		for (int i = 0; i < strs.length; i++) {
			String name = strs[i];
			if(!TextUtils.isEmpty(name)){
				currentPath += name+"/";
				names.add(name);
				paths.add(currentPath);
			}
		}
	}
	
	private void dealWithClick(View v){
		ItemView view = (ItemView)v;
		if(listener != null){
//			dealWithPath(view.getPath(), view.isRoot());
			listener.onItemViewClick(view);
		}
	}
	
	public void setOnPathItemClickListener(OnPathItemClickListener onPathItemClickListener){
		this.listener = onPathItemClickListener;
	}
	
	public interface OnPathItemClickListener {
		public void onItemViewClick(ItemView view);
	}

}


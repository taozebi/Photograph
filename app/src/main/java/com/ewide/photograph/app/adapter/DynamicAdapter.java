package com.ewide.photograph.app.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ewide.photograph.R;


public class DynamicAdapter extends BaseAdapter {

	private Context context;
	private List<String> titleItems;
	private List<Map<String, Object>> listData;
	private int screenWidth = 0;

	public DynamicAdapter(Context context, List<Map<String, String>> listHeaderItems, List<Map<String, Object>> listData) {
		super();
		this.context = context;
		this.titleItems = new ArrayList<String>();
		for (int i = 0; i < listHeaderItems.size(); i++) {
			Map<String, String> itemMmap = listHeaderItems.get(i);
			for(Map.Entry<String, String> entry:itemMmap.entrySet()){
				String keyItem = entry.getKey(); 
				titleItems.add(keyItem);
			}
		}

		this.listData = listData;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
	}
	
	public DynamicAdapter(Context context, String[] listHeaderItems, List<Map<String, Object>> listData) {
		super();
		this.context = context;
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < listHeaderItems.length; i++) {
			items.add(listHeaderItems[i]);
		}
		this.titleItems = items;
		this.listData = listData;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return listData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int index, View arg1, ViewGroup arg2) {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(new ListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		layout.setGravity(Gravity.CENTER_VERTICAL);
		Map<String,Object> map = listData.get(index);
		for (int i = 0; i < titleItems.size(); i++) {
//			Map<String, String> itemMmap = titleItems.get(i);
//			for(Map.Entry<String, String> entry:itemMmap.entrySet()){    
////			     System.out.println(entry.getKey()+"--->"+ 
			     String keyItem = titleItems.get(i); 
			     TextView textView = new TextView(context);
			     textView.setPadding(0, 10, 0, 10);
//			     if(titleItems.size()*300 > screenWidth){
//			    	 textView.setLayoutParams(new LinearLayout.LayoutParams(300, LayoutParams.WRAP_CONTENT));
//			     }else{
			    	 textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
//			     }
			     textView.setTextSize(16);
			     textView.setSingleLine(true);
			     textView.setEllipsize(TruncateAt.END);
			     textView.setGravity(Gravity.CENTER);
			     textView.setText(map.get(keyItem)!=null?map.get(keyItem).toString():"");
			     
			     layout.addView(textView);
			     if(i != titleItems.size() -1){
			    	 LinearLayout line = new LinearLayout(context);
			    	 line.setLayoutParams(new LayoutParams( 1, LayoutParams.MATCH_PARENT));
			    	 line.setBackgroundColor(context.getResources().getColor(R.color.gray));
			    	 layout.addView(line);
			     }
//			}
		}
		return layout;
	}

}

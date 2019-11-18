package com.ewide.photograph.common.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;


import com.ewide.photograph.app.global.MRApplication;
import com.ewide.photograph.common.util.ProgressDialogUtil;
import com.ewide.photograph.common.util.T;
import com.ewide.photograph.common.view.TitleBar;

import butterknife.ButterKnife;

/**
 * CommonActivity
 * Created by Taoze on 2018/3/23.
 */
public abstract class CommonActivity extends BaseActivity {

	protected static final int SHOW_TOAST = 2000;

	/** 总布局. */
	protected RelativeLayout baseLayout = null;
	
	/** 标题栏布局. */
	protected TitleBar titleBar = null;
	
	/** 主内容布局. */
	protected RelativeLayout contentLayout = null;
	
	protected LayoutInflater mInflater;

	/** fragment 界面是否销毁*/
	private boolean active = true;


	private Handler mUiHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(isActive()){
				if(msg.what == SHOW_TOAST){
					ProgressDialogUtil.dismiss();
					String content = (String) msg.obj;
					T.showShort(getApplicationContext(),content);
				}else{
					onHandleMessage(msg);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		((MRApplication)getApplication()).addActivity(this);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			// 透明状态栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			// 透明导航栏
////			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//		}
		mInflater = LayoutInflater.from(this);
		
		baseLayout = new RelativeLayout(this);
		
		contentLayout = new RelativeLayout(this);
		
		titleBar = new TitleBar(this);

//		titleBar.setId(titleBarID);
//		titleBar.setFitsSystemWindows(true);
//		titleBar.setClipToPadding(false);
		titleBar.setOnBackBtnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(MRApplication.TAG, "CommonActivity back clicked");
				if(!onBack()){
					((MRApplication) getApplication()).getTopActivity().finish();
				}
			}
		});
		
		titleBar.setOnSubmitBtnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSubmit();
			}
		});
		
		RelativeLayout.LayoutParams titleLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		titleLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		baseLayout.addView(titleBar,titleLayoutParams);
		
		RelativeLayout.LayoutParams contentLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		contentLayoutParams.addRule(RelativeLayout.BELOW,titleBar.getId());
//		contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
		baseLayout.addView(contentLayout,contentLayoutParams);
		
		RelativeLayout.LayoutParams baseParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		setContentView(baseLayout,baseParams);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.active = false;
		((MRApplication)getApplication()).removeActivity(this);
	}

	private boolean isActive(){
		return active;
	}

	protected void onSubmit() {
		
	}
	
	/**
	 * 返回键按下时回调，如返回false，则会自动调用finish方法。
	 * @return
	 */
	protected boolean onBack(){
		Log.d(MRApplication.TAG,"CommonActivity --> onBack()");
		return false;
	}

	/**
	 * 描述：用指定的View填充主界面.
	 * @param contentView  指定的View
	 */
	@Override
	public void setContentView(View contentView) {
		contentLayout.removeAllViews();
		contentLayout.addView(contentView,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//注解控件
		ButterKnife.bind(this);
		onInitData();
	}
	
	/**
	 * 描述：用指定资源ID表示的View填充主界面.
	 * @param resId  指定的View的资源ID
	 */
	@Override
	public void setContentView(int resId) {
		setContentView(mInflater.inflate(resId, null));
	}

	/**
	 * 子类重写handleMessage
	 * @param msg
	 */
	protected void onHandleMessage(Message msg){

	}

	protected void sendToastMessage(String obj){
		Message msg = mUiHandler.obtainMessage();
		msg.what = SHOW_TOAST;
		msg.obj = obj;
		mUiHandler.sendMessage(msg);
	}

	protected void sendMessage(int what, Object obj){
		Message msg = mUiHandler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		mUiHandler.sendMessage(msg);
	}

	protected void sendMessage(int what){
		mUiHandler.sendEmptyMessage(what);
	}

	protected void sendMessageDelayed(int what, long delayMillis){
		mUiHandler.sendEmptyMessageDelayed(what,delayMillis);
	}
	protected void sendMessageDelayed(int what, Object obj, long delayMillis){
		Message msg = mUiHandler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		mUiHandler.sendMessageDelayed(msg,delayMillis);
	}

	/**
	 * 设置是否显示标题
	 * @param visible  是否可见
	 */
	protected void setTitleBarVisible(boolean visible) {
		if(titleBar != null){
			titleBar.setVisibility(visible? View.VISIBLE: View.GONE);
		}
	}
	
	protected void addActionView(int resId){
		if(titleBar != null){
			titleBar.addActionView(mInflater.inflate(resId, null));
		}
	}
	
	protected void addActionView(View view){
		if(titleBar != null){
			titleBar.addActionView(view);
		}
	}

	public void showContent(Class<? extends BaseFragment> clazz){}

	/**
	 * 初始化数据
	 */
	public abstract void onInitData();
}


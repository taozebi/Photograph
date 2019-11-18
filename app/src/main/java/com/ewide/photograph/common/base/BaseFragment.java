package com.ewide.photograph.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ewide.photograph.app.global.MRApplication;
import com.ewide.photograph.common.log.LogUtils;
import com.ewide.photograph.common.util.ProgressDialogUtil;
import com.ewide.photograph.common.view.TitleBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *  BaseFragment
 *  Created by Taoze on 2018/3/23.
 */
public abstract class BaseFragment extends Fragment{

	protected static final int REFRESH_DATA = 1000;
	protected static final int SHOW_TOAST = 2000;
	private static final int BACK_ENABLE = 1;

	private BaseFragment baseFragment;
	
	protected View rootView;
	
	protected LayoutInflater mInflater;
	
	FManager moduleManager;
	
	protected TitleBar titleBar;
	
	private int tempRequestCode = -1;

	private boolean invlidate = false;

	/** fragment 界面是否销毁*/
	private boolean active = true;

	private Bundle backBundle;

	private Unbinder unbinder;

	private Handler mUiHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(isActive()){
				onHandleMessage(msg);
			}
		}
	};

	public Handler getUiHandler(){
		return this.mUiHandler;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			// 透明状态栏
//			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		}
		this.mInflater = inflater;
		LinearLayout baseLayout = new LinearLayout(getActivity());
		baseLayout.setOrientation(LinearLayout.VERTICAL);
		
		titleBar = new TitleBar(getActivity());
		
		titleBar.setOnBackBtnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!onBack()){
					back();
				}
			}
		});
		
		titleBar.setOnSubmitBtnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSubmit();
			}
		});

		baseLayout.addView(titleBar,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		titleBar.setBackBtnVisibility(true);
		titleBar.setSubmitBtnVisibility(false);
		
		View contentView = onCreateContentView(inflater,container,savedInstanceState);
		baseLayout.addView(contentView,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		rootView = baseLayout;
		unbinder = ButterKnife.bind(this,rootView);
		refresh(true);
		return rootView;
	}


	
	/**
	 * 创建内容View
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	public void back(){
		if(invlidate){
			moduleManager.back(invlidate,backBundle);
			invlidate = false;
			backBundle = null;
		}else{
			moduleManager.back(false,null);
		}
		titleBar.setBackBtnEnable(false);
		sendMessageDelayed(BACK_ENABLE,500);
	}

//	/**
//	 * 关闭Slide页面
//	 */
//	protected void close(){
//		DrawerLayout drawer = (DrawerLayout)((MainActivity)getActivity()).findViewById(R.id.drawer);
//		if(drawer.isDrawerOpen(GravityCompat.END)){
//    		drawer.closeDrawer(GravityCompat.END);
//    	}
//	}
//	
	/**
	 * 描述：初始化数据。建议在此方法内填充视图的数据。<br/>
	 * <b>注意：此方法在onBindView后执行。</b>
	 */
	protected abstract void onInitData();

	/**
	 * 子类重写handleMessage
	 * @param msg
	 */
	protected void onHandleMessage(Message msg){
		if(msg.what == BACK_ENABLE){
			Log.e(MRApplication.TAG,"handleMsg back");
			if(titleBar != null){
				titleBar.setBackBtnEnable(true);
			}
		}
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
	 * 描述：用指定资源ID表示的View填充主界面，视图创建完毕后调用，建议在此方法内注册监听。<br/>
	 * <b>注意：此方法在onInitData前执行。</b>
	 * @param contentView 内容View
	 */
	protected void onBindView(View contentView) {
		
	}
	
	/**
	 * 设置后退按钮是否可见
	 * @param visible
	 */
	protected void setBackBtnVisible(boolean visible){
		if(titleBar != null){
			titleBar.setBackBtnVisibility(visible);
		}
	}
	protected boolean isActive(){
		return active;
	}
	public boolean onKeyUp(int keyCode, KeyEvent event){
		return false;
	}

	/**
	 * 视图销毁时回调
	 */
	protected void onDestroyUI(){
		active = false;
	}
	
	/**
	 * 返回键按下时回调，如返回false，则会自动调用FManager.back方法。
	 * @return
	 */
	protected boolean onBack(){
		return false;
	}
	
	protected void onSubmit(){
		invlidateBackRefresh(null);
	}
	
	protected void onRefresh(Bundle bundle){
		
	}

	/**
	 * 刷新当前界面
	 * @param resetBind 是否重新注册控件监听
	 */
	public void refresh(boolean resetBind){
		if(resetBind){
			onBindView(rootView);
		}
		onInitData();
	}

	/**
	 * 刷新当前界面和数据。不注册控件监听。
	 */
	public void refreshData(){
		onInitData();
	}

	/**
	 * 标记后退刷新，调用此方法后，如果按下后退键，则前一个界面会自动调用onRefresh进行刷新。
	 * @param bundle 返回前一个界面的Bundle
	 */
	protected void invlidateBackRefresh(Bundle bundle){
		this.invlidate = true;
		this.backBundle = bundle;
	}

	/**
	 * 标记后退刷新，调用此方法后，如果按下后退键，则前一个界面会自动调用onRefresh进行刷新。
	 */
	protected void invlidateBackRefresh(){
		this.invlidate = true;
	}

	/**
     * 描述：设置标题文本.
     * @param text 文本
     */
	public void setTitleText(String text) {
		if(titleBar != null){
			titleBar.setTitleText(text);
		}
	}
	
	/**
	 * 描述：设置标题文本.
	 * @param resId 资源ID
	 */
	public void setTitleText(int resId) {

		if(titleBar != null){
			titleBar.setTitleText(resId);
		}
	}
	
	/**
     * 描述：设置标题文本.
     * @param text 文本
     */
	public void setSubmitBtnText(String text) {
		if(titleBar != null){
			titleBar.setSubmitBtnText(text);
		}
	}

	/**
	 * 描述：设置Submit 按钮字体颜色.
	 * @param resId 颜色资源ID
	 */
	public void setSubmitBtnTextColor(int resId) {
		if(titleBar != null){
			titleBar.setSubmitBtnTextColor(resId);
		}
	}
	
	/**
	 * 描述：设置标题文本.
	 * @param resId 资源ID
	 */
	public void setSubmitBtnText(int resId) {
		if(titleBar != null){
			titleBar.setSubmitBtnText(resId);
		}
	}
	
	/**
	 * 设置是否包含后退按钮
	 * @param visible 是否可见
	 * */
	public void setBackBtnVisibility(boolean visible) {
		if(titleBar != null){
			titleBar.setBackBtnVisibility(visible);
		}
	}

	/**
	 * 设置TitleBar是否可见
	 * @param visibility View.VISIBLE: View.GONE: View.INVISIBLE
	 */
	public void setTitleBarVisibility(int visibility){
		if(titleBar != null){
			titleBar.setVisibility(visibility);
		}
	}
	
	/**
	 * 设置是否包含确认按钮
	 * @param visible  是否可见
	 */
	public void setSubmitBtnVisibility(boolean visible) {
		if(titleBar != null){
			titleBar.setSubmitBtnVisibility(visible);
		}
	}
	
	public FManager getModuleManager() {
		return moduleManager;
	}

	// Fragment startActivityForResult target 2.3.3 or before
	/*private void mStartActivityForResult(Intent intent){
		getActivity().startActivityForResult(intent, getModuleManager().getRequestCode());
	}*/
	
	/*@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		this.tempRequestCode = requestCode;
		mStartActivityForResult(intent);
	}*/
	
	/*public void activityResult(int resultCode, Intent data) {
		onActivityResult(tempRequestCode, resultCode, data);
	}*/

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof BaseFragment) {
			((BaseFragment) parentFragment).startActivityForResultFromChildFragment(intent, requestCode, this);
		} else {
			baseFragment = null;
			super.startActivityForResult(intent, requestCode);
		}
	}

	private void startActivityForResultFromChildFragment(Intent intent, int requestCode, BaseFragment childFragment) {
		baseFragment = childFragment;

		Fragment parentFragment = getParentFragment();
		if (parentFragment != null && parentFragment instanceof BaseFragment) {
			((BaseFragment) parentFragment).startActivityForResultFromChildFragment(intent, requestCode, this);
		} else {
			super.startActivityForResult(intent, requestCode);
		}
	}

	@Override
	public final void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (baseFragment != null) {
			baseFragment.onActivityResult(requestCode, resultCode, data);
			baseFragment = null;
		} else {
			onActivityResultNestedCompat(requestCode, resultCode, data);
		}
	}

	public void onActivityResultNestedCompat(int requestCode, int resultCode, Intent data) {

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
	
	/**
	 * 根据ID返回View
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	protected <T extends View> T findViewById(int id){
		if(rootView != null){
			return (T) rootView.findViewById(id);
		}
		return null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils.d(getClass().getName()+" onActivityCreated");
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		LogUtils.d(getClass().getName()+" onDestroyView");
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtils.d(getClass().getName()+" onAttach");
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		LogUtils.d(getClass().getName()+" onDetach");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtils.d(getClass().getName()+" onResume");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtils.d(getClass().getName()+" onPause");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbinder.unbind();
		LogUtils.d(getClass().getName()+" onDestroy");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LogUtils.d(getClass().getName()+" onCreate");
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LogUtils.d(getClass().getName()+" onStart");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		LogUtils.d(getClass().getName()+" onStop");
	}

	public void hideSoftInput(View view , boolean hide){
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		// 获取软键盘的显示状态
		boolean isOpen=imm.isActive();
		if(hide){
			// 强制隐藏软键盘
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}else{
			// 强制显示软键盘
			imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
		}
		// 如果软键盘已经显示，则隐藏，反之则显示
//		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void showProgressDialog(String title, String msg){
		ProgressDialogUtil.show(getActivity(),title,msg);
	}

	public void showProgressDialog(String title, String msg, ProgressDialogUtil.OnCancelListener onCancelListener){
		ProgressDialogUtil.show(getActivity(),title,msg, onCancelListener);
	}

	public void dismissProgressDialog(){
		ProgressDialogUtil.dismiss();
	}
}

package com.ewide.photograph.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewide.photograph.R;

/**
 * Toolbar
 *
 * @version 1.0.0
 * @date 2016年10月10日
 */
@SuppressWarnings("ResourceType")
public class TitleBar extends LinearLayout {

	private RelativeLayout contentView = null;

	private TextView titleTextView = null;

	private ImageView backBtn = null;

	private Button submitBtn = null;

	private ImageView menuBtn = null;
	private ImageView rightBtn = null;
	private LinearLayout line = null;

	private PopupMenu popupMenu = null;

	private View actionView = null;

	/**
	 * 标题栏布局ID
	 */
	private final int titleBarID = 1;

	private OnMenuItemClickListener mOnMenuItemClickListener;

	public TitleBar(Context context) {
		super(context);
		initTitleBar(context);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initTitleBar(context);
	}

	private void initTitleBar(Context context) {
		setOrientation(LinearLayout.VERTICAL);
		contentView = new RelativeLayout(context);
		int titalBarHeight = (int) getResources().getDimension(R.dimen.titleBarHeight);
		contentView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, titalBarHeight));
		setGravity(Gravity.CENTER_VERTICAL);
		setId(titleBarID);
		int vpadding = (int) getResources().getDimension(R.dimen.titleBarPadding_V);
		int hpadding = (int) getResources().getDimension(R.dimen.titleBarPadding_H);
		contentView.setPadding(hpadding, vpadding, hpadding, vpadding);
		contentView.setBackgroundResource(R.color.titleBarBackground);

		backBtn = new ImageView(context);
		backBtn.setImageResource(R.drawable.btn_back_style);
		backBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		int titleImageMarginH = (int) getResources().getDimension(R.dimen.marginMiddle);
		int titleImageMarginV = (int) getResources().getDimension(R.dimen.marginSmall);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(titleImageMarginH,titleImageMarginV,titleImageMarginH,titleImageMarginV);
		backBtn.setLayoutParams(lp);
//		backBtn.setBackgroundResource(R.drawable.button_back_style);
		backBtn.setVisibility(INVISIBLE);

//		backBtn.setMinHeight(0);
//		backBtn.setMinWidth(0);
//        backBtn.setMinimumWidth(0);
//		backBtn.setMinimumHeight(0);

		contentView.addView(backBtn);

		titleTextView = new TextView(context);
		titleTextView.setText("");
//		titleTextView.setTextColor(getResources().getColor(R.color.lightBlack));
		titleTextView.setTextColor(getResources().getColor(R.color.white));
		final int pixelSize = getResources().getDimensionPixelSize(R.dimen.titleBarTextSize);
		final int fontSize = (int) (pixelSize / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
		titleTextView.setTextSize(fontSize);
		contentView.addView(titleTextView);

		menuBtn = new ImageView(context);
		menuBtn.setId(2);
		menuBtn.setVisibility(View.GONE);
		menuBtn.setImageResource(R.drawable.btn_more_style);
		menuBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//		menuBtn.setGravity(Gravity.CENTER);
//		menuBtn.setMinHeight(0);
//		menuBtn.setMinWidth(0);
		menuBtn.setMinimumWidth(0);
//		menuBtn.setMinimumHeight(0);
		menuBtn.setBackgroundResource(R.drawable.btn_more_style);
		menuBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupMenu.show();
			}
		});
		contentView.addView(menuBtn);
		popupMenu = new PopupMenu(context, menuBtn);

		submitBtn = new Button(context);
		submitBtn.setId(3);
		submitBtn.setText(R.string.confirm);
		submitBtn.setBackgroundResource(R.color.transparent);
		submitBtn.setLayoutParams(lp);
//		submitBtn.setTextColor(R.drawable.title_save_button_style);
//		ColorStateList csl = (ColorStateList)getResources().getColorStateList(R.drawable.button_submit_style);
//		submitBtn.setTextColor(csl);
		submitBtn.setVisibility(View.GONE);
//		submitBtn.setGravity(Gravity.CENTER);
//		submitBtn.setMinHeight(0);
		submitBtn.setMinWidth(0);
		submitBtn.setMinimumWidth(0);
		submitBtn.setPadding(10, 0, 10, 0);
//		submitBtn.setMinimumHeight(0);
		submitBtn.setText(R.string.confirm);
		submitBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		submitBtn.setTextColor(getResources().getColor(R.color.lightBlack));
		final int ps = getResources().getDimensionPixelSize(R.dimen.titleBarButtonTextSize);
		final int fs = (int) (ps / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
		submitBtn.setTextSize(fs);
		contentView.addView(submitBtn);

		rightBtn = new ImageView(context);
		rightBtn.setId(4);
		rightBtn.setVisibility(View.GONE);
		rightBtn.setLayoutParams(lp);
		rightBtn.setImageResource(R.drawable.btn_save_style);
		rightBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		contentView.addView(rightBtn);

		addView(contentView);

		line = new LinearLayout(context);
		line.setId(5);
		line.setBackgroundResource(R.color.grayLight);
		addView(line);

		setClickable(true);

		resetLayout();
	}

	public void addActionView(View view) {
		this.actionView = view;
		contentView.addView(view);
		resetLayout();
	}

	public void resetLayout() {
		int titleImageMarginH = (int) getResources().getDimension(R.dimen.marginMiddle);
		int titleImageMarginV = (int) getResources().getDimension(R.dimen.marginSmall);
		RelativeLayout.LayoutParams btnLayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		btnLayout.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		btnLayout.setMargins(titleImageMarginH,titleImageMarginV,titleImageMarginH,titleImageMarginV);
		backBtn.setLayoutParams(btnLayout);

		RelativeLayout.LayoutParams rightLp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		rightLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		rightLp.setMargins(titleImageMarginH,titleImageMarginV,titleImageMarginH,titleImageMarginV);
		rightBtn.setLayoutParams(rightLp);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleTextView.setGravity(Gravity.CENTER);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		titleTextView.setLayoutParams(layoutParams);

		if (menuBtn.getVisibility() == VISIBLE) {
			RelativeLayout.LayoutParams menuLP = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			menuLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			menuLP.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			menuBtn.setLayoutParams(menuLP);
		}

		if (submitBtn.getVisibility() == VISIBLE) {
			if (menuBtn.getVisibility() == VISIBLE) {
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//				lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
				lp.addRule(RelativeLayout.LEFT_OF, menuBtn.getId());
				lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
				submitBtn.setLayoutParams(lp);
			} else {
				RelativeLayout.LayoutParams submitLp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				submitLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
				submitLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
				submitBtn.setLayoutParams(submitLp);
			}
		}

		LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2);
		line.setLayoutParams(lineLp);


		if (actionView != null) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			lp.rightMargin = 5;
			if (submitBtn.getVisibility() == VISIBLE) {
//				lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
				lp.addRule(RelativeLayout.LEFT_OF, submitBtn.getId());
				lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			} else if (menuBtn.getVisibility() == VISIBLE) {
//				lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
				lp.addRule(RelativeLayout.LEFT_OF, menuBtn.getId());
				lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			} else {
				lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
				lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			}
			actionView.setLayoutParams(lp);
		}
	}

	public ImageView getBackBtn() {
		if (backBtn != null) {
			return backBtn;
		}
		return null;
	}

	public TextView getTitleTextView() {
		if (titleTextView != null) {
			return titleTextView;
		}
		return null;
	}

	public ImageView getRightBtn() {
		if (rightBtn != null) {
			return rightBtn;
		}
		return null;
	}
	public Button getSubmitBtn() {
		if (submitBtn != null) {
			return submitBtn;
		}
		return null;
	}

	/**
	 * 描述：设置标题文本.
	 *
	 * @param text 文本
	 */
	public void setTitleText(String text) {
		if (titleTextView != null) {
			titleTextView.setText(text);
		}
	}

	/**
	 * 描述：设置标题文本.
	 *
	 * @param resId 资源ID
	 */
	public void setTitleText(int resId) {
		if (titleTextView != null) {
			titleTextView.setText(getResources().getString(resId));
		}
	}

	/**
	 * 描述：设置标题文本.
	 *
	 * @param text 文本
	 */
	public void setSubmitBtnText(String text) {
		if (submitBtn != null) {
			submitBtn.setText(text);
		}
	}

	/**
	 * 描述：设置标题文本.
	 *
	 * @param resId 资源ID
	 */
	public void setSubmitBtnText(int resId) {
		if (submitBtn != null) {
			submitBtn.setText(getResources().getString(resId));
		}
	}

	/**
	 * 描述：设置标题文本.
	 *
	 * @param resId 资源ID
	 */
	public void setSubmitBtnTextColor(int resId) {
		if (submitBtn != null) {
			submitBtn.setTextColor(getResources().getColor(resId));
		}
	}

	/**
	 * 设置是否包含后退按钮
	 *
	 * @param visible 是否显示
	 */
	public void setBackBtnVisibility(boolean visible) {
		if (backBtn != null) {
			backBtn.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
			resetLayout();
		}
	}

	/**
	 * 设置返回按钮是否可用
	 *
	 * @param enable
	 */
	public void setBackBtnEnable(boolean enable) {
		if (backBtn != null) {
			backBtn.setEnabled(enable);
			resetLayout();
		}
	}

	/**
	 * 设置是否包含提交按钮
	 *
	 * @param visible 是否显示
	 */
	public void setSubmitBtnVisibility(boolean visible) {
		if (submitBtn != null) {
			submitBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
			resetLayout();
			if (visible) {
				rightBtn.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 设置是否包含确认按钮
	 *
	 * @param visible 是否显示
	 */
	public void setRightBtnVisibility(boolean visible) {
		if (rightBtn != null) {
			rightBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
			resetLayout();
			if (visible) {
				submitBtn.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 设置弹出菜单是否显示
	 *
	 * @param visible 是否显示
	 */
	public void setMenuVisibility(boolean visible) {
		if (menuBtn != null) {
			menuBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
			resetLayout();
		}
	}

	public void inflateMenu(int menuRes) {
		menuBtn.setVisibility(VISIBLE);
		popupMenu.inflate(menuRes);
		resetLayout();
	}

	public void setOnSubmitBtnClickListener(OnClickListener onClickListener) {
		if (submitBtn != null) {
			submitBtn.setOnClickListener(onClickListener);
		}
	}

	public void setOnBackBtnClickListener(OnClickListener onClickListener) {
		if (backBtn != null) {
			backBtn.setOnClickListener(onClickListener);
		}
	}

	public void setOnLeftBtnClickListener(OnClickListener onClickListener) {
		if (rightBtn != null) {
			rightBtn.setOnClickListener(onClickListener);
		}
	}

	public void setOnRightBtnClickListener(OnClickListener onClickListener) {
		if (rightBtn != null) {
			rightBtn.setOnClickListener(onClickListener);
		}
	}

	public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
		this.mOnMenuItemClickListener = onMenuItemClickListener;
		if (onMenuItemClickListener == null) {
			popupMenu.setOnMenuItemClickListener(null);
		} else {
			popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					if (mOnMenuItemClickListener != null) {
						return mOnMenuItemClickListener.onMenuItemClick(item);
					}
					return false;
				}
			});
		}
	}

	public interface OnMenuItemClickListener {
		/**
		 * This method will be invoked when a menu item is clicked if the item
		 * itself did not already handle the event.
		 *
		 * @param item {@link MenuItem} that was clicked
		 * @return <code>true</code> if the event was handled,
		 * <code>false</code> otherwise.
		 */
		public boolean onMenuItemClick(MenuItem item);
	}
}


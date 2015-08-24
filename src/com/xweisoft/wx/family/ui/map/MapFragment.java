package com.xweisoft.wx.family.ui.map;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.ui.BaseFragment;
import com.xweisoft.wx.family.ui.contact.ContactSearchActivity;
import com.xweisoft.wx.family.ui.contact.ContactsFragmet;
import com.xweisoft.wx.family.ui.map.adapter.MyPagerAdapter;
import com.xweisoft.wx.family.util.LogX;

/**
 * 
 * @author Administrator
 * 
 */
public class MapFragment extends BaseFragment implements OnClickListener {

	ViewPager pager = null;
	@SuppressWarnings("deprecation")
	LocalActivityManager manager = null;
	private LinearLayout llMap;
	private LinearLayout llList;
	private LinearLayout titleLayout;
	private ImageView ivSearch;
	private ImageView common_shaixuan;
	private PopupWindow popupWindow;

	@Override
	public void initViews() {
		llMap = (LinearLayout) getActivity().findViewById(R.id.ll_map_map);
		llList = (LinearLayout) getActivity().findViewById(R.id.ll_map_list);
		titleLayout = (LinearLayout) getActivity().findViewById(
				R.id.common_title_layout);
		common_shaixuan = (ImageView) getActivity().findViewById(
				R.id.common_shaixuan);
		ivSearch = (ImageView) getActivity().findViewById(
				R.id.common_title_right_imageview);

	}

	@Override
	public void bindListener() {
		llMap.setOnClickListener(this);
		llList.setOnClickListener(this);
		ivSearch.setOnClickListener(this);
		common_shaixuan.setOnClickListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayouView = inflater.inflate(R.layout.map_fragment, container, false);
		return mLayouView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		manager = new LocalActivityManager(getActivity(), true);
		manager.dispatchCreate(savedInstanceState);
		initViews();
		initPagerViewer();
		bindListener();
	}

	/**
	 * 初始化PageViewer
	 */
	private void initPagerViewer() {
		pager = (ViewPager) getActivity().findViewById(R.id.vp_myorder);
		final ArrayList<View> list = new ArrayList<View>();

		Intent intent = new Intent(getActivity(), LocationDemo.class);
		list.add(getView("0", intent));
		Intent intent2 = new Intent(getActivity(), ListActivity.class);
		list.add(getView("1", intent2));
		pager.setAdapter(new MyPagerAdapter(list));
		pager.setCurrentItem(0);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				System.out.println("onPageSelected=" + position);
				if (position == 0) {
				}

				if (position == 1) {
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		// pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 通过activity获取视图
	 * 
	 * @param id
	 * @param intent
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	// popwindouw

	@SuppressLint("NewApi")
	private void showPopupWindow(View view) {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(mContext).inflate(
				R.layout.popwindow, null);
		// 设置按钮的点击事件
		Button button = (Button) contentView.findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "button is pressed",
						Toast.LENGTH_SHORT).show();
				popupWindow.dismiss();
			}
		});

		popupWindow = new PopupWindow(contentView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

		// popupWindow.setTouchable(true);

		popupWindow.setOutsideTouchable(true);
		// popupWindow.setTouchInterceptor(new OnTouchListener() {
		//
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// Log.i("mengdd", "onTouch : ");
		//
		// return false;
		// // 这里如果返回true的话，touch事件将被拦截
		// // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
		// }
		// });

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.common_gray_bg));

		// 设置好参数之后再show
		popupWindow.showAsDropDown(titleLayout, titleLayout.getWidth(), 0);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		// 地图
		case R.id.ll_map_map:
			System.out.println("ll_map_map is onClick ~");
			llMap.setBackgroundColor(Color.BLACK);
			llList.setBackgroundColor(Color.WHITE);
			pager.setCurrentItem(0);

			break;
		// 列表
		case R.id.ll_map_list:
			pager.setCurrentItem(1);
			llList.setBackgroundColor(Color.BLACK);
			llMap.setBackgroundColor(Color.WHITE);
			System.out.println("ll_map_list is onClick ~");
			break;
		// search
		case R.id.common_title_right_imageview:
			startActivity(new Intent(getActivity(), ContactSearchActivity.class));
			break;

		// 筛选common_shaixuan
		case R.id.common_shaixuan:
			showPopupWindow(view);
			break;

		default:
			break;
		}
	}
}

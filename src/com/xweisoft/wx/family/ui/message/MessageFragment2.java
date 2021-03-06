package com.xweisoft.wx.family.ui.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xweisoft.wx.family.R;
import com.xweisoft.wx.family.ui.BaseFragment;
import com.xweisoft.wx.family.ui.message.adpter.MessageListAdapter;

/**
 * 个人中心 <一句话功能简述> <功能详细描述>
 * 
 * @author poorgod
 * @version [版本号, 2015-5-11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class MessageFragment2 extends BaseFragment implements OnClickListener {
	
	private ListView listview;
	private MessageListAdapter listAdapter;
	private TextView title;


	@Override
	public void onClick(View arg0) {

	}

	@Override
	public void initViews() {
		listview = (ListView) getActivity().findViewById(R.id.listview1);
		title=(TextView) getActivity().findViewById(R.id.generalTitleLabel);
		listAdapter = new MessageListAdapter(getActivity());
		//title.setText("我的消息");
		listview.setAdapter(listAdapter);
	}

	@Override
	public void bindListener() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLayouView = inflater.inflate(R.layout.message_listview, container,
				false);
		return mLayouView;
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
	}
	
	
}

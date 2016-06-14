package com.example.expressscanningsystem.adapter;

import java.util.List;

import com.example.expressscanningsystem.R;
import com.example.expressscanningsystem.bean.TrackTheQueryBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TrackingSelectAdapter extends BaseAdapter{
	
	private Context context;
	private List<TrackTheQueryBean> list;
	
	public TrackingSelectAdapter(Context context,List<TrackTheQueryBean> list) {
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public TrackTheQueryBean getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=null;
		ViewHolder viewHolder=null;
		if(convertView != null){
			view=convertView;
			viewHolder=(ViewHolder) view.getTag();
		}else {
			view=View.inflate(context, R.layout.tracking_select_adapter,null);
			viewHolder=new ViewHolder();
			viewHolder.show_statues_tv=(TextView) view.findViewById(R.id.show_statues_tv);
			viewHolder.show_datetime_tv=(TextView) view.findViewById(R.id.show_datetime_tv);
			view.setTag(viewHolder);
		}
		TrackTheQueryBean theQueryBean=list.get(position);
		viewHolder.show_statues_tv.setText(theQueryBean.getFStatus());
		viewHolder.show_datetime_tv.setText(theQueryBean.getFDateTime());
		return view;
	}
	
	class ViewHolder{
		private TextView show_statues_tv;//显示状态
		private TextView show_datetime_tv;//显示时间
	}

}

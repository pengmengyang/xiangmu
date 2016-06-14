package com.example.expressscanningsystem.adapter;

import java.util.List;

import com.example.expressscanningsystem.R;
import com.example.expressscanningsystem.bean.NetBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShowNetSpinnerAdapter extends BaseAdapter{
	
	private Context context;
	private List<NetBean> list;
	
	public ShowNetSpinnerAdapter(Context context,List<NetBean> list) {
		
		this.context=context;
		this.list=list;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public NetBean getItem(int position) {
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
			view=View.inflate(context, R.layout.show_net_spinner_adapter, null);
			viewHolder=new ViewHolder();
			viewHolder.show_net_tv=(TextView) view.findViewById(R.id.show_net_tv);
			view.setTag(viewHolder);
		}
		NetBean netBean=list.get(position);
		viewHolder.show_net_tv.setText(netBean.getNetName());
		return view;
	}
	
	class ViewHolder{
		
		private TextView show_net_tv;
		
	}

}

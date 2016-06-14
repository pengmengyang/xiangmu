package com.example.expressscanningsystem.adapter;

import java.util.List;

import com.example.expressscanningsystem.R;
import com.example.expressscanningsystem.bean.EmployeeBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShowEmployeeSpinnerAdapter extends BaseAdapter{
	
	private Context context;
	private List<EmployeeBean> list;
	
	public ShowEmployeeSpinnerAdapter(Context context,List<EmployeeBean> list) {
		
		this.context=context;
		this.list=list;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public EmployeeBean getItem(int position) {
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
			view=View.inflate(context, R.layout.show_employee_spinner_adapter, null);
			viewHolder=new ViewHolder();
			viewHolder.show_employee_tv=(TextView) view.findViewById(R.id.show_employee_tv);
			viewHolder.show_employee_number_tv=(TextView) view.findViewById(R.id.show_employee_number_tv);
			view.setTag(viewHolder);
		}
		EmployeeBean employeeBean=list.get(position);
		viewHolder.show_employee_tv.setText(employeeBean.getEP_Name());
		viewHolder.show_employee_number_tv.setText(employeeBean.getUserNo());
		return view;
	}
	
	class ViewHolder{
		
		private TextView show_employee_tv;
		private TextView show_employee_number_tv;
		
	}

}

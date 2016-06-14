package com.example.expressscanningsystem.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.expressscanningsystem.R;
import com.example.expressscanningsystem.bean.ScanBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShowWaybillNumberListviewAdaper extends BaseAdapter{
	
	private Context context;
	private List<ScanBean> list;
	public List<ScanBean>list2;
	
	public ShowWaybillNumberListviewAdaper(Context context) {
		this.context=context;
		list=new ArrayList<ScanBean>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public ScanBean getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void addItem(ScanBean scanBean){
		boolean flag=false;
		for (int i = 0; i < list.size(); i++) {
			ScanBean scanBean2=list.get(i);
			if(scanBean.getBillNo().equals(scanBean2.getBillNo())){
				Toast.makeText(context, "该运单已经扫描",Toast.LENGTH_SHORT).show();
				flag=true;
				break;//退出循环
			}else {
				flag=false;
			}
		}
		if(!flag){
			if(list.size()<10){
				list.add(scanBean);
				this.notifyDataSetChanged();
			}else {
				Toast.makeText(context, "请先提交该十条数据",Toast.LENGTH_SHORT).show();
			}
			
		}
		list2=list;
	}
	
	public void remove(int position){
		list.remove(position);
		this.notifyDataSetChanged();
		list2=list;
	}
	public void removeAll(List<ScanBean> list){
		list.removeAll(list);
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=null;
		ViewHolder viewHolder=null;
		if(convertView != null){
			view=convertView;
			viewHolder=(ViewHolder) view.getTag();
		}else {
			view=View.inflate(context, R.layout.show_waybill_number_listview_adaper, null);
			viewHolder=new  ViewHolder();
			viewHolder.show_waybill_number_tv=(TextView) view.findViewById(R.id.show_waybill_number_tv);
			viewHolder.show_consignee_man_tv=(TextView) view.findViewById(R.id.show_consignee_man_tv);
			view.setTag(viewHolder);
		}
		ScanBean scanBean=list.get(position);
		viewHolder.show_waybill_number_tv.setText(scanBean.getBillNo());
		viewHolder.show_consignee_man_tv.setText(scanBean.getScanMan());
		return view;
	}
	
	class ViewHolder{
		private TextView show_waybill_number_tv;
		private TextView show_consignee_man_tv;
	}

}

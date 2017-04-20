package com.pactera.enterprisesecretary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pactera.enterprisesecretary.R;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaoyang on 2017/4/20.
 */

public class MessageAdapter extends BaseAdapter {

    private String TAG = "ActivityMultiRoom";
    private List<Map<String, Object>> allData;

    private Context context;
    private ViewHolder holder;

    public MessageAdapter(Context context, List<Map<String, Object>> allData){
        this.allData = allData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return allData.size();
    }

    @Override
    public Object getItem(int position) {
        return allData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {// 未创建过视图
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.listview_message, null);
            holder = new ViewHolder();
            holder.itemMessageIconImageView = (ImageView) convertView
                    .findViewById(R.id.itemMessageIconImageView);
            holder.itemMessageTitleTextView = (TextView) convertView
                    .findViewById(R.id.itemMessageTitleTextView);
            holder.itemMessageDescriptionTextView = (TextView) convertView
                    .findViewById(R.id.itemMessageDescriptionTextView);
            holder.itemMessageDateTextView = (TextView) convertView
                    .findViewById(R.id.itemMessageDateTextView);
            convertView.setTag(holder);
        } else {// 从缓存中取得
            holder = (ViewHolder) convertView.getTag();
        }
        // 赋值给控件
        Map<String, Object> myMessageMap = allData.get(position);

            holder.itemMessageTitleTextView.setText(myMessageMap.get("name").toString());
        holder.itemMessageDescriptionTextView.setText(myMessageMap.get("description").toString());

        return convertView;
    }


    // 列表组件的缓存
    static class ViewHolder {
        ImageView itemMessageIconImageView;
        TextView itemMessageTitleTextView, itemMessageDescriptionTextView, itemMessageDateTextView;
    }
}

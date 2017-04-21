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

public class UserAdapter extends BaseAdapter {

    private String TAG = "ActivityMultiRoom";
    private List<Map<String, Object>> allData;

    private Context context;
    private ViewHolder holder;

    public UserAdapter(Context context, List<Map<String, Object>> allData){
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
                    R.layout.listview_user, null);
            holder = new ViewHolder();
            holder.itemUserIconImageView = (ImageView) convertView
                    .findViewById(R.id.itemUserIconImageView);
            holder.itemUserTitleTextView = (TextView) convertView
                    .findViewById(R.id.itemUserTitleTextView);
            holder.itemUserDescriptionTextView = (TextView) convertView
                    .findViewById(R.id.itemUserDescriptionTextView);
            convertView.setTag(holder);
        } else {// 从缓存中取得
            holder = (ViewHolder) convertView.getTag();
        }
        // 赋值给控件
        Map<String, Object> myUserMap = allData.get(position);

            holder.itemUserTitleTextView.setText(myUserMap.get("name").toString());
        holder.itemUserDescriptionTextView.setText(myUserMap.get("description").toString());
        holder.itemUserIconImageView.setImageResource((int)myUserMap.get("image"));
        return convertView;
    }


    // 列表组件的缓存
    static class ViewHolder {
        ImageView itemUserIconImageView;
        TextView itemUserTitleTextView, itemUserDescriptionTextView ;
    }
}

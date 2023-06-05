package com.zqw.mobile.tradeside.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zqw.mobile.tradeside.R;
import com.zqw.mobile.tradeside.mvp.model.entity.ArealistBean;

import java.util.List;

/**
 * 横向展示 - 城市
 */
public class HotCityGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<ArealistBean> mCities;

    public HotCityGridAdapter(Context context, List<ArealistBean> mCities) {
        this.mContext = context;
        this.mCities = mCities;
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public String getItem(int position) {
        return mCities == null ? null : mCities.get(position).getAreaName();
    }

    public String getItemIdByPosition(int position) {
        return mCities == null ? null : mCities.get(position).getAreaId();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        HotCityViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.cp_item_hot_city_gridview, parent, false);
            holder = new HotCityViewHolder();
            holder.name = view.findViewById(R.id.tv_hot_city_name);
            view.setTag(holder);
        } else {
            holder = (HotCityViewHolder) view.getTag();
        }
        holder.name.setText(mCities.get(position).getAreaName());
        return view;
    }

    public static class HotCityViewHolder {
        TextView name;
    }
}

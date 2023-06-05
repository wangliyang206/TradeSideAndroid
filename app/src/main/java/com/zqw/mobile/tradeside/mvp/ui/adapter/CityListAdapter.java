package com.zqw.mobile.tradeside.mvp.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zqw.mobile.tradeside.R;
import com.zqw.mobile.tradeside.app.utils.CommonUtils;
import com.zqw.mobile.tradeside.mvp.model.entity.ArealistBean;
import com.zqw.mobile.tradeside.mvp.ui.widget.WrapHeightGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 选择城市
 */
public class CityListAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 4;

    private Context mContext;
    private LayoutInflater inflater;
    private List<ArealistBean> mCities;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnCityClickListener onCityClickListener;
    // 定位状态(定位成功1 or 失败 0)
    private int locateState = 0;
    private String locatedCity;

    public CityListAdapter(Context mContext, List<ArealistBean> mCities) {
        this.mContext = mContext;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);

    }

    public void initData() {
        if (mCities == null) {
            mCities = new ArrayList<>();
        }

        // 添加提示
        mCities.add(0, new ArealistBean("定位", "0", 0));
        mCities.add(1, new ArealistBean("全部", "1", 0));
        // 检索是否有热门
        if (CommonUtils.isNotEmpty(getHotCity())) {
            mCities.add(2, new ArealistBean("热门", "2", 0));
        }
        int size = mCities.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++) {
            //当前城市拼音首字母
            String currentLetter = CommonUtils.getFirstLetter(mCities.get(index).getPinyin());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? CommonUtils.getFirstLetter(mCities.get(index - 1).getPinyin()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }

    /**
     * 更新定位状态
     *
     * @param state
     */
    public void updateLocateState(int state, String city) {
        this.locateState = state;
        this.locatedCity = city;
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        int item = position < VIEW_TYPE_COUNT - 1 ? position : VIEW_TYPE_COUNT - 1;

        // 判断是否有热门
        if (CommonUtils.isNotEmpty(getHotCity())) {
            // 有热门
            return item;
        }

        // 没有热门
        return item == 2 ? item + 1 : item;
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public ArealistBean getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 筛选出热门城市
     */
    private List<ArealistBean> getHotCity() {
        List<ArealistBean> mCity = new ArrayList<>();

        for (ArealistBean info : mCities) {
            if (info.getHot() == 1) {
                mCity.add(info);
            }
        }
        return mCity;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        CityViewHolder holder;
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0:     //定位
                view = inflater.inflate(R.layout.cp_view_locate_city, parent, false);
                ViewGroup container = view.findViewById(R.id.layout_locate);
                TextView state = view.findViewById(R.id.tv_located_city);
                switch (locateState) {
                    case 0:
                        // 失败
                        state.setText(mContext.getString(R.string.cp_locating));
                        container.setOnClickListener(null);
                        break;
                    case 1:
                        // 成功
                        state.setText(locatedCity);
                        container.setOnClickListener(null);
//                        container.setOnClickListener(v -> {
//                            if (onCityClickListener != null) {
//                                onCityClickListener.onCityClick(locatedCity);
//                            }
//                        });
                        break;
                }
                break;
            case 1:     // 全部
                view = inflater.inflate(R.layout.cp_view_all_city, parent, false);
                LinearLayout lilaAll = view.findViewById(R.id.lila_cpviewallcity_all);
                lilaAll.setOnClickListener(v -> {
                    if (onCityClickListener != null) {
                        onCityClickListener.onCityClick("","全部区域");
                    }
                });
                break;
            case 2:     // 热门
                view = inflater.inflate(R.layout.cp_view_hot_city, parent, false);
                WrapHeightGridView gridView = view.findViewById(R.id.gridview_hot_city);
                final HotCityGridAdapter hotCityGridAdapter = new HotCityGridAdapter(mContext, getHotCity());
                gridView.setAdapter(hotCityGridAdapter);
                gridView.setOnItemClickListener((parent1, view1, position1, id) -> {
                    if (onCityClickListener != null) {
                        onCityClickListener.onCityClick(
                                hotCityGridAdapter.getItemIdByPosition(position1),
                                hotCityGridAdapter.getItem(position1));
                    }
                });
                break;
            case 3:     // 所有
                if (view == null) {
                    view = inflater.inflate(R.layout.cp_item_city_listview, parent, false);
                    holder = new CityViewHolder();
                    holder.letter = view.findViewById(R.id.tv_item_city_listview_letter);
                    holder.name = view.findViewById(R.id.tv_item_city_listview_name);
                    view.setTag(holder);
                } else {
                    holder = (CityViewHolder) view.getTag();
                }
                if (position >= 1) {
                    final String areaId = mCities.get(position).getAreaId();
                    final String city = mCities.get(position).getAreaName();
                    holder.name.setText(city);
                    String currentLetter = CommonUtils.getFirstLetter(mCities.get(position).getPinyin());
                    String previousLetter = position >= 1 ? CommonUtils.getFirstLetter(mCities.get(position - 1).getPinyin()) : "";
                    if (!TextUtils.equals(currentLetter, previousLetter)) {
                        holder.letter.setVisibility(View.VISIBLE);
                        holder.letter.setText(currentLetter);
                    } else {
                        holder.letter.setVisibility(View.GONE);
                    }
                    holder.name.setOnClickListener(v -> {
                        if (onCityClickListener != null) {
                            onCityClickListener.onCityClick(areaId, city);
                        }
                    });
                }
                break;
        }
        return view;
    }

    public static class CityViewHolder {
        TextView letter;
        TextView name;
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    public interface OnCityClickListener {
        void onCityClick(String id, String name);
    }
}

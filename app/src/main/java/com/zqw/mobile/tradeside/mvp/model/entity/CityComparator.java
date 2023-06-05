package com.zqw.mobile.tradeside.mvp.model.entity;

import java.util.Comparator;

/**
 * “城市”比较器
 */
public class CityComparator implements Comparator<ArealistBean> {
    @Override
    public int compare(ArealistBean lhs, ArealistBean rhs) {
        String a = "";
        String b = "";

        if (lhs.getPinyin() != null && rhs.getPinyin() != null) {
            a = lhs.getPinyin().substring(0, 1);
            b = rhs.getPinyin().substring(0, 1);
        }
        return a.compareTo(b);
    }
}

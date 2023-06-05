package com.zqw.mobile.tradeside.mvp.model.entity;

/**
 * 城市 实体类
 */
public class ArealistBean {
    public ArealistBean() {
    }

    public ArealistBean(String areaName, String pinyin, int hot) {
        this.areaName = areaName;
        this.pinyin = pinyin;
        this.hot = hot;
    }

    public ArealistBean(String areaId, String areaName, String pinyin, int hot) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.pinyin = pinyin;
        this.hot = hot;
    }

    /**
     * areaId :
     * areaName :
     */

    private String areaId;
    private String areaName;
    // 城市拼音
    private String pinyin = "";
    // 是否热门
    private int hot = 0;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}

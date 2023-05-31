/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zqw.mobile.tradeside.app.utils;

/**
 * ================================================
 * 放置 AndroidEventBus 的 Tag, 便于检索
 * Arms 核心库现在并不会依赖某个 EventBus, 要想使用 EventBus, 还请在项目中自行依赖对应的 EventBus
 * 现在支持两种 EventBus, greenrobot 的 EventBus 和畅销书 《Android源码设计模式解析与实战》的作者 何红辉 所作的 AndroidEventBus
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#3.5">EventBusTags wiki 官方文档</a>
 * Created by JessYan on 8/30/2016 16:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface EventBusTags {
    //####################################################首页####################################################
    String HOME_TAG = "Home_tag";
    String SALESDELIVERYORDER_TAG = "SalesDeliveryOrder_tag";
    String NEWCUSTOMERVISIT_TAG = "NewCustomerVisit_tag";
    String RECYCLEORDER_TAG = "RecycleOrder_tag";

    // 登录成功
    int LOGIN_SUCC_TAG = 0;
    // 1、弹出筛选；2、拜访记录》筛选》业务员；
    int OPEN_FILTER_TAG = 1;
    // 1、打开侧滑；2、拜访记录详情状态变更后刷新列表。
    int OPEN_MENU_TAG = 2;
    // 切换城市
    int SWITCH_CITIES_TAG = 3;
    // 活体检测返回
    int RETURN_LIVENESS_DETECTION = 4;
    // 一笔画完游戏 中 清空数据库操作
    int ONE_LINE_TO_END_CLEAR = 5;
}

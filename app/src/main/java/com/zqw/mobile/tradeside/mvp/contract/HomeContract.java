package com.zqw.mobile.tradeside.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public interface HomeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        // 首页定位失败
        void homemPositioningFailure();

        // 设置城市
        void homeSetLocateAdd(String city);

        // 首页热门关键字
        void homeSearchHot(List<String> list);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
//        // 业绩统计
//        Observable<PerformanceStatisticsResponse> getResultsStatistics();
//
//        // 问题投诉统计
//        Observable<ProblemComplaintsResponse> getProblemComplaints();
//
//        // 客户拜访统计
//        Observable<VisitStatisticsResponse> getVisitStatistics();
//
//        // 门店统计
//        Observable<StoreStatisticsResponse> getStoreStatistics();
//
//        // 订单统计
//        Observable<OrderStatisticsResponse> getOrderStatistics();
//
//        // 注册统计
//        Observable<RegisterStatisticsResponse> getRegisterStatistics();
//
//        // 验证角色权限
//        boolean checkRole(String menuName, String itemName);
    }
}

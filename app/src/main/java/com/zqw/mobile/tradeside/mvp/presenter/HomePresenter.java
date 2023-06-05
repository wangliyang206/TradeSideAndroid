package com.zqw.mobile.tradeside.mvp.presenter;

import android.app.Application;
import android.text.TextUtils;

import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.tradeside.app.global.AccountManager;
import com.zqw.mobile.tradeside.app.utils.RxUtils;
import com.zqw.mobile.tradeside.mvp.contract.HomeContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import timber.log.Timber;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * ================================================
 */
@FragmentScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AccountManager mAccountManager;
    @Inject
    Application mApplication;

    // 循环定位倒计时
    private Disposable mDisposable;

    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 初始化首页
     */
    public void initHome() {
        // 循环检测是否定位成功(每一秒检测一次，10秒后如果没有定位成功，则改成手动设置)
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(10)
                .compose(RxUtils.applySchedulers(mRootView))                                        // 切换线程
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Timber.i("###onNext#address=%s", mAccountManager.getAddress());
                        if (!TextUtils.isEmpty(mAccountManager.getAddress())) {
                            // 获取定位成功
                            getHome();
                            Timber.i("###initHome-Dispose");
                            // 跳转循环
                            mDisposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("###initHome-onError");
                    }

                    @Override
                    public void onComplete() {
                        Timber.i("###initHome-onComplete");
                        // 未定位成功，弹出定位界面(手动设置或者重试)
                        mRootView.homemPositioningFailure();
                    }
                });
    }

    /**
     * 热门关键字
     */
    private void loadSearchHot() {
        List<String> list = new ArrayList<>();
        list.add("铅产业全新模式！全新平台！");
        list.add("干电池");
        mRootView.homeSearchHot(list);
    }

    /**
     * 获取首页数据
     */
    public void getHome() {
        // 拿到定位数据并进行展示
        if (TextUtils.isEmpty(mAccountManager.getSelectCityId())) {
            mRootView.homeSetLocateAdd("全部区域");
        } else {
            mRootView.homeSetLocateAdd(mAccountManager.getSelectCity());
        }
        // 加载热门搜索
        loadSearchHot();
//        // 请求首页数据
//        if (IS_DEBUG_DATA) {
//            mRootView.loadPerformanceStatistics(new PerformanceStatisticsResponse(7, 7, 30, 30));
//
//            getVisitStatistics();
//        } else {
//            // 业绩统计
//            mModel.getResultsStatistics()
//                    .compose(RxUtils.applySchedulers(mRootView, false, true))
//                    .subscribe(new ErrorHandleSubscriber<PerformanceStatisticsResponse>(mErrorHandler) {
//                        @Override
//                        public void onError(Throwable t) {
//
//                            getVisitStatistics();
//                        }
//
//                        @Override
//                        public void onNext(PerformanceStatisticsResponse performanceInfo) {
//                            // 加载业绩统计
//                            if (performanceInfo != null)
//                                mRootView.loadPerformanceStatistics(performanceInfo);
//
//                            getVisitStatistics();
//                        }
//                    });
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAccountManager = null;
        this.mApplication = null;

        this.mDisposable = null;
    }
}

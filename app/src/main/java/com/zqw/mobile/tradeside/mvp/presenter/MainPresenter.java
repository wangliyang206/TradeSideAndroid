package com.zqw.mobile.tradeside.mvp.presenter;

import static com.zqw.mobile.tradeside.BuildConfig.IS_DEBUG_DATA;
import static com.zqw.mobile.tradeside.app.service.LocationService.SPAN_NORMAL;

import android.os.Bundle;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.integration.cache.IntelligentCache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DeviceUtils;
import com.zqw.mobile.tradeside.app.global.AccountManager;
import com.zqw.mobile.tradeside.app.service.LocationService;
import com.zqw.mobile.tradeside.app.utils.RxUtils;
import com.zqw.mobile.tradeside.mvp.contract.MainContract;
import com.zqw.mobile.tradeside.mvp.model.entity.AppUpdate;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * ================================================
 * Description:首页
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AccountManager mAccountManager;
    @Inject
    Cache<String, Object> extras;
    /**
     * 定位对象
     */
    private LocationService locationService;

    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 初始化
     */
    public void initData(Bundle bundle) {
        // 拿到缓存中的定位对象，然后开启定位
        locationService = ((LocationService) extras.get(IntelligentCache.getKeyOfKeep(LocationService.class.getName())));
        if (locationService != null) {
            locationService.setLocationOption(locationService.setScanSpan(SPAN_NORMAL));
            // 开启定位
            locationService.start();
        }

        // APP升级更新
        if (mAccountManager.getUpgrade())
            checkUpdateManager();
    }


    /**
     * APP升级更新
     */
    private void checkUpdateManager() {
        // 提醒过了
        mAccountManager.setUpgrade(false);
        if (IS_DEBUG_DATA) {
            AppUpdate appUpdate = new AppUpdate(112, "1.1.2", "小的来了", "小的来了.apk", "http:\\/\\/buypb.e1.luyouxia.net:28708\\/imgs\\/update\\/recycleapp-debug-1.1.2.apk", 1, 18, "1.更新了好多东西\n1.更新了好多东西\n1.更新了好多东西");
            if (haveNew(appUpdate)) {
                // 先提醒升级
                mRootView.askDialog(appUpdate);
            }
        } else {
            mModel.getVersion("android")
                    .compose(RxUtils.applySchedulers(mRootView, true, true))                                            // 切换线程
                    .subscribe(new ErrorHandleSubscriber<AppUpdate>(mErrorHandler) {
                        @Override
                        public void onError(Throwable t) {
                            // 不做任何处理
                        }

                        @Override
                        public void onNext(AppUpdate au) {
                            if (haveNew(au)) {
                                // 先提醒升级
                                mRootView.askDialog(au);
                            }
                        }
                    });
        }
    }

    /**
     * 版本号比较
     *
     * @return 需要升级返回true，否则返回false
     */
    private boolean haveNew(AppUpdate appUpdate) {
        boolean haveNew = false;
        if (appUpdate == null) {
            return false;
        }

        int curVersionCode = DeviceUtils.getVersionCode(mRootView.getActivity().getApplicationContext());
        if (curVersionCode < appUpdate.getVerCode()) {
            haveNew = true;
        }
        return haveNew;
    }

    @Override
    public void onDestroy() {
        // 关闭定位
        if (locationService != null)
            this.locationService.stop();

        super.onDestroy();
        this.mErrorHandler = null;
        this.mAccountManager = null;
        this.locationService = null;
        this.extras = null;
    }
}
package com.zqw.mobile.tradeside.mvp.presenter;

import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.tradeside.mvp.contract.MyContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:我的
 * <p>
 * Created by MVPArmsTemplate on 09/15/2021 16:32
 * ================================================
 */
@FragmentScope
public class MyPresenter extends BasePresenter<MyContract.Model, MyContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public MyPresenter(MyContract.Model model, MyContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;

    }
}

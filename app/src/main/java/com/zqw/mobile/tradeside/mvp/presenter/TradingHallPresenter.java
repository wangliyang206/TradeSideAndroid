package com.zqw.mobile.tradeside.mvp.presenter;

import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.tradeside.mvp.contract.TradingHallContract;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


/**
 * ================================================
 * Description:交易大厅
 * <p>
 * Created by MVPArmsTemplate on 09/15/2021 16:32
 * ================================================
 */
@FragmentScope
public class TradingHallPresenter extends BasePresenter<TradingHallContract.Model, TradingHallContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public TradingHallPresenter(TradingHallContract.Model model, TradingHallContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;

    }
}

package com.zqw.mobile.tradeside.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.tradeside.mvp.contract.TradingHallContract;

import javax.inject.Inject;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/15/2021 16:32
 * ================================================
 */
@FragmentScope
public class TradingHallModel extends BaseModel implements TradingHallContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public TradingHallModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}
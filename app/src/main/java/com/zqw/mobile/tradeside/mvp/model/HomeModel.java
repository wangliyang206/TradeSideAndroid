package com.zqw.mobile.tradeside.mvp.model;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.tradeside.app.global.AccountManager;
import com.zqw.mobile.tradeside.mvp.contract.HomeContract;

import javax.inject.Inject;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * ================================================
 */
@FragmentScope
public class HomeModel extends BaseModel implements HomeContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换
    @Inject
    AccountManager mAccountManager;

    @Inject
    public HomeModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;
        this.mAccountManager = null;
    }
}
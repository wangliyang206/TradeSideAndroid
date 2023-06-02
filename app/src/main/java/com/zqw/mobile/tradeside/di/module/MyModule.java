package com.zqw.mobile.tradeside.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.FragmentScope;
import com.zqw.mobile.tradeside.app.global.AccountManager;
import com.zqw.mobile.tradeside.app.global.RequestMapper;
import com.zqw.mobile.tradeside.mvp.contract.MyContract;
import com.zqw.mobile.tradeside.mvp.model.MyModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/15/2021 16:32
 * ================================================
 */
@Module
public abstract class MyModule {

    @Binds
    abstract MyContract.Model bindAboutModel(MyModel model);

    @FragmentScope
    @Provides
    static AccountManager provideAccountManager(MyContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @FragmentScope
    @Provides
    static IRequestMapper providerRequestMapper(MyContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @FragmentScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }
}
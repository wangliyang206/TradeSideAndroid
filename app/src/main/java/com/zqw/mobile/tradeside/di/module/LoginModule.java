package com.zqw.mobile.tradeside.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.tradeside.app.global.AccountManager;
import com.zqw.mobile.tradeside.app.global.RequestMapper;
import com.zqw.mobile.tradeside.mvp.contract.LoginContract;
import com.zqw.mobile.tradeside.mvp.model.LoginModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
@Module
//构建LoginModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class LoginModule {

    @Binds
    abstract LoginContract.Model bindLoginModel(LoginModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(LoginContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(LoginContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }
}
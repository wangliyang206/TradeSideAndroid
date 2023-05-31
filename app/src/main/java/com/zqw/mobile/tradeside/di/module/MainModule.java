package com.zqw.mobile.tradeside.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;

import com.zqw.mobile.tradeside.mvp.contract.MainContract;
import com.zqw.mobile.tradeside.mvp.model.MainModel;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
@Module
//构建MainModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class MainModule {

    @Binds
    abstract MainContract.Model bindMainModel(MainModel model);
}
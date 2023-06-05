package com.zqw.mobile.tradeside.di.module;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.cj.IRequestMapper;
import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.tradeside.app.global.AccountManager;
import com.zqw.mobile.tradeside.app.global.RequestMapper;
import com.zqw.mobile.tradeside.mvp.contract.CityPickerContract;
import com.zqw.mobile.tradeside.mvp.model.CityPickerModel;
import com.zqw.mobile.tradeside.mvp.model.entity.ArealistBean;
import com.zqw.mobile.tradeside.mvp.ui.adapter.CityListAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/05 17:07
 * ================================================
 */
@Module
//构建CityPickerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
public abstract class CityPickerModule {

    @Binds
    abstract CityPickerContract.Model bindCityPickerModel(CityPickerModel model);

    @ActivityScope
    @Provides
    static AccountManager provideAccountManager(CityPickerContract.View view) {
        return new AccountManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static IRequestMapper providerRequestMapper(CityPickerContract.View view, AccountManager mAccountManager) {
        return new RequestMapper(view.getActivity(), mAccountManager);
    }

    @ActivityScope
    @Provides
    static ApiOperator providerOperator(IRequestMapper requestMapper) {
        return new ApiOperator(requestMapper);
    }

    @ActivityScope
    @Provides
    static List<ArealistBean> provideCity() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static CityListAdapter provideCityListAdapter(CityPickerContract.View view, List<ArealistBean> list) {
        return new CityListAdapter(view.getActivity(), list);
    }
}
package com.zqw.mobile.tradeside.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.tradeside.di.module.CityPickerModule;
import com.zqw.mobile.tradeside.mvp.contract.CityPickerContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.tradeside.mvp.ui.activity.CityPickerActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/05 17:07
 * ================================================
 */
@ActivityScope
@Component(modules = CityPickerModule.class, dependencies = AppComponent.class)
public interface CityPickerComponent {

    void inject(CityPickerActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        CityPickerComponent.Builder view(CityPickerContract.View view);

        CityPickerComponent.Builder appComponent(AppComponent appComponent);

        CityPickerComponent build();
    }
}
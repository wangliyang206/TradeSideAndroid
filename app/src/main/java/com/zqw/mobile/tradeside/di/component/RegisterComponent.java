package com.zqw.mobile.tradeside.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.tradeside.di.module.RegisterModule;
import com.zqw.mobile.tradeside.mvp.contract.RegisterContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.tradeside.mvp.ui.activity.RegisterActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/01 17:58
 * ================================================
 */
@ActivityScope
@Component(modules = RegisterModule.class, dependencies = AppComponent.class)
public interface RegisterComponent {

    void inject(RegisterActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        RegisterComponent.Builder view(RegisterContract.View view);

        RegisterComponent.Builder appComponent(AppComponent appComponent);

        RegisterComponent build();
    }
}
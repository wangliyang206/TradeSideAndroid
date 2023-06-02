package com.zqw.mobile.tradeside.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;
import com.zqw.mobile.tradeside.di.module.ForgotPasswordModule;
import com.zqw.mobile.tradeside.mvp.contract.ForgotPasswordContract;

import com.jess.arms.di.scope.ActivityScope;
import com.zqw.mobile.tradeside.mvp.ui.activity.ForgotPasswordActivity;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/01 17:58
 * ================================================
 */
@ActivityScope
@Component(modules = ForgotPasswordModule.class, dependencies = AppComponent.class)
public interface ForgotPasswordComponent {

    void inject(ForgotPasswordActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        ForgotPasswordComponent.Builder view(ForgotPasswordContract.View view);

        ForgotPasswordComponent.Builder appComponent(AppComponent appComponent);

        ForgotPasswordComponent build();
    }
}
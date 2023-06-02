package com.zqw.mobile.tradeside.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.FragmentScope;
import com.zqw.mobile.tradeside.di.module.HomeModule;
import com.zqw.mobile.tradeside.mvp.contract.HomeContract;
import com.zqw.mobile.tradeside.mvp.ui.fragment.HomeFragment;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 05/31/2019 10:34
 * ================================================
 */
@FragmentScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomeFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder view(HomeContract.View view);

        Builder appComponent(AppComponent appComponent);

        HomeComponent build();
    }
}
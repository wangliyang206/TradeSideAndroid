package com.zqw.mobile.tradeside.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.FragmentScope;
import com.zqw.mobile.tradeside.di.module.TradingHallModule;
import com.zqw.mobile.tradeside.mvp.contract.TradingHallContract;
import com.zqw.mobile.tradeside.mvp.ui.fragment.TradingHallFragment;

import dagger.BindsInstance;
import dagger.Component;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/15/2021 16:32
 * ================================================
 */
@FragmentScope
@Component(modules = TradingHallModule.class, dependencies = AppComponent.class)
public interface TradingHallComponent {
    void inject(TradingHallFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TradingHallComponent.Builder view(TradingHallContract.View view);

        TradingHallComponent.Builder appComponent(AppComponent appComponent);

        TradingHallComponent build();
    }
}
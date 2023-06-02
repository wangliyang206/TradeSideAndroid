package com.zqw.mobile.tradeside.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.FragmentScope;
import com.zqw.mobile.tradeside.di.module.MyModule;
import com.zqw.mobile.tradeside.mvp.contract.MyContract;
import com.zqw.mobile.tradeside.mvp.ui.fragment.MyFragment;

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
@Component(modules = MyModule.class, dependencies = AppComponent.class)
public interface MyComponent {
    void inject(MyFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        MyComponent.Builder view(MyContract.View view);

        MyComponent.Builder appComponent(AppComponent appComponent);

        MyComponent build();
    }
}
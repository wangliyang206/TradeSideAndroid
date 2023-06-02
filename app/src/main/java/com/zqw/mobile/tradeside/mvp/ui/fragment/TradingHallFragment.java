package com.zqw.mobile.tradeside.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.tradeside.R;
import com.zqw.mobile.tradeside.di.component.DaggerTradingHallComponent;
import com.zqw.mobile.tradeside.mvp.contract.TradingHallContract;
import com.zqw.mobile.tradeside.mvp.presenter.TradingHallPresenter;

/**
 * @ProjectName: TradeSideAndroid
 * @Package: com.zqw.mobile.tradeside.mvp.ui.fragment
 * @ClassName: TradingHallFragment
 * @Description: 交易大厅
 * @Author: WLY
 * @CreateDate: 2023/6/2 14:49
 */
public class TradingHallFragment extends BaseFragment<TradingHallPresenter> implements TradingHallContract.View {
    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTradingHallComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tradinghall, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    /**
     * 双击刷新
     */
    public void onDoubleClick() {

    }
}
package com.zqw.mobile.tradeside.mvp.ui.activity;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import com.zqw.mobile.tradeside.app.global.AccountManager;
import com.zqw.mobile.tradeside.app.utils.EventBusTags;
import com.zqw.mobile.tradeside.di.component.DaggerCityPickerComponent;
import com.zqw.mobile.tradeside.mvp.contract.CityPickerContract;
import com.zqw.mobile.tradeside.mvp.model.entity.MainEvent;
import com.zqw.mobile.tradeside.mvp.presenter.CityPickerPresenter;
import com.zqw.mobile.tradeside.R;
import com.zqw.mobile.tradeside.mvp.ui.adapter.CityListAdapter;
import com.zqw.mobile.tradeside.mvp.ui.widget.SideLetterBar;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Description:切换城市
 * <p>
 * Created on 2023/06/05 17:07
 *
 * @author 赤槿
 * module name is CityPickerActivity
 */
public class CityPickerActivity extends BaseActivity<CityPickerPresenter> implements CityPickerContract.View {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.listview_all_city)
    ListView mListView;

    @BindView(R.id.tv_letter_overlay)
    TextView overlay;

    @BindView(R.id.side_letter_bar)
    SideLetterBar mLetterBar;

    @BindView(R.id.view_citypickeractivity_loading)
    View mLoading;                                                                                  // 加载效果
    /*------------------------------------------------业务区域------------------------------------------------*/
    // 适配器
    @Inject
    CityListAdapter mCityAdapter;

    // 基本对象
    @Inject
    AccountManager accountManager;

    // 是否手动定位跳转过来的
    private boolean isManual = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mCityAdapter = null;
        this.accountManager = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCityPickerComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_city_picker;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        setTitle("选择城市");

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            isManual = bundle.getBoolean("isManual", false);
        }

        // 设置适配器
        mListView.setAdapter(mCityAdapter);

        // 绑定悬浮框
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(letter -> {
            int position = mCityAdapter.getLetterPosition(letter);
            mListView.setSelection(position);
        });

        if (isManual) {
            // 手动定位
            mCityAdapter.updateLocateState(0, null);
        } else {
            mCityAdapter.updateLocateState(1, accountManager.getCity());
        }

        if (mPresenter != null) {
            mPresenter.initData();
        }

        mCityAdapter.setOnCityClickListener((id, name) -> {
            // 验证一下经纬度，如果没有经纬度则需要初始一个默认经纬度
            if (TextUtils.isEmpty(accountManager.getProvince()))
                accountManager.updateLocation(22.549795059798033, 114.07030793044207, 72.81403f, "广东省", "深圳市", "福田区", "广东省深圳市福田区鹏程路5-1");
            // 设置已选择的城市
            accountManager.setSelectCityId(id);
            accountManager.setSelectCity(name);
            // 返回选择的城市
            EventBus.getDefault().post(new MainEvent(EventBusTags.SWITCH_CITIES_TAG, name), EventBusTags.HOME_TAG);
            killMyself();
        });
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {
        if (mLoading != null)
            mLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (mLoading != null)
            mLoading.setVisibility(View.GONE);
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
        finish();
    }
}
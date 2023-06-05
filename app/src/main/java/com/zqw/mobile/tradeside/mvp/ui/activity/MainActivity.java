package com.zqw.mobile.tradeside.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.qiangxi.checkupdatelibrary.CheckUpdateOption;
import com.qiangxi.checkupdatelibrary.Q;
import com.zqw.mobile.tradeside.R;
import com.zqw.mobile.tradeside.app.global.Constant;
import com.zqw.mobile.tradeside.app.utils.CommonUtils;
import com.zqw.mobile.tradeside.app.utils.EventBusTags;
import com.zqw.mobile.tradeside.di.component.DaggerMainComponent;
import com.zqw.mobile.tradeside.mvp.contract.MainContract;
import com.zqw.mobile.tradeside.mvp.model.entity.AppUpdate;
import com.zqw.mobile.tradeside.mvp.model.entity.MainEvent;
import com.zqw.mobile.tradeside.mvp.presenter.MainPresenter;
import com.zqw.mobile.tradeside.mvp.ui.fragment.HomeFragment;
import com.zqw.mobile.tradeside.mvp.ui.fragment.MyFragment;
import com.zqw.mobile.tradeside.mvp.ui.fragment.TradingHallFragment;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Description:首页
 * <p>
 * Created on 2023/05/31 17:16
 *
 * @author 赤槿
 * module name is MainActivity
 */
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    /*----------------------------------------控件信息----------------------------------------*/
    @BindView(R.id.activity_main)
    ConstraintLayout contentLayout;                                                                 // 总布局

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mNavigation;                                                               // 底部导航

    /*--------------------------------业务信息--------------------------------*/
    private static final String POSITION = "position";
    private static final String SELECT_ITEM = "bottomNavigationSelectItem";
    private int position;                                                                           // 保存当前tab下标
    private long firstClickTime = 0;                                                                // 双击刷新功能 - 记录点击时间

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_TRADINGHALL = 1;
    private static final int FRAGMENT_MY = 2;

    // 首页
    private HomeFragment tabHome;
    // 交易大厅
    private TradingHallFragment tabTradingHall;
    // 我的
    private MyFragment tabMe;

    // 交易大厅数量
    private BadgeDrawable mTradingHallNum;

    @Override
    protected void onDestroy() {
        // 注销服务广播
        this.unregisterReceiver(mReceiver);
        this.mReceiver = null;
        this.mTradingHallNum = null;
        super.onDestroy();
        this.mNavigation = null;
        this.tabHome = null;
        this.tabTradingHall = null;
        this.tabMe = null;

    }

    /**
     * 关闭滑动返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 初始化消息
        initNoticeReceiver();

        // 初始化
        mNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_warehouse:
                    showFragment(FRAGMENT_HOME);
                    doubleClick(FRAGMENT_HOME);
                    onClickHome();
                    break;
                case R.id.action_tradinghall:
                    showFragment(FRAGMENT_TRADINGHALL);
                    doubleClick(FRAGMENT_TRADINGHALL);
                    break;
                case R.id.action_me:
                    showFragment(FRAGMENT_MY);
                    onClickMe();
                    break;
            }
            return true;
        });

        if (savedInstanceState != null) {
            tabHome = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getName());
            tabTradingHall = (TradingHallFragment) getSupportFragmentManager().findFragmentByTag(TradingHallFragment.class.getName());
            tabMe = (MyFragment) getSupportFragmentManager().findFragmentByTag(MyFragment.class.getName());
            // 恢复 recreate 前的位置
            showFragment(savedInstanceState.getInt(POSITION));
            mNavigation.setSelectedItemId(savedInstanceState.getInt(SELECT_ITEM));
        } else {
            showFragment(FRAGMENT_HOME);
        }

        if (mPresenter != null) {
            mPresenter.initData(getIntent().getExtras());
        }

        initUnread();
    }

    /**
     * 初始化未读
     */
    private void initUnread() {
        mTradingHallNum = mNavigation.getOrCreateBadge(R.id.action_tradinghall);
        // 默认隐藏
        mTradingHallNum.setVisible(false);
    }

    /**
     * 点击首页
     */
    public void onClickHome() {
//        if (tabHome != null)
//            tabHome.getNotReadChatCount();
    }

    /**
     * 刷新消息
     */
    public void onRefreshMessage() {
//        if (tabTradingHall != null)
//            tabTradingHall.onDoubleClick();
    }

    /**
     * 点击进入“我的”
     */
    private void onClickMe() {
//        if (tabMe != null)
//            tabMe.getStatus();
    }

    /**
     * 初始化消息通知
     */
    private void initNoticeReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//        filter.addAction(MESSAGE_RECEIVED_ACTION);
//        registerReceiver(mReceiver, filter);
//
//        //主动获取一次
//        String regId = JPushInterface.getRegistrationID(this);
//        if (!TextUtils.isEmpty(regId)) {
//            JPushTools.noticeUIDeveiceRegIdSucc(this, regId);
//        }
    }

    /**
     * 事件    回调
     */
    @Subscriber(tag = EventBusTags.HOME_TAG, mode = ThreadMode.POST)
    private void eventBusEvent(MainEvent mainEvent) {
//        if (mainEvent.getCode() == EventBusTags.VOICE_PLAYBACK_TAG) {
//            // 语音播报
//            if (mPresenter != null) {
//                mPresenter.onVoiceBroadcast(mainEvent.getMsg());
//            }
//        }

        // 即时通讯
//        if (mainEvent.getCode() == EventBusTags.CHAT_NOT_MESSAGE_TAG) {
//            if (mainEvent.getPosition() > 0) {
//                // 有未读数量
//                msgNum.setVisibility(View.VISIBLE);
//            } else {
//                msgNum.setVisibility(View.INVISIBLE);
//            }
//        }
    }


    @Override
    public void onBackPressed() {
        CommonUtils.exitSys(getActivity());
    }

    /**
     * 显示Fragment
     */
    private void showFragment(int index) {
        this.position = index;

        // 第一步，隐藏所有Fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragment(ft);

        // 第二步，显示指定的Fragment
        switch (index) {
            case FRAGMENT_HOME:
                if (tabHome == null) {
                    tabHome = new HomeFragment();
                    ft.add(R.id.container, tabHome, HomeFragment.class.getName());
                } else {
                    ft.show(tabHome);
                }
                break;
            case FRAGMENT_TRADINGHALL:
                if (tabTradingHall == null) {
                    tabTradingHall = new TradingHallFragment();
                    ft.add(R.id.container, tabTradingHall, TradingHallFragment.class.getName());
                } else {
                    ft.show(tabTradingHall);
                }
                break;
            case FRAGMENT_MY:
                if (tabMe == null) {
                    tabMe = new MyFragment();
                    ft.add(R.id.container, tabMe, MyFragment.class.getName());
                } else {
                    ft.show(tabMe);
                }
        }

        ft.commit();
    }

    /**
     * 隐藏Fragment
     */
    private void hideFragment(FragmentTransaction ft) {
        // 如果不为空，就先隐藏起来
        if (tabHome != null) {
            ft.hide(tabHome);
        }

        if (tabTradingHall != null) {
            ft.hide(tabTradingHall);
        }

        if (tabMe != null) {
            ft.hide(tabMe);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // recreate 时记录当前位置 (在 Manifest 已禁止 Activity 旋转,所以旋转屏幕并不会执行以下代码)
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, position);
        outState.putInt(SELECT_ITEM, mNavigation.getSelectedItemId());
    }

    /**
     * 双击刷新界面
     */
    public void doubleClick(int index) {
        long secondClickTime = System.currentTimeMillis();
        if ((secondClickTime - firstClickTime < 500)) {
            if (index == FRAGMENT_HOME) {
                tabHome.onDoubleClick();
            } else if (index == FRAGMENT_TRADINGHALL) {
                tabTradingHall.onDoubleClick();
            }
        } else {
            firstClickTime = secondClickTime;
        }
    }

    @Override
    public void askDialog(AppUpdate info) {
        Q.show(this, new CheckUpdateOption.Builder()
                .setAppName(info.getName())
                .setFileName("/" + info.getFileName())
                .setFilePath(Constant.APP_UPDATE_PATH)
//                .setImageUrl("http://imgsrc.baidu.com/imgad/pic/item/6c224f4a20a446233d216c4f9322720e0cf3d730.jpg")
                .setImageResId(R.mipmap.icon_upgrade_logo)
                .setIsForceUpdate(info.getForce() == 1)
                .setNewAppSize(info.getNewAppSize())
                .setNewAppUpdateDesc(info.getNewAppUpdateDesc())
                .setNewAppUrl(info.getFilePath())
                .setNewAppVersionName(info.getVerName())
                .setNotificationSuccessContent("下载成功，点击安装")
                .setNotificationFailureContent("下载失败，点击重新下载")
                .setNotificationIconResId(R.mipmap.ic_launcher)
                .setNotificationTitle(getString(R.string.app_name))
                .build(), (view, imageUrl) -> {
            // 下载图片
//            view.setScaleType(ImageView.ScaleType.FIT_XY);
//            mImageLoader.loadImage(getActivity(),
//                    ImageConfigImpl
//                            .builder()
//                            .url(imageUrl)
//                            .imageView(view)
//                            .build());
        });
    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
//                //第一步，验证当前广播是否是(注册的设备ID号)
//                String regid = intent.getStringExtra(JPushTools.DEVICE_REG_ID);
//                if (!TextUtils.isEmpty(regid)) {
//                    if (mPresenter != null) {
//                        mPresenter.uploadJPushRegId(regid);
//                    }
//                }
//
//                //第二步，验证当前广播是否是(推送过来的消息)
//                String extraMsg = intent.getStringExtra(JPushTools.EXTRA_MESSAGE);
//                if (!TextUtils.isEmpty(extraMsg)) {
//
//                }
//
//                // 第三步，此消息是：弹出新订单
//                boolean newOrderMsg = intent.getBooleanExtra(JPushTools.POPUP_NEWORDER_MESSAGE, false);
//                if (newOrderMsg) {
//                    // 提示首页弹出新订单
//                    PopupPushMessage mPushMessage = new PopupPushMessage(getApplicationContext(),
//                            intent.getStringExtra(JPushTools.MESSAGE_TITLE),
//                            intent.getStringExtra(JPushTools.MESSAGE_CONTENT),
//                            intent.getStringExtra(JPushTools.MESSAGE_VOICE), click -> {
//                        if (click) {
//                            // 确定(不做任何处理)
//                            tabHome.onDoubleClick();
//                        } else {
//                            // 订单详情
//                            Bundle bundle = new Bundle();
//                            bundle.putString("orderNo", intent.getStringExtra(JPushTools.ORDER_NO_CONTENT));
//                            ActivityUtils.startActivity(bundle, OrdersDetailsActivity.class);
//                        }
//                    });
//                    mPushMessage.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
//                }
//
//                // 第四步，此消息是：通知
//                boolean notice = intent.getBooleanExtra(JPushTools.MESSAGE_NOTICE, false);
//                if (notice) {
//                    // 声音提醒
//                    EventBusManager.getInstance().post(new MainEvent(EventBusTags.VOICE_PLAYBACK_TAG, intent.getStringExtra(JPushTools.MESSAGE_VOICE)), EventBusTags.HOME_TAG);
//                    // 广播消息
//                    if (tabHome != null) {
//                        List<String> list = new ArrayList<>();
//                        list.add(intent.getStringExtra(JPushTools.MESSAGE_CONTENT));
//                        tabHome.setAnnouncement(list);
//                        tabHome.onDoubleClick();
//                    }
//                }
//            }
        }
    };

    public Activity getActivity() {
        return this;
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
        finish();
    }
}
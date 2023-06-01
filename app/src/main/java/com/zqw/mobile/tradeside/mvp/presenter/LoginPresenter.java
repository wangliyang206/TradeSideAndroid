package com.zqw.mobile.tradeside.mvp.presenter;

import static com.zqw.mobile.tradeside.BuildConfig.IS_DEBUG_DATA;

import android.os.Bundle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.blankj.utilcode.util.RegexUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DeviceUtils;
import com.zqw.mobile.tradeside.app.global.AccountManager;
import com.zqw.mobile.tradeside.app.utils.RxUtils;
import com.zqw.mobile.tradeside.mvp.contract.LoginContract;
import com.zqw.mobile.tradeside.mvp.model.entity.AppUpdate;
import com.zqw.mobile.tradeside.mvp.model.entity.CommonResponse;
import com.zqw.mobile.tradeside.mvp.model.entity.LoginResponse;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * ================================================
 * Description:登录
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AccountManager mAccountManager;
    /**
     * 当前状态是否是首次启动登录
     */
    private Boolean isStartLogin = true;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
        super(model, rootView);
    }


    /**
     * 获取传进来的值
     */
    public void getBundleValues(Bundle bundle) {
        if (bundle != null) {
            isStartLogin = bundle.getBoolean("isStartLogin", true);
        }
    }

    /**
     * 初始化默认值
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        if (IS_DEBUG_DATA) {
            mRootView.setUsernName("15032134297");
            mRootView.setPassword("123456");
        } else {
            mRootView.setUsernName(mAccountManager.getAccount());
            mRootView.setPassword(mAccountManager.getPassword());
        }

        //APP升级更新
        if (isStartLogin)
            checkUpdateManager();
    }

    /**
     * 点击登录按钮
     */
    public void btnLoginOnClick(String username, String password, boolean mSwitchLogin, boolean isAgreement) {
//        测试Bugly上报
//        CrashReport.testJavaCrash();
        // 用户输入有效性验证
        if (!checkInput(username, password, mSwitchLogin, isAgreement)) {
            return;
        }

        if (mSwitchLogin) {
            // 短信验证码登录
            loginSMS(username);
        } else {
            // 账号密码登录
            login(username, password);
        }
    }


    /**
     * 短信验证码登录
     */
    private void loginSMS(String username) {
        if (IS_DEBUG_DATA) {
            mRootView.jumbToQuickLogin(username);
        } else {
            // 执行登录操作
            mModel.loginSMS(username)
                    .compose(RxUtils.applySchedulers(mRootView))                                    // 切换线程
                    .subscribe(new ErrorHandleSubscriber<CommonResponse>(mErrorHandler) {
                        @Override
                        public void onNext(CommonResponse loginResponse) {
                            mRootView.jumbToQuickLogin(username);
                        }
                    });
        }
    }

    /**
     * 账号密码登录
     */
    private void login(String username, String password) {

        if (IS_DEBUG_DATA) {
            mAccountManager.saveAccountInfo(username, password, new LoginResponse("50154b09-14bd-49bf-87a7-d53ce5d29f80", "R000014", "吴存柏", "18124027304", "http://q.qlogo.cn/qqapp/1105462061/00EF3352A990BEAA5C066F45655A4A06/100"));
            mRootView.jumbToMain();
        } else {
            // 执行登录操作
            mModel.toLogin(username, password)
                    .compose(RxUtils.applySchedulers(mRootView))                               // 切换线程
                    .subscribe(new ErrorHandleSubscriber<LoginResponse>(mErrorHandler) {
                        @Override
                        public void onNext(LoginResponse loginResponse) {
                            if (loginResponse != null) {
                                mAccountManager.saveAccountInfo(username, password, loginResponse);
                            }

                            mRootView.jumbToMain();
                        }
                    });
        }
    }

    /**
     * 用户输入有效性验证
     *
     * @return 校验是否通过
     */
    private boolean checkInput(String username, String password, boolean mSwitchLogin, boolean isAgreement) {
        // 用户名和密码不能为空，为空时返回false同时给出提示。
        if ("".equals(username)) {
            mRootView.showMessage("请输入手机号");
            return false;
        }
        if (!RegexUtils.isMobileSimple(username)) {
            mRootView.showMessage("请输入有效的手机号！");
            return false;
        }

        if (!mSwitchLogin) {
            if ("".equals(password)) {
                mRootView.showMessage("请输入密码");
                return false;
            }
            if (password.length() < 6 || password.length() > 20) {
                mRootView.showMessage("密码长度为6-20位，建议字母与数字组合");
                return false;
            }
        }

        if (!isAgreement) {
            mRootView.showMessage("请阅读并同意相关协议");
            return false;
        }
        return true;
    }


    /**
     * APP升级更新
     */
    private void checkUpdateManager() {
        if (IS_DEBUG_DATA) {
            AppUpdate appUpdate = new AppUpdate(112, "1.1.2", "小的来了", "小的来了.apk", "http:\\/\\/buypb.e1.luyouxia.net:28708\\/imgs\\/update\\/recycleapp-debug-1.1.2.apk", 0, 18, "1.更新了好多东西\n1.更新了好多东西\n1.更新了好多东西");
            if (haveNew(appUpdate)) {
                // 先提醒升级
                mRootView.askDialog(appUpdate);
            }
        } else {
            mModel.getVersion("android")
                    .compose(RxUtils.applySchedulers(mRootView, true, true)) // 切换线程
                    .subscribe(new ErrorHandleSubscriber<AppUpdate>(mErrorHandler) {
                        @Override
                        public void onError(Throwable t) {
                            // 不做任何处理
                        }

                        @Override
                        public void onNext(AppUpdate au) {
                            if (haveNew(au)) {
                                // 先提醒升级
                                mRootView.askDialog(au);
                            }
                        }
                    });
        }
    }

    /**
     * 版本号比较
     *
     * @return 是否升级
     */
    private boolean haveNew(AppUpdate appUpdate) {
        boolean haveNew = false;
        if (appUpdate == null) {
            return false;
        }

        int curVersionCode = DeviceUtils.getVersionCode(mRootView.getActivity().getApplicationContext());
        if (curVersionCode < appUpdate.getVerCode()) {
            haveNew = true;
        }
        return haveNew;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAccountManager = null;
    }
}
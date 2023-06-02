package com.zqw.mobile.tradeside.mvp.presenter;

import static com.zqw.mobile.tradeside.BuildConfig.IS_DEBUG_DATA;

import android.text.TextUtils;

import com.blankj.utilcode.util.RegexUtils;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import com.zqw.mobile.tradeside.app.utils.RxUtils;
import com.zqw.mobile.tradeside.mvp.contract.ForgotPasswordContract;
import com.zqw.mobile.tradeside.mvp.model.entity.CommonResponse;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * ================================================
 * Description:忘记密码
 * <p>
 * Created by MVPArmsTemplate on 2023/06/01 17:58
 * ================================================
 */
@ActivityScope
public class ForgotPasswordPresenter extends BasePresenter<ForgotPasswordContract.Model, ForgotPasswordContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    /**
     * 获取手机验证码倒计时时间(秒)
     */
    private final int mobileCheckCodeTime = 60;// 1分钟

    @Inject
    public ForgotPasswordPresenter(ForgotPasswordContract.Model model, ForgotPasswordContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 发送验证码
     */
    public void btnSendSMSOnClick(String account) {
        if (TextUtils.isEmpty(account)) {
            mRootView.showMessage("请输入手机号");
            return;
        }

        if (!RegexUtils.isMobileSimple(account)) {
            mRootView.showMessage("您输入的手机号格式错误");
            return;
        }

        if (IS_DEBUG_DATA) {
            onCountDown();
        } else {
            mModel.sendSms(account)
                    .compose(RxUtils.applySchedulers(mRootView))                                    // 切换线程
                    .subscribe(new ErrorHandleSubscriber<CommonResponse>(mErrorHandler) {
                        @Override
                        public void onError(Throwable t) {
                            super.onError(t);

                            // 发送失败后禁用“下一步”按钮
                            mRootView.onDisableClick();
                        }

                        @Override
                        public void onNext(CommonResponse commonResponse) {
                            onCountDown();
                        }
                    });
        }

    }


    /**
     * 倒计时
     */
    private void onCountDown() {
        // 验证码已经发送成功，所以“下一步”可以点击了
        mRootView.onEnableClick();

        // 开始做倒计时效果
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(mobileCheckCodeTime + 1)                                                      // 超过多少秒停止执行
                .map(aLong -> {
                    return mobileCheckCodeTime - aLong;                                             // 由于是倒计时，需要将倒计时的数字反过来
                })
                .observeOn(AndroidSchedulers.mainThread())                                          // 切换线程
                .doOnSubscribe(disposable -> {
                    // 在执行的过程中按钮不可以点击
                    mRootView.onDisableSendVerificationCodeButton();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))                               // 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long num) {
                        // 显示倒计时
                        mRootView.setCountDownTips(num + "s");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        // 在执行完毕后按钮才可以点击
                        mRootView.onEnableSendVerificationCodeButton();
                    }
                });
    }

    /**
     * 提交
     */
    public void btnSubmit(String account, String verifyCode, String password, String confimPwd) {
        if (checkInput(account, verifyCode, password, confimPwd)) {
            // 改变按钮颜色
            mRootView.onDisableClick();

            if (IS_DEBUG_DATA) {
                mRootView.onResetSuccess();
            } else {
                mModel.forgotPassword(account, verifyCode, password)
                        .compose(RxUtils.applySchedulers(mRootView))                                // 切换线程
                        .subscribe(new ErrorHandleSubscriber<CommonResponse>(mErrorHandler) {
                            @Override
                            public void onError(Throwable t) {
                                super.onError(t);
                                mRootView.onResetError();
                            }

                            @Override
                            public void onNext(CommonResponse commonResponse) {
                                mRootView.onResetSuccess();
                            }
                        });
            }
        }
    }

    /**
     * 用户输入有效性验证
     *
     * @return 校验是否通过
     */
    private boolean checkInput(String account, String verifyCode, String password, String confimPwd) {

        if (TextUtils.isEmpty(account)) {
            mRootView.showMessage("请输入手机号");
            return false;
        }

        if (!RegexUtils.isMobileSimple(account)) {
            mRootView.showMessage("您输入的手机号格式错误");
            return false;
        }

        if (TextUtils.isEmpty(verifyCode)) {
            mRootView.showMessage("请输入验证码");
            return false;
        }

        if ("".equals(password)) {
            mRootView.showMessage("您输入的密码不能为空！");
            return false;
        }

        if (password.length() < 6 || password.length() > 20) {
            mRootView.showMessage("密码长度为6-20位，建议字母与数字组合");
            return false;
        }

        if (!password.equals(confimPwd)) {
            mRootView.showMessage("您两次输入的密码不一致");
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
    }
}
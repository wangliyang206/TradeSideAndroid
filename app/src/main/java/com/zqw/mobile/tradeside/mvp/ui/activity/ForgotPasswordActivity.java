package com.zqw.mobile.tradeside.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.KeyboardUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.tradeside.R;
import com.zqw.mobile.tradeside.app.utils.EventBusTags;
import com.zqw.mobile.tradeside.di.component.DaggerForgotPasswordComponent;
import com.zqw.mobile.tradeside.mvp.contract.ForgotPasswordContract;
import com.zqw.mobile.tradeside.mvp.model.entity.MainEvent;
import com.zqw.mobile.tradeside.mvp.presenter.ForgotPasswordPresenter;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:忘记密码
 * <p>
 * Created on 2023/06/02 09:24
 *
 * @author 赤槿
 * module name is ForgotPasswordActivity
 */
public class ForgotPasswordActivity extends BaseActivity<ForgotPasswordPresenter> implements ForgotPasswordContract.View {

    /*----------------------------------------------------------------控件信息----------------------------------------------------------------*/

    @BindView(R.id.edit_forgotpasswordactivity_phone)
    EditText edtTxtPhoneNumber;                                                                     // 手机号

    @BindView(R.id.edit_forgotpasswordactivity_password)
    EditText edtTxtPassWord;                                                                        // 密码

    @BindView(R.id.edit_forgotpasswordactivity_confimpwd)
    EditText edtTxtConfimPwd;                                                                       // 确认密码

    @BindView(R.id.edit_forgotpasswordactivity_sendverifycode)
    EditText edtTextVerifyCode;                                                                     // 验证码

    @BindView(R.id.btn_forgotpasswordactivity_sendverifycode)
    Button btnSendVerifyCode;                                                                       // 发送验证码

    @BindView(R.id.btn_forgotpasswordactivity_confim)
    Button btnConfim;                                                                               // 确认(提交)

    /*----------------------------------------------------------------业务信息----------------------------------------------------------------*/
    /**
     * 对话框
     */
    private MaterialDialog mDialog;

    @Override
    protected void onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(getWindow());
        if (mDialog != null) {
            this.mDialog.dismiss();
        }
        super.onDestroy();
        this.mDialog = null;
    }

    /**
     * 将状态栏改为浅色模式(状态栏 icon 和字体会变成深色)
     */
    public boolean useLightStatusBar() {
        return true;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerForgotPasswordComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_forgot_password;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 设置标题
        setTitle("忘记密码");
        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this).content(R.string.common_execute).progress(true, 0).build();

    }

    @OnClick({
            R.id.btn_forgotpasswordactivity_sendverifycode,                                         // 发送验证码
            R.id.btn_forgotpasswordactivity_confim,                                                 // 确认(提交)
    })
    @Override
    public void onClick(View v) {
        hideInput();
        if (v == btnSendVerifyCode) {                                                               // 发送验证码
            String account = edtTxtPhoneNumber.getText().toString().trim();
            if (mPresenter != null) {
                mPresenter.btnSendSMSOnClick(account);
            }

        } else if (v == btnConfim) {                                                                // 确认(提交)
            String account = edtTxtPhoneNumber.getText().toString().trim();
            String verifyCode = edtTextVerifyCode.getText().toString().trim();
            String password = edtTxtPassWord.getText().toString().trim();
            String confimPwd = edtTxtConfimPwd.getText().toString().trim();

            // 执行提交
            if (mPresenter != null) {
                mPresenter.btnSubmit(account, verifyCode, password, confimPwd);
            }
        }
    }

    // 隐藏软键盘
    private void hideInput() {
        KeyboardUtils.hideSoftInput(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideInput();
        return super.onTouchEvent(event);
    }


    @Override
    public void onDisableSendVerificationCodeButton() {
        btnSendVerifyCode.setEnabled(false);
    }

    @Override
    public void onEnableSendVerificationCodeButton() {
        btnSendVerifyCode.setEnabled(true);
        // 重新设置按钮“提示”
        btnSendVerifyCode.setText("发送验证码");
    }

    @Override
    public void setCountDownTips(String str) {
        // 显示倒计时
        btnSendVerifyCode.setText(str);
    }

    @Override
    public void onDisableClick() {
        btnConfim.setEnabled(false);
        btnConfim.setClickable(false);
    }

    @Override
    public void onEnableClick() {
        btnConfim.setEnabled(true);
        btnConfim.setClickable(true);
    }

    /**
     * 密码已找回
     */
    @Override
    public void onResetSuccess() {
        hideInput();
        showMessage("密码已找回，请使用新密码重新登录。");
        EventBus.getDefault().post(new MainEvent(EventBusTags.COMM_RESULT_TAG, edtTxtPhoneNumber.getText().toString().trim()), EventBusTags.FORGOTPASSWORD_TAG);
        killMyself();
    }

    /**
     * 密码找回失败
     */
    @Override
    public void onResetError() {
        onEnableClick();
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {
        if (mDialog != null)
            mDialog.show();
    }

    @Override
    public void hideLoading() {
        if (mDialog != null)
            mDialog.cancel();
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
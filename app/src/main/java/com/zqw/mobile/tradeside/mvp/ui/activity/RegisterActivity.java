package com.zqw.mobile.tradeside.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.tradeside.R;
import com.zqw.mobile.tradeside.app.global.Constant;
import com.zqw.mobile.tradeside.di.component.DaggerRegisterComponent;
import com.zqw.mobile.tradeside.mvp.contract.RegisterContract;
import com.zqw.mobile.tradeside.mvp.presenter.RegisterPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:注册
 * <p>
 * Created on 2023/06/01 17:58
 *
 * @author 赤槿
 * module name is RegisterActivity
 */
public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {

    /*----------------------------------------------------------------控件信息----------------------------------------------------------------*/
    @BindView(R.id.edit_registeractivity_name)
    EditText editName;                                                                              // 姓名

    @BindView(R.id.edit_registeractivity_phone)
    EditText editMobile;                                                                            // 手机号

    @BindView(R.id.edit_registeractivity_password)
    EditText editPassword;                                                                          // 密码

    @BindView(R.id.edit_registeractivity_sendverifycode)
    EditText editVerifyCode;                                                                        // 验证码

    @BindView(R.id.btn_registeractivity_sendverifycode)
    Button btnSendVerifyCode;                                                                       // 发送验证码

    @BindView(R.id.btn_registeractivity_confim)
    Button btnConfim;                                                                               // 确认(提交)

    @BindView(R.id.checkbox_registeractivity_cb)
    CheckBox checkBox;                                                                              // 勾选协议

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
        this.checkBox = null;
    }

    /**
     * 将状态栏改为浅色模式(状态栏 icon 和字体会变成深色)
     */
    public boolean useLightStatusBar() {
        return true;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRegisterComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_register;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 设置标题
        setTitle("用户注册");
        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this).content(R.string.common_execute).progress(true, 0).build();
    }

    @OnClick({
            R.id.txvi_registeractivity_tips1,
            R.id.txvi_registeractivity_tips2,                                                       // 服务协议
            R.id.txvi_registeractivity_tips3,
            R.id.txvi_registeractivity_tips4,                                                       // 隐私政策
            R.id.btn_registeractivity_sendverifycode,                                               // 发送验证码
            R.id.btn_registeractivity_confim,                                                       // 确认(提交)
    })
    @Override
    public void onClick(View v) {
        hideInput();
        if (v == btnSendVerifyCode) {                                                               // 发送验证码
            String account = editMobile.getText().toString().trim();
            if (mPresenter != null) {
                mPresenter.btnSendSMSOnClick(account);
            }

        } else if (v == btnConfim) {                                                                // 确认(提交)
            String name = editName.getText().toString().trim();
            String account = editMobile.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String verifyCode = editVerifyCode.getText().toString().trim();

            // 执行提交
            if (mPresenter != null && mPresenter.checkInput(name, account, password, verifyCode, checkBox.isChecked())) {
                mPresenter.btnSubmit(name, account, password, verifyCode);
            }
        } else if (v.getId() == R.id.txvi_registeractivity_tips1 || v.getId() == R.id.txvi_registeractivity_tips3) { // 我同意协议
            checkBox.setChecked(!checkBox.isChecked());
        } else if (v.getId() == R.id.txvi_registeractivity_tips2) {                                 // 服务协议
            onJump(true);
        } else if (v.getId() == R.id.txvi_registeractivity_tips4) {                                 // 隐私政策
            onJump(false);
        }
    }

    /**
     * 查看服务协议
     */
    private void onJump(boolean isServiceAgreement) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isShowTop", true);
        if (isServiceAgreement) {
            bundle.putString("URL", Constant.serviceAgreementUrl);
            bundle.putString("TITLE", "服务协议");
            ActivityUtils.startActivity(bundle, NewWindowX5Activity.class);
        } else {
            bundle.putString("URL", Constant.privacyPolicyUrl);
            bundle.putString("TITLE", "隐私政策");
            ActivityUtils.startActivity(bundle, NewWindowX5Activity.class);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideInput();
        return super.onTouchEvent(event);
    }

    /**
     * 隐藏软键盘
     */
    private void hideInput() {
        KeyboardUtils.hideSoftInput(this);
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
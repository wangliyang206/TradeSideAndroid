package com.zqw.mobile.tradeside.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.qiangxi.checkupdatelibrary.CheckUpdateOption;
import com.qiangxi.checkupdatelibrary.Q;
import com.zqw.mobile.tradeside.R;
import com.zqw.mobile.tradeside.app.dialog.PopupTipsDialog;
import com.zqw.mobile.tradeside.app.global.AccountManager;
import com.zqw.mobile.tradeside.app.global.Constant;
import com.zqw.mobile.tradeside.app.utils.EventBusTags;
import com.zqw.mobile.tradeside.app.utils.MyClickableSpan;
import com.zqw.mobile.tradeside.di.component.DaggerLoginComponent;
import com.zqw.mobile.tradeside.mvp.contract.LoginContract;
import com.zqw.mobile.tradeside.mvp.model.entity.AppUpdate;
import com.zqw.mobile.tradeside.mvp.model.entity.MainEvent;
import com.zqw.mobile.tradeside.mvp.presenter.LoginPresenter;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:登录
 * <p>
 * Created on 2023/05/31 17:11
 *
 * @author 赤槿
 * module name is LoginActivity
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View, TextWatcher, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
    @BindView(R.id.activity_login)
    LinearLayout contentLayout;                                                                     // 界面总布局

    @BindView(R.id.edttxt_loginactivity_username)
    EditText edtTxtUsername;                                                                        // 用户名输入框
    @BindView(R.id.imgvi_loginactivity_userclose_icon)
    ImageView imgviUserClose;                                                                       // 清空用户名输入框

    @BindView(R.id.edttxt_loginactivity_password)
    EditText edtTxtPassword;                                                                        // 密码输入框
    @BindView(R.id.imgvi_loginactivity_passwordclose_icon)
    ImageView imgviPwdClose;                                                                        // 清空密码输入框
    @BindView(R.id.imgvi_modifyloginpwdactivity_password_see)
    ImageView imgviPwdsee;                                                                          // 密码可见

    @BindView(R.id.txvi_loginactivity_forgotpassword)                                               // 获取验证码、忘记密码
    TextView txviForgotPassword;

    @BindView(R.id.txvi_loginactivity_switchlogin)
    TextView txviSwitchLogin;                                                                       // 切换登录方式：短信验证码登录、账号密码登录

    @BindView(R.id.btn_loginactivity_submit)
    Button btnLogin;                                                                                // 登录

    @BindView(R.id.txvi_loginactivity_tips)
    TextView txviAgreement;                                                                         // 协议提示
    @BindView(R.id.checkbox_loginactivity_cb)
    CheckBox checkBox;                                                                              // 勾选协议
    /*------------------------------------------------业务信息------------------------------------------------*/
    @Inject
    AccountManager mAccountManager;
    // 登录对话框
    private MaterialDialog mDialog;

    // 密码是否可见
    private Boolean isVisible = false;
    // 是否退出APP
    private boolean isExitAPP = false;
    // 登录方式：true 短信验证码、false 账号密码登录
    private boolean mSwitchLogin = false;

    @Override
    protected void onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(getWindow());
        if (mDialog != null) {
            this.mDialog.dismiss();
        }

        if (isExitAPP) {
            if (mPresenter != null) {
                mPresenter.stopLocationService();
            }
        }
        super.onDestroy();
        this.mDialog = null;

        this.mAccountManager = null;
    }

    /**
     * 关闭滑动返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String phone = bundle.getString("phone");
            if (TextUtils.isEmpty(phone)) {
                setUsernName(mAccountManager.getAccount());
                setPassword(mAccountManager.getPassword());
            } else {
                // 从注册过来的
                setUsernName(phone);
            }

            String password = bundle.getString("password");
            if (!TextUtils.isEmpty(password)) {
                setPassword(password);
            }
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 不需要默认事件
        getIntent().putExtra("isInitToolbar", true);

        SpannableString agreement = new SpannableString(getString(R.string.login_agreement_tips));
        agreement.setSpan(new MyClickableSpan("《服务协议》"), 7, 13, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        agreement.setSpan(new MyClickableSpan("《隐私政策》"), 14, 20, SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        if (txviAgreement != null) {
            txviAgreement.setText(agreement);
            txviAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        }

        edtTxtPassword.setTypeface(Typeface.DEFAULT);
        edtTxtPassword.setTransformationMethod(new PasswordTransformationMethod());
        /*---------------增加监听--------------*/
        edtTxtUsername.addTextChangedListener(this);
        edtTxtPassword.addTextChangedListener(this);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnLogin.setEnabled(isChecked);
        });

        // 初始化Loading对话框
        mDialog = new MaterialDialog.Builder(this).content(R.string.loginactivity_login_processtips).progress(true, 0).build();
    }

    /**
     * 忘记密码    回调
     */
    @Subscriber(tag = EventBusTags.FORGOTPASSWORD_TAG, mode = ThreadMode.POST)
    private void eventBusEvent(MainEvent mainEvent) {
        if (mainEvent.getCode() == EventBusTags.COMM_RESULT_TAG) {
            setUsernName(mainEvent.getMsg());

            edtTxtPassword.setText("");
            edtTxtPassword.setFocusable(true);
            edtTxtPassword.setFocusableInTouchMode(true);
            edtTxtPassword.requestFocus();
        }
    }

    /**
     * 点击事件
     */
    @OnClick({
            R.id.imvi_login_close,                                                                  // 返回
            R.id.btn_loginactivity_submit,                                                          // 登录or获取验证码
            R.id.txvi_loginactivity_register,                                                       // 注册按钮
            R.id.imgvi_loginactivity_userclose_icon,                                                // 清空用户名输入框
            R.id.imgvi_loginactivity_passwordclose_icon,                                            // 清空密码输入框
            R.id.imgvi_modifyloginpwdactivity_password_see,                                         // 密码是否可见
            R.id.txvi_loginactivity_switchlogin,                                                    // 切换登录方式：短信验证码登录、账号密码登录
            R.id.txvi_loginactivity_forgotpassword})
    @Override
    public void onClick(View v) {
        hideInput();
        switch (v.getId()) {
            case R.id.imvi_login_close:                                                             // 返回
                onBackPressed();
                break;
            case R.id.btn_loginactivity_submit:                                                     // 登录
                String username = edtTxtUsername.getText().toString().trim();
                String password = edtTxtPassword.getText().toString().trim();
                if (mPresenter != null) {
                    mPresenter.btnLogin(username, password, mSwitchLogin, checkBox.isChecked());
                }
                break;
            case R.id.txvi_loginactivity_register:                                                  // 新用户注册
                ActivityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.txvi_loginactivity_switchlogin:                                               // 切换登录方式：短信验证码登录、账号密码登录
                switchLogin();
                break;
            case R.id.imgvi_loginactivity_userclose_icon:                                           // 清空用户名输入框
                edtTxtUsername.setText("");
                break;
            case R.id.imgvi_loginactivity_passwordclose_icon:                                       // 清空密码输入框
                edtTxtPassword.setText("");
                break;
            case R.id.imgvi_modifyloginpwdactivity_password_see:                                    // 密码是否可见
                if (isVisible) {                                                                    // 可见
                    isVisible = false;
                    imgviPwdsee.setBackgroundResource(R.mipmap.icon_statistics_see);
                    edtTxtPassword
                            .setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());
                } else {                                                                            // 不可见
                    isVisible = true;
                    imgviPwdsee.setBackgroundResource(R.mipmap.icon_statistics_no_see);
                    edtTxtPassword
                            .setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());
                }
                edtTxtPassword.setSelection(edtTxtPassword.length());
                break;
            case R.id.txvi_loginactivity_forgotpassword:                                            // 忘记密码or获取验证码
                if (mSwitchLogin) {
                    if (mPresenter != null) {
                        mPresenter.loginSMS(edtTxtUsername.getText().toString().trim(), checkBox.isChecked());
                    }
                } else {
                    ActivityUtils.startActivity(ForgotPasswordActivity.class);
                }
                break;
        }
    }

    /**
     * 切换登录方式
     */
    private void switchLogin() {
        if (mSwitchLogin) {                                                                         // 账号密码登录
            mSwitchLogin = false;
            // 提示可以切换短信验证码
            txviSwitchLogin.setText("验证码登录");
            imgviPwdsee.setVisibility(View.VISIBLE);
            txviForgotPassword.setText("忘记密码");
            edtTxtPassword.setTransformationMethod(new PasswordTransformationMethod());
        } else {                                                                                    // 短信验证码
            mSwitchLogin = true;
            // 提示可以切换账号密码登录
            txviSwitchLogin.setText("密码登录");
            imgviPwdsee.setVisibility(View.GONE);
            txviForgotPassword.setText("获取验证码");
            edtTxtPassword.setTransformationMethod(new HideReturnsTransformationMethod());
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideInput() {
        KeyboardUtils.hideSoftInput(edtTxtUsername);
        KeyboardUtils.hideSoftInput(edtTxtPassword);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideInput();
        return super.onTouchEvent(event);
    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        PopupTipsDialog.PopupClick popupClick = click -> {
            if (click) {
                isExitAPP = true;
                AppUtils.exitApp();
            }
        };
        PopupTipsDialog phoneDialog = new PopupTipsDialog(this, "你真的要退出吗？", popupClick);
        phoneDialog.showAtLocation(contentLayout, Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    @Override
    public void setUsernName(String usernName) {
        edtTxtUsername.setText(usernName);
        if (!TextUtils.isEmpty(usernName)) {
            // 设置光标
            edtTxtUsername.setSelection(usernName.length());
            // 显示清空用户名按钮
            imgviUserClose.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setPassword(String password) {
        edtTxtPassword.setText(password);
        if (!TextUtils.isEmpty(password)) {
            // 显示清空密码按钮
            imgviPwdClose.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void jumbToMain() {
        ActivityUtils.startActivity(MainActivity.class);

        // 关闭当前窗口
        killMyself();
    }

    @Override
    public void setTipsValue(Long str) {
        txviForgotPassword.setText(str + "s");
    }

    @Override
    public void setBtnEnabled(boolean val) {
        if (val) {
            // 倒计时完毕
            txviForgotPassword.setText("发送验证码");
            txviForgotPassword.setOnClickListener(this);
        } else {
            // 正在倒计时
            txviForgotPassword.setOnClickListener(null);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int userSize = edtTxtUsername.getText().length();
        if (userSize > 0) {
//			edtTxtUsername.setSelection(userSize);                                                  // 设置光标
            imgviUserClose.setVisibility(View.VISIBLE);                                             // 显示清空用户名按钮
        } else {
            imgviUserClose.setVisibility(View.GONE);
        }

        if (edtTxtPassword.getText().length() > 0) {
            imgviPwdClose.setVisibility(View.VISIBLE);                                              // 显示清空密码按钮
        } else {
            imgviPwdClose.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

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
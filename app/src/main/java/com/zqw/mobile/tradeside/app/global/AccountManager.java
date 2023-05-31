package com.zqw.mobile.tradeside.app.global;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.bugly.crashreport.CrashReport;
import com.zqw.mobile.tradeside.BuildConfig;
import com.zqw.mobile.tradeside.app.utils.AppPreferencesHelper;
import com.zqw.mobile.tradeside.mvp.model.entity.LoginResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * @ Title: AccountManager
 * @ Package com.zqw.mobile.smelter.api
 * @ Description: 用户信息存取类：从SharedPreferences中读取用户登录信息
 * @ author: wly
 * @ date: 2020/09/15 14:24
 */
@Singleton
public final class AccountManager {

    /*----------------------------------------------业务常量-------------------------------------------------*/
    // Token
    private final String TOKEN = "Token";
    // 用户id
    private final String USERID = "Userid";
    // 头像
    private final String PHOTOURL = "PhotoUrl";
    // 账号
    private final String ACCOUNT = "Account";
    // 密码
    private final String PASSWORD = "Password";
    // 用户姓名
    private final String USER_NAME = "UserName";
    // 电话
    private final String RECYCLE_PHONE = "Mobile";

    // 是否提醒过 APP升级
    private final String UPGRADE = "Upgrade";
    /**
     * APP启动次数(达到一定次数后清理日志)
     */
    private final String START_TIME = "startTime";
    /**
     * 是否同意隐私政策
     */
    private static final String PRIVACY_POLICY = "privacyPolicy";
    /*----------------------------------------------操作对象-------------------------------------------------*/

    private AppPreferencesHelper spHelper;

    @Inject
    public AccountManager(Context context) {
        this.spHelper = new AppPreferencesHelper(context.getApplicationContext(), BuildConfig.SHARED_NAME_INVEST, 1);

        updateBugly();
    }

    /**
     * 更新Bugly状态
     */
    private void updateBugly() {
        try {
            String userId = getUserid();
            if (!isLogin())
                userId = "Not Login";
            CrashReport.setUserId(userId);// 记录当前是谁上报的
        } catch (Exception ignored) {
        }
    }

    /**
     * 保存登录信息(登录成功后调用此方法)
     *
     * @param account       账号
     * @param password      密码
     * @param loginResponse 用户信息
     */
    public void saveAccountInfo(String account, String password, LoginResponse loginResponse) {
        spHelper.put(ACCOUNT, account);
        spHelper.put(PASSWORD, password);
        spHelper.put(TOKEN, loginResponse.getToken());

        updateAccountInfo(loginResponse);
    }

    /**
     * 更新登录信息(登录成功后调用此方法)
     *
     * @param loginResponse 用户信息
     */
    public void updateAccountInfo(LoginResponse loginResponse) {
        spHelper.put(USERID, loginResponse.getUserId());
        spHelper.put(USER_NAME, loginResponse.getLoginName());
        spHelper.put(PHOTOURL, "");
        spHelper.put(RECYCLE_PHONE, loginResponse.getUserPhone());

        updateBugly();
    }

    /**
     * 清除账号信息(手动点击退出登录后调用此方法)
     */
    public void clearAccountInfo() {
        spHelper.put(ACCOUNT, "");
        spHelper.put(PASSWORD, "");
        spHelper.put(TOKEN, "");
        spHelper.put(USER_NAME, "");
        spHelper.put(USERID, "");
        spHelper.put(PHOTOURL, "");
        spHelper.put(RECYCLE_PHONE, "");

        updateBugly();
    }

    /**
     * 设置Token
     *
     * @param token token
     */
    public void setToken(String token) {
        spHelper.put(TOKEN, token);
    }

    /**
     * 获取用户名称(昵称)
     *
     * @return 如果为空则返回账号
     */
    public String getUserName() {
        String username = spHelper.getPref(USER_NAME, "");
        if (TextUtils.isEmpty(username)) {
            username = spHelper.getPref(ACCOUNT, "");
        }
        return username;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        spHelper.put(USER_NAME, name);
    }

    /**
     * 获取账号
     *
     * @return 回调
     */
    public String getAccount() {
        return spHelper.getPref(ACCOUNT, "");
    }

    /**
     * 获取电话
     */
    public String getCurrPhone() {
        return spHelper.getPref(RECYCLE_PHONE, "");
    }

    /**
     * 获取密码
     *
     * @return 回调
     */
    public String getPassword() {
        return spHelper.getPref(PASSWORD, "");
    }

    /**
     * 获取Token
     *
     * @return 返回数据
     */
    public String getToken() {
        String str = spHelper.getPref(TOKEN, "");
        Timber.i("RetrofitFactoty：Token=%s", str);
        return str;
    }

    /**
     * 获取头像URL
     *
     * @return 返回数据
     */
    public String getPhotoUrl() {
        return spHelper.getPref(PHOTOURL, "");
    }

    /**
     * 更新头像URL
     */
    public void setPhotoUrl(String url) {
        spHelper.put(PHOTOURL, url);
    }

    /**
     * 获取用户id
     *
     * @return 返回数据
     */
    public String getUserid() {
        return spHelper.getPref(USERID, "");
    }


    /**
     * 当前是否登录
     *
     * @return token存在则表示已登录(返回true)否则未登录(返回false)
     */
    public boolean isLogin() {
        String token = spHelper.getPref(TOKEN, "");
        return !TextUtils.isEmpty(token);
    }

    /**
     * APP升级记录(APP启动时初始设置，提醒过一次后，修改设置)：true 表示 需要提醒设置，false 表示 已经提醒过了
     */
    public void setUpgrade(boolean isVal) {
        spHelper.put(UPGRADE, isVal);
    }

    /**
     * 获取APP升级记录
     *
     * @return 回调
     */
    public boolean getUpgrade() {
        return spHelper.getPref(UPGRADE, false);
    }

    /**
     * 获取当前是否同意隐私政策
     */
    public boolean getPrivacyPolicy() {
        return spHelper.getPref(PRIVACY_POLICY, false);
    }

    /**
     * 设置隐私政策
     */
    public void setPrivacyPolicy(boolean isValue) {
        spHelper.put(PRIVACY_POLICY, isValue);
    }


    /**
     * 保存APP启动次数
     */
    public void setStartTime(int num) {
        spHelper.put(START_TIME, num);
    }


    /**
     * 获取APP启动次数，如果达到一定次数开始清理日志
     */
    public int getStartTime() {
        return spHelper.getPref(START_TIME, 0);
    }
}
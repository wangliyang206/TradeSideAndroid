package com.zqw.mobile.tradeside.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import com.zqw.mobile.tradeside.mvp.model.entity.AppUpdate;
import com.zqw.mobile.tradeside.mvp.model.entity.CommonResponse;
import com.zqw.mobile.tradeside.mvp.model.entity.LoginResponse;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
public interface LoginContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        /** 设置 账号 */
        void setUsernName(String usernName);
        /** 设置 密码 */
        void setPassword(String password);
        /** 跳转致首页 */
        void jumbToMain();
        /** 跳转致快捷登录 */
        void jumbToQuickLogin(String mobile);

        /** 升级询问 */
        void askDialog(AppUpdate info);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        /** 登录 */
        Observable<LoginResponse> toLogin(String username, String password);

        /** 登录-获取短信验证码 */
        Observable<CommonResponse> loginSMS(String mobile);

        /** 获取APP版本信息 */
        Observable<AppUpdate> getVersion(String type);
    }
}
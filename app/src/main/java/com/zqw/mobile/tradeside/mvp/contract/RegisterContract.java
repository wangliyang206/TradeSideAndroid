package com.zqw.mobile.tradeside.mvp.contract;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.zqw.mobile.tradeside.mvp.model.entity.CommonResponse;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/01 17:58
 * ================================================
 */
public interface RegisterContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {
        /**
         * 禁用“发送验证码”按钮
         */
        void onDisableSendVerificationCodeButton();

        /**
         * 启用“发送验证码”按钮
         */
        void onEnableSendVerificationCodeButton();

        /**
         * 设置“倒计时”提示
         */
        void setCountDownTips(String str);

        /**
         * 禁用按钮
         */
        void onDisableClick();

        /**
         * 启用按钮
         */
        void onEnableClick();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {
        /**
         * 发送短信验证码
         */
        Observable<CommonResponse> verificationCode(String phone);

        /**
         * 注册
         */
        Observable<CommonResponse> register(String name, String phone, String password, String code);
    }
}
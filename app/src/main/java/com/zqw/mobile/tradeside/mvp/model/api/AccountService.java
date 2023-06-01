package com.zqw.mobile.tradeside.mvp.model.api;

import com.jess.arms.cj.GsonRequest;
import com.jess.arms.cj.GsonResponse;
import com.zqw.mobile.tradeside.mvp.model.entity.AppUpdate;
import com.zqw.mobile.tradeside.mvp.model.entity.CommonResponse;
import com.zqw.mobile.tradeside.mvp.model.entity.LoginResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 包名： PACKAGE_NAME
 * 对象名： AccountService
 * 描述：账户相关接口
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/24 10:03
 */

public interface AccountService {

    /*-----------------------------------------------------------------------用户基本-----------------------------------------------------------------------*/
    //登录
    @POST("member/login")
    Observable<GsonResponse<LoginResponse>> toLogin(@Body GsonRequest<Map<String, Object>> request);

    //快捷登录
    @POST("member/quickLogin")
    Observable<GsonResponse<LoginResponse>> quickLogin(@Body GsonRequest<Map<String, String>> request);

    //验证Token有效性
    @POST("member/validToken")
    Observable<GsonResponse<LoginResponse>> validToken(@Body GsonRequest<Map<String, Object>> request);

    //登录-获取短信验证码
    @POST("member/loginSMS")
    Observable<GsonResponse<CommonResponse>> loginSMS(@Body GsonRequest<Map<String, String>> request);

    //发送短信验证码
    @POST("member/forgetPassword")
    Observable<GsonResponse<CommonResponse>> sendSms(@Body GsonRequest<Map<String, Object>> request);

    //重置密码
    @POST("member/resetPwd")
    Observable<GsonResponse<CommonResponse>> forgotPassword(@Body GsonRequest<Map<String, Object>> request);

    /*-----------------------------------------------------------------------通用-----------------------------------------------------------------------*/

    // 获取APP版本信息
    @POST("system/getVersion")
    Observable<GsonResponse<AppUpdate>> getVersion(@Body GsonRequest<Map<String, Object>> request);

    // 下载
    @Streaming
    @GET()
    Observable<ResponseBody> download(@Url String Url);

}

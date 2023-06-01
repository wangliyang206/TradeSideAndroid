package com.zqw.mobile.tradeside.mvp.model;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.tradeside.mvp.contract.LoginContract;
import com.zqw.mobile.tradeside.mvp.model.api.AccountService;
import com.zqw.mobile.tradeside.mvp.model.api.SystemService;
import com.zqw.mobile.tradeside.mvp.model.entity.AppUpdate;
import com.zqw.mobile.tradeside.mvp.model.entity.CommonResponse;
import com.zqw.mobile.tradeside.mvp.model.entity.LoginResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/05/31 14:33
 * ================================================
 */
@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public LoginModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;
    }

    @Override
    public Observable<LoginResponse> toLogin(String username, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("userPhone", username);
        params.put("password", password);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).toLogin(request));
    }

    @Override
    public Observable<LoginResponse> quickLogin(String mobile, String code) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).quickLogin(request));
    }

    @Override
    public Observable<CommonResponse> loginSMS(String mobile) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).loginSMS(request));
    }

    @Override
    public Observable<AppUpdate> getVersion(String type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        // 主体类型：0代表是旧版-找铅网，1代表是新版-找铅网
        params.put("mainBody", 1);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(SystemService.class).getVersion(request));
    }
}
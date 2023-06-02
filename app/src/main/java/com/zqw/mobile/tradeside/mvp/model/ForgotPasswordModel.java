package com.zqw.mobile.tradeside.mvp.model;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.tradeside.mvp.contract.ForgotPasswordContract;
import com.zqw.mobile.tradeside.mvp.model.api.AccountService;
import com.zqw.mobile.tradeside.mvp.model.entity.CommonResponse;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 2023/06/01 17:58
 * ================================================
 */
@ActivityScope
public class ForgotPasswordModel extends BaseModel implements ForgotPasswordContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public ForgotPasswordModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;

    }

    @Override
    public Observable<CommonResponse> forgotPassword(String account, String code, String newPwd) {
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("code", code);
        params.put("newPwd", newPwd);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).forgotPassword(request));
    }

    @Override
    public Observable<CommonResponse> sendSms(String mobile) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).sendSms(request));
    }
}
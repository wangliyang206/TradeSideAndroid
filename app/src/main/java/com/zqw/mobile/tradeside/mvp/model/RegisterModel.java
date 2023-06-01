package com.zqw.mobile.tradeside.mvp.model;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.tradeside.mvp.contract.RegisterContract;
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
public class RegisterModel extends BaseModel implements RegisterContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public RegisterModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;

    }

    @Override
    public Observable<CommonResponse> verificationCode(String phone) {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).sendSms(request));
    }

    @Override
    public Observable<CommonResponse> register(String name, String phone, String password, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("phone", phone);
        params.put("password", password);
        params.put("code", code);

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).register(request));
    }
}
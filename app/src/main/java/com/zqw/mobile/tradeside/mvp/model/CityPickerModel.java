package com.zqw.mobile.tradeside.mvp.model;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.tradeside.mvp.contract.CityPickerContract;
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
 * Created by MVPArmsTemplate on 2023/06/05 17:07
 * ================================================
 */
@ActivityScope
public class CityPickerModel extends BaseModel implements CityPickerContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public CityPickerModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;
    }

    @Override
    public Observable<CommonResponse> getCityPicker() {
        Map<String, Object> params = new HashMap<>();

        return apiOperator.chain(params, request -> mRepositoryManager.obtainRetrofitService(AccountService.class).getCityPicker(request));
    }
}
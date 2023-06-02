package com.zqw.mobile.tradeside.mvp.model;

import com.jess.arms.cj.ApiOperator;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.zqw.mobile.tradeside.mvp.contract.MainContract;
import com.zqw.mobile.tradeside.mvp.model.api.SystemService;
import com.zqw.mobile.tradeside.mvp.model.entity.AppUpdate;

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
public class MainModel extends BaseModel implements MainContract.Model {
    @Inject
    ApiOperator apiOperator;                                                                        // 数据转换

    @Inject
    public MainModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.apiOperator = null;
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
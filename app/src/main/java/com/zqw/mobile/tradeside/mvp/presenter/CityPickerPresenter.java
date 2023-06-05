package com.zqw.mobile.tradeside.mvp.presenter;

import static com.zqw.mobile.tradeside.BuildConfig.IS_DEBUG_DATA;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.zqw.mobile.tradeside.app.utils.CommonUtils;
import com.zqw.mobile.tradeside.app.utils.RxUtils;
import com.zqw.mobile.tradeside.mvp.contract.CityPickerContract;
import com.zqw.mobile.tradeside.mvp.model.entity.ArealistBean;
import com.zqw.mobile.tradeside.mvp.model.entity.CityComparator;
import com.zqw.mobile.tradeside.mvp.model.entity.CommonResponse;
import com.zqw.mobile.tradeside.mvp.ui.adapter.CityListAdapter;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

/**
 * ================================================
 * Description:切换城市
 * <p>
 * Created by MVPArmsTemplate on 2023/06/05 17:07
 * ================================================
 */
@ActivityScope
public class CityPickerPresenter extends BasePresenter<CityPickerContract.Model, CityPickerContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;

    // 数据源
    @Inject
    List<ArealistBean> mList;
    @Inject
    CityListAdapter mAdapter;


    @Inject
    public CityPickerPresenter(CityPickerContract.Model model, CityPickerContract.View rootView) {
        super(model, rootView);
    }

    public void initData() {
        if (IS_DEBUG_DATA) {
            mList.add(new ArealistBean("001","安庆", "anqing", 0));
            mList.add(new ArealistBean("001","安阳", "anyang", 0));
            mList.add(new ArealistBean("001","安化", "anhua", 0));
            mList.add(new ArealistBean("001","阿拉善盟", "alashan", 0));
            mList.add(new ArealistBean("001","阿勒泰", "aletai", 0));
            mList.add(new ArealistBean("001","北京", "beijing", 1));
            mList.add(new ArealistBean("001","保定", "baoding", 1));
            mList.add(new ArealistBean("001","北海", "beihai", 1));
            mList.add(new ArealistBean("001","巴楚县", "bachuxian", 0));
            mList.add(new ArealistBean("001","滨州", "binzhou", 0));
            mList.add(new ArealistBean("001","博白县", "bobaixian", 0));
            mList.add(new ArealistBean("001","博尔塔拉", "boertala", 0));
            mList.add(new ArealistBean("001","成都", "chengdu", 1));
            mList.add(new ArealistBean("001","重庆", "chongqing", 1));
            mList.add(new ArealistBean("001","承德", "chengde", 0));
            mList.add(new ArealistBean("001","长治", "changzhi", 0));
            mList.add(new ArealistBean("001","赤峰", "chifeng", 0));
            mList.add(new ArealistBean("001","曹妃甸", "caofeidian", 0));
            mList.add(new ArealistBean("001","大连", "dalian", 0));
            mList.add(new ArealistBean("001","东莞", "dongguan", 0));
            mList.add(new ArealistBean("001","大安市", "daanshi", 0));
            mList.add(new ArealistBean("001","达拉特旗", "dalateqi", 0));
            mList.add(new ArealistBean("001","佛山", "foshan", 0));
            mList.add(new ArealistBean("001","广州", "guangzhou", 0));
            mList.add(new ArealistBean("001","贵阳", "guiyang", 0));
            mList.add(new ArealistBean("001","杭州", "huangzhou", 1));
            mList.add(new ArealistBean("001","哈尔滨", "haerbin", 0));
            mList.add(new ArealistBean("001","合肥", "hefei", 0));
            mList.add(new ArealistBean("001","衡水", "hengshui", 0));
            mList.add(new ArealistBean("001","石家庄", "shijiazhuang", 1));
            mList.add(new ArealistBean("001","陕西", "shanxia", 1));
            mList.add(new ArealistBean("001","山西", "shanxi", 1));

            Collections.sort(mList, new CityComparator());
            mAdapter.initData();
            mAdapter.notifyDataSetChanged();
        } else {
            mModel.getCityPicker()
                    .compose(RxUtils.applySchedulers(mRootView))                               // 切换线程
                    .subscribe(new ErrorHandleSubscriber<CommonResponse>(mErrorHandler) {
                        @Override
                        public void onNext(CommonResponse response) {
                            if (response != null) {
                                if (CommonUtils.isNotEmpty(response.getAreaList()))
                                    mList.addAll(response.getAreaList());
                                Collections.sort(mList, new CityComparator());
                                mAdapter.initData();
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAdapter = null;
        this.mList = null;
    }
}
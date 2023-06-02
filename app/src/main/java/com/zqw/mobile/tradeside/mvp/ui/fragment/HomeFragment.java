package com.zqw.mobile.tradeside.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.zqw.mobile.tradeside.R;
import com.zqw.mobile.tradeside.app.utils.EventBusTags;
import com.zqw.mobile.tradeside.di.component.DaggerHomeComponent;
import com.zqw.mobile.tradeside.mvp.contract.HomeContract;
import com.zqw.mobile.tradeside.mvp.model.entity.MainEvent;
import com.zqw.mobile.tradeside.mvp.presenter.HomePresenter;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

/**
 * 首页(业务)
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.View, View.OnClickListener {
    /*------------------------------------------------控件信息------------------------------------------------*/
//    @BindView(R.id.imvi_fragmenthome_head)
//    CircleImageView imviHead;                                                                       // 头像
//
//    @BindView(R.id.txvi_fragmenthome_city)
//    TextView txviCity;                                                                              // 城市
//
//    @BindView(R.id.lila_fragmenthome_search)
//    LinearLayout lilaSearch;                                                                        // 搜索
//    @BindView(R.id.vstv_fragmenthome_view)
//    VerticalScrollTextView txviSearchHot;                                                           // 搜索热门关键字
//    @BindView(R.id.rela_fragmenthome_signin)
//    RelativeLayout relaSignIn;                                                                      // 签到打卡
//
//    @BindView(R.id.lila_fragmenthome_nottargeted)
//    LinearLayout lilaNotTargeted;                                                                   // 定位失败界面
//    @BindView(R.id.lila_fragmenthome_loading)
//    View lilaLoading;                                                                               // Loading
//
//    @BindView(R.id.txvi_fragmenthome_performa_uservisit)
//    TextView txviPerformaUserVisit;                                                                 // 业绩信息-个人拜访量
//    @BindView(R.id.txvi_fragmenthome_performa_userstoreinto)
//    TextView txviPerformaUserStoreInto;                                                             // 业绩信息-个人注册量
//    @BindView(R.id.txvi_fragmenthome_performa_allvisit)
//    TextView txviPerformaAllVisit;                                                                  // 业绩信息-团队拜访量
//    @BindView(R.id.txvi_fragmenthome_performa_allstoreinto)
//    TextView txviPerformaAllStoreInto;                                                              // 业绩信息-团队注册量
//
//    @BindView(R.id.cola_fragmenthome_complaints)
//    ConstraintLayout colaComplaintsLayout;                                                          // 问题投诉
//    @BindView(R.id.txvi_fragmenthome_complaints_daycomplaints)
//    TextView txviComplaintsDayComplaints;                                                           // 问题投诉-当日投诉量
//    @BindView(R.id.txvi_fragmenthome_complaints_totalcomplaint)
//    TextView txviComplaintsTotalComplaints;                                                         // 问题投诉-累计投诉量
//    @BindView(R.id.txvi_fragmenthome_complaints_storecomplaints)
//    TextView txviComplaintsStoreComplaints;                                                         // 问题投诉-门店投诉量
//    @BindView(R.id.txvi_fragmenthome_complaints_recyclerscomplaints)
//    TextView txviComplaintsRecyclersComplaints;                                                     // 问题投诉-回收员投诉量
//    @BindView(R.id.lila_fragmenthome_complaints_details)
//    LinearLayout lilaComplaintsDetails;                                                             // 问题投诉-查看详情
//
//    @BindView(R.id.cola_fragmenthome_visit)
//    ConstraintLayout colaVisitLayout;                                                               // 客情拜访
//    @BindView(R.id.txvi_fragmenthome_visit_checkinrecord)
//    TextView txviVisitCheckInRecord;                                                                // 客情拜访-打卡记录
//    @BindView(R.id.txvi_fragmenthome_visit_dayvisit)
//    TextView txviVisitDayVisit;                                                                     // 客情拜访-当日拜访量
//    @BindView(R.id.txvi_fragmenthome_visit_totalvisit)
//    TextView txviVisitTotalVisit;                                                                   // 客情拜访-累计拜访量
//    @BindView(R.id.txvi_fragmenthome_visit_accompanyvisit)
//    TextView txviVisitAccompanyVisit;                                                               // 客情拜访-累计陪访量
//    @BindView(R.id.txvi_fragmenthome_visit_clock)
//    TextView txviVisitClock;                                                                        // 客情拜访-打卡
//    @BindView(R.id.txvi_fragmenthome_visit_visit)
//    TextView txviVisitVisit;                                                                        // 客情拜访-拜访
//    @BindView(R.id.lila_fragmenthome_visit_details)
//    LinearLayout lilaVisitDetails;                                                                  // 客情拜访-拜访详情
//    @BindView(R.id.lila_fragmenthome_accompany_details)
//    LinearLayout lilaAccompanyVisitDetails;                                                         // 客情拜访-陪访详情
//
//    @BindView(R.id.cola_fragmenthome_storestatistic)
//    ConstraintLayout colaStoreStatisticsLayout;                                                     // 门店统计
//    @BindView(R.id.txvi_fragmenthome_storestatistic_totalcount)
//    TextView txviStoreTotalCount;                                                                   // 门店统计-总门店
//    @BindView(R.id.txvi_fragmenthome_storestatistic_zombie)
//    TextView txviStoreZombie;                                                                       // 门店统计-僵尸
//    @BindView(R.id.txvi_fragmenthome_storestatistic_silent)
//    TextView txviStoreSilent;                                                                       // 门店统计-静默
//    @BindView(R.id.txvi_fragmenthome_storestatistic_active)
//    TextView txviStoreActive;                                                                       // 门店统计-活跃
//    @BindView(R.id.txvi_fragmenthome_storestatistic_nearbystores)
//    TextView txviNearbyStores;                                                                      // 门店统计-附近门店
//    @BindView(R.id.lila_fragmenthome_storestatistic_details)
//    LinearLayout lilaStoreStatisticsDetails;                                                        // 门店统计-查看详情
//
//    @BindView(R.id.cola_fragmenthome_orderstatistic)
//    ConstraintLayout colaOrderLayout;                                                               // 订单统计
//    @BindView(R.id.txvi_fragmenthome_orderstatistic_ordernum)
//    TextView txviOrderNum;                                                                          // 订单统计-当日回收单数
//    @BindView(R.id.txvi_fragmenthome_orderstatistic_recyweight)
//    TextView txviOrderRecyWeight;                                                                   // 订单统计-当日回收重量
//    @BindView(R.id.txvi_fragmenthome_orderstatistic_recyweightunit)
//    TextView txviOrderRecyWeightUnit;
//    @BindView(R.id.txvi_fragmenthome_orderstatistic_delivery)
//    TextView txviOrderDelivery;                                                                     // 订单统计-当日交货重量
//    @BindView(R.id.txvi_fragmenthome_orderstatistic_deliveryunt)
//    TextView txviOrderDeliveryUnit;
//    @BindView(R.id.txvi_fragmenthome_orderstatistic_stocktotal)
//    TextView txviOrderStockTotal;                                                                   // 订单统计-车辆库存
//    @BindView(R.id.txvi_fragmenthome_orderstatistic_stocktotalunit)
//    TextView txviOrderStockTotalUnit;
//    @BindView(R.id.lila_fragmenthome_orderstatistic_details)
//    LinearLayout lilaOrderDetails;                                                                  // 订单统计-查看详情
//
//    @BindView(R.id.cola_fragmentregister_storestatistic)
//    ConstraintLayout colaStoreLayout;                                                               // 门店
//    @BindView(R.id.txvi_fragmentregister_storestatistic_registertoday)
//    TextView txviStoreTodayRegister;                                                                // 门店-今日注册
//    @BindView(R.id.txvi_fragmentregister_storestatistic_totalregister)
//    TextView txviStoreTotalRegister;                                                                // 门店-累计注册
//    @BindView(R.id.txvi_fragmentregister_storestatistic_register)
//    TextView txviStoreRegister;                                                                     // 门店-门店注册
//    @BindView(R.id.txvi_fragmentregister_storestatistic_quickregister)
//    TextView txviStoreQuickRegister;                                                                // 门店-门店快捷注册
//    @BindView(R.id.lila_fragmentregister_storestatistic_details)
//    LinearLayout lilaStoreDetails;                                                                  // 门店-查看详情
//
//    @BindView(R.id.cola_fragmentregister_specialstore)
//    ConstraintLayout colaSpecialStoreLayout;                                                        // 特约商户
//    @BindView(R.id.txvi_fragmentregister_specialstore_registertoday)
//    TextView txviSpecialStoreTodayRegister;                                                         // 特约商户-今日注册
//    @BindView(R.id.txvi_fragmentregister_specialstore_totalregister)
//    TextView txviSpecialStoreTotalRegister;                                                         // 特约商户-累计注册
//    @BindView(R.id.txvi_fragmentregister_specialstore_register)
//    TextView txviSpecialStoreRegister;                                                              // 特约商户-特约商户注册
//    @BindView(R.id.lila_fragmentregister_specialstore_details)
//    LinearLayout lilaSpecialStoreDetails;                                                           // 特约商户-查看详情
//
//    @BindView(R.id.cola_fragmentregister_qualifiedsupplier)
//    ConstraintLayout colaQualifiedSupplierLayout;                                                   // 资质商
//    @BindView(R.id.txvi_fragmentregister_qualifiedsupplier_registertoday)
//    TextView txviQualifiedSupplierTodayRegister;                                                    // 资质商-今日注册
//    @BindView(R.id.txvi_fragmentregister_qualifiedsupplier_totalregister)
//    TextView txviQualifiedSupplierTotalRegister;                                                    // 资质商-累计注册
//    @BindView(R.id.txvi_fragmentregister_qualifiedsupplier_register)
//    TextView txviQualifiedSupplierRegister;                                                         // 资质商-特约商户注册
//    @BindView(R.id.lila_fragmentregister_qualifiedsupplier_details)
//    LinearLayout lilaQualifiedSupplierDetails;                                                      // 资质商-查看详情

    /*------------------------------------------------业务区域------------------------------------------------*/
    @Inject
    ImageLoader mImageLoader;

    @Override
    public void onStart() {
        super.onStart();
//        txviSearchHot.startPlay();
    }

    @Override
    public void onResume() {
        super.onResume();

        // 初始化首页
        initView();
    }

    @Override
    public void onPause() {
        super.onPause();
//        txviSearchHot.stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.mImageLoader = null;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 初始化控件
     */
    private void initView() {
        if (mPresenter != null) {
            // 初始化
            mPresenter.initHome();
        }
    }

    /**
     * 刷新数据
     */
    private void onRefresh() {
        // 获取首页数据
        if (mPresenter != null) {
            mPresenter.getHome();
        }
    }

    @Override
    public void homemPositioningFailure() {

    }

    /**
     * 设置头像
     */
    @Override
    public void homeSetAvatar(String url) {
//        if (TextUtils.isEmpty(url)) {
//            mImageLoader.loadImage(getContext(),
//                    ImageConfigImpl
//                            .builder()
//                            .fallback(R.drawable.profile6)
//                            .placeholder(R.mipmap.ic_home_head)
//                            .imageView(imviHead).build());
//        } else {
//            mImageLoader.loadImage(getContext(),
//                    ImageConfigImpl
//                            .builder()
//                            .url(url)
//                            .fallback(R.mipmap.ic_home_head)
//                            .placeholder(R.mipmap.ic_home_head)
//                            .imageView(imviHead).build());
//        }
    }

    //    @OnClick({
//            R.id.lila_fragmenthome_loading,                                                         // Loading
//            R.id.imvi_fragmenthome_head,                                                            // 点击头像
//            R.id.btn_home_manual,                                                                   // 定位失败-手动定位
//            R.id.btn_home_load,                                                                     // 定位失败-重新加载
//            R.id.lila_fragmenthome_city,                                                            // 切换城市
//            R.id.lila_fragmenthome_search,                                                          // 拜访搜索
//            R.id.txvi_fragmenthome_visit_visit,
//            R.id.rela_fragmenthome_signin,                                                          // 签到/打卡
//            R.id.txvi_fragmenthome_visit_clock,
//            R.id.lila_fragmenthome_complaints_details,                                              // 问题投诉 - 查看详情
//            R.id.lila_fragmenthome_visit_details,                                                   // 客情拜访 - 拜访详情
//            R.id.lila_fragmenthome_accompany_details,                                               // 客情拜访 - 陪访详情
//            R.id.txvi_fragmenthome_visit_checkinrecord,                                             // 打卡记录
//            R.id.txvi_fragmenthome_storestatistic_nearbystores,                                     // 门店信息 - 附近门店
//            R.id.lila_fragmenthome_storestatistic_details,                                          // 门店信息 - 查看详情
//            R.id.lila_fragmenthome_orderstatistic_details,                                          // 订单信息 - 查看详情
//
//            R.id.lila_fragmentregister_storestatistic_details,                                      // 门店注册列表
//            R.id.txvi_fragmentregister_storestatistic_register,                                     // 门店注册
//            R.id.txvi_fragmentregister_storestatistic_quickregister,                                // 门店快捷注册
//            R.id.lila_fragmentregister_specialstore_details,                                        // 特约商户注册列表
//            R.id.txvi_fragmentregister_specialstore_register,                                       // 特约商户注册
//            R.id.lila_fragmentregister_qualifiedsupplier_details,                                   // 资质商注册列表
//            R.id.txvi_fragmentregister_qualifiedsupplier_register,                                  // 资质商注册
//    })
    @Override
    public void onClick(View v) {
//        Bundle bundle = new Bundle();
//        switch (v.getId()) {
//            case R.id.imvi_fragmenthome_head:                                                       // 点击头像
//                // 点击头像，打开侧滑
//                EventBus.getDefault().post(new MainEvent(EventBusTags.OPEN_MENU_TAG), EventBusTags.HOME_TAG);
//                break;
//            case R.id.btn_home_manual:                                                              // 定位失败-手动定位
//                // 跳转至 定位界面
//                bundle.putBoolean("isManual", true);
//                ActivityUtils.startActivity(bundle, CityPickerActivity.class);
//                break;
//            case R.id.btn_home_load:                                                                // 定位失败-重新加载
//                // 初始化首页内容
//                initView();
//                break;
//            case R.id.lila_fragmenthome_city:                                                       // 切换城市
//                ActivityUtils.startActivity(CityPickerActivity.class);
//                break;
//            case R.id.lila_fragmenthome_search:                                                     // 拜访搜索
//            case R.id.txvi_fragmenthome_visit_visit:
//                ActivityUtils.startActivity(VisitSearchActivity.class);
//                break;
//            case R.id.rela_fragmenthome_signin:                                                     // 签到/打卡
//            case R.id.txvi_fragmenthome_visit_clock:
//                bundle.putBoolean("isHome", true);
//                ActivityUtils.startActivity(bundle, SignInActivity.class);
//                break;
//            case R.id.lila_fragmenthome_complaints_details:                                         // 问题投诉 - 查看详情
//                ActivityUtils.startActivity(ProblemComplaintsActivity.class);
//                break;
//            case R.id.lila_fragmenthome_visit_details:                                              // 客情拜访 - 拜访详情
//                ActivityUtils.startActivity(VisitRecordListActivity.class);
//                break;
//            case R.id.lila_fragmenthome_accompany_details:                                          // 客情拜访 - 陪访详情
//                ActivityUtils.startActivity(AccompanyVisitListActivity.class);
//                break;
//            case R.id.txvi_fragmenthome_visit_checkinrecord:                                        // 打卡记录
//                ActivityUtils.startActivity(CheckInRecordActivity.class);
//                break;
//            case R.id.txvi_fragmenthome_storestatistic_nearbystores:                                // 门店信息 - 附近门店
//                ActivityUtils.startActivity(NearbyStoresActivity.class);
//                break;
//            case R.id.lila_fragmenthome_storestatistic_details:                                     // 门店信息 - 查看详情
//                ActivityUtils.startActivity(StoreInfoActivity.class);
//                break;
//            case R.id.lila_fragmenthome_orderstatistic_details:                                     // 订单信息 - 查看详情
//                ActivityUtils.startActivity(RecycleOrderActivity.class);
//                break;
//
//            case R.id.lila_fragmentregister_storestatistic_details:                                 // 门店注册列表
//                ActivityUtils.startActivity(RegistStoreListActivity.class);
//                break;
//            case R.id.txvi_fragmentregister_storestatistic_register:                                // 门店注册
//                if (!CommonUtils.isDoubleClick())
//                    ActivityUtils.startActivity(RegistStoreActivity.class);
//                break;
//            case R.id.txvi_fragmentregister_storestatistic_quickregister:                           // 门店快捷注册
//                if (!CommonUtils.isDoubleClick())
//                    ActivityUtils.startActivity(StoreQuickRegistrationActivity.class);
//                break;
//            case R.id.lila_fragmentregister_specialstore_details:                                   // 特约商户注册列表
//                ActivityUtils.startActivity(RegistSpecialStoreListActivity.class);
//                break;
//            case R.id.txvi_fragmentregister_specialstore_register:                                  // 特约商户注册
//                if (!CommonUtils.isDoubleClick())
//                    ActivityUtils.startActivity(RegistSpecialStoreActivity.class);
//                break;
//            case R.id.lila_fragmentregister_qualifiedsupplier_details:                              // 资质商注册列表
//                ActivityUtils.startActivity(RegistAptitudeListActivity.class);
//                break;
//            case R.id.txvi_fragmentregister_qualifiedsupplier_register:                             // 资质商注册
//                if (!CommonUtils.isDoubleClick())
//                    ActivityUtils.startActivity(RegistAptitudeActivity.class);
//                break;
//        }
    }

    /**
     * 首页特殊事件    回调
     */
    @Subscriber(tag = EventBusTags.HOME_TAG, mode = ThreadMode.POST)
    private void eventBusEvent(MainEvent mainEvent) {
//        // 切换区域
//        if (mainEvent.getCode() == EventBusTags.SWITCH_CITIES_TAG) {
//            // 隐藏定位失败界面
//            if (lilaNotTargeted != null)
//                lilaNotTargeted.setVisibility(View.GONE);
//        }
    }

    /**
     * 热门关键字
     */
    @Override
    public void homeSearchHot(List<String> list) {
//        txviSearchHot.setDataSource(list);
    }

    /**
     * 设置城市
     */
    @Override
    public void homeSetLocateAdd(String str) {
//        txviCity.setText(str);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {
        // 隐藏定位失败界面
//        if (lilaNotTargeted != null)
//            lilaNotTargeted.setVisibility(View.GONE);
        // 加载Loading
//        if (lilaLoading != null)
//            lilaLoading.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
//        if (lilaLoading != null) {
//            lilaLoading.setVisibility(View.GONE);
//        }
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    /**
     * 双击刷新
     */
    public void onDoubleClick() {
        onRefresh();
    }
}

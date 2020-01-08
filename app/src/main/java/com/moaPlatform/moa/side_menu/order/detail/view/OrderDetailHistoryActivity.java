package com.moaPlatform.moa.side_menu.order.detail.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.delivery_menu.store_detail.StoreDetailActivity;
import com.moaPlatform.moa.side_menu.order.OrderCommonOrderTypeView;
import com.moaPlatform.moa.side_menu.order.detail.model.ReqOrderDetail;
import com.moaPlatform.moa.side_menu.order.detail.model.ResOrderDetailHistoryShellModel;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailHistoryActivity extends AppCompatActivity {
    OrderDetailAdapter orderDetailAdapter;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_history);
        orderSet();
        orderServer();
    }

    /**
     * 주문 내역 리스트 어댑터 초기화 및 뷰 연결
     */
    private void orderSet() {
        RecyclerView recyclerView = findViewById(R.id.orderRecyclerView);
        orderDetailAdapter = new OrderDetailAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(orderDetailAdapter);
    }

    /**
     * 주문 내역 리스트 갱신
     *
     * @param orderDetailList 갱신할 리스트
     */
    private void listUpdate(List<ResOrderDetailHistoryShellModel.ResOrderDetailHistoryProductInfoModel> orderDetailList) {
        if (orderDetailList == null)
            return;
        // 기본 상품과 추가 옵션 상품을 담을 배열
        List<Object> orderHistoryProductList = new ArrayList<>();
        for (ResOrderDetailHistoryShellModel.ResOrderDetailHistoryProductInfoModel orderDetailHistoryProductInfoModel : orderDetailList) {
            // 기본 상품을 추가
            orderHistoryProductList.add(orderDetailHistoryProductInfoModel);
            for (ResOrderDetailHistoryShellModel.ResOrderDetailHistoryAddOptionCategoryModel orderDetailHistoryAddOptionCategoryModel : orderDetailHistoryProductInfoModel.orderDetailHistoryAddOptionCategoryModelList) {
                // 추가 옵션 상품을 추가
                orderHistoryProductList.addAll(orderDetailHistoryAddOptionCategoryModel.orderDetailHistoryAddOptionProductModelList);
            }
        }

        orderDetailAdapter.setData(orderHistoryProductList);
        orderDetailAdapter.notifyDataSetChanged();
    }

    /**
     * 뷰 초기화
     *
     * @param orderDetailHistoryInfoModel 주문내역 정보 담고 있는 모델
     */
    private void init(ResOrderDetailHistoryShellModel.ResOrderDetailHistoryInfoModel orderDetailHistoryInfoModel) {
        try {
            // 타이틀 세팅
            CommonTitleView viewOrderHistoryTitle = findViewById(R.id.viewOrderHistoryTitle);
            viewOrderHistoryTitle.setTitleName(R.string.activity_order_detail_history_title);
            viewOrderHistoryTitle.setBackButtonClickListener(v -> finish());

            // 주문 상점
            TextView tvOrderDetailHistoryStoreName = findViewById(R.id.tvOrderDetailHistoryStoreName);
            tvOrderDetailHistoryStoreName.setText(orderDetailHistoryInfoModel.storeName);

            // 주문 상태 코드
            OrderCommonOrderTypeView viewOrderDetailHistoryOrderType = findViewById(R.id.viewOrderDetailHistoryOrderType);
            viewOrderDetailHistoryOrderType.orderTypeCheck(orderDetailHistoryInfoModel.orderStatCd);

            // 주문 취소
            TextView tvOrderDetailHistoryOrderCancel = findViewById(R.id.tvOrderDetailHistoryOrderCancel);
            // 주문 취소는 접수 대기 상태에서만 가능
            final String ORDER_WAIT = "00";
            if (orderDetailHistoryInfoModel.orderStatCd.equals(ORDER_WAIT)) {
                tvOrderDetailHistoryOrderCancel.setText(getString(R.string.activity_order_detail_history_order_cancel));
                tvOrderDetailHistoryOrderCancel.setBackgroundResource(R.drawable.common_edit_text_form);
                tvOrderDetailHistoryOrderCancel.setVisibility(View.VISIBLE);
                // 해당 버튼 클릭 시 주문 취소 메소드 실행
                tvOrderDetailHistoryOrderCancel.setOnClickListener(v -> orderCancelDialogShow());
            } else {
                tvOrderDetailHistoryOrderCancel.setVisibility(View.GONE);
            }

            // 고객 주문 취소
            final String ORDER_TYPE_CLIENT_CANCEL = "11";
            // 매장 주문 취소
            final String ORDER_TYPE_STORE_CANCEL = "12";
            // 취소 사유 뷰
            View clOrderDetailHistoryOrderCancelGroup = findViewById(R.id.clOrderDetailHistoryOrderCancelGroup);
            if (orderDetailHistoryInfoModel.orderStatCd.equals(ORDER_TYPE_CLIENT_CANCEL) || orderDetailHistoryInfoModel.orderStatCd.equals(ORDER_TYPE_STORE_CANCEL)) {
                // 매장 또는 사용자가 주문 취소 시
                clOrderDetailHistoryOrderCancelGroup.setVisibility(View.VISIBLE);
                // 주문 취소 사유 내용
                TextView tvOrderDetailHistoryCancelReasonType = findViewById(R.id.tvOrderDetailHistoryCancelReasonType);
                tvOrderDetailHistoryCancelReasonType.setText(orderDetailHistoryInfoModel.orderCancelReasonContent);

                tvOrderDetailHistoryOrderCancel.setText(getString(R.string.activity_order_detail_history_again_order));
                tvOrderDetailHistoryOrderCancel.setBackgroundResource(R.drawable.shape_shopping_cart_detail_add_menu);
                tvOrderDetailHistoryOrderCancel.setTextColor(getResources().getColor(R.color.coral));
                tvOrderDetailHistoryOrderCancel.setVisibility(View.VISIBLE);
                tvOrderDetailHistoryOrderCancel.setOnClickListener(v -> {
                    Intent storeDetailIntent = new Intent(getApplicationContext(), StoreDetailActivity.class);
                    storeDetailIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    storeDetailIntent.putExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), Integer.valueOf(orderDetailHistoryInfoModel.storId));
                    startActivity(storeDetailIntent);
                });
            } else {
                clOrderDetailHistoryOrderCancelGroup.setVisibility(View.GONE);
            }

            // 주문 번호
            TextView tvOrderDetailHistoryOrderNumber = findViewById(R.id.tvOrderDetailHistoryOrderNumber);
            tvOrderDetailHistoryOrderNumber.setText(orderDetailHistoryInfoModel.orderId);

            // 주문 시간
            TextView tvOrderDetailHistoryOrderTime = findViewById(R.id.tvOrderDetailHistoryOrderTime);
            tvOrderDetailHistoryOrderTime.setText(orderDetailHistoryInfoModel.orderTime);

            // 주문 방법
            TextView tvOrderDetailHistoryOrderMethod = findViewById(R.id.tvOrderDetailHistoryOrderMethod);
            tvOrderDetailHistoryOrderMethod.setText(orderDetailHistoryInfoModel.payMethodCodeName);

            // 배달료
            TextView tvOrderDetailHistoryDeliveryPrice = findViewById(R.id.tvOrderDetailHistoryDeliveryPrice);
            tvOrderDetailHistoryDeliveryPrice.setText(getString(R.string.item_order_detail_history_product_price_unit, StringUtil.convertCommaPrice(orderDetailHistoryInfoModel.deliveryPrice)));

            // 주소(도로명 기본 주소)
            TextView tvOrderDetailHistoryRoadAddress = findViewById(R.id.tvOrderDetailHistoryRoadAddress);
            tvOrderDetailHistoryRoadAddress.setText(orderDetailHistoryInfoModel.orderRoadNmDefaultAddress);
            // 주소(도로명 상세 주소)
            TextView tvOrderDetailHistoryRoadDetailAddress = findViewById(R.id.tvOrderDetailHistoryRoadDetailAddress);
            tvOrderDetailHistoryRoadDetailAddress.setText(orderDetailHistoryInfoModel.orderRoadDetailAddress);
            // 주소(주문 지번 주소)
            TextView tvOrderDetailJibunAddress = findViewById(R.id.tvOrderDetailJibunAddress);
            tvOrderDetailJibunAddress.setText(orderDetailHistoryInfoModel.orderJibunAddress);

            // 연락처
            TextView tvOrderDetailHistoryCallNumber = findViewById(R.id.tvOrderDetailHistoryCallNumber);
            tvOrderDetailHistoryCallNumber.setText(orderDetailHistoryInfoModel.orderPhoneNumber);

            // 요청사항
            TextView tvOrderDetailHistoryRequestedTerm = findViewById(R.id.tvOrderDetailHistoryRequestedTerm);
            tvOrderDetailHistoryRequestedTerm.setText(orderDetailHistoryInfoModel.orderReqItem);

            //주문 합계
            TextView orderTotal = findViewById(R.id.tvOrderDetailHistoryOrderTotal);
            String orderTotalPriceText = commaPriceText(R.string.activity_order_detail_default_price, orderDetailHistoryInfoModel.sumAmt);
            orderTotal.setText(orderTotalPriceText);

            // 할인 금액
            // 포인트 사용 금액
            TextView tvOrderDetailHistoryMoaPoint = findViewById(R.id.tvOrderDetailHistoryMoaPoint);
            String usePointText = commaPriceText(R.string.activity_order_detail_sale_amount_point_coupon_price, orderDetailHistoryInfoModel.pointUseAmount);
            tvOrderDetailHistoryMoaPoint.setText(usePointText);
            Map<String, Integer> saleMap = orderDetailHistoryInfoModel.getOderDetailHistorySaleMap();
            // 쿠폰 사용 금액
            TextView tvOrderDetailHistoryCoupon = findViewById(R.id.tvOrderDetailHistoryCoupon);
            int useCouponPrice = 0;
            // 쿠폰 가격이 있는지 체크후 null 이 아닐경우 가격 덮어씀
            final String SALE_COUPON_TYPE = "coupon";
            if (saleMap.get(SALE_COUPON_TYPE) != null) {
                useCouponPrice = saleMap.get(SALE_COUPON_TYPE);
            }
            String couponText = commaPriceText(R.string.activity_order_detail_sale_amount_point_coupon_price, useCouponPrice);
            tvOrderDetailHistoryCoupon.setText(couponText);
            // 매장 할인 금액
            TextView tvOrderDetailHistoryStoreSale = findViewById(R.id.tvOrderDetailHistoryStoreSale);
            int useStoreSalePrice = 0;
            // 쿠폰 가격이 있는지 체크후 null 이 아닐경우 가격 덮어씀
            final String SALE_STORE_TYPE = "timeEvent";
            if (saleMap.get(SALE_STORE_TYPE) != null) {
                useStoreSalePrice = saleMap.get(SALE_COUPON_TYPE);
            }
            String storeSaleText = commaPriceText(R.string.activity_order_detail_sale_amount_point_coupon_price, useStoreSalePrice);
            tvOrderDetailHistoryStoreSale.setText(storeSaleText);

            // 결제 수단
            TextView tvOrderDetailHistoryPaymentMethod = findViewById(R.id.tvOrderDetailHistoryPaymentMethod);
            tvOrderDetailHistoryPaymentMethod.setText(orderDetailHistoryInfoModel.payMethodSeparationCodeName);

            // 결제 금액 - 주문금액 - 할인금액 가격
            TextView tvOrderDetailHistoryPaymentAmount = findViewById(R.id.tvOrderDetailHistoryPaymentAmount);
            String totalPaymentText = commaPriceText(R.string.activity_order_detail_default_price, orderDetailHistoryInfoModel.payAmt);
            tvOrderDetailHistoryPaymentAmount.setText(totalPaymentText);

            //적립금
            TextView tvOrderDetailHistoryPaymentReserve = findViewById(R.id.tvOrderDetailHistoryPaymentReserve);
            String paymentReserveText = commaPriceText(R.string.activity_order_detail_reserve_unit, orderDetailHistoryInfoModel.saveAmt);
            tvOrderDetailHistoryPaymentReserve.setText(paymentReserveText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String commaPriceText(@StringRes int StringResId, int price) {
        // 천단위로 콤마 찍음
        String commaPrice = StringUtil.convertCommaPrice(price);
        // 텍스트 뷰에 표시할 String 문구료 변경
        return getString(StringResId, commaPrice);
    }

    /**
     * 주문 취소
     */
    private void orderCancelDialogShow() {
        YesOrNoDialog yesOrNoDialog = new YesOrNoDialog();
        yesOrNoDialog.dialogContent("주문을 취소 하시겠습니까?");
        yesOrNoDialog.show(getSupportFragmentManager(), "orderCancel");
        yesOrNoDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
            @Override
            public void onDialogNo() {
                yesOrNoDialog.dismiss();
            }

            @Override
            public void onDialogYes() {
                yesOrNoDialog.dismiss();
                orderCancelServer();
            }
        });
    }

    /**
     * 서버에서 주문 상세 정보 조회함
     */
    private void orderServer() {
        ReqOrderDetail reqOrderDetail = new ReqOrderDetail();
        reqOrderDetail.orderId = getIntent().getStringExtra(MoaConstants.EXTRA_ORDER_ID);
        RetrofitClient.getInstance().getMoaService().orderDetailSelect(reqOrderDetail).enqueue(new Callback<ResOrderDetailHistoryShellModel>() {

            @Override
            public void onResponse(@NonNull Call<ResOrderDetailHistoryShellModel> call, @NonNull Response<ResOrderDetailHistoryShellModel> response) {
                ResOrderDetailHistoryShellModel resOrderDetail = response.body();
                if (resOrderDetail == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resOrderDetail)) {
                    orderServer();
                    return;
                }
                init(resOrderDetail.getOrderDetailHistoryInfoModel());
                listUpdate(resOrderDetail.orderDetailHistoryProductList());
            }

            @Override
            public void onFailure(@NonNull Call<ResOrderDetailHistoryShellModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(OrderDetailHistoryActivity.this, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void orderCancelServer() {
        ReqOrderDetail reqOrderDetail = new ReqOrderDetail();
        reqOrderDetail.orderId = getIntent().getStringExtra(MoaConstants.EXTRA_ORDER_ID);
        RetrofitClient.getInstance().getMoaService().orderCancel(reqOrderDetail).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null) {
                    return;
                }

                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    orderCancelServer();
                    return;
                }

                if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    cancelSuccess();
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(intent);
                    orderServer();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //주문취소 성공시 나오는 토스트
    private void cancelSuccess() {
        Toast.makeText(this, "주문이 취소되었습니다.", Toast.LENGTH_SHORT).show();
    }

}

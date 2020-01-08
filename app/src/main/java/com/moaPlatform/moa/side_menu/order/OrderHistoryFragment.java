package com.moaPlatform.moa.side_menu.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.order.model.OrderDto;
import com.moaPlatform.moa.side_menu.order.model.ReqOrder;
import com.moaPlatform.moa.side_menu.order.model.ResOrderCancel;
import com.moaPlatform.moa.side_menu.order.model.ResOrderItems;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.custom_view.CommonLoadingView;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryFragment extends Fragment {
    static final String ARG_ORDER_LIST_START_DATE = "ARG_ORDER_LIST_START_DATE";
    static final String ARG_ORDER_LIST_END_DATE = "ARG_ORDER_LIST_END_DATE";
    static final String ARG_ORDER_LIST_ORDER_TYPE = "ARG_ORDER_LIST_ORDER_TYPE";

    private String listType;
    private String requestStartDate, requestEndDate;

    private TextView tvReservationHistoryTitleCnt;
    private RelativeLayout llReservationHistoryEmptyContainer;

    private OrderRecyclerViewAdapter orderRecyclerViewAdapter;
    private CommonLoadingView viewOrderHistoryLoading;

    @StringRes
    private int orderCountResId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            requestStartDate = getArguments().getString(ARG_ORDER_LIST_START_DATE);
            requestEndDate = getArguments().getString(ARG_ORDER_LIST_END_DATE);
            listType = getArguments().getString(ARG_ORDER_LIST_ORDER_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        initView(view);
        return view;
    }

    void getOrderList(String startDate, String endDate) {
        if (ObjectUtil.checkNotNull(startDate) || ObjectUtil.checkNotNull(endDate)) {
            String requestStartDate = startDate.replace("-", "");
            String requestEndDate = endDate.replace("-", "");

            if (listType.equals(OrderRecyclerViewAdapter.LIST_TYPE_ORDER_TYPE)) {
                getOrderHistory(requestStartDate, requestEndDate);
                orderCountResId = R.string.order_history_count;
            } else {
                getOrderCancelList(requestStartDate, requestEndDate);
                orderCountResId = R.string.order_cancel_count;
            }
        } else {
            Logger.d("필수 파라미터가 존재하지 않습니다.");
        }
    }

    private void initView(View view) {
        // 로딩 세팅
        viewOrderHistoryLoading = view.findViewById(R.id.viewOrderHistoryLoading);

        tvReservationHistoryTitleCnt = view.findViewById(R.id.tvReservationHistoryTitleCnt);
        RecyclerView recyclerReservationHistory = view.findViewById(R.id.recyclerReservationHistory);
        llReservationHistoryEmptyContainer = view.findViewById(R.id.llReservationHistoryEmptyContainer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerReservationHistory.setLayoutManager(layoutManager);
        orderRecyclerViewAdapter = new OrderRecyclerViewAdapter(getContext(), listType);
        recyclerReservationHistory.setAdapter(orderRecyclerViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderList(requestStartDate, requestEndDate);
    }

    private void listUpdate(List<OrderDto> orderList) {

        if (orderList != null && orderList.size() > 0) {
            llReservationHistoryEmptyContainer.setVisibility(View.GONE);
        } else {
            if (llReservationHistoryEmptyContainer != null) {
                llReservationHistoryEmptyContainer.setVisibility(View.VISIBLE);
            }
        }
        orderRecyclerViewAdapter.setData(orderList);
        orderRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * 주문 내역 조회
     */
    private void getOrderHistory(String start, String end) {
        viewOrderHistoryLoading.loadingAnimationPlay(getActivity());

        ReqOrder reqOrderCheck = new ReqOrder();
        reqOrderCheck.postStartDt = start;
        reqOrderCheck.postLastDt = end;
        RetrofitClient.getInstance().getMoaService().orderListSelect(reqOrderCheck).enqueue(new Callback<ResOrderItems>() {

            @Override
            public void onResponse(@NonNull Call<ResOrderItems> call, @NonNull Response<ResOrderItems> response) {
                ResOrderItems resOrderItems = response.body();
                if (resOrderItems == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resOrderItems)) {
                    getOrderHistory(start, end);
                    return;
                }
                tvReservationHistoryTitleCnt.setText(getString(orderCountResId, String.valueOf(resOrderItems.orderList.size())));
                listUpdate(resOrderItems.orderList);
                viewOrderHistoryLoading.animationStop(getActivity());
            }

            @Override
            public void onFailure(@NonNull Call<ResOrderItems> call, @NonNull Throwable t) {
                viewOrderHistoryLoading.animationStop(getActivity());
                Logger.e(t.getMessage());
                Toast.makeText(getContext(), getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 취소 내역 조회
     */
    private void getOrderCancelList(String start, String end) {
        viewOrderHistoryLoading.loadingAnimationPlay(getActivity());

        ReqOrder reqOrderCancel = new ReqOrder();
        reqOrderCancel.postStartDt = start;
        reqOrderCancel.postLastDt = end;
        RetrofitClient.getInstance().getMoaService().orderListCancle(reqOrderCancel).enqueue(new Callback<ResOrderCancel>() {

            @Override
            public void onResponse(@NonNull Call<ResOrderCancel> call, @NonNull Response<ResOrderCancel> response) {
                ResOrderCancel resOrderCancel = response.body();
                if (resOrderCancel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resOrderCancel)) {
                    getOrderCancelList(start, end);
                    return;
                }
                if (resOrderCancel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    tvReservationHistoryTitleCnt.setText(getString(orderCountResId, String.valueOf(resOrderCancel.orderList.size())));
                    listUpdate(resOrderCancel.orderList);
                }
                viewOrderHistoryLoading.animationStop(getActivity());
            }

            @Override
            public void onFailure(@NonNull Call<ResOrderCancel> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(getContext(), getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
                viewOrderHistoryLoading.animationStop(getActivity());
            }
        });
    }

}

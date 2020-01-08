package com.moaPlatform.moa.side_menu.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.coupon.model.AvailableCouponsItems;
import com.moaPlatform.moa.side_menu.coupon.model.ResSelectCoupon;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponFragment extends Fragment {
    private boolean isAvailableCoupon = true;
    private View view;
    private CouponRecyclerViewAdapter couponRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private RelativeLayout linNoCoupon, coupon_count;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.coupon_recyclerview, container, false);
        init();
        couponSetting();

        TextView couponRegister = view.findViewById(R.id.couponRegister);
        couponRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CouponRegisterMainActivity.class);
            startActivity(intent);
        });

        if (isAvailableCoupon) {
            couponUsedServer();
        } else {
            couponServer();
        }
        return view;
    }

    void setUnavailableCoupon() {
        isAvailableCoupon = false;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.coupon_recycle);
        linNoCoupon = view.findViewById(R.id.nocoupon);
        coupon_count = view.findViewById(R.id.coupon_count_toptolbar);
    }

    private void couponSetting() {
        couponRecyclerViewAdapter = new CouponRecyclerViewAdapter(isAvailableCoupon);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(couponRecyclerViewAdapter);
    }

    private void emptyView() {
        recyclerView.setVisibility(View.GONE);
        linNoCoupon.setVisibility(View.VISIBLE);
        coupon_count.setVisibility(View.VISIBLE);
    }

    private void listUpdate(List<AvailableCouponsItems> arrayList) {
        if (arrayList == null)
            return;
        couponRecyclerViewAdapter.setData(arrayList);
        couponRecyclerViewAdapter.notifyDataSetChanged();
        if (arrayList.size() == 0) {
            emptyView();
        }
    }

    private void initCountCoupon() {
        coupon_count.setVisibility(View.GONE);
    }

    private void initView(ResSelectCoupon resSelectCoupon) {
        TextView coupon_number = view.findViewById(R.id.coupon_count);
        coupon_number.setText(String.valueOf(resSelectCoupon.userCpCnt));
    }

    private void couponServer() {
        CommonModel commonModel = new CommonModel();

        RetrofitClient.getInstance().getMoaService().couponSelect(commonModel).enqueue(new Callback<ResSelectCoupon>() {
            @Override
            public void onResponse(@NonNull Call<ResSelectCoupon> call, @NonNull Response<ResSelectCoupon> response) {
                ResSelectCoupon resSelectCoupon = response.body();
                if (resSelectCoupon == null) {
                    emptyView();
                    return;
                }
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resSelectCoupon)) {
                    couponServer();
                    return;
                }
                listUpdate(resSelectCoupon.couponList);
                initView(resSelectCoupon);
            }

            @Override
            public void onFailure(@NonNull Call<ResSelectCoupon> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(view.getContext(), getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void couponUsedServer() {
        CommonModel commonModel = new CommonModel();
        RetrofitClient.getInstance().getMoaService().couponUsedSelect(commonModel).enqueue(new Callback<ResSelectCoupon>() {

            @Override
            public void onResponse(@NonNull Call<ResSelectCoupon> call, @NonNull Response<ResSelectCoupon> response) {
                ResSelectCoupon resSelectCoupon = response.body();
                if (resSelectCoupon == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resSelectCoupon)) {
                    couponServer();
                    return;
                }
                listUpdate(resSelectCoupon.couponList);
                initCountCoupon();
            }

            @Override
            public void onFailure(@NonNull Call<ResSelectCoupon> call, @NonNull Throwable t) {
                Logger.i(t.getMessage());
                Toast.makeText(view.getContext(), getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }
}


package com.moaPlatform.moa.top_menu.location.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.top_menu.location.model.ResAddressSearchBaseModel;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressViewHolder extends RecyclerView.ViewHolder implements ViewDataInitHelper {
    private ViewDataInitHelper viewDataInitHelper;
    @BindView(R.id.tvItemLocationSettingAddressRoadAddress)
    TextView roadAddress;
    @BindView(R.id.tvItemLocationSettingAddressJibunAddress)
    TextView jibunAddress;
    public @BindView(R.id.ivItemLocationSettingAddressIc)
    ImageView locationIc;
    public @BindView(R.id.llItemLocationSettingAddressRemoveGroup)
    View addressRemove;
    @BindView(R.id.llLocationSettingAddressHeader)
    LinearLayout viewTitle;
    @BindView(R.id.tvItemLocationSettingAddressHeaderTitle)
    TextView tvTitle;
    @BindView(R.id.llLocationEmptyAddress)
    View llLocationEmptyAddress;

    public AddressViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        viewDataInitHelper = this;
        llLocationEmptyAddress.setVisibility(View.GONE);
    }

    public void init(ResAddressSearchBaseModel.AddressHistoryModel addressHistoryModel, int position) {

        locationIcInit(R.drawable.mylocation_g);
        roadAddress.setText(addressHistoryModel.roadDefaultAddress);
        jibunAddress.setText(viewDataInitHelper.getChangeHtmlFormat(addressHistoryModel.jibunAddress));

        if (position <= 1) {
            viewTitle.setVisibility(View.VISIBLE);
            if (position == 0) {
                tvTitle.setText("기본 주소");
                locationIcInit(R.drawable.mylocation);
                addressRemove.setVisibility(View.GONE);
                return;
            }
            tvTitle.setText("최근 주소");
        } else {
            viewTitle.setVisibility(View.GONE);
        }

    }

    public void addressSearchInit(ResAddressSearchBaseModel.AddressModel addressModel) {
        locationIcInit(R.drawable.mylocation_g);
        roadAddress.setText(addressModel.fullRoadAddress);
        jibunAddress.setText(viewDataInitHelper.getChangeHtmlFormat(itemView.getResources().getString(R.string.location_setting_address_item_jibun, addressModel.jibunAddress)));
        addressRemove.setVisibility(View.GONE);
    }

    public void titleShow() {
        viewTitle.setVisibility(View.VISIBLE);
    }

    public void titleHidden() {
        viewTitle.setVisibility(View.GONE);
    }

    private void locationIcInit(int imgFile) {
        Glide.with(itemView)
                .load(imgFile)
                .into(locationIc);
    }
}

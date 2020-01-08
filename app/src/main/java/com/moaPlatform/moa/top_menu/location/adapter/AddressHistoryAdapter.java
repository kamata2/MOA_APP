package com.moaPlatform.moa.top_menu.location.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.top_menu.location.model.ResAddressSearchBaseModel;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddressHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ResAddressSearchBaseModel.AddressHistoryModel> addressHistoryModelList = new ArrayList<>();
    private ListItemClickListener listItemClickListener;

    public enum AddressHistoryClickType {
        // 주소 삭제
        ADDRESS_REMOVE,
        // 기본 주소 변경
        DEFAULT_ADDRESS_CHANGE
    }

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void setAddressHistoryModelList(List<ResAddressSearchBaseModel.AddressHistoryModel> addressHistoryModelList) {
        this.addressHistoryModelList.clear();
        this.addressHistoryModelList.addAll(addressHistoryModelList);
        if (addressHistoryModelList.size() == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeChanged(0, addressHistoryModelList.size());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_setting_address_item, parent, false);
        if (addressHistoryModelList.size() != 0) {
            return new AddressHistoryHolder(view);
        } else {
            return new AddressEmptyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddressHistoryHolder) {
            AddressHistoryHolder addressHistoryHolder = (AddressHistoryHolder) holder;
            addressHistoryHolder.init(addressHistoryModelList.get(position), position);

            addressHistoryHolder.btnAddressRemove.setOnClickListener(v -> {
                if (listItemClickListener != null) {
                    listItemClickListener.clickItem(AddressHistoryClickType.ADDRESS_REMOVE, position, addressHistoryModelList.get(position));
                }
            });

            addressHistoryHolder.clItemLocationSettingAddressContent.setOnClickListener(v -> {
                if (listItemClickListener != null) {
                    listItemClickListener.clickItem(AddressHistoryClickType.DEFAULT_ADDRESS_CHANGE, position, addressHistoryModelList.get(position));
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return addressHistoryModelList.size() == 0 ? 1 : addressHistoryModelList.size();
    }

    /**
     * 아이템 삭제
     *
     * @param position 삭제할 포지션
     */
    public void removeItem(int position) {
        addressHistoryModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, addressHistoryModelList.size());
    }

    public void defaultAddressChange(int position, ResAddressSearchBaseModel.AddressHistoryModel addressHistoryModel) {
        addressHistoryModelList.get(0).isDefaultAddess = "N";
        addressHistoryModelList.set(position, addressHistoryModel);
        Collections.swap(addressHistoryModelList, 0, position);
        notifyDataSetChanged();
    }

    class AddressHistoryHolder extends RecyclerView.ViewHolder {
        private View llLocationExistAddress;
        private View emptyView;
        public View clItemLocationSettingAddressContent;

        // 헤더 뷰
        private View viewHeader;
        // 헤더 타이틀
        private TextView tvHeaderTitle;

        // 주소 삭제 버튼
        public View btnAddressRemove;
        // 주소 아이콘
        private ImageView addressIc;
        // 도로명 주소
        private TextView tvRoadAddress;
        // 지번 주소
        private TextView tvJibunAddress;

        // 기본 주소 아이콘
        private final int DEFAUL_ADDRESS_IC = R.drawable.mylocation;
        // 최근 주소 아이콘
        private final int RECENTLY_ADDRESS_IC = R.drawable.mylocation_g;

        AddressHistoryHolder(@NonNull View itemView) {
            super(itemView);
            clItemLocationSettingAddressContent = itemView.findViewById(R.id.clItemLocationSettingAddressContent);
            llLocationExistAddress = itemView.findViewById(R.id.llLocationExistAddress);
            llLocationExistAddress.setVisibility(View.VISIBLE);
            emptyView = itemView.findViewById(R.id.llLocationEmptyAddress);
            emptyView.setVisibility(View.GONE);

            viewHeader = itemView.findViewById(R.id.llLocationSettingAddressHeader);
            tvHeaderTitle = itemView.findViewById(R.id.tvItemLocationSettingAddressHeaderTitle);

            btnAddressRemove = itemView.findViewById(R.id.llItemLocationSettingAddressRemoveGroup);
            addressIc = itemView.findViewById(R.id.ivItemLocationSettingAddressIc);
            tvRoadAddress = itemView.findViewById(R.id.tvItemLocationSettingAddressRoadAddress);
            tvJibunAddress = itemView.findViewById(R.id.tvItemLocationSettingAddressJibunAddress);
        }

        public void init(ResAddressSearchBaseModel.AddressHistoryModel addressHistoryModel, int position) {

            tvRoadAddress.setText(addressHistoryModel.roadDefaultAddress);
            tvJibunAddress.setText(StringUtil.convertHtmlFormat(addressHistoryModel.jibunAddress));

            if (position <= 1) {
                viewHeader.setVisibility(View.VISIBLE);
                if (position == 0) {
                    tvHeaderTitle.setText(itemView.getResources().getString(R.string.location_setting_default_address));
                    btnAddressRemove.setVisibility(View.GONE);
                    locationIcInit(DEFAUL_ADDRESS_IC);
                    return;
                } else {
                    tvHeaderTitle.setText(itemView.getResources().getString(R.string.location_setting_recently_address));
                }
            } else {
                viewHeader.setVisibility(View.GONE);
            }

            // 최근 주소의 위치 아이콘
            locationIcInit(RECENTLY_ADDRESS_IC);

        }

        private void locationIcInit(int imgFile) {
            Glide.with(itemView)
                    .load(imgFile)
                    .into(addressIc);
        }
    }

    class AddressEmptyHolder extends RecyclerView.ViewHolder {
        View emptyView;
        View llLocationExistAddress;
        // 주소 없을때 표시할 문구
        TextView tvEmptyText;

        AddressEmptyHolder(@NonNull View itemView) {
            super(itemView);
            llLocationExistAddress = itemView.findViewById(R.id.llLocationExistAddress);
            llLocationExistAddress.setVisibility(View.GONE);
            emptyView = itemView.findViewById(R.id.llLocationEmptyAddress);
            emptyView.setVisibility(View.VISIBLE);

            tvEmptyText = itemView.findViewById(R.id.tvItemLocationSettingAddressEmptyText);
            tvEmptyText.setText(StringUtil.convertHtmlFormat(itemView.getResources().getString(R.string.location_setting_search_address_introduce)));
        }
    }
}

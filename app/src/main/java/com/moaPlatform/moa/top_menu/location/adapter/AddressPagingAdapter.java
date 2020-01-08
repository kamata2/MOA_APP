package com.moaPlatform.moa.top_menu.location.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.top_menu.location.holder.AddressLoadingViewHolder;
import com.moaPlatform.moa.top_menu.location.holder.AddressViewHolder;
import com.moaPlatform.moa.top_menu.location.listener.PagingItemClickListener;
import com.moaPlatform.moa.top_menu.location.model.ResAddressSearchBaseModel;
import com.moaPlatform.moa.util.Logger;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class AddressPagingAdapter extends PagedListAdapter<ResAddressSearchBaseModel.AddressModel, RecyclerView.ViewHolder> {

    private Boolean isLoading = true;
    private PagingItemClickListener pagingItemClickListener;

    public enum AddressPagingClickType {
        ADDRESS_CLICK
    }

    public AddressPagingAdapter(PagingItemClickListener pagingItemClickListener) {
        super(DIFF_CALLBACK);
        this.pagingItemClickListener = pagingItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == R.layout.location_setting_address_item) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_setting_address_item, parent, false);
            return new AddressViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_setting_address_loading_item, parent, false);
            Logger.i("view 값 : 로딩 ");
            return new AddressLoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case R.layout.location_setting_address_item :
                ResAddressSearchBaseModel.AddressModel addressModel = getItem(position);
                if (addressModel != null) {
                    AddressViewHolder addressViewHolder = ((AddressViewHolder)holder);
                    if (position == 0)
                        addressViewHolder.titleShow();
                    else
                        addressViewHolder.titleHidden();
                    addressViewHolder.addressSearchInit(addressModel);
                    addressViewHolder.itemView.setOnClickListener((View view) -> {
                        if (pagingItemClickListener != null) {
                            pagingItemClickListener.ItemClickListener(addressModel);
                        }
                    });
                } else {
                    Logger.i("데이터 널");
                }
                break;
            case R.layout.location_setting_address_loading_item :
                Logger.i("view 값 : bind ");
                break;
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (isLoading && position == getItemCount() - 1) {
            return R.layout.location_setting_address_loading_item;
        } else {
            return R.layout.location_setting_address_item;
        }
    }

    /**
     * 로딩 관련 작업
     * @param isLoading
     * 유딩 유무
     */
    public void setLoading(boolean isLoading) {
        boolean before = this.isLoading;
        this.isLoading = isLoading;
        boolean after = this.isLoading;
        if (before != after) {
            if (before) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        }
    }

    private static DiffUtil.ItemCallback<ResAddressSearchBaseModel.AddressModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<ResAddressSearchBaseModel.AddressModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ResAddressSearchBaseModel.AddressModel oldItem, @NonNull ResAddressSearchBaseModel.AddressModel newItem) {
            Logger.i("areItemsTheSame");
            return oldItem.bdMgtSn.equals(newItem.bdMgtSn);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ResAddressSearchBaseModel.AddressModel oldItem, @NonNull ResAddressSearchBaseModel.AddressModel newItem) {
            Logger.i("areContentsTheSame");
            return oldItem.equals(newItem);
        }
    };
}

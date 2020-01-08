package com.moaPlatform.moa.side_menu.order.detail.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.order.detail.model.ResOrderDetailHistoryShellModel;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.StringUtil;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailHistoryHolder> {

    private List orderDetailHistoryProductList;
    int productCount = 1;

    @NonNull
    @Override
    public OrderDetailHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail_history_product, parent, false);
        return new OrderDetailHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailHistoryHolder holder, int position) {
        if (orderDetailHistoryProductList.get(position) instanceof ResOrderDetailHistoryShellModel.ResOrderDetailHistoryProductInfoModel) {
            ResOrderDetailHistoryShellModel.ResOrderDetailHistoryProductInfoModel defaultProductModel;
            defaultProductModel = (ResOrderDetailHistoryShellModel.ResOrderDetailHistoryProductInfoModel) orderDetailHistoryProductList.get(position);
            holder.defaultProductInit(defaultProductModel);
        } else {
            ResOrderDetailHistoryShellModel.ResOrderDetailHistoryAddOptionProductModel addProductModel;
            addProductModel = (ResOrderDetailHistoryShellModel.ResOrderDetailHistoryAddOptionProductModel) orderDetailHistoryProductList.get(position);
            holder.addProductInit(addProductModel);
        }
    }

    @Override
    public int getItemCount() {
        return orderDetailHistoryProductList == null ? 0 : orderDetailHistoryProductList.size();
    }


    public void setData(List<Object> orderDetailHistoryProductList) {
        this.orderDetailHistoryProductList = orderDetailHistoryProductList;
    }

    class OrderDetailHistoryHolder extends RecyclerView.ViewHolder {
        TextView tvItemOrderDetailHistoryProductName, tvItemOrderDetailHistoryProductPrice;
        // 상품 이름 (기본 옵션 있을경우)
        @StringRes
        int productSingleResId = R.string.item_order_detail_history_product_name_single;
        // 상품 이름 (기본 옵션 없을 경우)
        @StringRes
        int productDefaultOptionResId = R.string.item_order_detail_history_product_name_default_option;
        // 추가 옵션
        @StringRes
        int addOptionProductResId = R.string.item_order_detail_history_add_product_product_name_single;
        // 상품 가격 표시용
        @StringRes
        int priceResId = R.string.item_order_detail_history_product_price_unit;

        OrderDetailHistoryHolder(@NonNull View itemView) {
            super(itemView);
            // 상품 이름
            tvItemOrderDetailHistoryProductName = itemView.findViewById(R.id.tvItemOrderDetailHistoryProductName);

            // 상품 가격
            tvItemOrderDetailHistoryProductPrice = itemView.findViewById(R.id.tvItemOrderDetailHistoryProductPrice);
        }

        /**
         * 기본 옵션 상품 세팅
         *
         * @param defaultProductModel 기본 상품 정보가 담겨있는 모델
         */
        void defaultProductInit(ResOrderDetailHistoryShellModel.ResOrderDetailHistoryProductInfoModel defaultProductModel) {
            productCount = defaultProductModel.productCount;
            // 상품 이름 세팅
            String productNameText;
            if (ObjectUtil.checkNotNull(defaultProductModel.storeDefaultOptionName) && !defaultProductModel.storeDefaultOptionName.equals("")) {
                productNameText = itemView.getResources().getString(productDefaultOptionResId, defaultProductModel.storeName, defaultProductModel.storeDefaultOptionName, defaultProductModel.productCount);
            } else {
                productNameText = itemView.getResources().getString(productSingleResId, defaultProductModel.storeName, defaultProductModel.productCount);
            }
            tvItemOrderDetailHistoryProductName.setText(productNameText);
            // 가격 세팅
            String defaultProductPriceText = getPriceText(defaultProductModel.getProductPrice());
            tvItemOrderDetailHistoryProductPrice.setText(defaultProductPriceText);

        }

        /**
         * 추가옵션 상품 세팅
         *
         * @param addProductModel 추가 옵션 상품 정보가 담겨있는 모델
         */
        void addProductInit(ResOrderDetailHistoryShellModel.ResOrderDetailHistoryAddOptionProductModel addProductModel) {
            // 상품 이름 세팅
            String productNameText;
            productNameText = itemView.getResources().getString(addOptionProductResId, addProductModel.addOptionProductName);
            tvItemOrderDetailHistoryProductName.setText(productNameText);
            // 가격 세팅
            String addProductPriceText = getPriceText(addProductModel.addOptionProductPrice * productCount);
            tvItemOrderDetailHistoryProductPrice.setText(addProductPriceText);
        }

        /**
         * return
         */
        private String getPriceText(int price) {
            String priceComma = StringUtil.convertCommaPrice(price);
            return itemView.getResources().getString(priceResId, priceComma);
        }
    }
}

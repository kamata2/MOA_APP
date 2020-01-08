package com.moaPlatform.moa.delivery_menu.store_product.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.delivery_menu.store_product.model.ResStoreProductDetailInfoModel;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.MoneyConverter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreProductPriceAdapter extends RecyclerView.Adapter<StoreProductPriceAdapter.StoreProductPriceHolder> implements MoneyConverter{
    private int selectRadioItemPos = 0;
    private int defaultProductPrice = 0;
    private ArrayList<ResStoreProductDetailInfoModel.DefaultProductOptionModel> defaultProductOptionList;
    private MoneyConverter moneyConverter = this;

    private ListItemClickListener listItemClickListener;

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void storProdOptListSetting(ArrayList<ResStoreProductDetailInfoModel.DefaultProductOptionModel> defaultProductOptionList, int defaultProductPrice) {
        this.defaultProductOptionList = defaultProductOptionList;
        this.defaultProductPrice = defaultProductPrice;
    }

    @NonNull
    @Override
    public StoreProductPriceAdapter.StoreProductPriceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_product_price_item, parent, false);
        return new StoreProductPriceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreProductPriceHolder holder, int position) {
        ResStoreProductDetailInfoModel.DefaultProductOptionModel storeProductOptionModel = defaultProductOptionList.get(position);
        holder.init(storeProductOptionModel, position);
        holder.itemView.setOnClickListener((View view) -> {
            listItemClickListener.clickItem(CodeTypeManager.StoreInfo.STORE_PRODUCT_DEFAULT_OPTION_CHANGE.getType(), position, storeProductOptionModel);
            selectRadioItemPos = position;
        });
    }

    @Override
    public int getItemCount() {
        return defaultProductOptionList == null ? 0 : defaultProductOptionList.size();
    }

    class StoreProductPriceHolder extends RecyclerView.ViewHolder {
        // 가격 옵션 라이오 버튼
        @BindView(R.id.priceOptionName)
        public RadioButton priceOptionName;
        // 옵션별 가격
        @BindView(R.id.price)
        TextView tvPrice;

        StoreProductPriceHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void init(ResStoreProductDetailInfoModel.DefaultProductOptionModel storeProductOptionModel, int position) {
            priceOptionName.setText(storeProductOptionModel.productOptionName);
            priceOptionName.setChecked(position == selectRadioItemPos);
            tvPrice.setText(moneyConverter.commaUnitChange((storeProductOptionModel.productOptionPrice + defaultProductPrice), R.string.store_product_price_item_option_price, itemView ));
        }
    }

}

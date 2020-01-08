package com.moaPlatform.moa.delivery_menu.store_product.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.delivery_menu.store_product.model.AddProductOptionModel;
import com.moaPlatform.moa.delivery_menu.store_product.model.ResStoreProductDetailInfoModel;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.MoneyConverter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddProductOptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MoneyConverter {

    private final int VIEW_TYPE_HEADER = 0;
    private ArrayList<AddProductOptionModel> addProductOptionModels;
    private MoneyConverter moneyConverter = this;

    private ListItemChangeListener listItemChangeListener;
    private ListItemClickListener listItemClickListener;

    public void listUpdate(ArrayList<AddProductOptionModel> addProductOptionModels) {
        this.addProductOptionModels = addProductOptionModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_product_add_product_item_header, parent, false);
            return new AddProductOptionHeaderHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_product_add_product_item_content, parent, false);
            return new AddProductOptionContentHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddProductOptionHeaderHolder) {
            AddProductOptionHeaderHolder headerHolder = (AddProductOptionHeaderHolder) holder;
            headerHolder.init((ResStoreProductDetailInfoModel.AddProductCategoryModel) addProductOptionModels.get(position));
        } else {
            AddProductOptionContentHolder contentHolder = (AddProductOptionContentHolder) holder;
            ResStoreProductDetailInfoModel.DefaultProductOptionModel defaultProductOptionModel = (ResStoreProductDetailInfoModel.DefaultProductOptionModel) addProductOptionModels.get(position);
            contentHolder.init(defaultProductOptionModel);

            contentHolder.itemView.setOnClickListener((View view) -> {

                if (contentHolder.isAddProductCheck()) {
                    if(listItemClickListener != null){
                        listItemClickListener.clickItem(CodeTypeManager.StoreInfo.STORE_PRODUCT_ADD_OPTION_REMOVE.getType(), position, defaultProductOptionModel);
                        contentHolder.addProductCheckBoxChange();
                    }
                } else {
                    if (listItemChangeListener != null) {
                        listItemChangeListener.onItemChanged(CodeTypeManager.StoreInfo.STORE_PRODUCT_ADD_OPTION_ADD.getType(), position, defaultProductOptionModel, contentHolder);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return addProductOptionModels == null ? 0 : addProductOptionModels.size();

    }

    @Override
    public int getItemViewType(int position) {
        final int VIEW_TYPE_CONTENT = 1;
        return addProductOptionModels.get(position) instanceof ResStoreProductDetailInfoModel.AddProductCategoryModel ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    class AddProductOptionHeaderHolder extends RecyclerView.ViewHolder {
        private TextView title;

        AddProductOptionHeaderHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }

        void init(ResStoreProductDetailInfoModel.AddProductCategoryModel addProductCategoryModel) {
            title.setText(String.format(
                    itemView.getResources().getString(R.string.store_product_add_option_head_title),
                    addProductCategoryModel.addProductCategoryName,
                    addProductCategoryModel.duplicateSelectionCount)
            );
        }

    }

    public class AddProductOptionContentHolder extends RecyclerView.ViewHolder {
        private CheckBox cbAddOptProductCheckBox;
        private TextView tvPrice;

        AddProductOptionContentHolder(@NonNull View itemView) {
            super(itemView);
            cbAddOptProductCheckBox = itemView.findViewById(R.id.addOptProduct);
            tvPrice = itemView.findViewById(R.id.addOptPrice);
        }

        void init(ResStoreProductDetailInfoModel.DefaultProductOptionModel defaultProductOptionModel) {
            cbAddOptProductCheckBox.setText(defaultProductOptionModel.productOptionName);
            tvPrice.setText(
                    moneyConverter.commaUnitChange(defaultProductOptionModel.productOptionPrice, R.string.store_product_add_option_price, itemView)
            );
        }

        boolean isAddProductCheck() {
            return cbAddOptProductCheckBox.isChecked();
        }

        public void addProductCheckBoxChange() {
            cbAddOptProductCheckBox.setChecked(!cbAddOptProductCheckBox.isChecked());
        }
    }

    public interface ListItemChangeListener<T> {
        void onItemChanged(int type, int position, Object object, RecyclerView.ViewHolder holder);
    }

    public void setListItemChangeListener(ListItemChangeListener listItemChangeListener) {
        this.listItemChangeListener = listItemChangeListener;
    }
    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

}

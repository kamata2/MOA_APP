package com.moaPlatform.moa.bottom_menu.shopping_cart.main;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.delivery_menu.store_product.model.ResStoreProductDetailInfoModel;
import com.moaPlatform.moa.util.TimeEventHelper;
import com.moaPlatform.moa.util.custom_view.CommonTimeEventView;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.MoneyConverter;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ShoppingCartViewHolder> implements MoneyConverter {
    private ArrayList<ResShoppingCartList.ShoppingCartModel> shoppingCartList;
    private MoneyConverter moneyConverter = this;
    private ListItemClickListener listItemClickListener;

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    /**
     * 리스트 업데이트
     *
     * @param shoppingCartList 쇼핑 리스트 갱신할 리스튼
     */
    public void listUpdate(ArrayList<ResShoppingCartList.ShoppingCartModel> shoppingCartList) {
        this.shoppingCartList = shoppingCartList;
    }

    @NonNull
    @Override
    public ShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_default_item, parent, false);
        return new ShoppingCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartViewHolder holder, int position) {
        ResShoppingCartList.ShoppingCartModel shoppingCartModel = shoppingCartList.get(position);
        holder.init(shoppingCartModel);

        holder.clearBtn.setOnClickListener((View v) -> listItemClickListener.clickItem(CodeTypeManager.ShoppingCart.SHOPPING_CART_REMOVE_DIALOG_SHOW.getType(), position, shoppingCartModel));
        holder.itemView.setOnClickListener((View v) -> listItemClickListener.clickItem(CodeTypeManager.ShoppingCart.INTENT_SHOPPING_CART_DETAIL_ACTIVITY.getType(), position, shoppingCartModel));
        holder.btnShoppingCartDefaultOrder.setOnClickListener((View v) -> listItemClickListener.clickItem(CodeTypeManager.ShoppingCart.INTENT_SHOPPING_CART_DETAIL_ACTIVITY.getType(), position, shoppingCartModel));
    }

    @Override
    public int getItemCount() {
        return shoppingCartList == null ? 0 : shoppingCartList.size();
    }

    /**
     * 리스트 삭제
     *
     * @param position 삭제할 포지션
     */
    void removeList(int position) {
        shoppingCartList.remove(position);

        notifyDataSetChanged();
    }

    public class ShoppingCartViewHolder extends RecyclerView.ViewHolder {
        // 썸네일
        ImageView thumbNail;
        // 가게 이름
        public TextView storeName;
        // 메뉴, 총 가격
        TextView tvMenuContent, tvTotalPrice;
        // 비우기 버튼
        Button clearBtn, btnShoppingCartDefaultOrder;

        LinearLayout llShoppingCartTimeEventGroupOne, llShoppingCartTimeEventGroupTwo;
        TextView tvTimeEventOne, tvTimeEventTwo, tvTimeEventThree, tvTimeEventFour;

        // 타임 이벤트 표시할 뷰
        CommonTimeEventView viewShoppingCartDefaultItemTimeEventOne, viewShoppingCartDefaultItemTimeEventTwo;
        CommonTimeEventView viewShoppingCartDefaultItemTimeEventThree, viewShoppingCartDefaultItemTimeEventFour;

        ShoppingCartViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbNail = itemView.findViewById(R.id.thumbNail);
            storeName = itemView.findViewById(R.id.storeName);
            tvMenuContent = itemView.findViewById(R.id.menuContent);
            tvTotalPrice = itemView.findViewById(R.id.totalPrice);
            clearBtn = itemView.findViewById(R.id.clearBtn);
            btnShoppingCartDefaultOrder = itemView.findViewById(R.id.btnShoppingCartDefaultOrder);
            llShoppingCartTimeEventGroupOne = itemView.findViewById(R.id.llShoppingCartTimeEventGroupOne);
            llShoppingCartTimeEventGroupTwo = itemView.findViewById(R.id.llShoppingCartTimeEventGroupTwo);
            tvTimeEventOne = itemView.findViewById(R.id.tvTemp1);
            tvTimeEventTwo = itemView.findViewById(R.id.tvTemp2);
            tvTimeEventThree = itemView.findViewById(R.id.tvTemp3);
            tvTimeEventFour = itemView.findViewById(R.id.tvTemp4);
            // 타임 이벤트 표시 뷰
            viewShoppingCartDefaultItemTimeEventOne = itemView.findViewById(R.id.viewShoppingCartDefaultItemTimeEventOne);
            viewShoppingCartDefaultItemTimeEventTwo = itemView.findViewById(R.id.viewShoppingCartDefaultItemTimeEventTwo);
            viewShoppingCartDefaultItemTimeEventThree = itemView.findViewById(R.id.viewShoppingCartDefaultItemTimeEventThree);
            viewShoppingCartDefaultItemTimeEventFour = itemView.findViewById(R.id.viewShoppingCartDefaultItemTimeEventFour);
        }

        private void init(ResShoppingCartList.ShoppingCartModel shoppingCartModel) {
            List<TextView> timeEventViewList = new ArrayList<>();
            timeEventViewList.add(tvTimeEventOne);
            timeEventViewList.add(tvTimeEventTwo);
            timeEventViewList.add(tvTimeEventThree);
            timeEventViewList.add(tvTimeEventFour);

            List<CommonTimeEventView> commonTimeEventViewList = new ArrayList<>();
            commonTimeEventViewList.add(viewShoppingCartDefaultItemTimeEventOne);
            commonTimeEventViewList.add(viewShoppingCartDefaultItemTimeEventTwo);
            commonTimeEventViewList.add(viewShoppingCartDefaultItemTimeEventThree);
            commonTimeEventViewList.add(viewShoppingCartDefaultItemTimeEventFour);

            for (TextView timeEventView : timeEventViewList) {
                timeEventView.setVisibility(View.GONE);
            }

            storeName.setText(shoppingCartModel.storeName);
            String totalPrice = "총 주문 금액 : " + moneyConverter.commaUnitChange(shoppingCartModel.totalAmount) + "원";
            tvTotalPrice.setText(totalPrice);
            tvMenuContent.setText(getProductList(shoppingCartModel.shoppingCartDetailList).replace(" ", "\u00A0"));
            Glide.with(itemView)
                    .load(BuildConfig.FILE_SERVER_BASE_URL + shoppingCartModel.storeThumbnail)
                    .into(thumbNail);

            StoreInfoModel.TimeEventInfo timeEventInfo = shoppingCartModel.timeEventModel;

            new TimeEventHelper
                    .TimeEventBuilder(commonTimeEventViewList)
                    .setSaveRate(Integer.valueOf(shoppingCartModel.saveRate))
                    .setAddSaveRate(timeEventInfo.addSaveRate)
                    .setFixedRate(timeEventInfo.fixRate)
                    .setFixedAmount(timeEventInfo.fixAmt)
                    .build();

        }

        /**
         * 장바구니의 매장별 상품 리스트 가져오기
         *
         * @param shoppingCartDetailList 장바구니 상세 리스트
         * @return 상품 리스트 반환
         */
        private String getProductList(ArrayList<ResShoppingCartList.ShoppingCartDetailInfoModel> shoppingCartDetailList) {
            List<String> menuList = new ArrayList<>();
            for (ResShoppingCartList.ShoppingCartDetailInfoModel shoppingCartDetailInfoModel : shoppingCartDetailList) {
                List<String> productOptionList = new ArrayList<>();
                for (ResStoreProductDetailInfoModel.AddProductCategoryModel addProductCategoryModel : shoppingCartDetailInfoModel.shoppingCartDetailOptionList) {
                    for (ResStoreProductDetailInfoModel.DefaultProductOptionModel defaultProductOptionModel : addProductCategoryModel.addProductOptionItemList) {
                        productOptionList.add(defaultProductOptionModel.productOptionName);
                    }
                }
                StringBuilder productInfo = new StringBuilder();
                productInfo.append(shoppingCartDetailInfoModel.storeProductName).append(" ").append(shoppingCartDetailInfoModel.cnt);
                if (productOptionList.size() != 0) {
                    productInfo.append("(").append(TextUtils.join(",", productOptionList)).append(")");
                }
                menuList.add(productInfo.toString());
            }
            return TextUtils.join(", ", menuList);
        }
    }

}

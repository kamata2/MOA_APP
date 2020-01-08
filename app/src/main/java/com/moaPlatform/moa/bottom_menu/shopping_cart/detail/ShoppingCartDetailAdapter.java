package com.moaPlatform.moa.bottom_menu.shopping_cart.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.shopping_cart.detail.model.ResShoppingCartDetailModel;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import static com.moaPlatform.moa.bottom_menu.shopping_cart.detail.ShoppingCartDetailAdapter.shoppingCartDetailItemClickType.PRODUCT_COUNT_MINUS;
import static com.moaPlatform.moa.bottom_menu.shopping_cart.detail.ShoppingCartDetailAdapter.shoppingCartDetailItemClickType.PRODUCT_COUNT_PLUS;
import static com.moaPlatform.moa.bottom_menu.shopping_cart.detail.ShoppingCartDetailAdapter.shoppingCartDetailItemClickType.PRODUCT_ITEM_CHECK;
import static com.moaPlatform.moa.bottom_menu.shopping_cart.detail.ShoppingCartDetailAdapter.shoppingCartDetailItemClickType.PRODUCT_ITEM_REMOVE;
import static com.moaPlatform.moa.bottom_menu.shopping_cart.detail.ShoppingCartDetailAdapter.shoppingCartDetailItemClickType.PRODUCT_ITEM_UNCHECK;

public class ShoppingCartDetailAdapter extends RecyclerView.Adapter<ShoppingCartDetailAdapter.ShoppingCartDetailHolder> implements ViewDataInitHelper {

    // 장바구니 리스트
    private List<ResShoppingCartDetailModel.ShoppingCartDetailModel> shoppingCartDetailList = new ArrayList<>();
    private ViewDataInitHelper viewDataInitHelper = this;
    private ListItemClickListener listItemClickListener;

    // 장바구니 리스트 아이템 클릭 타입
    public enum shoppingCartDetailItemClickType {
        // 상품 개수 증가, 상품 개수 감소, 상품 삭제, 상품 체크, 상품 체크 해제
        PRODUCT_COUNT_PLUS, PRODUCT_COUNT_MINUS, PRODUCT_ITEM_REMOVE, PRODUCT_ITEM_CHECK, PRODUCT_ITEM_UNCHECK
    }

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    /**
     * 리스트 세팅
     *
     * @param shoppingCartDetailList 장바구니 리스트
     */
    void adapterListSetting(List<ResShoppingCartDetailModel.ShoppingCartDetailModel> shoppingCartDetailList) {
        // this.shoppingCartDetailList = shoppingCartDetailList 를 할경우
        // 체크박스 선택 및 선택 해제할때 마다 shoppingCartDetailModel.shoppingCartDetailInfoModel.shoppingCartDetailList 에
        // 데이터 추가 및 삭제를 하는데 그 결과가 해당 어뎁터에 있는 shoppingCartDetailList 이 영향을 받음 따라서
        // addAll 을 사용해서 영향을 안받게함
        this.shoppingCartDetailList.addAll(shoppingCartDetailList);
    }

    @NonNull
    @Override
    public ShoppingCartDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_detail_item, parent, false);
        return new ShoppingCartDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingCartDetailHolder holder, int position) {
        ResShoppingCartDetailModel.ShoppingCartDetailModel shoppingCartDetailModel = shoppingCartDetailList.get(position);
        // 뷰 초기화
        holder.init(shoppingCartDetailModel);
        // 상품 개수 감소시해는 상품개수가 0이 되지 않도록 조건문을 걸어줌
        holder.lbQuantityDown.setOnClickListener(v -> {
            if (shoppingCartDetailModel.getCnt() > 1) {
                listItemClickListener.clickItem(PRODUCT_COUNT_MINUS, position, shoppingCartDetailModel);
            }
        });
        // 상품 개수 증가
        holder.lbQuantityUp.setOnClickListener(v -> listItemClickListener.clickItem(PRODUCT_COUNT_PLUS, position, shoppingCartDetailModel));
        // 상품 삭제
        holder.btnClose.setOnClickListener(v -> listItemClickListener.clickItem(PRODUCT_ITEM_REMOVE, position, shoppingCartDetailModel));
        // 상품 체크박스 클릭시
        holder.cbProductName.setOnCheckedChangeListener((buttonView, isChecked) -> listItemClickListener.clickItem(isChecked ? PRODUCT_ITEM_CHECK : PRODUCT_ITEM_UNCHECK, position, shoppingCartDetailModel));
    }

    @Override
    public int getItemCount() {
        return shoppingCartDetailList != null ? shoppingCartDetailList.size() : 0;
    }

    void shoppingCartListRemoveAll() {
        shoppingCartDetailList.clear();
        notifyDataSetChanged();
    }

    public ResShoppingCartDetailModel.ShoppingCartDetailModel getItem(int position) {
        return shoppingCartDetailList.get(position);
    }

    /**
     * 장바구니 상품 개수 증감
     *
     * @param productChangeType 증가 및 감소 할 타입
     * @param position          변경할 n번째 포지션
     */
    void productCountChange(int productChangeType, int position) {
        ResShoppingCartDetailModel.ShoppingCartDetailModel itemModel = shoppingCartDetailList.get(position);
        if (productChangeType == ShoppingCartDetailController.SHOPPING_CART_PRODUCT_ITEM_PLUS) {
            itemModel.setCnt(itemModel.getCnt() + 1);
        } else if (productChangeType == ShoppingCartDetailController.SHOPPING_CART_PRODUCT_ITEM_MINUS) {
            itemModel.setCnt(itemModel.getCnt() - 1);
        }
        notifyItemChanged(position);
    }

    /**
     * 장바구니 아이템 삭제
     *
     * @param position 삭제할 n번쨰 포지션
     */
    void shoppingCartItemRemove(int position) {
        shoppingCartDetailList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, shoppingCartDetailList.size());
    }

    /**
     * 장바구니 상세 정보 홀더
     */
    class ShoppingCartDetailHolder extends RecyclerView.ViewHolder {
        // 상품 이름
        private CheckBox cbProductName;
        // 상품 개수
        private TextView tvProductQuantity;
        // 상품 + 옵션 총합
        private TextView tvSumPrice;
        // 상품 가격
        private TextView tvProductPrice;
        // 옵션 정보들 표시 뷰
        private TextView tvOptionText;
        // 옵션 가격 뷰
        private TextView tvOptionPrice;
        // 상품 감소 버튼, 상품 증가 버튼
        private ImageButton lbQuantityDown, lbQuantityUp;
        // 상품 삭제 아이콘
        private View btnClose;
        // 옵션 레이아웃
        private ConstraintLayout viewOptionGroup;

        ShoppingCartDetailHolder(@NonNull View itemView) {
            super(itemView);
            cbProductName = itemView.findViewById(R.id.storeNameCheckBox);
            tvProductQuantity = itemView.findViewById(R.id.productQuantity);
            tvSumPrice = itemView.findViewById(R.id.sumPrice);
            tvProductPrice = itemView.findViewById(R.id.priceContent);
            tvOptionText = itemView.findViewById(R.id.optionContent);
            tvOptionPrice = itemView.findViewById(R.id.optionPrice);
            lbQuantityDown = itemView.findViewById(R.id.quantityDown);
            lbQuantityUp = itemView.findViewById(R.id.quantityUp);
            btnClose = itemView.findViewById(R.id.btnClose);
            viewOptionGroup = itemView.findViewById(R.id.viewOptionGroup);
        }

        /**
         * 초기화sumPrice
         *
         * @param shoppingCartDetailModel 장바구니 상세 정보 모델
         */
        public void init(ResShoppingCartDetailModel.ShoppingCartDetailModel shoppingCartDetailModel) {
            // 상품 이름 세팅
            cbProductName.setText(shoppingCartDetailModel.sotreProductName);
            // 상품 수량 세팅
            tvProductQuantity.setText(shoppingCartDetailModel.getCntToString());
            // 상품 기본 가격 세팅
            tvProductPrice.setText(viewDataInitHelper.commaUnitChange(shoppingCartDetailModel.getProductPrice(), R.string.shopping_cart_detail_activity_price, itemView));
            // 추가 옵션 가격 및 리스트 구하기
            // 추가 옵션 가격
            int addOptionPrice = 0;
            Map<String, String> addProductMap = shoppingCartDetailModel.getAddProductMap();
            final String ADD_PRODUCT_LIST = addProductMap.get(shoppingCartDetailModel.ADD_PRODUCT_LIST);
            final String ADD_PRODUCT_TOTAL_PRICE = addProductMap.get(shoppingCartDetailModel.ADD_PRODUCT_TOTAL_PRICE);
            // 추가 옵션 유무에 따라 옵션 뷰를 보이거나 숨기고 옵션 리스트 및 가격 세팅

            String addOptionList = "(" + ADD_PRODUCT_LIST + ")";

            if (addOptionList == null || addOptionList.equals("()")) {
                viewOptionGroup.setVisibility(View.GONE);
            } else {
                viewOptionGroup.setVisibility(View.VISIBLE);
                tvOptionText.setText(addOptionList);
                addOptionPrice = Integer.valueOf(ADD_PRODUCT_TOTAL_PRICE);
                tvOptionPrice.setText(viewDataInitHelper.commaUnitChange(addOptionPrice, R.string.shopping_cart_detail_activity_option_price, itemView));
            }

//            if (ObjectUtil.checkNotNull(ADD_PRODUCT_TOTAL_PRICE) && ADD_PRODUCT_TOTAL_PRICE.equals("0")) {
//                viewOptionGroup.setVisibility(View.GONE);
//            } else {
//                viewOptionGroup.setVisibility(View.VISIBLE);
//                String addOptionList = "(" + ADD_PRODUCT_LIST + ")";
//                tvOptionText.setText(addOptionList);
//                addOptionPrice = Integer.valueOf(ADD_PRODUCT_TOTAL_PRICE);
//                tvOptionPrice.setText(viewDataInitHelper.commaUnitChange(addOptionPrice, R.string.shopping_cart_detail_activity_option_price, itemView));
//
//            }
            // 상품 총합 가격 세팅
            tvSumPrice.setText(viewDataInitHelper.commaUnitChange(shoppingCartDetailModel.getTotalPrice(addOptionPrice), R.string.shopping_cart_detail_activity_price, itemView));
        }

    }
}

package com.moaPlatform.moa.util;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.EatOutStoreProductListActivity;
import com.moaPlatform.moa.delivery_menu.store_detail.StoreDetailActivity;
import com.moaPlatform.moa.delivery_menu.store_detail.model.ResStoreDetailInfo;
import com.moaPlatform.moa.delivery_menu.store_product.StoreProductActivity;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * grid
 */
public class StoreGridListFragment extends Fragment implements ListItemClickListener {

    private StoreGridListAdpater storeGridListAdpater;
    private View view;
    private GridLayoutManager gridLayoutManager;
    private String storeName = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            storeName = getArguments().getString(MoaConstants.EXTRA_STORE_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.store_grid_list_fragment, container, false);
        init();
        return view;
    }

    private void init() {
        RecyclerView storeRecyclerView = view.findViewById(R.id.storeRecyclerView);
//        storeRecyclerView.setNestedScrollingEnabled(false);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        storeRecyclerView.setLayoutManager(gridLayoutManager);
        storeGridListAdpater = new StoreGridListAdpater();
        storeGridListAdpater.setListItemClickListener(this);
        storeRecyclerView.setAdapter(storeGridListAdpater);
    }

    /**
     * 리스트 업데이트
     * @param adStoreList
     * 광고 등록한 가맹점 리스트
     */
    public void listUpdate(ArrayList adStoreList) {
        storeGridListAdpater.listUpdate(adStoreList);
        storeGridListAdpater.notifyDataSetChanged();
    }

    /**
     * 리스트 업데이트 - 배달 매장 화면일때
     * @param representativeMenuList
     * 대표 메뉴 리스트
     * @param allMenuList
     * 전체 매뉴 리스트
     */
    public void listUpdate(ArrayList<ResStoreDetailInfo.StoreMenuInfoModel> representativeMenuList, ArrayList<ResStoreDetailInfo.StoreMenuCategoryInfo> allMenuList ) {

        ArrayList<BaseModel> baseList = new ArrayList<>(representativeMenuList);
        int allMenuStartPosition = baseList.size();
        for (int i = 0; i < allMenuList.size(); i++) {
            baseList.add(allMenuList.get(i));
            baseList.addAll(allMenuList.get(i).storeMenuList);
        }
        listUpdate(baseList);
        storeGridListAdpater.setStartAllmenuPosition(allMenuStartPosition);
        gridLayoutManager.setSpanSizeLookup(
                new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (position >= allMenuStartPosition)
                            return 2;
                        return 1;
                    }
                }
        );
    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {
        /**
         * 2019-05-07 EatOutStoreProductListActivity 화면에서는 구매 페이지로 이동하지 않는다.
         */
        if(getActivity() instanceof EatOutStoreProductListActivity){

        }else{
            Intent intent = null;
            if (codeType.equals(CodeTypeManager.StoreInfo.AD_STORE_INFO.getType())) {
                intent = new Intent(getContext(), StoreDetailActivity.class);
                intent.putExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), ((StoreInfoModel) object).storeId);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            } else {
                intent = new Intent(getContext(), StoreProductActivity.class);
                intent.putExtra(CodeTypeManager.StoreInfo.STORE_PRODUCT_CODE.toString(), ((ResStoreDetailInfo.StoreMenuInfoModel)object).productCode);
                //intent.putExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), ((StoreDetailActivity) Objects.requireNonNull(getActivity())).storeId);
                intent.putExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), ((StoreDetailActivity) Objects.requireNonNull(getActivity())).storeId);
                intent.putExtra(MoaConstants.EXTRA_STORE_NAME, storeName);
                intent.putExtra("deliveryTitle", ((StoreDetailActivity)getActivity()).deliveryTitle);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            startActivity(intent);
        }
    }
}

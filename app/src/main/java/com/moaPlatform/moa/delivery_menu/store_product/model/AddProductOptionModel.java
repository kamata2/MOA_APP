package com.moaPlatform.moa.delivery_menu.store_product.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.BaseModel;

public class AddProductOptionModel extends BaseModel {

    //중복선택가능개수
    @SerializedName("dplctChoiceEnblCnt")
    public String duplicateSelectionCount;
    //매장추가상품항목_ID
    @SerializedName("storAddProdCtgryId")
    public int addProductCategoryId;
    // 현재 체크중인 사이즈
    public int checkingSize = 0;

    public boolean checkPossible() {
        if (checkingSize < Integer.valueOf(duplicateSelectionCount)) {
            checkingSize++;
            return true;
        } else {
            return false;
        }
    }
}

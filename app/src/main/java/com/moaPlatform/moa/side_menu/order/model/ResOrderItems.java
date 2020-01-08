package com.moaPlatform.moa.side_menu.order.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class ResOrderItems extends CommonModel {
    @SerializedName("orderDtoList")
    public List<OrderDto> orderList = new ArrayList<>();
}

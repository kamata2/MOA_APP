package com.moaPlatform.moa.top_menu.location;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.main.MainActivity;
import com.moaPlatform.moa.top_menu.location.adapter.AddressHistoryAdapter;
import com.moaPlatform.moa.top_menu.location.adapter.AddressPagingAdapter;
import com.moaPlatform.moa.top_menu.location.listener.PagingItemClickListener;
import com.moaPlatform.moa.top_menu.location.model.AddressViewModel;
import com.moaPlatform.moa.top_menu.location.model.ReqLocationXySearchModel;
import com.moaPlatform.moa.top_menu.location.model.ResAddressSearchBaseModel;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.models.BaseModel;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AddressListFragment extends Fragment implements PagingItemClickListener, ListItemClickListener, RetrofitConnectionResult {

    // 뷰
    private View view;
    // 위치 컨트롤러
    private LocationServerController locationServerController;
    // 주소 관련 뷰 모델
    private AddressViewModel addressViewModel;
    // 주소 리스트 표시할 뷰
    private RecyclerView rvAddressList;
    private AddressPagingAdapter addressPagingAdapter;

    private int clickItemPos = -1;
    private AddressHistoryAdapter addressHistoryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.location_setting_address_list_fragment, container, false);
//        listSetting();
        init();
        return view;
    }

    /**
     * 초기화
     */
    private void init() {

        if (getActivity() != null && getActivity() instanceof LocationSettingActivity) {
            ((LocationSettingActivity)getActivity()).animationStart();
        }

        // 기본 주소 및 최근 주소 적용 어뎁터 세팅
        rvAddressList = view.findViewById(R.id.rvAddressList);
        rvAddressList.setLayoutManager(new LinearLayoutManager(getContext()));
        addressHistoryAdapter = new AddressHistoryAdapter();
        addressHistoryAdapter.setListItemClickListener(addressHistoryClickListener);
        rvAddressList.setAdapter(addressHistoryAdapter);

        // 검색한 주소를 표시할 페이징 어뎁터
        addressPagingAdapter = new AddressPagingAdapter(this);

        // 주소 컨트롤러 세팅
        locationServerController = new LocationServerController(getContext());
        locationServerController.setRetrofitConnectionResult(this);

        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
        // 서버에서 받은 데이터 세팅
        addressViewModel.getUserAddressList().observe(this, addressHistoryModels -> {
            rvAddressList.setAdapter(addressHistoryAdapter);
            addressHistoryAdapter.setAddressHistoryModelList(addressHistoryModels);
            ((LocationSettingActivity)getActivity()).animationStop();
        });
//        addressViewModel.isDataEmpty.observe(this, isDataEmpty -> new Thread(() -> getActivity().runOnUiThread(() -> {
//            TextView tvLocationSettingAddressEmptyAddress = view.findViewById(R.id.tvLocationSettingAddressEmptyAddress);
//            if (isDataEmpty) {
//                tvLocationSettingAddressEmptyAddress.setVisibility(View.VISIBLE);
//            } else {
//                tvLocationSettingAddressEmptyAddress.setVisibility(View.GONE);
//            }
//        })).start());

        // 기본 주소 및 최근 주소 api 호출
        locationServerController.onAddressHistory();

    }

    public void addressHistoryAdapterConnet() {
        rvAddressList.setAdapter(addressHistoryAdapter);
    }

    void addressSearch(String searchKeyword) {
        ((LocationSettingActivity)getActivity()).animationStart();
        rvAddressList.setAdapter(addressPagingAdapter);
        addressViewModel.addressInit(searchKeyword);
        addressViewModel.addressList.observe(this, addressPagingAdapter::submitList);
        addressViewModel.isLoading.observe(this, aBoolean -> {
            addressPagingAdapter.setLoading(aBoolean);
            if (!aBoolean) {
                ((LocationSettingActivity)getActivity()).animationStop();
            }
        });
        // 주소 검색시 데이터 우무 처리
        addressViewModel.isDataEmpty.observe(this, isDataEmpty -> new Thread(() -> getActivity().runOnUiThread(() -> {
            View llLocationSettingAddressEmptyView = view.findViewById(R.id.llLocationSettingAddressEmptyView);
            if (isDataEmpty) {
                llLocationSettingAddressEmptyView.setVisibility(View.VISIBLE);
            } else {
                llLocationSettingAddressEmptyView.setVisibility(View.GONE);
            }
        })).start());

    }

//    void addressSearch() {
//        String inputAddress = "";
//        if (getArguments() != null)
//            inputAddress = getArguments().getString(CodeTypeManager.AddressManager.SEARCH_ADDRESS_KEYWORD.toString());
//        recyclerView.setAdapter(addressPagingAdapter);
//        AddressViewModel addressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
//        addressViewModel.addressInit(inputAddress);
//        addressViewModel.addressList.observe(this, addressPagingAdapter::submitList);
//        addressViewModel.isLoading.observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                addressPagingAdapter.setLoading(aBoolean);
//                if (!aBoolean) {
//                    ((LocationSettingActivity)getActivity()).animationStop();
//                }
//            }
//        });
//    }

    /**
     * 기본주소가 리스트의 0번쨰에 오도록 정렬
     *
     * @param addressHistoryModelList 기본 주소 및 최근 주소가 합쳐진 리스트
     */
    private void onAddressHistorySort(List<ResAddressSearchBaseModel.AddressHistoryModel> addressHistoryModelList) {
        if (addressHistoryModelList.size() > 0) {
            int cnt = 0;
            for (ResAddressSearchBaseModel.AddressHistoryModel addressHistoryModel : addressHistoryModelList) {
                if (addressHistoryModel.isDefaultAddess.equals("Y"))
                    break;
                cnt++;
            }
            Collections.swap(addressHistoryModelList, 0, cnt);
        }
        addressViewModel.setAddressList(addressHistoryModelList);
    }


    /**
     * 기본 주소 변경
     */
    private void defaultAddressChange(int position, ResAddressSearchBaseModel.AddressHistoryModel addressHistoryModel) {
        YesOrNoDialog addressDialog = new YesOrNoDialog();
        addressDialog.dialogContent("기본 주소를 변경하시겠습니까?");
        addressDialog.show(getChildFragmentManager(), "addressChangeDialog");
        addressDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
            @Override
            public void onDialogNo() {
                addressDialog.dismiss();
            }

            @Override
            public void onDialogYes() {
                ((LocationSettingActivity)getActivity()).animationStart();
                clickItemPos = position;
                locationServerController.defaultAddressChange(addressHistoryModel);
                addressDialog.dismiss();
//                locationServerController.defaultAddressChange(addressHistoryModel);
//                locationServerController.setHttpConnectionResult((type, baseModel) -> {
//                    if (((CommonModel) baseModel).resultValue.equals(ServerSideMsg.SUCCESS)) {
//                        addressHistoryModel.isDefaultAddess = "Y";
//                        addressHistoryAdapter.defaultAddressChange(position, addressHistoryModel);
//                        App.getInstance().defaultAddressChange(addressHistoryModel);
//                        addressDialog.dismiss();
//
//                        //2019-07-22 기본 주소를 변경하였을시에는 메인으로 이동처리(임실장님)
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    }
//                });
            }
        });
    }

    @Override
    public void ItemClickListener(BaseModel baseModel) {
        if (baseModel != null) {
            ReqLocationXySearchModel reqModel = new ReqLocationXySearchModel();
            reqModel.init((ResAddressSearchBaseModel.AddressModel) baseModel);
            locationServerController.onLocationSearchXy(reqModel);
        }
    }

    //
    @Override
    public void clickItem(Object codeType, int position, Object object) {
//        if (codeType.equals(CodeTypeManager.AddressManager.ADDRESS_REMOVE.getType())) {
//            locationServerController.removeAddress((ResAddressSearchBaseModel.AddressHistoryModel) object);
//            locationServerController.setHttpConnectionResult((type1, removeResultModel) -> {
//                if (type1 == CodeTypeManager.AddressManager.ADDRESS_REMOVE.getType() && ((CommonModel) removeResultModel).resultValue.equals(ServerSideMsg.SUCCESS))
//                    addressHistoryAdapter.removeItem(position);
//            });
//        } else if (codeType.equals(CodeTypeManager.AddressManager.DEFAULT_ADDRESS_CHANGE.getType())) {
//            defaultAddressChange(position, (ResAddressSearchBaseModel.AddressHistoryModel) object);
//        }
    }

    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {
        if (type == locationServerController.API_ADDRESS_HISTORY) {
            // 기본 주소 및 최근 주소 결과 값
            ResAddressSearchBaseModel resAddressSearchBaseModel = (ResAddressSearchBaseModel) baseModel;
            onAddressHistorySort(resAddressSearchBaseModel.getAddressHistroyModelArrayList());
        } else if (type == locationServerController.API_ADDRESS_REMOVE) {
            addressViewModel.removeItemAddressList(clickItemPos);
            clickItemPos = -1;
        } else if (type == locationServerController.API_COORDINATES_TRANSFORM) {
            if (getActivity() != null && getActivity() instanceof LocationSettingActivity) {
                ((LocationSettingActivity) getActivity()).detailLocationFragmentShow((ResAddressSearchBaseModel.LocationCoordinate) baseModel);
            }
        } else if (type == locationServerController.API_DEFAULT_ADDRESS_CHANGE) {
            ((LocationSettingActivity)getActivity()).animationStop();
            Toast.makeText(getContext(), "위치가 설정되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onRetrofitFail(int type, String msg) {
        clickItemPos = -1;
    }

    ListItemClickListener addressHistoryClickListener = (codeType, position, object) -> {
        switch ((AddressHistoryAdapter.AddressHistoryClickType) codeType) {
            case ADDRESS_REMOVE:
                clickItemPos = position;
                locationServerController.onAddressRemove((ResAddressSearchBaseModel.AddressHistoryModel) object);
                break;
            case DEFAULT_ADDRESS_CHANGE:
                defaultAddressChange(position, (ResAddressSearchBaseModel.AddressHistoryModel) object);
//                clickItemPos = position;
//                locationServerController.defaultAddressChange((ResAddressSearchBaseModel.AddressHistoryModel) object);
                break;
        }
    };

}

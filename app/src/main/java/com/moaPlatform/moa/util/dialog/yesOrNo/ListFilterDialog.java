package com.moaPlatform.moa.util.dialog.yesOrNo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.custom_view.ListFilterItemView;
import com.moaPlatform.moa.util.models.KeyValue;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 리스트 필터 다이얼로그
 * 평점순 | 즐겨찾기 순
 * 적립률 순 | 테이크아웃 매장
 * 거리순  | 사장님 댓글 순
 * 리뷰 순 | 이벤트 여부
 */
public class ListFilterDialog extends BaseDialogFragment {

    private ArrayList<ListFilterItemView> filterViewList = new ArrayList<>();
    private ArrayList<KeyValue> filterDataList = new ArrayList<>();

    private ListFilterDialogFragmentListener listFilterDialogFragmentListener;
    private ListFilterSeletedNameListener listFilterSeletedNameListener;

    public static final String FROM_VIEW_TYPE_EATOUT_LIST = "FROM_VIEW_TYPE_EATOUT_LIST";
    private String fromViewType;

    private String selectedItemKey;
    private String selectedName;

    @BindView(R.id.rlListFilterClose)
    RelativeLayout rlListFilterClose;
    @BindView(R.id.tvListFilterTitle)
    TextView tvListFilterTitle;
    @BindView(R.id.tvListFilterComplete)
    RelativeLayout tvListFilterComplete;
    @BindView(R.id.llListFilterLeftContainer)
    LinearLayout llListFilterLeftContainer;
    @BindView(R.id.llListFilterRightContainer)
    LinearLayout llListFilterRightContainer;
    @BindView(R.id.viewListFilterLeftContainerDivider)
    View viewListFilterLeftContainerDivider;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 타이틀바 제거
        this.setCancelable(false);
        View view = inflater.inflate(R.layout.dialog_list_filter, container, false);
        unbinder = ButterKnife.bind(this, view);

        initListData();
        drawFilterView();

        rlListFilterClose.setOnClickListener((View v) -> this.dismiss());
        tvListFilterComplete.setOnClickListener((View v) -> onClickComplete());     //완료 버튼 선택시 이벤트

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null){
            unbinder.unbind();
        }
    }

    private void initListData(){
        try{
            filterDataList.clear();
            if (fromViewType != null && fromViewType.equals(FROM_VIEW_TYPE_EATOUT_LIST)) {
                filterDataList.add(new KeyValue(MoaConstants.ListFilter.PARAM_VALUE_LIST_FILTER_DISTANCE.getOrder(), getString(R.string.dialog_list_filter_oder_distance)));
                filterDataList.add(new KeyValue(MoaConstants.ListFilter.PARAM_VALUE_LIST_FILTER_GRADE.getOrder(), getString(R.string.dialog_list_filter_oder_grade)));
                filterDataList.add(new KeyValue(MoaConstants.ListFilter.PARAM_VALUE_LIST_FILTER_FAVOR.getOrder(), getString(R.string.dialog_list_filter_oder_favor)));
                filterDataList.add(new KeyValue(MoaConstants.ListFilter.PARAM_VALUE_LIST_FILTER_REVIEW.getOrder(), getString(R.string.dialog_list_filter_oder_review)));
            }else{
                filterDataList.add(new KeyValue(MoaConstants.ListFilter.PARAM_VALUE_LIST_FILTER_GRADE.getOrder(), getString(R.string.dialog_list_filter_oder_grade)));
                filterDataList.add(new KeyValue(MoaConstants.ListFilter.PARAM_VALUE_LIST_FILTER_SAVING.getOrder(), getString(R.string.dialog_list_filter_oder_saving)));
                filterDataList.add(new KeyValue(MoaConstants.ListFilter.PARAM_VALUE_LIST_FILTER_DISTANCE.getOrder(), getString(R.string.dialog_list_filter_oder_distance)));
                filterDataList.add(new KeyValue(MoaConstants.ListFilter.PARAM_VALUE_LIST_FILTER_REVIEW.getOrder(), getString(R.string.dialog_list_filter_oder_review)));
                filterDataList.add(new KeyValue(MoaConstants.ListFilter.PARAM_VALUE_LIST_FILTER_FAVOR.getOrder(), getString(R.string.dialog_list_filter_oder_favor)));
                filterDataList.add(new KeyValue(MoaConstants.ListFilter.PARAM_VALUE_LIST_FILTER_TAKE_OUT.getOrder(), getString(R.string.dialog_list_filter_oder_takeout)));
                filterDataList.add(new KeyValue(MoaConstants.ListFilter.PARAM_VALUE_LIST_FILTER_OWNER_REPLY.getOrder(), getString(R.string.dialog_list_filter_oder_owner_reply)));
                filterDataList.add(new KeyValue(MoaConstants.ListFilter.PARAM_VALUE_LIST_FILTER_EVENT.getOrder(), getString(R.string.dialog_list_filter_oder_event)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void drawFilterView() {
        try {

            filterViewList.clear();
            llListFilterLeftContainer.removeAllViews();
            llListFilterRightContainer.removeAllViews();


            if (ObjectUtil.checkNotNull(fromViewType) && fromViewType.equals(FROM_VIEW_TYPE_EATOUT_LIST)) {
                viewListFilterLeftContainerDivider.setVisibility(View.GONE);
            }

            for (int position = 0; position < filterDataList.size(); position++) {

                ListFilterItemView filterView = new ListFilterItemView(getContext());
                filterView.setSelected(false);
                filterView.setText(filterDataList.get(position).value);
                filterView.setTag(filterDataList.get(position).key);
                filterView.setOnClickListener(v -> drawSelectedItem(filterView.getTag().toString()));

                filterViewList.add(filterView);

                if (fromViewType != null && fromViewType.equals(FROM_VIEW_TYPE_EATOUT_LIST)) {
                    llListFilterLeftContainer.addView(filterView);
                }else{
                    if (position > 3) {
                        llListFilterRightContainer.addView(filterView);
                    } else {
                        llListFilterLeftContainer.addView(filterView);
                    }
                }
            }

            drawSelectedItem(selectedItemKey);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 선택한 항목 v 체크 및 텍스트 색상 UI 변경처리
     * @param key
     */
    private void drawSelectedItem(String key){

        for(ListFilterItemView view : filterViewList){
            if(view != null && view.getTag() != null){
                if(view.getTag().equals(key)){
                    selectedItemKey = key;
                    selectedName = view.getTvFilterText();
                    view.setSelected(true);
                }else{
                    view.setSelected(false);
                }
            }
        }
    }

    private void onClickComplete(){
        if(listFilterDialogFragmentListener != null){
            if(selectedItemKey != null){
                listFilterDialogFragmentListener.onSelectItem(selectedItemKey);
            }
        }

        if (listFilterSeletedNameListener != null) {
            if (selectedName != null) {
                listFilterSeletedNameListener.onSelectItemName(selectedName);
            }
        }
        dismiss();
    }

    /**
     * java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
     * 오류 해결하기 위해 사용
     */
    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    public interface ListFilterDialogFragmentListener{
        public void onSelectItem(String order);
    }

    public interface ListFilterSeletedNameListener{
        public void onSelectItemName(String name);
    }

    public void setSelectedItemKey(String selectedItemKey) {
        this.selectedItemKey = selectedItemKey;
    }

    public void setListFilterDialogFragmentListener(ListFilterDialogFragmentListener listFilterDialogFragmentListener) {
        this.listFilterDialogFragmentListener = listFilterDialogFragmentListener;
    }

    public void setListFilterSeletedNameListener(ListFilterSeletedNameListener listFilterSeletedNameListener) {
        this.listFilterSeletedNameListener = listFilterSeletedNameListener;
    }

    public void setFromViewType(String fromViewType) {
        this.fromViewType = fromViewType;
    }
}

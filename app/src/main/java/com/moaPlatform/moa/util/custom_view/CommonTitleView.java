package com.moaPlatform.moa.util.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moaPlatform.moa.R;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import butterknife.BindView;

/**
 * 공통 타이틀바
 */
public class CommonTitleView extends BaseLinearLayoutView {

    @BindView(R.id.rlCommonTitleViewCotainer)
    RelativeLayout rlCommonTitleViewCotainer;

    @BindView(R.id.rlCommonTitleBack)
    RelativeLayout rlBack;

    @BindView(R.id.btnCommonTitleText)
    TextView tvTitleText;

    @BindView(R.id.viewCommonTitleDivider)
    View viewCommonTitleDivider;

    public CommonTitleView(Context context) {
        super(context);
    }

    public CommonTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int layoutResourceId() {
        return R.layout.common_title_view;
    }

    @Override
    void initViews() {

    }

    /**
     * 하단 라인 노출 여부
     * @param isShow
     */
    public void isShowBottomLine(boolean isShow){
        if(viewCommonTitleDivider == null){
            return;
        }

        if(isShow){
            viewCommonTitleDivider.setVisibility(View.VISIBLE);
        }else{
            viewCommonTitleDivider.setVisibility(View.GONE);
        }
    }

    /**
     * 배경화면 투명색으로 처리
     */
    public void setBackgroundTransparent() {
        if(rlCommonTitleViewCotainer != null){
            rlCommonTitleViewCotainer.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        }
        if(tvTitleText != null){
            tvTitleText.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        }
        if(viewCommonTitleDivider != null){
            viewCommonTitleDivider.setVisibility(GONE);
        }
    }

    /**
     * 닫기 버튼으로 변경
     */
    public void setClosedButtonType() {
        ImageView lbBackIc = findViewById(R.id.lbBackIc);
        lbBackIc.setBackgroundResource(R.drawable.black_close);
    }

    /**
     * @param listener 뒤로가기 리스너
     */
    public void setBackButtonClickListener(OnClickListener listener) {
        if (rlBack != null && listener != null) {
            rlBack.setOnClickListener(listener);
        }
    }

    public void setTitleName(String titleName) {
        if (tvTitleText != null && titleName != null) {
            tvTitleText.setText(titleName);
        }
    }

    /**
     * 타이틀 세팅
     * @param titleName
     * string.xml 에 정의된 타이틀
     */
    public void setTitleName(@StringRes int titleName) {
        setTitleName(getContext().getString(titleName));
    }

    /**
     * 뒤로가기 버튼 숨김 처리
     */
    public void onHideBackButton() {
        rlBack.setVisibility(GONE);
    }
}

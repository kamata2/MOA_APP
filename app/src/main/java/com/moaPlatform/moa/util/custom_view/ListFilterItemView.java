package com.moaPlatform.moa.util.custom_view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.ObjectUtil;

import androidx.core.content.ContextCompat;
import butterknife.BindView;

/**
 * 다이얼로그 필터 item view
 */
public class ListFilterItemView extends BaseLinearLayoutView {

    @BindView(R.id.ivFilterSelected)
    ImageView ivFilterSelected;

    @BindView(R.id.tvFilterText)
    TextView tvFilterText;

    private boolean isSelected;

    public ListFilterItemView(Context context) {
        super(context);
    }

    public ListFilterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListFilterItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int layoutResourceId() {
        return R.layout.item_filter_view;
    }

    @Override
    void initViews() {
        ivFilterSelected.setVisibility(View.GONE);
        tvFilterText.setTextColor(ContextCompat.getColor(context, R.color.color_343434));
    }

    public void setText(String text){
        if (tvFilterText != null) {
            if (ObjectUtil.checkNotNull(text)) {
                tvFilterText.setText(text);
            }
        }
    }

    public void setSelected(boolean isSelected){
        this.isSelected = isSelected;

        if(ivFilterSelected == null | tvFilterText == null){
            return;
        }
        if(isSelected){
            ivFilterSelected.setVisibility(View.VISIBLE);
            tvFilterText.setTextColor(ContextCompat.getColor(context, R.color.coral));
            tvFilterText.setTypeface(Typeface.DEFAULT_BOLD);
        }else{
            ivFilterSelected.setVisibility(View.GONE);
            tvFilterText.setTextColor(ContextCompat.getColor(context, R.color.color_343434));
            tvFilterText.setTypeface(Typeface.DEFAULT);
        }
    }

    public String getTvFilterText() {
        return tvFilterText.getText().toString();
    }
}

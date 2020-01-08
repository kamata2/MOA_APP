package com.moaPlatform.moa.side_menu.customercenter.faq.view;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.customercenter.faq.model.FaqDetailItems;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FaqDetailDataRecyclerViewAdapter extends RecyclerView.Adapter<FaqDetailDataHolder> {
    private List<FaqDetailItems> mFaqDetailItems;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    private TextView mTv;
    private ImageView mArrow, mArrowDown;

    @NonNull
    @Override
    public FaqDetailDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.side_customercenter_ask_row, parent, false);
        return new FaqDetailDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqDetailDataHolder holder, int position) {
        final FaqDetailItems FAQDetailItems = mFaqDetailItems.get(position);

        holder.tTitleText.setText(FAQDetailItems.dtlQstnTitle);
        holder.tDetailText.setText(FAQDetailItems.dtlQstnAnsw);

        mTv = holder.tDetailText;
        mArrow = holder.arrow;
        mArrowDown = holder.arrowDown;
        changeVisibility(selectedItems.get(position));

        holder.rParentRelative.setOnClickListener(v -> {
            if (selectedItems.get(position)) {
                selectedItems.delete(position);
            } else {
                selectedItems.delete(prePosition);
                selectedItems.put(position, true);
            }
            if (prePosition != -1) notifyItemChanged(prePosition);
            notifyItemChanged(position);
            prePosition = position;
        });
    }

    @Override
    public int getItemCount() {
        return mFaqDetailItems == null ? 0 : mFaqDetailItems.size();
    }

    public void setData(List<FaqDetailItems> mFaqDetailItems) {
        this.mFaqDetailItems = mFaqDetailItems;
        selectedItems.clear();
        prePosition = -1;
    }

    private void changeVisibility(final boolean isExpanded) {
        mTv.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        mArrow.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        mArrowDown.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
    }

}

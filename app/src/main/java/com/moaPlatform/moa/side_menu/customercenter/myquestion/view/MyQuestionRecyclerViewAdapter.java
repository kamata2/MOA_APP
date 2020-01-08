package com.moaPlatform.moa.side_menu.customercenter.myquestion.view;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.customercenter.faq.view.FaqDataHolder;
import com.moaPlatform.moa.side_menu.customercenter.myquestion.model.MyQuestionItems;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyQuestionRecyclerViewAdapter extends RecyclerView.Adapter<FaqDataHolder> {
    private List<MyQuestionItems> mMyQuestionItems;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    private TextView mTv;
    private ImageView mArrow, mArrowDown;

    @NonNull
    @Override
    public FaqDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.side_customercenter_my_row, parent, false);
        return new FaqDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqDataHolder holder, int position) {
        final MyQuestionItems myQuestionItems = mMyQuestionItems.get(position);
        String str = myQuestionItems.answStat;
        holder.tAnswerType.setVisibility(View.GONE);
        holder.tAnswerTypeOther.setVisibility(View.GONE);

        if (str.contains("대기")) {
            holder.tAnswerType.setText(myQuestionItems.answStat);
            holder.tAnswerType.setVisibility(View.VISIBLE);
        } else {
            holder.tAnswerTypeOther.setText(myQuestionItems.answStat);
            holder.tAnswerTypeOther.setVisibility(View.VISIBLE);
        }

        holder.tDateTime.setText(myQuestionItems.inqryDt);
        holder.getAnswerType2.setText("[" + myQuestionItems.inqryTp + "]");
        holder.tTitle.setText(myQuestionItems.inqryTitle);
        if (myQuestionItems.answDt.equals("")) {
            holder.tDetailContent.setText("답변 대기 중입니다.");
        } else {
            holder.tDetailContent.setText(myQuestionItems.answDt);
        }
        mTv = holder.tDetailContent;
        mArrow = holder.mArrow;
        mArrowDown = holder.mArrowDown;
        changeVisibility(selectedItems.get(position));

        holder.rMainRelativeLayout.setOnClickListener(v -> {
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
        return mMyQuestionItems == null ? 0 : mMyQuestionItems.size();
    }

    public void setData(List<MyQuestionItems> mMyQuestionItems) {
        this.mMyQuestionItems = mMyQuestionItems;
    }

    private void changeVisibility(final boolean isExpanded) {
        mTv.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        mArrow.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        mArrowDown.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
    }
}

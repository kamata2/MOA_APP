package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.model.response.PointSaveUseModel;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.models.MoneyConverter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletPointHisrotyAdapter extends RecyclerView.Adapter<WalletPointHisrotyAdapter.WalletPointHistoryViewHolder> implements MoneyConverter {

    private List<PointSaveUseModel> list = new ArrayList<>();
    private ListItemClickListener listItemClickListener;

    public WalletPointHisrotyAdapter(ListItemClickListener listener) {
        this.listItemClickListener = listener;
    }

    @NonNull
    @Override
    public WalletPointHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_point_history, parent, false);
        return new WalletPointHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletPointHistoryViewHolder holder, int position) {
        PointSaveUseModel pointSaveUseItem = list.get(position);
        holder.init(pointSaveUseItem, position);
        holder.itemView.setOnClickListener((View view) -> {
            if (listItemClickListener != null) {
                listItemClickListener.clickItem(0, position, pointSaveUseItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class WalletPointHistoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvWalletPointHistoryDate)    public TextView tvWalletPointHistoryDate;
        @BindView(R.id.tvWalletPointHistoryDesc)    public TextView tvWalletPointHistoryDesc;
        @BindView(R.id.tvWalletPointHistoryName)    public TextView tvWalletPointHistoryName;
        @BindView(R.id.tvWalletPointHistoryPoint)   public TextView tvWalletPointHistoryPoint;

        private ConstraintLayout.LayoutParams layoutParams;

        WalletPointHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void init(PointSaveUseModel model, int position) {
            tvWalletPointHistoryDate.setText(model.saveUseDt);
            tvWalletPointHistoryDesc.setText(model.saveUseSepaCntnt);
            tvWalletPointHistoryName.setText(model.pointSaveUseCntnt);
            tvWalletPointHistoryPoint.setText(commaUnitChange(model.occrAmt) + "p");
        }
    }

    public void setList(List<PointSaveUseModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}

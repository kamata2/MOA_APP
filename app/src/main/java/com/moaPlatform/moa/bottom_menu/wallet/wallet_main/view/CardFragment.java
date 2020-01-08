package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.payment.CardListItem;
import com.moaPlatform.moa.util.DeviceUtil;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CardFragment extends Fragment {

    private RelativeLayout topLayout, bottomLayout, rlCard;
    private LinearLayout llDelCard;
    private CheckBox ckCard;
    private final double aspectCard = 1.652;        //카드 이미지 가로세로 비율

    private CardListItem cardListItem;
    private int position;

    private CardDeleteFragmentListener cardDeleteFragmentListener;
    private ListItemClickListener listItemClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Logger.d("CardFragment onCreateView position >>> " + position);

        View view = inflater.inflate(R.layout.card_slider, container, false);

        topLayout = view.findViewById(R.id.cardslidertop);
        bottomLayout = view.findViewById(R.id.cardsliderbottom);
        rlCard = view.findViewById(R.id.rlCardSilderContainer);

        int cardWidth = (int) (DeviceUtil.getScreenWidth(getContext()) - (getResources().getDimension(R.dimen.item_card_slider_margin_horizontal) * 2));
        float cardHeight = (float) (cardWidth / aspectCard);
        rlCard.getLayoutParams().width = cardWidth;
        rlCard.getLayoutParams().height = (int) cardHeight;

        topLayout.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);

        if (!ObjectUtil.checkNotNull(cardListItem.getCARDTOKEN())) {
            rlCard.setBackgroundResource(R.drawable.cardbg_empty);
            bottomLayout.setVisibility(View.VISIBLE);

            rlCard.setOnClickListener((View v) -> {
                if(listItemClickListener != null){
                    listItemClickListener.clickItem(0, position, null);
                }
            });

        } else {
//            listItemClickListener = null;
            topLayout.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.GONE);

            llDelCard = view.findViewById(R.id.cardelete);
            ckCard = view.findViewById(R.id.cardcheck);
            rlCard.setBackgroundResource(R.drawable.cardbg);
            ckCard.setChecked(true);
            TextView cardNum = view.findViewById(R.id.cardnumbers);

//            CardListItem cardListItem = cardList.get(position);
//            String cardNick = cardListItem.getCARDNICK();
//            String cardToken = cardListItem.getCARDTOKEN();

            if(cardListItem != null){
                if(ObjectUtil.checkNotNull(cardListItem.getCARDNICK())){
                    cardNum.setText(cardListItem.getCARDNICK());
                }
            }
            llDelCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //serverSideMoaPay.deleteOneCard(cardToken);
                    if(cardDeleteFragmentListener != null){
                        cardDeleteFragmentListener.callBack(position);
                    }
                }
            });
        }

        return view;
    }

    public void setDefaultData(CardListItem item, int position){
        this.cardListItem = item;
        this.position = position;
    }

    public interface CardDeleteFragmentListener{
        void callBack(int position);
    }

    public void setCardDeleteFragmentListener(CardDeleteFragmentListener cardDeleteFragmentListener) {
        this.cardDeleteFragmentListener = cardDeleteFragmentListener;
    }
    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }
}

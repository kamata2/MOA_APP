package com.moaPlatform.moa.bottom_menu.wallet.wallet_main.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.moaPlatform.moa.bottom_menu.wallet.wallet_main.view.CardFragment;
import com.moaPlatform.moa.payment.CardListItem;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends FragmentStatePagerAdapter {

    private ListItemClickListener listItemClickListener;
    private CardDeleteAdapterListener cardDeleteAdapterListener;

    private List<CardListItem> cardDataList = new ArrayList<>();

    public CardPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        if (cardDataList == null ) {
            return 0;
        } else {
            return cardDataList.size();
        }
    }

    @Override
    public Fragment getItem(int position) {

        CardFragment cardFragment = new CardFragment();
        cardFragment.setDefaultData(cardDataList.get(position), position);
        cardFragment.setCardDeleteFragmentListener(deletePosition -> {
            if(cardDeleteAdapterListener != null){
                cardDeleteAdapterListener.onCardDelete(cardDataList.get(deletePosition).getCARDTOKEN(), deletePosition);
            }
        });

        cardFragment.setListItemClickListener((codeType, clickPosition, object) -> {
            if(listItemClickListener != null){
                listItemClickListener.clickItem(codeType, clickPosition, object);
            }
        });
        return cardFragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void removePage(int position) {
        if (cardDataList != null && cardDataList.get(position) != null) {
            cardDataList.remove(position);
            notifyDataSetChanged();
        }
    }

    public void setCardDataList(List<CardListItem> cardDataList) {
        this.cardDataList = cardDataList;
        notifyDataSetChanged();
    }

    public void clearCardList(){
        if(cardDataList != null){
            cardDataList.clear();
            notifyDataSetChanged();
        }
    }

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void setCardDeleteAdapterListener(CardDeleteAdapterListener cardDeleteAdapterListener) {
        this.cardDeleteAdapterListener = cardDeleteAdapterListener;
    }

    public interface CardDeleteAdapterListener{
        void onCardDelete(String cardToken, int position);
    }

    public List<CardListItem> getCardDataList() {
        return cardDataList;
    }
}

package com.moaPlatform.moa.side_menu.favorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.favorite.model.SearchedStoreItems;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder> {

    public final int FAVOR_ADAPTER_HEADER_CNT = 1;      //adapter의 포지션과 리스트 데이터의 포지션을 계산하기 위함
    private List<SearchedStoreItems> searchedStoreItems;
    private ListItemClickListener listItemClickListener = null;

    public enum BookmarkManager {
        // 가맹점 삭제, 가맹점으로 이동
        STORE_REMOVE, SOTRE_MOVE
    }

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark_store, parent, false);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
        holder.init(position - 1);

        holder.rlBookmarkStoreDeleteGroup.setOnClickListener(v -> {
            if (listItemClickListener != null)
                listItemClickListener.clickItem(BookmarkManager.STORE_REMOVE
                        , position - FAVOR_ADAPTER_HEADER_CNT
                        , searchedStoreItems.get(position - FAVOR_ADAPTER_HEADER_CNT));
        });

        if (position - FAVOR_ADAPTER_HEADER_CNT >= 0) {
            holder.itemView.setOnClickListener(v -> {
                if (listItemClickListener != null)
                    listItemClickListener.clickItem(BookmarkManager.SOTRE_MOVE
                            , position - FAVOR_ADAPTER_HEADER_CNT
                            , searchedStoreItems.get(position - FAVOR_ADAPTER_HEADER_CNT));
            });
        }

    }

    @Override
    public int getItemCount() {
        return searchedStoreItems == null ? FAVOR_ADAPTER_HEADER_CNT : searchedStoreItems.size() + FAVOR_ADAPTER_HEADER_CNT;
    }


    public void setData(List<SearchedStoreItems> searchedStoreItems) {
        this.searchedStoreItems = searchedStoreItems;
        notifyDataSetChanged();
    }


    // 홀더
    class FavoriteHolder extends RecyclerView.ViewHolder {

        // 헤더 뷰
        View clBookmarkStoreRegisteredStoreListGroup;
        // 내용 뷰
        View clItemBookmarkStoreContent;
        // 가맹점 썸네일
        ImageView ivBookmarkStoreThumbnail;
        // 가맹점 이름
        TextView tvBookmarkStoreName;
        // 가맹점 평점
        TextView tvBookmarkStoreGPA;
        // 리뷰
        TextView tvBookmarkStoreReview;
        // 즐겨찾기
        TextView tvBookmarkStoreBookmark;
        // 즐겨찾기 삭제 뷰
        View rlBookmarkStoreDeleteGroup;

        FavoriteHolder(@NonNull View itemView) {
            super(itemView);
            clBookmarkStoreRegisteredStoreListGroup = itemView.findViewById(R.id.clBookmarkStoreRegisteredStoreListGroup);
            clItemBookmarkStoreContent = itemView.findViewById(R.id.clItemBookmarkStoreContent);
            ivBookmarkStoreThumbnail = itemView.findViewById(R.id.ivBookmarkStoreThumbnail);
            tvBookmarkStoreName = itemView.findViewById(R.id.tvBookmarkStoreName);
            tvBookmarkStoreGPA = itemView.findViewById(R.id.tvBookmarkStoreGPA);
            tvBookmarkStoreReview = itemView.findViewById(R.id.tvBookmarkStoreReview);
            tvBookmarkStoreBookmark = itemView.findViewById(R.id.tvBookmarkStoreBookmark);
            rlBookmarkStoreDeleteGroup = itemView.findViewById(R.id.rlBookmarkStoreDeleteGroup);
        }

        public void init(int position) {
            if (position >= 0) {
                // 가맹점 정보 모델
                SearchedStoreItems bookmarkStoreInfoModel = searchedStoreItems.get(position);

                clBookmarkStoreRegisteredStoreListGroup.setVisibility(View.GONE);
                clItemBookmarkStoreContent.setVisibility(View.VISIBLE);
                // 썸네일 세팅
                glideImage(ivBookmarkStoreThumbnail, bookmarkStoreInfoModel.imageUrl);
                // 가맹점 이름 세팅
                tvBookmarkStoreName.setText(bookmarkStoreInfoModel.storNm);
                // 가맹점 평점
                String storeGPA = String.valueOf(bookmarkStoreInfoModel.evalScor);
                tvBookmarkStoreGPA.setText(storeGPA);
                // 가맹점 리뷰
                // 리뷰 개수에 콤마 찍음
                String reviewCountCommaConvert = StringUtil.convertCommaPrice(bookmarkStoreInfoModel.storRevwCnt);
                String reviewString = itemView.getResources().getString(R.string.bookmarkstoreholder_store_review, reviewCountCommaConvert);
                // 리뷰 표시
                tvBookmarkStoreReview.setText(reviewString);
                // 즐겨찾기 개수에 콤마 찍음
                String bookmarkCountCommaConvert = StringUtil.convertCommaPrice(bookmarkStoreInfoModel.bmarkCnt);
                String bookmarkString = itemView.getResources().getString(R.string.bookmarkstoreholder_store_bookmark, bookmarkCountCommaConvert);
                tvBookmarkStoreBookmark.setText(bookmarkString);


            } else {
                clBookmarkStoreRegisteredStoreListGroup.setVisibility(View.VISIBLE);
                clItemBookmarkStoreContent.setVisibility(View.GONE);
            }
        }

        private void glideImage(ImageView imageview, String subUrl) {

            Logger.d("image path >>>> " + BuildConfig.FILE_SERVER_BASE_URL + subUrl);

            Glide.with(itemView.getContext())
                    .asBitmap()
                    .load(BuildConfig.FILE_SERVER_BASE_URL + subUrl)
                    .into(imageview);
        }
    }

    public List<SearchedStoreItems> getList() {
        return searchedStoreItems;
    }
}

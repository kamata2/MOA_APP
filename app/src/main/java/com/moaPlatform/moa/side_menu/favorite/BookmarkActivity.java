package com.moaPlatform.moa.side_menu.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.EatOutStoreDetailActivity;
import com.moaPlatform.moa.delivery_menu.store_detail.StoreDetailActivity;
import com.moaPlatform.moa.model.data_model.StoreInfoChangedModel;
import com.moaPlatform.moa.side_menu.favorite.model.ResFavorite;
import com.moaPlatform.moa.side_menu.favorite.model.SearchedStoreItems;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.BaseModel;

import java.util.List;

import javax.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 사이드 메뉴의 즐겨찾기 화면
 */
public class BookmarkActivity extends AppCompatActivity implements RetrofitConnectionResult, ListItemClickListener {

    // 즐겨찾기 리스트 어뎁터
    FavoriteAdapter favoriteRecyclerAdapter;
    private BookmarkController bookmarkController;
    private LinearLayout llBookmarkEmpty;
    private List<SearchedStoreItems> storeItemsList;
    // 즐겨찾기 삭제시 보여줄 다이얼로그
    private YesOrNoDialog bookmarkRemoveDialog = null;

    private int clickPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_main);
        init();
        bookmarkSetting();
        getBookmarkList();
    }

    /**
     * 초기화
     */
    private void init() {
        // 상탄 툴바 세팅
        CommonTitleView viewFavoriteMainToolbar = findViewById(R.id.viewBookmarkMainToolbar);
        viewFavoriteMainToolbar.setBackButtonClickListener(v -> finish());
        viewFavoriteMainToolbar.setTitleName(getString(R.string.bookmarkactivity_top_toolbar_title));

        llBookmarkEmpty = findViewById(R.id.llBookmarkEmpty);
        llBookmarkEmpty.setVisibility(View.GONE);
    }

    /**
     * 즐겨찾기 세팅
     */
    private void bookmarkSetting() {
        favoriteRecyclerAdapter = new FavoriteAdapter();
        favoriteRecyclerAdapter.setListItemClickListener(this);
        // 즐겨찾기 RecyclerView 세팅
        RecyclerView rvBookmark = findViewById(R.id.rvFavoriteMainBookmark);
        rvBookmark.setLayoutManager(new LinearLayoutManager(this));
        rvBookmark.setAdapter(favoriteRecyclerAdapter);

    }

    private void getBookmarkList() {

        bookmarkController = new BookmarkController();
        bookmarkController.setContext(this);
        bookmarkController.setRetrofitConnectionResult(this);
        bookmarkController.getBookmarkList();
    }

    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {
        if (type == bookmarkController.SERVER_CALL_BOOKMARK_LIST) {
            listUpdate(((ResFavorite) baseModel).storeItemsList);
        } else if (type == bookmarkController.SERVER_CALL_BOOKMARK_STORE_REMOVE) {
            storeItemsList.remove(clickPosition);
            listUpdate(storeItemsList);
            bookmarkRemoveDialog.dismiss();
        }
    }

    @Override
    public void onRetrofitFail(int type, String msg) {
        if (type == bookmarkController.SERVER_CALL_BOOKMARK_LIST) {
            llBookmarkEmpty.setVisibility(View.VISIBLE);
        } else {
            clickPosition = -1;
        }
    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {
        Logger.d("clickItem position >>>> " + position);
        clickPosition = position;
        switch ((FavoriteAdapter.BookmarkManager) codeType) {
            case STORE_REMOVE:
                bookmarkRemoveDialogShow();
                break;
            case SOTRE_MOVE:
                Intent intent;
                if (storeItemsList.get(position).serviceSepa.equals("delivery")) {
                    intent = new Intent(getApplicationContext(), StoreDetailActivity.class);
                    intent.putExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), storeItemsList.get(position).storId);
                    intent.putExtra(MoaConstants.EXTRA_STORE_NAME, storeItemsList.get(position).storNm);
                } else {
                    intent = new Intent(getApplicationContext(), EatOutStoreDetailActivity.class);
                    intent.putExtra(MoaConstants.EXTRA_STORE_ID, storeItemsList.get(position).storId);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MoaConstants.EXTRA_STORE_LIST_POSITION, position);
                startActivityForResult(intent, MoaConstants.REQUEST_STORE_DETAIL);
                clickPosition = -1;
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("requestCode " + requestCode + " resultCode " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MoaConstants.REQUEST_STORE_DETAIL){

            if(resultCode == MoaConstants.RESULT_STORE_DETAIL_INFO_CHANGED){
                if(data != null){
                    try{
                        StoreInfoChangedModel storeInfoChangedModel = new Gson().fromJson(
                                data.getStringExtra(MoaConstants.EXTRA_STORE_INFO_CHANGED_DATA), StoreInfoChangedModel.class);

                        Logger.d("storeInfoChangedModel >>> " + storeInfoChangedModel.toString());
                        SearchedStoreItems tempModel = favoriteRecyclerAdapter.getList().get(storeInfoChangedModel.position);
                        if(tempModel.bmarkCnt != Integer.valueOf(storeInfoChangedModel.getFovoriteCount())){
                            //즐겨찾기 cnt가 다르면 취소한것으로 간주하고 리스트에서 삭제처리한다.
                            favoriteRecyclerAdapter.getList().remove(storeInfoChangedModel.position);
                            favoriteRecyclerAdapter.notifyDataSetChanged();
                            if (favoriteRecyclerAdapter.getList().size() == 0) {
                                llBookmarkEmpty.setVisibility(View.VISIBLE);
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void listUpdate(List<SearchedStoreItems> list) {
        storeItemsList = list;
        if (storeItemsList == null || storeItemsList.size() == 0) {
            llBookmarkEmpty.setVisibility(View.VISIBLE);
        }
        favoriteRecyclerAdapter.setData(storeItemsList);
    }

    private void bookmarkRemoveDialogShow() {
        if (bookmarkRemoveDialog == null)
            bookmarkRemoveDialog = new YesOrNoDialog();
        bookmarkRemoveDialog.dialogContent("즐겨찾기 매장을 삭제하시겠습니까?");
        bookmarkRemoveDialog.show(getSupportFragmentManager(), "bookmarkRemoveDialog");
        bookmarkRemoveDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
            @Override
            public void onDialogNo() {
                bookmarkRemoveDialog.dismiss();
            }

            @Override
            public void onDialogYes() {
                bookmarkController.removeBookmarkStore(storeItemsList.get(clickPosition).storId);
            }
        });
    }

}
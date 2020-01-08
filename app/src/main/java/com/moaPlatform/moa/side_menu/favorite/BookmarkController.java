package com.moaPlatform.moa.side_menu.favorite;

import com.moaPlatform.moa.side_menu.favorite.model.ReqBookmarkStoreRemove;
import com.moaPlatform.moa.side_menu.favorite.model.ResFavorite;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.StoreInfoModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jiwun on 2019-07-12.
 * 즐겨찾기 컨트롤러
 */
class BookmarkController extends BaseController {

    // 즐겨찾기 목록 불러오기
    public final int SERVER_CALL_BOOKMARK_LIST = 66001;
    public final int SERVER_CALL_BOOKMARK_STORE_REMOVE = 66002;

    /**
     * 즐겨찾기 목록 불러오기
     */
    void getBookmarkList() {

        RetrofitClient.getInstance().getMoaService().favoriteSelect(new CommonModel()).enqueue(new Callback<ResFavorite>() {
            @Override
            public void onResponse(@NonNull Call<ResFavorite> call, @NonNull Response<ResFavorite> response) {

                switch (resModelCheck("getBookmarkLisst", response.body())) {
                    case MODEL_NULL:
                        onRetrofitFail(SERVER_CALL_BOOKMARK_LIST, "");
                        break;
                    case RESPONSE_DATA_FAIL:
                        onRetrofitFail(SERVER_CALL_BOOKMARK_LIST, "");
                        break;
                    case MODEL_ACCESS_TOKEN_REFRESH:
                        getBookmarkList();
                        break;
                    case RESPONSE_DATA_SUCCESS:
                        onRetrofitSuccess(SERVER_CALL_BOOKMARK_LIST, response.body());
                        break;
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResFavorite> call, @NonNull Throwable t) {
                onRetrofitFail(SERVER_CALL_BOOKMARK_LIST, "");

            }
        });

    }

    /**
     * 즐겨찾기 삭제
     * 삭제할 가맹점 아이디
     */
    void removeBookmarkStore(int storeId) {
        ReqBookmarkStoreRemove reqBookmarkStoreRemove = new ReqBookmarkStoreRemove();
        reqBookmarkStoreRemove.setStoreId(storeId);
        RetrofitClient.getInstance().getMoaService().removeBookmark(reqBookmarkStoreRemove).enqueue(new Callback<StoreInfoModel>() {
            @Override
            public void onResponse(@NonNull Call<StoreInfoModel> call, @NonNull Response<StoreInfoModel> response) {
                switch (resModelCheck("removeBookmarkStore", response.body())) {
                    case MODEL_ACCESS_TOKEN_REFRESH:
                        removeBookmarkStore(storeId);
                        break;
                    case RESPONSE_DATA_SUCCESS:
                        onRetrofitSuccess(SERVER_CALL_BOOKMARK_STORE_REMOVE, response.body());
                        break;
                    case RESPONSE_DATA_FAIL:
                        serverErrorMsgShow();
                        onRetrofitFail(SERVER_CALL_BOOKMARK_STORE_REMOVE, "");
                        break;
                    case MODEL_NULL:
                        serverErrorMsgShow();
                        onRetrofitFail(SERVER_CALL_BOOKMARK_STORE_REMOVE, "");
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoreInfoModel> call, @NonNull Throwable t) {
                onRetrofitFail(SERVER_CALL_BOOKMARK_STORE_REMOVE, "");
                serverErrorMsgShow();
            }
        });

    }

}

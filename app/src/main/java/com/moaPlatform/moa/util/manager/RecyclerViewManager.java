package com.moaPlatform.moa.util.manager;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 리사이클러뷰 매니져
 */
public class RecyclerViewManager {

    /**
     * 리사이클러뷰 기본 세팅후 반환
     * @param recyclerView
     * 연결할 리사이클러뷰
     * @param adapter
     * 어댑터
     */
    public void recyclerViewInit(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(defaultLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL));
    }

    /**
     * 리사이클러뷰 기본 세팅후 반환
     * @param recyclerView
     * 연결할 리사이클러뷰
     * @param adapter
     * 어댑터
     * @param orientation
     * 정렬 방향
     */
    public void recyclerViewInit(RecyclerView recyclerView, RecyclerView.Adapter adapter, int orientation) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(defaultLayoutManager(recyclerView.getContext(), orientation));
    }

    /**
     * 리사이클러뷰 기본 세팅후
     * @param recyclerView
     * 세팅할 리사이클러뷰
     * @param adapter
     * 연결할 어댑터
     * @param orientation
     * 정렬 방향
     * @param scrollingEnabled
     * 스크롤 방지 유무
     */
    public void recyclerViewInit(RecyclerView recyclerView, RecyclerView.Adapter adapter, int orientation, boolean scrollingEnabled) {
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(defaultLayoutManager(recyclerView.getContext(), orientation));
        recyclerView.setNestedScrollingEnabled(scrollingEnabled);
    }

    /**
     * 리사이클러뷰에 적용할 레이아웃 세팅
     * @param context
     * context
     * @param orientation
     * 정렬 방향
     * @return
     * 레이아웃 세팅후 반환
     */
    private LinearLayoutManager defaultLayoutManager(Context context, int orientation) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(orientation);
        return linearLayoutManager;
    }

}

package com.moaPlatform.moa.util.interfaces;

/**
 * 리스트에서 아이템 클릭 시 사용
 */
public interface ListItemClickListener<T> {
    void clickItem(T codeType, int position, Object object);
}

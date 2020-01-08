package com.moaPlatform.moa.top_menu.location.model;

/**
 * 주소 검색시 사용할 req 모델
 */
public class ReqAddressSearchModel {
    // 승인키
    public String key = "U01TX0FVVEgyMDE4MTEwNjEwMzM1MTEwODI4MDk=";
    // 현재 페이지 번호
    public int currentPage = 1;
    // 페이지당 출력할 결과 row 수
    public int countPerPage = 100;
    // 주소 검색어
    public String keyword = "";
    // 검색 결과 형식
    public String resultType = "json";
}

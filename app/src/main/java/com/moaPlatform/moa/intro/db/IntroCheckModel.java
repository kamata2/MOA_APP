package com.moaPlatform.moa.intro.db;

import io.realm.RealmObject;

/**
 * 인트로 화면에서 type 값에 따라 표시할 화면이 다름
 * 0 : 다이얼로그 허용
 * 1 : 이용약관 동의 완료
 * 2 : 위치 저장 완료
 */
public class IntroCheckModel extends RealmObject {
    public int type = 0;
}

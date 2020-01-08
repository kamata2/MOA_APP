package com.moaPlatform.moa.util.interfaces;

import com.moaPlatform.moa.util.manager.CodeTypeManager;

/**
 * Created by jiwun on 2019-05-21.
 * 클래스끼리 통신할때 사용할 인터페이스
 */
public interface ClassConnectInterface {
    void onActType(CodeTypeManager.ClassCodeManager type);
}

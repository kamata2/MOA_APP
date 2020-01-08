package com.moaPlatform.moa.util.db;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmController {

    /**
     *  realm 의 설정
     *  버전, realm의 파일 이름 등
     * */
    public RealmConfiguration realmConfig() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("moa.realm")
                .schemaVersion(1)
                .migration(new MoaRealmMigration())
                .build();
        return config;
    }

    public Realm realmInstance() {
        return Realm.getInstance(realmConfig());
    }
}

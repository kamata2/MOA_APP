package com.moaPlatform.moa.util.db;

import androidx.annotation.Nullable;
import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * realm 사용시 새로운 모델을 추가하거나
 * 필드를 추가해서 버전이 바뀌었을경우
 * 처리해주는 부분
 * */
public class MoaRealmMigration implements RealmMigration {

    //Caused by: java.lang.IllegalArgumentException: Configurations cannot be different if used to open the same file.
    // The most likely cause is that equals() and hashCode() are not overridden in the migration class: com.moaPlatform.moa.util.db.MoaRealmMigration

    @Override
    public int hashCode() {
        return RealmMigration.class.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj != null && obj instanceof RealmMigration; // obj instance of your Migration class name, here My class is Migration.
    }

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

//        if (oldVersion == 2) {
//            schema.create("RealmTest4")
//                    .addField("fff",String.class);
//            oldVersion++;
//        }
    }
}

package io.realm;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.ImportFlag;
import io.realm.ProxyUtils;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsList;
import io.realm.internal.OsObject;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.Property;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.android.JsonUtils;
import io.realm.internal.objectstore.OsObjectBuilder;
import io.realm.log.RealmLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("all")
public class com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxy extends com.moaPlatform.moa.intro.db.IntroCheckModel
    implements RealmObjectProxy, com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface {

    static final class IntroCheckModelColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long typeIndex;

        IntroCheckModelColumnInfo(OsSchemaInfo schemaInfo) {
            super(1);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("IntroCheckModel");
            this.typeIndex = addColumnDetails("type", "type", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        IntroCheckModelColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new IntroCheckModelColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final IntroCheckModelColumnInfo src = (IntroCheckModelColumnInfo) rawSrc;
            final IntroCheckModelColumnInfo dst = (IntroCheckModelColumnInfo) rawDst;
            dst.typeIndex = src.typeIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private IntroCheckModelColumnInfo columnInfo;
    private ProxyState<com.moaPlatform.moa.intro.db.IntroCheckModel> proxyState;

    com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (IntroCheckModelColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.moaPlatform.moa.intro.db.IntroCheckModel>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public int realmGet$type() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.typeIndex);
    }

    @Override
    public void realmSet$type(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.typeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.typeIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("IntroCheckModel", 1, 0);
        builder.addPersistedProperty("type", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static IntroCheckModelColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new IntroCheckModelColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "IntroCheckModel";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "IntroCheckModel";
    }

    @SuppressWarnings("cast")
    public static com.moaPlatform.moa.intro.db.IntroCheckModel createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.moaPlatform.moa.intro.db.IntroCheckModel obj = realm.createObjectInternal(com.moaPlatform.moa.intro.db.IntroCheckModel.class, true, excludeFields);

        final com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface objProxy = (com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface) obj;
        if (json.has("type")) {
            if (json.isNull("type")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'type' to null.");
            } else {
                objProxy.realmSet$type((int) json.getInt("type"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.moaPlatform.moa.intro.db.IntroCheckModel createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        final com.moaPlatform.moa.intro.db.IntroCheckModel obj = new com.moaPlatform.moa.intro.db.IntroCheckModel();
        final com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface objProxy = (com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("type")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$type((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'type' to null.");
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return realm.copyToRealm(obj);
    }

    private static com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating uexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.moaPlatform.moa.intro.db.IntroCheckModel.class), false, Collections.<String>emptyList());
        io.realm.com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxy obj = new io.realm.com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.moaPlatform.moa.intro.db.IntroCheckModel copyOrUpdate(Realm realm, IntroCheckModelColumnInfo columnInfo, com.moaPlatform.moa.intro.db.IntroCheckModel object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null) {
            final BaseRealm otherRealm = ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm();
            if (otherRealm.threadId != realm.threadId) {
                throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
            }
            if (otherRealm.getPath().equals(realm.getPath())) {
                return object;
            }
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (com.moaPlatform.moa.intro.db.IntroCheckModel) cachedRealmObject;
        }

        return copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.moaPlatform.moa.intro.db.IntroCheckModel copy(Realm realm, IntroCheckModelColumnInfo columnInfo, com.moaPlatform.moa.intro.db.IntroCheckModel newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.moaPlatform.moa.intro.db.IntroCheckModel) cachedRealmObject;
        }

        com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface realmObjectSource = (com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface) newObject;

        Table table = realm.getTable(com.moaPlatform.moa.intro.db.IntroCheckModel.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addInteger(columnInfo.typeIndex, realmObjectSource.realmGet$type());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.moaPlatform.moa.intro.db.IntroCheckModel object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.moaPlatform.moa.intro.db.IntroCheckModel.class);
        long tableNativePtr = table.getNativePtr();
        IntroCheckModelColumnInfo columnInfo = (IntroCheckModelColumnInfo) realm.getSchema().getColumnInfo(com.moaPlatform.moa.intro.db.IntroCheckModel.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        Table.nativeSetLong(tableNativePtr, columnInfo.typeIndex, rowIndex, ((com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface) object).realmGet$type(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.moaPlatform.moa.intro.db.IntroCheckModel.class);
        long tableNativePtr = table.getNativePtr();
        IntroCheckModelColumnInfo columnInfo = (IntroCheckModelColumnInfo) realm.getSchema().getColumnInfo(com.moaPlatform.moa.intro.db.IntroCheckModel.class);
        com.moaPlatform.moa.intro.db.IntroCheckModel object = null;
        while (objects.hasNext()) {
            object = (com.moaPlatform.moa.intro.db.IntroCheckModel) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            Table.nativeSetLong(tableNativePtr, columnInfo.typeIndex, rowIndex, ((com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface) object).realmGet$type(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, com.moaPlatform.moa.intro.db.IntroCheckModel object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.moaPlatform.moa.intro.db.IntroCheckModel.class);
        long tableNativePtr = table.getNativePtr();
        IntroCheckModelColumnInfo columnInfo = (IntroCheckModelColumnInfo) realm.getSchema().getColumnInfo(com.moaPlatform.moa.intro.db.IntroCheckModel.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        Table.nativeSetLong(tableNativePtr, columnInfo.typeIndex, rowIndex, ((com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface) object).realmGet$type(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.moaPlatform.moa.intro.db.IntroCheckModel.class);
        long tableNativePtr = table.getNativePtr();
        IntroCheckModelColumnInfo columnInfo = (IntroCheckModelColumnInfo) realm.getSchema().getColumnInfo(com.moaPlatform.moa.intro.db.IntroCheckModel.class);
        com.moaPlatform.moa.intro.db.IntroCheckModel object = null;
        while (objects.hasNext()) {
            object = (com.moaPlatform.moa.intro.db.IntroCheckModel) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            Table.nativeSetLong(tableNativePtr, columnInfo.typeIndex, rowIndex, ((com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface) object).realmGet$type(), false);
        }
    }

    public static com.moaPlatform.moa.intro.db.IntroCheckModel createDetachedCopy(com.moaPlatform.moa.intro.db.IntroCheckModel realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.moaPlatform.moa.intro.db.IntroCheckModel unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.moaPlatform.moa.intro.db.IntroCheckModel();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.moaPlatform.moa.intro.db.IntroCheckModel) cachedObject.object;
            }
            unmanagedObject = (com.moaPlatform.moa.intro.db.IntroCheckModel) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface unmanagedCopy = (com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface) unmanagedObject;
        com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface realmSource = (com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$type(realmSource.realmGet$type());

        return unmanagedObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("IntroCheckModel = proxy[");
        stringBuilder.append("{type:");
        stringBuilder.append(realmGet$type());
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState<?> realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long rowIndex = proxyState.getRow$realm().getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxy aIntroCheckModel = (com_moaPlatform_moa_intro_db_IntroCheckModelRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aIntroCheckModel.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aIntroCheckModel.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aIntroCheckModel.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}

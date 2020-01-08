package com.moaPlatform.moa.util.models;

public class KeyValue {

    private static final String TAG = KeyValue.class.getSimpleName();

    public String key;
    public String value;

    public KeyValue(String key, String value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "[" + TAG + "] key= " + key
                + ", value= " + value + "\n";
    }
}

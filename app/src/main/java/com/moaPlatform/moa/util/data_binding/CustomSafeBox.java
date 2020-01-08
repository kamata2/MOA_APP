package com.moaPlatform.moa.util.data_binding;

import androidx.databinding.InverseMethod;

public class CustomSafeBox {
    @InverseMethod("boxBoolean")
    public static boolean unBox(Boolean b) {
        return (b != null) && b.booleanValue();
    }

    public static Boolean boxBoolean(boolean b) {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }
}

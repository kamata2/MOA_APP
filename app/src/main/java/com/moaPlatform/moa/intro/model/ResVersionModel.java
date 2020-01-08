package com.moaPlatform.moa.intro.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ResVersionModel extends CommonModel {

    @SerializedName("version")
    public VersionModel versionModel;
}


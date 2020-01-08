package com.moaPlatform.moa.util.custom_view.temp.model;

public class MarginModel {

    public boolean isResponsiveMargin = false;
    public int leftMargin = 0;
    public int topMargin = 0;
    public int rightMargin = 0;
    public int bottomMargin = 0;

    public void setMargin(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
    }

}

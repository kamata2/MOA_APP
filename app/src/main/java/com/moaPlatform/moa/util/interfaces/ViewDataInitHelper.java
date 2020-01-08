package com.moaPlatform.moa.util.interfaces;

import android.content.Context;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.util.models.MoneyConverter;

public interface ViewDataInitHelper extends TextSettingHtmlFormat, MoneyConverter {

    default void textViewInit(TextView view, String inputData) {
        view.setText(inputData);
    }

    default void textViewInit(TextView view, int stringId, String inputData) {
        if(inputData != null){
            textViewInit(view, view.getContext().getString(stringId, inputData));
        }
    }

    default void textViewInit(View view, int layoutId, String inputData) {
        TextView textView = view.findViewById(layoutId);
        if(inputData != null){
            textView.setText(inputData);
        }
    }

    default void textViewInit(View view, int layoutId, Spanned inputData) {
        TextView textView = view.findViewById(layoutId);
        if(inputData != null){
            textView.setText(inputData);
        }
    }

    default void textViewInit(View view, int layoutId, int inputData) {
        textViewInit(view, layoutId, String.valueOf(inputData));
    }

    default void textViewInit(View view, int layoutId, float inputData) {
        textViewInit(view, layoutId, String.valueOf(inputData));
    }

    default void textViewInit(View view, int layoutId, int stringId, String inputData, Context context) {
        textViewInit(view, layoutId, context.getString(stringId, inputData));
    }

    default void textViewInitHtml(View view, int layoutId, int stringId, String inputData, Context context) {
        textViewInit(view, layoutId, getChangeHtmlFormat(context.getString(stringId, inputData)));
    }

    @Deprecated
    default void textViewInitConvertPrice(View view, int layoutId, int stringId, int price) {
        textViewInit(view, layoutId, commaUnitChange(price, stringId, view));
    }

    @Deprecated
    default void textViewInitConvertPrice(View view, int layoutId, int stringId, String price) {
        textViewInitConvertPrice(view, layoutId, stringId, Integer.valueOf(price));
    }

    default void imageViewInit(View view, int layoutId, String subUrl) {
        ImageView imageView = view.findViewById(layoutId);
        Glide.with(view)
                .load(BuildConfig.FILE_SERVER_BASE_URL + subUrl)
                .into(imageView);
    }

    default String getEditTextData(View view, int layoutId) {
        EditText editText = view.findViewById(layoutId);
        return editText.getText().toString();
    }

    default boolean textViewVisibility(View view, String checkData) {
        if (checkData != null) {
            view.setVisibility(View.VISIBLE);
            return true;
        } else {
            view.setVisibility(View.GONE);
            return false;
        }
    }

    default void textViewVisibility(View view, int layoutId, int stringId, String checkData) {
        TextView targetView = view.findViewById(layoutId);
        if (textViewVisibility(targetView, checkData))
            textViewInit(targetView, stringId, checkData);
    }

    default void textViewVisibility(View view, int layoutId, String checkData) {
        TextView targetView = view.findViewById(layoutId);
        if (textViewVisibility(targetView, checkData))
            textViewInit(targetView, checkData);
    }

    default void viewVisibility(View view, int layoutId, String checkData) {
        View targetView = view.findViewById(layoutId);
        targetView.setVisibility(checkData != null && checkData.equals("Y") ? View.VISIBLE : View.GONE);
    }

    default String getTextString(View view, int layoutId) {
        TextView textView = view.findViewById(layoutId);
        return textView.getText().toString();
    }

}

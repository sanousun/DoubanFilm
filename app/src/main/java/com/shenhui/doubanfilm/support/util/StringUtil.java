package com.shenhui.doubanfilm.support.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.util.List;

public class StringUtil {

    public static SpannableString getSpannableString(String str, int color) {
        SpannableString span = new SpannableString(str);
        span.setSpan(new ForegroundColorSpan(
                color), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    public static SpannableString getSpannableString1(String str, Object... whats) {
        SpannableString span = new SpannableString(str);
        for (Object what:whats) {
            span.setSpan(what, 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return span;
    }

    public static String getListString(List<String> list, char s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            str.append(i == 0 ? "" : s).append(list.get(i));
        }
        return str.toString();
    }
}

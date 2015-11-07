package com.shenhui.doubanfilm.support.util;

import com.shenhui.doubanfilm.bean.CelebrityEntity;

import java.util.List;

public class CelebrityUtil {

    public static String list2String(List<CelebrityEntity> entities, char s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < entities.size(); i++) {
            sb.append(i == 0 ? "" : s).append(entities.get(i).getName());
        }
        return sb.toString();
    }
}

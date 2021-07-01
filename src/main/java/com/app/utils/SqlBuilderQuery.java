package com.app.utils;

import java.util.Map;

public class SqlBuilderQuery {

        public static String getPerhitungan (Map<String, Object> params) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(params.get("query"));
            return buffer.toString();
        }
}

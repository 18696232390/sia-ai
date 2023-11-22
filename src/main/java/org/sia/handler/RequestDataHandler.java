package org.sia.handler;

import java.util.Map;

public class RequestDataHandler {

    private static final ThreadLocal<Map<String, String>> requestData = new ThreadLocal<>();

    public static void setPayload(Map<String,String> payloads) {
        requestData.set(payloads);
    }


    public static String getUserId() {
        Map<String, String> data = requestData.get();
        if (null == data) {
            return null;
        }
        return data.get("userId");
    }

    public static void remove() {
        requestData.remove();
    }

}

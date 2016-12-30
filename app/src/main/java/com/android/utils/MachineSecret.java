package com.android.utils;

public class MachineSecret {
    public final static String secret(String key) {
        String secret = "";
        int[] numbers = {5, 9, 8, 7, 6, 0, 4, 3, 2, 1};
        char[] chars = key.toCharArray();
        int[] keys = new int[key.length()];
        for (int i = 0; i < chars.length; i++) {
            String str = String.valueOf(chars[i]);
            keys[i] = Integer.valueOf(str);
        }
        for (int i = 0; i < keys.length; i++) {
            secret += numbers[keys[i]];
        }
        return secret;
    }
}

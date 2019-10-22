package jp.android.questionapp.common;

import java.util.regex.Pattern;

public class CommonUtils {

    /* 必須チェック */
    public static boolean isEmpty(String str) {

        boolean flg = true;
        if (str == null) {
            flg = false;
        } else if (str.length() == 0) {
            flg = false;
        }

        return flg;
    }

    /* 半角英数字チェック */
    public static boolean isHalfAlphanumeric(String str) {

        return Pattern.matches("^[0-9a-zA-Z]+$", str);
    }
}

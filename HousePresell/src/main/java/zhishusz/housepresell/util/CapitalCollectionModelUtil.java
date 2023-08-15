package zhishusz.housepresell.util;

/**
 * Created by Dechert on 2018-11-19.
 * Company: zhishusz
 */

public class CapitalCollectionModelUtil {
    public static String number2Name(String number) {
        if (number == null) {
            return "Number is null";
        }
        switch (number) {
            case "1":
                return "对接银行核心系统";
            case "2":
                return "正泰银行端";
            case "3":
                return "对接银行特色平台";
            default:
                return "No match or dirty data";
        }
    }

    public static int name2Number(String name) {
        if (name == null) {
            return -1;
        }
        switch (name) {
            case "对接银行核心系统":
                return 1;
            case "正泰银行端":
                return 2;
            case "对接银行特色平台":
                return 3;
            default:
                return -1;
        }
    }

}

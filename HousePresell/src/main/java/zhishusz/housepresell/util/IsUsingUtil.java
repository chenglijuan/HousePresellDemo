package zhishusz.housepresell.util;

/**
 * Created by Dechert on 2018-11-19.
 * Company: zhishusz
 */

public class IsUsingUtil {
    public static String number2Name(Integer number) {
        if (number == null) {
            return "Number is null";
        }
        switch (number) {
            case 0:
                return "已启用";
            case 1:
                return "未启用";
            default:
                return "未识别";
        }
    }

}

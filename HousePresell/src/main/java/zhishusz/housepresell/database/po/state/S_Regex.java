package zhishusz.housepresell.database.po.state;

/**
 * 正则校验规则
 * 
 * @author Administrator
 * @date 2020/08/27
 */
public class S_Regex {
    /*
     * 手机号
     */
    public static final String PhoneNumber = "/^(13[0-9]|14[01456879]|15[0-3,5-9]|16[2567]|17[0-8]|18[0-9]|19[0-3,5-9])\\d{8}$/";

    /*
     * 手机号
     * 1开头后面十位数字
     */
    public static final String PhoneNumberNormal = "^[1]\\d{10}$";
    /*
     * 证件号
     */
    public static final String IncardNumber =
        "/^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$/";

}

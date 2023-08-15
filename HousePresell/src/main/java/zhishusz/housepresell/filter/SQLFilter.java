package zhishusz.housepresell.filter;

import org.apache.commons.lang.StringUtils;
import zhishusz.housepresell.exception.RoolBackException;

/**
 * sql过滤
 *
 * @author xlf
 * @email xlfbe696@gmail.com
 * @date 2017年4月19日 上午10:41:25
 */
public class SQLFilter {

    /**
     * SQL注入过滤
     *
     * @param str
     *            待验证的字符串
     */
    public static String sqlInject(String str) {

//        if (StringUtils.isBlank(str)) {
//            return str;
//        }
        if (null == str) {
            return null;
        }
        // 去掉'|"|;|\字符
        str = str.replaceAll("<", "＜");
        str = str.replaceAll(">", "＞");
        str = str.replaceAll("'", "＇");
        str = str.replaceAll(";", "﹔");
        str = str.replaceAll("&", "＆");
        str = str.replaceAll("%", "﹪");
        str = str.replaceAll("#", "＃");
        str = str.replaceAll("sleep", " ");
        str = str.replaceAll("select", "seleᴄt");// "c"→"ᴄ"
        str = str.replaceAll("prompt", " ");
        str = str.replaceAll("truncate", "trunᴄate");// "c"→"ᴄ"
        str = str.replaceAll("exec", "exeᴄ");// "c"→"ᴄ"
        str = str.replaceAll("union", "uniᴏn");// "o"→"ᴏ"
        str = str.replaceAll("drop", "drᴏp");// "o"→"ᴏ"
        str = str.replaceAll("insert", "ins℮rt");// "e"→"℮"
        str = str.replaceAll("delete", "delet℮");// "e"→"℮"
        str = str.replaceAll("script", "sᴄript");// "c"→"ᴄ"
        str = str.replaceAll("iframe", "ifram℮");// "e"→"℮"
        str = str.replaceAll("onmouseover", "onmouseov℮r");// "e"→"℮"
        str = str.replaceAll("onmousemove", "onmousemov℮");
        str = str + "";
        //        str = str.replaceAll("join", "jᴏin");// "o"→"ᴏ"
        //        str = str.replaceAll("count", "cᴏunt");// "o"→"ᴏ"
        //        str = str.replaceAll("update", "updat℮");// "e"→"℮"
//        System.out.println("str===="+str);
        return str;
    }
}

package zhishusz.housepresell.util;


import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

/**
 * @Author: chenglijuan
 * @Data: 2021/7/20  16:33
 * @Decription:
 * @Modified:
 */
public class PwdUtil {

    public static String DEFAULTPWD = "CZzttg00";

    public static final String PW_PATTERN = "^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{6,20}";

    public static String checkPwd(String pwd){

        if(StringUtils.isBlank(pwd)){
            return "密码不能为空";
        }
        //去空格
        pwd = pwd.trim();
        //密码长度 8到16位
        if(pwd.length() < 8 || pwd.length() > 16){
            return "密码长度不够";
        }
        Pattern PATTERN = Pattern.compile(PW_PATTERN);
        boolean flag = PATTERN.matcher(pwd).matches();
        if(!flag){
            return "密码要求大小写字母和数字组合";
        }
        return "";
    }

//    public static void main(String[] args) {
//
//        String url = "http://21.1.13.230:8080/esb_new/service/cz/Gjj_getApproveInfo";
//
//        Map<String , Object> map = new HashMap<>();
//        //开发企业名称
//        map.put("companyName" , "鸿韵(江苏常州)实业投资有限公司");
//        //项目名称
//        map.put("projectName" , "云翌花园");
//        //公积金楼幢id
//        //map.put("gjjBuildingId" , buildInfo.getGjjTableId());
//        //公积金楼幢id
//        map.put("gjjTableId" , "37361");
//        //施工编号
//        map.put("eCodeFromConstruction" , "2幢");
//        //在建层数
//        map.put("currentConstruction" , "10");
//        //总层数
//        map.put("floorNumer" , "26.0");
//        //进度文字说明
//        map.put("remark" , "");
//
//        List<String> smAttachmentList = new ArrayList<>();
//        smAttachmentList.add("http://21.9.2.198:8083/OssSave/bananaUpload/202108/22/bd380cbd74c34594bdc2f08fdcf9e19e.jpg");
//        smAttachmentList.add("http://21.9.2.198:8083/OssSave/bananaUpload/202108/22/be5fe59132934ba681e0c1342734299b.jpg");
//        smAttachmentList.add("http://21.9.2.198:8083/OssSave/bananaUpload/202108/22/c51a257666d343d681c6c9f2f046cb30.jpg");
//        smAttachmentList.add("http://21.9.2.198:8083/OssSave/bananaUpload/202108/22/84b50d3ae92f49a18f58543a90ca3943.jpg");
//
//        map.put("smAttachmentCfgList" , smAttachmentList);
//
//
//        Gson gson = new Gson();
//        String jsonMap = gson.toJson(map);
//        ToInterface toFace = new ToInterface();
//
//        String json = toFace.doPost(jsonMap, url);
//        System.out.println("PushApprovalInfo：" + json);
//
//    }

}

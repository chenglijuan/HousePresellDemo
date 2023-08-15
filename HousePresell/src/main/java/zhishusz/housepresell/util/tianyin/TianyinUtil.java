package zhishusz.housepresell.util.tianyin;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.entity.ContentType;
import zhishusz.housepresell.util.tianyin.entity.*;

public class TianyinUtil {
    public static void personalSealsCreate() {
        Map<String, String> heads = new HashMap<>();
        heads.put("x-timevale-project-id", TianyinConfig.projectKey_);
        String signature = HmacSHA256Utils.hmacSha256("", TianyinConfig.SECRET_);
        heads.put("x-timevale-signature", signature);
        Map<String, String> params = new HashMap<>();
        params.put("pageIndex", "1");
        params.put("pageSize", "5");
        params.put("name", "");
        params.put("mobile", "");
        params.put("licenseNumber", "");
        String result = HttpUtil.doGet("http://47.96.112.93:8888//V1/accounts/outerAccounts/list", heads, params);
        System.out.println(result);
    }

    public static void signFlowsCreate() throws IOException {
        Map<String, String> heads = new HashMap<>();
        heads.put("x-timevale-project-id", "1001014");
        FlowDocBean flowDocBean = new FlowDocBean();
        flowDocBean.setDocFilekey("$49c159c5-b0ab-4b89-b864-8da8888d2aa1$3589958504");
        flowDocBean.setDocName("");
        List<FlowDocBean> docBeanList = new ArrayList<>();
        docBeanList.add(flowDocBean);
        List<SignInfoBeanV2> signPos = new ArrayList<>();
        SignInfoBeanV2 v2 = new SignInfoBeanV2();
        v2.setPosPage("1-3");
        v2.setPosX(200.0F);
        v2.setPosY(100.0F);
        v2.setSignType(Integer.valueOf(2));
        signPos.add(v2);
        List<StandardSignDocBean> docBeans = new ArrayList<>();
        StandardSignDocBean bean = new StandardSignDocBean();
        bean.setSignPos(signPos);
        bean.setDocFilekey("$49c159c5-b0ab-4b89-b864-8da8888d2aa1$3589958504");
        docBeans.add(bean);
        List<StandardSignerInfoBean> infoBeans = new ArrayList<>();
        StandardSignerInfoBean infoBean = new StandardSignerInfoBean();
        infoBean.setAccountId("c594ca18-3809-4b01-8408-771e32fcc8a0");
        infoBean.setAccountType(Integer.valueOf(2));
        infoBean.setAutoSign(false);
        infoBean.setSignDocDetails(docBeans);
        infoBeans.add(infoBean);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("allowAddAttachment", "0");
        jsonObject.put("initiatorAccountId", "279e974f-577d-47fa-86cd-6672c617043a");
        jsonObject.put("accountType", "1");
        jsonObject.put("signDocs", docBeanList);
        jsonObject.put("signers", infoBeans);
        jsonObject.put("subject", "");
        jsonObject.put("callbackUrl", "");
        System.out.println("jsonObject="+ jsonObject.toJSONString());
        String signature = HmacSHA256Utils.hmacSha256(jsonObject.toJSONString(), "5f02792fcc03157b98a9da5e4cd78d0e");
        heads.put("x-timevale-signature", signature);
        String result = HttpUtil.doPost(TianyinConfig.API_URL_ +"V1/signFlows/create", heads, jsonObject.toJSONString());
        System.out.println(result);
    }

    public static String signFlowsCreate1(String fileKey, String docName, String accountId, String initiatorAccountId) throws IOException {
        Map<String, String> heads = new HashMap<>();
        heads.put("x-timevale-project-id", "1001014");
        FlowDocBean flowDocBean = new FlowDocBean();
        flowDocBean.setDocFilekey(fileKey);
        flowDocBean.setDocName(docName);
        List<FlowDocBean> docBeanList = new ArrayList<>();
        docBeanList.add(flowDocBean);
        List<SignInfoBeanV2> signPos = new ArrayList<>();
        SignInfoBeanV2 v2 = new SignInfoBeanV2();
        v2.setPosPage("1-3");
        v2.setPosX(200.0F);
        v2.setPosY(100.0F);
        v2.setSignType(Integer.valueOf(0));
        signPos.add(v2);
        List<StandardSignDocBean> docBeans = new ArrayList<>();
        StandardSignDocBean bean = new StandardSignDocBean();
        bean.setSignPos(signPos);
        bean.setDocFilekey(fileKey);
        docBeans.add(bean);
        List<StandardSignerInfoBean> infoBeans = new ArrayList<>();
        StandardSignerInfoBean infoBean = new StandardSignerInfoBean();
        infoBean.setAccountId(accountId);
        infoBean.setAccountType(Integer.valueOf(2));
        infoBean.setAutoSign(false);
        infoBean.setSignDocDetails(docBeans);
        infoBeans.add(infoBean);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("allowAddAttachment", "0");
        jsonObject.put("initiatorAccountId", initiatorAccountId);
        jsonObject.put("accountType", "1");
        jsonObject.put("signDocs", docBeanList);
        jsonObject.put("signers", infoBeans);
        jsonObject.put("subject", "");
        jsonObject.put("callbackUrl", "http://223.111.184.236:8000/HousePresell/TY_CallBackController");
        System.out.println("jsonObject="+ jsonObject.toJSONString());
        String signature = HmacSHA256Utils.hmacSha256(jsonObject.toJSONString(), "5f02792fcc03157b98a9da5e4cd78d0e");
        heads.put("x-timevale-signature", signature);
        String result = HttpUtil.doPost(TianyinConfig.API_URL_+"V1/signFlows/create", heads, jsonObject.toJSONString());
        System.out.println("result=" + result);
        return result;
    }

    public static void testDoPutForUploadFile2() throws IOException {
        File file = new File("e:\\Oracle");
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[fis.available()];
        fis.read(data);
        fis.close();
        Map<String, String> heads = new HashMap<>();
        heads.put("x-timevale-project-id", "1001014");
        String result = HttpUtil.doPostFile("http://47.96.112.93:8888//V1/files/upload", data, "file", "Oracle", heads, null, ContentType.create("application/pdf"));
        System.out.println(result);
    }

    public static String getPreviewUrl(String fileKey) throws IOException {
        Map<String, String> heads = new HashMap<>();
        heads.put("x-timevale-project-id", "1001014");
        String signature = HmacSHA256Utils.hmacSha256("", "5f02792fcc03157b98a9da5e4cd78d0e");
        heads.put("x-timevale-signature", signature);
        Map<String, String> params = new HashMap<>();
        params.put("fileKey", fileKey);
        String result = HttpUtil.doGet("http://47.96.112.93:8888//V1/signDocs/getPreviewUrl", heads, params);
        System.out.println(result);
        return result;
    }


    //创建外部机构
    public static String createOuterOrgans(String agentAccountId,String agentUniqueId,String cardNo,String email,
                                           String legalLicenseNumber,String legalLicenseType,String legalMobile,
                                           String legalName,String licenseNumber,String licenseType,String organizeName,String organizeNo){
        Map<String, String> heads = new HashMap<>();
        heads.put("x-timevale-project-id",TianyinConfig.projectKey_);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("agentAccountId", agentAccountId);
        jsonObject.put("agentUniqueId", agentUniqueId);
        jsonObject.put("cardNo", cardNo);
        jsonObject.put("email", email);
        jsonObject.put("legalLicenseNumber", legalLicenseNumber);
        jsonObject.put("legalLicenseType", legalLicenseType);
        jsonObject.put("legalMobile", legalMobile);

        jsonObject.put("legalName", legalName);
        jsonObject.put("licenseNumber", licenseNumber);
        jsonObject.put("licenseType", licenseType);

        jsonObject.put("organizeName", organizeName);
        jsonObject.put("organizeNo", organizeNo);
        System.out.println("jsonObject="+ jsonObject.toJSONString());
        String signature = HmacSHA256Utils.hmacSha256(jsonObject.toJSONString(), TianyinConfig.SECRET_);
        heads.put("x-timevale-signature", signature);
        String result = HttpUtil.doPost(TianyinConfig.API_URL_+TianyinConfig.createOuterOrgans, heads, jsonObject.toJSONString());
        System.out.println("result="+result );
        return null;
    }

    //外部机构发布签署流程
    public static String outOrgSignFlowsCreate(String fileKey, String docName,
                                               String accountId, String initiatorAccountId,String authorizationOrganizeId) throws IOException {
        Map<String, String> heads = new HashMap<>();
        heads.put("x-timevale-project-id", TianyinConfig.projectKey_);
        FlowDocBean flowDocBean = new FlowDocBean();
        flowDocBean.setDocFilekey(fileKey);
        flowDocBean.setDocName(docName);
        List<FlowDocBean> docBeanList = new ArrayList<>();
        docBeanList.add(flowDocBean);
        List<SignInfoBeanV2> signPos = new ArrayList<>();
        SignInfoBeanV2 v2 = new SignInfoBeanV2();
        v2.setPosPage("1-3");
        v2.setPosX(200.0F);
        v2.setPosY(100.0F);
        v2.setSignType(Integer.valueOf(0));
        signPos.add(v2);
        List<StandardSignDocBean> docBeans = new ArrayList<>();
        StandardSignDocBean bean = new StandardSignDocBean();
        bean.setSignPos(signPos);
        bean.setDocFilekey(fileKey);
        docBeans.add(bean);
        List<StandardSignerInfoBean> infoBeans = new ArrayList<>();
        StandardSignerInfoBean infoBean = new StandardSignerInfoBean();
        infoBean.setAccountId(accountId);
        infoBean.setAccountType(2);
        infoBean.setAutoSign(false);
        infoBean.setSignDocDetails(docBeans);
        infoBean.setAuthorizationOrganizeId(authorizationOrganizeId);
        infoBeans.add(infoBean);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("allowAddAttachment", "0");
        jsonObject.put("initiatorAccountId", initiatorAccountId);
        jsonObject.put("accountType", "1");
        jsonObject.put("signDocs", docBeanList);
        jsonObject.put("signers", infoBeans);
        jsonObject.put("subject", "测试外部机构签章");
        jsonObject.put("callbackUrl", "http://223.111.184.236:8000/HousePresell/TY_CallBackController");
        System.out.println("jsonObject="+ jsonObject.toJSONString());
        String signature = HmacSHA256Utils.hmacSha256(jsonObject.toJSONString(), "5f02792fcc03157b98a9da5e4cd78d0e");
        heads.put("x-timevale-signature", signature);
        String result = HttpUtil.doPost(TianyinConfig.API_URL_+"V1/signFlows/create", heads, jsonObject.toJSONString());
        System.out.println("result=" + result);
        return result;
    }


    //外部机构绑定经办人接口
    public static String outerOrgansBindAgent(String accountId,String organizeId){
        Map<String, String> heads = new HashMap<>();
        heads.put("x-timevale-project-id", TianyinConfig.projectKey_);
        List<StandardAgentModel> agentList = new ArrayList<StandardAgentModel>();
        StandardAgentModel model = new StandardAgentModel();
        model.setAccountId(accountId);
        model.setIsDefault(1);
        agentList.add(model);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("agentList", agentList);
        jsonObject.put("organizeId", organizeId);
        jsonObject.put("organizeNo", "");
        System.out.println("jsonObject="+ jsonObject.toJSONString());
        String signature = HmacSHA256Utils.hmacSha256(jsonObject.toJSONString(), "5f02792fcc03157b98a9da5e4cd78d0e");
        heads.put("x-timevale-signature", signature);
        String result = HttpUtil.doPost(TianyinConfig.API_URL_+TianyinConfig.bindAgent, heads, jsonObject.toJSONString());
        System.out.println("result=" + result);
        return result;
    }

    public static void main(String[] args) {
        //        String agentUniqueId = "";
//        String agentAccountId = "c594ca18-3809-4b01-8408-771e32fcc8a0";
//        String cardNo = "";
//        String email = "";
//        String legalLicenseNumber = "";
//        String legalLicenseType = "IDCard";
//        String legalMobile = "";
//        String legalName= "";
//        String licenseNumber="91320411MA1N01MU8P";
//        String licenseType="SOCNO";
//        String organizeName= "常州正泰房产居间服务有限公司";
//        String organizeNo= "";
//
//        createOuterOrgans(agentAccountId,agentUniqueId,cardNo,email,legalLicenseNumber,legalLicenseType,legalMobile,legalName,
//                licenseNumber,licenseType,organizeName,organizeNo);



//        String accountId= "c594ca18-3809-4b01-8408-771e32fcc8a0";
//        String authorizationOrganizeId = "2f288e97-e82e-4be2-a0ea-3f0d13b48bad";
//        outerOrgansBindAgent(accountId,authorizationOrganizeId);




        String fileKey= "$7366da3e-4c8d-488d-a95b-7d34e36809c6$1232830605";
        String docName= "Oracle透明网关常见问题处理.pdf";
        String accountId= "c594ca18-3809-4b01-8408-771e32fcc8a0";
        String initiatorAccountId = "279e974f-577d-47fa-86cd-6672c617043a";
        String authorizationOrganizeId = "2f288e97-e82e-4be2-a0ea-3f0d13b48bad";

        try {
            outOrgSignFlowsCreate(fileKey,docName,accountId,initiatorAccountId,authorizationOrganizeId);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

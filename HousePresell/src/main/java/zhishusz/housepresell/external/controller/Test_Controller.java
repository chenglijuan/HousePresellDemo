package zhishusz.housepresell.external.controller;


import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.google.gson.Gson;
import com.xiaominfo.oss.sdk.ReceiveMessage;
import com.xiaominfo.oss.sdk.client.FileBytesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDao;
import zhishusz.housepresell.database.dao.Tgpf_SpecialFundAppropriated_AFDtlDao;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.toInterface.To_NodeChange;
import zhishusz.housepresell.database.po.toInterface.To_ProjProgForcastPhoto;
import zhishusz.housepresell.external.po.PaymentLaunchDtlModel;
import zhishusz.housepresell.external.po.PaymentLaunchModel;
import zhishusz.housepresell.initialize.Tg_HolidayInitService;
import zhishusz.housepresell.timer.HandlerPicAndSubmitService;

import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.SocketUtil;
import zhishusz.housepresell.util.ToInterface;
import zhishusz.housepresell.util.fileupload.OssServerUtil;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;


/*
 * Controller公积金接口对接
 * Company：ZhiShuSZ
 */
@Controller
@Transactional
public class Test_Controller {


    @Autowired
    private HandlerPicAndSubmitService service;

    @Autowired
    private Tgpf_SpecialFundAppropriated_AFDtlDao tgpf_specialFundAppropriated_afDtlDao;

    @Autowired
    private Tgpf_SpecialFundAppropriated_AFDao tgpf_SpecialFundAppropriated_AFDao;


    @Autowired
    private Tg_HolidayInitService tg_holidayInitService;

    @Autowired
    private OssServerUtil ossUtil;// 本地上传OSS


    @RequestMapping(value = "/TEST_dingshiqi", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public void execute(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject obj) {


//        System.out.println("obj="+obj);
//
//        PaymentLaunchModel afModel = new PaymentLaunchModel();
//        List<PaymentLaunchDtlModel> transferData = new ArrayList<PaymentLaunchDtlModel>();
//
//        String orgCode = obj.getString("orgCode");
//        String isNeedCheck = obj.getString("isNeedCheck");
//        String secretKey = obj.getString("secretKey");
//
//        String accType = obj.getString("accType");
//        String amount = obj.getString("amount");
//        String bizId = obj.getString("bizId");
//        String ledgerBankCode = obj.getString("ledgerBankCode");
//        String payAccNo = obj.getString("payAccNo");
//        String recBankAccNo = obj.getString("recBankAccNo");
//        String receiveAccName = obj.getString("receiveAccName");
//        String use = obj.getString("use");
//        String projectName = obj.getString("projectName");
//        String remark = obj.getString("remark");
//
//
//        PaymentLaunchDtlModel model = new PaymentLaunchDtlModel();
//        model.setAccType(accType);
//        model.setAmount(new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
//        model.setAreaCode("");
//        model.setBizId(bizId);
//        model.setCrashFlag("");
//        model.setLedgerBankCode(ledgerBankCode);
//        model.setPayAccNo(payAccNo);
//        model.setRecBankAccNo(recBankAccNo);
//        model.setReceiveAccName(receiveAccName);
//        model.setReceiveBankCode("");
//        model.setUse(use);
//        model.setProjectName(projectName);
//        model.setRemark(remark);
//
//        afModel.setIsNeedCheck(false);
//        afModel.setOperator("");
//        afModel.setOrgCode(orgCode);
//        afModel.setSecretKey(secretKey);
//        afModel.setWorkflowKey("");
//        afModel.setTransferData(transferData);
//
//        transferData.add(model);
//        String jsonString = JSONObject.toJSONString(afModel);
//        System.out.println("json=" + jsonString);
//
//        String url = "http://172.18.5.31:8090/rest/app/pay/batchBankTransfers.html";
//
//        String httpStringPostRequest = SocketUtil.getInstance().HttpStringPostRequest(url, jsonString);
//        System.out.println("httpStringPostRequest=" + httpStringPostRequest);




//        //将附件提交到oss服务器
//        String urlPath = "D:\\uploadxy\\bd306812126644a2b45da55da80cff96.pdf";
//        FileBytesResponse fileBytesResponse = null;//返回参数
//        ossUtil.setRemoteType("1");
//        ReceiveMessage uploadOrgObjJson = ossUtil.upload(urlPath);//模拟上传获取返回路径
//        if(uploadOrgObjJson.getData()!=null)
//        {
//            fileBytesResponse = uploadOrgObjJson.getData().get(0);
//            if(uploadOrgObjJson.getData().get(0).getUrl()!=null)
//            {
//                urlPath = uploadOrgObjJson.getData().get(0).getUrl();//获取文件路径
//            }
//        }

//
//        String webTheLink = "http://172.21.105.15:811/index.asp";
//
//        String action = obj.getString("action");
//        String cate = obj.getString("cate");
//        String ts_bld_id = obj.getString("ts_bld_id");
//        String ts_id = obj.getString("ts_id");
//        String ts_pj_id = obj.getString("ts_pj_id");
//        String jdtime = obj.getString("jdtime");
//        String news_title = obj.getString("news_title");
//        String news_title1 = obj.getString("news_title1");
//        String smallpic = obj.getString("smallpic");
//        String image2 = obj.getString("image2");
//        String dqlc = obj.getString("dqlc");
//
//        Gson gson = new Gson();
//        String jsonMap;
//        String decodeStr;
//        ToInterface toFace = new ToInterface();
//        boolean interfaceUtil;
//
//        // 推送信息
//        To_ProjProgForcastPhoto pushVo = new To_ProjProgForcastPhoto();
//        pushVo.setAction(action);
//        pushVo.setCate(cate);
//        pushVo.setTs_id(ts_id);
//        pushVo.setTs_bld_id(ts_bld_id);
//        pushVo.setTs_pj_id(ts_pj_id);
//        pushVo.setJdtime(jdtime);
//        pushVo.setNews_title(news_title);
//        pushVo.setNews_title1(news_title1);
//        pushVo.setSmallpic(smallpic);
//        pushVo.setImage2(image2);
//        pushVo.setDqlc(dqlc);
//
//
//        jsonMap = gson.toJson(pushVo);
//        decodeStr = Base64Encoder.encode(jsonMap);
//        System.out.println(jsonMap);
//        System.out.println(decodeStr);
//        interfaceUtil = toFace.interfaceUtil(decodeStr, webTheLink);
////
//        System.out.println(interfaceUtil);




//        Map<String, Object> pushVo = new HashMap<String, Object>();
//        pushVo.put("cate", "sso_login");
//        pushVo.put("userid", 635);
//        pushVo.put("stim", MyDatetime.getInstance().dateToString2(System.currentTimeMillis()));
//        pushVo.put("etim", MyDatetime.getInstance().dateToString2(System.currentTimeMillis() + 3600000));
//
//        Gson gson = new Gson();
//
//        String jsonMap = gson.toJson(pushVo);
//
//        System.out.println(jsonMap);
//
//        String decodeStr = Base64Encoder.encode(jsonMap);
//
//        System.out.println(decodeStr);

//        ToInterface toFace = new ToInterface();
//        // String str = toFace.commonInterface("http://apits.czzhengtai.com:811/", decodeStr);
//        String str = toFace.commonInterface("http://172.21.105.15:811/", decodeStr);
//        System.out.println(str);
//
//        if (StrUtil.isNotBlank(str)) {
//            Map<String, String> fromJson = gson.fromJson(str, Map.class);
//            System.out.println(fromJson.get("aeecss_token"));
//        } else {
//            System.out.println("系统没返回");
//        }




    }


    public static void main(String[] args) {




//        JSONObject object1 = new JSONObject();
//        object1.put("contractNo","0630A0B6DA9F4BC6AF50");
//        object1.put("tripleagreementCode","202200011220");
//
//        JSONObject object2 = new JSONObject();
//        object2.put("contractNo","0630A0B6DA9F4BC6AF51");
//        object2.put("tripleagreementCode","202200011221");
//
//
//        JSONArray array = new JSONArray();
//        array.add(object1);
//        array.add(object2);
//
//        JSONObject object = new JSONObject();
//        object.put("data",array);
//
//        System.out.println("array="+object.toJSONString());




      /*  To_ProjProgForcastPhoto pushVo = new To_ProjProgForcastPhoto();
        pushVo.setCate("pjsptj");
        pushVo.setTs_id("4152");
        pushVo.setTs_pj_id("1319");
        pushVo.setJdtime("2023-01-15");

        Gson gson = new Gson();
        String jsonMap = gson.toJson(pushVo);
        System.out.println(jsonMap);
        String decodeStr = Base64Encoder.encode(jsonMap);
        System.out.println(decodeStr);

        ToInterface toFace = new ToInterface();
        boolean interfaceUtil = toFace.interfaceUtil(decodeStr, "http://apits.czzhengtai.com:811/index.asp");

        if (interfaceUtil) {
            System.out.println("200");
        } else {
            System.out.println("300");
        }

*/


//
//        //获取网站ssotoken
//        Map<String, Object> pushVo = new HashMap<String, Object>();
//        pushVo.put("cate", "sso_login");
//        pushVo.put("userid", "652");
//        pushVo.put("stim", MyDatetime.getInstance().dateToString2(System.currentTimeMillis()));
//        pushVo.put("etim", MyDatetime.getInstance().dateToString2(System.currentTimeMillis() + 3600000));
////
//        Gson gson = new Gson();
////
//        String jsonMap = gson.toJson(pushVo);
////
//        System.out.println(jsonMap);
////
//        String decodeStr = Base64Encoder.encode(jsonMap);
////
//        System.out.println(decodeStr);
////
//       try{
//           ToInterface toFace = new ToInterface();
//           //String str = toFace.commonInterface("http://apits.czzhengtai.com:811/",decodeStr);
//           String str = toFace.commonInterface("http://172.21.105.15:811/index.asp", decodeStr);
//          //  String str = toFace.commonInterface("http://172.21.105.15:8003/", decodeStr);
//           System.out.println(str);
////
//           if (StrUtil.isNotBlank(str)) {
//               Map<String, String> fromJson = gson.fromJson(str, Map.class);
//               System.out.println(fromJson.get("aeecss_token"));
//           } else {
//               System.out.println("系统没返回");
//           }
//       }catch (Exception e){
//           e.printStackTrace();
//       }

//        JSONObject obj = new JSONObject();
//        String gjjurl = "http://21.1.13.10:8080/esb_new/service/cz/Gjj_getApproveInfo";
//
//        String companyName = obj.getString("companyName");
//        String projectName = obj.getString("projectName");
//        String gjjTableId = obj.getString("gjjTableId");
//        String eCodeFromConstruction = obj.getString("eCodeFromConstruction");
//        String currentConstruction = obj.getString("currentConstruction");
//        String floorNumer = obj.getString("floorNumer");
//        String remark = obj.getString("remark");
//        JSONArray picurl = obj.getJSONArray("smAttachmentCfgList");
//
//        //构建基本参数
//        Map<String , Object> map = new HashMap<>();
//        //开发企业名称
//        map.put("companyName" , companyName);
//        //项目名称
//        map.put("projectName" , projectName);
//        //公积金楼幢id
//        map.put("gjjTableId" , gjjTableId);
//        //施工编号
//        map.put("eCodeFromConstruction" , eCodeFromConstruction);
//        //在建层数  建设进度类型（1-主体结构 2-外立面装饰 3-室内装修
//        //  如果是主体结构
//            //否则当前在建层数是 总层数
//        map.put("currentConstruction" , currentConstruction);
//
//        //总层数
//        map.put("floorNumer" ,floorNumer);
//        //进度文字说明
//        map.put("remark" , remark);
//
//        //构建附件参数
////        List<String> smAttachmentList = new ArrayList<>();
//
//
//        //附件信息
//        map.put("smAttachmentCfgList" , picurl);
//
//        Gson gson = new Gson();
//        String jsonMap = gson.toJson(map);
//        System.out.println("PushApprovalInfo"+ jsonMap);
//
//        ToInterface toFace = new ToInterface();
//
//        try {
//            String json = toFace.doPost(jsonMap, gjjurl);
//            System.out.println("PushApprovalInfo：" + json);
//        } catch (Exception e) {
//            System.out.println("PushApprovalInfo：" + e.getMessage());
//            e.printStackTrace();
//        }
//



        /*********************************用款申请推送******************************************************/

//        PaymentLaunchModel afModel = new PaymentLaunchModel();
//        List<PaymentLaunchDtlModel> transferData = new ArrayList<PaymentLaunchDtlModel>();
//
//        String orgCode = obj.getString("orgCode");
//        String isNeedCheck = obj.getString("isNeedCheck");
//        String secretKey = obj.getString("secretKey");
//
//        String accType = obj.getString("accType");
//        String amount = obj.getString("amount");
//        String bizId = obj.getString("bizId");
//        String ledgerBankCode = obj.getString("ledgerBankCode");
//        String payAccNo = obj.getString("payAccNo");
//        String recBankAccNo = obj.getString("recBankAccNo");
//        String receiveAccName = obj.getString("receiveAccName");
//        String use = obj.getString("use");
//        String projectName = obj.getString("projectName");
//        String remark = obj.getString("remark");
//
//
//        PaymentLaunchDtlModel model = new PaymentLaunchDtlModel();
//        model.setAccType(accType);
//        model.setAmount(new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
//        model.setAreaCode("");
//        model.setBizId(bizId);
//        model.setCrashFlag("");
//        model.setLedgerBankCode(ledgerBankCode);
//        model.setPayAccNo(payAccNo);
//        model.setRecBankAccNo(recBankAccNo);
//        model.setReceiveAccName(receiveAccName);
//        model.setReceiveBankCode("");
//        model.setUse(use);
//        model.setProjectName(projectName);
//        model.setRemark(remark);
//
//        afModel.setIsNeedCheck(false);
//        afModel.setOperator("");
//        afModel.setOrgCode(orgCode);
//        afModel.setSecretKey(secretKey);
//        afModel.setWorkflowKey("");
//        afModel.setTransferData(transferData);
//
//        transferData.add(model);
//        String jsonString = JSONObject.toJSONString(afModel);
//        System.out.println("json=" + jsonString);
//
//        String url = "http://172.18.5.31:8090/rest/app/pay/batchBankTransfers.html";

//        String httpStringPostRequest = SocketUtil.getInstance().HttpStringPostRequest(url, jsonString);
//        System.out.println("httpStringPostRequest=" + httpStringPostRequest);
        /*************************************用款申请推送**************************************************/



        /**********************************进度巡查*************************************************************/
//
//        String webTheLink = "http://apits.czzhengtai.com:811/";
//
//        String action = obj.getString("action");
//        String cate = obj.getString("cate");
//        String ts_bld_id = obj.getString("ts_bld_id");
//        String ts_id = obj.getString("ts_id");
//        String ts_pj_id = obj.getString("ts_pj_id");
//        String jdtime = obj.getString("jdtime");
//        String news_title = obj.getString("news_title");
//        String news_title1 = obj.getString("news_title1");
//        String smallpic = obj.getString("smallpic");
//        String image2 = obj.getString("image2");
//        String dqlc = obj.getString("dqlc");
//
//        Gson gson = new Gson();
//        String jsonMap;
//        String decodeStr;
//        ToInterface toFace = new ToInterface();
//        boolean interfaceUtil;
//
//        // 推送信息
//        To_ProjProgForcastPhoto pushVo = new To_ProjProgForcastPhoto();
//        pushVo.setAction(action);
//        pushVo.setCate(cate);
//        pushVo.setTs_id(ts_id);
//        pushVo.setTs_bld_id(ts_bld_id);
//        pushVo.setTs_pj_id(ts_pj_id);
//        pushVo.setJdtime(jdtime);
//        pushVo.setNews_title(news_title);
//        pushVo.setNews_title1(news_title1);
//        pushVo.setSmallpic(smallpic);
//        pushVo.setImage2(image2);
//        pushVo.setDqlc(dqlc);
//
//
//        jsonMap = gson.toJson(pushVo);
//        decodeStr = Base64Encoder.encode(jsonMap);
////        System.out.println(jsonMap);
//        System.out.println(decodeStr);
//        interfaceUtil = toFace.interfaceUtil(decodeStr, webTheLink);
//
//        System.out.println(interfaceUtil);

    /**********************************进度巡查*************************************************************/

        /**********************************进度巡查*************************************************************/


//        String webTheLink = "http://apits.czzhengtai.com:811/";
//
//        String action = obj.getString("action");
//        String cate = obj.getString("cate");
//        String ts_bld_id = obj.getString("ts_bld_id");
//        String ts_id = obj.getString("ts_id");
//        String jdzt = obj.getString("jdzt");
//        String jfbatzs_url = obj.getString("jdtime");
//        String gswz_url = obj.getString("news_title");
//        String jlbg_name1 = obj.getString("jlbg_name1");
//        String jlbg_url1 = obj.getString("jlbg_url1");
//        String jlbg_name2 = obj.getString("jlbg_name2");
//        String jlbg_url2 = obj.getString("jlbg_url2");
//        String jdtime = obj.getString("jdtime");
//        String gabhdzb = obj.getString("gabhdzb");
//        String jlbg_url = obj.getString("jlbg_url");
//        String gswz_picurl = obj.getString("gswz_picurl");
//
//        Gson gson = new Gson();
//        String jsonMap;
//        String decodeStr;
//        ToInterface toFace = new ToInterface();
//        boolean interfaceUtil;
//
//
//        To_NodeChange nodeChange = new To_NodeChange();
//        nodeChange.setAction(action);
//        nodeChange.setCate(cate);
//        nodeChange.setTs_bld_id(ts_bld_id);
//        nodeChange.setTs_id(ts_id);
//        nodeChange.setJdzt(jdzt);
//        nodeChange.setJfbatzs_url("");
//        nodeChange.setGswz_url("");
//        nodeChange.setJlbg_name1(jlbg_name1);
//        nodeChange.setJlbg_url1(jlbg_url1);
//        nodeChange.setJlbg_name2(jlbg_name2);
//        nodeChange.setJlbg_url2(jlbg_url2);
//        nodeChange.setJdtime(jdtime);
//        nodeChange.setGabhdzb("");
//        nodeChange.setGswz_picurl("");
//        nodeChange.setJlbg_url(jlbg_url1 + "," + jlbg_url2);
//
//        // 记录接口交互信息
//
//        jsonMap = gson.toJson(nodeChange);
//        decodeStr = Base64Encoder.encode(jsonMap);
//        System.out.println(jsonMap);
//        System.out.println(decodeStr);
//        interfaceUtil = toFace.interfaceUtil(decodeStr, webTheLink);

/**********************************监理报告*************************************************************/



    }


}

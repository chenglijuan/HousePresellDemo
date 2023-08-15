package zhishusz.housepresell.dingding.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zhishusz.housepresell.database.dao.Tgpf_SocketMsgDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount;
import zhishusz.housepresell.database.po.Empj_BldLimitAmount_Dtl;
import zhishusz.housepresell.database.po.Tgpf_SocketMsg;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.dingding.entity.PanYunDingDingAssignTaskEntity;
import zhishusz.housepresell.dingding.service.PanYunDingDingPushDataService;
import zhishusz.housepresell.dingding.utils.SoapUtil;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description 攀云钉钉推送数据服务实现类
 * @Author jxx
 * @Date 2020/9/28 20:55
 * @Version
 **/
@Service
public class PanYunDingDingPushDataServiceImplbak implements PanYunDingDingPushDataService {

    /**
     * 访问地址
     */
    // private static String url = "http://www.pan-yun.com/ZhengTai/ZhengTaiH3WebSer.asmx?wsdl";
    private static String url = "http://172.18.5.189:8811/ZhengTaiH3WebSer.asmx?wsdl";

    @Autowired
    private Tgpf_SocketMsgDao tgpf_SocketMsgDao;

    /**
     * 命名空间
     */
    private static String namespace = "http://tempuri.org/";

    private static SimpleDateFormat SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS =
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger LOGGER = LoggerFactory.getLogger(PanYunDingDingPushDataServiceImplbak.class);

    @Override
    public boolean pushSupervisorCompanyData(Empj_BldLimitAmount bldLimitAmount) {

        StringBuffer sb = new StringBuffer();

        System.out.println("---------------------pushSupervisorCompanyData Start---------------------");
        Map<String, Object> oneParams = new HashMap<>(3);
        Emmp_CompanyInfo companyInfoOne = bldLimitAmount.getCompanyOne();
        oneParams.put("Name", bldLimitAmount.getCompanyOneName());
        oneParams.put("Code", companyInfoOne.geteCode());
        oneParams.put("PersonPhone", "");
        System.out.println("pushSupervisorCompanyData 1 param : " + JSONObject.toJSONString(oneParams));

        sb.append("pushSupervisorCompanyData 1 param : " + JSONObject.toJSONString(oneParams) + "/n");

        String resultOne = "success";
            //SoapUtil.splicingMessage(namespace, url, namespace + "SupervisionCompany", "SupervisionCompany", oneParams);
        System.out.println("resultOne : " + resultOne);
        sb.append("resultOne : " + resultOne + "/n");

//        Map<String, Object> twoParams = new HashMap<>(3);
//        Emmp_CompanyInfo companyInfoTwo = bldLimitAmount.getCompanyTwo();
//        twoParams.put("Name", bldLimitAmount.getCompanyTwoName());
//        twoParams.put("Code", companyInfoTwo.geteCode());
//        twoParams.put("PersonPhone", "");
//        System.out.println("pushSupervisorCompanyData 2 param : " + JSONObject.toJSONString(twoParams));
//        sb.append("pushSupervisorCompanyData 2 param : " + JSONObject.toJSONString(twoParams) + "/n");
//
//        String resultTwo =
//            SoapUtil.splicingMessage(namespace, url, namespace + "SupervisionCompany", "SupervisionCompany", twoParams);
//        System.out.println("resultTwo : " + resultTwo);
//        sb.append("resultTwo : " + resultTwo);

        System.out.println("---------------------pushSupervisorCompanyData End---------------------");

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());

        tgpf_SocketMsg.setMsgDirection(String.valueOf(bldLimitAmount.getTableId()) + "--pushSupervisorCompanyData");
        tgpf_SocketMsg.setMsgContentArchives(sb.toString());
        tgpf_SocketMsg.setReturnCode("200");
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);
        
        if(resultOne.contains("success")){
        	return true;
        }else{
        	return false;
        }
    }


//    @Override
//    public boolean pushSupervisorCompanyData(Empj_BldLimitAmount bldLimitAmount) {
//
//        StringBuffer sb = new StringBuffer();
//
//        System.out.println("---------------------pushSupervisorCompanyData Start---------------------");
//        Map<String, Object> oneParams = new HashMap<>(3);
//        Emmp_CompanyInfo companyInfoOne = bldLimitAmount.getCompanyOne();
//        oneParams.put("Name", bldLimitAmount.getCompanyOneName());
//        oneParams.put("Code", companyInfoOne.geteCode());
//        oneParams.put("PersonPhone", "");
//        System.out.println("pushSupervisorCompanyData 1 param : " + JSONObject.toJSONString(oneParams));
//
//        sb.append("pushSupervisorCompanyData 1 param : " + JSONObject.toJSONString(oneParams) + "/n");
//
//        String resultOne =
//                SoapUtil.splicingMessage(namespace, url, namespace + "SupervisionCompany", "SupervisionCompany", oneParams);
//        System.out.println("resultOne : " + resultOne);
//        sb.append("resultOne : " + resultOne + "/n");
//
//        Map<String, Object> twoParams = new HashMap<>(3);
//        Emmp_CompanyInfo companyInfoTwo = bldLimitAmount.getCompanyTwo();
//        twoParams.put("Name", bldLimitAmount.getCompanyTwoName());
//        twoParams.put("Code", companyInfoTwo.geteCode());
//        twoParams.put("PersonPhone", "");
//        System.out.println("pushSupervisorCompanyData 2 param : " + JSONObject.toJSONString(twoParams));
//        sb.append("pushSupervisorCompanyData 2 param : " + JSONObject.toJSONString(twoParams) + "/n");
//
//        String resultTwo =
//                SoapUtil.splicingMessage(namespace, url, namespace + "SupervisionCompany", "SupervisionCompany", twoParams);
//        System.out.println("resultTwo : " + resultTwo);
//        sb.append("resultTwo : " + resultTwo);
//
//        System.out.println("---------------------pushSupervisorCompanyData End---------------------");
//
//        // 记录接口交互信息
//        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
//        tgpf_SocketMsg.setTheState(S_TheState.Normal);
//        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
//        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
//        tgpf_SocketMsg.setMsgStatus(1);
//        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
//
//        tgpf_SocketMsg.setMsgDirection(String.valueOf(bldLimitAmount.getTableId()) + "--pushSupervisorCompanyData");
//        tgpf_SocketMsg.setMsgContentArchives(sb.toString());
//        tgpf_SocketMsg.setReturnCode("200");
//        tgpf_SocketMsgDao.save(tgpf_SocketMsg);
//
//        if(resultOne.contains("success") && resultTwo.contains("success")){
//            return true;
//        }else{
//            return false;
//        }
//    }

    @Override
    public boolean pushProjectData(Empj_BldLimitAmount bldLimitAmount) {

        StringBuffer sb = new StringBuffer();

        System.out.println("---------------------pushProjectData Start---------------------");
        Map<String, Object> params = new HashMap<>(2);
        params.put("ProjectCode", bldLimitAmount.geteCodeOfProject());
        params.put("ProjectName", bldLimitAmount.getTheNameOfProject());
        System.out.println("pushProjectData param : " + JSONObject.toJSONString(params));
        sb.append("pushProjectData param : " + JSONObject.toJSONString(params) + "/n");

        String result = SoapUtil.splicingMessage(namespace, url, namespace + "ProjectInfo", "ProjectInfo", params);
        System.out.println("result : " + result);
        sb.append("result : " + result);

        System.out.println("---------------------pushProjectData End---------------------");

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());

        tgpf_SocketMsg.setMsgDirection(String.valueOf(bldLimitAmount.getTableId()) + "--pushProjectData");
        tgpf_SocketMsg.setMsgContentArchives(sb.toString());
        tgpf_SocketMsg.setReturnCode("200");
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);
        
        if(result.contains("success")){
        	return true;
        }else{
        	return false;
        }
    }

    @Override
    public boolean pushBuildingData(Empj_BldLimitAmount bldLimitAmount) {

        StringBuffer sb = new StringBuffer();
        System.out.println("---------------------pushBuildingData Start---------------------");
        for (Empj_BldLimitAmount_Dtl dtl : bldLimitAmount.getDtlList()) {

            if ("1".equals(dtl.getApprovalResult())) {
                Map<String, Object> params = new HashMap<>(3);
                params.put("ProjectCode", dtl.geteCodeOfProject());
                params.put("BuildingCode", dtl.geteCodeOfBuilding());
                params.put("BuildingName", dtl.geteCodeFromConstruction());
                System.out.println("pushBuildingData param : " + JSONObject.toJSONString(params));

                sb.append("pushBuildingData param : " + JSONObject.toJSONString(params) + "/n");

                String result =
                    SoapUtil.splicingMessage(namespace, url, namespace + "BuildingInfo", "BuildingInfo", params);
                System.out.println("result : " + result);

                sb.append("result : " + result + "/n");
            }

        }
        System.out.println("---------------------pushBuildingData End---------------------");

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());

        tgpf_SocketMsg.setMsgDirection(String.valueOf(bldLimitAmount.getTableId()) + "--pushBuildingData");
        tgpf_SocketMsg.setMsgContentArchives(sb.toString());
        tgpf_SocketMsg.setReturnCode("200");
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);
        
        if(sb.toString().contains("success")){
        	return true;
        }else{
        	return false;
        }
    }

    @Override
    public void pushChangeNodeData(Empj_BldLimitAmount bldLimitAmount) {

        StringBuffer sb = new StringBuffer();

        System.out.println("---------------------pushChangeNodeData Start---------------------");
        for (Empj_BldLimitAmount_Dtl dtl : bldLimitAmount.getDtlList()) {

            if ("1".equals(dtl.getApprovalResult())) {
                Map<String, Object> params = new HashMap<>(1);
                // params.put("NodeName", dtl.getPredictionNodeName());
                params.put("NodeName", dtl.getExpectFigureProgress().getStageName());
                System.out.println("pushChangeNodeData param : " + JSONObject.toJSONString(params));

                sb.append("pushChangeNodeData param : " + JSONObject.toJSONString(params) + "/n");
                String result =
                    SoapUtil.splicingMessage(namespace, url, namespace + "ChangeNode", "ChangeNode", params);
                System.out.println("result : " + result);
                sb.append("result : " + result + "/n");

            }

        }
        System.out.println("---------------------pushChangeNodeData End---------------------");

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());

        tgpf_SocketMsg.setMsgDirection(String.valueOf(bldLimitAmount.getTableId()) + "--pushChangeNodeData");
        tgpf_SocketMsg.setMsgContentArchives(sb.toString());
        tgpf_SocketMsg.setReturnCode("200");
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);
    }

    @Override
    public boolean assignTask(Empj_BldLimitAmount bldLimitAmount) {

        StringBuffer sb = new StringBuffer();
        System.out.println("---------------------assignTask Start---------------------");

        PanYunDingDingAssignTaskEntity entity = new PanYunDingDingAssignTaskEntity();
        entity.setTaskId(bldLimitAmount.geteCode());
        entity.setProjectCode(bldLimitAmount.geteCodeOfProject());
        entity.setCreateUserPhone(bldLimitAmount.getUserStart().getPhoneNumber());
        entity.setSupervisorCompCode(bldLimitAmount.getCompanyOne().geteCode());
        entity.setContactOne(bldLimitAmount.getContactOne());
        entity.setContactTwo(bldLimitAmount.getContactTwo());
        entity.setTelephoneOne(bldLimitAmount.getTelephoneOne());
        entity.setTelephoneTwo(bldLimitAmount.getTelephoneTwo());
        entity.setProjAddr(StrUtil.isBlank(bldLimitAmount.getProject().getAddress()) ? ""
            : bldLimitAmount.getProject().getAddress().trim());
        Date date = new Date();
        if (null != bldLimitAmount.getAppointmentDateOne()) {
            date = bldLimitAmount.getAppointmentDateOne();
        }
        entity.setAppointment(SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS.format(date));
        List<PanYunDingDingAssignTaskEntity.BuildingInfo> buildingInfos = new LinkedList<>();
        for (Empj_BldLimitAmount_Dtl dtl : bldLimitAmount.getDtlList()) {

            if ("1".equals(dtl.getApprovalResult())) {
                PanYunDingDingAssignTaskEntity.BuildingInfo buildingInfo =
                    new PanYunDingDingAssignTaskEntity.BuildingInfo();

                buildingInfo.setBuildingCode(dtl.geteCodeOfBuilding());
                buildingInfo.setFloor(dtl.getUpfloorNumber().toString());
                buildingInfo.setChangeNode(dtl.getExpectFigureProgress().getStageName());
                buildingInfos.add(buildingInfo);
            }

        }
        entity.setBuilds(buildingInfos);
        Map<String, Object> params4One = new HashMap<>(1);
        params4One.put("TaskContent", JSONObject.toJSONString(entity));
        System.out.println("assignTask TaskContentOne : " + JSONObject.toJSONString(entity));

        sb.append("assignTask TaskContentOne : " + JSONObject.toJSONString(entity) + "/n");
        String resultOne = SoapUtil.splicingMessage(namespace, url, namespace + "AssignTask", "AssignTask", params4One);
        System.out.println("resultOne : " + resultOne);
        sb.append("resultOne : " + resultOne + "/n");

        entity.setSupervisorCompCode(bldLimitAmount.getCompanyTwo().geteCode());
        if (null != bldLimitAmount.getAppointmentDateTwo()) {
            date = bldLimitAmount.getAppointmentDateTwo();
        } else {
            date = new Date();
        }
        entity.setAppointment(SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS.format(date));

        Map<String, Object> params4Two = new HashMap<>(1);
        params4Two.put("TaskContent", JSONObject.toJSONString(entity));
        System.out.println("assignTask TaskContentTwo : " + JSONObject.toJSONString(entity));
        sb.append("assignTask TaskContentTwo : " + JSONObject.toJSONString(entity) + "/n");
        String resultTwo = SoapUtil.splicingMessage(namespace, url, namespace + "AssignTask", "AssignTask", params4Two);
        System.out.println("resultTwo : " + resultTwo);
        sb.append("resultTwo : " + resultTwo + "/n");

        System.out.println("---------------------assignTask End---------------------");

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());

        tgpf_SocketMsg.setMsgDirection(String.valueOf(bldLimitAmount.getTableId()) + "--assignTask");
        tgpf_SocketMsg.setMsgContentArchives(sb.toString());
        tgpf_SocketMsg.setReturnCode("200");
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);
        
        if(resultOne.contains("success") && resultTwo.contains("success")){
        	return true;
        }else{
        	return false;
        }
    }
}

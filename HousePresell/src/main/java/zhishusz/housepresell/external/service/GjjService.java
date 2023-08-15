package zhishusz.housepresell.external.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import zhishusz.housepresell.controller.form.*;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.JsonUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.gjj.GJJ_LoanInforMation;
import zhishusz.housepresell.util.gjj.Gjj_BuildingDetail;
import zhishusz.housepresell.util.gjj.Gjj_BulidingRelation;

/**
 * 网银数据推送接收service
 *
 * @author Administrator
 */
@Service
@Transactional
@Slf4j
public class GjjService {

    @Autowired
    private Tgpf_SocketMsgDao tgpf_SocketMsgDao;// 接口报文表

    @Autowired
    private Sm_AttachmentDao attacmentDao;

    @Autowired
    private Empj_BuildingInfoDao empj_BuildingInfoDao;

    @Autowired
    private Gjj_LoanInforMationDao gjj_loanInforMationDao;

    @Autowired
    private Tgxy_TripleAgreementDao tgxy_tripleAgreementDao;
    @Autowired
    private Sm_BaseParameterDao sm_BaseParameterDao;

    @Autowired
    private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;

//    public static String replaceUrl = "changzhou.zhishusz.com:19000";

    public static String replaceUrl = "172.18.5.180:19000";

   /* @Autowired
    private Gjj_BulidingRelationDao gjj_bulidingRelationDao;*/

    //
    public synchronized Properties getInfoByeCodeFromPresellSystemExecute(HttpServletRequest request, JSONObject obj) {
        Properties properties = new Properties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        if (obj == null) {
            return MyBackInfo.fail(properties, "请输入传递参数");
        }
        String json = obj.toJSONString();
        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
        tgpf_SocketMsg.setMsgStatus(1);// 发送状态
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
        //公积金提交数据到正泰
        tgpf_SocketMsg.setMsgDirection("GJJ_TO_ZT_001");// 报文方向
        tgpf_SocketMsg.setMsgContentArchives(json);// 报文内容
        tgpf_SocketMsg.setReturnCode("200");// 返回码
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        // 预售证编号
        String ecodeofpresell = obj.getString("ecodeofpresell");
        if (StringUtils.isBlank(ecodeofpresell)) {
            return MyBackInfo.fail(properties, "请输入预售证编号");
        }
        // 通过预售证编号去查询楼撞信息等

        Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
        baseParameterForm.setTheState(S_TheState.Normal);
        baseParameterForm.setTheValue("91003");
        baseParameterForm.setParametertype("91");
        Sm_BaseParameter baseParameter = sm_BaseParameterDao
                .findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));
        if (null == baseParameter) {
            return MyBackInfo.fail(properties, "未查询到配置参数！");
        }

        Empj_BuildingInfoForm form = new Empj_BuildingInfoForm();
        form.setTheState(S_TheState.Normal);
        form.seteCodeFromPresellCert(ecodeofpresell);
        List<Gjj_BuildingDetail> list = new ArrayList<Gjj_BuildingDetail>();

        List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBaseHql(), form));

        //附件配置表
        Sm_AttachmentCfg attachmentCfg = new Sm_AttachmentCfg();
        attachmentCfg.setTableId(54l);
        attachmentCfg.seteCode("010201N18122700003");

        Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();
        attachmentForm.setTheState(S_TheState.Normal);
        attachmentForm.setAttachmentCfg(attachmentCfg);
        attachmentForm.setSourceType(attachmentCfg.geteCode());
        attachmentForm.setBusiType("06110201");

        Tgxy_EscrowAgreementForm escrowAgreementForm = new Tgxy_EscrowAgreementForm();
        escrowAgreementForm.setTheState(S_TheState.Normal);

        Tgxy_EscrowAgreement tgxy_EscrowAgreement;


        List<Sm_Attachment> attachments = null;
        List<String> smAttachments = null;

        Gjj_BuildingDetail detail = null;
        for (Empj_BuildingInfo buildingInfo : empj_BuildingInfoList) {
            detail = new Gjj_BuildingDetail();
            if (buildingInfo.getDevelopCompany() != null) {
                detail.setCompanyName(buildingInfo.getDevelopCompany().getTheName());
            }
            detail.setEcodeofpresell(ecodeofpresell);
            detail.setTableId(buildingInfo.getTableId());
            detail.setProjectName(buildingInfo.getProject().getTheName());
            detail.seteCodeFromPublicSecurity(buildingInfo.geteCodeFromPublicSecurity());
            detail.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());
            detail.setBuildingId(buildingInfo.getTableId());

            //获取楼栋托管协议编号
            escrowAgreementForm.setTheNameOfProject(buildingInfo.getTheNameOfProject());
            escrowAgreementForm.setBuildingInfoCodeList(buildingInfo.geteCodeFromConstruction());

            String hql3 = tgxy_EscrowAgreementDao.getBasicHQL3();

            tgxy_EscrowAgreement = tgxy_EscrowAgreementDao.findOneByQuery_T(tgxy_EscrowAgreementDao.getQuery(hql3, escrowAgreementForm));
            if (tgxy_EscrowAgreement != null) {
                attachmentForm.setSourceId(tgxy_EscrowAgreement.getTableId() + "");
            }
//
//            System.out.println("sourceId"+attachmentForm.getSourceId());
            Sm_Attachment attachment = attacmentDao.findOneByQuery_T(attacmentDao.getQuery(attacmentDao.getBasicHQL(), attachmentForm));
            //存在附件
            if (attachment != null) {
                smAttachments = new ArrayList<String>();
                if (StringUtils.isNotBlank(attachment.getTheLink())) {
                    smAttachments.add(attachment.getTheLink().replace(replaceUrl, baseParameter.getTheName()));
                }
            }
            detail.setSmAttachmentCfgList(smAttachments);
            list.add(detail);
        }
        properties.put("detailList", list);

        Tgpf_SocketMsg tgpf_SocketMsg1 = new Tgpf_SocketMsg();
        tgpf_SocketMsg1.setTheState(S_TheState.Normal);// 状态：正常
        tgpf_SocketMsg1.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
        tgpf_SocketMsg1.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
        tgpf_SocketMsg1.setMsgStatus(1);// 发送状态
        tgpf_SocketMsg1.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
        //公积金提交数据到正泰
        tgpf_SocketMsg1.setMsgDirection("ZT_TO_GJJ_001");// 报文方向
        tgpf_SocketMsg1.setMsgContentArchives(new JsonUtil().propertiesToJson(properties));// 报文内容
        tgpf_SocketMsg1.setReturnCode("200");// 返回码
        tgpf_SocketMsgDao.save(tgpf_SocketMsg1);


        return properties;
    }


    //查出公符合积金提供参数的对应数据并对其更新公积金id
    public synchronized Properties getApproveInfo(HttpServletRequest request, JSONObject obj) {
        Properties properties = new Properties();
        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        if (obj.getString("tableId") == null) {
            return MyBackInfo.fail(properties, "请输入楼幢id");
        }
        if (obj.getString("gjjTableId") == null) {
            return MyBackInfo.fail(properties, "请输入公积金楼幢id");
        }

        String json = obj.toJSONString();
        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
        tgpf_SocketMsg.setMsgStatus(1);// 发送状态
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
        //公积金提交数据到正泰
        tgpf_SocketMsg.setMsgDirection("GJJ_TO_ZT_002");// 报文方向
        tgpf_SocketMsg.setMsgContentArchives(json);// 报文内容
        tgpf_SocketMsg.setReturnCode("200");// 返回码
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        Empj_BuildingInfo obj1 = JSONObject.parseObject(obj.toString(), Empj_BuildingInfo.class);

        Empj_BuildingInfo empj_buildingInfo = empj_BuildingInfoDao.findById(Long.valueOf(obj1.getTableId()));
        if (null == empj_buildingInfo) {
            return MyBackInfo.fail(properties, "未查询到有效的楼幢信息！");
        }

        empj_buildingInfo.setGjjState("0");
        empj_buildingInfo.setGjjTableId(obj1.getGjjTableId());
        empj_BuildingInfoDao.update(empj_buildingInfo);


        //托管楼栋对应多个公积金楼栋id
//        Gjj_BuildingForm form = new Gjj_BuildingForm();
//        form.setGjjBuildingId(obj1.getGjjTableId());
//        form.setEmpjBuildingId(obj1.getTableId()+"");
//        List<Gjj_BulidingRelation> relations = gjj_bulidingRelationDao.findByPage(gjj_bulidingRelationDao.getQuery(gjj_bulidingRelationDao.getBasicHQL(), form));
//        //如果关系不存在  新增
//        Gjj_BulidingRelation addrelation = new Gjj_BulidingRelation();
//        if(relations == null || relations.size() <= 0){
//            addrelation.setGjjBuildingId(obj1.getGjjTableId());
//            addrelation.setEmpjBuildingId(obj1.getTableId()+"");
//            gjj_bulidingRelationDao.save(addrelation);
//        }

        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg1 = new Tgpf_SocketMsg();
        tgpf_SocketMsg1.setTheState(S_TheState.Normal);// 状态：正常
        tgpf_SocketMsg1.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
        tgpf_SocketMsg1.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
        tgpf_SocketMsg1.setMsgStatus(1);// 发送状态
        tgpf_SocketMsg1.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
        //公积金提交数据到正泰
        tgpf_SocketMsg1.setMsgDirection("ZT_TO_GJJ_002");// 报文方向
        tgpf_SocketMsg1.setMsgContentArchives(new JsonUtil().propertiesToJson(properties));// 报文内容
        tgpf_SocketMsg1.setReturnCode("200");// 返回码
        tgpf_SocketMsgDao.save(tgpf_SocketMsg1);

        return properties;
    }


    //每月初第一个工作日，中心将上月进入可放款审批流程的楼栋信息推送给正泰（正泰标注状态，被标记的楼幢不需再做三这个接口推送）
    public synchronized Properties getLoanApproveInfo(HttpServletRequest request, JSONObject obj) {
        Properties properties = new Properties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        String json = obj.toJSONString();
        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
        tgpf_SocketMsg.setMsgStatus(1);// 发送状态
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
        //公积金提交数据到正泰
        tgpf_SocketMsg.setMsgDirection("GJJ_TO_ZT_004");// 报文方向
        tgpf_SocketMsg.setMsgContentArchives(json);// 报文内容
        tgpf_SocketMsg.setReturnCode("200");// 返回码
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        JSONArray data = obj.getJSONArray("data");
        List<Empj_BuildingInfo> list = JSONObject.parseArray(data.toString(), Empj_BuildingInfo.class);
        Empj_BuildingInfoForm empj_buildingInfoForm = new Empj_BuildingInfoForm();


        for (Empj_BuildingInfo empj_buildingInfo : list) {
            if (empj_buildingInfo.getGjjTableId() != null) {
                empj_buildingInfoForm.setGjjTableId(empj_buildingInfo.getGjjTableId());
                try {
                    Empj_BuildingInfo empj_buildingInfo1 = (Empj_BuildingInfo) empj_BuildingInfoDao.findOneByQuery(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), empj_buildingInfoForm));
                    if (empj_buildingInfo1.getTheState() == 0) {
                        empj_buildingInfo1.setGjjState("1");
                        empj_buildingInfo1.setApproveMonth(empj_buildingInfo.getApproveMonth());
                        empj_BuildingInfoDao.update(empj_buildingInfo1);
                    } else {
                        return MyBackInfo.fail(properties, "楼幢的状态处于失效状态");
                    }
                } catch (NullPointerException ex) {
                    return MyBackInfo.fail(properties, "请输入正确公积金楼幢id");
                }

            } else {
                return MyBackInfo.fail(properties, "请输入公积金楼幢id");
            }
        }


        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg1 = new Tgpf_SocketMsg();
        tgpf_SocketMsg1.setTheState(S_TheState.Normal);// 状态：正常
        tgpf_SocketMsg1.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
        tgpf_SocketMsg1.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
        tgpf_SocketMsg1.setMsgStatus(1);// 发送状态
        tgpf_SocketMsg1.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
        //公积金提交数据到正泰
        tgpf_SocketMsg1.setMsgDirection("ZT_TO_GJJ_004");// 报文方向
        tgpf_SocketMsg1.setMsgContentArchives(new JsonUtil().propertiesToJson(properties));// 报文内容
        tgpf_SocketMsg1.setReturnCode("200");// 返回码
        tgpf_SocketMsgDao.save(tgpf_SocketMsg1);

        return properties;
    }


    //    中心贷款终审后将贷款申请信息推送给正泰（每天）
    public synchronized Properties getFinalReviewOfLoanByDay(HttpServletRequest request, JSONObject obj) {
        Properties properties = new Properties();

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        String json = obj.toJSONString();
        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);// 状态：正常
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
        tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
        tgpf_SocketMsg.setMsgStatus(1);// 发送状态
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
        //公积金提交数据到正泰
        tgpf_SocketMsg.setMsgDirection("GJJ_TO_ZT_005");// 报文方向
        tgpf_SocketMsg.setMsgContentArchives(json);// 报文内容
        tgpf_SocketMsg.setReturnCode("200");// 返回码
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);
//        Tgxy_TripleAgreementForm tgxy_tripleAgreementForm=new Tgxy_TripleAgreementForm();


        JSONArray data = obj.getJSONArray("data");
        List<GJJ_LoanInforMation> list = JSONObject.parseArray(data.toString(), GJJ_LoanInforMation.class);

        for (GJJ_LoanInforMation gjj_loanInforMation : list) {

            if (gjj_loanInforMation != null) {
                gjj_loanInforMationDao.save(gjj_loanInforMation);
            }

//            if (gjj_loanInforMation.getEcodeofcontractrecord()!=null){
//                tgxy_tripleAgreementForm.seteCodeOfContractRecord(gjj_loanInforMation.getEcodeofcontractrecord());
//                Tgxy_TripleAgreement array=(Tgxy_TripleAgreement)tgxy_tripleAgreementDao.findOneByQuery(tgxy_tripleAgreementDao.getQuery(tgxy_tripleAgreementDao.getBasicHQL(),tgxy_tripleAgreementForm));
//                if (array!=null){
//                    gjj_loanInforMationDao.save(gjj_loanInforMation);
//                }else {
//                    return  MyBackInfo.fail(properties, "合同备案编号出现错误");
//                }
//
//            }else {
//                return  MyBackInfo.fail(properties, "请输入合同备案编号");
//
//            }
        }


        // 记录接口交互信息
        Tgpf_SocketMsg tgpf_SocketMsg1 = new Tgpf_SocketMsg();
        tgpf_SocketMsg1.setTheState(S_TheState.Normal);// 状态：正常
        tgpf_SocketMsg1.setCreateTimeStamp(System.currentTimeMillis());// 创建时间
        tgpf_SocketMsg1.setLastUpdateTimeStamp(System.currentTimeMillis());// 最后修改日期
        tgpf_SocketMsg1.setMsgStatus(1);// 发送状态
        tgpf_SocketMsg1.setMsgTimeStamp(System.currentTimeMillis());// 发生时间
        //公积金提交数据到正泰
        tgpf_SocketMsg1.setMsgDirection("ZT_TO_GJJ_005");// 报文方向
        tgpf_SocketMsg1.setMsgContentArchives(new JsonUtil().propertiesToJson(properties));// 报文内容
        tgpf_SocketMsg1.setReturnCode("200");// 返回码
        tgpf_SocketMsgDao.save(tgpf_SocketMsg1);

        return properties;
    }

    public Properties PushApprovalInfo(Long tableId) {
        Properties pro = new MyProperties();


        return pro;
    }
}

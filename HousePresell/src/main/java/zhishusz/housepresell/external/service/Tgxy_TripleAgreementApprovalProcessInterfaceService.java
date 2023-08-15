package zhishusz.housepresell.external.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.BaseForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_HouseInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreement;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.external.po.TripleAgreementModel;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.SocketUtil;

/**
 * 三方协议提交接口
 * 
 * @author Administrator
 *
 */
@Service
@Transactional
public class Tgxy_TripleAgreementApprovalProcessInterfaceService {
    // 附件
    @Autowired
    private Sm_AttachmentDao sm_AttachmentDao;
    /*@Autowired
    private Sm_AttachmentCfgDao sm_AttachmentCfgDao;*/
    // 基础参数
    @Autowired
    private Sm_BaseParameterDao sm_BaseParameterDao;
    // 接口报文表
    /*@Autowired
    private Tgpf_SocketMsgDao tgpf_SocketMsgDao;*/

    public static Logger log = Logger.getLogger(Tgxy_TripleAgreementApprovalProcessInterfaceService.class);

    @SuppressWarnings({"unchecked", "static-access"})
    public Properties execute(Tgxy_TripleAgreement tripleAgreement, BaseForm from) {
        Properties properties = new MyProperties();

        /*htbh	String	必填	合同编号
        xybh	String	必填	协议编号
        qymc	String	必填	企业名称
        xmmc	String	必填	项目名称
        msrxm	String	必填	买受人姓名
        sgzl	String	必填	施工座落
        jzmj	String	必填	建筑面积
        fjm	String	必填	附件*/

        // 基础信息
        Empj_ProjectInfo project = tripleAgreement.getProject();
        Emmp_CompanyInfo developCompany = project.getDevelopCompany();
        Empj_HouseInfo house = tripleAgreement.getHouse();

        log.info("推送SFXY附件信息START：" + System.currentTimeMillis());
        System.out.println("推送SFXY附件信息START：" + System.currentTimeMillis());
        
        // 记录接口交互信息
        /*Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
        tgpf_SocketMsg.setTheState(S_TheState.Normal);
        tgpf_SocketMsg.setUserStart(from.getUser());
        tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setUserUpdate(from.getUser());
        tgpf_SocketMsg.setMsgStatus(1);
        tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setMsgDirection("DAOUT");
        tgpf_SocketMsgDao.save(tgpf_SocketMsg);*/

        /*
         * 单据附件信息
         */
        Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
        sm_AttachmentForm.setSourceId(String.valueOf(tripleAgreement.getTableId()));
        sm_AttachmentForm.setBusiType("06110301");
        sm_AttachmentForm.setTheState(S_TheState.Normal);

        // 加载所有相关附件信息
        List<Sm_Attachment> sm_AttachmentList =
            sm_AttachmentDao.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));

        /*
         * 除“买卖合同签字页”的附件，其他附件拼接推送
         */
        StringBuffer sb = new StringBuffer();
        for (Sm_Attachment sm_Attachment : sm_AttachmentList) {
            if (null != sm_Attachment.getAttachmentCfg() && null != sm_Attachment.getAttachmentCfg().getTheName()) {
                sb.append(sm_Attachment.getAttachmentCfg().getTheName() + "#" + sm_Attachment.getTheLink() + ",");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        log.info("推送SFXY附件信息END：" + System.currentTimeMillis());
        System.out.println("推送SFXY附件信息END：" + System.currentTimeMillis());

        TripleAgreementModel model = new TripleAgreementModel();
        model.setHtbh(tripleAgreement.geteCodeOfContractRecord());
        model.setXybh(tripleAgreement.geteCodeOfTripleAgreement());
        model.setQymc(developCompany.getTheName());
        model.setXmmc(project.getTheName());
        model.setSgzl(house.getPosition());
        model.setJzmj(house.getActualArea().toString());
        model.setFjm(sb.toString());
        model.setMsrxm(tripleAgreement.getBuyerName());

        /*
         *  参数名	 类型	描述  Int  
         *  返回值“1”表示数据插入成功
         *  返回值“0”表示数据插入异常
         */
        String query = model.toStringAdd();

        Sm_BaseParameterForm paraModel = new Sm_BaseParameterForm();
        paraModel.setParametertype("70");
        paraModel.setTheValue("700001");
        List<Sm_BaseParameter> list = new ArrayList<>();
        list =
            sm_BaseParameterDao.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), paraModel));

        if (list.isEmpty()) {
            return MyBackInfo.fail(properties, "未查询到相应的请求接口，请查询基础参数是否正确！");
        }

        String url = list.get(0).getTheName();

        log.info("推送SFXY========START：" + System.currentTimeMillis());
        System.out.println("推送SFXY========START：" + System.currentTimeMillis());
        // 正式接口请求
        int restFul = SocketUtil.getInstance().getRestFul(url, query);

        log.info("推送SFXY========END：" + System.currentTimeMillis());
        System.out.println("推送SFXY========END：" + System.currentTimeMillis());

        
        /*tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
        tgpf_SocketMsg.setRemark(url);
        tgpf_SocketMsg.setMsgContentArchives(query);
        tgpf_SocketMsg.setReturnCode(String.valueOf(restFul));
        tgpf_SocketMsgDao.update(tgpf_SocketMsg);*/
        
        log.info("query:" + query);
//        log.info("tgpf_SocketMsg:" + tgpf_SocketMsg.toString());

        if (restFul == 0) {
            return MyBackInfo.fail(properties, "提交异常，请稍后再试！");
        }

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;

    }

    @SuppressWarnings("static-access")
    public Properties testService() {

        Properties properties = new MyProperties();

        log.info("推送SFXY========START：" + System.currentTimeMillis());
        System.out.println("推送SFXY========START：" + System.currentTimeMillis());
        // 正式接口请求
        int restFul = SocketUtil.getInstance().getRestFul(
            "http://172.18.5.251:8082/ws/services/myservice/services/insertSfXy",
            "htbh=000078&xybh=123456789012345&qymc=sdsd&xmmc=金梅花园&msrxm=gsx&sgzl=100&jzmj=200&fj=d:/work/pic/201906191120.pdf");
        properties.put("restFul", restFul);

        log.info("推送SFXY========END：" + System.currentTimeMillis());
        System.out.println("推送SFXY========END：" + System.currentTimeMillis());

        return properties;

    }
}

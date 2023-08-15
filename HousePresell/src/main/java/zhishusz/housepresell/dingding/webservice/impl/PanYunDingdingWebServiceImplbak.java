package zhishusz.housepresell.dingding.webservice.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmountForm;
import zhishusz.housepresell.controller.form.Empj_BldLimitAmount_DtlForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.dao.*;
import zhishusz.housepresell.database.po.*;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.dingding.entity.PanYunDingDingSavePhotoUrlsEntity;
import zhishusz.housepresell.dingding.entity.PanYunDingDingSavePhotoUrlsSonEntity;
import zhishusz.housepresell.dingding.entity.PanYunDingDingSaveSignUrlsEntity;
import zhishusz.housepresell.dingding.webservice.PanYunDingdingWebService;

import javax.jws.WebService;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 攀云钉钉保存图片webService接口实现类
 * @Author jxx
 * @Date 2020/9/27 10:50
 * @Version
 **/
@WebService(endpointInterface = "zhishusz.housepresell.dingding.webservice.PanYunDingdingWebService")
@Transactional
public class PanYunDingdingWebServiceImplbak implements PanYunDingdingWebService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PanYunDingdingWebServiceImplbak.class);

	@Autowired
	private Empj_BldLimitAmountDao empj_BldLimitAmountDao;

	@Autowired
	private Empj_BldLimitAmount_DtlDao empj_BldLimitAmount_DtlDao;

	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;

	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;

	@Autowired
	private Tgpf_SocketMsgDao tgpf_SocketMsgDao;

	@Override
	public String savePhotoUrls(String result) {
		// 记录接口交互信息   记录返回数据
		Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
		tgpf_SocketMsg.setTheState(S_TheState.Normal);
		tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
		tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpf_SocketMsg.setMsgStatus(1);
		tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
		tgpf_SocketMsg.setMsgDirection("--PanYunDingDingWebServicepicUrls-savePhotoUrls");
		tgpf_SocketMsg.setMsgContentArchives(result);
		tgpf_SocketMsg.setReturnCode("200");
		tgpf_SocketMsgDao.save(tgpf_SocketMsg);

		System.out.println("PanYunDingDingWebService savePhotoUrls: " + result);
		System.out
				.println("-----------------------PanYunDingDingWebService savePhotoUrls Start-----------------------");
		Map<String, Object> params = new HashMap<>();
		if (StringUtils.isBlank(result)) {
			params.put("code", 100);
			params.put("message", "result is Blank");
			System.out.println("-------result is Blank-PanYunDingDingWebService savePhotoUrls End-----------------------");
			return JSONObject.toJSONString(params);
		}
		PanYunDingDingSavePhotoUrlsEntity savePhotoUrlsEntity = JSONObject.parseObject(result,
				PanYunDingDingSavePhotoUrlsEntity.class);
		System.out.println("taskId" + savePhotoUrlsEntity.getTaskId());
		if (null == savePhotoUrlsEntity || StringUtils.isBlank(savePhotoUrlsEntity.getTaskId())) {
			params.put("code", 101);
			params.put("message", "Params taskId is Blank");
			System.out.println(
					"-------Params taskId is Blank-PanYunDingDingWebService savePhotoUrls End----------------");
			return JSONObject.toJSONString(params);
		}
		List<PanYunDingDingSavePhotoUrlsSonEntity> imageInfos = savePhotoUrlsEntity.getEntities();
		if (null == imageInfos || imageInfos.isEmpty()) {
			params.put("code", 102);
			params.put("message", "Params image infos is Blank");
			System.out.println(
					"-------Params image infos is Blank-PanYunDingDingWebService savePhotoUrls End------------");
			return JSONObject.toJSONString(params);
		}
		Map<String, PanYunDingDingSavePhotoUrlsSonEntity> imageInfoMap = new HashMap<>(imageInfos.size());
		for (PanYunDingDingSavePhotoUrlsSonEntity imageInfo : imageInfos) {
			if (StringUtils.isBlank(imageInfo.getBuildingCode())) {
				params.put("code", 103);
				params.put("message", "Params buildCode is Blank");
				System.out.println(
						"-------Params buildCode is Blank-PanYunDingDingWebService savePhotoUrls End------------");
				return JSONObject.toJSONString(params);
			}
			imageInfoMap.put(imageInfo.getBuildingCode(), imageInfo);
		}
		Empj_BldLimitAmountForm model = new Empj_BldLimitAmountForm();
		model.setTheState(0);
		model.seteCode(savePhotoUrlsEntity.getTaskId());
		Empj_BldLimitAmount bldLimitAmount = empj_BldLimitAmountDao
				.findOneByQuery_T(empj_BldLimitAmountDao.getQuery(empj_BldLimitAmountDao.getBasicHQL(), model));
		if (null == bldLimitAmount) {
			params.put("code", 400);
			params.put("message", "TaskId is not exist");
			System.out.println(
					"-------TaskId is not exist-PanYunDingDingWebService savePhotoUrls End-----------------------");
			return JSONObject.toJSONString(params);
		}

		// 确定上传的监理公司，确定上传附件类型
		String supervisorCode = savePhotoUrlsEntity.getSupervisorCode();
		if (StringUtils.isBlank(supervisorCode)) {
			params.put("code", 104);
			params.put("message", "Params supervisorCode is Blank");
			System.out.println(
					"-------Params supervisorCode is Blank-PanYunDingDingWebService savePhotoUrls End------------");
			return JSONObject.toJSONString(params);
		}

		String companyOneCode = bldLimitAmount.getCompanyOne().geteCode();
		String companyTwoCode = bldLimitAmount.getCompanyTwo().geteCode();
		Sm_AttachmentCfgForm cfgForm = new Sm_AttachmentCfgForm();
		cfgForm.setTheState(S_TheState.Normal);
		cfgForm.setBusiType("03030101");
		if (supervisorCode.equals(companyOneCode)) {
			cfgForm.setTheName("实勘图片A");
			bldLimitAmount.setUploadOne(savePhotoUrlsEntity.getSupervisorName());
			bldLimitAmount.setReturnTimeOne(savePhotoUrlsEntity.getReturnTime());
			bldLimitAmount.setAssignTasksTimeOne(savePhotoUrlsEntity.getAssignTasksTime());
			empj_BldLimitAmountDao.update(bldLimitAmount);
		} else if (supervisorCode.equals(companyTwoCode)) {
			cfgForm.setTheName("实勘图片B");
			bldLimitAmount.setUploadTwo(savePhotoUrlsEntity.getSupervisorName());
			bldLimitAmount.setReturnTimeTwo(savePhotoUrlsEntity.getReturnTime());
			bldLimitAmount.setAssignTasksTimeTwo(savePhotoUrlsEntity.getAssignTasksTime());
			empj_BldLimitAmountDao.update(bldLimitAmount);
		} else {
			params.put("code", 105);
			params.put("message", "Not Find SupervisorCompany");
			System.out.println(
					"-------Not Find SupervisorCompany Blank-PanYunDingDingWebService savePhotoUrls End------------");
			return JSONObject.toJSONString(params);
		}
		Sm_AttachmentCfg smAttachmentCfg = smAttachmentCfgDao
				.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), cfgForm));
		if (null == smAttachmentCfg) {
			params.put("code", 201);
			params.put("message", "Not Find AttachmentCfg");
			System.out.println(
					"-------Not Find AttachmentCfg Blank-PanYunDingDingWebService savePhotoUrls End------------");
			return JSONObject.toJSONString(params);
		}

		Empj_BldLimitAmount_DtlForm dtlForm = new Empj_BldLimitAmount_DtlForm();
		dtlForm.setTheState(S_TheState.Normal);
		dtlForm.seteCodeOfMainTable(bldLimitAmount.geteCode());
		dtlForm.setMainTable(bldLimitAmount);
		List<Empj_BldLimitAmount_Dtl> dtlList = empj_BldLimitAmount_DtlDao
				.findByPage(empj_BldLimitAmount_DtlDao.getQuery(empj_BldLimitAmount_DtlDao.getBasicHQL(), dtlForm));

		if (null == dtlList || dtlList.isEmpty()) {
			params.put("code", 300);
			params.put("message", "Building info is not exist");
			System.out.println(
					"-------Building info is not exist-PanYunDingDingWebService savePhotoUrls End----------------");
			return JSONObject.toJSONString(params);
		}
		for (Empj_BldLimitAmount_Dtl dtl : dtlList) {
		    System.out.println("-----------------------PanYunDingDingWebService dtlList Start-----------------------");
			// PanYunDingDingSavePhotoUrlsSonEntity imageInfo =
			// imageInfoMap.get(dtl.geteCode());
			// 保存对应变更楼幢的附件信息
			PanYunDingDingSavePhotoUrlsSonEntity imageInfo = imageInfoMap.get(dtl.geteCodeOfBuilding());
			if (null != imageInfo) {
				List<String> picUrls = imageInfo.getPicUrls();
				Sm_Attachment smAttachment;
				for (String picUrl : picUrls) {
					smAttachment = new Sm_Attachment();
					System.out.println("-----------------------PanYunDingDingWebService picUrls-----------------------");
					smAttachment.setTheState(S_TheState.Normal);
					smAttachment.setCreateTimeStamp(System.currentTimeMillis());
					smAttachment.setLastUpdateTimeStamp(System.currentTimeMillis());
					smAttachment.setRecordTimeStamp(System.currentTimeMillis());
					smAttachment.setSourceType(smAttachmentCfg.geteCode());
					smAttachment.setAttachmentCfg(smAttachmentCfg);
					smAttachment.setBusiType("03030100");
					smAttachment.setSourceId(String.valueOf(dtl.getTableId()));
					smAttachment.setFileType("jpg");
					smAttachment.setTheLink(picUrl);
					smAttachment.setRemark(savePhotoUrlsEntity.getSupervisorName());
					sm_AttachmentDao.save(smAttachment);
				}
			}
			 
			System.out.println("-----------------------PanYunDingDingWebService dtlList Start-----------------------");
			
		}

		params.put("code", 200);
		params.put("message", "Success");


		//记录返回数据

		System.out.println("-----------------------PanYunDingDingWebService savePhotoUrls End-----------------------");
		return JSONObject.toJSONString(params);
	}
	
	@Override
    public String saveSign(String result) {

		// 记录接口交互信息   记录返回数据
		Tgpf_SocketMsg tgpf_SocketMsg = new Tgpf_SocketMsg();
		tgpf_SocketMsg.setTheState(S_TheState.Normal);
		tgpf_SocketMsg.setCreateTimeStamp(System.currentTimeMillis());
		tgpf_SocketMsg.setLastUpdateTimeStamp(System.currentTimeMillis());
		tgpf_SocketMsg.setMsgStatus(1);
		tgpf_SocketMsg.setMsgTimeStamp(System.currentTimeMillis());
		tgpf_SocketMsg.setMsgDirection("--PanYunDingDingWebServicepicUrls-saveSign");
		tgpf_SocketMsg.setMsgContentArchives(result);
		tgpf_SocketMsg.setReturnCode("200");
		tgpf_SocketMsgDao.save(tgpf_SocketMsg);

        System.out.println("PanYunDingDingWebService saveSignUrls: " + result);
        System.out.println("-----------------------PanYunDingDingWebService saveSignUrls Start-----------------------");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isBlank(result)) {
            params.put("code", 100);
            params.put("message", "result is Blank");
            System.out
                .println("-------result is Blank-PanYunDingDingWebService saveSignUrls End-----------------------");
            return JSONObject.toJSONString(params);
        }
        PanYunDingDingSaveSignUrlsEntity saveSignUrlsEntity =
            JSONObject.parseObject(result, PanYunDingDingSaveSignUrlsEntity.class);
        System.out.println("taskId" + saveSignUrlsEntity.getTaskId());
        if (null == saveSignUrlsEntity || StringUtils.isBlank(saveSignUrlsEntity.getTaskId())) {
            params.put("code", 101);
            params.put("message", "Params taskId is Blank");
            System.out
                .println("-------Params taskId is Blank-PanYunDingDingWebService saveSignUrls End----------------");
            return JSONObject.toJSONString(params);
        }

        Empj_BldLimitAmountForm model = new Empj_BldLimitAmountForm();
        model.setTheState(0);
        model.seteCode(saveSignUrlsEntity.getTaskId());
        Empj_BldLimitAmount bldLimitAmount = empj_BldLimitAmountDao
            .findOneByQuery_T(empj_BldLimitAmountDao.getQuery(empj_BldLimitAmountDao.getBasicHQL(), model));
        if (null == bldLimitAmount) {
            params.put("code", 400);
            params.put("message", "TaskId is not exist");
            System.out
                .println("-------TaskId is not exist-PanYunDingDingWebService saveSignUrls End-----------------------");
            return JSONObject.toJSONString(params);
        }

        String supervisorCode = saveSignUrlsEntity.getSupervisorCode();
        if (StringUtils.isBlank(supervisorCode)) {
            params.put("code", 104);
            params.put("message", "Params supervisorCode is Blank");
            System.out
                .println("-------Params supervisorCode is Blank-PanYunDingDingWebService saveSignUrls End------------");
            return JSONObject.toJSONString(params);
        }

        String companyOneCode = bldLimitAmount.getCompanyOne().geteCode();
        String companyTwoCode = bldLimitAmount.getCompanyTwo().geteCode();
        if (supervisorCode.equals(companyOneCode)) {
            bldLimitAmount.setSignTimeOne(saveSignUrlsEntity.getSignTime());
            empj_BldLimitAmountDao.update(bldLimitAmount);
        } else if (supervisorCode.equals(companyTwoCode)) {
            bldLimitAmount.setSignTimeTwo(saveSignUrlsEntity.getSignTime());
            empj_BldLimitAmountDao.update(bldLimitAmount);
        } else {
            params.put("code", 105);
            params.put("message", "Not Find SupervisorCompany");
            System.out.println(
                "-------Not Find SupervisorCompany Blank-PanYunDingDingWebService saveSignUrls End------------");
            return JSONObject.toJSONString(params);
        }

        params.put("code", 200);
        params.put("message", "Success");

        System.out.println("-----------------------PanYunDingDingWebService saveSignUrls End-----------------------");

        return JSONObject.toJSONString(params);
    }
}

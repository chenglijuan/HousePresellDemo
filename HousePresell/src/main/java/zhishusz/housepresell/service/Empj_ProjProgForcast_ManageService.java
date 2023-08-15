package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaominfo.oss.sdk.OSSClientProperty;

import cn.hutool.core.util.StrUtil;
import zhishusz.housepresell.controller.form.Empj_ProjProgForcast_ManageForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_AFDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_DTLDao;
import zhishusz.housepresell.database.dao.Empj_ProjProgForcast_ManageDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_AF;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_DTL;
import zhishusz.housepresell.database.po.Empj_ProjProgForcast_Manage;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg_Copy;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;

/*
 * Service列表查询：工程进度巡查管理 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_ProjProgForcast_ManageService {
	@Autowired
	private Empj_ProjProgForcast_ManageDao empj_ProjProgForcast_ManageDao;
	@Autowired
	private Empj_ProjProgForcast_AFDao empj_ProjProgForcast_AFDao;
	@Autowired
	private Empj_ProjProgForcast_DTLDao empj_ProjProgForcast_DTLDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private CommonService commonService;

	@Autowired
	private Sm_AttachmentCfgRebuild cfgRebuild;

	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;
	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;

	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;

	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;

	@Autowired
	private OssServerUtil ossUtil;

	@Autowired
	private OSSClientProperty oss;

	@Autowired
	private Sm_ApprovalProcess_DeleteService deleteService;

	private static final String excelName = "日常巡查统计报告";
	private static final String excelName_pro = "工程进度巡查";

	// private static final String BUSI_CODE = "03030206";

	private static final String BUSI_CODE = "03030202";

	public Properties execute(Empj_ProjProgForcast_ManageForm model) {
		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties afListExecute(Empj_ProjProgForcast_ManageForm model) {
		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		Sm_User user = model.getUser();
		if (null == user) {
			return MyBackInfo.fail(properties, "请先登录！");
		}

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();

		// 区域
		Long areaId = model.getAreaId();
		if (null == areaId) {
			model.setAreaId(null);
		}

		// 项目
		Long projectId = model.getProjectId();
		if (null == projectId) {
			model.setProjectId(null);
		}

		// 楼幢
		Long buildId = model.getBuildId();
		if (null == buildId) {
			model.setBuildId(null);
		}

		// 网站审核状态
		String webHandelState = model.getWebHandelState();
		if (StrUtil.isBlank(webHandelState)) {
			model.setWebHandelState(null);
		}

		// 网站推送状态
		String webPushState = model.getWebPushState();
		if (StrUtil.isBlank(webPushState)) {
			model.setWebPushState(null);
		}

		// 单据状态
		String approvalState = model.getApprovalState();
		if (StringUtils.isBlank(approvalState)) {
			model.setApprovalState(null);
		}

		if (StringUtils.isBlank(keyword)) {
			model.setKeyword(null);
		} else {
			model.setKeyword("%" + keyword + "%");
		}

		model.setTheState(S_TheState.Normal);
		Integer totalCount = empj_ProjProgForcast_ManageDao.findByPage_Size(
				empj_ProjProgForcast_ManageDao.getQuery_Size(empj_ProjProgForcast_ManageDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Empj_ProjProgForcast_Manage> empj_ProjProgForcast_ManageList;
		if (totalCount > 0) {
			empj_ProjProgForcast_ManageList = empj_ProjProgForcast_ManageDao.findByPage(
					empj_ProjProgForcast_ManageDao.getQuery(empj_ProjProgForcast_ManageDao.getBasicHQL(), model),
					pageNumber, countPerPage);
		} else {
			empj_ProjProgForcast_ManageList = new ArrayList<>();
		}

		List<Properties> list = new ArrayList<>();
		Properties pro;
		for (Empj_ProjProgForcast_Manage manage : empj_ProjProgForcast_ManageList) {
			pro = new MyProperties();
			pro.put("tableId", manage.getTableId());
			pro.put("afId", manage.getAfEntity().getTableId());
			pro.put("afCode", manage.getCode());
			pro.put("areaName", manage.getAreaName());
			pro.put("projectName", manage.getProjectName());
			pro.put("buildCode", StrUtil.isBlank(manage.getBuildCode()) ? "" : manage.getBuildCode());
			pro.put("webPushState", manage.getWebPushState());
			pro.put("webHandelState", manage.getWebHandelState());
			pro.put("forcastTime", manage.getForcastTime());
			pro.put("forcastPeople", manage.getForcastPeople());
			pro.put("submitDate", manage.getSubmitDate());
			pro.put("approvalState", manage.getApprovalState());

			pro.put("projectId", manage.getProject().getTableId());
			pro.put("dtlId", null == manage.getDtlEntity() ? "" : manage.getDtlEntity().getTableId());
			pro.put("buildId", null == manage.getBuildingInfo() ? "" : manage.getBuildingInfo().getTableId());

			pro.put("level", null == manage.getDtlEntity() ? "project" : "build");

			list.add(pro);

		}

		properties.put("empj_ProjProgForcast_ManageList", list);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		return properties;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Properties detailExecute(Empj_ProjProgForcast_ManageForm model) {
		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		Long tableId = model.getTableId();
		if (null == tableId) {
			return MyBackInfo.fail(properties, "请选择查看的单据！");
		}

		Empj_ProjProgForcast_Manage manage = empj_ProjProgForcast_ManageDao.findById(tableId);
		if (null == manage) {
			return MyBackInfo.fail(properties, "单据信息已失效，请刷新后重试！");
		}

		Properties pro = new MyProperties();
		pro.put("tableId", manage.getTableId());
		pro.put("afId", manage.getAfEntity().getTableId());
		pro.put("afCode", manage.getCode());
		pro.put("areaName", manage.getAreaName());
		pro.put("projectName", manage.getProjectName());
		pro.put("buildCode", StrUtil.isBlank(manage.getBuildCode()) ? "" : manage.getBuildCode());
		pro.put("webPushState", manage.getWebPushState());
		pro.put("webHandelState", manage.getWebHandelState());
		pro.put("forcastTime", manage.getForcastTime());
		pro.put("forcastPeople", manage.getForcastPeople());
		pro.put("submitDate", manage.getSubmitDate());
		pro.put("approvalState", manage.getApprovalState());
		pro.put("updateName", manage.getUserUpdate().getTheName());
		pro.put("updateTime", MyDatetime.getInstance().dateToString2(manage.getLastUpdateTimeStamp()));

		pro.put("projectId", manage.getProject().getTableId());
		pro.put("dtlId", null == manage.getDtlEntity() ? "" : manage.getDtlEntity().getTableId());
		pro.put("buildId", null == manage.getBuildingInfo() ? "" : manage.getBuildingInfo().getTableId());
		pro.put("webHandelInfo", StrUtil.isBlank(manage.getWebHandelInfo()) ? "" : manage.getWebHandelInfo());

		pro.put("level", null == manage.getDtlEntity() ? "project" : "build");

		properties.put("empj_PaymentBond_Manage", pro);

		List detailForAdmin = new ArrayList<>();
		// 如果是楼幢
		if (null != manage.getDtlEntity()) {
			Sm_AttachmentForm sm_AttachmentForm;
			List<Sm_Attachment> sm_AttachmentList;
			// 查询同一附件类型下的所有附件信息（附件信息归类）
			List<Sm_Attachment> smList = null;
			List<Sm_AttachmentCfg_Copy> smAttachmentCfgList;
			Sm_AttachmentCfg_Copy copy;

			Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
			form.setBusiType(BUSI_CODE);
			form.setTheState(S_TheState.Normal);
			List<Sm_AttachmentCfg> smAttachmentCfgList_Copy = smAttachmentCfgDao
					.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

			smAttachmentCfgList = new ArrayList<>();
			for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList_Copy) {
				copy = new Sm_AttachmentCfg_Copy();
				BeanUtils.copyProperties(sm_AttachmentCfg, copy);

				smAttachmentCfgList.add(copy);

			}

			sm_AttachmentForm = new Sm_AttachmentForm();
			sm_AttachmentForm.setSourceId(manage.getDtlEntity().getTableId().toString());
			sm_AttachmentForm.setBusiType(BUSI_CODE);
			sm_AttachmentForm.setTheState(S_TheState.Normal);

			// 加载所有楼幢下的相关附件信息
			sm_AttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
			if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
				sm_AttachmentList = new ArrayList<Sm_Attachment>();
			}

			for (Sm_Attachment sm_Attachment : sm_AttachmentList) {
				String sourceType = sm_Attachment.getSourceType();
				for (Sm_AttachmentCfg_Copy sm_AttachmentCfg : smAttachmentCfgList) {
					if (sm_AttachmentCfg.geteCode().equals(sourceType)) {
						smList = sm_AttachmentCfg.getSmAttachmentList();
						if (null == smList || smList.size() == 0) {
							smList = new ArrayList<Sm_Attachment>();
						}
						smList.add(sm_Attachment);
						sm_AttachmentCfg.setSmAttachmentList(smList);
					}
				}
			}

			/*
			 * 附件信息
			 */
			detailForAdmin = cfgRebuild.getDetailForAdmin3(smAttachmentCfgList);

		}
		properties.put("attachementList", detailForAdmin);

		return properties;
	}

	public Properties submitExecute(Empj_ProjProgForcast_ManageForm model) {
		Properties properties = new MyProperties();

		long nowTimeStamp = System.currentTimeMillis();
		Sm_User user = model.getUser();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		Long tableId = model.getTableId();
		if (null == tableId) {
			return MyBackInfo.fail(properties, "请选择提交的单据！");
		}

		Empj_ProjProgForcast_Manage manage = empj_ProjProgForcast_ManageDao.findById(tableId);
		if (null == manage) {
			return MyBackInfo.fail(properties, "单据信息已失效，请刷新后重试！");
		}

		// 区分是项目提交还是楼幢提交
		if (null == manage.getDtlEntity()) {
			// 项目提交
			Empj_ProjProgForcast_AF forcast_AF = manage.getAfEntity();
			manage.setSubmitDate(MyDatetime.getInstance().dateToString2(nowTimeStamp));
			manage.setLastUpdateTimeStamp(nowTimeStamp);
			manage.setUserUpdate(user);
			manage.setWebHandelState("-1");
			manage.setApprovalState(S_ApprovalState.Examining);

			forcast_AF.setLastUpdateTimeStamp(nowTimeStamp);
			forcast_AF.setUserUpdate(user);
			forcast_AF.setApprovalState(S_ApprovalState.Examining);
			forcast_AF.setHandleState("0");
			forcast_AF.setWebHandelState("-1");

			Boolean pushProjProgProject = commonService.pushProjProgProject(forcast_AF);
			if (pushProjProgProject) {
				manage.setWebPushState("2");
				manage.setApprovalState(S_ApprovalState.Examining);
				// 已推送
				forcast_AF.setWebPushState("2");
			} else {
				manage.setWebPushState("1");
				// 推送中
				forcast_AF.setWebPushState("1");
			}
			empj_ProjProgForcast_ManageDao.update(manage);
			empj_ProjProgForcast_AFDao.update(forcast_AF);

		} else {
			// 楼幢提交
			Empj_ProjProgForcast_DTL dtlEntity = manage.getDtlEntity();

			manage.setSubmitDate(MyDatetime.getInstance().dateToString2(nowTimeStamp));
			manage.setLastUpdateTimeStamp(nowTimeStamp);
			manage.setUserUpdate(user);
			manage.setWebPushState("1");
			manage.setApprovalState(S_ApprovalState.Examining);
			empj_ProjProgForcast_ManageDao.update(manage);

			dtlEntity.setLastUpdateTimeStamp(nowTimeStamp);
			dtlEntity.setUserUpdate(user);
			dtlEntity.setApprovalState(S_ApprovalState.Examining);
			dtlEntity.setWebPushState("1");
			empj_ProjProgForcast_DTLDao.update(dtlEntity);

		}

		return properties;
	}

	public Properties buildSubmitExecute(Empj_ProjProgForcast_ManageForm model) {
		Properties properties = new MyProperties();

		long nowTimeStamp = System.currentTimeMillis();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		Long tableId = model.getTableId();
		if (null == tableId) {
			return MyBackInfo.fail(properties, "请选择提交的单据！");
		}

		Empj_ProjProgForcast_Manage manage = empj_ProjProgForcast_ManageDao.findById(tableId);
		if (null == manage) {
			return MyBackInfo.fail(properties, "单据信息已失效，请刷新后重试！");
		}

		// 楼幢提交
		Empj_ProjProgForcast_DTL dtlEntity = manage.getDtlEntity();

		commonService.pushSumitBuildProjProgForcast(manage.getAfEntity(), dtlEntity);

		manage.setSubmitDate(MyDatetime.getInstance().dateToString2(nowTimeStamp));
		manage.setLastUpdateTimeStamp(nowTimeStamp);
		manage.setWebHandelState("-1");
		manage.setWebPushState("2");
		manage.setApprovalState(S_ApprovalState.Examining);
		empj_ProjProgForcast_ManageDao.update(manage);

		dtlEntity.setLastUpdateTimeStamp(nowTimeStamp);
		dtlEntity.setApprovalState(S_ApprovalState.Examining);
		dtlEntity.setHandleState("-1");
		dtlEntity.setWebPushState("2");
		empj_ProjProgForcast_DTLDao.update(dtlEntity);

		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties saveExecute(Empj_ProjProgForcast_ManageForm model) {
		Properties properties = new MyProperties();

		long nowTimeStamp = System.currentTimeMillis();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		Long tableId = model.getTableId();
		if (null == tableId) {
			return MyBackInfo.fail(properties, "请选择提交的单据！");
		}

		Empj_ProjProgForcast_Manage manage = empj_ProjProgForcast_ManageDao.findById(tableId);
		if (null == manage) {
			return MyBackInfo.fail(properties, "单据信息已失效，请刷新后重试！");
		}

		Empj_ProjProgForcast_DTL dtlEntity = manage.getDtlEntity();
		if (null != dtlEntity) {
			List<Map<String, Object>> attachementList = model.getAttachementList();
			if (attachementList.size() < 4) {
				return MyBackInfo.fail(properties, "请上传足够的附件信息！");
			}

			System.out.println(attachementList.toString());

			Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
			sm_AttachmentForm.setSourceId(dtlEntity.getTableId().toString());
			sm_AttachmentForm.setBusiType(BUSI_CODE);
			sm_AttachmentForm.setTheState(S_TheState.Normal);

			// 删除原附件信息
			List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
			for (Sm_Attachment sm_Attachment : sm_AttachmentList) {
				sm_Attachment.setTheState(S_TheState.Deleted);
				sm_Attachment.setUserUpdate(model.getUser());
				sm_Attachment.setLastUpdateTimeStamp(nowTimeStamp);
				sm_AttachmentDao.update(sm_Attachment);
			}

			Sm_Attachment attachment;
			Sm_AttachmentCfgForm form;
			Sm_AttachmentCfg sm_AttachmentCfg;
			// 保存新附件信息
			for (Map<String, Object> map1 : attachementList) {
				attachment = new Sm_Attachment();
				// 查询附件配置表
				form = new Sm_AttachmentCfgForm();
				form.seteCode((String) map1.get("sourceType"));
				sm_AttachmentCfg = smAttachmentCfgDao
						.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

				attachment.setTheLink((String) (null == map1.get("theLink") ? "" : map1.get("theLink")));
				attachment.setTheSize((String) (null == map1.get("theSize") ? "" : map1.get("theSize")));
				attachment.setFileType((String) (null == map1.get("fileType") ? "" : map1.get("fileType")));
				attachment.setSourceType((String) (null == map1.get("sourceType") ? "" : map1.get("sourceType")));
				attachment.setAttachmentCfg(sm_AttachmentCfg);
				attachment.setSourceId(dtlEntity.getTableId().toString());
				attachment.setBusiType(BUSI_CODE);
				attachment.setTheState(S_TheState.Normal);
				attachment.setRemark((String) (null == map1.get("remark") ? "" : map1.get("remark")));
				attachment.setUserStart(model.getUser());
				attachment.setUserUpdate(model.getUser());
				attachment.setCreateTimeStamp(nowTimeStamp);
				attachment.setLastUpdateTimeStamp(nowTimeStamp);
				sm_AttachmentDao.save(attachment);
			}

		}

		return properties;
	}

}

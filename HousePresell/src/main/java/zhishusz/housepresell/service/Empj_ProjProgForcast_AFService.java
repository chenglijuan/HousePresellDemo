package zhishusz.housepresell.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaominfo.oss.sdk.OSSClientProperty;
import com.xiaominfo.oss.sdk.ReceiveMessage;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.Empj_ProjProgForcast_AFForm;
import zhishusz.housepresell.controller.form.Empj_ProjProgForcast_DTLForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
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
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg_Copy;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.exportexcelvo.Empj_ProjProgInspectionExcelVO;
import zhishusz.housepresell.exportexcelvo.Empj_ProjProgInspectionReportExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import zhishusz.housepresell.util.picture.MatrixUtil;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgDataInfo;
import zhishusz.housepresell.util.rebuild.Sm_AttachmentCfgRebuild;

/*
 * Service列表查询：工程进度巡查-主 Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_ProjProgForcast_AFService {
	@Autowired
	private Empj_ProjProgForcast_AFDao empj_ProjProgForcast_AFDao;
	@Autowired
	private Empj_ProjProgForcast_DTLDao empj_ProjProgForcast_DTLDao;
	@Autowired
	private Empj_ProjProgForcast_ManageDao empj_ProjProgForcast_ManageDao;
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

	private static final String BUSI_CODE = "03030202";

	public Properties execute(Empj_ProjProgForcast_AFForm model) {
		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties afListExecute(Empj_ProjProgForcast_AFForm model) {
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

		Long areaId = model.getAreaId();
		if (null == areaId) {
			model.setAreaId(null);
		}

		Long projectId = model.getProjectId();
		if (null == projectId) {
			model.setProjectId(null);
		}

		String forcastTime = model.getForcastTime();
		if (StringUtils.isBlank(forcastTime)) {
			model.setForcastTime(null);
		}

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

		Integer totalCount = empj_ProjProgForcast_AFDao.findByPage_Size(
				empj_ProjProgForcast_AFDao.getQuery_Size(empj_ProjProgForcast_AFDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Empj_ProjProgForcast_AF> empj_ProjProgForcast_AFList;
		if (totalCount > 0) {
			empj_ProjProgForcast_AFList = empj_ProjProgForcast_AFDao.findByPage(
					empj_ProjProgForcast_AFDao.getQuery(empj_ProjProgForcast_AFDao.getBasicHQL(), model), pageNumber,
					countPerPage);
		} else {
			empj_ProjProgForcast_AFList = new ArrayList<>();
		}

		List<Properties> list = new ArrayList<>();
		Properties pro;
		for (Empj_ProjProgForcast_AF mpj_ProjProgForcast_AF : empj_ProjProgForcast_AFList) {
			pro = new MyProperties();
			pro.put("tableId", mpj_ProjProgForcast_AF.getTableId());
			pro.put("eCode", mpj_ProjProgForcast_AF.geteCode());
			pro.put("forcastTime", StringUtils.isBlank(mpj_ProjProgForcast_AF.getForcastTime()) ? ""
					: mpj_ProjProgForcast_AF.getForcastTime());
			pro.put("forcastPeople", StringUtils.isBlank(mpj_ProjProgForcast_AF.getForcastPeople()) ? ""
					: mpj_ProjProgForcast_AF.getForcastPeople());
			pro.put("areaName", StringUtils.isBlank(mpj_ProjProgForcast_AF.getAreaName()) ? ""
					: mpj_ProjProgForcast_AF.getAreaName());
			pro.put("projectName", StringUtils.isBlank(mpj_ProjProgForcast_AF.getProjectName()) ? ""
					: mpj_ProjProgForcast_AF.getProjectName());
			pro.put("approvalState", StringUtils.isBlank(mpj_ProjProgForcast_AF.getApprovalState()) ? "0"
					: mpj_ProjProgForcast_AF.getApprovalState());

			pro.put("createTimeStamp",
					MyDatetime.getInstance().dateToString2(mpj_ProjProgForcast_AF.getCreateTimeStamp()));

			list.add(pro);

		}

		properties.put("empj_ProjProgForcast_AFList", list);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties batchDeletetExecute(Empj_ProjProgForcast_AFForm model) {
		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		Long[] idArr = model.getIdArr();

		if (idArr == null || idArr.length < 1) {
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}

		Empj_ProjProgForcast_AF projProgForcast_AF;
		Empj_ProjProgForcast_DTLForm childModel;
		List<Empj_ProjProgForcast_DTL> empj_ProjProgForcast_DTLList;
		for (Long tableId : idArr) {
			projProgForcast_AF = empj_ProjProgForcast_AFDao.findById(tableId);
			if (projProgForcast_AF == null) {
				return MyBackInfo.fail(properties, "存在无效的单据信息！");
			}

			projProgForcast_AF.setTheState(S_TheState.Deleted);
			empj_ProjProgForcast_AFDao.update(projProgForcast_AF);

			childModel = new Empj_ProjProgForcast_DTLForm();
			childModel.setAfCode(projProgForcast_AF.geteCode());
			childModel.setAfEntity(projProgForcast_AF);

			empj_ProjProgForcast_DTLList = empj_ProjProgForcast_DTLDao.findByPage(
					empj_ProjProgForcast_DTLDao.getQuery(empj_ProjProgForcast_DTLDao.getBasicHQL(), childModel));
			for (Empj_ProjProgForcast_DTL projProgForcast_DTL : empj_ProjProgForcast_DTLList) {
				projProgForcast_DTL.setTheState(S_TheState.Deleted);
				empj_ProjProgForcast_DTLDao.save(projProgForcast_DTL);
			}

			// 删除审批流
			deleteService.execute(tableId, model.getBusiCode());
		}

		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties detailExecute(Empj_ProjProgForcast_AFForm model) {
		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		Long tableId = model.getTableId();
		if (null == tableId) {
			return MyBackInfo.fail(properties, "请选择查看的单据！");
		}

		Empj_ProjProgForcast_AF projProgForcast_AF = empj_ProjProgForcast_AFDao.findById(tableId);
		if (null == projProgForcast_AF) {
			return MyBackInfo.fail(properties, "单据信息已失效，请刷新后重试！");
		}

		Properties afPro = new MyProperties();
		afPro.put("tableId", projProgForcast_AF.getTableId());
		afPro.put("eCode", projProgForcast_AF.geteCode());
		afPro.put("forcastTime",
				StrUtil.isBlank(projProgForcast_AF.getForcastTime()) ? "" : projProgForcast_AF.getForcastTime());
		afPro.put("forcastPeople",
				StrUtil.isBlank(projProgForcast_AF.getForcastPeople()) ? "" : projProgForcast_AF.getForcastPeople());
		afPro.put("areaName",
				StrUtil.isBlank(projProgForcast_AF.getAreaName()) ? "" : projProgForcast_AF.getAreaName());
		afPro.put("projectName",
				StrUtil.isBlank(projProgForcast_AF.getProjectName()) ? "" : projProgForcast_AF.getProjectName());
		afPro.put("approvalState",
				StrUtil.isBlank(projProgForcast_AF.getApprovalState()) ? "0" : projProgForcast_AF.getApprovalState());

		afPro.put("userUpdate",
				null == projProgForcast_AF.getUserUpdate() ? "" : projProgForcast_AF.getUserUpdate().getTheName());
		afPro.put("lastUpdateTimeStamp", null == projProgForcast_AF.getLastUpdateTimeStamp() ? ""
				: MyDatetime.getInstance().dateToSimpleString(projProgForcast_AF.getLastUpdateTimeStamp()));
		afPro.put("userRecord",
				null == projProgForcast_AF.getUserRecord() ? "" : projProgForcast_AF.getUserRecord().getTheName());
		afPro.put("recordTimeStamp", null == projProgForcast_AF.getRecordTimeStamp() ? ""
				: MyDatetime.getInstance().dateToSimpleString(projProgForcast_AF.getRecordTimeStamp()));
		
		afPro.put("projectId", null == projProgForcast_AF.getProject() ? "" : projProgForcast_AF.getProject().getTableId());
		
		afPro.put("webPushState", null == projProgForcast_AF.getWebPushState() ? "0" : projProgForcast_AF.getWebPushState());
		afPro.put("webHandelState", null == projProgForcast_AF.getWebHandelState() ? "-1" : projProgForcast_AF.getWebHandelState());

		properties.put("empj_PaymentBond_AF", afPro);
		Empj_ProjProgForcast_DTLForm dtlModel = new Empj_ProjProgForcast_DTLForm();
		dtlModel.setTheState(S_TheState.Normal);
		dtlModel.setAfCode(projProgForcast_AF.geteCode());
		dtlModel.setAfEntity(projProgForcast_AF);

		List<Empj_ProjProgForcast_DTL> empj_ProjProgForcast_DTLList = empj_ProjProgForcast_DTLDao
				.findByPage(empj_ProjProgForcast_DTLDao.getQuery(empj_ProjProgForcast_DTLDao.getBasicHQL(), dtlModel));

		List<Properties> dtlList = new ArrayList<>();
		Properties dtl;

		Sm_AttachmentForm sm_AttachmentForm;
		List<Sm_Attachment> sm_AttachmentList;
		// 查询同一附件类型下的所有附件信息（附件信息归类）
		List<Sm_Attachment> smList = null;
		List<Sm_AttachmentCfg_Copy> smAttachmentCfgList;
		Sm_AttachmentCfg_Copy copy;

		Sm_AttachmentCfgDataInfo dataInfo;

		Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
		form.setBusiType(BUSI_CODE);
		form.setTheState(S_TheState.Normal);
		List<Sm_AttachmentCfg> smAttachmentCfgList_Copy = smAttachmentCfgDao
				.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));
		List detailForAdmin;
		String buildProgressType;
		String buildProgress;
		for (Empj_ProjProgForcast_DTL empj_ProjProgForcast_DTL : empj_ProjProgForcast_DTLList) {
			dtl = new MyProperties();

			dtl.put("tableId", empj_ProjProgForcast_DTL.getTableId());
			dtl.put("buildingId", empj_ProjProgForcast_DTL.getBuildInfo().getTableId());
			dtl.put("buildCode", empj_ProjProgForcast_DTL.getBuildCode());
			dtl.put("hasAchieve", StrUtil.isBlank(empj_ProjProgForcast_DTL.getHasAchieve()) ? "-1"
					: empj_ProjProgForcast_DTL.getHasAchieve());
			dtl.put("floorUpNumber", empj_ProjProgForcast_DTL.getFloorUpNumber());
			dtl.put("nowNodeName", empj_ProjProgForcast_DTL.getNowNodeName());

			/*
			 * a)当勾选主体结构时，输入框维护楼层数，与现有校验一致 b)当勾选外面装饰时，输入框维护百分比数字 例：输入数字10 显示10%。
			 * c)当勾选室内装修时，输入框维护百分比数字 例：输入数字10 显示10%。
			 */
			buildProgressType = empj_ProjProgForcast_DTL.getBuildProgressType();
			buildProgress = empj_ProjProgForcast_DTL.getBuildProgress();

			if (StrUtil.isNotBlank(buildProgress) && !"null".equals(buildProgress)) {
				if ("1".equals(buildProgressType)) {
					dtl.put("buildProgress", "主体结构施工：" + buildProgress + "层");
				} else if ("2".equals(buildProgressType)) {
					dtl.put("buildProgress", "外立面装饰施工：" + buildProgress + "%");
				} else if ("3".equals(buildProgressType)) {
					dtl.put("buildProgress", "室内装修施工：" + buildProgress + "%");
				} else {
					dtl.put("buildProgress", StrUtil.isBlank(empj_ProjProgForcast_DTL.getBuildProgress()) ? ""
							: empj_ProjProgForcast_DTL.getBuildProgress());
				}
			} else {
				dtl.put("buildProgress", "");
			}

			dtl.put("buildProgressType", buildProgressType);
			dtl.put("deliveryType", empj_ProjProgForcast_DTL.getBuildInfo().getDeliveryType());
			
			dtl.put("webHandelState", null == empj_ProjProgForcast_DTL.getWebHandelState()?"-1":empj_ProjProgForcast_DTL.getWebHandelState());
			dtl.put("webPushState", null == empj_ProjProgForcast_DTL.getWebPushState()?"0":empj_ProjProgForcast_DTL.getWebPushState());

			smAttachmentCfgList = new ArrayList<>();
			for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList_Copy) {
				copy = new Sm_AttachmentCfg_Copy();
				BeanUtils.copyProperties(sm_AttachmentCfg, copy);

				smAttachmentCfgList.add(copy);

			}

			sm_AttachmentForm = new Sm_AttachmentForm();
			sm_AttachmentForm.setSourceId(empj_ProjProgForcast_DTL.getTableId().toString());
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

			empj_ProjProgForcast_DTL.setSmAttachmentCfgList(smAttachmentCfgList);
			/*
			 * 附件信息
			 */
			detailForAdmin = cfgRebuild.getDetailForAdmin3(smAttachmentCfgList);
			dtl.put("attachementList", detailForAdmin);

			dtlList.add(dtl);
		}

		properties.put("empj_PaymentBond_DTLList", dtlList);

		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties saveExecute(Empj_ProjProgForcast_AFForm model) {
		Properties properties = new MyProperties();

		long nowTimeStamp = System.currentTimeMillis();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		Long tableId = model.getTableId();
		if (null == tableId) {
			return MyBackInfo.fail(properties, "请选择需要修改的单据！");
		}

		Empj_ProjProgForcast_AF projProgForcast_AF = empj_ProjProgForcast_AFDao.findById(tableId);
		if (null == projProgForcast_AF) {
			return MyBackInfo.fail(properties, "单据信息已失效，请刷新后重试！");
		}

		String forcastTime = model.getForcastTime();
		if (StrUtil.isBlank(forcastTime)) {
			return MyBackInfo.fail(properties, "请选择巡查时间！");
		}

		/*
		 * String forcastPeople = model.getForcastPeople(); if
		 * (StrUtil.isBlank(forcastPeople)) { return MyBackInfo.fail(properties,
		 * "请输入巡查人！"); }
		 */

		String forcastPeople = model.getUser().getTheNameOfCompany();

		Long dtlId;
		Long buildingId;
		String buildCode;
		String hasAchieve;
		String buildProgress;
		String buildProgressType;
		Double floorUpNumber;
		Integer floorUpNumberInt;
		Empj_ProjProgForcast_DTL dtl;
		List<Map<String, Object>> attachementList;

		List<Empj_ProjProgForcast_DTLForm> dtlList = model.getEmpj_ProjProgForcast_DTL();
		for (Empj_ProjProgForcast_DTLForm empj_ProjProgForcast_DTLForm : dtlList) {

			dtlId = empj_ProjProgForcast_DTLForm.getTableId();
			buildingId = empj_ProjProgForcast_DTLForm.getBuildingId();
			buildCode = empj_ProjProgForcast_DTLForm.getBuildCode();
			hasAchieve = empj_ProjProgForcast_DTLForm.getHasAchieve();
			if (StrUtil.isBlank(hasAchieve) || "-1".equals(hasAchieve) || "99".equals(hasAchieve)) {
				hasAchieve = "1";
			}
			buildProgress = empj_ProjProgForcast_DTLForm.getBuildProgress();
			floorUpNumber = empj_ProjProgForcast_DTLForm.getFloorUpNumber();

			buildProgressType = empj_ProjProgForcast_DTLForm.getBuildProgressType();

			floorUpNumberInt = Integer.parseInt(new DecimalFormat("0").format(floorUpNumber));

			/*
			 * if ("1".equals(hasAchieve)) {
			 * 
			 * if (StrUtil.isBlank(buildProgressType)) { return
			 * MyBackInfo.fail(properties, "楼幢：" + buildCode + " 未选择建设进度类型"); }
			 * 
			 * if (StrUtil.isBlank(buildProgress)) { return
			 * MyBackInfo.fail(properties, "楼幢：" + buildCode + "未填写当前建设进度！"); }
			 * 
			 * if("1".equals(buildProgressType)){ if
			 * (!ReUtil.isMatch("^[0-9]*$", buildProgress)) { return
			 * MyBackInfo.fail(properties, "请填写有效的建设进度（当前建设楼层数）！"); }
			 * 
			 * if (Integer.valueOf(buildProgress.trim()) > floorUpNumberInt) {
			 * return MyBackInfo.fail(properties, "建设进度不能大于地上层数！"); } }
			 * 
			 * if("2".equals(buildProgressType) ||
			 * "3".equals(buildProgressType)){ if (!ReUtil.isMatch("^[0-9]*$",
			 * buildProgress)) { return MyBackInfo.fail(properties,
			 * "请填写有效的建设进度（维护百分比）！"); }
			 * 
			 * if (Integer.valueOf(buildProgress.trim()) > 100) { return
			 * MyBackInfo.fail(properties, "维护百分比不能大于100！"); }
			 * 
			 * }
			 * 
			 * }
			 */
			attachementList = empj_ProjProgForcast_DTLForm.getAttachementList();

			Sm_Attachment attachment;
			Sm_AttachmentCfgForm form;
			Sm_AttachmentCfg sm_AttachmentCfg;
			List<Map<String, Object>> listMap;

			boolean flag = true;
			for (Map<String, Object> map : attachementList) {
				if (null == map.get("sourceType") && null != map.get("smAttachmentList")) {
					listMap = (List<Map<String, Object>>) map.get("smAttachmentList");
					if (!listMap.isEmpty()) {
						flag = false;
					}
				} else {
					flag = false;
				}
			}

			/*
			 * if ("1".equals(hasAchieve) && flag) { return
			 * MyBackInfo.fail(properties, "楼幢：" + buildCode + "未上传附件信息！"); }
			 */

			dtl = empj_ProjProgForcast_DTLDao.findById(dtlId);
			if (null != dtl) {
				dtl.setHasAchieve(hasAchieve);
				dtl.setBuildProgressType(buildProgressType);
				dtl.setBuildProgress(buildProgress);
				dtl.setLastUpdateTimeStamp(nowTimeStamp);
				dtl.setUserUpdate(model.getUser());

				empj_ProjProgForcast_DTLDao.update(dtl);

				/*
				 * 设置附件信息
				 */
				Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
				sm_AttachmentForm.setSourceId(dtlId.toString());
				sm_AttachmentForm.setBusiType(BUSI_CODE);
				sm_AttachmentForm.setTheState(S_TheState.Normal);

				// 先删除
				List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
						.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL(), sm_AttachmentForm));
				for (Sm_Attachment sm_Attachment : sm_AttachmentList) {
					sm_Attachment.setTheState(S_TheState.Deleted);
					sm_Attachment.setUserUpdate(model.getUser());
					sm_Attachment.setLastUpdateTimeStamp(nowTimeStamp);
					sm_AttachmentDao.update(sm_Attachment);
				}

				// 再保存 attachementList
				/*
				 * Sm_Attachment attachment; Sm_AttachmentCfgForm form;
				 * Sm_AttachmentCfg sm_AttachmentCfg; List<Map<String , Object>>
				 * listMap;
				 */
				List<Map<String, Object>> attachmentList = empj_ProjProgForcast_DTLForm.getAttachementList();

				for (Map<String, Object> map : attachmentList) {

					if (null == map.get("sourceType") && null != map.get("smAttachmentList")) {
						listMap = (List<Map<String, Object>>) map.get("smAttachmentList");
						for (Map<String, Object> map1 : listMap) {
							attachment = new Sm_Attachment();
							// 查询附件配置表
							form = new Sm_AttachmentCfgForm();
							form.seteCode((String) map1.get("sourceType"));
							sm_AttachmentCfg = smAttachmentCfgDao.findOneByQuery_T(
									smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

							attachment.setTheLink((String) (null == map1.get("url") ? "" : map1.get("url")));
							attachment.setTheSize((String) (null == map1.get("theSize") ? "" : map1.get("theSize")));
							attachment.setFileType((String) (null == map1.get("fileType") ? "" : map1.get("fileType")));
							attachment.setSourceType(
									(String) (null == map1.get("sourceType") ? "" : map1.get("sourceType")));
							attachment.setAttachmentCfg(sm_AttachmentCfg);
							attachment.setSourceId(dtlId.toString());
							attachment.setBusiType(BUSI_CODE);
							attachment.setTheState(S_TheState.Normal);
							attachment.setRemark((String) (null == map1.get("name") ? "" : map1.get("name")));
							attachment.setUserStart(model.getUser());
							attachment.setUserUpdate(model.getUser());
							attachment.setCreateTimeStamp(nowTimeStamp);
							attachment.setLastUpdateTimeStamp(nowTimeStamp);
							sm_AttachmentDao.save(attachment);
						}

					} else {
						attachment = new Sm_Attachment();
						// 查询附件配置表
						form = new Sm_AttachmentCfgForm();
						form.seteCode((String) map.get("sourceType"));
						sm_AttachmentCfg = smAttachmentCfgDao
								.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

						attachment.setTheLink((String) (null == map.get("theLink") ? "" : map.get("theLink")));
						attachment.setTheSize((String) (null == map.get("theSize") ? "" : map.get("theSize")));
						attachment.setFileType((String) (null == map.get("fileType") ? "" : map.get("fileType")));
						attachment.setSourceType((String) (null == map.get("sourceType") ? "" : map.get("sourceType")));
						attachment.setAttachmentCfg(sm_AttachmentCfg);
						attachment.setSourceId(dtlId.toString());
						attachment.setBusiType(BUSI_CODE);
						attachment.setTheState(S_TheState.Normal);
						attachment.setRemark((String) (null == map.get("remark") ? "" : map.get("remark")));
						attachment.setUserStart(model.getUser());
						attachment.setUserUpdate(model.getUser());
						attachment.setCreateTimeStamp(nowTimeStamp);
						attachment.setLastUpdateTimeStamp(nowTimeStamp);
						sm_AttachmentDao.save(attachment);
					}

				}

			}

		}

		projProgForcast_AF.setForcastTime(forcastTime);
		projProgForcast_AF.setForcastPeople(forcastPeople);
		projProgForcast_AF.setLastUpdateTimeStamp(nowTimeStamp);
		projProgForcast_AF.setUserUpdate(model.getUser());
		empj_ProjProgForcast_AFDao.update(projProgForcast_AF);

		properties.put("tableId", projProgForcast_AF.getTableId());

		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties submitExecuteBak(Empj_ProjProgForcast_AFForm model) {

		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		long nowTimeStamp = System.currentTimeMillis();

		model.setButtonType("2");
		String busiCode = BUSI_CODE;

		Long tableId = model.getTableId();
		if (tableId == null || tableId < 1) {
			return MyBackInfo.fail(properties, "请选择有效的单据信息！");
		}

		Empj_ProjProgForcast_AF forcast_AF = empj_ProjProgForcast_AFDao.findById(tableId);
		if (null == forcast_AF) {
			return MyBackInfo.fail(properties, "选择的单据信息已失效，请刷新后重试！");
		}

		if (StrUtil.isBlank(forcast_AF.getForcastPeople()) || StrUtil.isBlank(forcast_AF.getForcastPeople())) {
			return MyBackInfo.fail(properties, "请先维护‘巡查时间’和‘巡查人’信息！");
		}

		Empj_ProjProgForcast_DTLForm dtlModel = new Empj_ProjProgForcast_DTLForm();
		dtlModel.setTheState(S_TheState.Normal);
		dtlModel.setAfCode(forcast_AF.geteCode());
		dtlModel.setAfEntity(forcast_AF);

		List<Empj_ProjProgForcast_DTL> empj_ProjProgForcast_DTLList = empj_ProjProgForcast_DTLDao
				.findByPage(empj_ProjProgForcast_DTLDao.getQuery(empj_ProjProgForcast_DTLDao.getBasicHQL(), dtlModel));

		String buildProgressType;
		String buildProgress;
		Double floorUpNumber;
		Integer floorUpNumberInt;

		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);

		// 附件信息
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setBusiType("03030202");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		// 需处理的附件信息
		List<String> attachmentList;

		// 附件信息
		List<Sm_Attachment> sm_AttachmentList;
		Sm_Attachment attachment;

		boolean isCheck;

		for (Empj_ProjProgForcast_DTL empj_ProjProgForcast_DTL : empj_ProjProgForcast_DTLList) {

			if (StrUtil.isBlank(empj_ProjProgForcast_DTL.getHasAchieve())
					|| "-1".equals(empj_ProjProgForcast_DTL.getHasAchieve().trim())) {
				return MyBackInfo.fail(properties, "楼幢：" + empj_ProjProgForcast_DTL.getBuildCode() + " ‘是否达到正负零’未维护！");
			}

			if ("1".equals(empj_ProjProgForcast_DTL.getHasAchieve())) {

				buildProgressType = empj_ProjProgForcast_DTL.getBuildProgressType();
				buildProgress = empj_ProjProgForcast_DTL.getBuildProgress();

				Matcher m = p.matcher(buildProgress);
				buildProgress = m.replaceAll("").trim();

				floorUpNumber = empj_ProjProgForcast_DTL.getFloorUpNumber();
				floorUpNumberInt = Integer.parseInt(new DecimalFormat("0").format(floorUpNumber));

				if (StrUtil.isBlank(buildProgressType)) {
					return MyBackInfo.fail(properties, "楼幢：" + empj_ProjProgForcast_DTL.getBuildCode() + " 未选择建设进度类型");
				}

				if (StrUtil.isBlank(buildProgress)) {
					return MyBackInfo.fail(properties, "楼幢：" + empj_ProjProgForcast_DTL.getBuildCode() + "未填写当前建设进度！");
				}

				if ("1".equals(buildProgressType)) {
					if (!ReUtil.isMatch("^[0-9]*$", buildProgress)) {
						return MyBackInfo.fail(properties, "请填写有效的建设进度（当前建设楼层数）！");
					}

					if (Integer.valueOf(buildProgress.trim()) > floorUpNumberInt) {
						return MyBackInfo.fail(properties, "建设进度不能大于地上层数！");
					}
				}

				if ("2".equals(buildProgressType) || "3".equals(buildProgressType)) {
					if (!ReUtil.isMatch("^[0-9]*$", buildProgress)) {
						return MyBackInfo.fail(properties, "请填写有效的建设进度（维护百分比）！");
					}

					if (Integer.valueOf(buildProgress.trim()) > 100) {
						return MyBackInfo.fail(properties, "维护百分比不能大于100！");
					}

				}

			}

			// 校验附件
			sm_AttachmentForm.setSourceId(empj_ProjProgForcast_DTL.getTableId().toString());

			attachmentList = new ArrayList<>();
			// 加载所有楼幢下的相关附件信息
			sm_AttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getHandlerPhotoHQL(), sm_AttachmentForm));
			if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
				return MyBackInfo.fail(properties, "楼幢：" + empj_ProjProgForcast_DTL.getBuildCode() + " 未上传附件信息！");
			}

			if (sm_AttachmentList.size() < 4) {
				return MyBackInfo.fail(properties,
						"楼幢：" + empj_ProjProgForcast_DTL.getBuildCode() + " 未上传足够的附件信息（至少四张）！");
			}

			isCheck = true;
			for (Sm_Attachment sm_Attachment : sm_AttachmentList) {
				if ("总平图".equals(sm_Attachment.getAttachmentCfg().getTheName())) {
					isCheck = false;
					break;
				}
			}

			if (isCheck) {
				return MyBackInfo.fail(properties, "楼幢：" + empj_ProjProgForcast_DTL.getBuildCode() + " 未上传总平图！");
			}

		}

		// 判断单据审批状态
		if (S_ApprovalState.Examining.equals(forcast_AF.getApprovalState())) {
			return MyBackInfo.fail(properties, "该协议已在审核中，不可重复提交");
		}
		if (S_ApprovalState.Completed.equals(forcast_AF.getApprovalState())) {
			return MyBackInfo.fail(properties, "该协议已审批完成，不可重复提交");
		}

		properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());

		if ("noApproval".equals(properties.getProperty("info"))) {
			return MyBackInfo.fail(properties, "未配置对应的审批流程！");
		} else if ("fail".equals(properties.getProperty(S_NormalFlag.result))) {
			return properties;
		} else {

			// 判断当前登录用户是否有权限发起审批
			Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
					.get("sm_approvalProcess_cfg");
			// 审批操作
			properties = sm_approvalProcessService.execute(forcast_AF, model, sm_approvalProcess_cfg);

			forcast_AF.setCompany(user.getCompany());
			forcast_AF.setCompanyName(user.getCompany().getTheName());
			forcast_AF.setLastUpdateTimeStamp(nowTimeStamp);
			forcast_AF.setUserUpdate(user);
			forcast_AF.setApplyDate(nowTimeStamp);
			forcast_AF.setApprovalState(S_ApprovalState.Examining);
			forcast_AF.setHandleState("0");
			empj_ProjProgForcast_AFDao.update(forcast_AF);

		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties submitExecute(Empj_ProjProgForcast_AFForm model) {

		Properties properties = new MyProperties();

		Sm_User user = model.getUser();
		long nowTimeStamp = System.currentTimeMillis();

		model.setButtonType("2");
		String busiCode = BUSI_CODE;

		Long tableId = model.getTableId();
		if (tableId == null || tableId < 1) {
			return MyBackInfo.fail(properties, "请选择有效的单据信息！");
		}

		Empj_ProjProgForcast_AF forcast_AF = empj_ProjProgForcast_AFDao.findById(tableId);
		if (null == forcast_AF) {
			return MyBackInfo.fail(properties, "选择的单据信息已失效，请刷新后重试！");
		}

		if (StrUtil.isBlank(forcast_AF.getForcastPeople()) || StrUtil.isBlank(forcast_AF.getForcastPeople())) {
			return MyBackInfo.fail(properties, "请先维护‘巡查时间’和‘巡查人’信息！");
		}

		Empj_ProjProgForcast_DTLForm dtlModel = new Empj_ProjProgForcast_DTLForm();
		dtlModel.setTheState(S_TheState.Normal);
		dtlModel.setAfCode(forcast_AF.geteCode());
		dtlModel.setAfEntity(forcast_AF);
		dtlModel.setHasAchieve("1");

		List<Empj_ProjProgForcast_DTL> empj_ProjProgForcast_DTLList = empj_ProjProgForcast_DTLDao
				.findByPage(empj_ProjProgForcast_DTLDao.getQuery(empj_ProjProgForcast_DTLDao.getBasicHQL(), dtlModel));

		String buildProgressType;
		String buildProgress;
		Double floorUpNumber;
		Integer floorUpNumberInt;

		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);

		// 附件信息
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setBusiType("03030202");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		// 需处理的附件信息
		List<String> attachmentList;

		// 附件信息
		List<Sm_Attachment> sm_AttachmentList;
		Sm_Attachment attachment;

		boolean isCheck;

		for (Empj_ProjProgForcast_DTL empj_ProjProgForcast_DTL : empj_ProjProgForcast_DTLList) {

			if (StrUtil.isBlank(empj_ProjProgForcast_DTL.getHasAchieve())
					|| "-1".equals(empj_ProjProgForcast_DTL.getHasAchieve().trim())) {
				return MyBackInfo.fail(properties, "楼幢：" + empj_ProjProgForcast_DTL.getBuildCode() + " ‘是否达到正负零’未维护！");
			}

			if ("1".equals(empj_ProjProgForcast_DTL.getHasAchieve())) {

				buildProgressType = empj_ProjProgForcast_DTL.getBuildProgressType();
				buildProgress = empj_ProjProgForcast_DTL.getBuildProgress();

				Matcher m = p.matcher(buildProgress);
				buildProgress = m.replaceAll("").trim();

				floorUpNumber = empj_ProjProgForcast_DTL.getFloorUpNumber();
				floorUpNumberInt = Integer.parseInt(new DecimalFormat("0").format(floorUpNumber));

				if (StrUtil.isBlank(buildProgressType)) {
					return MyBackInfo.fail(properties, "楼幢：" + empj_ProjProgForcast_DTL.getBuildCode() + " 未选择建设进度类型");
				}

				if (StrUtil.isBlank(buildProgress)) {
					return MyBackInfo.fail(properties, "楼幢：" + empj_ProjProgForcast_DTL.getBuildCode() + "未填写当前建设进度！");
				}

				if ("1".equals(buildProgressType)) {
					if (!ReUtil.isMatch("^[0-9]*$", buildProgress)) {
						return MyBackInfo.fail(properties, "请填写有效的建设进度（当前建设楼层数）！");
					}

					if (Integer.valueOf(buildProgress.trim()) > floorUpNumberInt) {
						return MyBackInfo.fail(properties, "建设进度不能大于地上层数！");
					}
				}

				if ("2".equals(buildProgressType) || "3".equals(buildProgressType)) {
					if (!ReUtil.isMatch("^[0-9]*$", buildProgress)) {
						return MyBackInfo.fail(properties, "请填写有效的建设进度（维护百分比）！");
					}

					if (Integer.valueOf(buildProgress.trim()) > 100) {
						return MyBackInfo.fail(properties, "维护百分比不能大于100！");
					}

				}

				// 校验附件
				sm_AttachmentForm.setSourceId(empj_ProjProgForcast_DTL.getTableId().toString());

				attachmentList = new ArrayList<>();
				// 加载所有楼幢下的相关附件信息
				sm_AttachmentList = sm_AttachmentDao
						.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getHandlerPhotoHQL(), sm_AttachmentForm));
				if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
					return MyBackInfo.fail(properties, "楼幢：" + empj_ProjProgForcast_DTL.getBuildCode() + " 未上传附件信息！");
				}

				if (sm_AttachmentList.size() < 4) {
					return MyBackInfo.fail(properties,
							"楼幢：" + empj_ProjProgForcast_DTL.getBuildCode() + " 未上传足够的附件信息（至少四张）！");
				}

				isCheck = true;
				for (Sm_Attachment sm_Attachment : sm_AttachmentList) {
					if ("总平图".equals(sm_Attachment.getAttachmentCfg().getTheName())) {
						isCheck = false;
						break;
					}
				}

				if (isCheck) {
					return MyBackInfo.fail(properties, "楼幢：" + empj_ProjProgForcast_DTL.getBuildCode() + " 未上传总平图！");
				}

			}


		}

		// 判断单据审批状态
		if (S_ApprovalState.Examining.equals(forcast_AF.getApprovalState())) {
			return MyBackInfo.fail(properties, "该协议已在审核中，不可重复提交");
		}
		if (S_ApprovalState.Completed.equals(forcast_AF.getApprovalState())) {
			return MyBackInfo.fail(properties, "该协议已审批完成，不可重复提交");
		}

		//properties = sm_ApprovalProcessGetService.execute(busiCode, model.getUserId());
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String date = sdf.format(new Date());
		/**
		 * 新功能处理
		 */
		Empj_ProjProgForcast_Manage manage = new Empj_ProjProgForcast_Manage(forcast_AF,user);
		empj_ProjProgForcast_ManageDao.save(manage);
		manage.seteCode("03030206N" + date + String.format("%05d", manage.getTableId().intValue()));
		empj_ProjProgForcast_ManageDao.update(manage);
		
		for (Empj_ProjProgForcast_DTL empj_ProjProgForcast_DTL : empj_ProjProgForcast_DTLList) {
			Empj_ProjProgForcast_Manage manageDtl = new Empj_ProjProgForcast_Manage(forcast_AF,empj_ProjProgForcast_DTL,user);
			empj_ProjProgForcast_ManageDao.save(manageDtl);
			manageDtl.seteCode("03030206N" + date + String.format("%05d", manage.getTableId().intValue()));
			empj_ProjProgForcast_ManageDao.update(manage);
			
			//待审核
			empj_ProjProgForcast_DTL.setWebHandelState("-1");
			//推送中
			empj_ProjProgForcast_DTL.setWebPushState("1");
			
		}
		
		/**
		 * 新功能处理
		 */
		forcast_AF.setCompany(user.getCompany());
		forcast_AF.setCompanyName(user.getCompany().getTheName());
		forcast_AF.setLastUpdateTimeStamp(nowTimeStamp);
		forcast_AF.setUserUpdate(user);
		forcast_AF.setApplyDate(nowTimeStamp);
		forcast_AF.setApprovalState(S_ApprovalState.Examining);
		forcast_AF.setHandleState("0");
		
		forcast_AF.setWebHandelState("-1");
		
		Boolean pushProjProgProject = commonService.pushProjProgProject(forcast_AF);
		if(pushProjProgProject){
			//已推送
			forcast_AF.setWebPushState("2");
			manage.setWebPushState("2");
			empj_ProjProgForcast_ManageDao.update(manage);
		}else{
			//推送中
			forcast_AF.setWebPushState("1");
		}
		
		empj_ProjProgForcast_AFDao.update(forcast_AF);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	/**
	 * 报表
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Properties reportExecute(Empj_ProjProgForcast_AFForm model) {
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

		Long areaId = model.getAreaId();
		if (null == areaId) {
			model.setAreaId(null);
		}

		Long projectId = model.getProjectId();
		if (null == projectId) {
			model.setProjectId(null);
		}

		Long companyId = model.getCompanyId();
		if (null == companyId) {
			model.setCompanyId(null);
		}

		String timeStamp = model.getTimeStamp();
		if (StrUtil.isBlank(timeStamp)) {
			model.setTimeStamp(null);
		} else {
			model.setTimeStamp(timeStamp.trim());
		}

		String forcastTime = model.getForcastTime();
		if (StringUtils.isBlank(forcastTime)) {
			model.setForcastTime(null);
		}

		String timeStampStart = model.getTimeStampStart();
		if (StringUtils.isBlank(timeStampStart)) {
			model.setTimeStampStart(null);
		} else {
			model.setTimeStampStart(timeStampStart.trim());
		}

		String timeStampEnd = model.getTimeStampEnd();
		if (StringUtils.isBlank(timeStampEnd)) {
			model.setTimeStampEnd(null);
		} else {
			model.setTimeStampEnd(timeStampEnd.trim());
		}

		model.setApprovalState("已完结");
		model.setBusiState("已备案");

		if (StringUtils.isBlank(keyword)) {
			model.setKeyword(null);
		} else {
			model.setKeyword("%" + keyword + "%");
		}

		model.setTheState(S_TheState.Normal);

		Integer totalCount = empj_ProjProgForcast_AFDao.findByPage_Size(
				empj_ProjProgForcast_AFDao.getQuery_Size(empj_ProjProgForcast_AFDao.getBasicReportHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Empj_ProjProgForcast_AF> empj_ProjProgForcast_AFList;
		if (totalCount > 0) {
			empj_ProjProgForcast_AFList = empj_ProjProgForcast_AFDao.findByPage(
					empj_ProjProgForcast_AFDao.getQuery(empj_ProjProgForcast_AFDao.getBasicReportHQL(), model),
					pageNumber, countPerPage);
		} else {
			empj_ProjProgForcast_AFList = new ArrayList<>();
		}

		List<Properties> list = new ArrayList<>();
		Properties pro;
		for (Empj_ProjProgForcast_AF mpj_ProjProgForcast_AF : empj_ProjProgForcast_AFList) {
			pro = new MyProperties();
			pro.put("tableId", mpj_ProjProgForcast_AF.getTableId());
			pro.put("eCode", mpj_ProjProgForcast_AF.geteCode());
			pro.put("forcastTime", StringUtils.isBlank(mpj_ProjProgForcast_AF.getForcastTime()) ? ""
					: mpj_ProjProgForcast_AF.getForcastTime());
			pro.put("forcastPeople", StringUtils.isBlank(mpj_ProjProgForcast_AF.getForcastPeople()) ? ""
					: mpj_ProjProgForcast_AF.getForcastPeople());
			pro.put("companyName", StringUtils.isBlank(mpj_ProjProgForcast_AF.getCompanyName()) ? ""
					: mpj_ProjProgForcast_AF.getCompanyName());
			pro.put("areaName", StringUtils.isBlank(mpj_ProjProgForcast_AF.getAreaName()) ? ""
					: mpj_ProjProgForcast_AF.getAreaName());
			pro.put("projectName", StringUtils.isBlank(mpj_ProjProgForcast_AF.getProjectName()) ? ""
					: mpj_ProjProgForcast_AF.getProjectName());
			pro.put("approvalState", StringUtils.isBlank(mpj_ProjProgForcast_AF.getApprovalState()) ? "0"
					: mpj_ProjProgForcast_AF.getApprovalState());
			pro.put("buildCount",
					null == mpj_ProjProgForcast_AF.getBuildCount() ? 0 : mpj_ProjProgForcast_AF.getBuildCount());
			pro.put("approvalDate", null == mpj_ProjProgForcast_AF.getApplyDate() ? ""
					: MyDatetime.getInstance().dateToSimpleString(mpj_ProjProgForcast_AF.getApplyDate()));
			pro.put("recordTimeStamp", null == mpj_ProjProgForcast_AF.getRecordTimeStamp() ? ""
					: MyDatetime.getInstance().dateToSimpleString(mpj_ProjProgForcast_AF.getRecordTimeStamp()));

			list.add(pro);

		}

		properties.put("empj_ProjProgForcast_AFList", list);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		return properties;
	}

	/**
	 * 报表导出
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Properties reportExportExecute(Empj_ProjProgForcast_AFForm model) {
		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		Sm_User user = model.getUser();
		if (null == user) {
			return MyBackInfo.fail(properties, "请先登录！");
		}

		String keyword = model.getKeyword();

		Long areaId = model.getAreaId();
		if (null == areaId) {
			model.setAreaId(null);
		}

		Long projectId = model.getProjectId();
		if (null == projectId) {
			model.setProjectId(null);
		}

		Long companyId = model.getCompanyId();
		if (null == companyId) {
			model.setCompanyId(null);
		}

		String timeStamp = model.getTimeStamp();
		if (StrUtil.isBlank(timeStamp)) {
			model.setTimeStamp(null);
		} else {
			model.setTimeStamp(timeStamp.trim());
		}
		String forcastTime = model.getForcastTime();
		if (StringUtils.isBlank(forcastTime)) {
			model.setForcastTime(null);
		}

		String timeStampStart = model.getTimeStampStart();
		if (StringUtils.isBlank(timeStampStart)) {
			model.setTimeStampStart(null);
		} else {
			model.setTimeStampStart(timeStampStart.trim());
		}

		String timeStampEnd = model.getTimeStampEnd();
		if (StringUtils.isBlank(timeStampEnd)) {
			model.setTimeStampEnd(null);
		} else {
			model.setTimeStampEnd(timeStampEnd.trim());
		}

		model.setApprovalState("已完结");
		model.setBusiState("已备案");

		if (StringUtils.isBlank(keyword)) {
			model.setKeyword(null);
		} else {
			model.setKeyword("%" + keyword + "%");
		}

		model.setTheState(S_TheState.Normal);

		Integer totalCount = empj_ProjProgForcast_AFDao.findByPage_Size(
				empj_ProjProgForcast_AFDao.getQuery_Size(empj_ProjProgForcast_AFDao.getBasicReportHQL(), model));

		List<Empj_ProjProgForcast_AF> empj_ProjProgForcast_AFList;
		List<Empj_ProjProgInspectionReportExcelVO> list;
		Empj_ProjProgInspectionReportExcelVO pro;
		if (totalCount > 0) {

			list = new ArrayList<>();

			empj_ProjProgForcast_AFList = empj_ProjProgForcast_AFDao.findByPage(
					empj_ProjProgForcast_AFDao.getQuery(empj_ProjProgForcast_AFDao.getBasicReportHQL(), model));

			int i = 0;
			for (Empj_ProjProgForcast_AF mpj_ProjProgForcast_AF : empj_ProjProgForcast_AFList) {

				pro = new Empj_ProjProgInspectionReportExcelVO();

				pro.setOrdinal(++i);
				pro.setCompanyName(StringUtils.isBlank(mpj_ProjProgForcast_AF.getCompanyName()) ? ""
						: mpj_ProjProgForcast_AF.getCompanyName());
				pro.setAreaName(StringUtils.isBlank(mpj_ProjProgForcast_AF.getAreaName()) ? ""
						: mpj_ProjProgForcast_AF.getAreaName());
				pro.setProjectName(StringUtils.isBlank(mpj_ProjProgForcast_AF.getProjectName()) ? ""
						: mpj_ProjProgForcast_AF.getProjectName());
				pro.setBuildCount(null == mpj_ProjProgForcast_AF.getBuildCount() ? "0"
						: mpj_ProjProgForcast_AF.getBuildCount().toString());
//				pro.setApprovalDate(null == mpj_ProjProgForcast_AF.getApplyDate() ? ""
//						: MyDatetime.getInstance().dateToSimpleString(mpj_ProjProgForcast_AF.getApplyDate()));
				pro.setApprovalDate(mpj_ProjProgForcast_AF.getForcastTime());
				pro.setCode(mpj_ProjProgForcast_AF.geteCode());
				pro.setRecordTimeStamp(null == mpj_ProjProgForcast_AF.getRecordTimeStamp() ? ""
						: MyDatetime.getInstance().dateToSimpleString(mpj_ProjProgForcast_AF.getRecordTimeStamp()));
				list.add(pro);
			}

			DirectoryUtil directoryUtil = new DirectoryUtil();
			String relativeDir = directoryUtil.createRelativePathWithDate("Empj_ProjProgForcast_AFService");// 文件在项目中的相对路径
			String localPath = directoryUtil.getProjectRoot();// 项目路径

			String saveDirectory = localPath + relativeDir;// 文件在服务器文件系统中的完整路径

			if (saveDirectory.contains("%20")) {
				saveDirectory = saveDirectory.replace("%20", " ");
			}

			directoryUtil.mkdir(saveDirectory);

			String strNewFileName = excelName + "-"
					+ MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";

			String saveFilePath = saveDirectory + strNewFileName;

			// 通过工具类创建writer
			ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);

			// 自定义字段别名
			writer.addHeaderAlias("ordinal", "序号");
			writer.addHeaderAlias("companyName", "巡查机构");
			writer.addHeaderAlias("areaName", "区域");
			writer.addHeaderAlias("projectName", "项目");
			writer.addHeaderAlias("buildCount", "楼幢数");
//			writer.addHeaderAlias("approvalDate", "照片上传日期");
			writer.addHeaderAlias("approvalDate", "巡查日期");
			writer.addHeaderAlias("code", "巡查单号");
			writer.addHeaderAlias("recordTimeStamp", "备案日期");

			// 一次性写出内容，使用默认样式
			writer.write(list);

			// 关闭writer，释放内存
			writer.flush();

			writer.autoSizeColumn(0, true);
			writer.autoSizeColumn(1, true);
			writer.autoSizeColumn(2, true);
			writer.autoSizeColumn(3, true);
			writer.autoSizeColumn(4, true);
			writer.autoSizeColumn(5, true);
			writer.autoSizeColumn(6, true);
			writer.autoSizeColumn(7, true);

			writer.close();

			properties.put("fileURL", relativeDir + strNewFileName);

		} else {
			return MyBackInfo.fail(properties, "未查询到有效的单据信息！");
		}

		return properties;
	}

	@SuppressWarnings("unchecked")
	public Properties exportExecute(Empj_ProjProgForcast_AFForm model) {
		Properties properties = new MyProperties();

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		Sm_User user = model.getUser();
		if (null == user) {
			return MyBackInfo.fail(properties, "请先登录！");
		}

		String keyword = model.getKeyword();

		Long areaId = model.getAreaId();
		if (null == areaId) {
			model.setAreaId(null);
		}

		Long projectId = model.getProjectId();
		if (null == projectId) {
			model.setProjectId(null);
		}

		String forcastTime = model.getForcastTime();
		if (StringUtils.isBlank(forcastTime)) {
			model.setForcastTime(null);
		}

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
		model.setApprovalState("待提交");

		Integer totalCount = empj_ProjProgForcast_AFDao.findByPage_Size(
				empj_ProjProgForcast_AFDao.getQuery_Size(empj_ProjProgForcast_AFDao.getBasicHQL(), model));

		List<Empj_ProjProgForcast_AF> empj_ProjProgForcast_AFList;
		List<Empj_ProjProgInspectionExcelVO> list;
		Empj_ProjProgInspectionExcelVO pro;

		List<Empj_ProjProgForcast_DTL> empj_ProjProgForcast_DTLList;
		if (totalCount > 0) {
			empj_ProjProgForcast_AFList = empj_ProjProgForcast_AFDao
					.findByPage(empj_ProjProgForcast_AFDao.getQuery(empj_ProjProgForcast_AFDao.getBasicHQL(), model));

			list = new ArrayList<>();
			int i = 0;
			for (Empj_ProjProgForcast_AF mpj_ProjProgForcast_AF : empj_ProjProgForcast_AFList) {

				Empj_ProjProgForcast_DTLForm dtlModel = new Empj_ProjProgForcast_DTLForm();
				dtlModel.setTheState(S_TheState.Normal);
				dtlModel.setAfCode(mpj_ProjProgForcast_AF.geteCode());
				dtlModel.setAfEntity(mpj_ProjProgForcast_AF);
				empj_ProjProgForcast_DTLList = empj_ProjProgForcast_DTLDao.findByPage(
						empj_ProjProgForcast_DTLDao.getQuery(empj_ProjProgForcast_DTLDao.getBasicHQL(), dtlModel));

				for (Empj_ProjProgForcast_DTL dtl : empj_ProjProgForcast_DTLList) {
					pro = new Empj_ProjProgInspectionExcelVO();

					pro.setOrdinal(++i);
					pro.setCode(mpj_ProjProgForcast_AF.geteCode());
					pro.setAreaName(StringUtils.isBlank(mpj_ProjProgForcast_AF.getAreaName()) ? ""
							: mpj_ProjProgForcast_AF.getAreaName());
					pro.setProjectName(StringUtils.isBlank(mpj_ProjProgForcast_AF.getProjectName()) ? ""
							: mpj_ProjProgForcast_AF.getProjectName());

					pro.setBuildName(StringUtils.isBlank(dtl.getBuildCode()) ? "" : dtl.getBuildCode());
					/*
					 * pro.setNowNodeName(StringUtils.isBlank(dtl.getNowNodeName
					 * ()) ? "" : dtl.getNowNodeName());
					 */
					pro.setFloorUpNumber(null == dtl.getFloorUpNumber() ? 0.00 : dtl.getFloorUpNumber());
					pro.setBuildProgress(StringUtils.isBlank(dtl.getBuildProgress()) ? "" : dtl.getBuildProgress());
					pro.setForcastTime(StringUtils.isBlank(mpj_ProjProgForcast_AF.getForcastTime()) ? ""
							: mpj_ProjProgForcast_AF.getForcastTime());
					/*
					 * pro.setForcastPeople(StringUtils.isBlank(
					 * mpj_ProjProgForcast_AF.getForcastPeople()) ? "" :
					 * mpj_ProjProgForcast_AF.getForcastPeople());
					 */

					list.add(pro);

				}

			}

			DirectoryUtil directoryUtil = new DirectoryUtil();
			// 文件在项目中的相对路径
			String relativeDir = directoryUtil.createRelativePathWithDate("Empj_ProjProgForcast_AFService");
			// 项目路径
			String localPath = directoryUtil.getProjectRoot();

			// 文件在服务器文件系统中的完整路径
			String saveDirectory = localPath + relativeDir;

			if (saveDirectory.contains("%20")) {
				saveDirectory = saveDirectory.replace("%20", " ");
			}

			directoryUtil.mkdir(saveDirectory);

			String strNewFileName = excelName_pro + "-"
					+ MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";

			String saveFilePath = saveDirectory + strNewFileName;

			// 通过工具类创建writer
			ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);

			// 自定义字段别名
			writer.addHeaderAlias("ordinal", "序号");
			writer.addHeaderAlias("code", "巡查单号");
			writer.addHeaderAlias("areaName", "区域名称");
			writer.addHeaderAlias("projectName", "项目名称");
			writer.addHeaderAlias("buildName", "楼幢");
			// writer.addHeaderAlias("nowNodeName", "当前进度节点");
			writer.addHeaderAlias("floorUpNumber", "地上层数");
			writer.addHeaderAlias("buildProgress", "当前建设进度");
			writer.addHeaderAlias("forcastTime", "巡查时间");
			// writer.addHeaderAlias("forcastPeople", "巡查人");

			// 一次性写出内容，使用默认样式
			writer.write(list);

			// 关闭writer，释放内存
			writer.flush();

			writer.autoSizeColumn(0, true);
			writer.autoSizeColumn(1, true);
			writer.autoSizeColumn(2, true);
			writer.autoSizeColumn(3, true);
			writer.autoSizeColumn(4, true);
			writer.autoSizeColumn(5, true);
			writer.autoSizeColumn(6, true);
			writer.autoSizeColumn(7, true);
			// writer.autoSizeColumn(8, true);
			// writer.autoSizeColumn(9, true);

			writer.close();

			properties.put("fileURL", relativeDir + strNewFileName);

		} else {
			return MyBackInfo.fail(properties, "未查询到有效数据！");
		}

		return properties;
	}

	public Properties handlerExecute(Empj_ProjProgForcast_AFForm model) {
		Properties properties = new MyProperties();

		Long tableId = model.getTableId();
		Empj_ProjProgForcast_AF projProgForcast_AF = empj_ProjProgForcast_AFDao.findById(tableId);
		if (null == projProgForcast_AF) {
			return MyBackInfo.fail(properties, "未查询到有效信息！");
		}
		System.out.println("定时任务开始" + System.currentTimeMillis());

		// 查询水印图片位置
		Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
		baseParameterForm.setTheState(S_TheState.Normal);
		baseParameterForm.setTheValue("69005");
		baseParameterForm.setParametertype("69");
		Sm_BaseParameter baseParameter = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));

		if (null == baseParameter) {
			return MyBackInfo.fail(properties, "参数1");
		}

		// 查询缩放比列
		Sm_BaseParameterForm baseParameterForm2 = new Sm_BaseParameterForm();
		baseParameterForm2.setTheState(S_TheState.Normal);
		baseParameterForm2.setTheValue("69001");
		baseParameterForm2.setParametertype("69");
		Sm_BaseParameter baseParameter2 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm2));

		if (null == baseParameter2) {
			return MyBackInfo.fail(properties, "参数2");
		}

		Sm_BaseParameterForm baseParameterForm3 = new Sm_BaseParameterForm();
		baseParameterForm3.setTheState(S_TheState.Normal);
		baseParameterForm3.setTheValue("69002");
		baseParameterForm3.setParametertype("69");
		Sm_BaseParameter baseParameter3 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm3));

		if (null == baseParameter3) {
			return MyBackInfo.fail(properties, "参数3");
		}

		boolean isComplete = Boolean.TRUE;
		MatrixUtil picUtil = new MatrixUtil();

		String[] deskURL;

		// 查询需要处理图片的申请单信息
		Empj_ProjProgForcast_AFForm afModel = new Empj_ProjProgForcast_AFForm();
		afModel.setTheState(S_TheState.Normal);
		afModel.setApprovalState(S_ApprovalState.Examining);
		afModel.setHandleState("0");

		List<Empj_ProjProgForcast_AF> afList;
		Empj_ProjProgForcast_AF updateProjProgForcast_AF;

		// 子表信息
		List<Empj_ProjProgForcast_DTL> dtlList;
		Empj_ProjProgForcast_DTLForm dtlModel = new Empj_ProjProgForcast_DTLForm();
		dtlModel.setTheState(S_TheState.Normal);
		dtlModel.setHandleState("0");

		// 附件信息
		Sm_AttachmentForm sm_AttachmentForm = new Sm_AttachmentForm();
		sm_AttachmentForm.setBusiType("03030202");
		sm_AttachmentForm.setTheState(S_TheState.Normal);

		// 需处理的附件信息
		List<String> attachmentList;

		// 附件信息
		List<Sm_Attachment> sm_AttachmentList;
		Sm_Attachment attachment;

		String theLink;
		String pic;
		String smallPic;

		ReceiveMessage upload;

		/*
		 * 查询子表信息 进行图片处理
		 */
		dtlModel.setAfEntity(projProgForcast_AF);
		dtlModel.setAfId(projProgForcast_AF.getTableId());
		dtlList = empj_ProjProgForcast_DTLDao.findByPage(
				empj_ProjProgForcast_DTLDao.getQuery(empj_ProjProgForcast_DTLDao.getHandlerPicHQL(), dtlModel));

		for (Empj_ProjProgForcast_DTL dtl : dtlList) {
			try {

				sm_AttachmentForm.setSourceId(dtl.getTableId().toString());

				attachmentList = new ArrayList<>();
				// 加载所有楼幢下的相关附件信息
				sm_AttachmentList = sm_AttachmentDao.findByPage(
						sm_AttachmentDao.getQuery(sm_AttachmentDao.getHandlerPhotoHQL(), sm_AttachmentForm));

				System.out.println("楼幢： " + dtl.getBuildCode() + "满足的附件数据数" + sm_AttachmentList.size());

				if (null == sm_AttachmentList || sm_AttachmentList.size() == 0) {
					sm_AttachmentList = new ArrayList<Sm_Attachment>();
				}

				// 遍历总平图
				for (int i = 0; i < sm_AttachmentList.size(); i++) {

					attachment = sm_AttachmentList.get(i);
					theLink = sm_AttachmentList.get(i).getTheLink();
					pic = "";
					smallPic = "";

					deskURL = picUtil.addWaterMarkAndCompress(theLink, baseParameter.getTheName(),
							baseParameter2.getTheName(), baseParameter3.getTheName());

					// 高清图
					if (StrUtil.isNotBlank(deskURL[0])) {
						upload = ossUtil.upload(deskURL[0]);
						if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
								&& StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

							pic = upload.getData().get(0).getUrl();
							picUtil.deleteFile(deskURL[0]);
						}
					}

					// 缩略图
					if (StrUtil.isNotBlank(deskURL[1])) {
						upload = ossUtil.upload(deskURL[1]);
						if (null != upload && null != upload.getData() && !upload.getData().isEmpty()
								&& StrUtil.isNotBlank(upload.getData().get(0).getUrl())) {

							smallPic = upload.getData().get(0).getUrl();
							picUtil.deleteFile(deskURL[1]);
						}
					}

					if ("总平图".equals(sm_AttachmentList.get(i).getAttachmentCfg().getTheName())) {
						// 总平图
						attachment.setSortNum("1");
					} else {
						// 其他附件
						attachment.setSortNum("2");
					}

					attachment.setTheLink(pic);
					attachment.setMd5Info(theLink + "##" + pic + "##" + smallPic);
					attachment.setRecordTimeStamp(System.currentTimeMillis());
					sm_AttachmentDao.update(attachment);
				}

				// 更新图片处理状态
				dtl.setHandleState("1");
				empj_ProjProgForcast_DTLDao.update(dtl);

			} catch (Exception e) {

				isComplete = Boolean.FALSE;

				System.out.println("定时任务异常：" + e.getMessage());
			}
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	public Properties pushExecute(Empj_ProjProgForcast_AFForm model) {
		Properties properties = new MyProperties();

		Long tableId = model.getTableId();
		Empj_ProjProgForcast_AF projProgForcast_AF = empj_ProjProgForcast_AFDao.findById(tableId);
		if (null == projProgForcast_AF) {
			return MyBackInfo.fail(properties, "未查询到有效信息！");
		}

		Empj_ProjProgForcast_DTLForm dtlModel = new Empj_ProjProgForcast_DTLForm();
		List<Empj_ProjProgForcast_DTL> dtlList;
		dtlModel.setTheState(S_TheState.Normal);
		dtlModel.setAfCode(projProgForcast_AF.geteCode());
		dtlModel.setAfEntity(projProgForcast_AF);
		List<Empj_ProjProgForcast_DTL> empj_ProjProgForcast_DTLList = empj_ProjProgForcast_DTLDao
				.findByPage(empj_ProjProgForcast_DTLDao.getQuery(empj_ProjProgForcast_DTLDao.getBasicHQL(), dtlModel));

		for (Empj_ProjProgForcast_DTL dtl : empj_ProjProgForcast_DTLList) {
			commonService.pushProjProgForcastPhoto(projProgForcast_AF, dtl);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

}

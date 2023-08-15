package zhishusz.housepresell.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.xiaominfo.oss.sdk.ReceiveMessage;
import com.xiaominfo.oss.sdk.client.FileBytesResponse;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import zhishusz.housepresell.approvalprocess.ApprovalProcessCallBack_03010101;
import zhishusz.housepresell.controller.form.Empj_ProjectInfoForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Sm_CityRegionInfoDao;
import zhishusz.housepresell.database.dao.Sm_StreetInfoDao;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_ApprovalProcess_Cfg;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_StreetInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.extra.MsgInfo;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_ButtonType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.database.po.toInterface.To_Project;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.MyString;
import zhishusz.housepresell.util.ObjectCopier;
import zhishusz.housepresell.util.ToInterface;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import zhishusz.housepresell.util.picture.MatrixUtil;
import zhishusz.housepresell.util.project.AttachmentJudgeExistUtil;

/*
 * Service更新操作：项目信息
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Empj_ProjectInfoUpdateService
{
	private static final String ADD_BUSI_CODE = "03010101";

	private static final String UPDATE_BUSI_CODE = "03010102";

	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;

	@Autowired
	private Sm_CityRegionInfoDao sm_CityRegionInfoDao;

	@Autowired
	private Sm_StreetInfoDao sm_StreetInfoDao;

	@Autowired
	private Sm_AttachmentBatchAddService sm_AttachmentBatchAddService;

	@Autowired
	private Sm_BusiState_LogAddService logAddService;

	@Autowired
	private Sm_ApprovalProcessService sm_approvalProcessService;

	@Autowired
	private Sm_ApprovalProcessGetService sm_ApprovalProcessGetService;

	@Autowired
	private Sm_PoCompareResult sm_PoCompareResult;

	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;

	@Autowired
	private AttachmentJudgeExistUtil attachmentJudgeExistUtil;

	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;

	public Properties execute(Empj_ProjectInfoForm model)
	{
		Properties properties = new MyProperties();

		Long tableId = model.getTableId();
		Integer theState = S_TheState.Normal;
		// String busiState = S_BusiState.NoRecord; //model.getBusiState()
		Long lastUpdateTimeStamp = System.currentTimeMillis();
		// Long userRecordId = model.getUserRecordId();
		// Long recordTimeStamp = model.getRecordTimeStamp();
		Long developCompanyId = model.getDevelopCompanyId();
		String theName = model.getTheName();
		Long cityRegionId = model.getCityRegionId();
		Long streetId = model.getStreetId();
		String address = model.getAddress();
		String introduction = model.getIntroduction();
		Double longitude = model.getLongitude();
		Double latitude = model.getLatitude();
		String projectLeader = model.getProjectLeader();
		String leaderPhone = model.getLeaderPhone();
		String remark = model.getRemark();
		String buttonType = model.getButtonType();// 提交保存按钮

		if (developCompanyId == null || developCompanyId < 1)
		{
			return MyBackInfo.fail(properties, "开发企业不能为空");
		}
		if (theName == null || theName.length() == 0)
		{
			return MyBackInfo.fail(properties, "项目名称不能为空");
		}
		if (cityRegionId == null || cityRegionId < 1)
		{
			return MyBackInfo.fail(properties, "所属区域不能为空");
		}
		if (streetId == null || streetId < 1)
		{
			return MyBackInfo.fail(properties, "街道不能为空");
		}
		if (address == null || address.length() == 0)
		{
			return MyBackInfo.fail(properties, "项目地址不能为空");
		}
		if (longitude == null || longitude < 1)
		{
			return MyBackInfo.fail(properties, "经度不能为空");
		}
		if (latitude == null || latitude < 1)
		{
			return MyBackInfo.fail(properties, "纬度不能为空");
		}
		if (projectLeader == null || projectLeader.length() == 0)
		{
			return MyBackInfo.fail(properties, "项目负责人不能为空");
		}
		if (leaderPhone == null || leaderPhone.length() == 0)
		{
			return MyBackInfo.fail(properties, "项目负责人联系电话不能为空");
		}
		if (leaderPhone != null && !MyString.getInstance().checkPhoneNumber(leaderPhone))
		{
			return MyBackInfo.fail(properties, "项目负责人联系电话格式不正确");
		}
		// if (!MyString.getInstance().checkPhoneNumber(leaderPhone))
		// {
		// return MyBackInfo.fail(properties, "项目负责人电话格式不正确");
		// }
		if (introduction == null || introduction.length() == 0)
		{
			return MyBackInfo.fail(properties, "项目简介不能为空");
		}
		if (introduction != null && introduction.length() > 200)
		{
			return MyBackInfo.fail(properties, "项目简介长度不能超过200字");
		}
		if (remark != null && remark.length() > 200)
		{
			return MyBackInfo.fail(properties, "项目备注长度不能超过200字");
		}

		MsgInfo msgInfo = attachmentJudgeExistUtil.isExist(model);
		if (!msgInfo.isSuccess())
		{
			return MyBackInfo.fail(properties, msgInfo.getInfo());
		}

		Sm_User userUpdate = (Sm_User) model.getUser();
		if (userUpdate == null)
		{
			return MyBackInfo.fail(properties, "操作人不存在，请先登录");
		}
		// Emmp_CompanyInfo developCompany =
		// (Emmp_CompanyInfo)emmp_CompanyInfoDao.findById(developCompanyId);
		// if(developCompany == null)
		// {
		// return MyBackInfo.fail(properties, "开发企业不存在");
		// }
		Sm_CityRegionInfo cityRegion = (Sm_CityRegionInfo) sm_CityRegionInfoDao.findById(cityRegionId);
		if (cityRegion == null)
		{
			return MyBackInfo.fail(properties, "区域不存在");
		}
		Sm_StreetInfo street = (Sm_StreetInfo) sm_StreetInfoDao.findById(streetId);
		if (street == null)
		{
			return MyBackInfo.fail(properties, "街道不存在");
		}

		Empj_ProjectInfo empj_ProjectInfo = empj_ProjectInfoDao.findByIdWithClear(tableId);
		if (empj_ProjectInfo == null)
		{
			return MyBackInfo.fail(properties, "项目信息不存在");
		}
		// Integer nameTotalCount =
		// empj_ProjectInfoDao.findByPage_Size(empj_ProjectInfoDao.getQuery_Size(empj_ProjectInfoDao.getSameProjectNameListHQL(),
		// model));
		// if (nameTotalCount > 0)
		// {
		// return MyBackInfo.fail(properties, "项目名称重复");
		// }

		Empj_ProjectInfo empj_ProjectInfoOld = ObjectCopier.copy(empj_ProjectInfo);

		empj_ProjectInfo.setTheState(theState);
		empj_ProjectInfo.setUserUpdate(userUpdate);
		empj_ProjectInfo.setLastUpdateTimeStamp(lastUpdateTimeStamp);
		// empj_ProjectInfo.setDevelopCompany(developCompany);
		// empj_ProjectInfo.seteCodeOfDevelopCompany(developCompany.geteCode());
		empj_ProjectInfo.setTheName(theName);
		empj_ProjectInfo.setCityRegion(cityRegion);
		empj_ProjectInfo.setStreet(street);
		empj_ProjectInfo.setAddress(address);
		empj_ProjectInfo.setLongitude(longitude);
		empj_ProjectInfo.setLatitude(latitude);
		empj_ProjectInfo.setProjectLeader(projectLeader);
		empj_ProjectInfo.setLeaderPhone(leaderPhone);
		empj_ProjectInfo.setIntroduction(introduction);
		empj_ProjectInfo.setRemark(remark);

		Boolean isSame = sm_PoCompareResult.execute(empj_ProjectInfoOld, empj_ProjectInfo);
		if (isSame)
		{
			for (Sm_AttachmentForm formOSS : model.getGeneralAttachmentList())
			{
				// 如果有form没有tableId，说明有新增
				if (formOSS.getTableId() == null || formOSS.getTableId() == 0)
				{
					isSame = false;// 有新增不一样
					break;
				}
			}
			if (isSame) // 如果没有新增再看有没有删除
			{
				Integer totalCountNew = model.getGeneralAttachmentList().length;

				Sm_AttachmentForm theForm = new Sm_AttachmentForm();
				theForm.setTheState(S_TheState.Normal);
				theForm.setBusiType("03010101");
				theForm.setSourceId(MyString.getInstance().parse(model.getTableId()));

				Integer totalCount = sm_AttachmentDao
						.findByPage_Size(sm_AttachmentDao.getQuery_Size(sm_AttachmentDao.getBasicHQL(), theForm));

				if (totalCountNew < totalCount)
				{
					isSame = false;// 有删除不一样
				}
			}
		}

		// 审批流
		/**
		 * TODO
		 * 已备案：走变更审批流程（BUSI_CODE = xxxx02），其他和新增一样，但不要将数据保存到PO
		 * 未备案：点击保存，直接保存到数据，不需要走审批流，因为当前审批流程状态是待提交（审核中不允许编辑，已完结审批状态是已备案）
		 * 点击提交，走新增审批流程（BUSI_CODE = xxxx01），其他和新增一样
		 * 更新时已备案的项目做日志相关操作
		 */
		// 如果是未备案则先保存到数据库然后根据是提交按钮还是保存按钮判断是否走新增的审批流
		if (S_BusiState.NoRecord.equals(empj_ProjectInfo.getBusiState()))
		{
			empj_ProjectInfoDao.update(empj_ProjectInfo);
			sm_AttachmentBatchAddService.execute(model, tableId);

			// 如果是提交按钮则需要走新增的审批流
			if (S_ButtonType.Submit.equals(buttonType))
			{
				properties = sm_ApprovalProcessGetService.execute(ADD_BUSI_CODE, model.getUserId());
				// 判断是否满足审批条件（有审批角色，单审批流程）
				if ("fail".equals(properties.getProperty(S_NormalFlag.result)))
				{
					return properties;
				}
				// 配置审批流程需走审批流
				if (!"noApproval".equals(properties.getProperty(S_NormalFlag.info)))
				{
					// 审批操作
					Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
							.get("sm_approvalProcess_cfg");
					sm_approvalProcessService.execute(empj_ProjectInfo, model, sm_approvalProcess_cfg);
				}
				else
				{
					empj_ProjectInfo.setUserRecord(userUpdate);
					empj_ProjectInfo.setRecordTimeStamp(lastUpdateTimeStamp); // 已备案的添加备案人、备案日期
					empj_ProjectInfo.setBusiState(S_BusiState.HaveRecord); // 已备案
					empj_ProjectInfo.setApprovalState(S_ApprovalState.Completed); // 已完结
					empj_ProjectInfoDao.update(empj_ProjectInfo);
					sm_AttachmentBatchAddService.execute(model, tableId);

					// 推送数据到门户网站
					/*Boolean interFaceAction = toInterFaceAction(empj_ProjectInfo, empj_ProjectInfo.geteCode(),
							model.getGeneralAttachmentList());*/
					/*Boolean interFaceAction = toInterFaceAction(empj_ProjectInfo, String.valueOf(empj_ProjectInfo.getTableId()),
							model.getGeneralAttachmentList());

					if (!interFaceAction)
					{
						properties.put(S_NormalFlag.result, S_NormalFlag.fail);
						properties.put(S_NormalFlag.info, "消息推送门户网站失败！");
					}*/
				}
			}
		}
		// 已备案如果信息不一致，需要走变更审批流
		else if (!isSame)
		{
			properties = sm_ApprovalProcessGetService.execute(UPDATE_BUSI_CODE, model.getUserId());
			// 没有配置审批流程无需走审批流直接保存数据库
			if ("noApproval".equals(properties.getProperty(S_NormalFlag.info)))
			{
				empj_ProjectInfo.setUserRecord(userUpdate);
				empj_ProjectInfo.setRecordTimeStamp(lastUpdateTimeStamp); // 已备案的添加备案人、备案日期
				empj_ProjectInfo.setBusiState(S_BusiState.HaveRecord); // 已备案
				empj_ProjectInfo.setApprovalState(S_ApprovalState.Completed); // 已完结
				empj_ProjectInfoDao.update(empj_ProjectInfo);
				sm_AttachmentBatchAddService.execute(model, tableId);

				// 推送数据到门户网站
				/*Boolean interFaceAction = toInterFaceAction(empj_ProjectInfo, empj_ProjectInfo.geteCode(),
						model.getGeneralAttachmentList());*/
				/*Boolean interFaceAction = toInterFaceAction(empj_ProjectInfo, String.valueOf(empj_ProjectInfo.getTableId()),
						model.getGeneralAttachmentList());

				if (!interFaceAction)
				{
					properties.put(S_NormalFlag.result, S_NormalFlag.fail);
					properties.put(S_NormalFlag.info, "消息推送门户网站失败！");
				}*/

				Empj_ProjectInfo empj_ProjectInfoNew = ObjectCopier.copy(empj_ProjectInfo);
				logAddService.addLog(model, tableId, empj_ProjectInfoOld, empj_ProjectInfoNew);
			}
			else
			{
				if ("fail".equals(properties.getProperty(S_NormalFlag.result)))
				{
					return properties;
				}

				// 做一个还原操作（审批流会有一个sava操作，防止上面的set操作将变更后的数据直接保存到PO中
				try
				{
					PropertyUtils.copyProperties(empj_ProjectInfo, empj_ProjectInfoOld);
				}
				catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
				{
					e.printStackTrace();
				}

				// 审批操作
				Sm_ApprovalProcess_Cfg sm_approvalProcess_cfg = (Sm_ApprovalProcess_Cfg) properties
						.get("sm_approvalProcess_cfg");
				sm_approvalProcessService.execute(empj_ProjectInfo, model, sm_approvalProcess_cfg);
			}
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}

	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;

	private Log log = LogFactory.getCurrentLogFactory().createLog(Empj_ProjectInfoUpdateService.class);

	@Autowired
	private OssServerUtil ossUtil;

	/**
	 * 系统推送数据到门户网站
	 * 
	 * @param model
	 * @param eCode
	 * @param attachmentList
	 */
	public Boolean toInterFaceAction(Empj_ProjectInfo empj_ProjectInfo, String eCode,
			Sm_AttachmentForm[] attachmentList)
	{
		
		// 查询地址
		Sm_BaseParameterForm baseParameterForm0 = new Sm_BaseParameterForm();
		baseParameterForm0.setTheState(S_TheState.Normal);
		baseParameterForm0.setTheValue("69004");
		baseParameterForm0.setParametertype("69");
		Sm_BaseParameter baseParameter0 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm0));

		if (null == baseParameter0)
		{
			log.equals("未查询到配置路径！");

			return false;
		}
				
		// 查询水印图片位置
		Sm_BaseParameterForm baseParameterForm = new Sm_BaseParameterForm();
		baseParameterForm.setTheState(S_TheState.Normal);
		baseParameterForm.setTheValue("69003");
		baseParameterForm.setParametertype("69");
		Sm_BaseParameter baseParameter = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm));

		if (null == baseParameter)
		{
			log.equals("未查询到水印图片位置！");

			return false;
		}

		// 查询缩放比列
		Sm_BaseParameterForm baseParameterForm2 = new Sm_BaseParameterForm();
		baseParameterForm2.setTheState(S_TheState.Normal);
		baseParameterForm2.setTheValue("69001");
		baseParameterForm2.setParametertype("69");
		Sm_BaseParameter baseParameter2 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm2));

		if (null == baseParameter2)
		{
			log.equals("未查询到缩放限定宽度！");

			return false;
		}

		Sm_BaseParameterForm baseParameterForm3 = new Sm_BaseParameterForm();
		baseParameterForm3.setTheState(S_TheState.Normal);
		baseParameterForm3.setTheValue("69001");
		baseParameterForm3.setParametertype("69");
		Sm_BaseParameter baseParameter3 = sm_BaseParameterDao
				.findOneByQuery_T(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), baseParameterForm3));

		if (null == baseParameter3)
		{
			log.equals("未查询到缩放限定高度！");

			return false;
		}

		To_Project project = new To_Project();
		project.setAction("edit");
		project.setCate("pj");
		project.setPj_title(empj_ProjectInfo.getTheName());
		project.setTs_pj_id(eCode);
		project.setPj_area_name(empj_ProjectInfo.getCityRegion().getTheName());
		project.setTs_area_id(String.valueOf(empj_ProjectInfo.getCityRegion().getTableId()));
		project.setPj_kfs(empj_ProjectInfo.getDevelopCompany().getTheName());
		project.setPj_dz(empj_ProjectInfo.getAddress());

		StringBuffer buffer = new StringBuffer();

		if (attachmentList != null && attachmentList.length > 0)
		{
			for (Sm_AttachmentForm attachmentForm : attachmentList)
			{
				String sourceType = attachmentForm.getSourceType();

				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sourceType);
				Sm_AttachmentCfg sm_AttachmentCfg = sm_AttachmentCfgDao
						.findOneByQuery_T(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), form));

				if (null != sm_AttachmentCfg)
				{
					if (sm_AttachmentCfg.getTheName().contains("鸟瞰图"))
					{
						MatrixUtil picUtil = new MatrixUtil();
						String deskURL = picUtil.compressionPic(attachmentForm.getTheLink(), baseParameter.getTheName(),
								baseParameter2.getTheName(), baseParameter3.getTheName());

						// 保存oss
						ReceiveMessage upload = ossUtil.upload(deskURL);

						if (null == upload)
						{
							log.equals("连接Oss-Server失败！");

							return false;
						}

						FileBytesResponse ossMessage = upload.getData().get(0);

						if (null == ossMessage)
						{
							log.equals("上传文件失败！");

							return false;
						}

						// 删除文件
						MatrixUtil.deleteFile(deskURL);

						String httpUrl = ossMessage.getUrl();

						buffer.append(httpUrl);
					}
				}
			}
		}

		project.setPj_pic(buffer.toString());
		project.setPj_content(empj_ProjectInfo.getIntroduction());
		project.setTs_jl_name("");
		project.setTs_jl_id("");
		project.setPj_ysz("");
		project.setPj_longitude(Double.toString(empj_ProjectInfo.getLongitude()));
		project.setPj_latitude(Double.toString(empj_ProjectInfo.getLatitude()));

		Gson gson = new Gson();

		String jsonMap = gson.toJson(project);

		System.out.println(jsonMap);

		String decodeStr = Base64Encoder.encode(jsonMap);

		System.out.println(decodeStr);

		ToInterface toFace = new ToInterface();

		return toFace.interfaceUtil(decodeStr,baseParameter0.getTheName());
	}
}

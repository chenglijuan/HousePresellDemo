package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Tgpj_BuildingAccountForm;
import zhishusz.housepresell.controller.form.Tgxy_EscrowAgreementForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpj_BuildingAccountDao;
import zhishusz.housepresell.database.dao.Tgxy_EscrowAgreementDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BuildingAccount;
import zhishusz.housepresell.database.po.Tgxy_EscrowAgreement;
import zhishusz.housepresell.database.po.state.S_ApplyState;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：托管合作协议
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgxy_EscrowAgreementAddService
{
	// 贷款托管合作协议
	private static final String BUSI_CODE = "06110201";
	@Autowired
	private Tgxy_EscrowAgreementDao tgxy_EscrowAgreementDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;// eCode生成规则
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	@Autowired
	private Tgpj_BuildingAccountDao tgpj_BuildingAccountDao;// 楼幢账户

	public Properties execute(Tgxy_EscrowAgreementForm model)
	{
		Properties properties = new MyProperties();

		/*
		 * xsz by time 2018-8-28 15:12:07 新增贷款托管合作协议
		 * 
		 * 1.贷款托管合作协议基本信息
		 * 2.附件信息
		 * 
		 * 接收前端字段:
		 * 创建人Id（userStartId）、协议版本（agreementVersion）、签约申请日期（
		 * contractApplicationDate）、项目Id（projectId）、
		 * 楼幢信息Id（idArr）、其它约定事项（OtherAgreedMatters）、争议解决方式（disputeResolution）
		 * 
		 * 后台默认字段：
		 * 1.协议状态（agreementState）：新增时默认为：0--正常
		 * 
		 * 2.创建时间（createTimeStamp）、最后修改时间（lastUpdateTimeStamp）：取系统当前时间
		 * 
		 * 3.创建人（userStart）、修改人（userUpdate）：新增时都是创建人Id
		 * 
		 * 其他注意字段：
		 * 1.编号（eCode）:新增时自动生成，规则待定
		 * ==》业务编号+N+YY+MM+DD+日自增长流水号（5位）
		 * 具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
		 * 贷款托管合作协议签署
		 * 06110201
		 * 
		 * 2.协议编号（eCodeOfAgreement）：自动编号，详细规则见需求文件
		 * -->自动编号触发时间：托管协议编号生成时点为法务部门审核通过时生成
		 * 
		 * 3.开发企业信息：保存时由项目信息带出
		 * 
		 * 4.所属区域（cityRegion）：保存时由项目信息带出
		 * 
		 * 5.项目名称：保存时由项目信息带出
		 * 
		 * 6.业务流程状态（businessProcessState）：根据业务申请流程自动维护，初始保存有 （1--保存 2--提交）
		 * 
		 * 7.楼幢编号-拼接（buildingInfoCodeList）：保存时由多个楼幢信息拼接
		 * 
		 * -->子表信息(楼幢表：Empj_BuildingInfo)
		 * 楼幢号（国土）（eCodeOfLand）、施工编号（eCodeFromConstruction）、公安编号（
		 * eCodeFromPublicSecurity）
		 * 
		 * 
		 * 相关校验：
		 * 1.申请托管的楼幢必须是已备案且未签署托管合作协议的楼幢。
		 * 
		 * 
		 */

		// Long userStartId = model.getUserStartId();// 创建人id
		Long userStartId = model.getUserId();
		String escrowCompany = model.getEscrowCompany();// 托管机构
		String agreementVersion = model.getAgreementVersion();// 协议版本
		String contractApplicationDate = model.getContractApplicationDate();// 签约申请日期
		String OtherAgreedMatters = model.getOtherAgreedMatters(); // 其它约定事项
		String disputeResolution = model.getDisputeResolution(); // 争议解决方式
		// String businessProcessState = model.getBusinessProcessState(); //
		// 业务流程状态

		Long projectId = model.getProjectId(); // 项目id

		// 多选的楼幢Id
		Long[] idArr = model.getIdArr();

		// 一般非空校验
		if (userStartId == null || userStartId < 1)
		{
			return MyBackInfo.fail(properties, "请先进行登录");
		}
		if (escrowCompany == null || escrowCompany.length() == 0)
		{
			return MyBackInfo.fail(properties, "托管机构不能为空");
		}
		if (agreementVersion == null || agreementVersion.length() == 0)
		{
			return MyBackInfo.fail(properties, "未查询到有效的协议版本");
		}
		/*
		 * 暂时注释
		 * ---------------start--------------------
		 */
		// if (contractApplicationDate == null ||
		// contractApplicationDate.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "签约申请日期不能为空");
		// }
		// if (disputeResolution == null || disputeResolution.length() == 0)
		// {
		// return MyBackInfo.fail(properties, "争议解决方式不能为空");
		// }
		/*
		 * 暂时注释
		 * ---------------end--------------------
		 */
		if (projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "请选择项目");
		}
		if (idArr == null || idArr.length == 0)
		{
			return MyBackInfo.fail(properties, "楼幢不能为空");
		}

		// 设置保存对象
		Tgxy_EscrowAgreement tgxy_EscrowAgreement = new Tgxy_EscrowAgreement();
		//审批流程状态
		tgxy_EscrowAgreement.setApprovalState(S_ApprovalState.WaitSubmit);

		// 查询操作人信息
		Sm_User userStart = (Sm_User) sm_UserDao.findById(userStartId);
		if (userStart == null)
		{
			return MyBackInfo.fail(properties, "查询操作人信息为空");
		}

		// 设置操作人、操作时间相关信息
		tgxy_EscrowAgreement.setUserStart(userStart);
		tgxy_EscrowAgreement.setUserUpdate(userStart);
		// 设置当前时间
		tgxy_EscrowAgreement.setCreateTimeStamp(System.currentTimeMillis());
		tgxy_EscrowAgreement.setLastUpdateTimeStamp(System.currentTimeMillis());

		// 查询项目相关信息
		Empj_ProjectInfo project = empj_ProjectInfoDao.findById(projectId);
		if (project == null)
		{
			return MyBackInfo.fail(properties, "查询项目信息为空");
		}

		/*
		 * xsz by time 2018-8-28 16:08:57
		 * 设置项目相关信息：
		 * 项目名称（theNameOfProject）、开发企业信息、区域信息
		 * 
		 */

		// 设置项目相关信息
		tgxy_EscrowAgreement.setProject(project);
		if (null == project.getTheName() || project.getTheName().trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "项目名称信息为空");
		}
		tgxy_EscrowAgreement.setTheNameOfProject(project.getTheName());

		// 设置区域相关信息
		Sm_CityRegionInfo cityRegion = project.getCityRegion();
		if (null == cityRegion)
		{
			return MyBackInfo.fail(properties, "项目所属区域信息为空");
		}
		tgxy_EscrowAgreement.setCityRegion(cityRegion);

		// 设置开发企业相关信息
		Emmp_CompanyInfo developCompany = project.getDevelopCompany();
		if (null == developCompany)
		{
			return MyBackInfo.fail(properties, "项目所属开发企业信息为空");
		}
		tgxy_EscrowAgreement.setDevelopCompany(developCompany);

		String theNameOfDevelopCompany = developCompany.getTheName();
		if (null == theNameOfDevelopCompany || theNameOfDevelopCompany.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "项目所属开发企业名称为空");
		}
		tgxy_EscrowAgreement.setTheNameOfDevelopCompany(theNameOfDevelopCompany);

		String eCodeOfDevelopCompany = developCompany.geteCode();
		if (null == eCodeOfDevelopCompany || eCodeOfDevelopCompany.trim().isEmpty())
		{
			return MyBackInfo.fail(properties, "项目所属开发企业编号为空");
		}
		tgxy_EscrowAgreement.seteCodeOfDevelopCompany(eCodeOfDevelopCompany);

		/*
		 * xsz by time 2018-8-28 16:29:28
		 * 保存子表楼幢信息
		 * 
		 */
		List<Empj_BuildingInfo> buildingInfoList = new ArrayList<Empj_BuildingInfo>();

		StringBuffer str = new StringBuffer();
		StringBuffer str2 = new StringBuffer();

		for (int i = 0; i < idArr.length; i++)
		{

			Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(idArr[i]);

			if (null == empj_BuildingInfo)
			{
				return MyBackInfo.fail(properties, "楼幢Id:" + idArr[i] + "查询信息为空");
			}

			// 校验 申请托管的楼幢必须是已备案（busiState=0）且未签署托管合作协议的楼幢。+++校验条件暂停，待确认
			// if (null == empj_BuildingInfo.getBusiState() ||
			// empj_BuildingInfo.getBusiState().trim().isEmpty()
			// || !"0".equals(empj_BuildingInfo.getBusiState()))
			// {
			// return MyBackInfo.fail(properties, "TableId:" + idArr[i] +
			// "楼幢信息未备案");
			// }

			// 校验楼幢信息在托管合作协议中是否已存在
			// Tgxy_EscrowAgreementForm fModel = new Tgxy_EscrowAgreementForm();
			// fModel.setProjectId(idArr[i]);
			// Integer totalCount = tgxy_EscrowAgreementDao.findByPage_Size(
			// tgxy_EscrowAgreementDao.getQuery_Size(tgxy_EscrowAgreementDao.getBasicHQL(),
			// fModel));
			// if (totalCount > 0)
			// {
			// return MyBackInfo.fail(properties, "TableId:" + idArr[i] +
			// "楼幢信息已签署托管合作协议");
			// }

			buildingInfoList.add(empj_BuildingInfo);

			if (null == empj_BuildingInfo.geteCodeFromConstruction()
					|| empj_BuildingInfo.geteCodeFromConstruction().trim().isEmpty())
			{
				return MyBackInfo.fail(properties, "楼幢Id:" + idArr[i] + "施工编号为空");
			}
			str.append(empj_BuildingInfo.geteCodeFromConstruction() + "，");
			str2.append(null == empj_BuildingInfo.geteCodeFromPublicSecurity()?" ": empj_BuildingInfo.geteCodeFromPublicSecurity()+ "，");
			
		}

		// 拼接楼幢编号及公安编号
		String buildingInfoCodeList = str.toString();
		buildingInfoCodeList = buildingInfoCodeList.substring(0, buildingInfoCodeList.length() - 1);

		String buildingInfoGabhList = str2.toString();
		buildingInfoGabhList = buildingInfoGabhList.substring(0, buildingInfoGabhList.length() - 1);
		
		tgxy_EscrowAgreement.setBuildingInfoList(buildingInfoList);
		tgxy_EscrowAgreement.setBuildingInfoCodeList(buildingInfoCodeList);
		tgxy_EscrowAgreement.setBuildingInfoGabhList(buildingInfoGabhList);

		/*
		 * =====================strart===================
		 * xsz by 2018-10-12 22:32:27
		 * 用于回调函数中，因审批流程有问题，暂时先在此方法中写死，后期直接注释
		 * =====================start===================
		 */
		
		tgxy_EscrowAgreement.seteCode(sm_BusinessCodeGetService.getEscrowAgreementEcode(cityRegion.getTheName()));
		tgxy_EscrowAgreement.seteCodeOfAgreement(tgxy_EscrowAgreement.geteCode());
		/*
		 * =====================end===================
		 * xsz by 2018-10-12 22:32:27 
		 * 用于回调函数中，因审批流程有问题，暂时先在此方法中写死，后期直接注释
		 * =====================end===================
		 */
		
		// 设置保存基本数据
		tgxy_EscrowAgreement.setTheState(S_TheState.Normal);// 状态
		tgxy_EscrowAgreement.setAgreementVersion(agreementVersion);// 协议版本
		tgxy_EscrowAgreement.setEscrowCompany(escrowCompany);// 托管机构
		tgxy_EscrowAgreement.setContractApplicationDate(contractApplicationDate);// 签约申请日期
		tgxy_EscrowAgreement.setOtherAgreedMatters(OtherAgreedMatters);// 其他签约事项
		tgxy_EscrowAgreement.setDisputeResolution(disputeResolution);// 争议解决方式
		tgxy_EscrowAgreement.setBusinessProcessState("1");// 业务流程状态 1--保存 2--提交
		tgxy_EscrowAgreement.setAgreementState("0");// 协议状态默认：0-正常

		Serializable tableId = tgxy_EscrowAgreementDao.save(tgxy_EscrowAgreement);
		
		/*
		 * xsz by time 2018-9-18 14:43:10
		 * 后台整合附件信息
		 */
		String smAttachmentList = null;
		if (null != model.getSmAttachmentList())
		{
			smAttachmentList = model.getSmAttachmentList().toString();

			List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);

			for (Sm_Attachment sm_Attachment : gasList)
			{
				//查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));
				
				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
				
				sm_Attachment.setUserStart(userStart);
				sm_Attachment.setUserUpdate(userStart);
				sm_Attachment.setCreateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setLastUpdateTimeStamp(System.currentTimeMillis());
				sm_Attachment.setSourceId(tableId.toString());// 关联Id
				// sm_Attachment.setBusiType("Tgxy_EscrowAgreement");// 业务类型
				sm_Attachment.setTheState(S_TheState.Normal);
				sm_AttachmentDao.save(sm_Attachment);
			}
		}

		properties.put("tableId", new Long(tableId.toString()));
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

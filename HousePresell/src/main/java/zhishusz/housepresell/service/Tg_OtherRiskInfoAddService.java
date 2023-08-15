package zhishusz.housepresell.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Tg_OtherRiskInfoForm;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tg_OtherRiskInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_CityRegionInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_OtherRiskInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.SocketUtil;
	
/*
 * Service添加操作：其他风险信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_OtherRiskInfoAddService
{
	@Autowired
	private Tg_OtherRiskInfoDao tg_OtherRiskInfoDao;
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;
	@Autowired
	private Sm_BusinessCodeGetService sm_BusinessCodeGetService;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao smAttachmentCfgDao;
	
	private static final String BUSI_CODE = "21020305";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	public Properties execute(Tg_OtherRiskInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Tg_OtherRiskInfo tg_OtherRiskInfo = new Tg_OtherRiskInfo();

		Sm_User user = model.getUser();
		
		Long projectId = model.getProjectId();
		String riskInputDate = model.getRiskInputDate();
		String riskInfo = model.getRiskInfo();
		Boolean isUserd = model.getIsUsed();
		String remark = model.getRemark();
		
		if(projectId == null || projectId < 1)
		{
			return MyBackInfo.fail(properties, "项目信息不能为空");
		}
		if(riskInputDate == null || riskInputDate.length() == 0)
		{
			return MyBackInfo.fail(properties, "风险维护日期不能为空");
		}
		if(riskInfo == null || riskInfo.length() == 0)
		{
			return MyBackInfo.fail(properties, "风险信息不能为空");
		}
		else if( SocketUtil.getInstance().getRealLength(riskInfo) > 4000 )
		{
			return MyBackInfo.fail(properties, "风险信息过长，请精简后保存！");
		}
		if(isUserd == null )
		{
			return MyBackInfo.fail(properties, "请确定是否录用");
		}
		
		Empj_ProjectInfo project = (Empj_ProjectInfo)empj_ProjectInfoDao.findById(projectId);
		if(project == null)
		{
			return MyBackInfo.fail(properties, "项目信息有误");
		}
		else
		{
			tg_OtherRiskInfo.setProject(project);
			tg_OtherRiskInfo.setTheNameOfProject(project.getTheName());
		}
		
		if( null!= remark && SocketUtil.getInstance().getRealLength(remark) > 4000 )
		{
			return MyBackInfo.fail(properties, "备注过长，请精简后保存！");
		}
		
		// 查询开发企业
		Emmp_CompanyInfo emmp_CompanyInfo = project.getDevelopCompany();
		if(emmp_CompanyInfo == null){
			return MyBackInfo.fail(properties, "为查询到有效的开发企业数据！");
		}
		else
		{
			tg_OtherRiskInfo.setDevelopCompany(emmp_CompanyInfo);
			tg_OtherRiskInfo.seteCodeOfDevelopCompany(emmp_CompanyInfo.getTheName());
		}
		
		//查询所属区域
		Sm_CityRegionInfo sm_CityRegionInfo = project.getCityRegion();
		if(sm_CityRegionInfo == null){
			return MyBackInfo.fail(properties, "为查询到有效的区域！");
		}
		else
		{
			tg_OtherRiskInfo.setCityRegion(sm_CityRegionInfo);
			tg_OtherRiskInfo.setTheNameOfCityRegion(sm_CityRegionInfo.getTheName());
		}

		/*
		 * xsz by time 2018-11-21 14:47:06
		 * 判断附件是否必须上传
		 */
		// 判断是否有必传
		Sm_AttachmentCfgForm sm_AttachmentCfgForm = new Sm_AttachmentCfgForm();
		sm_AttachmentCfgForm.setBusiType(BUSI_CODE);
		sm_AttachmentCfgForm.setTheState(S_TheState.Normal);
		List<Sm_AttachmentCfg> sm_AttachmentCfgList = smAttachmentCfgDao
				.findByPage(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), sm_AttachmentCfgForm));

		// 先判断是否有附件传递
		List<Sm_Attachment> attachmentList;
		if (null != model.getSmAttachmentList() && !model.getSmAttachmentList().trim().isEmpty())
		{
			attachmentList = JSON.parseArray(model.getSmAttachmentList().toString(), Sm_Attachment.class);
		}
		else
		{
			attachmentList = new ArrayList<Sm_Attachment>();
		}
		
		if (null != sm_AttachmentCfgList && sm_AttachmentCfgList.size() > 0)
		{

			for (Sm_AttachmentCfg sm_AttachmentCfg : sm_AttachmentCfgList)
			{
				// 根据业务判断是否有必传的附件配置
				if (sm_AttachmentCfg.getIsNeeded())
				{
					Boolean isExistAttachment = false;
	
					if (attachmentList.size() > 0)
					{
	
						for (Sm_Attachment sm_Attachment : attachmentList)
						{
							if (sm_AttachmentCfg.geteCode().equals(sm_Attachment.getSourceType()))
							{
								isExistAttachment = true;
								break;
							}
						}
	
					}
	
					if (!isExistAttachment)
					{
						return MyBackInfo.fail(properties, sm_AttachmentCfg.getTheName() + "未上传,此附件为必须上传附件");
					}
	
				}
			}
		}
		
	
		
		tg_OtherRiskInfo.setTheState(S_TheState.Normal);
		tg_OtherRiskInfo.setBusiState("0");
		tg_OtherRiskInfo.seteCode(sm_BusinessCodeGetService.execute(BUSI_CODE));
		tg_OtherRiskInfo.setUserStart(user);
		tg_OtherRiskInfo.setCreateTimeStamp(System.currentTimeMillis());
		tg_OtherRiskInfo.setUserUpdate(user);
		tg_OtherRiskInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
		tg_OtherRiskInfo.setUserRecord(user);
		tg_OtherRiskInfo.setRecordTimeStamp(System.currentTimeMillis());
		tg_OtherRiskInfo.setRiskInputDate(riskInputDate);
		tg_OtherRiskInfo.setRiskInfo(riskInfo);
		tg_OtherRiskInfo.setIsUsed(isUserd);
		tg_OtherRiskInfo.setRemark(remark);
		Serializable entity = tg_OtherRiskInfoDao.save(tg_OtherRiskInfo);
		
		
		// 附件信息
		String smAttachmentList = null;
		if (null != model.getSmAttachmentList())
		{
			smAttachmentList = model.getSmAttachmentList().toString();
		}

		List<Sm_Attachment> gasList = JSON.parseArray(smAttachmentList, Sm_Attachment.class);

		if (null != gasList && gasList.size() > 0)
		{
			for (Sm_Attachment sm_Attachment : gasList)
			{
				// 查询附件配置表
				Sm_AttachmentCfgForm form = new Sm_AttachmentCfgForm();
				form.seteCode(sm_Attachment.getSourceType());
				Sm_AttachmentCfg sm_AttachmentCfg = smAttachmentCfgDao
						.findOneByQuery_T(smAttachmentCfgDao.getQuery(smAttachmentCfgDao.getBasicHQL(), form));

				sm_Attachment.setAttachmentCfg(sm_AttachmentCfg);
				sm_Attachment.setSourceId(entity.toString());
				sm_Attachment.setTheState(S_TheState.Normal);
				smAttachmentDao.save(sm_Attachment);
			}
		}

		properties.put("tableId", entity.toString());
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

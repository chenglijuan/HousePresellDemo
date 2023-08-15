package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tg_OtherRiskInfoForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tg_OtherRiskInfoDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Tg_OtherRiskInfo;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：其他风险信息
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_OtherRiskInfoDetailService
{
	@Autowired
	private Tg_OtherRiskInfoDao tg_OtherRiskInfoDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	
	private static final String BUSI_CODE = "21020305";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"


	public Properties execute(Tg_OtherRiskInfoForm model)
	{
		Properties properties = new MyProperties();

		Long tg_OtherRiskInfoId = model.getTableId();
		Tg_OtherRiskInfo tg_OtherRiskInfo = (Tg_OtherRiskInfo)tg_OtherRiskInfoDao.findById(tg_OtherRiskInfoId);
		if(tg_OtherRiskInfo == null || S_TheState.Deleted.equals(tg_OtherRiskInfo.getTheState()))
		{
			return MyBackInfo.fail(properties, "其他风险信息不存在");
		}
		
		List<Sm_Attachment> smAttachmentList = new ArrayList<Sm_Attachment>();
		
		Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();

		String sourceId = String.valueOf(tg_OtherRiskInfoId);
		attachmentForm.setBusiType(BUSI_CODE);
		attachmentForm.setSourceId(sourceId);
		attachmentForm.setTheState(S_TheState.Normal);
		
		// 查询附件
		smAttachmentList = smAttachmentDao
				.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), attachmentForm));

		if (null == smAttachmentList || smAttachmentList.size() == 0)
		{
			smAttachmentList = new ArrayList<Sm_Attachment>();
		}
		
		// 查询附件类型
		List<Sm_Attachment> smList = null;
		Sm_AttachmentCfgForm attachmentCfgForm = new Sm_AttachmentCfgForm();
		attachmentCfgForm.setTheState(S_TheState.Normal);
		attachmentCfgForm.setBusiType(BUSI_CODE);
		List<Sm_AttachmentCfg> smAttachmentCfgList = sm_AttachmentCfgDao
				.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), attachmentCfgForm));
		if (null == smAttachmentCfgList || smAttachmentCfgList.size() == 0)
		{
			smAttachmentCfgList = new ArrayList<Sm_AttachmentCfg>();
		}
		else
		{
			for (Sm_Attachment sm_Attachment : smAttachmentList)
			{
				Long cfgTableId = sm_Attachment.getAttachmentCfg().getTableId();

				for (Sm_AttachmentCfg sm_AttachmentCfg : smAttachmentCfgList)
				{
					if (sm_AttachmentCfg.getTableId() == cfgTableId)
					{
						smList = sm_AttachmentCfg.getSmAttachmentList();
						if (null == smList || smList.size() == 0)
						{
							smList = new ArrayList<Sm_Attachment>();
						}
						smList.add(sm_Attachment);
						sm_AttachmentCfg.setSmAttachmentList(smList);
					}
				}
			}
		}
		
		properties.put("smAttachmentCfgList", smAttachmentCfgList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tg_OtherRiskInfo", tg_OtherRiskInfo);

		return properties;
	}
}

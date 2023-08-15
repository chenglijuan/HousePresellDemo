package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskLetterForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskLetterReceiverForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterReceiverDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Tg_PjRiskLetter;
import zhishusz.housepresell.database.po.Tg_PjRiskLetterReceiver;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service详情：项目风险函
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskLetterDetailService
{
	@Autowired
	private Tg_PjRiskLetterDao tg_PjRiskLetterDao;
	@Autowired
	private Tg_PjRiskLetterReceiverDao tg_PjRiskLetterReceiverDao;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;

	public Properties execute(Tg_PjRiskLetterForm model)
	{
		Properties properties = new MyProperties();
		
		//获取业务编号
		String busiCode = model.getBusiCode();
		
		List<Sm_Attachment> smAttachmentList = new ArrayList<Sm_Attachment>();
				
		Long tg_PjRiskLetterId = model.getTableId();
		Tg_PjRiskLetter tg_PjRiskLetter = (Tg_PjRiskLetter)tg_PjRiskLetterDao.findById(tg_PjRiskLetterId);
		if(tg_PjRiskLetter == null || S_TheState.Deleted.equals(tg_PjRiskLetter.getTheState()))
		{
			return MyBackInfo.fail(properties, "项目风险函不存在，请核实！");
		}
		
		// 查询所有发送邮件的对象
		Tg_PjRiskLetterReceiverForm tg_PjRiskLetterReceiverForm = new Tg_PjRiskLetterReceiverForm();
		tg_PjRiskLetterReceiverForm.setTheState(S_TheState.Normal);
		tg_PjRiskLetterReceiverForm.setTg_PjRiskLetter(tg_PjRiskLetter);
		tg_PjRiskLetterReceiverForm.setSendWay(1);
		
		Integer totalCount = tg_PjRiskLetterReceiverDao.findByPage_Size(tg_PjRiskLetterReceiverDao.getQuery_Size(tg_PjRiskLetterReceiverDao.getBasicHQL(), tg_PjRiskLetterReceiverForm));
	
		List<Tg_PjRiskLetterReceiver> tg_PjRiskLetterReceiverList;
		if(totalCount > 0)
		{
			tg_PjRiskLetterReceiverList = tg_PjRiskLetterReceiverDao.findByPage(tg_PjRiskLetterReceiverDao.getQuery(tg_PjRiskLetterReceiverDao.getBasicHQL(), tg_PjRiskLetterReceiverForm));
		}
		else
		{
			tg_PjRiskLetterReceiverList = new ArrayList<Tg_PjRiskLetterReceiver>();
		}
		
		Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();

		String sourceId = String.valueOf(tg_PjRiskLetterId);
		attachmentForm.setBusiType(busiCode);
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
		attachmentCfgForm.setBusiType(busiCode);
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
				
		properties.put("tg_PjRiskLetter", tg_PjRiskLetter);
		properties.put("tg_PjRiskLetterReceiverList", tg_PjRiskLetterReceiverList);
		properties.put("smAttachmentCfgList", smAttachmentCfgList);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/*
 * Service列表查询：附件配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_AttachmentCfgListService
{
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_AttachmentDao sm_AttachmentDao;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Sm_AttachmentCfgForm model)
	{
		model.setTheState(S_TheState.Normal);
		Properties properties = new MyProperties();
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		
		Integer totalCount = sm_AttachmentCfgDao.findByPage_Size(sm_AttachmentCfgDao.getQuery_Size(sm_AttachmentCfgDao.getBasicHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Sm_AttachmentCfg> sm_AttachmentCfgList;
		if(totalCount > 0)
		{
			sm_AttachmentCfgList = sm_AttachmentCfgDao.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			sm_AttachmentCfgList = new ArrayList<Sm_AttachmentCfg>();
		}

		//************* 查询附件 START**********
		//判断是否有传BusiType 和 SourceId
		if (model.getBusiType() != null && model.getBusiType().length() > 0 &&
				model.getSourceId() != null && model.getSourceId().length() > 0) 
		{
			Sm_AttachmentForm from = new Sm_AttachmentForm();

			from.setBusiType(model.getBusiType());
			from.setSourceId(model.getSourceId());
			from.setTheState(S_TheState.Normal);

			List<Sm_Attachment> sm_AttachmentList = sm_AttachmentDao
					.findByPage(sm_AttachmentDao.getQuery(sm_AttachmentDao.getBasicHQL2(), from));

			if (sm_AttachmentList == null|| sm_AttachmentList.size() == 0)
			{
				sm_AttachmentList = new ArrayList<>();
				for (Sm_AttachmentCfg sm_AttachmentCfg : sm_AttachmentCfgList)
				{
					sm_AttachmentCfg.setSmAttachmentList(sm_AttachmentList);
				}
			}

			for (Sm_Attachment sm_Attachment : sm_AttachmentList)
			{
				Long cfgTableId = sm_Attachment.getAttachmentCfg().getTableId();

				for (Sm_AttachmentCfg sm_AttachmentCfg : sm_AttachmentCfgList)
				{
					if (cfgTableId.equals(sm_AttachmentCfg.getTableId()))
					{
						List<Sm_Attachment> smList = sm_AttachmentCfg.getSmAttachmentList();
						if (smList == null || smList.size() == 0)
						{
							smList = new ArrayList<>();
						}
						smList.add(sm_Attachment);
						sm_AttachmentCfg.setSmAttachmentList(smList);
					}
				}
			}
		}
		else
		{
			for(Sm_AttachmentCfg sm_AttachmentCfg : sm_AttachmentCfgList)
			{
					sm_AttachmentCfg.setSmAttachmentList(new ArrayList<Sm_Attachment>());
			}
		}
		//************* 查询附件 END**********
		
		/*
		 * 返回用户签章信息
		 */
		Sm_User user = model.getUser();
		Map<String, String> map = new HashMap<>();
		map.put("isSignature", null == user.getIsSignature()?"0":user.getIsSignature());
		map.put("ukeyNumber", null == user.getUkeyNumber()?"":user.getUkeyNumber());
		properties.put("signatureInfo", map);

		properties.put("sm_AttachmentCfgList", sm_AttachmentCfgList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

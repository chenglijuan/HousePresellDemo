package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_AcceptFileType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service业务分类操作：附件配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_AttachmentCfgToYwService
{
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	
	public Properties execute(Sm_AttachmentCfgForm model)
	{
		Properties properties = new MyProperties();
		
		Long tableid= model.getTableId();
		if (tableid == null)
		{
			return MyBackInfo.fail(properties, "没有需要删除的信息");
		}	
		Sm_BaseParameter sm_BaseParameter=	sm_BaseParameterDao.findById(tableid);
		if(sm_BaseParameter==null){
			return MyBackInfo.fail(properties, "选中的信息不存在");
		}
		
		//查询没有删除的数据
		model.setTheState(S_TheState.Normal);
		model.setBusiType(sm_BaseParameter.getTheValue());
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());		
		
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
		
		properties.put("sm_BaseParameter", sm_BaseParameter);
		properties.put("sm_AttachmentCfgList", sm_AttachmentCfgList);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

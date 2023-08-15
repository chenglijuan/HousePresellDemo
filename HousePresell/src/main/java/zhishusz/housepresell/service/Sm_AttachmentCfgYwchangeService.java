package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_AcceptFileType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service业务分类操作：附件配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Sm_AttachmentCfgYwchangeService
{
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	
	public Properties execute(Sm_AttachmentCfgForm model,String busiTypeId)
	{
		Properties properties = new MyProperties();
		
		Sm_BaseParameterForm BaseParameterFrom=new Sm_BaseParameterForm();
		BaseParameterFrom.setTheName(busiTypeId);	
		Sm_BaseParameter baseParameter=new Sm_BaseParameter();
		//根据业务编码找到对应的业务类型
		@SuppressWarnings("unchecked")
		Query<Sm_BaseParameter> query = sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getTheValueHQL(), BaseParameterFrom);					
		List<Sm_BaseParameter> list =query.list();
		for(Sm_BaseParameter Sm_BaseParameter:list){		
			model.setBusiType(Sm_BaseParameter.getTheValue());	
			//获取创建人
			baseParameter.setUserStart(Sm_BaseParameter.getUserStart());
			//获取创建时间
			baseParameter.setCreateTimeStamp(Sm_BaseParameter.getCreateTimeStamp());
			//获取业务类型的id
			baseParameter.setTableId(Sm_BaseParameter.getTableId());
		}
		//查询没有删除的数据
		model.setTheState(S_TheState.Normal);
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

		}else{
			 sm_AttachmentCfgList=new ArrayList<Sm_AttachmentCfg>();
		}		
		
		properties.put("baseParameter", baseParameter);
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

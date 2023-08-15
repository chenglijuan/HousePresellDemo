package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：附件配置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Sm_AttachmentCfgToAddService
{	
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	@Autowired
	private SessionFactory sessionFactory;
	
	public Properties execute(Sm_AttachmentCfgForm model)
	{
		Properties properties = new MyProperties();
		model.setParamenterType("1");
		//获取业务编码表中的信息	
		String  sql="SELECT * FROM Sm_BaseParameter WHERE parameterType='"+model.getParamenterType() +"' and parentParameter IS NOT NULL AND TABLEID not in( SELECT parentParameter FROM  Sm_BaseParameter WHERE parameterType='"+model.getParamenterType()+"' AND parentParameter IS NOT NULL) order by thevalue asc";
		List<Sm_BaseParameter> sm_BaseParameterlist=new ArrayList<Sm_BaseParameter>();
		sm_BaseParameterlist=sessionFactory.getCurrentSession().createNativeQuery(sql, Sm_BaseParameter.class).getResultList();
		
		properties.put("theNamelist",sm_BaseParameterlist);	
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		return properties;
	}
}

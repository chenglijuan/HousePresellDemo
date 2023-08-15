package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementVerMngForm;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementVerMngDao;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementVerMng;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：三方协议版本管理
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgxy_TripleAgreementVerMngToAddsService
{
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private Tgxy_CoopAgreementVerMngDao tgxy_CoopAgreementVerMngDao;
	
	public Properties execute(Tgxy_CoopAgreementVerMngForm model)
	{
		Properties properties = new MyProperties();
		model.setTheState(S_TheState.Normal);
		if(model.getTheType()==null){
			model.setTheType("4");
		}
		String sql="select * from Tgxy_CoopAgreementVerMng where 1=1 and theState=0 and thetype="+model.getTheType()+" and enabletimestamp < = TO_CHAR(SYSDATE,'YYYY-MM-DD') and (downtimestamp >= TO_CHAR(SYSDATE,'YYYY-MM-DD') or downTimeStamp is null ) and approvalstate='已完结'";
		Tgxy_CoopAgreementVerMng tgxy_CoopAgreementVerMng=new Tgxy_CoopAgreementVerMng();		
		List<Tgxy_CoopAgreementVerMng> tgxy_CoopAgreementVerMnglist=sessionFactory.getCurrentSession().createNativeQuery(sql,Tgxy_CoopAgreementVerMng.class).getResultList();
		if(tgxy_CoopAgreementVerMnglist.size()>0){
			tgxy_CoopAgreementVerMng=tgxy_CoopAgreementVerMnglist.get(0);
		}else{
			return MyBackInfo.fail(properties, "请先维护合作协议版本管理");
		}
		properties.put("tgxy_CoopAgreementVerMng", tgxy_CoopAgreementVerMng);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

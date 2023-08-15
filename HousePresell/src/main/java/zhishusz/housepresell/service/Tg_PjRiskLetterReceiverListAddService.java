package zhishusz.housepresell.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tg_PjRiskLetterReceiverForm;
import zhishusz.housepresell.database.dao.Emmp_OrgMemberDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterReceiverDao;
import zhishusz.housepresell.database.po.Emmp_OrgMember;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskLetterReceiver;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目风险函
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskLetterReceiverListAddService
{
	@Autowired
	private Tg_PjRiskLetterReceiverDao tg_PjRiskLetterReceiverDao;
	@Autowired
	private Sm_UserDao sm_UserDao;

	
	@SuppressWarnings("unchecked")
	public Properties execute(Tg_PjRiskLetterReceiverForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User user = model.getUser();
		
		Long[] idArr = model.getIdArr();
		
		if( null == idArr || idArr.length == 0)
		{
			return MyBackInfo.fail(properties, "没有需要新增的内容！");
		}
		else
		{
			for(int i = 0; i < idArr.length ;i++)
			{
				
				Sm_User emmp_OrgMember = (Sm_User)sm_UserDao.findById(idArr[i]);
				if(emmp_OrgMember == null || S_TheState.Deleted.equals(emmp_OrgMember.getTheState()))
				{
					return MyBackInfo.fail(properties, "存在不合规范的机构成员");
				}
				
				Tg_PjRiskLetterReceiver tg_PjRiskLetterReceiver = new Tg_PjRiskLetterReceiver();
				
				tg_PjRiskLetterReceiver.setTheState(S_TheState.Normal);
//				tg_PjRiskLetterReceiver.setBusiState(S_TheState.Normal);
//				tg_PjRiskLetterReceiver.seteCode();
				tg_PjRiskLetterReceiver.setUserStart(user);
				tg_PjRiskLetterReceiver.setCreateTimeStamp(System.currentTimeMillis());
				tg_PjRiskLetterReceiver.setUserUpdate(user);
				tg_PjRiskLetterReceiver.setLastUpdateTimeStamp(System.currentTimeMillis());
				tg_PjRiskLetterReceiver.setEmmp_OrgMember(emmp_OrgMember);
				tg_PjRiskLetterReceiver.setEmmp_CompanyInfo(emmp_OrgMember.getCompany());
				tg_PjRiskLetterReceiver.setTheNameOfDepartment(emmp_OrgMember.getTheNameOfDepartment());
//				tg_PjRiskLetterReceiver.setTheType(emmp_OrgMember.getTheType());
				tg_PjRiskLetterReceiver.setTheName(emmp_OrgMember.getTheName());
				tg_PjRiskLetterReceiver.setRealName(emmp_OrgMember.getRealName());
//				tg_PjRiskLetterReceiver.setPositionName(emmp_OrgMember.getPositionName());
				tg_PjRiskLetterReceiver.setEmail(emmp_OrgMember.getEmail());
				tg_PjRiskLetterReceiver.setSendWay(0);
		
				tg_PjRiskLetterReceiverDao.save(tg_PjRiskLetterReceiver);
			}
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

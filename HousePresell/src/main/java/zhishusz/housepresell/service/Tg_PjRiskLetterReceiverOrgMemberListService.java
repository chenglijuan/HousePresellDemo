package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Sm_UserForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskLetterReceiverForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterReceiverDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：项目风险函
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tg_PjRiskLetterReceiverOrgMemberListService
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
		
		Emmp_CompanyInfo emmp_CompanyInfo = user.getCompany();
		model.setEmmp_CompanyInfo(emmp_CompanyInfo);

		
		model.setTheState(S_TheState.Normal);
		
		Sm_UserForm emmp_OrgMemberForm = new Sm_UserForm();
		emmp_OrgMemberForm.setCompanyId(emmp_CompanyInfo.getTableId());
		emmp_OrgMemberForm.setTheState(S_TheState.Normal);
		
		Integer totalCount = sm_UserDao.findByPage_Size(sm_UserDao.getQuery_Size(sm_UserDao.getBasicHQL(), emmp_OrgMemberForm));
		
		// 查询未新增的用户成员，新增
		List<Sm_User> emmp_OrgMemberList;
		List<Sm_User> emmp_OrgMemberListReturn = new ArrayList<Sm_User>();
		if(totalCount > 0)
		{
			emmp_OrgMemberList = sm_UserDao.findByPage(sm_UserDao.getQuery(sm_UserDao.getBasicHQL(), emmp_OrgMemberForm));
			
			for(Sm_User emmp_OrgMember : emmp_OrgMemberList)
			{
				Tg_PjRiskLetterReceiverForm tg_PjRiskLetterReceiverForm = new Tg_PjRiskLetterReceiverForm();
				tg_PjRiskLetterReceiverForm.setTheState(S_TheState.Normal);
				tg_PjRiskLetterReceiverForm.setEmmp_OrgMember(emmp_OrgMember);
				
				Integer receiverCount = tg_PjRiskLetterReceiverDao.findByPage_Size(tg_PjRiskLetterReceiverDao.getQuery_Size(tg_PjRiskLetterReceiverDao.getBasicHQL(), tg_PjRiskLetterReceiverForm));

				if(receiverCount == 0)
				{
					emmp_OrgMemberListReturn.add(emmp_OrgMember);
				}
			}		
		}
		else
		{
			emmp_OrgMemberList = new ArrayList<Sm_User>();
		}
		
		properties.put("emmp_OrgMemberList", emmp_OrgMemberListReturn);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

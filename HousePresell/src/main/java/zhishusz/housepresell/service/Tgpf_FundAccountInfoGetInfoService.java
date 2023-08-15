package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Tgpf_FundAccountInfoForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_FundAccountInfoDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_FundAccountInfo;
import zhishusz.housepresell.database.po.state.S_BusiState;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：推送给财务系统-设置
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_FundAccountInfoGetInfoService
{
	@Autowired
	private Tgpf_FundAccountInfoDao tgpf_FundAccountInfoDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	public Properties execute(Tgpf_FundAccountInfoForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User userStart = model.getUser();
		
		// 配置日期
		String configTime = myDatetime.dateToSimpleString(System.currentTimeMillis());

		
		Empj_BuildingInfoForm empj_BuildingInfoForm = new Empj_BuildingInfoForm();
		empj_BuildingInfoForm.setTheState(S_TheState.Normal);
		empj_BuildingInfoForm.setBusiState(S_BusiState.HaveRecord);
		empj_BuildingInfoForm.setBusiState("已备案");
		empj_BuildingInfoForm.setEscrowState(S_EscrowState.HasEscrowState);
		
		Integer buildingCount = empj_BuildingInfoDao.findByPage_Size(empj_BuildingInfoDao.getQuery_Size(empj_BuildingInfoDao.getSpecialHQL(), empj_BuildingInfoForm));

		List<Empj_BuildingInfo> empj_BuildingInfoList;
		if(buildingCount > 0)
		{
			empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getSpecialHQL(), empj_BuildingInfoForm));
			for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
			{
				Tgpf_FundAccountInfoForm tgpf_FundAccountInfoForm = new Tgpf_FundAccountInfoForm();
				
				tgpf_FundAccountInfoForm.setBuilding(empj_BuildingInfo);
				
				Integer fundAccountInfoCount = tgpf_FundAccountInfoDao.findByPage_Size(tgpf_FundAccountInfoDao.getQuery_Size(tgpf_FundAccountInfoDao.getBasicHQL(), tgpf_FundAccountInfoForm));				
				
				if(fundAccountInfoCount > 0)
				{
					continue;
				}
				
				Emmp_CompanyInfo emmp_CompanyInfo = empj_BuildingInfo.getDevelopCompany();
				
				if(null == emmp_CompanyInfo)
				{
					continue;
				}
				
				Empj_ProjectInfo empj_ProjectInfo = empj_BuildingInfo.getProject();
				
				if(null == empj_ProjectInfo)
				{
					continue;
				}
				
				Tgpf_FundAccountInfo tgpf_FundAccountInfo = new Tgpf_FundAccountInfo();
				tgpf_FundAccountInfo.setTheState(S_TheState.Normal);
//				tgpf_FundAccountInfo.setBusiState(busiState);
//				tgpf_FundAccountInfo.seteCode(eCode);
				tgpf_FundAccountInfo.setUserStart(userStart);
				tgpf_FundAccountInfo.setCreateTimeStamp(System.currentTimeMillis());
				tgpf_FundAccountInfo.setUserUpdate(userStart);
				tgpf_FundAccountInfo.setLastUpdateTimeStamp(System.currentTimeMillis());
//				tgpf_FundAccountInfo.setUserRecord(userRecord);
//				tgpf_FundAccountInfo.setRecordTimeStamp(recordTimeStamp);
				tgpf_FundAccountInfo.setCompanyInfo(emmp_CompanyInfo);
				
				try
				{
					tgpf_FundAccountInfo.setTheNameOfCompany(emmp_CompanyInfo.getTheName());
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(empj_BuildingInfo.getTableId());
				}
				
				tgpf_FundAccountInfo.seteCodeOfCompany(emmp_CompanyInfo.geteCodeFromPresellSystem());
				tgpf_FundAccountInfo.setFullNameOfCompanyFromFinanceSystem(emmp_CompanyInfo.getTheName());
				tgpf_FundAccountInfo.setShortNameOfCompanyFromFinanceSystem(emmp_CompanyInfo.getShortName());
				tgpf_FundAccountInfo.setProject(empj_ProjectInfo);
				tgpf_FundAccountInfo.setTheNameOfProject(empj_ProjectInfo.getTheName());
				tgpf_FundAccountInfo.seteCodeOfProject(empj_ProjectInfo.geteCode());
				tgpf_FundAccountInfo.setFullNameOfProjectFromFinanceSystem(empj_ProjectInfo.getTheName());
				tgpf_FundAccountInfo.setShortNameOfProjectFromFinanceSystem(empj_ProjectInfo.getLegalName());
				tgpf_FundAccountInfo.setBuilding(empj_BuildingInfo);
				tgpf_FundAccountInfo.seteCodeFromConstruction(empj_BuildingInfo.geteCodeFromConstruction());
				tgpf_FundAccountInfo.seteCodeOfBuilding(empj_BuildingInfo.geteCodeFromPublicSecurity());
				tgpf_FundAccountInfo.setFullNameOfBuildingFromFinanceSystem(empj_ProjectInfo.getTheName()+"_"+empj_BuildingInfo.geteCodeFromConstruction());
				tgpf_FundAccountInfo.setShortNameOfBuildingFromFinanceSystem(empj_BuildingInfo.geteCodeFromConstruction());
				tgpf_FundAccountInfo.setOperateType(0);
				tgpf_FundAccountInfo.setConfigureUser(userStart.getTheName());
				tgpf_FundAccountInfo.setConfigureTime(configTime);
								
				tgpf_FundAccountInfoDao.save(tgpf_FundAccountInfo);
							
			}		
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

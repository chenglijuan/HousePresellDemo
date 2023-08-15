package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.controller.form.Tgpf_RemainRightForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_BuildingRemainRightLogDao;
import zhishusz.housepresell.database.dao.Tgpf_RemainRightDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpf_BuildingRemainRightLog;
import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.database.po.state.S_ApprovalState;
import zhishusz.housepresell.database.po.state.S_EscrowState;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：初始计算
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BuildingRemainRightPreCalculationService
{
	@Autowired
	private Tgpf_BuildingRemainRightLogDao tgpf_BuildingRemainRightLogDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpf_RemainRightDao tgpf_RemainRightDao;
	@Autowired
	private Tgpf_BuildingRemainRightLogPublicAddService tgpf_BuildingRemainRightLogPublicAddService;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_BuildingRemainRightLogForm model)
	{
		Properties properties = new MyProperties();
		
		
		Empj_BuildingInfoForm empj_BuildingInfoForm = new Empj_BuildingInfoForm();
		empj_BuildingInfoForm.setTheState(S_TheState.Normal);
		empj_BuildingInfoForm.setApprovalState(S_ApprovalState.Completed);
		empj_BuildingInfoForm.setExceptEscrowState(S_EscrowState.UnEscrowState);
		/*empj_BuildingInfoForm.setProjectId(876L);
		empj_BuildingInfoForm.seteCodeFromConstruction("71幢");*/
		
		Integer totalCount = empj_BuildingInfoDao.findByPage_Size(empj_BuildingInfoDao.getQuery_Size(empj_BuildingInfoDao.getBasicHQL(), empj_BuildingInfoForm));
		
		List<Empj_BuildingInfo> empj_BuildingInfoList;
		if(totalCount > 0)
		{
			empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), empj_BuildingInfoForm));
			
			for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
			{
				Long buildingId = empj_BuildingInfo.getTableId();//楼幢Id
				String billTimeStamp = myDatetime.dateToSimpleString(System.currentTimeMillis());//记账日期
				String srcBusiType = "迁移数据手动计算";//来源业务类型			
				
				
				// 查询是否进行过计算，计算过就不计算！
				Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm = new Tgpf_BuildingRemainRightLogForm();
				tgpf_BuildingRemainRightLogForm.setBuildingId(buildingId);
				tgpf_BuildingRemainRightLogForm.setTheState(S_TheState.Normal);
				
				
				List<Tgpf_BuildingRemainRightLog> tgpf_BuildingRemainRightLogList = tgpf_BuildingRemainRightLogDao.findByPage(tgpf_BuildingRemainRightLogDao.createCriteriaForList(tgpf_BuildingRemainRightLogForm));
				
				if( null != tgpf_BuildingRemainRightLogList && tgpf_BuildingRemainRightLogList.size() > 0)
				{
					continue;
				}
				
				Tgpf_BuildingRemainRightLogForm buildingRemainRightLogForm = new Tgpf_BuildingRemainRightLogForm();
				buildingRemainRightLogForm.setSrcBusiType(srcBusiType);
				buildingRemainRightLogForm.setBuildingId(buildingId);
				buildingRemainRightLogForm.setBillTimeStamp(billTimeStamp);
				
				
				properties = tgpf_BuildingRemainRightLogPublicAddService.execute(buildingRemainRightLogForm);	
				System.out.println("------结束时间:"+System.currentTimeMillis());
				
			}
			
		}
		else
		{
			return MyBackInfo.fail(properties, "楼幢已经全部进行留存权益计算！");
		}
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

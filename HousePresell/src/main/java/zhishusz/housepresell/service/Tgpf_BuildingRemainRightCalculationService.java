package zhishusz.housepresell.service;

import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.controller.form.Tgpf_RemainRightForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Tgpf_BuildingRemainRightLogDao;
import zhishusz.housepresell.database.dao.Tgpf_RemainRightDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Tgpf_BuildingRemainRightLog;
import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：重新计算
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tgpf_BuildingRemainRightCalculationService
{
	@Autowired
	private Tgpf_BuildingRemainRightLogDao tgpf_BuildingRemainRightLogDao;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	@Autowired
	private Tgpf_RemainRightDao tgpf_RemainRightDao;
	@Autowired
	private Tgpf_BuildingRemainRightLogPublicAddService tgpf_BuildingRemainRightLogPublicAddService;
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_BuildingRemainRightLogForm model)
	{
		Properties properties = new MyProperties();
		
		Long tableId = model.getTableId();//旧楼幢留存权益
		Long buildingId = model.getBuildingId();//楼幢Id
		String billTimeStamp = model.getBillTimeStamp();//记账日期
		String srcBusiType = model.getSrcBusiType();//来源业务类型
		
		if(tableId == null || tableId < 1)
		{
			return MyBackInfo.fail(properties, "请选择需要重新计算的楼幢留存权益");
		}
		
		if(buildingId == null || buildingId < 1)
		{
			return MyBackInfo.fail(properties, "请选择需要重新计算的楼幢");
		}
		
		if(billTimeStamp == null || billTimeStamp.length() < 1)
		{
			return MyBackInfo.fail(properties, "选择需要重新计算的楼幢留存权益没有记账日期");
		}
		
		Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm = new Tgpf_BuildingRemainRightLogForm();
		tgpf_BuildingRemainRightLogForm.setBuildingId(buildingId);
		tgpf_BuildingRemainRightLogForm.setTheState(S_TheState.Normal);
		
		List<Tgpf_BuildingRemainRightLog> tgpf_BuildingRemainRightLogList = tgpf_BuildingRemainRightLogDao.findByPage(tgpf_BuildingRemainRightLogDao.createCriteriaForList(tgpf_BuildingRemainRightLogForm));
		for(Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog : tgpf_BuildingRemainRightLogList)
		{
			Long timeStamp = MyDatetime.getInstance().stringToLong(tgpf_BuildingRemainRightLog.getBillTimeStamp());
			if(MyDatetime.getInstance().stringToLong(billTimeStamp) < timeStamp)
			{
				return MyBackInfo.fail(properties, "重新计算的留存权益不是最新的留存权益");
			}
		}
		
		Empj_BuildingInfo empj_BuildingInfo = empj_BuildingInfoDao.findById(buildingId);
		if(empj_BuildingInfo == null)
		{
			return MyBackInfo.fail(properties, "选择的楼幢不存在");
		}

		Tgpf_BuildingRemainRightLog tgpf_BuildingRemainRightLog = tgpf_BuildingRemainRightLogDao.findById(tableId);
		if(tgpf_BuildingRemainRightLog == null)
		{
			return MyBackInfo.fail(properties, "选择的楼幢留存权益不存在");
		}
		
		if(srcBusiType.equals(tgpf_BuildingRemainRightLog.getSrcBusiType()))
		{
			return MyBackInfo.fail(properties, "重新计算的留存权益的业务来源类型不能是手工计算的");
		}
		
		tgpf_BuildingRemainRightLog.setTheState(S_TheState.Deleted);
		tgpf_BuildingRemainRightLogDao.save(tgpf_BuildingRemainRightLog);
		
		Tgpf_RemainRightForm tgpf_RemainRightForm = new Tgpf_RemainRightForm();
		tgpf_RemainRightForm.setBuildingRemainRightLogId(tgpf_BuildingRemainRightLog.getTableId());
		List<Tgpf_RemainRight> tgpf_RemainRightList = tgpf_RemainRightDao.findByPage(tgpf_RemainRightDao.getQuery(tgpf_RemainRightDao.getBasicHQL(), tgpf_RemainRightForm));
		
		if(tgpf_RemainRightList != null && tgpf_RemainRightList.isEmpty())
		{
			for(Tgpf_RemainRight tgpf_RemainRight : tgpf_RemainRightList)
			{
				tgpf_RemainRight.setTheState(S_TheState.Deleted);
				tgpf_RemainRightDao.save(tgpf_RemainRight);
			}
		}
		
		properties = tgpf_BuildingRemainRightLogPublicAddService.execute(model);
		
		return properties;
	}
}

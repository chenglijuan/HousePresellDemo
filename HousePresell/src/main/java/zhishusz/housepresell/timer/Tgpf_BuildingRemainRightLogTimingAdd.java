package zhishusz.housepresell.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Empj_BuildingInfoForm;
import zhishusz.housepresell.controller.form.Tgpf_BuildingRemainRightLogForm;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.service.Tgpf_BuildingRemainRightLogPublicAddService;

@Transactional(transactionManager="transactionManager")
@Component("tgpf_BuildingRemainRightLogTimingAdd")  
public class Tgpf_BuildingRemainRightLogTimingAdd 
{  
	@Autowired
	private Tgpf_BuildingRemainRightLogPublicAddService tgpf_BuildingRemainRightLogPublicAddService;
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;
	
	//每日凌晨计算前一天的留存权益
	@SuppressWarnings("unchecked")
	@Scheduled(cron = "0 0 2 * * ?")
	public void execute() 
    {
		System.out.println("定时任务开始");
//		Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm = new Tgpf_BuildingRemainRightLogForm();
//		//取昨天
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//		Date date=new Date();
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		calendar.add(Calendar.DAY_OF_MONTH, -1);
//		date = calendar.getTime();
//		tgpf_BuildingRemainRightLogForm.setBillTimeStamp(sdf.format(date));
//		tgpf_BuildingRemainRightLogForm.setBuildingId(11440L);
//		tgpf_BuildingRemainRightLogForm.setSrcBusiType("自动计算");
//		tgpf_BuildingRemainRightLogPublicAddService.execute(tgpf_BuildingRemainRightLogForm);
		Empj_BuildingInfoForm empj_BuildingInfoForm = new Empj_BuildingInfoForm();
		List<Empj_BuildingInfo> empj_BuildingInfoList = empj_BuildingInfoDao.findByPage(empj_BuildingInfoDao.getQuery(empj_BuildingInfoDao.getBasicHQL(), empj_BuildingInfoForm));
		if(empj_BuildingInfoList != null && !empj_BuildingInfoList.isEmpty())
		{
			for(Empj_BuildingInfo empj_BuildingInfo : empj_BuildingInfoList)
			{
				Tgpf_BuildingRemainRightLogForm tgpf_BuildingRemainRightLogForm = new Tgpf_BuildingRemainRightLogForm();
				//取昨天
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Date date=new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				date = calendar.getTime();
				tgpf_BuildingRemainRightLogForm.setBillTimeStamp(sdf.format(date));
				tgpf_BuildingRemainRightLogForm.setBuildingId(empj_BuildingInfo.getTableId());
				tgpf_BuildingRemainRightLogForm.setSrcBusiType("自动计算");
				tgpf_BuildingRemainRightLogPublicAddService.execute(tgpf_BuildingRemainRightLogForm);
			}
		}
    }
	
	/**
	 * 定时处理航拍图片信息
	 */
	public void handlePicExecute(){
		
	}
}
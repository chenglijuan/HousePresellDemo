package zhishusz.housepresell.service;

import zhishusz.housepresell.controller.form.Sm_BusinessCodeForm;
import zhishusz.housepresell.database.dao.Sm_BusinessCodeDao;
import zhishusz.housepresell.database.po.Sm_BusinessCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

import javax.transaction.Transactional;

/*
 * Service获取：业务编号
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Sm_BusinessCodeGetService
{
	@Autowired
	private Sm_BusinessCodeDao sm_BusinessCodeDao;

	public synchronized String execute(String busiCode)
	{
		Calendar calendar = Calendar.getInstance();
		Integer theYear = calendar.get(Calendar.YEAR);
		Integer theMonth = calendar.get(Calendar.MONTH) + 1;// Calendar的月份是从0开始的
		Integer theDay = calendar.get(Calendar.DAY_OF_MONTH);

		Sm_BusinessCodeForm model = new Sm_BusinessCodeForm();
		model.setBusiCode(busiCode);
		model.setTheYear(theYear);
		model.setTheMonth(theMonth);
		model.setTheDay(theDay);

		Sm_BusinessCode sm_BusinessCode = (Sm_BusinessCode) sm_BusinessCodeDao
				.findOneByQuery(sm_BusinessCodeDao.getQuery(sm_BusinessCodeDao.getBasicHQL(), model));
		if (sm_BusinessCode == null)
		{
			sm_BusinessCode = initBusinessCode(busiCode, theYear, theMonth, theDay);
		}
		sm_BusinessCode.setTicketCount(sm_BusinessCode.getTicketCount() + 1);
		sm_BusinessCodeDao.save(sm_BusinessCode);

		return sm_BusinessCode.getStringFormat();
	}

	public synchronized String getTicketCount(String busiCode)
	{
		Calendar calendar = Calendar.getInstance();
		Integer theYear = calendar.get(Calendar.YEAR);
		Integer theMonth = calendar.get(Calendar.MONTH) + 1;// Calendar的月份是从0开始的
		Integer theDay = calendar.get(Calendar.DAY_OF_MONTH);

		Sm_BusinessCodeForm model = new Sm_BusinessCodeForm();
		model.setBusiCode(busiCode);
		model.setTheYear(theYear);
		model.setTheMonth(theMonth);
		model.setTheDay(theDay);

		Integer ticketCount = 0;

		Sm_BusinessCode sm_BusinessCode = (Sm_BusinessCode) sm_BusinessCodeDao
				.findOneByQuery(sm_BusinessCodeDao.getQuery(sm_BusinessCodeDao.getBasicHQL(), model));
		if (sm_BusinessCode == null)
		{
			ticketCount = 1;
			sm_BusinessCode = initBusinessCode(busiCode, theYear, theMonth, theDay);
		}
		else
		{
			ticketCount = sm_BusinessCode.getTicketCount() + 1;
		}
		sm_BusinessCodeDao.save(sm_BusinessCode);
		return String.format("%05d", ticketCount);
	}

	private Sm_BusinessCode initBusinessCode(String busiCode, Integer theYear, Integer theMonth, Integer theDay)
	{
		Sm_BusinessCode sm_BusinessCode;
		sm_BusinessCode = new Sm_BusinessCode();
		sm_BusinessCode.setBusiCode(busiCode);
		sm_BusinessCode.setTheYear(theYear);
		sm_BusinessCode.setTheMonth(theMonth);
		sm_BusinessCode.setTheDay(theDay);
		sm_BusinessCode.setTicketCount(0);
		return sm_BusinessCode;
	}

	// 贷款合作协议编号生成规则 年+月+日+区域+两位流水号
	public synchronized String getEscrowAgreementEcode(String area)
	{
		String busiCode = "99";
		// 根据区域名称获取区域编号
		if (null == area || area.trim().isEmpty())
		{
			busiCode = "99";
		}
		else if (area.contains("金坛区"))
		{
			busiCode = "10";
		}
		else if (area.contains("武进区"))
		{
			busiCode = "20";
		}
		else if (area.contains("经开区"))
		{
			busiCode = "21";
		}
		else if (area.contains("新北区"))
		{
			busiCode = "30";
		}
		else if (area.contains("天宁区"))
		{
			busiCode = "40";
		}
		else if (area.contains("钟楼区"))
		{
			busiCode = "50";
		}
		else if (area.contains("溧阳市"))
		{
			busiCode = "60";
		}
		

		Calendar calendar = Calendar.getInstance();
		Integer theYear = calendar.get(Calendar.YEAR);
		Integer theMonth = calendar.get(Calendar.MONTH) + 1;// Calendar的月份是从0开始的
		Integer theDay = calendar.get(Calendar.DAY_OF_MONTH);

		Sm_BusinessCodeForm model = new Sm_BusinessCodeForm();
		model.setBusiCode(busiCode);
		model.setTheYear(theYear);
		model.setTheMonth(theMonth);
		model.setTheDay(theDay);

		Sm_BusinessCode sm_BusinessCode = (Sm_BusinessCode) sm_BusinessCodeDao
				.findOneByQuery(sm_BusinessCodeDao.getQuery(sm_BusinessCodeDao.getBasicHQL(), model));
		if (sm_BusinessCode == null)
		{
			sm_BusinessCode = initBusinessCode(busiCode, theYear, theMonth, theDay);
		}
		sm_BusinessCode.setTicketCount(sm_BusinessCode.getTicketCount() + 1);
		sm_BusinessCodeDao.save(sm_BusinessCode);

		return sm_BusinessCode.getEscrowAgreementStringFormat();
	}

	// 三方协议编号生成 合作协议号+四位流水号（按天流水）ECODEOFAGREEMENT	2204285003
	public synchronized String getTripleAgreementEcode(String busiCode)
	{

		//busiCode 合作协议编号
//		Calendar calendar = Calendar.getInstance();
//		Integer theYear = calendar.get(Calendar.YEAR);
//		Integer theMonth = calendar.get(Calendar.MONTH) + 1;// Calendar的月份是从0开始的
//		Integer theDay = calendar.get(Calendar.DAY_OF_MONTH);

		Sm_BusinessCodeForm model = new Sm_BusinessCodeForm();
		model.setBusiCode(busiCode);
//		model.setTheYear(theYear);
//		model.setTheMonth(theMonth);
//		model.setTheDay(theDay);

		Sm_BusinessCode sm_BusinessCode = (Sm_BusinessCode) sm_BusinessCodeDao
				.findOneByQuery(sm_BusinessCodeDao.getQuery(sm_BusinessCodeDao.getBasicHQL(), model));
		if (sm_BusinessCode == null)
		{
			sm_BusinessCode = initBusinessCode(busiCode, null, null, null);
		}
		sm_BusinessCode.setTicketCount(sm_BusinessCode.getTicketCount() + 1);
		sm_BusinessCodeDao.save(sm_BusinessCode);

		return sm_BusinessCode.getTripleAgreementStringFormat();
	}
	
	// 三方协议编号生成 合作协议号+四位流水号（按天流水）
	public synchronized String getCoopAgreementSettleEcode(String busiCode)
	{

		//busiCode 合作协议编号
		Calendar calendar = Calendar.getInstance();
		Integer theYear = calendar.get(Calendar.YEAR);
		Integer theMonth = calendar.get(Calendar.MONTH) + 1;// Calendar的月份是从0开始的
		Integer theDay = calendar.get(Calendar.DAY_OF_MONTH);

		Sm_BusinessCodeForm model = new Sm_BusinessCodeForm();
		model.setBusiCode(busiCode);
		model.setTheYear(theYear);
		model.setTheMonth(theMonth);
		model.setTheDay(theDay);

		Sm_BusinessCode sm_BusinessCode = (Sm_BusinessCode) sm_BusinessCodeDao
				.findOneByQuery(sm_BusinessCodeDao.getQuery(sm_BusinessCodeDao.getBasicHQL(), model));
		if (sm_BusinessCode == null)
		{
			sm_BusinessCode = initBusinessCode(busiCode, theYear, theMonth, theDay);
		}
		sm_BusinessCode.setTicketCount(sm_BusinessCode.getTicketCount() + 1);
		sm_BusinessCodeDao.save(sm_BusinessCode);

		return sm_BusinessCode.getCoopAgreementSettleStringFormat();
	}

}

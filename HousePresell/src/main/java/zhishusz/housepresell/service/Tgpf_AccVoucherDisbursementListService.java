package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import zhishusz.housepresell.controller.form.Tgpf_AccVoucherForm;
import zhishusz.housepresell.database.dao.Tgpf_AccVoucherDao;
import zhishusz.housepresell.database.po.Tgpf_AccVoucher;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service列表查询：推送给财务系统-凭证
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgpf_AccVoucherDisbursementListService
{
	@Autowired
	private Tgpf_AccVoucherDao tgpf_AccVoucherDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_AccVoucherForm model)
	{
		Properties properties = new MyProperties();
		
		String billTimeStap = model.getBillTimeStamp();

		if (null == billTimeStap || billTimeStap.trim().isEmpty())
		{
			billTimeStap = myDatetime.dateToSimpleString(System.currentTimeMillis());
			model.setPayoutTimeStamp(billTimeStap);
		}else{
			model.setPayoutTimeStamp(billTimeStap);
			model.setBillTimeStamp(null);			
		}
		
		String keyword = model.getKeyword();
		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%"+keyword+"%");
		}else{
			model.setKeyword(null);
		}	
		
		model.setTheType("拨付");
		
		String busiState = model.getBusiState();
		
		if (null != busiState && !busiState.trim().isEmpty())
		{
			
		}else{
			model.setBusiState(null);
		}	
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		Integer totalCount = tgpf_AccVoucherDao.findByPage_Size(tgpf_AccVoucherDao.createNewCriteriaForList(model));
//		Integer totalCount = tgpf_AccVoucherDao.findByPage_Size(tgpf_AccVoucherDao.getQuery_Size(tgpf_AccVoucherDao.getPayHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_AccVoucher> tgpf_AccVoucherList;
		if(totalCount > 0)
		{
			tgpf_AccVoucherList = tgpf_AccVoucherDao.findByPage(tgpf_AccVoucherDao.createNewCriteriaForList(model), pageNumber, countPerPage);
//			tgpf_AccVoucherList = tgpf_AccVoucherDao.findByPage(tgpf_AccVoucherDao.getQuery(tgpf_AccVoucherDao.getPayHQL(), model), pageNumber, countPerPage);
		}
		else
		{
			tgpf_AccVoucherList = new ArrayList<Tgpf_AccVoucher>();
		}
		
		properties.put("tgpf_AccVoucherList", tgpf_AccVoucherList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);
		
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
		return properties;
	}
}

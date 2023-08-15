package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgpf_BalanceOfAccountForm;
import zhishusz.housepresell.database.dao.Tgpf_BalanceOfAccountDao;
import zhishusz.housepresell.database.po.Tgpf_BalanceOfAccount;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementReview_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.exportexcelvo.Tgpf_BalanceOfAccountExportExcelVO;
import zhishusz.housepresell.exportexcelvo.Tgxy_TripleAgreementReviewExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service 列表查询：业务对账 excel 导出
 * Company：ZhiShuSZ
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Tgpf_BalanceOfAccountExportExcelService
{
	@Autowired
	private Tgpf_BalanceOfAccountDao tgpf_BalanceOfAccountDao;
	
	private static final String excelName = "三方协议考评";
	
	MyDatetime myDatetime = MyDatetime.getInstance();

	@SuppressWarnings("unchecked")
	public Properties execute(Tgpf_BalanceOfAccountForm model)
	{
		Properties properties = new MyProperties();

		String keyword = model.getKeyword();
		String billTimeStap = model.getBillTimeStamp();
		int accountType = 0; // 业务对账 
		
		if (null != keyword && !keyword.trim().isEmpty())
		{
			model.setKeyword("%"+keyword+"%");
		}else{
			model.setKeyword(null);
		}
		
		if (null == billTimeStap || billTimeStap.trim().isEmpty())
		{
			
			model.setBillTimeStamp(myDatetime.getSpecifiedDayBefore(myDatetime.dateToSimpleString(System.currentTimeMillis())));
		}
		
		
		if ( null == model.getAccountType() || 0 == model.getAccountType() )
		{
			model.setReconciliationDate("1");
		}else{
			model.setAccountType(1);
			accountType = 1; //网银对账
		}
		
		model.setTheState(S_TheState.Normal);
		
//		tgpf_BalanceOfAccountBusContrastAddService.execute(model);
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		

		Integer totalCount = tgpf_BalanceOfAccountDao.findByPage_Size(tgpf_BalanceOfAccountDao.getQuery_Size(tgpf_BalanceOfAccountDao.getSpecialHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList;
		if(totalCount > 0)
		{
			tgpf_BalanceOfAccountList = tgpf_BalanceOfAccountDao.findByPage(tgpf_BalanceOfAccountDao.getQuery(tgpf_BalanceOfAccountDao.getSpecialHQL(), model));
		}
		else
		{
			tgpf_BalanceOfAccountList = new ArrayList<Tgpf_BalanceOfAccount>();
		}


		//初始化文件保存路径，创建相应文件夹
		DirectoryUtil directoryUtil = new DirectoryUtil();
		String relativeDir = directoryUtil.createRelativePathWithDate("Tgpf_BalanceOfAccountExportExcel");//文件在项目中的相对路径
		String localPath = directoryUtil.getProjectRoot();//项目路径
		
		String saveDirectory = localPath + relativeDir;//文件在服务器文件系统中的完整路径
		
		saveDirectory = saveDirectory.replace("%20"," ");
		directoryUtil.mkdir(saveDirectory);
		
		String strNewFileName = excelName + "-"
				+ MyDatetime.getInstance().dateToString(System.currentTimeMillis(),"yyyyMMddHHmmss")
				+ ".xlsx";
		
		String saveFilePath = saveDirectory + strNewFileName;
		
		// 通过工具类创建writer
		ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);
		
		//自定义字段别名
		writer.addHeaderAlias("ordinal", "");
		
		writer.addHeaderAlias("billTimeStamp", "记账日期");
		writer.addHeaderAlias("bankName", "银行名称");
		writer.addHeaderAlias("escrowedAccount", "托管账户");
		writer.addHeaderAlias("escrowedAccountTheName", "托管账号名称");
		if( 1 == accountType)
		{
			writer.addHeaderAlias("bankTotalCount", "网银总笔数");
			writer.addHeaderAlias("bankTotalAmount", "网银总金额");
		}
		writer.addHeaderAlias("centerTotalCount", "业务总笔数");
		writer.addHeaderAlias("centerTotalAmount", "业务总金额");
		if( 0 == accountType)
		{
			writer.addHeaderAlias("bankTotalCount", "银行总笔数");
			writer.addHeaderAlias("bankTotalAmount", "银行总金额");
		}
		writer.addHeaderAlias("reconciliationState", "对账状态");
	
		List<Tgpf_BalanceOfAccountExportExcelVO> list = formart(tgpf_BalanceOfAccountList);
		// 一次性写出内容，使用默认样式
		writer.write(list);
		
		// 关闭writer，释放内存
		writer.flush();
		writer.close();
		
		properties.put("fileURL", relativeDir+strNewFileName);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	
	
	/**
	 * po 格式化
	 * @param tg_Build_ViewList
	 * @return
	 */
	List<Tgpf_BalanceOfAccountExportExcelVO> formart(List<Tgpf_BalanceOfAccount> tgpf_BalanceOfAccountList){
		List<Tgpf_BalanceOfAccountExportExcelVO>  tgpf_BalanceOfAccountExportExcelList = new ArrayList<Tgpf_BalanceOfAccountExportExcelVO>();
		int ordinal = 0;
		for (Tgpf_BalanceOfAccount tgpf_BalanceOfAccount : tgpf_BalanceOfAccountList)
		{
			++ordinal;
			Tgpf_BalanceOfAccountExportExcelVO tgpf_BalanceOfAccountExportExcel = new Tgpf_BalanceOfAccountExportExcelVO();
			tgpf_BalanceOfAccountExportExcel.setOrdinal(ordinal);
			
			tgpf_BalanceOfAccountExportExcel.setBillTimeStamp(tgpf_BalanceOfAccount.getBillTimeStamp());
			tgpf_BalanceOfAccountExportExcel.setBankName(tgpf_BalanceOfAccount.getBankName());
			tgpf_BalanceOfAccountExportExcel.setEscrowedAccount(tgpf_BalanceOfAccount.getEscrowedAccount());
			tgpf_BalanceOfAccountExportExcel.setEscrowedAccountTheName(tgpf_BalanceOfAccount.getEscrowedAccountTheName());
			tgpf_BalanceOfAccountExportExcel.setCenterTotalCount (tgpf_BalanceOfAccount.getCenterTotalCount());
			tgpf_BalanceOfAccountExportExcel.setCenterTotalAmount(tgpf_BalanceOfAccount.getCenterTotalAmount());
			tgpf_BalanceOfAccountExportExcel.setBankTotalCount(tgpf_BalanceOfAccount.getBankTotalCount());
			tgpf_BalanceOfAccountExportExcel.setBankTotalAmount(tgpf_BalanceOfAccount.getBankTotalAmount());
			
			if(null == tgpf_BalanceOfAccount.getReconciliationState() || 0 == tgpf_BalanceOfAccount.getReconciliationState())
			{
				tgpf_BalanceOfAccountExportExcel.setReconciliationState("未对账");
			}
			else
			{
				tgpf_BalanceOfAccountExportExcel.setReconciliationState("已对账");
			}
			
			
			tgpf_BalanceOfAccountExportExcelList.add(tgpf_BalanceOfAccountExportExcel);
		}
		return tgpf_BalanceOfAccountExportExcelList;
	}
}

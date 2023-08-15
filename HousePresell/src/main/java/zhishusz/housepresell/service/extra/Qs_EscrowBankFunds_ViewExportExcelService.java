package zhishusz.housepresell.service.extra;

import zhishusz.housepresell.controller.form.extra.Qs_EscrowBankFunds_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_BankBranchDao;
import zhishusz.housepresell.database.dao.extra.Qs_EscrowBankFunds_ViewDao;
import zhishusz.housepresell.database.po.Emmp_BankBranch;
import zhishusz.housepresell.database.po.extra.Qs_EscrowBankFunds_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Qs_EscrowBankFunds_ViewExportExcleVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Service添加操作：贷款银行资金明细-导出Excel
 * Company：ZhiShuSZ
 */
@Service
// @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
@Transactional
public class Qs_EscrowBankFunds_ViewExportExcelService
{
	@Autowired
	private Qs_EscrowBankFunds_ViewDao qs_EscrowBankFunds_ViewDao;
	@Autowired
	private Emmp_BankBranchDao emmp_BankBranchDao;

	private static final String excelName = "托管银行资金情况表";

	@SuppressWarnings("unchecked")
	public Properties execute(Qs_EscrowBankFunds_ViewForm model)
	{
		Properties properties = new MyProperties();

		String keyword = model.getKeyword();
		String theNameOfDepositBank = model.getTheNameOfDepositBank();
		String timeStamp = model.getTimeStamp();

		if (null != theNameOfDepositBank && theNameOfDepositBank.trim().isEmpty())
		{
			model.setTheNameOfDepositBank(null);
		}
		else
		{
			Emmp_BankBranch emmp_BankBranch = emmp_BankBranchDao.findById(new Long(theNameOfDepositBank));
			if(null != emmp_BankBranch){
				model.setTheNameOfDepositBank(emmp_BankBranch.getTheName());
			}
		}

		if (null != timeStamp && timeStamp.trim().isEmpty())
		{
			model.setTimeStamp(null);
		}

		if (null == keyword || keyword.length() <= 0)
		{
			model.setKeyword(null);
		}
		else
		{
			model.setKeyword("%" + keyword + "%");
		}

		Integer totalCount = qs_EscrowBankFunds_ViewDao.findByPage_Size(
				qs_EscrowBankFunds_ViewDao.getQuery_Size(qs_EscrowBankFunds_ViewDao.getBasicHQL(), model));


		List<Qs_EscrowBankFunds_View> qs_EscrowBankFunds_ViewList;
		if (totalCount > 0)
		{
			qs_EscrowBankFunds_ViewList = qs_EscrowBankFunds_ViewDao.findByPage(
					qs_EscrowBankFunds_ViewDao.getQuery(qs_EscrowBankFunds_ViewDao.getBasicHQL(), model));

			if (null == qs_EscrowBankFunds_ViewList || qs_EscrowBankFunds_ViewList.size() == 0)
			{
				return MyBackInfo.fail(properties, "没有查询到有效的数据，请核对查询条件！");
			}else{
				
				/*
				 * xsz by time 2018-9-5 10:29:15
				 * 合计时的比例计算逻辑：
				 * 大额占比 = 大额存单/托管收入
				 * 大额+活期占比 = (大额存单+活期)/托管收入
				 * 理财占比 = (结构性存款+保本理财)/托管收入
				 * 总资金沉淀占比 = (大额存单+活期+结构性存款+保本理财)/托管收入
				 * 
				 * 存款合计 = 大额存单+结构性存款+保本理财
				 * 存款大额占比 = 大额存单/存款合计
				 * 存款理财占比 = (结构性存款+保本理财)/存款合计 = 1-存款大额占比
				 */

				// 日期 TIMESTAMP timeStamp
				// 存款银行 THENAMEOFBANK theNameOfBank
				// 开户行 THENAMEOFDEPOSITBANK theNameOfDepositBank
				// 托管账号 THEACCOUNT theAccount
				// 托管账号名称 THENAME theName
				// 托管收入 INCOME income
				// 托管支出 PAYOUT payout
				// 大额存单 CERTOFDEPOSIT certOfDeposit
				// 结构性存款 STRUCTUREDDEPOSIT structuredDeposit
				// 保本理财 BREAKEVENFINANCIAL breakEvenFinancial
				// 活期余额 CURRENTBALANCE currentBalance
				// 大额占比 LARGERATIO largeRatio
				// 大额+活期占比 LARGEANDCURRENTRATIO largeAndCurrentRatio
				// 理财占比 FINANCIALRATIO financialRatio
				// 总资金沉淀占比 TOTALFUNDSRATIO totalFundsRatio
				// 正在办理中 INPROGRESS inProgress
				/*MyDouble dplan = MyDouble.getInstance();
				Qs_EscrowBankFunds_View qe = new Qs_EscrowBankFunds_View();
				qe.setIncome(0.00);
				qe.setPayout(0.00);
				qe.setCertOfDeposit(0.00);
				qe.setStructuredDeposit(0.00);
				qe.setBreakEvenFinancial(0.00);
				qe.setCurrentBalance(0.00);
				qe.setInProgressAccount(0.00);
				qe.setTimeStamp("-");
				qe.setInProgress("-");
				qe.setTheNameOfDepositBank("合计");

				for (Qs_EscrowBankFunds_View qs : qs_EscrowBankFunds_ViewList)
				{

					qe.setIncome(dplan.doubleAddDouble(qe.getIncome(), qs.getIncome()));// 托管收入
					qe.setPayout(dplan.doubleAddDouble(qe.getPayout(), qs.getPayout()));// 托管支出
					qe.setCertOfDeposit(dplan.doubleAddDouble(qe.getCertOfDeposit(), qs.getCertOfDeposit()));// 大额存单
					qe.setStructuredDeposit(dplan.doubleAddDouble(qe.getStructuredDeposit(), qs.getStructuredDeposit()));// 结构性存款
					qe.setBreakEvenFinancial(dplan.doubleAddDouble(qe.getBreakEvenFinancial(), qs.getBreakEvenFinancial()));// 保本理财
					qe.setCurrentBalance(dplan.doubleAddDouble(qe.getCurrentBalance(), qs.getCurrentBalance()));// 活期余额
					qe.setInProgressAccount(dplan.doubleAddDouble(qe.getInProgressAccount(), qs.getInProgressAccount()));// 正在办理中金额

				}
				
				 * doubleAddDouble 加
				 * doubleSubtractDouble 减
				 * doubleMultiplyDouble 乘
				 * div 除
				 * getShort() 保留小数位
				 
				// 大额占比 = 大额存单/托管收入
				Double a = MyDouble.getInstance().div(qe.getCertOfDeposit(), qe.getIncome(), 4);
				Double b = MyDouble.getInstance().doubleMultiplyDouble(a, 100.00);
				Double c = MyDouble.getInstance().getShort(b, 2);
				qe.setLargeRatio(c);

				// 大额+活期占比 = (大额存单+活期)/托管收入
				Double a1 = MyDouble.getInstance().doubleAddDouble(qe.getCertOfDeposit(), qe.getCurrentBalance());
				Double b1 = MyDouble.getInstance().div(a1, qe.getIncome(), 4);
				Double c1 = MyDouble.getInstance().doubleMultiplyDouble(b1, 100.00);
				Double d1 = MyDouble.getInstance().getShort(c1, 2);
				qe.setLargeAndCurrentRatio(d1);

				// 理财占比 = (结构性存款+保本理财)/托管收入
				Double a2 = MyDouble.getInstance().doubleAddDouble(qe.getStructuredDeposit(), qe.getBreakEvenFinancial());
				Double b2 = MyDouble.getInstance().div(a2, qe.getIncome(), 4);
				Double c2 = MyDouble.getInstance().doubleMultiplyDouble(b2, 100.00);
				Double d2 = MyDouble.getInstance().getShort(c2, 2);
				qe.setFinancialRatio(d2);

				// 总资金沉淀占比 = (大额存单+活期+结构性存款+保本理财)/托管收入
				Double a3 = MyDouble.getInstance().doubleAddDouble(a1, a2);
				Double b3 = MyDouble.getInstance().div(a3, qe.getIncome(), 4);
				Double c3 = MyDouble.getInstance().doubleMultiplyDouble(b3, 100.00);
				Double d3 = MyDouble.getInstance().getShort(c3, 2);
				qe.setTotalFundsRatio(d3);

				// //存款合计 = 大额存单+结构性存款+保本理财
				// Double ckhj =
				// MyDouble.getInstance().doubleAddDouble(qe.getCertOfDeposit(),
				// a2);
				//
				// properties.put("ckhj", ckhj);
				//
				// //存款大额占比 = 大额存单/存款合计
				// Double a4 = MyDouble.getInstance().div(qe.getCertOfDeposit(),
				// ckhj,4);
				// Double b4 = MyDouble.getInstance().doubleMultiplyDouble(a4,
				// 100.00);
				// Double ckdezb = MyDouble.getInstance().getShort(b4, 2);
				//
				// properties.put("ckdezb", ckdezb);
				//
				// //存款理财占比 = (结构性存款+保本理财)/存款合计 = 1-存款大额占比
				// Double cklczb =
				// MyDouble.getInstance().doubleSubtractDouble(100.00, ckdezb);
				//
				// properties.put("cklczb", cklczb);

				// 合计信息入列表
				qs_EscrowBankFunds_ViewList.add(qe);*/
				
				//初始化文件保存路径，创建相应文件夹
				DirectoryUtil directoryUtil = new DirectoryUtil();
				String relativeDir = directoryUtil.createRelativePathWithDate("Qs_EscrowBankFunds_ViewExportExcelService");//文件在项目中的相对路径
				String localPath = directoryUtil.getProjectRoot();//项目路径
				
				String saveDirectory = localPath + relativeDir;//文件在服务器文件系统中的完整路径
				
				if (saveDirectory.contains("%20"))
				{
					saveDirectory = saveDirectory.replace("%20", " ");
				}
				
				directoryUtil.mkdir(saveDirectory);
				
				String strNewFileName = excelName + "-"
						+ MyDatetime.getInstance().dateToString(System.currentTimeMillis(),"yyyyMMddHHmmss")
						+ ".xlsx";
				
				String saveFilePath = saveDirectory + strNewFileName;
				
				// 通过工具类创建writer
				ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);
				
				//自定义字段别名
				writer.addHeaderAlias("tableId", "序号");
				writer.addHeaderAlias("timeStamp", "日期");				
				writer.addHeaderAlias("theNameOfBank", "银行名称");
				writer.addHeaderAlias("theNameOfDepositBank", "开户行名称");
				writer.addHeaderAlias("theAccount", "托管账号");
				writer.addHeaderAlias("theName", "托管账户名");
				writer.addHeaderAlias("income", "托管收入");
				writer.addHeaderAlias("payout", "托管支出");
				writer.addHeaderAlias("certOfDeposit", "大额存单");
				writer.addHeaderAlias("structuredDeposit", "结构性存款");
				writer.addHeaderAlias("breakEvenFinancial", "保本理财");
				writer.addHeaderAlias("currentBalance", "活期");
				writer.addHeaderAlias("largeRatio", "大额占比");
				writer.addHeaderAlias("largeAndCurrentRatio", "大额+活期占比");
				writer.addHeaderAlias("financialRatio", "理财占比");
				writer.addHeaderAlias("totalFundsRatio", "总资金沉淀占比");
				writer.addHeaderAlias("inProgress", "正在办理中");
				writer.addHeaderAlias("inProgressAccount", "正在办理总资金占比");
				writer.addHeaderAlias("transferOutAmount", "转出金额");
				writer.addHeaderAlias("transferInAmount", "转入金额");
				
				
//				List<Qs_EscrowBankFunds_ViewExportExcleVO> list = formart(qs_EscrowBankFunds_ViewList);
				
				// 一次性写出内容，使用默认样式
				writer.write(qs_EscrowBankFunds_ViewList);
				
				// 关闭writer，释放内存
				writer.flush();
				
				writer.autoSizeColumn(0, true);
				writer.autoSizeColumn(1, true);
				writer.autoSizeColumn(2, true);
				writer.autoSizeColumn(3, true);
				writer.autoSizeColumn(4, true);
				writer.autoSizeColumn(5, true);
				writer.autoSizeColumn(6, true);
				writer.autoSizeColumn(7, true);
				writer.autoSizeColumn(8, true);
				writer.autoSizeColumn(9, true);
				writer.autoSizeColumn(10, true);
				writer.autoSizeColumn(11, true);
				writer.autoSizeColumn(12, true);
				writer.autoSizeColumn(13, true);
				writer.autoSizeColumn(14, true);
				writer.autoSizeColumn(15, true);
				writer.autoSizeColumn(16, true);
				writer.autoSizeColumn(17, true);
				writer.autoSizeColumn(18, true);
				writer.autoSizeColumn(19, true);
				writer.close();
				
				properties.put("fileURL", relativeDir+strNewFileName);
				
			}

		}
		else
		{
			return MyBackInfo.fail(properties, "未查询到有效数据");
		}

		properties.put("qs_EscrowBankFunds_ViewList", qs_EscrowBankFunds_ViewList);
		properties.put(S_NormalFlag.keyword, keyword);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	List<Qs_EscrowBankFunds_ViewExportExcleVO> formart(List<Qs_EscrowBankFunds_View> qs_EscrowBankFunds_ViewList){
		
		List<Qs_EscrowBankFunds_ViewExportExcleVO> list = new ArrayList<Qs_EscrowBankFunds_ViewExportExcleVO>();
		int ordinal = 0;
		for (Qs_EscrowBankFunds_View po : qs_EscrowBankFunds_ViewList)
		{
			++ordinal;
			Qs_EscrowBankFunds_ViewExportExcleVO vo = new Qs_EscrowBankFunds_ViewExportExcleVO();
			
			vo.setOrdinal(ordinal);
			vo.setBreakEvenFinancial(po.getBreakEvenFinancial());
			vo.setCertOfDeposit(po.getCertOfDeposit());
			vo.setCurrentBalance(po.getCurrentBalance());
			vo.setFinancialRatio(po.getFinancialRatio());
			//vo.setInProgress(po.getInProgress());//资金性质

			/*if (null != po.getInProgress() && po.getInProgress().trim().length() > 0)
			{
				switch (po.getInProgress())
				{
				case "0":

					vo.setInProgress("自有资金");

					break;

				case "1":

					vo.setInProgress("商业贷款");

					break;

				case "2":

					vo.setInProgress("公积金贷款");

					break;

				case "3":

					vo.setInProgress("公转商贷款");

					break;

				case "4":

					vo.setInProgress("公积金首付款");

					break;
				}
			}*/
			
			vo.setInProgressAccount(po.getInProgressAccount());
			vo.setLargeAndCurrentRatio(po.getLargeAndCurrentRatio());
			vo.setLargeRatio(po.getLargeRatio());
			vo.setPayout(po.getPayout());
			vo.setStructuredDeposit(po.getStructuredDeposit());
			vo.setTheNameOfDepositBank(po.getTheNameOfDepositBank());
			vo.setTotalFundsRatio(po.getTotalFundsRatio());
			vo.setTransferOutAmount(po.getTransferOutAmount());
			vo.setTransferInAmount(po.getTransferInAmount());
			list.add(vo);
		}
		
		return list;
		
	}
	
}

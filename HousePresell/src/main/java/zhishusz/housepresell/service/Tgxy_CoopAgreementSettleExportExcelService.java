package zhishusz.housepresell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.Tgxy_CoopAgreementSettleForm;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgxy_CoopAgreementSettleDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgxy_CoopAgreementSettle;
import zhishusz.housepresell.database.po.Tgxy_TripleAgreementReview_View;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Tgxy_CoopAgreementSettleExportExcelVO;
import zhishusz.housepresell.exportexcelvo.Tgxy_TripleAgreementReviewExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/*
 * Service列表查询：三方协议结算-主表
 * Company：ZhiShuSZ
 * */
@Service
@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
public class Tgxy_CoopAgreementSettleExportExcelService
{
	@Autowired
	private Tgxy_CoopAgreementSettleDao tgxy_CoopAgreementSettleDao;
	@Autowired
	private Sm_UserDao sm_UserDao;
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	private static final String excelName = "三方协议计量结算";
	
	@SuppressWarnings("unchecked")
	public Properties execute(Tgxy_CoopAgreementSettleForm model)
	{		
		Properties properties = new MyProperties();
		
		Sm_User userStart = model.getUser(); // admin
		
		// 获取代理公司
		Emmp_CompanyInfo agentCompany = userStart.getCompany();
		if( null != agentCompany && S_CompanyType.Zhengtai.equals(agentCompany.getTheType()))
		{
			model.setAgentCompany(null);
		}
		else
		{
			model.setAgentCompany(agentCompany);
		}
		
		
		
		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		if(null == keyword || keyword.length() == 0)
		{
			model.setKeyword(null);
		}
		else
		{
			keyword = "%" + keyword + "%";
			model.setKeyword(keyword);
		}
		
		Integer totalCount = tgxy_CoopAgreementSettleDao.findByPage_Size(tgxy_CoopAgreementSettleDao.getQuery_Size(tgxy_CoopAgreementSettleDao.getSpecialHQL(), model));
		
		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0) totalPage++;
		if (pageNumber > totalPage && totalPage != 0) pageNumber = totalPage;
		
		List<Tgxy_CoopAgreementSettle> tgxy_CoopAgreementSettleList;
		if(totalCount > 0)
		{
			tgxy_CoopAgreementSettleList = tgxy_CoopAgreementSettleDao.findByPage(tgxy_CoopAgreementSettleDao.getQuery(tgxy_CoopAgreementSettleDao.getSpecialHQL(), model));
		}
		else
		{
			tgxy_CoopAgreementSettleList = new ArrayList<Tgxy_CoopAgreementSettle>();
		}
		
		//初始化文件保存路径，创建相应文件夹
		DirectoryUtil directoryUtil = new DirectoryUtil();
		String relativeDir = directoryUtil.createRelativePathWithDate("Tgxy_CoopAgreementSettleExcel");//文件在项目中的相对路径
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
		
		writer.addHeaderAlias("eCode", "结算确认单号");
		writer.addHeaderAlias("companyName", "代理公司");
		writer.addHeaderAlias("applySettlementDate", "申请结算日期");
		writer.addHeaderAlias("startSettlementDate", "结算开始日期");
		writer.addHeaderAlias("endSettlementDate", "结算截至日期");		
		writer.addHeaderAlias("protocolNumbers", "三方协议号有效份数");
		writer.addHeaderAlias("userUpdate", "操作人");
		writer.addHeaderAlias("lastUpdateTimeStamp", "操作日期");
		writer.addHeaderAlias("userRecord", "结算人");
		writer.addHeaderAlias("signTimeStamp", "结算日期");
		writer.addHeaderAlias("settlementState", "结算状态");
	
		List<Tgxy_CoopAgreementSettleExportExcelVO> list = formart(tgxy_CoopAgreementSettleList);
		// 一次性写出内容，使用默认样式
		writer.write(list);
		
		// 关闭writer，释放内存
		writer.flush();
		writer.close();
		
		properties.put("fileURL", relativeDir+strNewFileName);
		properties.put("fileName", strNewFileName);
		properties.put("fullFileName", saveFilePath);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	/**
	 * po 格式化
	 * @param tg_Build_ViewList
	 * @return
	 */
	List<Tgxy_CoopAgreementSettleExportExcelVO> formart(List<Tgxy_CoopAgreementSettle> tgxy_CoopAgreementSettleList){
		List<Tgxy_CoopAgreementSettleExportExcelVO>  Tgxy_CoopAgreementSettleExportExceList = new ArrayList<Tgxy_CoopAgreementSettleExportExcelVO>();
		int ordinal = 0;
		for (Tgxy_CoopAgreementSettle tgxy_CoopAgreementSettle : tgxy_CoopAgreementSettleList)
		{
			++ordinal;
			Tgxy_CoopAgreementSettleExportExcelVO tgxy_CoopAgreementSettleExportExcel = new Tgxy_CoopAgreementSettleExportExcelVO();
			tgxy_CoopAgreementSettleExportExcel.setOrdinal(ordinal);
			
			int settlementState = tgxy_CoopAgreementSettle.getSettlementState();
			String settlement = "已结算";
			if( 0 == settlementState)
			{
				settlement = "未申请";
			}else if( 1 == settlementState)
			{
				settlement = "结算中";
			}
			
			tgxy_CoopAgreementSettleExportExcel.seteCode(tgxy_CoopAgreementSettle.geteCode());
			tgxy_CoopAgreementSettleExportExcel.setCompanyName(tgxy_CoopAgreementSettle.getCompanyName());
			tgxy_CoopAgreementSettleExportExcel.setApplySettlementDate(tgxy_CoopAgreementSettle.getApplySettlementDate());
			tgxy_CoopAgreementSettleExportExcel.setStartSettlementDate(tgxy_CoopAgreementSettle.getStartSettlementDate());
			tgxy_CoopAgreementSettleExportExcel.setEndSettlementDate (tgxy_CoopAgreementSettle.getEndSettlementDate());
			tgxy_CoopAgreementSettleExportExcel.setProtocolNumbers(tgxy_CoopAgreementSettle.getProtocolNumbers());
			tgxy_CoopAgreementSettleExportExcel.setUserUpdate(tgxy_CoopAgreementSettle.getUserUpdate().getTheName());
			tgxy_CoopAgreementSettleExportExcel.setLastUpdateTimeStamp(myDatetime.dateToSimpleString(tgxy_CoopAgreementSettle.getLastUpdateTimeStamp()));
			if(null != tgxy_CoopAgreementSettle.getUserRecord())
			{
				tgxy_CoopAgreementSettleExportExcel.setUserRecord(tgxy_CoopAgreementSettle.getUserRecord().getTheName());
				tgxy_CoopAgreementSettleExportExcel.setSignTimeStamp(tgxy_CoopAgreementSettle.getSignTimeStamp());				
			}			
			tgxy_CoopAgreementSettleExportExcel.setSettlementState(settlement);		
			Tgxy_CoopAgreementSettleExportExceList.add(tgxy_CoopAgreementSettleExportExcel);
		}
		return Tgxy_CoopAgreementSettleExportExceList;
	}
}

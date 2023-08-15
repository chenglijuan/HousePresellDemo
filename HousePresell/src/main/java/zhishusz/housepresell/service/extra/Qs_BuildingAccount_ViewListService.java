package zhishusz.housepresell.service.extra;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.controller.form.extra.Qs_BuildingAccount_ViewForm;
import zhishusz.housepresell.database.dao.Emmp_CompanyInfoDao;
import zhishusz.housepresell.database.dao.Empj_BuildingInfoDao;
import zhishusz.housepresell.database.dao.Empj_ProjectInfoDao;
import zhishusz.housepresell.database.dao.extra.Qs_BuildingAccount_ViewDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Empj_ProjectInfo;
import zhishusz.housepresell.database.po.extra.Qs_BuildingAccount_View;
import zhishusz.housepresell.database.po.state.S_CompanyType;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：楼幢账户表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Qs_BuildingAccount_ViewListService
{
	@Autowired
	private Qs_BuildingAccount_ViewDao qs_BuildingAccount_ViewDao;
	@Autowired
	private Emmp_CompanyInfoDao emmp_CompanyInfoDao;// 开发企业
	@Autowired
	private Empj_ProjectInfoDao empj_ProjectInfoDao;// 项目
	@Autowired
	private Empj_BuildingInfoDao empj_BuildingInfoDao;// 楼幢

	@SuppressWarnings("unchecked")
	public Properties execute(Qs_BuildingAccount_ViewForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		String keyword = model.getKeyword();
		String theNameOfCompany = model.getTheNameOfCompany();// 开发企业
		String theNameOfProject = model.getTheNameOfProject();// 项目
		String ecodeFromConstruction = model.geteCodeFromConstruction();// 施工楼幢

		if (null != theNameOfCompany && theNameOfCompany.trim().isEmpty())
		{
			model.setTheNameOfCompany(null);
		}
		else
		{
			Emmp_CompanyInfo companyInfo = emmp_CompanyInfoDao.findById(Long.parseLong(theNameOfCompany));

			model.setTheNameOfCompany(companyInfo.getTheName());
		}
		
		//用户判断
		Integer userType=model.getUser().getTheType();
		if(userType==2) 
		{
			//普通机构用户
			String compnayType = model.getUser().getCompany().getTheType();
			if(S_CompanyType.Development.equals(compnayType)||S_CompanyType.Witness.equals(compnayType))
			{
				model.setTheNameOfCompany(model.getUser().getCompany().getTheName());
			}
			
		}

		if (null != theNameOfProject && theNameOfProject.trim().isEmpty())
		{
			model.setTheNameOfProject(null);
		}
		else
		{
			Empj_ProjectInfo projectInfo = empj_ProjectInfoDao.findById(Long.parseLong(theNameOfProject));

			model.setTheNameOfProject(projectInfo.getTheName());
		}

		if (null != ecodeFromConstruction && ecodeFromConstruction.trim().isEmpty())
		{
			model.seteCodeFromConstruction(null);
		}
		else
		{
			Empj_BuildingInfo buildingInfo = empj_BuildingInfoDao.findById(Long.parseLong(ecodeFromConstruction));
			
			model.seteCodeFromConstruction(buildingInfo.geteCodeFromConstruction());
		}

		if (keyword != null)
		{
			model.setKeyword("%" + keyword + "%");
		}

		Integer totalCount = qs_BuildingAccount_ViewDao.findByPage_Size(
				qs_BuildingAccount_ViewDao.getQuery_Size(qs_BuildingAccount_ViewDao.getBasicHQL(), model));

		Integer totalPage = totalCount / countPerPage;
		if (totalCount % countPerPage > 0)
			totalPage++;
		if (pageNumber > totalPage && totalPage != 0)
			pageNumber = totalPage;

		List<Qs_BuildingAccount_View> qs_BuildingAccount_ViewList;
		if (totalCount > 0)
		{
			qs_BuildingAccount_ViewList = qs_BuildingAccount_ViewDao.findByPage(
					qs_BuildingAccount_ViewDao.getQuery(qs_BuildingAccount_ViewDao.getBasicHQL(), model), pageNumber,
					countPerPage);

			/*
			 * xsz by time 2018-9-6 09:26:21
			 * ===》需要合计信息字段：
			 * 托管面积（escrowArea）、
			 * 建筑面积（buildingArea）、
			 * 初始受限额度（orgLimitedAmount）、
			 * 当前受限额度（currentLimitedAmount）、
			 * 总入账金额（totalAccountAmount）、
			 * 溢出金额（spilloverAmount）、
			 * 已拨付金额（payoutAmount）、
			 * 已申请未拨付金额（appliedNoPayoutAmount）、
			 * 已申请退款未拨付金额（applyRefundPayoutAmount）、
			 * 已退款金额（refundAmount）、
			 * 当前托管余额（currentEscrowFund）、
			 * 可划拨金额（allocableAmount）
			 * 
			 * ===》不需要合计信息字段：
			 * 开发企业（theNameOfCompany）、
			 * 项目（theNameOfProject）、
			 * 施工楼幢（eCodeFromConstruction）、
			 * 托管标准（escrowStandard）、
			 * 交付类型（deliveryType）、
			 * 当前形象进度（currentFigureProgress）、
			 * 当前受限比例（currentLimitedRatio）、
			 * 预售系统楼幢住宅备案均价（recordAvgPriceBldPS）、
			 * 楼幢住宅备案均价（recordAvgPriceOfBuilding）
			 * 
			 */

			Qs_BuildingAccount_View ba = new Qs_BuildingAccount_View();
			// 设置不需要合计字段
			ba.setTheNameOfCompany("-");
			ba.setTheNameOfProject("-");
			ba.seteCodeFromConstruction("-");
			ba.setEscrowStandard("-");
			ba.setDeliveryType("-");
			ba.setCurrentFigureProgress("-");
			ba.setCurrentLimitedRatio(null);
			ba.setRecordAvgPriceBldPS(null);
			ba.setRecordAvgPriceOfBuilding(null);
			ba.setCityregion("-");
			ba.setIspresell("-");
			ba.setEscrowagrecordTime("-");

			// 设置初始值
			ba.setEscrowArea(0.00);
			ba.setBuildingArea(0.00);
			ba.setOrgLimitedAmount(0.00);
			ba.setCurrentLimitedAmount(0.00);
			ba.setTotalAccountAmount(0.00);
			ba.setSpilloverAmount(0.00);
			ba.setPayoutAmount(0.00);
			ba.setAppliedNoPayoutAmount(0.00);
			ba.setApplyRefundPayoutAmount(0.00);
			ba.setRefundAmount(0.00);
			ba.setCurrentEscrowFund(0.00);
			ba.setAllocableAmount(0.00);
			
			ba.setXjsxPrice(0.00);
			ba.setYxsxPrice(0.00);
			ba.setZfbzPrice(0.00);
			
			ba.setSumfamilyNumber(0);
			ba.setSignhouseNum(0);
			ba.setRecordhouseNum(0);
			ba.setDeposithouseNum(0);
			
			/*
			 * double类型计算
			 * 
			 * doubleAddDouble 加
			 * doubleSubtractDouble 减
			 * doubleMultiplyDouble 乘
			 * div 除
			 * getShort() 保留小数位
			 */
			MyDouble dplan = MyDouble.getInstance();

			for (Qs_BuildingAccount_View qv : qs_BuildingAccount_ViewList)
			{
				// 托管面积
				ba.setEscrowArea(dplan.doubleAddDouble(ba.getEscrowArea(), null==qv.getEscrowArea()?0.00:qv.getEscrowArea()));
				// 建筑面积
				ba.setBuildingArea(dplan.doubleAddDouble(ba.getBuildingArea(), null==qv.getBuildingArea()?0.00:qv.getBuildingArea()));
				// 初始受限额度
				ba.setOrgLimitedAmount(dplan.doubleAddDouble(ba.getOrgLimitedAmount(), null==qv.getOrgLimitedAmount()?0.00:qv.getOrgLimitedAmount()));
				// 当前受限额度
				ba.setCurrentLimitedAmount(
						dplan.doubleAddDouble(ba.getCurrentLimitedAmount(), null==qv.getCurrentLimitedAmount()?0.00:qv.getCurrentLimitedAmount()));
				// 总入账金额
				ba.setTotalAccountAmount(dplan.doubleAddDouble(ba.getTotalAccountAmount(), null==qv.getTotalAccountAmount()?0.00:qv.getTotalAccountAmount()));
				// 溢出金额
				ba.setSpilloverAmount(dplan.doubleAddDouble(ba.getSpilloverAmount(), null==qv.getSpilloverAmount()?0.00:qv.getSpilloverAmount()));
				// 已拨付金额
				ba.setPayoutAmount(dplan.doubleAddDouble(ba.getPayoutAmount(), null==qv.getPayoutAmount()?0.00:qv.getPayoutAmount()));
				// 已申请未拨付金额
				ba.setAppliedNoPayoutAmount(
						dplan.doubleAddDouble(ba.getAppliedNoPayoutAmount(), null==qv.getAppliedNoPayoutAmount()?0.00:qv.getAppliedNoPayoutAmount()));
				// 已申请退款未拨付金额
				ba.setApplyRefundPayoutAmount(
						dplan.doubleAddDouble(ba.getApplyRefundPayoutAmount(), null==qv.getApplyRefundPayoutAmount()?0.00:qv.getApplyRefundPayoutAmount()));
				// 已退款金额
				ba.setRefundAmount(dplan.doubleAddDouble(ba.getRefundAmount(), null==qv.getRefundAmount()?0.00:qv.getRefundAmount()));
				// 当前托管余额
				ba.setCurrentEscrowFund(dplan.doubleAddDouble(ba.getCurrentEscrowFund(), null==qv.getCurrentEscrowFund()?0.00:qv.getCurrentEscrowFund()));
				// 可划拨金额
				ba.setAllocableAmount(dplan.doubleAddDouble(ba.getAllocableAmount(), null==qv.getAllocableAmount()?0.00:qv.getAllocableAmount()));
				
				// 现金受限额度
				ba.setXjsxPrice(dplan.doubleAddDouble(ba.getXjsxPrice(), null==qv.getXjsxPrice()?0.00:qv.getXjsxPrice()));
				// 有效受限额度
				ba.setYxsxPrice(dplan.doubleAddDouble(ba.getYxsxPrice(), null==qv.getYxsxPrice()?0.00:qv.getYxsxPrice()));
				// 支付保证金额
				ba.setZfbzPrice(dplan.doubleAddDouble(ba.getZfbzPrice(), null==qv.getZfbzPrice()?0.00:qv.getZfbzPrice()));
				
				ba.setSumfamilyNumber(ba.getSumfamilyNumber() + (null == qv.getSumfamilyNumber()?0:qv.getSumfamilyNumber()));
				ba.setSignhouseNum(ba.getSignhouseNum() + (null == qv.getSignhouseNum()?0:qv.getSignhouseNum()));
				ba.setRecordhouseNum(ba.getRecordhouseNum() + (null == qv.getRecordhouseNum()?0:qv.getRecordhouseNum()));
				ba.setDeposithouseNum(ba.getDeposithouseNum() + (null == qv.getDeposithouseNum()?0:qv.getDeposithouseNum()));

			}

			// 合计信息入列表
			ba.setTheNameOfCompany("合计");
			qs_BuildingAccount_ViewList.add(ba);

		}
		else
		{
			qs_BuildingAccount_ViewList = new ArrayList<Qs_BuildingAccount_View>();
		}

		properties.put("qs_BuildingAccount_ViewList", qs_BuildingAccount_ViewList);
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

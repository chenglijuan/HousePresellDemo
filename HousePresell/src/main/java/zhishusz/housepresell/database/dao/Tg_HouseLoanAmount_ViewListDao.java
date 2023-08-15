package zhishusz.housepresell.database.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import oracle.jdbc.OracleTypes;
import zhishusz.housepresell.database.po.Tg_HouseLoanAmount_View;

/*
 * Dao数据库操作：户入账明细
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_HouseLoanAmount_ViewListDao extends BaseDao<Tg_HouseLoanAmount_View> {

	@Autowired
	private SessionFactory sessionFactory;

	public String getBasicHQL() {
		return "from Tg_HouseLoanAmount_View where 1=1"
				+ " <#if keyword??> and (companyName like :keyword or projectName like :keyword or eCodeFroMconstruction like :keyword or billTimeStamp like :keyword or unitCode like :keyword or roomId like :keyword or buyerName like :keyword)</#if>"
				+ " <#if companyName??> and companyName=:companyName</#if>"
				+ " <#if companyNames?? && (companyNames?size>0)> and companyName in :companyNames</#if>"
				+ " <#if projectName??> and projectName=:projectName</#if>"
				+ " <#if ECodeFroMconstruction??> and eCodeFroMconStruction=:ECodeFroMconstruction</#if>"
				+ " <#if paymentMethod??> and paymentMethod=:paymentMethod</#if>"
				+ " <#if fundProperty??> and fundProperty=:fundProperty</#if>"
				+ " <#if billTimeStamp??> and reconciliationTSFromOB >=:billTimeStamp</#if>"
				+ " <#if endBillTimeStamp??> and reconciliationTSFromOB <=:endBillTimeStamp</#if>"
				+ " order by billTimeStamp desc, companyName asc, projectName asc, eCodeFroMconstruction desc, unitCode asc, roomId asc";
	}

	public String getHouseBasicHQL() {
		return "from Tg_HouseLoanAmount_View where 1=1" + " <#if companyName??> and companyName=:companyName</#if>"
				+ " <#if companyNames?? && (companyNames?size>0)> and companyName in :companyNames</#if>"
				+ " <#if projectName??> and projectName=:projectName</#if>"
				+ " <#if ECodeFroMconstruction??> and eCodeFroMconStruction=:ECodeFroMconstruction</#if>"
				+ " <#if ECodeOfTripleagreement??> and eCodeOfTripleagreement=:ECodeOfTripleagreement</#if>"
				+ " <#if paymentMethod??> and paymentMethod=:paymentMethod</#if>"
				+ " <#if fundProperty??> and fundProperty=:fundProperty</#if>"
				+ " <#if billTimeStamp??> and reconciliationTSFromOB >=:billTimeStamp</#if>"
				+ " <#if endBillTimeStamp??> and reconciliationTSFromOB <=:endBillTimeStamp</#if>"
				+ " order by billtimestamp desc";
	}

	/**
	 * 户入账明细
	 * 
	 * @param comapanyId   开发企业Id
	 * @param projectId    项目Id
	 * @param keyWord      关键字
	 * @param beginTime    开始日期
	 * @param endTime      结束日期
	 * @param pageNumber   第几页
	 * @param countPerPage 每页显示条数
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getHouseLoanAmount_View(String userId, String comapanyId, String projectId,
			String buildingId, String keyWord, String startBillTimeStamp, String endBillTimeStamp, Integer pageNumber,
			Integer countPerPage) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call GET_HouseLoanAmount_View(?,?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		
		try {
			// 设置参数
			sp.setString(1, userId);
			sp.setString(2, comapanyId);
			sp.setString(3, projectId);
			sp.setString(4, buildingId);
			sp.setString(5, keyWord);
			sp.setString(6, startBillTimeStamp);
			sp.setString(7, endBillTimeStamp);
			sp.setInt(8, pageNumber);
			sp.setInt(9, countPerPage);

			// 游标类型
			sp.registerOutParameter(10, OracleTypes.CURSOR);
			sp.registerOutParameter(11, OracleTypes.INTEGER);
			sp.registerOutParameter(12, OracleTypes.INTEGER);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(10);

			List<Tg_HouseLoanAmount_View> tg_HouseLoanAmount_ViewList = new ArrayList<Tg_HouseLoanAmount_View>();
			Tg_HouseLoanAmount_View tg_HouseLoanAmount_View = null;

			while (rs.next()) {
				tg_HouseLoanAmount_View = new Tg_HouseLoanAmount_View();

				// 主键
				tg_HouseLoanAmount_View.setTableId(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));				
				//记账日期
				tg_HouseLoanAmount_View.setBillTimeStamp(ObjectUtils.toString(rs.getObject(2)));
				// 开发企业
				tg_HouseLoanAmount_View.setCompanyName(ObjectUtils.toString(rs.getObject(3)));
				// 项目名称
				tg_HouseLoanAmount_View.setProjectName(ObjectUtils.toString(rs.getObject(4)));
				// 楼幢
				tg_HouseLoanAmount_View.seteCodeFroMconstruction(ObjectUtils.toString(rs.getObject(5)));
				//公安编号
				tg_HouseLoanAmount_View.seteCodeFromPublicSecurity(ObjectUtils.toString(rs.getObject(6)));
				//单元
				tg_HouseLoanAmount_View.setUnitCode(ObjectUtils.toString(rs.getObject(7)));				
				//房间号
				tg_HouseLoanAmount_View.setRoomId(ObjectUtils.toString(rs.getObject(8)));
				//建筑面积
				tg_HouseLoanAmount_View.setForEcastArea(ObjectUtils.toString(rs.getObject(9)));
				//合同状态
				tg_HouseLoanAmount_View.setContractStatus(Integer.valueOf(ObjectUtils.toString(rs.getObject(10))));				
				//买受人名称
				tg_HouseLoanAmount_View.setBuyerName(ObjectUtils.toString(rs.getObject(11)));				
				//买受人证件号
				tg_HouseLoanAmount_View.seteCodeOfcertificate(ObjectUtils.toString(rs.getObject(12)));
				//合同总价
				tg_HouseLoanAmount_View.setContractSumPrice(Double.valueOf(ObjectUtils.toString(rs.getObject(13))));
				//付款方式
				tg_HouseLoanAmount_View.setPaymentMethod(Integer.valueOf(ObjectUtils.toString(rs.getObject(14))));	
				//按揭金额
				tg_HouseLoanAmount_View.setLoanAmount(Double.valueOf(ObjectUtils.toString(rs.getObject(15))));				
				//三方协议号
				tg_HouseLoanAmount_View.seteCodeOfTripleagreement(ObjectUtils.toString(rs.getObject(16)));				
				//入账金额
				tg_HouseLoanAmount_View.setLoanAmountIn(Double.valueOf(ObjectUtils.toString(rs.getObject(17))));
				//资金性质
				tg_HouseLoanAmount_View.setFundProperty(Integer.valueOf(ObjectUtils.toString(rs.getObject(18))));	
				//入账日期
				tg_HouseLoanAmount_View.setReconciliationTSFromOB(ObjectUtils.toString(rs.getObject(19)));
				//贷款银行
				tg_HouseLoanAmount_View.setLoanBank(ObjectUtils.toString(rs.getObject(20)));				
				//主借款人姓名
				tg_HouseLoanAmount_View.setTheNameOfCreditor(ObjectUtils.toString(rs.getObject(21)));

				tg_HouseLoanAmount_ViewList.add(tg_HouseLoanAmount_View);
			}

			map.put("tg_HouseLoanAmount_ViewList", tg_HouseLoanAmount_ViewList);

			int totalPage = sp.getInt(11);
			map.put("totalPage", totalPage);

			int totalCount = sp.getInt(12);

			map.put("totalCount", totalCount);

			if (null != rs) {
				rs.close();
			}
			sp.close();

		} catch (SQLException e) {
			System.out.println("查询异常");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (sp != null) {
				sp.close();
			}
			if (connection != null) {
				connection.close();
			}
			/*if(sessionFactory!=null) {
				sessionFactory.getCurrentSession().flush();
			}*/
		}
		return map;
	}
}

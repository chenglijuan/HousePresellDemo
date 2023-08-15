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
import zhishusz.housepresell.database.po.Tg_Build_View;

/*
 * Dao数据库操作：托管楼幢明细
 * Company：ZhiShuSZ
 * */
@Repository
public class Tg_Build_ViewListDao extends BaseDao<Tg_Build_View> {
	/*
	 * 关键字搜索条件 单元信息、房间号、合同编号、三方协议号
	 */

	@Autowired
	private SessionFactory sessionFactory;

	public String getBasicHQL() {
		return "from Tg_Build_View where 1=1"
				+ " <#if keyword??> and (unitCode like :keyword or roomId like :keyword or contractNo like :keyword or eCodeOfTripleagreement like :keyword )</#if>"
				+ " <#if companyName??> and companyName=:companyName</#if>"
				+ " <#if projectName??> and projectName=:projectName</#if>"
				+ " <#if escrowState??> and escrowState =:escrowState</#if>"
				+ " <#if paymentMethod??> and paymentMethod =:paymentMethod</#if>"
				+ " <#if ECodeFroMconstruction??> and eCodeFroMconstruction=:ECodeFroMconstruction</#if>"
				+ " <#if billTimeStamp??> and billTimeStamp >=:billTimeStamp</#if>"
				+ " <#if endBillTimeStamp??> and billTimeStamp <=:endBillTimeStamp</#if>"
				+ " order by companyName asc, projectName asc, eCodeFroMconstruction asc, escrowState asc";
	}

	/**
	 * 托管楼幢明细
	 * 
	 * @param userid        用户主键
	 * @param companyId     企业主键
	 * @param projectId     项目主键
	 * @param buildId       楼幢主键
	 * @param keyWord       关键字
	 * @param beginTime     开始日期
	 * @param endTime       结束日期
	 * @param paymentMethod 付款类型
	 * @param escrowState   托管状态
	 * @param pageNumber    第几页
	 * @param countPerPage  每页显示条数
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getBuild_View(String userid, String companyId, String projectId, String buildId,
			String keyWord, String paymentMethod, String escrowState, String beginTime, String endTime,
			Integer pageNumber, Integer countPerPage) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call GET_Build_View(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		try {
			// 设置参数
			sp.setString(1, userid);
			sp.setString(2, companyId);
			sp.setString(3, projectId);
			sp.setString(4, buildId);
			sp.setString(5, keyWord);
			sp.setString(6, paymentMethod);
			sp.setString(7, escrowState);
			sp.setString(8, beginTime);
			sp.setString(9, endTime);
			sp.setInt(10, pageNumber);
			sp.setInt(11, countPerPage);

			// 游标类型
			sp.registerOutParameter(12, OracleTypes.CURSOR);
			sp.registerOutParameter(13, OracleTypes.INTEGER);
			sp.registerOutParameter(14, OracleTypes.INTEGER);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			
			rs = (ResultSet) sp.getObject(12);

			List<Tg_Build_View> tg_Build_ViewList = new ArrayList<Tg_Build_View>();
			Tg_Build_View tg_Build_View = null;

			while (rs.next()) {
				tg_Build_View = new Tg_Build_View();

				// 主键
				tg_Build_View.setTableId(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));
				// 记账日期
				tg_Build_View.setBillTimeStamp(ObjectUtils.toString(rs.getObject(2)));
				// 开发企业
				tg_Build_View.setCompanyName(ObjectUtils.toString(rs.getObject(3)));
				// 项目
				tg_Build_View.setProjectName(ObjectUtils.toString(rs.getObject(4)));
				// 楼幢
				tg_Build_View.seteCodeFroMconstruction(ObjectUtils.toString(rs.getObject(5)));
				// 单元
				tg_Build_View.setUnitCode(ObjectUtils.toString(rs.getObject(6)));
				// 房间号
				tg_Build_View.setRoomId(ObjectUtils.toString(rs.getObject(7)));
				// 户建筑面积
				tg_Build_View.setForEcastArea(ObjectUtils.toString(rs.getObject(8)));
				// 合同状态
				tg_Build_View.setContractStatus(ObjectUtils.toString(rs.getObject(9)).isEmpty()?null:Integer.valueOf(ObjectUtils.toString(rs.getObject(9))));
				// 合同编号
				tg_Build_View.setContractNo(ObjectUtils.toString(rs.getObject(10)));
				// 三方协议号
				tg_Build_View.seteCodeOfTripleagreement(ObjectUtils.toString(rs.getObject(11)));
				// 承购人姓名
				tg_Build_View.setBuyerName(ObjectUtils.toString(rs.getObject(12)));
				// 证件号
				tg_Build_View.seteCodeOfCertificate(ObjectUtils.toString(rs.getObject(13)));
				// 借款人姓名
				tg_Build_View.setTheNameOfCreditor(ObjectUtils.toString(rs.getObject(14)));
				// 合同总价
				tg_Build_View.setContractSumPrice(Double.valueOf(ObjectUtils.toString(rs.getObject(15))));
				// 付款方式
				tg_Build_View.setPaymentMethod(Integer.valueOf(ObjectUtils.toString(rs.getObject(16))));
				// 首付款金额
				tg_Build_View.setFirstPaymentAmount(Double.valueOf(ObjectUtils.toString(rs.getObject(17))));
				// 合同贷款金额
				tg_Build_View.setLoanAmount(Double.valueOf(ObjectUtils.toString(rs.getObject(18))));
				// 托管状态
				tg_Build_View.setEscrowState(Integer.valueOf(ObjectUtils.toString(rs.getObject(19))));
				// 合同签订日期
				tg_Build_View.setContractSignDate(ObjectUtils.toString(rs.getObject(20)));
				// 贷款入账金额
				tg_Build_View.setLoanAmountIn(Double.valueOf(ObjectUtils.toString(rs.getObject(21))));
				// 留存权益
				tg_Build_View.setTheAmountOfRetainedequity(Double.valueOf(ObjectUtils.toString(rs.getObject(22))));
				// 对账状态
				tg_Build_View.setStatementState(Integer.valueOf(ObjectUtils.toString(rs.getObject(23))));		
		         

				tg_Build_ViewList.add(tg_Build_View);
			}

			map.put("tg_Build_ViewList", tg_Build_ViewList);

			int totalPage = sp.getInt(13);
			map.put("totalPage", totalPage);

			int totalCount = sp.getInt(14);

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
		}
		return map;
	}
}

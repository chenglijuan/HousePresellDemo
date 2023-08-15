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
import zhishusz.housepresell.database.po.Tg_RetainedRightsView;
import zhishusz.housepresell.database.po.Tg_RetainedRightsView2;

/*
 * Dao数据库操作：留存权益查询
 */
@Repository
public class Tgpf_RetainedRightsDao extends BaseDao<Tg_RetainedRightsView>
{
	@Autowired
	private SessionFactory sessionFactory;
	
	public String getBasicHQL()
	{
		return "from Tg_RetainedRightsView where 1=1"	
                + " <#if enterTimeStampStart??> and enterTimeStamp <=: enterTimeStampStart</#if>"
                + " <#if enterTimeStampEnd??> and enterTimeStamp >= : enterTimeStampEnd</#if>"
                + " <#if fromDate??> and fromDate = :fromDate</#if>"
                + " <#if projectName??> and projectName = :projectName</#if>"
                + " <#if keyword??> and (projectName like :keyword or ecodeFromConstruction like :keyword or ecodeOfBuildingUnit like :keyword or ecodeFromRoom like :keyword or buyer like :keyword ) </#if>";
	}
	
	/**
	 * 留存权益查询
	 * 
	 * @param userid       用户主键
	 * @param projectId    项目主键
	 * @param keyWord      关键字
	 * @param beginTime    开始日期
	 * @param endTime      结束日期
	 * @param pageNumber   第几页
	 * @param countPerPage 每页显示条数
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> getRetainedRightsView(String userid,  String projectId,
			String keyWord, String beginTime, String endTime, Integer pageNumber, Integer countPerPage)
			throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
		String sql = "{call GET_RetainedRightsView(?,?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		
		try {
			// 设置参数
			sp.setString(1, userid);
			sp.setString(2, projectId);
			sp.setString(3, keyWord);
			sp.setString(4, beginTime);
			sp.setString(5, endTime);
			sp.setInt(6, pageNumber);
			sp.setInt(7, countPerPage);

			// 游标类型
			sp.registerOutParameter(8, OracleTypes.CURSOR);
			sp.registerOutParameter(9, OracleTypes.INTEGER);
			sp.registerOutParameter(10, OracleTypes.INTEGER);
			// 执行存储过程
			sp.execute();
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(8);

			List<Tg_RetainedRightsView> tg_RetainedRightsList = new ArrayList<Tg_RetainedRightsView>();
			Tg_RetainedRightsView tg_RetainedRightsView = null;

			while (rs.next()) {
				tg_RetainedRightsView = new Tg_RetainedRightsView();

				// 主键
				tg_RetainedRightsView.setTableId(Long.valueOf(ObjectUtils.toString(rs.getObject(1))));				
				//到账日期
				tg_RetainedRightsView.setArrivalTimeStamp(ObjectUtils.toString(rs.getObject(2)));
				//入账日期
				tg_RetainedRightsView.setEnterTimeStamp(ObjectUtils.toString(rs.getObject(3)));
				//项目名称
				tg_RetainedRightsView.setProjectName(ObjectUtils.toString(rs.getObject(4)));
				//楼幢施工编号
				tg_RetainedRightsView.setEcodeFromConstruction(ObjectUtils.toString(rs.getObject(5)));
				//单元信息
				tg_RetainedRightsView.setEcodeOfBuildingUnit(ObjectUtils.toString(rs.getObject(6)));
				//房间号
				tg_RetainedRightsView.setEcodeFromRoom(ObjectUtils.toString(rs.getObject(7)));
				//买受人名称
				tg_RetainedRightsView.setBuyer(ObjectUtils.toString(rs.getObject(8)));
				//按揭入账金额
				tg_RetainedRightsView.setDepositAmountFromloan(Double.valueOf(ObjectUtils.toString(rs.getObject(9))));
				//累计支付金额
				tg_RetainedRightsView.setCumulativeAmountPaid(Double.valueOf(ObjectUtils.toString(rs.getObject(10))));
				//留存权益总金额
				tg_RetainedRightsView.setTheAmount(Double.valueOf(ObjectUtils.toString(rs.getObject(11))));
				//未到期权益金额
				tg_RetainedRightsView.setAmountOfInterestNotdue(Double.valueOf(ObjectUtils.toString(rs.getObject(12))));
				//到期权益金额
				tg_RetainedRightsView.setAmountOfInterestdue(Double.valueOf(ObjectUtils.toString(rs.getObject(13))));
				//本次支付金额
				tg_RetainedRightsView.setAmountOfThisPayment(Double.valueOf(ObjectUtils.toString(null == rs.getObject(14)?0.00:rs.getObject(14))));
				//拨付日期
				tg_RetainedRightsView.setFromDate(ObjectUtils.toString(rs.getObject(15)));				

				tg_RetainedRightsList.add(tg_RetainedRightsView);
			}

			map.put("tg_RetainedRightsList", tg_RetainedRightsList);

			int totalPage = sp.getInt(9);
			map.put("totalPage", totalPage);

			int totalCount = sp.getInt(10);

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

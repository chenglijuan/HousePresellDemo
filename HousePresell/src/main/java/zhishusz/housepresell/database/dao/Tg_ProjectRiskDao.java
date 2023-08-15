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
import zhishusz.housepresell.database.po.Tg_ProjectRiskView;

/*
 * Dao : 项目风险明细表
 * */
@Repository
public class Tg_ProjectRiskDao	extends BaseDao<Tg_ProjectRiskView>
{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public String getBasicHQL(){
		
		return "FROM Tg_ProjectRiskView WHERE 1=1"
				+"<#if managedProjects??> and managedProjects =:managedProjects</#if>"
				+"<#if dateQuery??> and dateQuery =:dateQuery</#if>"
				+"<#if area??> and area =:area</#if>"
				+"<#if attachment??> and attachment =:attachment</#if>"
				+"<#if landMortgage??> and landMortgage =:landMortgage</#if>"
				+"<#if riskRating??> and riskRating =:riskRating</#if>"
				+" order by managedProjects desc";
				
	}
	

	/**
	 * 项目风险明细表调用存储过程查询
	 * @param PI_QY 区域Id
	 * @param PI_PROJECT 项目Id
	 * @param PI_RQ 托管日期
	 * @param PI_ISDY 是否抵押
	 * @param PI_RISKRANK 风险评级
	 * @param PI_USER 当前登录人
	 * @param attachment 查封情况(未签合同)
	 * @param attachmented 查封情况(已签合同)
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, String>> getTg_ProjectRisk(Long PI_QY,Long PI_PROJECT,String PI_RQ,Long PI_ISDY,Long PI_RISKRANK,Long PI_USER,String attachment,String attachmented) throws SQLException
	{
		List<Map<String, String>> list = null;
		Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();

		String sql = "{call PRC_GET_PROJECTRISK(?,?,?,?,?,?,?,?,?)}";
		CallableStatement sp = connection.prepareCall(sql);
		ResultSet rs = null;
		
		try
		{
			//设置参数
			sp.setLong(1, PI_QY);
			sp.setLong(2, PI_PROJECT);
			sp.setString(3, PI_RQ);
			sp.setLong(4, PI_ISDY);
			sp.setLong(5, PI_RISKRANK);
			sp.setLong(7, PI_USER);
			sp.setString(8, attachment);
			sp.setString(9, attachmented);
			
			// 游标类型
			sp.registerOutParameter(6, OracleTypes.CURSOR);
			// 执行存储过程
			sp.execute(); 
			// 获取返回的对象,再将对象转为记录集
			rs = (ResultSet) sp.getObject(6); 
//			Integer a = 0;
//			while (rs.next())
//			{
//				a++;
//			}
//			System.out.println("记录数="+a);
			list = new ArrayList<Map<String, String>>();
			Map<String, String> resultMap = null;
			
			while (rs.next())
			{
				resultMap = new HashMap<String, String>();
				
				resultMap.put("TABLEID", ObjectUtils.toString(rs.getObject(1)));
				//托管项目
				resultMap.put("MANAGEDPROJECTS", ObjectUtils.toString(rs.getObject(2)));
				//托管楼幢
				resultMap.put("FLOORBUILDING", ObjectUtils.toString(rs.getObject(3)));
				//托管面积(平方米)
				resultMap.put("MANAGEDAREA", ObjectUtils.toString(rs.getObject(4)));
				//预售许可批准日期
				resultMap.put("DATEOFPRESALE", ObjectUtils.toString(rs.getObject(5)));
				//地上总层数
				resultMap.put("TOTALOFOVERGROUND", ObjectUtils.toString(rs.getObject(6)));
				//当前建设进度
				resultMap.put("CURRENTCONSTRUCTION", ObjectUtils.toString(rs.getObject(7)));
				//进度更新时间
				resultMap.put("UPDATETIME", ObjectUtils.toString(rs.getObject(8)));
				//合同约定交付时间
				resultMap.put("APPOINTEDTIME", ObjectUtils.toString(rs.getObject(9)));
				//进度评定
				resultMap.put("PROGRESSEVALUATION", ObjectUtils.toString(rs.getObject(10)));
				//合同签订率
				resultMap.put("SIGNTHEEFFICIENCY", ObjectUtils.toString(rs.getObject(11)));
				//合同备案率
				resultMap.put("CONTRACTFILINGRATE", ObjectUtils.toString(rs.getObject(12)));
				//合同贷款率
				resultMap.put("CONTRACTLOANRATIO", ObjectUtils.toString(rs.getObject(13)));
				//托管满额率
				resultMap.put("HOSTINGFULLRATE", ObjectUtils.toString(rs.getObject(14)));
				//未签订合同查封
				resultMap.put("UNSIGNEDCONTRACT", ObjectUtils.toString(rs.getObject(15)));
				//已签订合同查封
				resultMap.put("ALREADYUNSIGNEDCONTRACT", ObjectUtils.toString(rs.getObject(16)));
				//限制
				resultMap.put("ASTRICT", ObjectUtils.toString(rs.getObject(17)));
				//土地抵押情况(有/无)
				resultMap.put("LANDMORTGAGE", ObjectUtils.toString(rs.getObject(18)));
				//其他风险
				resultMap.put("OTHERRISKS", ObjectUtils.toString(rs.getObject(19)));
				//风险评级(高/中/低)
				resultMap.put("RISKRATING", ObjectUtils.toString(rs.getObject(20)));
				//所属区域
				resultMap.put("AREA", ObjectUtils.toString(rs.getObject(21)));
				//日期
				resultMap.put("DATEQUERY", ObjectUtils.toString(rs.getObject(22)));
				//查封情况
				resultMap.put("ATTACHMENT", ObjectUtils.toString(rs.getObject(23)));
				//有无保函(有/无)
                resultMap.put("PAYGUARANTEE", ObjectUtils.toString(rs.getObject(28)));

				list.add(resultMap);
			}
			
			if(null!=rs)
			{
				rs.close();
			}
			sp.close();
			

		}
		catch (SQLException e)
		{
			System.out.println("查询异常");
			e.printStackTrace();
		}
		finally
		{
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
		return list;
	}
	
}

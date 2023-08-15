package zhishusz.housepresell.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import zhishusz.housepresell.controller.form.extra.Tb_b_contractFrom;
import zhishusz.housepresell.database.po.state.S_NormalFlag;

/**
 * 棣栭〉灞曠ず鍔犺浇-鍚堜綔鏈烘瀯棣栭〉
 * 
 * @ClassName: Sm_HomePageLoadViewDepositService
 * @Description:TODO
 * @author: yangyu
 * @date: 2019骞�1鏈�10鏃�
 * @version V1.0
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class Sm_HomePageLoadViewDepositService
{
	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings({})
	public Properties execute(Tb_b_contractFrom model)
	{

		Properties properties = new Properties();

		Long regionId = model.getCityRegionId();

		if (null == regionId || regionId == 1L)
		{
			regionId = null;
		}

		String comanyId = String.valueOf(model.getUser().getCompany().getTableId());

		String sql = "SELECT * "
				+ "  FROM (SELECT row_number() over(ORDER BY SUBSTR(A.DEPOSITDATETIME, 1, 7) DESC) ROW_NUM, "
				+ "               SUBSTR(A.DEPOSITDATETIME, 1, 7) AS queryTime , "
				+ "               SUM(DECODE(A.LX, 1, A.LOANAMOUNTFROMBANK, 0))/10000 AS loanAmountFromBankC, "
				+ "               SUM(DECODE(A.LX, " + "                          2, "
				+ "                          A.LOANAMOUNTFROMBANK, " + "                          3, "
				+ "                          A.LOANAMOUNTFROMBANK, "
				+ "                          0))/10000 AS currentPayoutAmountC " + "          FROM V_DETAIL A "
				+ "         WHERE EXISTS " + "         (SELECT NULL " + "                  FROM EMPJ_PROJECTINFO P "
				+ "                 WHERE EXISTS(SELECT NULL "
				+ "                                FROM SM_PERMISSION_RANGEAUTH C "
				+ "                               INNER JOIN SM_PERMISSION_RANGE B "
				+ "                                  ON C.TABLEID = B.RANGEAUTH "
				+ "                               WHERE C.EMMP_COMPANYINFO = ? "
				+ "                                           AND (? IS NULL OR B.CITYREGIONINFO = ?)"
				+ "                                 AND P.CITYREGION = B.CITYREGIONINFO) AND P.TABLEID = A.PROJECT) "
				+ "           AND SUBSTR(A.DEPOSITDATETIME, 1, 7) < TO_CHAR(SYSDATE, 'YYYY-MM') "
				+ "         GROUP BY SUBSTR(A.DEPOSITDATETIME, 1, 7)) " + " WHERE ROW_NUM <= 12 "
				+ " ORDER BY ROW_NUM DESC";

		List<Map<String, Object>> listMap = new ArrayList<>();

		try
		{
			Connection connection = SessionFactoryUtils.getDataSource(sessionFactory).getConnection();
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, comanyId);

			if (null == regionId)
			{
				ps.setString(2, "");
				ps.setString(3, "");
			}
			else
			{
				ps.setLong(2, regionId);
				ps.setLong(3, regionId);
			}

			ResultSet rs = ps.executeQuery();
			Map<String, Object> depositMap = null;

			while (rs.next())
			{
				depositMap = new HashMap<>();

				depositMap.put("queryTime", rs.getString("QUERYTIME"));

				depositMap.put("loanAmountFromBankC", rs.getString("LOANAMOUNTFROMBANKC"));// 閾惰鏀炬閲戦

				depositMap.put("currentPayoutAmountC", rs.getString("CURRENTPAYOUTAMOUNTC"));// 閾惰鎷ㄤ粯閲戦

				listMap.add(depositMap);

			}

			if (null != rs)
			{
				rs.close();
			}

			connection.close();

		}
		catch (SQLException e)
		{
			System.out.println("鏌ヨ寮傚父");
			e.printStackTrace();
		}

		properties.put("depositSituationMapList", listMap);
		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
}

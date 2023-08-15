package zhishusz.housepresell.database.dao.extra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.extra.Tb_b_contract;
import zhishusz.housepresell.database.po.extra.Tb_b_personofcontract;
import zhishusz.housepresell.util.JDBCConnectionUtil;

/**
 * 买受人Dao
 * 
 * @ClassName: Tb_b_personofcontractDao
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月7日 上午10:59:37
 * @version V1.0
 *
 */
@Repository
public class Tb_b_personofcontractDao
{

	private JDBCConnectionUtil dao = new JDBCConnectionUtil();

	// 根据合同备案号查询买受人信息
	public List<Tb_b_personofcontract> getTb_b_personofcontractDetail2(String beianno)
	{

		List<Tb_b_personofcontract> tb_b_personofcontractList = new ArrayList<Tb_b_personofcontract>();

		Tb_b_personofcontract vo;

		try
		{

			Connection conn = dao.getConn();

			/*
			 * xsz by time 2018-11-14 17:21:51
			 * 使用左关联查询
			 * 
			 * String sql =
			 * "select a.rowguid contractId,a.beianno eCodeOfContract,a.msr buyerName,'1' certificateType,a.msrsfz eCodeOfcertificate,a.lxsj contactPhone,nvl(b.lxdz,'-') contactAdress,'-' agentName,'-' agentCertType,'-' agentCertNumber,'-' agentPhone,'-' agentAddress from tb_b_contract a,v_tb_b_contractxml b where a.rowguid=b.rowguid and a.htbh='"
			 * + beianno + "'";
			 */
			String sql = "select a.rowguid contractId,a.beianno eCodeOfContract,a.msr buyerName,'1' certificateType,a.msrsfz eCodeOfcertificate,a.lxsj contactPhone,nvl(b.lxdz, '-') contactAdress,'-' agentName,'-' agentCertType,'-' agentCertNumber,'-' agentPhone,'-' agentAddress from tb_b_contract a left join v_tb_b_contractxml b on a.rowguid = b.rowguid where a.htbh = ? ";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, beianno);
			ResultSet rs = ps.executeQuery();

			while (rs.next())
			{
				vo = new Tb_b_personofcontract();

				vo.setContractId(rs.getString("contractId"));
				vo.setBuyerName(rs.getString("buyerName"));
				vo.setCertificateType(rs.getString("certificateType"));
				vo.setECodeOfcertificate(rs.getString("eCodeOfcertificate"));
				vo.setContactPhone(rs.getString("contactPhone"));
				vo.setContactAdress(null == rs.getString("contactAdress") ? "" : rs.getString("contactAdress"));
				vo.setAgentName(rs.getString("agentName"));
				vo.setAgentCertType(rs.getString("agentCertType"));
				vo.setAgentCertNumber(rs.getString("agentCertNumber"));
				vo.setAgentPhone(rs.getString("agentPhone"));
				vo.setAgentAddress(rs.getString("agentAddress"));
				vo.setECodeOfContract(rs.getString("eCodeOfContract"));

				tb_b_personofcontractList.add(vo);
			}

			if (conn != null)
			{
				conn.close();
			}

		}
		catch (Exception e)
		{
			System.out.println("连接异常" + e.getMessage());
		}

		return tb_b_personofcontractList;

	}

}

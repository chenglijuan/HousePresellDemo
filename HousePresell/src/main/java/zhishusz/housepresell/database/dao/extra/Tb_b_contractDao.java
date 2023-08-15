package zhishusz.housepresell.database.dao.extra;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Repository;

import zhishusz.housepresell.database.po.extra.Tb_b_contract;
import zhishusz.housepresell.util.JDBCConnectionUtil;

/**
 * 合同信息Dao
 * 
 * @ClassName: Tb_b_ontractDao
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月2日 下午8:53:57
 * @version V1.0
 *
 */
@Repository
public class Tb_b_contractDao {

    private JDBCConnectionUtil dao = new JDBCConnectionUtil();

    // 根据合同备案号查询合同信息
    public Tb_b_contract getTb_b_ontractDetail2(String beianno) {

        Tb_b_contract vo = new Tb_b_contract();

        try {

            Connection conn = dao.getConn();

            /*
             * xsz by time 2018-11-14 12:34:54
             * 改用段总提供的视图进行查询
             * 字段保持不变
             * 
             * String sql = "select c.*,d.jfrq from tb_b_contract
             * c,tb_b_contractxml d where c.htbh='"+beianno+"' and
             * c.rowguid=d.rowguid";
             */

            /*
             * xsz by time 2018-11-14 12:34:54
             * 改用段总提供的视图进行查询
             * 字段保持不变
             */
            String sql = "select * from VIEW_B_CONTRACT where (htbh= ? or BEIANNO=? ) and is_delete = 0";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, beianno);
            ps.setString(2, beianno);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                vo.setBeianno(null == rs.getString("htbh") ? "" : rs.getString("htbh").trim());
                vo.setRoomlocation(null == rs.getString("roomlocation") ? "" : rs.getString("roomlocation").trim());
                vo.setMsr(null == rs.getString("msr") ? "" : rs.getString("msr").trim());
                vo.setCmr(null == rs.getString("cmr") ? "" : rs.getString("cmr").trim());
                vo.setProjectid(null == rs.getString("projectid") ? "" : rs.getString("projectid").trim());
                vo.setBuildingid(null == rs.getString("buildingid") ? "" : rs.getString("buildingid").trim());
                vo.setRoomid(null == rs.getString("roomid") ? "" : rs.getString("roomid").trim());
                vo.setQdwctime(null == rs.getString("qdwctime") ? "" : rs.getString("qdwctime").trim());
                vo.setQdtime(null == rs.getString("qdtime") ? "" : rs.getString("qdtime").trim());
                vo.setContractprice(rs.getString("contractprice").trim());
                vo.setMj(rs.getString("mj").trim());
                vo.setFkfs(rs.getString("fkfs").trim());
                vo.setHtbh(rs.getString("htbh").trim());
                vo.setJfrq(null == rs.getString("jfrq") ? "" : rs.getString("jfrq").trim());
                vo.setSfk(rs.getString("sfk").trim());
            }

            if (conn != null) {
                conn.close();
            }

        } catch (Exception e) {
            System.out.println("连接异常" + e.getMessage());
        }

        return vo;
    }

    // 根据户室查询合同信息
    public Tb_b_contract getContractByRoomId(String roomId) {

        Tb_b_contract vo = new Tb_b_contract();

        try {

            Connection conn = dao.getConn();

            /*
             * xsz by time 2018-11-14 12:34:54
             * 改用段总提供的视图进行查询
             * 字段保持不变
             * 
             * String sql = "select c.*,d.jfrq from tb_b_contract
             * c,tb_b_contractxml d where c.htbh='"+beianno+"' and
             * c.rowguid=d.rowguid";
             */

            /*
             * xsz by time 2018-11-14 12:34:54
             * 改用段总提供的视图进行查询
             * 字段保持不变
             */
            String sql = "select * from VIEW_B_CONTRACT where roomid= ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roomId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                vo.setBeianno(null == rs.getString("htbh") ? "" : rs.getString("htbh").trim());
                vo.setRoomlocation(null == rs.getString("roomlocation") ? "" : rs.getString("roomlocation").trim());
                vo.setMsr(null == rs.getString("msr") ? "" : rs.getString("msr").trim());
                vo.setCmr(null == rs.getString("cmr") ? "" : rs.getString("cmr").trim());
                vo.setProjectid(null == rs.getString("projectid") ? "" : rs.getString("projectid").trim());
                vo.setBuildingid(null == rs.getString("buildingid") ? "" : rs.getString("buildingid").trim());
                vo.setRoomid(null == rs.getString("roomid") ? "" : rs.getString("roomid").trim());
                vo.setQdwctime(null == rs.getString("qdwctime") ? "" : rs.getString("qdwctime").trim());
                vo.setQdtime(null == rs.getString("qdtime") ? "" : rs.getString("qdtime").trim());
                vo.setContractprice(rs.getString("contractprice").trim());
                vo.setMj(rs.getString("mj").trim());
                vo.setFkfs(rs.getString("fkfs").trim());
                vo.setHtbh(rs.getString("htbh").trim());
                vo.setJfrq(null == rs.getString("jfrq") ? "" : rs.getString("jfrq").trim());
                vo.setSfk(rs.getString("sfk").trim());
            }

            if (conn != null) {
                conn.close();
            }

        } catch (Exception e) {
            System.out.println("连接异常" + e.getMessage());
        }

        return vo;
    }

}

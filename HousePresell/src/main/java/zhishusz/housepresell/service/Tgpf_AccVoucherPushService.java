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

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tgpf_AccVoucherForm;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tgpf_AccVoucherDao;
import zhishusz.housepresell.database.dao.Tgpf_DepositDetailDao;
import zhishusz.housepresell.database.po.Emmp_CompanyInfo;
import zhishusz.housepresell.database.po.Empj_BuildingInfo;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_AccVoucher;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.util.ExtraBaseDao;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service单个推送：推送给财务系统-入账凭证
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_AccVoucherPushService
{
	@Autowired
	private Tgpf_AccVoucherDao tgpf_AccVoucherDao;
	@Autowired
	private Tgpf_DepositDetailDao tgpf_DepositDetailDao;		//资金归集明细
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	
	private ExtraBaseDao extraBaseDao = new ExtraBaseDao();
	
	MyDatetime mydate = MyDatetime.getInstance();
	
	MyDouble myDouble = MyDouble.getInstance();
	
	private static final String stableCode = "001012001";

	public Properties execute(Tgpf_AccVoucherForm model)
	{
		Properties properties = new MyProperties();	
		// 操作人
		Sm_User user = model.getUser();
		
		String username = user.getTheName();

		Long[] idArr = model.getIdArr();
		
		String currentTime = mydate.dateToSimpleString(System.currentTimeMillis());
		
		Map<String, String> returnMap = new HashMap<String,String>();
		
		String key = "";
		
		if( null == idArr || idArr.length < 0)
		{
			return MyBackInfo.fail(properties, "请选择需要进行日终对账的单据！");
		}

		for (int i = 0; i < idArr.length; i++)
		{
			Long tgpf_AccVoucherId = idArr[i];
			Tgpf_AccVoucher tgpf_AccVoucher = (Tgpf_AccVoucher) tgpf_AccVoucherDao.findById(tgpf_AccVoucherId);
			
			if(tgpf_AccVoucher.getTradeCount() > 0 && tgpf_AccVoucher.getDayEndBalancingState() == 2)
			{	
				if("".equals(key))
				{
					key = tgpf_AccVoucher.getTableId().toString();
				}
				else
				{
					key = key + "," + tgpf_AccVoucher.getTableId().toString();
				}			
			}
			else if( null != tgpf_AccVoucher.getDayEndBalancingState() && tgpf_AccVoucher.getDayEndBalancingState() != 2)
			{
				return MyBackInfo.fail(properties, "请先进行日终结算！");
			}
		}
		
		try
		{
			returnMap = tgpf_AccVoucherDao.postPZ(key, "1", username);
		}
		catch (SQLException e)
		{
			System.out.print(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String pi_sign = returnMap.get("pi_sign");

		String po_ret = returnMap.get("po_ret");
			
		if("1".equals(pi_sign))
		{
			return MyBackInfo.fail(properties, po_ret);
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	 
	/**
	 * 查询企业信息，如果不存在则新增，存在返回编码
	 * @param company 企业名称
	 * @param conn	连接
	 * @return
	 * @throws SQLException
	 */
	public String getCompanyCode(Emmp_CompanyInfo company, Connection conn) throws SQLException
	{
		// 初始化企业编码
		String gl_item_code = "";

		// 根据企业名称和标准码查询企业编码
		String sql = "select * from GL_ACC_ITEM where GL_ITEM_NAME='" + company.getTheName() + "' and CO_CODE='"
				+ stableCode + "'";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		if (rs.next())
		{
			gl_item_code = rs.getString("gl_item_code");
		}
		else
		{
			// 当前年
			String currentYear = String.valueOf(mydate.getCurrentYear());
			String maxSql = "select nvl(max(GL_ITEM_CODE),0) maxcode from gl_acc_item  where co_code='001012001' and fiscal='"
					+ currentYear + "'";// 搜索不到000开始(gl_item_code 为001)

			PreparedStatement ps2 = conn.prepareStatement(maxSql);
			ResultSet rs2 = ps2.executeQuery();
			if(rs2.next())
			{
				int code = rs2.getInt("maxcode");
				gl_item_code = String.format("%03d", code + 1);
			}
			// 新增财务库-企业信息
			String insertSql = " insert into gl_acc_item (CO_CODE, ACC_ITEM_CODE, GL_ITEM_CODE, FISCAL, GL_ITEM_NAME, ASS_CODE, IS_LOWEST, IS_USED, "
					+ " DOWN_STAT, ZS_RATE, COMMENTS, PARENT_ITEM_CODE, GL_ITEM_NAME1, IS_ADD_CHILD_ACCITEM, SOURCE_CODE, ACCOUNT, TIER, IS_INVOU) "
					+ " values ('" + stableCode + "', 'ACC_ITEM3', '" + gl_item_code + "', '" + currentYear + "', '"
					+ company.getTheName() + "', null, 'Y', 'Y', 1, 1.000000, " + " null, null, '"
					+ company.getTheName() + "', 'Y', null, '001', 1, 'Y') ";

			PreparedStatement ps3 = conn.prepareStatement(insertSql);
			ps3.executeUpdate();
		}
		return gl_item_code;
	}
	
	/**
	 * 先查询项目信息，如果不存在则插入记录
	 * 查询楼幢信息，如果不存在，则插入记录
	 * @param projectName 项目名称
	 * @param building
	 * @param conn
	 * @return
	 */
	public ArrayList<String> getbuildingMsg(String projectName ,Empj_BuildingInfo building,Connection conn) throws SQLException
	{
		//先查询项目是否存在不存在则插入楼幢信息
		//查询楼幢不存在才插入项目信息
		ArrayList<String> bascodes = new ArrayList<String>();				
		String basitem_code = "";
		String basITEM_ISN = "";
		String builditem_code = "";
		String buildITEM_ISN = "";		
		// 当前年
		String currentYear = String.valueOf(mydate.getCurrentYear());	

		// 楼幢施工编号
		String eCodeFromConstruction = building.geteCodeFromConstruction();
		// 根据项目名称和标准码查询企业编码
		String projectSql = "select * from GL_ITEM where ITEM_NAME1='"+ projectName +"' and CO_CODE='"+stableCode+"'";
		PreparedStatement ps = conn.prepareStatement(projectSql);
		ResultSet rs = ps.executeQuery();
		if (rs.next())
		{
			basitem_code = rs.getString("Item_code");
			basITEM_ISN = rs.getString("Item_isn");
		}
		else
		{
			String maxSql = "select nvl(max(substr(ITEM_CODE,0,4)),0)+1 maxcode from GL_ITEM  where co_code='001012001' and fiscal='"
					+ currentYear + "'";// 搜索不到000开始(gl_item_code 为001)
			PreparedStatement ps2 = conn.prepareStatement(maxSql);
			ResultSet rs2 = ps2.executeQuery();

			if (rs2.next())
			{
				int code = rs2.getInt("maxcode");
				basitem_code = String.format("%04d", code);
			}

			String maxIsnSql = "select nvl(max(substr(ITEM_ISN,0,5)),0)+1 isnCode from GL_ITEM  where co_code='001012001' and fiscal='"
					+ currentYear + "'";// 搜索不到000开始(gl_item_code 为001)
			PreparedStatement ps3 = conn.prepareStatement(maxIsnSql);
			ResultSet rs3 = ps3.executeQuery();
			if (rs3.next())
			{
				int isnCode = rs3.getInt("isnCode");
				basITEM_ISN = String.format("%05d", isnCode);
			}

			// 新增财务库-项目信息
			String insertProjectSql = "insert into GL_ITEM (CO_CODE, ITEM_CODE, ITEM_NAME, ITEM_NAME1, ASS_CODE, SOURCE_CODE, ITEM_ISN, FISCAL, "
					+ " TIER, IS_USED, IS_LOWEST, IS_INVOU, PAR_ITEM_ISN, PAR_ITEM_CODE, ITEM1_TYPE, IS_SYSTEM, ACCOUNT, ITEM_TYPE, IS_ADD_CHILD_ITEM) "
					+ " values ('" + stableCode + "', '" + basitem_code + "', '" + projectName + "', '" + projectName
					+ "', null, null, " + "'" + basITEM_ISN + "', '" + currentYear
					+ "', 1, 'Y', 'N', 'Y', null, null, null, 'N', '001', '1', 'Y')";

			PreparedStatement ps4 = conn.prepareStatement(insertProjectSql);
			ps4.executeUpdate();

		}
		// 查询楼幢信息
		String buildingSql = "select * from GL_ITEM where ITEM_NAME1='"+ projectName +"_"+ eCodeFromConstruction +"' and CO_CODE='"+stableCode+"'";
		PreparedStatement ps5 = conn.prepareStatement(buildingSql);
		ResultSet rs5 = ps5.executeQuery();
		if (rs5.next())
		{
			builditem_code = rs5.getString("Item_code");
			buildITEM_ISN = rs5.getString("Item_isn");
		}else
		{
			String maxSql = "select nvl(max(substr(ITEM_CODE,5,4)),0)+1 maxcode from GL_ITEM where ITEM_NAME1 like '"+ projectName +"_%' and co_code='001012001'";// 搜索不到000开始(gl_item_code 为001)
			PreparedStatement ps6 = conn.prepareStatement(maxSql);
			ResultSet rs6 = ps6.executeQuery();

			if (rs6.next())
			{
				int code = rs6.getInt("maxcode");
				builditem_code = basitem_code + String.format("%04d", code);
			}

			String maxIsnSql = "select nvl(max(substr(ITEM_ISN,6,4)),0)+1 isnCode from GL_ITEM  where ITEM_NAME1 like '"+ projectName +"_%' and co_code='001012001'";// 搜索不到000开始(gl_item_code 为001)
			PreparedStatement ps7 = conn.prepareStatement(maxIsnSql);
			ResultSet rs7 = ps7.executeQuery();
			if (rs7.next())
			{
				int isnCode = rs7.getInt("isnCode");
				buildITEM_ISN = basITEM_ISN + String.format("%04d", isnCode);
			}

			// 新增财务库-项目信息
			String insertProjectSql = "insert into GL_ITEM (CO_CODE, ITEM_CODE, ITEM_NAME, ITEM_NAME1, ASS_CODE, SOURCE_CODE, ITEM_ISN, FISCAL,  "
					+ " TIER, IS_USED, IS_LOWEST, IS_INVOU, PAR_ITEM_ISN, PAR_ITEM_CODE, ITEM1_TYPE, IS_SYSTEM, ACCOUNT, ITEM_TYPE, IS_ADD_CHILD_ITEM) "
					+ " values ('"+stableCode+"', '"+builditem_code+"', '"+eCodeFromConstruction+"', '"+projectName+"_"+eCodeFromConstruction+"', null, null, "
					+ "'"+buildITEM_ISN+"', '"+currentYear+"', 2, 'Y', 'Y', 'Y', '"+basITEM_ISN+"', '"+basitem_code+"', null, 'N', '001', '1', 'Y')";

			PreparedStatement ps8 = conn.prepareStatement(insertProjectSql);
			ps8.executeUpdate();
		}
			
		bascodes.add(basitem_code);
		bascodes.add(basITEM_ISN);
		bascodes.add(builditem_code);
		bascodes.add(buildITEM_ISN);
		
		return bascodes;
	}


	/**
	 * 插入入账凭证目录
	 * @param arrivaldate 	入账日期
	 * @param loanAmount  	入账金额
	 * @param userName	 	操作人
	 * @param bankName		贷款行名称
	 * @param conn			数据库连接
	 * @throws SQLException
	 */
	public void insertPzml(String arrivaldate,String loanAmount,String userName,String bankName , Connection conn) throws SQLException
	{
		// 初始化凭证目录编号
		String vou_no = "";
		// 当前日期
		String currentTime = mydate.dateToSimpleString(System.currentTimeMillis());		
		// 根据项目名称和标准码查询企业编码
		String projectSql = "select nvl(max(substr(VOU_NO,7,4)),0)+1 maxcode from gl_vou_head where VOU_NO like 'JZ-"
				+ arrivaldate.substring(5, 7) + "-%' and co_code='001012001' and fiscal='" + arrivaldate.substring(0, 4)
				+ "'";// 查不到为0000开始+1
		PreparedStatement ps = conn.prepareStatement(projectSql);
		ResultSet rs = ps.executeQuery();

		if (rs.next())
		{
			int code = rs.getInt("maxcode");
			vou_no = String.format("%04d", code); // 生成新的凭证目录
		}
		
		// 新增财务库-项目信息
		String insertProjectSql = "insert into gl_vou_head (VOU_NO, CO_CODE, ACCOUNT_ID, FISCAL, FIS_PERD, VOU_TYPE_CODE, VOU_DESC, VOU_DATE, AMT_CR, ACCE_CNT, AMT_DR, INPUTOR,  "
				+ " AUDITOR, POSTER, CDATE, ADATE, POST_DATE, STATUS, IS_REVERSE, REVER_MON, REVER_YEAR, VOU_SOURCE, COMPO_SOURCE, FSR, FSDATE, PRINT_STATUS, IS_PREPOSTED, CHECKER,  "
				+ " CHECKER_DATE, ERR_MESSAGE, ERR_FLAG, ERROR) values ('JZ-"+ arrivaldate.substring(5, 7) +"-"+vou_no+"', '"+stableCode+"', '001', "
				+ "'"+ arrivaldate.substring(0,4)+"', '"+ arrivaldate.substring(5, 7)+"', 'JZ', "
				+ "'"+arrivaldate+"-"+bankName+"-托管资金"+"', to_date('"+arrivaldate+"', 'yyyy-mm-dd'), "
				+ "'"+ loanAmount +"' , '0', '"+ loanAmount +"', '"+userName+"', null,null, to_date('"+currentTime+"', 'yyyy-mm-dd'),  "
				+ "null, null, 'Open', 'N', 0, 0, 'Import', null, null, null, '1', 'N', null, null, null, 'N', null) ";

		PreparedStatement ps2 = conn.prepareStatement(insertProjectSql);
		ps2.executeUpdate();
	}
	
	/**
	 * 
	 * @param VOU_NO 		凭证目录编号
	 * @param arrivaldate	入账日期
	 * @param conn			数据库连接
	 * @return
	 */
	public int getVou_Seq(String VOU_NO,String arrivaldate,Connection conn) throws SQLException
	{
		int vou_Seq = 1;
		// 查询凭证是否存在记录
		String vou_SeqSql = "select max(VOU_SEQ) maxcode from gl_vou_detail where  co_code='001012001' and "
				+ "VOU_NO='JZ-"+arrivaldate.substring(5, 7)+"-"+ VOU_NO +"' and fiscal='"+arrivaldate.substring(0, 4)+"' ";
		PreparedStatement ps = conn.prepareStatement(vou_SeqSql);
		ResultSet rs = ps.executeQuery();

		if (rs.next())
		{
			vou_Seq = rs.getInt("maxcode") + 1;
			
			String fix_SegsSql = "select FIX_SEGS from AS_NO  "
					+ "where FIX_SEGS like '001012001"+arrivaldate.substring(0, 4)+"001_APP_JZ-"+arrivaldate.substring(5, 7)+"-%' ";
			PreparedStatement ps2 = conn.prepareStatement(fix_SegsSql);
			ResultSet rs2 = ps2.executeQuery();

			if (rs2.next())
			{				
				// 新增财务库-项目信息
				String insertProjectSql = "update AS_NO set CUR_INDEX = '"+VOU_NO +"'  "
						+ " where COMPO_ID='GL_VOU_HEADVOU_NO' and FIX_SEGS='001012001"+arrivaldate.substring(0, 4)+"001_APP_JZ-"+arrivaldate.substring(5, 7)+"-'";
				
				PreparedStatement ps3 = conn.prepareStatement(insertProjectSql);
				ps3.executeUpdate();				
			}
			else
			{
				// 新增财务库-项目信息
				String insertProjectSql = "insert into AS_NO (COMPO_ID,FIX_SEGS,CUR_INDEX)   "
						+ " values ('GL_VOU_HEADVOU_NO','001012001"+arrivaldate.substring(0, 4)+"001_APP_JZ-"+arrivaldate.substring(5, 7)+"-','1')  ";				
				PreparedStatement ps4 = conn.prepareStatement(insertProjectSql);
				ps4.executeUpdate();
			}
		}
		else
		{
			// 新增财务库-项目信息
			String insertProjectSql = "insert into AS_NO (COMPO_ID,FIX_SEGS,CUR_INDEX)   "
					+ " values ('GL_VOU_HEADVOU_NO','001012001"+arrivaldate.substring(0, 4)+"001_APP_JZ-"+arrivaldate.substring(5, 7)+"-','1')  ";			
			PreparedStatement ps5 = conn.prepareStatement(insertProjectSql);
			ps5.executeUpdate();							
		}
		return vou_Seq;
	}
		
	/**
	 * 
	 * @param arr 
	 * 	<p>
	 *		<ul>
	 *			<li>String[0] 入账日期</li>
	 *          <li>String[1] 项目名称</li>
	 *          <li>String[2] 楼幢名称</li>
	 *          <li>String[3] 银行编号</li>
	 *          <li>String[4] 贷款金额</li>
	 *		</ul>
	 *	</p>
	 * @param conn
	 * @throws SQLException
	 */
	public void insertPzInfo(String[] arr, Connection conn) throws SQLException
	{
		String arrivaldate = arr[0];
		String projectName = arr[1];
		String buildingName = arr[2];
		String subjCode = arr[3];
		String loanAmount = arr[4];
		String companyName = arr[5];
				
		String vou_SeqSql = "select max(substr(VOU_NO,7,4)) maxcode from gl_vou_head where VOU_NO like 'JZ-"+arrivaldate.substring(5, 7)+"-%' "
				+ "and co_code='001012001' and fiscal='"+arrivaldate.substring(0, 4)+"' ";
		PreparedStatement ps = conn.prepareStatement(vou_SeqSql);
		ResultSet rs = ps.executeQuery();

		if (rs.next())
		{
			int maxCode = rs.getInt("maxcode");
			String vou_No = String.format("%04d", maxCode);// 获取最后生成的凭证目录目录

			int vou_Seq = getVou_Seq(vou_No, arrivaldate, conn);

			// 新增财务库-项目信息
			String insertProjectSql = "insert into gl_vou_detail (VOU_NO, CO_CODE, ACCOUNT_ID, FISCAL, VOU_SEQ, REF, DESCPT, ACC_CODE, DR_CR, STAD_AMT,  "
					+ "FIS_PERD, VOU_DESC, STATUS, IS_PREPOSTED, OPPO_ACC_CODE, VOU_DATE, RELATION_FLAG)  "
					+ "values ('JZ-"+arrivaldate.substring(5, 7)+"-"+vou_No+"', '"+ stableCode +"', '001', '"+arrivaldate.substring(0,4)+"',  "
					+ "'"+vou_Seq+"', null, '"+arrivaldate+"-"+projectName+"-"+buildingName+"-"+ companyName +"-托管资金"+"', '"+subjCode+"', 1,  "
					+ "'"+loanAmount+"', '"+arrivaldate.substring(5, 7)+"',  "
					+ "'"+arrivaldate+"-"+projectName+"-"+buildingName+"-"+ companyName +"-托管资金"+"', 'Open', 'N', null,  "
					+ "to_date('"+arrivaldate+"', 'yyyy-mm-dd'), null)";
			PreparedStatement ps2 = conn.prepareStatement(insertProjectSql);
			ps2.executeUpdate();
		}
	}
	
	/**
	 * 插入凭证内容和凭证辅助核算
	 * @param arr
	 * 	<p>
	 *		<ul>
	 *			<li>String[0] 入账日期</li>
	 *          <li>String[1] 项目名称</li>
	 *          <li>String[2] 楼幢名称</li>
	 *          <li>String[3] 贷款金额</li>
	 *          <li>String[4] 银行编号</li>
	 *		</ul>
	 *	</p>
	 * @param bascodes
	 * @param conn
	 * @throws SQLException
	 */
	public void insertPzInfo2(String[] arr,ArrayList<String> bascodes ,Connection conn) throws SQLException
	{
		//贷方:入账日期-项目-楼幢-银行-企业-托管资金2122
		String builditem_code=bascodes.get(2);
		String buildITEM_ISN=bascodes.get(3);
		
		String arrivaldate = arr[0];
		String projectName = arr[1];
		String buildingName = arr[2];
		String loanAmount = arr[3];
		String companyCode = arr[4];
		String bankBranchName = arr[5];
		String companyName = arr[6];
		
		String vou_SeqSql = "select max(substr(VOU_NO,7,4)) maxcode from gl_vou_head where VOU_NO like 'JZ-"+arrivaldate.substring(5, 7)+"-%' "
				+ "and co_code='001012001' and fiscal='"+arrivaldate.substring(0, 4)+"' ";
		PreparedStatement ps = conn.prepareStatement(vou_SeqSql);
		ResultSet rs = ps.executeQuery();

		if (rs.next())
		{
			int maxCode = rs.getInt("maxcode");
			String vou_No = String.format("%04d", maxCode);// 获取最后生成的凭证目录目录

			int vou_Seq = getVou_Seq(vou_No, arrivaldate, conn);

			// 新增财务库-项目信息
			String insertPzInfoSql = "insert into gl_vou_detail (VOU_NO, CO_CODE, ACCOUNT_ID, FISCAL, VOU_SEQ, REF, DESCPT, ACC_CODE, DR_CR, STAD_AMT,  "
					+ "FIS_PERD, VOU_DESC, STATUS, IS_PREPOSTED, OPPO_ACC_CODE, VOU_DATE, RELATION_FLAG)  "
					+ "values ('JZ-"+arrivaldate.substring(5, 7)+"-"+vou_No+"', '"+ stableCode +"', '001', '"+arrivaldate.substring(0,4)+"',  "
					+ "'"+vou_Seq+"', null, '"+arrivaldate+"-"+projectName+"-"+buildingName+"-"+ bankBranchName +"-"+ companyName+"-托管资金"+"', '2122', -1,  "
					+ "'"+loanAmount+"', '"+arrivaldate.substring(5, 7)+"',  "
					+ "'"+arrivaldate+"-"+projectName+"-"+buildingName+"-"+ bankBranchName +"-"+ companyName+"-托管资金"+"', 'Open', 'N', null,  "
					+ "to_date('"+arrivaldate+"', 'yyyy-mm-dd'), null)";
			PreparedStatement ps2 = conn.prepareStatement(insertPzInfoSql);
			ps2.executeUpdate();
			
			// 新增财务库-项目信息
			String insertPzHsInfoSql = "insert into gl_vou_detail_ass (VOU_NO, CO_CODE, ACCOUNT_ID, FISCAL, VOU_SEQ, VOU_DETAIL_SEQ, B_ACC_CODE, DR_CR, CUR_CODE, EX_RATE, CURR_AMT,  "
					+ "STAD_AMT, QTY, PRICE, ACC_ITEM1, ACC_ITEM2, ACC_ITEM3, ACC_ITEM4, ACC_ITEM5, ACC_ITEM6, ACC_ITEM7, ACC_ITEM8, B_CO_CODE, BUSS_DATE, BOD_NO,  "
					+ "ACC_ITEM10, ACC_ITEM9, ACC_ITEM11, ACC_ITEM12, ACC_ITEM13, ACC_ITEM14, ACC_ITEM15, ACC_ITEM16, ITEM_CODE, OUTLAY_CODE, ITEM_ISN, FIS_PERD,   "
					+ "ACC_CODE, RELATION_FLAG, DETAIL_RELATION_FLAG) values ('JZ-"+arrivaldate.substring(5, 7)+"-"+vou_No+"', '"+stableCode+"', '001', '"+arrivaldate.substring(0,4)+"',  "
					+ "'"+ vou_Seq +"', 1, '*', -1, 'RMB', 1.0000, null, '"+loanAmount+"', null, null, '*', '*', '"+companyCode+"', '*', '*', '*', '*', '*', '*',  "
					+ "null, null, '*', '*', '*', '*', '*', '*', '*', '*', '"+builditem_code+"', '*', '"+buildITEM_ISN+"', '"+arrivaldate.substring(5, 7)+"', 2122, null, null)";
			PreparedStatement ps3 = conn.prepareStatement(insertPzHsInfoSql);
			ps3.executeUpdate();
		}
	}
	
	/**
	 * 
	 * @param theName	名称
	 * @param parametertype	参数类型
	 * @return
	 */
	public String getParameter(String theName,String parametertype)
	{		
		String retParam = "";
		
		Sm_BaseParameterForm sm_BaseParameterForm = new Sm_BaseParameterForm();
		sm_BaseParameterForm.setTheState(0);
		sm_BaseParameterForm.setTheName(theName);
		sm_BaseParameterForm.setParametertype(parametertype);
		
		Integer totalCount = sm_BaseParameterDao.findByPage_Size(sm_BaseParameterDao.getQuery_Size(sm_BaseParameterDao.getBasicHQL(), sm_BaseParameterForm));
	
		List<Sm_BaseParameter> sm_BaseParameterList;
		if(totalCount > 0)
		{
			sm_BaseParameterList = sm_BaseParameterDao.findByPage(sm_BaseParameterDao.getQuery(sm_BaseParameterDao.getBasicHQL(), sm_BaseParameterForm));
			retParam = sm_BaseParameterList.get(0).getTheValue();
		}
		
		return retParam;
	}

	
}

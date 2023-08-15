package zhishusz.housepresell.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Tgpf_CyberBankStatementDtlForm;
import zhishusz.housepresell.database.dao.Emmp_BankInfoDao;
import zhishusz.housepresell.database.dao.Sm_UserDao;
import zhishusz.housepresell.database.dao.Tgpf_CyberBankStatementDtlDao;
import zhishusz.housepresell.database.po.Emmp_BankInfo;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpf_CyberBankStatementDtl;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import zhishusz.housepresell.util.excel.ExcelUtil2;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import com.xiaominfo.oss.sdk.OSSClientProperty;

import cn.hutool.json.JSONUtil;

/*
 * Service添加操作：网银对账-后台上传的账单原始Excel数据-明细表
 * Company：ZhiShuSZ
 */
@Service
@Transactional
public class Tgpf_CyberBankStatementDtlAddService
{
	@Autowired
	private Tgpf_CyberBankStatementDtlDao tgpf_CyberBankStatementDtlDao;
	@Autowired
	private Emmp_BankInfoDao emmp_BankInfoDao;
	@Autowired
	private OSSClientProperty oss;

	private final String downloadFilePath = "abc.xls";

	private final String downloadFilePath2 = "abc.xlsx";

	private final String downloadFilePath3 = "abc.csv";

	@Autowired
	private Sm_UserDao sm_UserDao;
	// 记账日期
	private String billTimeStamp = "";

	private List<Tgpf_CyberBankStatementDtl> tgpf_CyberBankStatementDtlList = null;

	@SuppressWarnings("rawtypes")
	public Properties execute(Tgpf_CyberBankStatementDtlForm model)
	{
		Properties properties = new MyProperties();

		Long userId = model.getUserId();

		String json = model.getSm_attachment();
		if (null == json)
		{
			return MyBackInfo.fail(properties, "'sm_attachment'不能为空");
		}

		Sm_User userStart = (Sm_User) sm_UserDao.findById(userId);

		Sm_Attachment sm_attachment = JSONUtil.toBean(json, Sm_Attachment.class);

		OssServerUtil ossUtil = new OssServerUtil();

		// 获取文件保存路径
		String path = this.getClass().getClassLoader().getResource("").getPath();

		if (path.contains("%20"))
		{
			path = path.replace("%20", " ");
		}

		// 获取附件地址
		String httpPath = sm_attachment.getTheLink();//http://61.132.107.213:19000/OssSave/bananaUpload/201811/07/a0b571e4f9644453ac27fba78a40e943.xls

//		boolean isInner = isInner(httpPath);//判断ip地址
//
//		if(!isInner){
//			Properties pro = null;
//			try
//			{
//				DirectoryUtil directoryUtil = new DirectoryUtil();
//				String localPath = directoryUtil.getProjectRoot()+"WEB-INF/classes/Oss-Server.properties";// 项目路径
//				if (localPath.contains("%20"))
//				{
//					localPath = localPath.replace("%20", " ");
//				}
//				//创建对象
//				pro = new Properties();
//				InputStream in = new BufferedInputStream(new FileInputStream(localPath));
//				pro.load(in);
//			}
//			catch (Exception e)
//			{
//				return MyBackInfo.fail(properties, "加载Oss-Server配置文件出现问题，请联系管理员");
//			}
//
//			String upload = pro.getProperty("remote");//获取下载地址
//
//			String spiltUrl = httpPath.substring(httpPath.indexOf("http://"), httpPath.lastIndexOf(":"));
//
//			httpPath = httpPath.replace(spiltUrl, upload);//替换下载路径
//		}

		if (null == httpPath || httpPath.length() == 0)
		{
			return MyBackInfo.fail(properties, "Sm_Attachment(TheLink：'" + httpPath + "')不能为空");
		}

		// 文件保存绝对地址
		String saveFilePath = "";

		if (httpPath.endsWith(".xls"))
		{
			saveFilePath = path + "\\" + this.downloadFilePath;
		}
		else if (httpPath.endsWith(".xlsx"))
		{
			saveFilePath = path + "\\" + this.downloadFilePath2;
		}
		else if (httpPath.endsWith(".csv"))
		{
			saveFilePath = path + "\\" + this.downloadFilePath3;
		}

		ossUtil.method(httpPath, saveFilePath);

		// 获取银行名称
		List read = ExcelUtil2.read(saveFilePath);

		Long bankId = sm_attachment.getBankId();
		if (null == bankId || bankId == 0)
		{
			return MyBackInfo.fail(properties, "没有查询到有效的数据，请核对是否选择银行！");
		}

		if(null == read || read.size() < 1){
			return MyBackInfo.fail(properties, "解析excel文件失败");
		}

		// 查询银行
		Emmp_BankInfo emmp_BankInfo = emmp_BankInfoDao.findById(bankId);
		// 获取银行编号
		String bankNo = emmp_BankInfo.getBankNo();
		// 获取记账日期
		billTimeStamp = sm_attachment.getBillTimeStamp();

		Integer flag = 0;
		// 判断是网银数据是哪家银行
		switch (bankNo)
		{
			case "04"://"江苏银行":

				flag = this.JSBankListToBean(read, userStart);
				break;

			case "05"://"工商银行":

				flag = this.GSBankListToBean(read, userStart);
				break;

			case "14"://"广发银行":

				flag = this.GFBankListToBean(read, userStart);
				break;

			case "01"://"建设银行":

				flag = this.JSHBankListToBean(read, userStart);
				break;

			case "23"://"江南银行":

				flag = this.JNBankListToBean(read, userStart);
				break;

			case "07"://"光大银行":

				flag = this.GDBankListToBean(read, userStart);
				break;

			case "13"://"华夏银行":

				flag = this.HXBankListToBean(read, userStart);
				break;

			case "24"://"江阴农村商业银行":

				flag = this.NSHBankListToBean(read, userStart);
				break;

			case "06"://"交通银行":

				flag = this.JTBankListToBean(read, userStart);
				break;

			case "16"://"南京银行":

				flag = this.NJBankListToBean(read, userStart);
				break;

			case "02"://"农业银行":

				flag = this.NYBankListToBean(read, userStart);
				break;

			case "28"://"平安银行":

				flag = this.PABankListToBean(read, userStart);
				break;

			case "19"://"浦发银行":

				flag = this.PFBankListToBean(read, userStart);
				break;

			case "17"://"上海银行":

				flag = this.SHHBankListToBean(read, userStart);
				break;

			case "20"://"兴业银行":

				flag = this.XYBankListToBean(read, userStart);
				break;

			case "12"://"邮政储蓄银行":

				flag = this.YCHBankListToBean(read, userStart);
				break;

			case "08"://"招商银行":

				flag = this.ZHSHBankListToBean(read, userStart);
				break;

			case "22"://"浙商银行":

				flag = this.ZHSHABankListToBean(read, userStart);
				break;

			case "03"://"中国银行":

				flag = this.ZHGBankListToBean(read, userStart);
				break;

			case "11"://"中信银行":

				flag = this.ZHXBankListToBean(read, userStart);
				break;

			case "10"://"苏州银行":

				flag = this.SZBankListToBean(read, userStart);
				break;

			case "09"://"民生银行":

				flag = this.MSHBankListToBean(read, userStart);
				break;

			case "25"://"无锡农商行":

				flag = this.WXNSHBankListToBean(read, userStart);
				break;

			case "27"://"江苏武进建信村镇银行":

				flag = this.JSWJJXBankListToBean(read, userStart);
				break;

			case "30"://"常州新北中成村镇银行常州营业部":

				flag = this.ZCCZBankListToBean(read, userStart);
				break;

			default:

				flag = 5;
				break;
		}
		System.out.println("flag="+flag);

		// 判断返回值
		if (flag == 1)
		{
			return MyBackInfo.fail(properties, "保存失败，记账日期：'" + billTimeStamp + "'和交易日期不符，请核验后重新上传！");
		}
		else if (flag == 2)
		{
			return MyBackInfo.fail(properties, "保存失败，文件格式有误，请核验后重新上传！");
		}
		else if (flag == 4)
		{
			return MyBackInfo.fail(properties, "保存失败，该文件数据已存在，请核验后重新上传！");
		}
		else if (flag == 0)
		{
			if (null == tgpf_CyberBankStatementDtlList || tgpf_CyberBankStatementDtlList.size() == 0)
			{
				return MyBackInfo.fail(properties, "没有数据保存失败，文件格式有误，请核验后重新上传！");
			}
		}
		else if (flag == 5)
		{
			return MyBackInfo.fail(properties, "银行编号不正确，请核对上传银行！");
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		properties.put("tgpf_CyberBankStatementDtlList", tgpf_CyberBankStatementDtlList);
		properties.put("uploadTimeStamp", MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));

		return properties;
	}

	/**
	 * 江苏银行-数据格式转换
	 *
	 * @param list
	 * @param userStart  登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer JSBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 3; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).contains("--"))
					{
						break;
					}
					else
					{
						// 判断是是否是贷款
						string = stringsList.get(5);
						if (!string.toString().contains("贷"))
						{
							continue;
						}
						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");


						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());
						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(2);
						if (!MyDatetime.getInstance().stringDateFormat(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat(tradeTimeStamp));
						// 对方账号
						tgpf_CyberBankStatementDtl.setRecipientAccount((String) stringsList.get(10));
						// 对方账号名称
						tgpf_CyberBankStatementDtl.setRecipientName((String) stringsList.get(11));
						// 备注摘要
						tgpf_CyberBankStatementDtl.setRemark((String) stringsList.get(9));
						// 收入金额
						String income = (String) stringsList.get(6);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(7);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 * 工商银行-数据格式转换
	 *
	 * @param list
	 * @param userStart  登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer GSBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 2; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
						// 判断是是否是贷款
//						string = stringsList.get(3);
						if (!string.toString().contains("贷"))
						{
							continue;
						}
						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						Sm_User sm_User = new Sm_User();
						sm_User.setTableId(123456L);

						tgpf_CyberBankStatementDtl.setUserStart(sm_User);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
//						String tradeTimeStamp = (String) stringsList.get(10);
						String tradeTimeStamp = (String) stringsList.get(3);
						if (!tradeTimeStamp.trim().equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl.setTradeTimeStamp(tradeTimeStamp.trim());
						// 对方账号
//						tgpf_CyberBankStatementDtl.setRecipientAccount((String) stringsList.get(1));
						tgpf_CyberBankStatementDtl.setRecipientAccount(null);
						// 对方账号名称
//						tgpf_CyberBankStatementDtl.setRecipientName((String) stringsList.get(4));
						tgpf_CyberBankStatementDtl.setRecipientName((String) stringsList.get(6));
						// 备注摘要
//						tgpf_CyberBankStatementDtl.setRemark((String) stringsList.get(7));
						tgpf_CyberBankStatementDtl.setRemark((String) stringsList.get(1));
						// 收入金额
//						String income = (String) stringsList.get(11);
						String income = (String) stringsList.get(2);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						tgpf_CyberBankStatementDtl.setBalance(null);
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(sm_User.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 * 广发银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer GFBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 8; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");


						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(1);

						if (!MyDatetime.getInstance().stringDateFormat2(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat2(tradeTimeStamp));
						// 对方账号
						tgpf_CyberBankStatementDtl.setRecipientAccount((String) stringsList.get(5));
						// 对方账号名称
						tgpf_CyberBankStatementDtl.setRecipientName((String) stringsList.get(6));
						// 备注摘要
						tgpf_CyberBankStatementDtl.setRemark((String) stringsList.get(10));
						// 收入金额
						String income = (String) stringsList.get(2);
						if(null == income || income.trim().isEmpty() || "-".equals(income) || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(4);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 * 建设银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer JSHBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 1; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(8);

						if (!MyDatetime.getInstance().stringDateFormat(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat(tradeTimeStamp));
						// 对方账号
						String recipientAccount = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						tgpf_CyberBankStatementDtl.setRemark((String) stringsList.get(10));
						// 收入金额
						String income = (String) stringsList.get(2);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(3);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 * 江南银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer JNBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 5; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(1);

						if (!MyDatetime.getInstance().stringDateFormat2(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat2(tradeTimeStamp));
						// 对方账号
						String recipientAccount = (String) stringsList.get(7);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(8);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = (String) stringsList.get(6);
						/**
						 * xsz by time 2019-5-24 15:15:52
						 * bug#2478 溧阳 托管 江南银行网银上传 摘要 非过滤字段 增加‘POS商户入账’（该字段不过滤）
						 */
						if(null == remark || remark.trim().isEmpty() || !(remark.trim().equals("转账") || !(remark.trim().equals("POS商户入账")))){
							continue;
						}
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(3);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 * 光大银行-数据格式转换
	 *
	 * @param list
	 * @param userStart  登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer GDBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 16; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!tradeTimeStamp.trim().equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						/*
						 * 解析获取摘要中的数据
						 * 例：放款--借据号(76641716000061001)贷款账号(76640163000020654)
						 * 借款人名称(蒋学志)
						 */
						String remark = (String) stringsList.get(9);
						String recipientAccount = "";
						String recipientName = "";
						if (null != remark && !remark.trim().isEmpty())
						{
							String[] strings = remark.split("\\)");
							for (int j = 0; j < strings.length; j++)
							{
								if (strings[j].trim().contains("贷款账号"))
								{
									recipientAccount = strings[j].substring(strings[j].indexOf("(") + 1);
								}

								if (strings[j].trim().contains("借款人名称"))
								{
									recipientName = strings[j].substring(strings[j].indexOf("(") + 1);
								}
							}
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(tradeTimeStamp);
						// 对方账号
						recipientAccount = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						recipientName = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(3);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 * 光大银行-数据格式转换
	 *
	 * @param list
	 * @param userStart  登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer GDBankListToBean1(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 15; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!tradeTimeStamp.trim().equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						/*
						 * 解析获取摘要中的数据
						 * 例：放款--借据号(76641716000061001)贷款账号(76640163000020654)
						 * 借款人名称(蒋学志)
						 */
						String remark = (String) stringsList.get(9);
						String recipientAccount = "";
						String recipientName = "";
						if (null != remark && !remark.trim().isEmpty())
						{
							String[] strings = remark.split("\\)");
							for (int j = 0; j < strings.length; j++)
							{
								if (strings[j].trim().contains("贷款账号"))
								{
									recipientAccount = strings[j].substring(strings[j].indexOf("(") + 1);
								}

								if (strings[j].trim().contains("借款人名称"))
								{
									recipientName = strings[j].substring(strings[j].indexOf("(") + 1);
								}
							}
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(tradeTimeStamp);
						// 对方账号
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(3);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 * 华夏银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer HXBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 1; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!tradeTimeStamp.trim().equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(tradeTimeStamp);
						// 对方账号
						String recipientAccount = (String) stringsList.get(7);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(8);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						tgpf_CyberBankStatementDtl.setRemark((String) stringsList.get(6));
						// 收入金额
						String income = (String) stringsList.get(3);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	农村商业银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer NSHBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 3; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!tradeTimeStamp.trim().equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(tradeTimeStamp.trim());
						// 对方账号
						String recipientAccount = (String) stringsList.get(9);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(8);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = stringsList.get(7).toString().trim();
						if(null == remark || remark.isEmpty() || (!remark.contains("转账")&&!remark.contains("转帐"))){
							continue;
						}
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(5);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 * 交通银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer JTBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 2; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).contains("--"))
					{
						break;
					}
					else
					{
						// 判断是是否是贷款
						string = stringsList.get(10);
						if (!string.toString().contains("贷"))
						{
							continue;
						}
						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());
						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);
						if (!MyDatetime.getInstance().stringDateFormat2(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat2(tradeTimeStamp));
						// 对方账号
						tgpf_CyberBankStatementDtl.setRecipientAccount((String) stringsList.get(8));
						// 对方账号名称
						tgpf_CyberBankStatementDtl.setRecipientName((String) stringsList.get(9));
						// 备注摘要
						tgpf_CyberBankStatementDtl.setRemark((String) stringsList.get(1));
						// 收入金额
						String income = (String) stringsList.get(5);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(7);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	南京银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer NJBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 3; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						String stringDateFormat = MyDatetime.getInstance().stringDateFormat(tradeTimeStamp.trim());

						if (!stringDateFormat.equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(stringDateFormat);
						// 对方账号
						String recipientAccount = (String) stringsList.get(8);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount.trim());
						// 对方账号名称
						String recipientName = (String) stringsList.get(7);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName.trim());
						// 备注摘要
						String remark = (String) stringsList.get(1);
						if(null == remark || remark.trim().isEmpty() || !(remark.trim().equals("贷款发放"))){
							continue;
						}
						tgpf_CyberBankStatementDtl.setRemark(remark.trim());
						// 收入金额
						String income = (String) stringsList.get(3);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(4);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	农业银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer NYBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 2; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().equals("汇总收入金额"))
					{
						break;
					}
					else
					{

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!MyDatetime.getInstance().stringDateFormat3(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat3(tradeTimeStamp));
						// 对方账号
						String recipientAccount = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(7);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						tgpf_CyberBankStatementDtl.setRemark((String) stringsList.get(8));
						// 收入金额
						String income = (String) stringsList.get(1);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(3);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	平安银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer PABankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 1; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!MyDatetime.getInstance().stringDateFormat(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat(tradeTimeStamp));
						// 对方账号
						String recipientAccount = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = (String) stringsList.get(9);
						if(null == remark || remark.trim().isEmpty() || !(remark.trim().equals("委托转账"))){
							continue;
						}
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(3);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(4);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	浦发银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer PFBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			//获取账户
			//String recipientAccount = ((List)list.get(0)).get(1).toString();
			//获取账号名称
			//String recipientName = ((List)list.get(1)).get(1).toString();
			for (int i = 4; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!MyDatetime.getInstance().stringDateFormat(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat(tradeTimeStamp));
						// 对方账号
//						String recipientAccount = (String) stringsList.get(8);
						String recipientAccount = (String) stringsList.get(7);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
//						String recipientName = (String) stringsList.get(9);
						String recipientName = (String) stringsList.get(8);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
//						tgpf_CyberBankStatementDtl.setRemark((String) stringsList.get(10));
						tgpf_CyberBankStatementDtl.setRemark((String) stringsList.get(9));
						// 收入金额
//						String income = (String) stringsList.get(6);
						String income = (String) stringsList.get(5);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
//						String balance = (String) stringsList.get(7);
						String balance = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	上海银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer SHHBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 7; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
						// 判断是是否是贷款
						string = stringsList.get(3);
						if (!string.toString().contains("贷"))
						{
							continue;
						}

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(1);

						if (!tradeTimeStamp.trim().equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(tradeTimeStamp.trim());
						// 对方账号
						String recipientAccount = (String) stringsList.get(8);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(9);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = stringsList.get(7).toString().trim();
						if(null == remark || remark.isEmpty() || !remark.contains("转帐")){
							continue;
						}
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(5);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	兴业银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer XYBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 1; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
						/*// 判断是是否是贷款
						string = stringsList.get(3);
						if (!string.toString().contains("贷"))
						{
							continue;
						}*/

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(14);

						if (!MyDatetime.getInstance().stringDateFormat2(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat2(tradeTimeStamp));
						// 对方账号
						String recipientAccount = (String) stringsList.get(10);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(11);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = (String) stringsList.get(9);
						if(null == remark || remark.trim().isEmpty() || !(remark.trim().equals("贷款发放"))){
							continue;
						}
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(7);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(8);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	邮政储蓄银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer YCHBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 5; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
                        /*// 判断是是否是贷款
                        string = stringsList.get(3);
                        if (!string.toString().contains("贷"))
                        {
                            continue;
                        }*/

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!tradeTimeStamp.trim().equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(tradeTimeStamp.trim());
						// 对方账号
						String recipientAccount = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = (String) stringsList.get(10);
                       /* if(null == remark || remark.trim().isEmpty() || !(remark.trim().equals("个人向对公转账"))){
                          continue;
                        }*/
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(3);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(4);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *  邮政储蓄银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer YCHBankListToBeanBak(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 4; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
                        /*// 判断是是否是贷款
                        string = stringsList.get(3);
                        if (!string.toString().contains("贷"))
                        {
                            continue;
                        }*/

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!tradeTimeStamp.trim().equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(tradeTimeStamp.trim());
						// 对方账号
						String recipientAccount = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(4);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = (String) stringsList.get(9);
						if(null == remark || remark.trim().isEmpty() || !(remark.trim().equals("个人向对公转账"))){
							continue;
						}
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(3);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	招商银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer ZHSHBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 8; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
						// 判断是是否是贷款
						string = stringsList.get(3);
						if (null == string || !(string.toString().trim().equals("个贷放款")))
						{
							continue;
						}

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!MyDatetime.getInstance().stringDateFormat(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat(tradeTimeStamp));
						// 对方账号
						String recipientAccount = (String) stringsList.get(17);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(16);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = (String) stringsList.get(7);
						if(null == remark || remark.trim().isEmpty()){
							continue;
						}
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(5);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	浙商银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer ZHSHABankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 4; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
						/*// 判断是是否是贷款
						string = stringsList.get(3);
						if (!string.toString().contains("贷"))
						{
							continue;
						}*/

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if(MyDatetime.getInstance().stringDateFormat4(tradeTimeStamp).equals("false")){
							continue;
						}

						if (!MyDatetime.getInstance().stringDateFormat4(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat4(tradeTimeStamp));
						// 对方账号
						String recipientAccount = (String) stringsList.get(7);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount.trim());
						// 对方账号名称
						String recipientName = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName.trim());
						// 备注摘要
						String remark = (String) stringsList.get(1);
						if(null == remark || remark.trim().isEmpty() || !(remark.trim().equals("兑付"))){
							continue;
						}
						tgpf_CyberBankStatementDtl.setRemark(remark.trim());
						// 收入金额
						String income = (String) stringsList.get(4);
						if(income.contains(",")){
							income = income.replaceAll(",", "");
						}
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(5);
						if(balance.contains(",")){
							balance = balance.replaceAll(",", "");
						}
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	中国银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer ZHGBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 8; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
						// 判断是是否是贷款
						string = stringsList.get(1);
						if (null == string || string.toString().trim().isEmpty() || !string.toString().contains("贷款放款"))
						{
							continue;
						}

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(10);

						if (!MyDatetime.getInstance().stringDateFormat(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat(tradeTimeStamp));
						// 对方账号
						String recipientAccount = (String) stringsList.get(4);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						tgpf_CyberBankStatementDtl.setRemark((String) stringsList.get(23));
						// 收入金额
						String income = (String) stringsList.get(13);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(14);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	中信银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer ZHXBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 13; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
						/*// 判断是是否是贷款
						string = stringsList.get(1);
						if (!string.toString().contains("贷款放款"))
						{
							continue;
						}*/

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!tradeTimeStamp.trim().equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(tradeTimeStamp.trim());
						// 对方账号
						String recipientAccount = (String) stringsList.get(2);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(3);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = stringsList.get(7).toString().trim();
						/*if(null == remark || remark.isEmpty() || !remark.contains("转账")){
							continue;
						}*/
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(6);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(7);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	苏州银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer SZBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 7; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
						/*// 判断是是否是贷款
						string = stringsList.get(1);
						if (!string.toString().contains("贷款放款"))
						{
							continue;
						}*/

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!MyDatetime.getInstance().stringDateFormat2(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat2(tradeTimeStamp));
						// 对方账号
						String recipientAccount = (String) stringsList.get(4);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = (String) stringsList.get(7);
						if(null == remark || remark.trim().isEmpty() || !(remark.trim().equals("放贷"))){
							continue;
						}
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(1);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(3);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	民生银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer MSHBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 14; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
						/*// 判断是是否是贷款
						string = stringsList.get(1);
						if (!string.toString().contains("贷款放款"))
						{
							continue;
						}*/

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!tradeTimeStamp.trim().equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(tradeTimeStamp.trim());
						// 对方账号
						String recipientAccount = (String) stringsList.get(7);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(8);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(3);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(4);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	无锡农商行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer WXNSHBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 6; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
						/*// 判断是是否是贷款
						string = stringsList.get(1);
						if (!string.toString().contains("贷款放款"))
						{
							continue;
						}*/

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(0);

						if (!MyDatetime.getInstance().stringDateFormat4(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat4(tradeTimeStamp));
						// 对方账号
						String recipientAccount = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = (String) stringsList.get(4);
						if(null == remark || remark.trim().isEmpty() || !(remark.trim().equals("放贷"))){
							continue;
						}
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(2);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(3);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 *	江苏武进建信村镇银行-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer JSWJJXBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 1; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{
						/*// 判断是是否是贷款
						string = stringsList.get(1);
						if (!string.toString().contains("贷款放款"))
						{
							continue;
						}*/

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String tradeTimeStamp = (String) stringsList.get(1);

						if (!MyDatetime.getInstance().stringDateFormat(tradeTimeStamp).equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(MyDatetime.getInstance().stringDateFormat(tradeTimeStamp));
						// 对方账号
						//String recipientAccount = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setRecipientAccount("");
						// 对方账号名称
						//String recipientName = (String) stringsList.get(5);
						tgpf_CyberBankStatementDtl.setRecipientName("");
						// 备注摘要
						tgpf_CyberBankStatementDtl.setRemark((String) stringsList.get(3));
						// 收入金额
						String income = (String) stringsList.get(5);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						/*
						 * flag = isSaveCheck(tgpf_CyberBankStatementDtl);
						 *
						 * if (flag == 4)
						 * {
						 * return flag;
						 * }
						 */

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 * 常州新北中成村镇银行常州营业部-数据格式转换
	 *
	 * @param list
	 * @param userStart 登陆用户
	 */
	@SuppressWarnings("rawtypes")
	public Integer ZCCZBankListToBean(List list, Sm_User userStart)
	{
		// 判断记账状态
		Integer flag = 0;
		tgpf_CyberBankStatementDtlList = new ArrayList<Tgpf_CyberBankStatementDtl>();
		// 获取网银测试数据
		try
		{
			for (int i = 1; i < list.size(); i++)
			{
				List stringsList = (List) list.get(i);
				if (null != stringsList && stringsList.size() > 0)
				{
					// 判断是否是最后一行
					Object string = stringsList.get(0);
					if ((string.toString()).trim().isEmpty())
					{
						break;
					}
					else
					{

						Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl = new Tgpf_CyberBankStatementDtl();
						tgpf_CyberBankStatementDtl.setTheState(S_TheState.Normal);
						tgpf_CyberBankStatementDtl.setBusiState("0");
						tgpf_CyberBankStatementDtl.seteCode("");

						tgpf_CyberBankStatementDtl.setUserStart(userStart);

						// 创建时间
						tgpf_CyberBankStatementDtl.setCreateTimeStamp(System.currentTimeMillis());

						// 交易日期
						String format = (String) stringsList.get(1);

//						Object object2 = stringsList.get(1);
//
//
//						Date object = (Date) stringsList.get(1);
//
//						SimpleDateFormat formatDate2 = new SimpleDateFormat("yyyy-MM-dd");
//						String format = formatDate2.format(object);

						/*if (!MyDatetime.getInstance().stringDateFormat(tradeTimeStamp).equals(billTimeStamp))*/
						if (!format.equals(billTimeStamp))
						{
							flag = 1;
							return flag;
						}

						tgpf_CyberBankStatementDtl
								.setTradeTimeStamp(format);
						// 对方账号
						String recipientAccount = (String) stringsList.get(6);
						tgpf_CyberBankStatementDtl.setRecipientAccount(recipientAccount);
						// 对方账号名称
						String recipientName = (String) stringsList.get(7);
						tgpf_CyberBankStatementDtl.setRecipientName(recipientName);
						// 备注摘要
						String remark = (String) stringsList.get(2);
						if(null == remark || remark.trim().isEmpty() || !(remark.trim().equals("收入"))){
							continue;
						}
						tgpf_CyberBankStatementDtl.setRemark(remark);
						// 收入金额
						String income = (String) stringsList.get(3);
						if(null == income || income.trim().isEmpty() || MyDouble.getInstance().parse(income) <= 0){
							continue;
						}
						tgpf_CyberBankStatementDtl.setIncome(MyDouble.getInstance().parse(income));
						// 余额
						String balance = (String) stringsList.get(4);
						tgpf_CyberBankStatementDtl.setBalance(MyDouble.getInstance().parse(balance));
						// 文件上传日期
						tgpf_CyberBankStatementDtl.setUploadTimeStamp(
								MyDatetime.getInstance().dateToSimpleString(System.currentTimeMillis()));
						// 上传人
						tgpf_CyberBankStatementDtl.setUploadUser(userStart.getTheName());
						// 对账状态
						tgpf_CyberBankStatementDtl.setReconciliationState(0);

						tgpf_CyberBankStatementDtlDao.save(tgpf_CyberBankStatementDtl);

						tgpf_CyberBankStatementDtlList.add(tgpf_CyberBankStatementDtl);
					}
				}
			}
		}
		catch (Exception e)
		{
			flag = 2;
			return flag;
		}
		return flag;
	}

	/**
	 * 判断数据是否已经存在
	 *
	 * @param tgpf_CyberBankStatementDtl
	 * @return
	 */
	Integer isSaveCheck(Tgpf_CyberBankStatementDtl tgpf_CyberBankStatementDtl)
	{
		// 对方账号
		String recipientAccount = tgpf_CyberBankStatementDtl.getRecipientAccount();
		// 对方账号名称
		String recipientName = tgpf_CyberBankStatementDtl.getRecipientName();
		// 交易金额
		Double income = tgpf_CyberBankStatementDtl.getIncome();

		// 查询数据是否已经存在
		if (null != recipientAccount && recipientAccount.length() > 0 && null != recipientName
				&& recipientName.length() > 0)
		{
			Tgpf_CyberBankStatementDtlForm form = new Tgpf_CyberBankStatementDtlForm();
			form.setTheState(S_TheState.Normal);
			form.setRecipientAccount(recipientAccount);
			form.setRecipientName(recipientName);
			form.setIncome(income);
			form.setTradeTimeStamp(tgpf_CyberBankStatementDtl.getTradeTimeStamp());
			Integer count = tgpf_CyberBankStatementDtlDao.findByPage_Size(
					tgpf_CyberBankStatementDtlDao.getQuery_Size(tgpf_CyberBankStatementDtlDao.getBasicHQL2(), form));
			if (count > 0)
			{
				return 4;
			}
		}
		return 0;
	}

	/**
	 * 判断请求地址是否是内网ip
	 * @param ip
	 * @return
	 */
	boolean isInner(String ip)
	{
		String reg = "(10|172|192)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";
		Pattern p = Pattern.compile(reg);
		Matcher matcher = p.matcher(ip);
		return matcher.find();
	}
}

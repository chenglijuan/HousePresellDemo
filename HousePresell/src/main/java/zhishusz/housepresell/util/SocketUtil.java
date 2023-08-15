package zhishusz.housepresell.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import lombok.extern.slf4j.Slf4j;
import zhishusz.housepresell.database.po.state.S_SocketMsgLength;

@Slf4j
public class SocketUtil
{
	private static SocketUtil instance;

	private SocketUtil()
	{

	}

	public static SocketUtil getInstance()
	{
		if (instance == null)
			instance = new SocketUtil();

		return instance;
	}

	public static final String SPACE = " ";
	private static final String SEPARATE = "|";

	/**
	 * 将以逗号分隔的字符串转换成字符串数组
	 * 
	 * @author wuyu
	 * @since 2018年8月9日16:42:52
	 * @param valStr
	 * @return String[]
	 */
	public static String[] StrList(String valStr)
	{
		int i = 0;
		String TempStr = valStr;
		String[] returnStr = new String[valStr.length() + 1 - TempStr.replace(",", "").length()];
		valStr = valStr + ",";
		while (valStr.indexOf(',') > 0)
		{
			returnStr[i] = valStr.substring(0, valStr.indexOf(','));
			valStr = valStr.substring(valStr.indexOf(',') + 1, valStr.length());

			i++;
		}
		return returnStr;
	}

	/**
	 * 获取字符串编码
	 * 
	 * @author wuyu
	 * @since 2018年8月9日16:42:52
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str)
	{
		String encode = "GB2312";
		try
		{
			if (str.equals(new String(str.getBytes(encode), encode)))
			{
				String s = encode;
				return s;
			}
		}
		catch (Exception exception)
		{
		}
		encode = "ISO-8859-1";
		try
		{
			if (str.equals(new String(str.getBytes(encode), encode)))
			{
				String s1 = encode;
				return s1;
			}
		}
		catch (Exception exception1)
		{
		}
		encode = "UTF-8";
		try
		{
			if (str.equals(new String(str.getBytes(encode), encode)))
			{
				String s2 = encode;
				return s2;
			}
		}
		catch (Exception exception2)
		{
		}
		encode = "GBK";
		try
		{
			if (str.equals(new String(str.getBytes(encode), encode)))
			{
				String s3 = encode;
				return s3;
			}
		}
		catch (Exception exception3)
		{
		}
		return "";
	}

	/**
	 * 获取字符串真实长度
	 * <p>
	 * 针对中文
	 * </p>
	 * 
	 * @author wuyu
	 * @since 2018年8月9日16:42:52
	 * @param str
	 * @return
	 */
	public static int getRealLength(String str)
	{
		int len = 0;
		if (null == str)
		{
			return len;
		}
		for (int index = 0; index < str.length(); index++)
		{
			if (str.charAt(index) > '\377')
				len += 2;
			else
				len++;
		}
		return len;
	}

	/**
	 * 补足字符串 add by wuyu
	 * 
	 * @param src
	 *            字符串
	 * @param size
	 *            长度
	 * @param padStr
	 *            补足字符串
	 * @return
	 */
	public static String fillConCNCode(Object o, int size, String padStr)
	{
		String src = "";
		if (null != o)
		{
			// return null;
			if (o instanceof BigDecimal)
			{
				src = ((BigDecimal) o).toString();
			}
			else
			{
				src = (String) o;
			}
		}
		int cnRealLen = getRealLength(src);// 包含中文的实际src长度
		int cnLen = src.length(); // 字符串长度
		int realFillSize = size - (cnRealLen - cnLen); // 实际填充长度
		return StringUtils.rightPad(src, realFillSize, padStr);
	}

	/**
	 * 去null
	 * 
	 * @author wuyu
	 * @since 2018年8月9日16:42:52
	 * @param o
	 * @return
	 */
	public static String noNull(Object o)
	{
		return o == null ? "" : (String) o;
	}

	/**
	 * 生成md5
	 * 
	 * @author wuyu
	 * @since 2018年8月9日16:42:52
	 * @param str
	 * @return
	 */
	public static String md5(String str)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes("GBK"));
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++)
			{
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();

		}
		return str;
	}

	/**
	 * 处理拼包
	 * 
	 * @author wuyu
	 * @since 2018年8月9日16:42:52
	 * @param genPackageLen
	 *            总包长
	 * @param sb
	 *            业务部分
	 * @return 报文（含MD5）
	 */
	public static String dataToString(int genPackageLen, StringBuffer sb)
	{
		String len = StringUtils.leftPad(String.valueOf(genPackageLen), 4, "0") + "|"; // 0000|
		String md5 = fillConCNCode(md5(sb.toString()), 32, StringUtils.SPACE) + "|"; // MD5~32|
		
		System.out.println("接收到的md5 :"+md5);
		
		return sb.insert(0, len).append(md5).toString();
	}
	
	/**
	 * 检测包
	 * 
	 * @author wuyu
	 * @since 2018年8月9日16:42:52
	 * @param pack
	 *            数据包
	 * @param checkData
	 *            检测数据项
	 * 	<p>
	 *		<ul>
	 *			<li>String[0] 区位码</li>
	 *          <li>String[1] 交易码</li>
	 *          <li>String[2] md5</li>
	 *		</ul>
	 *	</p>
	 */
	public static String checkPackage(String pack, String[] checkData)
	{
		// 包长检测 数据包总包长与实际包长比较
		if (Integer.parseInt(checkData[0]) != getRealLength(pack.trim()))
		{
			return "0013";
		}
		// 包长检测 数据包总包长与规定包长比较
		if (Integer.parseInt(checkData[0]) != S_SocketMsgLength.getMsg(checkData[1]))
		{
			return "0013";
		}

		// MD5校验
		String data = StringUtils.substring(pack.trim(), 5, pack.trim().length() - 33);
//		if (!md5(data).equals(checkData[2]))
//		{
//			return "0011";
//		}
		
		return "true";
	}
	
	/**
	 * 解析报文，去除每一个域的空格
	 * @author wuyu
	 * @since 2018年8月14日17:12:50
	 * @param message 报文信息
	 * @return
	 */
	public static String[] msgAnalysis(String message)
	{
		String[] preRecMsgSplit = StringUtils.splitPreserveAllTokens(message, SEPARATE);

		String[] recMsgSplit = new String[preRecMsgSplit.length];

		for (int i = 0; i < preRecMsgSplit.length; i++)
		{
			recMsgSplit[i] = preRecMsgSplit[i].trim();
		}

		return recMsgSplit;
	}
	
	/**
	 * 返回的错误报文
	 * @param returnData 返回信息
	 * 	<p>
	 *		<ul>
	 *			<li>String[0] 区位码</li>
	 *          <li>String[1] 交易码</li>
	 *          <li>String[2] 返回码</li>
	 *          <li>String[3] 备注</li>
	 *		</ul>
	 *	</p>		
	 * @return 返回报文
	 */
	public static String getFailureMsg(String[] returnData)
	{

		StringBuffer sb = new StringBuffer();

		sb.append(StringUtils.leftPad(String.valueOf("69"), 4, "0"));
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[0], 2, " ")); // 区位码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[1], 4, " ")); // 交易码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[2], 4, " ")); // 返回码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[3], 50, " ")); // 备注
		sb.append(SEPARATE);

		return sb.toString();
	}
	
	/**
	 * 返回的正确报文，三方协议信息查询应答[1101]
	 * @param returnData 返回信息
	 * 	<p>
	 *		<ul>
	 *			<li>String[0] 区位码</li>
	 *          <li>String[1] 交易码</li>
	 *          <li>String[2] 返回码</li>
	 *          <li>String[3] 备注</li>
	 *          <li>String[4] 预购合同编号</li>
	 *          <li>String[5] 预售人名称</li>
	 *          <li>String[6] 三方协议编号</li>
	 *          <li>String[7] 交款序号</li>
	 *          <li>String[8] 协议应交款金额</li>
	 *          <li>String[9] 合同成交价格</li>
	 *          <li>String[10] 托管账户名称</li>
	 *          <li>String[11] 托管账号</li>
	 *          <li>String[12] 预购人名称</li>
	 *		</ul>
	 *	</p>		
	 * @return 返回报文
	 */
	public static String getSuccessMsg_1101(String[] returnData)
	{
		StringBuffer sb = new StringBuffer();

		sb.append(fillConCNCode(returnData[0], 2, " ")); // 区位码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[1], 4, " ")); // 交易码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[2], 4, " ")); // 返回码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[3], 50, " ")); // 备注
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[4], 30, " ")); // 预购合同编号
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[5], 100, " ")); // 预售人名称
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[6], 100, " ")); // 预购人名称
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[7], 16, " ")); // 三方协议编号
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[8], 2, " ")); // 交款序号
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[9], 20, " ")); // 已入账总金额
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[10], 20, " ")); // 合同成交价格
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[11], 100, " ")); // 托管账户名称
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[12], 30, " ")); // 托管账号
		sb.append(SEPARATE);


		String returnMsg = dataToString(S_SocketMsgLength.getMsg("1101"), sb);

		return returnMsg;
	}
	
	/**
	 * 返回的正确报文，缴款记账应答[1102]
	 * @param returnData 返回信息
	 * 	<p>
	 *		<ul>
	 *			<li>String[0] 区位码</li>
	 *          <li>String[1] 交易码</li>
	 *          <li>String[2] 返回码</li>
	 *          <li>String[3] 备注</li>
	 *          <li>String[4] 管理系统流水号</li>
	 *          <li>String[5] 银行端平台流水号</li>
	 *		</ul>
	 *	</p>		
	 * @return 返回报文
	 */
	public static String getSuccessMsg_1102(String[] returnData)
	{
		StringBuffer sb = new StringBuffer();

		sb.append(fillConCNCode(returnData[0], 2, " ")); // 区位码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[1], 4, " ")); // 交易码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[2], 4, " ")); // 返回码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[3], 50, " ")); // 备注
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[4], 12, " ")); // 管理系统流水号
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[5], 30, " ")); // 银行端平台流水号
		sb.append(SEPARATE);

		String returnMsg = dataToString(S_SocketMsgLength.getMsg("1102"), sb);

		return returnMsg;
	}
	
	/**
	 * 返回的正确报文，缴款冲正应答[1192]
	 * @param returnData 返回信息
	 * 	<p>
	 *		<ul>
	 *			<li>String[0] 区位码</li>
	 *          <li>String[1] 交易码</li>
	 *          <li>String[2] 返回码</li>
	 *          <li>String[3] 备注</li>
	 *          <li>String[4] 管理系统流水号</li>
	 *          <li>String[5] 银行端平台流水号</li>
	 *		</ul>
	 *	</p>		
	 * @return 返回报文
	 */
	public static String getSuccessMsg_1192(String[] returnData)
	{
		StringBuffer sb = new StringBuffer();

		sb.append(fillConCNCode(returnData[0], 2, " ")); // 区位码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[1], 4, " ")); // 交易码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[2], 4, " ")); // 返回码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[3], 50, " ")); // 备注
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[4], 12, " ")); // 管理系统流水号
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[5], 30, " ")); // 银行端平台流水号
		sb.append(SEPARATE);

		String returnMsg = dataToString(S_SocketMsgLength.getMsg("1192"), sb);

		return returnMsg;
	}
	
	/**
	 * 返回的正确报文，交易明细日终对账单上传应答[4101]
	 * @param returnData 返回信息
	 * 	<p>
	 *		<ul>
	 *			<li>String[0] 区位码</li>
	 *          <li>String[1] 交易码</li>
	 *          <li>String[2] 返回码</li>
	 *          <li>String[3] 备注</li>
	 *          <li>String[4] 文件名</li>
	 *		</ul>
	 *	</p>		
	 * @return 返回报文
	 */
	public static String getSuccessMsg_4101(String[] returnData)
	{
		StringBuffer sb = new StringBuffer();

		sb.append(fillConCNCode(returnData[0], 2, " ")); // 区位码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[1], 4, " ")); // 交易码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[2], 4, " ")); // 返回码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[3], 50, " ")); // 备注
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[4], 30, " ")); // 文件名
		sb.append(SEPARATE);

		String returnMsg = dataToString(S_SocketMsgLength.getMsg("4101"), sb);

		return returnMsg;
	}
	
	/**
	 * 返回的正确报文，预售许可证信息查询应答[6101]
	 * @param returnData 返回信息
	 * 	<p>
	 *		<ul>
	 *			<li>String[0] 区位码</li>
	 *          <li>String[1] 交易码</li>
	 *          <li>String[2] 返回码</li>
	 *          <li>String[3] 备注</li>
	 *          <li>String[4] 预售许可证状态</li>
	 *          <li>String[5] 托管状态</li>
	 *          <li>String[6] 预售许可证编号</li>
	 *          <li>String[7] 开发商名称</li>
	 *          <li>String[8] 项目名称</li>
	 *          <li>String[9] 项目地址</li>
	 *          <li>String[10] 预售范围</li>
	 *          <li>String[11] 发证时间</li>
	 *          <li>String[12] 预售总面积</li>
	 *          <li>String[13] 预售总户数</li>
	 *          <li>String[14] 备注一</li>
	 *          <li>String[15] 备注二</li>
	 *          <li>String[16] 备注三</li>
	 *          <li>String[17] 备注四</li>
	 *          <li>String[18] 查询时间</li>
	 *		</ul>
	 *	</p>		
	 * @return 返回报文
	 */
	public static String getSuccessMsg_6101(String[] returnData)
	{
		StringBuffer sb = new StringBuffer();

		sb.append(fillConCNCode(returnData[0], 2, " ")); // 区位码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[1], 4, " ")); // 交易码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[2], 4, " ")); // 返回码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[3], 50, " ")); // 备注
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[4], 2, " ")); // 预售许可证状态
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[5], 2, " ")); // 托管状态
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[6], 50, " ")); // 预售许可证编号
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[7], 60, " ")); // 开发商名称
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[8], 100, " ")); // 项目名称
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[9], 100, " ")); // 项目地址
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[10], 200, " ")); // 预售范围
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[11], 20, " ")); // 发证时间
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[12], 20, " ")); // 预售总面积
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[13], 5, " ")); // 预售总户数
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[14], 30, " ")); // 备注一
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[15], 30, " ")); // 备注二
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[16], 60, " ")); // 备注三
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[17], 100, " ")); // 备注四
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[18], 20, " ")); // 查询时间
		sb.append(SEPARATE);

		String returnMsg = dataToString(S_SocketMsgLength.getMsg("6101"), sb);

		return returnMsg;
	}
	
	/**
	 * 返回的正确报文，预售合同查询应答[6102]
	 * @param returnData 返回信息
	 * 	<p>
	 *		<ul>
	 *			<li>String[0] 区位码</li>
	 *          <li>String[1] 交易码</li>
	 *          <li>String[2] 返回码</li>
	 *          <li>String[3] 备注</li>
	 *          <li>String[4] 预售合同编号</li>
	 *          <li>String[5] 合同状态</li>
	 *          <li>String[6] 房屋坐落</li>
	 *          <li>String[7] 建筑面积</li>
	 *          <li>String[8] 合同金额</li>
	 *          <li>String[9] 贷款金额</li>
	 *          <li>String[10] 是否装修</li>
	 *          <li>String[11] 预售人名称</li>
	 *          <li>String[12] 预售人证号</li>
	 *          <li>String[13] 承购人名称</li>
	 *          <li>String[14] 承购人证件号</li>
	 *          <li>String[15] 是否托管</li>
	 *          <li>String[16] 三方协议号</li>
	 *          <li>String[17] 托管机构名称</li>
	 *          <li>String[18] 托管状态</li>
	 *          <li>String[19] 托管金额</li>
	 *          <li>String[20] 已缴款金额</li>
	 *          <li>String[21] 备注一</li>
	 *          <li>String[22] 备注二</li>
	 *          <li>String[23] 查询时间</li>
	 *		</ul>
	 *	</p>		
	 * @return 返回报文
	 */
	public static String getSuccessMsg_6102(String[] returnData)
	{
		StringBuffer sb = new StringBuffer();

		sb.append(fillConCNCode(returnData[0], 2, " ")); // 区位码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[1], 4, " ")); // 交易码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[2], 4, " ")); // 返回码
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[3], 50, " ")); // 备注
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[4], 30, " ")); // 预售合同编号
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[5], 2, " ")); // 合同状态
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[6], 100, " ")); // 房屋坐落
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[7], 10, " ")); // 建筑面积
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[8], 20, " ")); // 合同金额
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[9], 20, " ")); // 贷款金额
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[10], 2, " ")); // 是否装修
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[11], 60, " ")); // 预售人名称
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[12], 30, " ")); // 预售人证号
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[13], 200, " ")); // 承购人名称
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[14], 200, " ")); // 承购人证件号
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[15], 2, " ")); // 是否托管
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[16], 16, " ")); // 三方协议号
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[17], 60, " ")); // 托管机构名称
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[18], 2, " ")); // 托管状态
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[19], 20, " ")); // 托管金额
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[20], 20, " ")); // 已缴款金额
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[21], 30, " ")); // 备注一
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[22], 50, " ")); // 备注二
		sb.append(SEPARATE);
		sb.append(fillConCNCode(returnData[23], 20, " ")); // 查询时间
		sb.append(SEPARATE);

		String returnMsg = dataToString(S_SocketMsgLength.getMsg("6102"), sb);

		return returnMsg;
	}
	
	/**
	 * xsz by time 2019-7-6 14:34:16
	 * 档案归档接口
	 * @param url
	 * @param query
	 * @return
	 */
	public static int getRestFul(String url,String query)
	{
		int res = 0;
		try {
			URL restURL = new URL(url);			
			HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
			//请求方式
			conn.setRequestMethod("POST");
			//请求表头
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//@FormParam
			conn.setDoOutput(true);
			//输入参数编码方式
			byte[] postData = query.getBytes("UTF-8");
			
			PrintStream ps = new PrintStream(conn.getOutputStream());
			ps.write(postData);
			ps.flush();
			ps.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while((line = br.readLine()) != null ){
			  //接口调用返回结果
			  res=Integer.valueOf(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	

	/**
	 * xsz by 2019-11-6 14:12:40
	 * HttpClient发送json字符串post请求
	 * @param url
	 * @param json
	 * @return
	 */
    public String HttpStringPostRequest(String url, String json) {

    	log.info("推送银企直联请求地址："+url);
    	log.info("推送银企直联请求参数："+json);
        String returnValue = "error";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try{
            //第一步：创建HttpClient对象
            httpClient = HttpClients.createDefault();

            //第二步：创建httpPost对象
            HttpPost httpPost = new HttpPost(url);

            //第三步：给httpPost设置JSON格式的参数
            StringEntity requestEntity = new StringEntity(json,"utf-8");
            requestEntity.setContentEncoding("UTF-8");

            httpPost.setHeader("Content-type", "application/json");

            httpPost.setEntity(requestEntity);

            //第四步：发送HttpPost请求，获取返回值
            returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法

        }
        catch(Exception e)
        {
        	log.error("推送银企直联接口异常："+e.getMessage(), e);
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //第五步：处理返回值
        return returnValue;
    }
	
}

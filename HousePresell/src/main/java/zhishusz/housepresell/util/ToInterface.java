package zhishusz.housepresell.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import com.itextpdf.text.log.SysoCounter;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import zhishusz.housepresell.util.picture.MatrixUtil;

/**
 * 调用URL接口发送报文
 *
 * @author yang.yu
 * @Date 2019年5月8日11点39分
 */
public class ToInterface
{
//	private String PathURL = "http://211.149.201.151/";

	private String IS_OK = "1"; // 成功

	private String IS_FALSE = "0"; // 失败

	private static String charset = "utf-8";
	private static HttpClient httpClient = HttpClients.createDefault();


	private Log log = LogFactory.getCurrentLogFactory().createLog(ToInterface.class);

	/**
	 * 调用对方接口方法
	 *
	 * @param path
	 * @param PathURL
	 *            对方或第三方提供的路径
	 * @param data
	 *            向对方或第三方发送的数据，大多数情况下给对方发送JSON数据让对方解析
	 */
	public boolean interfaceUtil(String data,String PathURL)
	{
		boolean flag = true;

		try
		{
			URL url = new URL(PathURL);
			// 打开和url之间的连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			PrintWriter out = null;
			// 请求方式
			conn.setRequestMethod("POST");
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
			// 最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
			// post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数即数据
			out.print(data);
			// 缓冲数据
			out.flush();
			// 获取URLConnection对象对应的输入流
			InputStream is = conn.getInputStream();
			// 构造一个字符流缓存
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String str = "";
			while ((str = br.readLine()) != null)
			{
				if (IS_OK.equals(str.trim()))
				{
					flag = true;
				}
				else
				{
					flag = false;
				}
			}
			// 关闭流
			is.close();
			// 断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
			// 固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
			conn.disconnect();

			System.out.println("完整结束");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("数据推送门户网站异常：" + e.getMessage());

			flag = false;
		}

		return flag;
	}

	/**
	 * 设置输出编码为 utf-8
	 * @param data
	 * @param PathURL
	 * @return
	 */
	public boolean interfaceUtilUTF8(String data,String PathURL)
	{
		boolean flag = true;

		try
		{
			URL url = new URL(PathURL);
			// 打开和url之间的连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			PrintWriter out = null;
			// 请求方式
			conn.setRequestMethod("POST");
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Charset", "utf-8");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			// 设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
			// 最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
			// post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数即数据
			out.print(data);
			// 缓冲数据
			out.flush();
			// 获取URLConnection对象对应的输入流
			InputStream is = conn.getInputStream();
			// 构造一个字符流缓存
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String str = "";
			while ((str = br.readLine()) != null)
			{
				if (IS_OK.equals(str.trim()))
				{
					flag = true;
				}
				else
				{
					flag = false;
				}
			}
			// 关闭流
			is.close();
			// 断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
			// 固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
			conn.disconnect();

			System.out.println("完整结束");
		}
		catch (Exception e)
		{
			log.error("数据推送门户网站异常：" + e.getMessage());

			flag = false;
		}

		return flag;
	}

	public String commonInterface(String PathURL,String data){
		try
		{
			URL url = new URL(PathURL);
			// 打开和url之间的连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			PrintWriter out = null;
			// 请求方式
			conn.setRequestMethod("POST");
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
			// 最常用的Http请求无非是get和post，get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，
			// post与get的 不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数即数据
			out.print(data);
			// 缓冲数据
			out.flush();
			// 获取URLConnection对象对应的输入流
			InputStream is = conn.getInputStream();
			// 构造一个字符流缓存
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String readLine = br.readLine();
			/*String str = "";
			while ((str = br.readLine()) != null)
			{
				System.out.println(str);
			}*/
			// 关闭流
			is.close();
			// 断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
			// 固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
			conn.disconnect();

			System.out.println("完整结束");
			return readLine;

		}
		catch (Exception e)
		{
			System.out.println("完整异常");
			e.printStackTrace();
			log.error("数据推送门户网站异常：" + e.getMessage());
			return null;

		}
	}


	public static String doPost(String data,String PathURL){
		HttpPost httpPost;
		String result = null;
		try {
			httpPost = new HttpPost(PathURL);
			// 设置参数
			httpPost.setHeader("content-Type","application/json;charset=UTF-8");
			httpPost.setHeader("Accept","application/json");
			StringEntity entity = new StringEntity(data, Charset.forName("UTF-8"));
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, charset);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}



	public static void main(String[] args) throws Exception
	{
		// 初始化文件保存路径，创建相应文件夹
		DirectoryUtil directoryUtil = new DirectoryUtil();

		String localPath = directoryUtil.getProjectRoot();// 项目路径

		if (localPath.contains("%20"))
		{
			localPath = localPath.replace("%20", " ");
		}
		if (localPath.contains("file:/"))
		{
			localPath = localPath.replace("file:/", "");
		}

		String localPath1 = localPath + "01.png";

		String localPath2 = localPath + "02.png";

		String localPath3 = localPath + "03.png";

		String importPath = "http://cz.zhishusz.com:19000/OssSave//bananaUpload/201905/13/a510cb119233424d8bff6d28cc7d82af.jpeg";

		MatrixUtil picUtil = new MatrixUtil();

		String httpUrl = picUtil.compressionPic(importPath, "C:\\Users\\ZS004\\Desktop\\logo.png", "460", "345");

		System.out.println("http路径：" + httpUrl);

		// markImgMark("C:\\Users\\ZS004\\Desktop\\1.png",
		// "C:\\Users\\ZS004\\Desktop\\logo.png", localPath2);

		/*
		 * Map<String, String> map = new HashMap<>();
		 *
		 * map.put("action", "edit");
		 * map.put("cate", "pj");
		 * map.put("pj_title", "水木连华项目");
		 * map.put("ts_pj_id", "112");
		 * map.put("pj_area_name", "天宁区");
		 * map.put("ts_area_id", "01");
		 * map.put("pj_kfs", "重锤开发");
		 * map.put("pj_dz", "天宁区水木连华路123号");
		 * map.put("pj_pic", "https://image.baidu.com/search/detail");
		 * map.put("pj_content", "水木连华项目");
		 * map.put("ts_jl_name", "水木连华监理公司");
		 * map.put("ts_jl_id", "113");
		 * map.put("pj_longitude", "116.23");
		 * map.put("pj_latitude", "23.56");
		 * map.put("pj_str1", "");
		 * map.put("pj_str2", "");
		 * map.put("pj_str3", "");
		 * map.put("pj_str4", "");
		 * map.put("pj_num1", "");
		 * map.put("pj_num2", "");
		 * map.put("pj_num3", "");
		 * map.put("pj_num4", "");
		 *
		 * String jsonMap = JSONUtil.toJsonStr(map);
		 *
		 * System.out.println(jsonMap);
		 *
		 * String decodeStr = Base64Encoder.encode(jsonMap);
		 *
		 * System.out.println(decodeStr);
		 *
		 * interfaceUtil(decodeStr);
		 *
		 *
		 * try
		 * {
		 * doPost(decodeStr);
		 * }
		 * catch (Exception e)
		 * {
		 * // TODO Auto-generated catch block
		 * e.printStackTrace();
		 * }
		 */
	}
}


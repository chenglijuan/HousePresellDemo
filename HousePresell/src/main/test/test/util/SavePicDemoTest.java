package test.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.xiaominfo.oss.sdk.ReceiveMessage;

import zhishusz.housepresell.initialize.BaseJunitTest;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.fileupload.OssServerUtil;
import zhishusz.housepresell.util.picture.MatrixUtil;

/*
 * 公共类测试
 * Company：ZhiShuSZ
 */
public class SavePicDemoTest extends BaseJunitTest
{

	@Test
	public void execute() throws Exception
	{
		// 创建导出路径
		DirectoryUtil directoryUtil = new DirectoryUtil();
		String localPath = directoryUtil.getProjectRoot();// 项目路径
		if (localPath.contains("file:/"))
		{
			localPath = localPath.replace("file:/", "");
		}

		// D:/Workspaces/MyEclipse%202017%20CI/.metadata/.me_tcat85/webapps/HousePresell/
		String saveDirectory = localPath + "pic/";// 文件在服务器文件系统中的完整路径

		if (saveDirectory.contains("%20"))
		{
			saveDirectory = saveDirectory.replace("%20", " ");
		}

		directoryUtil.mkdir(saveDirectory);

		MatrixUtil.inserPIC2("吴煜是个嘤嘤怪！", saveDirectory);

	}

	@Test
	public void testPic() throws Exception
	{
		// String root =
		// Thread.currentThread().getContextClassLoader().getResource("/").getPath();

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

		OssServerUtil ossUtil = new OssServerUtil();
		
		// 保存oss
		ReceiveMessage upload = ossUtil.upload(httpUrl);

		System.out.println("http路径：" + httpUrl);
	}

	@Test
	public void execute2() throws Exception
	{
		String ip = "http://192.168.1.8:19000/OssSave/bananaUpload/201811/07/a0b571e4f9644453ac27fba78a40e943.xls";
		boolean flag = isInner(ip);
		System.out.println(flag);
	}

	public boolean isInner(String ip)
	{
		String reg = "(10|172|192)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";
		Pattern p = Pattern.compile(reg);
		Matcher matcher = p.matcher(ip);
		return matcher.find();
	}
}

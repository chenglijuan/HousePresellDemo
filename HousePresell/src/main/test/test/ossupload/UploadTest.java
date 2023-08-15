package test.ossupload;

import zhishusz.housepresell.util.fileupload.OssServerUtil;
import com.xiaominfo.oss.sdk.OSSClient;
import com.xiaominfo.oss.sdk.OSSClientProperty;
import com.xiaominfo.oss.sdk.ReceiveMessage;

import org.junit.Test;

import java.io.File;

/**
 * Created by Dechert on 2018-08-17.
 * Company: zhishusz
 */

public class UploadTest
{
	@Test
	public void upload(){
		//客户端上传
//		String url = "http://127.0.0.1:18000";
		String url = "http://192.168.1.155:18000";
		//        OSSClientProperty ossClientProperty = new OSSClientProperty(url, "province_IIII");
		OSSClientProperty ossClientProperty = new OSSClientProperty(url, "DCTUpload");
		//        ossClientProperty.setAppid("zh");
		ossClientProperty.setAppid("ossu0oiiw");
		//        ossClientProperty.setAppsecret("123123");
		ossClientProperty.setAppsecret("325iwm59");
		ossClientProperty.setExtra("43643regrde");
//		HashMap<String, String> stringStringHashMap = new HashMap<>();
//		stringStringHashMap.put("the test", "ok success");
//		ossClientProperty.setExtraMap(stringStringHashMap);
//		OSSKeyMap ossKeyMap=new OSSKeyMap();
//		ossKeyMap.setKey("the");
//		ossKeyMap.setValue("success");
//		ossClientProperty.setExtra(ossKeyMap);
		OSSClient ossClient = new OSSClient(ossClientProperty, "BASE64");
		//File uploadFile=new File("C:\\Users\\xiaoymin\\Desktop\\aa.jpg");
		//File uploadFile = new File("D:\\source\\oss-server\\static\\wechat.jpg");
		File uploadFile = new File("E:\\5_Material\\icon2x.png");

//		ReceiveMessage ossClientMessage = ossClient.uploadFile(uploadFile);

//		System.out.println(JSON.toJSONString(ossClientMessage));

		OssServerUtil ossServerUtil = new OssServerUtil();
		ossServerUtil.setOssClient(ossClient);
		ReceiveMessage upload = ossServerUtil.upload(uploadFile);
//		System.out.println();
	}

	@Test
	public void sendMessage(){
		OssServerUtil ossServerUtil = new OssServerUtil();
		OSSClientProperty ossClientProperty = new OSSClientProperty();
		ossClientProperty.setAppid("ossu0oiiw");
		ossClientProperty.setAppsecret("325iwm59");
		ossClientProperty.setProject("DCTUpload");
		ossClientProperty.setRemote("http://192.168.1.155:18000");
//		OSSKeyMap ossKeyMap = new OSSKeyMap();
//		ossKeyMap.setValue("the isss");
//		ossKeyMap.setKey("dddsss");
		ossClientProperty.setExtra("ossKeyMap");
		OSSClient ossClient = new OSSClient(ossClientProperty,"BASE64");
		ossServerUtil.setOssClient(ossClient);

		ossServerUtil.stringUpload("my test string ok");
	}

	@Test
	public void download(){
		OssServerUtil ossServerUtil = new OssServerUtil();
		String download = ossServerUtil
				.download("http://192.168.1.155/OssSave/DCTUpload/201808/28/b0df136caf07426299f3c3e20bc6c189.txt");

		ossServerUtil.download("http://192.168.1.155/OssSave/DCTUpload/201808/28/b0df136caf07426299f3c3e20bc6c189.txt","D:\\1_MyProject\\tr.txt");
		System.out.println(download);
	}

}

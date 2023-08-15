package zhishusz.housepresell.util.license;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;
import java.util.prefs.Preferences;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultKeyStoreParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * Created by Dechert on 2018-09-17.
 * Company: zhishusz
 */

public class VerifyLicense
{
	//common param
	private static String publicAlias = "";
	private static String storePwd = "";
	private static String subject = "";
	private static String licPath = "";
	private static String pubPath = "";
	private String info = "";
	private Gson gson = new Gson();
	private String mac;
	private String ip;

	public void setParam(String propertiesPath)
	{
		// 获取参数
		Properties prop = new Properties();
		InputStream in = getClass().getClassLoader().getResourceAsStream(propertiesPath);
		try
		{
			prop.load(in);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		publicAlias = prop.getProperty("PUBLIC_ALIAS");
		storePwd = prop.getProperty("STORE_PWD");
		subject = prop.getProperty("SUBJECT");
		licPath = prop.getProperty("LIC_PATH");
		pubPath = prop.getProperty("PUB_Path");
	}

	public boolean verify()
	{
		/************** 证书使用者端执行 ******************/

		LicenseManager licenseManager = LicenseManagerHolder.getLicenseManager(initLicenseParams());
		// 安装证书
		try
		{
			LicenseParam licenseParam = licenseManager.getLicenseParam();
			LicenseContent install = licenseManager.install(new File(licPath));
			verifyMacAndIp(install);
			System.out.println("客户端安装证书成功!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("客户端证书安装失败!");
			return false;
		}
		// 验证证书
		try
		{
			licenseManager.verify();
			System.out.println("客户端验证证书成功!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("客户端证书验证失效!");
			return false;
		}
		return true;
	}

	/**
	 * 验证IP和MAC是否匹配
	 * @param install
	 */
	private void verifyMacAndIp(LicenseContent install)
	{
		try
		{
			InetAddress inetAddress = InetAddress.getLocalHost();
			String localMac = getLocalMac(inetAddress);
			String ip = inetAddress.getHostAddress();
			String info = install.getInfo();
			HashMap<String, String> hashMap = gson.fromJson(info, HashMap.class);
			String infoMacIp = hashMap.get(localMac);
			if (infoMacIp == null)
			{
				throw new IllegalArgumentException("MAC地址不正确！");
			}
			else
			{
				if (ip.equals(infoMacIp))
				{

				}
				else
				{
					throw new IllegalArgumentException("IP地址不正确！");
				}
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}

	}

	// 返回验证证书需要的参数
	private static LicenseParam initLicenseParams()
	{
		Preferences preference = Preferences.userNodeForPackage(VerifyLicense.class);
		CipherParam cipherParam = new DefaultCipherParam(storePwd);

		KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(VerifyLicense.class, pubPath, publicAlias, storePwd,
				null);
		LicenseParam licenseParams = new DefaultLicenseParam(subject, preference, privateStoreParam, cipherParam);
		return licenseParams;
	}

	public String getInfo()
	{
		return info;
	}

	public void setInfo(String info)
	{
		this.info = info;
	}

	private String getLocalMac(InetAddress ia) throws SocketException
	{
		//获取网卡，获取地址
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mac.length; i++)
		{
			if (i != 0)
			{
				sb.append("-");
			}
			//字节转换为整数
			int temp = mac[i] & 0xff;
			String str = Integer.toHexString(temp);
			if (str.length() == 1)
			{
				sb.append("0" + str);
			}
			else
			{
				sb.append(str);
			}
		}
		return sb.toString().toUpperCase();
	}
}

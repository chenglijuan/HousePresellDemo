package zhishusz.housepresell.database.po.internal;

public class OssServerAccount
{
	/**
	 * ossServer的url
	 * 例如：http://127.0.0.1:18000
	 */
	private String ossServerUrl;
	/**
	 * 项目的Code（应用编码），不是项目名称
	 */
	private String projectCode;
	/**
	 * appId开发人员的appId
	 */
	private String appId;
	/**
	 * appSecret开发人员的appSecret
	 */
	private String appSecret;

	public String getOssServerUrl()
	{
		return ossServerUrl;
	}

	public void setOssServerUrl(String ossServerUrl)
	{
		this.ossServerUrl = ossServerUrl;
	}

	public String getProjectCode()
	{
		return projectCode;
	}

	public void setProjectCode(String projectCode)
	{
		this.projectCode = projectCode;
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public String getAppSecret()
	{
		return appSecret;
	}

	public void setAppSecret(String appSecret)
	{
		this.appSecret = appSecret;
	}
}

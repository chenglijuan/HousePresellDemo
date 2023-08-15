package zhishusz.housepresell.util.rebuild;
/**
 * 附件配置额外封装参数
 * @ClassName:  Sm_AttachmentCfgDataInfo   
 * @Description:TODO   
 * @author: xushizhong 
 * @date:   2018年8月30日 下午5:29:51   
 * @version V1.0 
 *
 */
public class Sm_AttachmentCfgDataInfo
{
	private String extra;
	private String appid;
	private String appsecret;
	private String remote;
	private String project;
	
	private String upLoadUrl;
	public String getExtra()
	{
		return extra;
	}
	public void setExtra(String extra)
	{
		this.extra = extra;
	}
	public String getAppid()
	{
		return appid;
	}
	public void setAppid(String appid)
	{
		this.appid = appid;
	}
	public String getAppsecret()
	{
		return appsecret;
	}
	public void setAppsecret(String appsecret)
	{
		this.appsecret = appsecret;
	}
	public String getRemote()
	{
		return remote;
	}
	public void setRemote(String remote)
	{
		this.remote = remote;
	}
	public String getProject()
	{
		return project;
	}
	public void setProject(String project)
	{
		this.project = project;
	}
	public String getUpLoadUrl()
	{
		return upLoadUrl;
	}
	public void setUpLoadUrl(String upLoadUrl)
	{
		this.upLoadUrl = upLoadUrl;
	}
	
}

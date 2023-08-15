package zhishusz.housepresell.service;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zhishusz.housepresell.controller.form.Sm_AttachmentCfgForm;
import zhishusz.housepresell.controller.form.Sm_AttachmentForm;
import zhishusz.housepresell.controller.form.Sm_BaseParameterForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskLetterForm;
import zhishusz.housepresell.controller.form.Tg_PjRiskLetterReceiverForm;
import zhishusz.housepresell.database.dao.Sm_AttachmentCfgDao;
import zhishusz.housepresell.database.dao.Sm_AttachmentDao;
import zhishusz.housepresell.database.dao.Sm_BaseParameterDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterDao;
import zhishusz.housepresell.database.dao.Tg_PjRiskLetterReceiverDao;
import zhishusz.housepresell.database.po.Sm_Attachment;
import zhishusz.housepresell.database.po.Sm_AttachmentCfg;
import zhishusz.housepresell.database.po.Sm_BaseParameter;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tg_PjRiskLetter;
import zhishusz.housepresell.database.po.Tg_PjRiskLetterReceiver;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.database.po.state.S_TheState;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyProperties;
	
/*
 * Service添加操作：项目风险函
 * Company：ZhiShuSZ
 * */
@Service
@Transactional
public class Tg_PjRiskLetterSendMailService
{
	@Autowired
	private Tg_PjRiskLetterDao tg_PjRiskLetterDao;
	@Autowired
	private Sm_BaseParameterDao sm_BaseParameterDao;
	@Autowired
	private Tg_PjRiskLetterReceiverDao tg_PjRiskLetterReceiverDao;
	@Autowired
	private Tg_PjRiskLetterPrintService tg_PjRiskLetterPrintService;
	@Autowired
	private Sm_AttachmentDao smAttachmentDao;
	@Autowired
	private Sm_AttachmentCfgDao sm_AttachmentCfgDao;
	
	
	MyDatetime myDatetime = MyDatetime.getInstance();
	
	private static final String BUSI_CODE = "21020304";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	private static final String busiType = "240103";//具体业务编码参看SVN文件"Document\原始需求资料\功能菜单-业务编码.xlsx"
	
	public Properties execute(Tg_PjRiskLetterForm model)
	{
		Properties properties = new MyProperties();
		
		Sm_User user = model.getUser();
		
		String emailAccount = getParameter("邮箱账号","53");
		String emailPassword = getParameter("邮箱密码","53");
		
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

        Properties props = System.getProperties(); // 获取系统属性

        props.setProperty("mail.smtp.host", "smtp-mail.outlook.com"); // 设置smtp服务器名填写到“mail.smtp.host”关键字中

        // 设置javamail的缺省属性
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // gmail的邮箱用的是SSL安全套接字层
        props.setProperty("mail.smtp.socketFactory.fallback", "false"); // 只处理SSL的连接
        props.setProperty("mail.smtp.port", "587"); // 使用SSL协议对应的端口改变默认端口
        props.setProperty("mail.smtp.socketFactory.port", "587");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.fallback", "true");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props, new Authenticator() { // 创建邮件会话对象
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount,
                		emailPassword); // 向调用程序返回填写的用户名和密码
            }
        });

		Long tg_PjRiskLetterId = model.getTableId();
		Tg_PjRiskLetter tg_PjRiskLetter = (Tg_PjRiskLetter)tg_PjRiskLetterDao.findById(tg_PjRiskLetterId);
		if(tg_PjRiskLetter == null || S_TheState.Deleted.equals(tg_PjRiskLetter.getTheState()))
		{
			return MyBackInfo.fail(properties, "项目风险函不存在，请核实！");
		}
				
//		properties = tg_PjRiskLetterPrintService.execute(model);
//		String url = "";
//		
//		if(properties.getProperty("result").equals("fail"))
//		{
//			return properties;
//		}
//		else
//		{
//			url = properties.getProperty("url");
//		}
		
		// 创建导出路径
//		DirectoryUtil directoryUtil = new DirectoryUtil();
//		String localPath = directoryUtil.getProjectRoot();// 项目路径
//		// D:/Workspaces/MyEclipse%202017%20CI/.metadata/.me_tcat85/webapps/HousePresell/
//		String saveDirectory = localPath + url ;// 文件在服务器文件系统中的完整路径
//
//		if (saveDirectory.contains("%20"))
//		{
//			saveDirectory = saveDirectory.replace("%20", " ");
//		}
		
		String urlPath = "";
				
		Sm_AttachmentCfgForm attachmentCfgForm = new Sm_AttachmentCfgForm();
		attachmentCfgForm.setTheState(S_TheState.Normal);
		attachmentCfgForm.setBusiType(busiType);
		List<Sm_AttachmentCfg> smAttachmentCfgList = sm_AttachmentCfgDao
				.findByPage(sm_AttachmentCfgDao.getQuery(sm_AttachmentCfgDao.getBasicHQL(), attachmentCfgForm));
		
		Sm_AttachmentCfg sm_AttachmentCfg = new Sm_AttachmentCfg();

		if (null == smAttachmentCfgList || smAttachmentCfgList.size() == 0)
		{
			smAttachmentCfgList = new ArrayList<Sm_AttachmentCfg>();
		}
		else
		{
			sm_AttachmentCfg = smAttachmentCfgList.get(0);
			String cfgeCode = sm_AttachmentCfg.geteCode();
			
			Sm_AttachmentForm attachmentForm = new Sm_AttachmentForm();
			
			attachmentForm.setSourceType(cfgeCode);
			attachmentForm.setSourceId(tg_PjRiskLetterId.toString());
			attachmentForm.setTheState(S_TheState.Normal);
			
			List<Sm_Attachment> smAttachmentList = new ArrayList<Sm_Attachment>();
			
			Sm_Attachment sm_Attachment = new Sm_Attachment();
			// 查询附件
			smAttachmentList = smAttachmentDao
					.findByPage(smAttachmentDao.getQuery(smAttachmentDao.getBasicHQL2(), attachmentForm));

			if (null == smAttachmentList || smAttachmentList.size() == 0)
			{
				smAttachmentList = new ArrayList<Sm_Attachment>();
			}
			else
			{
				sm_Attachment = smAttachmentList.get(0);
				urlPath = sm_Attachment.getTheLink();
			}
		}
		
		// 查询所有发送邮件的对象
		Tg_PjRiskLetterReceiverForm tg_PjRiskLetterReceiverForm = new Tg_PjRiskLetterReceiverForm();
		tg_PjRiskLetterReceiverForm.setTheState(S_TheState.Normal);
		tg_PjRiskLetterReceiverForm.setTg_PjRiskLetter(tg_PjRiskLetter);
		tg_PjRiskLetterReceiverForm.setSendWay(1);

		Integer totalCount = tg_PjRiskLetterReceiverDao.findByPage_Size(tg_PjRiskLetterReceiverDao
				.getQuery_Size(tg_PjRiskLetterReceiverDao.getBasicHQL(), tg_PjRiskLetterReceiverForm));

		List<Tg_PjRiskLetterReceiver> tg_PjRiskLetterReceiverList;
		if (totalCount > 0)
		{
			tg_PjRiskLetterReceiverList = tg_PjRiskLetterReceiverDao.findByPage(tg_PjRiskLetterReceiverDao
					.getQuery(tg_PjRiskLetterReceiverDao.getBasicHQL(), tg_PjRiskLetterReceiverForm));
			
			String email = "";
			
			for(Tg_PjRiskLetterReceiver tg_PjRiskLetterReceiver : tg_PjRiskLetterReceiverList)
			{
				
				if( null == tg_PjRiskLetterReceiver.getEmail() || tg_PjRiskLetterReceiver.getEmail().length() < 0)
				{
					continue;
				}
				else if( 1 == tg_PjRiskLetterReceiver.getSendStatement())
				{
					continue;
				}
				else
				{
					email = tg_PjRiskLetterReceiver.getEmail();		
					
					 try
						{
				        	MimeMessage msg = new MimeMessage(session); // 创建Mimemessage消息
							// 设置message的各个属性
							msg.setFrom(new InternetAddress(emailAccount)); // 消息的from域
							msg.setRecipients(Message.RecipientType.TO,
							        InternetAddress.parse(email, false)); // 消息的收件人
							msg.setSubject("项目风险函"); // 消息主题
							
							String result = tg_PjRiskLetter.getBasicSituation();
							if( null != urlPath || !"".equals(urlPath))
							{
								result = result + "<br>" + "请查看附件：" + urlPath;
							}
							
							msg.setText(result); // 消息内容
							msg.setSentDate(new Date()); // 消息发送时间
							
//							String filePath = saveDirectory;
							
							// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
							Multipart multipart = new MimeMultipart();
							
							// 添加邮件正文
							BodyPart contentBodyPart = new MimeBodyPart();
							// 邮件内容
//							String result = tg_PjRiskLetter.getBasicSituation();
							contentBodyPart.setContent(result, "text/html;charset=UTF-8");
							multipart.addBodyPart(contentBodyPart);
							
							// 添加附件			
//							if (filePath != null && !"".equals(filePath)) {
//								BodyPart attachmentBodyPart = new MimeBodyPart();
//								// 根据附件路径获取文件,
//								FileDataSource dataSource = new FileDataSource(filePath);
//								attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
//								//MimeUtility.encodeWord可以避免文件名乱码
//								
//								 attachmentBodyPart.setFileName(MimeUtility.encodeWord(dataSource.getFile().getName()));
//								 multipart.addBodyPart(attachmentBodyPart);
//								 
//							}
							
							// 邮件的文本内容
							msg.setContent(multipart);
							
							Transport.send(msg); // 利用Transport消息传输类的send方法发送消息
						}
						catch (AddressException e)
						{
							System.out.print(e.getMessage());
							e.printStackTrace();
						}
						catch (MessagingException e)
						{
							System.out.print(e.getMessage());
							e.printStackTrace();
						}
//						catch (UnsupportedEncodingException e)
//						{
//							System.out.print(e.getMessage());
//							e.printStackTrace();
//						}
										
					tg_PjRiskLetterReceiver.setSendStatement(1);
					tg_PjRiskLetterReceiver.setSendTimeStamp(myDatetime.dateToSimpleString(System.currentTimeMillis()));
					
					tg_PjRiskLetterReceiverDao.save(tg_PjRiskLetterReceiver);
				}
			}				
		}
		else
		{
			tg_PjRiskLetterReceiverList = new ArrayList<Tg_PjRiskLetterReceiver>();
		}

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);
		
        return properties;
		
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

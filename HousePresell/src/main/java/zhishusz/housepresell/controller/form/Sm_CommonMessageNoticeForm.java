package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_CommonMessage;
import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.util.IFieldAnnotation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：消息通知
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Sm_CommonMessageNoticeForm extends NormalActionForm
{
	private static final long serialVersionUID = -7056014432243863833L;
	
	@Getter @Setter
	private Long tableId;//表ID
	
	private String eCode;	//编号
	@Getter @Setter 
	private Sm_User userStart; //创建人
	@Getter @Setter
	private Long createTimeStamp; //创建时间
	@Getter @Setter
	private Sm_User userUpdate; //修改人
	@Getter @Setter
	private Long lastUpdateTimeStamp; //最后修改日期
	@Getter @Setter
	private Integer theState;// 状态 S_TheState 初始为Normal	
	@Getter @Setter
	private String messageType; //消息类型	
	@Getter @Setter
	private String busiState; //消息业务状态 
	@Getter @Setter
	private String busiCode; //业务编码	
	@Getter @Setter
	private String orgDataId;//关联数据表ID	
	@Getter @Setter
	private String orgDataCode;//关联数据表eCode-冗余	
	@Getter @Setter
	private String theTitle;//主题	
	@Getter @Setter
	private String theContent;//内容	
	@Getter @Setter
	private String theData;//JSON格式 //扩展数据	
	@Getter @Setter
	private String sendTimeStamp;//发送日期yyyy-MM-dd HH:mm:ss
	@Getter @Setter
	public String remark;// 备注
	@Getter @Setter
	private Sm_User receiver;// 接收人
	@Getter @Setter
	private Long receiverId;// 接收人Id
	@Getter @Setter
	private Sm_CommonMessage message; //消息主体
	
	@Getter @Setter
	private Integer isReader;//是否已读 S_IsReaderState
	
	@Getter @Setter
	private String otherBusiCode;//关联业务业务编号
	
	public String geteCode() {
		return eCode;
	}
	public void seteCode(String eCode) {
		this.eCode = eCode;
	}//名称
}

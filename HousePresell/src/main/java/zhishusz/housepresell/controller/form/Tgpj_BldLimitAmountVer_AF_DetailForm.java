package zhishusz.housepresell.controller.form;

import zhishusz.housepresell.database.po.Sm_User;
import zhishusz.housepresell.database.po.Tgpj_BldLimitAmountVer_AF;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Form表单：受限额度设置
 * Company：ZhiShuSZ
 * */
@ToString(callSuper=true)
public class Tgpj_BldLimitAmountVer_AF_DetailForm extends NormalActionForm
{
	private static final long serialVersionUID = -2185831769349283963L;
	
	@Getter @Setter
	private Long tableId;//表ID
	@Getter @Setter
	private Integer theState;//状态 S_TheState 初始为Normal
	@Getter @Setter
	private String busiState;//业务状态
	@Getter @Setter
	private String eCode;//编号
	@Getter @Setter
	private Sm_User userStart;//创建人
	@Getter @Setter
	private Long userStartId;//创建人-Id
	@Getter @Setter
	private Long createTimeStamp;//创建时间
	@Getter @Setter
	private Sm_User userUpdate;//最后修改人人
	@Getter @Setter
	private Long userUpdateId;//修改人-Id
	@Getter @Setter
	private Long lastUpdateTimeStamp;//最后修改日期
	@Getter @Setter
	private Sm_User userRecord;//备案人
	@Getter @Setter
	private Long userRecordId;//备案人-Id
	@Getter @Setter
	private Long recordTimeStamp;//备案日期
	@Getter @Setter
	private Tgpj_BldLimitAmountVer_AF bldLimitAmountVerMng;//关联受限额度主表
	@Getter @Setter
	private Long bldLimitAmountVerMngId;//关联受限额度主表-Id
	@Getter @Setter
	private String stageName;//阶段名称
	@Getter @Setter
	private Double limitedAmount;//受限额度
	
	
}

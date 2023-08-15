package zhishusz.housepresell.database.po;

import java.io.Serializable;

import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import lombok.Getter;
import lombok.Setter;


/*
 * 对象名称：托管项目统计表（项目部）
 * */
@ITypeAnnotation(remark="托管项目统计表（项目部）")
public class Tg_LoanProjectCountP_View  implements Serializable
{

	private static final long serialVersionUID = 8745338413068938256L;

	@Getter @Setter @IFieldAnnotation(remark="表ID", isPrimarykey=true)
	private Long tableId;//主键
	
	@Getter @Setter @IFieldAnnotation(remark="集团")
	private String companyGroup;//集团
	
	@Getter @Setter @IFieldAnnotation(remark="开发企业")
	private String companyName;//开发企业
	
	@Getter @Setter @IFieldAnnotation(remark="区域")
	private String cityRegion;//区域
	
	@Getter @Setter @IFieldAnnotation(remark="托管项目")
	private String projectName;//托管项目
	
	@Getter @Setter @IFieldAnnotation(remark="托管楼幢")
	private String eCodeFromConstruction;//托管楼幢
	
	@Getter @Setter @IFieldAnnotation(remark="交付类型")
	private String deliveryType;//交付类型
	
	@Getter @Setter @IFieldAnnotation(remark="地上总层数")
	private Double upTotalFloorNumber;//地上总层数
	
	@Getter @Setter @IFieldAnnotation(remark="托管面积")
	private Double escrowArea;//托管面积
	
	@Getter @Setter @IFieldAnnotation(remark="托管楼幢备案均价")
	private Double recordAvgPriceOfBuilding;//托管楼幢备案均价
	
	@Getter @Setter @IFieldAnnotation(remark="初始受限额度")
	private Double orgLimitedAmount;//初始受限额度
	
	@Getter @Setter @IFieldAnnotation(remark="当前受限额度")
	private Double currentLimitedAmount;//当前受限额度
	
	@Getter @Setter @IFieldAnnotation(remark="当前建设进度")
	private String currentBuildProgress;//当前建设进度
	
	@Getter @Setter @IFieldAnnotation(remark="当前受限节点")
	private String currentLimitedNote;//当前受限节点
	
	@Getter @Setter @IFieldAnnotation(remark="托管余额")
	private Double currentEscrowFund;//托管余额
	
	@Getter @Setter @IFieldAnnotation(remark="抵充额度")
	private Double amountOffset;//抵充额度
	
	@Getter @Setter @IFieldAnnotation(remark="总户数")
	private Double sumFamilyNumber;//总户数
	
	@Getter @Setter @IFieldAnnotation(remark="签约户数")
	private Double signHouseNum;//签约户数
	
	@Getter @Setter @IFieldAnnotation(remark="备案户数")
	private Double recordHouseNum;//备案户数
	
	@Getter @Setter @IFieldAnnotation(remark="托管户数")
	private Double depositHouseNum;//托管户数
	
	@Getter @Setter @IFieldAnnotation(remark="预售证（有/无）")
	private String isPresell;//预售证（有/无）
	
	@Getter @Setter @IFieldAnnotation(remark="合作协议备案时间")
	private String escrowAgRecordTime;//合作协议备案时间
	
	@Getter @Setter @IFieldAnnotation(remark="项目信息")
	private Empj_ProjectInfo projectInfo;

	public String geteCodeFromConstruction()
	{
		return eCodeFromConstruction;
	}

	public void seteCodeFromConstruction(String eCodeFromConstruction)
	{
		this.eCodeFromConstruction = eCodeFromConstruction;
	}
}

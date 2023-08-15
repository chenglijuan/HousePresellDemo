package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;

/*
 * 对象名称：项目风险评级
 */

public class Tg_PjRiskRatingExportExcelVO
{	

	@Getter @Setter
	private Integer ordinal;//序号 	
	private String eCode;//评级单号
	@Getter @Setter
	private String theNameOfCityRegion;//所属区域
	@Getter @Setter
	private String companyName;//开发企业名称
	@Getter @Setter
	private String projectName;//项目名称
	@Getter @Setter
	private String operateDate;//评级日期yyyy-MM-dd
	@Getter @Setter
	private String theLevel;//评级级别
	@Getter @Setter
	private String userUpdate;//操作人	
	@Getter @Setter
	private String lastUpdateTimeStamp;//操作日期

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	
}

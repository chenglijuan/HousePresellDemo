package zhishusz.housepresell.util.rebuild;

import java.util.Properties;

import org.springframework.stereotype.Service;


import zhishusz.housepresell.database.dao.Tg_ProjectRiskDao;
import zhishusz.housepresell.database.po.Tg_ProjectRiskView;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuild : 项目风险明细表
 * */
@Service
public class Tg_ProjectRiskListRebuild extends RebuilderBase<Tg_ProjectRiskView>
{
	@Override
	public Properties getSimpleInfo(Tg_ProjectRiskView object)
	{
		if(object == null)
			return null;
		Properties properties = new MyProperties();

		properties.put("tableId", object.getTableId());

		properties.put("managedProjects", object.getManagedProjects());//托管项目
		properties.put("area", object.getArea());//区域

		properties.put("floorBuilding", object.getFloorBuilding());//托管楼幢
		properties.put("managedArea", object.getManagedArea());//托管面积
		properties.put("dateOfPresale", object.getDateOfPresale());//预售许可批准日期
		properties.put("totalOfoverground", object.getTotalOfoverground());//地上总层数
		properties.put("currentConstruction", object.getCurrentConstruction());//当前建设进度
		properties.put("updateTime", object.getUpdateTime());//进度更新时间
		properties.put("appointedTime", object.getAppointedTime());//合同约定交付时间

		properties.put("progressEvaluation", object.getProgressEvaluation());//进度评定
		properties.put("signTheEfficiency", object.getSignTheEfficiency());//合同签订率
		properties.put("contractFilingRate", object.getContractFilingRate());//合同备案率
		properties.put("contractLoanRatio", object.getContractLoanRatio());//合同贷款率
		properties.put("hostingFullRate", object.getHostingFullRate());//托管满额率

		properties.put("unsignedContract", object.getUnsignedContract());//未签订合同查封
		properties.put("alreadyUnsignedContract", object.getAlreadyUnsignedContract());//已签订合同查封
		properties.put("astrict", object.getAstrict());//限制
		properties.put("payguarantee", object.getPayguarantee());//土地抵押情况(有/无)
		properties.put("landMortgage", object.getLandMortgage());//土地抵押情况(有/无)
		properties.put("otherRisks", object.getOtherRisks());//其他情况
		properties.put("riskRating", object.getRiskRating());//风险评级(高/中/低)

		return properties;
	}

	@Override
	public Properties getDetail(Tg_ProjectRiskView object)
	{

		return null;
	}



}

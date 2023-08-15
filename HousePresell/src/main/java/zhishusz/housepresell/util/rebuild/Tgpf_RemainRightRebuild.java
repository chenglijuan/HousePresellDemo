package zhishusz.housepresell.util.rebuild;

import zhishusz.housepresell.database.po.Tgpf_RemainRight;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/*
 * Rebuilder：留存权益
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_RemainRightRebuild extends RebuilderBase<Tgpf_RemainRight>
{
	@Override
	public Properties getSimpleInfo(Tgpf_RemainRight object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("enterTimeStamp", object.getEnterTimeStamp());
		properties.put("buyer", object.getBuyer());
		properties.put("theNameOfCreditor", object.getTheNameOfCreditor());
		properties.put("idNumberOfCreditor", object.getIdNumberOfCreditor());
		properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());
		properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());
		properties.put("srcBusiType", object.getSrcBusiType());
		if (object.getProject() != null) {
			properties.put("projectId", object.getProject().getTableId());
		}
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("building", object.getBuilding());
		if (object.getBuilding() != null) {
			properties.put("buildingId", object.getBuilding().getTableId());
		}
		properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
		properties.put("buildingUnit", object.getBuildingUnit());
//		properties.put("buildingUnitId", object.getBuildingUnit().getTableId());
		properties.put("eCodeOfBuildingUnit", object.geteCodeOfBuildingUnit());
		properties.put("eCodeFromRoom", object.geteCodeFromRoom());
		properties.put("actualDepositAmount", object.getActualDepositAmount());
		properties.put("depositAmountFromLoan", object.getDepositAmountFromLoan());
		properties.put("theAccountFromLoan", object.getTheAccountFromLoan());
		properties.put("fundProperty", object.getFundProperty());
		properties.put("theRatio", object.getTheRatio());
		properties.put("theAmount", MyDouble.getInstance().getShort(object.getTheAmount(), 2));
		properties.put("limitedRetainRight", MyDouble.getInstance().getShort(object.getLimitedRetainRight(), 2));
		properties.put("withdrawableRetainRight", MyDouble.getInstance().getShort(object.getWithdrawableRetainRight(), 2));
		properties.put("currentDividedAmout", object.getCurrentDividedAmout());
		properties.put("remark", object.getRemark());
		
		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_RemainRight object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("theState", object.getTheState());
		properties.put("busiState", object.getBusiState());
		properties.put("eCode", object.geteCode());
		properties.put("userStart", object.getUserStart());
		properties.put("userStartId", object.getUserStart().getTableId());
		properties.put("createTimeStamp", object.getCreateTimeStamp());
		properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
		properties.put("userRecord", object.getUserRecord());
		properties.put("userRecordId", object.getUserRecord().getTableId());
		properties.put("recordTimeStamp", object.getRecordTimeStamp());
		properties.put("enterTimeStamp", object.getEnterTimeStamp());
		properties.put("buyer", object.getBuyer());
		properties.put("theNameOfCreditor", object.getTheNameOfCreditor());
		properties.put("idNumberOfCreditor", object.getIdNumberOfCreditor());
		properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());
		properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());
		properties.put("srcBusiType", object.getSrcBusiType());
		properties.put("project", object.getProject());
		properties.put("projectId", object.getProject().getTableId());
		properties.put("theNameOfProject", object.getTheNameOfProject());
		properties.put("building", object.getBuilding());
		properties.put("buildingId", object.getBuilding().getTableId());
		properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
		properties.put("buildingUnit", object.getBuildingUnit());
		properties.put("buildingUnitId", object.getBuildingUnit().getTableId());
		properties.put("eCodeOfBuildingUnit", object.geteCodeOfBuildingUnit());
		properties.put("eCodeFromRoom", object.geteCodeFromRoom());
		properties.put("actualDepositAmount", object.getActualDepositAmount());
		properties.put("depositAmountFromLoan", object.getDepositAmountFromLoan());
		properties.put("theAccountFromLoan", object.getTheAccountFromLoan());
		properties.put("fundProperty", object.getFundProperty());
		properties.put("bank", object.getBank());
		properties.put("bankId", object.getBank().getTableId());
		properties.put("theNameOfBankPayedIn", object.getTheNameOfBankPayedIn());
		properties.put("theRatio", object.getTheRatio());
		properties.put("theAmount", object.getTheAmount());
		properties.put("limitedRetainRight", object.getLimitedRetainRight());
		properties.put("withdrawableRetainRight", object.getWithdrawableRetainRight());
		properties.put("currentDividedAmout", object.getCurrentDividedAmout());
		properties.put("remark", object.getRemark());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_RemainRight> tgpf_RemainRightList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_RemainRightList != null)
		{
			for(Tgpf_RemainRight object:tgpf_RemainRightList)
			{
				Properties properties = new MyProperties();
				
				properties.put("theState", object.getTheState());
				properties.put("busiState", object.getBusiState());
				properties.put("eCode", object.geteCode());
				properties.put("userStart", object.getUserStart());
				properties.put("userStartId", object.getUserStart().getTableId());
				properties.put("createTimeStamp", object.getCreateTimeStamp());
				properties.put("lastUpdateTimeStamp", object.getLastUpdateTimeStamp());
				properties.put("userRecord", object.getUserRecord());
				properties.put("userRecordId", object.getUserRecord().getTableId());
				properties.put("recordTimeStamp", object.getRecordTimeStamp());
				properties.put("enterTimeStamp", object.getEnterTimeStamp());
				properties.put("buyer", object.getBuyer());
				properties.put("theNameOfCreditor", object.getTheNameOfCreditor());
				properties.put("idNumberOfCreditor", object.getIdNumberOfCreditor());
				properties.put("eCodeOfContractRecord", object.geteCodeOfContractRecord());
				properties.put("eCodeOfTripleAgreement", object.geteCodeOfTripleAgreement());
				properties.put("srcBusiType", object.getSrcBusiType());
				properties.put("project", object.getProject());
				properties.put("projectId", object.getProject().getTableId());
				properties.put("theNameOfProject", object.getTheNameOfProject());
				properties.put("building", object.getBuilding());
				properties.put("buildingId", object.getBuilding().getTableId());
				properties.put("eCodeOfBuilding", object.geteCodeOfBuilding());
				properties.put("buildingUnit", object.getBuildingUnit());
				properties.put("buildingUnitId", object.getBuildingUnit().getTableId());
				properties.put("eCodeOfBuildingUnit", object.geteCodeOfBuildingUnit());
				properties.put("eCodeFromRoom", object.geteCodeFromRoom());
				properties.put("actualDepositAmount", object.getActualDepositAmount());
				properties.put("depositAmountFromLoan", object.getDepositAmountFromLoan());
				properties.put("theAccountFromLoan", object.getTheAccountFromLoan());
				properties.put("fundProperty", object.getFundProperty());
				properties.put("bank", object.getBank());
				properties.put("bankId", object.getBank().getTableId());
				properties.put("theNameOfBankPayedIn", object.getTheNameOfBankPayedIn());
				properties.put("theRatio", object.getTheRatio());
				properties.put("theAmount", object.getTheAmount());
				properties.put("limitedRetainRight", object.getLimitedRetainRight());
				properties.put("withdrawableRetainRight", object.getWithdrawableRetainRight());
				properties.put("currentDividedAmout", object.getCurrentDividedAmout());
				properties.put("remark", object.getRemark());
				
				list.add(properties);
			}
		}
		return list;
	}
	
	public List<Properties> executeCompare(List<Tgpf_RemainRight> oldPlatformList, List<Tgpf_RemainRight> oldUploadList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(oldPlatformList != null)
		{
			for(Tgpf_RemainRight oldPlatform:oldPlatformList)
			{
				Properties properties = getSimpleInfo(oldPlatform);
				properties.put("hasPlatformData", true);
				boolean hasUpload = false;
				for(Tgpf_RemainRight oldUpload:oldUploadList)
				{
					if (oldPlatform.geteCodeOfContractRecord() != null && oldPlatform.geteCodeOfContractRecord().equals(oldUpload.geteCodeOfContractRecord())) {
						hasUpload = true;
						oldUploadList.remove(oldUpload);
						properties.put("theAmount_Upload", MyDouble.getInstance().getShort(oldUpload.getTheAmount(), 2));
						properties.put("limitedRetainRight_Upload", MyDouble.getInstance().getShort(oldUpload.getLimitedRetainRight(), 2));
						properties.put("withdrawableRetainRight_Upload", MyDouble.getInstance().getShort(oldUpload.getWithdrawableRetainRight(), 2));
						properties.put("theAmount_Compare", MyDouble.getInstance().getShort(oldPlatform.getTheAmount() - oldUpload.getTheAmount(), 2));
						properties.put("limitedRetainRight_Compare", MyDouble.getInstance().getShort(oldPlatform.getLimitedRetainRight() - oldUpload.getLimitedRetainRight(), 2));
						properties.put("withdrawableRetainRight_Compare", MyDouble.getInstance().getShort(oldPlatform.getWithdrawableRetainRight() - oldUpload.getWithdrawableRetainRight(), 2));
						properties.put("hasUploadData", true);
						break;
					}
				}
				if (!hasUpload) {
					properties.put("theAmount_Upload", 0);
					properties.put("limitedRetainRight_Upload", 0);
					properties.put("withdrawableRetainRight_Upload", 0);
					properties.put("theAmount_Compare", 0);
					properties.put("limitedRetainRight_Compare", 0);
					properties.put("withdrawableRetainRight_Compare", 0);
//					properties.put("theAmount_Compare", MyDouble.getInstance().getShort(oldPlatform.getTheAmount(), 2));
//					properties.put("limitedRetainRight_Compare", MyDouble.getInstance().getShort(oldPlatform.getLimitedRetainRight(), 2));
//					properties.put("withdrawableRetainRight_Compare", MyDouble.getInstance().getShort(oldPlatform.getWithdrawableRetainRight(), 2));
					properties.put("hasUploadData", false);
				}
				list.add(properties);
			}
//			for(Tgpf_RemainRight oldUpload:oldUploadList)
//			{
//				Properties properties = getSimpleInfo(oldUpload);
//				properties.put("hasPlatformData", false);
//				properties.put("hasUploadData", true);
//				properties.put("theAmount", 0);
//				properties.put("limitedRetainRight", 0);
//				properties.put("withdrawableRetainRight", 0);
//				properties.put("theAmount_Upload", MyDouble.getInstance().getShort(oldUpload.getTheAmount(), 2));
//				properties.put("limitedRetainRight_Upload", MyDouble.getInstance().getShort(oldUpload.getLimitedRetainRight(), 2));
//				properties.put("withdrawableRetainRight_Upload", MyDouble.getInstance().getShort(oldUpload.getWithdrawableRetainRight(), 2));
//				properties.put("theAmount_Compare", MyDouble.getInstance().getShort(-oldUpload.getTheAmount(), 2));
//				properties.put("limitedRetainRight_Compare", MyDouble.getInstance().getShort(-oldUpload.getLimitedRetainRight(), 2));
//				properties.put("withdrawableRetainRight_Compare", MyDouble.getInstance().getShort(-oldUpload.getWithdrawableRetainRight(), 2));
//				list.add(properties);
//			}
		}
		return list;
	}
}

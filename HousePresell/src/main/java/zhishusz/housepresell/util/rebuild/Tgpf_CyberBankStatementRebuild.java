package zhishusz.housepresell.util.rebuild;

import java.util.ArrayList;
import java.util.Properties;
import org.springframework.stereotype.Service;
import java.util.List;

import zhishusz.housepresell.database.po.Tgpf_CyberBankStatement;
import zhishusz.housepresell.util.MyProperties;

/*
 * Rebuilder：网银对账-后台上传的账单原始Excel数据-主表
 * Company：ZhiShuSZ
 * */
@Service
public class Tgpf_CyberBankStatementRebuild extends RebuilderBase<Tgpf_CyberBankStatement>
{
	@Override
	public Properties getSimpleInfo(Tgpf_CyberBankStatement object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("uploadTimeStamp", object.getUploadTimeStamp());
		properties.put("theNameOfBank", object.getTheNameOfBank());
		properties.put("theNameOfBankBranch", object.getTheNameOfBankBranch());
		properties.put("theAccountOfBankAccountEscrowed", object.getTheAccountOfBankAccountEscrowed());
		properties.put("theNameOfBankAccountEscrowed", object.getTheNameOfBankAccountEscrowed());
		properties.put("transactionCount", object.getTransactionCount());
		properties.put("transactionAmount", object.getTransactionAmount());
		properties.put("fileUploadState", object.getFileUploadState());
		properties.put("billTimeStamp", object.getBillTimeStamp());

		return properties;
	}

	@Override
	public Properties getDetail(Tgpf_CyberBankStatement object)
	{
		if(object == null) return null;
		Properties properties = new MyProperties();
		
		properties.put("tableId", object.getTableId());
		properties.put("theNameOfBank", object.getTheNameOfBank());
		properties.put("theNameOfBankBranch", object.getTheNameOfBankBranch());
		properties.put("theAccountOfBankAccountEscrowed", object.getTheAccountOfBankAccountEscrowed());
		properties.put("theNameOfBankAccountEscrowed", object.getTheNameOfBankAccountEscrowed());
		properties.put("fileUploadState", object.getFileUploadState());
		properties.put("uploadTimeStamp", object.getUploadTimeStamp());
		properties.put("billTimeStamp", object.getBillTimeStamp());
		
		return properties;
	}
	
	@SuppressWarnings("rawtypes")
	public List getDetailForAdmin(List<Tgpf_CyberBankStatement> tgpf_CyberBankStatementList)
	{
		List<Properties> list = new ArrayList<Properties>();
		if(tgpf_CyberBankStatementList != null)
		{
			for(Tgpf_CyberBankStatement object:tgpf_CyberBankStatementList)
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
				properties.put("theNameOfBank", object.getTheNameOfBank());
				properties.put("theAccountOfBankAccountEscrowed", object.getTheAccountOfBankAccountEscrowed());
				properties.put("theNameOfBankAccountEscrowed", object.getTheNameOfBankAccountEscrowed());
				properties.put("theNameOfBankBranch", object.getTheNameOfBankBranch());
				properties.put("reconciliationState", object.getReconciliationState());
				properties.put("reconciliationUser", object.getReconciliationUser());
				properties.put("orgFilePath", object.getOrgFilePath());
				
				list.add(properties);
			}
		}
		return list;
	}
}

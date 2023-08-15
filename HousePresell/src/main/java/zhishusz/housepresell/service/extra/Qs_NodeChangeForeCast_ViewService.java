package zhishusz.housepresell.service.extra;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import zhishusz.housepresell.controller.form.extra.Qs_RecordAmount_ViewForm;
import zhishusz.housepresell.database.dao.Qs_NodeChangeForeCast_ViewDao;
import zhishusz.housepresell.database.po.extra.Qs_NodeChangeForeCast_View;
import zhishusz.housepresell.database.po.state.S_NormalFlag;
import zhishusz.housepresell.exportexcelvo.Qs_NodeChangeForeCast_ViewExportExcelVO;
import zhishusz.housepresell.util.Checker;
import zhishusz.housepresell.util.DirectoryUtil;
import zhishusz.housepresell.util.MyBackInfo;
import zhishusz.housepresell.util.MyDatetime;
import zhishusz.housepresell.util.MyDouble;
import zhishusz.housepresell.util.MyProperties;

/*
 * Service添加操作：节点变更预测
 * Company：ZhiShuSZ
 */
@Service
public class Qs_NodeChangeForeCast_ViewService
{
    
    private static final String excelName = "节点变更预测";
    private MyDouble dplan = MyDouble.getInstance();
	@Autowired
	private Qs_NodeChangeForeCast_ViewDao qs_NodeChangeForeCast_ViewDao;


	public Properties execute(Qs_RecordAmount_ViewForm model)
	{
		Properties properties = new MyProperties();

		Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
		Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
		
		String billTimeStamp = model.getBillTimeStamp();
		String endBillTimeStamp = model.getEndBillTimeStamp();
		if(StrUtil.isBlank(billTimeStamp) && StrUtil.isBlank(endBillTimeStamp)){
		    return MyBackInfo.fail(properties, "请选择预测日期！");
		} 
		
		List<Qs_NodeChangeForeCast_View> qs_RecordAmount_ViewList = new ArrayList<>();
		
		try {
            qs_RecordAmount_ViewList = qs_NodeChangeForeCast_ViewDao.getNodeChangeForeCast(billTimeStamp, 1, 20);
        } catch (SQLException e) {
            
        }
		
		Integer totalCount = 0;
		Integer totalPage = 1;
		if(!qs_RecordAmount_ViewList.isEmpty()){
		    
		    totalCount = qs_RecordAmount_ViewList.size();
		    totalPage = totalCount / countPerPage + 1;
	        Integer indexPage = ( pageNumber - 1 ) * countPerPage;
	        Integer endPage = pageNumber * countPerPage ;
	        if(totalCount <= endPage){
	            endPage = totalCount;
	        }
		    
	        properties.put("Qs_NodeChangeForeCast_ViewList", qs_RecordAmount_ViewList.subList(indexPage, endPage));
		}else{
		    properties.put("Qs_NodeChangeForeCast_ViewList", qs_RecordAmount_ViewList);
		}
		
		properties.put(S_NormalFlag.totalPage, totalPage);
		properties.put(S_NormalFlag.pageNumber, pageNumber);
		properties.put(S_NormalFlag.countPerPage, countPerPage);
		properties.put(S_NormalFlag.totalCount, totalCount);

		properties.put(S_NormalFlag.result, S_NormalFlag.success);
		properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

		return properties;
	}
	
	@SuppressWarnings("unchecked")
    public Properties executeCopy(Qs_RecordAmount_ViewForm model)
    {
        Properties properties = new MyProperties();

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
        // 关键字
        String keyword = model.getKeyword();
    
        /*String forecastcompletedate = model.getFORECASTCOMPLETEDATE();
        if(StrUtil.isBlank(forecastcompletedate)){
            
        }*/
        
        String billTimeStamp = model.getBillTimeStamp();
        String endBillTimeStamp = model.getEndBillTimeStamp();
        if(StrUtil.isBlank(billTimeStamp) && StrUtil.isBlank(endBillTimeStamp)){
            return MyBackInfo.fail(properties, "请选择预测日期！");
        } 
        
        if (keyword != null && keyword.trim().length() >= 1)
        {
            model.setKeyword("%" + keyword + "%");
        }
        else
        {
            model.setKeyword(null);
        }

        Integer totalCount = qs_NodeChangeForeCast_ViewDao
                .findByPage_Size(qs_NodeChangeForeCast_ViewDao.getQuery_Size(qs_NodeChangeForeCast_ViewDao.getBasicHQL(), model));

        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Qs_NodeChangeForeCast_View> qs_RecordAmount_ViewList;
        if (totalCount > 0)
        {
            qs_RecordAmount_ViewList = qs_NodeChangeForeCast_ViewDao.findByPage(
                qs_NodeChangeForeCast_ViewDao.getQuery(qs_NodeChangeForeCast_ViewDao.getBasicHQL(), model), pageNumber,
                    countPerPage);
            
            Double countAmountDou =  MyDouble.getInstance().parse(qs_NodeChangeForeCast_ViewDao.findByPage_DoubleSum(qs_NodeChangeForeCast_ViewDao.getQuery_Sum(qs_NodeChangeForeCast_ViewDao.getBasicHQL(), "APPAMOUNT", model)));
            
            Qs_NodeChangeForeCast_View nodeChangeForeCast_View = new Qs_NodeChangeForeCast_View();
            nodeChangeForeCast_View.setTABLEID(0L);
            nodeChangeForeCast_View.setCOMMPANYNAME("合计");
            nodeChangeForeCast_View.setAPPAMOUNT(new BigDecimal(countAmountDou));
            
            qs_RecordAmount_ViewList.add(nodeChangeForeCast_View);
            
        }
        else
        {
            qs_RecordAmount_ViewList = new ArrayList<Qs_NodeChangeForeCast_View>();
        }

        properties.put("Qs_NodeChangeForeCast_ViewList", qs_RecordAmount_ViewList);
        properties.put(S_NormalFlag.keyword, keyword);
        properties.put(S_NormalFlag.totalPage, totalPage);
        properties.put(S_NormalFlag.pageNumber, pageNumber);
        properties.put(S_NormalFlag.countPerPage, countPerPage);
        properties.put(S_NormalFlag.totalCount, totalCount);

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
	
	@SuppressWarnings("unchecked")
    public Properties exportExecute(Qs_RecordAmount_ViewForm model)
    {
        Properties properties = new MyProperties();

        Integer pageNumber = Checker.getInstance().checkPageNumber(model.getPageNumber());
        Integer countPerPage = Checker.getInstance().checkCountPerPage(model.getCountPerPage());
        // 关键字
        String keyword = model.getKeyword();
    
        /*String forecastcompletedate = model.getFORECASTCOMPLETEDATE();
        if(StrUtil.isBlank(forecastcompletedate)){
            
        }*/
        
        String billTimeStamp = model.getBillTimeStamp();
        String endBillTimeStamp = model.getEndBillTimeStamp();
        if(StrUtil.isBlank(billTimeStamp) && StrUtil.isBlank(endBillTimeStamp)){
            return MyBackInfo.fail(properties, "请选择预测日期！");
        } 
        
        if (keyword != null && keyword.trim().length() >= 1)
        {
            model.setKeyword("%" + keyword + "%");
        }
        else
        {
            model.setKeyword(null);
        }
        
        List<Qs_NodeChangeForeCast_View> qs_RecordAmount_ViewList = new ArrayList<>();
        
        try {
            qs_RecordAmount_ViewList = qs_NodeChangeForeCast_ViewDao.getNodeChangeForeCast(billTimeStamp, 1, 20);
        } catch (SQLException e) {
            
        }
        
        if(!qs_RecordAmount_ViewList.isEmpty()){
         // 初始化文件保存路径，创建相应文件夹
            DirectoryUtil directoryUtil = new DirectoryUtil();
            String relativeDir = directoryUtil.createRelativePathWithDate("Qs_NodeChangeForeCast_View");
            String localPath = directoryUtil.getProjectRoot();
            String saveDirectory = localPath + relativeDir;

            if (saveDirectory.contains("%20")) {
                saveDirectory = saveDirectory.replace("%20", " ");
            }

            directoryUtil.mkdir(saveDirectory);

            String strNewFileName = excelName + "-"
                    + MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";

            String saveFilePath = saveDirectory + strNewFileName;

            // 通过工具类创建writer
            ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);

            // 自定义字段别名
            writer.addHeaderAlias("ordinal", "序号");
            writer.addHeaderAlias("COMMPANYNAME", "开发企业");
            writer.addHeaderAlias("PROJECTNAME", "项目");
            writer.addHeaderAlias("BUILDCODE", "施工楼幢");
            writer.addHeaderAlias("CURRENTESCROWFUND", "当前托管余额（元）");
            writer.addHeaderAlias("ORGLIMITEDAMOUNT", "初始受限额度（元）");
            writer.addHeaderAlias("CASHLIMITEDAMOUNT", "现金受限额度（元）");
            writer.addHeaderAlias("CURRENTFIGUREPROGRESS", "当前形象进度");
            writer.addHeaderAlias("CURRENTLIMITEDRATIO", "当前受限比例（%）");
            writer.addHeaderAlias("NODELIMITEDAMOUNT", "当前节点受限额度（元）");
            writer.addHeaderAlias("EFFECTIVELIMITEDAMOUNT", "当前有效受限额度（元）");
            /*writer.addHeaderAlias("FORECASTCOMPLETEDATE", "预测日期");*/
            writer.addHeaderAlias("FORECASTNODENAME", "预测形象进度");
            writer.addHeaderAlias("LIMITEDAMOUNT", "预测受限比例（元）");
            writer.addHeaderAlias("NODELIMITAMOUNT", "预测节点受限金额（元）");
            writer.addHeaderAlias("EFFLIMITAMOUNT", "预测有效受限额度（元）");
            writer.addHeaderAlias("APPAMOUNT", "需拨付金额（元）");

            List<Qs_NodeChangeForeCast_ViewExportExcelVO> qs_NodeChangeForeCast_ViewExportExcelVOList = formart(qs_RecordAmount_ViewList);

            // 一次性写出内容，使用默认样式
            writer.write(qs_NodeChangeForeCast_ViewExportExcelVOList);

            // 关闭writer，释放内存
            writer.flush();

            writer.autoSizeColumn(0, true);
            writer.autoSizeColumn(1, true);
            writer.autoSizeColumn(2, true);
            writer.autoSizeColumn(3, true);
            writer.autoSizeColumn(4, true);
            writer.autoSizeColumn(5, true);
            writer.autoSizeColumn(6, true);
            writer.autoSizeColumn(7, true);
            writer.autoSizeColumn(8, true);
            writer.autoSizeColumn(9, true);
            writer.autoSizeColumn(10, true);
            writer.autoSizeColumn(11, true);
            writer.autoSizeColumn(12, true);
            writer.autoSizeColumn(13, true);
            writer.autoSizeColumn(14, true);
            writer.autoSizeColumn(15, true);
            /*writer.autoSizeColumn(16, true);*/

            writer.close();

            properties.put("fileName", strNewFileName);
            properties.put("fileURL", relativeDir + strNewFileName);
            properties.put("fullFileName", saveFilePath);
        }else{
            return MyBackInfo.fail(properties, "未查询到有效数据！");
        }

        /*Integer totalCount = qs_NodeChangeForeCast_ViewDao
                .findByPage_Size(qs_NodeChangeForeCast_ViewDao.getQuery_Size(qs_NodeChangeForeCast_ViewDao.getBasicHQL(), model));

        Integer totalPage = totalCount / countPerPage;
        if (totalCount % countPerPage > 0)
            totalPage++;
        if (pageNumber > totalPage && totalPage != 0)
            pageNumber = totalPage;

        List<Qs_NodeChangeForeCast_View> qs_RecordAmount_ViewList;
        if (totalCount > 0)
        {
            qs_RecordAmount_ViewList = qs_NodeChangeForeCast_ViewDao.findByPage(
                qs_NodeChangeForeCast_ViewDao.getQuery(qs_NodeChangeForeCast_ViewDao.getBasicHQL(), model), pageNumber,
                    countPerPage);
            
            Double countAmountDou =  MyDouble.getInstance().parse(qs_NodeChangeForeCast_ViewDao.findByPage_DoubleSum(qs_NodeChangeForeCast_ViewDao.getQuery_Sum(qs_NodeChangeForeCast_ViewDao.getBasicHQL(), "APPAMOUNT", model)));
            
            Qs_NodeChangeForeCast_View nodeChangeForeCast_View = new Qs_NodeChangeForeCast_View();
            nodeChangeForeCast_View.setTABLEID(0L);
            nodeChangeForeCast_View.setCOMMPANYNAME("合计");
            nodeChangeForeCast_View.setAPPAMOUNT(new BigDecimal(countAmountDou));
            
            qs_RecordAmount_ViewList.add(nodeChangeForeCast_View);
            
            // 初始化文件保存路径，创建相应文件夹
            DirectoryUtil directoryUtil = new DirectoryUtil();
            String relativeDir = directoryUtil.createRelativePathWithDate("Qs_NodeChangeForeCast_View");
            String localPath = directoryUtil.getProjectRoot();
            String saveDirectory = localPath + relativeDir;

            if (saveDirectory.contains("%20")) {
                saveDirectory = saveDirectory.replace("%20", " ");
            }

            directoryUtil.mkdir(saveDirectory);

            String strNewFileName = excelName + "-"
                    + MyDatetime.getInstance().dateToString(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".xlsx";

            String saveFilePath = saveDirectory + strNewFileName;

            // 通过工具类创建writer
            ExcelWriter writer = ExcelUtil.getWriter(saveFilePath);

            // 自定义字段别名
            writer.addHeaderAlias("ordinal", "序号");
            writer.addHeaderAlias("COMMPANYNAME", "开发企业");
            writer.addHeaderAlias("PROJECTNAME", "项目");
            writer.addHeaderAlias("BUILDCODE", "施工楼幢");
            writer.addHeaderAlias("CURRENTESCROWFUND", "当前托管余额（元）");
            writer.addHeaderAlias("ORGLIMITEDAMOUNT", "初始受限额度（元）");
            writer.addHeaderAlias("CASHLIMITEDAMOUNT", "现金受限额度（元）");
            writer.addHeaderAlias("CURRENTFIGUREPROGRESS", "当前形象进度");
            writer.addHeaderAlias("CURRENTLIMITEDRATIO", "当前受限比例（%）");
            writer.addHeaderAlias("NODELIMITEDAMOUNT", "当前节点受限额度（元）");
            writer.addHeaderAlias("EFFECTIVELIMITEDAMOUNT", "当前有效受限额度（元）");
            writer.addHeaderAlias("FORECASTCOMPLETEDATE", "预测日期");
            writer.addHeaderAlias("FORECASTNODENAME", "预测形象进度");
            writer.addHeaderAlias("LIMITEDAMOUNT", "预测受限比例（元）");
            writer.addHeaderAlias("NODELIMITAMOUNT", "预测节点受限金额（元）");
            writer.addHeaderAlias("EFFLIMITAMOUNT", "预测有效受限额度（元）");
            writer.addHeaderAlias("APPAMOUNT", "需拨付金额（元）");

            List<Qs_NodeChangeForeCast_ViewExportExcelVO> qs_NodeChangeForeCast_ViewExportExcelVOList = formart(qs_RecordAmount_ViewList);

            // 一次性写出内容，使用默认样式
            writer.write(qs_NodeChangeForeCast_ViewExportExcelVOList);

            // 关闭writer，释放内存
            writer.flush();

            writer.autoSizeColumn(0, true);
            writer.autoSizeColumn(1, true);
            writer.autoSizeColumn(2, true);
            writer.autoSizeColumn(3, true);
            writer.autoSizeColumn(4, true);
            writer.autoSizeColumn(5, true);
            writer.autoSizeColumn(6, true);
            writer.autoSizeColumn(7, true);
            writer.autoSizeColumn(8, true);
            writer.autoSizeColumn(9, true);
            writer.autoSizeColumn(10, true);
            writer.autoSizeColumn(11, true);
            writer.autoSizeColumn(12, true);
            writer.autoSizeColumn(13, true);
            writer.autoSizeColumn(14, true);
            writer.autoSizeColumn(15, true);
            writer.autoSizeColumn(16, true);

            writer.close();

            properties.put("fileName", strNewFileName);
            properties.put("fileURL", relativeDir + strNewFileName);
            properties.put("fullFileName", saveFilePath);
            
        }
        else
        {
            return MyBackInfo.fail(properties, "未查询到有效数据！");
        }*/

        properties.put(S_NormalFlag.result, S_NormalFlag.success);
        properties.put(S_NormalFlag.info, S_NormalFlag.info_Success);

        return properties;
    }
	
	@SuppressWarnings("static-access")
    List<Qs_NodeChangeForeCast_ViewExportExcelVO> formart(
        List<Qs_NodeChangeForeCast_View> qs_RecordAmount_ViewList) {
    List<Qs_NodeChangeForeCast_ViewExportExcelVO> list = new ArrayList<Qs_NodeChangeForeCast_ViewExportExcelVO>();
    int ordinal = 0;
    for (Qs_NodeChangeForeCast_View po : qs_RecordAmount_ViewList) {
        ++ordinal;
        Qs_NodeChangeForeCast_ViewExportExcelVO vo = new Qs_NodeChangeForeCast_ViewExportExcelVO();
        vo.setOrdinal(ordinal);
        vo.setCOMMPANYNAME(null == po.getCOMMPANYNAME() ? "" : po.getCOMMPANYNAME());
        vo.setPROJECTNAME(null == po.getPROJECTNAME() ? "" : po.getPROJECTNAME());
        vo.setBUILDCODE(null == po.getBUILDCODE() ? "" : po.getBUILDCODE());
        vo.setCURRENTESCROWFUND(null == po.getCURRENTESCROWFUND() ? "" : dplan.pointTOThousandths(po.getCURRENTESCROWFUND().doubleValue()));
        vo.setORGLIMITEDAMOUNT(null == po.getORGLIMITEDAMOUNT() ? "" : dplan.pointTOThousandths(po.getORGLIMITEDAMOUNT().doubleValue()));
        vo.setCASHLIMITEDAMOUNT(null == po.getCASHLIMITEDAMOUNT() ? "" : dplan.pointTOThousandths(po.getCASHLIMITEDAMOUNT().doubleValue()));
        vo.setCURRENTFIGUREPROGRESS(null == po.getCURRENTFIGUREPROGRESS() ? "" : po.getCURRENTFIGUREPROGRESS());
        vo.setCURRENTLIMITEDRATIO(null == po.getCURRENTLIMITEDRATIO() ? "" : po.getCURRENTLIMITEDRATIO().toString());
        vo.setNODELIMITEDAMOUNT(null == po.getNODELIMITEDAMOUNT() ? "" : dplan.pointTOThousandths(po.getNODELIMITEDAMOUNT().doubleValue()));
        vo.setEFFECTIVELIMITEDAMOUNT(null == po.getEFFECTIVELIMITEDAMOUNT() ? "" : dplan.pointTOThousandths(po.getEFFECTIVELIMITEDAMOUNT().doubleValue()));
        /*vo.setFORECASTCOMPLETEDATE(null == po.getFORECASTCOMPLETEDATE() ? "" : po.getFORECASTCOMPLETEDATE());*/
        vo.setFORECASTNODENAME(null == po.getFORECASTNODENAME() ? "" : po.getFORECASTNODENAME());
        vo.setLIMITEDAMOUNT(null == po.getLIMITEDAMOUNT() ? "" : po.getLIMITEDAMOUNT().toString());
        vo.setNODELIMITAMOUNT(null == po.getNODELIMITAMOUNT() ? "" : dplan.pointTOThousandths(po.getNODELIMITAMOUNT().doubleValue()));
        vo.setEFFLIMITAMOUNT(null == po.getEFFLIMITAMOUNT() ? "" : dplan.pointTOThousandths(po.getEFFLIMITAMOUNT().doubleValue()));
        vo.setAPPAMOUNT(null == po.getAPPAMOUNT() ? "" : dplan.pointTOThousandths(po.getAPPAMOUNT().doubleValue()) );
        list.add(vo);

    }

    return list;
}
}

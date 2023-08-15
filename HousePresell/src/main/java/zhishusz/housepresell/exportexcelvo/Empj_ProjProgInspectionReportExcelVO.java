package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * 工程进度巡查
 * 
 * @author xsz
 * @since 2020-9-1 15:19:25
 */
@Getter
@Setter
@ITypeAnnotation(remark = "工程进度巡查-主")
public class Empj_ProjProgInspectionReportExcelVO {
    
    /**
     * 序号
     */
    private Integer ordinal;

    /**
     * 巡查机构
     */
    private String companyName;

    /**
     * 区域
     */
    private String areaName;

    /**
     * 项目
     */
    private String projectName;

    /**
     * 楼幢数
     */
    private String buildCount;

    /**
     * 照片上传日期
     */
    private String approvalDate;

    /**
     * 巡查单号
     */
    private String code;
    
    /**
     * 巡查日期
     */
    private String recordTimeStamp;

}

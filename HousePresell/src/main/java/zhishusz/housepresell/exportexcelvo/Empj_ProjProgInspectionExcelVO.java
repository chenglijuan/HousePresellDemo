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
public class Empj_ProjProgInspectionExcelVO {
    
    /**
     * 序号
     */
    private Integer ordinal;
    
    /**
     * 巡查单号
     */
    private String code;
    
    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 项目名称
     */
    private String projectName;
    
    /**
     * 楼幢
     */
    private String buildName;
    
    /**
     * 当前进度节点
     */
//    private String nowNodeName;
    
    /**
     * 地上层数
     */
    private Double floorUpNumber;
    
    /**
     * 当前建设进度
     */
    private String buildProgress;
    
    /**
     * 巡查时间
     */
    private String forcastTime;
    
    /**
     * 巡查人
     */
//    private String forcastPeople;

}

package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * 项目进度巡查-主
 * 
 * @author xsz
 * @since 2020-9-1 15:19:25
 */
@Getter
@Setter
@ITypeAnnotation(remark = "项目进度巡查-主")
public class Empj_ProjProgInspection_AFExcleVO {
    
    private Integer ordinal;

    private String areaName;

    private String projectName;

    private String buildCode;

    private String deliveryType;

    private String buildProgress;

    private String nowNodeName;
    
    private String forecastCompleteDate;
    
    private String determine;
    
    private String dataSources;

    private String updateDateTime;

}

package zhishusz.housepresell.exportexcelvo;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

/**
 * 统计报表-节点变更预测表
 * 
 * @ClassName: Qs_NodeChangeForeCast_View
 * @Description:TODO
 * @author: xushizhong
 * @date: 2018年9月6日 上午11:50:30
 * @version V1.0
 *
 */

@ITypeAnnotation(remark = "节点变更预测表")
public class Qs_NodeChangeForeCast_ViewExportExcelVO{


    @Getter @Setter
    private Integer ordinal;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "开发企业")
    private String COMMPANYNAME;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "项目")
    private String PROJECTNAME;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "施工楼幢")
    private String BUILDCODE;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "托管余额")
    private String CURRENTESCROWFUND;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "初始受限额度")
    private String ORGLIMITEDAMOUNT;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "现金受限额度")
    private String CASHLIMITEDAMOUNT;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前形象进度")
    private String CURRENTFIGUREPROGRESS;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前受限比例")
    private String CURRENTLIMITEDRATIO;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前节点受限额度")
    private String NODELIMITEDAMOUNT;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前有效受限额度")
    private String EFFECTIVELIMITEDAMOUNT;
    
    /*@Getter
    @Setter
    @IFieldAnnotation(remark = "预测完成日期")
    private String FORECASTCOMPLETEDATE;*/

    @Getter
    @Setter
    @IFieldAnnotation(remark = "预测形象进度")
    private String FORECASTNODENAME;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "预测受限比例")
    private String LIMITEDAMOUNT;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "预测节点受限金额")
    private String NODELIMITAMOUNT;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "预测有效受限额度")
    private String EFFLIMITAMOUNT;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "需拨付金额")
    private String APPAMOUNT;

}

package zhishusz.housepresell.database.po.extra;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class Qs_NodeChangeForeCast_View implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7257452368193224081L;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "表ID")
    private Long TABLEID;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "预测完成日期")
    private String FORECASTCOMPLETEDATE;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "开发企业")
    private String COMMPANYNAME;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "企业ID")
    private Long COMMPANYID;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "项目")
    private String PROJECTNAME;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "项目ID")
    private Long PROJECTID;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "施工楼幢")
    private String BUILDCODE;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "托管余额")
    private BigDecimal CURRENTESCROWFUND;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "初始受限额度")
    private BigDecimal ORGLIMITEDAMOUNT;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "现金受限额度")
    private BigDecimal CASHLIMITEDAMOUNT;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前形象进度")
    private String CURRENTFIGUREPROGRESS;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前受限比例")
    private BigDecimal CURRENTLIMITEDRATIO;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前节点受限额度")
    private BigDecimal NODELIMITEDAMOUNT;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "当前有效受限额度")
    private BigDecimal EFFECTIVELIMITEDAMOUNT;

    @Getter
    @Setter
    @IFieldAnnotation(remark = "预测形象进度")
    private String FORECASTNODENAME;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "预测受限比例")
    private BigDecimal LIMITEDAMOUNT;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "预测节点受限金额")
    private BigDecimal NODELIMITAMOUNT;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "预测有效受限额度")
    private BigDecimal EFFLIMITAMOUNT;
    
    @Getter
    @Setter
    @IFieldAnnotation(remark = "需拨付金额")
    private BigDecimal APPAMOUNT;

}

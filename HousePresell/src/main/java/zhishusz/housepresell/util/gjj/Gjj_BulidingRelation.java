package zhishusz.housepresell.util.gjj;

import lombok.Getter;
import lombok.Setter;
import zhishusz.housepresell.util.IFieldAnnotation;
import zhishusz.housepresell.util.ITypeAnnotation;

import java.io.Serializable;

/**
 * @Author: chenglijuan
 * @Data: 2021/8/25  16:17
 * @Decription:
 * @Modified:
 */
@ITypeAnnotation(remark="托管楼栋与公积金楼栋id关系表")
public class Gjj_BulidingRelation implements Serializable {

    @Getter
    @Setter
    @IFieldAnnotation(remark="表id",isPrimarykey = true)
    private Long tableId;

    @Getter@Setter
    @IFieldAnnotation(remark="托管楼栋id")
    private String empjBuildingId;

    @Getter@Setter
    @IFieldAnnotation(remark="公积金楼栋id")
    private String gjjBuildingId;

    @Override
    public String toString() {
        return "Gjj_BulidingRelation{" +
                "tableId=" + tableId +
                ", empjBuildingId='" + empjBuildingId + '\'' +
                ", gjjBuildingId='" + gjjBuildingId + '\'' +
                '}';
    }
}

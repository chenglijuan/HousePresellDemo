package zhishusz.housepresell.dingding.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description 攀云钉钉保存图片接口对象
 * @Author jxx
 * @Date 2020/9/28 15:43
 * @Version
 **/
public class PanYunDingDingSavePhotoUrlsEntity implements Serializable {

    /**
     * 任务ID
     */
    @JSONField(name = "TaskID", ordinal = 0)
    private String taskId;

    public String getSupervisorCode() {
        return supervisorCode;
    }

    public void setSupervisorCode(String supervisorCode) {
        this.supervisorCode = supervisorCode;
    }

    /**
     * 监理名称
     */
    @JSONField(name = "supervisorCode", ordinal = 1)
    private String supervisorCode;

    /**
     * 子元素
     */
    @JSONField(name = "imageInfos", ordinal = 2)
    private List<PanYunDingDingSavePhotoUrlsSonEntity> entities;
    
    /**
     * 监理名称
     */
    @JSONField(name = "supervisorName", ordinal = 3)
    private String supervisorName;
    
    
    /**
     * 回传时间
     */
    @JSONField(name = "ReturnTime", ordinal = 4)
    private String returnTime;
    
    /**
     * 任务分配时间
     */
    @JSONField(name = "AssignTasksTime", ordinal = 5)
    private String assignTasksTime;
    

    public String getSupervisorName() {
        return supervisorName;
    }

    public void setSupervisorName(String supervisorName) {
        this.supervisorName = supervisorName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<PanYunDingDingSavePhotoUrlsSonEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<PanYunDingDingSavePhotoUrlsSonEntity> entities) {
        this.entities = entities;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getAssignTasksTime() {
        return assignTasksTime;
    }

    public void setAssignTasksTime(String assignTasksTime) {
        this.assignTasksTime = assignTasksTime;
    }
    
    
}

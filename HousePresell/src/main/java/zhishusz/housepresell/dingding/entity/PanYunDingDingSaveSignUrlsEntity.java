package zhishusz.housepresell.dingding.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description 攀云钉钉保存现场签到时间接口对象
 * @Author jxx
 * @Date 2020/9/28 15:43
 * @Version
 **/
public class PanYunDingDingSaveSignUrlsEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7386968026953633660L;

    /**
     * 任务ID
     */
    @JSONField(name = "TaskID", ordinal = 0)
    private String taskId;

    /**
     * 监理编号
     */
    @JSONField(name = "SupervisorCode", ordinal = 1)
    private String supervisorCode;
    
    /**
     * 现场签到时间
     */
    @JSONField(name = "SignTime", ordinal = 2)
    private String signTime;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSupervisorCode() {
        return supervisorCode;
    }

    public void setSupervisorCode(String supervisorCode) {
        this.supervisorCode = supervisorCode;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }
    
    

    

    
    
}

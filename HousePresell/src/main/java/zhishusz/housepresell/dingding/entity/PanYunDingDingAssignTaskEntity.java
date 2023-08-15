package zhishusz.housepresell.dingding.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description 攀云钉钉任务分派对象
 * @Author jxx
 * @Date 2020/9/28 20:44
 * @Version
 **/
public class PanYunDingDingAssignTaskEntity implements Serializable {
    /**
     * 任务ID
     */
    @JSONField(name = "TaskID", ordinal = 0)
    private String taskId;

    /**
     * 创建人电话
     */
    @JSONField(name = "CreateUserPhone", ordinal = 1)
    private String createUserPhone;

    /**
     * 项目code
     */
    @JSONField(name = "ProjectCode", ordinal = 2)
    private String projectCode;

    /**
     * 监理人公司CODE
     */
    @JSONField(name = "SupervisionCompCode", ordinal = 3)
    private String supervisorCompCode;

    /**
     * 变更节点
     */
    @JSONField(name = "Appointment", ordinal = 4)
    private String appointment;

    /**
     * 楼幢信息
     */
    @JSONField(name = "Builds", ordinal = 5)
    private List<BuildingInfo> builds;
    
    /**
    * 联系人A
    */
   @JSONField(name = "ContactOne", ordinal = 6)
   private String contactOne;
    
    /**
    * 联系方式A
    */
   @JSONField(name = "TelephoneOne", ordinal = 7)
   private String telephoneOne;
   
   /**
    * 联系人B
    */
   @JSONField(name = "ContactTwo", ordinal = 8)
   private String contactTwo;
    
    /**
    * 联系方式B
    */
   @JSONField(name = "TelephoneTwo", ordinal = 9)
   private String telephoneTwo;
   
   /**
    * 项目地址
    */
   @JSONField(name = "ProjAddr", ordinal = 10)
   private String projAddr;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCreateUserPhone() {
        return createUserPhone;
    }

    public void setCreateUserPhone(String createUserPhone) {
        this.createUserPhone = createUserPhone;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getSupervisorCompCode() {
        return supervisorCompCode;
    }

    public void setSupervisorCompCode(String supervisorCompCode) {
        this.supervisorCompCode = supervisorCompCode;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }
    

    public String getContactOne() {
        return contactOne;
    }

    public void setContactOne(String contactOne) {
        this.contactOne = contactOne;
    }

    public String getTelephoneOne() {
        return telephoneOne;
    }

    public void setTelephoneOne(String telephoneOne) {
        this.telephoneOne = telephoneOne;
    }

    public String getContactTwo() {
        return contactTwo;
    }

    public void setContactTwo(String contactTwo) {
        this.contactTwo = contactTwo;
    }

    public String getTelephoneTwo() {
        return telephoneTwo;
    }

    public void setTelephoneTwo(String telephoneTwo) {
        this.telephoneTwo = telephoneTwo;
    }

    public List<BuildingInfo> getBuilds() {
        return builds;
    }

    public void setBuilds(List<BuildingInfo> builds) {
        this.builds = builds;
    }
    
    public String getProjAddr() {
        return projAddr;
    }

    public void setProjAddr(String projAddr) {
        this.projAddr = projAddr;
    }



    /**
     * @Description 攀云钉钉楼幢数据
     * @Author jxx
     * @Date 2020/9/28 20:44
     * @Version
     **/
    public static class BuildingInfo implements Serializable {
        /**
         * 楼幢CODE
         */
        @JSONField(name = "BuildingCode", ordinal = 0)
        private String buildingCode;

        /**
         * 层数
         */
        @JSONField(name = "floor", ordinal = 1)
        private String floor;

        /**
         * 变更节点
         */
        @JSONField(name = "ChangeNode", ordinal = 2)
        private String changeNode;

        public String getBuildingCode() {
            return buildingCode;
        }

        public void setBuildingCode(String buildingCode) {
            this.buildingCode = buildingCode;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public String getChangeNode() {
            return changeNode;
        }

        public void setChangeNode(String changeNode) {
            this.changeNode = changeNode;
        }
    }
}

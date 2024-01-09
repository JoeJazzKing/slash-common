package com.heycine.slash.common.activity.demo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhiji.zhou
 * 
 * @since 2021-10-08
 */
@Data
@ApiModel("申诉流程定义")
public class ProcessDefDTO implements Serializable {

    @ApiModelProperty(value = "定义的流程Key")
    private String defProcessKey;
    @ApiModelProperty(value = "定义的流程名称")
    private String defProcessName;
    @ApiModelProperty(value = "定义的审核Key")
    private String defAuditKey;
    @ApiModelProperty(value = "定义的审核名称")
    private String defAuditName;

    @ApiModelProperty(value = "审核人员部门/角色")
    private String[] defAuditRole;
    @ApiModelProperty(value = "审核人员岗位")
    private String[] defAuditPosition;

    @ApiModelProperty(value = "逻辑设置")
    private List<LogicConfig> defLogicConfig;

    @ApiModelProperty(value = "审核选项")
    private List<AuditOption> auditOption;

    @Data
    public static class LogicConfig implements Serializable {

        @ApiModelProperty(value = "事件名称")
        private String defEventName;

        @ApiModelProperty(value = "事件Key")
        private String defEventKey;

        @ApiModelProperty(value = "事件流程名称")
        private String defEventProcessName;

        @ApiModelProperty(value = "事件流程Key")
        private String defEventProcessKey;

        @ApiModelProperty(value = "事件结果（申诉成功，申诉失败，申诉完成，-）")
        private String defEventResult;

        public void setDefEventKey(String defEventKey) {
            this.defEventKey = replaceChar(defEventKey);
        }

        public void setDefEventProcessKey(String defEventProcessKey) {
            this.defEventProcessKey = replaceChar(defEventProcessKey);
        }
    }

    @Data
    public static class AuditOption implements Serializable {

        @ApiModelProperty(value = "事件名称")
        private String defEventName;

        @ApiModelProperty(value = "事件Key")
        private String defEventKey;

        public void setDefEventKey(String defEventKey) {
            this.defEventKey = replaceChar(defEventKey);
        }
    }

    public void setDefProcessKey(String defProcessKey) {
        this.defProcessKey = replaceChar(defProcessKey);
    }

    public void setDefAuditKey(String defAuditKey) {
        this.defAuditKey = replaceChar(defAuditKey);
    }

    /**
     * 过滤特殊字符
     * 
     * @param sourceStr
     * 
     * @return
     */
    public static String replaceChar(String sourceStr) {

        return sourceStr.replace("-", "");
    }

}
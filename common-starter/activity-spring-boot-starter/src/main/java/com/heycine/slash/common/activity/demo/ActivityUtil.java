package com.heycine.slash.common.activity.demo;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONArray;
import com.heycine.slash.common.activity.config.ActivitySecurityAdapter;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ActivityUtil {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private ActivitySecurityAdapter activitySecurityUtil;

    /**
     * 部署流程
     */
    public void deployment(BpmnModel bpmnModel, String processDefinitionKey, String processDefinitionName) {
        repositoryService.createDeployment().addBpmnModel(processDefinitionKey + ".bpmn", bpmnModel)
                .name(processDefinitionName).deploy();
    }

    /**
     * 启动流程实例
     *
     * @param processDefinitionKey 流程定义Key
     * @param businessKey          业务ID
     * @param applyUserKey         申请人key
     * @param applyUserValue       申请人value
     * @return 流程实例id
     */
    public String startProcessInstance(String processDefinitionKey, String businessKey, String applyUserKey,
                                       String applyUserValue) {
        activitySecurityUtil.logInAs(applyUserValue);

        ProcessInstance processInstance = processRuntime
                .start(ProcessPayloadBuilder.start().withProcessDefinitionKey(processDefinitionKey)
                        .withBusinessKey(businessKey).withVariable(applyUserKey, applyUserValue).build());

        log.info("Act启动流程完毕！流程定义Key：{}，流程实例id：{}", processDefinitionKey, processInstance.getId());
        return processInstance.getId();
    }

    /**
     * 完成任务
     *
     * @param applyUser
     */
    public void completedTask(String processDefinitionKey, String processInstanceId, String applyUser) {
        activitySecurityUtil.logInAs(applyUser);

        Task currentTask = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey)
                .processInstanceId(processInstanceId).taskAssignee(applyUser).singleResult();
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(currentTask.getId()).build());

        log.info("Act任务完成！流程定义Key：{}，任务id：{}，执行人：{}", processDefinitionKey, currentTask.getId(), applyUser);
    }

    /**
     * 完成任务
     *
     * @param applyUser
     */
    public void completedTask(String processInstanceId, String applyUser) {
        activitySecurityUtil.logInAs(applyUser);

        Task currentTask = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(applyUser)
                .singleResult();
        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(currentTask.getId()).build());

        log.info("Act任务完成！任务id：{}，执行人：{}", currentTask.getId(), applyUser);
    }

    /**
     * 完成任务
     *
     * @param taskId
     * @param map    变量键和值
     */
    public void completedTask(String userKey, String taskId, Map<String, Object> map) {
        activitySecurityUtil.logInAs(userKey);

        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId).withVariables(map).build());
    }

    /**
     * 查询组任务
     *
     * @param businessKey
     * @param groups
     * @return
     */
    public Task findGroupTasksByGroups(String businessKey, String groups) {

        return taskService.createTaskQuery().processInstanceBusinessKey(businessKey)
                .taskCandidateGroupIn(Collections.singletonList(groups)).singleResult();
    }

    /**
     * 查询组任务
     *
     * @param businessKey
     * @return
     */
    public Task findGroupTasksByGroups(String businessKey) {

        return taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
    }

    /**
     * 拾取任务
     *
     * @param businessKey
     * @param userKey
     * @param group
     */
    public Task claimTaskByBusinessKey(String businessKey, String userKey, String group) {
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).taskCandidateGroup(group)
                .singleResult();
        if (null != task) {
            taskService.claim(task.getId(), userKey);
            return task;
        }

        return null;
    }

    /**
     * 获取任务节点 out流程线
     *
     * @return
     */
    public List<KeyValueDTO> getOutFlow(Task currentTask) {
        if (null == currentTask) {
            return null;
        }
        // 获取当前模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId());
        // 获取当前节点
        FlowElement flowElement = bpmnModel.getFlowElement(currentTask.getTaskDefinitionKey());
        // 获取当前节点的一下个网关
        FlowElement targetFlowElement = CollectionUtils.firstElement(((UserTask) flowElement).getOutgoingFlows())
                .getTargetFlowElement();
        if (targetFlowElement instanceof ExclusiveGateway) {
            ExclusiveGateway exclusiveGateway = (ExclusiveGateway) targetFlowElement;
            List<SequenceFlow> outgoingFlows = exclusiveGateway.getOutgoingFlows();

            List<KeyValueDTO> result = new ArrayList<>();
            outgoingFlows.forEach(dto -> result.add(new KeyValueDTO(dto.getId(), dto.getName())));
            return result;
        }

        return null;
    }

    /**
     * 获取任务节点 表单参数
     *
     * @param currentTask
     * @return
     */
    public List<KeyValueDTO> getFromVariable(Task currentTask) {
        if (null == currentTask) {
            return null;
        }

        // 获取当前模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId());
        // 获取当前节点
        FlowElement flowElement = bpmnModel.getFlowElement(currentTask.getTaskDefinitionKey());
        // 获取任务定义
        UserTask userTask = (UserTask) flowElement;

        List<FormProperty> formProperties = userTask.getFormProperties();
        if (CollectionUtil.isNotEmpty(formProperties)) {
            Map<String, String> variableMap = formProperties.stream().collect(Collectors.toMap(FormProperty::getId, FormProperty::getVariable, (k1, k2) -> k2));
            String variable = variableMap.get(ProcessDefDTO.AuditOption.class.getSimpleName());
            List<ProcessDefDTO.AuditOption> auditOptions = JSONArray.parseArray(variable, ProcessDefDTO.AuditOption.class);

            List<KeyValueDTO> result = new ArrayList<>();
            for (ProcessDefDTO.AuditOption auditOption : auditOptions) {
                result.add(new KeyValueDTO(auditOption.getDefEventKey(), auditOption.getDefEventName()));
            }
            return  result;
        }

        return null;
    }

    /**
     * 获取运行中的流程实例-历史信息，根据流程定义key
     *
     * @param processKey
     * @return
     */
    public List<HistoricProcessInstance> getProcessInstanceHistoricByDefKey(String processKey) {

        return historyService.createHistoricProcessInstanceQuery().processDefinitionKey(processKey).list();
    }

    /**
     * 获取运行中的流程实例，根据流程定义key
     *
     * @param processKey
     * @return
     */
    public List<Execution> getProcessInstanceByDefKey(String processKey) {

        return runtimeService.createExecutionQuery().processDefinitionKey(processKey).list();
    }

    /**
     * 获取运行中的流程实例，根据流程定义的businessKey
     *
     * @param businessKey
     * @return
     */
    public Execution getProcessInstanceByBusinessKey(String businessKey) {

        return runtimeService.createExecutionQuery().processInstanceBusinessKey(businessKey).singleResult();
    }

    /**
     * 挂起流程
     *
     * @param processKey
     * @return
     */
    public void suspendProcessByDefKey(String processKey) {

        repositoryService.suspendProcessDefinitionByKey(processKey);
    }

    /**
     * 获取流程的结束事件 [只限于已经结束的流程实例]
     *
     * @param processInstanceId
     */
    public HistoricActivityInstance getEndEvent(String processInstanceId) {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).finished().orderByHistoricActivityInstanceEndTime().desc().list();

        return CollectionUtil.getFirst(list);
    }

    /**
     * 查询用户的组任务，包含未完成和已完成的
     *
     * @param processKey
     * @param group
     */
    public List<HistoricTaskInstance> findHistoricAllGroupTaskByUser(String processKey, String userKey, String group) {

        return historyService.createHistoricTaskInstanceQuery().processDefinitionKey(processKey).or()
                .taskCandidateGroup(group).taskAssignee(userKey).endOr().list();
    }

    /**
     * 查询业务ids，根据processInstanceIds
     *
     * @param processInstanceIds
     * @return
     */
    public List<HistoricProcessInstance> findBusinessKeysByProcessInstanceIds(Set<String> processInstanceIds) {

        return historyService.createHistoricProcessInstanceQuery().processInstanceIds(processInstanceIds).list();
    }

    /**
     * 获取当前任务
     *
     * @param businessKey
     */
    public Task getCurrentTask(String businessKey) {
        return taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
    }

    /**
     * 删除流程实例（强制）
     *
     * @param businessKeys
     */
    public void deleteTasksByBusiness(Collection<String> businessKeys) {
        for (String businessKey : businessKeys) {
            // 获取流程实例ID
            HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
            if (null == hpi) {
                continue;
            }
            String processInstanceId = hpi.getId();

            org.activiti.engine.runtime.ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
            if (pi == null) {
                //该流程实例已经完成了
                historyService.deleteHistoricProcessInstance(processInstanceId);
            } else {
                //该流程实例未结束的
                runtimeService.deleteProcessInstance(processInstanceId, "");
                historyService.deleteHistoricProcessInstance(processInstanceId);

            }
        }
    }

    /**
     * 删除流程实例（强制）
     *
     * @param businessKeys
     */
    public void deleteProcessInstance(String businessKeys) {
        this.deleteTasksByBusiness(Collections.singletonList(businessKeys));
    }
}

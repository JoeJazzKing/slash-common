package com.heycine.slash.common.activity.util;

import com.heycine.slash.common.activity.config.ActivitySecurityAdapter;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 流程实例、流程变量、个人任务
 */
@Component
@Slf4j
public class ActivityV4Util {

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
     * 流程挂起 -全部流程
     *
     * @param processDefinitionKey
     */
    public void processSuspend(String processDefinitionKey) {
        repositoryService.suspendProcessDefinitionByKey(processDefinitionKey, true, null);
    }

    /**
     * 流程激活 -全部流程
     *
     * @param processDefinitionKey
     */
    public void processActivate(String processDefinitionKey) {
        repositoryService.activateProcessDefinitionByKey(processDefinitionKey, true, null);
    }

    /**
     * 流程挂起 -单个
     *
     * @param processInstanceId
     */
    public void processSuspendOne(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    /**
     * 流程激活 -单个
     *
     * @param processInstanceId
     */
    public void processActivateOne(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    /**
     * 流程变量
     */
    public void proceesVariables() {
        // 启动一个流程实例时，添加流程变量
        ProcessInstance processInstance = processRuntime.start(
                ProcessPayloadBuilder.start()
                        .withProcessDefinitionKey(null)
                        .withBusinessKey(null)
                        // 流程变量
                        .withVariable(null, null)
                        .build()
        );

        // 完成任务时，添加流程变量
        taskRuntime.complete(
                TaskPayloadBuilder.complete()
                        .withTaskId(null)
                        // 流程变量
                        .withVariables(null)
                        .build()
        );
    }

    /**
     * 个人任务分配 -固定分配
     */
    public void personalTaskDistributionFixed() {

        log.info("在流程建模的时候，给Assignee字段填写固定的值，即是固定分配。");
    }

    /**
     * 个人任务分配 -表达式分配
     */
    public void personalTaskDistributionExpression() {
        // UEL-VALUE
        log.info("在流程建模的时候，给Assignee字段填写表达式，例如：${assignee001}，即是UEL-VALUE分配。");

        // UEL-METHOD, DemoBean表示spring容器中的一个bean，表示调用该bean的getUserKey()方法。
        log.info("在流程建模的时候，给Assignee字段填写表达式，例如：${DemoBean.getUserKey()}，即是UEL-METHOD分配。");

        // UEL-VALUE 和 UEL-METHOD
        log.info("在流程建模的时候，给Assignee字段填写表达式，例如：${DemoBean.getUserKey(assignee001)}，即是UEL-VALUE + UEL-METHOD分配。");
    }

    /**
     * 个人任务分配 -监听器分配
     */
    public void personalTaskDistributionMonitor() {
        /*
           事件类型
           create: 任务创建后触发
           assignment: 任务分配后触发
           complete: 任务完成后触发
           All: 所有事件都触发
         */
        log.info("在流程建模的时候，选择监听器和事件类型，代码中写一个类实现TaskListener，并重写notify方法！");
    }

    /**
     * 个人任务查询
     */
    public void personalTaskSelect() {

        taskService.createTaskQuery()
                .processDefinitionKey(null)
                // 负责人
                .taskAssignee(null)
                .includeProcessVariables()
                .list();
    }

    /**
     * 个人任务查询 -关联的businessKey
     */
    public void personalTaskBusinessKey() {
        // 首先查询到任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(null)
                .taskAssignee(null)
                .singleResult();

        // 然后获取流程实例对象
        String processInstanceId = task.getProcessInstanceId();
        org.activiti.engine.runtime.ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        // 最后获取到businessKey
        String businessKey = processInstance.getBusinessKey();
    }

    /**
     * 个人任务办理(完成任务)
     */
    public void personalTaskComplete() {
        Task task = taskService.createTaskQuery()
                .taskId(null)
                .taskAssignee(null)
                .singleResult();

        taskService.complete(task.getId());
    }

}

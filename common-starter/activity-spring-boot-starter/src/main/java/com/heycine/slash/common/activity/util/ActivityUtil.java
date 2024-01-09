package com.heycine.slash.common.activity.util;

import cn.hutool.core.collection.CollectionUtil;
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
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * 根据定义的bpmn资源，png资源
     *
     * @param processName 流程名称
     * @param bpmnPath    例如：process/appeal.bpmn
     * @param pngPath     例如：process/appeal.png
     */
    public void deployment(String processName, String bpmnPath, String pngPath) {
        InputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(bpmnPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.info("获取bpmn资源异常！{}", e.getMessage());
            return;
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        repositoryService.createDeployment()
                .addClasspathResource(bpmnPath)
                .addClasspathResource(pngPath)
                .name(processName)
                .deploy();
    }

    /**
     * 部署流程
     * 根据代码构造的bpmn模型，进行流程部署，可用于实现高度自定义的流程业务
     *
     * @param bpmnModel
     * @param processDefinitionKey
     * @param processDefinitionName
     */
    public void deployment(BpmnModel bpmnModel, String processDefinitionKey, String processDefinitionName) {
        String bpmnModelResourceName = processDefinitionKey + ".bpmn";

        repositoryService.createDeployment()
                .addBpmnModel(bpmnModelResourceName, bpmnModel)
                .name(processDefinitionName)
                .deploy();
    }

    /**
     * 启动流程实例
     *
     * @param processDefinitionKey 流程定义的key
     * @param businessKey          业务主键
     * @param applyUserKey         用户变量key
     * @param applyUserValue       用户变量value：实际的用户主键
     * @return 流程实例的ID
     */
    public String startProcessInstance(String processDefinitionKey, String businessKey,
                                       String applyUserKey, String applyUserValue) {
        activitySecurityUtil.logInAs(applyUserValue);

        ProcessInstance processInstance = processRuntime.start(
                ProcessPayloadBuilder.start()
                        .withProcessDefinitionKey(processDefinitionKey)
                        .withBusinessKey(businessKey)
                        .withVariable(applyUserKey, applyUserValue)
                        .build()
        );

        log.info("Activiti流程实例启动完毕！流程定义Key：{}，流程实例id：{}", processDefinitionKey, processInstance.getId());
        return processInstance.getId();
    }


    /**
     * 查询任务[多个]
     * 根据用户的业务主键查询，查询用户的任务
     *
     * @param processDefinitionKey 流程定义的key
     * @param user                 用户主键
     * @return 任务列表
     */
    public List<Task> findTasks(String processDefinitionKey, String user) {

        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateOrAssigned(user)
                .list();

        for (Task task : tasks) {
            ProcessInstance processInstance = processRuntime.processInstance(task.getProcessInstanceId());
            log.info("流程定义的key：{}, 业务主键：{}", processDefinitionKey, processInstance.getBusinessKey());
        }

        return tasks;
    }

    /**
     * 查询任务[单个]
     * 只限根据业务主键
     *
     * @param businessKey
     * @return
     */
    public Task findTask(String businessKey) {

        return taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
    }

    /**
     * 查询组任务[多个]
     * 根据流程定义的key和用户去哪先，查询用户执行中的任务
     *
     * @param processDefinitionKey 流程定义的key
     * @param userAuthorities      用户权限集合
     */
    public List<Task> findTaskGroups(String processDefinitionKey, List<String> userAuthorities) {

        return taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateGroupIn(userAuthorities)
                .list();
    }

    /**
     * 查询组任务[多个]
     * 根据流程定义的key、用户权限集合、用户主键，查询用户的任务 --> 包含已完成和未完成的任务
     * 用户权限集合主要用于查询候选人任务，用户主键主要用于查询已拾取或者已完成的任务
     *
     * @param processDefinitionKey 流程定义的key
     * @param userAuthorities      用户权限集合
     * @param user                 用户主键
     */
    public List<HistoricTaskInstance> findTaskGroups(String processDefinitionKey, List<String> userAuthorities, String user) {

        return historyService.createHistoricTaskInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .or()
                .taskCandidateGroupIn(userAuthorities)
                .taskAssignee(user)
                .endOr()
                .list();
    }

    /**
     * 查询组任务[单个]
     * 根据业务主键和用户权限集合，查询单个组任务
     *
     * @param businessKey     业务主键
     * @param userAuthorities 用户权限集合
     * @return 单个任务
     */
    public Task findTaskGroup(String businessKey, List<String> userAuthorities) {

        return taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskCandidateGroupIn(userAuthorities)
                .singleResult();
    }

    /**
     * 查询当前任务
     * 根据业务主键
     *
     * @param businessKey 业务主键
     */
    public Task findTaskCurrent(String businessKey) {

        return taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();
    }

    /**
     * 拾取任务
     * 根据taskId
     *
     * @param taskId          任务id
     * @param user            用户主键
     * @param userAuthorities 用户权限集合
     */
    public Task claimTask(String taskId, String user, List<String> userAuthorities) {
        activitySecurityUtil.logInAs(user);

        // 查询任务是否存在
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateGroupIn(userAuthorities)
                .singleResult();

        // 然后拾取
        if (null != task) {
            taskService.claim(task.getId(), user);
        }

        return task;
    }

    /**
     * 拾取任务
     * 根据业务主键
     *
     * @param businessKey     业务主键
     * @param user            用户主键
     * @param userAuthorities 用户权限集合
     */
    public Task claimTaskByBusinessKey(String businessKey, String user, List<String> userAuthorities) {
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskCandidateGroupIn(userAuthorities)
                .singleResult();

        if (null != task) {
            taskService.claim(task.getId(), user);
        }

        return task;
    }

    /**
     * 完成任务
     *
     * @param user         用户主键
     * @param taskId       任务id
     * @param variablesMap 变量键和值
     */
    public void completedTask(String taskId, String user, Map<String, Object> variablesMap) {
        activitySecurityUtil.logInAs(user);

        taskRuntime.complete(
                TaskPayloadBuilder.complete()
                        .withTaskId(taskId)
                        .withVariables(variablesMap)
                        .build()
        );
    }

    /**
     * 完成任务
     *
     * @param processInstanceId 流程实例id
     * @param user              用户主键
     */
    public void completedTask(String processInstanceId, String user) {
        activitySecurityUtil.logInAs(user);

        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee(user)
                .singleResult();

        if (null != task) {
            taskRuntime.complete(
                    TaskPayloadBuilder.complete()
                            .withTaskId(task.getId())
                            .build()
            );
        }
    }

    /**
     * 完成任务
     *
     * @param processInstanceId 流程实例id
     * @param user              用户主键
     */
    public void completedTask(String processDefinitionKey, String processInstanceId, String user) {
        activitySecurityUtil.logInAs(user);

        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .processInstanceId(processInstanceId)
                .taskAssignee(user)
                .singleResult();

        if (null != task) {
            taskRuntime.complete(
                    TaskPayloadBuilder.complete()
                            .withTaskId(task.getId())
                            .build()
            );
        }
    }

    /**
     * 获取任务 --> 网关 --> out流程线
     *
     * @return
     */
    public List<SequenceFlow> getOutFlow(Task currentTask) {
        if (null == currentTask) {
            return null;
        }

        // 获取当前模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId());

        // 获取当前节点
        FlowElement flowElement = bpmnModel.getFlowElement(currentTask.getTaskDefinitionKey());

        // 获取当前节点的一下个网关
        FlowElement targetFlowElement =
                CollectionUtils.firstElement(((UserTask) flowElement).getOutgoingFlows())
                        .getTargetFlowElement();

        // 获取网关out流程线
        List<SequenceFlow> outgoingFlows = null;
        if (targetFlowElement instanceof ExclusiveGateway) {
            outgoingFlows = ((ExclusiveGateway) targetFlowElement).getOutgoingFlows();
        }

        return outgoingFlows;
    }

    /**
     * 获取任务 --> 网关 --> out流程线
     *
     * @return
     */
    public List<SequenceFlow> getOutFlow(String processDefinitionKey, List<String> userAuthorities) {
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateGroupIn(userAuthorities)
                .singleResult();

        // 获取当前模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());

        // 获取当前节点
        FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());

        // 获取当前节点的一下个网关
        UserTask userTask = (UserTask) flowElement;
        SequenceFlow sequenceFlow = CollectionUtils.firstElement(userTask.getOutgoingFlows());
        FlowElement targetFlowElement = sequenceFlow.getTargetFlowElement();

        // 获取网关out流程线
        List<SequenceFlow> outgoingFlows = null;
        if (targetFlowElement instanceof ExclusiveGateway) {
            outgoingFlows = ((ExclusiveGateway) targetFlowElement).getOutgoingFlows();
        }

        return outgoingFlows;
    }

    /**
     * 查询历史流程实例
     * 根据流程实例id集合
     *
     * @param processInstanceIds 流程实例id集合
     * @return
     */
    public List<HistoricProcessInstance> findHistoryProcessInstance(Set<String> processInstanceIds) {

        return historyService.createHistoricProcessInstanceQuery()
                .processInstanceIds(processInstanceIds)
                .list();
    }

    /**
     * 获取流程实例的结束事件
     * [只限于已经结束的流程实例]
     *
     * @param processInstanceId
     */
    public HistoricActivityInstance getEndEvent(String processInstanceId) {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .orderByHistoricActivityInstanceEndTime().desc()
                .list();

        return CollectionUtil.getFirst(list);
    }

}

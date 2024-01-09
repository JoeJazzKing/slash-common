package com.heycine.slash.common.activity.util;

import com.heycine.slash.common.activity.config.ActivitySecurityAdapter;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 组任务、网关
 */
@Component
@Slf4j
public class ActivityV5Util {

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
     * 设置任务候选人
     * 可以设置用户key，也可以设置用户分组key
     */
    public void setTaskCandidateUsers() {

        log.info("在流程建模的时候，设置candidate-users或者candidate-groups, 多个候选人之间用逗号分割");
    }

    /**
     * 查询组任务
     *
     * @param processDefinitionKey
     * @param user
     */
    public List<Task> findGroupTask(String processDefinitionKey, String user) {
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskCandidateUser(user)
                .list();

        return list;
    }

    /**
     * 拾取组任务
     *
     * @param taskId
     * @param user
     */
    public void claimGroupTask(String taskId, String user) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(user)
                .singleResult();

        if (null != task) {
            taskService.claim(taskId, user);
        }
    }

    /**
     * 查询个人待办任务
     *
     * @param processDefinitionKey
     * @param user
     */
    public List<Task> findWaitPersonalTask(String processDefinitionKey, String user) {
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskAssignee(user)
                .list();

        return list;
    }

    /**
     * 办理个人待办任务
     *
     * @param taskId
     * @param user
     */
    public void completeWaitPersonalTask(String taskId, String user) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(user)
                .singleResult();

        // 完成任务
        if (null != task) {
            taskService.complete(task.getId());
        }
    }

    /**
     * 归还组任务
     *
     * @param taskId
     * @param user
     */
    public void backGroupTask(String taskId, String user) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(user)
                .singleResult();

        // 如果设置为null, 即是归还组任务, 任务没有负责人
        if (null != task) {
            taskService.setAssignee(task.getId(), null);
        }
    }

    /**
     * 交接组任务
     *
     * @param taskId
     * @param user
     */
    public void handoverGroupTask(String taskId, String user) {
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(user)
                .singleResult();

        // 设置该任务的新负责人
        if (null != task) {
            taskService.setAssignee(task.getId(), "taotao");
        }
    }

    /**
     * 排他网关 ExclusiveGateway TODO
     */
    public void exclusiveGateway() {
        log.info("排他网关只会选择一个为true的分支执行。如果有两个分支条件都为true, 排他网关会选择id值较小的一条分支去执行。");

        log.info("为什么要用排他网关?" +
                "不用排他网关也可以实现分支,如:在连线的condition条件上设置分支条件。" +
                "在连线设置condition条件的缺点:如果条件都不满足,流程就结束了(是异常结束)。");
    }

    /**
     * 并行网关 ParallelGateway TODO
     */
    public void parallelGateway() {

        log.info("并行网关不会解析条件。 即使顺序流中定义了条件,也会被忽略");
    }

    /**
     * 包含网关 InclusiveGateway TODO
     */
    public void inclusiveGateway() {

        log.info("并行网关不会解析条件。 即使顺序流中定义了条件,也会被忽略");
    }

    /**
     * 事件网关 EventGateway TODO
     */
    public void eventGateway() {

    }

}

package com.heycine.slash.common.activity.util;

import cn.hutool.core.collection.CollectionUtil;
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
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 流程部署、启动流程实例、任务查询、任务处理
 * 流程定义查询、流程定义删除、流程资源下载、流程历史查看
 */
@Component
@Slf4j
public class ActivityV3Util {

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

        repositoryService.createDeployment()
                // 流程名称
                .name(processName)
                // 定义的bpmn文件
                .addClasspathResource(bpmnPath)
                // 定义的png图片
                .addClasspathResource(pngPath)
                .deploy();
    }

    /**
     * 部署流程
     * 根据定义的bpmn资源，png资源
     *
     * @param processName 流程名称
     * @param bpmnFile    例如：process/appeal.bpmn
     * @param pngFile     例如：process/appeal.png
     * @return 流程定义的KEY
     */
    public String deployment(String processName, MultipartFile bpmnFile, MultipartFile pngFile) {
        try {
             Deployment deploy = repositoryService.createDeployment()
                    // 流程名称
                    .name(processName)
                    // 定义的bpmn文件
                    .addInputStream(bpmnFile.getOriginalFilename(), bpmnFile.getInputStream())
                    // 定义的png图片
                    .addInputStream(pngFile.getOriginalFilename(), bpmnFile.getInputStream())
                    .deploy();

             return  "部署ID: " + deploy.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
        // 权限验证
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
     * 任务查询
     * @param processDefinitionKey 流程定义的key
     * @param user 用户主键
     * @return 任务列表
     */
    public List<Task> findTask(String processDefinitionKey, String user) {

        return taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskAssignee(user)
                .list();
    }

    /**
     * 任务处理
     * @param taskId
     * @param variables
     */
    public void completeTask(String taskId, String user, Map<String, Object> variables) {
        activitySecurityUtil.logInAs(user);

        taskRuntime.complete(
                TaskPayloadBuilder.complete()
                        .withTaskId(taskId)
                        .withVariables(variables)
                        .build()
        );
    }

    /**
     * 查询流程定义
     *
     * @param processDefinitionKey
     * @return
     */
    public List<ProcessDefinition> findProcessDefinition(String processDefinitionKey) {

        return repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                // 版本号排序，倒叙
                .orderByProcessDefinitionVersion().desc()
                .list();
    }

    /**
     * 删除流程定义(所有版本号)
     * @param processDefinitionKey
     */
    public void deleteProcessDefinition(String processDefinitionKey) {
        try {
            for (ProcessDefinition processDefinition : this.findProcessDefinition(processDefinitionKey)) {

                repositoryService.deleteDeployment(processDefinition.getDeploymentId());

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("删除流程定义异常：{}", ex.getMessage());
        }
    }

    /**
     * 删除流程定义(所有版本号) [强制]
     * @param processDefinitionKey
     */
    public void deleteProcessDefinitionForce(String processDefinitionKey) {
        try {
            for (ProcessDefinition processDefinition : this.findProcessDefinition(processDefinitionKey)) {

                repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("强制删除流程定义异常：{}", ex.getMessage());
        }
    }

    /**
     * 流程资源下载(最新版本号)  -bpmn文件资源
     * @param processDefinitionKey
     * @return
     */
    public InputStream getProcessResourceBpmn(String processDefinitionKey) {
        ProcessDefinition firstProcessDefinition = CollectionUtil.getFirst(this.findProcessDefinition(processDefinitionKey));

        return repositoryService.getResourceAsStream(firstProcessDefinition.getDeploymentId(), firstProcessDefinition.getResourceName());
    }

    /**
     * 流程资源下载(最新版本号) -图片
     * @param processDefinitionKey
     * @return
     */
    public InputStream getProcessResourceImage(String processDefinitionKey) {
        ProcessDefinition firstProcessDefinition = CollectionUtil.getFirst(this.findProcessDefinition(processDefinitionKey));

        return repositoryService.getResourceAsStream(firstProcessDefinition.getDeploymentId(), firstProcessDefinition.getDiagramResourceName());
    }

    /**
     * 历史查看 -大纲总览
     */
    public void historyQuery() {
        // 流程实例历史
        historyService.createHistoricProcessInstanceQuery();
        // 任务历史
        historyService.createHistoricTaskInstanceQuery();
        // 活动历史
        historyService.createHistoricActivityInstanceQuery();
        // 变量历史
        historyService.createHistoricVariableInstanceQuery();
        // 详情历史
        historyService.createHistoricDetailQuery();
    }

    /**
     * 历史查看 流程实例
     */
    public List<HistoricProcessInstance> historyQueryProcess(String processDefinitionKey) {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();

        return list;
    }

    /**
     * 历史查看 任务
     */
    public List<HistoricTaskInstance> historyQueryTask(String processInstanceId) {
         List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        return list;
    }

    /**
     * 历史查看 活动
     */
    public List<HistoricActivityInstance> historyQueryActivity(String processInstanceId) {
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        return list;
    }

}

package com.heycine.slash.common.activity.demo;//package com.trax.lenz.appeal.controller;

import com.alibaba.fastjson.JSONArray;
import com.heycine.slash.common.activity.config.ActivitySecurityAdapter;
import com.heycine.slash.common.activity.util.ActivityV3Util;
import com.heycine.slash.common.activity.util.ActivityV4Util;
import com.heycine.slash.common.activity.util.ActivityV5Util;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("actTest")
@Slf4j
public class ActivitiTestController {

    @Autowired
    private ActivityV3Util activityV3Util;
    @Autowired
    private ActivityV4Util activityV4Util;
    @Autowired
    private ActivityV5Util activityV5Util;

    @Autowired
    private ActivitySecurityAdapter activitySecurityUtil;

    /**
     * 部署流程
     */
    @PostMapping("deployment")
    public String deployment(@RequestParam String processName,  @RequestParam MultipartFile bpmnFile, @RequestParam MultipartFile pngFile) throws FileNotFoundException {
        String deployment = activityV3Util.deployment(processName, bpmnFile, pngFile);

        return "流程部署ID: " + deployment;
    }

    /**
     * 启动流程实例
     */
    @PostMapping("start")
    public String startProcess(@RequestParam String processDefinitionKey, String businessKey, String userKey, String userValue) {
        // 启动流程实例
        String processInstanceId = activityV3Util.startProcessInstance(processDefinitionKey, businessKey, userKey, userValue);


        return "流程实例ID: " + processInstanceId;
    }

    /**
     * 查询任务
     */
    @PostMapping("findTask")
    public String findTask(@RequestParam String processDefinitionKey, String user) {

        List<Task> task = activityV3Util.findTask(processDefinitionKey, user);

        return JSONArray.toJSONString(task.toString());
    }

    /**
     * 完成任务
     */
    @PostMapping("completeTask")
    public String completeTask(@RequestParam String taskId, String user) {

        activityV3Util.completeTask(taskId, user, null);

        return "完成任务";
    }

    /**
     * 流程定义查询
     */
    @PostMapping("processDefinitionSelect")
    public String processDefinitionSelect(@RequestParam String processDefinitionKey) {

        List<ProcessDefinition> processDefinition = activityV3Util.findProcessDefinition(processDefinitionKey);

        return JSONArray.toJSONString(processDefinition.stream().map(ProcessDefinition::getVersion).collect(Collectors.toList()));
    }

    /**
     * 流程定义删除
     */
    @PostMapping("processDefinitionDelete")
    public String processDefinitionDelete(@RequestParam String processDefinitionKey) {

        // 规范删除
//        activityV3Util.deleteProcessDefinition(processDefinitionKey);

        // 强制删除
        activityV3Util.deleteProcessDefinitionForce(processDefinitionKey);

        return "流程定义删除成功";
    }

    /**
     * 流程资源下载
     */
    @PostMapping("processResourceDownload")
    public String processResourceDownload(@RequestParam String processDefinitionKey, HttpServletResponse response) throws IOException {
        // BPMN资源
        InputStream processResourceBpmn = activityV3Util.getProcessResourceBpmn(processDefinitionKey);
        // 图片资源
        InputStream processResourceImage = activityV3Util.getProcessResourceImage(processDefinitionKey);

        writeFile(response, processResourceBpmn);
//        writeFile(response, processResourceImage);
        return "OK";
    }

    /**
     * 流程历史查看 -流程实例历史
     */
    @PostMapping("processInstanceHistorySelect")
    public String processHistorySelect(@RequestParam String processDefinitionKey) {

        List<HistoricProcessInstance> historicProcessInstances = activityV3Util.historyQueryProcess(processDefinitionKey);

        return JSONArray.toJSONString(historicProcessInstances.stream().map(HistoricProcessInstance::getId).collect(Collectors.toList()));
    }

    /**
     * 流程历史查看 -任务历史
     */
    @PostMapping("taskHistorySelect")
    public String taskHistorySelect(@RequestParam String processInstanceId) {

        List<HistoricTaskInstance> historicTaskInstances = activityV3Util.historyQueryTask(processInstanceId);

        return JSONArray.toJSONString(historicTaskInstances.stream().map(HistoricTaskInstance::getId).collect(Collectors.toList()));
    }

    /**
     * 流程历史查看 -活动历史
     */
    @PostMapping("activityHistorySelect")
    public String activityHistorySelect(@RequestParam String processInstanceId) {

        List<HistoricActivityInstance> historicActivityInstances = activityV3Util.historyQueryActivity(processInstanceId);

        return JSONArray.toJSONString(historicActivityInstances.stream().map(HistoricActivityInstance::getActivityId).collect(Collectors.toList()));
    }

    /**
     *
     * @description: 将输入流输出到页面
     */
    private static void writeFile(HttpServletResponse resp, InputStream inputStream) {
        OutputStream out = null;
        try {
            out = resp.getOutputStream();
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = inputStream.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

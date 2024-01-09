package com.heycine.slash.common.activity.util;

import com.heycine.slash.common.activity.config.ActivitySecurityConfiguration;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;

import java.util.Collections;
import java.util.List;

public class BpmnUtil {

    public static final String START_EVENT = "START";
    public static final String SUCCESS_END_EVENT = "SUCCESS_END";
    public static final String FAIL_END_EVENT = "FAIL_END";
    public static final String COMPLETE_END_EVENT = "COMPLETE_END";
    public static final String BACK_AUDIT_EVENT_SUFFIX = "_BACK";

    public static final String INIT_EVENT = "INIT_CREATE";
    public static final String WAIT_EVENT = "WAIT_AUDIT";

    public static final String GATEWAY_ID = "GATEWAY";
    public static final String GATEWAY_NAME = "EX网关";

    public static final String INIT_USER_VARIABLE = "${applyUser}";
    public static final String INIT_USER_VARIABLE_INNER = "applyUser";
    public static final String AUDIT_EVENT_VARIABLE = "${%s==%s}";
    public static final String AUDIT_EVENT_VARIABLE_INNER = "eventResult";

    public static final String SPLIT_SYMBOL = "-";
    public static final String SPLIT_SYMBOL_ = "_";

    /**
     * 构造用户分组权限
     *
     * @param var1
     * @param var2
     * 
     * @return
     */
    public static String buildUserGroup(String var1, String var2) {

        return var1 + SPLIT_SYMBOL + var2;
    }

    /**
     * 构造事件UEL变量
     *
     * @param var1
     * @param var2
     * 
     * @return
     */
    public static String buildEventVariable(String var1, String var2) {

        return var1 + SPLIT_SYMBOL_ + var2;
    }

    /**
     * 创建BPMN2.0模型框架
     *
     * @param process
     * 
     * @return
     */
    public static BpmnModel createBpmnModel(Process process) {
        BpmnModel model = new BpmnModel();
        model.addProcess(process);
        return model;
    }

    /**
     * 创建BPMN2.0模型框架 -Process
     *
     * @param id
     * @param name
     * 
     * @return
     */
    public static Process createProcess(String id, String name) {
        Process process = new Process();
        process.setId(id);
        process.setName(name);
        return process;
    }

    /**
     * 创建开始事件
     *
     * @return
     */
    public static StartEvent createStartEvent(String id) {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(id);
        startEvent.setName("流程开始");
        return startEvent;
    }

    /**
     * 创建结束事件
     *
     * @return
     */
    public static EndEvent createEndEvent(String id) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(id);
        endEvent.setName("流程结束");
        return endEvent;
    }

    /**
     * 创建结束事件
     *
     * @return
     */
    public static EndEvent createEndEvent(String id, String name) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(id);
        endEvent.setName(name);
        return endEvent;
    }

    /**
     * 创建箭头
     *
     * @param fromId
     *            来源id
     * @param toId
     *            目标id
     * 
     * @return
     */
    public static SequenceFlow createSequenceFlow(String fromId, String toId, String uelExpression, String key,
            String desc) {
        SequenceFlow flow = createSequenceFlow(fromId, toId);
        flow.setConditionExpression(uelExpression);
        flow.setId(key);
        flow.setName(desc);
        return flow;
    }

    /**
     * 创建箭头
     *
     * @param fromId
     *            来源id
     * @param toId
     *            目标id
     * 
     * @return
     */
    public static SequenceFlow createSequenceFlow(String id, String fromId, String toId) {
        SequenceFlow flow = createSequenceFlow(fromId, toId);
        flow.setId(id);
        return flow;
    }

    /**
     * 创建箭头
     *
     * @param fromId
     *            来源id
     * @param toId
     *            目标id
     * 
     * @return
     */
    public static SequenceFlow createSequenceFlow(String fromId, String toId) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(fromId);
        flow.setTargetRef(toId);
        return flow;
    }

    /**
     * 创建task
     *
     * @param id
     *            任务id
     * @param name
     *            任务名称
     * @param assignee
     *            负责人
     * 
     * @return
     */
    public static UserTask createUserTask(String id, String name, String assignee, List<String> users, String groups) {

        return createUserTask(id, name, assignee, users, Collections.singletonList(groups));
    }

    /**
     * 创建task
     *
     * @param id
     *            任务id
     * @param name
     *            任务名称
     * @param assignee
     *            负责人
     * 
     * @return
     */
    public static UserTask createUserTask(String id, String name, String assignee, List<String> users,
            List<String> groups) {
        UserTask userTask = createUserTask(id, name, assignee);
        userTask.setCandidateUsers(users);
        userTask.setCandidateGroups(groups);
        return userTask;
    }

    /**
     * 创建task
     *
     * @param id
     *            任务id
     * @param name
     *            任务名称
     * @param assignee
     *            负责人
     * 
     * @return
     */
    public static UserTask createUserTask(String id, String name, String assignee) {
        UserTask userTask = createUserTask(id, name);
        userTask.setAssignee(assignee);
        return userTask;
    }

    /**
     * 创建task
     *
     * @param id
     *            任务id
     * @param name
     *            任务名称
     * 
     * @return
     */
    public static UserTask createUserTask(String id, String name) {
        UserTask userTask = new UserTask();
        userTask.setId(id);
        userTask.setName(name);
        return userTask;
    }

    /**
     * 创建排他网关
     *
     * @param id
     * @param name
     * 
     * @return
     */
    public static ExclusiveGateway createExclusiveGateway(String id, String name) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(id);
        exclusiveGateway.setName(name);
        return exclusiveGateway;
    }

    /**
     * 测试创建
     */
    public static void main(String[] args) {

        generateBpmnTest("test", "测试流程");
    }

    /**
     * 测试一下呗
     *
     * @param processId
     * @param processName
     * 
     * @return
     */
    public static BpmnModel generateBpmnTest(String processId, String processName) {
        Process process = createProcess(processId, processName);
        BpmnModel model = createBpmnModel(process);

        // 首先，创建元素
        process.addFlowElement(createStartEvent("start"));
        process.addFlowElement(createUserTask("task0", "创建申请单", "${applyUser}"));
        process.addFlowElement(createUserTask("task1", "一审", null, null,
                Collections.singletonList(ActivitySecurityConfiguration.ONE_AUDIT)));
        process.addFlowElement(createExclusiveGateway("gateway1", "一审EX网关"));
        process.addFlowElement(createUserTask("task2", "二审", null, null,
                Collections.singletonList(ActivitySecurityConfiguration.TWO_AUDIT)));
        process.addFlowElement(createExclusiveGateway("gateway2", "二审EX网关"));
        process.addFlowElement(createUserTask("task3", "三审", null, null,
                Collections.singletonList(ActivitySecurityConfiguration.THREE_AUDIT)));
        process.addFlowElement(createEndEvent("end"));

        // 然后，创建flow连线
        process.addFlowElement(createSequenceFlow("start", "task0"));
        process.addFlowElement(createSequenceFlow("task0", "task1"));
        process.addFlowElement(createSequenceFlow("task1", "gateway1"));
        process.addFlowElement(createSequenceFlow("gateway1", "task2", "${isPass}", "PASS1", "通过"));
        process.addFlowElement(createSequenceFlow("gateway1", "end", "${!isPass}", "NO_PASS1", "不通过"));
        process.addFlowElement(createSequenceFlow("task2", "gateway2"));
        process.addFlowElement(createSequenceFlow("gateway2", "task3", "${isPass}", "PASS1", "通过"));
        process.addFlowElement(createSequenceFlow("gateway2", "end", "${!isPass}", "NO_PASS1", "不通过"));
        process.addFlowElement(createSequenceFlow("task3", "end"));

        // 生成BPMN自动布局
        new BpmnAutoLayout(model).execute();

        /*
         * // 生成本地文件 byte[] convertToXML = new BpmnXMLConverter().convertToXML(model); String byteStr = new
         * String(convertToXML) .replaceAll("&lt;", "<").replaceAll("&gt;", ">"); InputStream inputStream = new
         * ByteArrayInputStream(byteStr.getBytes(StandardCharsets.UTF_8)); File file = new File("process/appeal.bpmn");
         * OutputStream outputStream = null; try { outputStream = new FileOutputStream(file); IOUtils.copy(inputStream,
         * outputStream); } catch (Exception e) { e.printStackTrace(); } System.out.println("BPMN创建成功！");
         */

        return model;
    }

}

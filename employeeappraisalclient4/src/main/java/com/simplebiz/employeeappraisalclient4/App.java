package com.simplebiz.employeeappraisalclient4;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.UserTaskServicesClient;

import com.simplebiz.employeeappraisal4.Employee;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final String SERVER_URL = "http://127.0.0.1:8080/kie-server/services/rest/server";
	
	private static final MarshallingFormat FORMAT = MarshallingFormat.JSON;  
	
    public static void main( String[] args )
    {
    	System.out.println("JBPM Server URL : "+SERVER_URL);
    	System.out.println("Default Marshaling format : "+FORMAT);
        startProcessWithParameter();
        performManagerTask();
        performMDTask();
    }
    
    public static void startProcessWithParameter() {
    	String containerName = "EmployeeAppraisal4";
    	String processId = "EmployeeAppraisal4.MainAppraisalProcessFlow";
    	String userNameToStart =  "supervisor1";
    	String passwordToStart = "supervisor1234";
    	System.out.println("================ START PROCESS WITH PARAMETER =====================");
    	System.out.println("Container Id : "+containerName);
    	System.out.println("Process Id : "+processId);
    	System.out.println("userNameToStart: "+userNameToStart);
    	System.out.println("passwordToStart : "+passwordToStart);
    	
    	 KieServicesConfiguration conf = KieServicesFactory.newRestConfiguration(SERVER_URL,userNameToStart,passwordToStart);
    	 conf.setMarshallingFormat(FORMAT);
    	 KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(conf);  
    	 ProcessServicesClient processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
    	 Employee employee = new Employee();
    	 employee.setName("Jessada SriharatThammarong");
    	 employee.setCode("04422233333");
    	 employee.setPosition("LABOR");
    	 employee.setCurrentSalary(9000.00);
    	 employee.setCurrentVocationDate(10);
    	 employee.setUsageVocationDate(1);

    	 Map<String,Object> employeeInnerDataMap = new LinkedHashMap<String,Object>();
    	 employeeInnerDataMap.put("com.simplebiz.employeeappraisal4.Employee", employee);
    	 
    	 Map<String,Object> employeeDataMap = new LinkedHashMap<String,Object>();
    	 employeeDataMap.put("employee",employeeInnerDataMap);
    	 System.out.println("Parameter for starting process : "+employeeDataMap);
    	 long processInstanceId = processClient.startProcess(containerName, processId,employeeDataMap);
 		 System.out.println("Started process instance id : "+processInstanceId);
 		 
    }
    
    public static void performManagerTask() {
    	String containerName = "EmployeeAppraisal4";
    	String userNameToGetTask =  "manager1";
    	String passwordToGetTask = "manager1!";
    	System.out.println("================ START Manager Task =====================");
    	System.out.println("Container Id : "+containerName);
    	System.out.println("userNameToGetTask: "+userNameToGetTask);
    	System.out.println("passwordToGetTask : "+passwordToGetTask);
    	
    	 KieServicesConfiguration conf = KieServicesFactory.newRestConfiguration(SERVER_URL,userNameToGetTask,passwordToGetTask);
    	 conf.setMarshallingFormat(FORMAT);
    	 KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(conf);  
    	
    	 UserTaskServicesClient userTaskService = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
     	 List<TaskSummary> taskSummaryList = userTaskService.findTasksAssignedAsPotentialOwner(userNameToGetTask,0, 10);
    	 
     	System.out.println("================ TASK LIST =====================");
     	for(TaskSummary taskSummary:taskSummaryList) {
     		System.out.println("------------------------------------------------");
     		System.out.println("Task Id : "+taskSummary.getId());
     		System.out.println("Task Name : "+taskSummary.getName());
     		System.out.println("Task ContainerId : "+taskSummary.getContainerId());
     		System.out.println("Task Owner : "+taskSummary.getActualOwner());
     		System.out.println("-------------------------------------------------");
     		
     		Map<String,Object> taskInput = userTaskService.getTaskInputContentByTaskId(containerName, taskSummary.getId());
     		System.out.println("------------------------------------------------");
     		System.out.println("taskInput : "+taskInput);
     		Employee employee = (Employee)taskInput.get("employee"); 
     		System.out.println("Name : "+employee.getName());
     		System.out.println("Code : "+employee.getCode());
     		System.out.println("Position : "+employee.getPosition());
     		System.out.println("CurrentSalary : "+employee.getCurrentSalary());
     		System.out.println("VocationDate : "+employee.getCurrentVocationDate());
     		System.out.println("Usage Vocation Date : "+employee.getUsageVocationDate());
     		double proposedSalary = employee.getCurrentSalary() + 1000;
    		System.out.println("Set Proposed Salary : "+proposedSalary);
     		employee.setProposedSalary(proposedSalary);
     		int proposedVocation = employee.getCurrentVocationDate() + 3;
     		System.out.println("Set Proposed Vocation Date : "+proposedSalary);
     		employee.setProposedVocationDate(proposedVocation);
     		String comment = "This employee should be increased salary and vocation date.";
     		System.out.println("Set comment : "+comment);
     		employee.setComment(comment);
     		Map<String,Object> employeeInnerDataMap = new LinkedHashMap<String,Object>();
     		employeeInnerDataMap.put("com.simplebiz.employeeappraisal4.Employee", employee);
     		Map<String,Object> employeeDataMap = new LinkedHashMap<String,Object>();
        	employeeDataMap.put("employee",employeeInnerDataMap);
        	userTaskService.startTask(containerName, taskSummary.getId(), userNameToGetTask);
    		userTaskService.completeTask(containerName,  taskSummary.getId(), userNameToGetTask, employeeDataMap);
     		System.out.println("------------------------------------------------");
     	}
     	System.out.println("================================================");
    }
    
    
    
    public static void performMDTask() {
    	String containerName = "EmployeeAppraisal4";
    	String userNameToGetTask =  "mduser1";
    	String passwordToGetTask = "mduser1!";
    	System.out.println("================ START MD TASK =====================");
    	System.out.println("Container Id : "+containerName);
    	System.out.println("userNameToGetTask: "+userNameToGetTask);
    	System.out.println("passwordToGetTask : "+passwordToGetTask);
    	
    	 KieServicesConfiguration conf = KieServicesFactory.newRestConfiguration(SERVER_URL,userNameToGetTask,passwordToGetTask);
    	 conf.setMarshallingFormat(FORMAT);
    	 KieServicesClient kieServicesClient = KieServicesFactory.newKieServicesClient(conf);  
    	
    	 UserTaskServicesClient userTaskService = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
     	 List<TaskSummary> taskSummaryList = userTaskService.findTasksAssignedAsPotentialOwner(userNameToGetTask,0, 10);
    	 
     	System.out.println("================ TASK LIST =====================");
     	for(TaskSummary taskSummary:taskSummaryList) {
     		System.out.println("------------------------------------------------");
     		System.out.println("Task Id : "+taskSummary.getId());
     		System.out.println("Task Name : "+taskSummary.getName());
     		System.out.println("Task ContainerId : "+taskSummary.getContainerId());
     		System.out.println("Task Owner : "+taskSummary.getActualOwner());
     		System.out.println("-------------------------------------------------");
     		
     		Map<String,Object> taskInput = userTaskService.getTaskInputContentByTaskId(containerName, taskSummary.getId());
     		System.out.println("------------------------------------------------");
     		System.out.println("taskInput : "+taskInput);
     		Employee employee = (Employee)taskInput.get("employee"); 
     		System.out.println("Name : "+employee.getName());
     		System.out.println("Code : "+employee.getCode());
     		System.out.println("Position : "+employee.getPosition());
     		System.out.println("CurrentSalary : "+employee.getCurrentSalary());
     		System.out.println("Proposed Salary : "+employee.getProposedSalary());
     		System.out.println("VocationDate : "+employee.getCurrentVocationDate());
     		System.out.println("Proposed Vocation Date : "+employee.getProposedVocationDate());
     		System.out.println("Usage Vocation Date : "+employee.getUsageVocationDate());
     		System.out.println("Comment : "+employee.getComment());
     		double proposedSalary = employee.getCurrentSalary() + 100;
    		System.out.println("Set Approved Salary : "+proposedSalary);
     		employee.setApprovedSalary(proposedSalary);
     		int proposedVocation = employee.getCurrentVocationDate() - 1;
     		System.out.println("Set Approved Vocation Date : "+proposedSalary);
     		employee.setApprovedVocationDate(proposedVocation);
     		String comment = "This employee should not increased vocation date and salary should improve little bit";
     		System.out.println("Set Approved comment : "+comment);
     		employee.setApproveComment(comment);
     		Map<String,Object> employeeInnerDataMap = new LinkedHashMap<String,Object>();
     		employeeInnerDataMap.put("com.simplebiz.employeeappraisal4.Employee", employee);
     		Map<String,Object> employeeDataMap = new LinkedHashMap<String,Object>();
        	employeeDataMap.put("employee",employeeInnerDataMap);
        	userTaskService.startTask(containerName, taskSummary.getId(), userNameToGetTask);
    		userTaskService.completeTask(containerName,  taskSummary.getId(), userNameToGetTask, employeeDataMap);
     		System.out.println("------------------------------------------------");
     	}
     	System.out.println("================================================");
    }
}

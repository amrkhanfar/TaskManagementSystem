package com.managementsystem.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Employee;
import com.managementsystem.model.Role;
import com.managementsystem.model.Task;
import com.managementsystem.model.TaskStatus;
import com.managementsystem.model.Team;
import com.managementsystem.model.comparators.TaskRepresentationStatusCompare;
import com.managementsystem.model.comparators.TaskRepresentationTitleCompare;
import com.managementsystem.service.EmployeeService;
import com.managementsystem.service.TaskService;
import com.managementsystem.service.TeamService;
import com.managementsystem.util.DataMapper;
import com.managementsystem.util.TaskRepresntation;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 
 * TaskController - Controller handling CRUD operations for Task entities And
 * approvals + listing.
 *
 * @author
 * @version Oct 8, 2024
 */
public class TaskController extends HttpServlet {

    private TaskService taskService = new TaskService();
    private EmployeeService employeeService = new EmployeeService();
    private TeamService teamService = new TeamService();
    private com.managementsystem.dao.EmployeeTaskDAO employeeTaskDAO = new com.managementsystem.dao.EmployeeTaskDAO();
    private DataMapper dataMapper = new DataMapper();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	HttpSession session = request.getSession(false);
	if (session == null) {
	    response.sendRedirect("login.jsp");
	    return;
	}

	Employee currentUser = (Employee) session.getAttribute("currentUser");
	if (currentUser == null) {
	    response.sendRedirect("login.jsp");
	    return;
	}

	String action = request.getParameter("action");
	if (action == null) {
	    action = "listMyTasks";
	}

	switch (action) {

	case "createAssignCurrent":
	    showCreateAndAssignToCurrentUserForm(request, response, currentUser);
	    break;
	case "create":
	    showCreateTaskForm(request, response, currentUser);
	    break;
	case "edit":
	    showEditTaskForm(request, response, currentUser);
	    break;
	case "sort":
	    sortTable(request, response, currentUser);
	    break;
	    
	/*Not used because it got replaced with an ajax implementation in doPost*/    
	case "markCompleted": 
	    markTaskCompleted(request, response, currentUser);
	    break;
	default:
	    listMyTasks(request, response, currentUser);
	    break;
	}
    }

    private void sortTable(HttpServletRequest request, HttpServletResponse response, Employee currentUser) throws IOException {
	String column = request.getParameter("column");
	String sortDirection = request.getParameter("sortDirection");
	
	List<TaskRepresntation> allTasksRep = new ArrayList<>();
	if (currentUser.getRole_id() == Role.MANAGER) {
	    List<Task> allTasks = taskService.getAllTasks();
	    for (Task task : allTasks) {
		Employee assignedEmployee = employeeTaskDAO.getEmployeeByTaskId(task.getId());
		TaskRepresntation taskRepresntation = new TaskRepresntation(task, assignedEmployee);
		allTasksRep.add(taskRepresntation);
	    }
	    
	} else if (currentUser.getRole_id() == Role.TEAM_LEADER) {
	    for (Employee employee : employeeService.getEmployeesByTeam(currentUser.getTeam_id())) {
		for (Task task : taskService.getTasksByEmployee(employee.getId())) {
		    TaskRepresntation taskRepresntation = new TaskRepresntation(task, employee);
		    allTasksRep.add(taskRepresntation);
		    }
	    }
	}
	    
	
	TaskRepresentationTitleCompare trTitleCompare = new TaskRepresentationTitleCompare();
	TaskRepresentationStatusCompare trStatusCompare = new TaskRepresentationStatusCompare();
	
	if (!sortDirection.equals("default")) {
	    Comparator comparator = null;
	    
	    if (column.equals("title")) {
		    comparator = trTitleCompare;
		} else if (column.equals("status")) {
		    comparator = trStatusCompare;
		}
		
		if (sortDirection.equals("desc")) {
			comparator = comparator.reversed();
		    }
		
		Collections.sort(allTasksRep, comparator);
	}
	
	StringBuilder htmlResponse = new StringBuilder();
	for(TaskRepresntation task : allTasksRep) {
	    htmlResponse.append("<tr data-task-id='").append(task.getData().getId()).append("'>")
            .append("<td>").append(task.getData().getTask_title()).append("</td>")
            .append("<td>").append(dataMapper.getStatusMap().get(task.getData().getTask_status())).append("</td>")
            .append("<td>").append(task.getData().getTask_description()).append("</td>")
            .append("<td>").append(task.getAssignedEmployee().getName()).append("</td>")
            .append("<td>")
            .append("<a href='' class='delete-button-small' id='task-delete-button'>Delete</a>")
            .append(" | <a href='tasks?action=edit&id=").append(task.getData().getId()).append("' class='edit-button-small'>Edit</a>")
            .append(" | ");

        // Conditional rendering based on task status
        if ("pending".equals(dataMapper.getStatusMap().get(task.getData().getTask_status()))) {
            htmlResponse.append("<a href='tasks?action=approve&id=").append(task.getData().getId()).append("' class='submit-button-small' id='approve-button'>Approve</a>");
        } else {
            htmlResponse.append("<select class='select-field' id='task-status-select-field' name='taskStatus'>");
            for (Map.Entry<Integer, String> status : dataMapper.getStatusMap().entrySet()) {
                if (!"pending".equals(status.getValue())) {
                    htmlResponse.append("<option value='").append(status.getKey()).append("'")
                        .append(task.getData().getTask_status() == status.getKey() ? " selected" : "")
                        .append(">").append(status.getValue()).append("</option>");
                }
            }
            htmlResponse.append("</select>");
        }
        htmlResponse.append("</td></tr>");
	}
	response.setContentType("text/html");
	response.getWriter().write(htmlResponse.toString());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	HttpSession session = request.getSession(false);

	if (session == null) {
	    response.sendRedirect("login.jsp");
	    return;
	}

	Employee currentUser = (Employee) session.getAttribute("currentUser");
	if (currentUser == null) {
	    response.sendRedirect("login.jsp");
	    return;
	}

	String action = request.getParameter("action");
	switch (action) {
	case "createAssignCurrent":
	    createAndAssignToCurrentUser(request, response, currentUser);
	    break;
	case "create":
	    createTask(request, response, currentUser);
	    break;
	case "edit":
	    editTask(request, response, currentUser);
	    break;
	case "updateStatus": //ajax
	    System.out.print("update status: " +  request.getParameter("id"));
	    updateTaskStatus(request, response, currentUser);
	    break;
	case "approve":
	    System.out.print("Approve task: " + request.getParameter("id"));
	    approveTask(request, response, currentUser);
	    break;
	case "delete":	//ajax
	    deleteTask(request, response, currentUser);
	    break;
	default:
	    listMyTasks(request, response, currentUser);
	}
    }

    private void listMyTasks(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
	    throws ServletException, IOException {
	List<Task> tasks = taskService.getTasksByEmployee(currentUser.getId());
	List<TaskRepresntation> pendingTasksRep = new ArrayList<>();
	List<TaskRepresntation> allTasksRep = new ArrayList<>();

	/* Getting pending and other tasks for managers and team leaders */
	if (currentUser.getRole_id() == Role.MANAGER) {
	    List<Task> pendingTasks = taskService.getAllPendingTasks();
	    for(Task task : pendingTasks) {
		Employee assignedEmployee = employeeTaskDAO.getEmployeeByTaskId(task.getId());
		TaskRepresntation taskRepresentation = new TaskRepresntation(task, assignedEmployee);
		pendingTasksRep.add(taskRepresentation);
	    }
	    
	    List<Task> allTasks = taskService.getAllTasks();
	    for (Task task : allTasks) {
		Employee assignedEmployee = employeeTaskDAO.getEmployeeByTaskId(task.getId());
		TaskRepresntation taskRepresntation = new TaskRepresntation(task, assignedEmployee);
		allTasksRep.add(taskRepresntation);
	    }
	    
	} else if (currentUser.getRole_id() == Role.TEAM_LEADER) {
	    for (Employee employee : employeeService.getEmployeesByTeam(currentUser.getTeam_id())) {
		for(Task task : taskService.getPendingTasksForEmployee(employee.getId())) {
		    TaskRepresntation taskRepresentation = new TaskRepresntation(task, employee);
		    pendingTasksRep.add(taskRepresentation);
		    }
		for (Task task : taskService.getTasksByEmployee(employee.getId())) {
		    TaskRepresntation taskRepresntation = new TaskRepresntation(task, employee);
		    allTasksRep.add(taskRepresntation);
		    }
	    }
	}
	request.setAttribute("userTasks", tasks);
	request.setAttribute("allTasks", allTasksRep);
	request.setAttribute("pendingTasks", pendingTasksRep);
	request.setAttribute("action", "listMyTasks");
	request.getRequestDispatcher("taskList.jsp").forward(request, response);
	return;
    }

    private void showCreateTaskForm(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
	    throws ServletException, IOException {
	List<Employee> employees;
	if (currentUser.getRole_id() == Role.MANAGER) {
	    employees = employeeService.getAllEmployees();
	    request.setAttribute("employees", employees);
	} else if (currentUser.getRole_id() == Role.TEAM_LEADER) {
	    employees = employeeService.getEmployeesByTeam(currentUser.getTeam_id());
	    request.setAttribute("employees", employees);
	}

	request.setAttribute("currentUser", currentUser);
	request.setAttribute("action", "create");

	request.getRequestDispatcher("taskForm.jsp").forward(request, response);
	return;
    }

    private void showCreateAndAssignToCurrentUserForm(HttpServletRequest request, HttpServletResponse response,
	    Employee currentUser) throws ServletException, IOException {
	List<Employee> employees = new ArrayList();
	if (currentUser.getRole_id() == Role.MANAGER) {
	    employees = employeeService.getAllEmployees();
	} else if (currentUser.getRole_id() == Role.TEAM_LEADER) {
	    employees = employeeService.getEmployeesByTeam(currentUser.getTeam_id());
	}
	request.setAttribute("employees", employees);

	request.setAttribute("currentUser", currentUser);
	request.setAttribute("action", "createAssignCurrent");

	request.getRequestDispatcher("taskForm.jsp").forward(request, response);
	return;
    }
    
    private void markTaskCompleted(HttpServletRequest request, HttpServletResponse response, Employee currentUser) throws ServletException, IOException {
	try {
	    String id = request.getParameter("id");

	    if (id == null || id.isEmpty()) {
		throw new IllegalArgumentException("Make sure no fields empty");
	    }

	    int idParsed = Integer.parseInt(id);
	    taskService.changeToCompleted(idParsed, currentUser.getId());

	    request.setAttribute("notification", "Task mark as completed");
	    listMyTasks(request, response, currentUser);
	} catch (IllegalArgumentException | AuthorizationException e) {
	    request.setAttribute("errorMessage", e.getMessage());
	    listMyTasks(request, response, currentUser);
	}
    }

    private void showEditTaskForm(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
	    throws ServletException, IOException {
	int taskId = Integer.parseInt(request.getParameter("id"));
	Task task = taskService.getTaskById(taskId);
	Employee assignedEmployee = employeeTaskDAO.getEmployeeByTaskId(taskId);
	List<Employee> employees = new ArrayList();
	
	if (currentUser.getRole_id() == Role.MANAGER) {
	    employees = employeeService.getAllEmployees();
	} else if (currentUser.getRole_id() == Role.TEAM_LEADER) {
	    employees = employeeService.getEmployeesByTeam(currentUser.getTeam_id());
	}
	request.setAttribute("employees", employees);
	request.setAttribute("assignedEmployee", assignedEmployee);
	request.setAttribute("task", task);
	request.setAttribute("action", "edit");
	request.setAttribute("currentUser", currentUser);
	request.getRequestDispatcher("taskForm.jsp").forward(request, response);
	return;
    }

    private void approveTask(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
	    throws ServletException, IOException {
	Map<String, String> jsonResponse = new HashMap<>();
	try {
	    String id = request.getParameter("id");

	    if (id == null || id.isEmpty()) {
		throw new IllegalArgumentException("Make sure no fields empty");
	    }

	    int idParsed = Integer.parseInt(id);
	    taskService.approveTask(idParsed, currentUser.getId());
	    
	    jsonResponse.put("status", "success");
	    jsonResponse.put("message", "Task approved successfully");
	} catch (IllegalArgumentException | AuthorizationException e) {
	    jsonResponse.put("status", "error");
	    jsonResponse.put("message", e.getMessage());
	}
	response.setContentType("application/json");
	
	String json = new Gson().toJson(jsonResponse);
	response.getWriter().write(json);
    }
    
    private void deleteTask(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
	    throws ServletException, IOException{
	String id = request.getParameter("id");
	Map<String, String> jsonResponse = new HashMap<>();
	    
	try {
	    if (id == null || id.isEmpty()) {
		throw new IllegalArgumentException("Task ID is missing");
	    }
	    
	    int idParsed = Integer.parseInt(id);
	    taskService.deleteTask(idParsed);
	    
	    jsonResponse.put("status", "success");
	    jsonResponse.put("message", "Task Deleted Successfully");
	    
	} catch (IllegalArgumentException e) {
	    jsonResponse.put("status", "error");
	    jsonResponse.put("message", e.getMessage());
	}
	
	response.setContentType("application/json");
	response.setCharacterEncoding("UTF-8");
	
	String json = new Gson().toJson(jsonResponse);
	response.getWriter().write(json);
    }
    
    private void updateTaskStatus(HttpServletRequest request, HttpServletResponse response, Employee currentUser) throws IOException {
	String id = request.getParameter("id");
	String status = request.getParameter("status");
	Map<String, String> jsonResponse = new HashMap<>();
	
	try {
	    if (id == null || id.isEmpty() || status == null || status.isEmpty()) {
		throw new IllegalArgumentException("Task ID / Updated status is missing");
	    }
	    
	    int idParsed = Integer.parseInt(id);
	    int statusParsed = Integer.parseInt(status);
	    
	    taskService.updateTaskStatus(idParsed, statusParsed, currentUser.getId());
	    
	    jsonResponse.put("status", "success");
	    jsonResponse.put("message", "Task status updated successfully");
	} catch (IllegalArgumentException | AuthorizationException e) {
	    jsonResponse.put("status", "error");
	    jsonResponse.put("message", e.getMessage());
	}
	
	response.setContentType("application/json");
	response.setCharacterEncoding("UTF-8");
	
	String json = new Gson().toJson(jsonResponse);
	response.getWriter().write(json);
    }

    private void createAndAssignToCurrentUser(HttpServletRequest request, HttpServletResponse response,
	    Employee currentUser) throws ServletException, IOException {

	try {

	    String status = request.getParameter("taskStatus");
	    String title = request.getParameter("taskTitle");
	    String description = request.getParameter("taskDescription");

	    if (status == null || title == null || description == null || status.isEmpty() || title.isEmpty()
		    || description.isEmpty()) {
		throw new IllegalArgumentException("Make sure no fields empty");
	    }

	    int taskStatus;

	    String normalizedStatus = status.trim().toLowerCase();

	    if (normalizedStatus.equals("pending")) {
		taskStatus = TaskStatus.PENDING;
	    } else if (normalizedStatus.equals("assigned")) {
		taskStatus = TaskStatus.ASSIGNED_IN_PROGRESS;
	    } else {
		throw new IllegalArgumentException("Unknown status: " + status);
	    }

	    Task task = new Task(-1, title, description, taskStatus);

	    int generatedId = taskService.createTask(task);
	    taskService.assignTask(generatedId, currentUser.getId());

	    request.setAttribute("notification", "New task has been created successfuly");
	    listMyTasks(request, response, currentUser);

	} catch (IllegalArgumentException e) {
	    request.setAttribute("errorMessage", e.getMessage());
	    listMyTasks(request, response, currentUser);
	}

    }

    private void createTask(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
	    throws ServletException, IOException {

	try {
	    String id = request.getParameter("id");
	    String status = request.getParameter("taskStatus");
	    String title = request.getParameter("taskTitle");
	    String description = request.getParameter("taskDescription");
	    String employeeId = request.getParameter("assignedEmployee");

	    if (status == null || title == null || description == null || employeeId == null || status.isEmpty()
		    || title.isEmpty() || description.isEmpty() || employeeId.isEmpty()) {
		throw new IllegalArgumentException("Make sure no fields empty");
	    }

	    int employeeIdParsed = Integer.parseInt(employeeId);

	    String normalizedStatus = status.trim().toLowerCase();
	    int taskStatus;
	    if (normalizedStatus.equals("pending")) {
		taskStatus = TaskStatus.PENDING;
	    } else if (normalizedStatus.equals("assigned")) {
		taskStatus = TaskStatus.ASSIGNED_IN_PROGRESS;
	    } else {
		throw new IllegalArgumentException("Unknown status: " + status);
	    }

	    Task task = new Task(-1, title, description, taskStatus);
	    System.out.print(task);

	    int generatedId = taskService.createTask(task);
	    taskService.assignTask(generatedId, employeeIdParsed);

	    request.setAttribute("notification", "New task has been created successfuly");
	    listMyTasks(request, response, currentUser);
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	    request.setAttribute("errorMessage", e.getMessage());
	    listMyTasks(request, response, currentUser);
	}

    }

    private void editTask(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
	    throws ServletException, IOException {
	try {
	    String id = request.getParameter("id");
	    String status = request.getParameter("taskStatus");
	    String title = request.getParameter("taskTitle");
	    String description = request.getParameter("taskDescription");
	    String employeeId = request.getParameter("assignedEmployee");

	    if (status == null || title == null || description == null || employeeId == null || status.isEmpty()
		    || title.isEmpty() || description.isEmpty() || employeeId.isEmpty()) {
		throw new IllegalArgumentException("Make sure no fields empty");
	    }

	    int idParsed = Integer.parseInt(id);
	    int employeeIdParsed = Integer.parseInt(employeeId);

	    int taskStatus;

	    String normalizedStatus = status.trim().toLowerCase();

	    if (normalizedStatus.equals("pending")) {
		taskStatus = TaskStatus.PENDING;
	    } else if (normalizedStatus.equals("assigned")) {
		taskStatus = TaskStatus.ASSIGNED_IN_PROGRESS;
	    } else {
		throw new IllegalArgumentException("Unknown status: " + status);
	    }
	    
	    
	    Task task = new Task(idParsed, title, description, taskStatus);
	    taskService.updateTask(task);	//updating the task table
	    
	    /* Checking if the assigned employee changed. If so, employeeTask table needs to be updated */
	    Employee assignedEmployee = employeeTaskDAO.getEmployeeByTaskId(idParsed);
	    if(assignedEmployee.getId() != employeeIdParsed) {
		employeeTaskDAO.removeTaskFromEmployee(idParsed, assignedEmployee.getId());
		employeeTaskDAO.assignTaskToEmployee(idParsed, employeeIdParsed);
	    }
	    
	    request.setAttribute("notification", "New task has been edited successfuly");
	    listMyTasks(request, response, currentUser);

	} catch (IllegalArgumentException e) {
	    request.setAttribute("errorMessage", e.getMessage());
	    listMyTasks(request, response, currentUser);
	}
    }
}

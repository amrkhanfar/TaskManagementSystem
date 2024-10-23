package com.managementsystem.deprecated;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.managementsystem.exception.AuthorizationException;
import com.managementsystem.model.Employee;
import com.managementsystem.model.Role;
import com.managementsystem.model.Task;
import com.managementsystem.model.TaskStatus;
import com.managementsystem.model.Team;
import com.managementsystem.service.EmployeeService;
import com.managementsystem.service.TaskService;
import com.managementsystem.service.TeamService;
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
 * This got deprecated because I needed to apply ajax requests on the front-end pages
 * Requests will be handled diffrently for delete action and I had to add sorting to
 * to tables
 *
 * @author
 * @version Oct 8, 2024
 */
public class TaskController extends HttpServlet {

    private TaskService taskService = new TaskService();
    private EmployeeService employeeService = new EmployeeService();
    private TeamService teamService = new TeamService();
    private com.managementsystem.dao.EmployeeTaskDAO employeeTaskDAO = new com.managementsystem.dao.EmployeeTaskDAO();

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
	case "approve":
	    approveTask(request, response, currentUser);
	    break;
	case "markCompleted":
	    markTaskCompleted(request, response, currentUser);
	    break;
	case "delete":
	    System.out.print("delete task: " + (String)session.getAttribute("id") );
	    deleteTask(request, response, currentUser);
	    break;
	default:
	    listMyTasks(request, response, currentUser);
	    break;

	}

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
	    System.out.print("entered create task case");
	    createTask(request, response, currentUser);
	    break;
	case "edit":
	    editTask(request, response, currentUser);
	    break;
	default:
	    listMyTasks(request, response, currentUser);
	}
    }

    private void listMyTasks(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
	    throws ServletException, IOException {
	List<Task> tasks = taskService.getTasksByEmployee(currentUser.getId());
	request.setAttribute("userTasks", tasks);
	request.setAttribute("action", "listMyTasks");

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
	
	request.setAttribute("allTasks", allTasksRep);
	request.setAttribute("pendingTasks", pendingTasksRep);
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

	try {
	    String id = request.getParameter("id");

	    if (id == null || id.isEmpty()) {
		throw new IllegalArgumentException("Make sure no fields empty");
	    }

	    int idParsed = Integer.parseInt(id);
	    taskService.updateTaskStatus(idParsed, TaskStatus.PENDING, currentUser.getId());

	    request.setAttribute("notification", "Task approved successfully");
	    listMyTasks(request, response, currentUser);
	} catch (IllegalArgumentException | AuthorizationException e) {
	    request.setAttribute("errorMessage", e.getMessage());
	    listMyTasks(request, response, currentUser);
	}
    }
    
    private void deleteTask(HttpServletRequest request, HttpServletResponse response, Employee currentUser)
	    throws ServletException, IOException{
	
	try {
	    String id = request.getParameter("id");

	    if (id == null || id.isEmpty()) {
		throw new IllegalArgumentException("Make sure no fields empty");
	    }
	    
	    int idParsed = Integer.parseInt(id);
	    taskService.deleteTask(idParsed);

	    request.setAttribute("notification", "Task Deleted Successfully");
	    listMyTasks(request, response, currentUser);
	    
	} catch (IllegalArgumentException e) {
	    request.setAttribute("errorMessage", e.getMessage());
	    listMyTasks(request, response, currentUser);
	}
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

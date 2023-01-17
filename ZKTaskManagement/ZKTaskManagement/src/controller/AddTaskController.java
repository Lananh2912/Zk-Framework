package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import bean.Task;
import bean.User;
import model.TaskServiceImpl;
import service.TaskService;

public class AddTaskController extends SelectorComposer<Component> {
    private static final long serialVersionUID = 1L;
    
    @Wire
    Window modalDialog;
    @Wire
	private Textbox nameTb;
	@Wire
	private Textbox statusTb;
	@Wire
	private Textbox descriptionTb;
	@Wire
	private Combobox  priorityCb;
	@Wire
	private Textbox depcodeTb;
	
	private TaskService taskService = new TaskServiceImpl(
			"jdbc:mysql://localhost:3306/task_management", "root", "123456789");
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Session sess = Sessions.getCurrent();
		User user = (User) sess.getAttribute("sessionCurr");
//		System.out.print(user.getDepcode());
		depcodeTb.setValue(user.getDepcode());	
	}

	//Khi click vào button add
    @Listen("onClick = #addTaskBtn")
    public void addTask() {
    	
    	Task task = new Task();
    	//Lấy dữ liệu nhập từ bàn phím
    	task.setName(nameTb.getValue());
		task.setStatus(statusTb.getValue());
		task.setDescription(descriptionTb.getValue());
		task.setPriority(priorityCb.getValue());
		task.setDepcode(depcodeTb.getValue());
		 Task checkTask = taskService.selectTaskByName(nameTb.getValue());
		 if (checkTask.getId() != null) {
			 Messagebox.show("Tên công việc đã tồn tại!");
		 } else {
		//Lưu vào DB
			 taskService.insertTask(task);
    	
			 Messagebox.show("Add Task Complete!");
			 modalDialog.detach();
			 Executions.sendRedirect("searchMvc.zul"); 
		 }
    };
}

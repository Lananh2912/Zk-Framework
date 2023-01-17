package controller;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import bean.Comment;
import bean.Task;
import bean.User;
import model.TaskServiceImpl;
import service.TaskService;

public class InsertCommentController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	@Wire
	Window modalDialog;
	@Wire
	Listbox cmntListbox;
	@Wire
	private Textbox desTb;
	@Wire
	private Textbox usernameTb;
	@Wire
	private Textbox  taskTb;
	@Wire
	private Textbox commentTb;
	@Wire
	private A uploadFile;
	
	private String pathOfNewUploadFile;
	
	private byte[] fileData = null;
	
	private TaskService taskService = new TaskServiceImpl(
			"jdbc:mysql://localhost:3306/task_management", "root", "123456789");
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		Integer id = (Integer) Executions.getCurrent().getArg().get("id");
		
		Task task = taskService.selectTask(id);
		List<Comment> listCmt = taskService.selectComment(task.getName());
		cmntListbox.setModel(new ListModelList<Comment>(listCmt));
		
		Session sess = Sessions.getCurrent();
		User user = (User) sess.getAttribute("sessionCurr");
//		System.out.print(user.getDepcode());
		taskTb.setValue(task.getName());
		desTb.setValue(task.getDescription());
		usernameTb.setValue(user.getUsername());
		uploadFile.setLabel(task.getUrl());
	}
	
	@Listen("onClick = #addCommentBtn")
	public void addComment() {
		Comment cmt = new Comment();
		cmt.setUsername(usernameTb.getValue());
		cmt.setComment(commentTb.getValue());
		cmt.setTaskname(taskTb.getValue());
		
		taskService.insertComment(cmt);
		
		Messagebox.show("Add Comment Complete!");
    	modalDialog.detach();
//        Executions.sendRedirect("home.zul"); 
	}
	
	@Listen("onClick=#uploadFile")
	public void doDownloadSyllabusAttachFile() throws FileNotFoundException {
		String fileDirOfUploadFile = "D:/files/";
		String fileName = uploadFile.getLabel();
		org.zkoss.zul.Filedownload.save(new java.io.File(fileDirOfUploadFile + fileName), null);
	}
	
}

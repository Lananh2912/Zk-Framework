package controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.A;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import bean.Task;
import model.TaskServiceImpl;
import service.TaskService;

public class ViewTaskController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	@Wire
	Window modalDialog;
	@Wire
	Label msg; 
	@Wire
	private Textbox nameTb;
	@Wire
	private Combobox  statusTb;
	@Wire
	private Textbox descriptionTb;
	@Wire
	private Combobox  priorityCb;
	@Wire
	private Textbox dateDb;
	@Wire
	private Textbox depcodeTb;
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

		msg.setValue(id.toString());

		Task task = taskService.selectTask(id);	
		
		nameTb.setValue(task.getName());
		statusTb.setValue(task.getStatus());
		descriptionTb.setValue(task.getDescription());
		priorityCb.setValue(task.getPriority());
		depcodeTb.setValue(task.getDepcode());
		uploadFile.setLabel(task.getUrl());
	}

	
	@Listen("onClick=#uploadFile")
	public void doDownloadSyllabusAttachFile() throws FileNotFoundException {
		String fileDirOfUploadFile = "D:/files/";
		String fileName = uploadFile.getLabel();
		org.zkoss.zul.Filedownload.save(new java.io.File(fileDirOfUploadFile + fileName), null);
	}
	

}

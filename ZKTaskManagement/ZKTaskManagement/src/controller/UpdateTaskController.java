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

public class UpdateTaskController extends SelectorComposer<Component> {
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

	@Listen("onClick = #updateTaskBtn")
	public void updateTask() throws IOException {

		int id = Integer.valueOf(msg.getValue());
		String name = nameTb.getValue();
		String status = statusTb.getValue();
		String description = descriptionTb.getValue();
		String depcode = depcodeTb.getValue();
		String priority = null;
		if("To do".equals(status)) {
			priority = priorityCb.getValue();
		} else if("Doing".equals(status)) {
			priority = priorityCb.getValue();
		}
		Date date = java.sql.Date.valueOf(java.time.LocalDate.now());
		String depCode = depcodeTb.getValue();
		String url = uploadFile.getLabel();
	
		String fileDirOfUploadFile = "D:/files/";
		
		if((pathOfNewUploadFile !=null)) {
			saveAndDeleteFile(fileDirOfUploadFile, fileData, pathOfNewUploadFile, url); // Luu vao thu muc 
		}
//		System.out.print(pathOfNewUploadFile);
		
		
		Task checkTask = taskService.selectTaskByName(nameTb.getValue());
//		System.out.print(checkTask.getId());
		 if (checkTask.getId() != null && checkTask.getId() != id) {
			 
			 Messagebox.show("Tên công việc đã tồn tại!");
			 
		 } else {
			 
			 Task task = new Task(id, nameTb.getValue(), status, description, priority, date, depCode, pathOfNewUploadFile);
			 
			 taskService.updateTask(task);
				
			 Messagebox.show("Update successfull!");

			 modalDialog.detach();

			 Executions.sendRedirect("searchMvc.zul");
		 }
	};
	
	@Listen("onUpload = button#addFile")
	public void uploadFile(UploadEvent event) {
		try {
			Media media = event.getMedia();
			if (media.isBinary()) {
				InputStream inputStream = media.getStreamData();
				fileData = IOUtils.toByteArray(inputStream);
			} else {
				fileData = media.getStringData().getBytes();
			}
			
			String fn = media.getName();
			int index = fn.lastIndexOf(".");
			String dateUpload = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss").format(new Date()).toString();
			String newFn = fn.substring(0,index) + "_" + dateUpload + "." + fn.substring(index+1);
			uploadFile.setLabel(newFn);
			pathOfNewUploadFile = newFn;
		}
		catch (Exception e) {
			Messagebox.show("Upload failed");
			e.printStackTrace();
		}
	}
	
	private void saveAndDeleteFile(String fileDir, byte[] fileContent, String pathOfNewFile, String pathOfOldFile) throws IOException  { 
		try (OutputStream fos = new FileOutputStream("D:/files/" + pathOfNewFile)) {
            fos.write(fileContent);
        }
	}
	
	@Listen("onClick=#uploadFile")
	public void doDownloadSyllabusAttachFile() throws FileNotFoundException {
		String fileDirOfUploadFile = "D:/files/";
		String fileName = uploadFile.getLabel();
		org.zkoss.zul.Filedownload.save(new java.io.File(fileDirOfUploadFile + fileName), null);
	}
	

}

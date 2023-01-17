package controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zul.*;
import org.zkoss.zul.ext.Selectable;

import bean.LoginBean;
import bean.Task;
import bean.User;
import model.TaskServiceImpl;
import service.TaskService;

public class SearchController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox keywordBox;
	@Wire
	private Datebox fromDate;
	@Wire
	private Datebox toDate;
	@Wire
	private Listbox taskListbox;
	@Wire
	private Label nameLabel;
	@Wire
	private Label statusLabel;
	@Wire
	private Label descriptionLabel;
	@Wire
	private Button addButton;

	private TaskService taskService = new TaskServiceImpl("jdbc:mysql://localhost:3306/task_management", "root",
			"123456789");

	@SuppressWarnings("deprecation")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
//		Date date = new SimpleDateFormat("dd/MM/yyyy").parse("01/06/2022");
		
		Date date = new Date();
		date.setDate(01);
		
		Date todate2 = new Date();
		
		fromDate.setValue(date);
		toDate.setValue(todate2);

		Session sess = Sessions.getCurrent();
		User user = (User) sess.getAttribute("sessionCurr");
		if((user.getDepcode() != null && "tvp".contentEquals(user.getDepcode()))) {
			user.setDepcode(null);
		}
		String datesrc = new SimpleDateFormat("yyyy/MM/dd").format(fromDate.getValue()).toString();
		String toDatestr = new SimpleDateFormat("yyyy/MM/dd").format(toDate.getValue()).toString();
//		System.out.print(datesrc);
		List<Task> result = taskService.search(null, user.getDepcode(), datesrc, toDatestr);
		//set data model cho Listbox
		taskListbox.setModel(new ListModelList<Task>(result));
	}

	@Listen("onClick = #addButton")
	public void showAddModal() {	
		
		Window window = (Window) Executions.createComponents("addTask.zul", null, null);
		window.doModal();
	}

	@Listen("onEdit=#taskListbox")
	public void showUpdateModalDialog(ForwardEvent evt) {

		Button editBtn = (Button) evt.getOrigin().getTarget();
		String editBtnId = editBtn.getId();
		Integer id = Integer.valueOf(editBtnId.substring(4));
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		Window window = (Window) Executions.createComponents("editTask.zul", null, map);
		window.doModal();
	}
	
	@Listen("onView=#taskListbox")
	public void showViewModalDialog(ForwardEvent evt) {

		Button editBtn = (Button) evt.getOrigin().getTarget();
		String editBtnId = editBtn.getId();
		Integer id = Integer.valueOf(editBtnId.substring(4));
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		Window window = (Window) Executions.createComponents("viewTask.zul", null, map);
		window.doModal();
	}
	
	@Listen("onCancel=#taskListbox")
	public void doCancel(final ForwardEvent evt) {
		
		Session sess = Sessions.getCurrent();
		final User user = (User) sess.getAttribute("sessionCurr");

		Messagebox.show("Are you sure to cancel?", "Cancel", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
				new EventListener<Event>() {
					@Override
					public void onEvent(final Event confirmEvt) throws InterruptedException {
						if (Messagebox.ON_YES.equals(confirmEvt.getName())) {
							Button cancelBtn = (Button) evt.getOrigin().getTarget();
							String cancelBtnId = cancelBtn.getId();
							Integer id = Integer.valueOf(cancelBtnId.substring(6));
							Task task = taskService.selectTask(id);
							task.setStatus("Cancel");
							taskService.updateTask(task);
							String date = new SimpleDateFormat("yyyy/MM/dd").format(fromDate.getValue()).toString();
							String toDatestr = new SimpleDateFormat("yyyy/MM/dd").format(toDate.getValue()).toString();
							List<Task> result = taskService.search(null, user.getDepcode(), date, toDatestr);
							taskListbox.setModel(new ListModelList<Task>(result));
						} else {
							return;
						}
					}
				});
	}

	@Listen("onDelete=#taskListbox")
	public void doDelete(final ForwardEvent evt) {
		
		Session sess = Sessions.getCurrent();
		final User user = (User) sess.getAttribute("sessionCurr");
		Messagebox.show("Are you sure to delete?", "Delete", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
				new EventListener<Event>() {
					@Override
					public void onEvent(final Event confirmEvt) throws InterruptedException {
						if (Messagebox.ON_YES.equals(confirmEvt.getName())) {
							Button deleteBtn = (Button) evt.getOrigin().getTarget();
							String deleteBtnId = deleteBtn.getId();
							Integer id = Integer.valueOf(deleteBtnId.substring(6));
							Messagebox.show("Xóa thành công!");
							taskService.deleteTask(id);
							String date = new SimpleDateFormat("yyyy/MM/dd").format(fromDate.getValue()).toString();
							String toDatestr = new SimpleDateFormat("yyyy/MM/dd").format(toDate.getValue()).toString();
							List<Task> result = taskService.search(null, user.getDepcode(), date, toDatestr);
							taskListbox.setModel(new ListModelList<Task>(result));
						} else {
							return;
						}
					}
				});
	}
	@Listen("onClick = #searchButton")
	public void search() {
		Session sess = Sessions.getCurrent();
		User user = (User) sess.getAttribute("sessionCurr");
//		System.out.print(user.getDepcode());
		String keyword = keywordBox.getValue();
		String date = new SimpleDateFormat("yyyy/MM/dd").format(fromDate.getValue()).toString();
		String toDatestr = new SimpleDateFormat("yyyy/MM/dd").format(toDate.getValue()).toString();
//		System.out.print(date);
		List<Task> result = taskService.search(keyword, user.getDepcode(), date, toDatestr);
		taskListbox.setModel(new ListModelList<Task>(result));
	}
 
}

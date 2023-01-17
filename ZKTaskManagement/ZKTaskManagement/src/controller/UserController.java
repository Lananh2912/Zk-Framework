package controller;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import bean.Task;
import bean.User;
import model.UserModel;

public class UserController extends SelectorComposer<Component>{
	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox keywordBox;
	@Wire
	private Listbox userListbox;
	@Wire
	private Label nameLabel;
	@Wire
	private Label passwordLabel;
	@Wire
	private Label fullnameLabel;
	@Wire
	private Button addButton;
	
	UserModel userModel = new UserModel("jdbc:mysql://localhost:3306/task_management", "root", "123456789");
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		List<User> result = userModel.search(null);
		//set data model cho Listbox
		userListbox.setModel(new ListModelList<User>(result));
	}
	
	@Listen("onClick = #addButton")
	public void showAddModal() {	
		
		Window window = (Window) Executions.createComponents("addUser.zul", null, null);
		window.doModal();
	}
	
	@Listen("onEdit=#userListbox")
	public void showUpdateModalDialog(ForwardEvent evt) {

		Button editBtn = (Button) evt.getOrigin().getTarget();
		String editBtnId = editBtn.getId();
		Integer id = Integer.valueOf(editBtnId.substring(4));
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		Window window = (Window) Executions.createComponents("editUser.zul", null, map);
		window.doModal();
	}
	
	@Listen("onDelete=#userListbox")
	public void doDelete(final ForwardEvent evt) {
		Messagebox.show("Are you sure to delete?", "Delete", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
				new EventListener<Event>() {
					@Override
					public void onEvent(final Event confirmEvt) throws InterruptedException {
						if (Messagebox.ON_YES.equals(confirmEvt.getName())) {
							Button deleteBtn = (Button) evt.getOrigin().getTarget();
							String deleteBtnId = deleteBtn.getId();
							Integer id = Integer.valueOf(deleteBtnId.substring(6));
							Messagebox.show("Task Id: " + id);
							userModel.deleteUser(id);
							List<User> result = userModel.search(null);
							userListbox.setModel(new ListModelList<User>(result));
						} else {
							return;
						}
					}
				});
	}
	
	@Listen("onClick = #searchButton")
	public void search() {
		String keyword = keywordBox.getValue();
		List<User> result = userModel.search(keyword);
		userListbox.setModel(new ListModelList<User>(result));
	}

}

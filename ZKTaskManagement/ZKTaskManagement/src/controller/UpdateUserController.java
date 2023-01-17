package controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import bean.Task;
import bean.User;

import model.UserModel;

public class UpdateUserController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	@Wire
    Window modalDialog;
	@Wire
	Label msg;
    @Wire
	private Textbox usernameTb;
	@Wire
	private Textbox passwordTb;
	@Wire
	private Textbox fullnameTb;
	@Wire
	private Textbox  permissionTb;
	@Wire
	private Textbox  depcodeTb;
	
	private String depcode;
	
	UserModel userModel = new UserModel("jdbc:mysql://localhost:3306/task_management", "root", "123456789");
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		Integer id = (Integer) Executions.getCurrent().getArg().get("id");

		msg.setValue(id.toString());

		User user = userModel.selectUser(id);	
		
		usernameTb.setValue(user.getUsername());
		passwordTb.setValue(user.getPassword());
		fullnameTb.setValue(user.getFullname());
		permissionTb.setValue(user.getPermission());
		depcodeTb.setValue(user.getDepcode());
	}

	@Listen("onClick = #updateUserBtn")
	public void updateUser() {

		int id = Integer.valueOf(msg.getValue());
		String username = usernameTb.getValue();
		String password = passwordTb.getValue();
		String fullname = fullnameTb.getValue();
		String permission = permissionTb.getValue();
		String depcode = depcodeTb.getValue();
		User user = new User(id, username, password, fullname, permission, depcode);
		userModel.updateUser(user);
		
		Messagebox.show("Update successfull!");

		modalDialog.detach();

		Executions.sendRedirect("userMvc.zul");
	}
}

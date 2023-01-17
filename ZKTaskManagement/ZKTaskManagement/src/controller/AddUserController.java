package controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import bean.Task;
import bean.User;
import model.UserModel;

public class AddUserController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	
	@Wire
    Window modalDialog;
    @Wire
	private Textbox usernameTb;
	@Wire
	private Textbox passwordTb;
	@Wire
	private Textbox fullnameTb;
	@Wire
	private Textbox  permissionCb;
	@Wire
	private Textbox  depcodeTb;
	
	UserModel userModel = new UserModel("jdbc:mysql://localhost:3306/task_management", "root", "123456789");
	
	@Listen("onClick = #addUserBtn")
    public void addUser() {
		
		User checkUser = userModel.selectUserByUsername(usernameTb.getValue());
		if(checkUser.equals(null)) {
			User user = new User();
	    	//Lấy dữ liệu nhập từ bàn phím
	    	user.setUsername(usernameTb.getValue());
			user.setPassword(passwordTb.getValue());
			user.setFullname(fullnameTb.getValue());
			user.setPermission(permissionCb.getValue());
			user.setDepcode(depcodeTb.getValue());
			//Lưu vào DB
			userModel.insertUser(user);
	    	
	    	Messagebox.show("Add User Complete!");
	    	modalDialog.detach();
	        Executions.sendRedirect("userMvc.zul"); 	
		}
		else {
			Messagebox.show("Tài khoản đã tồn tại!");
		}
    }
	
}

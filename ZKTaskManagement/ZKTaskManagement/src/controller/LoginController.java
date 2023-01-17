package controller;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;

import bean.LoginBean;
import bean.User;
import model.LoginModel;
import model.UserModel;

public class LoginController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
    
//    @Wire
//    Window modalDialog;
    @Wire
    Textbox usernameTb;
    @Wire
    Textbox passwordTb;
    @Wire
    Label message;
    @Wire
    Menuitem logout;
    
    private LoginModel loginModel = new LoginModel(
			"jdbc:mysql://localhost:3306/task_management", "root", "123456789");
    
    private UserModel userModel = new UserModel("jdbc:mysql://localhost:3306/task_management", "root", "123456789");
    
    @Listen("onClick = #loginBtn")
    public void login() {
    	String username = usernameTb.getValue();
    	String password = passwordTb.getValue();
    	
    	LoginBean bean = new LoginBean();
    	bean.setUsername(username);
		bean.setPassword(password);
		
		boolean status = loginModel.checkAccount(bean);
		if(!status) {
			message.setValue("Tên đăng nhập hoặc mật khẩu không đúng!");
			return;
		}
		
		User user = userModel.selectUserByUsername(usernameTb.getValue());
		
		Session sess = Sessions.getCurrent();
		sess.setAttribute("sessionCurr",user);
		Executions.sendRedirect("home.zul");
    }
    
    @Listen("onClick = #logout")
    public void logout() {
    	loginModel.logout();
    	Executions.sendRedirect("login.zul");
    }
}

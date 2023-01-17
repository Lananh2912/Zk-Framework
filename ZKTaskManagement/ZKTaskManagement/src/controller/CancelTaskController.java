package controller;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

import bean.Task;
import bean.User;
import model.TaskServiceImpl;
import service.TaskService;

public class CancelTaskController extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	@Wire
	private Listbox cancelTaskLb;
	
	private TaskService taskService = new TaskServiceImpl("jdbc:mysql://localhost:3306/task_management", "root",
			"123456789");
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		Session sess = Sessions.getCurrent();
		User user = (User) sess.getAttribute("sessionCurr");
		if((user.getDepcode() != null && "tvp".contentEquals(user.getDepcode()))) {
			user.setDepcode(null);
		}

		List<Task> result = taskService.getCancelTask(user.getDepcode());
		//set data model cho Listbox
		cancelTaskLb.setModel(new ListModelList<Task>(result));
	}
	
	@Listen("onReOpen=#cancelTaskLb")
	public void doCancel(final ForwardEvent evt) {
		
		Session sess = Sessions.getCurrent();
		final User user = (User) sess.getAttribute("sessionCurr");
		if((user.getDepcode() != null && "tvp".contentEquals(user.getDepcode()))) {
			user.setDepcode(null);
		}

		Messagebox.show("Bạn có chắc muốn mở lại công việc?", "ReOpen", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
				new EventListener<Event>() {
					@Override
					public void onEvent(final Event confirmEvt) throws InterruptedException {
						if (Messagebox.ON_YES.equals(confirmEvt.getName())) {
							Button cancelBtn = (Button) evt.getOrigin().getTarget();
							String cancelBtnId = cancelBtn.getId();
							Integer id = Integer.valueOf(cancelBtnId.substring(6));
							Task task = taskService.selectTask(id);
							task.setStatus("To do");
							taskService.updateTask(task);
							List<Task> result = taskService.getCancelTask(user.getDepcode());
							cancelTaskLb.setModel(new ListModelList<Task>(result));
						} else {
							return;
						}
					}
				});
	};
}

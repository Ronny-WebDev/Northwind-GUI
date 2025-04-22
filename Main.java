package northwindapp;
import java.awt.Component;

import javax.swing.*;

public class Main 
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Northwind App");
			frame.setSize(1000, 500);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JTabbedPane tabs = new JTabbedPane();
			tabs.addTab("Employees", new EmployeesTab());
			tabs.addTab("Products", new ProductsTab());
			tabs.addTab("Reports", new ReportsTab());
			tabs.addTab("Notifications", new NotificationsTab());
			tabs.addChangeListener(e -> {
				int index = tabs.getSelectedIndex();
				Component selectedComp = tabs.getComponent(index);
				if(selectedComp instanceof EmployeesTab)
				{
					EmployeesTab employeesTab = (EmployeesTab) (selectedComp);
					employeesTab.loadEmployees("");
				}
				else if(selectedComp instanceof ProductsTab)
				{
					ProductsTab productsTab = (ProductsTab) (selectedComp);
					productsTab.loadProducts();
				}
				else if(selectedComp instanceof ReportsTab)
				{
					ReportsTab reportsTab = (ReportsTab) (selectedComp);
					reportsTab.loadWarehouse();
				}
				else if(selectedComp instanceof NotificationsTab)
				{
					NotificationsTab notificationsTab = (NotificationsTab) (selectedComp);
					notificationsTab.loadClients("");
					notificationsTab.loadInactiveClients("");
				}
			});
			frame.add(tabs);
			frame.setVisible(true);
		});
	}
}

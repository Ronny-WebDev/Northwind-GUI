package northwindapp;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class EmployeesTab extends JPanel
{
	JTable table;
	JTextField search;
	JScrollPane scroll;
	
	public EmployeesTab()
	{
		setLayout(new BorderLayout());
		JPanel searchPanel = new JPanel();
		search = new JTextField(20);
		JButton filter = new JButton("Filter");
		searchPanel.add(search);
		searchPanel.add(filter);
		add(searchPanel, BorderLayout.NORTH);
		loadEmployees("");
		filter.addActionListener(e -> {
			String text = search.getText();
			loadEmployees(text);
		});
	}
	
	public void loadEmployees(String filter)
	{
		ArrayList<Object[]> data = new ArrayList<>();
		String query = "SELECT first_name, last_name, job_title AS job, address, city, state_province AS region, country_region AS country, zip_postal_code AS postal_code, business_phone AS phone, company AS office FROM employees";
		if(!filter.isEmpty())
		{
			query += " WHERE first_name LIKE ? OR last_name LIKE ? OR job_title LIKE ? OR city LIKE ? OR address LIKE ? OR state_province LIKE ? OR country_region LIKE ? OR zip_postal_code LIKE ? OR business_phone LIKE ? OR company LIKE ?";
		}
		try(Connection c = DBConnection.connect(); PreparedStatement p = c.prepareStatement(query);)
		{
			if(!filter.isEmpty())
			{
				p.setString(1, "%" + filter + "%");
				p.setString(2, "%" + filter + "%");
				p.setString(3, "%" + filter + "%");
				p.setString(4, "%" + filter + "%");
				p.setString(5, "%" + filter + "%");
				p.setString(6, "%" + filter + "%");
				p.setString(7, "%" + filter + "%");
				p.setString(8, "%" + filter + "%");
				p.setString(9, "%" + filter + "%");
				p.setString(10, "%" + filter + "%");
			}
			
			ResultSet r = p.executeQuery();
			while(r.next())
			{
				String job = r.getString("job");
				String active = "Yes";
				if(job == null)
				{
					active = "No";
				}
				data.add(new Object[]
				{
				r.getString("first_name"),
				r.getString("last_name"),
				job,
				r.getString("address"),
				r.getString("city"),
                r.getString("region"),
                r.getString("country"),
                r.getString("postal_code"),
                r.getString("phone"),
                r.getString("office"),
                active
				});
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
		String[] col = {"First Name", "Last Name", "Job", "Address", "City", "Region", "Country", "Postal Code", "Phone", "Office", "Active"};
		Object[][] arr = new Object[data.size()][];
		data.toArray(arr);
		if(scroll != null)
		{
			remove(scroll);
		}
		table = new JTable(arr, col);
		scroll = new JScrollPane(table);
		add(scroll, BorderLayout.CENTER);
		revalidate();
		repaint();
	}
}
	

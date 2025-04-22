package northwindapp;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class NotificationsTab extends JPanel
{
	JTable clients, inactiveClients;
	JScrollPane scroll, inactiveScroll;
	JTextField search;
	JButton addButton, updateButton, refreshButton, deleteButton;
	
	
	public NotificationsTab()
	{
		setLayout(new BorderLayout());
		JPanel top = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		addButton = new JButton("Add Client");
		updateButton = new JButton("Update Client");
		refreshButton = new JButton("Refresh");
		deleteButton = new JButton("Delete Client");
		buttonPanel.add(addButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(refreshButton);
		
		JPanel searchPanel = new JPanel();
		search = new JTextField(20);
		JButton searchButton = new JButton("Search");
		searchPanel.add(new JLabel("Search Clients: "));
		searchPanel.add(search);
		searchPanel.add(searchButton);
		top.add(buttonPanel, BorderLayout.NORTH);
		top.add(searchPanel, BorderLayout.SOUTH);
		add(top, BorderLayout.NORTH);
		
		JPanel center = new JPanel(new GridLayout(4, 1));
		scroll = new JScrollPane();
		center.add(new JLabel("All Clients"));
		center.add(scroll);
		inactiveScroll = new JScrollPane();
		center.add(new JLabel("Inactive Clients"));
		center.add(inactiveScroll);
		add(center, BorderLayout.CENTER);
		
		loadClients("");
		loadInactiveClients("");
		addButton.addActionListener(e -> addClient());
		updateButton.addActionListener(e -> updateClient());
		refreshButton.addActionListener(e -> {
			loadClients("");
			loadInactiveClients("");
		});
		deleteButton.addActionListener(e -> deleteClient());
		searchButton.addActionListener(e -> {
			String term = search.getText().trim();
			loadClients(term);
			loadInactiveClients(term);
		});
	}
	
	public void loadClients(String filter)
	{
		ArrayList<Object[]> data = new ArrayList<>();
        String query = "SELECT * FROM customers";
        if (!filter.isEmpty()) 
        {
            query += " WHERE id LIKE ? OR first_name LIKE ? OR last_name LIKE ? OR email_address LIKE ? OR company LIKE ? OR business_phone LIKE ?";
        }
        try (Connection c = DBConnection.connect(); PreparedStatement p = c.prepareStatement(query);) 
        {  	
        	if (!filter.isEmpty()) 
        	{
                   p.setString(1, "%" + filter + "%");
                   p.setString(2, "%" + filter + "%");
                   p.setString(3, "%" + filter + "%");
                   p.setString(4, "%" + filter + "%");
                   p.setString(5, "%" + filter + "%");
                   p.setString(6, "%" + filter + "%");
            }
        	ResultSet r = p.executeQuery();
            while (r.next()) 
            {
                data.add(new Object[]{
                        r.getInt("id"),
                        r.getString("first_name"),
                        r.getString("last_name"),
                        r.getString("email_address"),
                        r.getString("company"),
                        r.getString("business_phone")
                });
            }

        } catch (SQLException e) 
        {
            e.printStackTrace();
        }
        String[] col = {"ID", "First Name", "Last Name", "Email", "Company", "Phone"};
        Object[][] arr = new Object[data.size()][];
        data.toArray(arr);
        clients = new JTable(arr, col);
        scroll.setViewportView(clients);
        revalidate();
        repaint();
	}
	
	public void loadInactiveClients(String filter) 
	{
        ArrayList<Object[]> data = new ArrayList<>();
        String query = "SELECT id, first_name, last_name, email_address, company, business_phone FROM customers WHERE id NOT IN (SELECT customer_id FROM orders WHERE customer_id IS NOT NULL)";
        if (!filter.isEmpty()) 
        {
        	query += " AND (id LIKE ? OR first_name LIKE ? OR last_name LIKE ? OR email_address LIKE ? OR company LIKE ? OR business_phone LIKE ?)";
        }
        try (Connection c = DBConnection.connect(); PreparedStatement p = c.prepareStatement(query);) 
        {
        	if (!filter.isEmpty()) 
        	{
                   p.setString(1, "%" + filter + "%");
                   p.setString(2, "%" + filter + "%");
                   p.setString(3, "%" + filter + "%");
                   p.setString(4, "%" + filter + "%");
                   p.setString(5, "%" + filter + "%");
                   p.setString(6, "%" + filter + "%");
            }
        	ResultSet r = p.executeQuery();
            while (r.next()) {
                data.add(new Object[]{
                        r.getInt("id"),
                        r.getString("first_name"),
                        r.getString("last_name"),
                        r.getString("email_address"),
                        r.getString("company"),
                        r.getString("business_phone")
                });
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        String[] col = {"ID", "First Name", "Last Name", "Email", "Company", "Phone"};
        Object[][] arr = new Object[data.size()][];
        data.toArray(arr);
        inactiveClients = new JTable(arr, col);
        inactiveScroll.setViewportView(inactiveClients);
        revalidate();
        repaint();
    }
	
	public void updateClient()
	{
		int r = clients.getSelectedRow();
		if(r == -1) {JOptionPane.showMessageDialog(this, "Please select the client you wish to update"); return;}
		
		int id = (int) (clients.getValueAt(r, 0));
		String firstName = (String) (clients.getValueAt(r, 1));
		String lastName = (String) (clients.getValueAt(r, 2));
		String email = (String) (clients.getValueAt(r, 3));
		String company = (String) (clients.getValueAt(r, 4));
		String phoneNum = (String) (clients.getValueAt(r, 5));
		ClientDialog dialog = new ClientDialog(new String[]{firstName, lastName, email, company, phoneNum});
		dialog.setVisible(true);
		
		if (dialog.isSubmitted()) 
		{
			String query = "UPDATE customers SET first_name=?, last_name=?, email_address=?, company=?, business_phone=? WHERE id=?";
            try (Connection c = DBConnection.connect(); PreparedStatement p = c.prepareStatement(query)) 
            {
                p.setString(1, dialog.getFirstName());
                p.setString(2, dialog.getLastName());
                p.setString(3, dialog.getEmail());
                p.setString(4, dialog.getCompany());
                p.setString(5, dialog.getPhone());
                p.setInt(6, id);
                p.executeUpdate();
                loadClients("");
                loadInactiveClients("");
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
		}
	}
	
	private void addClient() 
	{
        ClientDialog dialog = new ClientDialog(null);
        dialog.setVisible(true);
        if (dialog.isSubmitted()) 
        {
        	String query = "INSERT INTO customers (first_name, last_name, email_address, company, business_phone) VALUES (?, ?, ?, ?, ?)";
            try (Connection c = DBConnection.connect(); PreparedStatement p = c.prepareStatement(query)) 
            {
                p.setString(1, dialog.getFirstName());
                p.setString(2, dialog.getLastName());
                p.setString(3, dialog.getEmail());
                p.setString(4, dialog.getCompany());
                p.setString(5, dialog.getPhone());
                p.executeUpdate();
                loadClients("");
                loadInactiveClients("");
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }
    }
	
	private void deleteClient() 
	{
        int r = clients.getSelectedRow();
        if (r == -1) {JOptionPane.showMessageDialog(this, "Please select a client to delete."); return;}
        int id = (int) clients.getValueAt(r, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this client?");
        if (confirm == JOptionPane.YES_OPTION) 
        {
        	String query = "DELETE FROM customers WHERE id = ?";
            try (Connection c = DBConnection.connect(); PreparedStatement p = c.prepareStatement(query)) 
            {
                p.setInt(1, id);
                p.executeUpdate();
                loadClients("");
                loadInactiveClients("");
            } 
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
        }
	
	
	}
}

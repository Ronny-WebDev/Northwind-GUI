package northwindapp;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class ReportsTab extends JPanel
{
	
	JTable table;
	JScrollPane scroll;
	
	public ReportsTab()
	{
		setLayout(new BorderLayout());
		loadWarehouse();
	}
	
	public void loadWarehouse()
	{
		ArrayList<Object[]> data = new ArrayList<>();
		String query = "SELECT s.company AS warehouse_name, p.category AS category, COUNT(*) AS product_count FROM products p JOIN suppliers s ON s.id = CAST(p.supplier_ids AS UNSIGNED) WHERE p.category IS NOT NULL GROUP BY s.company, p.category";
		try(Connection c = DBConnection.connect(); PreparedStatement p = c.prepareStatement(query); ResultSet r = p.executeQuery();)
		{
			while(r.next())
			{
				data.add(new Object[]
				{
				r.getString("warehouse_name"),
				r.getString("category"),
                r.getString("product_count")
				});
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		String[] col = {"Warehouse Name", "Category", "Product Count"};
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

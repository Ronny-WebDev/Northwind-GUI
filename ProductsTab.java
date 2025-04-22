package northwindapp;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class ProductsTab extends JPanel
{
	JTable table;
	JScrollPane scroll;
	
	public ProductsTab()
	{
		setLayout(new BorderLayout());
		JButton addProd = new JButton("Add Product");
		add(addProd, BorderLayout.NORTH);
		loadProducts();
		addProd.addActionListener(e -> {
			new AddProduct(this);
		});
	}
	
	public void loadProducts()
	{
		ArrayList<Object[]> data = new ArrayList<>();
		String query = "SELECT product_name, list_price, quantity_per_unit, category, supplier_ids FROM products";
		try(Connection c = DBConnection.connect(); PreparedStatement p = c.prepareStatement(query); ResultSet r = p.executeQuery();)
		{
			while(r.next())
			{
				data.add(new Object[]{
						r.getString("product_name"),
						r.getString("list_price"),
						r.getString("quantity_per_unit"),
						r.getString("category"),
						r.getString("supplier_ids")
				});
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		String[] cols = {"Product Name", "Price", "Quantity", "Category", "Supplier ID"};
        Object[][] arr = new Object[data.size()][];
        data.toArray(arr);
        if(scroll != null)
        {
        	remove(scroll);
        }
        
        table = new JTable(arr, cols);
        scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
        revalidate();
        repaint();
	}
}

package northwindapp;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class AddProduct extends JDialog
{
	public AddProduct(ProductsTab parentTab)
	{
		setTitle("Add New Product");
		setSize(400, 300);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(6,2));
		
		JTextField name = new JTextField();
		JTextField price = new JTextField();
		JTextField qty = new JTextField();
		JComboBox<String> supplier = new JComboBox<>();
		JComboBox<String> category = new JComboBox<>();
		try (Connection c = DBConnection.connect(); PreparedStatement p = c.prepareStatement("SELECT id, company FROM suppliers"); ResultSet r = p.executeQuery()) 
		{
			while (r.next()) 
			{
	                supplier.addItem(r.getInt("id") + " - " + r.getString("company"));
	        }
	    } 
		catch (SQLException e) 
		{
	            e.printStackTrace();
	    }
		
		try (Connection c = DBConnection.connect(); PreparedStatement p = c.prepareStatement("SELECT DISTINCT category FROM products WHERE category IS NOT NULL"); ResultSet r = p.executeQuery())
		{
			while (r.next()) 
			{
				category.addItem(r.getString("category"));
	        }
	    } 
		catch (SQLException e) 
		{
			e.printStackTrace();
	    }
		
		add(new JLabel("Name:"));
        add(name);
        add(new JLabel("Price:"));
        add(price);
        add(new JLabel("Quantity per Unit:"));
        add(qty);
        add(new JLabel("Supplier:"));
        add(supplier);
        add(new JLabel("Category:"));
        add(category);
        
        JButton submit = new JButton("Add Product");
        add(new JLabel());
        add(submit);
        submit.addActionListener(e -> {
        	String Name = name.getText();
        	String Quantity = qty.getText();
        	double Price = Double.parseDouble(price.getText());
        	String Category = (String) (category.getSelectedItem());
        	String Supplier = (String) (supplier.getSelectedItem());
        	
        	if(Supplier == null || !Supplier.contains("-"))
        	{
        		JOptionPane.showInputDialog(this, "Invalid Supplier");
        		return;
        	}
        	
        	int SupplierID = Integer.parseInt(Supplier.split(" - ")[0]);
        	String insertSQL = "INSERT INTO products (product_name, list_price, quantity_per_unit, category, supplier_ids) VALUES (?, ?, ?, ?, ?)";
        	try(Connection c = DBConnection.connect(); PreparedStatement p = c.prepareStatement(insertSQL))
        	{
        		p.setString(1, Name);
                p.setDouble(2, Price);
                p.setString(3, Quantity);
                p.setString(4, Category);
                p.setString(5, String.valueOf(SupplierID));
                p.executeUpdate();
                JOptionPane.showMessageDialog(this, "Product Added Succesfully");
                dispose();
                parentTab.loadProducts();
        	}
        	catch(SQLException exception)
        	{
        		exception.printStackTrace();
        		JOptionPane.showMessageDialog(this, "Unable to Add Product");
        	}
        });
        setVisible(true);
	}
}

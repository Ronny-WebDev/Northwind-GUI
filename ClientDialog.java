package northwindapp;
import javax.swing.*;
import java.awt.*;

public class ClientDialog extends JDialog
{
	private JTextField firstNameField, lastNameField, emailField, companyField, phoneField;
    private boolean submitted = false;
    
    public ClientDialog(String[] existingData) 
    {
        setTitle(existingData == null ? "Add Client" : "Update Client");
        setModal(true);
        setSize(400, 300);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        form.add(firstNameField);

        form.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        form.add(lastNameField);

        form.add(new JLabel("Email:"));
        emailField = new JTextField();
        form.add(emailField);

        form.add(new JLabel("Company:"));
        companyField = new JTextField();
        form.add(companyField);

        form.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        form.add(phoneField);

        add(form, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        buttons.add(ok);
        buttons.add(cancel);
        add(buttons, BorderLayout.SOUTH);

        if (existingData != null) 
        {
            firstNameField.setText(existingData[0]);
            lastNameField.setText(existingData[1]);
            emailField.setText(existingData[2]);
            companyField.setText(existingData[3]);
            phoneField.setText(existingData[4]);
        }

        ok.addActionListener(e -> {
            submitted = true;
            dispose();
        });

        cancel.addActionListener(e -> dispose());
    }
    
    public boolean isSubmitted()
    {
    	return submitted;
    }
    
    public String getFirstName()
    {
    	return firstNameField.getText().trim();
    }
    
    public String getLastName() 
    {
        return lastNameField.getText().trim();
    }

    public String getEmail() 
    {
        return emailField.getText().trim();
    }

    public String getCompany() 
    {
        return companyField.getText().trim();
    }

    public String getPhone() 
    {
        return phoneField.getText().trim();
    }
}

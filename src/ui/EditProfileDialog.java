package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.Profile;
import controller.MediaLibraryController;

public class EditProfileDialog extends JDialog implements ActionListener {

    private final JPanel contentPanel = new JPanel();
    private JTextField textName;
    private JTextArea textDescription;
    private JCheckBox chckbxNewCheckBox;

    boolean profileModified = false;
    Profile profile;

    /**
     * Create the dialog.
     * 
     * @param profile
     *            the profile to edit
     */
    public EditProfileDialog(Profile profile) {
	this.profile = profile;

	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	setModal(true);
	setTitle("Edit Profile");
	setBounds(100, 100, 450, 300);
	getContentPane().setLayout(new BorderLayout());
	contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	getContentPane().add(contentPanel, BorderLayout.CENTER);
	GridBagLayout gbl_contentPanel = new GridBagLayout();
	gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
	gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0 };
	gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
	gbl_contentPanel.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
	contentPanel.setLayout(gbl_contentPanel);
	{
	    JLabel lblNewLabel = new JLabel("Name:");
	    GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
	    gbc_lblNewLabel.anchor = GridBagConstraints.NORTHEAST;
	    gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
	    gbc_lblNewLabel.gridx = 0;
	    gbc_lblNewLabel.gridy = 0;
	    contentPanel.add(lblNewLabel, gbc_lblNewLabel);
	}
	{
	    textName = new JTextField();
	    GridBagConstraints gbc_textName = new GridBagConstraints();
	    gbc_textName.insets = new Insets(0, 0, 5, 0);
	    gbc_textName.fill = GridBagConstraints.HORIZONTAL;
	    gbc_textName.gridx = 1;
	    gbc_textName.gridy = 0;
	    contentPanel.add(textName, gbc_textName);
	    textName.setColumns(10);
	}
	{
	    JLabel lblNewLabel_1 = new JLabel("Description:");
	    GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
	    gbc_lblNewLabel_1.anchor = GridBagConstraints.NORTHEAST;
	    gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
	    gbc_lblNewLabel_1.gridx = 0;
	    gbc_lblNewLabel_1.gridy = 1;
	    contentPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
	}
	{
	    JScrollPane scrollPane = new JScrollPane();
	    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
	    gbc_scrollPane.fill = GridBagConstraints.BOTH;
	    gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
	    gbc_scrollPane.gridx = 1;
	    gbc_scrollPane.gridy = 1;
	    contentPanel.add(scrollPane, gbc_scrollPane);
	    {
		textDescription = new JTextArea();
		scrollPane.setViewportView(textDescription);
	    }
	}
	{
	    chckbxNewCheckBox = new JCheckBox("Default Profile");
	    GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
	    gbc_chckbxNewCheckBox.anchor = GridBagConstraints.WEST;
	    gbc_chckbxNewCheckBox.gridx = 1;
	    gbc_chckbxNewCheckBox.gridy = 2;
	    contentPanel.add(chckbxNewCheckBox, gbc_chckbxNewCheckBox);
	}
	{
	    JPanel buttonPane = new JPanel();
	    buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
	    getContentPane().add(buttonPane, BorderLayout.SOUTH);
	    {
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	    }
	    {
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);
	    }
	}

	// Populate the fields with data from the profile
	textName.setText(profile.getName());
	textDescription.setText(profile.getDescription());
	chckbxNewCheckBox.setSelected(profile.getIsDefault() == 1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String command = e.getActionCommand();
	if ("OK".equals(command)) {
	    doOk();
	} else if ("Cancel".equals(command)) {
	    dispose();
	}
    }

    private void doOk() {
	MediaLibraryController controller = new MediaLibraryController();
	if (controller.updateProfile(profile.getId(), textName.getText(), textDescription.getText())) {
	    profileModified = true;
	    dispose();
	} else {
	    JOptionPane.showMessageDialog(this, "Failed to edit profile.");
	    return;
	}
    }
}

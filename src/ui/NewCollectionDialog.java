package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

import model.Collection;

import controller.MediaLibraryController;
import javax.swing.JScrollPane;

public class NewCollectionDialog extends JDialog implements ActionListener {

    private final JPanel contentPanel = new JPanel();
    private JTextField textName;
    private JTextArea textDescription;
    private Collection parent;
    Collection child = null;

    /**
     * Create the dialog.
     */
    public NewCollectionDialog() {
	this(null);
    }

    /**
     * Create the dialog.
     * 
     * @param parent
     *            the Collection to add the new Collection to
     */
    public NewCollectionDialog(Collection parent) {
	this.parent = parent;

	setModal(true);
	setTitle("New Collection");
	setBounds(100, 100, 450, 300);
	getContentPane().setLayout(new BorderLayout());
	contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	getContentPane().add(contentPanel, BorderLayout.CENTER);
	GridBagLayout gbl_contentPanel = new GridBagLayout();
	gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
	gbl_contentPanel.rowHeights = new int[] { 0, 0, 0 };
	gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
	gbl_contentPanel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
	contentPanel.setLayout(gbl_contentPanel);
	{
	    JLabel lblName = new JLabel("Name:");
	    GridBagConstraints gbc_lblName = new GridBagConstraints();
	    gbc_lblName.insets = new Insets(0, 0, 5, 5);
	    gbc_lblName.anchor = GridBagConstraints.EAST;
	    gbc_lblName.gridx = 0;
	    gbc_lblName.gridy = 0;
	    contentPanel.add(lblName, gbc_lblName);
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
	    JLabel lblDescription = new JLabel("Description:");
	    GridBagConstraints gbc_lblDescription = new GridBagConstraints();
	    gbc_lblDescription.anchor = GridBagConstraints.NORTHEAST;
	    gbc_lblDescription.insets = new Insets(0, 0, 0, 5);
	    gbc_lblDescription.gridx = 0;
	    gbc_lblDescription.gridy = 1;
	    contentPanel.add(lblDescription, gbc_lblDescription);
	}
	{
	    JScrollPane scrollPane = new JScrollPane();
	    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
	    gbc_scrollPane.fill = GridBagConstraints.BOTH;
	    gbc_scrollPane.gridx = 1;
	    gbc_scrollPane.gridy = 1;
	    contentPanel.add(scrollPane, gbc_scrollPane);
	    {
		textDescription = new JTextArea();
		scrollPane.setViewportView(textDescription);
		textDescription.setWrapStyleWord(true);
		textDescription.setLineWrap(true);
	    }
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String command = e.getActionCommand();
	if ("OK".equals(command)) {
	    doOK();
	} else if ("Cancel".equals(command)) {
	    doCancel();
	}
    }

    private void doOK() {
	MediaLibraryController controller = new MediaLibraryController();
	child = controller.createCollection(textName.getText(), textDescription.getText());
	if (child == null) {
	    JOptionPane.showMessageDialog(this, "Failed to create the collection.");
	    return;
	}
	if (parent != null) {
	    // We are adding this collection to another collection
	    if (!controller.addCollectionToCollection(parent.getId(), child.getId())) {
		JOptionPane.showMessageDialog(this, "Failed to add the collection to the selected collection.");
		return;
	    }
	}
	dispose();
    }

    private void doCancel() {
	dispose();
    }

}

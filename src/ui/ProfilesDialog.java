package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;

import model.Profile;

import controller.MediaLibraryController;

public class ProfilesDialog extends JDialog implements ActionListener, MouseListener {

    private final JPanel contentPanel = new JPanel();
    private JList list;
    
    boolean profilesSwitched = false;
    boolean currentProfileModified = false;
    Profile currentProfile;
    
    /**
     * Create the dialog.
     * @param profile 
     */
    public ProfilesDialog(Profile profile) {
	this.currentProfile = profile;
	setModal(true);
	setTitle("Profiles");
	setBounds(100, 100, 450, 300);
	getContentPane().setLayout(new BorderLayout());
	contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	getContentPane().add(contentPanel, BorderLayout.CENTER);
	GridBagLayout gbl_contentPanel = new GridBagLayout();
	gbl_contentPanel.columnWidths = new int[] { 0, 0 };
	gbl_contentPanel.rowHeights = new int[] { 0, 0 };
	gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
	gbl_contentPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
	contentPanel.setLayout(gbl_contentPanel);
	{
	    JScrollPane scrollPane = new JScrollPane();
	    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
	    gbc_scrollPane.fill = GridBagConstraints.BOTH;
	    gbc_scrollPane.gridx = 0;
	    gbc_scrollPane.gridy = 0;
	    contentPanel.add(scrollPane, gbc_scrollPane);
	    {
		DefaultListModel listModel = new DefaultListModel();
		list = new JList(listModel);
		scrollPane.setViewportView(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    }
	}
	{
	    JPanel buttonPane = new JPanel();
	    buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
	    getContentPane().add(buttonPane, BorderLayout.SOUTH);
	    {
		JButton btnNewProfile = new JButton("New Profile");
		btnNewProfile.setActionCommand("New Profile");
		btnNewProfile.addActionListener(this);
		buttonPane.add(btnNewProfile);
	    }
	    {
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	    }
	}
	
	list.addMouseListener(this);

	updateList();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String command = e.getActionCommand();
	if ("New Profile".equals(command)) {
	    doNewProfile();
	} else if ("OK".equals(command)) {
	    dispose();
	} else if ("Switch".equals(command)) {
	    doSwitch();
	} else if ("Edit".equals(command)) {
	    doEdit();
	} else if ("Delete".equals(command)) {
	    doDelete();
	}
    }

    private void doSwitch() {
	Profile profile = (Profile)list.getSelectedValue();
	if (profile == null) {
	    JOptionPane.showMessageDialog(this, "Please make a selection.");
	    return;
	}
	profilesSwitched = MediaLibraryController.setCurrentProfile(profile.getId());
	dispose();
    }

    private void doDelete() {
	Profile profile = (Profile)list.getSelectedValue();
	if(profile == null){
	    JOptionPane.showMessageDialog(this, "Please select a profile.");
	    return;
	}
	else if(profile.equals(currentProfile)){
	    JOptionPane.showMessageDialog(this, "Can't delete current profile.");
	    return;
	}
	
	MediaLibraryController controller = new MediaLibraryController();
	if(controller.deleteProfile(profile.getId())){
	    updateList();
	}
    }

    private void doEdit() {
	Profile profile = (Profile)list.getSelectedValue();
	if(profile == null){
	    JOptionPane.showMessageDialog(this, "Please select a profile.");
	    return;
	}
	EditProfileDialog editDialog = new EditProfileDialog(profile);
	editDialog.setVisible(true);
	updateList();
	if(profile.equals(currentProfile) && editDialog.profileModified){
	    currentProfileModified = true;
	}
    }

    private void doNewProfile() {
	NewProfileDialog dialog = new NewProfileDialog();
	dialog.setVisible(true);
	updateList();
    }

    private void updateList() {
	DefaultListModel listModel = ((DefaultListModel) list.getModel());
	listModel.removeAllElements();
	for (Profile profile : new MediaLibraryController().getProfiles()) {
	    listModel.addElement(profile);
	}
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
	if (e.isPopupTrigger()) {
	    showContextMenu(e);
	}
    }

    private void showContextMenu(MouseEvent e) {
	if (list.equals(e.getSource())) {
	    list.setSelectedIndex(list.locationToIndex(e.getPoint()));
	    
	    JPopupMenu contextMenu = new JPopupMenu();

	    JMenuItem mntmUse = new JMenuItem("Switch to Profile");
	    mntmUse.setActionCommand("Switch");
	    mntmUse.addActionListener(this);
	    contextMenu.add(mntmUse);
	    
	    JMenuItem mntmEdit = new JMenuItem("Edit");
	    mntmEdit.setActionCommand("Edit");
	    mntmEdit.addActionListener(this);
	    contextMenu.add(mntmEdit);

	    JMenuItem mntmDelete = new JMenuItem("Delete");
	    mntmDelete.setActionCommand("Delete");
	    mntmDelete.addActionListener(this);
	    contextMenu.add(mntmDelete);

	    contextMenu.show(list, e.getX(), e.getY());
	}
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	if (e.isPopupTrigger()) {
	    showContextMenu(e);
	}
    }

}

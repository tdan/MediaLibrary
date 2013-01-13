package ui;

import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.WindowConstants;

import controller.MediaLibraryController;
import database.DatabaseSupport;
import javax.swing.JTree;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreePath;

import model.Collection;
import model.MediaItem;
import model.Profile;

public class ClientWindow implements ActionListener, WindowListener, MouseListener {

    /**
     * Launch the application.
     * 
     * @param args
     *            application arguments
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    ClientWindow window = new ClientWindow();
		    window.frmMediaLibrary.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    MediaLibraryController controller = new MediaLibraryController();
    private JFrame frmMediaLibrary;

    private JTree tree;

    /**
     * Create the application.
     */
    public ClientWindow() {
	initialize();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String command = e.getActionCommand();
	if ("New Collection".equals(command)) {
	    doNewCollection();
	} else if ("Edit Collection".equals(command)) {
	    doEditCollection();
	} else if ("Delete Collection".equals(command)) {
	    doDeleteCollection();
	} else if ("New Media Item".equals(command)) {
	    doNewMediaItem();
	} else if ("Edit Media Item".equals(command)) {
	    doEditMediaItem();
	} else if ("Delete Media Item".equals(command)) {
	    doDeleteMediaItem();
	} else if ("About".equals(command)) {
	    doAbout();
	} else if ("Profiles...".equals(command)) {
	    doProfiles();
	} else if ("Exit".equals(command)) {
	    doExit();
	}
    }

    private void doAbout() {
	AboutDialog dialog = new AboutDialog();
	dialog.setLocationRelativeTo(frmMediaLibrary);
	dialog.setVisible(true);
    }

    private void doDeleteCollection() {
	TreePath path = tree.getSelectionPath();
	if (path == null) {
	    JOptionPane.showMessageDialog(frmMediaLibrary, "Please make a selection.");
	    return;
	}
	Object node = path.getLastPathComponent();
	if (!(node instanceof Collection))
	    return;
	int choice = JOptionPane.showConfirmDialog(frmMediaLibrary, "Are you sure?", "Select and Option",
		JOptionPane.YES_NO_OPTION);
	if (choice == JOptionPane.YES_OPTION) {
	    TreePath parentPath = path.getParentPath();
	    int index = controller.getIndexOfChild(parentPath.getLastPathComponent(), node);
	    if (controller.deleteCollection(((Collection) node).getId())) {
		controller.fireTreeNodesRemoved(new TreeModelEvent(controller, parentPath, new int[] { index },
			new Object[] { node }));
	    }
	}
    }

    private void doDeleteMediaItem() {
	TreePath path = tree.getSelectionPath();
	if (path == null) {
	    JOptionPane.showMessageDialog(frmMediaLibrary, "Please make a selection.");
	    return;
	}
	Object node = path.getLastPathComponent();
	if (!(node instanceof MediaItem))
	    return;
	int choice = JOptionPane.showConfirmDialog(frmMediaLibrary, "Are you sure?", "Select and Option",
		JOptionPane.YES_NO_OPTION);
	if (choice == JOptionPane.YES_OPTION) {
	    TreePath parentPath = path.getParentPath();
	    int index = controller.getIndexOfChild(parentPath.getLastPathComponent(), node);
	    if (controller.deleteMediaItem(((MediaItem) node).getId())) {
		controller.fireTreeNodesRemoved(new TreeModelEvent(controller, parentPath, new int[] { index },
			new Object[] { node }));
	    }
	}
    }

    private void doEditCollection() {
	TreePath path = tree.getSelectionPath();
	if (path == null) {
	    JOptionPane.showMessageDialog(frmMediaLibrary, "Please make a selection.");
	    return;
	}
	Object node = path.getLastPathComponent();
	if (node instanceof Collection) {
	    EditCollectionDialog dialog = new EditCollectionDialog((Collection) node);
	    dialog.setLocationRelativeTo(frmMediaLibrary);
	    dialog.setVisible(true); // Does not return until the dialog is disposed
	    TreePath parentPath = path.getParentPath();
	    controller.fireTreeNodesChanged(new TreeModelEvent(controller, parentPath, new int[] { controller
		    .getIndexOfChild(parentPath.getLastPathComponent(), dialog.collection) },
		    new Object[] { dialog.collection }));
	}
    }

    private void doEditMediaItem() {
	TreePath path = tree.getSelectionPath();
	if (path == null) {
	    JOptionPane.showMessageDialog(frmMediaLibrary, "Please make a selection.");
	    return;
	}
	Object node = path.getLastPathComponent();
	if (node instanceof MediaItem) {
	    EditMediaItemDialog dialog = new EditMediaItemDialog((MediaItem) node);
	    dialog.setLocationRelativeTo(frmMediaLibrary);
	    dialog.setVisible(true); // Does not return until the dialog is disposed
	    TreePath parentPath = path.getParentPath();
	    controller.fireTreeNodesChanged(new TreeModelEvent(controller, parentPath, new int[] { controller
		    .getIndexOfChild(parentPath.getLastPathComponent(), dialog.mediaItem) },
		    new Object[] { dialog.mediaItem }));
	}
    }

    private void doExit() {
	frmMediaLibrary.dispose();
    }

    private void doNewCollection() {
	TreePath path = tree.getSelectionPath();
	if (path == null) {
	    JOptionPane.showMessageDialog(frmMediaLibrary, "Please make a selection.");
	    return;
	}
	Object node = path.getLastPathComponent();
	if (node instanceof MediaItem) {
	    JOptionPane.showMessageDialog(frmMediaLibrary, "You cannot add a collection to a media item.");
	    return;
	}
	NewCollectionDialog dialog;
	if (node instanceof Collection) {
	    dialog = new NewCollectionDialog((Collection) node);
	} else {
	    dialog = new NewCollectionDialog();
	}
	dialog.setLocationRelativeTo(frmMediaLibrary);
	dialog.setVisible(true); // Does not return until the dialog is disposed
	if (dialog.child != null) {
	    controller.fireTreeNodesInserted(new TreeModelEvent(controller, path, new int[] { controller
		    .getIndexOfChild(node, dialog.child) }, new Object[] { dialog.child }));
	}
    }

    private void doNewMediaItem() {
	TreePath path = tree.getSelectionPath();
	if (path == null) {
	    JOptionPane.showMessageDialog(frmMediaLibrary, "Please make a selection.");
	    return;
	}
	Object parent = path.getLastPathComponent();
	if (parent instanceof MediaItem) {
	    JOptionPane.showMessageDialog(frmMediaLibrary, "You cannot add a media item to another media item");
	    return;
	}
	NewMediaItemDialog dialog;
	if (parent instanceof Collection) {
	    dialog = new NewMediaItemDialog((Collection) parent);
	} else {
	    dialog = new NewMediaItemDialog();
	}
	dialog.setLocationRelativeTo(frmMediaLibrary);
	dialog.setVisible(true); // Does not return until the dialog is disposed
	if (dialog.child != null) {
	    controller.fireTreeNodesInserted(new TreeModelEvent(controller, path, new int[] { controller
		    .getIndexOfChild(parent, dialog.child) }, new Object[] { dialog.child }));
	}
    }

    private void doProfiles() {
	Profile currentProfile = new MediaLibraryController().getCurrentProfile();
	ProfilesDialog dialog = new ProfilesDialog(currentProfile);
	dialog.setLocationRelativeTo(frmMediaLibrary);
	dialog.setVisible(true);

	if (dialog.profilesSwitched || dialog.currentProfileModified) {
	    TreePath rootPath = tree.getPathForRow(0);
	    controller.fireTreeStructureChanged(new TreeModelEvent(controller, rootPath));
	}
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
	DatabaseSupport.delete(); // Comment this out if you don't want to wipe
				  // our your db every time
	DatabaseSupport.open();

	frmMediaLibrary = new JFrame();
	frmMediaLibrary.setTitle("Media Library");
	frmMediaLibrary.setBounds(100, 100, 800, 600);
	frmMediaLibrary.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	JMenuBar menuBar = new JMenuBar();
	frmMediaLibrary.setJMenuBar(menuBar);

	JMenu mnFile = new JMenu("File");
	menuBar.add(mnFile);

	JMenu mnNew = new JMenu("New...");
	mnFile.add(mnNew);

	JMenuItem mntmCollection = new JMenuItem("Collection");
	mntmCollection.setActionCommand("New Collection");
	mntmCollection.addActionListener(this);
	mnNew.add(mntmCollection);

	JMenuItem mntmMediaItem = new JMenuItem("Media Item");
	mntmMediaItem.setActionCommand("New Media Item");
	mntmMediaItem.addActionListener(this);
	mnNew.add(mntmMediaItem);

	JMenuItem mntmExit = new JMenuItem("Exit");
	mntmExit.setActionCommand("Exit");
	mntmExit.addActionListener(this);

	JMenuItem mntmProfiles = new JMenuItem("Profiles...");
	mntmProfiles.setActionCommand("Profiles...");
	mntmProfiles.addActionListener(this);
	mnFile.add(mntmProfiles);
	mnFile.add(mntmExit);

	JMenu mnHelp = new JMenu("Help");
	menuBar.add(mnHelp);

	JMenuItem mntmAbout = new JMenuItem("About");
	mntmAbout.setActionCommand("About");
	mntmAbout.addActionListener(this);
	mnHelp.add(mntmAbout);

	JScrollPane scrollPane = new JScrollPane();
	frmMediaLibrary.getContentPane().add(scrollPane, BorderLayout.CENTER);

	tree = new JTree(controller);
	scrollPane.setViewportView(tree);

	frmMediaLibrary.addWindowListener(this);
	tree.addMouseListener(this);

	tree.setSelectionRow(0);
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

    @Override
    public void mouseReleased(MouseEvent e) {
	if (e.isPopupTrigger()) {
	    showContextMenu(e);
	}
    }

    private void showCollectionContextMenu(MouseEvent e) {
	JPopupMenu contextMenu = new JPopupMenu();

	JMenuItem mntmEdit = new JMenuItem("Edit");
	mntmEdit.setActionCommand("Edit Collection");
	mntmEdit.addActionListener(this);
	contextMenu.add(mntmEdit);

	JMenuItem mntmNewCollection = new JMenuItem("New Collection");
	mntmNewCollection.setActionCommand("New Collection");
	mntmNewCollection.addActionListener(this);
	contextMenu.add(mntmNewCollection);

	JMenuItem mntmNewMediaItem = new JMenuItem("New Media Item");
	mntmNewMediaItem.setActionCommand("New Media Item");
	mntmNewMediaItem.addActionListener(this);
	contextMenu.add(mntmNewMediaItem);

	JMenuItem mntmDelete = new JMenuItem("Delete");
	mntmDelete.setActionCommand("Delete Collection");
	mntmDelete.addActionListener(this);
	contextMenu.add(mntmDelete);

	contextMenu.show(tree, e.getX(), e.getY());
    }

    private void showContextMenu(MouseEvent e) {
	if (tree.equals(e.getSource())) {
	    TreePath path = tree.getPathForLocation(e.getX(), e.getY());
	    if (path == null)
		return;
	    Object node = path.getLastPathComponent();
	    tree.setSelectionPath(path);
	    if (node instanceof Profile)
		showProfileContextMenu(e);
	    if (node instanceof Collection)
		showCollectionContextMenu(e);
	    if (node instanceof MediaItem)
		showMediaItemContextMenu(e);
	}
    }

    private void showMediaItemContextMenu(MouseEvent e) {
	JPopupMenu contextMenu = new JPopupMenu();

	JMenuItem mntmEdit = new JMenuItem("Edit");
	mntmEdit.setActionCommand("Edit Media Item");
	mntmEdit.addActionListener(this);
	contextMenu.add(mntmEdit);

	JMenuItem mntmDelete = new JMenuItem("Delete");
	mntmDelete.setActionCommand("Delete Media Item");
	mntmDelete.addActionListener(this);
	contextMenu.add(mntmDelete);

	contextMenu.show(tree, e.getX(), e.getY());
    }

    private void showProfileContextMenu(MouseEvent e) {
	JPopupMenu contextMenu = new JPopupMenu();

	JMenuItem mntmNewCollection = new JMenuItem("New Collection");
	mntmNewCollection.setActionCommand("New Collection");
	mntmNewCollection.addActionListener(this);
	contextMenu.add(mntmNewCollection);

	JMenuItem mntmNewMediaItem = new JMenuItem("New Media Item");
	mntmNewMediaItem.setActionCommand("New Media Item");
	mntmNewMediaItem.addActionListener(this);
	contextMenu.add(mntmNewMediaItem);

	contextMenu.show(tree, e.getX(), e.getY());
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
	Window window = e.getWindow();
	if (frmMediaLibrary.equals(window)) {
	    DatabaseSupport.close();
	    System.exit(0);
	}
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

}

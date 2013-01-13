package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class AboutDialog extends JDialog implements ActionListener {

    private final JPanel contentPanel = new JPanel();

    /**
     * Create the dialog.
     */
    public AboutDialog() {
	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	setModal(true);
	setTitle("About");
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
		JTextArea txtrThisApplicationWas = new JTextArea();
		txtrThisApplicationWas.setBackground(UIManager.getColor("Label.background"));
		txtrThisApplicationWas.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtrThisApplicationWas.setWrapStyleWord(true);
		txtrThisApplicationWas.setLineWrap(true);
		txtrThisApplicationWas
			.setText("The Media Library Project\r\n=========================\r\n\r\nA project by students Trung An, Cyle Dawson, and Gavin Monroe for the COM S 362 course at Iowa State University in the spring of 2012");
		txtrThisApplicationWas.setEditable(false);
		scrollPane.setViewportView(txtrThisApplicationWas);
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
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	String command = e.getActionCommand();
	if ("OK".equals(command)) {
	    dispose();
	}
    }

}

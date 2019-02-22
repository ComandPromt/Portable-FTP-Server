/*
    This file is part of PortableFtpServer.

    PortableFtpServer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    PortableFtpServer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with PortableFtpServer.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.erc.pftps;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JFormattedTextField;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.SwingConstants;

import org.erc.pftps.services.FTPServer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JPasswordField;


/**
 * The Class MainWindow.
 */
public class MainWindow extends JFrame{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3422060991230203001L;
	
	/** The txt user. */
	private JTextField txtUser;
	
	/** The txt password. */
	private JPasswordField txtPassword;
	
	/** The txt folder. */
	private JTextField txtFolder;
	
	/** The txt port. */
	private JFormattedTextField txtPort;
	
	/** The txt log. */
	private JTextArea txtLog;
	
	/** The btn folder. */
	private JButton btnFolder;
	
	/** The btn start. */
	private JButton btnStart;
	
	/** The message console. */
	private MessageConsole messageConsole;
	
	/** The ftp server. */
	private FTPServer ftpServer;
	
	/** The scroll pane. */
	private JScrollPane scrollPane;
	
	/** The is started. */
	private boolean isStarted = false; 
	
	/** The chooser. */
	private JFileChooser chooser;
	
	/** Preferences file */
	private Preferences preferences = new Preferences();
	
	
	/**
	 * Create the application.
	 */
	public MainWindow() {

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Portable FTP Server");
		setBounds(10, 10, 530, 480);
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - getHeight()) / 2);
	    setLocation(x, y);
	    
		chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Portable FTP Server - Home Folder");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	    
		getContentPane().setLayout(null);
		
		JLabel lblUser = new JLabel("User");
		lblUser.setFont(new Font("Dialog", Font.BOLD, 18));
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblUser.setBounds(12, 73, 71, 32);
		getContentPane().add(lblUser);
		
		txtUser = new JTextField();
		txtUser.setHorizontalAlignment(SwingConstants.CENTER);
		txtUser.setBounds(87, 75, 86, 32);
		getContentPane().add(txtUser);
		txtUser.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Dialog", Font.BOLD, 18));
		lblPassword.setBounds(180, 78, 120, 22);
		getContentPane().add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setHorizontalAlignment(SwingConstants.CENTER);
		txtPassword.setColumns(10);
		txtPassword.setBounds(308, 75, 86, 32);
		getContentPane().add(txtPassword);
		
		txtFolder = new JTextField();
		txtFolder.setHorizontalAlignment(SwingConstants.CENTER);
		txtFolder.setFont(new Font("Dialog", Font.BOLD, 12));
		txtFolder.setEditable(false);
		txtFolder.setBounds(21, 10, 451, 38);
		getContentPane().add(txtFolder);
		txtFolder.setColumns(10);
		
		btnFolder = new JButton("...");
		btnFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 if (chooser.showOpenDialog(btnFolder) == JFileChooser.APPROVE_OPTION) {
					 txtFolder.setText(chooser.getSelectedFile().getAbsolutePath());
				 }    
			}
		});
		btnFolder.setBounds(484, 10, 33, 38);
		getContentPane().add(btnFolder);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setFont(new Font("Dialog", Font.BOLD, 18));
		lblPort.setBounds(200, 134, 46, 14);
		getContentPane().add(lblPort);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Start
				if(!isStarted){
					
					// Validate fielts
					if(txtUser.getText()!=null && !txtUser.getText().isEmpty() && 
						txtFolder.getText()!=null && !txtFolder.getText().isEmpty()){
						
						// Store preferences
						preferences.set("FTP.PORT", txtPort.getText());
						preferences.set("FTP.USER", txtUser.getText());
						preferences.set("FTP.PASSWORD", new String(txtPassword.getPassword()));
						preferences.set("FTP.FOLDER", txtFolder.getText());
						
						// Configure and start
						ftpServer.setPort(Integer.parseInt(txtPort.getText()));
						ftpServer.setUser(txtUser.getText(), txtPassword.getPassword(), txtFolder.getText());
						if (ftpServer.start()){
							isStarted = true;
							txtPort.setEnabled(false);
							txtUser.setEnabled(false);
							txtPassword.setEnabled(false);
							btnFolder.setEnabled(false);
							btnStart.setText("Stop");
						}else{
							System.err.println("Error starting server");	
						}
					}else{
						System.err.println("Invalid port,user or folder");
					}
					
				// Stop
				}else{
					ftpServer.stop();
					isStarted = false;
					txtPort.setEnabled(true);
					txtUser.setEnabled(true);
					txtPassword.setEnabled(true);
					btnFolder.setEnabled(true);
					btnStart.setText("Start");
				}
			}
		});
		btnStart.setBounds(423, 74, 94, 32);
		getContentPane().add(btnStart);
		
		txtPort = new JFormattedTextField();
		txtPort.setHorizontalAlignment(SwingConstants.CENTER);
		txtPort.setColumns(5);
		txtPort.setBounds(308, 128, 86, 30);
		getContentPane().add(txtPort);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 170, 496, 268);
		getContentPane().add(scrollPane);
		
				txtLog = new JTextArea();
				scrollPane.setViewportView(txtLog);
				txtLog.setText("Ready.");
				txtLog.setForeground(Color.LIGHT_GRAY);
				txtLog.setBackground(Color.BLACK);
				txtLog.setFont(new Font("Monospaced", Font.PLAIN, 10));
				txtLog.setColumns(20);
				txtLog.setRows(20);
				txtLog.setEditable(false);
				
				messageConsole = new MessageConsole(txtLog);
		messageConsole.setMessageLines(100);
		messageConsole.redirectErr();
		messageConsole.redirectOut();
		
		ftpServer = new FTPServer();
		
		// load preferences
		txtPort.setText(preferences.getString("FTP.PORT", "22"));
		txtFolder.setText(preferences.getString("FTP.FOLDER", ""));
		

	}
}

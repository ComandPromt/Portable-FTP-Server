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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.erc.pftps.services.FTPServer;
import org.erc.pftps.services.Metodos;

/**
 * The Class MainWindow.
 */
public class MainWindow extends JFrame {

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
	 * 
	 * @throws UnknownHostException
	 */

	public MainWindow() throws UnknownHostException {

		getContentPane().setBackground(Color.WHITE);

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Portable FTP Server");
		setBounds(10, 10, 555, 480);

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

		JLabel lblUser = new JLabel("");
		lblUser.setIcon(new ImageIcon(MainWindow.class.getResource("/imagenes/user.png")));
		lblUser.setFont(new Font("Dialog", Font.BOLD, 16));
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblUser.setBounds(20, 108, 119, 48);
		getContentPane().add(lblUser);

		txtUser = new JTextField();
		txtUser.setHorizontalAlignment(SwingConstants.CENTER);
		txtUser.setBounds(149, 124, 86, 32);
		getContentPane().add(txtUser);
		txtUser.setColumns(10);

		JLabel lblPassword = new JLabel("");
		lblPassword.setIcon(new ImageIcon(MainWindow.class.getResource("/imagenes/lock.png")));
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setFont(new Font("Dialog", Font.BOLD, 16));
		lblPassword.setBounds(20, 165, 119, 48);
		getContentPane().add(lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setHorizontalAlignment(SwingConstants.CENTER);
		txtPassword.setColumns(10);
		txtPassword.setBounds(149, 181, 86, 32);
		getContentPane().add(txtPassword);

		txtFolder = new JTextField();
		txtFolder.setBackground(SystemColor.menu);
		txtFolder.setHorizontalAlignment(SwingConstants.CENTER);
		txtFolder.setFont(new Font("Dialog", Font.BOLD, 14));
		txtFolder.setEditable(false);
		txtFolder.setBounds(20, 71, 408, 32);
		getContentPane().add(txtFolder);
		txtFolder.setColumns(10);

		btnFolder = new JButton("");
		btnFolder.setIcon(new ImageIcon(MainWindow.class.getResource("/imagenes/folder.png")));
		btnFolder.setFont(new Font("Tahoma", Font.BOLD, 16));

		btnFolder.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (chooser.showOpenDialog(btnFolder) == JFileChooser.APPROVE_OPTION) {
					txtFolder.setText(chooser.getSelectedFile().getAbsolutePath());
				}

			}

		});

		btnFolder.setBounds(438, 71, 87, 31);
		getContentPane().add(btnFolder);

		JLabel lblPort = new JLabel("");
		lblPort.setIcon(new ImageIcon(MainWindow.class.getResource("/imagenes/port.png")));
		lblPort.setHorizontalAlignment(SwingConstants.CENTER);
		lblPort.setFont(new Font("Dialog", Font.BOLD, 16));
		lblPort.setBounds(239, 108, 93, 48);
		getContentPane().add(lblPort);

		btnStart = new JButton("");

		btnStart.setIcon(new ImageIcon(MainWindow.class.getResource("/imagenes/start.png")));

		btnStart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// Start
				if (!isStarted) {

					// Validate fielts
					if (txtUser.getText() != null && !txtUser.getText().isEmpty() && txtFolder.getText() != null
							&& !txtFolder.getText().isEmpty()) {

						// Store preferences
						preferences.set("FTP.PORT", txtPort.getText());
						preferences.set("FTP.USER", txtUser.getText());
						preferences.set("FTP.PASSWORD", new String(txtPassword.getPassword()));
						preferences.set("FTP.FOLDER", txtFolder.getText());

						// Configure and start
						ftpServer.setPort(Integer.parseInt(txtPort.getText()));
						ftpServer.setUser(txtUser.getText(), txtPassword.getPassword(), txtFolder.getText());

						if (ftpServer.start()) {
							isStarted = true;
							txtPort.setEnabled(false);
							txtUser.setEnabled(false);
							txtPassword.setEnabled(false);
							btnFolder.setEnabled(false);
							btnStart.setIcon(new ImageIcon(MainWindow.class.getResource("/imagenes/stop.png")));
						}

						else {
							Metodos.mensaje("Error starting server", 1);

						}

					}

					else {
						Metodos.mensaje("Invalid port, user or folder", 1);
					}

					// Stop
				}

				else {
					ftpServer.stop();
					isStarted = false;
					txtPort.setEnabled(true);
					txtUser.setEnabled(true);
					txtPassword.setEnabled(true);
					btnFolder.setEnabled(true);
					btnStart.setIcon(new ImageIcon(MainWindow.class.getResource("/imagenes/start.png")));
				}

			}

		});

		btnStart.setBounds(438, 138, 87, 67);
		getContentPane().add(btnStart);

		txtPort = new JFormattedTextField();
		txtPort.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtPort.setHorizontalAlignment(SwingConstants.CENTER);
		txtPort.setColumns(5);
		txtPort.setBounds(327, 126, 101, 30);
		getContentPane().add(txtPort);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 237, 503, 188);
		getContentPane().add(scrollPane);

		txtLog = new JTextArea();
		scrollPane.setViewportView(txtLog);
		txtLog.setText("Ready.");
		txtLog.setForeground(Color.LIGHT_GRAY);
		txtLog.setBackground(Color.BLACK);
		txtLog.setFont(new Font("Monospaced", Font.PLAIN, 14));
		txtLog.setColumns(20);
		txtLog.setRows(20);
		txtLog.setEditable(false);

		messageConsole = new MessageConsole(txtLog);
		messageConsole.setMessageLines(100);
		messageConsole.redirectErr();
		messageConsole.redirectOut();

		ftpServer = new FTPServer();

		// load preferences
		txtPort.setText("21");
		txtFolder.setText(preferences.getString("FTP.FOLDER", ""));

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 524, 62);
		getContentPane().add(menuBar);

		JMenu mnNewMenu = new JMenu(
				"Acciones                                                                         ");
		mnNewMenu.setForeground(Color.BLACK);
		mnNewMenu.setIcon(new ImageIcon(MainWindow.class.getResource("/imagenes/settings.png")));
		mnNewMenu.setFont(new Font("Segoe UI", Font.BOLD, 16));
		menuBar.add(mnNewMenu);

		final JMenuItem mntmNewMenuItem_2 = new JMenuItem("Ocultar log");
		mntmNewMenuItem_2.setIcon(new ImageIcon(MainWindow.class.getResource("/imagenes/hide.png")));

		mntmNewMenuItem_2.addMouseListener(new MouseAdapter() {

			@Override

			public void mousePressed(MouseEvent e) {

				if (scrollPane.isVisible()) {

					mntmNewMenuItem_2.setText("Ver log");

					scrollPane.setVisible(false);

					txtLog.setVisible(false);

				}

				else {

					mntmNewMenuItem_2.setText("Ocultar log");

					scrollPane.setVisible(true);

					txtLog.setVisible(true);

				}

			}

		});

		mntmNewMenuItem_2.setFont(new Font("Segoe UI", Font.BOLD, 16));
		mnNewMenu.add(mntmNewMenuItem_2);

		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(245, 183, 183, 30);
		getContentPane().add(lblNewLabel_1);

		InetAddress address = InetAddress.getLocalHost();
		lblNewLabel_1.setText(address.getHostAddress());

	}
}

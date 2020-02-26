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

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.erc.pftps.services.FTPServer;

/**
 * The Class Start.
 */
public class Start {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");

		if(args == null || args.length<1){
	       java.awt.EventQueue.invokeLater ( new Runnable() {
		        public void run() {
		    		try{
		    			JFrame.setDefaultLookAndFeelDecorated(true);
		    			JDialog.setDefaultLookAndFeelDecorated(true);
		    			System.setProperty("sun.awt.noerasebackground", "true");
		    			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						MainWindow window = new MainWindow();
						window.setVisible(true);
					} catch (Exception e) {
						//
					}
		        }
	        });
		}else{
			if (args.length == 4){
				FTPServer server = new FTPServer();
				server.setPort(Integer.parseInt(args[0]));
				server.setUser(args[1], args[2].toCharArray(), args[3]);
				server.start();
			} else{
				System.out.println("Invalid arguments");
				System.out.println("");
				System.out.println("Use without arguments for GUI or call with:");
				System.out.println("<port> <user> <password> <folder>");
			}
		}
	}

}

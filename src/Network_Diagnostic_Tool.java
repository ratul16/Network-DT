import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;

import javax.swing.JTextArea;
import java.awt.Color;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Network_Diagnostic_Tool {

	private JFrame frmNetwordDiagnosticTool;
	private JTextField textInput;
	private JTextField oct1;
	private JTextField oct2;
	private JTextField oct3;
	private JTextField oct4;
	private JTextField mask;
	private JLabel lblEnterIpAddress;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Network_Diagnostic_Tool window = new Network_Diagnostic_Tool();
					window.frmNetwordDiagnosticTool.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Network_Diagnostic_Tool() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNetwordDiagnosticTool = new JFrame();
		frmNetwordDiagnosticTool.setTitle("Netword Diagnostic Tool");
		frmNetwordDiagnosticTool.setBounds(100, 100, 548, 739);
		frmNetwordDiagnosticTool.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNetwordDiagnosticTool.getContentPane().setLayout(null);

		JLabel lblTitle = new JLabel("Enter your URL or IP Address");
		lblTitle.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblTitle.setBounds(151, 11, 267, 34);
		frmNetwordDiagnosticTool.getContentPane().add(lblTitle);

		textInput = new JTextField();
		textInput.setBounds(10, 56, 512, 34);
		frmNetwordDiagnosticTool.getContentPane().add(textInput);
		textInput.setColumns(10);
		final JTextArea textResult = new JTextArea();
		textResult.setBackground(Color.LIGHT_GRAY);

		JButton btnPing = new JButton("Ping ");
		btnPing.setFont(new Font("Comic Sans MS", Font.ITALIC, 12));
		btnPing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { // Ping Command and
															// output
				String p1 = textInput.getText();
				String test = "ping " + p1;

				Process sysProcess = null;
				try {
					sysProcess = Runtime.getRuntime().exec(test);
				} catch (IOException e2) {
					System.err.println("Error");

				}

				BufferedReader pingreader = new BufferedReader(new InputStreamReader(sysProcess.getInputStream()));

				String line;
				String nextline = "\n";
				try {
					while ((line = pingreader.readLine()) != null) {

						// System.out.println(line);

						textResult.append(line + nextline);
						BufferedWriter bw = new BufferedWriter(new FileWriter("ping_log.txt"));
						bw.write(textResult.getText());
						bw.close();

					}
				} catch (IOException e1) {
					System.err.println("Error");
				}

			}
		});
		btnPing.setBounds(55, 101, 137, 34);
		frmNetwordDiagnosticTool.getContentPane().add(btnPing);

		JButton btnTracret = new JButton("Trace Route");
		btnTracret.setFont(new Font("Comic Sans MS", Font.ITALIC, 12));
		btnTracret.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) { // Trace-route
															// Command and
															// output
				String t1 = textInput.getText();
				String test = "tracert -d " + t1;

				Process sysProcess = null;
				try {
					sysProcess = Runtime.getRuntime().exec(test);
				} catch (IOException e2) {
					System.err.println("Error");

				}

				BufferedReader pingreader = new BufferedReader(new InputStreamReader(sysProcess.getInputStream()));

				String line;
				String nextline = "\n";
				try {
					while ((line = pingreader.readLine()) != null) {

						// System.out.println(line);
						textResult.append(line + nextline);
						BufferedWriter bw1 = new BufferedWriter(new FileWriter("tracert_log.txt"));
						bw1.write(textResult.getText());
						bw1.close();
					}
				} catch (IOException e1) {
					System.err.println("Error");
				}
			}
		});
		btnTracret.setBounds(335, 101, 137, 34);
		frmNetwordDiagnosticTool.getContentPane().add(btnTracret);

		JButton btnSubnet = new JButton("Calculate Subnet");
		btnSubnet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) { // Subnet Calculator
				int prefix = Integer.valueOf(mask.getText());
				int pos = prefix / 8; // important octet position
				int rem = prefix % 8; // binary digit of importan octet

				String binIpFull = "";
				String subBinary = "";
				String subBinaryFull = "";
				String binnetidFull = "";
				String binbcidFull = "";
				String netmaskFull = "";
				String netidFull = "";
				String bcidFull = "";

				// loop to generate binary mask from octet pos
				for (int i = 1; i <= 8; i++) {
					if (i <= rem) {
						subBinary += "1";
					} else {
						subBinary += "0";
					}
				}

				// netmask (integer version of subBinary
				int netmask = Integer.parseInt(String.valueOf(subBinary), 2);

				// ip octets array
				int[] oct = { Integer.valueOf(oct1.getText()), Integer.valueOf(oct2.getText()),
						Integer.valueOf(oct3.getText()), Integer.valueOf(oct4.getText()) };

				// binary of important octet
				String binOct = String.format("%8s", Integer.toBinaryString(oct[pos])).replace(' ', '0');

				String binNetId = "";
				String binBcId = "";

				for (int i = 0; i < 8; i++) {
					String binPointer = subBinary.substring(i, i + 1);
					if (binPointer.equals("1")) {
						binNetId += binOct.substring(i, i + 1);
						binBcId += binOct.substring(i, i + 1);
					} else {
						binNetId += "0";
						binBcId += "1";
					}
				}

				// loop to generate display-ready strings
				for (int i = 0; i <= 3; i++) {
					binIpFull += String.format("%8s", Integer.toBinaryString(oct[i])).replace(' ', '0');
					if (i < pos) {
						subBinaryFull += "11111111";
						netmaskFull += "255";
						binnetidFull += String.format("%8s", Integer.toBinaryString(oct[i])).replace(' ', '0');
						binbcidFull += String.format("%8s", Integer.toBinaryString(oct[i])).replace(' ', '0');
						netidFull += oct[i];
						bcidFull += oct[i];
					} else if (i > pos) {
						subBinaryFull += "00000000";
						netmaskFull += "0";
						binnetidFull += "00000000";
						binbcidFull += "00000000";
						netidFull += "0";
						bcidFull += "1";
					} else {
						subBinaryFull += subBinary;
						netmaskFull += String.valueOf(netmask);
						binnetidFull += binNetId;
						binbcidFull += binBcId;
						netidFull += Integer.parseInt(String.valueOf(binNetId), 2);
						bcidFull += Integer.parseInt(String.valueOf(binBcId), 2);
					}
					if (i != 3) {
						subBinaryFull += ".";
						netmaskFull += ".";
						binnetidFull += ".";
						binbcidFull += ".";
						netidFull += ".";
						bcidFull += ".";
						binIpFull += ".";
					}

				}

				textResult.append("Subnet Mask:  " + netmaskFull + "\n");
				textResult.append("Network ID:  " + netidFull + "\n");
				textResult.append("Broadcast ID:  " + bcidFull + "\n");

			}
		});
		btnSubnet.setFont(new Font("Comic Sans MS", Font.ITALIC, 12));
		btnSubnet.setBounds(151, 227, 228, 34);
		frmNetwordDiagnosticTool.getContentPane().add(btnSubnet);
		textResult.setBounds(10, 282, 512, 407);
		frmNetwordDiagnosticTool.getContentPane().add(textResult);

		oct1 = new JTextField();
		oct1.setBounds(10, 185, 86, 20);
		frmNetwordDiagnosticTool.getContentPane().add(oct1);
		oct1.setColumns(10);

		oct2 = new JTextField();
		oct2.setColumns(10);
		oct2.setBounds(106, 185, 86, 20);
		frmNetwordDiagnosticTool.getContentPane().add(oct2);

		oct3 = new JTextField();
		oct3.setColumns(10);
		oct3.setBounds(199, 185, 86, 20);
		frmNetwordDiagnosticTool.getContentPane().add(oct3);

		oct4 = new JTextField();
		oct4.setColumns(10);
		oct4.setBounds(293, 185, 86, 20);
		frmNetwordDiagnosticTool.getContentPane().add(oct4);

		mask = new JTextField();
		mask.setColumns(10);
		mask.setBounds(447, 185, 75, 20);
		frmNetwordDiagnosticTool.getContentPane().add(mask);

		JLabel label = new JLabel("/");
		label.setFont(new Font("Tahoma", Font.PLAIN, 13));
		label.setBounds(412, 184, 10, 20);
		frmNetwordDiagnosticTool.getContentPane().add(label);

		lblEnterIpAddress = new JLabel("Enter IP Address for calculating subnet");
		lblEnterIpAddress.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		lblEnterIpAddress.setBounds(132, 146, 267, 34);
		frmNetwordDiagnosticTool.getContentPane().add(lblEnterIpAddress);

		JMenuBar menuBar = new JMenuBar(); // Creating Menu bar
		frmNetwordDiagnosticTool.setJMenuBar(menuBar);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frmNetwordDiagnosticTool,
						"Version 1.3 \nDate: 4.10.2016 \nDeveloper: Sabit Zubair\n                     Hasibul Alam Ratul");
			}
		});
		mnHelp.add(mntmAbout);
	}
}

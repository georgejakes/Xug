import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class TestFrame extends JFrame {
	public TestFrame() {
	}

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//The Frame
		final JFrame mainFrame=new JFrame("Test1");		
		mainFrame.setSize(700, 400);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Tab Pane
		JTabbedPane jtp=new JTabbedPane();
		//Encode Panel
		JPanel panel1=new JPanel();
		panel1.setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.anchor=GridBagConstraints.WEST;
		gbc.insets=new Insets(2,0,2,0);
		int gbccounter=0;
		/*gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.fill=GridBagConstraints.HORIZONTAL;*/
		JLabel labBlank=new JLabel("    ");//For Blank spaces
		//Message to be inputed
		JLabel labMessage=new JLabel("Enter the Message Here");
		gbc.gridx=0;gbc.gridy=gbccounter;
		panel1.add(labMessage,gbc);
		final JTextField tfMessage=new JTextField(20);
		gbc.gridx=1;gbc.gridy=gbccounter++;
		panel1.add(tfMessage,gbc);
		//Cover File
		final JFileChooser fcCoverFile=new JFileChooser();
		JButton bCoverFile=new JButton("Browse");
		final JTextField tfCoverFile=new JTextField(20);
		bCoverFile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int returnVal=fcCoverFile.showOpenDialog(mainFrame);
				if(returnVal==JFileChooser.APPROVE_OPTION){
					java.io.File coverFile=fcCoverFile.getSelectedFile();
					tfCoverFile.setText(""+coverFile.getAbsolutePath());
				}
				else{
					tfCoverFile.setText("");
				}
			}
			
		});
		JLabel labCoverFile=new JLabel("Search Cover File");
		gbc.gridx=0;gbc.gridy=gbccounter;
		panel1.add(labCoverFile,gbc);
		gbc.gridx=1;gbc.gridy=gbccounter;
		panel1.add(tfCoverFile,gbc);
		gbc.gridx=2;gbc.gridy=gbccounter++;
		panel1.add(bCoverFile,gbc);
		
		//Output File
		JLabel labOutputFileDir=new JLabel("Search Output file directory");
		gbc.gridx=0;gbc.gridy=gbccounter;
		panel1.add(labOutputFileDir,gbc);
		final JTextField tfOutputFileDir=new JTextField(20);
		gbc.gridx=1;gbc.gridy=gbccounter;
		panel1.add(tfOutputFileDir,gbc);
		final JFileChooser fcOutputFileDir=new JFileChooser();
		fcOutputFileDir.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		JButton bOutputFileDir=new JButton("Browse");
		
		bOutputFileDir.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int returnVal=fcOutputFileDir.showOpenDialog(mainFrame);
				if(returnVal==JFileChooser.APPROVE_OPTION){
					java.io.File outputFileDir=fcOutputFileDir.getSelectedFile();
					tfOutputFileDir.setText(""+outputFileDir.getAbsolutePath());
				}
				else{
					tfOutputFileDir.setText("");
				}
			}
			
		});
		gbc.gridx=2;gbc.gridy=gbccounter++;
		panel1.add(bOutputFileDir,gbc);
		
		/*JLabel labOutputFile=new JLabel("Name for the Encrypted File");
		gbc.gridx=0;gbc.gridy=gbccounter;
		panel1.add(labOutputFile,gbc);
		final JTextField tfOutputFile=new JTextField(20);
		gbc.gridx=1;gbc.gridy=gbccounter++;
		panel1.add(tfOutputFile,gbc);
		*/
		//Message Limit
		JLabel labMsgLimit=new JLabel("Message Limit per Frame");
		gbc.gridx=0;gbc.gridy=gbccounter;
		panel1.add(labMsgLimit,gbc);
		final JTextField tfMsgLimit=new JTextField(20);
		gbc.gridx=1;gbc.gridy=gbccounter++;
		panel1.add(tfMsgLimit,gbc);
		//Cluster Blah
		JLabel labCluster=new JLabel("Cluster Number");
		gbc.gridx=0;gbc.gridy=gbccounter;
		panel1.add(labCluster,gbc);
		final JTextField tfCluster=new JTextField(20);
		gbc.gridx=1;gbc.gridy=gbccounter++;
		panel1.add(tfCluster,gbc);
		//Threshold
		JLabel labThreshold=new JLabel("Threshhold");
		gbc.gridx=0;gbc.gridy=gbccounter;
		panel1.add(labThreshold,gbc);
		final JTextField tfThreshold=new JTextField(20);
		gbc.gridx=1;gbc.gridy=gbccounter++;
		panel1.add(tfThreshold,gbc);
		//Password
		JLabel labPassword=new JLabel("Password");
		gbc.gridx=0;gbc.gridy=gbccounter;
		panel1.add(labPassword,gbc);
		final JPasswordField pfPassword=new JPasswordField(20);
		gbc.gridx=1;gbc.gridy=gbccounter++;
		panel1.add(pfPassword,gbc);
		JButton bEncode=new JButton("Encode");
		
		//Encode Button
		gbc.gridx=1;gbc.gridy=gbccounter++;
		panel1.add(bEncode,gbc);
		/*gbc.gridx=0;gbc.gridy=8;
		panel1.add(labBlank,gbc);
		gbc.gridx=1;gbc.gridy=9;
		panel1.add(labBlank,gbc);*/
		//Status
		JLabel labStatus=new JLabel("Status:");
		gbc.gridx=0;gbc.gridy=gbccounter;
		panel1.add(labStatus,gbc);
		
		final JTextField tfStatus=new JTextField(20);
		gbc.gridx=1;gbc.gridy=gbccounter++;
		panel1.add(tfStatus,gbc);
		tfStatus.disable();
		//
		bEncode.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				tfStatus.enable();
				tfStatus.setText("Processing");
				tfStatus.disable();
				String message=tfMessage.getText();
				String coverFile=tfCoverFile.getText();
				String outputFile=tfOutputFileDir.getText();
				int limit=Integer.parseInt(tfMsgLimit.getText());
				int cluster=Integer.parseInt(tfCluster.getText());
				int threshold=Integer.parseInt(tfThreshold.getText());
				@SuppressWarnings("deprecation")
				String password=pfPassword.getText();
				//ADD ANYTHING ELSE IF YOU WANT AND CALL THE NEEDED FUNCTION
				boolean result=EncodeModule.EncodeVideo(coverFile, outputFile, message, limit, password, cluster, threshold);
				if(result=true){
					tfStatus.setText("Successfully Complete");
				}
				else{
					tfStatus.setText("Error");
				}
			}
			
		});
		
		
		//Decode
		JPanel panel2=new JPanel();
		
		panel2.setLayout(new GridBagLayout());
		GridBagConstraints gbc2=new GridBagConstraints();
		gbc2.anchor=GridBagConstraints.WEST;
		/*gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.fill=GridBagConstraints.HORIZONTAL;*/
		int gbc2counter=0;
		//Search Encrypt File
		final JFileChooser fcEncryptFile=new JFileChooser();
		JButton bEncryptFile=new JButton("Browse");
		final JTextField tfEncryptFile=new JTextField(20);
		bEncryptFile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int returnVal=fcEncryptFile.showOpenDialog(mainFrame);
				if(returnVal==JFileChooser.APPROVE_OPTION){
					java.io.File encryptFile=fcEncryptFile.getSelectedFile();
					tfEncryptFile.setText(""+encryptFile.getAbsolutePath());
				}
				else{
					tfEncryptFile.setText("");
				}
			}
			
		});
		JLabel labEncryptFile=new JLabel("Search Encrypt File");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		panel2.add(labEncryptFile,gbc2);
		gbc2.gridx=1;gbc2.gridy=gbc2counter;
		panel2.add(tfEncryptFile,gbc2);
		gbc2.gridx=2;gbc2.gridy=gbc2counter++;
		panel2.add(bEncryptFile,gbc2);
		
		//Password
		JLabel labPassword2=new JLabel("Password");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		panel2.add(labPassword2,gbc2);
		final JPasswordField pfPassword2=new JPasswordField(20);
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		panel2.add(pfPassword2,gbc2);
		
		//Message Limit
		JLabel labMsgLimit2=new JLabel("Message Limit per Frame");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		panel2.add(labMsgLimit2,gbc2);
		final JTextField tfMsgLimit2=new JTextField(20);
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		panel2.add(tfMsgLimit2,gbc2);
		
		//Threshold
		JLabel labThreshold2=new JLabel("Threshold");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		panel2.add(labThreshold2,gbc2);
		final JTextField tfThreshold2=new JTextField(20);
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		panel2.add(tfThreshold2,gbc2);
		
		//Cluster Size
		JLabel labCluster2=new JLabel("Cluster Size");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		panel2.add(labCluster2,gbc2);
		final JTextField tfCluster2=new JTextField(20);
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		panel2.add(tfCluster2,gbc2);
		
		// Boolean Input
		JLabel labBoolean2=new JLabel("Clustered/Unclustered (true/false)");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		panel2.add(labBoolean2,gbc2);
		final JTextField tfBoolean2=new JTextField(20);
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		panel2.add(tfBoolean2,gbc2);
		
		//Decode button
		JButton bDecode=new JButton("Decode");
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		
		panel2.add(bDecode,gbc2);
		
		gbc2.gridx=0;gbc2.gridy=gbc2counter++;
		panel2.add(labBlank,gbc2);
		
		//Status
		JLabel labStatus2=new JLabel("Status:");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		panel2.add(labStatus2,gbc2);
		final JTextField tfStatus2=new JTextField(20);
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		panel2.add(tfStatus2,gbc2);
		
		//Answer
		JLabel labMessage2=new JLabel("Answer:");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		panel2.add(labMessage2,gbc2);
		final JTextField tfMessage2=new JTextField(20);
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		panel2.add(tfMessage2,gbc2);
		tfMessage2.disable();
		tfStatus2.disable();
		bDecode.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				tfStatus.enable();
				tfStatus2.setText("Processing");
				tfStatus2.disable();
				String encryptFile=tfEncryptFile.getText();
				
				int limit=Integer.parseInt(tfMsgLimit2.getText());
				@SuppressWarnings("deprecation")
				String password=pfPassword2.getText();
				int threshold=Integer.parseInt(tfThreshold2.getText());
				int cluster=Integer.parseInt(tfCluster2.getText());
				boolean inputBoolean=Boolean.parseBoolean(tfBoolean2.getText());
				//ADD ANYTHING ELSE IF YOU WANT AND CALL THE NEEDED FUNCTION
				String decodeModuleReturn=DecodeModule.DecodeVideo(encryptFile, threshold);
				Cluster temp=new Cluster(cluster);
				ArrayList<Integer> clusterReturn=temp.GetCluster(decodeModuleReturn);
				String messageReturned=DecodeModule.DecodeVideo(encryptFile, limit, password, clusterReturn, cluster, threshold, inputBoolean);
				if(messageReturned!=null){
					tfStatus2.enable();
					tfStatus.setText("Successfully Complete");
					tfMessage2.setText(messageReturned.substring(0, 20));
					tfStatus2.disable();
					tfMessage.disable();
				}
				else{
					tfStatus.setText("Error");
				}
				tfMessage2.setText(messageReturned);
				
			}
			
		});
		
		
		/*JLabel labMessage=new JLabel("Enter the Message Here");
		gbc.gridx=0;gbc.gridy=0;
		panel1.add(labMessage,gbc);
		JTextField tfMessage=new JTextField(20);
		gbc.gridx=1;gbc.gridy=0;
		panel1.add(tfMessage,gbc);
		
		final JFileChooser fcCoverFile=new JFileChooser();
		JButton bCoverFile=new JButton("Browse");
		final JTextField tfCoverFile=new JTextField(20);
		bCoverFile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int returnVal=fcCoverFile.showOpenDialog(mainFrame);
				if(returnVal==JFileChooser.APPROVE_OPTION){
					java.io.File coverFile=fcCoverFile.getSelectedFile();
					tfCoverFile.setText(""+coverFile.getAbsolutePath());
				}
				else{
					tfCoverFile.setText("");
				}
			}
			
		});
		JLabel labCoverFile=new JLabel("Search Cover File");
		gbc.gridx=0;gbc.gridy=1;
		panel1.add(labCoverFile,gbc);
		gbc.gridx=1;gbc.gridy=1;
		panel1.add(tfCoverFile,gbc);
		gbc.gridx=2;gbc.gridy=1;
		panel1.add(bCoverFile,gbc);
		JButton bEncode=new JButton("Encode");
		gbc.gridx=1;gbc.gridy=2;
		bEncode.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//ADD ACTION TO BE TAKEN ON ENCODE
			}
			
		});
		panel1.add(bEncode,gbc);
*/		
		
		jtp.add("Encode",panel1);
		jtp.add("Decode",panel2);
		mainFrame.add(jtp);
		mainFrame.setVisible(true);
		
	}

}

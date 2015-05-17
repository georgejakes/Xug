import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SwingWorker;



public class TestFrame extends JFrame {
	public TestFrame() {
	}

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//The Frame
		final JFrame mainFrame=new JFrame("Test1");		
		mainFrame.setSize(1000, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Tab Pane
		JTabbedPane jtp=new JTabbedPane();
		// Panel
		JPanel panel1=new JPanel();
		panel1.setLayout(new BoxLayout(panel1,BoxLayout.PAGE_AXIS));
		JPanel tempPanel1=new JPanel();
		tempPanel1.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		GridBagConstraints gbc=new GridBagConstraints();
		gbc.anchor=GridBagConstraints.WEST;
		gbc.insets=new Insets(2,0,2,0);
		int gbccounter=0;
		/*gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.fill=GridBagConstraints.HORIZONTAL;*/
		JLabel labBlank=new JLabel("    ");//For Blank spaces
		//Message to be inputed
		JLabel labMessage=new JLabel("Enter the Message Here");
		/*gbc.gridx=0;gbc.gridy=gbccounter;*/
		tempPanel1.add(Box.createRigidArea(new Dimension(200,0)));
		tempPanel1.add(labMessage);
		tempPanel1.add(Box.createRigidArea(new Dimension(25,0)));
		final JTextField tfMessage=new JTextField(20);
		/*gbc.gridx=1;gbc.gridy=gbccounter++;*/
		tempPanel1.add(tfMessage);
		final JLabel labMessageError=new JLabel("");
		tempPanel1.add(labMessageError);
		panel1.add(tempPanel1);
		//Cover File
		tempPanel1=new JPanel();
		tempPanel1.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
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
		tempPanel1.add(Box.createRigidArea(new Dimension(200,0)));/*gbc.gridx=0;gbc.gridy=gbccounter;*/
		
		tempPanel1.add(labCoverFile);
		/*gbc.gridx=1;gbc.gridy=gbccounter;*/
		tempPanel1.add(Box.createRigidArea(new Dimension(75,0)));
		tempPanel1.add(tfCoverFile,gbc);
		/*gbc.gridx=2;gbc.gridy=gbccounter++;*/
		tempPanel1.add(bCoverFile,gbc);
		final JLabel labCoverFileError=new JLabel("");
		tempPanel1.add(labCoverFileError);
		panel1.add(tempPanel1);
		//Output File
		tempPanel1=new JPanel();
		tempPanel1.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		JLabel labOutputFileDir=new JLabel("Search Output file directory");
		/*gbc.gridx=0;gbc.gridy=gbccounter;*/
		tempPanel1.add(Box.createRigidArea(new Dimension(200,0)));
		tempPanel1.add(labOutputFileDir);
		tempPanel1.add(Box.createRigidArea(new Dimension(0,0)));
		final JTextField tfOutputFileDir=new JTextField(20);
		/*gbc.gridx=1;gbc.gridy=gbccounter;*/
		tempPanel1.add(tfOutputFileDir);
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
		/*gbc.gridx=2;gbc.gridy=gbccounter++;*/
		tempPanel1.add(bOutputFileDir);
		final JLabel labOutputFileError=new JLabel("");
		tempPanel1.add(labOutputFileError);
		panel1.add(tempPanel1);
		tempPanel1=new JPanel();
		tempPanel1.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		
		/*JLabel labOutputFile=new JLabel("Name for the Encrypted File");
		gbc.gridx=0;gbc.gridy=gbccounter;
		panel1.add(labOutputFile,gbc);
		final JTextField tfOutputFile=new JTextField(20);
		gbc.gridx=1;gbc.gridy=gbccounter++;
		panel1.add(tfOutputFile,gbc);
		*/
		//Message Limit
		JLabel labMsgLimit=new JLabel("Message Limit per Frame");
		/*gbc.gridx=0;gbc.gridy=gbccounter;*/
		tempPanel1.add(Box.createRigidArea(new Dimension(200,0)));
		tempPanel1.add(labMsgLimit);
		tempPanel1.add(Box.createRigidArea(new Dimension(20,0)));
		/*final JTextField tfMsgLimit=new JTextField(20);*/
		final JSlider tfMsgLimit1=new JSlider(0,50);
		/*tfMsgLimit1.setMajorTickSpacing(2);*/
		tfMsgLimit1.setPaintTicks(true);
		tfMsgLimit1.setPaintLabels(true);
		tfMsgLimit1.setSnapToTicks(false);
		tfMsgLimit1.setLabelTable(tfMsgLimit1.createStandardLabels(10));
		/*gbc.gridx=1;gbc.gridy=gbccounter++;*/
		tempPanel1.add(tfMsgLimit1);
		final JTextField tfMsgLimitNo=new JTextField(2);
		tfMsgLimitNo.setText(""+tfMsgLimit1.getValue());
		tfMsgLimitNo.setEditable(false);
		tfMsgLimit1.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				tfMsgLimitNo.setText(""+tfMsgLimit1.getValue());
			}
			
		});
		tempPanel1.add(tfMsgLimitNo);
		panel1.add(tempPanel1);
		
		//Cluster Blah
		tempPanel1=new JPanel();
		tempPanel1.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		tempPanel1.add(Box.createRigidArea(new Dimension(200,0)));
		JLabel labCluster=new JLabel("Cluster Number");
		/*gbc.gridx=0;gbc.gridy=gbccounter;*/
		tempPanel1.add(labCluster);
		tempPanel1.add(Box.createRigidArea(new Dimension(85,0)));
		/*final JTextField tfCluster=new JTextField(20);*/
		final JSlider tfCluster=new JSlider(1,49);
		tfCluster.setMinorTickSpacing(2);
		tfCluster.setPaintTicks(true);
		tfCluster.setPaintLabels(true);
		tfCluster.setSnapToTicks(true);
		tfCluster.setLabelTable(tfCluster.createStandardLabels(12));
		/*gbc.gridx=1;gbc.gridy=gbccounter++;*/
		tempPanel1.add(tfCluster);
		final JTextField tfClusterNo=new JTextField(2);
		tfClusterNo.setText(""+tfCluster.getValue());
		tfClusterNo.setEditable(false);
		tfCluster.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				tfClusterNo.setText(""+tfCluster.getValue());
			}
			
		});
		tempPanel1.add(tfClusterNo);
		
		panel1.add(tempPanel1);
		//Threshold
		tempPanel1=new JPanel();
		tempPanel1.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		tempPanel1.add(Box.createRigidArea(new Dimension(200,0)));
		JLabel labThreshold=new JLabel("Threshhold");
		/*gbc.gridx=0;gbc.gridy=gbccounter;*/
		tempPanel1.add(labThreshold,gbc);
		/*final JTextField tfThreshold=new JTextField(20);*/
		
		final JSlider tfThreshold=new JSlider(1,8);
		/*tfThreshold.setMajorTickSpacing(2);*/
		tfThreshold.setPaintTicks(true);
		tfThreshold.setPaintLabels(true);
		tfThreshold.setSnapToTicks(true);
		tfThreshold.setLabelTable(tfThreshold.createStandardLabels(1));
		/*gbc.gridx=1;gbc.gridy=gbccounter++;*/
		tempPanel1.add(Box.createRigidArea(new Dimension(115,0)));
		tempPanel1.add(tfThreshold,gbc);
		
		final JTextField tfThresholdNo=new JTextField(2);
		tfThresholdNo.setText(""+tfThreshold.getValue());
		tfThresholdNo.setEditable(false);
		tfThreshold.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				tfThresholdNo.setText(""+tfThreshold.getValue());
			}
			
		});
		tempPanel1.add(tfThresholdNo);
		
		
		panel1.add(tempPanel1);
		//Password
		tempPanel1=new JPanel();
		tempPanel1.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		tempPanel1.add(Box.createRigidArea(new Dimension(200,0)));
		JLabel labPassword=new JLabel("Password");
		/*gbc.gridx=0;gbc.gridy=gbccounter;*/
		tempPanel1.add(labPassword);
		tempPanel1.add(Box.createRigidArea(new Dimension(125,0)));
		final JPasswordField pfPassword=new JPasswordField(20);
		/*gbc.gridx=1;gbc.gridy=gbccounter++;*/
		tempPanel1.add(pfPassword);
		final JLabel labPasswordError=new JLabel("");
		/*gbc.gridx=0;gbc.gridy=gbccounter;*/
		tempPanel1.add(labPasswordError);
		panel1.add(tempPanel1);
		
		//Button
		JButton b=new JButton("Encode");
		tempPanel1=new JPanel();
		tempPanel1.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		tempPanel1.add(Box.createRigidArea(new Dimension(200,0)));
		tempPanel1.add(Box.createRigidArea(new Dimension(150,0)));
		
		/*gbc.gridx=1;gbc.gridy=gbccounter++;*/
		tempPanel1.add(b);
		panel1.add(tempPanel1);
		/*gbc.gridx=0;gbc.gridy=8;
		panel1.add(labBlank,gbc);
		gbc.gridx=1;gbc.gridy=9;
		panel1.add(labBlank,gbc);*/
		//Status
		
		tempPanel1=new JPanel();
		tempPanel1.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		tempPanel1.add(Box.createRigidArea(new Dimension(200,0)));
		JLabel labStatus=new JLabel("Status:");
		/*gbc.gridx=0;gbc.gridy=gbccounter;*/
		tempPanel1.add(labStatus);
		tempPanel1.add(Box.createRigidArea(new Dimension(20,0)));
		
		final JTextField tfStatus=new JTextField(20);
		/*final JProgressBar pbStatus=new JProgressBar(0,100);
		
		pbStatus.setStringPainted(true);
		Border borderStaus=BorderFactory.createTitledBorder("Status");
		pbStatus.setBorder(borderStaus);*/
		/*gbc.gridx=1;gbc.gridy=gbccounter++;*/
		tempPanel1.add(tfStatus);
		panel1.add(tempPanel1);
		tfStatus.setEditable(false);
		//
		b.addActionListener(new ActionListener(){

			@SuppressWarnings("unused")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				/*tfStatus.enable();
				tfStatus.setText("Processing");
				tfStatus.disable();*/
				/*SwingWorker<Void,Void> task = new SwingWorker<Void,Void>(){

					@Override
					protected Void doInBackground() throws Exception {
						// TODO Auto-generated method stub
						
						int n=0;
						while (n<=100000000){
							pbStatus.setValue(n++/1000000);
						}
						return null;
					}
					
				};
				task.addPropertyChangeListener(this);
				task.execute();*/
				
				/*StatusBar sb=new StatusBar(pbStatus);
				Thread newThread=new Thread(sb);
				newThread.start();*/
				String message=tfMessage.getText();
				
				String coverFile=tfCoverFile.getText();
				String outputFile=tfOutputFileDir.getText();
				int limit=tfMsgLimit1.getValue();
				int cluster=tfCluster.getValue();
				int threshold=tfThreshold.getValue();
				@SuppressWarnings("deprecation")
				String password=pfPassword.getText();
				//ADD ANYTHING ELSE IF YOU WANT AND CALL THE NEEDED FUNCTION
				if(validateEncode(message,coverFile,outputFile,password))
				{tfStatus.setText("Processing");
					/*labMessageError.setText(message);labCoverFileError.setText(coverFile);labOutputFileError.setText(outputFile);*/
				boolean result=EncodeModule.EncodeVideo(coverFile, outputFile, message, limit, password, cluster, threshold);
				/*boolean result=Module.Video(coverFile, outputFile, message, limit, password, cluster, threshold);*/
				if(result=true){
					tfStatus.setText("Successfully Complete");
				}
				else{
					tfStatus.setText("Error");
				}
				}
			
			}

			private boolean validateEncode(String message, String coverFile, String outputFile, String password) {
				// TODO Auto-generated method stub
				boolean flag=true;
				if(message.equals(null)||message.equals("")){
					labMessageError.setText("Please Enter a message");
					flag=false;
				}else{
					labMessageError.setText("");
				}
				if(coverFile.equals(null)||coverFile.equals("")){
					labCoverFileError.setText("Please Enter a Cover File");
					flag=false;
				}
				else{
					labCoverFileError.setText("");
				}
				if(outputFile.equals(null)||outputFile.equals("")){
					labOutputFileError.setText("Please Enter a Output File");
					flag=false;
				}
				else{
					labOutputFileError.setText("");
				}
				if(password.equals(null)||password.equals("")){
					labPasswordError.setText("Please Enter a password");
					flag=false;
				}
				else{
					labPasswordError.setText("");
				}
				return flag;
			}
		});
		
		
		//Decode
		JPanel panel2=new JPanel();
		
		panel2.setLayout(new BoxLayout(panel2,BoxLayout.PAGE_AXIS));
		JPanel tempPanel2=new JPanel();
		tempPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		
		GridBagConstraints gbc2=new GridBagConstraints();
		gbc2.anchor=GridBagConstraints.WEST;
		/*gbc.gridwidth=GridBagConstraints.REMAINDER;
		gbc.fill=GridBagConstraints.HORIZONTAL;*/
		
		int gbc2counter=0;
		//Search Encrypt File
		final JFileChooser fcEncryptFile=new JFileChooser();
		JButton bEncryptFile=new JButton("Browse");
		final JTextField tfEncryptFile=new JTextField(25);
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
		tempPanel2.add(Box.createRigidArea(new Dimension(120,0)));
		tempPanel2.add(labEncryptFile);
		gbc2.gridx=1;gbc2.gridy=gbc2counter;
		tempPanel2.add(Box.createRigidArea(new Dimension(95,0)));
		tempPanel2.add(tfEncryptFile);
		gbc2.gridx=2;gbc2.gridy=gbc2counter++;
		tempPanel2.add(bEncryptFile);
		final JLabel labEncryptFileError=new JLabel("");
		tempPanel2.add(labEncryptFileError);
		panel2.add(tempPanel2);
		
		//Password
		tempPanel2=new JPanel();
		tempPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		JLabel labPassword2=new JLabel("Password");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		tempPanel2.add(Box.createRigidArea(new Dimension(120,0)));
		tempPanel2.add(labPassword2);
		tempPanel2.add(Box.createRigidArea(new Dimension(160,0)));
		final JPasswordField pfPassword2=new JPasswordField(25);
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		tempPanel2.add(pfPassword2);
		JLabel labPasswordError2=new JLabel("");
		tempPanel2.add(labPasswordError2);
		panel2.add(tempPanel2);
		//Message Limit
		tempPanel2=new JPanel();
		tempPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		JLabel labMsgLimit2=new JLabel("Message Limit per Frame");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		tempPanel2.add(Box.createRigidArea(new Dimension(120,0)));
		tempPanel2.add(labMsgLimit2);
		tempPanel2.add(Box.createRigidArea(new Dimension(50,0)));
		/*final JSlider tfMsgLimit2=new JSlider(5,21);
		tfMsgLimit2.setMajorTickSpacing(2);
		tfMsgLimit2.setPaintTicks(true);
		tfMsgLimit2.setPaintLabels(true);
		tfMsgLimit2.setSnapToTicks(true);
		tfMsgLimit2.setLabelTable(tfMsgLimit2.createStandardLabels(2));
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		tempPanel2.add(tfMsgLimit2);*/
		final JSlider tfMsgLimit2=new JSlider(0,50);
		/*tfMsgLimit1.setMajorTickSpacing(2);*/
		tfMsgLimit2.setPaintTicks(true);
		tfMsgLimit2.setPaintLabels(true);
		tfMsgLimit2.setSnapToTicks(false);
		tfMsgLimit2.setLabelTable(tfMsgLimit2.createStandardLabels(10));
		/*gbc.gridx=1;gbc.gridy=gbccounter++;*/
		tempPanel2.add(tfMsgLimit2);
		final JTextField tfMsgLimitNo2=new JTextField(2);
		tfMsgLimitNo2.setText(""+tfMsgLimit2.getValue());
		tfMsgLimitNo2.setEditable(false);
		tfMsgLimit2.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				tfMsgLimitNo2.setText(""+tfMsgLimit2.getValue());
			}
			
		});
		tempPanel2.add(tfMsgLimitNo2);
		panel2.add(tempPanel2);
		//Threshold
		tempPanel2=new JPanel();
		tempPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		JLabel labThreshold2=new JLabel("Threshold");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		tempPanel2.add(Box.createRigidArea(new Dimension(120,0)));
		tempPanel2.add(labThreshold2);
		tempPanel2.add(Box.createRigidArea(new Dimension(35,0)));
		/*final JTextField tfThreshold2=new JTextField(20);*/
		/*final JSlider tfThreshold2=new JSlider(1,8);
		tfThreshold2.setMajorTickSpacing(1);
		
		tfThreshold2.setPaintTicks(true);
		
		tfThreshold2.setLabelTable(tfThreshold2.createStandardLabels(1));
		tfThreshold2.setSnapToTicks(true);
		tfThreshold2.setPaintLabels(true);
		tfThreshold2.setLabelTable(tfThreshold2.createStandardLabels(2));
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		tempPanel2.add(tfThreshold2);
		*/
		final JSlider tfThreshold2=new JSlider(1,8);
		/*tfThreshold.setMajorTickSpacing(2);*/
		tfThreshold2.setPaintTicks(true);
		tfThreshold2.setPaintLabels(true);
		tfThreshold2.setSnapToTicks(true);
		tfThreshold2.setLabelTable(tfThreshold2.createStandardLabels(1));
		/*gbc.gridx=1;gbc.gridy=gbccounter++;*/
		tempPanel2.add(Box.createRigidArea(new Dimension(115,0)));
		tempPanel2.add(tfThreshold2);
		
		final JTextField tfThresholdNo2=new JTextField(2);
		tfThresholdNo2.setText(""+tfThreshold2.getValue());
		tfThresholdNo2.setEditable(false);
		tfThreshold2.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				tfThresholdNo2.setText(""+tfThreshold2.getValue());
			}
			
		});
		tempPanel2.add(tfThresholdNo2);
		
		

		
		panel2.add(tempPanel2);
		//Cluster Size
		tempPanel2=new JPanel();
		tempPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		JLabel labCluster2=new JLabel("Cluster Size");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		tempPanel2.add(Box.createRigidArea(new Dimension(120,0)));
		tempPanel2.add(labCluster2);
		tempPanel2.add(Box.createRigidArea(new Dimension(140,0)));
		/*final JSlider tfCluster2=new JSlider(5,21);
		tfCluster2.setMajorTickSpacing(2);
		tfCluster2.setPaintTicks(true);
		tfCluster2.setPaintLabels(true);
		tfCluster2.setSnapToTicks(true);
		tfCluster2.setLabelTable(tfCluster2.createStandardLabels(2));
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		tempPanel2.add(tfCluster2);*/
		
		final JSlider tfCluster2=new JSlider(1,49);
		tfCluster2.setMinorTickSpacing(2);
		tfCluster2.setPaintTicks(true);
		tfCluster2.setPaintLabels(true);
		tfCluster2.setSnapToTicks(true);
		tfCluster2.setLabelTable(tfCluster2.createStandardLabels(12));
		/*gbc.gridx=1;gbc.gridy=gbccounter++;*/
		tempPanel2.add(tfCluster2);
		final JTextField tfClusterNo2=new JTextField(2);
		tfClusterNo2.setText(""+tfCluster2.getValue());
		tfClusterNo2.setEditable(false);
		tfCluster2.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				tfClusterNo2.setText(""+tfCluster2.getValue());
			}
			
		});
		tempPanel2.add(tfClusterNo2);
		
		panel2.add(tempPanel2);
		// Boolean Input
		tempPanel2=new JPanel();
		tempPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		
		JLabel labBoolean2=new JLabel("Clustered/Unclustered (true/false)");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		tempPanel2.add(Box.createRigidArea(new Dimension(120,0)));
		tempPanel2.add(labBoolean2);
		
		/*final JTextField tfBoolean2=new JTextField(25);*/
		final String x="OFF";
		final JToggleButton tgBoolean2=new JToggleButton("OFF");
		ItemListener itemListener = new ItemListener() {
		    public void itemStateChanged(ItemEvent itemEvent) {
		        int state = itemEvent.getStateChange();
		        if (state == ItemEvent.SELECTED) {
		            tgBoolean2.setText("ON");// show your message here
		        } else {
		            tgBoolean2.setText("OFF");// remove your message
		        }
		    }
		};
		tgBoolean2.addItemListener(itemListener);
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		tempPanel2.add(tgBoolean2);
		panel2.add(tempPanel2);
		//Decode button
		tempPanel2=new JPanel();
		tempPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		
		JButton bDecode=new JButton("Decode");
		gbc2.gridx=2;gbc2.gridy=gbc2counter++;
		tempPanel2.add(Box.createRigidArea(new Dimension(500,0)));
		tempPanel2.add(bDecode);
		panel2.add(tempPanel2);
		gbc2.gridx=0;gbc2.gridy=gbc2counter++;
		
		/*tempPanel2=new JPanel();
		tempPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		tempPanel2.add(labBlank);
		panel2.add(tempPanel2);*/
		//Status
		tempPanel2=new JPanel();
		tempPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		JLabel labStatus2=new JLabel("Status:");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		tempPanel2.add(Box.createRigidArea(new Dimension(120,0)));
		tempPanel2.add(labStatus2);
		tempPanel2.add(Box.createRigidArea(new Dimension(150,0)));
		final JTextField tfStatus2=new JTextField(25);
		/*JProgressBar pbStatus2=new JProgressBar(0,100);
		
		pbStatus2.setStringPainted(true);
		Border borderStaus2=BorderFactory.createTitledBorder("Status");
		pbStatus2.setBorder(borderStaus2);*/
		/*gbc.gridx=1;gbc.gridy=gbccounter++;*/
		tempPanel2.add(tfStatus2);
	/*	gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		tempPanel2.add(tfStatus2);*/
		panel2.add(tempPanel2);
		//Answer
		tempPanel2=new JPanel();
		tempPanel2.setLayout(new FlowLayout(FlowLayout.LEFT,10,0));
		JLabel labMessage2=new JLabel("Answer:");
		gbc2.gridx=0;gbc2.gridy=gbc2counter;
		tempPanel2.add(Box.createRigidArea(new Dimension(120,0)));
		tempPanel2.add(labMessage2);
		tempPanel2.add(Box.createRigidArea(new Dimension(145,0)));
		final JTextArea tfMessage2=new JTextArea(10,25);
		gbc2.gridx=1;gbc2.gridy=gbc2counter++;
		/*tempPanel2.add(Box.createRigidArea(new Dimension(120,0)));*/
		tempPanel2.add(tfMessage2);
		panel2.add(tempPanel2);
		tfMessage2.setEditable(false);
		tfStatus2.setEditable(false);
		bDecode.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				tfStatus2.setText("Processing");
				
				String encryptFile=tfEncryptFile.getText();
				
				int limit=tfMsgLimit2.getValue();
				@SuppressWarnings("deprecation")
				String password=pfPassword2.getText();
				int threshold=tfThreshold2.getValue();
				int cluster=tfCluster2.getValue();
				boolean inputBoolean=tgBoolean2.isSelected();
				//ADD ANYTHING ELSE IF YOU WANT AND CALL THE NEEDED FUNCTION
				if(validateDecode(encryptFile,password)){
				String decodeModuleReturn=DecodeModule.DecodeVideo(encryptFile, threshold);
				Cluster temp=new Cluster(cluster);
				ArrayList<Integer> clusterReturn=temp.GetCluster(decodeModuleReturn);
				String messageReturned=DecodeModule.DecodeVideo(encryptFile, limit, password, clusterReturn, cluster, threshold, inputBoolean);
				if(messageReturned!=null){
					
					tfStatus2.setText("Successfully Complete");
					tfMessage2.setText(messageReturned.substring(0, 20));
					
				}
				else{
					
					tfStatus2.setText("Error");
					
				}
				tfMessage2.setText(messageReturned);
				}
			}
			private boolean validateDecode(String encryptFile, String password) {
				// TODO Auto-generated method stub
				boolean flag=true;
				if(encryptFile.equals(null)||encryptFile.equals("")){
					labEncryptFileError.setText("Not Filled");
					flag=false;
				}else{
					labEncryptFileError.setText("");
				}
				if(password.equals(null)||password.equals("")){
					labPasswordError.setText("Not Filled");
					flag=false;
				}else{
					labPasswordError.setText("");
				}
				
				return flag;
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
		JButton b=new JButton("");
		gbc.gridx=1;gbc.gridy=2;
		b.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				//ADD ACTION TO BE TAKEN ON 
			}
			
		});
		panel1.add(b,gbc);
*/		
		
		jtp.add("Encode",panel1);
		jtp.add("Decode",panel2);
		mainFrame.add(jtp);
		mainFrame.setVisible(true);
		
	}

}

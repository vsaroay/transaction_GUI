/* Vikram Saroay
	Csc 20 Section 1
	Semester Project
	
	This program runs a GUI for the user to keep track of their transactions in
	a personal created account. The program allows the user to backup all their
	transactions in one chart and also allows them to select and delete any 
	transaction they made.
*/
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Transaction implements Serializable{
   String date;
   int type;
   String checkNum;
   String transDescp;
   double payDebit;
   double deposit;
   double balanceUpd;
}   
/*********Main Project Class******************************************************************/
public class Checkbook implements ActionListener{

   static Container contentPane;// static contentPane
   static CardLayout contentPaneLayout;
   // Main screen 1
   static JTextField mainAccountNameText = new JTextField();
   static JTextField mainBalanceText = new JTextField("0.0");
   static JButton mainNewAccountButton = new JButton("Create New Account");
	static JButton mainLoadTransButton = new JButton("Load Trans From File");
   static JButton mainAddTransButton = new JButton("Add New Transaction");
   static JButton mainSearchTransButton = new JButton("Search Transactions");
   static JButton mainSortTransButton = new JButton("Sort Transactions");
   static JButton mainViewTransButton = new JButton("View/Delete Transactions");
   static JButton mainBackupTransButton = new JButton("Backup Transactions");
   static JButton mainExitButton = new JButton("Exit");
   // New Account 2
   static JTextField newAccountAccountNameText = new JTextField();
   static JTextField newAccountBalanceText = new JTextField();
   static JButton newAccountCreateButton = new JButton("Create");
   static JButton newAccountCancelButton = new JButton("Cancel");
   // Load Account 3
   static JTextField loadTransAccountNameText = new JTextField();
   static JButton loadTransLoadButton = new JButton("Load");
   static JButton loadTransCancelButton = new JButton("Cancel");
   // Save Transaction 4
   static JTextField dateText = new JTextField(12);
   static JComboBox<String> transType = new JComboBox<String>();
   static String comboItems[] = {"Deposit", "Automatic Deposit", "ATM Withdrawal", "Check", "Debit Card", "Other"};
   static JTextField checkNumText = new JTextField(12);
   static JTextField transDescriptionText = new JTextField(12);
   static JTextField paymentText = new JTextField(12);
   static JTextField deposCredText = new JTextField(12);
   static JButton saveTransSaveButton = new JButton("Save New Transaction");
   static JButton saveTransMenuButton = new JButton("Top Menu");
   // Search Trans 5
   static JRadioButton byType = new JRadioButton("By Type");
   static JRadioButton byDate = new JRadioButton("By Date");
   static JTextField searchTransStringText = new JTextField();
   static JButton searchTransSearchButton = new JButton("Search");
   static JButton searchTransMenuButton = new JButton("Top Menu");
   // Sort Trans 6
   static JButton sortTransSortButton = new JButton("Sort");
   static JButton sortTransMenuButton = new JButton("Top Menu");
   // View Transactions 7
   static JButton deleteButton = new JButton("Delete Selected");
   static JButton viewTransMenuButton = new JButton("Top Menu");
   
   /********     Other Variables Used   *******************************************************/
   static JScrollPane scrollPane = new JScrollPane();
   static String tableColumns[] = {"Date", "Trans. Type", "Check No.", 
                                   "Trans. Description", "Payment/Debit(-)", "Deposit/Credit(+)", "Balance"};
   
   static Transaction transArray[] = new Transaction[100];// Keep track of the number of transactions
   static int transCount;
   
   static ArrayList<Transaction> transList = new ArrayList<Transaction>();// ArrayList for trans made
   static JTable table;
   
   static String accountName;// Name entered by user
   static double balance;// Initial balance 
   static double balanceNew;// initial + update

/************************************************************************************************/   
/************************************************************************************************/   
   public void actionPerformed(ActionEvent e){
      Object source = e.getSource();     
      if(source == mainNewAccountButton){ 
         contentPaneLayout.show(contentPane, "newA");
      }
      if(source == mainLoadTransButton)
         contentPaneLayout.show(contentPane, "loadT");

      if(source == mainAddTransButton){
         contentPaneLayout.show(contentPane, "saveT");
      }

      if(source == mainSearchTransButton){
         contentPaneLayout.show(contentPane, "searchT");
      
      }   
      if(source == mainSortTransButton){
         contentPaneLayout.show(contentPane, "sortT");
         
      }   
      if(source == mainViewTransButton){
         String tableData[][] = new String[transList.size()][7];
         for (int i = 0; i < transList.size(); i++){
            Transaction index = transList.get(i);
            tableData[i][0] = index.date;
            tableData[i][1] = (String)index.type;//Integer.toString(index.type);
            tableData[i][2] = index.checkNum;
            tableData[i][3] = index.transDescp;
            tableData[i][4] = ""+index.payDebit;
            tableData[i][5] = ""+index.deposit;
            tableData[i][6] = ""+index.balanceUpd; 
            //tableData[i][6] = ""+balanceNew;/////////////////*************************
         }
         table = new JTable(tableData, tableColumns);
         JScrollPane tmp = new JScrollPane(table);
         scrollPane.setViewport(tmp.getViewport());
         contentPaneLayout.show(contentPane, "viewT");
      }
         
      if(source == newAccountCreateButton){
         accountName = newAccountAccountNameText.getText();
         balance = Double.parseDouble(newAccountBalanceText.getText());
         mainAccountNameText.setText(accountName);
         mainBalanceText.setText(""+balance);
         transCount = 0;
         contentPaneLayout.show(contentPane, "main");
         mainBackupTransButton.setEnabled(true);
         //mainAccountNameText.setText("");
         //mainBalanceText.setText("");
      }
      //save the account info to a file
      if(source == mainBackupTransButton){
      try{
         FileOutputStream fos = new FileOutputStream (accountName, false);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
         for (int i = 0; i < transList.size(); i++){
            oos.writeObject(transList.get(i));
         }
      }catch(FileNotFoundException a){ System.out.println(a.toString());
		}catch(IOException a){ 
      a.printStackTrace();}
      
      mainBackupTransButton.setEnabled(false);
	   }
      //load the saved file
      if(source == loadTransLoadButton){
         transList.clear();
         accountName = loadTransAccountNameText.getText();
         try {
				FileInputStream fis = new FileInputStream (accountName);
				ObjectInputStream ois = new ObjectInputStream(fis);
				while (true) {
					Transaction St = (Transaction) ois.readObject();
					transList.add(St);
				}
			} catch(EOFException a){
			} catch(Exception a){ a.printStackTrace();
		   }
         mainAccountNameText.setText(accountName);
         mainBalanceText.setText(""+balanceNew);////////////////////*****************************
         contentPaneLayout.show(contentPane, "main");
      }
        
      if(source == saveTransSaveButton){
         Transaction temp = new Transaction();
         temp.date = dateText.getText();
         temp.type = transType.getSelectedIndex();
         temp.checkNum = checkNumText.getText();
         temp.transDescp = transDescriptionText.getText();
         if (temp.type == 0 || temp.type == 1){
            temp.deposit = Double.parseDouble(deposCredText.getText());
            //temp.balanceUpd += Double.parseDouble(deposCredText.getText());
            balanceNew += Double.parseDouble(deposCredText.getText());
            mainBalanceText.setText(""+balanceNew);
            temp.payDebit = 0;
         }
         else {
            temp.payDebit = Double.parseDouble(paymentText.getText());
            //temp.balanceUpd -= Double.parseDouble(paymentText.getText());
            balanceNew -= Double.parseDouble(deposCredText.getText());
            mainBalanceText.setText(""+balanceNew);
            temp.deposit = 0;
         }
         transList.add(temp);
         transCount++;
         dateText.setText("");
         checkNumText.setText("");
         transDescriptionText.setText("");
         deposCredText.setText("");
         paymentText.setText("");
      }
         
      //if(source == searchTransSearchButton)
         
      //if(source == sortTransSortButton)
         
      if(source == deleteButton){
         int selected = table.getSelectedRow();
         transList.remove(selected);
         
         String tableData[][] = new String[transList.size()][7];
         for (int i = 0; i < transList.size(); i++){
            Transaction index = transList.get(i);
            tableData[i][0] = index.date;
            tableData[i][1] = Integer.toString(index.type);
            tableData[i][2] = index.checkNum;
            tableData[i][3] = index.transDescp;
            tableData[i][4] = ""+index.payDebit;
            tableData[i][5] = ""+index.deposit;
            tableData[i][6] = ""; 
         }
         JTable table = new JTable(tableData, tableColumns);
         JScrollPane tmp = new JScrollPane(table);
         scrollPane.setViewport(tmp.getViewport());
      }   
      if(source==newAccountCancelButton || source==loadTransCancelButton || source==saveTransMenuButton 
         || source==searchTransMenuButton || source==sortTransMenuButton || source==viewTransMenuButton){
         contentPaneLayout.show(contentPane, "main"); 
      }
      if(source == mainExitButton)
         System.exit(0);
   }
//000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000//   
//000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000//   
/* The main method has 7 screens that pop up accoding to what the user clicks
	   when using the program */
   public static void main(String[] args){
      JFrame mainFrame = new JFrame("Checkbook");
      mainFrame.setSize(800,250);
      mainFrame.setLocationRelativeTo(null);// Makes the screen pop in center
      mainFrame.setResizable(false);
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Closes on X
      contentPane = (JPanel) mainFrame.getContentPane();
      contentPane.setLayout(contentPaneLayout = new CardLayout()); // Card Layout setting
//-------------------AL---------------------------------------------------------------------------//
      ActionListener AL = new Checkbook();// Action Listener
      
      mainNewAccountButton.addActionListener(AL);// screen 1 buttons
      mainLoadTransButton.addActionListener(AL);
      mainAddTransButton.addActionListener(AL);
      mainSearchTransButton.addActionListener(AL);
      mainSortTransButton.addActionListener(AL);
      mainViewTransButton.addActionListener(AL);
      mainBackupTransButton.addActionListener(AL);
      mainExitButton.addActionListener(AL);
      mainAccountNameText.addActionListener(AL);
      mainBalanceText.addActionListener(AL);
      newAccountCreateButton.addActionListener(AL);// screen 2 buttons
      newAccountCancelButton.addActionListener(AL);
      newAccountAccountNameText.addActionListener(AL);
      newAccountBalanceText.addActionListener(AL);
      loadTransLoadButton.addActionListener(AL);// screen 3 buttons
      loadTransCancelButton.addActionListener(AL);
      loadTransAccountNameText.addActionListener(AL);
      saveTransSaveButton.addActionListener(AL);// screen 4 buttons
      saveTransMenuButton.addActionListener(AL);
      dateText.addActionListener(AL);
      transType.addActionListener(AL); 
      checkNumText.addActionListener(AL); 
      transDescriptionText.addActionListener(AL); 
      paymentText.addActionListener(AL);
      deposCredText.addActionListener(AL);
      searchTransSearchButton.addActionListener(AL);// screen 5 buttons
      searchTransMenuButton.addActionListener(AL);
      byType.addActionListener(AL);
      byDate.addActionListener(AL);
      sortTransSortButton.addActionListener(AL);// screen 6 buttons
      sortTransMenuButton.addActionListener(AL);
      deleteButton.addActionListener(AL);// screen 7 buttons
      viewTransMenuButton.addActionListener(AL);
      
///////////////////////////Main Screen ////////////////////////////////////////////////////////////////
      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BorderLayout());
		
      // Top layer-->> the Title on the Screen
		JLabel mainLabel = new JLabel("Use The Buttons Below To Manage Transactions", JLabel.CENTER);
      mainLabel.setFont(mainLabel.getFont().deriveFont (25.0f));// set font size of label     
      mainPanel.add(mainLabel, BorderLayout.NORTH);
      
		// Center layer-->> text fields
      JPanel mainCenterPanel = new JPanel();
      mainCenterPanel.setLayout(new FlowLayout());
      JLabel mainAccountNameLabel = new JLabel("Account Name: ");
      mainAccountNameText.setColumns(12);
      mainAccountNameText.setEditable(false);
      JLabel mainBalanceLabel = new JLabel("Balance: ");
      mainBalanceText.setColumns(12);
      mainBalanceText.setEditable(false);
      mainBalanceText.setHorizontalAlignment(JTextField.RIGHT);
      mainCenterPanel.add(mainAccountNameLabel);
      mainCenterPanel.add(mainAccountNameText);
      mainCenterPanel.add(mainBalanceLabel);
      mainCenterPanel.add(mainBalanceText);
      mainPanel.add(mainCenterPanel, BorderLayout.CENTER);
      
		// Bottom layer-->> The Main buttons
      JPanel mainBottomPanel = new JPanel();
      mainBottomPanel.setLayout(new GridLayout(2,4,2,2));
      
      mainBottomPanel.add(mainNewAccountButton);
      mainBottomPanel.add(mainLoadTransButton);
      mainBottomPanel.add(mainAddTransButton);
      mainBottomPanel.add(mainSearchTransButton);
      mainBottomPanel.add(mainSortTransButton);
      mainBottomPanel.add(mainViewTransButton);
      mainBottomPanel.add(mainBackupTransButton);
      mainBackupTransButton.setEnabled(false);///////////////////////////////////****************************
      mainBottomPanel.add(mainExitButton);
      
      mainPanel.add(mainBottomPanel, BorderLayout.SOUTH);
      
///////////////////////////New Account Screen /////////////////////////////////////////////////////////
      JPanel newAccountPanel = new JPanel();
      newAccountPanel.setLayout(new BorderLayout());
      
      // Top layer-->> The Title
      JLabel newAccountLabel = new JLabel("Create a New Account", JLabel.CENTER);
      newAccountLabel.setFont(newAccountLabel.getFont().deriveFont (25.0f));    
      newAccountPanel.add(newAccountLabel, BorderLayout.NORTH);
      
      // Middle layer-->> Text Fields
      JPanel newAccountCenterPanel = new JPanel(new GridLayout(2,2,2,2));// Main Center
      JPanel newAccountCenterTopPanel = new JPanel(new GridLayout(4,5,2,2));// Top Center
      newAccountCenterTopPanel.setLayout(new FlowLayout());
      JLabel newAccountAccountNameLabel = new JLabel("Account Name: ");
      newAccountAccountNameText.setColumns(12);
      newAccountCenterTopPanel.add(newAccountAccountNameLabel);
      newAccountCenterTopPanel.add(newAccountAccountNameText);
      JPanel newAccountCenterBottomPanel = new JPanel(new GridLayout(4,5,2,2));// Bottom Center
      newAccountCenterBottomPanel.setLayout(new FlowLayout());
      JLabel newAccountBalanceLabel = new JLabel("Initial Balance: ");
      newAccountBalanceText.setColumns(12);
      newAccountCenterBottomPanel.add(newAccountBalanceLabel);
      newAccountCenterBottomPanel.add(newAccountBalanceText);
      newAccountCenterPanel.add(newAccountCenterTopPanel);// add Top n Bottom to Main Center
      newAccountCenterPanel.add(newAccountCenterBottomPanel);
      newAccountPanel.add(newAccountCenterPanel, BorderLayout.CENTER);// add Main Center to Border
      
      // Bottom layer-->> Buttons
      JPanel newAccountBottomPanel = new JPanel();
      newAccountBottomPanel.setLayout(new FlowLayout());
      newAccountBottomPanel.add(newAccountCreateButton);
      newAccountBottomPanel.add(newAccountCancelButton);
      
      newAccountPanel.add(newAccountBottomPanel, BorderLayout.SOUTH);
      
///////////////////////////Load Trans Screen /////////////////////////////////////////////////////////////////
      JPanel loadTransPanel = new JPanel();
      loadTransPanel.setLayout(new GridLayout(4,5,2,2));
		
      // Top layer-->> The Title
      JLabel loadTransLabel = new JLabel("Load Transactions From a File", JLabel.CENTER);
      loadTransLabel.setFont(loadTransLabel.getFont().deriveFont (25.0f));    
      loadTransPanel.add(loadTransLabel);
		
      // Middle layer-->> Text Field
      JPanel loadTransCenterPanel = new JPanel(new GridLayout(4,5,2,2));
      loadTransCenterPanel.setLayout(new FlowLayout());
      JLabel loadTransAccountNameLabel = new JLabel("Account Name: ");
      loadTransAccountNameText.setColumns(12);
      loadTransCenterPanel.add(loadTransAccountNameLabel);
      loadTransCenterPanel.add(loadTransAccountNameText);
      loadTransPanel.add(loadTransCenterPanel);
		
      // Bottom layer-->> Buttons
      JPanel loadTransBottomPanel = new JPanel();
      loadTransBottomPanel.setLayout(new FlowLayout());
      loadTransBottomPanel.add(loadTransLoadButton);
      loadTransBottomPanel.add(loadTransCancelButton);
      
      loadTransPanel.add(loadTransBottomPanel);
      
///////////////////////////Create/save Trans Screen //////////////////////////////////////////////////////
      JPanel saveTransPanel = new JPanel();
      saveTransPanel.setLayout(new BorderLayout());
		
      // Middle layer-->> Text Fields 
      JPanel saveTransCenterPanel = new JPanel(new GridLayout(7,2,2,2));
      saveTransPanel.add(saveTransCenterPanel, BorderLayout.CENTER);
      
      JLabel dateLabel = new JLabel("Date");
      dateLabel.setHorizontalAlignment(JTextField.RIGHT);
		saveTransCenterPanel.add(dateLabel);
      saveTransCenterPanel.add(dateText);
		
		// ComboBox-->> Trans. Type
		JLabel transTypeLabel = new JLabel("Trans. Type");
		transTypeLabel.setHorizontalAlignment(JTextField.RIGHT);
		saveTransCenterPanel.add(transTypeLabel);
		transType.addItem("Deposit");
		transType.addItem("Automatic Deposit");
		transType.addItem("ATM Withdrawal");
		transType.addItem("Check");
		transType.addItem("Debit Card");
		transType.addItem("Other");
		saveTransCenterPanel.add(transType);
      
		JLabel checkNumLabel = new JLabel("Check No.");
		saveTransCenterPanel.add(checkNumLabel);
      checkNumLabel.setHorizontalAlignment(JTextField.RIGHT);
      saveTransCenterPanel.add(checkNumText);

		JLabel transDescriptionLabel = new JLabel("Trans Description");
		saveTransCenterPanel.add(transDescriptionLabel);
      transDescriptionLabel.setHorizontalAlignment(JTextField.RIGHT);
      saveTransCenterPanel.add(transDescriptionText);
      
      JLabel paymentLabel = new JLabel("Payment/Debit(-)");
		saveTransCenterPanel.add(paymentLabel);
      paymentLabel.setHorizontalAlignment(JTextField.RIGHT);
      saveTransCenterPanel.add(paymentText);
      
      JLabel deposCredLabel = new JLabel("Deposit/Credit(+)");
		saveTransCenterPanel.add(deposCredLabel);
      deposCredLabel.setHorizontalAlignment(JTextField.RIGHT);
      saveTransCenterPanel.add(deposCredText);
		
      //Bottom layer-->> Buttons
      JPanel saveTransBottomPanel = new JPanel();
      saveTransBottomPanel.setLayout(new FlowLayout());
      saveTransBottomPanel.add(saveTransSaveButton);
      saveTransBottomPanel.add(saveTransMenuButton);

      saveTransPanel.add(saveTransBottomPanel, BorderLayout.SOUTH);
		
///////////////////////////Search Trans Screen ////////////////////////////////////////////////////////////
      JPanel searchTransPanel = new JPanel();
      searchTransPanel.setLayout(new BorderLayout());
      
      JLabel searchTransLabel = new JLabel("Search Transaction by Date/Type/Check No./Description", JLabel.CENTER); 
      searchTransPanel.add(searchTransLabel, BorderLayout.NORTH);
      
      // Center layer-->> the table
      scrollPane = new JScrollPane();
      searchTransPanel.add(scrollPane, BorderLayout.CENTER);
      
      // Bottom layer-->> Texts and Buttons
      JPanel searchTransBottomPanel = new JPanel(new GridLayout(2,2,2,2));// Main Bottom
      JPanel searchTransBottom1Panel = new JPanel(new GridLayout(4,5,2,2));// Bottom (top portion)
      searchTransBottom1Panel.setLayout(new FlowLayout());
      JLabel searchTransStringLabel = new JLabel("Search String: ");
      searchTransStringText.setColumns(12);
      searchTransBottom1Panel.add(searchTransStringLabel);
      searchTransBottom1Panel.add(searchTransStringText);
      JPanel searchTransBottom2Panel = new JPanel(new GridLayout(4,5,2,2));// Bottom (bottom portion)
      searchTransBottom2Panel.setLayout(new FlowLayout());
      searchTransBottom2Panel.add(searchTransSearchButton);
      searchTransBottom2Panel.add(searchTransMenuButton);
      searchTransBottomPanel.add(searchTransBottom1Panel);// add 1 n 2 to Main Bottom
      searchTransBottomPanel.add(searchTransBottom2Panel);
      searchTransPanel.add(searchTransBottomPanel, BorderLayout.SOUTH);// add Main Bottom to Border
      
///////////////////////////Sort Trans Screen ////////////////////////////////////////////////////////////////
		JPanel sortTransPanel = new JPanel();
      sortTransPanel.setLayout(new BorderLayout());
		
		// Top layer-->> Title
		JLabel sortTransLabel = new JLabel("Sort Transactions", JLabel.CENTER);
      sortTransLabel.setFont(sortTransLabel.getFont().deriveFont (25.0f));    
      sortTransPanel.add(sortTransLabel, BorderLayout.NORTH);
		
		// Middle layer-->> Radio Buttons
		JPanel sortTransCenterPanel = new JPanel();
		sortTransCenterPanel.setLayout(new FlowLayout());
      ButtonGroup sortTrans = new ButtonGroup();
      sortTrans.add(byType);
      sortTrans.add(byDate);
		byType.setSelected(true);
		sortTransCenterPanel.add(byType);
		sortTransCenterPanel.add(byDate);
		sortTransPanel.add(sortTransCenterPanel, BorderLayout.CENTER);
		
		// Bottom layer-->> Buttons
		JPanel sortTransBottomPanel = new JPanel();
      sortTransBottomPanel.setLayout(new FlowLayout());
      sortTransBottomPanel.add(sortTransSortButton);
      sortTransBottomPanel.add(sortTransMenuButton);
      sortTransPanel.add(sortTransBottomPanel, BorderLayout.SOUTH);
		
///////////////////////////View all Trans Screen ///////////////////////////////////////////////////////////
      JPanel viewTransPanel = new JPanel();
      viewTransPanel.setLayout(new BorderLayout());
      
      // Top layer-->> Title
      JLabel viewTransLabel = new JLabel("Transactions Currently In The Checkbook", JLabel.CENTER);   
      viewTransPanel.add(viewTransLabel, BorderLayout.NORTH);
      
      // Center layer-->> table
      scrollPane = new JScrollPane();
      viewTransPanel.add(scrollPane, BorderLayout.CENTER);
      
      // Bottom layer-->> Buttons
      JPanel viewTransBottomPanel = new JPanel();
      viewTransBottomPanel.setLayout(new FlowLayout());
      viewTransBottomPanel.add(deleteButton);
      viewTransBottomPanel.add(viewTransMenuButton);
      viewTransPanel.add(viewTransBottomPanel, BorderLayout.SOUTH);
//*****************************************************************************************//
//*****************************************************************************************//
      contentPane.add("main", mainPanel);
      contentPaneLayout.show(contentPane, "main");
      contentPane.add("newA", newAccountPanel);
      contentPane.add("loadT", loadTransPanel);
      contentPane.add("saveT", saveTransPanel);
		contentPane.add("searchT", searchTransPanel);
		contentPane.add("sortT", sortTransPanel);
      contentPane.add("viewT", viewTransPanel);
      
      mainFrame.setVisible(true);
   } 
}
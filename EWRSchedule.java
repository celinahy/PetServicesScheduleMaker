package edu.ucalgary.oop;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;  
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * This class is used to create the GUI for the EWR Schedule Builder.
 * @author Celina Hy
 * @version 1.0
 * @since 1.0
 */
public class EWRSchedule implements ActionListener{
    
    private String nextDay;
    private JTextArea dataForSchedule;
    private JFrame frame;
    private JButton connectData;
    private JTextField pathName;
    private String pathNameString;
    private JTextField userName;
    private String userNameString;
    private JPasswordField passWord;
    private String passWordString;
    private JTable table;
    private JButton generateScheduleButton;
    private JButton toTxtButton;
    private JButton changeDBButton;
    private Schedule schedule;
    private ArrayList<Treatment> treatmentsList;
    private Object[][] entriesForTable;

    /**
     * This constructor generates the GUI that is displayed on screen.
     */
    public EWRSchedule() {
        frame= new JFrame("EWR Schedule");
        
        startScreen(frame);
        frame.setSize(620, 800);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * This method creates the start screen of the GUI.
     * Start screen contains the login infrastructure and input field checking.
     * @param frame The frame of the GUI.
     */
    public void startScreen(JFrame frame) {

        //title
        String partialTitle = "EWR Schedule Builder for (";

        //date to be appended onto title
        LocalDate nextDay = LocalDate.now();
        nextDay = nextDay.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-dd-yyyy");
        this.nextDay = nextDay.format(formatter);
        String fullTitle = partialTitle + this.nextDay + ")";
        JLabel title = new JLabel(fullTitle);
        title.setBounds(50,50,300,40);

        JPanel credentials = new JPanel();
        credentials.setLayout(new BorderLayout());

        JLabel instructions = new JLabel("Please edit credentials.");

        //panels for container of credentials, and for each credential themselves
        JPanel inputFields = new JPanel();
        inputFields.setLayout(new BoxLayout(inputFields, BoxLayout.Y_AXIS));

        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new FlowLayout());

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new FlowLayout());

        JPanel passWordPanel = new JPanel();
        passWordPanel.setLayout(new FlowLayout());

        JLabel path = new JLabel("Pathname:");
        JLabel user = new JLabel("Username: ");
        JLabel password = new JLabel("Password:");

        this.pathName = new JTextField("jdbc:mysql://localhost/ewr", 20);
        this.userName = new JTextField("user1", 20);
        this.passWord = new JPasswordField("ensf", 20);

        //adding label and textfield to each credential panel
        pathPanel.add(path);
        pathPanel.add(pathName);

        userPanel.add(user);
        userPanel.add(userName);

        passWordPanel.add(password);
        passWordPanel.add(passWord);

        //adding credential panels to credential container
        inputFields.add(pathPanel);
        inputFields.add(userPanel);
        inputFields.add(passWordPanel);

        JLabel warning = new JLabel("If no credentials are provided, default credentials will be used.");

        credentials.add(instructions, BorderLayout.NORTH);
        credentials.add(inputFields, BorderLayout.CENTER);
        credentials.add(warning, BorderLayout.PAGE_END);
        credentials.setBounds(50, 150, 500, 200);

        connectData = new JButton("Connect to Database");
        connectData.setBounds(50, 500, 500, 40);
        connectData.addActionListener(this);

        frame.add(title);
        frame.add(credentials);
        frame.add(connectData);
    }

    /**
     * This method creates the screen after connecting to the database, and gives the user the option to edit start hour of any treatment.
     * @param frame The frame of the GUI.
     * @param path The path to the database.
     * @param user The username to the database.
     * @param password The password to the database.
     * @throws CouldNotConnectToDatabaseException If the database cannot be connected to.
     * @throws SQLException If there is an error with the SQL query.
     * @throws CouldNotCloseDatabaseConnectionException If the database connection cannot be closed.
     */
    public void midScreen(JFrame frame, String path, String user, String password) throws CouldNotConnectToDatabaseException, SQLException, CouldNotCloseDatabaseConnectionException {

        try {
            this.schedule = new Schedule();
            schedule.createTaskList(path, user, password);

            frame.getContentPane().removeAll();
            frame.revalidate();
            frame.repaint();

            String partialTitle = "EWR Schedule Builder for (";
            String fullTitle = partialTitle + this.nextDay + ")";

            JLabel title = new JLabel(fullTitle);
            title.setBounds(50,50,300,40);

            JLabel tLabel = new JLabel("Confirm to make schedule, or modify the start hour of any treatments.");
            JLabel extraInstructions = new JLabel("Please ensure the start hour is an integer between 0 and 23.");
            tLabel.setBounds(50, 90, 500, 40);
            extraInstructions.setBounds(50, 110, 500, 40);

            String[] columnTitles = { "Treatment ID", "Animal Name(s)", "Description","Start Time"};

            treatmentsList = schedule.getTreatmentList();
            entriesForTable = new Object[treatmentsList.size()][4];
            int currentIndex = 0;

            for(Treatment treatment : treatmentsList){

                entriesForTable[currentIndex][0] = treatment.getTreatmentID();
                entriesForTable[currentIndex][1] = treatment.getAnimalName();
                entriesForTable[currentIndex][2] = treatment.getDescription();
                entriesForTable[currentIndex][3] = treatment.getStartHour();
                currentIndex += 1;
            }

            TableModel model = new EditableTableModel(columnTitles, entriesForTable);
            table = new JTable(model);
            table.createDefaultColumnsFromModel();
            table.setGridColor(Color.BLACK);

            DefaultTableCellRenderer leftAlign = new DefaultTableCellRenderer();
            leftAlign.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
            table.getColumn("Treatment ID").setCellRenderer(leftAlign);
            table.getColumn("Start Time").setCellRenderer(leftAlign);

            JScrollPane table2 = new JScrollPane(table);
            table2.setBounds(50, 150, 500, 500);

            generateScheduleButton = new JButton("Generate Schedule"); //other button will say export to file
            generateScheduleButton.setBounds(420, 700, 150, 40);
            generateScheduleButton.addActionListener(this);

            frame.add(title);
            frame.add(tLabel);
            frame.add(extraInstructions);
            frame.add(table2);
            frame.add(generateScheduleButton);
        }

        catch (CouldNotConnectToDatabaseException e) {
            JOptionPane.showMessageDialog(null, "Could not connect to database with the current credentials.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Exception");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        catch (CouldNotCloseDatabaseConnectionException e) {
            JOptionPane.showMessageDialog(null, "Could not close database.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * This method creates the final screen of the GUI that displays the schedule, incomplete or complete.
     * @param frame The frame of the GUI.
     */
    public void finalScreen(JFrame frame) {

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();

        String partialTitle = "EWR Schedule Builder for (";
        String fullTitle = partialTitle + this.nextDay + ")";

        JLabel title = new JLabel(fullTitle);
        title.setBounds(50,50,300,40);

        JLabel displayInfo = new JLabel("Generated schedule below. ");
        displayInfo.setBounds(50, 110, 500, 40);

        String errorMessage = new String();
        boolean exceptionCaught = false;
        try {
            schedule.createSchedule();
        } catch (CannotCreateScheduleException e) {
            errorMessage = "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n"
            +e.getMessage() + "\n"
            + "PLEASE EDIT DATABASE ACCORDINGLY, AND REGENERATE SCHEDULE\n" + "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n";
            exceptionCaught = true;
        }

        dataForSchedule = new JTextArea();
        dataForSchedule.setText(errorMessage + schedule.print());
        dataForSchedule.setCaretPosition(0);
        dataForSchedule.setEditable(false);
        dataForSchedule.setBounds(50,150,500,500);

        JScrollPane scroll = new JScrollPane(dataForSchedule);
        scroll.setBounds(50, 150, 500, 500);

        changeDBButton = new JButton("Edit database");
        changeDBButton.setBounds(260, 700, 150, 40);
        changeDBButton.addActionListener(this);

        toTxtButton = new JButton("Export to .txt File");
        toTxtButton.setBounds(420, 700, 150, 40);
        toTxtButton.addActionListener(this);

        frame.add(title);
        frame.add(displayInfo);
        frame.add(scroll);
        frame.add(changeDBButton);
        frame.add(toTxtButton);

        //volunteer popup info
        if (exceptionCaught == false) {
            boolean[] volunteerList = schedule.getVolunteerList();
            int index = 0;
            for (boolean hour: volunteerList) {
                if (hour == true) {
                    if (index <= 9) {
                        String hourToString = Integer.toString(index);
                        String message = "Can you confirm that you've called a volunteer for hour 0" + hourToString + ":00 (based on military time)";
                        while (true) {
                            int volunteerPopup = JOptionPane.showConfirmDialog(null, message, "Volunteer Required", JOptionPane.YES_NO_OPTION);
                            if (volunteerPopup == 0) {
                                break;
                            }
                        }

                    }
                    else {
                        String hourToString = Integer.toString(index);
                        String message = "Can you confirm that you've called a volunteer for hour " + hourToString + ":00 (based on military time)";
                        while (true) {
                            int volunteerPopup = JOptionPane.showConfirmDialog(null, message, "Volunteer Required", JOptionPane.YES_NO_OPTION);
                            if (volunteerPopup == 0) {
                                break;
                            }
                        }
                    }
                }
                index += 1;
            }

        }
    }

    /**
     * This method exports the schedule to a .txt file.
     * @throws IOException If the file cannot be created.
     */
    public void toTxt() throws IOException {

        String filename = this.nextDay + "-Schedule.txt";
        File fileObj = new File(filename);
        boolean test;

         try {
            test = fileObj.createNewFile();
            if (test) {
                 try {
                    FileWriter writer = new FileWriter(fileObj);
                    this.dataForSchedule.write(writer);
                    writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            } 
            else {
                JOptionPane.showMessageDialog(null, "File already exists on your machine");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }
    
    /**
     * This is a service method that serves events.
     * @param event The event that triggers the method.
     */
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == connectData) {
            String pathNameTemp = pathName.getText();
            String userNameTemp = userName.getText();

            String pathWS1 = pathNameTemp.replace("/^\\s+|\\s+$/g", "");
            String userWS2 = userNameTemp.replace("/^\\s+|\\s+$/g", "");

            pathNameString = pathWS1.replace(" ", "");
            userNameString = userWS2.replace(" ", "");
            passWordString = new String(passWord.getPassword());

            try {
                midScreen(frame, pathNameString, userNameString, passWordString);
            }

            catch (CouldNotConnectToDatabaseException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            catch (CouldNotCloseDatabaseConnectionException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        if(event.getSource() == generateScheduleButton){

            int index = 0;
            for (Treatment treatment: treatmentsList) {
                Object startHour = (table.getValueAt(index, 3));
                int startHourInt = (int)startHour;
                treatment.setStartHour(startHourInt);
                index += 1;
            }

            try {
                
                schedule.updateTreatments(pathNameString, userNameString, passWordString, treatmentsList);
                finalScreen(frame);
            }

            catch (CouldNotConnectToDatabaseException e) {
                throw new RuntimeException(e);
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
            catch (CouldNotCloseDatabaseConnectionException e) {
                throw new RuntimeException(e);
            }
        }
        
        if(event.getSource() == changeDBButton) {
            try {
                midScreen(frame, pathNameString, userNameString, passWordString);
            }

            catch (CouldNotConnectToDatabaseException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            catch (CouldNotCloseDatabaseConnectionException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        if(event.getSource() == toTxtButton) {
            try {
                toTxt();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * This is the main method of the program.
     * This is the entry method of the program.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            new EWRSchedule();
        });
    }

}

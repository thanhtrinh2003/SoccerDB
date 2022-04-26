/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import DataStructures.SoccerPlayer;
import DataStructures.Team;
import HelpMenu.JHelpFrame;
import StaticClasses.InfoTableModel;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author jward
 */
public class MainFrame extends javax.swing.JFrame {

    private Team team;
    private String filePath;
    private String teamName;
    private String teamMascot;

    /** Creates new form MainFrame */
    public MainFrame() {
        guiOptions();

        // Get filepath of deafault save location
        setDefaultFilePath();
        load();
                
        initComponents();
        
        saveMenuItem.setEnabled(true); 
        teamPanel.setLayout(new BorderLayout());
        teamPanel.updateUI();
        teamPanel.removeAll();
        
        updateTeamPanel(new TeamInfo(team.getTeamName(), team.getTeamMascot(), filePath + "logo.png"));
        refreshPanels();
    }

    // Set options, look, and feel of GUI
    private void guiOptions()
    {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null,
                    ex);
        }        
    }

    
    
    // **** All Methods for file I/O are written here **** //
    // *************************************************** //
    private void setDefaultFilePath(){
        String dir = System.getProperty("user.dir");
        String defaultPath = dir + "/DATA/default.txt";

        String path = "";

        try {
            // Load file and read info to RAM from file
            BufferedReader loadFile = new BufferedReader(new FileReader(defaultPath));
            path = loadFile.readLine();
            path = dir + "/DATA/" + path + "/";
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error trying to "
                    + "load default file: " + ex,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        filePath = path;        
    }
        
    protected String getFilePath(){
        return filePath + "data.txt";
    }
    
    public void load() {
        try {
            // Load file and read info to RAM from file
            BufferedReader loadFile;
            loadFile = new BufferedReader(new FileReader(
                    filePath + "data.txt"));
            
            teamName = loadFile.readLine();
            teamMascot = loadFile.readLine();
            team = new Team(teamName, teamMascot);
            team.setNumGames(0);

            // Create a string to read in data from the file
            String input;
            
            /** Read in from text file 2 lines for each athlete while there are 
             * still lines to be read in
             * First line is their personal info (first, last, number, position)
             * Second line is their goal data (0, 3, 1, 0, 1, etc.)
             */ 
            while ((input = loadFile.readLine()) != null) {
                String[] values = input.split(",");
                String firstName = values[0];
                String lastName = values[1];
                int num = Integer.parseInt(values[2]);
                String pos = values[3];
                
                // Read in goal data
                input = loadFile.readLine();
                values = input.split(",");
                ArrayList<Integer> goals = new ArrayList<>();
                team.setNumGames(values.length);
                for (int g = 0; g < team.getNumGames(); g++) {
                    goals.add(Integer.parseInt(values[g]));
                }

                team.getPlayers().add(new SoccerPlayer(firstName, lastName, num, pos, goals));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error trying to "
                    + "load file: " + ex,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void save() {
        // Output the data to a text file
        try (PrintWriter writer = new PrintWriter(new File(filePath + "data.txt"))) {
        	//print out the name of high school and the team name first
        	writer.println(teamName);
        	writer.println(teamMascot);
        	
            // Loop through players and write their information to the file
            // Names on the first line, goals on the second line
            for (int n = 0; n < team.getPlayers().size(); n++) {
                writer.println(team.getPlayers().get(n).getFirstName() + ","
                        + team.getPlayers().get(n).getLastName() + ","
                        + team.getPlayers().get(n).getNumber() + ","
                        + team.getPlayers().get(n).getPosition());
                for (int g = 0; g < team.getNumGames() - 1; g++) {
                    writer.print(team.getPlayers().get(n).getGoals(g) + ",");
                }
                writer.print(team.getPlayers().get(n).getGoals(team.getNumGames() - 1));
                writer.println();
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error trying to load file: " + ex,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void importPlayers(String file) {
        String input;

        try {
            // Load file and read info to RAM from file
            BufferedReader loadFile = new BufferedReader(new FileReader(file));

            /** Read in from text file 1 lines for each athlete while there are 
             * still lines to be read in (Only takes in personal information)
             * Format: first, last, number, position
             */ 
            while ((input = loadFile.readLine()) != null) {
                String[] values = input.split(",");
                String firstName = values[0];
                String lastName = values[1];
                int num = Integer.parseInt(values[2]);
                String pos = values[3];

                team.getPlayers().add(new SoccerPlayer(firstName, lastName, num, pos, team.getNumGames()));
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error trying to "
                    + "load file: " + ex,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }






    
    // **** Methods which will manipulate the data in the Database **** //
    // **************************************************************** //
    public void addPlayer(String firstName, String lastName, int number, String position) {
        team.getPlayers().add(new SoccerPlayer(firstName, lastName, number, position, team.getNumGames()));
    }

    public void deletePlayer(int index) {
        team.getPlayers().remove(index);
    }

    private void editPlayer(int athlete, int type) {
        switch (type) {
            case 0:
                String firstName = JOptionPane.showInputDialog(this, "Edit first name for \n"
                        + team.getPlayers().get(athlete).getFirstName() + " "
                        + team.getPlayers().get(athlete).getLastName(), team.getPlayers().get(athlete).getFirstName());
                team.getPlayers().get(athlete).setFirstName(firstName);
                break;
            case 1:
                String lastName = JOptionPane.showInputDialog(this, "Edit last name for \n"
                        + team.getPlayers().get(athlete).getFirstName() + " "
                        + team.getPlayers().get(athlete).getLastName(), team.getPlayers().get(athlete).getLastName());
                team.getPlayers().get(athlete).setLastName(lastName);
                break;
            case 2:
                 try {
                    int number = Integer.parseInt(JOptionPane.showInputDialog(this, "Edit number for \n"
                            + team.getPlayers().get(athlete).getFirstName() + " "
                            + team.getPlayers().get(athlete).getLastName(), team.getPlayers().get(athlete).getNumber()));
                    
                    team.getPlayers().get(athlete).setNumber(number);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter an number.",
                            "Invalid Data",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 3:
                String position = JOptionPane.showInputDialog(this, "Edit position for \n"
                        + team.getPlayers().get(athlete).getFirstName() + " "
                        + team.getPlayers().get(athlete).getLastName(), team.getPlayers().get(athlete).getPosition());
                team.getPlayers().get(athlete).setPosition(position);
                break;
            default:
                break;
        }
        refreshPanels();
    }

    public void sortPlayers(int type) {
        // Selection Sort
        for (int end = team.getPlayers().size() - 1; end > 0; end--) {
            int max = 0;
            // Find index value of Max
            for (int x = 1; x <= end; x++) {
                if (team.getPlayers().get(x).get(type).toLowerCase().compareTo(
                        team.getPlayers().get(max).get(type).toLowerCase()) > 0) {
                    max = x;
                }
            }
            // Swap Max with End
            SoccerPlayer temp = team.getPlayers().get(max);
            team.getPlayers().set(max, team.getPlayers().get(end));
            team.getPlayers().set(end, temp);
        }
    }

    private void addGame() {
    	team.setNumGames(team.getNumGames()+1);
        for (int p = 0; p < team.getPlayers().size(); p++) {
            team.getPlayers().get(p).setGoals(team.getNumGames(), 0);
        }
        refreshPanels();
    }

    private void editGoalData(int athlete, int game) {
        if (game >= 0) {
            int newGoals = Integer.parseInt(
                    JOptionPane.showInputDialog(this, "Enter goals scored\n"
                    + "for " + team.getPlayers().get(athlete).getFirstName() + " "
                    + team.getPlayers().get(athlete).getLastName(), team.getPlayers().get(athlete).getGoals(game)));

            team.getPlayers().get(athlete).setGoals(game, newGoals);
            refreshPanels();
        }
    }


    
    
    
    // ************* Methods which change/update the GUI ************* //
    // *************************************************************** //
    private void updateTeamPanel(JPanel current){        
        teamPanel.setLayout(new BorderLayout());
        teamPanel.updateUI();
        teamPanel.removeAll();
        teamPanel.add(current);
    }
    
    protected void refreshPanels() {
        drawAthletePanel();
        drawGoalPanel();
    }

    private void drawAthletePanel() {
        // Get Roster Header
        String[] rosterHeader = {
            "First Name", "Last Name", "Number", "Position", "Avg Goals"
        };

        // Get data for table
        String[][] rosterData = new String[team.getPlayers().size()][rosterHeader.length];
        // Populate table with data
        for (int x = 0; x < rosterData.length; x++) {
            // Athlete Info
            rosterData[x][0] = team.getPlayers().get(x).getFirstName();
            rosterData[x][1] = team.getPlayers().get(x).getLastName();
            rosterData[x][2] = String.valueOf(team.getPlayers().get(x).getNumber());
            rosterData[x][3] = team.getPlayers().get(x).getPosition();
            rosterData[x][4] = String.format("%.2f", team.getPlayers().get(x).getAverage());
        }
        
        //Create JTable with all our data
        rosterTable.setModel(new InfoTableModel(rosterData, rosterHeader));

        rosterTable.getColumnModel().getColumn(0).setMinWidth(100);
        rosterTable.getColumnModel().getColumn(1).setMinWidth(25);
        rosterTable.getColumnModel().getColumn(2).setMinWidth(100);
    }
    
    private void drawGoalPanel() {
        // Get Roster Header
        String[] goalsHeader = new String[team.getNumGames() + 2];
        goalsHeader[0] = "First Name";
        goalsHeader[1] = "Last Name";

        for (int n = 1; n <= team.getNumGames(); n++) {
            goalsHeader[n + 1] = "Game " + n;
        }

        // Get data for table
        String[][] goalsData = new String[team.getPlayers().size()][goalsHeader.length];
        // Populate table with data
        for (int x = 0; x < goalsData.length; x++) {
            // Athlete Info
            goalsData[x][0] = team.getPlayers().get(x).getFirstName();
            goalsData[x][1] = team.getPlayers().get(x).getLastName();
            for (int n = 2; n <= team.getNumGames()  + 1; n++) {
                goalsData[x][n] = String.valueOf(team.getPlayers().get(x).getGoals(n - 2));
            }
        }

        // Create JTable with all our data
        goalsTable.setModel(new InfoTableModel(goalsData, goalsHeader));

        goalsTable.getColumnModel().getColumn(0).setMinWidth(100);
    }
   
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        teamInfoPanel = new javax.swing.JPanel();
        teamPanel = new javax.swing.JPanel();
        switchPanelButton = new javax.swing.JButton();
        infoPanel = new javax.swing.JPanel();
        infoScrollPane = new javax.swing.JScrollPane();
        rosterTable = new javax.swing.JTable();
        addPlayerButton = new javax.swing.JButton();
        deletePlayerButton = new javax.swing.JButton();
        goalsPanel = new javax.swing.JPanel();
        goalsScrollPane = new javax.swing.JScrollPane();
        goalsTable = new javax.swing.JTable();
        addGameButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        importNamesMenuItem = new javax.swing.JMenuItem();
        quitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        addPlayerMenuItem = new javax.swing.JMenuItem();
        deletePlayerMenuItem = new javax.swing.JMenuItem();
        sortMenu = new javax.swing.JMenu();
        sortFirstNameMenuItem = new javax.swing.JMenuItem();
        sortLastNameMenuItem = new javax.swing.JMenuItem();
        sortNumberMenuItem = new javax.swing.JMenuItem();
        sortPositionMenuItem = new javax.swing.JMenuItem();
        sortAverageGoalsMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout teamPanelLayout = new javax.swing.GroupLayout(teamPanel);
        teamPanel.setLayout(teamPanelLayout);
        teamPanelLayout.setHorizontalGroup(
            teamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 355, Short.MAX_VALUE)
        );
        teamPanelLayout.setVerticalGroup(
            teamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 185, Short.MAX_VALUE)
        );

        switchPanelButton.setText("View MVP");
        switchPanelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                switchPanelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout teamInfoPanelLayout = new javax.swing.GroupLayout(teamInfoPanel);
        teamInfoPanel.setLayout(teamInfoPanelLayout);
        teamInfoPanelLayout.setHorizontalGroup(
            teamInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(teamInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(teamInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teamPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(teamInfoPanelLayout.createSequentialGroup()
                        .addComponent(switchPanelButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        teamInfoPanelLayout.setVerticalGroup(
            teamInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(teamInfoPanelLayout.createSequentialGroup()
                .addComponent(teamPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(switchPanelButton))
        );

        jTabbedPane1.addTab("Team Information", teamInfoPanel);

        rosterTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        rosterTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rosterTableMouseClicked(evt);
            }
        });
        infoScrollPane.setViewportView(rosterTable);

        addPlayerButton.setText("Add Player");
        addPlayerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPlayerButtonActionPerformed(evt);
            }
        });

        deletePlayerButton.setText("Delete Player");
        deletePlayerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePlayerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infoScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addComponent(addPlayerButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deletePlayerButton)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(infoScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addPlayerButton)
                    .addComponent(deletePlayerButton))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Roster", infoPanel);

        goalsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        goalsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goalsTableMouseClicked(evt);
            }
        });
        goalsScrollPane.setViewportView(goalsTable);

        addGameButton.setText("Add Game");
        addGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGameButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout goalsPanelLayout = new javax.swing.GroupLayout(goalsPanel);
        goalsPanel.setLayout(goalsPanelLayout);
        goalsPanelLayout.setHorizontalGroup(
            goalsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(goalsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(goalsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(goalsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                    .addComponent(addGameButton))
                .addContainerGap())
        );
        goalsPanelLayout.setVerticalGroup(
            goalsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, goalsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(goalsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addGameButton)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Goals", goalsPanel);

        fileMenu.setText("File");

        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setText("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Save as...");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        importNamesMenuItem.setText("Import");
        importNamesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importNamesMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(importNamesMenuItem);

        quitMenuItem.setText("Quit");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(quitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        addPlayerMenuItem.setText("Add Player");
        addPlayerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPlayerMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(addPlayerMenuItem);

        deletePlayerMenuItem.setText("Delete Player");
        deletePlayerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePlayerMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(deletePlayerMenuItem);

        sortMenu.setText("Sort");

        sortFirstNameMenuItem.setText("First Name");
        sortFirstNameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortFirstNameMenuItemActionPerformed(evt);
            }
        });
        sortMenu.add(sortFirstNameMenuItem);

        sortLastNameMenuItem.setText("Last Name");
        sortLastNameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortLastNameMenuItemActionPerformed(evt);
            }
        });
        sortMenu.add(sortLastNameMenuItem);

        sortNumberMenuItem.setText("Number");
        sortNumberMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortNumberMenuItemActionPerformed(evt);
            }
        });
        sortMenu.add(sortNumberMenuItem);

        sortPositionMenuItem.setText("Position");
        sortPositionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortPositionMenuItemActionPerformed(evt);
            }
        });
        sortMenu.add(sortPositionMenuItem);

        sortAverageGoalsMenuItem.setText("Average Goals");
        sortAverageGoalsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortAverageGoalsMenuItemActionPerformed(evt);
            }
        });
        sortMenu.add(sortAverageGoalsMenuItem);

        editMenu.add(sortMenu);

        menuBar.add(editMenu);

        helpMenu.setText("Help");

        helpMenuItem.setText("Help Menu");
        helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(helpMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // ************* Methods which are called from GUI *************** //
    // *************************************************************** //
    private void switchPanelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_switchPanelButtonActionPerformed
        if(switchPanelButton.getText().equals("View MVP")){
            updateTeamPanel(new ProfilePanel(team.getMVP()));
            switchPanelButton.setText("View Team Info");
        }
        else
        {
            updateTeamPanel(new TeamInfo(team.getTeamName(), team.getTeamMascot(),  filePath + "logo.png"));
            switchPanelButton.setText("View MVP");
        }
    }//GEN-LAST:event_switchPanelButtonActionPerformed

    private void rosterTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rosterTableMouseClicked
        if (evt.getClickCount() > 1) {
            int athlete = rosterTable.getSelectedRow();
            int type = rosterTable.getSelectedColumn();

            editPlayer(athlete, type);
        }
    }//GEN-LAST:event_rosterTableMouseClicked

    private void addPlayerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPlayerButtonActionPerformed
        JDialog dialog = new JDialog(this, true);
        dialog.getContentPane().add(new AddPanel(this));
        dialog.pack();
        dialog.setVisible(true);

        refreshPanels();
    }//GEN-LAST:event_addPlayerButtonActionPerformed

    private void deletePlayerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePlayerButtonActionPerformed
        int index = rosterTable.getSelectedRow();
        if(index == -1)
        {
        	JOptionPane.showMessageDialog(this, "Please select the player you want to delete");
        }
        else
        {
        	deletePlayer(index);
            refreshPanels();
        }
    }//GEN-LAST:event_deletePlayerButtonActionPerformed

    private void goalsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goalsTableMouseClicked
        if (evt.getClickCount() > 1) {
            int athlete = goalsTable.getSelectedRow();
            int game = goalsTable.getSelectedColumn() - 2;

            editGoalData(athlete, game);
        }
    }//GEN-LAST:event_goalsTableMouseClicked

    private void addGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addGameButtonActionPerformed
        addGame();
    }//GEN-LAST:event_addGameButtonActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        JDialog dialog = new JDialog(this, true);
        dialog.getContentPane().add(new OpenPanel(this, OpenPanel.OPEN_FILE));
        dialog.pack();
        dialog.setVisible(true);

        setDefaultFilePath();
        load();
        saveMenuItem.setEnabled(true); 
        updateTeamPanel(new TeamInfo(team.getTeamName(), team.getTeamMascot(),  filePath + "logo.png"));
        refreshPanels();
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitMenuItemActionPerformed
        save();
        System.exit(0);
    }//GEN-LAST:event_quitMenuItemActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        save();
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
        JDialog dialog = new JDialog(this, true);
        dialog.getContentPane().add(new SavePanel(this));
        dialog.pack();
        dialog.setVisible(true);
        
        save();
        saveMenuItem.setEnabled(true); 
        refreshPanels(); 
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void importNamesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importNamesMenuItemActionPerformed
        JDialog dialog = new JDialog(this, true);
        dialog.getContentPane().add(new OpenPanel(this, OpenPanel.IMPORT));
        dialog.pack();
        dialog.setVisible(true);

        refreshPanels();
    }//GEN-LAST:event_importNamesMenuItemActionPerformed

    private void addPlayerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPlayerMenuItemActionPerformed
        JDialog dialog = new JDialog(this, true);
        dialog.getContentPane().add(new AddPanel(this));
        dialog.pack();
        dialog.setVisible(true);

        refreshPanels();
    }//GEN-LAST:event_addPlayerMenuItemActionPerformed

    private void deletePlayerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletePlayerMenuItemActionPerformed
        int index = rosterTable.getSelectedRow();
        deletePlayer(index);
        refreshPanels();
    }//GEN-LAST:event_deletePlayerMenuItemActionPerformed

    private void sortFirstNameMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortFirstNameMenuItemActionPerformed
        sortPlayers(SoccerPlayer.FIRSTNAME);
        refreshPanels();
    }//GEN-LAST:event_sortFirstNameMenuItemActionPerformed

    private void sortLastNameMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortLastNameMenuItemActionPerformed
        sortPlayers(SoccerPlayer.LASTNAME);
        refreshPanels();
    }//GEN-LAST:event_sortLastNameMenuItemActionPerformed

    private void sortNumberMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortNumberMenuItemActionPerformed
        sortPlayers(SoccerPlayer.NUMBER);
        refreshPanels();
    }//GEN-LAST:event_sortNumberMenuItemActionPerformed

    private void sortPositionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortPositionMenuItemActionPerformed
        sortPlayers(SoccerPlayer.POSITION);
        refreshPanels();
    }//GEN-LAST:event_sortPositionMenuItemActionPerformed

    private void sortAverageGoalsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortAverageGoalsMenuItemActionPerformed
        sortPlayers(SoccerPlayer.AVERAGE);
        refreshPanels();
    }//GEN-LAST:event_sortAverageGoalsMenuItemActionPerformed

    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMenuItemActionPerformed
        new JHelpFrame("HelpMenu/HelpMain.html").setVisible(true);
    }//GEN-LAST:event_helpMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addGameButton;
    private javax.swing.JButton addPlayerButton;
    private javax.swing.JMenuItem addPlayerMenuItem;
    private javax.swing.JButton deletePlayerButton;
    private javax.swing.JMenuItem deletePlayerMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPanel goalsPanel;
    private javax.swing.JScrollPane goalsScrollPane;
    private javax.swing.JTable goalsTable;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JMenuItem importNamesMenuItem;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JScrollPane infoScrollPane;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JTable rosterTable;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem sortAverageGoalsMenuItem;
    private javax.swing.JMenuItem sortFirstNameMenuItem;
    private javax.swing.JMenuItem sortLastNameMenuItem;
    private javax.swing.JMenu sortMenu;
    private javax.swing.JMenuItem sortNumberMenuItem;
    private javax.swing.JMenuItem sortPositionMenuItem;
    private javax.swing.JButton switchPanelButton;
    private javax.swing.JPanel teamInfoPanel;
    private javax.swing.JPanel teamPanel;
}

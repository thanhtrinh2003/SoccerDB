package DataStructures;

import java.util.ArrayList;

/**
 * A Class to keep details of Barbies
 * @author Joe 
 * @version 20081030
 */
public class SoccerPlayer {
    // instance variables - or data members

    private String firstName;
    private String lastName;
    private int number;
    private String position;
    private String imageFileLocation;
    private ArrayList<Integer> goals;
    
    // Static variables for sort method
    public static final int FIRSTNAME = 0;
    public static final int LASTNAME = 1;
    public static final int NUMBER = 2;
    public static final int POSITION = 3;
    public static final int AVERAGE = 4;
    
    public static final int MAX_GAMES = 25;

    // Constructors
    public SoccerPlayer() {
        this(null, null, 0, null);
    }

    public SoccerPlayer(String firstName, String lastName, int number, String pos) {
        this(firstName, lastName, number, pos, null, new ArrayList<Integer>());
    }

    public SoccerPlayer(String firstName, String lastName, int number, String pos,
            int numGames) {
        this(firstName, lastName, number, pos, null, null);
        
        goals = new ArrayList<Integer>();
        for (int n = 0; n < numGames; n++) {
            goals.add(0);
        }
    }

    public SoccerPlayer(String firstName, String lastName, int number, String pos,
            ArrayList<Integer> goals) {
        this(firstName, lastName, number, pos, null, goals);
    }

    public SoccerPlayer(String firstName, String lastName, int number, String pos,
            String imageFileLocation, ArrayList<Integer> goals) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.position = pos;
        this.imageFileLocation = imageFileLocation;
        this.goals = goals;
    }
    
    
    // Mutator Methods
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setGoals(int gameNumber, int numGoals) {
        if(gameNumber > goals.size())
        {
            goals.add(gameNumber-1, numGoals);
        }
        else
        {
            goals.set(gameNumber, numGoals);
        }
}
    
    public void setImageFileLocation(String imageFileLocation){
        this.imageFileLocation = imageFileLocation;
    }

    // Accessor Methods
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getNumber() {
        return number;
    }

    public String getPosition() {
        return position;
    }
    
    public String getImageFileLocation(){
        return imageFileLocation;
    }

    public ArrayList<Integer> getGoals() {
        return goals;
    }
    
    public int getGoals(int gameNumber) {
        return goals.get(gameNumber);
    }

    public double getAverage() {
        double total = 0;
        double numGames = 0;

        for (int n = 0; n < goals.size(); n++) {
            total = total + goals.get(n);
            numGames++;
        }

        if (numGames > 0) {
            return (total / numGames);
        } else {
            return -1;
        }
    }

    public double getTotalGoals() {
        double total = 0;

        for (int n = 0; n < goals.size(); n++) {
            total = total + goals.get(n);
        }
        
        return total;
    }

    public String get(int type) {
        if (type == FIRSTNAME) {
            return firstName;
        } else if (type == LASTNAME) {
            return lastName;
        } else if (type == NUMBER) {
            return String.format("%3d", number);
        } else if (type == POSITION) {
            return position;
        } else if (type == AVERAGE) {
            return String.format("%5.2f", getAverage());
        } else {
            return "";
        }
    }

    // To String Method to Print Object Info
    @Override
    public String toString() {
        String output = String.format("%12s %12s %5d %12s", firstName, lastName, number, position);

        return (output);
    }
}

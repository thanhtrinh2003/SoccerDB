/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructures;

import java.util.ArrayList;

/**
 *
 * @author jward
 */
public class Team {
    
    private ArrayList<SoccerPlayer> players;
    private String teamName;
    private String teamMascot;
    private int numGames;
    
    public Team(String name, String mascot, String logoFile, ArrayList<SoccerPlayer> players){
        teamName = name;
        teamMascot = mascot;
        this.players = players;
    }
    
    public Team(String name, String mascot){
        this(name, mascot, null, new ArrayList<SoccerPlayer>());
    }
    
    public SoccerPlayer getMVP(){
        if(players.size() == 0)
        {
            return null;
        }
        SoccerPlayer mvp = players.get(0);
        for(SoccerPlayer cur: players){
            if(cur.getAverage() > mvp.getAverage())
            {
                mvp = cur;
            }
        }
        return mvp;
    }
    
    public void addPlayer(SoccerPlayer newPlayer)
    {
        players.add(newPlayer);
    }

    public void removePlayers(int index) {
        players.remove(index);
    }


    public ArrayList<SoccerPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<SoccerPlayer> players) {
        this.players = players;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamMascot() {
        return teamMascot;
    }

    public void setTeamMascot(String teamMascot) {
        this.teamMascot = teamMascot;
    }

    public int getNumGames() {
        return numGames;
    }

    public void setNumGames(int numGames) {
        this.numGames = numGames;
    }

    
    
    
    
    
}

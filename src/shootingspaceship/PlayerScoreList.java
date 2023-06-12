/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship;

/**
 *
 * @author 윤재민
 */
public class PlayerScoreList {
    String playerName;
    int playerScore;
    public PlayerScoreList()
    {
        System.out.println("초기화");
        playerName = "NONE";
        playerScore = 1;
        System.out.println("PlayerScoreList 기본 생성자");
    }
    public PlayerScoreList(String playerName, int playerScore)
    {
        this.playerName = playerName;
        this.playerScore = playerScore;
    }
    
    public String getPlayerName()
    {
        return playerName;
    }
    public int getPlayerScore()
    {
        return playerScore;
    }
    
    public static void SWAP(PlayerScoreList p1, PlayerScoreList p2)
    {
        PlayerScoreList temp;
        temp = p1;
        p1 = p2;
        p2 = temp;
        System.out.println("SWAP");
    }
    public String getString()
    {
        String getS = playerName;
        getS = getS.concat(":");
        getS = getS.concat(Integer.toString(playerScore));
        getS = getS.concat("/");
        //System.out.println("getStirng = "+getS );
        return getS;
    }
}

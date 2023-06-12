package shootingspaceship;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Seo
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class Sound extends Thread {
    private Player musicPlayer;
    private boolean isLoop;
    private File file;
    private FileInputStream fis;
    private BufferedInputStream bis;
    
    public Sound(String name, boolean isLoop){
        try{
            this.isLoop = isLoop;
            file = new File(shootingspaceship.Shootingspaceship.class.getResource("../sound/" + name).toURI());
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            musicPlayer = new Player(bis);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public int getTime(){
        if(musicPlayer == null)
            return 0;
        return musicPlayer.getPosition();
    }
    public void close(){
        isLoop = false;
        musicPlayer.close();
        this.interrupt();
    }
    //@Override
   public void run(){
        try{
            do{
                musicPlayer.play();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                musicPlayer = new Player(bis);
            }while (isLoop);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
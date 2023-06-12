/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skill;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import shootingspaceship.Player;
import static shootingspaceship.Shootingspaceship.stage;
import static shootingspaceship.Shootingspaceship.width;
import shootingspaceship.Shot;
import shootingspaceship.Sound;

/**
 *
 * @author 윤재민
 */
public class Metaor extends Skill{
    private double randomX;
    private int count = 0;
    private int shotCount;
    private int randomTime;
    private javax.swing.Timer timer;
    
    public Metaor(Player player, Shot[] shots)
    {
        name = "Mataor";
        speed = 3;
        damage = 10 + 2.5 * player.getDamage();
        this.shots = shots;
        icon =  new ImageIcon(".\\src\\image\\skill\\qSkill.png");
        image = icon.getImage();
        //image = player.getSubShotImage();   //임시 이미지 사용
        count = 0;
        skillSound = new Sound("qSkill.wav", false);
        skillName = METAOR;
        shotNum = 50;        //shotNum = 총알 수
        this.coolTime = 30; //얼티메이트 메테오 쿨타임 40;
        //th = new 
        System.out.println("메테오");
        super.getInformation();
        System.out.println("데미지 = 10 + 2.5 * 기본데미지");
        System.out.println("==================================");
        GenerateMataor gMataor = new GenerateMataor();
        
        th = new Thread(gMataor);
        th.start();
    }
    
    class GenerateMataor implements Runnable
    {
        public void run()
        {
            while(count < shotNum && stage > 0)
            {
                
                    randomX = Math.random()*width;
                

                for(int i=0; i<shots.length; ++i)
                {
                    if(shots[i] == null)
                    {
                        //System.out.print("메테오 shots[ "+i+" ]");
                        //System.out.print(" count : "+count);
                        shots[i] = new Shot((int)randomX, 0 ,damage, (float)5);
                        shots[i].setSizeXY(50);
                        shots[i].setImage(image);
                        ++count
                        ;break;
                    }
                }
                /*
                do{
                    randomTime = (int)(Math.random() * 1000);
                }while( 500 < randomTime && randomTime < 1000);*/

                try {
                    TimeUnit.MILLISECONDS.sleep(150);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Metaor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //System.out.println(name+" 종료");
        }
    }
/*private class Generate implements runnable {
        public void actionPerformed(ActionEvent e) {
            if(count < shotCount)
            {
            }
            
            else
                timer.stop();
        }
    }*/
}

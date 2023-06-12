/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skill;

import javax.swing.ImageIcon;
import shootingspaceship.Player;
import shootingspaceship.Shot;
import shootingspaceship.Sound;

/**
 *
 * @author 윤재민
 */
public class Shotgun extends Skill{
    public Shotgun(Player player, Shot[] shots)
    {
        name = "Shotgun";
        speed = 3;
        damage = 8 + 2 * player.getDamage();
        this.shots = shots;
        icon =  new ImageIcon(".\\src\\image\\skill\\eSkill.png");
        image = icon.getImage();
        skillSound = new Sound("eSkill.wav", false);
        //image = player.getSubShotImage();   //임시 이미지 사용
        skillName = SHOTGUN;
        shotNum = 10;    //총알 수 = 1
        this.coolTime = 5; //얼티메이트 메테오 쿨타임 40;
        //th = new 
        System.out.println("Shotgun");
        super.getInformation();
        System.out.println("데미지 = 8 + 2 * 기본데미지");
        System.out.println("==================================");
        int count=0;
        while( count < shotNum )
        {
            for(int i=0; i<shots.length; ++i)
            {
                if(shots[i] == null)
                {
                    shots[i] = player.generateShot(-0.3+(count*0.6/shotNum));
                    shots[i].setImage(image);
                    break;
                }
            }
            ++count;
        }
        
    }
}

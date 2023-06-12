/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skill;

import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import shootingspaceship.EnemyShot;
import shootingspaceship.Player;
import shootingspaceship.Shot;
import shootingspaceship.Sound;

/**
 *
 * @author 윤재민
 */
public class FireHalfRing extends Skill{
    ArrayList enemies;
    private int fireShieldHp;
    private int[] hp;
    private int distance;
    private double angle;
    private int split = 1;
    int count;
    public FireHalfRing(Player player,Shot[] shots, EnemyShot[] enemyShots)
    {
        name = "FireHalfRing";
        speed = 3;
        damage = 5 + 2 * player.getDamage();
        //스킬 기본데미지 5 + 무기 기본데미지 * 2
        this.enemyShots = enemyShots;
        skillSound = new Sound("rSkill.wav", false);
        icon = new ImageIcon(".\\src\\image\\skill\\dSkill.png");
        image = icon.getImage();   //임시 이미지 사용
        skillName = FIREHALFRING;
        shotNum = 15;    //총알 수 = 1
        this.shots = shots;
        angle = 360/shotNum/2;        //각도
        this.coolTime = 20; //얼티메이트 메테오 쿨타임 40;
        
        
        distance = 120;
        count =0 ;
        System.out.println("FireHalfRing 사용");
        super.getInformation();
        System.out.println("데미지 = 5 + 2 * 기본 데미지");
        System.out.println("==================================");
        while(count < shotNum)
        {
            for(int i=0; i<shots.length; i++)
            {
                if(shots[i]== null)
                {
                    shots[i] = new Shot(player.getX(), player.getY(), damage);       //플레이어 좌표에 shotNum 개 만큼 생성
                    shots[i].setSizeXY(30);
                    shots[i].setImage(image);
                    shots[i].moveX(distance * Math.sin(Math.toRadians( angle * count)));
                    shots[i].moveY(-distance * Math.cos(Math.toRadians( angle * count)));
                    count++;
                    break;
                }
            }
        }
        
        //Active gFireShield = new Active();
        //th = new Thread(gFireShield);
        //th.start();
    }

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skill;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import shootingspaceship.EnemyShot;
import shootingspaceship.Player;
import shootingspaceship.Shot;
import shootingspaceship.SkillName;
import shootingspaceship.Sound;
/**
 *
 * @author yunje
 */
public class Skill implements SkillName{
    protected String name;      //스킬 이름
    protected double speed;     //스킬 스피드
    protected double damage;    //스킬 데미지
    protected double slope;     //기울기

    protected int skillName;    //스킬 타입 : 공격, 방어, 버프 등등
    protected int shotNum = 0;      //총알 수 
    protected ImageIcon icon;     //아이콘
    protected Image image;        //이미지
    protected ImageIcon shotIcon; //
    protected Image shotImage;
    protected boolean loop;       //run 실행 온 오프
    protected Shot[] shots;
    protected EnemyShot[] enemyShots;
    protected double coolTime = 0;   //스킬 쿨타임
    protected Player player;
    protected Sound skillSound;
    Thread th;
    
    //생성자
    public Skill()
    {
        
    }
    
    public String getName(){
        return name;
    }

    public double getSpeed(){
        return speed;
    }
    public double getDamage(){
        return damage;
    }

    public double getCoolTime(){
        return coolTime;
    }

    public void getInformation()
    {
        System.out.println("================스킬================");
        System.out.println("이름    : "+name);
        System.out.println("쿨타입  : "+coolTime);

        switch(skillName/100)
        {
            case ATTACK:
                System.out.println("총알 수 : "+shotNum);
                System.out.println("스피드  : "+speed);
                System.out.println("데미지  : "+damage);
                System.out.println("타입    : 공격");
                break;
            case DEFENSE:
                System.out.println("타입    : 방어");
                break;
            case BUFF:
                System.out.println("타입    : 버프");
                break;
            case ULTIMATE:
                System.out.println("타입    : 궁극기");
                break;
            default:
                System.out.println("타입    : 없음");
                break;
        }
    }
    
}

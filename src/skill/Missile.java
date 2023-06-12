/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skill;

import enemy.Enemy;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import shootingspaceship.EnemyShot;
import shootingspaceship.Player;
import static shootingspaceship.Shootingspaceship.stage;
import static shootingspaceship.Shootingspaceship.width;
import shootingspaceship.Shot;
import static shootingspaceship.SkillName.ULTIMATEMETAOR;
import shootingspaceship.Sound;

/**
 *
 * @author 윤재민
 */
public class Missile extends Skill {
    private Enemy enemy, target;
    private ArrayList enemies;
    private int count;
    public Missile(Player player, Shot[] shots,ArrayList enemies)
    {
        name = "Missile";
        speed = 3;
        damage = 12 + 2.5 * player.getDamage();
        this.shots = shots;
        this.enemies = enemies;
        this.player = player;
        //icon =  new ImageIcon(".\\src\\image\\playerImage\\shot.png");
        //image = icon.getImage();
        icon = new ImageIcon(".\\src\\image\\skill\\wSkill.png");
        image = icon.getImage();   //임시 이미지 사용
        skillName = MISSILE;
        count = 0;
        shotNum = 12;    //총알 수 = 1
        target = null;  //타겟 초기화
        this.coolTime = 15; //얼티메이트 메테오 쿨타임 40;
        skillSound = new Sound("wSkill.mp3", false);
        super.getInformation();
        System.out.println("데미지 = 12 + 2.5 * 기본데미지");
        System.out.println("==================================");
        GenerateMissile gMissile = new GenerateMissile();
        th = new Thread(gMissile);
        th.start();
    }
    
    class GenerateMissile implements Runnable
    {
        public void run()
        {
            double distance = 10000;        //거리 값 저장, 비교
            double temp;            //임시 거리 값 저장
            while(count < shotNum && stage>0)
            {
                for(int i=0; i<enemies.size(); i++)//보스가 있는지 먼저 탐색
                {
                    enemy = (Enemy)enemies.get(i);
                    if(enemy.getCheckBoss() || enemy.getCheckElite())   //적 유닛중 현재 보스 또는 엘리트가 있으면 그 대상을 공격
                    {
                        target = enemy; //타겟 설정
                        break;
                    }
                }
                
                if(target != null)  
                {
                    if(target.getCheckBoss() || target.getCheckElite())//타겟이 보스 또는 엘리트 인 경우
                    {
                        for(int i=0; i<shots.length; ++i)
                        {
                            if(shots[i] == null) //샷이 비어있으면 : 총알이 없으면
                            {
                                shots[i] = new Shot(player.getX(), player.getY(), damage, target);
                                shots[i].setSizeXY(30);
                                shots[i].setImage(image);
                                count++;    //카운트 증가
                                //System.out.print("미사일 shots[ "+i+" ]");
                                //System.out.println(" count : "+count+"   target : Boss & Elite");
                                break;  //for문 벗어남
                            }
                        }


                        try {
                            TimeUnit.MILLISECONDS.sleep(200);   //미사일 딜레이
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Missile.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        continue;   // 다시 처음으로 반복 , 밑에 실행 x
                    }
                }
                
                //보스나 엘리트가 현재 없어서 타겟이 없을 경우
                for(int i=0; i<enemies.size(); i++)
                {
                    enemy = (Enemy)enemies.get(i);
                    temp = Math.sqrt(Math.pow(player.getX()- enemy.getX(), 2) + Math.pow(player.getY() - enemy.getY(),2));  //플레이어와 현재 탐색중인 유닛과의 거리
                    if(temp < distance) //현재 유닛이 기존의 최단거리 보다 짧다면 타겟은 현재 유닛으로 설정
                    {
                        target = enemy;     //타겟 업데이트
                        distance = temp;    //최단거리 업데이트
                    }
                }
                
                if(target != null)
                {
                    for(int i=0; i<shots.length; ++i)
                    {
                        if(shots[i] == null) //샷이 비어있으면 : 총알이 없으면
                        {
                            shots[i] = new Shot(player.getX(), player.getY(), damage, target);
                            shots[i].setSizeXY(30);
                            shots[i].setImage(image);
                            count++;    //카운트 증가
                            //System.out.print("미사일 shots[ "+i+" ]");
                            //System.out.println(" count : "+count+"   target : enemy");
                            break;  //for문 벗어남
                        }
                    }
                }
                
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Missile.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println(name+" 종료");
        }
    }
}

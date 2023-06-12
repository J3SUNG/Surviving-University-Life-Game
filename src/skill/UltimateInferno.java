/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skill;

import enemy.Enemy;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import shootingspaceship.EnemyShot;
import shootingspaceship.Player;
import static shootingspaceship.Shootingspaceship.*;
import shootingspaceship.Shot;
import shootingspaceship.Sound;
import static ui.Intro.ship;

/**
 *
 * @author 윤재민
 */
public class UltimateInferno extends Skill {

    private int count = 0;
    private int randomAngle = 0;
    private double radian = 0;
    ArrayList enemies;
    private int timeCount = 0;
    boolean useCheck = false;
    private Sound deadSound;

    
    public UltimateInferno(Player player, Shot[] shots, ArrayList enemies, EnemyShot[] enemyShots) {
        if (!useCheck) //사용중 아닐 때만 가능
        {
            name = "UltimateInferno";
            speed = 5;
            this.player = player;
            damage = 15 + 5 * player.getDamage();
            this.shots = shots;
            this.enemies = enemies;
            this.enemyShots = enemyShots;
            timeCount = 10;
            this.coolTime = 60; //얼티메이트 메테오 쿨타임 40;
            skillSound = new Sound("fSkill.mp3", false);
            icon = new ImageIcon(".\\src\\image\\skill\\inferno1.png");
            image = icon.getImage();   //임시 이미지 사용
            ship.setInfernoImage(image);
            skillName = ULTIMATEINFERNO;
            
            System.out.println("UltimateInferno 사용");
            System.out.println("걷잡을 수 없이 큰 불");

            getInformation();

            Active ultiInferno = new Active();
            th = new Thread(ultiInferno);
            th.start();
        } else {
            System.out.println("인페프노 사용중 : 중복 사용 불가");
        }

    }

    public void getInformation() {
        System.out.println("================스킬=====================");
        System.out.println("이름            : " + name);
        System.out.println("초당 전체 데미지 : " + damage);
        System.out.println("지속 시간       : " + timeCount);
        System.out.println("타입            : 궁극기");
        System.out.println("효과            : 공격 제거, 초당 전체 피해");
        System.out.println("데미지 = 15 + 5 * 기본데미지");
        System.out.println("========================================");
    }

    class Active implements Runnable {
        
        public void run() {
            useCheck = true;
            INFERNO_Check = true;       //슈팅스페이스쉽.자바에서 그리기로 사용
            Enemy enemy;
            while (count < timeCount) {
                switch (count % 2) {
                    case 0:
                        icon = new ImageIcon(".\\src\\image\\skill\\inferno1.png");
                        image = icon.getImage();   //임시 이미지 사용
                        ship.setInfernoImage(image);
                        break;
                    case 1:
                        icon = new ImageIcon(".\\src\\image\\skill\\inferno2.png");
                        image = icon.getImage();   //임시 이미지 사용
                        ship.setInfernoImage(image);
                        break;
                }
                for (int i = 0; i < enemies.size(); ++i) {
                    enemy = (Enemy) enemies.get(i);
                    if (enemy != null) {
                        System.out.println("인페르노 전체 데미지 : " + damage);
                        enemy.hpDecrease(damage);   //전체 enemy hp 감소
                        if (enemy.getHp() <= 0) {
                            ++ship.unitRemoveCount;      //유닛 제거 수
                            ++ship.stageUnitRemoveCount; //현재 스테이지 유닛 제거 수
                            if (enemy.getCheckElite()) //잡은 유닛이 엘리트일 경우
                            {
                                deadSound = new Sound("stage" + stage + "elitedead.mp3",false);
                                deadSound.start();
                                ship.changeStageBGM(stage);
                                ship.timer.setDelay(ship.enemyTimeGap);   //타이머 시간 다시 원래대로
                                ship.cycleCountingState = true;      //다시 사이클 카운트 시작
                            }

                            if (enemy.getCheckBoss()) //잡은 유닛이 보스일 경우
                            {
                                deadSound = new Sound("stage" + stage + "bossdead.mp3",false);
                                deadSound.start();
                                System.out.println(ship.stage + " 보스 잡음");
                                System.out.println(ship.stage + " 보스 잡음");
                                System.out.println(ship.stage + " 보스 잡음");
                                ship.changeStage();
                            }

                            //게임 스코어 증가, 총 게임 점수 = 사이클 카운팅이 true일 때 유닛을 죽여서 획득한 점수 + 플레이어 체력*30
                            if(ship.cycleCountingState)  //사이클 카운팅이 살아있을 때만
                                score += enemy.getPoint();  //잡은 유닛의 점수 합
                            ship.inGameUI.score.setText("스코어 : "+Integer.toString(score+(int)ship.player.getHp()*30));
                            enemies.remove(i);  //0 이하 체력 없엠
                            System.out.println("인페르노 : 적 제거");
                        }

                    }
                }
                for (int i = 0; i < enemyShots.length; ++i) {
                    enemyShots[i] = null;
                }
                System.out.println("인페르노 : 적 공격 제거");
                try {
                    Thread.sleep(1000);
                    count += 1;
                } catch (InterruptedException ex) {
                    Logger.getLogger(UltimateInferno.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            useCheck = false;
            INFERNO_Check = false;
        }
    }

}

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
public class UltimateMetaor extends Skill {

    private int metaorIndex = 0;
    ArrayList enemies;
    private Sound deadSound;

    public UltimateMetaor(Player player, Shot[] shots, ArrayList enemies, EnemyShot[] enemyShots) {
        name = "UltimateMataor";
        speed = 3;
        damage = 250 + 40 * player.getDamage();
        this.shots = shots;
        this.enemies = enemies;
        this.enemyShots = enemyShots;
        this.coolTime = 40; //얼티메이트 메테오 쿨타임 40;
        icon = new ImageIcon(".\\src\\image\\skill\\rSkill.png");
        image = icon.getImage();   //임시 이미지 사용
        skillName = ULTIMATEMETAOR;
        shotNum = 1;    //총알 수 = 1
        skillSound = new Sound("rSkill.wav", false);

        System.out.println("UltimateMataor 사용");
        System.out.println("참고 사항 : 여러발을 쏴도 한번 밖에 데미지 입지 않음");
        super.getInformation();
        System.out.println("데미지 = 250 + 40 * 기본 데미지");
        System.out.println("==================================");
        for (int i = 0; i < shots.length; ++i) {
            if (shots[i] == null) {
                metaorIndex = i;
                shots[i] = new Shot(player.getX(), player.getY(), damage);
                shots[i].setSizeXY(400);
                shots[i].setImage(image);
                shots[i].setUltimateShot(true);
                break;
            }
        }
        Active ultiMetaor = new Active();
        th = new Thread(ultiMetaor);
        th.start();
    }

    class Active implements Runnable {

        public void run() {

            Enemy enemy;
            while (shots[metaorIndex] != null && shots[metaorIndex].getX() < width) //매테오가 벗어나지 않았다면    //무브는 shots[i].move 에서 해줌
            {
                for (int i = 0; i < enemyShots.length; ++i) //적 공격 탐색
                {
                    if (enemyShots[i] == null) {
                        continue;
                    }
                    try {
                        if (-shots[metaorIndex].getSizeY() / 2 <= (enemyShots[i].getY() - shots[metaorIndex].getY()) && (enemyShots[i].getY() - shots[metaorIndex].getY() <= shots[metaorIndex].getSizeY() / 2)) {
                            if (-shots[metaorIndex].getSizeX() / 2 <= (enemyShots[i].getX() - shots[metaorIndex].getX()) && (enemyShots[i].getX() - shots[metaorIndex].getX() <= shots[metaorIndex].getSizeX() / 2)) {
                                enemyShots[i] = null;   //충돌 하면 에너미 샷 사라지게 함, 궁극기 메테오는 사라지지 않음
                            }
                        }
                    } catch (NullPointerException e) {
                        System.out.println("UltimateMataor : enemyShots 검사중 널포인터 발생");
                    }
                }

                for (int i = 0; i < enemies.size(); ++i) {
                    enemy = (Enemy) enemies.get(i);
                    if (enemy == null) {
                        continue;
                    }
                    if (!enemy.getUltimateHpDecrease()) //현재 궁극기에 피해를 입지 않은 enemy 만 검사
                    {
                        try {
                            if (-shots[metaorIndex].getSizeY() / 2 <= (enemy.getY() - shots[metaorIndex].getY()) && (enemy.getY() - shots[metaorIndex].getY() <= shots[metaorIndex].getSizeY() / 2)) {
                                if (-shots[metaorIndex].getSizeX() / 2 <= (enemy.getX() - shots[metaorIndex].getX()) && (enemy.getX() - shots[metaorIndex].getX() <= shots[metaorIndex].getSizeX() / 2)) {
                                    enemy.hpDecrease(damage);
                                    enemy.setUltimateHpDecrease(true);      //궁극기에 피해를 입음
                                    if (enemy.getHp() < 0) {
                                        
                                        ++ship.unitRemoveCount;      //유닛 제거 수
                                        ++ship.stageUnitRemoveCount; //현재 스테이지 유닛 제거 수
                                        if (enemy.getCheckElite()) //잡은 유닛이 엘리트일 경우
                                        {
                                            ship.timer.setDelay(ship.enemyTimeGap);   //타이머 시간 다시 원래대로
                                            ship.cycleCountingState = true;      //다시 사이클 카운트 시작
                                            deadSound = new Sound("stage" + stage + "elitedead.mp3",false);
                                            deadSound.start();
                                            ship.changeStageBGM(stage);
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
                                        enemies.remove(i);  //피가 0 이하라면 제거
                                    }
                                }
                            }
                        } catch (NullPointerException e) {
                            System.out.println("UltimateMataor : enemy 검사중 널포인터 발생");
                        }
                    }

                }
                try {
                    //th.sleep(100);
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(UltimateMetaor.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }
    }

}

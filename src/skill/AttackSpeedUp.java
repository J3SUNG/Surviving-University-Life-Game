/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skill;

import java.util.logging.Level;
import java.util.logging.Logger;
import shootingspaceship.Player;
import shootingspaceship.Sound;
import ui.InGameUI;

/**
 *
 * @author 윤재민
 */
public class AttackSpeedUp extends Skill{
    private Player player;
    private int timeDuration = 0;   //지속 시간
    boolean useCheck = false;
    private InGameUI inGameUI;
    
    public AttackSpeedUp(Player player, InGameUI inGameUI)
    {
        if(!useCheck)
        {   //사용 중이 아닐 때만
            name = "AttackSpeedUp";
            skillName = ATTACKSPEEDUP;
            this.player = player;
            this.inGameUI = inGameUI;
            Active damageUp = new Active();
            this.coolTime = 100; //얼티메이트 메테오 쿨타임 40;
            timeDuration = 40;
            skillSound = new Sound("sSkill.wav", false);
            System.out.println("AttackSpeedUp 사용");
            super.getInformation();
            System.out.println("지속시간 : "+timeDuration);
            System.out.println("효과 공격 속도 x 2");
            System.out.println("==================================");
            th = new Thread(damageUp);
            th.start();
        }else
        {
            System.out.println("                                    DamageUp 중복 사용 불가");
        }
    
    }
    class Active implements Runnable{
        public void run()
        {
            double previousAttackSpeed = player.getAttackSpeed();
            double AttackSpeed = previousAttackSpeed * 2;
            
            useCheck = true;
            System.out.println("현재 공속 : "+previousAttackSpeed);
            System.out.println("AttackSpeedUp : 시작");
            System.out.println("버프 적용 공속 : "+AttackSpeed);
            player.setAttackSpeed(AttackSpeed);
            try {//바뀐채 
                Thread.sleep(1000*timeDuration);    //20초 지속
            } catch (InterruptedException ex) {
                Logger.getLogger(DamageUp.class.getName()).log(Level.SEVERE, null, ex);
            }
            //지속 시간이 끝나고
            inGameUI.SSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/sskillx.png")));
            player.setAttackSpeed(previousAttackSpeed);
            System.out.println("AttackSpeedUp : 종료       AttackSpeed = "+player.getDamage());
            useCheck = false;
        }
    }
}

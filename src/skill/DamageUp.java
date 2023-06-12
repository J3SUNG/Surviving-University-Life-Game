/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skill;

import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import shootingspaceship.Player;
import shootingspaceship.Sound;
import ui.InGameUI;

/**
 *
 * @author 윤재민
 */
public class DamageUp extends Skill {
    private Player player;
    private int timeDuration = 0;   //지속 시간
    boolean useCheck = false;
    private InGameUI inGameUI;

    public DamageUp(Player player, InGameUI inGameUI)
    {
        if(!useCheck)
        {   //사용 중이 아닐 때만
            skillSound = new Sound("aSkill.wav", false);
            name = "DamageUp";
            skillName = DAMAGEUP;
            this.inGameUI = inGameUI;
            this.player = player;
            Active damageUp = new Active();
            this.coolTime = 60; //얼티메이트 메테오 쿨타임 40;
            timeDuration = 20;
            icon = new ImageIcon(".\\src\\image\\skill\\damageUp.png");
            image = icon.getImage();   //임시 이미지 사용
            System.out.println("DamageUp 사용");
            super.getInformation();
            System.out.println("지속시간 : "+timeDuration);
            System.out.println("효과 공격력 x 2, 공격 스킬 사용전 적용 가능");
            System.out.println("==================================");
            th = new Thread(damageUp);
            th.start();
        }else
        {
            System.out.println("                                    DamageUp 중복 사용 불가");
        }
    
    }
    class Active implements Runnable{
        int previousShotSize = player.getShotSize();
        int damageUpShotSize = (int)(player.getShotSize()*1.5);
        //Image previousShotImage = player.getShotImage();
        //Image damageUpShotImage = player.getDamageUpShotImage();
        public void run()
        {
            double previousDamage = player.getDamage();
            double upDamage = previousDamage * 2;
            player.setShotSize(damageUpShotSize);   //기본 공격 사이즈 변경
            //player.setShotImage(damageUpShotImage); //데미지 업 이미지
            useCheck = true;
            System.out.println("현재 데미지 : "+previousDamage);
            System.out.println("DamageUp : 시작");
            System.out.println("버프 데미지 : "+upDamage);
            player.setDamage(upDamage);
            try {//바뀐채 
                Thread.sleep(1000*timeDuration);    //20초 지속
                
            } catch (InterruptedException ex) {
                Logger.getLogger(DamageUp.class.getName()).log(Level.SEVERE, null, ex);
            }
            //지속 시간이 끝나고
            player.setDamage(previousDamage);           //원래 데미지로
            //player.setShotImage(previousShotImage);     //원래 이미지
            inGameUI.ASkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/askillx.png")));
            System.out.println("DamageUp : 종료       Damage = "+player.getDamage());
            useCheck = false;
            
            player.setShotSize(previousShotSize); //다시 기본 사이즈로
        }
    }
    
}

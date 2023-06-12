/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skill;

import enemy.Enemy;
import java.awt.Graphics;
import java.util.ArrayList;
import shootingspaceship.EnemyShot;
import shootingspaceship.Player;
import shootingspaceship.Shot;
import shootingspaceship.SkillName;
import shootingspaceship.Sound;
import ui.InGameUI;

/**
 *
 * @author 윤재민
 */
public class UseSkill implements SkillName{
    Player player;
    Shot[] shots;
    ArrayList enemies;
    EnemyShot[] enemyShots;
    Skill skill;
    Sound skillSound[] = new Sound[9];
    InGameUI inGameUI;
    public UseSkill(Player player, Shot[] shots, ArrayList enemies ,EnemyShot[] enemyShots, InGameUI inGameUI)
    {
        this.player = player;
        this.shots = shots;
        this.enemies = enemies;
        this.enemyShots = enemyShots;
        this.inGameUI = inGameUI;
    }
    
    public void skillSoundClose()
    {
        for(int i=0; i<9; i++)
        {
            if(skillSound[i]!=null)
                skillSound[i].close();
        }
    }
    
    public double SkillName(int select)
    {    
    
        switch(select)
        {
            case METAOR:
                skill = new Metaor(player, shots);
                skillSound[0] = new Sound("qSkill.mp3", false);
                skillSound[0].start();
                inGameUI.QSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/qskillx.png")));
                break;
            case SHOTGUN:
                skill = new Shotgun(player, shots);
                skillSound[1] = new Sound("eSkill.mp3", false);
                skillSound[1].start();
                inGameUI.ESkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/eskillx.png")));
                break;
            case BARRIER:
                player.barrierSwitch();
                skillSound[2] = new Sound("cSkill.mp3",false);
                skillSound[2].start();
                 return 0;   //배리어는 쿨타임이 없음
            case ULTIMATEMETAOR:
                skill = new UltimateMetaor(player, shots, enemies, enemyShots);
                skillSound[3] = new Sound("rSkill.mp3", false);
                skillSound[3].start();
                inGameUI.RSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/rskillx.png")));
                break;
            case MISSILE:
                skill = new Missile(player, shots, enemies);
                skillSound[4] = new Sound("wSkill.mp3", false);
                skillSound[4].start();
                inGameUI.WSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/wskillx.png")));
                break;
            case FIREHALFRING:
                skill = new FireHalfRing(player, shots,enemyShots);
                skillSound[5] = new Sound("eSkill.mp3", false);
                skillSound[5].start();
                inGameUI.DSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/dskillx.png")));
                break;
            case ULTIMATEINFERNO:
                skill = new UltimateInferno(player, shots, enemies, enemyShots);
                skillSound[6] = new Sound("fSkill.mp3", false);
                skillSound[6].start();
                inGameUI.FSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/fskillx.png")));
                break;
            case DAMAGEUP:
                skill = new DamageUp(player, inGameUI);
                skillSound[7] = new Sound("aSkill.mp3", false);
                skillSound[7].start();
                inGameUI.ASkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/askillon.png")));      //스킬이 켜져 있는 동안 쓰일 이미지
                break;
            case ATTACKSPEEDUP:
                skill = new AttackSpeedUp(player, inGameUI);
                skillSound[8] = new Sound("sSkill.mp3", false);
                skillSound[8].start();
                inGameUI.SSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/sskillon.png")));      //스킬이 켜져 있는 동안 쓰일 이미지
                break;
        }
        return skill.getCoolTime();
    }
}

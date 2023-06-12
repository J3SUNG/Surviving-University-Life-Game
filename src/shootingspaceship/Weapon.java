/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship;

import java.awt.Image;

/**
 *
 * @author yunje
 */
public class Weapon {
    private String name;        //무기 이름
    private double attackSpeed; //초당 공격 횟수
    private double damage;      //무기 데미지
    private int level;          //무기 레벨
    private Image shotImage;        //공격 이미지
    private Image damageUpShotImage;
    
    //생성자
    public Weapon(String name, double attackSpeed, double damage, Image shotImage, Image damageUpShotImage){
        this.name = name;
        this.attackSpeed = attackSpeed;
        this.damage = damage;
        level = 1;
        this.shotImage = shotImage;
        this.damageUpShotImage = damageUpShotImage;
    }
    //무기 이름
    public String getName(){
        return name;
    }
    //무기 초당 공속
    public double getAttackSpeed()
    {
        return attackSpeed;
    }
    //무기 데미지
    public double getDamage()
    {
        return damage;
    }
    //무기 레벨
    public int getLevel(){
        return level;
    }
    
    //무기 레벨업
    public void levelUp()
    {
        damage*=1.2;    //레벨업당 데미지 1.2배
        ++level;
    }
    
    public Image getShotImage()
    {
        return shotImage;
    }
    
    public Image getDamageUpShotImage()
    {
        return damageUpShotImage;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship;

/**
 *
 * @author yunje
 */
public class SkillShot extends Shot{
    private String name;    //스킬 이름
    protected int speed;    //스킬 스피드
    protected int damage;   //스킬 데미지
    //생성자
    public SkillShot(String name, int x_pos, int y_pos, int speed, int damage)
    {
        super(x_pos, y_pos);
        this.name = name;
        this.speed = speed;
        this.damage = damage;
    }
    
    public String getName(){
        return name;
    }

    public int getSpeed(){
        return speed;
    }
    public double getDamage(){
        return damage;
    }
    
}

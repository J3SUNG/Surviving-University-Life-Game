/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enemy;
//import static shootingspaceship.Shootingspaceship.e

import javax.swing.ImageIcon;
import shootingspaceship.EnemyShot;
import shootingspaceship.Player;
import shootingspaceship.Shot;

/**
 *
 * @author 윤재민
 */
public class S3Elite extends Enemy{
      
    Player target;
    double slope;
    //              x_pos,   y_pos   
    public S3Elite(int x, int y, float delta_x, float delta_y, int max_x, int max_y, float enemySpeed, Player target)
    {
        super(x,y,delta_x,delta_y,max_x, max_y, enemySpeed);
        hp = 2000;
        originalHp = hp;
        damage = 2;
        speed = 1 + enemySpeed;
        attackSpeed = 4;
        collisionDamage = 1000;
        point = 3000;
        this.target = target;
        slope = (target.getY()-y_pos)/(target.getX()-x_pos);
        unitType = ELITE3;
        checkElite = true;
        icon =  new ImageIcon(".\\src\\image\\enemyImage\\Stage3\\elite.png");
        image = icon.getImage();
        shotIcon = new ImageIcon(".\\src\\image\\enemyImage\\Stage3\\eliteShot.png");
        shotImage = shotIcon.getImage();
        sizeX = 150;
        sizeY = 150;
        collision_distanceX = (int)(sizeX/2);
        collision_distanceY = (int)(sizeY/2);
    }
    
    public EnemyShot generateShot() {
        //Shot shot = new Shot(x_pos, y_pos); //현재 player의 위치(x,y)에서 공격 시작
        //기본공격 생성 x,y에 기본데미지
        slope = (target.getY()-y_pos)/(target.getX()-x_pos);
        EnemyShot shot = new EnemyShot((int)x_pos, (int)y_pos, damage, slope);
        shot.setUnitType(unitType);
        shot.setImage(shotImage);
        shot.setSizeXY(30);
        return shot;
    }
    
    //이동
    public void move()
    {
        x_pos += delta_x;
        y_pos += delta_y;
        
        if (x_pos < 800) {
            delta_x *= -1;
            x_pos += 1;
        } 
        if (x_pos > 1000) {
            delta_x *= -1;
            x_pos -= 1;
        }
        
        if (y_pos > max_y-300) {
            delta_y = -delta_y;
            y_pos -= 1;
        }
        else if (y_pos < 0+300) {
            delta_y = -delta_y;
            y_pos += 1;
        }
    }
}

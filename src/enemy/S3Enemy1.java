/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enemy;

import javax.swing.ImageIcon;
import shootingspaceship.EnemyShot;
import shootingspaceship.Player;
import shootingspaceship.Shot;
import static shootingspaceship.UnitType.STAGE3ENEMY1;

/**
 *
 * @author 윤재민
 */
public class S3Enemy1 extends Enemy{
    Player target;
    double slope;
    double x;
    
    public S3Enemy1(int x, int y, float delta_x, float delta_y, int max_x, int max_y, float delta_x_inc, Player target) {
        super(x,y,delta_x,delta_y,max_x,max_y,delta_x_inc);
        //delta_x *= 1.5;
        hp = 30;
        originalHp = hp;
        damage = 1;
        speed = 1.6f;
        attackSpeed = 1;
        collisionDamage = 4;
        point = 50;
        this.target = target;
        unitType = STAGE3ENEMY1;
        icon =  new ImageIcon(".\\src\\image\\enemyImage\\Stage3\\enemy1.png");
        image = icon.getImage();
        shotIcon = new ImageIcon(".\\src\\image\\enemyImage\\Stage3\\enemy1Shot.png");
        shotImage = shotIcon.getImage();
        sizeX = 80;
        sizeY = 80;
        collision_distanceX = (int)(sizeX/2);
        collision_distanceY = (int)(sizeY/2);
    }
    @Override
    public void move() {
        x_pos += delta_x-(speed/2);
        y_pos += delta_y*(speed);

        if (x_pos < 0) {
            x_pos = 1200;
        } 
        
        if (y_pos > max_y-10) {
            y_pos = max_y-15;
            y_pos -= 3;
            delta_y = -delta_y;
        }
        else if (y_pos < 10) {
            y_pos = 15;
            y_pos +=3;
            delta_y = -delta_y;
        }
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
}

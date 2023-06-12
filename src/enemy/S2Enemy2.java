/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enemy;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import shootingspaceship.Player;
import shootingspaceship.UnitType;        
/**
 *
 * @author 윤재민
 */
public class S2Enemy2 extends Enemy{
    Player target;
    double slope;
    double x;
    
    public S2Enemy2(int x, int y, float delta_x, float delta_y, int max_x, int max_y, float enemySpeed, Player target) {
        super(x,y,delta_x,delta_y,max_x, max_y,enemySpeed);
        //delta_x *= 1.5;
        hp = 13;
        originalHp = hp;
        damage = 1;
        speed = 4;
        attackSpeed = 1;
        collisionDamage = 3;
        point = 35;
        this.target = target;
        unitType = STAGE2ENEMY2;
        icon =  new ImageIcon(".\\src\\image\\enemyImage\\Stage2\\enemy2.png");
        image = icon.getImage();
        sizeX = 60;
        sizeY = 60;
        collision_distanceX = (int)(sizeX/2);
        collision_distanceY = (int)(sizeY/2);
    }
    @Override
    public void move() {
        //System.out.println("E1.move");
        //speed 거리 만큼 움직임
        slope = (y_pos  - target.getY()) / (x_pos-target.getX());    //기울기
        x = Math.sqrt(speed*speed/(1+slope*slope));     //x증가량
        if(x_pos < target.getX())       // enemy---타겟(플레이어)
        {
            x_pos += x;
            y_pos += x * slope;         //y증가량
        }
        else
        {   
            x_pos-= x;                  // 타겟(플레이어)---enemy
            y_pos -= x * slope;
        }
    }    
    
    
    

    public void draw(Graphics g) {
        g.setColor(Color.green);
        int[] x_poly = {(int) x_pos-15  , (int) x_pos   , (int) x_pos-5 , (int) x_pos };
        int[] y_poly = {(int) y_pos     , (int) y_pos-10, (int) y_pos    , (int) y_pos+10};
        g.fillPolygon(x_poly, y_poly, 4); 
    }

}

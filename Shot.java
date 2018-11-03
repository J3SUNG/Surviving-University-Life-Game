/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship;

import java.awt.Graphics;
import java.awt.Color;

/**
 *
 * @author wgpak
 */
public class Shot {

    private int x_pos;      //shot x좌표
    private int y_pos;      //shot y좌표
    private boolean alive;  //shot 생존 여부
    private final int radius = 7;   //반지름
    
    protected double damage;    //공격 데미지

    //생성자1
    public Shot(int x, int y) {
        x_pos = x;
        y_pos = y;
        alive = true;
        damage = 1;    //임시 기본 공격 값 1
    }
    
    public Shot(int x, int y, double damage)
    {
        this(x,y);
        this.damage = damage;
        
    }
    
    //shot y좌표
    public int getY() {
        return y_pos;
    }

    //shot x좌표
    public int getX() {
        return x_pos;
    }

    //shot 데미지
    public double getDamage(){
        return damage;
    }
    
    
    //shot 이동(입력 값)
    public void moveShot(int speed) {
        x_pos += speed;
    }

    //shot 그리기
    public void drawShot(Graphics g) {
        if (!alive) {
            return;
        }
        g.setColor(Color.yellow);
        g.fillOval(x_pos, y_pos, radius, radius);
    }

    //shot 충돌
    public void collided() {
        alive = false;  //생존 x false
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Seo
 */
public class Enemy_1 extends Enemy{
    private Player target;
    private float slope;
    private int speed;
    
    public Enemy_1(int x, int y, float delta_x, float delta_y, int max_x, int max_y, float delta_x_inc) {
        super(x,y,delta_x,delta_y,max_x,max_y,delta_x_inc);
        target = null;
        //delta_x *= 1.5;
        hp = 1;
        damage = 1;
        slope = 0;
        speed = 3;
    }
    public boolean move(Player target) {
        slope = (y_pos  - target.getY()) / (x_pos-target.getX());
        if(x_pos > target.getX()){
            x_pos -= speed;
            y_pos -= speed * slope; 
        }
        else{
            x_pos += speed;
            x_pos += speed * slope;
        }
        
        if (x_pos < 0) {
            return false;
        } 

        if (y_pos > max_y) {
            y_pos = max_y;
            delta_y = -delta_y;
        }
        else if (y_pos < 0) {
            y_pos = 0;
            delta_y = -delta_y;
        }
        return true;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.red);
        
        int[] x_poly = {(int) x_pos-15  , (int) x_pos   , (int) x_pos-5 , (int) x_pos };
        int[] y_poly = {(int) y_pos     , (int) y_pos-10, (int) y_pos    , (int) y_pos+10};
        g.fillPolygon(x_poly, y_poly, 4); 
        
    }
}

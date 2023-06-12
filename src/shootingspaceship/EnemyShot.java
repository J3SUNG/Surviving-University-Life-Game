/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship;

import enemy.Enemy;
import shootingspaceship.Player;
import shootingspaceship.Shot;

/**
 *
 * @author Seo
 */
public class EnemyShot extends Shot {
    private boolean checkBoss = false;
    private Player target;
    private float c=0;
    double dx;
    double dy;
    double randomValue = (Math.random()-0.5)*10;
    double speedShot = 1;
    
    public EnemyShot(int x, int y) {
        super(x, y);
    }

    public EnemyShot(int x, int y, double damage) {
        super(x, y);
        this.damage = damage;
    }

    public EnemyShot(int x, int y, double damage, double slope) {
        super(x, y, damage, slope);
        this.slope = slope;
    }

    public EnemyShot(int x, int y, double damage, double slope, boolean checkBoss, int i, int j) {
        super(x, y, damage, slope, i, j);
        this.checkBoss = checkBoss;
    }

    public EnemyShot(int x, int y, double damage, Player target) {
        this(x,y,damage);
        this.target = target;
    }
    @Override
    public void moveShot(int speed){
        double x = Math.sqrt(Math.pow(speed,2)/(1+Math.pow(slope, 2)));  
        x_pos -= x;
        y_pos -= x * slope;
    }
    
    public boolean getCheckBoss()
    {
        return checkBoss;
    }
    public void moveShot(int speed, boolean isBoss){
        double shotGunSlope = -1.0+(random*0.1);
        double shotGun = Math.sqrt(Math.pow(speed,2)/(1+Math.pow(shotGunSlope, 2)));
        double curveShot = Math.sin(x_pos/100)*2;
        dx= (float)(4*(Math.cos(0.4*(i+c))));
        dy= (float)(4*(Math.sin(0.4*(i+c))));
        
        if(j==-1){
            if(x_pos<600){      //600까지 총알 같이 이동
                    x_pos -= shotGun + random2;
                    y_pos -= shotGun * shotGunSlope;    // 랜덤 흩뿌리기
            }
            else{
                x_pos -= 5;
                y_pos -= 0;
            }
        }
        else if(j==-2){
            x_pos -= 5;
            y_pos -= curveShot; // 커브샷 */ 
        }
        else if(j==-3){
            if(x_pos < 500){
                x_pos -= 3;
                y_pos -= randomValue/3;
            }
            else{
                x_pos -= 12;
                y_pos -= randomValue;
            }
        }
        else if(j==-4){
            x_pos -= speedShot;
            y_pos -= (randomValue/2)*2;
            //System.out.println("enemyShot.java 스피드 샷 : "speedShot);
            speedShot+=0.08f;
        }
        else if(j==-5){
            x_pos -= 10;
            y_pos -= i-2;
        }
        else{
            x_pos -= dx;
            y_pos -= dy;
            if(j==1){
                if( c == 0){
                    c = 1f;
                }
                else{
                    c = 0;
                }
            }
        }
    }
}

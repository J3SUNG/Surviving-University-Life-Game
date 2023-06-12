/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import shootingspaceship.Shootingspaceship;
import enemy.Enemy;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author wgpak
 */
public class Shot {

    protected double x_pos;      //shot x좌표
    protected double y_pos;      //shot y좌표
    protected boolean alive;  //shot 생존 여부
    protected final int radius = 7;   //반지름
    
    protected double slope = 0;       //기울기(공격 대각선 이동, 타겟팅 공격)
    protected double previousSlope = 0;//공격 대상이 죽었을 경우 기울기 초기화 해줄 때 사용
    protected int moveCount = 0;      //move count 타겟팅 공격중 공격대상이 죽었을 경우
    protected double damage;        //공격 데미지
    private Enemy target;           //유도탄 공격 대상 : 죽었을 경우 오른쪽으로 이동 지나치기
    protected boolean ultimateShot = false; //궁극기 샷인지 아닌지
    protected double speed = 0;
    
    private int curveNum=0;
    
    private int unitType;
    ImageIcon icon;
    Image image;
    protected int sizeX;
    protected int sizeY;
    double random = (Math.random()*100)%20;
    double random2 = (Math.random());
    int i;
    int j;
    int curveCount = 0;
    boolean curve = false;
    
    //생성자1
    public Shot(int x, int y) {
        x_pos = x;
        y_pos = y;
        alive = true;
        damage = 1;    //임시 기본 공격 값 1
        slope = 0;      //기울기 기본 값 0
        target = null;
    }
    //생성자+데미지
    public Shot(int x, int y, double damage)
    {
        this(x,y);
        this.damage = damage;
    }
    public Shot(int x, int y, double damage, int i, boolean h)
    {
        this(x,y);
        this.damage = damage;
        curveCount = i;
        curve = true;               //커브샷 서브공격
    }
    //생성자 + 기울기만 있을 경우   일반 대각선 공격
    public Shot(int x, int y, double damage, double slope)
    {
        this(x,y,damage);
        this.slope = slope;
    }
    
    public Shot(int x, int y, double damage, double slope, int i, int j)
    {
        this(x,y,damage);
        this.slope = slope;
        this.i = i;
        this.j = j;
    }
    //생성자 + 공격대상(target)
    public Shot(int x, int y, double damage, Enemy target)
    {
        this(x,y,damage);
        this.target = target;       //타겟 설정
        slope = (y - target.getY()) / (x-target.getX());    //기울기 설정    
    }
    
    public boolean getUltimateShot()
    {
        return ultimateShot;
    }
    public void setUltimateShot(boolean ultimate)
    {
        ultimateShot = ultimate;
    }
    //shot y좌표
    public double getY() {
        return y_pos;
    }

    //shot x좌표
    public double getX() {
        return x_pos;
    }

    //위치 조정
    public void moveX(double distance)
    {
        x_pos+=distance;
    }
    public void moveY(double distance)
    {
        y_pos+=distance;
    }
    
    //shot 데미지
    public double getDamage(){
        return damage;
    }
    
    public void setDamage(double damage){
        this.damage = damage;
    }
    public void setSpeed(double speed)
    {
        this.speed = speed;
    }
    public double getSpeed()
    {
        return speed;
    }
    public int getSizeX()
    {
        return sizeX;
    }
    public int getSizeY()
    {
        return sizeY;
    }
    public void setSizeX(int sizeX)
    {
        this.sizeX = sizeX;
    }
    public void setSizeY(int sizeY)
    {
        this.sizeY = sizeY;
    }
    public void setSizeXY(int size)
    {
        sizeX = size;
        sizeY = size;
    }
    public void setSizeXY(int sizeX, int sizeY)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
    
    //샷 이미지 중심 x좌표
    public int getMiddleX()
    {
        return (int)(x_pos-(sizeX/2));
    }
    //샷 이미지의 중심 y좌표
    public int getMiddleY()
    {
        return (int)(y_pos-(sizeY/2));
    }
    
    public Image getImage()
    {
        return image;
    }
    public void setImage(Image image)
    {
        this.image = image;
    }
    public ImageIcon getImageIcon()
    {
        return icon;
    }
    public void setImageIcon(ImageIcon icon)
    {
        this.icon = icon;
    }


    public int getUnitType()
    {
        return unitType;
    }
    public void setUnitType(int unitType)
    {
        this.unitType = unitType;
    }
 
    public void moveShot()      //주어진 speed 가 있을 경우
    {
        if(speed > 0){
            double x = Math.sqrt(Math.pow(speed,2)/(1+Math.pow(slope, 2)));     //x 는 계산하면서 제곱이 되기 때문에 무조건 양수
            if(target == null ) //타겟이 없을 때
            {
                if(speed<0)
                {
                    x_pos -= x;
                    y_pos -= x * slope;
                }
                else
                {   
                    x_pos += x;
                    y_pos += x * slope;
                }
            }
            else    //타겟이 있을 때
            {
                //double x = Math.sqrt(Math.pow(speed,2)/(1+Math.pow(slope, 2)));
                //x_pos+= x;
                //y_pos+= x*slope;
                if(x_pos < target.getX())       // 총알  -- 적
                {
                    x_pos += x;
                    y_pos += x * slope;
                }
                else
                {   
                    x_pos-= x;              // 적 -- 총알
                    y_pos -= x * slope;
                }
                //문제 발생  - 타겟이 죽었을 경우 죽은 근처에서 가만히 뺑뺑 돔 : 해결 moveConut, previousSlope 이용
                if(moveCount%5 == 0)        //실행 속도 때문에 
                {
                    previousSlope = slope;  //이전의 슬로프는 몇번의 반복때 값 설정
                }
                slope = (y_pos  - target.getY()) / (x_pos-target.getX());   //기울기 변경

                moveCount++;
                //System.out.print("slope = "+slope+"move : "+moveCount++);
                //System.out.println("    slope 차이 = "+(slope - previousSlope));
                if( moveCount > 10 &&(slope - previousSlope)==0.0)
                {
                    target = null;
                    slope = 0;
                }
            }
            return;
        }
    }
    //shot 이동(입력 값)
    public void moveShot(int speed) {
        double x = Math.sqrt(Math.pow(speed,2)/(1+Math.pow(slope, 2)));     //x 는 계산하면서 제곱이 되기 때문에 무조건 양수
        double curveShot = Math.sin(x/20);
        if(curveCount == 1){
            curveShot *= -1;
        }
        
        if(target == null ) //타겟이 없을 때
        {
            if(speed<0)
            {
                x_pos -= x;
                y_pos -= x * slope;
            }
            else
            {   
                if(curve == true){
                    x_pos += x;
                    y_pos += curveShot;
                }
                else{
                    x_pos += x;
                    y_pos += x * slope;
                }
            }
        }
        else    //타겟이 있을 때
        {
            //double x = Math.sqrt(Math.pow(speed,2)/(1+Math.pow(slope, 2)));
            //x_pos+= x;
            //y_pos+= x*slope;
            if(x_pos < target.getX())       // 총알  -- 적
            {
                x_pos += x;
                y_pos += x * slope;
            }
            else
            {   
                x_pos-= x;              // 적 -- 총알
                y_pos -= x * slope;
            }
            //문제 발생  - 타겟이 죽었을 경우 죽은 근처에서 가만히 뺑뺑 돔 : 해결 moveConut, previousSlope 이용
            if(moveCount%5 == 0)        //실행 속도 때문에 
            {
                previousSlope = slope;  //이전의 슬로프는 몇번의 반복때 값 설정
            }
            slope = (y_pos  - target.getY()) / (x_pos-target.getX());   //기울기 변경
            
            moveCount++;
            //System.out.print("slope = "+slope+"move : "+moveCount++);
            //System.out.println("    slope 차이 = "+(slope - previousSlope));
            if( moveCount > 10 &&(slope - previousSlope)==0.0)
            {
                target = null;
                slope = 0;
            }
        }
    }

    //shot 그리기
    public void drawShot(Graphics g) {

        g.setColor(Color.yellow);
        g.fillOval((int)x_pos, (int)y_pos, radius, radius);
    }

    //shot 충돌
    public void collided() {
        alive = false;  //생존 x false
    }

}

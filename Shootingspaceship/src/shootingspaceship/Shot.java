/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author wgpak
 */
public class Shot {

    private double x_pos;      //shot x좌표
    private double y_pos;      //shot y좌표
    private boolean alive;  //shot 생존 여부
    private final int radius = 7;   //반지름
    
    private double slope = 0;       //기울기(공격 대각선 이동, 타겟팅 공격)
    private double previousSlope = 0;//공격 대상이 죽었을 경우 기울기 초기화 해줄 때 사용
    private int moveCount = 0;      //move count 타겟팅 공격중 공격대상이 죽었을 경우
    protected double damage;        //공격 데미지
    private Enemy target;           //유도탄 공격 대상 : 죽었을 경우 오른쪽으로 이동 지나치기

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
    //생성자 + 기울기만 있을 경우   일반 대각선 공격
    public Shot(int x, int y, double damage, double slope)
    {
        this(x,y,damage);
        this.slope = slope;
    }
    //생성자 + 공격대상(target)
    public Shot(int x, int y, double damage, Enemy target)
    {
        this(x,y,damage);
        this.target = target;       //타겟 설정
        slope = (y - target.getY()) / (x-target.getX());    //기울기 설정
        
}
    
    //shot y좌표
    public double getY() {
        return y_pos;
    }

    //shot x좌표
    public double getX() {
        return x_pos;
    }

    //shot 데미지
    public double getDamage(){
        return damage;
    }

    

    //shot 이동(입력 값)
    public void moveShot(int speed) {
        if(target == null ) //타겟이 없을 때
        {
            x_pos += speed;
            y_pos += speed * slope;
        }
        else    //타겟이 있을 때
        {
            if(x_pos < target.getX())       // 총알  -- 적
            {
                x_pos += speed;
                y_pos += speed * slope;
            }
            else
            {   
                x_pos-= speed;              // 적 -- 총알
                y_pos -= speed * slope;
            }
            //문제 발생  - 타겟이 죽었을 경우 죽은 근처에서 가만히 뺑뺑 돔 : 해결 moveConut, previousSlope 이용
            if(moveCount%5 == 0)        //실행 속도 때문에 
            {
                previousSlope = slope;  //이전의 슬로프는 몇번의 반복때 값 설정
            }
            slope = (y_pos  - target.getY()) / (x_pos-target.getX());   //기울기 변경
            
            System.out.print("slope = "+slope+"move : "+moveCount++);
            System.out.println("slope 차이 = "+(slope - previousSlope));
            if( moveCount > 10 &&(slope - previousSlope)==0.0)
            {
                target = null;
                slope = 0;
            }
        }
    }

    //shot 그리기
    public void drawShot(Graphics g) {
        if (!alive) {
            return;
        }
        g.setColor(Color.yellow);
        g.fillOval((int)x_pos, (int)y_pos, radius, radius);
    }

    //shot 충돌
    public void collided() {
        alive = false;  //생존 x false
    }
}

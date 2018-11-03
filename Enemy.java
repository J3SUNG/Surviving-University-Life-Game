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
public class Enemy {
    //기존 소스
    private float x_pos;        //enemy의 x좌표
    private float y_pos;        //enemy의 y좌표
    private float delta_x;      //x좌표 이동 속도
    private float delta_y;      //y좌표 이동 속도
    private int max_x;          //enemy 우측 범위 최대값
    private int max_y;          //enemy 아래 범위 최대값
    private float delta_y_inc;  //델타 y 증가값(y 이동속도 증가)
    private final int collision_distance = 10;  //충돌 거리( 이 크기 만큼 +- 범위 안에 닿으면 충돌했다고 본다 )

    //추가 작성
    protected double hp;
    
//생성자
    public Enemy(int x, int y, float delta_x, float delta_y, int max_x, int max_y, float delta_y_inc) {
        x_pos = x;
        y_pos = y;
        this.delta_x = delta_x;
        this.delta_y = delta_y;
        this.max_x = max_x;
        this.max_y = max_y;
        this.delta_y_inc = delta_y_inc;
        hp = 5;         //임시 hp 5 현재 player 무기 데미지1
    }

    //enemy 이동 메소드
    public void move() {
        x_pos += delta_x;
        y_pos += delta_y;

        if (x_pos < 0) {
            x_pos = 0;
            delta_x = -delta_x;
        } else if (x_pos > max_x) {
            x_pos = max_x;
            delta_x = -delta_x;
        }
        if (y_pos > max_y) {
            y_pos = 0;
            delta_y += delta_y_inc;
        }
    }
    
    //hp 감소(value = 피해 데미지)
    public void hpDecrease(double value)
    {
        hp -= value;
    }
    public double getHp(){
        return hp;
    }

    //반환형 bool , 플레이어 공격과 충돌 하였는지 확인( 매개변수 shots 는 player의 shots 배열)
    public boolean isCollidedWithShot(Shot[] shots) {
        /*
        기존 소스내용
        for (Shot shot : shots) {
            if (shot == null) {
                continue;
            }
            if (-collision_distance <= (y_pos - shot.getY()) && (y_pos - shot.getY() <= collision_distance)) {
                if (-collision_distance <= (x_pos - shot.getX()) && (x_pos - shot.getX() <= collision_distance)) {
                    //collided.
                    shot.collided();
                    hp -= shot.getDamage();//hp 감소
                    shot=null;                  
                    return true;    //enemy가 shot과 충돌함
                }
            }
        }*/
        //기본 for문 형태로 변경        
        //이유 : Shot 클래스의 객체가 가르키는 곳을 미리 null로 해주지 않으면
        //잠깐 동안 프로그램이 여러번 반복동작 하여
        //hp가 중복해서 깍임
        for(int i=0; i<shots.length; i++)
        {
            if(shots[i] == null){
                continue;
            }
           if (-collision_distance <= (y_pos - shots[i].getY()) && (y_pos - shots[i].getY() <= collision_distance)) {
                if (-collision_distance <= (x_pos - shots[i].getX()) && (x_pos - shots[i].getX() <= collision_distance)) {
                    //collided.
                    shots[i].collided();
                    
                    hp-=shots[i].getDamage();//hp 감소
                    shots[i] = null;        //해주지 않으면 살아있는 동안 hp 감소 여러번 반복
                    return true;    //enemy가 shot과 충돌함
                }
            }
        
        }
        return false;   //enemy가 shot과 충돌하지 않음
    }

    //반환형 bool , 플레이어와 충돌하였는지 확인
    public boolean isCollidedWithPlayer(Player player) {
        if (-collision_distance <= (y_pos - player.getY()) && (y_pos - player.getY() <= collision_distance)) {
            if (-collision_distance <= (x_pos - player.getX()) && (x_pos - player.getX() <= collision_distance)) {
                //collided.
                return true;    //플레이어와 충돌함
            }
        }
        return false;       //플레이어와 충돌하지 않음
    }

    //enemy 그리기
    public void draw(Graphics g) {
        g.setColor(Color.yellow);
        int[] x_poly = {(int) x_pos-15  , (int) x_pos   , (int) x_pos-5 , (int) x_pos };
        int[] y_poly = {(int) y_pos     , (int) y_pos-10, (int) y_pos    , (int) y_pos+10};
        g.fillPolygon(x_poly, y_poly, 4); 
    }
}

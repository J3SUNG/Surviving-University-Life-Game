/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package shootingspaceship;
package enemy;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import shootingspaceship.EnemyShot;
import shootingspaceship.Shot;
import shootingspaceship.Player;
import shootingspaceship.UnitType;
/**
 *
 * @author wgpak
 */
public class Enemy implements UnitType{
    //기존 소스
    protected float x_pos;        //enemy의 x좌표
    protected float y_pos;        //enemy의 y좌표
    protected float max_x;
    protected float max_y;
    protected float delta_x;      //x좌표 이동 속도
    protected float delta_y;      //y좌표 이동 속도
    protected float delta_y_inc;  //델타 y 증가값(y 이동속도 증가)
    protected int collision_distanceX = 10;  //충돌 거리( 이 크기 만큼 +- 범위 안에 닿으면 충돌했다고 본다 )
    protected int collision_distanceY = 10;  //충돌 거리( 이 크기 만큼 +- 범위 안에 닿으면 충돌했다고 본다 )
    
    //추가 작성
    protected double hp;                    //현재 체력
    protected double originalHp;            //원래 체력
    protected double damage;              //기본 공격 데미지
    protected double collisionDamage;     //충돌 데미지
    protected double attackSpeed; //공격속도(초당 공격 횟수) ex) 2 = 1초에 2번
    protected int unitType = -1;
    protected double speed;
    protected boolean checkBoss = false;        //보스 인지 아닌지
    protected boolean checkElite = false;       //엘리트 인지 아닌지
    protected boolean ultimateHpDecrease = false;       //플레이어 궁극기에 데미지를 입었는지  false : 안입음, true 공격 받음
    protected int point = 0;    //유닛 포인트, 잡은 유닛 별 점수
    
    //이미지 관련
    ImageIcon icon;     //enemy 아이콘
    Image image;        //enemy 이미지
    ImageIcon shotIcon; //enemyShot 아이콘
    Image shotImage;    //enemyShot 이미지
    protected int sizeX;//이미지 가로길이
    protected int sizeY;//이미지 세로길이
    
    //이미지 관련//
    //==================
    //기본 생성자    들어오는 인자값이 없으면 
    public Enemy()
    {
        x_pos = 1200;     //제일 오른쪽
        y_pos=(float)Math.random()*900;  //y좌표 높이 랜덤
        hp = 1;
        damage = 1;
        collisionDamage = 1;
        attackSpeed = 0.5;
        max_x = 1200;
        max_y = 900;
    }
    //생성자
    public Enemy(int x, int y, float delta_x, float delta_y,int max_x, int max_y, float delta_y_inc) {
        x_pos = x;
        y_pos = y;
        this.delta_x = delta_x;
        this.delta_y = delta_y;
        this.delta_y_inc = delta_y_inc;
        this.max_x = max_x;
        this.max_y = max_y;
        hp = 2;         //임시 hp 5 현재 player 무기 데미지1
        damage = 1;     //임시 데미지 1
        collisionDamage = 3*damage;     //임시 : 충돌 데미지 데미지 * 3
        attackSpeed = 1;
        checkBoss = false;
        checkElite = false;
    }
    
    //enemy 이동 메소드
    public void move(){}   //움직임이 없는 유닛의 경우 오버라이딩 하여 빈칸으로 생성
    //hp 감소(value = 피해 데미지)
    public void hpDecrease(double value)
    {
        hp -= value;
    }
    
    public int getPoint()
    {
        System.out.println("포인트 획득 : "+point);
        return point;
    }
    public int getUnitType()
    {
        return unitType;
    }
    public double getHp(){
        return hp;
    }
    public double getOriginalHp()
    {
        return originalHp;
    }
    public double getDamage()
    {
        return damage;
    }
    public double getAttackSpeed()
    {
        return attackSpeed;
    }
    public boolean getUltimateHpDecrease()
    {
        return ultimateHpDecrease;
    }
    public void setUltimateHpDecrease(boolean decrease)
    {
        ultimateHpDecrease = decrease;
    }
    
    //이미지 가로 사이즈
    public int getSizeX()
    {
        return sizeX;
    }
    //이미지 세로 사이즈
    public int getSizeY()
    {
        return sizeY;
    }
    //이미지의 중심 x좌표
    public int getMiddleX()
    {
        return (int)(x_pos-(sizeX/2));
    }
    //이미지의 중심 y좌표
    public int getMiddleY()
    {
        return (int)(y_pos-(sizeY/2));
    }
    public double getCollisionDamage()
    {
        return collisionDamage;
    }
    public ImageIcon getImageIcon()
    {
        return icon;
    }
    
    public Image getImage()
    {
        return image;   
    }    
    public double getX()
    {
        return x_pos;
    }
    
    public double getY()
    {
        return y_pos;
    }
    
    public boolean getCheckBoss()
    {
        return checkBoss;
    }
    public boolean getCheckElite()
    {
        return checkElite;
    }
    
    
    
    public EnemyShot generateShot() {
        //Shot shot = new Shot(x_pos, y_pos); //현재 player의 위치(x,y)에서 공격 시작
        //기본공격 생성 x,y에 기본데미지
        EnemyShot shot = new EnemyShot((int)x_pos, (int)y_pos, damage);
        return shot;
    }
    
    public EnemyShot generateShot(int i, int j) {
        //Shot shot = new Shot(x_pos, y_pos); //현재 player의 위치(x,y)에서 공격 시작
        //기본공격 생성 x,y에 기본데미지
        EnemyShot shot = new EnemyShot((int)x_pos, (int)y_pos, damage);
        return shot;
    }
    
    //반환형 bool , 플레이어 공격과 충돌 하였는지 확인( 매개변수 shots 는 player의 shots 배열)
    public boolean isCollidedWithShot(Shot[] shots) {
        for(int i=0; i<shots.length; i++)
        {
            if(shots[i] == null){
                continue;
            }
            
            if (-collision_distanceY <= (y_pos - shots[i].getY()) && (y_pos - shots[i].getY() <= collision_distanceY)) {
                if (-collision_distanceX <= (x_pos - shots[i].getX()) && (x_pos - shots[i].getX() <= collision_distanceX)) {
                    //collided.
                    shots[i].collided();
                    if(!shots[i].getUltimateShot())     //얼티메이트 샷이 아닐때, 궁극기가 아닐때 만 처리 -------- 궁극기는 따로 처리함
                    {
                        hp-=shots[i].getDamage();//hp 감소
                        shots[i] = null;        //해주지 않으면 살아있는 동안 hp 감소 여러번 반복
                    }
                    return true;    //enemy가 shot과 충돌함
                }
            }
        }
        return false;   //enemy가 shot과 충돌하지 않음
    }

    //반환형 bool , 플레이어와 충돌하였는지 확인
    public boolean isCollidedWithPlayer(Player player) {
        if (-collision_distanceY <= (y_pos - player.getY()) && (y_pos - player.getY() <= collision_distanceY)) {
            if (-collision_distanceX <= (x_pos - player.getX()) && (x_pos - player.getX() <= collision_distanceX)) {
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

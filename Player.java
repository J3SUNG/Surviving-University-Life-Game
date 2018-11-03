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
import java.awt.Graphics;
import java.awt.Color;
import shootingspaceship.Shot;

public class Player {
    //기존 코드
    private int x_pos;          //player x좌표
    private int y_pos;          //player y좌표
    private int min_x;          //좌측 범위 최대값
    private int max_x;          //우측 범위 최대값
    private int min_y;          //위 범위 최대값
    private int max_y;          //아래 범위 최대값z
    
    //추가 작성 코드
    private double attackSpeed; //공격속도(초당 공격 횟수) ex) 2 = 1초에 2번
    private double damage=2;    //플레이어 기본공격 데미지  변경방법 = 무기교체
    private double hp=5;        //체력
    private int level =1;       //레벨
    
    protected double skill[] = new double[4];    //qwer 이나 asdf 스킬 사용 할
    
    
    
    //Player 생성자
    public Player(int x, int y, int min_x, int max_x, int min_y, int max_y) {
        x_pos = x;
        y_pos = y;
        this.min_x = min_x;
        this.max_x = max_x;
        this.min_y = min_y;
        this.max_y = max_y;
        attackSpeed = 5; //공격속도
    }
    
    //Player 생성자 x,y 생성좌표,   min max xy 이동 최대 범위,  hp 체력, damage 데미지
    public Player(int x, int y, int min_x, int max_x, int min_y, int max_y, double hp, double damage)
    {
        this(x,y,min_x,max_x,min_y,max_y);
        this.hp = hp;
        this.damage = damage;
    }
    
    
    public Player(int x, int y, int min_x, int max_x, int min_y, int max_y, double hp, Weapon weapon)
    {
        this(x,y,min_x,max_x,min_y,max_y);
        this.hp = hp;
        this.damage = weapon.getDamage();
        level = 1;
    }

    //좌우 이동 메소드
    public void moveX(int speed) {
        x_pos += speed;
        if( x_pos < min_x) x_pos = min_x;
        if( x_pos > max_x) x_pos = max_x;
    }
    
    //상하 이동 메소드
    public void moveY(int speed){
        y_pos += speed;
        if(y_pos <min_y) y_pos = min_y;
        if(y_pos >max_y) y_pos = max_y;
    }
    //x 좌표 리턴
    public int getX() {
        return x_pos;
    }
    //y좌표 리턴
    public int getY() {
        return y_pos;
    }
    
    //공격속도
    public double getAttackSpeed()
    {
        return attackSpeed;
    }
    
    //플레이어 체력 리턴
    public double getHp(){
        return hp;
    }
    
    //플레이어 데미지 리턴
    public double getDamage(){
        return damage;
    }
    
    public int getLevel(){
        return level;
    }
    
    //기본무기 변경
    public void weaponChange(Weapon weapon){
        attackSpeed = weapon.getAttackSpeed();  //초당공격속도 = 무기 초당공격속도
        damage = weapon.getDamage();            //데미지 = 무기 데미지
    }
    
    //플레이어 레벨업
    public void levelUp()
    {
        hp*=1.3;    //임시 : 레벨업 할 때 마다 hp = 1.3배
        ++level;    //레벨업
        
    }
    
    //hp 감소(value = 피해 데미지)
    public void hpDecrease(double value){
        hp-=value;
    }

    //공격 생성, 반환형 Shot 객체
    public Shot generateShot() {
        //Shot shot = new Shot(x_pos, y_pos); //현재 player의 위치(x,y)에서 공격 시작
        Shot shot = new Shot(x_pos, y_pos, damage); //기본공격 생성 x,y에 기본데미지
        return shot;
    }
 

    //플레이어 그리기 현재 > 모양으로 생겼음
    public void drawPlayer(Graphics g) {
        g.setColor(Color.red);
        int[] x_poly = {x_pos, x_pos - 15, x_pos-10, x_pos - 15};
        int[] y_poly = {y_pos, y_pos - 10, y_pos   , y_pos + 10};
        g.fillPolygon(x_poly, y_poly, 4);
    }
}

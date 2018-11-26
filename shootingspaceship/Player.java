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
    private double damage=1;    //플레이어 기본공격 데미지  변경방법 = 무기교체
    private double hp=5;        //체력
    private int level =1;       //레벨
    private int attackType = 1; //미구현 : 공격 타입 1 = 일반 공격 한발씩, 공격 타입 2 = 두발 공격
    
    private boolean barrier;
    private double barrierHp;
    public double skill[] = new double[4];    //qwer 이나 asdf 스킬 사용 할 예정
    int collision_distance = 10;
    
    
    //Player 생성자
    public Player(int x, int y, int min_x, int max_x, int min_y, int max_y) {
        x_pos = x;
        y_pos = y;
        this.min_x = min_x;
        this.max_x = max_x;
        this.min_y = min_y;
        this.max_y = max_y;
        attackSpeed = 5;    //공격속도
        barrier = false;    //배리어 여부 x
        barrierHp = 5;      //기본 배리어 체력 0
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
    public void changeWeapon(Weapon weapon){
        //무기 교체
        System.out.println("무기 교체");
        System.out.printf("무기 : %s,\t공속 : %.2f, 데미지 : %.2f\n",weapon.getName(),weapon.getAttackSpeed(),weapon.getDamage());
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
    public void decreaseHp(double value){
        if(barrier && barrierHp>0)//배리어 on 이면서 배리어 체력이 0 이상일 때
        {
            barrierHp-=value;
            System.out.print("현재 barrierHp : "+barrierHp);
            if(barrierHp<0) //만약 배리어 체력이 0보다 작으면 그 값 만큼 hp 에 대미지
                hp+=barrierHp;  
        }
        else
            hp-=value;
        System.out.println("  현재 HP : "+hp);
    }
    public void barrierSwitch()
    {
        if(barrier)
            barrier = false;
        else
            barrier = true;
    }

    //공격 생성, 반환형 Shot 객체
    public Shot generateShot() {
        //Shot shot = new Shot(x_pos, y_pos); //현재 player의 위치(x,y)에서 공격 시작
        Shot shot = new Shot(x_pos, y_pos, damage); //기본공격 생성 x,y에 기본데미지
        return shot;
    }
    
    //공격 생성, 기울기 있음 (단순 대각선 공격)
    public Shot generateShot(double slope) {
        //Shot shot = new Shot(x_pos, y_pos); //현재 player의 위치(x,y)에서 공격 시작
        Shot shot = new Shot(x_pos, y_pos, damage, slope); //기본공격 생성 x,y에 기본데미지
        return shot;
    }
    
    //공격 생성, 유도탄(대상)
    public Shot generateShot(Enemy target)
    {
        Shot shot = new Shot(x_pos, y_pos, damage, target);
        return shot;
    }
 
    //매개변수 SkillShot s 미구현     : 임시 구현
    public Shot[] generateSkillShot()
    {
        Shot[] shot = new Shot[4];
        
        shot[0] = new Shot(x_pos, y_pos+15, damage);
        shot[1] = new Shot(x_pos, y_pos-15, damage);
        shot[2] = new Shot(x_pos+10, y_pos+7, damage);
        shot[3] = new Shot(x_pos+10, y_pos-7, damage);
                
         
        return shot;
    }
 
    //반환형 bool , enemy 공격과 충돌 하였는지 확인( 매개변수 shots 는 enemy의 enemyShots 배열)
    public boolean isCollidedWithShot(Shot[] shots) {
        //기존 소스내용에서
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

    //플레이어 그리기 현재 > 모양으로 생겼음
    public void drawPlayer(Graphics g) {
        g.setColor(Color.red);
        int[] x_poly = {x_pos, x_pos - 15, x_pos-10, x_pos - 15};
        int[] y_poly = {y_pos, y_pos - 10, y_pos   , y_pos + 10};
        g.fillPolygon(x_poly, y_poly, 4);
    }
}

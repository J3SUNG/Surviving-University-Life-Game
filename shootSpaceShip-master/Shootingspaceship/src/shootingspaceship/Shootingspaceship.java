package shootingspaceship;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
//https://tmondev.blog.me/220393974518 참고

public class Shootingspaceship extends JPanel implements Runnable {

    private Thread th;
    private Player player;
    private Shot[] shots;                   //player 공격
    private Shot[] enemyShots;              //enemy 공격
    private ArrayList enemies;
    private ArrayList enemies_1;        //enemy_1 생성
    
    private final int shotSpeed = 3;        //공격의 이동 속도
    private final int playerLeftSpeed = -2; //플레이어 이동 속도
    private final int playerRightSpeed = 2;
    private final int playerUpSpeed = -2;
    private final int playerDownSpeed = 2;
    private final int width = 1200;
    private final int height = 900;
    private final int playerMargin = 5;
    
    private final int enemyMaxDownSpeed = 1;
    private final int enemyMaxHorizonSpeed = 1;
    private final int enemyTimeGap = 1000; //unit: msec
    private final float enemyDownSpeedInc = 100.3f;
    private final int maxEnemySize = 100;
    
    private int enemySize;
    private javax.swing.Timer timer;    //enemy 자동 생성을 위한 timer
    private boolean playerMoveLeft; 
    private boolean playerMoveRight;
    private boolean playerMoveUp;  
    private boolean playerMoveDown;
    private Image dbImage;
    private Graphics dbg;
    private Random rand;
    private int maxShotNum = 250;       //공격 최대 개수

    private javax.swing.Timer autoAttackTimer;  //자동 공격 간격 타이머
    
    public Weapon[] weapon = new Weapon[100];   //무기
    public SkillShot[] skill = new SkillShot[100];      //스킬
    
    Timer eTimer = new Timer();
    
    public Shootingspaceship() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(width, height));
        player = new Player(width / 2, (int) (height * 0.9), playerMargin, width-playerMargin, 20, height-20 );
        shots = new Shot[ maxShotNum ];         //player 공격
        enemyShots = new Shot[maxShotNum];      //enemy 공격
        enemies = new ArrayList();
        enemies_1 = new ArrayList();
        
        enemySize = 0;
        rand = new Random(1);
        timer = new javax.swing.Timer(enemyTimeGap, new addANewEnemy());//enemy 생성 타이머
        timer.start();//enemy 생성 타이머 시작
        
        
        autoAttackTimer = new javax.swing.Timer((int)(1000/player.getAttackSpeed()), new autoAttack()); //자동공격 설정
        autoAttackTimer.start();    //자동공격 시작
        
        addKeyListener(new ShipControl());
        setFocusable(true);
    }

    public void start() {
        th = new Thread(this);
        th.start();
    }
    

    private class addANewEnemy implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (enemySize <= maxEnemySize) {
                float downspeed;
                do {
                    downspeed = rand.nextFloat() * enemyMaxDownSpeed;
                } while (downspeed == 0);

                float horspeed = rand.nextFloat()- enemyMaxHorizonSpeed;
                //System.out.println("enemySize=" + enemySize + " downspeed=" + downspeed + " horspeed=" + horspeed);
                int height1 = (int) (rand.nextFloat() * height);     // height인데 왜 width 곱하는지?
                
                Enemy newEnemy;
                Enemy_1 newEnemy_1;     //Enemy_1 초기화
                newEnemy_1 = new Enemy_1(width, height1, horspeed, downspeed, width, height, enemyDownSpeedInc);
                enemies_1.add(newEnemy_1);
                
                for(int i=0; i<5; ++i)
                {
                    newEnemy = new Enemy(width, (int)(height1 % (height-250)+(50*i)), horspeed, downspeed, width, height, enemyDownSpeedInc);
                    enemies.add(newEnemy);
                    ++enemySize;
                }
            } else {
                timer.stop();
            }
        }
    }

    //자동 공격
    private class autoAttack implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < shots.length; i++) {
                if (shots[i] == null) {
                shots[i] = player.generateShot();
                break;
                }
            }
            /*if(enemies.size()>1) //적이 있을 때
            {
                Enemy[] enemy111 = new Enemy[2];
                int c=0;
                enemy111[0] = (Enemy)enemies.get(0);
                enemy111[1] = (Enemy)enemies.get(1);
                for(int i=0; i<enemyShots.length; ++i)
                {
                    if (enemyShots[i] == null) {
                        switch(c++)
                        {
                            case 0:
                                enemyShots[i] = enemy111[0].generateShot();
                                break;
                            case 1:
                                enemyShots[i] = enemy111[1].generateShot();
                                break;
                        }
                        if(c>1)
                            break;
                            //c=0;
                    
                    }
                }
            }*/
        }
    }
    //플레이어 조작키
    private class ShipControl implements KeyListener {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerMoveLeft = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    playerMoveRight = true;
                    break;
                case KeyEvent.VK_UP:
                    playerMoveUp = true;
                    break;
                case KeyEvent.VK_DOWN:
                    playerMoveDown = true;
                    break;
                case KeyEvent.VK_A:
                    //스킬 슬롯1 : A
                    player.changeWeapon(weapon[0]);
                    autoAttackTimer.setDelay((int) (1000/player.getAttackSpeed())); //자동공격을 현재 공속으로 맞춤
                    break;
                case KeyEvent.VK_S:
                    player.changeWeapon(weapon[1]);
                    autoAttackTimer.setDelay((int) (1000/player.getAttackSpeed())); //자동공격을 현재 공속으로 맞춤
                    //스킬 슬롯2 : S
                    break;
                case KeyEvent.VK_D:
                    player.changeWeapon(weapon[2]); 
                    //스킬 슬롯3 : D
                    autoAttackTimer.setDelay((int) (1000/player.getAttackSpeed())); //자동공격을 현재 공속으로 맞춤
                    break;
                case KeyEvent.VK_F:
                    player.changeWeapon(weapon[3]);
                    //스킬 슬롯4 : F
                    autoAttackTimer.setDelay((int) (1000/player.getAttackSpeed())); //자동공격을 현재 공속으로 맞춤
                    break;
                case KeyEvent.VK_Q:     //스킬Q
                    Shot[] tempShot = new Shot[player.generateSkillShot().length];
                    tempShot = player.generateSkillShot();
                    int j=0;
                    for (int i = 0; i < shots.length; i++) {
                        if (shots[i] == null) {
                            shots[i] = tempShot[j++];
                            if(j==tempShot.length)
                                break;
                        }
                    }
                    
                    break;
                case KeyEvent.VK_W:     //스킬W 임시 유도탄
                    //가장 가까운적 찾기
                    Iterator enemyList = enemies.iterator();
                    double distance = 10000;
                    double temp;
                    int number=0;
                    Enemy target = null;
                    if(enemies.size() != 0)
                      target = (Enemy)enemies.get(0);
                    while (enemyList.hasNext()) {
                        Enemy enemy = (Enemy) enemyList.next();
                        enemy.getX();
                        enemy.getY();
                        temp = Math.sqrt(Math.pow(player.getX()- enemy.getX(), 2) + Math.pow(player.getY() - enemy.getY(),2));
                        if(distance > temp) //만약 distance가 temp 값 보다 크다면( 가리키는 enemy 교체)
                        {
                            distance = temp;
                            target = enemy;
                        }
                        ++number;
                    }
                    //가장 가까운 적에게 유도탄 생성
                    for (int i = 0; i < shots.length; i++) {
                        if (shots[i] == null) 
                        {         //공격이 가능 할 때

                            if(target == null)
                                shots[i] = player.generateShot(); //타겟이 없으면 일반 공격
                            else            //가장 가까운 적을 타겟으로 잡기
                                shots[i] = player.generateShot(target);

                            //shots[i] = player.generateShot();
                            //break;
                            break;
                        }
                    }
                    break;
                case KeyEvent.VK_E:
                    int z=0;
                    while(z<7)
                    {
                        for(int i=0; i<shots.length; ++i)
                        {
                            if(shots[i] == null)
                            {
                                shots[i] = player.generateShot(-0.3+(z*0.1));
                                break;
                            }
                        }
                        ++z;
                    }
                    break;
                case KeyEvent.VK_R:
                    player.barrierSwitch();
                    break;
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerMoveLeft = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    playerMoveRight = false;
                    break;
                case KeyEvent.VK_UP:
                    playerMoveUp = false;
                case KeyEvent.VK_DOWN:
                    playerMoveDown = false;
            }
        }

        public void keyTyped(KeyEvent e) {
        }
    }
    
    
    public void run() {
        //int c=0;
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        while (true) {
            //System.out.println( ++c );
            // do operations on shots in shots array
            for (int i = 0; i < shots.length; i++) 
            {
                if (shots[i] != null) {
                    // move shot
                    shots[i].moveShot(shotSpeed);       //무브 샷
                
                    // test if shot is out

                    
                    //만약 공격의 위치가 최대범위를(화면) 벗어나면 shot = null
                    if (shots[i].getX() > width || shots[i].getY() > height || shots[i].getX() < 0  || shots[i].getY() < 0) {
                        // remove shot from array
                        shots[i] = null;
                    }
                }
                
            }
            for (int i = 0; i < enemyShots.length; i++) 
            {
                if(enemyShots[i] != null)
                {
                    enemyShots[i].moveShot(-shotSpeed);
                    if (enemyShots[i].getX() > width || enemyShots[i].getY() > height || enemyShots[i].getX() < 0  || enemyShots[i].getY() < 0) {
                        // remove shot from array
                        enemyShots[i] = null;
                    }
                }
            }

            //플레이어 이동
            if (playerMoveLeft) {
                player.moveX(playerLeftSpeed);
                if (playerMoveRight) {
                    player.moveX(playerRightSpeed);
                } else if(playerMoveUp){
                   player.moveY(playerUpSpeed);
                } else if(playerMoveDown){
                    player.moveY(playerDownSpeed);
                }
            } else if (playerMoveRight) {
                player.moveX(playerRightSpeed);
                if (playerMoveLeft) {
                    player.moveX(playerLeftSpeed);
                } else if(playerMoveUp){
                   player.moveY(playerUpSpeed);
                } else if(playerMoveDown){
                    player.moveY(playerDownSpeed);
                }
            } else if(playerMoveUp){
                player.moveY(playerUpSpeed);
            } else if(playerMoveDown){
                player.moveY(playerDownSpeed);
            }

            
            Iterator enemyList = enemies.iterator();
            Iterator enemyList_1 = enemies_1.iterator();
            
            Player target = null;
            double temp;
            
            while (enemyList.hasNext()) {
                Enemy enemy = (Enemy) enemyList.next();
                enemy.move();
            }
            while (enemyList_1.hasNext()){
                Enemy_1 enemy_1 = (Enemy_1) enemyList_1.next();
                
                target = player;
                enemy_1.move(target);
            }

            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                // do nothing
            }

            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        }
    }

    public void initImage(Graphics g) {
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }

        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

        dbg.setColor(getForeground());
        //paint (dbg);

        g.drawImage(dbImage, 0, 0, this);
    }

    
    public void paintComponent(Graphics g) {
        initImage(g);

        // draw player
        player.drawPlayer(g);

        Iterator enemyList = enemies.iterator();
        Iterator enemyList_1 = enemies_1.iterator();
        
        
        while (enemyList.hasNext()) {
            Enemy enemy = (Enemy) enemyList.next();
            enemy.draw(g);
            //enemy와 shot 충돌
            if (enemy.isCollidedWithShot(shots)) {
                if( enemy.getHp()<= 0)
                    enemyList.remove();
            }
            //enemy 와 player 충돌
            if (enemy.isCollidedWithPlayer(player)) {
                enemyList.remove();
                player.decreaseHp(enemy.getDamage());   //player hp 감소
                if(player.getHp()<=0)       //player hp가 0이하 일 때
                    System.exit(0);         //프로그램 종료
            }
        }
        while (enemyList_1.hasNext()) {
            Enemy_1 enemy_1 = (Enemy_1) enemyList_1.next();
            enemy_1.draw(g);
            if (enemy_1.isCollidedWithShot(shots)){
                enemyList_1.remove();
                player.decreaseHp(enemy_1.getDamage());
                if(player.getHp()<= 0)
                    System.exit(0);
                
            }
        }
        if(player.isCollidedWithShot(enemyShots))
        {
            if(player.getHp()<=0)
            {
                System.out.println("player 체력 0");
                System.exit(0);
            }
            System.out.println("enemy의 총알 맞음 Hp : "+player.getHp());
        }
        
        for(int i=0; i<shots.length; i++)
        {
            //if(shots[i] != null && shots[i].getX()>width)
            //    shots[i].collided();
        }
        // draw shots
        for (int i = 0; i < shots.length; i++) {
            if (shots[i] != null) {
                shots[i].drawShot(g);
            }
        }
        for (int i = 0; i < enemyShots.length; i++) {
            if (enemyShots[i] != null) {
                enemyShots[i].drawShot(g);
            }
        }
        
    }
    
    //무기 생성 : file을 읽어서 저장된 내용을 통해 객체 생성
    public void generateWeapon(String filename)
    {
        //저장 형식
        //무기이름:초당공격속도:데미지
        //현재 들어가 있는 값
        //Spring:1.00:2.00
        //Summer:1.00:1.00
        //Fall:4.00:1.00
        //Winter:8.00:1.00
        String data = "";
        try{    
            FileReader dataFile = new FileReader("Weapon.txt");
            char[] buf = new char[1000];
            dataFile.read(buf);
            data = new String(buf);
        }catch(FileNotFoundException e){
            System.out.println("Weapon.txt : No file exits");
            System.exit(1);
        }catch(IOException e){
            System.out.println("IO Error");
            System.exit(2);
        }
        String s1[] = data.split("\n");
        String result[];
        
        String name = null;
        double attackSpeed = 0;
        double damage = 0;
        
        for(int i=0; i<s1.length; i++)
        {
            result = s1[i].split(":");
            
            for(int j=0; j<result.length; j++)
            switch(j)
                {
                    case 0:
                        name = result[j];
                        break;
                    case 1:
                        
                        attackSpeed = Double.parseDouble(result[j]);
                        break;
                    case 2:
                        damage = Double.parseDouble(result[j]);
                        break;
                }
            weapon[i] = new Weapon(name, attackSpeed, damage);
        }
        System.out.println("무기 정보 불러오기");// test
    }
    
    public void generateSkill(String filename)
    {
        //저장 형식
        //스킬 이름:스킬이동속도:데미지
        //현재 들어가 있는 값 없음
                
        String data = "";
        try{    
            FileReader dataFile = new FileReader("Skill.txt");
            char[] buf = new char[1000];
            dataFile.read(buf);
            data = new String(buf);
        }catch(FileNotFoundException e){
            System.out.println("Skill.txt : No file exits");
            System.exit(1);
        }catch(IOException e){
            System.out.println("IO Error");
            System.exit(2);
        }
        String s1[] = data.split("\n"); //한줄 씩 나누기
        String result[];
        
        String name = null;
        double attackSpeed = 0;
        double damage = 0;
        
        for(int i=0; i<s1.length; i++)
        {
            result = s1[i].split(":");
            
            for(int j=0; j<result.length; j++)
            switch(j)
                {
                    case 0: //스킬 이름
                        name = result[j];   
                        break;
                    case 1: //스킬 이동 속도
                        
                        attackSpeed = Double.parseDouble(result[j]);
                        break;
                    case 2: //데미지
                        damage = Double.parseDouble(result[j]);
                        break;
                }
            //skill[i] = new SkillShot();
        }
        System.out.println("스킬 정보 불러오기");// test
    }
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
            // TODO code application logic here
        
        JFrame frame = new JFrame("Shooting");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Shootingspaceship ship = new Shootingspaceship();
        ship.generateWeapon("Weapon.txt");  //파일 읽어 무기 생성
        ship.generateSkill("Skill.txt");    //파일 읽어 스킬 생성
        frame.getContentPane().add(ship);
        frame.pack();
        frame.setVisible(true);
        ship.start();
    }
}
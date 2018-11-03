package shootingspaceship;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;



public class Shootingspaceship extends JPanel implements Runnable {

    private Thread th;
    private Player player;
    private Shot[] shots;
    private ArrayList enemies;
    private final int shotSpeed = 3;        //공격의 이동 속도
    private final int playerLeftSpeed = -2; //플레이어 이동 속도
    private final int playerRightSpeed = 2;
    private final int playerUpSpeed = -2;
    private final int playerDownSpeed = 2;
    private final int width = 1200;
    private final int height = 900;
    private final int playerMargin = 10;
    private final int enemyMaxDownSpeed = 1;
    private final int enemyMaxHorizonSpeed = 1;
    private final int enemyTimeGap = 2000; //unit: msec
    private final float enemyDownSpeedInc = 0.3f;
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
    public Weapon[] weapon = new Weapon[100];

    
    public Shootingspaceship() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(width, height));
        player = new Player(width / 2, (int) (height * 0.9), playerMargin, width-playerMargin, 20, height-20 );
        shots = new Shot[ maxShotNum ];
        enemies = new ArrayList();
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
            if (++enemySize <= maxEnemySize) {
                float downspeed;
                do {
                    downspeed = rand.nextFloat() * enemyMaxDownSpeed;
                } while (downspeed == 0);

                float horspeed = rand.nextFloat() * 2 * enemyMaxHorizonSpeed - enemyMaxHorizonSpeed;
                //System.out.println("enemySize=" + enemySize + " downspeed=" + downspeed + " horspeed=" + horspeed);

                Enemy newEnemy = new Enemy((int) (rand.nextFloat() * width), 0, horspeed, downspeed, width, height, enemyDownSpeedInc);
                enemies.add(newEnemy);
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
                    player.weaponChange(weapon[0]);
                    autoAttackTimer.setDelay((int) (1000/player.getAttackSpeed())); //자동공격을 현재 공속으로 맞춤
                    break;
                case KeyEvent.VK_S:
                    player.weaponChange(weapon[1]);
                    autoAttackTimer.setDelay((int) (1000/player.getAttackSpeed())); //자동공격을 현재 공속으로 맞춤
                    //스킬 슬롯2 : S
                    break;
                case KeyEvent.VK_D:
                    player.weaponChange(weapon[2]); 
                    //스킬 슬롯3 : D
                    autoAttackTimer.setDelay((int) (1000/player.getAttackSpeed())); //자동공격을 현재 공속으로 맞춤
                    break;
                case KeyEvent.VK_F:
                    player.weaponChange(weapon[3]);
                    //스킬 슬롯4 : F
                    autoAttackTimer.setDelay((int) (1000/player.getAttackSpeed())); //자동공격을 현재 공속으로 맞춤
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
            for (int i = 0; i < shots.length; i++) {
                if (shots[i] != null) {
                    // move shot
                    shots[i].moveShot(shotSpeed);   

                    // test if shot is out
                    //만약 공격의 위치가 우측 최대범위를(화면) 벗어나면 shot = null
                    if (shots[i].getX() > width) {
                        // remove shot from array
                        shots[i] = null;
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
            while (enemyList.hasNext()) {
                Enemy enemy = (Enemy) enemyList.next();
                enemy.move();
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
        while (enemyList.hasNext()) {
            Enemy enemy = (Enemy) enemyList.next();
            enemy.draw(g);
            if (enemy.isCollidedWithShot(shots)) {
                if( enemy.getHp()<= 0)
                    enemyList.remove();
                //enemy.HpDecrease(shots);
                    //enemyList.remove();
            }
            if (enemy.isCollidedWithPlayer(player)) {
                enemyList.remove();
                System.exit(0);
            }
        }
        
        for(int i=0; i<shots.length; i++)
        {
            if(shots[i] != null && shots[i].getX()>width)
                shots[i].collided();
        }
        // draw shots
        for (int i = 0; i < shots.length; i++) {
            if (shots[i] != null) {
                shots[i].drawShot(g);
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
            System.out.println("No file exits");
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
        System.out.println("test");// test
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
        frame.getContentPane().add(ship);
        frame.pack();
        frame.setVisible(true);
        ship.start();
    }
}

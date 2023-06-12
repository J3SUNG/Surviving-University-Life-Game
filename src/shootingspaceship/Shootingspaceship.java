package shootingspaceship;

import skill.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import enemy.*;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import static shootingspaceship.UnitType.*;
import static shootingspaceship.SkillName.*;
import ui.InGameUI;
import ui.Intro;
import static ui.Intro.frame;
import static ui.Intro.intro;
import ui.ScoreBoard;

public class Shootingspaceship extends JPanel implements Runnable {

    private Thread th;
    public Player player;
    private Shot[] shots;                   //player 공격
    private EnemyShot[] enemyShots;              //enemy 공격
    private ArrayList enemies;
    private final int shotSpeed = 3;        //공격의 이동 속도
    private int playerLeftSpeed = -4; //플레이어 이동 속도
    private int playerRightSpeed = 4;
    private int playerUpSpeed = -4;
    private int playerDownSpeed = 4;
    public static final int width = 1200;
    public static final int height = 860;
    private final int playerMargin = 5;
    private final int enemyMaxDownSpeed = 1;
    private final int enemyMaxHorizonSpeed = 1;
    public final int enemyTimeGap = 3000; //unit: msec
    private float enemySpeed = 1f; // 이동속도
    private int enemySize;
    private int enemyNum = 0; // 어떤 에너미 출력할지qqqqqqqqq
    public boolean playerMoveLeft;
    public boolean playerMoveRight;
    public boolean playerMoveUp;
    public boolean playerMoveDown;
    private Image dbImage;
    private Graphics dbg;
    private Random rand;
    private int maxShotNum = 300;       //공격 최대 개수
    private int count = 0; // 저글링 5마리 카운트
    private int zergHeight;
    private float zergHor, zergDown; //저글링 동일 위치 나오게하기
    private int specialCount = 0; // 일정 카운트가 되면 엘리트와 보스가 나오게 할것.
    public static int score = 0; // 에너미를 일정 마리 잡았을 시 remove변경
    public static int stage = 0; // 스테이지 1~3
    private int stageUnit = 0;
    public int unitRemoveCount = 0;        //총 유닛 없앤 개수
    public int stageUnitRemoveCount = 0;   //스테이지에서 유닛 없앤 개수
    private boolean bossRemoveCheck = false; //해당 스테이지에서 보스를 잡았는지    //if(stageUnitRemoveCount > 일정 수 && bossRemoveCheck) 이면 stage++;

    private int cycleCount = 0;                 //유닛 생성 사이클 카운트
    public boolean cycleCountingState = true;  //사이클 카운트 상태 여부   : 엘리트나 보스가 나오고 나서 false :  잡히지 않으면 사이클 카운트 하지 않음
    private int enemyPackCreateCount = 0;       //현재 팩의 enemy 생성 개수
    private int packCount = 5;                  //한 팩의 enemy 개수
    private int enemyCreateCount = 0;           //현재 스테이지에서의 enemy 생성 개수
    private int enemyCreateCountSum = 0;        //총 에너미 생성 개수
    private int gameScore = 0;                  //게임 전체 스코어 : 잡은 유닛점수 + 남은 Hp + 깬 시간(반비례)
    private int eliteAttackCount = -1;
    private int attackNum;                      //몇번째 공격
    private int attackCycleNum = 0;               //공격 사이클 횟수
    private int bossAttackId = 1;                 //보스 패턴
    private int attackNumPlus = 0;
    private int plus1 = 1;      // +1  보스 회오리공격 패턴에 사용 
    
    private int selectMenu = 0;
    private int selectCharacter = 0;
    private boolean superMode = false;
    private boolean superModeUseCheck = false;      //한번이라도 슈퍼모드를 사용 했다면 0점 처리
    //슈퍼모드  : 쿨타임 제거, 스테이지 1,2,3 체인지 가능 관리자 접근 방법 숫자 0 : 5번
    private int superModeCount = 0;
    //슈퍼모드카운트가 5가 되면 false->true, true->false

    private javax.swing.Timer playerAutoAttack;     //플레이어 자동 공격 간격 타이머
    //스테이지 1 유닛 공격
    private javax.swing.Timer dragoonAutoAttack;    //드라군 자동 공격
    private javax.swing.Timer zerglingAutoAttack;   //저글링   자동 공격
    private javax.swing.Timer elite1AutoAttack;      //엘리트 자동 공격
    private javax.swing.Timer elite1AutoAttackGap;   //엘리트 자동 텀
    private javax.swing.Timer boss1AutoAttack;       //보스  자동 공격
    //스테이지 2 유닛 공격
    private javax.swing.Timer stage2Enemy1AutoAttack;   //드라군 자동 공격
    private javax.swing.Timer stage2Enemy3AutoAttack;   //엘리트 자동 공격
    private javax.swing.Timer elite2AutoAttack;         //엘리트 자동 공격
    private javax.swing.Timer boss2AutoAttack;       //보스  자동 공격
    //스테이지 3 유닛 공격
    private javax.swing.Timer stage3Enemy1AutoAttack;   //드라군 자동 공격
    private javax.swing.Timer stage3Enemy3AutoAttack;   //엘리트 자동 공격
    private javax.swing.Timer elite3AutoAttack;         //엘리트 자동 공격
    private javax.swing.Timer boss3AutoAttack;       //보스  자동 공격

    private javax.swing.Timer bossAttackChange;       //보스 공격 변경

    public javax.swing.Timer timer;    //enemy 자동 생성을 위한 timer
    private javax.swing.Timer timer2;    //enemy 자동 생성을 위한 timer
    private javax.swing.Timer shieldTimer;  //쉴드 생성 타이머
    private int shieldTime = 5000; //5초에 한번씩 증가

    addStage1Enemy stage1 = new addStage1Enemy();
    addStage2Enemy stage2 = new addStage2Enemy();
    addStage3Enemy stage3 = new addStage3Enemy();

    private BackgroundMove background;      //배경

    //public static Weapon[] weapon = new Weapon[10];   //무기
    public static ArrayList<Weapon> weapon = new ArrayList<>();
    public static Weapon myWeapon;

    public int playerSkill[] = new int[8];
    private int skillTime[] = new int[8];   //스킬 쿨타임
    public UseSkill useSkill;
    public static boolean INFERNO_Check = false;
    Timer eTimer = new Timer();

    private PlayerScoreList[] scoreList = new PlayerScoreList[10];
    private PlayerScoreList myScore;    //나의 게임 스코어, 닉네임
    private String myName;      //내 닉네임
    private int myRank = 0;     //내 랭크
    public final int maxBarrierHp = 25;
    //BGM 선언
    ArrayList<Track> trackList;

    private static int specialStage = 0;

    private Sound selectedMusic;
    private Sound deadSound;
    //에너미 사망음

    //스테이지1
    Dragoon dragoon;
    Scourge scourge;
    Zergling zergling;
    Elite1 elite1;
    Boss1 boss1;
    //스테이지2
    S2Enemy1 s2enemy1;
    S2Enemy2 s2enemy2;
    S2Enemy3 s2enemy3;
    S2Elite elite2;
    S2Boss boss2;
    //스테이지3
    S3Enemy1 s3enemy1;
    S3Enemy2 s3enemy2;
    S3Enemy3 s3enemy3;
    S3Elite elite3;
    S3Boss boss3;

    public InGameUI inGameUI;
    public ScoreBoard scoreBoard;                            //스코어 보드
   
    private Thread skillCoolTimeThread;

    public Shootingspaceship(InGameUI inGameUI) {
        background = new BackgroundMove();
        this.inGameUI = inGameUI;
        generateWeapon("Weapon.txt");       //4가지 무기 정보 불러오기
        setBackground(Color.black);
        setPreferredSize(new Dimension(width, 940));
        //player = new Player(width / 2, (int) (height * 0.9), playerMargin, width-playerMargin, 20, height-20 );
        player = new Player((int) (width * 0.1f), (int) (height / 2), playerMargin, width - playerMargin, 20, height - 20);
        shots = new Shot[maxShotNum];         //player 공격1
        enemyShots = new EnemyShot[maxShotNum];      //enemy 공격
        enemies = new ArrayList();
        enemySize = 0;
        rand = new Random(1);
        timer = new javax.swing.Timer(enemyTimeGap, stage1);//enemy 생성 타이머     //스테이지 1시작
        timer2 = new javax.swing.Timer(500, stage1);//enemy 생성 타이머             줄줄이 소시지를 위한 빠른생성 타이머, 스테이지 1
        bossAttackChange = new javax.swing.Timer(10000, new BossAttackChange());      // 보스공격 변경 10초마다
        useSkill = new UseSkill(player, shots, enemies, enemyShots, inGameUI);//스킬
        //플레이어 스킬
        playerSkill[0] = METAOR;
        playerSkill[1] = MISSILE;
        playerSkill[2] = SHOTGUN;
        playerSkill[3] = ULTIMATEMETAOR;
        playerSkill[4] = DAMAGEUP;
        playerSkill[5] = ATTACKSPEEDUP;
        playerSkill[6] = FIREHALFRING;
        playerSkill[7] = ULTIMATEINFERNO;
        INFERNO_Check = false;

        playerAutoAttack = new javax.swing.Timer((int) (1000), new playerAutoAttack()); //플레이어 자동공격 설정
        playerAutoAttack.setDelay((int) (1000 / player.getAttackSpeed()));

        //스테이지 1 유닛
        scourge = new Scourge(0, 0, 0, 0, width, height, enemySpeed, player);
        dragoon = new Dragoon(0, 0, 0, 0, width, height, enemySpeed, player);
        zergling = new Zergling(0, 0, 0, 0, width, height, enemySpeed, player);
        elite1 = new Elite1(0, 0, 0, 0, width, height, enemySpeed, player);
        boss1 = new Boss1(0, 0, 0, 0, width, height, enemySpeed, player);
        //스테이지 2 유닛
        s2enemy1 = new S2Enemy1(0, 0, 0, 0, width, height, enemySpeed, player);
        s2enemy2 = new S2Enemy2(0, 0, 0, 0, width, height, enemySpeed, player);
        s2enemy3 = new S2Enemy3(0, 0, 0, 0, width, height, enemySpeed, player);
        elite2 = new S2Elite(0, 0, 0, 0, width, height, enemySpeed, player);
        boss2 = new S2Boss(0, 0, 0, 0, width, height, enemySpeed, player);
        //스테이지 3 유닛
        s3enemy1 = new S3Enemy1(0, 0, 0, 0, width, height, enemySpeed, player);
        s3enemy2 = new S3Enemy2(0, 0, 0, 0, width, height, enemySpeed, player);
        s3enemy3 = new S3Enemy3(0, 0, 0, 0, width, height, enemySpeed, player);
        elite3 = new S3Elite(0, 0, 0, 0, width, height, enemySpeed, player);
        boss3 = new S3Boss(0, 0, 0, 0, width, height, enemySpeed, player);

        //스테이지 1 자동 공격 타이머
        dragoonAutoAttack = new javax.swing.Timer((int) (1000 / dragoon.getAttackSpeed()), new dragoonAutoAttack()); //드라군 자동공격 설정 
        zerglingAutoAttack = new javax.swing.Timer((int) (1000 / zergling.getAttackSpeed()), new zerglingAutoAttack()); //저글링 자동공격 설정

        elite1AutoAttack = new javax.swing.Timer((int) (1000 / elite1.getAttackSpeed()), new elite1AutoAttack()); //엘리트 자동공격 설정
        //elite1AutoAttack = new javax.swing.Timer(2000, new elite1AutoAttack()); //엘리트 자동공격 설정

        elite1AutoAttackGap = new javax.swing.Timer((int) (2000 / elite1.getAttackSpeed()), new elite1AutoAttack()); //엘리트 자동공격 설정
        boss1AutoAttack = new javax.swing.Timer((int) (300), new boss1AutoAttack()); //보스 자동공격 설정

        //스테이지 2 자동 공격 타이머
        stage2Enemy1AutoAttack = new javax.swing.Timer((int) (1000 / s2enemy1.getAttackSpeed()), new stage2Enemy1AutoAttack());
        stage2Enemy3AutoAttack = new javax.swing.Timer((int) (1000 / s2enemy3.getAttackSpeed()), new stage2Enemy3AutoAttack());
        elite2AutoAttack = new javax.swing.Timer((int) (1000 / elite2.getAttackSpeed()), new elite2AutoAttack());
        boss2AutoAttack = new javax.swing.Timer((int) (1000 / boss2.getAttackSpeed()), new boss2AutoAttack());
        //스테이지 3 자동 공격 타이머
        stage3Enemy1AutoAttack = new javax.swing.Timer((int) (1000 / s3enemy1.getAttackSpeed()), new stage3Enemy1AutoAttack());
        stage3Enemy3AutoAttack = new javax.swing.Timer((int) (1000 / s3enemy3.getAttackSpeed()), new stage3Enemy3AutoAttack());
        elite3AutoAttack = new javax.swing.Timer((int) (1000 / elite3.getAttackSpeed()), new elite3AutoAttack());
        boss3AutoAttack = new javax.swing.Timer((int) (1000 / boss3.getAttackSpeed()), new boss3AutoAttack());

        SkillCoolTime skillCoolTime = new SkillCoolTime();
        skillCoolTimeThread = new Thread(skillCoolTime);

        shieldTimer = new javax.swing.Timer(shieldTime, new ShieldIncreaseTimer());     //쉴드 증가 타이머
        setHpAndBarrier();
        //스테이지 1 자동 공격 타이머

        trackList = new ArrayList<Track>();

        trackList.add(new Track("stage1.mp3"));
        trackList.add(new Track("stage2.mp3"));
        trackList.add(new Track("stage3.mp3"));

        trackList.add(new Track("stage1elite.mp3"));
        trackList.add(new Track("stage2elite.mp3"));
        trackList.add(new Track("stage3elite.mp3"));

        trackList.add(new Track("stage1boss.mp3"));
        trackList.add(new Track("stage2boss.mp3"));
        trackList.add(new Track("stage3boss.mp3"));

        scoreBoard = new ScoreBoard();
        
        addKeyListener(new ShipControl());
        setFocusable(true);
    }

    public void start() {
        th = new Thread(this);
        th.start();
    }

    public void changeStageBGM(int stage) {
        stage = stage - 1;
        if (specialStage == 0) {
            if (selectedMusic != null) {
                selectedMusic.close();
            }
            selectedMusic = new Sound(trackList.get(stage).getStartMusic(), true);
            selectedMusic.start();
        } else if (specialStage == 1) {
            if (selectedMusic != null) {
                selectedMusic.close();
            }
            selectedMusic = new Sound(trackList.get(stage + 3).getStartMusic(), true);
            selectedMusic.start();
            specialStage = 0;
        } else if (specialStage == 2) {
            if (selectedMusic != null) {
                selectedMusic.close();
            }
            selectedMusic = new Sound(trackList.get(stage + 6).getStartMusic(), true);
            selectedMusic.start();
            specialStage = 0;
        }
    }

    public void returnToStageBGM(int stage) {
        stage = stage - 1;
        if (selectedMusic != null) {
            selectedMusic.close();
        }
        selectedMusic = new Sound(trackList.get(stage).getStartMusic(), true);
        specialStage = 0;
    }

    private class ShieldIncreaseTimer implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            
            if((player.getBarrierHp() < maxBarrierHp) && !player.barrierUseCheck())    //쉴드가 25보다 작을 때 && 배리어 사용하지 않고 있을 때
            {    player.increaseBarrierHp(1);
                System.out.println("------------------------");
                System.out.println(shieldTime / 1000 + "초 경과 쉴드+");
                System.out.println("------------------------");
            }
            setHpAndBarrier();
        }
    }

    private class SkillCoolTime implements Runnable {

        public void run() {
            for (int i = 0; i < 8; i++) //제일 처음 초기화
            {
                skillTime[i] = 0;
            }
            System.out.println("스킬 쿨타임 실행");
            while (true) {
                if (!superMode) //슈퍼모드가 아닐 때
                {
                    for (int i = 0; i < 8; i++) {
                        if (skillTime[i] > 0) {
                            skillTime[i] -= 200;
                            //System.out.println(i + ": cooltime: " +skillTime[i]);
                        }
                        if (skillTime[i] <= 0) {
                            changeSkillOnImage(i);
                            skillTime[i] = 0;

                        }
                    }
                } else //슈퍼 모드 일 때
                {
                    for (int i = 0; i < 8; i++) {
                        skillTime[i] = 0;
                        changeSkillOnImage(i);
                    }
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    //아무것두 안함
                }
            }
        }
    }

    private void changeSkillOnImage(int skillNum) {
        switch (skillNum) {
            case 0:
                inGameUI.QSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/qskill.png")));
                break;
            case 1:
                inGameUI.WSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/wskill.png")));
                break;
            case 2:
                inGameUI.ESkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/eskill.png")));
                break;
            case 3:
                inGameUI.RSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/rskill.png")));
                break;
            case 4:
                inGameUI.ASkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/askill.png")));
                break;
            case 5:
                inGameUI.SSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/sskill.png")));
                break;
            case 6:
                inGameUI.DSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/dskill.png")));
                break;
            case 7:
                inGameUI.FSkill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/UIImage/fskill.png")));
                break;
        }

    }

    private class BossAttackChange implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            ++bossAttackId;
            if (stage == 1) {
                if (bossAttackId == 4) {
                    bossAttackId = 1;
                }
            } else if (stage == 2) {
                if (bossAttackId == 5) {
                    bossAttackId = 1;
                }
            } else if (stage == 3) {
                if (bossAttackId == 8) {
                    bossAttackId = 1;
                }
            }

        }
    }

    //스테이지 1 enemy 생성---------------------------------------------------------------------
    private class addStage1Enemy implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            float downspeed;
            do {
                downspeed = rand.nextFloat() * enemyMaxDownSpeed;
            } while (downspeed == 0);

            float horspeed = rand.nextFloat() - enemyMaxHorizonSpeed;
            //System.out.println("enemySize=" + enemySize + " downspeed=" + downspeed + " horspeed=" + horspeed);
            int ranHeight = (int) (rand.nextFloat() * width);
            Enemy newEnemy;
            System.out.print("사이클 카운트 : " + cycleCount);
            System.out.print("  enemyCeateCount : " + enemyCreateCount);
            System.out.println("    스테이지 : " + stage);
            if (enemyCreateCount == 0) {
                //첫번째 실행일 경우
                stageUnit = ZERGLING;   //저글링 생성
                enemyPackCreateCount = 0;
            }
            if (cycleCount == 2) //유닛 사이클이 3번 돌면
            {
                stageUnit = ELITE1; //엘리트 생성
            }
            if (cycleCount == 5) //유닛 사이클이 8번 돌면(엘리트 이후 4번 : 엘리트가 생성 되는 것도 한 사이클로 취급)
            {
                stageUnit = BOSS1;
            }
            switch (stageUnit) //현재 뽑는 유닛
            {
                case ZERGLING:              //저글링
                    if (enemyPackCreateCount == 0) {
                        zergHeight = ranHeight;
                        zergHor = horspeed;
                        zergDown = downspeed;
                        timer.stop();       // 새로운 timer로 빠르게 생성
                        timer2.start();
                    }
                    newEnemy = new Zergling(width, zergHeight, zergHor, zergDown, width, height, enemySpeed, player);
                    enemies.add(newEnemy);
                    ++enemyCreateCount;     //enemy 생성 개수 증가
                    ++enemyPackCreateCount; //현재 팩의 enemy 개수 카운트

                    if (enemyPackCreateCount == 5) //5마리 전부 생성 했으면 다시 원래 타이머를 실행
                    {
                        enemyPackCreateCount = 0;
                        stageUnit = SCOURGE;    //다음 유닛 : 스커지
                        timer2.stop();
                        timer.start();
                    }
                    break;
                case SCOURGE:
                    for (int i = 0; i < 3; ++i) {
                        newEnemy = new Scourge(width, (int) (ranHeight % (height - 600) + (300 * i)), horspeed, downspeed, width, height, enemySpeed, player);
                        enemies.add(newEnemy);
                        ++enemyCreateCount;      //enemy 생성 개수 증가
                    }
                    stageUnit = DRAGOON;    //다음 유닛 : 드라군
                    break;
                case DRAGOON:
                    for (int i = 0; i < 4; ++i) {
                        newEnemy = new Dragoon(width, (int) (ranHeight % (height - 600) + (200 * i)), horspeed, downspeed, width, height, enemySpeed, player);
                        enemies.add(newEnemy);
                        ++enemyCreateCount;      //enemy 생성 개수 증가
                    }
                    stageUnit = ZERGLING;   // 다시 첫 유닛부터 생성(enemy1~3 반복)
                    if (cycleCountingState) {
                        ++cycleCount;  //유닛 1~3 까지 생성됨 사이클 1 증가
                    }
                    break;
                case ELITE1:
                    newEnemy = new Elite1(width, (int) (ranHeight % (height - 600) + 300), horspeed, downspeed, width, height, enemySpeed, player);
                    specialStage = 1;
                    changeStageBGM(stage);
                    enemies.add(newEnemy);
                    ++enemyCreateCount;         //유닛 생성 개수 증가
                    ++cycleCount;               //한 사이클로 취급
                    timer.setDelay(5000);       //엘리트 생성시 적 유닛 생성 시간 변경 : 5초
                    stageUnit = ZERGLING;       //다시 첫 유닛부터 생성
                    cycleCountingState = false; //사이클 카운팅 X   엘리트가 잡히면 다시 시작, 그리고 timer 시간 다시 원래대로
                    break;
                case BOSS1:
                    newEnemy = new Boss1(width, (int) (ranHeight % (height - 600) + 300), horspeed, downspeed, width, height, enemySpeed, player);
                    enemies.add(newEnemy);
                    specialStage = 2;
                    changeStageBGM(stage);

                    ++enemyCreateCount;
                    ++cycleCount;               //한 사이클로 취급
                    timer.setDelay(5000);       //엘리트 생성시 적 유닛 생성 시간 변경 : 5초    
                    stageUnit = ZERGLING;       //다시 첫 유닛부터 생성
                    cycleCountingState = false; //사이클 카운팅 X   보스가 잡히면 다시 시작, 그리고 다음 스테이지로 넘어감, timer 시간 다시 원래대로  : 구현은 remove 있는 곳에서
                    break;
            }
        }
    }

    //스테이지 2 enemy 생성---------------------------------------------------------------------
    private class addStage2Enemy implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            float downspeed;
            do {
                downspeed = rand.nextFloat() * enemyMaxDownSpeed;
            } while (downspeed == 0);

            float horspeed = rand.nextFloat() - enemyMaxHorizonSpeed;
            //System.out.println("enemySize=" + enemySize + " downspeed=" + downspeed + " horspeed=" + horspeed);
            int ranHeight = (int) (rand.nextFloat() * width);
            Enemy newEnemy;
            System.out.print("사이클 카운트 : " + cycleCount);
            System.out.print("  enemyCeateCount : " + enemyCreateCount);
            System.out.println("    스테이지 : " + stage);
            if (enemyCreateCount == 0) {
                //첫번째 실행일 경우
                stageUnit = STAGE2ENEMY1;   //저글링 생성
                enemyPackCreateCount = 0;
            }
            if (cycleCount == 2) //유닛 사이클이 3번 돌면
            {
                stageUnit = ELITE2; //엘리트 생성
            }
            if (cycleCount == 4) //유닛 사이클이 8번 돌면(엘리트 이후 4번 : 엘리트가 생성 되는 것도 한 사이클로 취급)
            {
                stageUnit = BOSS2;
            }
            switch (stageUnit) //현재 뽑는 유닛
            {
                case STAGE2ENEMY1:              //저글링
                    if (enemyPackCreateCount == 0) {
                        zergHeight = ranHeight;
                        zergHor = horspeed;
                        zergDown = downspeed;
                        timer.stop();       // 새로운 timer로 빠르게 생성
                        timer2.start();
                    }
                    newEnemy = new S2Enemy1(width, zergHeight, zergHor, zergDown, width, height, enemySpeed, player);
                    enemies.add(newEnemy);
                    ++enemyCreateCount;     //enemy 생성 개수 증가
                    ++enemyPackCreateCount; //현재 팩의 enemy 개수 카운트
                    //System.out.println("스테이지 2 팩");
                    if (enemyPackCreateCount == 5) //5마리 전부 생성 했으면 다시 원래 타이머를 실행
                    {
                        enemyPackCreateCount = 0;
                        stageUnit = STAGE2ENEMY2;    //다음 유닛 : enemy2
                        timer2.stop();
                        timer.start();
                    }
                    break;
                case STAGE2ENEMY2:
                    for (int i = 0; i < 3; ++i) {
                        newEnemy = new S2Enemy2(width, (int) (ranHeight % (height - 600) + (300 * i)), horspeed, downspeed, width, height, enemySpeed, player);
                        enemies.add(newEnemy);
                        ++enemyCreateCount;      //enemy 생성 개수 증가
                    }
                    stageUnit = STAGE2ENEMY3;    //다음 유닛 : enemy3
                    break;
                case STAGE2ENEMY3:
                    for (int i = 0; i < 4; ++i) {
                        newEnemy = new S2Enemy3(width, (int) (ranHeight % (height - 600) + (200 * i)), horspeed, downspeed, width, height, enemySpeed, player);
                        enemies.add(newEnemy);
                        ++enemyCreateCount;      //enemy 생성 개수 증가
                    }
                    stageUnit = STAGE2ENEMY1;   // 다시 첫 유닛부터 생성(enemy1~3 반복)
                    if (cycleCountingState) {
                        ++cycleCount;  //유닛 1~3 까지 생성됨 사이클 1 증가
                    }
                    break;
                case ELITE2:
                    newEnemy = new S2Elite(width, (int) (ranHeight % (height - 600) + 300), horspeed, downspeed, width, height, enemySpeed, player);
                    enemies.add(newEnemy);
                    specialStage = 1;
                    changeStageBGM(stage);

                    ++enemyCreateCount;         //유닛 생성 개수 증가
                    ++cycleCount;               //한 사이클로 취급
                    timer.setDelay(5000);       //엘리트 생성시 적 유닛 생성 시간 변경 : 5초
                    stageUnit = STAGE2ENEMY1;       //다시 첫 유닛부터 생성
                    cycleCountingState = false; //사이클 카운팅 X   엘리트가 잡히면 다시 시작, 그리고 timer 시간 다시 원래대로
                    break;
                case BOSS2:
                    newEnemy = new S2Boss(width, (int) (ranHeight % (height - 600) + 300), horspeed, downspeed, width, height, enemySpeed, player);
                    specialStage = 2;
                    changeStageBGM(stage);

                    enemies.add(newEnemy);
                    ++enemyCreateCount;
                    ++cycleCount;               //한 사이클로 취급
                    timer.setDelay(5000);       //엘리트 생성시 적 유닛 생성 시간 변경 : 5초    
                    stageUnit = STAGE2ENEMY1;       //다시 첫 유닛부터 생성
                    cycleCountingState = false; //사이클 카운팅 X   보스가 잡히면 다시 시작, 그리고 다음 스테이지로 넘어감, timer 시간 다시 원래대로  : 구현은 remove 있는 곳에서
                    break;
            }
        }
    }

    //스테이지 3 enemy 생성---------------------------------------------------------------------
    private class addStage3Enemy implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            float downspeed;
            do {
                downspeed = rand.nextFloat() * enemyMaxDownSpeed;
            } while (downspeed == 0);

            float horspeed = rand.nextFloat() - enemyMaxHorizonSpeed;
            //System.out.println("enemySize=" + enemySize + " downspeed=" + downspeed + " horspeed=" + horspeed);
            int ranHeight = (int) (rand.nextFloat() * width);
            Enemy newEnemy;
            System.out.print("사이클 카운트 : " + cycleCount);
            System.out.print("  enemyCeateCount : " + enemyCreateCount);
            System.out.println("    스테이지 : " + stage);
            if (enemyCreateCount == 0) {
                //첫번째 실행일 경우
                stageUnit = STAGE3ENEMY1;   //enemy1 생성
                enemyPackCreateCount = 0;
            }
            if (cycleCount == 2) //유닛 사이클이 3번 돌면
            {
                stageUnit = ELITE3; //엘리트 생성
            }
            if (cycleCount == 4) //유닛 사이클이 8번 돌면(엘리트 이후 4번 : 엘리트가 생성 되는 것도 한 사이클로 취급)
            {
                stageUnit = BOSS3;
            }
            switch (stageUnit) //현재 뽑는 유닛
            {
                case STAGE3ENEMY1:              //저글링
                    if (enemyPackCreateCount == 0) {
                        zergHeight = ranHeight;
                        zergHor = horspeed;
                        zergDown = downspeed;
                        timer.stop();       // 새로운 timer로 빠르게 생성
                        timer2.start();
                    }
                    newEnemy = new S3Enemy1(width, zergHeight, zergHor, zergDown, width, height, enemySpeed, player);
                    enemies.add(newEnemy);
                    ++enemyCreateCount;     //enemy 생성 개수 증가
                    ++enemyPackCreateCount; //현재 팩의 enemy 개수 카운트

                    if (enemyPackCreateCount == 5) //5마리 전부 생성 했으면 다시 원래 타이머를 실행
                    {
                        enemyPackCreateCount = 0;
                        stageUnit = STAGE3ENEMY2;    //다음 유닛 : 스커지
                        timer2.stop();
                        timer.start();
                    }
                    break;
                case STAGE3ENEMY2:
                    for (int i = 0; i < 3; ++i) {
                        newEnemy = new S3Enemy2(width, (int) (ranHeight % (height - 600) + (300 * i)), horspeed, downspeed, width, height, enemySpeed, player);
                        enemies.add(newEnemy);
                        ++enemyCreateCount;      //enemy 생성 개수 증가
                    }
                    stageUnit = STAGE3ENEMY3;    //다음 유닛 : 드라군
                    break;
                case STAGE3ENEMY3:
                    for (int i = 0; i < 4; ++i) {
                        newEnemy = new S3Enemy3(width, (int) (ranHeight % (height - 600) + (200 * i)), horspeed, downspeed, width, height, enemySpeed, player);
                        enemies.add(newEnemy);
                        ++enemyCreateCount;      //enemy 생성 개수 증가
                    }
                    stageUnit = STAGE3ENEMY1;   // 다시 첫 유닛부터 생성(enemy1~3 반복)
                    if (cycleCountingState) {
                        ++cycleCount;  //유닛 1~3 까지 생성됨 사이클 1 증가
                    }
                    break;
                case ELITE3:
                    newEnemy = new S3Elite(width, (int) (ranHeight % (height - 600) + 300), horspeed, downspeed, width, height, enemySpeed, player);
                    enemies.add(newEnemy);
                    specialStage = 1;
                    changeStageBGM(stage);
                    ++enemyCreateCount;         //유닛 생성 개수 증가
                    ++cycleCount;               //한 사이클로 취급
                    timer.setDelay(5000);       //엘리트 생성시 적 유닛 생성 시간 변경 : 5초
                    stageUnit = STAGE3ENEMY1;       //다시 첫 유닛부터 생성
                    cycleCountingState = false; //사이클 카운팅 X   엘리트가 잡히면 다시 시작, 그리고 timer 시간 다시 원래대로
                    break;
                case BOSS3:
                    newEnemy = new S3Boss(width, (int) (ranHeight % (height - 600) + 300), horspeed, downspeed, width, height, enemySpeed, player);
                    enemies.add(newEnemy);
                    specialStage = 2;
                    changeStageBGM(stage);
                    ++enemyCreateCount;
                    ++cycleCount;               //한 사이클로 취급
                    timer.setDelay(5000);       //엘리트 생성시 적 유닛 생성 시간 변경 : 5초    
                    stageUnit = STAGE3ENEMY1;       //다시 첫 유닛부터 생성
                    cycleCountingState = false; //사이클 카운팅 X   보스가 잡히면 다시 시작, 그리고 다음 스테이지로 넘어감, timer 시간 다시 원래대로  : 구현은 remove 있는 곳에서
                    break;
            }
        }
    }

    //플레이어 자동 공격
    int pCount = 0;

    private class playerAutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            pCount = 0;
            if (playerAutoAttack.getDelay() != (int) (1000 / player.getAttackSpeed())) //만약 플레이어 자동 공격 타이머와,  플레이어 공속 시간이 다를 경우 현재공속 적용 시켜주기
            {
                playerAutoAttack.setDelay((int) (1000 / player.getAttackSpeed()));     //발생 이유 : 공속 버프로 인해 다를 경우가 생김, 또는 무기 처음 장착
            }
            switch (player.getAttackType()) {
                case 1:     //일반 공격
                    for (int i = 0; i < shots.length; i++) {
                        if (shots[i] == null) {
                            shots[i] = player.generateShot();
                            //shots[i].setPlayerType();
                            //System.out.println("플레이어 shots["+i+"]");
                            break;
                        }
                    }
                    break;
                case 2:     //더블 공격
                    for (int i = 0; i < shots.length; i++) {
                        if (shots[i] == null) {
                            shots[i] = player.generateShot();
                            if (pCount == 0) {
                                shots[i].moveY(-10);
                                pCount++;
                            } else {
                                shots[i].moveY(10);
                                pCount = 0;
                                break;
                            }
                        }
                    }
                    break;
                case 3:     //일반공격 + 새끼 2마리 공격
                    for (int i = 0; i < shots.length; i++) {
                        if (shots[i] == null) {
                            if (pCount == 0) {
                                ++pCount;
                                //shots[i].setMaker("player");
                                shots[i] = player.generateShot();
                                shots[i].setUnitType(PLAYER);

                            } else if (pCount == 1) //새끼 공격 위
                            {
                                shots[i] = player.generateShot((int) 0);
                                shots[i].setDamage(shots[i].getDamage() * 9 / 10);     //새끼 공격은 하나당 데미지 9/10
                                shots[i].moveY(-14);
                                shots[i].moveX(-30);
                                shots[i].setImage(player.getShotImage());
                                shots[i].setSizeXY((int) (player.getShotSize() / 1.5));
                                ++pCount;
                            } else if (pCount == 2) //새끼 공격 아래
                            {
                                shots[i] = player.generateShot((int) 1);
                                shots[i].setDamage(shots[i].getDamage() * 9 / 10);     //새끼 공격은 하나당 데미지 9/10
                                shots[i].moveY(14);
                                shots[i].moveX(-30);
                                shots[i].setImage(player.getShotImage());
                                shots[i].setSizeXY((int) (player.getShotSize() / 1.5));
                                pCount = 0;
                                break;
                            }
                        }
                    }
            }
        }
    }
//스테이지 1 자동공격=======================================================================================
    //저글링 자동 공격

    private class zerglingAutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Enemy enemy;
            for (int i = 0; i < enemies.size(); ++i) {
                enemy = (Enemy) enemies.get(i);
                if (enemy.getUnitType() == ZERGLING) //드라군일 때 샷 쏘기
                {
                    for (int j = 0; j < enemyShots.length; ++j) {
                        if (enemyShots[j] == null) {
                            enemyShots[j] = enemy.generateShot();
                            break;
                        }
                    }
                }
            }
            //System.out.println("zerglingAutoAttack");
        }
    }

    private class dragoonAutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Enemy enemy;
            for (int i = 0; i < enemies.size(); ++i) {
                enemy = (Enemy) enemies.get(i);
                if (enemy.getUnitType() == DRAGOON) //드라군일 때 샷 쏘기
                {
                    for (int j = 0; j < enemyShots.length; ++j) {
                        if (enemyShots[j] == null) {
                            enemyShots[j] = enemy.generateShot();
                            break;
                        }
                    }
                }
            }
            //System.out.println("dragoonAutoAttack");
        }
    }

    //eliteAutoAttack  엘리트 자동 공격
    private class elite1AutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Enemy enemy;
            for (int i = 0; i < enemies.size(); ++i) {
                enemy = (Enemy) enemies.get(i);
                if (enemy.getUnitType() == ELITE1) //엘리트일 때 샷 쏘기
                {
                    if (eliteAttackCount == -1) //시작 변수로 다시 실행, 여기서 한타임 쉬게됨
                    {
                        elite1AutoAttack.setDelay((int) (1000 / elite1.getAttackSpeed())); //아래의 setDelay로 인해 남은 2초 후 바뀐 시간으로 실행
                        eliteAttackCount++;
                    } else if (0 <= eliteAttackCount && eliteAttackCount < 20) {                           //20발 빠르게 발사
                        for (int j = 0; j < enemyShots.length; ++j) {
                            if (enemyShots[j] == null) {
                                enemyShots[j] = enemy.generateShot();
                                ++eliteAttackCount;
                                break;
                            }
                        }
                    } else {
                        elite1AutoAttack.setDelay((int) (2000));         //타이머 시간 변경
                        eliteAttackCount = -1;                          //시작변수
                    }
                }
            }
            //System.out.println("Elite1AutoAttack");
        }
    }

    private class boss1AutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Enemy enemy;
            int fourAttack;
            switch (bossAttackId) {
                case 1:     //원
                    for (attackNum = 0; attackNum < 16; ++attackNum) {            //30발 동시 발사
                        for (int i = 0; i < enemies.size(); ++i) {
                            enemy = (Enemy) enemies.get(i);
                            if (enemy.getUnitType() == BOSS1) {
                                for (int j = 0; j < enemyShots.length; ++j) {
                                    if (enemyShots[j] == null) {
                                        boss1AutoAttack.setDelay(300);
                                        enemyShots[j] = enemy.generateShot(attackNum, attackCycleNum % 2);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    ++attackCycleNum;
                    break;
                case 2:     // 샷건
                    for (attackNum = 0; attackNum < 30; ++attackNum) {            //30발 동시 발사
                        for (int i = 0; i < enemies.size(); ++i) {
                            enemy = (Enemy) enemies.get(i);
                            if (enemy.getUnitType() == BOSS1) {
                                for (int j = 0; j < enemyShots.length; ++j) {
                                    if (enemyShots[j] == null) {
                                        boss1AutoAttack.setDelay(300);
                                        enemyShots[j] = enemy.generateShot(attackNum, -1);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 3:     // 커브샷
                    for (int i = 0; i < enemies.size(); ++i) {
                        enemy = (Enemy) enemies.get(i);
                        if (enemy.getUnitType() == BOSS1) {
                            for (int j = 0; j < enemyShots.length; ++j) {
                                if (enemyShots[j] == null) {
                                    boss1AutoAttack.setDelay(300);
                                    enemyShots[j] = enemy.generateShot(attackNum, -2);  // -2로 curve샷
                                    break;
                                }
                            }
                        }
                    }
                    break;
            }

        }
    }

//스테이지 2 자동공격=======================================================================================
    private class stage2Enemy1AutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Enemy enemy;
            for (int i = 0; i < enemies.size(); ++i) {
                enemy = (Enemy) enemies.get(i);
                if (enemy.getUnitType() == STAGE2ENEMY1) {
                    for (int j = 0; j < enemyShots.length; ++j) {
                        if (enemyShots[j] == null) {
                            enemyShots[j] = enemy.generateShot();
                            break;
                        }
                    }
                }
            }
            //System.out.println("stage2Enemy1AutoAttack");
        }
    }

    private class stage2Enemy3AutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Enemy enemy;
            for (int i = 0; i < enemies.size(); ++i) {
                enemy = (Enemy) enemies.get(i);
                if (enemy.getUnitType() == STAGE2ENEMY3) {
                    for (int j = 0; j < enemyShots.length; ++j) {
                        if (enemyShots[j] == null) {
                            enemyShots[j] = enemy.generateShot();
                            break;
                        }
                    }
                }
            }
            //System.out.println("stage2Enemy3AutoAttack");
        }
    }

    private class elite2AutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Enemy enemy;
            for (int i = 0; i < enemies.size(); ++i) {
                enemy = (Enemy) enemies.get(i);
                if (enemy.getUnitType() == ELITE2) //엘리트일 때 샷 쏘기
                {
                    if (eliteAttackCount == -1) //시작 변수로 다시 실행, 여기서 한타임 쉬게됨
                    {
                        elite2AutoAttack.setDelay((int) (1000 / elite2.getAttackSpeed())); //아래의 setDelay로 인해 남은 2초 후 바뀐 시간으로 실행
                        eliteAttackCount++;
                    } else if (0 <= eliteAttackCount && eliteAttackCount < 20) {                           //20발 빠르게 발사
                        for (int j = 0; j < enemyShots.length; ++j) {
                            if (enemyShots[j] == null) {
                                enemyShots[j] = enemy.generateShot();
                                ++eliteAttackCount;
                                break;
                            }
                        }
                    } else {
                        elite2AutoAttack.setDelay((int) (2000));         //타이머 시간 변경
                        eliteAttackCount = -1;                          //시작변수
                    }
                }
            }
            //System.out.println("Elite2AutoAttack");
        }
    }

    private class boss2AutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Enemy enemy;
            int fourAttack;
            switch (bossAttackId) {
                case 1:     //회오리
                    for (attackNum = 0; attackNum < 4; ++attackNum) {
                        for (int i = 0; i < enemies.size(); ++i) {
                            enemy = (Enemy) enemies.get(i);
                            if (enemy.getUnitType() == BOSS2) {
                                for (int j = 0; j < enemyShots.length; ++j) {
                                    if (enemyShots[j] == null) {
                                        fourAttack = 3 * attackNum;
                                        boss2AutoAttack.setDelay(100);
                                        enemyShots[j] = enemy.generateShot(attackNum + attackNumPlus + (fourAttack), 0);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (attackCycleNum > 30) {
                        plus1 *= -1;
                        attackCycleNum -= 1;
                    } else if (attackCycleNum < 0) {
                        plus1 *= -1;
                        attackCycleNum += 1;
                    }
                    attackCycleNum += plus1;
                    attackNumPlus += plus1;
                    break;
                case 2:     // 느려지는 랜덤총알
                    for (int i = 0; i < enemies.size(); ++i) {
                        enemy = (Enemy) enemies.get(i);
                        if (enemy.getUnitType() == BOSS2) {
                            for (int j = 0; j < enemyShots.length; ++j) {
                                if (enemyShots[j] == null) {
                                    boss2AutoAttack.setDelay(100);
                                    enemyShots[j] = enemy.generateShot(attackNum, -3);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case 3:     // 가속도 랜덤총알
                    for (int i = 0; i < enemies.size(); ++i) {
                        enemy = (Enemy) enemies.get(i);
                        if (enemy.getUnitType() == BOSS2) {
                            for (int j = 0; j < enemyShots.length; ++j) {
                                if (enemyShots[j] == null) {
                                    boss2AutoAttack.setDelay(100);
                                    enemyShots[j] = enemy.generateShot(attackNum, -4);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case 4:     // 개틀링건
                    for (attackNum = 0; attackNum < 5; ++attackNum) {
                        for (int i = 0; i < enemies.size(); ++i) {
                            enemy = (Enemy) enemies.get(i);
                            if (enemy.getUnitType() == BOSS2) {
                                for (int j = 0; j < enemyShots.length; ++j) {
                                    if (enemyShots[j] == null) {
                                        boss2AutoAttack.setDelay(100);
                                        enemyShots[j] = enemy.generateShot(attackNum, -5);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    break;
            }

        }
    }
//스테이지 3 자동공격=======================================================================================

    private class stage3Enemy1AutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Enemy enemy;
            for (int i = 0; i < enemies.size(); ++i) {
                enemy = (Enemy) enemies.get(i);
                if (enemy.getUnitType() == STAGE3ENEMY1) {
                    for (int j = 0; j < enemyShots.length; ++j) {
                        if (enemyShots[j] == null) {
                            enemyShots[j] = enemy.generateShot();
                            break;
                        }
                    }
                }
            }
            // System.out.println("stage3Enemy1AutoAttack");
        }
    }

    private class stage3Enemy3AutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Enemy enemy;
            for (int i = 0; i < enemies.size(); ++i) {
                enemy = (Enemy) enemies.get(i);
                if (enemy.getUnitType() == STAGE3ENEMY3) {
                    for (int j = 0; j < enemyShots.length; ++j) {
                        if (enemyShots[j] == null) {
                            enemyShots[j] = enemy.generateShot();
                            break;
                        }
                    }
                }
            }
            //System.out.println("stage3Enemy3AutoAttack");
        }
    }

    private class elite3AutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Enemy enemy;
            for (int i = 0; i < enemies.size(); ++i) {
                enemy = (Enemy) enemies.get(i);
                if (enemy.getUnitType() == ELITE3) //엘리트일 때 샷 쏘기
                {
                    if (eliteAttackCount == -1) //시작 변수로 다시 실행, 여기서 한타임 쉬게됨
                    {
                        elite1AutoAttack.setDelay((int) (1000 / elite1.getAttackSpeed())); //아래의 setDelay로 인해 남은 2초 후 바뀐 시간으로 실행
                        eliteAttackCount++;
                    } else if (0 <= eliteAttackCount && eliteAttackCount < 20) {                           //20발 빠르게 발사
                        for (int j = 0; j < enemyShots.length; ++j) {
                            if (enemyShots[j] == null) {
                                enemyShots[j] = enemy.generateShot();
                                ++eliteAttackCount;
                                break;
                            }
                        }
                    } else {
                        elite1AutoAttack.setDelay((int) (2000));         //타이머 시간 변경
                        eliteAttackCount = -1;                          //시작변수
                    }
                }
            }
            //System.out.println("Elite3AutoAttack");
        }
    }

    private class boss3AutoAttack implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Enemy enemy;
            int fourAttack;
            switch (bossAttackId) {
                case 1:     //원
                    for (attackNum = 0; attackNum < 16; ++attackNum) {            //30발 동시 발사
                        for (int i = 0; i < enemies.size(); ++i) {
                            enemy = (Enemy) enemies.get(i);
                            if (enemy.getUnitType() == BOSS3) {
                                for (int j = 0; j < enemyShots.length; ++j) {
                                    if (enemyShots[j] == null) {
                                        boss3AutoAttack.setDelay(300);
                                        enemyShots[j] = enemy.generateShot(attackNum, attackCycleNum % 2);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    ++attackCycleNum;
                    break;
                case 2:     //회오리
                    for (attackNum = 0; attackNum < 4; ++attackNum) {
                        for (int i = 0; i < enemies.size(); ++i) {
                            enemy = (Enemy) enemies.get(i);
                            if (enemy.getUnitType() == BOSS3) {
                                for (int j = 0; j < enemyShots.length; ++j) {
                                    if (enemyShots[j] == null) {
                                        fourAttack = 3 * attackNum;
                                        boss3AutoAttack.setDelay(100);
                                        enemyShots[j] = enemy.generateShot(attackNum + attackNumPlus + (fourAttack), 0);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (attackCycleNum > 30) {
                        plus1 *= -1;
                        attackCycleNum -= 1;
                    } else if (attackCycleNum < 0) {
                        plus1 *= -1;
                        attackCycleNum += 1;
                    }
                    attackCycleNum += plus1;
                    attackNumPlus += plus1;
                    break;
                case 3:     // 커브샷
                    for (int i = 0; i < enemies.size(); ++i) {
                        enemy = (Enemy) enemies.get(i);
                        if (enemy.getUnitType() == BOSS3) {
                            for (int j = 0; j < enemyShots.length; ++j) {
                                if (enemyShots[j] == null) {
                                    boss3AutoAttack.setDelay(300);
                                    enemyShots[j] = enemy.generateShot(attackNum, -2);  // -2로 curve샷
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case 4:     // 샷건
                    for (attackNum = 0; attackNum < 15; ++attackNum) {            //30발 동시 발사
                        for (int i = 0; i < enemies.size(); ++i) {
                            enemy = (Enemy) enemies.get(i);
                            if (enemy.getUnitType() == BOSS3) {
                                for (int j = 0; j < enemyShots.length; ++j) {
                                    if (enemyShots[j] == null) {
                                        boss3AutoAttack.setDelay(300);
                                        enemyShots[j] = enemy.generateShot(attackNum, -1);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 5:     // 느려지는 랜덤총알
                    for (int i = 0; i < enemies.size(); ++i) {
                        enemy = (Enemy) enemies.get(i);
                        if (enemy.getUnitType() == BOSS3) {
                            for (int j = 0; j < enemyShots.length; ++j) {
                                if (enemyShots[j] == null) {
                                    boss3AutoAttack.setDelay(100);
                                    enemyShots[j] = enemy.generateShot(attackNum, -3);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case 6:     // 가속도 랜덤총알
                    for (int i = 0; i < enemies.size(); ++i) {
                        enemy = (Enemy) enemies.get(i);
                        if (enemy.getUnitType() == BOSS3) {
                            for (int j = 0; j < enemyShots.length; ++j) {
                                if (enemyShots[j] == null) {
                                    boss3AutoAttack.setDelay(100);
                                    enemyShots[j] = enemy.generateShot(attackNum, -4);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case 7:     // 개틀링건
                    for (attackNum = 0; attackNum < 5; ++attackNum) {
                        for (int i = 0; i < enemies.size(); ++i) {
                            enemy = (Enemy) enemies.get(i);
                            if (enemy.getUnitType() == BOSS3) {
                                for (int j = 0; j < enemyShots.length; ++j) {
                                    if (enemyShots[j] == null) {
                                        boss3AutoAttack.setDelay(100);
                                        enemyShots[j] = enemy.generateShot(attackNum, -5);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    break;
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
                case KeyEvent.VK_Q:     //스킬Q
                    if (skillTime[0] == 0 && stage>0) {
                        skillTime[0] = 1000 * (int) useSkill.SkillName(playerSkill[0]);
                    }
                    break;
                case KeyEvent.VK_W:     //스킬 W
                    if (skillTime[1] == 0&& stage>0) {
                        skillTime[1] = 1000 * (int) useSkill.SkillName(playerSkill[1]);
                    }
                    break;
                case KeyEvent.VK_E:     //스킬 E
                    if (skillTime[2] == 0&& stage>0) {
                        skillTime[2] = 1000 * (int) useSkill.SkillName(playerSkill[2]);
                    }
                    break;
                case KeyEvent.VK_R:     //스킬 R
                    if (skillTime[3] == 0&& stage>0) {
                        skillTime[3] = 1000 * (int) useSkill.SkillName(playerSkill[3]);
                    }
                    break;
                case KeyEvent.VK_A:     //스킬 A
                    if (skillTime[4] == 0&& stage>0) {
                        skillTime[4] = 1000 * (int) useSkill.SkillName(playerSkill[4]);
                    }
                    break;
                case KeyEvent.VK_S:     //스킬 S
                    if (skillTime[5] == 0&& stage>0) {
                        skillTime[5] = 1000 * (int) useSkill.SkillName(playerSkill[5]);
                    }
                    break;
                case KeyEvent.VK_D:     //스킬 D
                    if (skillTime[6] == 0&& stage>0) {
                        skillTime[6] = 1000 * (int) useSkill.SkillName(playerSkill[6]);
                    }
                    break;
                case KeyEvent.VK_F:     //스킬 F
                    if (skillTime[7] == 0&& stage>0) {
                        skillTime[7] = 1000 * (int) useSkill.SkillName(playerSkill[7]);
                    }
                    break;
                case KeyEvent.VK_C:
                    useSkill.SkillName(BARRIER);
                    break;
                case KeyEvent.VK_9:
                    System.out.println("=======현재 상태 출력======");
                    System.out.println("stage : " + stage);
                    System.out.println("사이클 카운트 : " + cycleCount);
                    System.out.println("enemyCreateCount : " + enemyCreateCount);
                    System.out.println("timer.getDelay  : " + timer.getDelay());
                    System.out.println("timer2.getDelay : " + timer2.getDelay());
                    for (int i = 0; i < 8; i++) {
                        System.out.println("스킬 " + i + " 쿨타임 남은 시간 : " + skillTime[i] / 1000);
                    }

                    System.out.println("===========================");
                    break;
                case KeyEvent.VK_0:
                    superModeCount++;
                    if (superModeCount == 5) {
                        System.out.println("======================");
                        if (!superMode) //현재 슈퍼모드가 아닐 때
                        {
                            System.out.println("슈퍼모드 : ON");
                            superMode = true;
                            superModeUseCheck = true;
                            player.increaseBarrierHp(100000000);
                        } else {
                            System.out.println("슈퍼모드 : OFF");
                            superMode = false;
                            player.increaseBarrierHp(-player.getBarrierHp());
                        }
                        System.out.println("======================");
                        superModeCount = 0;
                    }
                    break;
                case KeyEvent.VK_1:
                    if (superMode) //슈퍼 모드일 떄 사용 가능
                    {
                        changeStage(1);
                    }

                    break;
                case KeyEvent.VK_2:
                    if (superMode) //슈퍼 모드일 때 사용 가능
                    {
                        changeStage(2);
                    }
                    break;
                case KeyEvent.VK_3:
                    if (superMode) //슈퍼 모드일 때 사용 가능
                    {
                        changeStage(3);
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(stage < 0){
                        changeStage();
                    }
                    else if(stage == 0){
                        stage= -1;
                    }
                    //System.out.println("현재 stage : " + stage + "  selectCharacter : " + selectCharacter);
                    //System.out.println("selectMenu : " + selectMenu);
                    //changeStage();
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
            int i;
            try {
                for (i = 0; i < maxShotNum; i++) {
                    if (shots[i] != null) {
                        // move shot
                        shots[i].moveShot(player.getShotSpeed());       //무브 샷
                        //만약 공격의 위치가 최대범위를(화면) 벗어나면 shot = null
                        if (shots[i].getX() > width || shots[i].getY() > height || shots[i].getX() < 0 || shots[i].getY() < 0) {
                            shots[i] = null;                        // remove shot from array
                        }
                    }

                    if (enemyShots[i] != null) {
                        if (enemyShots[i].getCheckBoss()) //보스라면  
                        {
                            enemyShots[i].moveShot(-shotSpeed, true);
                        } else //일반 유닛
                        {
                            enemyShots[i].moveShot(-shotSpeed);
                        }
                        //만약 enemyShot이 화면을 벗어나면 null
                        if (enemyShots[i].getX() > width || enemyShots[i].getY() > height || enemyShots[i].getX() < 0 || enemyShots[i].getY() < 0) {
                            // remove shot from array
                            enemyShots[i] = null;
                        }
                    }

                }
            } catch (NullPointerException e) {
                System.out.println("shots, enemyShots move 중 널포인터 발생");
            }

            Enemy enemy;
            for (i = 0; i < enemies.size(); ++i) {
                enemy = (Enemy) enemies.get(i);
                enemy.move();
                if (enemy.getX() < 0) //enemy가 왼쪽으로 화면을 벗어나면 remove
                {
                    enemies.remove(i);
                }
            }

            //플레이어 이동
            if (playerMoveLeft) {
                player.moveX(playerLeftSpeed);
                if (playerMoveRight) {
                    player.moveX(playerRightSpeed);
                } else if (playerMoveUp) {
                    player.moveY(playerUpSpeed);
                } else if (playerMoveDown) {
                    player.moveY(playerDownSpeed);
                }
            } else if (playerMoveRight) {
                player.moveX(playerRightSpeed);
                if (playerMoveLeft) {
                    player.moveX(playerLeftSpeed);
                } else if (playerMoveUp) {
                    player.moveY(playerUpSpeed);
                } else if (playerMoveDown) {
                    player.moveY(playerDownSpeed);
                }
            } else if (playerMoveUp) {
                player.moveY(playerUpSpeed);
            } else if (playerMoveDown) {
                player.moveY(playerDownSpeed);
            }

            repaint();

            try {
                Thread.sleep(9);
            } catch (InterruptedException ex) {
                // do nothing
            }

            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        }
    }

    public void changeStage() {
        timer.stop();   //현재 스테이지의 타이머 종료
        timer2.stop();  //현재 스테이지의 타이머 종료
        cycleCountingState = true;  //사이클 카운트 on
        cycleCount = 0;             //사이클 카운트 초기화
        stageUnitRemoveCount = 0;   //현재 스테이지 유닛 제거 수 초기화
        enemyCreateCount = 0;       //현재 스테이지에서 생성된 에너미 수 초기화
        enemyCreateCountSum += enemyCreateCount;    //총 생성된 enemy 개수에 현재 스테이지에서 생성된 enemy개수 더함
        for(int i=0; i<enemyShots.length; i++)
        {
            enemyShots[i] = null;
        }
        for(int i=0; i<shots.length; i++)
        {
            shots[i] = null;
        }
        useSkill.skillSoundClose();
        if(stage != 0 && stage > 0){
            ++stage;
            stage *= -1;
            enemies.clear();
            playerAutoAttack.stop();
        }
        else if(stage < 0){
            stage *= -1;
            playerAutoAttack.start();
        }
        
        System.out.println("스테이지는 " + stage);
        switch (stage) {
            case 1: // 시작화면 일 경우 ---> 게임 스테이지 1
                shieldTimer.start();    //자동 쉴드 증가
                timer = new javax.swing.Timer(enemyTimeGap, stage1);
                //스테이지 1 시작
                timer2 = new javax.swing.Timer(400, stage1);
                timer.start();//enemy 생성 타이머 시작
                playerAutoAttack.start();    //플레이어 자동공격 시작
                bossAttackChange.start();
                player.changeAttackType(1);
                dragoonAutoAttack.start();  //스테이지 1 유닛 타이머 시작
                zerglingAutoAttack.start();
                elite1AutoAttack.start();
                boss1AutoAttack.start();
                changeStageBGM(stage);
                break;
            case 2: //현재 스테이지가 1일 경우
                timer = new javax.swing.Timer(enemyTimeGap, stage2); //스테이지2 타이머 시작
                timer.start();
                timer2 = new javax.swing.Timer(400, stage2);
                //스테이지 2 유닛 공격 on
                stage2Enemy1AutoAttack.start();
                stage2Enemy3AutoAttack.start();
                elite2AutoAttack.start();
                boss2AutoAttack.start();
                player.changeAttackType(2);
                //공격 타입 변경
                changeStageBGM(stage);
                break;
            case 3: //현재 스테이지가 2일 경우
                timer = new javax.swing.Timer(enemyTimeGap, stage3);//스테이지 3 타이머 시작
                timer.start();
                timer2 = new javax.swing.Timer(400, stage3);
                //스테이지 3 유닛 공격 on
                stage3Enemy1AutoAttack.start();
                stage3Enemy3AutoAttack.start();
                elite3AutoAttack.start();
                boss3AutoAttack.start();
                player.changeAttackType(3);                         //공격 타입 변경
                changeStageBGM(stage);
                break;
            case 4: //현재 스테이지가 3일 경우
                gameOver();
                readGameScoreList("GameScore.txt");
                writeGameScoreList("GameScore.txt");
                //System.exit(0);
                //게임 종료, 그리고 점수 같은 화면 나타내주기, 그후 다시 메인 화면으로
                break;
        }
        //inGameUI.stage.setFont(font);
        inGameUI.stage.setText("스테이지 : " + stage);
        inGameUI.score.setText("스코어 : " + Integer.toString(score + (int) player.getHp() * 30));

        System.out.println("스테이지 변경 : " + stage);
    }

    public void changeStage(int changeStage) //스테이지 강제 변화
    {

        timer.stop();   //현재 스테이지의 타이머 종료
        timer2.stop();  //현재 스테이지의 타이머 종료
        cycleCountingState = true;  //사이클 카운트 on
        cycleCount = 0;             //사이클 카운트 초기화
        stageUnitRemoveCount = 0;   //현재 스테이지 유닛 제거 수 초기화
        enemyCreateCount = 0;       //현재 스테이지에서 생성된 에너미 수 초기화
        enemyCreateCountSum += enemyCreateCount;    //총 생성된 enemy 개수에 현재 스테이지에서 생성된 enemy개수 더함1

        System.out.println("강제 스테이지 변경 : " + stage + " -> " + changeStage);
        int prevStage = stage;
        System.out.println("이전 스테이지 : " + prevStage);
        stage = changeStage;

        switch (changeStage) {
            
            case 1: //바꾸고자 하는 스테이지가 1일 경우
                shieldTimer.start();    //자동 쉴드 증가
                timer = new javax.swing.Timer(enemyTimeGap, stage1); //스테이지2 타이머 시작
                timer.start();
                timer2 = new javax.swing.Timer(400, stage1);
                playerAutoAttack.start();    //플레이어 자동공격 시작
                bossAttackChange.start();
                dragoonAutoAttack.start();
                zerglingAutoAttack.start();
                elite1AutoAttack.start();
                boss1AutoAttack.start();
                player.changeAttackType(1);                         //공격 타입 변경

                changeStageBGM(changeStage);
                System.out.println("stage1BGM : on");
                break;
            case 2: //바꾸고자 하는 스테이지가 2일 경우
                timer = new javax.swing.Timer(enemyTimeGap, stage2); //스테이지2 타이머 시작
                timer.start();
                timer2 = new javax.swing.Timer(400, stage2);
                //스테이지 2 유닛 공격 on
                stage2Enemy1AutoAttack.start();
                stage2Enemy3AutoAttack.start();
                elite2AutoAttack.start();
                boss2AutoAttack.start();
                player.changeAttackType(2);                         //공격 타입 변경

                changeStageBGM(changeStage);
                System.out.println("stage2BGM : on");
                break;
            case 3: //바꾸고자 하는 스테이지가 3일 경우
                timer = new javax.swing.Timer(enemyTimeGap, stage3);//스테이지 3 타이머 시작
                timer.start();
                timer2 = new javax.swing.Timer(400, stage3);
                //스테이지 3 유닛 공격 ons
                stage3Enemy1AutoAttack.start();
                stage3Enemy3AutoAttack.start();
                elite3AutoAttack.start();
                boss3AutoAttack.start();
                player.changeAttackType(3);                         //공격 타입 변경
                changeStageBGM(changeStage);
                break;
        }
        inGameUI.stage.setText("스테이지 : " + stage);
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
    Image infernoImage;

    public void setInfernoImage(Image image) {
        this.infernoImage = image;
    }

    public void paintComponent(Graphics g) {
        initImage(g);
        background.BackgroundMove(g, stage);
        if(stage > 0){
            g.drawImage(player.getImage(), player.getMiddleX(), player.getMiddleY(), player.getSizeX(), player.getSizeY(), this);
        }

        //g.drawImage(, player.getMiddleX(),player.getMiddleY(), player.getSizeX(), player.getSizeY(), this);
        if (INFERNO_Check && stage > 0) //인페르노 사용 중일 때
        {
            g.drawImage(infernoImage, 0, 0, width, height, this);
        }

        try {
            for (int i = 0; i < enemies.size(); ++i) {
                Enemy enemy = (Enemy) enemies.get(i);
                g.drawImage(enemy.getImage(), enemy.getMiddleX(), enemy.getMiddleY(), enemy.getSizeX(), enemy.getSizeY(), this);//나중에 각 enemy안에 이미지를 넣어놓고 getImage와 getUnitXSize, getUnitYSize 를 이용해서 switch 문 없이 만들기

                if (enemy.getCheckElite()) //엘리트 라면 체력 바 표시
                {
                    g.setColor(Color.red);
                    int[] enemyHpBarX = {(int) enemy.getMiddleX(), (int) enemy.getMiddleX(), (int) enemy.getMiddleX() + (int) (enemy.getSizeX() * enemy.getHp() / enemy.getOriginalHp()), (int) enemy.getMiddleX() + (int) (enemy.getSizeX() * enemy.getHp() / enemy.getOriginalHp())};
                    int[] enemyHpBarY = {(int) enemy.getMiddleY() - 20, (int) enemy.getMiddleY(), (int) enemy.getMiddleY(), (int) enemy.getMiddleY() - 20};
                    //ㅁ0      ㅁ3
                    //ㅁ1      ㅁ2 배열 좌표 모양
                    g.fillPolygon(enemyHpBarX, enemyHpBarY, 4);
                }
                if(enemy.getCheckBoss())  //보스 라면 체력바 크게 표시
                {
                    g.setColor(Color.red);
                    int[] enemyHpBarX = { 100 , 100, 100+(int)(1000*(enemy.getHp()/enemy.getOriginalHp())), 100+(int)(1000*(enemy.getHp()/enemy.getOriginalHp()))};
                    int[] enemyHpBarY = { 80,  110, 110,  80};
                    //ㅁ0      ㅁ3
                    //ㅁ1      ㅁ2 배열 좌표 모양
                    g.fillPolygon(enemyHpBarX, enemyHpBarY, 4);
                }
                else
                {
                    g.setColor(Color.red);
                    int[] enemyHpBarX = {(int) enemy.getMiddleX(), (int) enemy.getMiddleX(), (int) enemy.getMiddleX() + (int) (enemy.getSizeX() * enemy.getHp() / enemy.getOriginalHp()), (int) enemy.getMiddleX() + (int) (enemy.getSizeX() * enemy.getHp() / enemy.getOriginalHp())};
                    int[] enemyHpBarY = {(int) enemy.getMiddleY() - 8, (int) enemy.getMiddleY(), (int) enemy.getMiddleY(), (int) enemy.getMiddleY() - 8};
                    //ㅁ0      ㅁ3
                    //ㅁ1      ㅁ2 배열 좌표 모양
                    g.fillPolygon(enemyHpBarX, enemyHpBarY, 4);
                }
                //enemy draw
                //enemy와 shot 충돌
                if (enemy.isCollidedWithShot(shots)) {
                    if (enemy.getHp() <= 0) {
                        ++unitRemoveCount;      //유닛 제거 수
                        ++stageUnitRemoveCount; //현재 스테이지 유닛 제거 수
                        if (enemy.getCheckElite()) //잡은 유닛이 엘리트일 경우
                        {
                            deadSound = new Sound("stage" + stage + "elitedead.mp3", false);
                            deadSound.start();
                            changeStageBGM(stage);
                            timer.setDelay(enemyTimeGap);   //타이머 시간 다시 원래대로
                            cycleCountingState = true;      //다시 사이클 카운트 시작
                        }
                        if (enemy.getCheckBoss()) //잡은 유닛이 보스일 경우
                        {
                            deadSound = new Sound("stage" + stage + "bossdead.mp3", false);
                            deadSound.start();
                            changeStageBGM(stage);
                            System.out.println(stage + " 보스 잡음");
                            System.out.println(stage + " 보스 잡음");
                            System.out.println(stage + " 보스 잡음");
                            changeStage();
                        }

                        //게임 스코어 증가, 총 게임 점수 = 사이클 카운팅이 true일 때 유닛을 죽여서 획득한 점수 + 플레이어 체력*30
                        if (cycleCountingState) //사이클 카운팅이 살아있을 때만
                        {
                            score += enemy.getPoint();  //잡은 유닛의 점수 합
                        }
                        inGameUI.score.setText("스코어 : " + Integer.toString(score + (int) player.getHp() * 30));
                        deadSound = new Sound("stage" + stage + "dead.mp3", false);
                        deadSound.start();
                        enemies.remove(i);      //유닛 제거

                        //죽는소리
                    }
                }
                //enemy 와 player 충돌
                if (enemy.isCollidedWithPlayer(player)) {
                    ++unitRemoveCount;      //유닛 제거 수
                    ++stageUnitRemoveCount; //현재 스테이지 유닛 제거 수
                    if (enemy.getCheckElite()) //잡은 유닛이 엘리트일 경우
                    {
                        deadSound = new Sound("stage" + stage + "bossdead.mp3", false);
                        deadSound.start();
                        changeStageBGM(stage);
                        timer.setDelay(enemyTimeGap);   //타이머 시간 다시 원래대로
                        cycleCountingState = true;      //다시 사이클 카운트 시작
                    }

                    if (enemy.getCheckBoss()) //잡은 유닛이 보스일 경우
                    {
                        deadSound = new Sound("stage" + stage + "bossdead.mp3", false);
                        deadSound.start();
                        changeStageBGM(stage);
                        System.out.println(stage + " 보스 잡음");
                        System.out.println(stage + " 보스 잡음");
                        System.out.println(stage + " 보스 잡음");
                        changeStage();
                    }
                    //if(cycleCountingState)    //사이클 카운팅이 살아있을 때만
                    //게임 스코어 증가, 빨리 깨면 남은 피도 고려해서 마지막에 점수 되기 때문에 유닛을 많이 잡았다고
                    //점수가 높아 지면 안됨     총 게임 점수 = 유닛을 죽여서 획득한 점수 + 피 + 시간(반비례)
                    player.decreaseHp(enemy.getCollisionDamage());   //player hp 감소
                    if (player.getHp() <= 0) //player hp가 0이하 일 때
                    {
                        System.out.println(player.getHp());
                        inGameUI.score.setText("스코어 : " + Integer.toString(score + (int) player.getHp() * 30));
                        gameOver();
                        readGameScoreList("GameScore.txt");
                        writeGameScoreList("GameScore.txt");
                        //System.exit(0);         //프로그램 종료
                    }
                    //게임 스코어 증가, 총 게임 점수 = 사이클 카운팅이 true일 때 유닛을 죽여서 획득한 점수 + 플레이어 체력*30
                    if (cycleCountingState) //사이클 카운팅이 살아있을 때만
                    {
                        score += enemy.getPoint();  //잡은 유닛의 점수 합
                    }
                    inGameUI.score.setText("스코어 : " + Integer.toString(score + (int) player.getHp() * 30));
                    deadSound = new Sound("stage" + stage + "dead.mp3", false);
                    deadSound.start();
                    enemies.remove(i);      //유닛 제거
                    //죽는소리
                }
            }

            if (player.isCollidedWithShot(enemyShots)) {
                if (player.getHp() <= 0) {
                    System.out.println("player 체력 0");
                    gameOver();
                    readGameScoreList("GameScore.txt");
                    writeGameScoreList("GameScore.txt");
                    //System.exit(0);
                }
                //System.out.println("enemy의 총알 맞음 Hp : "+player.getHp());
            }

            // draw shots
            for (int i = 0; i < maxShotNum; i++) {
                if (shots[i] != null) {
                    //shots[i].drawShot(g);
                    //g.drawImage(playerShotImage, (int)shots[i].getX()-15, (int)shots[i].getY()-15, 30, 30, this);
                    g.drawImage(shots[i].getImage(), shots[i].getMiddleX(), shots[i].getMiddleY(), shots[i].getSizeX(), shots[i].getSizeY(), this);
                }
                //Draw enemyShots
                if (enemyShots[i] != null) {
                    g.drawImage(enemyShots[i].getImage(), (int) (enemyShots[i].getX() - enemyShots[i].getSizeX() / 2), (int) (enemyShots[i].getY() - enemyShots[i].getSizeY() / 2), enemyShots[i].getSizeX(), enemyShots[i].getSizeY(), this);
                }
            }
        } catch (Exception e) {
            System.out.println("==============paintComponent==============");
            System.out.println("예외 처리 발생");
            System.out.println("현재 스테이지 : " + stage);
            System.out.println("현재 스테이지 유닛 생성 개수 : " + enemyCreateCount);
            System.out.println("총 유닛 생성 개수 : " + enemyCreateCountSum);
            System.out.println("cycleCountingState : " + cycleCountingState);
            System.out.println("==============paintComponent==============");
        }

    }

    //무기 생성 : file을 읽어서 저장된 내용을 통해 객체 생성
    public static void generateWeapon(String filename) {
        //저장 형식
        //무기이름:초당공격속도:데미지:shotImage

        String data = "";
        try {
            FileReader dataFile = new FileReader(filename);
            char[] buf = new char[1000];
            dataFile.read(buf);
            data = new String(buf);
        } catch (FileNotFoundException e) {
            System.out.println("Weapon.txt : No file exits");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IO Error");
            System.exit(2);
        }
        String s1[] = data.split("\n");
        String result[];

        String name = null;
        double attackSpeed = 0;
        double damage = 0;
        ImageIcon shotIcon = null;
        ImageIcon damageUpShotIcon = null;
        for (int i = 0; i < s1.length; i++) {
            result = s1[i].split(":");

            for (int j = 0; j < result.length; j++) {
                switch (j) {
                    case 0:
                        name = result[j];
                        break;
                    case 1:
                        attackSpeed = Double.parseDouble(result[j]);
                        break;
                    case 2:
                        damage = Double.parseDouble(result[j]);
                        break;
                    case 3:
                        shotIcon = new ImageIcon(result[j]);
                        break;
                    case 4:
                        damageUpShotIcon = new ImageIcon(result[j]);
                        break;
                }
            }
            weapon.add(new Weapon(name, attackSpeed, damage, shotIcon.getImage(), damageUpShotIcon.getImage()));
        }
        System.out.println("무기 정보 불러오기");
        Weapon tempWeapon;
        for (int i = 0; i < weapon.size(); i++) {
            tempWeapon = weapon.get(i);
            System.out.printf("무기 이름 : %s, 공격 속도 : %.2f, 데미지 : %.2f\n", tempWeapon.getName(), tempWeapon.getAttackSpeed(), tempWeapon.getDamage());
        }
    }

    public void setHpAndBarrier() {
        
        inGameUI.hp.setText("HP : " + Integer.toString((int) player.getHp()) + "/100");
        inGameUI.HpBar.setPreferredSize(new Dimension((int)(1.5 * player.getHp()),30));
        
        inGameUI.shield.setText("Shield : " + Integer.toString((int)player.getBarrierHp()) + "/25");
        if(!superMode)
            inGameUI.ShieldBar.setPreferredSize(new Dimension(6 * (int)player.getBarrierHp(),30));
        else
            inGameUI.ShieldBar.setPreferredSize(new Dimension(150,30));
        
    }
    public void setSelectCharater(int num) {
        selectCharacter = num;
        System.out.println("캐릭터 설정 : setSelectCharacter : " + selectCharacter + "       위치 shootingspaceship.java ");
        ImageIcon icon;
        ImageIcon moveIcon;
        ImageIcon barrierIcon;
        ImageIcon moveBarrierIcon;
        switch (num) {
            case 0: //남캐
                icon = new ImageIcon(".\\src\\image\\playerImage\\playerMan.png");
                moveIcon = new ImageIcon(".\\src\\image\\playerImage\\playerMan2.png");
                barrierIcon = new ImageIcon(".\\src\\image\\playerImage\\playerManShield.png");
                moveBarrierIcon = new ImageIcon(".\\src\\image\\playerImage\\playerManShield2.png");
                player.setImage(icon.getImage(), moveIcon.getImage(), barrierIcon.getImage(), moveBarrierIcon.getImage());
                break;
            case 1: //여캐
                icon = new ImageIcon(".\\src\\image\\playerImage\\playerWoman.png");
                moveIcon = new ImageIcon(".\\src\\image\\playerImage\\playerWoman2.png");
                barrierIcon = new ImageIcon(".\\src\\image\\playerImage\\playerWomanShield.png");
                moveBarrierIcon = new ImageIcon(".\\src\\image\\playerImage\\playerWomanShield2.png");
                player.setImage(icon.getImage(), moveIcon.getImage(), barrierIcon.getImage(), moveBarrierIcon.getImage());
                player.setSize(60, 120);
                playerLeftSpeed = -3; //플레이어 이동 속도
                playerRightSpeed = 3;
                playerUpSpeed = -3;
                playerDownSpeed = 3;
                break;
        }
    }

    public void setSelectWeapon(int num) {
        System.out.println("무기 설정 : setSelectWeapon        위치 shootingspaceship.java ");
        if (num < 0 || num > 3) {
            System.out.println("인트로에서 잘못된 무기 선택 발생 하였으니 수정 바람");
            System.out.println("정상적인 값은 0, 1, 2, 3");
            System.out.println("임의로 Spring 무기 장착");
            player.changeWeapon(weapon.get(0));
            return;
        }
        System.out.println("무기 넘버 : " + num);
        myWeapon = weapon.get(num);
        player.changeWeapon(myWeapon);
    }

    public void setStartStage() {
        System.out.println("setStartStage");
        System.out.println("게임 시작 : 전부 초기화");
        stage = 0;
        timer.stop();   //현재 스테이지의 타이머 종료
        timer2.stop();  //현재 스테이지의 타이머 종료
        cycleCountingState = true;  //사이클 카운트 on
        cycleCount = 0;             //사이클 카운트 초기화
        score = 0;
        stageUnitRemoveCount = 0;   //현재 스테이지 유닛 제거 수 초기화
        enemyCreateCount = 0;       //현재 스테이지에서 생성된 에너미 수 초기화
        enemyCreateCountSum = 0;    //총 생성된 enemy 개수에 현재 스테이지에서 생성된 enemy개수 더함
        skillCoolTimeThread.start();
        changeStage();
    }

    public void gameOver() {
        enemies.clear();
        for (int i = 0; i < maxShotNum; i++) {
            shots[i] = null;
            enemyShots[i] = null;
        }

        frame.dispose();
        selectedMusic.close();  //현재 진행중인 뮤직 클로즈
        intro.setVisible(true); //인트로 프레임 보여주기
        useSkill.skillSoundClose(); //사용중인 스킬 사운드 종료
        System.out.println("게임 오버 : Runnable 전부 종료");
        //runnable 전부 스탑
        //스테이지 생성 타이머 스탑
        timer.stop();
        timer2.stop();
        //자동 공격 타이머 스탑
        dragoonAutoAttack.stop();
        zerglingAutoAttack.stop();
        elite1AutoAttack.stop();
        boss1AutoAttack.stop();
        stage2Enemy1AutoAttack.stop();
        stage2Enemy3AutoAttack.stop();
        elite2AutoAttack.stop();
        boss2AutoAttack.stop();
        stage3Enemy1AutoAttack.stop();
        stage3Enemy3AutoAttack.stop();
        elite3AutoAttack.stop();
        boss3AutoAttack.stop();

        shieldTimer.stop();     //쉴드 타이머 멈춤.

        useSkill = null;
    }

    public void readGameScoreList(String fileName) {
        //양식
        //닉네임:점수
        System.out.println("파일 이름 : " + fileName + " 읽어오기");
        

        String data = "";
        try {
            FileReader dataFile = new FileReader(fileName);
            char[] buf = new char[1000];
            dataFile.read(buf);
            data = new String(buf);
        } catch (FileNotFoundException e) {
            System.out.println(fileName + " : No file exits");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IO Error");
            System.exit(2);
        }

        String s1[] = data.split("/");
        String result[];

        String usersName = null;
        int usersScore = 0;

        for (int i = 0; i < 10; i++) {
            result = s1[i].split(":");
            for (int j = 0; j < result.length; j++) {
                usersName = result[0];
                usersScore = (int) Double.parseDouble(result[1]);    //숫자 String을 더블로 인식함
            }
            //게임 스코어 읽어와서 저장
            scoreList[i] = new PlayerScoreList(usersName, (int) usersScore);
        }
        //System.out.print("플레이어 닉네임 입력(영어) : ");
        //myName = scanner.next();
        myName = JOptionPane.showInputDialog(null, "YOUR NAME?", "이름을 입력하시오", JOptionPane.OK_CANCEL_OPTION);
        
        
        if(myName == null || myName.equals(""))
            myName = "Anonymous";
        if (player.getHp() < 0) {
            player.decreaseHp(player.getHp()); //플레이어 체력 0만들기
        }
        gameScore = (int) (score + player.getHp() * 30);
        if (superModeUseCheck) //슈퍼 모드를 한번 이상 사용 했을 경우 랭킹에 포함 X     : 순서가 중요 랭킹1~10 비교 하기 전에 gameScore을 0점 처리 시켜야함
        {
            System.out.println("슈퍼모드 사용 체크 : " + superModeUseCheck);
            myRank = -1;    //언랭크
            gameScore = 0;  //0점 처리
        }
        myScore = new PlayerScoreList(myName, gameScore);
        System.out.println(myScore.getString());
        System.out.println("=============================");
        myRank = -1;

        for (int i = 0; i < 10; i++) {
            if (scoreList[i].getPlayerScore() < myScore.getPlayerScore()) //내 점수가 현재 점수판 랭크보다 클 경우
            {
                for (int j = 9; j > i; j--) {
                    scoreList[j] = scoreList[j - 1];        //뒤로 한순위 씩 밀기
                }
                scoreList[i] = myScore;     //내 이름,점수가 들어감
                myRank = i;
                break;
            }
        }

        System.out.println("=============나의 성적==============");
        System.out.println("내 이름 : " + myName);
        if (myRank != -1) {
            System.out.println("내 랭킹 : " + (myRank + 1));
        } else {
            System.out.println("내 링킹 : UNRANK");
        }
        System.out.println("최종 획득 점수 : " + gameScore);
        System.out.println("===============랭킹표===============");
        scoreBoard.setVisible(true);
        
        for (int i = 0; i < 10; i++) {
            setScoreText(i);
            System.out.printf("순위 %d 등 : %s,    %d 점\n", (i + 1), scoreList[i].getPlayerName(), scoreList[i].getPlayerScore());
        }
        setPlayerScore();
        System.out.println("===================================");
    }
    public void setPlayerScore(){
        int grade = 0;
        
        scoreBoard.playerName.setText("내 이름 : " + myName);
        scoreBoard.playerScore.setText("최종 획득 점수 : " + gameScore);
        
        if(gameScore<1000){
            scoreBoard.playerGrade.setText("학점 : F");
        }
        else if(gameScore<3000){
            scoreBoard.playerGrade.setText("학점 : C");
        }
        else if(gameScore<5500){
            scoreBoard.playerGrade.setText("학점 : C+");
        }
        else if(gameScore<9500){
            scoreBoard.playerGrade.setText("학점 : B");
        }
        else if(gameScore<12500){
            scoreBoard.playerGrade.setText("학점 : B+");
        }
        else if(gameScore<20500){
            scoreBoard.playerGrade.setText("학점 : A");
        }
        else{
            scoreBoard.playerGrade.setText("학점 : A+");
        }
        
        //scoreBoard.playerGrade.setText("AA");
        if (myRank != -1) 
            scoreBoard.playerRank.setText("내 랭킹 : " + (myRank + 1));
        else
            scoreBoard.playerRank.setText("내 링킹 : UNRANK");
    }
    public void setScoreText(int i){
        switch (i){
            case 0:
                scoreBoard.Name1.setText(scoreList[i].getPlayerName());
                scoreBoard.Score1.setText(Integer.toString(scoreList[i].getPlayerScore()));
                break;
            case 1:
                scoreBoard.Name2.setText(scoreList[i].getPlayerName());
                scoreBoard.Score2.setText(Integer.toString(scoreList[i].getPlayerScore()));
                break;
            case 2:
                scoreBoard.Name3.setText(scoreList[i].getPlayerName());
                scoreBoard.Score3.setText(Integer.toString(scoreList[i].getPlayerScore()));
                break;
            case 3:
                scoreBoard.Name4.setText(scoreList[i].getPlayerName());
                scoreBoard.Score4.setText(Integer.toString(scoreList[i].getPlayerScore()));
                break;
            case 4:
                scoreBoard.Name5.setText(scoreList[i].getPlayerName());
                scoreBoard.Score5.setText(Integer.toString(scoreList[i].getPlayerScore()));
                break;
            case 5:
                scoreBoard.Name6.setText(scoreList[i].getPlayerName());
                scoreBoard.Score6.setText(Integer.toString(scoreList[i].getPlayerScore()));
                break;
            case 6:
                scoreBoard.Name7.setText(scoreList[i].getPlayerName());
                scoreBoard.Score7.setText(Integer.toString(scoreList[i].getPlayerScore()));
                break;
            case 7:
                scoreBoard.Name8.setText(scoreList[i].getPlayerName());
                scoreBoard.Score8.setText(Integer.toString(scoreList[i].getPlayerScore()));
                break;
            case 8:
                scoreBoard.Name9.setText(scoreList[i].getPlayerName());
                scoreBoard.Score9.setText(Integer.toString(scoreList[i].getPlayerScore()));
                break;
            case 9:
                scoreBoard.Name10.setText(scoreList[i].getPlayerName());
                scoreBoard.Score10.setText(Integer.toString(scoreList[i].getPlayerScore()));
                break;
                
        }
    }


    public void writeGameScoreList(String fileName) {
        FileWriter scoreRankFile = null;
        try {
            scoreRankFile = new FileWriter(fileName);

            for (int i = 0; i < 10; i++) {
                scoreRankFile.write(scoreList[i].getString());//쓰기
            }

            scoreRankFile.close();      //저장
        } catch (IOException ex) {
            System.out.println(fileName + "저장 오류");
            System.out.println("writeGameScoreList : IOException 발생");
        }

    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Intro startIntro = new Intro();     //인트로 객채 만들면서 생성자 실행
        Intro.main(args);       //인트로 메인 함수 호출
    }
}

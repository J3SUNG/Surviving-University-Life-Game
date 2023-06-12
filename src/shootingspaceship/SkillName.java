/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship;

/**
 *
 * @author 윤재민
 */
public interface SkillName {
    //스킬 타입,       타입 값 = 스킬이름/100 과 동일
    public static final int ATTACK      = 1;
    public static final int DEFENSE     = 2;
    public static final int BUFF        = 3;
    public static final int ULTIMATE    = 5;
    //공격 스킬 100번대
    public static final int METAOR  = 100;
    public static final int SHOTGUN = 101;
    public static final int MISSILE = 102;
    public static final int FIREHALFRING = 103;

    //방어 계열 200번대
    public static final int BARRIER = 200;
    //public static final int
    
    //버프 계열 300번대
    public static final int DAMAGEUP        = 300;
    public static final int ATTACKSPEEDUP   = 301;

    //궁극기 500번대
    public static final int ULTIMATEMETAOR = 500;
    public static final int ULTIMATEINFERNO = 501;
    //public static final int ULTIMATE = 502;
    //public static final int ULTIMATE = 503;
    //public static final int ULTIMATE = 504;
}


package shootingspaceship;
import java.awt.*;
public class BackgroundMove {
    private int frame;
    public static Image bg;
    private int stage;
    
    public BackgroundMove(){
        frame = 0;
    }
   
    //가로 1024크기의 이미지를 왼쪽으로 이동
    //오른쪽 부분은 왼쪽으로 넘어간 부분을 볼 수 있게 그림
    public void BackgroundMove(Graphics g, int i){
        this.stage = i;
        if(stage == -1){        // 1스테이지 스토리
            bg = Toolkit.getDefaultToolkit().getImage("./src/image/otherImage/stage1story.png");
            g.drawImage(bg, 0, 0, null);
        }
        if(stage == -2){        // 2스00테이지 스토리
            System.out.println("stage2");
            bg = Toolkit.getDefaultToolkit().getImage("./src/image/otherImage/stage2story.png");
            g.drawImage(bg, 0, 0, null);
        }
        if(stage == -3){        // 3스테이지 스토리
            bg = Toolkit.getDefaultToolkit().getImage("./src/image/otherImage/stage3story.png");
            g.drawImage(bg, 0, 0, null);
        }
        if(stage == -4){
            bg = Toolkit.getDefaultToolkit().getImage("./src/image/otherImage/clear.png");
            g.drawImage(bg, 0, 0, null);
        }
        if(stage == 0){         //시나리오
            bg = Toolkit.getDefaultToolkit().getImage("./src/image/otherImage/mainstory.png");
            g.drawImage(bg, frame, 0, null);
        }
        if(stage == 1){
            bg = Toolkit.getDefaultToolkit().getImage("./src/image/otherImage/stage1.png");
            if( -3212 < frame ){
                g.drawImage(bg, frame, 0, null);
                frame -= 1;
                g.drawImage(bg, frame +3212, 0, null);
            }
            else{
                frame = 0;
            }
        }
        else if(stage == 2){
            bg = Toolkit.getDefaultToolkit().getImage("./src/image/otherImage/stage2.png");
            if( -3840 < frame ){
                g.drawImage(bg, frame, 0, null);
                frame -= 1;
                g.drawImage(bg, frame +3840, 0, null);
            }
            else{
                frame = 0;
            }
        }
        else if(stage == 3){
            bg = Toolkit.getDefaultToolkit().getImage("./src/image/otherImage/stage3.png");
            if( -3445 < frame ){
                g.drawImage(bg, frame, 0, null);
                frame -= 1;
                g.drawImage(bg, frame +3445, 0, null);
            }
            else{
                frame = 0;
            }
        }
        else if(stage == 4){ //클리어 화면
        }
        else if(stage == 5){ //죽음 화면
            
        }      
    }
}

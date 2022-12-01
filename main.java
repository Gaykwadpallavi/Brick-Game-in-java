import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;



public class main {

    public static void main(String[] args) {
        JFrame f=new JFrame();
        f.setTitle("Bricks Game");
        f.setSize(700,600);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setResizable(false);

        GamePlay gamePlay = new GamePlay();
        f.add(gamePlay);
    }   
}












class GamePlay extends JPanel implements ActionListener,KeyListener{
    private boolean play = false;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;
    private int score = 0;
    private int ballPasX = 120;
    private int ballPasY = 350;
    private int ballXDir = -1;
    private int ballYDir = -2;
    private int playerX = 350;
    private MapGenerator map;

    public GamePlay()
    {
       addKeyListener(this);
       setFocusable(true);
       setFocusTraversalKeysEnabled(true);
    
       timer = new Timer(delay, this);
       timer.start();
       map=new MapGenerator(3,7);
    }
    public void paint(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);
        
        g.setColor(Color.yellow);
        g.fillRect(0, 0,692, 3);
        g.fillRect(0, 3,3, 592);
        g.fillRect(691, 3,3, 592);

        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);
        
        map.draw((Graphics2D)g);

        g.setColor(Color.red);
        g.fillOval(ballPasX,ballPasY,20,20);

        g.setColor(Color.green);
        g.setFont(new Font("serif", Font.BOLD,20));
        g.drawString("Scroe : "+score, 550,30);

        if(ballPasY >=570)
        {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD,30));
            g.drawString("GameOver!! Score : "+score, 200,300);

            g.setFont(new Font("serif", Font.BOLD,25));
            g.drawString("Press Enter to Restart!!", 230, 350);
        }

        if(totalBricks <=0)
        {
            play = false;
            ballXDir = 0;
            ballYDir = 0;
            
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD,30));
            g.drawString("You Won*!! Score : "+score, 200,300);

            g.setFont(new Font("serif", Font.BOLD,25));
            g.drawString("Press Enter to Restart!!", 230, 350);
        }
    }
    

    private void moveLeft()
    {
        play=true;
        playerX -=20;
    }
    private void moveRight()
    {
        play=true;
        playerX +=20;
    }


    @Override
    public void keyPressed(KeyEvent e) {
       
        if(e.getKeyCode()==KeyEvent.VK_LEFT)
        {
            if(playerX <= 0 )
                playerX = 0;
            else
                moveLeft();
        }
        
        if(e.getKeyCode()==KeyEvent.VK_RIGHT)
        {
            if(playerX>=600)
                playerX = 600;
            else
                moveRight();
        }

        if(e.getKeyCode()==KeyEvent.VK_ENTER)
        {
            if(play)
            {
                score=0;
                totalBricks=21;
                ballPasX=120;
                ballPasY=350;
                ballXDir=-1;
                ballYDir=-2;
                playerX=320;

                map=new MapGenerator(3, 7);
            }
        }
        repaint();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(play)
        {
            if(ballPasX<=0)
            {
                ballXDir=-ballXDir;
            }
            if(ballPasX>=670)
            {
                ballXDir=-ballXDir;
            }
            if(ballPasY<=0)
            {
                ballYDir=-ballYDir;
            }
            Rectangle ballRect = new Rectangle(ballPasX,ballPasY,20,20);
            Rectangle paddleRect = new Rectangle(playerX, 550,100,8);

            if(ballRect.intersects(paddleRect))
            {
                ballYDir =-ballYDir;
            }

           A: for(int i=0;i<map.map.length;i++)
            {
                for(int j=0;j<map.map[0].length;j++)
                {
                    if(map.map[i][j]>0)
                    {
                        int width = map.brickWidth;
                        int height = map.brickHeight;
                        int brickXpos = 80+j*width;
                        int brickYpos = 50+i*height;

                        Rectangle brickRect = new Rectangle(brickXpos,brickYpos,width,height);

                        if(ballRect.intersects(brickRect))
                        {
                            map.setBrick(0,i,j);
                            totalBricks--;
                            score+=5; 

                            if(ballPasX+19<=brickXpos || ballPasX+1>=brickXpos+width)
                            {
                                ballXDir=-ballXDir;
                            }
                            else
                            {
                                ballYDir=-ballYDir;
                            }


                            break A;
                        }
                    }
                }
            }

            ballPasX+=ballXDir;
            ballPasY+=ballYDir;
        }
        repaint();
        
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    
}   










class MapGenerator 
{
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    MapGenerator (int row,int col)
    {
        map=new int[row][col];
        for(int i=0;i<row;i++) 
        {
            for(int j=0;j<col;j++)
            {
                map[i][j]=1;
            }
        }
        brickWidth = 540/col;
        brickHeight = 150/row;
    }
    public void setBrick(int value, int r, int c)
    {
        map[r][c]=value;
    }
    public void draw(Graphics2D g)
    {
        for(int i=0;i<map.length;i++)
        {
            for(int j=0;j<map[0].length;j++)
            {
                if(map[i][j]>0)
                {
                    g.setColor(Color.white);
                    g.fillRect(j*brickWidth+80, i*brickHeight+50, brickWidth, brickHeight);

                    g.setColor(Color.black);
                    g.setStroke(new BasicStroke(3));
                    g.drawRect(j*brickWidth+80, i*brickHeight+50, brickWidth, brickHeight);
                }
            }
        }
    }
}
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

public class TicTacToeGame extends JPanel implements MouseListener{
    int SCREEN_START = 1;
    int SCREEN_2DBOARD = 2;
    int SCREEN_3DBOARD = 3;
    int SCREEN_XWIN = 4;
    int SCREEN_OWIN = 5;
    int SCREEN_TIE = 6;
    
    int winningCountDown = 0;
    javax.swing.Timer timer;
    Image titleImage;
    Image backgroundImage;
    Image xImage;
    Image oImage;
    Image xImageSmall;
    Image oImageSmall;
    int screen = SCREEN_START;// 1 is title screen
    int players;
    int turn =1;  //0=nothing, 1=x, 2=o
    int gamePlayed;// 2d or 3d
    int[][] d2Board ={{0,0,0},
                      {0,0,0},
                      {0,0,0}}; 
                      
    int[][][] d3Board ={{{0,0,0},
                         {0,0,0},
                         {0,0,0}},
                         
                        {{0,0,0},
                         {0,0,0},
                         {0,0,0}},
                         
                        {{0,0,0},
                         {0,0,0},
                         {0,0,0}}}; 
    
    Polygon[][][] polyD3Board =new Polygon[3][3][3];
             
    public TicTacToeGame(){//constructor method
        addMouseListener(this);
        //add values to polygon array of arrays of arrays
        for(int i=0; i<=2; i++){//layer
            int LxOffset=(int)((253/3.0)*2)+100;// start of offset starting from very top cell, + 100 for border offset
            int LyOffset=(i*200+100);// layer and border offset down
            for(int j=0; j<=2; j++){//row- top left to bottom right
              int RxOffset= -(int)((253/3.0)*j);//
              int RyOffset= +(int)((140/3)*j);// using 3.0 for this made the bottom row overlap unevenly
                
                for(int k=0; k<=2; k++){//column
                   int xOffset =(149*k)+LxOffset+RxOffset;    //(   (700+((-700-(253*2))/3))/2  *k  );
                   int yOffset =(int)((40/3.0)*k)+LyOffset+RyOffset ;
                    polyD3Board[i][j][k]= new Polygon();
                    //                                             (base) +(lyr offset +border)
                    polyD3Board[i][j][k].addPoint(xOffset+(0),             (140/3)        +yOffset); // leftmost
                    polyD3Board[i][j][k].addPoint(xOffset+(253/3),         (0)            +yOffset); // top
                    polyD3Board[i][j][k].addPoint(xOffset+(447/3)+(253/3), (40/3)         +yOffset); // rightmost
                    polyD3Board[i][j][k].addPoint(xOffset+(447/3),         (40/3+(140/3)) +yOffset); //bottom
                }
            }
        }
      
        // load images
        try{
          titleImage=ImageIO.read(new File("Images/TitleImage.png"));
         backgroundImage= ImageIO.read(new File("Images/backgroundImage.png"));
         xImage = ImageIO.read(new File("Images/XImage.png"));
         oImage = ImageIO.read(new File("Images/OImage.png"));
         xImageSmall = xImage.getScaledInstance(110, 110, Image.SCALE_DEFAULT);
         oImageSmall = oImage.getScaledInstance(110, 110, Image.SCALE_DEFAULT);
        }catch (IOException e){}
    }
    public void paint(Graphics g){
        if (screen == SCREEN_START){// title screen
            startScreen(g);
        }else if (screen == SCREEN_2DBOARD){// main board
            drawBoard(g); 
        }else if(screen == SCREEN_3DBOARD){
            draw3DBoard(g);
        }else if (screen == SCREEN_XWIN ||screen == SCREEN_OWIN || screen == SCREEN_TIE){// 1/x win
            if (winningCountDown == 0) {
                if(gamePlayed==2)
                drawBoard(g);
                else if(gamePlayed==3)
                draw3DBoard(g);
                timer = new javax.swing.Timer(500, new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {repaint();} });
                timer.start();
            }
            
            if (winningCountDown++ < 3) {
                countdown(g, winningCountDown);                
            } else{
                timer.stop();
                    int imgSize=750;
                    g.setColor(new Color(207,226,243));
                    g.fillRect(0,0, 900, 900);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
                    g.setColor(new Color(150,0,10));
                    g.drawString("Click anywhere to restart",200,800);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 120));
                if (screen == SCREEN_XWIN){
                    g.drawString("Latte Wins!", 150,750);
                    g.drawImage(xImage.getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT), (int)((900-imgSize)/2),0, null);
                }else if (screen == SCREEN_OWIN){// 2/o win
                    g.drawString("Cashmere Wins!", 0,750);
                    g.drawImage(oImage.getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT), (int)((900-imgSize)/2),0, null);
                }else if (screen== SCREEN_TIE){ //tie
                    g.drawString("No Winner!", 100,750);
                    imgSize=460;
                    g.drawImage(oImage.getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT),0,100, null);
                    g.drawImage(xImage.getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT),440,100, null);
                }
            }
        }
    }
    public void countdown(Graphics g, int i){
        g.setColor(new Color(190,40,60));
        g.fillRect(420,542, 80, 84);
        g.setColor(new Color(150,0,10));
        g.setFont(new Font("TimesRoman", Font.PLAIN, 100)); 
        g.drawString(""+i, 430,620);
        //System.out.print(i);
    }
    public void startScreen(Graphics g){// title screen
        g.setColor(new Color(207,226,243));
        g.fillRect(0,0, 900, 900);
        
        g.drawImage(titleImage, 0, 0, null);
        //draw 1 player button
        g.setFont(new Font("TimesRoman", Font.PLAIN, 70));
        g.setColor(new Color(255,90,100));
        g.fillRect(90,450, 290, 130);
        g.setColor(new Color(150,0,10));
        
        g.drawString("1 Player", 90,570);
        
        // draw 2 player button
        g.setColor(new Color(255,90,100));
        g.fillRect(520,450, 290, 130);
        g.setColor(new Color(150,0,10));
        g.drawString("2 Player", 520,570);
        
        
        // draw 3d button
        g.setColor(new Color(255,90,100));
        g.fillRect(305,650, 290, 130);
        g.setColor(new Color(150,0,10));
        g.drawString("3D Board", 305,770);
    }
    public void drawBoard(Graphics g){//main playing board 
        //g.drawImage(backgroundImage, 0, 0, null);
        g.setColor(new Color(207,226,243));
        g.fillRect(0,0, 900, 900);
        
        g.setColor(new Color(0,0,0));
        g.fillRect(333, 100,4,700 );//233 pixel increments
        g.fillRect(566, 100,4,700 );
        g.fillRect(100, 333,700,4 );
        g.fillRect(100, 566,700,4 );
        
        for (int i=0; i<=2; i++){ //100,333,566,800  --- check y
            for (int j=0; j<=2; j++){       // --- check x
                if (d2Board[i][j]==1){
                    g.drawImage(xImage,100+(233*j),100+(233*i), null);
                }
                else if (d2Board[i][j]==2){
                    g.drawImage(oImage,100+(233*j),100+(233*i), null);
                }
            }
        }
        
       
    }
    public boolean matchDone(){
        if (screen == SCREEN_XWIN || screen == SCREEN_XWIN || screen == SCREEN_TIE)
             return true;
        return false;
    }
    public void d2checkWinner(){
       boolean allFilled= true;
       for (int i=1; i<=2; i++){ // i is the player
           for(int j=0; j<=2; j++){//loop through 0-2
                boolean rowFilled= true;
                boolean colFilled= true;
                for(int k=0; k<=2; k++){//loop through 0-2
                    if (!(d2Board[j][k]== i && rowFilled==true))
                    rowFilled=false;
                    if (!(d2Board[k][j]== i && colFilled==true))
                    colFilled=false;
                    if(d2Board[j][k] ==0 ||d2Board[k][j] ==0)
                    allFilled= false;
                }
                if (colFilled==true || rowFilled==true){screen=i+3;}
            }
        if(d2Board[0][0]==i && d2Board[1][1]==i && d2Board[2][2]==i) {
           screen=i+3;
           functionRunning = false;
        } else if(d2Board[0][2]==i && d2Board[1][1]==i && d2Board[2][0]==i) {
           screen=i+3;
           functionRunning = false;
        }
        }
       if(!(screen==SCREEN_XWIN || screen==SCREEN_OWIN)&& allFilled==true){
          screen=SCREEN_TIE;//tie
          functionRunning = false;
        }
    }
    public void draw3DBoard(Graphics g){//3d playing board 
        g.drawImage(backgroundImage, 0, 0, null);
        for (int i =0; i<=2; i++){
            for (int j =0; j<=2; j++){
                for (int k =0; k<=2; k++){
                    g.drawPolygon(polyD3Board[i][j][k]);
                    if (!(d3Board[i][j][k]==0)){
                        int LxOffset=(int)((253/3.0)*2)+100;  //reused from constructor method
                        int LyOffset=(i*200+100);
                        int RxOffset= -(int)((253/3.0)*j);
                        int RyOffset= +(int)((140/3)*j);
                        int xOffset =(149*k)+LxOffset+RxOffset;
                        int yOffset =(int)((40/3.0)*k)+LyOffset+RyOffset ;
                        if (d3Board[i][j][k]==1){// x
                            g.drawImage(xImageSmall,xOffset+60, yOffset-33, null);
                        }else if(d3Board[i][j][k]==2){//o
                            g.drawImage(oImageSmall,xOffset+60, yOffset-33, null);
                        }
                    }
                }
            }
        }

        d3checkWinner();
        repaint();
    }
    public void d3checkWinner(){
         boolean allFilled= true;
         for(int p=1; p<=2; p++){// p is the player
           for (int i=0; i<=2; i++){ // i is the layer
               for(int j=0; j<=2; j++){//loop through 0-2
                    boolean aFilled= true;// i is static the entire time,loops through "slices"
                    boolean bFilled= true;//j is each line of a slice
                    boolean cFilled= true;//k is each cell of a line
                    boolean dFilled= true; boolean eFilled= true; boolean fFilled= true;
                    for(int k=0; k<=2; k++){//loop through 0-2
                        if (!(d3Board[i][j][k]==p && aFilled==true))
                        aFilled=false; 
                        if (!(d3Board[i][k][j]== p&& bFilled==true))
                        bFilled=false; 
                        if (!(d3Board[j][i][k]== p&& cFilled==true))
                        cFilled=false; 
                        if (!(d3Board[j][k][i]== p&& dFilled==true))
                        dFilled=false; 
                        if (!(d3Board[k][i][j]== p&& eFilled==true))
                        eFilled=false;
                        if (!(d3Board[k][j][i]== p&& fFilled==true))
                        fFilled=false; 
                        if(d3Board[i][j][k]==0||d3Board[i][k][j]==0 ||d3Board[k][j][i]==0||d3Board[k][i][j]==0 ||d3Board[j][k][i]==0||d3Board[j][i][k]==0)
                        allFilled= false;
                    }
                    if (aFilled==true || bFilled==true || cFilled==true || dFilled==true || eFilled==true || fFilled==true){screen=p+3;}
                }
                //check diagonals horizontally on each layer 
               if(d3Board[i][0][0]==p && d3Board[i][1][1]==p && d3Board[i][2][2]==p) {
               screen=p+3;}
               else if(d3Board[i][0][2]==p && d3Board[i][1][1]==p && d3Board[i][2][0]==p) {
               screen=p+3;}
               boolean dia1=true; boolean dia2=true; boolean dia3=true; boolean dia4=true; boolean dia5=true; boolean dia6=true;
               boolean dia7=true; boolean dia8a=true; boolean dia8b=true; boolean dia8c=true; boolean dia8d=true;
               for(int j=0; j<=2; j++){//loop through 0-2
                    int k=2-j;
                    if(!(d3Board[i][j][j]==p&& dia1==true)) dia1=false;
                    if(!(d3Board[i][j][k]==p&& dia2==true)) dia2=false;
                    if(!(d3Board[j][i][j]==p&& dia3==true)) dia3=false;
                    if(!(d3Board[j][i][k]==p&& dia4==true)) dia4=false;
                    if(!(d3Board[j][j][i]==p&& dia5==true)) dia5=false;
                    if(!(d3Board[j][j][i]==p&& dia6==true)) dia6=false;
                    if(!(d3Board[j][k][i]==p&& dia7==true)) dia7=false;
                   
                    if(!(d3Board[j][j][j]==p&& dia8a==true))dia8a=false;
                    if(!(d3Board[j][j][k]==p&& dia8b==true))dia8b=false;
                    if(!(d3Board[j][k][j]==p&& dia8c==true))dia8c=false;
                    if(!(d3Board[k][j][j]==p&& dia8d==true))dia8d=false; 
                }
               if(dia1 || dia2|| dia3|| dia4|| dia5|| dia6|| dia7|| dia8a|| dia8b|| dia8c|| dia8d)
               screen=p+3;
            }
        }
           if(!(screen==SCREEN_XWIN || screen==SCREEN_OWIN)&& allFilled==true)
              screen=SCREEN_TIE;//tie
    
    }
    public int[] computerChoice(){
        int xTurns=0;// how many turns x has had so far
        boolean computerPlayed=false;
        for (int i=0; i<=2; i++){
            for (int j=0; j<=2; j++){
                if (d2Board[i][j]==1) xTurns++;
            }
        }
        
        if(xTurns>=2){// winning and blocking
            for (int p=2; p>=1; p--){ //p =2 first to prioritize winning over blocking
                for (int i=0; i<=2; i+=2){
                    for (int j=0; j<=2; j+=2){
                        int k=0; int m=0;// m because el looks like one
                        if(d2Board[i][j]==p){//if player in corner
                            if(i==0) k=2; else  k=0;//get coords of opposite corner
                            if(j==0) m=2; else  m=0;//
                            if(d2Board[1][1]==p &&d2Board[k][m]==0){// if player go in middle  -----------------solves diagonally if in corner and middle
                                return coords(k,m); // [k][m]// go in the opposite corner
                            }else if (d2Board[k][m]==p && d2Board[1][1]==0){// if player in opposite corner -----solves diagonally if in opposing corners
                                return coords(1,1); // [1][1]
                            }if (d2Board[j][1]==p){// if same player go in wall. top & bottom ------------------solves if in corner and wall touching it
                                if (d2Board[j][0]==p &&d2Board[j][2]==0){return coords(j,2);}          // [j][2]
                                else if (d2Board[j][2]==p &&d2Board[j][0]==0){return coords(j,0);}      //[j][0]
                            }  
                            if (d2Board[1][j]==p){// left &right
                                if (d2Board[0][j]==p &&d2Board[2][j]==0) {return coords(2,j);}           //[2][j]
                                else if (d2Board[2][j]==p &&d2Board[0][j]==0) {return coords(0,j);}      //[0][j]
                            } 
                        }
                    }
                }
                for (int i=0; i<=2; i++){
                    //   System.out.println("testing " + i + ". p= " +p);
                    if (d2Board[0][i]==p && d2Board[1][i]==0 && d2Board[2][i]==p){//rows--------------solves rows&cols of in opposing corners & sides
                        return coords(1,i);}     //[1][i]
                    if (d2Board[i][0]==p && d2Board[i][1]==0 && d2Board[i][2]==p){//columns
                        return coords(i,1);}  //[i][1]
                    if (d2Board[0][i]==0 && d2Board[1][i]==p && d2Board[2][i]==p){//rows--------------solves rows&cols of missing 1 corner
                        return coords(0,i);}     //[1][i]
                    if (d2Board[i][0]==0 && d2Board[i][1]==p && d2Board[i][2]==p){//columns
                        return coords(i,0);}  //[i][1]
                    if (d2Board[0][i]==p && d2Board[1][i]==p && d2Board[2][i]==0){//rows--------------solves rows&cols of missing other corner
                        return coords(2,i);}     //[1][i]
                    if (d2Board[i][0]==p && d2Board[i][1]==p && d2Board[i][2]==0){//columns
                        return coords(i,2);}  //[i][1]
                }
            }
            /*
            00 01 02
            10 11 12
            20 21 22
            */
           
        } else// automatically know that if 2 of the same are filled in a line, opposite player filled in as well
        
        if (xTurns==1){// if x has only gone once
            if(d2Board[1][1]==1){// if x in middle
                return coords(rand02(),rand02());//o go in corner. rand # 1/0, *2 = 2/0. it will choose 0,0| 0,2| 2,0| or 2,2 
            }else 
            if(d2Board[0][0]==1 || d2Board[0][2]==1 || d2Board[2][2]==1 || d2Board[2][0]==1){// if x went into a corner
                return coords(1,1);//[1][1]// go in middle
            }else {
                for (int i=0; i<=2; i+=2){
                    if(d2Board[1][i]==1){// 1,0 or 1,2    if x took left or right wall
                        return coords(rand02(),i);// [rand02][i]  take neighbouring corner
                    }else if(d2Board[i][1]==1){//0,1 or 2,1
                        return coords(i,rand02());// [i][rand02]
                    }
                }
            }
        }
        for (int i=0; i<=8; i++){// only let it loop 8 times, just in case
            int j= (int)(3*Math.random());
            int k= (int)(3*Math.random());
            if(d2Board[j][k]==0)
            return coords(j,k);
        }
        for(int i=0; i<=2; i++){  
            for(int j=0; j<=2; j++){  //last resort  
                if(d2Board[i][j]==0)
                    return coords(i,j);
            }
        }
        return coords(7,9);
    }           //(int)((b - a + 1) * Math.random() + a)            a is lowest bound, b is highest bound
    public int[] coords(int i, int j){
        int pair[] ={i,j}; return pair;
    }public int rand02(){
        return ((int)(2*Math.random()))*2;// math.rand gives number between 0-1, and *2 makes it 0 or 2
    }
    
    public void mouseClicked(MouseEvent e){}
    boolean functionRunning = false;
    public void mousePressed(MouseEvent e){
        boolean endScreen= false;
        
         if (screen==SCREEN_XWIN || screen==SCREEN_OWIN || screen==SCREEN_TIE){
                for (int i=0; i<=2; i++){
                    for (int j=0; j<=2; j++){
                        for (int k=0; k<=2; k++){
                            d3Board[i][j][k]=0;
                        }
                        d2Board[i][j]=0;
                    }
                }
                gamePlayed =0;
                players =0;
                turn =1;
                winningCountDown = 0;
                screen = SCREEN_START;
                repaint();
                endScreen=true;
                functionRunning = false;
        }
        if (functionRunning){
            return;// stops function from overlapping during timer
        }
        
        functionRunning = true;
        int x = e.getX();
        int y = e.getY();
        //screen 3 is 3d game, screen 2 is 2d game
        if(screen == SCREEN_START && endScreen==false){//title screen
            if (90<=x && 450<=y && x<=90+290 && y<=550+130){//1 player 2d
                screen =2; players =1; gamePlayed=2;}
            if (520<=x && 450<=y && x<=520+290 && y<=550+130){//2 player 2d
                screen =2; players =2; gamePlayed=2;}
            if (305<=x && 650<=y && x<=305+290 && y<=650+130){//2 player 3d
                screen =3; players =2; gamePlayed=3;}
        }else if (screen==SCREEN_2DBOARD){
            for (int i=0; i<=2; i++){ //100,333,566,800  --- check y
                int cellSize =233;
                if (y<800-(cellSize*(2-i)) && y>100+(cellSize*i)){
                    for (int j=0; j<=2; j++){       // --- check x
                        if(x<800-(cellSize*(2-j)) && x>100+(cellSize*j)){
                           if(d2Board[i][j]==0 ){
                                if(turn ==1){
                                    d2Board[i][j]=1;
                                    repaint();
                                    turn =2;
                                    
                                    d2checkWinner();
                                    if(matchDone()){
                                        turn=1;
                                        computerChoice();
                                        
                                        repaint();
                                        return;
                                    }
                                    
                                    if(players==1 && screen==SCREEN_2DBOARD){
                                       timer = new javax.swing.Timer(500, new ActionListener(){
                                           @Override
                                           public void actionPerformed(ActionEvent e) {
                                               int[] coords = computerChoice();
                                               d2Board[coords[0]][coords[1]]=2;
                                               repaint();
                                               turn=1;
                                               d2checkWinner();
                                               if(matchDone()){
                                                   turn=1;
                                                   repaint();
                                                   return;//exit method without reaching the end and declaring functionRunning as false during timer
                                               } else {
                                                   functionRunning = false;// 
                                               }
                                               ((Timer)e.getSource()).stop();
                                           } 
                                       });
                                       timer.start();
                                       return;
                                    }
                                }else if (turn==2 && players==2){
                                    d2Board[i][j]=2;
                                    turn =1;
                                    d2checkWinner();
                                }
                           }
                        }
                    }
                }
            }
            
                                
        }else if (screen==SCREEN_3DBOARD){//d3Board
            for (int i=0; i<=2; i++){// layer
                for (int j=0; j<=2; j++){//row          
                    for (int k=0; k<=2; k++){//collumn 
                        if(d3Board[i][j][k]==0 && polyD3Board[i][j][k].contains(x,y)){//check polygon contains
                            if (turn==1){
                                d3Board[i][j][k]=1;
                                turn=2;
                            }else{
                                d3Board[i][j][k]=2;
                                turn=1;
                            }
                        }
                    }
                }
            }
        }
        repaint();
        functionRunning = false;
    }
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    
}
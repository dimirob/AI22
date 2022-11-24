package rubcube;



import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class RubikCube implements Comparable<RubikCube>
{
    private int[][][] cube; //values 0-53 (stores initial position)

    /*THE THIRD DIMENSION OF THE CUBE INDICATES THE SIDE
     * front 0 , left 1, back 2 , right 3 , top 4 , bottom 5
     * 
     * WE START AS FOLLOWS:
     * 
     * "FRONT = RED , LEFT = GREEN , BACK = ORANGE , RIGHT = BLUE , TOP = WHITE , BOTTOM = YELLOW"
    */
     

    private double score; //the heuristic score for A*
    private RubikCube father = null; // the father state

    /*CONSTRUCTOR */

    RubikCube(int randmoves) //creates the initial rubikCube--Random State
    {
        cube=new int[6][3][3];
        int value=0;
        for(int s=0; s<6; s++)
        {
            for(int r=0; r<3; r++)
            {
                for(int c=0; c<3; c++)
                {
                    cube[s][r][c]=value;
                    value++;
                }
            }
        } //creates the cube

        randomizeCube(randmoves); //after that it randomises it 
    }

    RubikCube(int[][][] cube) //copy constructor 
    {
        this.cube = cube;
    }

    /*GETTERS-SETTERS*/

    public void setFather(RubikCube father){this.father=father;}
    public RubikCube getFather(){return this.father;}

    public void setScore(double score){this.score=score;}
    public double getScore(){return this.score;}




    /*MOVES --WE USE 10 MOVES-- */ 

    public void moveU()//moves upper side clockwise(to the left)
    /*front upper row  = right upper row 
    * left upper row = front upper row 
    * back upper row = left upper row 
    * right upper row = back upper row 
    * top face is twisted clockwise
    */
    {
       int [] row_temp = getRow(0,0); //holds the first front row 

       for(int col=0; col<3; col++)
       {
            cube[0][0][col]=cube[3][0][col];
            cube[3][0][col]=cube[2][0][col]; 
            cube[2][0][col]=cube[1][0][col];
            cube[1][0][col]=row_temp[col];

       } //twists first row clockwise

       twist_face_clockwise(4); //twists top face clockwise


    }

    public void moveUcc()//moves upper side counterclockwise(to the left)
    /*front upper row  = left upper row
     * right upper row = front upper row
     * back upper row = right upper row
     * left upper row = back upper row

     * top face is twisted  counter clockwise --
=======
     * top face is twisted clockwise

     */
    {
        int[] temp = getRow(0, 0);  //holds the first front row

        for (int col=0; col<3; col++){
            cube[0][0][col] = cube[1][0][col];
            cube[1][0][col] = cube[2][0][col];
            cube[2][0][col] = cube[3][0][col];
            cube[3][0][col] = temp[col];
        }           //twists first row countercolockwise

        twist_face_counterclockwise(4);         //twists top face counterclockwise

    }

    public void moveL()//moves left column clockwise(downwards)
    /*front left col = top left col

     *top left col = back right col reversed ---
     *back right col = bottom left col reversed ---

     *bottom left col = front left col 
     *left face is twisted clockwise
     */
    {
        int [] colf2 = getColumn(2, 2);
        int [] colf5 = getColumn(5, 0); 
        int [] colf0 = getColumn(0, 0);

        for(int row=0; row<3; row++)
        {
            cube[0][row][0] = cube[4][row][0];
            cube[4][row][0] = colf2[2-row];
            cube[2][row][2] = colf5[2-row];
            cube[5][row][0] = colf0[row];
        }

        twist_face_clockwise(1);
    }

    public void moveLcc()//moves left column counterclockwise(downwards)
    /*front left col = bottom left col

     *bottom left col =  back right col reversed ---
     *back right col = top left col reversed
     *top left col = front left col  ---
     *left face is twisted counterclockwise
    */
    {
        int[] colf2 = getColumn(2, 2);
        int[] colf4 = getColumn(4, 0);
        int[] colf0 = getColumn(0, 0);
        for (int row=0; row<3; row++){
            cube[0][row][0] = cube[5][row][0];
            cube[5][row][0] = colf2[2-row];
            cube[2][row][2] = colf4[2-row];

            cube[4][row][0] = colf0[row];
        }

        twist_face_counterclockwise(1);

    }

    public void moveR()//moves right column clockwise(upwards)

    /*front right column = bottom right column 
     *bottom right column = back left column (reversed) --
     *back left column = top right column (reversed) --

     *top right column = front right column 
     *right face twist clockwise
     */
    {
        int [] colf0 = getColumn(0, 2); 
        int [] colf2 = getColumn(2, 0);
        int [] colf4 = getColumn(4, 2);

        for(int row=0; row<3; row++)
        {
            cube[0][row][2] = cube[5][row][2];
            cube[5][row][2] = colf2[2-row];
            cube[2][row][0] = colf4[2-row];
            cube[4][row][2] = colf0[row];
        }

        twist_face_clockwise(1);
    }

    public void moveRcc()//moves right column counterclockwise(upwards)

    /*front right column = bottom right column ---
     *bottom right column = back left column (reversed)---
     *back left column = top right column (reversed)---
     *top right column = front right column ---

     *right face twist counterclockwise
     */
    {
        int [] colf0 = getColumn(0, 2);

        int [] colf2 = getColumn(2, 0);

        int [] colf4 = getColumn(4, 2);

        for (int row=0; row<3; row++)
        {

            cube[0][row][2] = cube[5][row][2];
            cube[5][row][2] = colf2[2-row];
            cube[2][row][0] = colf4[2-row];
            cube[4][row][2] = colf0[row];

        }

        twist_face_counterclockwise(3);
    }

    public void moveD()//moves down side clockwise(to the right)
    /*front down row = left down row 
     * left down row = back down row 
     * back down row = right down row 
     * right down row = front down row 
     * bottom face twist clockwise
     */
    {
        int [] row_temp = getRow(0,2);

        for(int col=0; col<3; col++)
        {
            cube[0][2][col]=cube[1][2][col];
            cube[1][2][col]=cube[2][2][col]; 
            cube[2][2][col]=cube[3][2][col];
            cube[3][2][col]=row_temp[col];

        } //twists bottom  row clockwise

       twist_face_clockwise(5); //twists bottom face clockwise
    }

    public void moveDcc()//moves down side counterclockwise(to the right)
    /*front down row = right down row
     * left down row = front down row
     * back down row = left down row
     * right down row = back down row
     * bottom face twist clockwise
     */
    {
        int [] row_temp = getRow(0,2);

        for (int col=0; col<3; col++)
        {
            cube[0][2][col] = cube[3][2][col];
            cube[3][2][col] = cube[2][2][col];
            cube[2][2][col] = cube[1][2][col];
            cube[1][2][col] = row_temp[col];
        }//twists bottom  row counterclockwise

        twist_face_counterclockwise(5); //twists bottom face counterclockwise
    }

    public void moveF()//twists front face clockwise

    /*left's right col = bottom's top row 
     *bottom's top row = right's left col (reversed)
     *right's left col = top's down row  
     *top's down row = left's right col (reversed)
     *front face twist clockwise

     */
    {
        int[] colf3 = getColumn(3, 0);
        int[] colf1 = getColumn(1, 2);

        for (int i=0; i<3; i++)
        {

            cube[1][i][2] = cube[5][0][i];
            cube[5][0][i] = colf3[2-i];
            cube[3][i][0] = cube[4][2][i];
            cube[4][2][i] = colf1[2-i];
        }//twists F side clockwise

        twist_face_clockwise(0);//twists front face clockwise
    }

    public void moveFcc()//twists front face counterclockwise
    /* left's right col = top's down row (reversed)
     * top's down row = right's left col
     * right's left col = bottom's top row (reversed)
     * bottom's top row = left's right col
     * front face twist counterclockwise
     */
    {
        int[] rowf4 = getRow(4,2);
        int[] colf3 = getColumn(3, 0);
        int[] rowf5 = getRow(5, 0);
        int[] colf1 = getColumn(1, 2);
        for (int i=0; i<3; i++)
        {
            cube[1][i][2] = rowf4[2-i];
            cube[4][2][i] = colf3[i];
            cube[3][i][0] = rowf5[2-i];

            cube[5][0][i] = colf1[2-i];
        }

        twist_face_counterclockwise(0);
    }




    /*FUNCTIONS USED FOR MOVES */
    public int[] getRow(int facevalue,int row)
    {
        int[] temp = new int[3];
        for(int c=0; c<3; c++){temp[c]=cube[facevalue][row][c];}
        return temp;
    }

    public int[] getColumn(int facevalue, int col)
    {
        int[] temp = new int[3];
        for(int r=0; r<3; r++){temp[r]=cube[facevalue][r][col];}
        return temp;
    }

    private void twist_face_clockwise(int facevalue)
    {
       int[] col0 = getColumn(facevalue, 0);
       int[] col1 = getColumn(facevalue, 1);
       int[] col2 = getColumn(facevalue, 2);
       for(int i=0; i<3; i++)
       {
        cube[facevalue][0][i] = col0[2-i];
        cube[facevalue][1][i] = col1[2-i];
        cube[facevalue][2][i] = col2[2-i];
       }
    }

    private void twist_face_counterclockwise(int facevalue)
    {

        int[] row0 = getRow(facevalue, 0);
        int[] row1 = getRow(facevalue, 1);
        int[] row2 = getRow(facevalue, 2);
        for (int i=0; i<3; i++)
        {
            cube[facevalue][i][0] = row0[2-i];
            cube[facevalue][i][1] = row1[2-i];
            cube[facevalue][i][2] = row2[2-i];

        }
    }



    /*OTHER FUNCTIONS */

    public void printCube() //prints the state of the cube
    {
        /*VISUALLY PRINTED AS

                   TOP FACE
         LEFT FACE| FRONT FACE|RIGHT FACE| BACK FACE
                   BOTTOM FACE

        */

        System.out.println("------------");
        int swap_face=0;
        for(int s=0; s<6; s++)
        {
            if(s==0){System.out.println("front");}
            else if(s==1){System.out.println("left");}
            else if(s==2){System.out.println("back");}
            else if(s==3){System.out.println("right");}
            else if(s==4){System.out.println("top");}
            else if(s==5){System.out.println("bottom");}

            for(int r=0; r<3; r++)
            {
                for(int c=0; c<3; c++)
                {
                    if(cube[s][r][c]<9)
                    {
                        System.out.print("r ");
                    }
                    else if(cube[s][r][c]<18)
                    {
                        System.out.print("g ");
                    }
                    else if(cube[s][r][c]<27)
                    {
                        System.out.print("o ");
                    }
                    else if(cube[s][r][c]<36)
                    {
                        System.out.print("b ");
                    }
                    else if(cube[s][r][c]<45)
                    {
                        System.out.print("w ");
                    }
                    else if(cube[s][r][c]<54)
                    {
                        System.out.print("y ");
                    }
                    // System.out.print(" "+cube[s][r][c]+" ");
                    if(c==2)
                    {
                        System.out.print("\n");
                        swap_face++;
                    }
                    if(swap_face==3)
                    {
                        System.out.print("\n \n");
                        swap_face=0;
                    }
                }
            }
        }
        System.out.println("------------");
    }

    public void printTemp() //to test if the cubies are moving correctly
    {
        System.out.println("------------");
        int swap_face=0;
        for(int s=0; s<6; s++)
        {
            for(int r=0; r<3; r++)
            {
                for(int c=0; c<3; c++)
                {
                    System.out.print(" "+cube[s][r][c]+" ");
                    if(c==2)
                    {
                        System.out.print("\n");
                        swap_face++;
                    }
                    if(swap_face==3)
                    {
                        System.out.print("\n \n");
                        swap_face=0;
                    }
                }
            }
        }
    }

    public void randomizeCube(int randmoves) //uses a set amount of random moves to scramble the cube 
    {
        Random r  = new Random();
        int upper = 10;
        int move;
        for(int i=0; i<randmoves; i++)
        {
            move = r.nextInt(upper); //chooses which move we are gonna use to shuffle 
            if(move==0){moveU();}
            else if(move==1){moveUcc();}
            else if(move==2){moveD();}
            else if(move==3){moveDcc();}
            else if(move==4){moveL();}
            else if(move==5){moveLcc();}
            else if(move==6){moveR();}
            else if(move==7){moveRcc();}
            else if(move==8){moveF();}
            else if(move==9){moveFcc();}
        }

    }
     
    
    /* --FOR ALGORITHM-- */
    
    public boolean checkForFinal(int sides_needed) 
    {
        int sides_solved = 0;
        int[] center_cubies = {cube[0][1][1], cube[1][1][1], cube[2][1][1],cube[3][1][1],cube[4][1][1],cube[5][1][1]}; 
        int current_center;  //red = 4 , green = 13 , orange = 22 , blue = 31  , white = 40 , yellow = 49
        boolean next_side = false;
        for (int z=0; z<6; z++)
        {
            current_center = center_cubies[z];
            for(int x=0; x<3; x++)
            {
                for(int y=0; y<3; y++)
                {    
                    if(current_center==4)
                    {
                        if(cube[z][x][y]>=9){next_side=true;}
                    }
                    else if(current_center==13)
                    {
                        if(cube[z][x][y]>=18 || cube[z][x][y]<9){next_side=true;}
                    }
                    else if(current_center==22)
                    {
                        if(cube[z][x][y]>=27 || cube[z][x][y]<18){next_side=true;}
                    }
                    else if(current_center==31)
                    {
                        if(cube[z][x][y]>=36 || cube[z][x][y]<27){next_side=true;}
                    }
                    else if(current_center==40)
                    {
                        if(cube[z][x][y]>=45 || cube[z][x][y]<36){next_side=true;}
                    }
                    else if(current_center==49)
                    {
                        if(cube[z][x][y]<45){next_side=true;}
                    }
                    if(next_side){break;}
                }
                if(next_side){break;}
            }
            if(next_side){next_side =false;}
            else{sides_solved++;}
            if(sides_solved==sides_needed || sides_solved ==5){return true;}
        } 
        return false;
    }
     
    public ArrayList<RubikCube> getCubeChildren(int heuristic, PositionHolder init_pos)
    {
        ArrayList<RubikCube> children = new ArrayList<>();
        RubikCube child = new RubikCube(this.cube);

        child.moveU();
        if(heuristic>0) child.evaluate(heuristic,init_pos);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveUcc();
        if(heuristic>0) child.evaluate(heuristic,init_pos);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveD();
        if(heuristic>0) child.evaluate(heuristic,init_pos);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveDcc();
        if(heuristic>0) child.evaluate(heuristic,init_pos);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveL();
        if(heuristic>0) child.evaluate(heuristic,init_pos);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveLcc();
        if(heuristic>0) child.evaluate(heuristic,init_pos);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveR();
        if(heuristic>0) child.evaluate(heuristic,init_pos);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveRcc();
        if(heuristic>0) child.evaluate(heuristic,init_pos);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveF();
        if(heuristic>0) child.evaluate(heuristic,init_pos);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveFcc();
        if(heuristic>0) child.evaluate(heuristic,init_pos);
        child.setFather(this);
        children.add(child);

        return children;
    }

    private void evaluate(int heuristic, PositionHolder init_pos)
    {
        if(heuristic==1){this.count3dManhattanDistance(init_pos);}
    }

    private void count3dManhattanDistance(PositionHolder init_pos) //this is the heuristic function for the solution of the cube 
    {
        int temp = 0;
        int init_x , init_y , needed_z = 0;
        int[] center_cubies = {cube[0][1][1], cube[1][1][1], cube[2][1][1],cube[3][1][1],cube[4][1][1],cube[5][1][1]};
        Arrays.sort(center_cubies);
        //red = 4 , green = 13 , orange = 22 , blue = 31  , white = 40 , yellow = 49

        for(int z = 0; z<6; z++)
        {
            for(int x = 0; x<3; x++)
            {
                for(int y = 0; y<3; y++)
                {
                    init_x = init_pos.getInitCoordsX(cube[z][x][y]);
                    init_y = init_pos.getInitCoordsY(cube[z][x][y]);
                    if(cube[z][x][y]<9){needed_z = center_cubies[0];}
                    else if(cube[z][x][y]<18){needed_z = center_cubies[1];}
                    else if(cube[z][x][y]<27){needed_z = center_cubies[2];}
                    else if(cube[z][x][y]<36){needed_z = center_cubies[3];}
                    else if(cube[z][x][y]<45){needed_z = center_cubies[4];}
                    else if(cube[z][x][y]<54){needed_z = center_cubies[5];}
                    temp += Math.abs(z-needed_z) + Math.abs(x - init_x) + Math.abs(y - init_y);
                }
            }
        }
        if(this.getFather() == null){this.score = temp / 8.0;}
        else
        {
            this.score = temp / 8.0 + this.father.getScore();
        }
    }

    @Override
    public int compareTo(RubikCube r) 
    {
        return Double.compare(this.score, r.getScore());
    }

}
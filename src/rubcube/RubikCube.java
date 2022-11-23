package rubcube;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

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
     

    private int sides_needed; //sides needed to be solved
    private double score; //the heuristic score
    private double real_cost;
    private RubikCube father; // the father state

    protected PositionHolder ph = new PositionHolder(); // used in our heuristic function

    /*CONSTRUCTOR */

    RubikCube(int randmoves, int sides_needed) //creates the initial rubikCube--Random State
    {
        cube=new int[6][3][3];
        this.sides_needed=sides_needed;
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
     *top left col = back right col reversed
     *back right col = bottom left col reversed
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
            cube[2][row][0] = colf5[2-row];
            cube[5][row][0] = colf0[row];
        }

        twist_face_clockwise(1);
    }

    public void moveLcc()//moves left column counterclockwise(downwards)
    /*front left col = bottom left col
     *top left col = front left col reversed
     *back right col = top left col reversed
     *bottom left col =  back right col
     *left face is twisted counterclockwise
    */
    {
        int[] colf2 = getColumn(2, 2);
        int[] colf5 = getColumn(5, 0);
        int[] colf0 = getColumn(0, 0);
        for (int row=0; row<3; row++){
            cube[0][row][0] = colf5[row];
            cube[2][row][2] = cube[4][2-row][0];
            cube[5][row][0] = colf2[2-row];
            cube[4][row][0] = colf0[row];
        }

        twist_face_counterclockwise(1);

    }

    public void moveR()//moves right column clockwise(upwards)
    /*front right column = bottom right column
     *bottom right column = back right column (reversed)
     *back left column = top right column (reversed)
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
            cube[2][row][2] = colf4[2-row];
            cube[4][row][2] = colf0[row];
        }

        twist_face_clockwise(1);
    }

    public void moveRcc()//moves right column counterclockwise(upwards)
    /*front right column = top right column
     *bottom right column = front right column
     *back left column = bottom right column (reversed)
     *top right column = back left column (reversed)
     *right face twist counterclockwise
     */
    {
        int [] colf0 = getColumn(0, 2);
        int [] colf2 = getColumn(2, 2);
        int [] colf4 = getColumn(4, 2);

        for (int row=0; row<3; row++)
        {
            cube[0][row][2] = colf4[row];
            cube[2][row][0] = cube[5][2-row][2];
            cube[4][row][0] = colf2[2-row];
            cube[5][row][2] = colf0[row];
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
    /*top down row = left right col (reversed)
     * right left col = top down row
     * bottom top row = right left col (reversed)
     * left right col = bottom top row
     * front face twist clockwise
     */
    {
        int[] colf3 = getColumn(3, 0);
        int[] colf1 = getColumn(1, 2);

        for (int i=0; i<3; i++)
        {
            cube[3][i][0] = cube[4][2][i];
            cube[4][2][i] = colf1[2-i];
            cube[1][i][2] = cube[5][0][i];
            cube[5][0][i] = colf3[2-i];
        }//twists F side clockwise

        twist_face_clockwise(0);        //twists front face cockwise
    }

    public void moveFcc()//twists front face counterclockwise
    /*top down row = right left col
     * left right col = top down row (reversed)
     * bottom top row = left right col
     * right left col = bottom top row (reversed)
     * front face twist counterclockwise
     */
    {
        int[] colf3 = getColumn(3, 0);
        int[] colf1 = getColumn(1, 2);

        for (int i=0; i<3; i++)
        {
            cube[1][i][2] = cube[4][2][2-i];
            cube[4][2][i] = colf3[i];
            cube[3][i][0] = cube[5][0][i];
            cube[5][0][i] = colf1[2-i];
        }

        twist_face_counterclockwise(0);
    }

    public void moveB() //twists back face clockwise
    /* top first row = right right col
     * left left col = top first row (reversed)
     * bottom down row = left left col (reversed)
     * right right col = bottom down row
     *  back face twist clockwise
     */
    {
        int[] colf3 = getColumn(3, 2);
        int[] colf1 = getColumn(1, 0);

        for (int i=0; i<3; i++)
        {
            cube[1][i][0] = cube[4][0][2-i];
            cube[4][0][i] = colf3[i];
            cube[3][i][2] = cube[5][2][i];
            cube[5][2][i] = colf1[2-i];
        }

        twist_face_clockwise(2);
    }

    public void moveBcc() //twists back face counterclockwise
        /* top first row = left left col (reversed)
         * right right col = top first row
         * bottom down row = right right col (reversed)
         * left left col = bottom down row
         * back face twist counterclockwise
         */
    {
        int[] colf3 = getColumn(3, 2);
        int[] colf1 = getColumn(1, 0);

        for (int i=0; i<3; i++)
        {
            cube[3][i][2] = cube[4][0][i];
            cube[4][0][i] = colf1[2-i];
            cube[1][i][0] = cube[5][2][i];
            cube[5][2][i] = colf3[2-i];
        }

        twist_face_counterclockwise(2);
    }

    /*SLICE MOVES*/

    public void moveM() // turns downwards the middle col of the front face
    /* front middle col = top middle col
     * bottom middle col = front middle col
     * back middle col = bottom middle col (reversed)
     * top middle col = back middle col (reversed)
     */
    {
        int[] colf5 = getColumn(5, 1);
        int[] colf2 = getColumn(2, 1);

        for (int i=0; i<3; i++)
        {
            cube[5][i][1] = cube[0][i][1];
            cube[0][i][1] = cube[4][i][1];
            cube[4][i][1] = colf2[2-i];
            cube[2][i][1] = colf5[2-i];
        }
    }

    public void moveMcc() // turns upwards the middle col of the front face
        /* front middle col = bottom middle col
         * top middle col = front middle col
         * back middle col = top middle col (reversed)
         * bottom middle col = back middle col (reversed)
         */
    {
        int[] colf5 = getColumn(5, 1);
        int[] colf2 = getColumn(2, 1);

        for (int i=0; i<3; i++)
        {
            cube[4][i][1] = cube[0][i][1];
            cube[0][i][1] = colf5[i];
            cube[5][i][1] = colf2[2-i];
            cube[2][i][1] = cube[4][2-i][1];
        }
    }


    public void moveE() // turns clockwise the middle row of the first face
    /* front middle row = left middle row
     * right middle row = front middle row
     * back middle row = right middle row
     * left middle row = back middle row
     */
    {
        int[] rowf3 = getRow(3, 1);
        int[] rowf1 = getRow(1, 1);

        for (int i=0; i<3; i++)
        {
            cube[3][1][i] = cube[0][1][i];
            cube[0][1][i] = rowf1[i];
            cube[1][1][i] = cube[2][1][i];
            cube[2][1][i] = rowf3[i];
        }
    }

    public void moveEcc() // turns counterclockwise the middle row of the first face
    /* front middle row = right middle row
     * left middle row = front middle row
     * back middle row = left middle row
     * right middle row = back middle row
     */
    {
        int[] rowf3 = getRow(3, 1);
        int[] rowf1 = getRow(1, 1);

        for (int i=0; i<3; i++)
        {
            cube[3][1][i] = cube[2][1][i];
            cube[2][1][i] = rowf1[i];
            cube[1][1][i] = cube[0][1][i];
            cube[0][1][i] = rowf3[i];
        }
    }

    public void moveS()     //turns clockwise the middle row of the top face
    /* top middle row = left middle col (reversed)
     * right middle col = top middle row
     * bottom middle row = right middle col (reversed)
     * left middle col = bottom middle row
     */
    {
        int[] colf1 = getColumn(1, 1);
        int[] colf3 = getColumn(3, 1);

        for (int i=0; i<3; i++)
        {
            cube[3][i][1] = cube[4][1][i];
            cube[4][1][i] = colf1[2-i];
            cube[1][i][1] = cube[5][1][i];
            cube[5][1][i] = colf3[2-i];
        }
    }

    public void moveScc()     //turns counterclockwise the middle row of the top face
        /* top middle row = right middle col
         * left middle col = top middle row (reversed)
         * bottom middle row = left middle col
         * right middle col = bottom middle row (reversed)
         */
    {
        int[] rowf4 = getRow(4, 1);
        int[] rowf5 = getColumn(5, 1);

        for (int i=0; i<3; i++)
        {
            cube[4][1][i] = cube[3][i][1];
            cube[3][i][1] = rowf5[2-i];
            cube[5][1][i] = cube[1][i][1];
            cube[1][i][1] = rowf4[2-i];
        }
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

    public void twist_face_clockwise(int facevalue)
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
        int[] col0 = getColumn(facevalue, 0);
        int[] col1 = getColumn(facevalue, 1);
        int[] col2 = getColumn(facevalue, 2);
        for (int i=0; i<3; i++){
            cube[facevalue][0][i] = col2[i];
            cube[facevalue][0][i] = col1[i];
            cube[facevalue][0][i] = col0[i];
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
        int upper = 18;
        int move;
        for(int i=0; i<randmoves; i++)
        {
            move = r.nextInt(upper); //chooses which move we are gonna use to shuffle 
            if(move==0) moveU();
            else if(move==1) moveUcc();
            else if(move==2) moveD();
            else if(move==3) moveDcc();
            else if(move==4) moveL();
            else if(move==5) moveLcc();
            else if(move==6) moveR();
            else if(move==7) moveRcc();
            else if(move==8) moveF();
            else if(move==9) moveFcc();
            else if (move==10) moveB();
            else if (move==11) moveBcc();
            else if (move==12) moveM();
            else if (move==13) moveMcc();
            else if (move==14) moveE();
            else if (move==15) moveEcc();
            else if (move==16) moveS();
            else if (move==17) moveScc();
        }

    }
     
    
    /* --FOR ALGORITHM-- */
    
    public boolean checkForFinal(int sides_needed)
    {
        boolean flag = true;   //to check if we have a wrong cubie in a face so we can skip its whole iteration
                                //and to help us check how many sides are complete
        int check = 0;
        for(int s=0; s<6; s++)
        {

            int prev = -1;    //initialize every time we enter a new face, stores the previous from the current value of the cube
            int curr = -1;

            for(int r=0; r<3; r++)
            {
                if (flag)       //if true check face
                {
                    for(int c=0; c<3; c++)
                    {
                        curr = cube[s][r][c];
                        if (r == 0)
                        {
                            prev = cube[s][r][c];
                            continue;
                        }
                        else
                        {
                            if ( (curr != prev+1) && (helping_conditions(prev, curr) == false) )
                            {
                                flag = false;
                                break;
                            }
                            prev = curr;
                        }
                    }
                }
                else break;
            }

            if (flag) check++;          //checks if the side is complete
            else flag = true;

        }

        if (check < sides_needed) return false;        //if it is larger than the sides needed is correct

        return true;
    }

    private boolean helping_conditions(int prev, int curr)
    {
        return ( (prev == 8 && curr == 9) || (prev == 17 && curr == 18) || (prev == 26 && curr == 27) ||
                (prev == 35 && curr == 36) || (prev == 44 && curr == 45));
    }
     
    public ArrayList<RubikCube> getCubeChildren(int heuristic)
    {
        ArrayList<RubikCube> children = new ArrayList<>();
        RubikCube child = new RubikCube(this.cube);

        child.moveU();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveUcc();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveD();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveDcc();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveL();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveLcc();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveR();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveRcc();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveF();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveFcc();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveB();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveBcc();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveM();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveMcc();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveE();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveEcc();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveS();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        child = new RubikCube(this.cube);
        child.moveScc();
        if(heuristic>0) child.evaluate(heuristic);
        child.setFather(this);
        children.add(child);

        return children;
    }

    public int compareTo(RubikCube rc)          //temporary, to find where it is used
    {
        return Double.compare(this.score, rc.score);
    }


    private void evaluate(int heuristic)
    {
        if(heuristic==1){this.heuristic1();}
    }

    private void findRealCost()         //finds the real cost of the move
    {
        this.real_cost = count3dManhattanDistance(0.0);
    }

    private void heuristic1()
        /*
       Uses the 3d Manhattan Distance function for the cubies
       For each cubie, compute the
       minimum number of moves required to correctly position and orient
       it, and sum these values over all cubies.Unfortunately, to be
       admissible, this value has to be divided by 8, since every twist
       moves 8 cubies
         */
    {
        this.score = count3dManhattanDistance(this.score)/8;
    }

    private void heiristic2()
    {
        int[] corner_cubies = new int[6];
        int[] edge_cubies = new int[6];

        int corrZ;
        int corrX;
        int corrY;
        int val;

        for (int z=0; z<6; z++)
        {

        }
    }

    private int getMax(int[] arr)   //returns the maximum value of the given array
    {
        int max = arr[0];

        for (int i=1; i<arr.length; i++)
        {
            if (arr[i] > max) max = arr[i];
        }

        return max;
    }

    private double count3dManhattanDistance(double s)
    {
        int corrZ;
        int corrX;
        int corrY;
        int val;
        double sum = s;

        for (int z=0; z<6; z++)
        {
            for (int x=0; x<3; x++)
            {
                for (int y=0; y<3; y++)
                {
                    val = cube[z][x][y];
                    corrZ = ph.getInitCoordsZ(val);
                    corrX = ph.getInitCoordsX(val);
                    corrY = ph.getInitCoordsY(val);

                    sum += (Math.abs(corrZ - z) + Math.abs(corrX - x) + Math.abs(corrY - y));
                }
            }
        }

        return sum;
    }

}
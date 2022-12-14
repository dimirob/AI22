package rubcube;
import java.util.Random;

public class RubikCube
{
    private int[][][] cube; //values 0-53 (stores initial position)
    //private int[] temp; // to store a row or column temporarily -- !!NEEDS CHANGE TO BE USED AS LOCAL MEMORY!!  



    /*THE THIRD DIMENSION OF THE CUBE INDICATES THE SIDE
     * front 0 , left 1, back 2 , right 3 , top 4 , bottom 5
     * 
     * WE START AS FOLLOWS:
     * 
     * "FRONT = RED , LEFT = GREEN , BACK = ORANGE , RIGHT = BLUE , TOP = WHITE , BOTTOM = YELLOW"
    */
     


    private double score; //the heuristic score
    private RubikCube father; // the father state

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


    /*GETTERS-SETTERS*/

    public void setFather(RubikCube father){this.father=father;}
    public RubikCube getFather(){return this.father;}

    public void setScore(double score){this.score=score;}
    public double getScore(){return this.score;}




    /*MOVES --WE USE 4 MOVES-- */ 

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
        //int[] rowf4 = getRow(4, 2);
        //int[] rowf5 = getRow(5, 1);

        for (int i=0; i<3; i++)
        {
            cube[1][i][2] = cube[4][2][2-i];
            cube[4][2][i] = colf3[i];
            cube[3][i][0] = cube[5][0][i];
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
        int upper = 4;
        int move;
        for(int i=0; i<randmoves; i++)
        {
            move = r.nextInt(upper); //chooses which move we are gonna use to shuffle 
            if(move==0){moveU();}
            else if(move==1){moveD();}
            else if(move==2){moveL();}
            else if(move==3){moveR();}
        }

    }



}
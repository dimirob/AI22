public class RubikCube
{
    private int[][][] cube; //values 0-53 (stores initial position)
    private int[] temp; // to store a row or column temporarily -- !!NEEDS CHANGE TO BE USED AS LOCAL MEMORY!!  



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

    RubikCube() //creates the initial rubikCube--Random State
    {
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

        randomizeCube(); //after that it randomises it 
    }


    /*GETTERS-SETTERS*/

    public void setFather(RubikCube father){this.father=father;}
    public RubikCube getFather(){return this.father;}

    public void setScore(double score){this.score=score;}
    public double getScore(){return this.score;}




    /*MOVES*/

    public void moveU()//moves upper side clockwise(to the left)
    /*front upper row  = right upper row 
    * left upper row = front upper row 
    * back upper row = left upper row 
    * right upper row = back upper row 
    * top face is twisted clockwise
    */
    {
       int [] row_temp = getRow(0,0);

       for(int col=0; col<3; col++)
       {
            cube[0][0][col]=cube[3][0][col];
            cube[3][0][col]=cube[2][0][col]; 
            cube[2][0][col]=cube[1][0][col];
            cube[1][0][col]=row_temp[col];

       } //twists first row clockwise

       twist_face_clockwise(4); //twists top face clockwise


    }

    public void moveL()//moves left column clockwise(downwards)
    /*front left col = top left col
     *top left col = back left col reversed
     *back left col = bottom left col reversed
     *bottom left col = front left col 
     *left face is twisted clockwise
     */
    {
        int [] col_temp = getColumn(0, 0); 

        for(int row=0; row<3; row++)
        {
            cube[0][row][0] = cube[4][row][0];
            cube[4][row][0] = cube[2][3-row][0];
            cube[2][row][0] = cube[5][3-row][0];
            cube[5][row][0] = col_temp[row];
        }

        twist_face_clockwise(1);
    }

    public void moveR()//moves right column clockwise(upwards)
    /*front right column = bottom right column
     *bottom right column = back right column (reversed)
     *back right column = top right column (reversed)
     *top right column = front right column 
     *right face twist clockwise
     */
    {
        int [] col_temp = getColumn(0, 2); 

        for(int row=0; row<3; row++)
        {
            cube[0][row][2] = cube[5][row][2];
            cube[5][row][2] = cube[2][3-row][2];
            cube[2][row][2] = cube[4][3-row][2];
            cube[4][row][2] = col_temp[row];
        }

        twist_face_clockwise(1);
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
            cube[2][2][col]=cube[4][2][col];
            cube[4][2][col]=row_temp[col];

        } //twists bottom  row clockwise

       twist_face_clockwise(5); //twists bottom face clockwise
    }


    /*FUNCTIONS USED FOR MOVES */
    public int[] getRow(int facevalue,int row)
    {
        for(int c=0; c<3; c++){temp[c]=cube[facevalue][row][c];}
        return temp;
    }

    public int[] getColumn(int facevalue, int col)
    {
        for(int r=0; r<3; r++){temp[r]=cube[facevalue][r][col];}
        return temp;
    }

    public void twist_face_clockwise(int facevalue)
    {
        int [] col2 = getColumn(4, 2);
        int [] row0 = getRow(4,0);
        for(int i=0; i<3; i++)
        {
            cube[facevalue][0][i] = cube[facevalue][3-i][0]; //row0 = col0
            cube[facevalue][i][0] = cube[facevalue][i][2]; // col0 = row2 
            cube[facevalue][i][2] = row0[i]; //col2=row0
            cube[facevalue][2][i] = col2[3-i];//row2=col2
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
    }

    public void randomizeCube() //uses a set amount of random moves to scramble the cube 
    {

    }



}
package rubcube;

public class RubikFace {

    private char[][] values = new char[3][3];                   //Facelets of this specific face
    private int id;                                             //Identifier used in twist function

    /* Constructor
     */
    public RubikFace(char color, int id){
        createface(color);
        this.id = id;
    }

    /* Fills the face with its facelets
    */
    public void createface(char color){
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                values[i][j] = color;
            }
        }
    }

    /*Returns the values of a specific row
    */
    public char[] getRow(int row){
        return values[row];
    }

    /*Sets the values of a specific row
     */
    public void setRow(int row, char[] new_v){
        this.values[row] = new_v;
    }

    /*Returns the values of a specific col
     */
    public char[] getCol(int col){
        char[] temp = new char[3];
        for (int i=0; i<3; i++){
            temp[i] = this.values[i][col];
        }
        return temp;
    }

    /*Sets the values of a specific col
     */
    public void setCol(int col, char[] new_v){
        for (int i=0; i<3; i++){
            this.values[i][col] = new_v[i];
        }
    }

    /*Returns the id of the face
     */
    public int getId(){ return this.id; }

    /*Prints all the facelets that make the face calling the function
     */
    public void printFace(){
        for (int i=0; i<3; i++){
            System.out.println("");
            for (int j=0; j<3; j++){
                System.out.print(values[i][j]);
                if (j == 3) System.out.println("");
            }
        }
        System.out.println("");
    }

}

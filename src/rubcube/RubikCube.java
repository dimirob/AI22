package rubcube;

import java.util.Arrays;
import java.util.stream.IntStream;

public class RubikCube {

    private RubikFace front;
    private RubikFace back;
    private RubikFace up;
    private RubikFace down;
    private RubikFace left;
    private RubikFace right;
    private RubikFace[] cube;

    public RubikCube(){
        front = new RubikFace('R',1);
        back = new RubikFace('O', 2);
        up = new RubikFace('W', 3);
        down = new RubikFace('Y', 4);
        left = new RubikFace('G', 5);
        right = new RubikFace('B', 6);
        createCube();
    }

    /*Method to initialize our cube
     */
    private void createCube(){
        cube = new RubikFace[] {front, back, up, down, left, right};
    }


    /*
    Twists rows using helping functions
     */
    public void twistRow(int rot, int row){
        if (rot == 1) this.twistRowClockwise(row);
        else if (rot == 2) this.twistRowCounterClockwise(row);
    }

    /*
    Twists cols using helping functions
     */
    public void twistCol(int rot, int col){
        if (rot == 1) this.twistColDownwards(col);
        else if (rot == 2) this.twistColUpwards(col);
    }

    /*
    Twists the front size
        # rot : defines the rotation of the move
     */
    public void twistSide(int rot){
        if (rot == 1) this.turnSideClockwise();
        else if(rot == 2) this.turnSideCounterClockwise();
    }

    /*
    Twists a row clockwise
        # row : defines the specific row to be changed
     */
    private void twistRowClockwise(int row){
        char[] temp1 = this.right.getRow(row);
        this.right.setRow(row, front.getRow(row));
        char[] temp2 = this.back.getRow(row);
        this.back.setRow(row, temp1);
        temp1 = this.left.getRow(row);
        this.left.setRow(row, temp2);
        this.front.setRow(row, temp1);

        if (row == 0) faceAlignment(2, up.getId());
        else if (row == 2) faceAlignment(1, down.getId());
    }

    /*
    Twists a row clockwise
        # row : defines the specific row to be changed
     */
    private void twistRowCounterClockwise(int row){
                char[] temp1 = this.left.getRow(row);
                this.left.setRow(row, front.getRow(row));
                char[] temp2 = this.back.getRow(row);
                this.back.setRow(row, temp1);
                temp1 = this.right.getRow(row);
                this.right.setRow(row, temp2);
                this.front.setRow(row, temp1);
                if (row == 0) this.faceAlignment(1, up.getId());
                else if (row == 2) this.faceAlignment(2, down.getId());
    }

    /*
    Twists a col downwards
        # col : defines the specific row to be changed
     */
    private void twistColDownwards(int col){
        switch(col){
            case 0:
                char[] temp1 = this.reverseArray(this.down.getCol(0));
                this.down.setCol(0, this.front.getCol(0));
                char[] temp2 = this.reverseArray(this.back.getCol(2));
                this.back.setCol(2, temp1);
                temp1 = this.up.getCol(0);
                this.up.setCol(0, temp2);
                this.front.setCol(0, temp1);
                this.faceAlignment(1, left.getId());
            case 2:
                char[] t1 = this.reverseArray(this.down.getCol(2));
                this.down.setCol(2, this.front.getCol(2));
                char[] t2 = this.reverseArray(this.back.getCol(0));
                this.back.setCol(0, t1);
                t1 = this.up.getCol(2);
                this.up.setCol(2, t2);
                this.front.setCol(2, t1);
                this.faceAlignment(1, right.getId());
        }
    }

    /*
    Twists a col upwards
        # col : defines the specific row to be changed
     */
    private void twistColUpwards(int col){
        switch (col){
            case 0:
                char[] temp1 = this.reverseArray(this.up.getCol(0));
                this.up.setCol(0, this.front.getCol(0));
                char[] temp2 = this.reverseArray(this.back.getCol(2));
                this.back.setCol(2, temp1);
                temp1 = this.down.getCol(0);
                this.down.setCol(0, temp2);
                this.front.setCol(0, temp1);
                this.faceAlignment(1, left.getId());
            case 2:
                char[] t1 = this.reverseArray(this.up.getCol(2));
                this.up.setCol(0, this.front.getCol(2));
                char[] t2 = this.reverseArray(this.back.getCol(0));
                this.back.setCol(2, t1);
                t1 = this.down.getCol(0);
                this.down.setCol(2, t2);
                this.front.setCol(2, t1);
                this.faceAlignment(10, right.getId());
        }
    }

    /*
    Turns front face sideways clockwise
     */
    private void turnSideClockwise(){
        char[] temp1 = this.reverseArray(this.right.getCol(0));
        this.right.setCol(0, this.up.getRow(2));
        char[] temp2 = this.down.getRow(0);
        this.down.setRow(0, temp1);
        temp1 = this.reverseArray(this.left.getCol(2));
        this.left.setCol(2, temp2);
        this.up.setRow(2, temp1);
        faceAlignment(1, front.getId());
    }

    /*
    Turns front face sideways counterclockwise
     */
    private void turnSideCounterClockwise(){
        char[] temp1 = this.left.getCol(2);
        this.left.setCol(2, this.reverseArray(this.up.getRow(2)));
        char[] temp2 = this.down.getRow(0);
        this.down.setRow(0, temp1);
        temp1 = this.right.getCol(0);
        this.right.setCol(0, this.reverseArray(temp2));
        this.up.setRow(2, temp1);
        faceAlignment(2, front.getId());
    }

    /*
    Aligns the content of the face according to the rotation
        # rot : defines the rotatin of the move
        # wanting_face_id : its the id of the face we want to allign
     */
    private void faceAlignment(int rot, int wanting_face_id){
        int face_id = findFace(wanting_face_id);
        if (rot == 1) this.faceAlClock(face_id);
        else if (rot == 2) this.faceAlCounterClock(face_id);
    }

    /*
    Aligns the content of the face when going clockwise
        # f_i : index of the wanted face in our cube array
     */
    private void faceAlClock(int f_i){
        char[] row0 = cube[f_i].getRow(0);
        char[] row1 = cube[f_i].getRow(1);
        char[] row2 = cube[f_i].getRow(2);
        cube[f_i].setCol(2, row0);
        cube[f_i].setCol(1, row1);
        cube[f_i].setCol(0, row2);
    }

    /*
    Aligns the content of the face when going counterclockwise
        # f_i : index of the wanted face in our cube array
     */
    private void faceAlCounterClock(int f_i){
        char[] row0 = cube[f_i].getRow(0);
        char[] row1 = cube[f_i].getRow(1);
        char[] row2 = cube[f_i].getRow(2);
        cube[f_i].setCol(0, row0);
        cube[f_i].setCol(1, row1);
        cube[f_i].setCol(2, row2);
    }

    /*
    Returns the index of the face we are searching in our cube array, using the id of each face
     */
    private int findFace(int id){
        int k = 1;
        for (int j=1; j<=6; j++){
            if (cube[j-1].getId() == id){
                k = j-1;
                break;
            }
        }
        return k;
    }

    /*
    Reverses the contents of an array
        # arr : the array needed to be reversed
     */
    private char[] reverseArray(char[] arr){
        for (int i=0; i<(arr.length/2); i++){
            char temp = arr[i];
            arr[i] = arr[arr.length-i-1];
            arr[arr.length-1] = temp;
        }
        return arr;
    }

    public void printCube(){
        System.out.println("---------------------------------");
        System.out.println("\nFront");
        front.printFace();
        System.out.println("\nRight");
        right.printFace();
        System.out.println("\nBack");
        back.printFace();
        System.out.println("\nLeft");
        left.printFace();
        System.out.println("\nUp");
        up.printFace();
        System.out.println("\nDown");
        down.printFace();
    }

}

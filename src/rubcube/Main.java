package rubcube;

public class Main {

    public static void main(String args[]){
        RubikCube rc = new RubikCube();
        rc.printCube();
        //System.out.println("Hello! I am a Robik's Cube Solver.") ;
        rc.twistRow(2, 0);
        rc.printCube();
        //rc.twistSide(1);
        rc.twistRow(2, 2);
        rc.printCube();
    }

}

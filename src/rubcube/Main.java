public class Main
{
    public static void main(String[] args)
    {
        //we need to create the cube and save its initial state for the heuristic search
        RubikCube Rcube = new RubikCube(20);
       Rcube.printCube();
    }
}
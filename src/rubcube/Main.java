package rubcube;

public class Main
{
    public static void main(String[] args)
    {
        //we need to create the cube and save its initial state for the heuristic search
        RubikCube Rcube = new RubikCube(20,Integer.parseInt(args[0]));
        Rcube.printCube();
    }
}
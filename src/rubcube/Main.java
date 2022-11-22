package rubcube;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        //we need to create the cube and save its initial state for the heuristic search
        RubikCube Rcube = new RubikCube(20);
        System.out.println("How many sides do we need to solve?");
        Scanner input = new Scanner(System.in);
        int sides_needed = Integer.parseInt(input.nextLine());
        SpaceSearcher search = new SpaceSearcher(sides_needed);
        Rcube.printCube();
    }
}
package rubcube;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Main
{
    public static void main(String[] args)
    {
        //we need to create the cube and save its initial state for the heuristic search
        RubikCube Rcube = new RubikCube(100);
        System.out.println("How many sides do we need to solve?");
        Scanner give_sides = new Scanner(System.in);
        int sides_needed = Integer.parseInt(give_sides.nextLine());
        SpaceSearcher search = new SpaceSearcher(sides_needed);
        //Rcube.printTemp();
        Rcube.printCube();
        long beginning = System.currentTimeMillis();
        RubikCube final_cube = search.A_Star(Rcube, 1);
        long end = System.currentTimeMillis();
        if(final_cube==null){System.out.println("Could not find a solution.");}
        else
        {
            RubikCube temp = final_cube;
            ArrayList<RubikCube> path_to_end = new ArrayList<>();
            path_to_end.add(final_cube);
            while(temp.getFather()!=null)
            {
                path_to_end.add(temp.getFather());
                temp = temp.getFather();
            }
            Collections.reverse(path_to_end);
            for(RubikCube cube_state : path_to_end)
            {
                cube_state.printCube();
            }
            System.out.println("Was calculated in : " +(end-beginning) + " seconds");
        }
    }
}
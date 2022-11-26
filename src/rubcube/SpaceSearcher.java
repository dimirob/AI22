package rubcube;

import java.util.ArrayList;
import java.util.Collections;

public class SpaceSearcher
{
    private ArrayList<RubikCube> frontier; //the frontier to be searched by the algorithm 
    private int sides_needed;

    SpaceSearcher(int sides_needed)
    {   
        this.sides_needed = sides_needed;
        this.frontier = new ArrayList<>();
    }

    public int get_sides_needed(){return sides_needed;}

    RubikCube A_Star(RubikCube initial_cube, int heuristic)
    {
        PositionHolder init_pos = new PositionHolder();

        if(initial_cube.checkForFinal(sides_needed)) return initial_cube;
        this.frontier.add(initial_cube);
        int c=0;
        int state=0;
        while(this.frontier.size() > 0)
        {
            RubikCube currentCube = this.frontier.remove(0);
            System.out.println("Current frontier "+currentCube.getScore());
            System.out.println("Current state "+currentCube.getState());
            currentCube.printCube();
            if(currentCube.checkForFinal(sides_needed)) return currentCube;
            this.frontier.addAll(currentCube.getCubeChildren(heuristic,init_pos,state));
            Collections.sort(this.frontier);
            state++;
            c++;
        }
        return null;
        //this is the class for the A*
    }



}
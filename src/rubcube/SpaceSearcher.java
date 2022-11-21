package rubcube;

import java.util.ArrayList;
import java.util.Collections;

public class SpaceSearcher
{
    private ArrayList<RubikCube> frontier; //the frontier to be searched by the algorithm 
    SpaceSearcher()
    {
        this.frontier = new ArrayList<>();
    }

    RubikCube A_Star(RubikCube initial_cube, int heuristic)
    {
        if(initial_cube.checkForFinal(sides_needed)) return initial_cube;
        this.frontier.add(initial_cube);

        while(this.frontier.size() > 0)
        {
            RubikCube currentCube = this.frontier.remove(0);
            if(currentCube.checkForFinal(sides_needed)) return currentCube;
            this.frontier.addAll(currentCube.getCubeChildren(heuristic));
            Collections.sort(this.frontier);
        }
        return null;
        //this is the class for the A*
    }

}
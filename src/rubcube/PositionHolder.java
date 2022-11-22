package rubcube;

public class PositionHolder
{ 
    //holds the initial position of the blocks
    private int[] coordsX,coordsY,coordsZ;

    PositionHolder() 
    /*default initialization --> for example to get the inital coordinates of the red square which i have numbered as 0
     * we need to coordsZ[0] coordsX[0] coordsY[0] 
     * --->this will be used in the heuristic function for the evaluation of the state
     */
    
    {
        int value =0;
        for(int z=0; z<6; z++)
        {
            for(int x=0; x<3; x++)
            {
                for(int y=0; y<3; y++)
                {
                    coordsZ[value]=z;
                    coordsX[value]=x;
                    coordsY[value]=y;
                    value++;
                }
            }
        }
    }

    public int getInitCoordsX(int value){return coordsX[value];}
    public int getInitCoordsY(int value){return coordsY[value];}
    public int getInitCoordsZ(int value){return coordsZ[value];}

}
package rubcube;

import java.util.Arrays;
import java.util.stream.IntStream;

public class RubikCube {

    private int[][][] cube = new int[6][3][3];

    public RubikCube(){
        //cube = IntStream.range(0, 6).forEach(x -> IntStream.range(0, 3).forEach(y -> Arrays.setAll(cube[x][y], z -> builder.build3dobject(x, y, z))));
    }

}

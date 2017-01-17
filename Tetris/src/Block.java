// Necessary Color import
import java.awt.Color;

// Block class containing information for Tetris Blocks
public class Block {

    // Instance variables
    private String myType;  // contains Block's type
    private Color myColor;  // contains Block's color

    // Block constructor
    public Block(String type){
        myType = type;
        myColor = assignMyColor();
    }

    // Returns the Color matrix representing the Tetris Block
    //  where black is empty space
    public Color[][] getBlock(){
        int[][] num = getNumBlock();
        Color[][] blk = new Color[num.length][num[0].length];

        for(int r = 0; r < blk.length; r++)
            for(int c = 0; c < blk[0].length; c++)
                if(num[r][c] == 1)
                    blk[r][c] = myColor;
                else
                    blk[r][c] = Color.BLACK;

        return blk;
    }

    // Returns the number matrix formatting which tells
    //  getBlock where to place the color of the block
    //  depending on the type
    public int[][] getNumBlock(){
        int[][] arr;

        if(myType.equals("LEFT_L")){
            arr = new int[][]{
                    {1,0,0},
                    {1,0,0},
                    {1,1,0}
            };
        }

        else if(myType.equals("RIGHT_L")){
            arr = new int[][]{
                    {0,0,1},
                    {0,0,1},
                    {0,1,1}
            };
        }

        else if(myType.equals("LINE")){
            arr = new int[][]{
                    {1,0,0,0},
                    {1,0,0,0},
                    {1,0,0,0},
                    {1,0,0,0}
            };
        }

        else if(myType.equals("SQUARE")){
            arr = new int[][]{
                    {1,1},
                    {1,1}
            };
        }

        else if(myType.equals("LEFT_ZIG")){
            arr = new int[][]{
                    {1,0,0},
                    {1,1,0},
                    {0,1,0}
            };
        }

        else if(myType.equals("RIGHT_ZIG")){
            arr = new int[][]{
                    {0,0,1},
                    {0,1,1},
                    {0,1,0}
            };
        }

        else{ //type = TEE
            arr = new int[][]{
                    {0,1,0},
                    {0,1,1},
                    {0,1,0}
            };
        }

        return arr;
    }

    // Returns the block's color depending on its type
    public Color assignMyColor(){
        if(myType.equals("LEFT_L"))
            return Color.ORANGE;
        if(myType.equals("RIGHT_L"))
            return Color.BLUE;
        if (myType.equals("LINE"))
            return Color.CYAN;
        if (myType.equals("SQUARE"))
            return Color.YELLOW;
        if(myType.equals("LEFT_ZIG"))
            return Color.GREEN;
        if(myType.equals("RIGHT_ZIG"))
            return Color.RED;
        else //type = TEE
            return Color.MAGENTA;
    }

}

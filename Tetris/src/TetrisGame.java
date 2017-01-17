/**
 *  Model Class - Operates the game
 */

// Color and KeyEvent imports
import java.awt.Color;
import java.awt.event.KeyEvent;

public class TetrisGame {

    // Instance variables
    private Block currBlk;                  // current falling Tetris Block
    private Color[][] piece;                // Color matrix representation of currBlk
    private int currRow;                    // current row of the Tetris Block matrix
    private int currCol;                    // current column of the Tetris Block matrix
    private int blkRow;                     // index displacement representing the bottommost row of color in the piece
    private int blkCol;                     // index displacement representing the rightmost column of color in the piece
    private boolean gameOver;               // indicator of whether the game is over or not
    private Color[][] board;                // color matrix of the board where the empty cells are black
    private String[] types = {"LEFT_L","RIGHT_L","LINE","SQUARE","LEFT_ZIG","RIGHT_ZIG","TEE"};
    private final static int HEIGHT = 20;   // number of rows in the board
    private final static int WIDTH = 16;    // number of columns in the board

    // TetrisGame constructor
    public TetrisGame(){
        board = new Color[HEIGHT][WIDTH]; //16 rows 20 cols 25 x 25 pixels
        initializeBoard();
        startBlock();
        gameOver = false;
    }

    // Standard accessor methods
    public Color[][] getBoard() { return pasteBlock(board); }
    public boolean isGameOver() { return gameOver; }

    // Sets up an empty board
    public void initializeBoard(){
        for(int row = 0; row < board.length; row++)
            for(int col = 0; col < board[0].length; col++)
                board[row][col] = Color.BLACK;
    }

    // Initializes a new Tetris block at the top of the screen and
    //  checks to see if the game is over
    public void startBlock(){
        currBlk = null; piece = null;
        currBlk = new Block(types[(int)(Math.random()*7)]);
        piece = currBlk.getBlock();
        currRow = 0;
        currCol = WIDTH/2 - 1;
        setDisplacement();
        if(!canMove("Down")){
            gameOver = true;
        }
    }

    // Executes the next time step for the falling Tetris piece
    public void next(){
        //check for downward movement
        if(canMove("Down"))
            currRow++;
        else {
            pasteBlock();
            checkRow();
            startBlock();
        }
    }

    // Executes the keyboard's command
    public void tryMove(KeyEvent e){
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_LEFT){
            if(canMove("Left"))
                currCol--;
        } else if(key == KeyEvent.VK_RIGHT){
            if(canMove("Right"))
                currCol++;
        } else if(key == KeyEvent.VK_UP){
            if(canMove("Rotate"))
                rotate();
        } else if(key == KeyEvent.VK_DOWN){
            if(canMove("Down"))
                currRow++;
        } else if(key == KeyEvent.VK_SPACE){
            while(canMove("Down"))
                currRow++;
        }
    }

    // Rotates the Tetris piece
    public void rotate(){
        Color[][] temp = new Color[piece.length][piece[0].length];
        for(int r = 0; r < piece.length; r++)
            for(int c = 0; c < piece[0].length; c++)
                temp[r][c] = piece[r][c];

        for(int r = 0; r < piece.length; r++)
            for(int c = 0; c < piece[0].length; c++)
                piece[r][c] = temp[c][r*(-1)+piece.length-1];

        setDisplacement();
    }

    // Sets BlkRow and BlkCol depending on the Tetris block's orientation
    public void setDisplacement(){
        for(int r = 0; r < piece.length; r++)
            for(int c = 0; c < piece[0].length; c++)
                if(!piece[r][c].equals(Color.BLACK))
                    blkRow = r;
        for(int c = 0; c < piece[0].length; c++)
            for(int r = 0; r < piece.length; r++)
                if(!piece[r][c].equals(Color.BLACK))
                    blkCol = c;
    }

    // Returns the indicator of whether or not the falling
    //  Tetris piece can move in the given direction
    public boolean canMove(String dir){
        if(dir.equals("Down")){
            if(blkRow+currRow == HEIGHT-1) return false;

            for(int r = 0; r < piece.length; r++)
                for(int c = 0; c < piece[0].length; c++)
                    if(r+currRow+1 < HEIGHT)
                        if(!piece[r][c].equals(Color.BLACK))
                            if(!board[r+currRow+1][c+currCol].equals(Color.BLACK))
                                return false;

            return true;

        } else if(dir.equals("Rotate")){

            Color[][] temp = new Color[piece.length][piece[0].length];
            for(int r = 0; r < piece.length; r++)
                for(int c = 0; c < piece[0].length; c++)
                    temp[r][c] = piece[c][(-1)*r+piece.length-1];

            for(int r = 0; r < temp.length; r++)
                for(int c = 0; c < temp[0].length; c++)
                    if(!temp[r][c].equals(Color.BLACK))
                        if(!onBoard(r,c))
                            return false;
            return true;

        } else if(dir.equals("Left")){

            if(leftmostCol() == 0) return false;

            for(int r = 0; r < piece.length; r++)
                for(int c = 0; c < piece[0].length; c++)
                    if(c+currRow-1 >= 0)
                        if(!piece[r][c].equals(Color.BLACK))
                            if(!board[r+currRow][c+currCol-1].equals(Color.BLACK))
                                return false;

            return true;

        } else {// right

            if(blkCol+currCol == WIDTH-1) return false;

            for(int r = 0; r < piece.length; r++)
                for(int c = 0; c < piece[0].length; c++)
                    if(c+currRow-1 < WIDTH)
                        if(!piece[r][c].equals(Color.BLACK))
                            if(!board[r+currRow][c+currCol+1].equals(Color.BLACK))
                                return false;

            return true;

        }
    }

    // Returns the indicator of whether the oriented location is within the board
    public boolean onBoard(int r, int c){
        return ((c+currCol >= 0 && c+currCol < WIDTH) && r+currRow < HEIGHT);
    }

    // Returns the leftmost column of the Tetris piece on the board
    public int leftmostCol(){
        for(int c = 0; c < piece[0].length; c++)
            for(int r = 0; r < piece.length; r++)
                if(!piece[r][c].equals(Color.BLACK))
                    return currCol+c;
        return currCol;
    }

    // Pastes the secured piece onto the matrix of the board
    public void pasteBlock(){
        for(int r = 0; r < HEIGHT; r++)
            for(int c = 0; c < WIDTH; c++)
                if(inBounds(r,c))
                    if(!piece[r-currRow][c-currCol].equals(Color.BLACK))
                        board[r][c] = piece[r-currRow][c-currCol];

    }

    // Returns and indicator of whether the (row,col) coordinates
    //  are within the falling Tetris block's coordinates or not
    public boolean inBounds(int row, int col){
        return (row >= currRow && row < (currRow+piece.length)
                    && col >= currCol && col < (currCol+piece[0].length));
    }

    // Temporarily pastes the falling block onto the board's matrix
    //  for painting purposes
    public Color[][] pasteBlock(Color[][] tempB){
        Color[][] b = new Color[board.length][board[0].length];

        for(int r = 0; r < HEIGHT; r++)
            for(int c = 0; c < WIDTH; c++)
                if(inBounds(r,c))
                    if(!piece[r-currRow][c-currCol].equals(Color.BLACK))
                        b[r][c] = piece[r-currRow][c-currCol];
                    else
                        b[r][c] = board[r][c];
                else
                    b[r][c] = board[r][c];
        return b;
    }

    // Checks to see if any rows within the fallen Tetris block's
    //  are completed and to be eliminated from the board
    public void checkRow(){
        boolean del;

        for(int r = 0; r <= blkRow; r++) {
            del = true;

            for (int c = 0; c < board[0].length; c++)
                if (board[currRow + r][c].equals(Color.BLACK))
                    del = false;

            if (del) {
                for (int row = currRow + r; row > 0; row--)
                    for (int c = 0; c < board[0].length; c++)
                        board[row][c] = board[row - 1][c];

                for (int c = 0; c < board[0].length; c++)
                    board[0][c] = Color.BLACK;
            }
        }
    }

}

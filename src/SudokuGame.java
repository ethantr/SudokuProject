import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.FontUIResource;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;

public class SudokuGame extends JFrame implements ActionListener {
    JButton button;
    JTextField[][] numBoxes = new JTextField[9][9];
    JPanel sudokuGrid = new JPanel();
    JPanel panel2 = new JPanel();
    int[][] boardTemp;
    int[][] board = new int[9][9];
    static int[][] solvedBoard = new int[9][9];

    Color lockedColour = new Color(200, 220, 230);
    Color unlockedColour = new Color(230, 250, 255);
    Color matchedColour = new Color(200, 0, 0);

    final static int GRID_SIZE = 9;

    // initialise the Sudoku game
    SudokuGame(int[][] b) {
        this.boardTemp = b;
        copyBoard(boardTemp, board);
        copyBoard(board, solvedBoard);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout());

        sudokuSolve(solvedBoard);

        buildTile();
        this.add(sudokuGrid);
        sudokuGrid.setPreferredSize(new Dimension(750, 750));

        button = new JButton("Enter");
        button.addActionListener(this);
        button.setPreferredSize(new Dimension(100, 100));

        panel2.add(button);
        panel2.setLayout(new GridLayout(2, 1));

        this.add(panel2);

        this.pack();
        this.setSize(1000, 1000);

        this.setVisible(true);
        displayBoard(solvedBoard);

    }

    // sudokusolve: AI to solve and output a sudoku board.
    private static boolean sudokuSolve(int[][] board) {

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {

                if (board[row][col] == 0) {

                    for (int numToTry = 1; numToTry <= GRID_SIZE; numToTry++) {

                        if (isValidMove(numToTry, row, col, board)) {
                            board[row][col] = numToTry;
                            System.out.println("Found valid move at " + row + "," + col);

                            // set new number and recurse solving board, until true.
                            if (sudokuSolve(board)) {
                                copyBoard(board, solvedBoard);

                                return true;
                            } else {
                                board[row][col] = 0;
                            }
                        }
                    }
                    // Backtrack as there isn't a valid move left.
                    return false;
                }
            }

        }
        return true;
    }

    // isValidMove: Checks if the move can legally be played on the board.
    private static boolean isValidMove(int num, int row, int col, int[][] board) {
        return validRow(num, row, board) && validCol(num, col, board) && validBox(num, row, col, board);
    }

    // validRow: Checks if the move can be legallly played in the selected row.
    private static boolean validRow(int num, int row, int[][] board) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }
        return true;
    }

    // validCol: Checks if the move can be legallly played in the selected column.
    private static boolean validCol(int num, int col, int[][] board) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }
        return true;
    }

    // validBox: Checks if the move can be legallly played in the selected box.
    private static boolean validBox(int num, int row, int col, int[][] board) {
        int localBoxRow = row - row % 3;
        int localBoxCol = col - col % 3;
        for (int r = localBoxRow; r < localBoxRow + 3; r++) {
            for (int c = localBoxCol; c < localBoxCol + 3; c++) {
                if (board[r][c] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    // buildTile: construct the JTextfield board
    public void buildTile() {
        sudokuGrid.setLayout(new GridLayout(9, 9));
        // for all the places on inputted board
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // if the intial board has valid number in it, lock the box
                if (board[row][col] != 0) {
                    numBoxes[row][col] = new JTextField(Integer.toString(board[row][col]));
                    numBoxes[row][col].setBackground(lockedColour);
                    numBoxes[row][col].setEditable(false);
                    // otherwise create a regular field that is blank
                } else {
                    numBoxes[row][col] = new JTextField();
                    numBoxes[row][col].setBackground(unlockedColour);
                }
                numBoxes[row][col].setFont(new FontUIResource("Keep Calm Med", Font.BOLD, 20));
                numBoxes[row][col].setHorizontalAlignment(JTextField.CENTER);
                numBoxes[row][col].setForeground(Color.black);
                // add to the panel
                sudokuGrid.add(numBoxes[row][col]);
            }
        }
    }

    // }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            // reset colours on board to clear matches
            resetColourBoard();
            // update the board array,
            updateBoard();
            // find matching tiles on board
            matchBoard(board);

            // if the board is solved//
            if (areBoardsEqual(board, solvedBoard)) {

                boardComplete();
            }

        }

    }

    // resetColourBoard: resets colours of tiles to locked or unlocked colours;
    public void resetColourBoard() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (numBoxes[row][col].isEditable()) {
                    numBoxes[row][col].setBackground(unlockedColour);
                } else {
                    numBoxes[row][col].setBackground(lockedColour);
                }
            }
        }
    }

    // boardComplete: activated when the user has solved the game.
    private void boardComplete() {
        System.out.println("Board complete");
    }

    // updateBoard: updates the board array from the inputs in the TextFrames
    public void updateBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String number = numBoxes[row][col].getText();
                // if tile is not empty and the number is between 1 & 9, update the current
                // position on board
                if (!number.isEmpty() && Integer.valueOf(number) >= 1 && Integer.valueOf(number) <= 9) {
                    board[row][col] = Integer.valueOf(number);
                    // otherwise put in placeholder as number is not valid
                } else {
                    board[row][col] = 0;
                }
            }
        }
    }

    // matchBoard: goes through board and finds matches in rows, coloums and boxs
    public void matchBoard(int[][] board) {

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int match = board[row][col];
                matchRow(match, row, board);
                matchCol(match, col, board);
                matchBox(match, row, col, board);
            }
        }
    }

    private void matchBox(int match, int row, int col, int[][] board) {
        int localBoxRow = row - row % 3;
        int localBoxCol = col - col % 3;
        int count = 0;
        for (int r = localBoxRow; r < localBoxRow + 3; r++) {
            for (int c = localBoxCol; c < localBoxCol + 3; c++) {
                if (board[r][c] == match && board[r][c] != 0) {
                    count += 1;
                }
            }
        }

        if (count >= 2) {
            for (int r = localBoxRow; r < localBoxRow + 3; r++) {
                for (int c = localBoxCol; c < localBoxCol + 3; c++) {
                    if (board[r][c] == match && board[r][c] != 0) {
                        numBoxes[row][col].setBackground(matchedColour);
                    }
                }

            }
        }

    }

    private void matchRow(int match, int row, int[][] board) {
        int count = 0;

        for (int col = 0; col < GRID_SIZE; col++) {
            if (board[row][col] == match && board[row][col] != 0 && count < 2) {
                count += 1;
            }
        }

        if (count >= 2) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == match && board[row][col] != 0) {
                    numBoxes[row][col].setBackground(matchedColour);
                }
            }

        }
    }

    private void matchCol(int match, int col, int[][] board) {
        int count = 0;

        for (int row = 0; row < GRID_SIZE; row++) {
            if (board[row][col] == match && board[row][col] != 0 && count < 2) {
                count += 1;
            }
        }

        if (count >= 2) {
            for (int row = 0; row < GRID_SIZE; row++) {
                if (board[row][col] == match && board[row][col] != 0) {
                    numBoxes[row][col].setBackground(matchedColour);
                }
            }

        }
    }

    // copyBoard: copys one sudoku startBoard to board2.
    private static void copyBoard(int[][] board1, int[][] board2) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                board2[row][col] = board1[row][col];
            }

        }

    }

    // areBoardsEqual: returns boolean value if boards match each other
    private static boolean areBoardsEqual(int[][] board1, int[][] board2) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board1[row][col] != board2[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setBoard(int[][] board) {
        this.board = board;
        setTile();
    }

    public void setTile() {
        sudokuGrid.setLayout(new GridLayout(9, 9));
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] != 0) {
                    numBoxes[r][c].setText(Integer.toString(board[r][c]));
                }
            }
        }
    }

    // displayBoard: Displays sudoku board in text form for debugging
    private static void displayBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (row != 0 && row % 3 == 0) {
                System.out.println("-----------");
            }
            for (int col = 0; col < GRID_SIZE; col++) {
                if (col != 0 && col % 3 == 0) {
                    System.out.print("|");
                }
                if (board[row][col] == 0) {
                    System.out.print(0);
                } else {
                    System.out.print(board[row][col]);
                }

            }
            System.out.println();
        }
    }
}

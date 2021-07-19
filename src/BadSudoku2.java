import java.util.Scanner;

public class BadSudoku2 {

    static Scanner scan = new Scanner(System.in);

    private static boolean validMove; // value if player wants to check for a valid move
    private static boolean guidedMove; // value if player wants to view if this move is correct in the final solution.
    private static boolean isGameComplete;
    final static int GRID_SIZE = 9;
    final static int BOARD_PLACEHOLDER = 0;

    static int[][] startBoard = { { 7, 0, 2, 0, 5, 0, 6, 0, 0 }, { 0, 0, 0, 0, 0, 3, 0, 0, 0 },
            { 1, 0, 0, 0, 0, 9, 5, 0, 0 }, { 8, 0, 0, 0, 0, 0, 0, 9, 0 }, { 0, 4, 3, 0, 0, 0, 7, 5, 0 },
            { 0, 9, 0, 0, 0, 0, 0, 0, 8 }, { 0, 0, 9, 7, 0, 0, 0, 0, 5 }, { 0, 0, 0, 2, 0, 0, 0, 0, 0 },
            { 0, 0, 7, 0, 4, 0, 2, 0, 3 } };

    static int[][] solvedBoard = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    static int[][] boardPlay = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };

    static boolean[][] lockedBoard = { { false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false },
            { false, false, false, false, false, false, false, false, false, false } };

    static SudokuGame inputFrame = new SudokuGame(startBoard);

    public static void main(String[] args) throws Exception {

        isGameComplete = false;
        validMove = true;
        guidedMove = true;
        copyBoard(startBoard, boardPlay);
        lockBoard(startBoard);

        System.out.println("Solving.... ");
        copyBoard(startBoard, solvedBoard);
        sudokuSolve(solvedBoard);
        displayBoard(solvedBoard);
        System.out.println("Complete.");

        do {
            playMove();
            isGameComplete = areBoardsEqual(boardPlay, solvedBoard);
        } while (!isGameComplete);

        System.out.println("You solved the board!");
        displayBoard(boardPlay);

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
        System.out.println("Board is complete.");
        return true;
    }

    // sudokusolve: AI to solve a sudoku board.
    private static boolean sudokuSolve(int[][] board) {

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {

                if (board[row][col] == BOARD_PLACEHOLDER) {

                    for (int numToTry = 1; numToTry <= GRID_SIZE; numToTry++) {

                        if (isValidMove(numToTry, row, col, board)) {
                            board[row][col] = numToTry;
                            System.out.println("Found valid move at " + row + "," + col);

                            // set new number and recurse solving board, until true.
                            if (sudokuSolve(board)) {
                                copyBoard(board, solvedBoard);

                                return true;
                            } else {
                                board[row][col] = BOARD_PLACEHOLDER;
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

    // Input a number between 1 - 9, and the row and column that the number will be
    // placed into. If user wants guided move, the program will return if the move
    // is valid.
    private static void playMove() {

        System.out.println("Play Next Move: ");
        displayBoard(boardPlay);

        int number = getNumber();
        int row = getRow();
        int column = getColumn();

        if (validMove) {
            System.out.println("This move can be applied? ");
            System.out.print(validRow(number, row, boardPlay));
            System.out.print(validCol(number, column, boardPlay));
            System.out.print(validBox(number, row, column, boardPlay));
            System.out.println("Therefore " + isValidMove(number, row, column, boardPlay));

        }

        if (guidedMove && isMoveInSolution(number, row, column)) {
            System.out.println("Move is correct.");
        } else if (guidedMove) {
            System.out.println("Move is not correct.");
        }

        // if chosen position does not have an existing number
        if (lockedBoard[row][column]) {
            System.out.println("This move cannot be played at (Row " + row + ", Column " + column + ") ");
        } else {
            System.out.println(
                    "Successful. You are inserting " + number + " into row " + (row + 1) + ", column " + (column + 1));
            System.out.println("Before: " + boardPlay[row][column]);
            boardPlay[row][column] = number;
            System.out.println("After: " + boardPlay[row][column]);
            inputFrame.setBoard(boardPlay);
        }

    }

    // getNumber: obtains number input.
    private static int getNumber() {
        int number;
        do {
            System.out.println("Enter a number between 1 and 9.");

            number = scan.nextInt();

            if (number >= 1 && number <= 9) {
                System.out.println("You have chosen " + number);
                break;
            }
            System.out.print("Number is not between 1 and 9. ");

        } while (true);
        return number;
    }

    // getRow: obtains row input.
    private static int getRow() {
        int row;
        // Enter row
        do {
            System.out.println("Select a row (1-9)");

            row = scan.nextInt() - 1;

            if (row >= 0 && row <= 8) {
                System.out.println("You have chosen " + (row + 1));
                break;
            }
            System.out.print("Row is not between 1 and 9. ");

        } while (true);
        return row;
    }

    // getColumn: obtains column input.
    private static int getColumn() {
        int column;
        do {
            System.out.println("Select a column (1-9)");

            column = scan.nextInt() - 1;

            if (column >= 0 && column <= 8) {
                System.out.println("You have chosen " + (column + 1));
                break;
            }
            System.out.print("Column is not between 1 and 9. ");

        } while (true);
        return column;
    }

    // isMoveInSolution: checks the number at position is the same as the one in the
    // solved board
    private static boolean isMoveInSolution(int number, int row, int column) {
        return (number == solvedBoard[row][column]);
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

    // displayBoard: Displays sudoku board in text form
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
                    System.out.print(BOARD_PLACEHOLDER);
                } else {
                    System.out.print(board[row][col]);
                }

            }
            System.out.println();
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

    // lockBoard: sets values of board to true where there are numbers currently
    // placed
    private static void lockBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] != 0) {
                    lockedBoard[row][col] = true;
                }

            }
        }
    }
    //////////////////////
    ///////////////////////////

    // isValidInt: Checks to see if response is an integer and returns inputted
    // value.
    // Otherwise, return integer outside of bounds
    private static int isValidInt(String input, String part) {
        try {
            int number = Integer.parseInt(input);
            System.out.println(input + " is a valid " + part + ".");
            return number;
        } catch (NumberFormatException e) {
            System.out.println(input + " is not a valid " + part + ".");
            return -1;
        }
    }

    public static void oldPlayMove() {
        String response;
        int number;
        int column;
        int row;
        do {
            System.out.println("Enter a number between 1 and 9.");
            response = scan.nextLine().trim();
            number = isValidInt(response, "number");

            if (number >= 1 && number <= 9) {
                System.out.println("You have chosen " + number);
                break;
            }
            System.out.print("Number is not between 1 and 9. ");

        } while (true);

        do {
            response = scan.nextLine().trim();
            number = isValidInt(response, "row");
            System.out.println("Select a row (1-9)");
            row = scan.nextInt();
            if (row >= 1 && row <= 9) {
                System.out.println("You have chosen " + row);
                break;
            }
            System.out.print("Row is not between 1 and 9. ");
        } while (true);

        do {
            response = scan.nextLine().trim();
            number = isValidInt(response, "column");
            System.out.println("Select a column (1-9)");
            column = scan.nextInt();
            if (column >= 1 && column <= 9) {
                System.out.println("You have chosen " + column);
                break;
            }
            System.out.print("Column is not between 1 and 9. ");
        } while (true);
    }

}
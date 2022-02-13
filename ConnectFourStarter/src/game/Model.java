package game;

import interfaces.IModel;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import util.GameSettings;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * This class represents the state of the game.
 * It has been partially implemented, but needs to be completed by you.
 *
 * @author <YOUR UUN>
 */
public class Model implements IModel {
    // A reference to the game settings from which you can retrieve the number
    // of rows and columns the board has and how long the win streak is.
    // 增加的私有字段
    private final static char BLANK = '_';
    private GameSettings settings;
    private int ROWS;
    private int COLS;
    private int MIN_STREAK_LENGTH;
    private char[][] board;
    private byte turn;
    private char token;
    private int row;  //当前的行
    private int col;  //当前的列
    private byte winner=0;

    // The default constructor.
    public Model() {
        // You probably won't need this.
    }

    // A constructor that takes another instance of the same type as its parameter.
    // This is called a copy constructor.
    public Model(IModel model) {
        // You may (or may not) find this useful for advanced tasks.
    }

    // Called when a new game is started on an empty board.
    public void initNewGame(GameSettings settings) {
        this.settings = settings;
        COLS = settings.nrCols;
        ROWS = settings.nrRows;
        MIN_STREAK_LENGTH = settings.minStreakLength;
        //
        turn = 0;
        winner = 0;
        //
        row = 0;
        col = 0;
        // This method still needs to be extended.
        // 渲染地图的逻辑
        int nrRows = this.settings.nrRows;
        int nrCols = this.settings.nrCols;
        board = new char[nrCols][nrRows];
        for (int width = 0; width < nrCols; width++) {
            for (int height = 0; height < nrRows; height++) {
                board[width][height] = BLANK;
            }
        }
    }

    // Called when a game state should be loaded from the given file.
    public void initSavedGame(String fileName) {
        // This is an advanced feature. If not attempting it, you can ignore this method.
        fileName = "saves" + "/" + fileName;
        File file = new File(fileName) ;
        try(Reader fr = new FileReader(file)) {
            char[] chars = new char[(int) file.length()];
            fr.read(chars);
            String content = String.valueOf(chars);
            String[] elements = content.split("\n");
            //
            int nrRows = Integer.parseInt(elements[0]);
            int nrCols = Integer.parseInt(elements[1]);
            int minStreakLength = Integer.parseInt(elements[2]);
            settings = new GameSettings(nrRows, nrCols, minStreakLength);
            COLS = settings.nrCols;
            ROWS = settings.nrRows;
            MIN_STREAK_LENGTH = settings.minStreakLength;
            row = 0;
            col = 0;
            //
            turn = (byte) (Integer.parseInt(elements[3]) - 1);  // getGameStatus: turn+1
            //
            board = new char[nrCols][nrRows];
            for (int width = 0; width < nrCols; width++) {
                for (int height = 0; height < nrRows; height++) {
                    char c_token = elements[height+4].toCharArray()[width];
                    switch(c_token)
                    {
                        case '0': c_token=BLANK; break;
                        case '1': c_token='X'; break;
                        case '2': c_token='O'; break;
                    }
                    board[width][height] = c_token;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Returns whether or not the passed in move is valid at this time.
    public boolean isMoveValid(int move) {
        // Assuming all moves are valid.
        if (move == CONCEDE_MOVE) {
            return true;
        }
        //
        if (move < COLS && move >= 0) {
            if (board[move][0] != BLANK) {
//				System.out.println("That column is full! Please select another one.");
                return false;
            }
            return true;
        } else {
            return false;
        }

    }

    // Actions the given move if it is valid. Otherwise, does nothing.
    public void makeMove(int move) {
        if (isMoveValid(move)) {
            if (move == IModel.CONCEDE_MOVE) {
                if (getActivePlayer() == 1) {
                    winner = GAME_STATUS_WIN_2;
                } else {
                    winner = GAME_STATUS_WIN_1;
                }
                return;
            }
            //
            for (int row = ROWS - 1; row >= 0; row--) {
                if (board[move][row] == BLANK) {
                    board[move][row] = token;
                    this.row = row;
                    this.col = move;
                    return;
                }
            }
        }
        // Not doing anything here yet.
    }

    private void switchUser() {
        turn++;
        if (turn % 2 == 1) {
            token = 'X';
        } else {
            token = 'O';
        }
    }

    // Returns one of the following codes to indicate the game's current status.
    // IModel.java in the "interfaces" package defines constants you can use for this.
    // 0 = Game in progress
    // 1 = Player 1 has won
    // 2 = Player 2 has won
    // 3 = Tie (board is full and there is no winner)
    // Step6
    public byte getGameStatus() {
        //有没有人输入-1
        if (winner != 0) {
            return winner;
        }
        //棋盘有没有满？
        if (this.isBoardFull()) {
            return IModel.GAME_STATUS_TIE;
        }
        //有没有胜出者？
        if (this.checkVertical() || this.checkHorizontal() || this.checkDiagonal()) {
            if (this.getActivePlayer() == 1) {
                return IModel.GAME_STATUS_WIN_1;
            } else {
                return IModel.GAME_STATUS_WIN_2;
            }
        }
        //
        this.switchUser();
        // Assuming the game is never ending.
        return IModel.GAME_STATUS_ONGOING;
    }

    private boolean isBoardFull() {
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                if (board[col][row] == BLANK) {
                    return false;
                }
            }
        }
//		System.out.println("GAME OVER: The board is full");
        return true;
    }

    private boolean checkVertical() {
        int vertical = 0;
        for (int row = 0; row < ROWS; row++) {
            if (board[this.col][row] == token) {
                vertical++;
                if (vertical >= MIN_STREAK_LENGTH) {
                    return true;
                }
            } else {
                vertical = 0;
            }
        }
        return false;
    }

    private boolean checkHorizontal() {
        int horizontal = 0;
        for (int col = 0; col < COLS; col++) {
            if (board[col][this.row] == token) {
                horizontal++;
                if (horizontal >= MIN_STREAK_LENGTH) {
                    return true;
                }
            } else {
                horizontal = 0;
            }
        }
        return false;
    }

    private boolean checkDiagonal() {
//		int diagonal = 0, antidiagonal = 0;
//		for (int extra = 0; extra < ROWS; extra++) {
//			for (int row = 0; row < ROWS; row++) {
//				//normal diagonal
//				for (int col = 0; col < COLS; col++) {
//					if (diagonal >= MIN_STREAK_LENGTH) {
//						return true;
//					} else if ((col + extra) == row && board[col][row] == token) {
//						diagonal++;
//					} else if ((row + extra) == col && board[col][row] == token) {
//						diagonal++;
//					} else if ((row + extra) == col || (col + extra) == row && board[col][row] != token) {
//						diagonal = 0;
//					}
//				}
//			}
//		}
//
//		for (int row = 0; row < ROWS; row++) {
//			for (int col = COLS - 1; col >= 0; col--) {
//				int difference = 0;
//				int metacount = 0;
//				difference = col - row;
//				metacount = Math.abs(difference);
//				if (antidiagonal >= MIN_STREAK_LENGTH) {
//					return true;
//				} else if ((metacount % 2) == 0 && board[col][row] == token) {
//					antidiagonal++;
//					row++;
//				} else if ((metacount % 2) != 0 && board[col][row] == token) {
//					antidiagonal = 0;
//				}
//			}
//		}
//		return false;


        // Check if piece won diagonally
        int diagonal = 0;
        for (int columnNum = 0; columnNum <= COLS - MIN_STREAK_LENGTH; columnNum++) {
            for (int rowNum = 0; rowNum <= ROWS - MIN_STREAK_LENGTH; rowNum++) {
                for (int i = 0; i < MIN_STREAK_LENGTH; i++) {
                    if (board[columnNum+i][rowNum+i] == token){
                        diagonal++;
                    }else {
                        diagonal=0;
                    }
//                if (board[columnNum][rowNum] == token &&
//                        board[columnNum + 1][rowNum + 1] == token &&
//                        board[columnNum + 2][rowNum + 2] == token &&
//                        board[columnNum + 3][rowNum + 3] == token) {
//                    return true;
//                }
                }
                if (diagonal >= MIN_STREAK_LENGTH) {
                    return true;
                }
            }
        }

        diagonal = 0;
        for (int columnNum = 0; columnNum <= COLS - MIN_STREAK_LENGTH; columnNum++) {
            for (int rowNum = MIN_STREAK_LENGTH-1; rowNum < ROWS; rowNum++) {
                for (int i = 0; i < MIN_STREAK_LENGTH; i++) {
                    if (board[columnNum+i][rowNum-i] == token){
                        diagonal++;
                    }else {
                        diagonal=0;
                    }
//                    if (board[columnNum][rowNum] == token &&
//                            board[columnNum + 1][rowNum - 1] == token &&
//                            board[columnNum + 2][rowNum - 2] == token &&
//                            board[columnNum + 3][rowNum - 3] == token) {
//                        return true;
//                    }
                }
                if (diagonal >= MIN_STREAK_LENGTH) {
					return true;
                }
            }
        }
        return false;
    }

    // Returns the number of the player whose turn it is.
    public byte getActivePlayer() {
        // Assuming it is always the turn of player 1.
        if (turn % 2 == 1) {
            return 1;
        } else {
            return 2;
        }
    }

    // Returns the owner of the piece in the given row and column on the board.
    // Return 1 or 2 for players 1 and 2 respectively or 0 for empty cells.
    public byte getPieceIn(int row, int column) {
        // Assuming all cells are empty for now.
        if (board[column][row] == 'X') {
            return 1;
        } else if (board[column][row] == 'O') {
            return 2;
        } else {
            return 0;
        }
    }

    // Returns a reference to the game settings, from which you can retrieve the
    // number of rows and columns the board has and how long the win streak is.
    public GameSettings getGameSettings() {
        return settings;
    }

    // =========================================================================
    // ================================ HELPERS ================================
    // =========================================================================

    // You may find it useful to define some helper methods here.
    public char[][] getBoard() {
        return board;
    }

    public char getToken() {
        return token;
    }
}

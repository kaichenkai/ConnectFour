package players;

import interfaces.IModel;
import interfaces.IPlayer;
import util.GameSettings;

import java.util.Random;

/**
 * Implementing this player is an advanced task.
 * See assignment instructions for what to do.
 * If not attempting it, just upload the file as it is.
 *
 * @author <YOUR UUN>
 */
public class WinDetectingPlayer implements IPlayer {
    // A reference to the model, which you can use to get information about
    // the state of the game. Do not use this model to make any moves!
    private IModel model;
    public byte playerId;

    // The constructor is called when the player is selected from the game menu.
    public WinDetectingPlayer() {
        // You may (or may not) need to perform some initialisation here.
    }

    // This method is called when a new game is started or loaded.
    // You can use it to perform any setup that may be required before
    // the player is asked to make a move. The second argument tells
    // you if you are playing as player 1 or player 2.
    public void prepareForGameStart(IModel model, byte playerId) {
        this.model = model;
        this.playerId = playerId;
    }

    // This method is called to ask the player to take their turn.
    // The move they choose should be returned from this method.
    public int chooseMove() {
        char[][] board = this.model.getBoard();
        GameSettings settings = this.model.getGameSettings();
        char token = this.model.getToken();
        // vertical
        int vertical = 0;
        for (int col = 0; col < settings.nrCols; col++) {
            for (int row = 0; row < settings.nrRows; row++) {
                if (board[col][row] == token) {
                    vertical++;
                    if (vertical == settings.minStreakLength - 1) {
                        return col;
                    }
                } else {
                    vertical = 0;
                }
            }
        }
        // horizontal
        int horizontal = 0;
        int blank_count = 0;
        int target_col = 0;
        for (int row = 0; row < settings.nrRows; row++) {
            for (int col = 0; col < settings.nrCols; col++) {
                if (board[col][row] == token) {
                    horizontal++;
                } else if (board[col][row] == '_') {
                    blank_count++;
                    target_col = col;
                } else {
                    horizontal = 0;
                    blank_count = 0;
                }
                //
                if (horizontal == settings.minStreakLength - 1 && blank_count == 1) {
                    return target_col;
                }
                //
                if (blank_count > 1) {
                    horizontal = 0;
                    blank_count = 0;
                }
            }
        }

        // Check if piece won diagonally
        int diagonal = 0;
        blank_count = 0;
        target_col = 0;
        for (int columnNum = 0; columnNum <= settings.nrCols - settings.minStreakLength; columnNum++) {
            for (int rowNum = 0; rowNum <= settings.nrRows - settings.minStreakLength; rowNum++) {
                for (int i = 0; i < settings.minStreakLength; i++) {
                    if (board[columnNum+i][rowNum+i] == token){
                        diagonal++;
                    }else if (board[columnNum+i][rowNum+i] == '_') {
                        blank_count++;
                        target_col = columnNum;
                    } else {
                        diagonal=0;
                        blank_count = 0;
                        break;
                    }
                }
                if (diagonal == settings.minStreakLength - 1 && blank_count == 1) {
                    return target_col;
                }
                //
                if (blank_count > 1) {
                    diagonal = 0;
                    blank_count = 0;
                }
            }
        }
        diagonal = 0;
        blank_count=0;
        target_col = 0;
        for (int columnNum = 0; columnNum <= settings.nrCols - settings.minStreakLength; columnNum++) {
            for (int rowNum = settings.minStreakLength-1; rowNum < settings.nrRows; rowNum++) {
                for (int i = 0; i < settings.minStreakLength; i++) {
                    if (board[columnNum+i][rowNum-i] == token){
                        diagonal++;
                    }else if (board[columnNum+i][rowNum-i] == '_') {
                        blank_count++;
                        target_col = columnNum;
                    } else {
                        diagonal=0;
                        blank_count=0;
                        break;
                    }
                }
                if (diagonal == settings.minStreakLength - 1 && blank_count == 1) {
                    return target_col;
                }
                //
                if (blank_count > 1) {
                    diagonal = 0;
                    blank_count = 0;
                }
            }
        }

        // Until you have implemented this player, it will always concede.
        return new Random().nextInt(this.model.getGameSettings().nrCols);
    }
}

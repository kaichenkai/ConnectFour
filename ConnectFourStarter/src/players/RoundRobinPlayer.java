package players;

import interfaces.IModel;
import interfaces.IPlayer;

/**
 * Implementing this player is an intermediate task.
 * See assignment instructions for what to do.
 * If not attempting it, just upload the file as it is.
 *
 * @author <YOUR UUN>
 */
public class RoundRobinPlayer implements IPlayer {
    // A reference to the model, which you can use to get information about
    // the state of the game. Do not use this model to make any moves!
    private IModel model;
    private byte playerId;
    private int col = IModel.CONCEDE_MOVE;

    // The constructor is called when the player is selected from the game menu.
    public RoundRobinPlayer() {
        // You can leave this empty.
    }

    // This method is called when a new game is started or loaded.
    // You can use it to perform any setup that may be required before
    // the player is asked to make a move. The second argument tells
    // you if you are playing as player 1 or player 2.
    public void prepareForGameStart(IModel model, byte playerId) {
        this.model = model;

        // Extend this method if required.
        this.playerId = playerId;
        this.col = IModel.CONCEDE_MOVE;
    }

    // This method is called to ask the player to take their turn.
    // The move they choose should be returned from this method.
    public int chooseMove() {
        col++;
        if (col >= model.getGameSettings().nrCols) {
            col = 0;
        }
        while (true) {
            if (this.model.getBoard()[col][0] != '_') {
                col++;
            } else {
                return col;
            }
        }
        // Until you have implemented this player, it will always concede.
//		return IModel.CONCEDE_MOVE;
    }
}

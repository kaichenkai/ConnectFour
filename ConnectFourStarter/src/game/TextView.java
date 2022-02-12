package game;

import interfaces.*;
import players.*;
import util.GameSettings;
import util.InputUtil;

/**
 * This class is used to interact with the user.
 * It has been partially implemented, but needs to be completed by you.
 *
 * @author <YOUR UUN>
 */
public class TextView implements IView
{
	public void displayWelcomeMessage()
	{
		System.out.println("Welcome to Connect Four!");
	}
	
	public void displayChosenMove(int move)
	{
		System.out.println("Selected move: " + move);
	}
	
	public void displayMoveRejectedMessage(int move)
	{
		System.out.println("The move (" + move + ") was rejected, please try again.");
	}
	
	public void displayActivePlayer(byte playerID)
	{
		System.out.println("\nPlayer " + playerID + " is next!");
	}
	
	public void displayGameStatus(byte gameStatus)
	{
		System.out.print("\nGame status: ");
		
		switch(gameStatus)
		{
			case IModel.GAME_STATUS_ONGOING: System.out.println("The game is in progress."); break;
			case IModel.GAME_STATUS_WIN_1: System.out.println("Player 1 has won!"); break;
			case IModel.GAME_STATUS_WIN_2: System.out.println("Player 2 has won!"); break;
			case IModel.GAME_STATUS_TIE: System.out.println("The game has ended in a tie!"); break;
			default : System.out.println("Error: Invalid/unknown game status"); break;
		}
	}
	
	public void displayBoard(IModel model)
	{
		System.out.println("\n-------- BOARD --------");
		
		int nrRows = model.getGameSettings().nrRows;
		int nrCols = model.getGameSettings().nrCols;
		char[][] board = model.getBoard();

		// Remove this and replace it with an actual representation of the board.
//		System.out.println("The board has " + nrRows + " rows and " + nrCols + " columns.");
		
		// Here is an example of how the output should look:
		//_ _ O O _ _ X
		//_ _ X O _ _ X
		//_ O X X _ _ O
		//_ X X O _ X O
		//X O O X O O O
		//X O X X X O X
        // 新增加的代码逻辑, 渲染地图
		for(int row = 0; row < nrRows; row++){
			for(int col = 0; col < nrCols; col++){
				System.out.print(String.format("%s ", board[col][row]));
			}
			System.out.println("");
		}
	}
	
	public char requestMenuSelection()
	{
		// Display menu options.
		System.out.println("\n-------- MENU --------");
		System.out.println("(1) Start new game");
		System.out.println("(2) Resume saved game");
		System.out.println("(3) Change game settings");
		System.out.println("(4) Change players");
		
		// Request and return user input.
		System.out.print("Select an option and confirm with enter or use any other key to quit: ");
		return InputUtil.readCharFromUser();
	}
	
	public String requestSaveFileName()
	{
		System.out.println("\n-------- LOAD GAME --------");
		System.out.print("File name (e.g. Save.txt): ");
		return InputUtil.readStringFromUser();
	}
	
	public GameSettings requestGameSettings()
	{
		System.out.println("\n-------- GAME SETTINGS --------");
		
		// Replace these lines with code that asks the user to enter the values.
//		System.out.println("This feature has not yet been implemented, using default settings instead.");
		int nrRows;
		while (true) {
			System.out.print("Please input nrRows: ");
			nrRows = InputUtil.readIntFromUser();
			if (nrRows < IModel.MIN_ROWS || nrRows > IModel.MAX_ROWS) {
				System.out.println("The nrRows should have no less than 3 and no more than 10 ");
			} else {
				break;
			}
		}
		int nrCols;
		while (true) {
			System.out.print("Please input nrCols: ");
			nrCols = InputUtil.readIntFromUser();
			if (nrCols< IModel.MIN_COLS|| nrCols > IModel.MAX_COLS) {
				System.out.println("The nrCols should have no less than 3 and no more than 10 ");
			} else {
				break;
			}
		}
		System.out.print("Please input streak: ");
		int streak = InputUtil.readIntFromUser();
		
		// Wrap the selected settings in a GameSettings instance and return (leave this code here).
		return new GameSettings(nrRows, nrCols, streak);
	}
	
	public IPlayer requestPlayerSelection(byte playerId)
	{
		System.out.println("\n-------- CHOOSE PLAYER " + playerId + " --------");
		System.out.println("(1) HumanPlayer");
		System.out.println("(2) RoundRobinPlayer");
		System.out.println("(3) WinDetectingPlayer");
		System.out.println("(4) CompetitivePlayer");
		
		// Request user input.
		System.out.print("Select an option and confirm with enter (invalid input will select a HumanPlayer): ");
		char selectedPlayer = InputUtil.readCharFromUser();
		
		// Instantiate the selected player class.
		switch(selectedPlayer)
		{
			case '2': return new RoundRobinPlayer();
			case '3': return new WinDetectingPlayer();
			case '4': return new CompetitivePlayer();
			default: return new HumanPlayer();
		}
	}
}

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OthelloModelTest {
    private OthelloModel model;

    @BeforeEach
    void setUp() {
        model = new OthelloModel();
    }

    @Test
    void testInitialBoard() {
        int[][] board = model.getBoard();
        assertEquals(OthelloModel.WHITE, board[3][3]);
        assertEquals(OthelloModel.BLACK, board[3][4]);
        assertEquals(OthelloModel.BLACK, board[4][3]);
        assertEquals(OthelloModel.WHITE, board[4][4]);
        assertEquals(OthelloModel.BLACK, model.getCurrentPlayer());
        assertFalse(model.isGameOver());
    }

    @Test
    void testValidMove() {
        assertTrue(model.isValidMove(2, 3, OthelloModel.BLACK));
        assertFalse(model.isValidMove(0, 0, OthelloModel.BLACK));
    }

    @Test
    void testMakeMoveAndFlip() {
        assertTrue(model.makeMove(2, 3)); // Black moves
        int[][] board = model.getBoard();
        assertEquals(OthelloModel.BLACK, board[2][3]); // New move
        assertEquals(OthelloModel.BLACK, board[3][3]); // Flipped from white
        assertEquals(OthelloModel.BLACK, board[4][3]); // Should stay black
        assertEquals(OthelloModel.BLACK, board[3][4]); // Should stay black
        assertEquals(OthelloModel.WHITE, board[4][4]); // Should stay white
    }

    @Test
    void testScoreCalculation() {
        model.makeMove(2, 3); // Black
        assertEquals(4, model.getScore(OthelloModel.BLACK));
        assertEquals(1, model.getScore(OthelloModel.WHITE));
    }

    @Test
    void testGameOverDetection() {
        // Fill the board with alternating pieces, no valid moves
        int[][] board = new int[OthelloModel.SIZE][OthelloModel.SIZE];
        for (int i = 0; i < OthelloModel.SIZE; i++) {
            for (int j = 0; j < OthelloModel.SIZE; j++) {
                board[i][j] = (i + j) % 2 == 0 ? OthelloModel.BLACK : OthelloModel.WHITE;
            }
        }
        // Set the board directly for testing
        model.setBoard(board);
        // No valid moves for either player
        assertFalse(model.hasValidMove(OthelloModel.BLACK));
        assertFalse(model.hasValidMove(OthelloModel.WHITE));
    }

    @Test
    void testGreedyMove() {
        // Black's best move at start is (2,3) or (3,2) or (4,5) or (5,4)
        int[] move = model.getGreedyMove();
        assertNotNull(move);
        assertTrue((move[0] == 2 && move[1] == 3) || (move[0] == 3 && move[1] == 2) ||
                   (move[0] == 4 && move[1] == 5) || (move[0] == 5 && move[1] == 4));
    }

    @Test
    void testDefensiveCopy() {
        int[][] board1 = model.getBoard();
        board1[0][0] = OthelloModel.BLACK;
        int[][] board2 = model.getBoard();
        assertEquals(OthelloModel.EMPTY, board2[0][0]);
    }
} 
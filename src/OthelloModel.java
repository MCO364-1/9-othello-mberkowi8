public class OthelloModel {
    public static final int SIZE = 8;
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    private int[][] board;
    private int currentPlayer;
    private boolean gameOver;

    public OthelloModel() {
        board = new int[SIZE][SIZE];
        resetBoard();
    }

    public void resetBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
        board[3][3] = WHITE;
        board[3][4] = BLACK;
        board[4][3] = BLACK;
        board[4][4] = WHITE;
        currentPlayer = BLACK;
        gameOver = false;
    }

    public int[][] getBoard() {
        int[][] copy = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    // For testing purposes only
    void setBoard(int[][] newBoard) {
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(newBoard[i], 0, board[i], 0, SIZE);
        }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getScore(int player) {
        int score = 0;
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == player) score++;
            }
        }
        return score;
    }

    public boolean isValidMove(int row, int col, int player) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || board[row][col] != EMPTY) return false;
        return canFlip(row, col, player);
    }

    private boolean canFlip(int row, int col, int player) {
        int opponent = (player == BLACK) ? WHITE : BLACK;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int r = row + dr, c = col + dc;
                boolean foundOpponent = false;
                while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == opponent) {
                    r += dr;
                    c += dc;
                    foundOpponent = true;
                }
                if (foundOpponent && r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasValidMove(int player) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(i, j, player)) return true;
            }
        }
        return false;
    }

    public boolean makeMove(int row, int col) {
        if (!isValidMove(row, col, currentPlayer)) return false;
        board[row][col] = currentPlayer;
        flip(row, col, currentPlayer);
        switchPlayer();
        checkGameOver();
        return true;
    }

    private void flip(int row, int col, int player) {
        int opponent = (player == BLACK) ? WHITE : BLACK;
        
        // Check each direction
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                
                // Look for a valid line of opponent pieces ending in a player piece
                int r = row + dr;
                int c = col + dc;
                boolean foundOpponent = false;
                
                // Move along the line until we find a non-opponent piece
                while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == opponent) {
                    r += dr;
                    c += dc;
                    foundOpponent = true;
                }
                
                // If we found opponent pieces and ended on a player piece, flip all pieces in between
                if (foundOpponent && r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == player) {
                    // Move back to original position and flip all in between
                    int flipR = row + dr;
                    int flipC = col + dc;
                    while (flipR != r || flipC != c) {
                        board[flipR][flipC] = player;
                        flipR += dr;
                        flipC += dc;
                    }
                }
            }
        }
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == BLACK) ? WHITE : BLACK;
        if (!hasValidMove(currentPlayer)) {
            currentPlayer = (currentPlayer == BLACK) ? WHITE : BLACK;
            if (!hasValidMove(currentPlayer)) {
                gameOver = true;
            }
        }
    }

    private void checkGameOver() {
        if (!hasValidMove(BLACK) && !hasValidMove(WHITE)) {
            gameOver = true;
        }
    }

    public int[] getGreedyMove() {
        int maxFlips = -1;
        int[] bestMove = null;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(i, j, currentPlayer)) {
                    int flips = countFlips(i, j, currentPlayer);
                    if (flips > maxFlips) {
                        maxFlips = flips;
                        bestMove = new int[]{i, j};
                    }
                }
            }
        }
        return bestMove;
    }

    private int countFlips(int row, int col, int player) {
        int opponent = (player == BLACK) ? WHITE : BLACK;
        int total = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int r = row + dr, c = col + dc, count = 0;
                while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == opponent) {
                    r += dr;
                    c += dc;
                    count++;
                }
                if (count > 0 && r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == player) {
                    total += count;
                }
            }
        }
        return total;
    }
} 
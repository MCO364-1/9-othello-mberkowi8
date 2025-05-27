import javax.swing.*;
import java.awt.*;

public class OthelloGUI extends JFrame {
    private OthelloModel model;
    private JButton[][] buttons;
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JButton resetButton;

    public OthelloGUI() {
        model = new OthelloModel();
        buttons = new JButton[OthelloModel.SIZE][OthelloModel.SIZE];
        setTitle("Othello");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(OthelloModel.SIZE, OthelloModel.SIZE));
        for (int i = 0; i < OthelloModel.SIZE; i++) {
            for (int j = 0; j < OthelloModel.SIZE; j++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(50, 50));
                btn.setBackground(new Color(0, 128, 0));
                btn.setOpaque(true);
                btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                final int row = i, col = j;
                btn.addActionListener(e -> handleMove(row, col));
                buttons[i][j] = btn;
                boardPanel.add(btn);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        statusLabel = new JLabel();
        scoreLabel = new JLabel();
        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            model.resetBoard();
            updateBoard();
        });
        infoPanel.add(statusLabel);
        infoPanel.add(scoreLabel);
        infoPanel.add(resetButton);
        add(infoPanel, BorderLayout.SOUTH);

        updateBoard();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleMove(int row, int col) {
        if (model.isGameOver()) return;
        if (model.getCurrentPlayer() == OthelloModel.BLACK) {
            if (model.makeMove(row, col)) {
                updateBoard();
                if (!model.isGameOver() && model.getCurrentPlayer() == OthelloModel.WHITE) {
                    makeComputerMove();
                }
            }
        }
    }

    private void makeComputerMove() {
        int[] move = model.getGreedyMove();
        if (move != null) {
            model.makeMove(move[0], move[1]);
            updateBoard();
        }
    }

    private void updateBoard() {
        int[][] board = model.getBoard();
        for (int i = 0; i < OthelloModel.SIZE; i++) {
            for (int j = 0; j < OthelloModel.SIZE; j++) {
                JButton btn = buttons[i][j];
                btn.setText("");
                if (board[i][j] == OthelloModel.BLACK) {
                    btn.setBackground(Color.BLACK);
                } else if (board[i][j] == OthelloModel.WHITE) {
                    btn.setBackground(Color.WHITE);
                } else {
                    btn.setBackground(new Color(0, 128, 0));
                    if (model.isValidMove(i, j, model.getCurrentPlayer())) {
                        btn.setText("•");
                    }
                }
            }
        }
        int blackScore = model.getScore(OthelloModel.BLACK);
        int whiteScore = model.getScore(OthelloModel.WHITE);
        scoreLabel.setText("Black: " + blackScore + "  White: " + whiteScore);
        if (model.isGameOver()) {
            String winner;
            if (blackScore > whiteScore) winner = "Black wins!";
            else if (whiteScore > blackScore) winner = "White wins!";
            else winner = "It's a tie!";
            statusLabel.setText("Game Over: " + winner);
        } else {
            statusLabel.setText((model.getCurrentPlayer() == OthelloModel.BLACK ? "Black's" : "White's") + " turn");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OthelloGUI::new);
    }
} 
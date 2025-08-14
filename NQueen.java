import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main extends JFrame {
    private final int N; // Number of queens
    private final int[][] board; // Board to store the solution
    private final ArrayList<int[]> solutions = new ArrayList<>();
    private int currentSolutionIndex = 0;

    public Main(int N) {
        this.N = N;
        this.board = new int[N][N];
        setTitle("N Queens Visualizer");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        solveNQueens(0);
        this.repaint();
    }

    // Method to find all solutions using backtracking
    private void solveNQueens(int row) {
        if (row == N) {
            // Solution found, add it to solutions list
            int[] solution = new int[N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] == 1) {
                        solution[i] = j; // Store column positions of queens
                        break;
                    }
                }
            }
            solutions.add(solution);
            return;
        }

        for (int col = 0; col < N; col++) {
            if (isSafe(row, col)) {
                board[row][col] = 1; // Place queen
                solveNQueens(row + 1); // Recur to place queen in next row
                board[row][col] = 0; // Backtrack
            }
        }
    }

    // Check if it's safe to place a queen at board[row][col]
    private boolean isSafe(int row, int col) {
        for (int i = 0; i < row; i++) {
            // Check column
            if (board[i][col] == 1) {
                return false;
            }
            // Check diagonals
            if (col - (row - i) >= 0 && board[i][col - (row - i)] == 1) {
                return false;
            }
            if (col + (row - i) < N && board[i][col + (row - i)] == 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Set up the board size
        int cellSize = 600 / N;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 600, 600);

        // Loop through the solutions and draw them on the board
        if (!solutions.isEmpty()) {
            int[] solution = solutions.get(currentSolutionIndex); // Get current solution to visualize
            for (int i = 0; i < N; i++) {
                int col = solution[i];
                g.setColor(Color.RED); // Set queen color
                g.fillRect(col * cellSize, i * cellSize, cellSize, cellSize);
            }
        }

        // Draw the grid
        g.setColor(Color.WHITE);
        for (int i = 0; i <= N; i++) {
            g.drawLine(0, i * cellSize, 600, i * cellSize); // Draw horizontal lines
            g.drawLine(i * cellSize, 0, i * cellSize, 600); // Draw vertical lines
        }
    }

    // Method to cycle through solutions on mouse click or timer event
    public void nextSolution() {
        currentSolutionIndex = (currentSolutionIndex + 1) % solutions.size();
        repaint();
    }

    public static void main(String[] args) {
        int N = 8; // Set the number of queens
        SwingUtilities.invokeLater(() -> {
            Main visualizer = new Main(N);
            visualizer.setVisible(true);

            // Optionally, cycle through solutions
            Timer timer = new Timer(1000, e -> visualizer.nextSolution());
            timer.start();
        });
    }
}

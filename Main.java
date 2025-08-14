import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Main extends JFrame {
    private final int N; // Number of queens
    private final int[][] board; // Board to store the solution
    private final ArrayList<int[]> solutions = new ArrayList<>();
    private int currentSolutionIndex = 0;
    private Timer timer; // Timer to cycle through the solutions
    private boolean isAnimating = false; // Track if animation is running

    public Main(int N) {
        this.N = N;
        this.board = new int[N][N];
        setTitle("N Queens Visualizer");
        setSize(600, 650); // Increased height for buttons
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        solveNQueens(0);
        
        // Set up the Start and Stop buttons
        JPanel panel = new JPanel();
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isAnimating) {
                    startAnimation();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopAnimation();
            }
        });

        panel.add(startButton);
        panel.add(stopButton);

        this.add(panel, BorderLayout.SOUTH);

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
        int cellSize = 600 / N;
        
        // Draw the chessboard with alternating colors
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                if ((row + col) % 2 == 0) {
                    g.setColor(new Color(238, 238, 210)); // Light square (beige-like)
                } else {
                    g.setColor(new Color(118, 150, 86)); // Dark square (greenish-brown)
                }
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
            }
        }

        // Visualizing the queens
        if (!solutions.isEmpty()) {
            int[] solution = solutions.get(currentSolutionIndex); // Get the current solution
            for (int i = 0; i < N; i++) {
                int col = solution[i];
                g.setColor(new Color(255, 0, 0)); // Queen color (red)

                // Queen styling: using a circle with a 'crown' effect for better aesthetics
                int queenSize = cellSize / 2;
                int x = col * cellSize + (cellSize - queenSize) / 2;
                int y = i * cellSize + (cellSize - queenSize) / 2;

                // Draw the queen as a circle
                g.fillOval(x, y, queenSize, queenSize);

                // Optionally, add a small crown-like visual (optional)
                g.setColor(Color.YELLOW);
                g.fillRect(x + queenSize / 4, y - queenSize / 4, queenSize / 2, queenSize / 4); // Crown
            }
        }

        // Draw the grid lines
        g.setColor(Color.BLACK);
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

    // Start the animation by creating and starting the timer
    private void startAnimation() {
        isAnimating = true;
        timer = new Timer(1000, e -> nextSolution()); // Change solution every 1 second
        timer.start();
    }

    // Stop the animation by stopping the timer
    private void stopAnimation() {
        isAnimating = false;
        if (timer != null) {
            timer.stop();
        }
    }

    public static void main(String[] args) {
        int N = 8; // Set the number of queens
        SwingUtilities.invokeLater(() -> {
            Main visualizer = new Main(N);
            visualizer.setVisible(true);
        });
    }
}


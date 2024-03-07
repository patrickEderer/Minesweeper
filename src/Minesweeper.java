import java.util.Scanner;

public class Minesweeper {
    Cell[][] board;
    boolean started = false;
    boolean lost = false;

    public Minesweeper(Cell[][] board) {
        //💣
        this.board = board;
    }

    public void start() {
        started = true;


        //init board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new Cell();
                board[i][j].setRevealed(false);
            }
        }

        Scanner scanner = new Scanner(System.in);

        boolean firstClick = true;

        //game loop
        do {
            draw(false);

            System.out.println("Field?");

            int x = 0;
            int y = 0;
            boolean mark = false;

            boolean success = false;
            do {
                try {
                    String[] input = scanner.nextLine().split(" ");
                    if (input.length == 3) mark = (input[0].equals("m"));
                    x = Integer.parseInt(input[mark ? 2 : 1]) - 1;
                    y = Integer.parseInt(input[mark ? 1 : 0]) - 1;

                    if (x >= board[0].length) {
                        System.out.println("x value too big!");
                        continue;
                    }
                    if (y >= board.length) {
                        System.out.println("y value too big!");
                        continue;
                    }
                    if (x < 0 || y < 0) {
                        System.out.println("value can not be 0!");
                        continue;
                    }

                    success = true;
                } catch (Exception ignored) {
                    System.out.println("Input needs to be in format: (m) x y");
                }
            } while (!success);

            if (firstClick) populateBoard(x, y);

            firstClick = false;

            updateCell(x, y, mark);

            if (checkWin()) stop(true);

            if (lost) stop(false);
        } while (started);
    }

    private void populateBoard(int excludedX, int excludedY) {
        //place bombs
        for (int i = 0; i < Main.bombs; i++) {
            int bombX;
            int bombY;
            do {
                bombX = (int) (Math.random() * Main.width);
                bombY = (int) (Math.random() * Main.height);
            } while (board[bombY][bombX].isBomb || ((Math.pow((bombX - excludedX), 2) <= 1) && (Math.pow((bombY - excludedY), 2) <= 1)));

            board[bombY][bombX].setBomb(true);
        }

        //calculate numbers
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int number = 0;
                for (int offsetX = -1; offsetX <= 1; offsetX++) {
                    for (int offsetY = -1; offsetY <= 1; offsetY++) {
                        if (i + offsetX >= Main.width || j + offsetY >= Main.height) continue;
                        if (i + offsetX < 0 || j + offsetY < 0) continue;

                        if (board[i + offsetX][j + offsetY].isBomb) number++;
                    }
                }
                board[i][j].setNumber(number);
            }
        }
    }

    private void draw(boolean bombs) {
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n  ");
        for (int i = 0; i < board[0].length; i++) {
            System.out.print("+-" + (i + 1) + (i >= 9 ? "" : "-"));
        }
        for (int i = 0; i < board.length; i++) {
            System.out.print("+\n");
            System.out.print((i + 1) + (i >= 9 ? "| " : " | "));
            for (Cell cell : board[i]) {
                if (bombs)
                    System.out.print((cell.isRevealed ? (cell.number != 0 ? cell.number + " " : (cell.isBomb ? "\uD83D\uDCA3" : "  ")) : (cell.isMarked ? "⚑ " : "█ ")) + "| ");
                else
                    System.out.print((cell.isRevealed ? (cell.number != 0 ? cell.number + " " : "  ") : (cell.isMarked ? "⚑ " : "█ ")) + "| ");
            }
            System.out.print("\n  ");
            for (int j = 0; j < board[i].length; j++) {
                System.out.print("+---");
            }
        }
        System.out.println("+");
    }

    private void updateCell(int x, int y, boolean mark) {
        if (board[x][y].isRevealed) return;

        if (mark) {
            board[x][y].setMarked(!board[x][y].isMarked);
            return;
        }
        if (board[x][y].isBomb) {
            lost = true;
            return;
        }
        board[x][y].setRevealed(true);

        if (board[x][y].number != 0) return;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                //update surrounding cells
                if (x + i >= 0 && x + i < board.length) {
                    if (y + j >= 0 && y + j < board[0].length) {
                        updateCell(x + i, y + j, false);
                    }
                }
            }
        }
    }

    private boolean checkWin() {
        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                if (cell.isBomb && !cell.isMarked) return false;
            }
        }
        return true;
    }

    private void stop(boolean won) {
        //reveal everything
        for (Cell[] cells : board) {
            for (Cell cell : cells) {
                cell.setRevealed(true);
            }
        }

        draw(true);

        System.out.println(won ? "You won!" : "You lost!");
        started = false;
    }
}

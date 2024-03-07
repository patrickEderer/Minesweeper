import java.util.List;
import java.util.Scanner;

public class Main {
    public static Cell[][] board;
    public static int width;
    public static int height;
    public static int bombs;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        //config
        for (String s : List.of("Width?", "Height?", "How many bombs?")) {
            boolean success = false;
            int input = 0;

            System.out.println(s);
            do {
                try {
                    input = scanner.nextInt();

                    //too big check
                    if (input >= 100 && !s.equals("How many bombs?")) {
                        System.out.println("Input too big(4-99)");
                        continue;
                    }
                    //too big check - bombs
                    if ((width * height) - 9 < bombs) {
                        System.out.println("Input too big(1-" + ((width * height) - 9) + ")");
                        continue;
                    }

                    //too small check
                    if (input <= 3 && !s.equals("How many bombs?")) {
                        System.out.println("Input too small(4-99)");
                        continue;
                    }
                    //too small check - bombs
                    if (input <= 0) {
                        System.out.println("Input too big(1-" + ((width * height) - 9) + ")");
                        continue;
                    }

                    switch (s) {
                        case "Width?" -> width = input;
                        case "Height?" -> height = input;
                        case "How many bombs?" -> bombs = input;
                    }

                    success = true;
                } catch (Exception ignored) {
                    System.out.println("Input needs to be a number!");
                    scanner.next();
                }
            } while (!success);
        }

        //create board
        board = new Cell[height][width];

        //game
        Minesweeper minesweeper = new Minesweeper(board);
        minesweeper.start();
    }
}
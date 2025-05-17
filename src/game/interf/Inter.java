package game.interf;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class Inter {
    public static String RESET = "\u001B[0m";
    public static String CYAN = "\u001B[36m";
    public static String YELLOW = "\u001B[33m";
    public static String PURPLE = "\u001B[35m";
    public static String BOLD = "\u001B[1m";
    public static String RED = "\u001B[31m";
    public static String GRADIENT_1 = "\u001B[38;5;213m";
    public static String GRADIENT_2 = "\u001B[38;5;219m";
    public static String GRADIENT_3 = "\u001B[38;5;225m";
    public static String GOLD_COLOR = "\u001B[33m";
    public static String GREEN = "\u001B[32m";
    public Scanner scanner = new Scanner(System.in);
    public abstract void display();

    public int handleInput() {
        try {
            System.out.print(BOLD + "  Ваш выбор: " + RESET );
            int input = scanner.nextInt();
            scanner.nextLine(); // Очищаем буфер
            return input;
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Очищаем неверный ввод
            return -1; // Возвращаем невалидное значение
        }
    }

    public void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }
}
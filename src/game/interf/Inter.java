package game.interf;
import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class Inter {
    protected static String RESET = "\u001B[0m";
    protected static String CYAN = "\u001B[36m";
    protected static String YELLOW = "\u001B[33m";
    protected static String PURPLE = "\u001B[35m";
    protected static String BOLD = "\u001B[1m";
    protected static String RED = "\u001B[31m";
    protected static String GRADIENT_1 = "\u001B[38;5;213m";
    protected static String GRADIENT_2 = "\u001B[38;5;219m";
    protected static String GRADIENT_3 = "\u001B[38;5;225m";
    protected static String GOLD_COLOR = "\u001B[33m";
    protected static String GREEN = "\u001B[32m";
    protected Scanner scanner = new Scanner(System.in);
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

    protected void clearConsole() {
        for (int i = 0; i < 50; i++) System.out.println();
    }
}
package game.interf;

public class CaveInterface extends Inter {

    public void print(String text) {
        System.out.println(text);
    }

    public void waitMillis(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void showPrompt() {
        System.out.println(YELLOW + "[W/A/S/D] " + CYAN + "Двигаться   " + YELLOW + "[L] " + RED + "Сдаться" + RESET);
    }

    public String readAction() {
        System.out.print(BOLD + "  ➤ Ваш выбор: " + RESET);
        return scanner.nextLine().trim().toUpperCase();
    }

    @Override
    public void display() { }
}

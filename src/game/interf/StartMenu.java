package game.interf;

public class StartMenu extends Inter {
    private static final String[] LOGO = {
            "            ██╗  ██╗███╗   ███╗██████╗ ",
            "            ██║  ██║████╗ ████║██╔══██╗",
            "            ███████║██╔████╔██║██████╔╝",
            "            ██╔══██║██║╚██╔╝██║██╔══██╗",
            "            ██║  ██║██║ ╚═╝ ██║██████╔╝",
            "            ╚═╝  ╚═╝╚═╝     ╚═╝╚═════╝",
            "             Heroes of Might and Baum"
    };

    @Override
    public void display() {
        clearConsole();
        for (String line : LOGO) {
            System.out.println(PURPLE + line + RESET);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
        System.out.println("\n" +
                GRADIENT_1 + "  ╔════════════════════════════════════════════╗\n" +
                GRADIENT_2 + "  ║" + BOLD + "      🏰  ДОБРО ПОЖАЛОВАТЬ В ИГРУ!  🏰     " + RESET + GRADIENT_2 + " ║\n" +
                GRADIENT_3 + "  ╠════════════════════════════════════════════╣\n" +
                GRADIENT_1 + "  ║                                            ║\n" +
                GRADIENT_2 + "  ║    " + YELLOW + "1. " + CYAN + "Начать новую игру" +
                " ".repeat(20) + GRADIENT_2 + "║\n" +
                GRADIENT_3 + "  ║    " + YELLOW + "2. " + CYAN + "Загрузить игру" +
                " ".repeat(23) + GRADIENT_3 + "║\n" +
                GRADIENT_1 + "  ║    " + YELLOW + "3. " + CYAN + "Показать таблицу лидеров" +
                " ".repeat(8) + GRADIENT_1 + "     ║\n" +
                GRADIENT_1 + "  ║    " + YELLOW + "4. " + PURPLE + "Выйти" +
                " ".repeat(32) + GRADIENT_1 + "║\n" +
                GRADIENT_2 + "  ║                                            ║\n" +
                GRADIENT_3 + "  ╚════════════════════════════════════════════╝\n" + RESET);

    }
}

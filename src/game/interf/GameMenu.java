package game.interf;

public class GameMenu extends Inter {
    @Override
    public void display() {
        System.out.println("🎮 Управление:");
        System.out.println("⬆️ [W] - Вверх  |  ⬇️ [S] - Вниз  |  ⬅️ [A] - Влево" +
                "  |  ➡️ [D] - Вправо  |  📜 [M] - Меню  |  ⏭️ [Q] - Пропуск хода  |  🪄 [X] - Использовать артефакт");
    }

    public void display(int points, int castleHealth, int enemyCastleHealth, int money, int hp, int power) {
        System.out.println("📊 Статистика:");
        System.out.println("🏃 Очки передвижения: " + points + "  |  💰 Золото: " + money +
                "  |  ❤️ Здоровье: " + hp + " HP" + "  |  \uD83D\uDCAA Сила: " + power +
                "  |  🏰 Замок: " + castleHealth + " HP" +
                "  |  ☠️ Вражеский замок: " + enemyCastleHealth + " HP");
    }

    //Beautiful but useful :(
    /*
    @Override
    public void display() {
        System.out.println(
                "  ╔══════════════════════════════════════╗\n" +
                "  ║            🎮 УПРАВЛЕНИЕ             ║\n" +
                "  ╠══════════════════════════════════════╣\n" +
                "  ║   ⬆️ [W] - Вверх  ⬇️ [S] - Вниз      ║\n" +
                "  ║   ⬅️ [A] - Влево  ➡️ [D] - Вправо    ║\n" +
                "  ║   📜 [M] - Меню   ⏭️ [Q] - Пропуск   ║\n" +
                "  ╚══════════════════════════════════════╝\n");
    }

    public void display(int points, int castleHealth, int enemyCastleHealth, int money, int hp) {
        // Цвета (ANSI-коды)
        String RESET = "\u001B[0m";
        String GREEN = "\u001B[32m";
        String RED = "\u001B[31m";
        String YELLOW = "\u001B[33m";
        String BLUE = "\u001B[34m";
        String CYAN = "\u001B[36m";

        // Прогресс-бары
        String hpBar = getProgressBar(hp, 100, "❤️", RED);
        String castleBar = getProgressBar(castleHealth, 200, "🏰", YELLOW);
        String enemyCastleBar = getProgressBar(enemyCastleHealth, 200, "☠️", RED);

        System.out.println("\n" +
                "  ╔══════════════════════════════════════════════════╗\n" +
                "  ║                  📊 СТАТУС ИГРЫ                  ║\n" +
                "  ╠══════════════════════════════════════════════════╣\n" +
                "  ║ " + CYAN + "🏃 Очки передвижения: " + points + RESET + "  " +
                GREEN + "💰 Золото: " + money + RESET + "        ║\n" +
                "  ║ " + hpBar + "              ║\n" +
                "  ║ " + castleBar + "              ║\n" +
                "  ║ " + enemyCastleBar + "              ║\n" +
                "  ╚══════════════════════════════════════════════════╝\n");
    }

    // Метод для создания прогресс-бара
    private String getProgressBar(int current, int max, String icon, String color) {
        int barLength = 20;
        int progress = (int) ((double) current / max * barLength);
        String bar = color + icon + " [";
        for (int i = 0; i < barLength; i++) {
            bar += (i < progress) ? "■" : " ";
        }
        bar += "] " + current + "/" + max + " HP" + "\u001B[0m";
        return bar;
    }
    */
}

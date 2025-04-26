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
}

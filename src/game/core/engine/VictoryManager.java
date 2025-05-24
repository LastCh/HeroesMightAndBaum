package game.core.engine;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static game.api.LogConfig.LOGGER;

public class VictoryManager {
    private static final File PLAYERS_FILE = new File("players.txt");
    // Повторяем формат кармы, чтобы при перезаписи не портить число
    private static final DecimalFormat KARMA_FORMAT =
            new DecimalFormat("0.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    /** Увеличивает счёт побед данного игрока на 1 и сортирует записи по убыванию побед */
    public static void addVictory(String playerName) {
        List<PlayerEntry> entries = loadEntries();
        boolean updated = false;

        for (PlayerEntry e : entries) {
            if (e.getName().equalsIgnoreCase(playerName)) {
                e.incrementWins();
                updated = true;
                break;
            }
        }
        if (!updated) {
            // Если игрока ещё нет в файле — создаём запись с 0 кармы и 1 победой
            entries.add(new PlayerEntry(playerName, 0.0, 1));
        }

        // Сортируем по убыванию побед
        entries.sort((a, b) -> Integer.compare(b.getWins(), a.getWins()));

        saveEntries(entries);
        LOGGER.info("Добавлена победа игроку: " + playerName);
    }

    /** Возвращает текущее количество побед игрока (или 0, если нет записи) */
    public static int loadPlayerWins(String playerName) {
        for (PlayerEntry e : loadEntries()) {
            if (e.getName().equalsIgnoreCase(playerName)) {
                return e.getWins();
            }
        }
        return 0;
    }

    /** Читает весь файл и парсит список записей */
    private static List<PlayerEntry> loadEntries() {
        List<PlayerEntry> list = new ArrayList<>();
        if (!PLAYERS_FILE.exists()) {
            return list;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(PLAYERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                PlayerEntry entry = PlayerEntry.parse(line);
                if (entry != null) {
                    list.add(entry);
                }
            }
        } catch (IOException | NumberFormatException e) {
            LOGGER.warning("Ошибка при чтении players.txt: " + e.getMessage());
        }
        return list;
    }

    /** Перезаписывает файл на основании списка записей */
    private static void saveEntries(List<PlayerEntry> entries) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PLAYERS_FILE))) {
            for (PlayerEntry e : entries) {
                writer.write(e.toLine());
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.severe("Ошибка записи players.txt: " + e.getMessage());
        }
    }

    public static List<PlayerStats> loadAllStats() {
        List<PlayerEntry> entries = loadEntries(); // приватный метод
        // сортируем
        entries.sort((a, b) -> {
            int cmp = Integer.compare(b.getWins(), a.getWins());
            if (cmp != 0) return cmp;
            return Double.compare(b.getKarma(), a.getKarma());
        });
        // преобразуем в DTO
        List<PlayerStats> result = new ArrayList<>(entries.size());
        for (PlayerEntry e : entries) {
            result.add(new PlayerStats(e.getName(), e.getKarma(), e.getWins()));
        }
        return result;
    }

    public static class PlayerStats {
        private final String name;
        private final double karma;
        private final int wins;
        public PlayerStats(String name, double karma, int wins) {
            this.name  = name;
            this.karma = karma;
            this.wins  = wins;
        }
        public String getName()  { return name; }
        public double getKarma() { return karma; }
        public int    getWins()  { return wins;  }
    }

    // === Внутренний класс для одной строки файла ===

    private static class PlayerEntry {
        private final String name;
        private final double karma;
        private int wins;

        private PlayerEntry(String name, double karma, int wins) {
            this.name = name;
            this.karma = karma;
            this.wins = wins;
        }

        static PlayerEntry parse(String line) {
            String[] parts = line.split(";");
            if (parts.length < 2) return null;
            String n = parts[0];
            double k = Double.parseDouble(parts[1]);
            int w = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
            return new PlayerEntry(n, k, w);
        }

        String getName() {
            return name;
        }

        double getKarma() {
            return karma;
        }

        int getWins() {
            return wins;
        }

        void incrementWins() {
            wins++;
        }

        /** Формирует строку вида "name;karma;wins" */
        String toLine() {
            return String.join(";",
                    name,
                    KARMA_FORMAT.format(karma),
                    String.valueOf(wins)
            );
        }
    }
}
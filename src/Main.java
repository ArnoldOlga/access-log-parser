import java.io.*;
import java.util.Scanner;

public class Main {

    public static final int MAX_LINE_LENGTH = 1024;
    public static final String GOOGLEBOT = "Googlebot";
    public static final String YANDEX_BOT = "YandexBot";

    public static void main(String[] args) {
        int n = 0;
        while (true) {
            System.out.println("Введите путь: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            if (!fileExists) {
                System.out.println("Файл не существует");
                continue;
            }
            boolean isDirectory = file.isDirectory();
            if (isDirectory) {
                System.out.println("Указан путь до директории");
            } else {
                n++;
                System.out.println("Путь указан верно");
                System.out.println("Это файл номер " + n);

                parseFile(path);
            }
        }
    }


    private static void parseFile(String path) {
        try (var reader = new BufferedReader(new FileReader(path))) {

            int numberOfLines = 0;
            int countYandexBot = 0;
            int countGooglebot = 0;

            String line;
            Statistics statistics = new Statistics();
            while ((line = reader.readLine()) != null) {
                int length = line.length();
                if (length > MAX_LINE_LENGTH) {
                    throw new LineTooLongException(length, MAX_LINE_LENGTH);
                }
                LogEntry logEntry = new LogEntry(line);
                statistics.addEntry(logEntry);

                numberOfLines++;
                String searchBot = findSearchBot(line);
                if (GOOGLEBOT.equals(searchBot)) {
                    countGooglebot++;
                }
                if (YANDEX_BOT.equals(searchBot)) {
                    countYandexBot++;
                }
            }

            System.out.println("Общее количество строк в файле: " + numberOfLines);
            System.out.println("Количество Googlebot в файле: " + countGooglebot);
            System.out.println("Количество YandexBot в файле: " + countYandexBot);
            System.out.println("Доля Googlebot в файле: " + (double) countGooglebot / numberOfLines);
            System.out.println("Доля YandexBot в файле: " + (double) countYandexBot / numberOfLines);
            System.out.println("Cредний объём трафика сайта за час: " + statistics.getTrafficRate());
            System.out.println("Доля для каждой операционной системы: " + statistics.calculateOSShare());
            System.out.println("Cреднее количество посещений сайта за час: " + statistics.averageNumberOfSiteVisits());
            System.out.println("Cреднее количество ошибочных запросов в час: " + statistics.numberOfErrorRequestsPerHour());
            System.out.println("Расчёт средней посещаемости одним пользователем: " + statistics.calculationOfattendanceByOneUser());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String findSearchBot(String line) {
        int endIndexUserAgent = line.lastIndexOf("\"");
        int startIndexUserAgent = line.lastIndexOf("\"", endIndexUserAgent - 1);
        String userAgent = line.substring(startIndexUserAgent + 1, endIndexUserAgent);


        int startIndex = 0;
        while (true) {
            startIndex = userAgent.indexOf("(", startIndex);
            if (startIndex == -1) {
                return null;
            }
            int endIndex = userAgent.indexOf(")", startIndex + 1);

            String brackets;
            if (endIndex != -1) {
                brackets = userAgent.substring(startIndex + 1, endIndex);
            } else {
                brackets = userAgent.substring(startIndex + 1);
            }

            String searchBot = parseBrackets(brackets);
            if (searchBot != null) {
                return searchBot;
            }

            startIndex++;
        }
    }

    private static String parseBrackets(String brackets) {
        String fragment;
        String[] parts = brackets.split(";");
        if (parts.length >= 2) {
            fragment = parts[1].replaceAll("\\s+", "");
        } else {
            return null;
        }

        int endIndexFragment = fragment.indexOf('/');
        String searchBot = null;
        if (endIndexFragment != -1) {
            searchBot = fragment.substring(0, endIndexFragment);
        }

        if (GOOGLEBOT.equals(searchBot) || YANDEX_BOT.equals(searchBot)) {
            return searchBot;
        }
        return null;
    }
}

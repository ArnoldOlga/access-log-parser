import java.io.*;
import java.util.Scanner;

public class Main {

    public static final int MAX_LINE_LENGTH = 1024;

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
            int maxLength = 0;
            int minLength = Integer.MAX_VALUE;

            String line;
            while ((line = reader.readLine()) != null) {
                int length = line.length();
                if (length > MAX_LINE_LENGTH) {
                    throw new LineTooLongException(length, MAX_LINE_LENGTH);
                }
                numberOfLines++;
                maxLength = Math.max(maxLength, length);
                minLength = Math.min(minLength, length);
            }

            System.out.println("Общее количество строк в файле: " + numberOfLines);
            System.out.println("Длину самой длинной строки в файле: " + maxLength);
            System.out.println("Длину самой короткой строки в файле: " + minLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



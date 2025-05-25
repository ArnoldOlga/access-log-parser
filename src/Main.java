import java.io.File;
import java.util.Scanner;

public class Main {
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
            }
        }
    }
}



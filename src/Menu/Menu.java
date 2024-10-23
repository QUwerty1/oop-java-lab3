package Menu;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import IntArray.IntArray;
import Writer.*;

public class Menu {

    public Menu() {
        reader = new Scanner(System.in);
    }

    public Menu(InputStream inputStream) {
        reader = new Scanner(inputStream);
    }

    final private Scanner reader;
    private ReadWriteType outputType;
    private String fileName;

    private int selectedTask;

    public int getSelectedTask() {
        return selectedTask;
    }

    public void selectOutputTypeMenu() {
        int outputId;
        do {
            System.out.println("""
                    Куда вы хотите вывести результат?
                    \t1. Консоль
                    \t2. Текстовый файл
                    \t3. Бинарный файл
                    """);
            outputId = reader.nextInt();

            if (selectedTask < 1 || selectedTask > 3) {
                System.out.println("Введено некорректное значение");
            }
        } while (selectedTask < 1 || selectedTask > 3);

        outputType = switch (outputId) {
            case 2 -> ReadWriteType.textFile;
            case 3 -> ReadWriteType.binaryFile;
            default -> ReadWriteType.console;
        };
    }

    public void selectFileNameMenu() {
        System.out.println("Введите путь до файла: ");
        fileName = reader.next();
    }

    public void selectTaskMenu() {
        do {

            System.out.println("""
                    \n0. Выйти из программы
                    Выбор задания:
                    \t1.Выполнить задание A
                    \t2.Выполнить задание B
                    \t3.Выполнить задание C
                    \t4.Выполнить задание D
                    """);
            selectedTask = reader.nextInt();

            if (selectedTask < 0 || selectedTask > 4) {
                System.out.println("Введено некорректное значение");
            }
        } while (selectedTask < 0 || selectedTask > 4);
    }

    public void writeOutputMenu(IntArray intArray) {
        boolean isWriteSucceeded = false;
        do {
            try {
                Writer writer = switch (outputType) {
                    case ReadWriteType.console -> new TextWriter(
                            System.out);
                    case ReadWriteType.textFile -> new TextWriter(
                            new FileOutputStream(fileName));
                    case ReadWriteType.binaryFile -> new BinaryWriter(
                            new FileOutputStream(fileName));
                };
                writer.write(intArray);

                isWriteSucceeded = true;
            } catch (IOException ex) {
                System.out.println("""
                        Произошла ошибка открытия файла
                        Доступные действия:
                        \t1. Попробовать ещё раз
                        \t2. Изменить путь к файлу
                        \t3. Выйти из программы
                        """);
                int choice;

                do {
                    choice = reader.nextInt();

                    if (choice == 2) {
                        selectFileNameMenu();
                    } else if (choice == 3) {
                        return;
                    }
                } while (choice < 1 || choice > 3);
            }
        } while (!isWriteSucceeded);
    }
}

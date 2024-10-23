package Menu;

import java.io.*;
import java.util.Scanner;

import IntArray.IntArray;
import Reader.Reader;
import Writer.*;
import Reader.*;
import Writer.Writer;

public class Menu {

    public Menu() {
        scanner = new Scanner(System.in);
    }

    final private Scanner scanner;

    private ReadWriteType inputType;
    private ReadWriteType outputType;

    private IntArray intArray;
    private String conditionString;
    private String stringResult;

    private String fileName;
    private int selectedTask;

    private int getIntAnswer(int min, int max, String text) {
        int answer = -1;
        boolean isRightAnswer = false;
        do {
            System.out.println(text);

            if (scanner.hasNextInt()) {
                answer = scanner.nextInt();

                if (answer >= min && answer <= max) {
                    isRightAnswer = true;
                } else {
                    System.out.println("Число выходит из диапазона");
                }
            } else {
                System.out.println("Введите число");
                scanner.next();
            }

        } while (!isRightAnswer);

        return answer;
    }

    public void selectOutputTypeMenu() {
        int outputId;
        if (selectedTask == 1 || selectedTask == 3) {
            outputId = getIntAnswer(1, 3, """
                    Куда вы хотите вывести результат?
                    \t1. Консоль
                    \t2. Текстовый файл
                    \t3. Бинарный файл
                    """);
        } else {
            outputId = getIntAnswer(1, 2, """
                    Куда вы хотите вывести результат?
                    \t1. Консоль
                    \t2. Текстовый файл
                    """);
        }

        outputType = switch (outputId) {
            case 2 -> ReadWriteType.textFile;
            case 3 -> ReadWriteType.binaryFile;
            default -> ReadWriteType.console;
        };
        if (outputType == ReadWriteType.textFile || outputType == ReadWriteType.binaryFile) selectFileNameMenu();
    }

    public void selectInputMenu() {
        int inputId = getIntAnswer(1, 3, """
                От куда вы хотите взять массив чисел?
                \t1. Консоль
                \t2. Текстовый файл
                \t3. Бинарный файл
                """);

        inputType = switch (inputId) {
            case 2 -> ReadWriteType.textFile;
            case 3 -> ReadWriteType.binaryFile;
            default -> ReadWriteType.console;
        };
        if (inputType == ReadWriteType.textFile || inputType == ReadWriteType.binaryFile) selectFileNameMenu();
    }

    private void selectFileNameMenu() {
        System.out.println("Введите путь до файла: ");
        fileName = scanner.next();
    }

    private void accessFileError(String header, ReadWriteType readWriteType) {
        System.out.println(header);
        if (readWriteType != ReadWriteType.console) {
            int choice = getIntAnswer(1, 2, """
                    Доступные действия:
                    \t1. Попробовать ещё раз
                    \t2. Изменить путь к файлу
                    """);
            if (choice == 2) {
                selectFileNameMenu();
            }
        }
    }

    public void readFileMenu() {
        boolean isReadSucceeded = false;
        do {
            try {
                Reader reader = switch (inputType) {
                    case ReadWriteType.console -> new TextReader(System.in);
                    case ReadWriteType.textFile -> new TextReader(new FileInputStream(fileName));
                    case ReadWriteType.binaryFile -> new BinaryReader(new FileInputStream(fileName));
                };
                if (inputType == ReadWriteType.console)
                    System.out.println("Введите массив чисел, разделенных пробелами");
                intArray = reader.read();
                isReadSucceeded = true;
            } catch (Exception ex) {
                accessFileError("Произошла ошибка чтения файла", inputType);
            }
        } while (!isReadSucceeded);
    }

    public void selectTaskMenu() {
        selectedTask = getIntAnswer(0, 4, """
                \n0. Выйти из программы
                Выбор задания:
                \t1. Выполнить задание A
                \t2. Выполнить задание B
                \t3. Выполнить задание C
                \t4. Выполнить задание D
                """);
    }

    public void writeFileMenu() {
        boolean isWriteSucceeded = false;
        do {
            try {
                Writer writer = switch (outputType) {
                    case ReadWriteType.console -> new TextWriter(System.out);
                    case ReadWriteType.textFile -> new TextWriter(new FileOutputStream(fileName));
                    case ReadWriteType.binaryFile -> new BinaryWriter(new FileOutputStream(fileName));
                };
                if (selectedTask == 1 || selectedTask == 3) {
                    writer.write(intArray);
                } else {
                    assert writer instanceof TextWriter;
                    ((TextWriter) writer).write(stringResult);
                }
                writer.close();
                isWriteSucceeded = true;
            } catch (Exception ex) {
                accessFileError("Произошла ошибка открытия файла", inputType);
            }
        } while (!isWriteSucceeded);
    }

    private void getConditionStringMenu() {
        System.out.println("Введите строку с условием:");
        boolean isRight = false;
        do {
            conditionString = scanner.next();

            try {
                intArray.checkForCondition(conditionString);
                isRight = true;
            } catch (NumberFormatException | IllegalStateException exception) {
                System.out.println("Строка имеет неверный формат");
            }
        } while (!isRight);
    }

    public void completeTasks() {
        switch (selectedTask) {
            case 1: {
                getConditionStringMenu();
                intArray = intArray.filteredArray(conditionString);
                break;
            }
            case 2: {
                getConditionStringMenu();
                stringResult = intArray.checkForCondition(conditionString) ? "Есть" : "Нет";
                break;
            }
            case 3: {
                intArray = intArray.filteredDuplicates();
                break;
            }
            default: {
                stringResult = intArray.getOrder().toString();
            }
        }
    }
}

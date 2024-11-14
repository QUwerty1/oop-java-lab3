package Menu;

import IntArray.IntArray;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import Reader.*;
import Writer.*;

public class ConsoleMenu {

    final private static Scanner scanner = new Scanner(System.in);
    private static ReadWriteType inputType = ReadWriteType.console;
    private static ReadWriteType outputType = ReadWriteType.console;

    private static int taskId;
    private static IntArray intArray = null;

    private static int[] numbersResult;
    private static String stringResult;

    public static void start() {

        MenuController.setCountStages(5);

        while (MenuController.getCurrentMenuStage() != -1) {
            switch (MenuController.getCurrentMenuStage()) {
                case 0 -> ChooseInput();
                case 1 -> Input();
                case 2 -> ChooseTask();
                case 3 -> CompleteTask();
                case 4 -> ChooseOutput();
                case 5 -> Output();
            }
        }
    }

    public static void ChooseInput() {
        int variant = chooseFromVariants(
                "Откуда вы хотите ввести массив",
                new String[]{
                        "Из консоли",
                        "Из текствого файла",
                        "Из бинарного файла"
                }
        );
        inputType = ReadWriteType.values()[variant - 1];
        MenuController.goNextStage();
    }

    public static void Input() {
        String pathToFile = "";

        if (inputType == ReadWriteType.console) {
            System.out.println("Введите числа, разделенные пробелами (12 -3 4 6):");

        } else {
            System.out.println("Введите путь к файлу");
            pathToFile = scanner.next();
        }

        try (Reader reader = switch (inputType) {
            case console -> new TextReader(System.in);
            case textFile -> new TextReader(new FileInputStream(pathToFile));
            case binaryFile -> new BinaryReader(new FileInputStream(pathToFile));
        }) {
            intArray = new IntArray(reader.read());
            MenuController.goNextStage();

        } catch (IOException | NumberFormatException ex) {
            if (inputType == ReadWriteType.console) {
                onWrongConsoleInput("Строка введена неверно");
            } else {
                onFileAccessError("Не удалось прочитать информацию из файла");
            }
        }
    }

    public static void ChooseTask() {
        taskId = chooseFromVariants(
                "Выберите задание",
                new String[]{
                        "А - Выберите из заданного множества числа, удовлетворяющие условию, введенному ввиде строки при запуске программы (“>0” или “<10”)",
                        "B - Проверить есть ли в массиве чисел такие: равные/неравные заданному (условие также пользователь вводит в виде строки «=1» или «<>1»)",
                        "C - В строке записаны числа разделенные пробелами, требуется удалить дубликаты",
                        "D - Определить упорядоченность массива чисел (по возрастанию/по убыванию/не упорядочены)"
                }
        );
        MenuController.goNextStage();
    }

    public static void CompleteTask() {
        if (taskId == 1 || taskId == 2) {
            System.out.println("Введите строку с условием (>12, <1, =-1, <>4):");
            String conditionString = scanner.next();
            try {
                if (taskId == 1)
                    numbersResult = intArray.filteredArray(conditionString).getNumbers();
                else
                    stringResult = intArray.checkForCondition(conditionString)
                            ? "Есть"
                            : "Нет";

                MenuController.goNextStage();

            } catch (WrongConditionStringException ex) {
                System.out.println("Строка с условием введена неверно");
            }
        } else {
            if (taskId == 3)
                numbersResult = intArray.filteredDuplicates().getNumbers();
            else
                stringResult = switch (intArray.getOrder()) {
                    case ascending -> "По возрастанию";
                    case descending -> "По убыванию";
                    case unordered -> "Не упорядочен";
                    case unknown -> "Не определено";
                };

            MenuController.goNextStage();
        }
    }

    public static void ChooseOutput() {
        String[] variants;
        if (taskId == 2 || taskId == 4) {
            variants = new String[]{
                    "Консоль",
                    "Текстовый файл"
            };
        } else {
            variants = new String[]{
                    "Консоль",
                    "Текстовый файл",
                    "Бинарный файл"
            };
        }
        int variant = chooseFromVariants(
                "Вывести результат в",
                variants
        );
        outputType = ReadWriteType.values()[variant - 1];
        MenuController.goNextStage();
    }

    public static void Output() {
        String pathToFile = "";

        if (outputType != ReadWriteType.console) {
            System.out.println("Введите путь к файлу");
            pathToFile = scanner.next();
        }

        try (Writer writer = switch (outputType) {
            case console -> new TextWriter(new OutputStreamShield(System.out));
            case textFile -> new TextWriter(
                    new FileOutputStream(pathToFile));
            case binaryFile -> new BinaryWriter(
                    new FileOutputStream(pathToFile));
        }) {
            if (taskId == 1 || taskId == 3) {
                writer.write(numbersResult);
            }
            else {
                writer.write(stringResult);
            }
            MenuController.exit();
        } catch (IOException e) {
            onFileAccessError("Не удалось записать результат в файл");
        }
    }

    public static int inputInt(int min, int max) {
        int num = -1;
        do {
            try {
                num = scanner.nextInt();
            } catch (InputMismatchException ex) {
                scanner.next();
                System.out.println("Введите число");
            }
            if (num < min || num > max)
                System.out.println("Введенное число выходит из диапазона");
        } while (num < min || num > max);

        return num;
    }

    public static int chooseFromVariants(String header, String[] variants) {
        System.out.println(header);
        for (int i = 0; i < variants.length; i++) {
            System.out.printf("\t%d. %s\n", i + 1, variants[i]);
        }
        return inputInt(1, variants.length);
    }

    public static void onFileAccessError(String header) {
        int variant = chooseFromVariants(
                header,
                new String[]{
                        "Попробовать ввести",
                        "Назад",
                        "Выйти"
                }
        );
        switch (variant) {
            case 2:
                MenuController.goPreviousStage();
                break;
            case 3:
                MenuController.exit();
                break;
        }
    }

    public static void onWrongConsoleInput(String header) {
        int variant = chooseFromVariants(
                header,
                new String[]{
                        "Попробовать снова",
                        "Назад",
                        "Выйти"
                }
        );
        switch (variant) {
            case 2:
                MenuController.goPreviousStage();
                break;
            case 3:
                MenuController.exit();
                break;
        }
    }


}

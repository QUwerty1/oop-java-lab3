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

        while (!MenuController.isExited()) {
            switch (MenuController.getCurrentMenuStage()) {
                case 0 -> chooseInput();
                case 1 -> input();
                case 2 -> chooseTask();
                case 3 -> completeTask();
                case 4 -> chooseOutput();
                case 5 -> output();
            }
        }
    }

    public static void chooseInput() {
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

    public static void input() {
        String pathToFile = "";

        if (inputType == ReadWriteType.console) {
            System.out.println("Введите числа, разделенные пробелами (12 -3 4 6):");

        } else {
            System.out.println("Введите путь к файлу");
            pathToFile = scanner.next();
        }

        try (Reader reader = switch (inputType) {
            case console -> new TextReader(
                    System.in);
            case textFile -> new TextReader(
                    new FileInputStream(pathToFile));
            case binaryFile -> new BinaryReader(
                    new FileInputStream(pathToFile));
        }) {
            intArray = new IntArray(reader.read());
            MenuController.goNextStage();

        } catch (IOException | NumberFormatException ex) {
            if (inputType == ReadWriteType.console) {
                onReadWriteError("Строка введена неверно");
            } else {
                onReadWriteError("Не удалось прочитать информацию из файла");
            }
        }
    }

    public static void chooseTask() {
        System.out.println("Массив: " + intArray);
        taskId = chooseFromVariants(
                "Выберите задание",
                new String[]{
                        "А - Выберать числа, удовлетворяющие условию (“>0” или “<10”)",
                        "B - Проверить есть ли в массиве чисел такие: равные/неравные заданному (условие также пользователь вводит в виде строки «=1» или «<>1»)",
                        "C - Удалить дубликаты",
                        "D - Определить упорядоченность массива чисел (по возрастанию/по убыванию/не упорядочены)",
                        "Выйти"
                }
        );
        if (taskId == 5)
            MenuController.exit();
        else
            MenuController.goNextStage();
    }

    public static void completeTask() {
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
                stringResult = intArray.getOrder().toString();

            MenuController.goNextStage();
        }
    }

    public static void chooseOutput() {
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

    public static void output() {
        String pathToFile = "";

        if (outputType != ReadWriteType.console) {
            System.out.println("Введите путь к файлу");
            pathToFile = scanner.next();
        }

        try (Writer writer = switch (outputType) {
            case console -> new TextWriter(
                    new OutputStreamShield(System.out));
            case textFile -> new TextWriter(
                    new FileOutputStream(pathToFile));
            case binaryFile -> new BinaryWriter(
                    new FileOutputStream(pathToFile));
        }) {
            if (taskId == 1 || taskId == 3) {
                writer.write(numbersResult);
                writer.write("\n");

                intArray = new IntArray(numbersResult);
            } else {
                writer.write(stringResult);
            }
            MenuController.goBackStages(3);
        } catch (IOException e) {
            onReadWriteError("Не удалось записать результат в файл");
        }
    }

    public static int inputInt(int min, int max) {
        int num = -1;
        do {
            try {
                num = scanner.nextInt();
                if (num < min || num > max)
                    System.out.println("Введенное число выходит из диапазона");
            } catch (InputMismatchException ex) {
                scanner.next();
                System.out.println("Введите число");
            }
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

    public static void onReadWriteError(String header) {
        int variant = chooseFromVariants(
                header,
                new String[]{
                        "Попробовать снова",
                        "Назад",
                        "Выйти"
                }
        );
        switch (variant) {
            case 2 -> MenuController.goPreviousStage();
            case 3 -> MenuController.exit();
        }
    }
}
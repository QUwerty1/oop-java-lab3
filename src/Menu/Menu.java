package Menu;

import IntArray.IntArray;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    public static Scanner scanner;
    private static int currentMenuStage = 0;
    private static int countStages = 1;

    public static void setCountStages(int newCountStages) throws StageOutOfBoundsException {
        if (newCountStages < 0)
            throw new StageOutOfBoundsException(
                    "countStages не может быть меньше 0");
        countStages = newCountStages;
    }

    public static int getCurrentMenuStage() {
        return currentMenuStage;
    }

    public static void goNextStage() throws StageOutOfBoundsException {
        if (currentMenuStage + 1 > countStages)
            throw new StageOutOfBoundsException(
                    "currentMenuStage не может быть больше, чем countStages");

        if (currentMenuStage != -1)
            currentMenuStage++;
    }

    public static void goPreviousStage() throws StageOutOfBoundsException {
        if (currentMenuStage - 1 < 0)
            throw new StageOutOfBoundsException(
                    "currentMenuStage не может быть меньше, чем 0");

        currentMenuStage--;
    }

    public static void goBackStages(int stages) throws StageOutOfBoundsException {
        if (currentMenuStage - stages < 0)
            throw new StageOutOfBoundsException(
                    "currentMenuStage не может быть меньше, чем 0");
        currentMenuStage -= stages;
    }

    public static void exit() {
        currentMenuStage = -1;
    }

    public static int showGetIntManu() {
        boolean isRightVal = false;
        int num = -1;
        do {
            try {
                num = scanner.nextInt();
                isRightVal = true;
            } catch (InputMismatchException exception) {
                scanner.next();
                System.out.println("Введите число");
            }
        } while (!isRightVal);
        return num;
    }

    public static int showGetIntManu(int min, int max) {
        int num;
        do {
            num = showGetIntManu();
            if (num < min || num > max)
                System.out.println("Значение выходит из дапазона");
        } while (num < min || num > max);
        return num;
    }

    public static int showChoicesMenu(String header, String[] choices) {
        System.out.println(header);
        for (int i = 0; i < choices.length; i++) {
            System.out.printf("\t%d. %s\n", i + 1, choices[i]);
        }
        return showGetIntManu(1, choices.length);
    }

    public static ReadWriteType showDataTypeMenu(
            String message, boolean isHideBinary) {
        String[] choices =
                isHideBinary ? new String[]{
                        "Консоль",
                        "Бинарный файл",
                } : new String[]{
                        "Консоль",
                        "Бинарный файл",
                        "Текстовый файл"
                };
        int typeId = showChoicesMenu(message, choices);
        return ReadWriteType.values()[typeId - 1];
    }

    public static String onFileAccessFail(String path, int stagesBack) {
        int choice = showChoicesMenu(
                "Выберите одно и следующих действий",
                new String[]{
                        "Попробовать еще раз",
                        "Изменить путь к файлу",
                        "Назад",
                        "Выйти из программы"
                }
        );
        switch (choice) {
            case 2: {
                System.out.println("Введите путь к файлу");
                path = scanner.next();
                break;
            }
            case 3: {
                goBackStages(stagesBack);
                break;
            }
            case 4: {
                exit();
            }
        }
        return path;
    }

    public static void onWrongConsoleInput(int stagesBack) {
        int choice = showChoicesMenu(
                "Выберите одно из следующих действий",
                new String[]{
                        "Попробовать ещё раз",
                        "Назад",
                        "Выйти из программы"
                }
        );
        switch (choice) {
            case 2: {
                goBackStages(stagesBack);
                break;
            }
            case 3: {
                exit();
            }
        }
    }

    public static String showInputArrayMenu(ReadWriteType inputType) {
        String path = "";
        if (inputType == ReadWriteType.textFile || inputType == ReadWriteType.binaryFile) {
            System.out.println("Введите путь к файлу");
            path = scanner.next();
        } else {
            System.out.println("Введите числа, разделенные пробелами");
        }
        return path;
    }

    public static int showTaskSelectMenu(IntArray intArray, int stagesBack) {
        System.out.printf("""
                \nМассив чисел: %s
                """, intArray);
        int choice = showChoicesMenu(
                "Выберите задание",
                new String[]{
                        "A",
                        "B",
                        "C",
                        "D",
                        "Назад",
                        "Выход"
                }
        );
        if (choice == 5) {
            goBackStages(stagesBack);
        }
        else if (choice == 6) {
            exit();
        }
        return choice;
    }

    static public String showInputConditionStringMenu() {
        System.out.println("Введите строку с условием [<1; >2; =3; <>4]");
        return scanner.next();
    }

    static public String showCompleteTasksMenu(IntArray intArray, int taskId, String conditionString) {
        String stringResult = "";
        switch (taskId) {
            case 1: {
                try {
                    intArray = intArray.filteredArray(conditionString);
                } catch (WrongConditionStringException e) {
                    System.out.println("Строка введена неверно");
                    goPreviousStage();
                }
                break;
            }
            case 2: {
                try {
                    stringResult = intArray.checkForCondition(conditionString)
                            ? "Есть" : "Нет";
                } catch (WrongConditionStringException e) {
                    System.out.println("Строка введена неверно");
                    goPreviousStage();
                }
                break;
            }
            case 3: {
                intArray = intArray.filteredDuplicates();
                break;
            }
            case 4: {
                stringResult = intArray.getOrder().toString();
            }
        }
        return stringResult;
    }
}

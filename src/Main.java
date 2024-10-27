import IntArray.IntArray;
import Menu.*;
import Reader.*;
import Writer.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Menu.setCountStages(8);
        Menu.scanner = new Scanner(System.in);

        IntArray intArray = new IntArray();
        ReadWriteType inputType = ReadWriteType.console;
        ReadWriteType outputType = ReadWriteType.console;

        int taskId = -1;
        String filePath = "";
        String conditionString = "";
        String stringContent = "";

        while (Menu.getCurrentMenuStage() != -1) {
            switch (Menu.getCurrentMenuStage()) {
                case 0: {
                    inputType = Menu.showDataTypeMenu(
                            "Выберите, с помощью чего вы хотите ввести массив?",
                            false
                    );
                    Menu.goNextStage();
                    break;
                }
                case 1: {
                    filePath = Menu.showInputArrayMenu(inputType);
                    Menu.goNextStage();
                    break;
                }
                case 2: {
                    try {
                        Reader reader = switch (inputType) {
                            case console -> new TextReader(System.in);
                            case textFile -> new TextReader(
                                    new FileInputStream(filePath));
                            case binaryFile -> new BinaryReader(
                                    new FileInputStream(filePath));
                        };
                        intArray = reader.read();
                        Menu.goNextStage();

                    } catch (IOException | NumberFormatException | NullPointerException exception) {
                        if (inputType == ReadWriteType.console) {
                            System.out.println("Вы ввели некорректное значение");
                            Menu.onWrongConsoleInput(2);
                        } else {
                            System.out.println("Не удалось прочитать файл");
                            filePath = Menu.onFileAccessFail(filePath, 2);
                        }
                        break;
                    }
                }
                case 3: {
                    taskId = Menu.showTaskSelectMenu(intArray, 3);
                    if (taskId < 5)
                        Menu.goNextStage();
                    break;
                }
                case 4: {
                    if (taskId == 3 || taskId == 4)
                        Menu.goNextStage();

                    conditionString = Menu.showInputConditionStringMenu();
                    Menu.goNextStage();
                    break;
                }
                case 5: {
                    stringContent = Menu.showCompleteTasksMenu(intArray, taskId, conditionString);
                    Menu.goNextStage();
                    break;
                }
                case 6: {
                    outputType = Menu.showDataTypeMenu(
                            "Куда вы хотите вывести результат?",
                            true
                    );
                    Menu.goNextStage();
                    break;
                }
                case 7: {
                    if (outputType == ReadWriteType.textFile ||
                            outputType == ReadWriteType.binaryFile) {

                        System.out.println("Введите путь к файлу");
                        filePath = Menu.scanner.next();
                    }
                    Menu.goNextStage();
                    break;
                }
                case 8: {
                    try (Writer writer = switch (outputType) {
                        case console -> new TextWriter(new OutputStreamShield(System.out));
                        case textFile -> new TextWriter(
                                new FileOutputStream(filePath));
                        case binaryFile -> new BinaryWriter(
                                new FileOutputStream(filePath));
                    }) {
                        if (taskId == 1 || taskId == 3) {
                            writer.write(intArray);
                        } else {
                            writer.write(stringContent);
                        }
                        Menu.goBackStages(5);

                    } catch (IOException | NumberFormatException | NullPointerException exception) {

                        System.out.println("Не удалось записать файл");
                        filePath = Menu.onFileAccessFail(filePath, 2);
                        break;
                    }
                }
            }
        }
    }
}
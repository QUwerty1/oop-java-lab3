package Menu;

public class MenuController {
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

    public static void exit() {
        currentMenuStage = -1;
    }
}

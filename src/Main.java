import Menu.Menu;

public class Main {
    public static void main(String[] args) {

        var menu = new Menu();
        menu.selectTaskMenu();
        menu.selectInputMenu();
        menu.readFileMenu();
        menu.completeTasks();
        menu.selectOutputTypeMenu();
        menu.writeFileMenu();
    }
}
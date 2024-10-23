package IntArray;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.IntPredicate;

public class IntArray implements Cloneable {

    private int[] numbers;

    public IntArray() { 
        numbers = new int[0];
    }

    public IntArray(int[] numbers) {
        this.numbers = numbers;
    }

    /**
     * @param numbersString Строка, содержащая числа в диапазоне от -2^31 до 2^31-1, разделенные пробелами
     */
    public IntArray(String numbersString) {
        parseString(numbersString);
    }

    public void setNumbers(int[] numbers) {
        System.arraycopy(numbers, 0, this.numbers, 0, numbers.length);
    }

    public int[] getNumbers() {
        return this.numbers;
    }

    /**
     * @return Новый массив чисел, значения которого идентичны значениям поля numbers
     */
    public int[] cloneNumbers() {
        int[] newNumbers = new int[this.numbers.length];
        System.arraycopy(this.numbers, 0, newNumbers, 0, this.numbers.length);
        return newNumbers;
    }


    /**
     * @param conditionString Услоиве фильтрации. Строка, содержащая знак из набора (>, <, =, <>) и число из диапазона от -2^31 до 2^31-1
     * @return Массив чисел, подходящих под условие
     */
    public IntArray filteredArray(String conditionString) throws IllegalStateException{
        int target;
        IntPredicate condition;

        switch (conditionString.charAt(0)) {
            case '>':
                target = Integer.parseInt(conditionString.substring(1));
                condition = i -> i > target;
                break;

            case '=':
                target = Integer.parseInt(conditionString.substring(1));
                condition = i -> i == target;
                break;

            case '<':
                if (conditionString.charAt(2) == '>') {
                    target = Integer.parseInt(conditionString.substring(2));
                    condition = i -> i != target;
                } else {
                    target = Integer.parseInt(conditionString.substring(1));
                    condition = i -> i < target;
                }
                break;

            default:
                throw new IllegalStateException("Первым символом должен быть знак из набора (>, <, =, <>)");
        }
        return new IntArray(Arrays.stream(this.numbers).filter(condition).toArray());
    }

    /**
     * @param numbersString Услоиве фильтрации. Строка, содержащая знак из набора (>, <, =, <>) и число из диапазона от -2^31 до 2^31-1
     */
    public void parseString(String numbersString) throws NumberFormatException{
        String[] strNumbers = numbersString.split(" ");
        this.numbers = new int[strNumbers.length];

        for (int i = 0; i < strNumbers.length; i++) {
            this.numbers[i] = Integer.parseInt(strNumbers[i]);
        }
    }

    /**
     * @param conditionString Услоиве фильтрации. Строка, содержащая знак из набора (>, <, =, <>) и число из диапазона от -2^31 до 2^31-1
     * @return Есть ли в массиве числа, удавлетворяющие условию
     */
    public boolean checkForCondition(String conditionString) throws IllegalStateException {
        return this.filteredArray(conditionString).numbers.length > 0;
    }


    /**
     * @return Массив чисел, не содержащий дубликатов
     */
    public IntArray filteredDuplicates() {
        var set = new HashSet<Integer>();
        int[] filteredNumbers = new int[numbers.length];
        int cnt = 0;
        for (int number : numbers) {
            if (!set.contains(number)) {
                filteredNumbers[cnt] = number;
                set.add(number);
                cnt++;
            }
        }
        int[] res = new int[cnt];
        System.arraycopy(filteredNumbers, 0, res, 0, cnt);

        return new IntArray(res);
    }

    /**
     * @return Упорядоченность массива
     */
    public OrderType getOrder() {
        int shift = 0;
        OrderType order;

        while (shift + 1 < numbers.length && numbers[shift] == numbers[shift + 1]) {
            shift++;
        }

        if (shift + 1 == numbers.length) order = OrderType.unknown;

        else if (numbers[shift] > numbers[shift + 1]) {
            int previous = numbers[0];
            boolean isDESC = true;
            for (int i = 1; i < numbers.length; i++) {
                if (previous >= numbers[i]) previous = numbers[i];
                else {
                    isDESC = false;
                    break;
                }
            }
            order = isDESC ? OrderType.descending : OrderType.unordered;
        } else {
            int previous = numbers[0];
            boolean isACS = true;
            for (int i = 1; i < numbers.length; i++) {
                if (previous <= numbers[i]) previous = numbers[i];
                else {
                    isACS = false;
                    break;
                }
            }
            order = isACS ? OrderType.ascending : OrderType.unordered;
        }

        return order;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int el : numbers) {
            res.append(el).append(" ");
        }
        return res.toString();
    }

    @Override
    public IntArray clone() throws CloneNotSupportedException {
        IntArray intArray = (IntArray) super.clone();
        intArray.setNumbers(cloneNumbers());
        return intArray;
    }
}

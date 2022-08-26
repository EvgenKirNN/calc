import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        //String input = " 6+   2";
        System.out.println("Введите выражение для вычисления суммы, разности, произведения или частного от деления без остатка\nдвух натуральных чисел от 1 до 10 включительно.\nИспользуйте арабские или римские числа. Для выхода просто нажмите клавишу Ввод:");
        Scanner in = new Scanner(System.in);
        while (true) {
            String input = in.nextLine();
            if (input.isEmpty()) { break;}
            System.out.println(calc(input));
        }
        in.close();
    }
    public static String calc(String input) {

        input = input.toUpperCase();
        input = input.replaceAll("\\s",""); //убираем пробелы

        char[] inputChar = input.toCharArray();
        char[] operators = {'+', '-', '*', '/'};
        char operator = 0;

        int[] num = new int[2];

        String[] arabNums = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] romNums = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

        int count = 0;
        for (int i = 0; i < operators.length; i++) {
            for (int j=0; j < inputChar.length; j++) {
                if (inputChar[j] == operators[i]) {
                    count += 1;
                    if (count == 2) {throw new RuntimeException("Ошибка! Должен быть один оператор и два операнда");}
                    operator = operators[i];
                }
            }
        }
        if (count == 0) { throw new RuntimeException("Ошибка! Строка не является математическим выражением");}

        //String[] nums = input.split(String.valueOf(Pattern.quote(operator)));
        String[] nums = input.split(Pattern.quote(String.valueOf(operator)));
        int countArab = 0, countRom = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 10; j++) {
                if (nums[i].equals(arabNums[j])) {num[i] = j + 1; countArab += 1;}
                if (nums[i].equals(romNums[j])) {num[i] = j + 1; countRom +=1;}
            }
        }

        if (countArab == 1 && countRom == 1) { throw new RuntimeException("Ошибка! Используются разные системы счисления");}
        if (countArab == 1 || countRom == 1) { throw new RuntimeException("Ошибка! Недопустимые числа");}

        int result = 0;
        switch (operator) {
            case '+':
                result = (int)(num[0] + num[1]);
                break;
            case '-':
                result = (int)(num[0] - num[1]);
                if (countRom == 2 && result < 1) { throw new RuntimeException("Ошибка! Недопустимый ответ. Римские числа могут быть только положительными");}
                break;
            case '*':
                result = (int)(num[0] * num[1]);
                break;
            case '/':
                result = (int)(num[0] / num[1]);
                if (countRom == 2 && (num[0] % num[1]) != 0) { throw new RuntimeException("Ошибка! Недопустимый ответ. Римские числа могут быть только целыми");}
                break;
        }

        if (countRom == 2) {
            List<RomNumber> romNumbers = RomNumber.getListOfRomNumber();
            String s = "";
            int i = romNumbers.size() - 1;
            while (result > 0 && i >= 0) {
                RomNumber currentNumber = romNumbers.get(i);
                if (currentNumber.getValue() <= result) {
                    s = s.concat(currentNumber.name());
                    result -= currentNumber.getValue();
                }
                else { i--; }
            }
            return s;
        }
        return String.valueOf(result);
    }
    enum RomNumber {
        I(1), IV(4), V(5), IX(9), X(10), XL(40), L(50), XC(90), C(100);
        int value;
        RomNumber(int value) {
            this.value = value;
        }
        int getValue() { return value; }
        static List<RomNumber> getListOfRomNumber() {
            return Arrays.stream(values()).collect(Collectors.toList());
        }
    }
}
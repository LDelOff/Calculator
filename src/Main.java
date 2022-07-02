import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws MyException {
        // Чтение строки
        Scanner in = new Scanner(System.in);
        System.out.println("Введите выражение:");
        String inputExpression = in.nextLine();
        // Основной обработчик строки
        String outputExpression = calc(inputExpression);
        // Вывод результата
        System.out.println(outputExpression);
    }

    public static String calc(String input) throws MyException // метод
    {
        // Уберем лишние пробелы
        input = input.trim();
        // Определим, какие операции у нас будут
        char[] signs = new char[4];
        signs[0] = '+';
        signs[1] = '-';
        signs[2] = '*';
        signs[3] = '/';
        int count = 0; // счётчик математических операций
        char signInd = '0'; // индекс математической операции
        String[] strings = new String[2];
        for (char i : signs) {
            if (input.indexOf(i) != -1) { // фиксируем наличие оператора, заодно и строчку поделим
                count++;
                if ((i == '+') || (i == '*')) {
                    String[] temp;
                    temp = input.split("\\" + i);
                    if (temp.length == 2) {
                        strings = temp;
                        signInd = i;
                    }
                } else {
                    String[] temp;
                    temp = input.split("" + i);
                    if (temp.length == 2) {
                        strings = temp;
                        signInd = i;
                    }
                }
            }
            // далее посмотрим, не повторяется ли оператор (лучше всего работает, если у нас в строке не более
            // 2‑х одинаковых операторов)
            if (input.indexOf(i) != input.lastIndexOf(i)) {
                count++;
            }
        }
        if (count == 0) {
            throw new MyException("строка не является математической операцией");
        } else if (count > 1) {
            throw new MyException("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }
        // инициализация чисел
        Number a = new Number();
        a.createNumber(strings[0]);
        Number b = new Number();
        b.createNumber(strings[1]);

        if (a.isRoman != b.isRoman) {
            throw new MyException("используются одновременно разные системы счисления");
        }

        Number c = new Number();
        switch (signInd) {
            case '+':
                c.numDecimal = a.numDecimal + b.numDecimal;
                break;
            case '-':
                c.numDecimal = a.numDecimal - b.numDecimal;
                break;
            case '*':
                c.numDecimal = a.numDecimal * b.numDecimal;
                break;
            case '/':
                c.numDecimal = a.numDecimal / b.numDecimal;
        }


        String ans = "";

        if (a.isRoman) {
            if (c.numDecimal < 1) {
                throw new MyException("в римской системе нет отрицательных чисел и нуля");
            } else {
                c.numRoman = c.decimal2Roman(c.numDecimal);
                ans = c.numRoman;
            }
        } else {
            ans = String.valueOf(c.numDecimal);
        }
        return ans;
    }
}

class Number {
    int numDecimal;         // Число в десятичной СС
    String numRoman;      // Число в римской СС
    boolean isRoman; // Принадлежность к римской СС

    void createNumber(String str) {
        str = str.trim();
        // пробуем в int перевести
        try {
            numDecimal = Integer.parseInt(str);
            if (numDecimal < 1 || numDecimal > 10) {
                throw new NumberFormatException("Калькулятор должен принимать на вход числа от 1 до 10 включительно");
            }
            numRoman = decimal2Roman(numDecimal);
            isRoman = false;
        } catch (NumberFormatException e) {
            numDecimal = roman2Decimal(str);
            if (numDecimal < 1 || numDecimal > 10) {
                throw new NumberFormatException("Калькулятор должен принимать на вход числа от 1 до 10 включительно");
            }
            numRoman = str;
            isRoman = true;
        }
    }

    int charToNumber(char ch) { // символ в цифру (на входе числа от 1(I) до 10(X))
        switch (ch) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            default:
                throw new NumberFormatException("Invalid format");
        }
    }

    int roman2Decimal(String roman) { // Алгоритм перевода из римской в арабскую
        if (roman.length() == 0)
            return 0;
        int integerValue = 0;
        int prevNumber = charToNumber(roman.charAt(0));
        for (int i = 1; i < roman.length(); i++) {
            char ch = roman.charAt(i);
            int number = charToNumber(ch);
            if (number <= prevNumber)
                integerValue += prevNumber;
            else
                integerValue -= prevNumber;
            prevNumber = number;
        }
        integerValue += prevNumber;
        return integerValue;
    }

    String decimal2Roman(int dec) { // Алгоритм перевода из арабских в римские (упрощён до 100)
        // 1 - I  // 5 - V  // 10 - X // 50 - L // 100 - C //
        String result = "";
        // выделяем сотни
        int a100 = dec / 100;
        for (int i = 0; i < a100; i++) {
            result = result + 'C';
        }
        int b100 = dec % 100;
        // выделяем десятки
        int a10 = b100 / 10;
        switch (a10) {
            case 1, 2, 3:
                for (int i = 0; i < a10; i++) {
                    result = result + 'X';
                }
                break;
            case 4:
                result = result + "XL";
                break;
            case 5, 6, 7, 8:
                result = result + "L";
                for (int i = 0; i < a10 - 5; i++) {
                    result = result + 'X';
                }
                break;
            case 9:
                result = result + "XC";
        }
        int b10 = b100 % 10;

        String[] b = {
                "",
                "I",
                "II",
                "III",
                "IV",
                "V",
                "VI",
                "VII",
                "VIII",
                "IX"
        };
        return result + b[b10];
    }
}

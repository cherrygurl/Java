import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Arrays;

class RomanArabicCalc {
    static Scanner scanner = new Scanner(System.in);
    static String num1, num2;       // аргументы из строки
    static int number1, number2;    // аргументы преобразованные к int
    static char operation;          // знак арифметической операции
    static int result;

    // вспомогательные массивы для конвертации римских цифр 
    static int[] romanValues = {1000,900,500,400,100,90,50,40,10,9,8,7,6,5,4,3,2,1};
    static String[] romanLetters = {"M","CM","D","CD","C","XC","L","XL","X","IX","VIII","VII","VI","V","IV","III","II","I"};

    // типы чисел
    enum numType {
        ARABIC,
        ROMAN,
        UNDEFINED
    };

    // функция для преобразования арабских цифр в римские
    private static String IntToRoman(int num) {

        StringBuilder result = new StringBuilder();

        for(int i = 0; i < romanValues.length; i++) {
            while(num >= romanValues[i]) {
                num -= romanValues[i];
                result.append(romanLetters[i]);
            }
        }
        return result.toString();
    }

    // функция для преобразования римских цифр в арабские
    private static int RomanToInt(String num) {

        String curChar;
        int idx;

        // проверяем формат
        for(int i = 0; i < num.length(); i++) {
            curChar = String.valueOf(num.charAt(i));
            idx = Arrays.asList(romanLetters).indexOf(curChar);
            if(idx<0)
                throw new NumberFormatException("Формат римского числа неверный");
        }
        // конвертируем
        idx = Arrays.asList(romanLetters).indexOf(num);
        return (idx >= 0) ? romanValues[idx] : -1;
    }

    // функция для определения типа вводимых данных
    public static numType GetNumType(String num) {
        try {
            Integer.parseInt(num);
            return numType.ARABIC;
        } catch (NumberFormatException e) {
            try {
                RomanToInt(num);
                return numType.ROMAN;
            } catch(NumberFormatException e2) {
                return numType.UNDEFINED;
            } 
        }
    }

    // функция вычисляющая арифметическую операцию над числами
    public static int doCalc(int num1, int num2, char op) {
        int result = 0;
        switch (op) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                result = num1 / num2;
                break;
            default:
                throw new IllegalArgumentException("Неверный знак операции!");
        }
        return result;
    }

    // функция вычисляющая выражение из строки
    public static String calc(String input) {
        int result;
        char curChar;
        int operationCount = 0;
        
        // убираем пробелы
        String inputTrim = input.replaceAll("\\s","");

        // ищем и сохраняем не больше одного знака операции и числа до и после знака 
        for (int i = 0; i < inputTrim.length(); i++) {
            curChar = inputTrim.charAt(i);
            if(curChar == '+' || curChar == '-' || curChar == '*' || curChar == '/') {
                operationCount++;
                if(operationCount>1)
                    throw new IllegalArgumentException("Формат математической операции не удовлетворяет заданию - два операнда и один оператор");
                operation = curChar;
                num1 = inputTrim.substring(0, i);
                num2 = inputTrim.substring(i+1);
            }
        }

        // исключение: операций больше одной, только одно число на вход
        if(operationCount < 1 || num2.length() == 0)
            throw new IllegalArgumentException("Строка не является математической операцией");
        
        // проверяем, что системы счисления одинаковые и нет неизвестных знаков
        numType n1 = GetNumType(num1);
        numType n2 = GetNumType(num2);

        if(n1 == numType.UNDEFINED || n2 == numType.UNDEFINED) 
            throw new IllegalArgumentException("Допустимы только арабские или римские цифры");
        
        if(n1 != n2)
            throw new IllegalArgumentException("Используются одновременно разные системы счисления");
        

        // преобразуем числа к int
        if(n1 == numType.ARABIC) {
            number1 = Integer.parseInt(num1);
            number2 = Integer.parseInt(num2);
        } else {
            number1 = RomanToInt(num1);
            number2 = RomanToInt(num2);
        }

        // проверяем, что введенные числа в диапазоне от 1 до 10
        if(number1 > 10 || number2 > 10 || number1 < 1 || number2 < 1) {
            throw new IllegalArgumentException("Вводимые числа должны быть от 1 до 10!");
        }

        // вычисляем результат
        result = doCalc(number1, number2, operation);

        if (result<0 && n1 == numType.ROMAN) 
            throw new NumberFormatException("В римской системе нет отрицательных чисел");
        
        return (n1 == numType.ROMAN) ? IntToRoman(result) : Integer.toString(result);
    }

    // приложение "Калькулятор"
    public static void main (String[] args) {
        System.out.println("Введите выражение (напр. 2+2) или два римских числа от I до X (напр. V+V) + Enter: ");
        
        // сканируем строку 
        String userIn = scanner.nextLine();
        
        String result = calc(userIn);

        System.out.println(num1 + " " + operation + " " + num2 + " = " + result);
    }
}

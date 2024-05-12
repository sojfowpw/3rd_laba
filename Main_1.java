import static java.lang.Math.*;

public class Main_1 {
    public static void main(String[] args) {
        double dx = 0.5; // шаг
        System.out.println("_______________________");
        System.out.println("|     X    |     Y    |"); // заголовок таблицы
        for (double x = -5.0; x <= 5.0; x += dx) { // проходимя по графику с шагом 0,5
            if (x == 0) { // исключаем 0
                continue;
            }
            System.out.println("-----------------------");
            System.out.printf("|%10.1f|%10f|\n", x, graphOfFunction(x)); // вывод в табличной форме
        }
        System.out.println("-----------------------");
    }

    public static double graphOfFunction(double x) { // график функции
        double result = 0;
        if (x <= -2.0) { // функция на промежутке от -5 до -2
            result = x * (-1.0 / 3.0) - 2.0 / 3.0;
        }
        else if (x > -2.0 && x < 2.0) { // функция на промежутках от -2 до 0 и от 0 до 2
            result = log(abs(1.0 / tan(x / 2.0)));
        }
        else if (x >= 2.0) { // функция на промежутке от 2 до 5
            result = x * (1.0 / 3.0) - 2.0 / 3.0;
        }
        return result;
    }
}
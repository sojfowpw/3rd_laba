import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Main_2_3 {
    static ArrayList<Integer> Eratosthenes(int size) { // генерация простых чисел методом Эратосфена
        ArrayList<Integer> prime_dig = new ArrayList<>(); // массив для простых чисел
        prime_dig.add(2);
        for (int i = 3; i <= size; i++) {
            boolean is_prime = true;
            for (int j = 0; j < prime_dig.size(); j++) {
                if (i % prime_dig.get(j) == 0) { // если число не делится на все предыдущие числа в векторе простых чисел, то оно простое
                    is_prime = false;
                    break;
                }
            }
            if (is_prime) { // если условие верно, то число простое => добавляем в массив
                prime_dig.add(i);
            }
        }
        return prime_dig;
    }

    static double algRandom(double a, double n) { // генерация случайного числа в диапазоне от a до n
        Random rand = new Random();
        double randDig = rand.nextDouble() * (n - a) + a;
        randDig = Math.round(randDig * 10.0) / 10.0;
        return randDig;
    }

    static int modPow(int base, int exponent, int module) { // возведение в степень по модулю
        int result = 1;
        base %= module; // привидение основания по модулю
        while (exponent > 0) {
            if (exponent % 2 == 1) { // если степень нечётная, уменьшаем её на единицу, сохраняем результат
                result = result * base % module; // умножаем по основанию и берём по модулю
                exponent--;
            }
            exponent /= 2;
            base = base * base % module; // если степень чётная, предыдущее значение возводим в квадрат
        }
        return result;
    }

    static int gost(int q, int bit) { // реализация госта
        int p; // простое число
        double z, N; // z - случайная величина от 0 до 1
        while (true) {
            z = algRandom(0, 1);
            N = Math.pow(2, bit - 1) / q + Math.pow(2, bit - 1) * z / q;
            if ((int) N % 2 != 0) { // если N - нечётное, добавляем 1
                N++;
            }
            for (int u = 0; true; u += 2) {
                p = (int) (N + u) * q + 1; // построение кандидата в простые числа
                if (p > Math.pow(2, bit)) { // проверка условия размерности
                    break;
                }
                if (modPow(2, p - 1, p) == 1 && modPow(2, (int) N + u, p) != 1) { // проверка условий: 2^(p-1) mod p = 1 и 2^(N+u) mod p != 1
                    return p;
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите количество бит: ");
        int bit = in.nextInt(); // количество бит
        ArrayList<Integer> prime_digits = Eratosthenes(500);
        int index = (int) algRandom(0.0, (double) prime_digits.size() - 10); // генерация случайного стартового индекса
        for (int i = 1; i <= 10; i++) {
            System.out.println(i + ":    " + gost(prime_digits.get(index + i), bit));
        }
    }
}

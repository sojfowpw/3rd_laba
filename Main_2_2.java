import java.util.*;

public class Main_2_2 {
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

    static int algRandom(int a, int n) { // генерация рандомного числа в диапазоне от а до n
        Random rand = new Random();
        return rand.nextInt(n - a + 1) + a;
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

    static int PoklingtonTest(int n, int t, ArrayList<Integer> prime_digits) { // тест Поклингтона
        int digit = n - 1;
        ArrayList<Integer> decomp = new ArrayList<>(); // массив для канонического разложения
        ArrayList<Integer> aj = new ArrayList<>(); // массив случайных целых чисел
        ArrayList<Integer> prime = Eratosthenes(3000); // массив простых чисел до 3000
        for (int i = 0; i < t; i++) {
            aj.add(algRandom(2, n)); // генерация случайного числа
            if (modPow(aj.get(i), n - 1, n) != 1) { // проверка aj ^ (n - 1) mod n
                return 0;
            }
        }
        for (int i = 0; digit > 1;) {
            if (digit % prime.get(i) == 0) {
                digit /= prime.get(i);
                decomp.add(prime.get(i)); // разложение числа на простые множители
            }
            else {
                i++;
            }
        }
        for (int j : aj) {
            int count = 0;
            for (int qi : decomp) {
                if (modPow(j, (n - 1) / qi, n) == 1) { // проверка a ^ ((n - 1) / q) mod n
                    count++; // счётчик результатов
                }
            }
            if (count == decomp.size()) { // если все результаты не равны 1, число простое
                return 1;
            }
        }
        return 0;
    }

    static int PoklingtonBuild(int bit, ArrayList<Integer> prime_digits, int t, int[] F) { // построение простого числа
        int maxdegree, degree, a; // maxdegree - максимально возможная степень, degree - случайная степень, a - степень
        int k; // k - количество отвергнутых чисел, определённых вероятностным тестом, как простые
        int R, bitF, n; // bitF - количество бит числа F
        while (true) {
            F[0] = 1;
            R = 1;
            bitF = 0;
            k = 0;
            for (int i = 0; i < prime_digits.size(); i++) { // цикл по элементам массива
                if (prime_digits.get(i) > Math.pow(2, bit / 2 + 1) - 1) { // проверка условия: число на 1 бит больше половины требуемого размера для простого числа
                    break;
                }
                for (maxdegree = 1; Math.pow(prime_digits.get(i), maxdegree) <= Math.pow(2, bit / 2 + 1); maxdegree++) {} // вычисление максимально возможной степени
                degree = algRandom(1, maxdegree - 1); // генерация случайной степени
                a = algRandom(0, degree); // генерация степени
                F[0] *= Math.pow(prime_digits.get(i), a); // построение числа F
                if (F[0] > Math.pow(2, bit / 2 + 1) - 1) { // проверка условия: число на 1 бит больше половины требуемого размера для простого числа
                    F[0] /= Math.pow(prime_digits.get(i), a); // если условие не выполнено, уменьшаем F
                }
            }
            bitF = (int) Math.ceil(Math.log(F[0]) / Math.log(2.0)); // вычисление количества бит числа F
            while (R % 2 != 0) {
                R = algRandom((int) Math.pow(2, bitF - 2), (int) Math.pow(2, bitF - 1)); // строим число R
            }
            n = R * F[0] + 1; // построение числа n
            if (PoklingtonTest(n, t, prime_digits) == 0 && PoklingtonTest(n, 1, prime_digits) == 1) {
                k++; // подсчёт количества отвергнутых чисел, определённых вероятностным тестом, как простые
            }
            if (PoklingtonTest(n, t, prime_digits) == 1) { // проверка на простоту
                System.out.print(k + " "); // вывод количества отвергнутых чисел, определённых вероятностным тестом, как простые
                break;
            }
        }
        return n;
    }

    static double probability(int[] F) { // вероятностный тест
        ArrayList<Integer> prime = Eratosthenes(3000); // массив с простыми числами до 3000
        ArrayList<Integer> decomp = new ArrayList<>(); // каноническое разложение числа
        double prob = 1.0;
        for (int i = 0; F[0] > 1;) {
            if (F[0] % prime.get(i) == 0) {
                F[0] /= prime.get(i);
                decomp.add(prime.get(i)); // разложение числа на простые множители
            }
            else {
                i++;
            }
        }
        for (int i : decomp) {
            prob *= (1.0 - 1.0 / i); // вероятность
        }
        return prob;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите количество бит: ");
        int bit = in.nextInt(); // колество бит
        int digit; // сгенерированное число
        int F[] = {1};
        ArrayList<Integer> prime_digits = Eratosthenes(500); // массив с простыми числами до 500
        for (int i = 1; i <= 10; i++) {
            System.out.print(i + ":    k = ");
            digit = PoklingtonBuild(bit, prime_digits, 6, F); // построение числа с параметром надёжности - 6
            if (probability(F) <= 0.1) { // если вероятность <= 0.1, то число прошло проверку
                System.out.println(digit + " +");
            } else {
                System.out.println(digit + " -");
            }
        }
    }
}

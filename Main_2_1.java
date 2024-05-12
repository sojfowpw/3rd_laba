import java.util.*;

public class Main_2_1 {
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

    static int MillerTest(int n, int t, ArrayList<Integer> prime_digits) { // тест Миллера
        int a = 0, result = 1, count = 0; // a - случайное целое число, result - флаг (1 - простое), count - счётчик для aj ^ (n - 1) mod n != 1
        ArrayList<Integer> decomp = new ArrayList<>(); // массив для канонического разложения
        ArrayList<Integer> aj = new ArrayList<>(); // массив случайных целых чисел
        ArrayList<Integer> prime = Eratosthenes(3000); // массив простых чисел до 3000
        for (int i = 0; i < t; i++) {
            a = algRandom(2, n); // генерация случайного числа
            aj.add(a);
            if (modPow(a, n - 1, n) != 1) { // проверка aj ^ (n - 1) mod n
                result = 0;
                break;
            }
        }
        int digit = n - 1;
        while (digit != 1) {
            for (int i = 0; i < prime.size(); i++) {
                if (digit % prime.get(i) == 0) {
                    digit /= prime.get(i);
                    decomp.add(prime.get(i)); // разложение числа на простые множители
                }
                if (digit == 1) {
                    break;
                }
            }
        }
        for (int i = 0; i < decomp.size(); i++) {
            for (int j = 0; j < aj.size(); j++) {
                if (modPow(aj.get(j), (n - 1) / decomp.get(i), n) == 1) { // проверка a ^ ((n - 1) / q) mod n
                    count++; // счётчик результатов
                }
            }
            if (count == aj.size()) { // если все результаты не равны 1, число составное
                result = 0;
            }
            count = 0;
        }
        return result;
    }

    static int MillerBuild(int bit, ArrayList<Integer> prime_digits, int t) { // построение простого числа
        int maxdegree, degree, a, m = 1; // а - степень, degree - случайная степень, maxdegree - максимально возможная степень, m - произвдение
        int n = 0, k = 0; // k - количество отвергнутых чисел, определённых вероятностным тестом, как простые
        while (true) {
            m = 1;
            k = 0;
            for (int i = 0; i < prime_digits.size(); i++) { // цикл по элементам массива
                if (prime_digits.get(i) > Math.pow(2, bit - 1) - 1) { // проверка условия: число на 1 бит меньше заданного значения
                    break;
                }
                for (maxdegree = 1; Math.pow(prime_digits.get(i), maxdegree) <= Math.pow(2, bit - 1); maxdegree++) {} // вычисление максимально возможной степени
                degree = algRandom(1, maxdegree - 1); // генерация случайной степени
                a = algRandom(0, degree); // генерация степени
                m *= Math.pow(prime_digits.get(i), a); // построение числа m
                if (m > Math.pow(2, bit - 1) - 1) { // проверка условия: число на 1 бит меньше заданного значения
                    m /= Math.pow(prime_digits.get(i), a); // если условие не выполнено, уменьшаем m
                }
            }
            n = 2 * m + 1; // построение числа n
            if (MillerTest(n, t, prime_digits) == 0 && MillerTest(n, 1, prime_digits) == 1) {
                k++; // подсчёт количества отвергнутых чисел, определённых вероятностным тестом, как простые
            }
            if (MillerTest(n, t, prime_digits) == 1) { // проверка на простоту
                System.out.print(k + " "); // вывод количества отвергнутых чисел, определённых вероятностным тестом, как простые
                break;
            }
        }
        return n;
    }

    static double probability(int n) { // вероятностный тест
        ArrayList<Integer> prime = Eratosthenes(3000); // массив с простыми числами до 3000
        ArrayList<Integer> decomp = new ArrayList<>(); // каноническое разложение числа
        int digit = n - 1;
        double funcEuler = 1.0; // функция Эйлера
        while (digit != 1) {
            for (int i = 0; i < prime.size(); i++) {
                if (digit % prime.get(i) == 0) {
                    digit /= prime.get(i);
                    decomp.add(prime.get(i)); // разложение числа на простые множители
                }
                if (digit == 1) {
                    break;
                }
            }
        }
        Map<Integer, Integer> countdig = new HashMap<>(); // map, где ключ - число, значение - его степень
        for (int i : decomp) {
            countdig.put(i, countdig.getOrDefault(i, 0) + 1); // подсчёт степени числа
        }
        for (Map.Entry<Integer, Integer> entry : countdig.entrySet()) {
            funcEuler *= Math.pow(entry.getKey(), entry.getValue() - 1); // функция Эйлера
        }
        return funcEuler / (n - 1); // вероятность
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите количество бит: ");
        int bit = in.nextInt(); // колество бит
        int digit; // сгенерированное число
        double prob = 0.0; // вероятность
        ArrayList<Integer> prime_digits = Eratosthenes(500); // массив с простыми числами до 500
        for (int i = 1; i <= 10; i++) {
            System.out.print(i + ":    k = ");
            digit = MillerBuild(bit, prime_digits, 6); // построение числа с параметром надёжности - 6
            prob = probability(digit); // вероятностный тест
            if (prob <= 0.1) { // если вероятность <= 0.1, то число прошло проверку
                System.out.println(digit + " +");
            } else {
                System.out.println(digit + " -");
            }
        }
    }
}

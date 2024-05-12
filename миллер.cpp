#include <iostream>
#include <vector>
#include <random>
#include <map>
#include <tuple>

using namespace std;

vector<int> Eratosthenes(int size) { // генерация простых чисел методом Эратосфена
    vector<int> prime_dig = { 2 }; // вектор для простых чисел
    for (int i = 3; i <= size; i++) { 
        bool is_prime = true;
        for (int j = 0; j < prime_dig.size(); j++) {  
            if (i % prime_dig[j] == 0) { // если число не делится на все предыдущие числа в векторе простых чисел, то оно простое
                is_prime = false;
                break;
            }
        }
        if (is_prime) { // если условие верно, то число простое => добавляем в вектор
            prime_dig.push_back(i);
        }
    }
    return prime_dig;
}

int algRandom(int a, int n) { // генерация рандомного числа в диапазоне от а до n
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<int> distribution(a, n);
    return distribution(gen);
}

int modPow(int base, int exponent, int module) { // возведение в степень по модулю
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

int MillerTest(int n, int t, const vector<int>& prime_digits) { // тест Миллера
    int a = 0, result = 1, count = 0; // a - случайное целое число, result - флаг (1 - простое), count - счётчик для aj ^ (n - 1) mod n != 1
    vector<int> decomp; // вектор для канонического разложения
    vector<int> aj; // вектор случайных целых чисел
    vector<int> prime = Eratosthenes(3000); // вектор простых чисел до 3000
    for (int i = 0; i < t; i++) {
        a = algRandom(2, n); // генерация случайного числа
        aj.push_back(a);
        if (modPow(a, n - 1, n) != 1) { // проверка aj ^ (n - 1) mod n
            result = 0;
            break;
        }
    }
    int digit = n - 1;
    while (digit != 1) {
        for (int i = 0; i < prime.size(); i++) {
            if (digit % prime[i] == 0) {
                digit /= prime[i];
                decomp.push_back(prime[i]); // разложение числа на простые множители
            }
            if (digit == 1) {
                break;
            }
        }
    }
    for (int i = 0; i < decomp.size(); i++) {
        for (int j = 0; j < aj.size(); j++) {
            if (modPow(aj[j], (n - 1) / decomp[i], n) == 1) { // проверка a ^ ((n - 1) / q) mod n
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

int MillerBuild(int bit, const vector<int>& prime_digits, int t) { // построение простого числа
    int maxdegree, degree, a, m; // а - степень, degree - случайная степень, maxdegree - максимально возможная степень, m - произведение
    int n = 0, k; // k - количество отвергнутых чисел, определённых вероятностным тестом, как простые
    while (true) {
        m = 1;
        k = 0;
        for (size_t i = 0; i < prime_digits.size(); i++) { // цикл по элементам вектора 
            if (prime_digits[i] > pow(2, bit - 1) - 1) { // проверка условия: число на 1 бит меньше заданного значения
                break; 
            }
            for (maxdegree = 1; pow(prime_digits[i], maxdegree) <= pow(2, bit - 1); maxdegree++) {  } // вычисление максимально возможной степени
            degree = algRandom(1, maxdegree - 1); // генерация случайной степени
            a = algRandom(0, degree); // генерация степени
            m *= pow(prime_digits[i], a); // построение числа m
            if (m > pow(2, bit - 1) - 1) { // проверка условия: число на 1 бит меньше заданного значения
                m /= pow(prime_digits[i], a); // если условие не выполнено, уменьшаем m
            }
        }
        n = 2 * m + 1; // построение числа n
        if (MillerTest(n, t, prime_digits) == 0 && MillerTest(n, 1, prime_digits) == 1) {
            k++; // подсчёт количества отвергнутых чисел, определённых вероятностным тестом, как простые
        }
        if (MillerTest(n, t, prime_digits) == 1) { // проверка на простоту 
            cout << k << "    "; // вывод количества отвергнутых чисел, определённых вероятностным тестом, как простые
            break;
        }
    }
    return n;
}

double probability(int n) { // вероятностный тест
    vector<int> prime = Eratosthenes(3000); // вектор с простыми числами до 3000
    vector<int> decomp; // каноническое разложение числа
    int digit = n - 1;
    double funcEuler = 1.0; // функция Эйлера
    while (digit != 1) {
        for (int i = 0; i < prime.size(); i++) {
            if (digit % prime[i] == 0) {
                digit /= prime[i];
                decomp.push_back(prime[i]); // разложение числа на простые множители
            }
            if (digit == 1) {
                break;
            }
        }
    }
    map<double, double> countdig; // map, где ключ - число, значение - его степень
    for (int i : decomp) {
        countdig[i]++; // подсчёт степени числа
    }
    for (auto& pair : countdig) {
        funcEuler *= pow(pair.first, pair.second - 1); // функция Эйлера
    }
    return funcEuler / (n - 1); // вероятность
}

int main() {
	setlocale(LC_ALL, "Russian");
	cout << "Введите количество бит: ";
	int bit; // количество бит
	cin >> bit;
    int digit; // сгенерированное число
    double prob = 0.0; // вероятность
    vector<int> prime_digits = Eratosthenes(500); // вектор с простыми числами до 500
    for (int i = 1; i <= 10; i++) {
        cout << i << ":    k = ";
        digit = MillerBuild(bit, prime_digits, 6); // построение числа с параметром надёжности - 6
        prob = probability(digit); // вероятностный тест
        if (prob <= 0.1) { // если вероятность <= 0.1, то число прошло проверку
            cout << digit << " +" << endl;
        }
        else {
            cout << digit << " -" << endl;
        }
    }
	return 0;
}
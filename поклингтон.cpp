#include <iostream>
#include <vector>
#include <random>

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

int PoklingtonTest(int n, int t, const vector<int>& prime_digits) { // Тест Поклингтона
    int digit = n - 1;
    vector<int> prime = Eratosthenes(3000); // вектор простых чисел до 3000
    vector<int> decomp; // вектор для канонического разложения
    vector<int> aj; // вектор случайных целых чисел
    for (int i = 0; digit > 1;) {
        if (digit % prime[i] == 0) {
            digit /= prime[i];
            decomp.push_back(prime[i]); // разложение числа на простые множители
        }
        else {
            i++;
        }
    }
    for (int i = 0; i < t; i++) { //генерация случайного числа
        aj.push_back(algRandom(2, n - 1));
        if (modPow(aj[i], n - 1, n) != 1) { // проверка aj ^ (n - 1) mod n
            return 0;
        }
    }
    for (int j : aj) {
        int count = 0;
        for (int qi : decomp) {
            if (modPow(j, (n - 1) / qi, n) != 1) { // проверка a ^ ((n - 1) / q) mod n
                count++; // счётчик результатов
            }
        }
        if (count == decomp.size()) { // если все результаты не равны 1, число простое
            return 1;
        }
    }
    return 0;
}

double probability(int F) { // вероятностный тест
    vector<int> prime = Eratosthenes(3000); // вектор с простыми числами до 3000
    vector<int> decomp; // вектор для канонического разложения числа
    double prob = 1.0;
    for (int i = 0; F > 1;) {
        if (F % prime[i] == 0) {
            F /= prime[i];
            decomp.push_back(prime[i]); // разложение числа на простые множители
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

int PoklingtonBuild(int bit, const vector<int>& prime_digits, int t, int& F) { // построение простого числа
    int maxdegree, degree, a; // maxdegree - максимально возможная степень, degree - случайная степень, a - степень
    int k; // k - количество отвергнутых чисел, определённых вероятностным тестом, как простые
    int R, bitF, n; // bitF - количество бит числа F
    while (true) {
        F = 1, R = 1, bitF = 0, k = 0;
        for (size_t i = 0; i < prime_digits.size(); i++) { // цикл по элементам вектора
            if (prime_digits[i] > pow(2, bit / 2 + 1) - 1) { // проверка условия: на 1 бит больше половины требуемого размера для простого числа
                break;
            }
            for (maxdegree = 1; pow(prime_digits[i], maxdegree) <= pow(2, bit / 2 + 1); maxdegree++) {} // вычисление максимально возможной степени
            degree = algRandom(1, maxdegree - 1); // генерация случайной степени
            a = algRandom(0, degree); // генерация степени
            F *= pow(prime_digits[i], a); // построение числа F
            if (F > pow(2, bit / 2 + 1) - 1) { // проверка условия: на 1 бит больше половины требуемого размера для простого числа
                F /= pow(prime_digits[i], a); // если условие не выполнено, уменьшаем F
            }
        }
        bitF = ceil(log2(F)); // вычисление количества бит числа F
        while (R % 2 != 0) {
            R = algRandom(pow(2, bitF - 2), pow(2, bitF - 1)); // строим число R
        }
        n = R * F + 1;
        if (PoklingtonTest(n, t, prime_digits) == 0 && PoklingtonTest(n, 1, prime_digits) == 1) {
            k++; // подсчёт количества отвергнутых чисел, определённых вероятностным тестом, как простые
        }
        if (PoklingtonTest(n, t, prime_digits) == 1) { // проверка на простоту
            cout << k << "    "; // вывод количества отвергнутых чисел, определённых вероятностным тестом, как простые
            break;
        }
    }
    return n;
}

int main() {
	setlocale(LC_ALL, "Russian");
    cout << "Введите количество бит: ";
    int bit; // количество бит 
    cin >> bit;
    int digit; // сгенерированное число 
    int F = 1;
    vector<int> prime_digits = Eratosthenes(500); // вектор с простыми числами до 500
    for (int i = 1; i <= 10; i++) {
        cout << i << ":    k = ";
        digit = PoklingtonBuild(bit, prime_digits, 6, F); // построение числа с параметром надёжности - 6
        if (probability(F) <= 0.1) { // если вероятность <= 0.1, то число прошло проверку
            cout << digit << " +" << endl;
        }
        else {
            cout << digit << " -" << endl;
        }
    }
	return 0;
}

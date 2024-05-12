#include <iostream>
#include <random>
#include <vector>

using namespace std;

vector<int> Eratosthenes(int size) { // генерация простых чисел методом Эратосфена
    vector<int> prime_dig = { 2 }; // вектор для простых чисел
    for (int i = 3; i <= 500; i++) {
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

double algRandom(double a, double n) { // генерация случайного числа в диапазоне
    random_device rd;
    mt19937_64 gen(rd());
    uniform_real_distribution<double> distribution(a, n);
    double randdig = distribution(gen);
    randdig = round(randdig * 10.0) / 10.0;
    return randdig;
}

int modPow(int base, int exponent, int module) { // возведение в степень по модулю
    int result = 1;
    base %= module;
    while (exponent > 0) {
        if (exponent % 2 == 1) { // если степень нечётная, уменьшаем её на единицу, сохраняем результат
            result = result * base % module;
            exponent--;
        }
        exponent /= 2;
        base = base * base % module; // если степень чётная, предыдущее значение возводим в квадрат
    }
    return result;
}

int gost(int q, int bit) { // реализация госта
    int p; // простое число
    double z, N; // z - случайная величина от 0 до 1
    while (true) {
        z = algRandom(0, 1);
        N = pow(2, bit - 1) / q + pow(2, bit - 1) * z / q;
        if ((int)N % 2 != 0) { // если N - нечётное, добавляем 1
            N++;
        }
        for (int u = 0; true; u += 2) {
            p = (N + u) * q + 1; // построение кандидата в простые числа
            if (p > pow(2, bit)) { // проверка условия размерности
                break;
            }
            if (modPow(2, p - 1, p) == 1 && modPow(2, N + u, p) != 1) { // проверка условий: 2^(p-1) mod p = 1 и 2^(N+u) mod p != 1
                return p;
            }
        }
    }
}

int main() {
    setlocale(LC_ALL, "russian");
    vector<int> prime_digits = Eratosthenes(500);
    cout << "Введите количество бит: ";
    int bit; // количество бит
    cin >> bit;
    int index = algRandom(0, prime_digits.size() - 10); // генерация случайного стартового индекса
    for (int i = 1; i <= 10; i++) {
        cout << i << ":    " << gost(prime_digits[index + i], bit) << endl;
    }
    return 0;
}

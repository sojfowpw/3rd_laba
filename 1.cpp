#include <iostream>
#include <cmath>
#include <iomanip>

using namespace std;

double graphOfFunction(double x) { // график функции
	if (x <= -2.0) { // функция на промежутке от -5 до -2
		return x * (-1.0 / 3.0) - 2.0 / 3.0;
	}
	else if (x > -2.0 && x < 2.0) { // функция на промежутках от -2 до 0 и от 0 до 2
		return log(fabs(1.0 / tan(x / 2.0)));
	}
	else if (x >= 2.0) { // функция на промежутке от 2 до 5
		return x * (1.0 / 3.0) - 2.0 / 3.0;
	}
}

int main() {
	setlocale(LC_ALL, "Russian");
	double dx = 0.5; // шаг
	cout << "_____________________" << endl;
	cout << "|" << setw(5) << "X" << setw(5) << "|" << setw(5) << "Y" << setw(5) << "|" << endl; // заголовок таблицы
	for (double x = -5.0; x <= 5.0; x += dx) { // проходимся по графику с шагом 0,5
		if (x == 0.0) { // исключение 0
			continue;
		}
		cout << "---------------------" << endl;
		cout << "|" << setw(9) << x << "|" << setw(9) << graphOfFunction(x) << "|" << endl; // вывод в табличной форме
	}
	cout << "---------------------";
	return 0;
}
#include <iostream>
#include <vector>
#include <iomanip>
#include <numeric>
#include <cmath>

using namespace std;

vector<double> coffee(double Tk, double Tsr, double r, int time) { // высчитывает температуру кофе со временем
	vector<double> temperatures; // вектор для температуры
	for (int i = 1; i <= time; i++) {
		double t = Tsr + (Tk - Tsr) * exp(-r * i); // вычисление по закону теплопроводности Ньютона
		temperatures.push_back(t);
	}
	return temperatures;
}

pair<double, double> aprox(const vector<double>& x, const vector<double>& y) { // значение аппроксимирующей прямой
	double sumx = accumulate(x.begin(), x.end(), 0.0); // сумма всех х
	double sumy = accumulate(y.begin(), y.end(), 0.0); // сумма всех у
	double xy = inner_product(x.begin(), x.end(), y.begin(), 0.0); // сумма произведения всех х на у
	double xx = inner_product(x.begin(), x.end(), x.begin(), 0.0); // сумма всех х в квадрате
	double n = x.size();
	pair <double, double> approximatingLine; // пара значений аппроксимирующей прямой
	approximatingLine.first = (n * xy - sumx * sumy) / (n * xx - sumx * sumx); // значение а
	approximatingLine.second = (sumy - approximatingLine.first * sumx) / n; // значение b
	return approximatingLine;
}

double korrel(const vector<double>& x, const vector<double>& y) { // коэффициент корреляции
	double xsr = accumulate(x.begin(), x.end(), 0.0) / x.size(); // среднее значение х
	double ysr = accumulate(y.begin(), y.end(), 0.0) / y.size(); // среднее значение у
	double sumxy = 0, sumxx = 0, sumyy = 0;
	for (int i = 0; i < x.size(); i++) {
		sumxy += (x[i] - xsr) * (y[i] - ysr); // сумма произведения разности всех х и х среднего на разность всех у на у среднего
		sumxx += (x[i] - xsr) * (x[i] - xsr); // сумма квадрата разности всех х и х среднего
		sumyy += (y[i] - ysr) * (y[i] - ysr); // сумма квадрата разности всех у и у среднего
	}
	return sumxy / sqrt(sumxx * sumyy); // подсчёт коэффициента
}

int main() {
	setlocale(LC_ALL, "Russian");
	double Tk = 90; // температура кофе
	double Tsr = 25; // температура окружающей среды
	double r = 0.005; // коэффициент остывания
	int time = 60; // время остывания в минутах
	vector<double> temperatures = coffee(Tk, Tsr, r, time); // заполнение вектора с температурой
	vector<double> times; // вектор для времени
	cout << "__________________________" << endl;
	cout << "|" << setw(3) << "time (x)|" << setw(5) << "temperature (y)|" << endl; // шапка таблицы 
	for (int i = 0, j = 1; i < temperatures.size() && j <= time; i++, j++) {
		cout << "|" << setw(5) << j << setw(4) << "|";
		cout << setw(9) << temperatures[i] << setw(7) << "|" << endl; // вывод значений в табличной форме
		times.push_back(i); // заполнение вектора со временем
	}
	cout << "Аппроксимирующая прямая: a = ";
	pair<double, double> approximatingLine = aprox(times, temperatures); // заполнение пары со значением аппроксимирующей прямой
	cout << approximatingLine.first << " b = " << approximatingLine.second << endl;
	cout << "Коэффициент корреляции: r = " << korrel(times, temperatures);
	return 0;
}

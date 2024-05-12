import java.util.ArrayList;
import java.util.List;

public class Main_3 {

    public static List<Double> coffee(double Tk, double Tsr, double r, int time) { // высчитывает температуру кофе со временем
        List<Double> temperatures = new ArrayList<>(); // вектор для температуры
        for (int i = 1; i <= time; i++) {
            double t = Tsr + (Tk - Tsr) * Math.exp(-r * i); // вычисление по закону теплопроводности Ньютона
            temperatures.add(t);
        }
        return temperatures;
    }

    public static double[] aprox(List<Double> x, List<Double> y) { // значение аппроксимирующей прямой
        double sumx = x.stream().mapToDouble(Double::doubleValue).sum(); // сумма всех х
        double sumy = y.stream().mapToDouble(Double::doubleValue).sum(); // сумма всех у
        double xy = 0, xx = 0;
        for (int i = 0; i < x.size(); i++) {
            xy += x.get(i) * y.get(i); // сумма произведения всех х на у
            xx += x.get(i) * x.get(i); // сумма всех х в квадрате
        }
        double n = x.size();
        double a = (n * xy - sumx * sumy) / (n * xx - sumx * sumx); // значение а
        double b = (sumy - a * sumx) / n; // значение b
        return new double[]{a, b};
    }

    public static double korrel(List<Double> x, List<Double> y) { // коэффициент корреляции
        double xsr = x.stream().mapToDouble(Double::doubleValue).average().orElse(0.0); // среднее значение х
        double ysr = y.stream().mapToDouble(Double::doubleValue).average().orElse(0.0); // среднее значение у
        double sumxy = 0, sumxx = 0, sumyy = 0;
        for (int i = 0; i < x.size(); i++) {
            sumxy += (x.get(i) - xsr) * (y.get(i) - ysr); // сумма произведения разности всех х и х среднего на разность всех у на у среднего
            sumxx += (x.get(i) - xsr) * (x.get(i) - xsr); // сумма квадрата разности всех х и х среднего
            sumyy += (y.get(i) - ysr) * (y.get(i) - ysr); // сумма квадрата разности всех у и у среднего
        }
        return sumxy / Math.sqrt(sumxx * sumyy); // подсчёт коэффициента
    }

    public static void main(String[] args) {
        double Tk = 90; // температура кофе
        double Tsr = 25; // температура окружающей среды
        double r = 0.005; // коэффициент остывания
        int time = 60; // время остывания в минутах
        List<Double> temperatures = coffee(Tk, Tsr, r, time); // заполнение вектора с температурой
        List<Double> times = new ArrayList<>(); // вектор для времени
        System.out.println("__________________________");
        System.out.println("|time (x)|temperature (y)|"); // шапка таблицы
        for (int i = 0, j = 1; i < temperatures.size() && j <= time; i++, j++) {
            System.out.printf("|%8d|%15.4f|\n", j, temperatures.get(i));
            times.add((double) i);
        }

        System.out.print("Аппроксимирующая прямая: a = ");
        double[] approximatingLine = aprox(new ArrayList<>(times), temperatures); // заполнение "пары" значением аппроксимирующей прямой
        System.out.println(approximatingLine[0] + " b = " + approximatingLine[1]);
        System.out.println("Коэффициент корреляции: r = " + korrel(new ArrayList<>(times), temperatures));
    }
}


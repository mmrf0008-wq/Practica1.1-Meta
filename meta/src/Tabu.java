import java.util.ArrayList;
import java.util.Random;

public class Tabu {
    private int n;
    private int[][] memoria;
    private int mv_actual_i;
    private int mv_actual_j;
    private double costeTotal;
    private int pv_actual_i;
    private int pv_actual_j;
    private int nuevo_inicio;
    private int numIteraciones;
    private double mp_coste;
    private double mejorCosteGlobal;
    private boolean mejoraGlobal;
    private double costemejorAnterior;
    private int cantEmpeoramiento;
    private int tenencia;
    private double porcentaje_estancamiento;
    private double prob_oscilamiento;
    private boolean mejorpendiente;
    private int limite_it;
    private ArrayList<Integer> mejorSolucion;


    public void tabu(ArrayList<Integer> solucion, int limiteIteraciones,
                     int tenenciaTabu, double por_os, double por_est) {

        MedidorTiempos.empezarContador();
        n = solucion.size();
        memoria = new int[n][n]; // diagonal superior memoria corto plazo. Diagonal inferior memori

        cantEmpeoramiento = 0;
        tenencia = tenenciaTabu;
        mv_actual_i = -1;  //mejor vecino actual (i)
        mv_actual_j = -1; //(j)
        limite_it = limiteIteraciones;
        prob_oscilamiento = por_os;
        porcentaje_estancamiento = por_est;
        pv_actual_i = -1; // peor vecina actual (i)
        pv_actual_j = -1;  //(j)
        numIteraciones = 0;
        mp_coste = Double.MAX_VALUE;
        mejorCosteGlobal = costeTotal;
        costeTotal = Algoritmos.calculoCosteTotal(solucion);
        costemejorAnterior = costeTotal;
        nuevo_inicio =0;


        while (numIteraciones < limiteIteraciones) {

            exploracionEntorno(solucion);

            decisionMovimientoAplicar(solucion);

            estancamiento(solucion);

        }

        MedidorTiempos.finalizarYMostrar("Busqueda tabu");
        System.out.println("Coste total " + Algoritmos.calculoCosteTotal(solucion));

    }

    private void actualizaMemorias(ArrayList<Integer>solucion, int i, int j) {

        // largo plazo: frecuencia de cada arista de la solución actual (diagonal inferior)
        for (int k = 0; k < n; k++) {
            int a = solucion.get(k), b = solucion.get((k + 1) % n);
            memoria[Math.max(a, b)][Math.min(a, b)]++;
        }
        // corto plazo: tabú hasta la iteración numIteraciones + tenencia (diagonal superior)
        memoria[Math.min(i, j)][Math.max(i, j)] = numIteraciones + tenencia;
        numIteraciones++;
    }

    private void decisionMovimientoAplicar(ArrayList<Integer> solucion) {
        int mov1, mov2;
        double delta;
        if (mejoraGlobal) {
            mov1 = mv_actual_i;
            mov2 = mv_actual_j;
            delta = mp_coste;
        } else {
            mov1 = pv_actual_i;
            mov2 = pv_actual_j;
            delta = mp_coste;
        }

        if (mov1 == -1) {          // no hay movimientos permitidos
            numIteraciones++;
            return;
        }
        if (mejorpendiente && delta >= 0) {
            mejorSolucion = new ArrayList<>(solucion);
            mejorpendiente = false;
        }

        Algoritmos.cambio(solucion, mov1, mov2);
        costeTotal += delta;

        if (costeTotal < mejorCosteGlobal - 1e-9) {
            mejorCosteGlobal = costeTotal;
            mejorpendiente = true;
        }
        if (costeTotal < costemejorAnterior - 1e-9) {   // mejor de la tanda actual
            costemejorAnterior = costeTotal;
            cantEmpeoramiento = 0;
        } else {
            cantEmpeoramiento++;
        }
        actualizaMemorias(solucion, mov1, mov2);
    }

    private void estancamiento(ArrayList<Integer> solucion ) {

        if (cantEmpeoramiento >= porcentaje_estancamiento && numIteraciones < limite_it) {
            cantEmpeoramiento = 0;

            if (mejorpendiente) {
                mejorSolucion = new ArrayList<>(solucion);
                mejorpendiente = false;
            }

            Random proba = new Random();
            int p = proba.nextInt(1, 100);
            if (p <= prob_oscilamiento) {
                masvisitados(solucion, n);
            } else {
                menosvisitados(solucion, n);
            }

            costeTotal = Algoritmos.calculoCosteTotal(solucion);
            costemejorAnterior = costeTotal;
            mp_coste = Double.MAX_VALUE;
            nuevo_inicio = proba.nextInt(0, n);
            numIteraciones++;
        }

    }
    private void masvisitados(ArrayList<Integer> solucion, int n){
            ArrayList<Integer> nueva = new ArrayList<>(n);
            boolean[] marcaf = new boolean[n];
            Random rand = new Random();

            int c = rand.nextInt(n);
            nueva.add(c);
            marcaf[c] = true;

            for (int k = 1; k < n; k++) {
                int ultima = nueva.get(k - 1);
                int mayor = -1;
                c = -1;

                for (int i = 0; i < n; i++) {
                    if (!marcaf[i]) {
                        // frecuencia de la arista en la diagonal inferior
                        int f = memoria[Math.max(ultima, i)][Math.min(ultima, i)];
                        if (f > mayor) {
                            mayor = f;
                            c = i;
                        }
                    }
                }
                nueva.add(c);
                marcaf[c] = true;
            }

            solucion.clear();
            solucion.addAll(nueva);

    }
    private void menosvisitados(ArrayList<Integer> solucion, int n){
        ArrayList<Integer> nueva = new ArrayList<>(n);
        boolean[] marcaf = new boolean[n];
        Random rand = new Random();

        int c = rand.nextInt(n);
        nueva.add(c);            // <-- esta línea faltaba
        marcaf[c] = true;

        for (int k = 1; k < n; k++) {
            int ultima = nueva.get(k - 1);
            int menor = Integer.MAX_VALUE;
            c = -1;

            for (int i = 0; i < n; i++) {
                if (!marcaf[i]) {
                    int f = memoria[Math.max(ultima, i)][Math.min(ultima, i)];
                    if (f < menor) {
                        menor = f;
                        c = i;
                    }
                }
            }
            nueva.add(c);
            marcaf[c] = true;
        }

        solucion.clear();
        solucion.addAll(nueva);
    }
    private void exploracionEntorno(ArrayList<Integer> solucion) {
        mejoraGlobal = false;
        mv_actual_i = mv_actual_j = -1;
        pv_actual_i = pv_actual_j = -1;
        mp_coste = Double.MAX_VALUE;     // mejor variación entre los no mejorantes

        for (int contador = 0; contador < n && !mejoraGlobal; contador++) {
            int i = (nuevo_inicio + contador) % n;

            for (int j = i + 2; j < n && !mejoraGlobal; j++) {
                if (i == 0 && j == n - 1) continue;     // adyacentes (cierre del ciclo)
                if (esTabu(memoria, i, j)) continue;

                double[] coste = Algoritmos.calculoCoste(i, j, solucion, n);
                double delta = coste[0] - coste[1];

                if (delta < 0) {                         // mejora: la aplicamos
                    mejoraGlobal = true;
                    mv_actual_i = i;
                    mv_actual_j = j;
                    mp_coste = delta;
                    nuevo_inicio = i;
                } else if (delta < mp_coste) {           // mejor de los peores
                    mp_coste = delta;
                    pv_actual_i = i;
                    pv_actual_j = j;
                }
            }
        }
    }

    private boolean esTabu(int[][] memoria, int i, int j) {

        //verificamos en la diagonal superior si el vecino generado es tabu
        int v1, v2 = -1;
        if (i < j) { //ponemos los valores para que esten dentro de la diagonal princial
            v1 = i;
            v2 = j;
        } else {
            v1 = j;
            v2 = i;
        }

        return (memoria[v1][v2] > 0);
    }
}
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/**
 * @file Algoritmos.java
 * @brief Clase que contiene algoritmos de optimización para el problema del viajante de comercio (TSP) o similares.
 */
public class FuncionesAux {
    /** Matriz de coordenadas/distancias de las ciudades. */
    protected static double[][] matriz;


    static class Candidato implements Comparable<Candidato> {
        double sumaDistancia;
        int ciudad;

        /**
         * Constructor de la clase Candidato
         *
         * @param sumaDistancia Sumatoria de distancias
         * @param ciudad        Ciudad
         */

        public Candidato(double sumaDistancia, int ciudad) {
            this.sumaDistancia = sumaDistancia;
            this.ciudad = ciudad;
        }

        /**
         * Compara dos candidatos según su sumatoria de distancias
         *
         * @param o Candidato a comparar
         * @return 1 si this es mayor que o -1 si this es menor que o 0 si son iguales
         */
        @Override
        public int compareTo(Candidato o) {
            return Double.compare(this.sumaDistancia, o.sumaDistancia);
        }
    }

    /**
     * @brief calcula la distancia euclidea segun dos indices
     * @param i primera ciudad a evaluar
     * @param j segunda ciudad a evaluar
     * @return valor decimal de la distancia euclidea entre los puntos
     */
    protected static double distancia_euclidea(int i, int j) {
        double dx = matriz[i][1] - matriz[j][1];
        double dy = matriz[i][2] - matriz[j][2];
        return sqrt(dx * dx + dy * dy);
    }

    /**
     * @brief Calcula el coste total combinatorio de todas las distancias cruzadas en la matriz.
     * @return Suma decimal de todas las distancias euclídeas de la matriz.
     */
    public static double calculoCosteTotal(){

        double num =0;
        for(int i =0; i < matriz.length; i++){
            for(int j =0; j < matriz.length; j++){
                num += distancia_euclidea(i, j);
            }
        }
        return num;
    }
    public static double calculoCosteTotal(ArrayList<Integer> solucion) {
        int n = solucion.size();
        double total = 0;
        for (int k = 0; k < n; k++) {
            int a = solucion.get(k);
            int b = solucion.get((k + 1) % n);   // el último se une con el primero
            total += distancia_euclidea(a, b);
        }
        return total;
    }


    /**
     * @param i        posicion
     * @param j        posicion
     * @param solucion vector donde se encuntran las paradas ordenadas
     * @param n        tamaño del vector
     * @return coste[1]  coste Actual , coste[0] costeAnterior
     */
    public static double[] calculoCoste(int i, int j, ArrayList<Integer> solucion, int n) {

        double[] costes = new double[2];
        double nuevoCoste = 0;
        double costeAnterior = 0;

        //calculamos el nuevo coste
        if (i >= 1) nuevoCoste += distancia_euclidea(solucion.get(j), solucion.get(i - 1));
        if (i == 0) nuevoCoste += distancia_euclidea(solucion.get(j), solucion.get(n - 1));

        if (i < (n - 1)) nuevoCoste += distancia_euclidea(solucion.get(j), solucion.get(i + 1));
        if (i == n - 1) nuevoCoste += distancia_euclidea(solucion.get(j), solucion.getFirst());

        if (j >= 1) nuevoCoste += distancia_euclidea(solucion.get(i), solucion.get(j - 1));
        if (j == 0) nuevoCoste += distancia_euclidea(solucion.get(i), solucion.get(n - 1));

        if (j < n - 1) nuevoCoste += distancia_euclidea(solucion.get(i), solucion.get(j + 1));
        if (j == n - 1) nuevoCoste += distancia_euclidea(solucion.get(i), solucion.getFirst());


        if (i >= 1) costeAnterior += distancia_euclidea(solucion.get(i), solucion.get(i - 1));
        if (i == 0) costeAnterior += distancia_euclidea(solucion.get(i), solucion.get(n - 1));

        if (i < n - 1) costeAnterior += distancia_euclidea(solucion.get(i), solucion.get(i + 1));
        if (i == n - 1) costeAnterior += distancia_euclidea(solucion.get(i), solucion.getFirst());

        if (j >= 1) costeAnterior += distancia_euclidea(solucion.get(j), solucion.get(j - 1));
        if (j == 0) costeAnterior += distancia_euclidea(solucion.get(j), solucion.get(n - 1));

        if (j < n - 1) costeAnterior += distancia_euclidea(solucion.get(j), solucion.get(j + 1));
        if (j == n - 1) costeAnterior += distancia_euclidea(solucion.get(j), solucion.getFirst());

        costes[0] = nuevoCoste;
        costes[1] = costeAnterior;

        return costes;
    }
/**
 * @brief Intercambia los elementos en los índices i y j del vector de soluciones.
 * @param vsolGA Lista sobre la cual se realiza el intercambio.
 * @param i Primer índice a intercambiar.
 * @param j Segundo índice a intercambiar.
 */
    public static void cambio(ArrayList<Integer> vsolGA, int i, int j) {
        int a = vsolGA.get(i);
        int b = vsolGA.get(j);

        vsolGA.set(i, b);
        vsolGA.set(j, a);

    }



    /**
     * @brief Comprueba si dos posiciones son contiguas dentro del ciclo o son la misma.
     * @param i Primer índice.
     * @param j Segundo índice.
     * @param tam Tamaño total de la secuencia.
     * @return `true` si las posiciones son iguales o adyacentes; `false` en caso contrario.
     */
    protected static boolean adyacentes(int i, int j, int tam) {
        if (i == j) {
            return true;
        }

        if (((i + 1) % tam) == j || ((j + 1) % tam) == i) {
            return true;
        }

        return false;
    }
    /**
     * @brief Establece la matriz global de distancias/coordenadas.
     * @param matriz Matriz bidimensional de datos para los cálculos del algoritmo.
     */
    public static void setMatriz(double[][] matriz) {
        FuncionesAux.matriz = matriz;
    }

    /**
     * @brief Comprueba si el cambio o movimiento propuesto representa una mejora efectiva en el coste.
     * @param i Primer índice del movimiento.
     * @param j Segundo índice del movimiento.
     * @param n Tamaño de la solución.
     * @param costes Array con el coste nuevo [0] y el coste anterior [1].
     * @return `true` si el nuevo coste es estrictamente menor al anterior y los nodos no son adyacentes; `false` en caso contrario.
     */
    public static boolean factorizacion(int i, int j, int n, double[] costes) {

        if (adyacentes(i, j, n)) {
            return false;
        }
        return  (costes[1] <= costes[0]);

    }
}


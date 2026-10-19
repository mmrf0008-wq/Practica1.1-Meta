import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

/**
 * @file Algoritmos.java
 * @brief Clase que contiene algoritmos de optimización para el problema del viajante de comercio (TSP) o similares.
 */
public class Algoritmos {
    /** Matriz de coordenadas/distancias de las ciudades. */
    private static double[][] matriz;


    private static class Candidato implements Comparable<Candidato> {
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
    private static double distancia_euclidea(int i, int j) {
        double dx = matriz[i][1] - matriz[j][1];
        double dy = matriz[i][2] - matriz[j][2];
        return sqrt(dx * dx + dy * dy);
    }

    /**
     * @brief Algoritmo Voraz (Greedy) para construir una ruta inicial.
     * @details Selecciona primero la ciudad con menor sumatoria total de distancias y,
     * a partir de ella, añade iterativamente la ciudad no visitada más cercana.
     * @return ArrayList con el orden de índices de las ciudades que forman la ruta.
     */
    public ArrayList<Integer> greedy() {

        MedidorTiempos.empezarContador();

        int n = matriz.length;

        ArrayList<Candidato> vectorSolucion = new ArrayList<>();

        double sumatorio;
        //suma de las distancias de las ciudades con respecto a la primer columna
        for (int i = 0; i < n; i++) {
            sumatorio = 0;
            for (int j = 0; j < n; j++) {
                sumatorio += distancia_euclidea(i, j);
            }
            // Guardamos el candidato con su sumatoria de distancias y su ciudad correspondiente en el vector de soluciones
            vectorSolucion.add(new Candidato(sumatorio, i));
        }

        // Ordenamos menor mayor
        Collections.sort(vectorSolucion);

        // Creamos un nuevo vector para almacenar solo las ciudades ordenadas según la sumatoria de distancias
        ArrayList<Integer> ciudadesOrdenadas = new ArrayList<>();
        boolean[] visitado = new boolean[n];

        // Ciudad con menor sumatoria de distancias
        int actual = vectorSolucion.getFirst().ciudad;
        ciudadesOrdenadas.add(actual);
        visitado[actual] = true;
        double costeTotal = 0.0, minDistancia;
        int siguienteCiudad;

        for (int i = 1; i < n; ++i) {
            minDistancia = Double.MAX_VALUE;
            siguienteCiudad = -1;

            // Ciudad más cercana a la ciudad actual que no haya sido visitada
            for (int j = 0; j < n; ++j) {
                if (!visitado[j] && (distancia_euclidea(actual, j) < minDistancia)) {
                    minDistancia = distancia_euclidea(actual, j);
                    siguienteCiudad = j;

                }

            }

            // La añadimos a la lista de ciudades ordenadas y marcamos como visitada
            if (siguienteCiudad != -1) {
                ciudadesOrdenadas.add(siguienteCiudad);
                visitado[siguienteCiudad] = true;
                costeTotal += minDistancia;
                actual = siguienteCiudad;
            }
        }

        int primeraCiudad = ciudadesOrdenadas.getFirst();

        costeTotal += distancia_euclidea(actual, primeraCiudad);

        MedidorTiempos.finalizarYMostrar("Greedy");

        // Devolvemos la ruta de las ciudades ordenadas
        //System.out.println("\t Coste total: " + costeTotal);
        System.out.println("coste total " + calculoCosteTotal(ciudadesOrdenadas));


        return ciudadesOrdenadas;
    }

    /**
     * @brief Algoritmo Greedy Aleatorizado para introducir estocasticidad en la construcción de la solución.
     * @param semilla Semilla para la generación de números aleatorios.
     * @param k Tamaño del conjunto de candidatos restrictivo (LRC) a considerar en cada paso.
     * @param costeTotal Acumulador inicial para el coste total.
     * @return Ruta aleatorizada construida a partir de la solución Greedy.
     */
    public ArrayList<Integer> greedyAleatorio(long semilla, int k, double costeTotal) {
        MedidorTiempos.empezarContador();

        java.util.Random rand = new java.util.Random(semilla);

        ArrayList<Integer> vectorgreedy = greedy();
        ArrayList<Integer> vsolAlea = new ArrayList<>(); //vector solucion de greedyAleatorio (vsolAlea = vector solucion Aleatorio)

        int pos, i = 0;

        boolean primera = true;


        while (!vectorgreedy.isEmpty()) {     //empezamos a rellenar el vector solucion aleatoriamente
            // Para cuando quedan menos soluciones que k
            if (vectorgreedy.size() < k) {
                k = vectorgreedy.size();
            }

            pos = rand.nextInt(k);

            //calculamos el coste

            if (primera) {
                primera = false;
                i = vectorgreedy.get(pos);
                costeTotal += distancia_euclidea(i, pos);
            } else {
                costeTotal += distancia_euclidea(i, pos);
                i = vectorgreedy.get(pos);
            }

            //se elimina la solucion del vector vsolucion
            Integer seleccionado = vectorgreedy.remove(pos);

            //el valor seleccionado del vector será nuestra solucion para agregar al vector
            vsolAlea.add(seleccionado);
        }
        int primeraCiudad = vsolAlea.getFirst();

        costeTotal += distancia_euclidea(matriz.length - 1, primeraCiudad);
        Log.print("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        MedidorTiempos.finalizarYMostrar("Greedy Aleatorio");

        System.out.println("\t Coste total: " + calculoCosteTotal(vsolAlea));


        //se devuelve la solucion
        return vsolAlea;
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
     * @brief Búsqueda Local aplicando la técnica "Don't Look Bits" (DLB) para acelerar la exploración de vecindarios.
     * @param solucion Solución de entrada que se mejorará mediante la búsqueda local.
     * @param costeAnterior Valor inicial del coste del recorrido.
     */
    public void DontLookbits(ArrayList<Integer> solucion, double costeAnterior) {
        MedidorTiempos.empezarContador();

        // recibir solucion greedy aleatorio
        int n = solucion.size();

        int[] vmascara = new int[n];        // crear vector igual tamaño que solucion, vector mascara

        Log.print("=================================================");
        Log.print("=== INICIO BÚSQUEDA LOCAL CON DON'T LOOK BITS ===");
        Log.print("Tamaño del problema (n): " + n);
        Log.print("Solución inicial: " + solucion);
        Log.print("=================================================");

        Log.print("");

        // la i y la j son ciclicas
        int i = 0;
        int j;
        boolean mejora = true;              // mejora true: hay ciudades que pueden ser movidas. mejora=false: vmascara =1 entera
        boolean haymejora;
        double[] costes;

        while (mejora) {          // la condicion de parada es que no pueda mover mas (matriz entera en 1)
            mejora = false;
            haymejora = false;
            if (i == n) i = 0;

            Log.print("---------------------------------------");
            Log.print("[EVALUANDO NODO BASE i = " + i + "]");

            if (vmascara[i] == 1) {
                Log.print("Máscara activada (DLB=1) en i = " + i + ". Nodo omitido.");
                i++;
                continue; // descartamos aquellas que no tengan mejoras
            }

            mejora = true;
            j = (i + 1) % n;
            Log.print("Nodo activo (DLB=0). Iniciando exploración del vecindario con j = " + j + " para i = " + i);

            while (j != i && !haymejora) {
                Log.print("  -> Comprobando par (i=" + i + ", j=" + j + ")");
                costes = calculoCoste(i, j, solucion, n);
                if (factorizacion(i, j, n, costes)) { // si hay mejora hacemos cambio
                    Log.print("  [¡MEJORA ENCONTRADA!] Aplicando movimiento 2-opt entre i=" + i + " y j=" + j);
                    Log.print("  Reactivando bits en máscara (DLB=0) para i: " + i + " y j: " + j);

                    vmascara[i] = 0;
                    vmascara[j] = 0;
                    cambio(solucion, i, j);
                    haymejora = true;

                    Log.print("  Nueva solución tras el cambio: " + solucion);
                }
                j++;
                if (j == n) j = 0;
                Log.print("---------------------------------------");
            }

            if (!haymejora) {
                Log.print("[FIN VECINDARIO] No se encontraron mejoras para i = " + i);
                vmascara[i] = 1;
                Log.print("Desactivando nodo: vmascara[" + i + "] = 1");
            }
            i++;
        }

        Log.print("=================================================");
        Log.print("=== BÚSQUEDA FINALIZADA: ÓPTIMO LOCAL ALCANZADO ===");
        Log.print("Solución final: " + solucion);
        Log.print("=================================================");

        Log.print("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        MedidorTiempos.finalizarYMostrar("Don't look bit");

        Log.print("\t Coste total: " + costeAnterior);
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
     * @brief Búsqueda Local con Primer Mejor (BLPM).
     * @details Modifica la solución aplicando el primer movimiento de intercambio que genere una mejora real,
     * deteniéndose tras alcanzar un límite de iteraciones o un óptimo local.
     * @param solucion Solución inicial a optimizar.
     * @param limitIteracc Límite máximo de iteraciones permitidas.
     * @param costeActual Coste actual de la solución.
     * @return Solución optimizada.
     */
    public ArrayList<Integer> blpm(ArrayList<Integer> solucion, int limitIteracc, double costeActual) {

        MedidorTiempos.empezarContador();
        int i = 0, j;

        int numIteraciones = 0;
        int n = solucion.size();
        boolean mejora;
        boolean[] vmascara = new boolean[n];
        double[] coste;
        boolean mejoraGlobal = false;

        while (numIteraciones < limitIteracc && !mejoraGlobal) { //condiciones basicas para que siga la busqueda
            if (vmascara[i]) { //si la casilla ha sido marcada como que no tiene mejora continuamos con el siguiente

                i = (i + 1) % n;
                continue;
            }
            mejora = false;
            j = (i + 1) % n; //j = i+1
            while (i != j && !mejora) {
                coste = calculoCoste(i, j, solucion, n);
                if (factorizacion(i, j, n, coste)) { // si hay mejora
                    cambio(solucion, i, j);
                    mejora = true;
                    numIteraciones++;
                    vmascara[i] = false;
                    vmascara[j] = false;
                    mejoraGlobal = true;
                    costeActual += coste[1] - coste[0];
                }
                if (!mejora) j = (j + 1) % n;
            }
            if (!mejora) {
                vmascara[i] = true;
            }
            i = (i + 1) % n;

        }
        Log.print("=================================================");
        Log.print("=== BÚSQUEDA FINALIZADA: ÓPTIMO LOCAL ALCANZADO ===");

        Log.print("=================================================");

        Log.print("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        MedidorTiempos.finalizarYMostrar("BLPM");
        System.out.println("Coste total " + calculoCosteTotal(solucion));


        return solucion;
    }
    /**
     * @brief Comprueba si dos posiciones son contiguas dentro del ciclo o son la misma.
     * @param i Primer índice.
     * @param j Segundo índice.
     * @param tam Tamaño total de la secuencia.
     * @return `true` si las posiciones son iguales o adyacentes; `false` en caso contrario.
     */
    private static boolean adyacentes(int i, int j, int tam) {
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
        Algoritmos.matriz = matriz;
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

        Log.print("    [Evaluando Factorización] i=" + i + ", j=" + j);

        if (adyacentes(i, j, n)) {
            Log.print("    -> Los nodos son contiguos o iguales (i=" + i + ", j=" + j + "), no se realiza el 2-opt.");
            return false;
        }


        Log.print("    -> Coste anterior: " + costes[1] + " | Coste nuevo: " + costes[0] + " (i=" + i + ", j=" + j + ")");

        if (costes[1] <= costes[0]) {
            Log.print("    -> Resultado: FALSE (El coste no disminuye)");
            return false; // no hubo mejora
        } else {
            Log.print("    -> Resultado: TRUE (Mejora detectada)");
            return true;
        }
    }
}


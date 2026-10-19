import java.util.ArrayList;
import java.util.Random;

/*
 * @file Tabu.java
 * @brief Implementación del algoritmo de Metaheurística de Búsqueda Tabú para optimización de rutas (TSP).
 */

/**
 * @class Tabu
 * @brief Clase que gestiona el proceso de Búsqueda Tabú con memoria a corto y largo plazo,
 * estrategias de intensificación, diversificación por oscilación y criterios de estancamiento.
 */
public class Tabu {
    private int n;                      /* Tamaño del problema (número de ciudades o nodos). */

    /**
     * Matriz de memoria combinada:
     * - Diagonal Superior: Memoria a corto plazo (tenencia tabú hasta una iteración determinada).
     * - Diagonal Inferior: Memoria a largo plazo (frecuencia de uso de cada arista).
     */
    private int[][] memoria;


    private int mv_actual_i;            /** Índice i del mejor vecino encontrado en la iteración actual. */
    private int mv_actual_j;            /** Índice j del mejor vecino encontrado en la iteración actual. */
    private double costeTotal;          /** Coste total acumulado de la solución actual. */
    private int pv_actual_i;            /** Índice i del peor vecino aceptable (de los no mejorantes) en la iteración actual. */
    private int pv_actual_j;            /** Índice j del peor vecino aceptable (de los no mejorantes) en la iteración actual. */
    private int nuevo_inicio;           /** Índice inicial de búsqueda circular para la exploración de vecindario. */
    private int numIteraciones;         /** Contador global de iteraciones realizadas. */
    private double mp_coste;            /** Variación de coste (delta) correspondiente al movimiento seleccionado. */
    private double mejorCosteGlobal;       /** Mejor coste global registrado durante toda la ejecución. */
    private boolean mejoraGlobal;           /** Bandera que indica si se ha encontrado un movimiento que mejora la solución global. */
    private double costemejorAnterior;      /** Registra el coste de la mejor solución alcanzada dentro de la tanda o fase actual. */
    private int cantEmpeoramiento;          /** Contador de iteraciones consecutivas en las que la solución ha empeorado. */
    private int tenencia;                   /** Duración o permanencia de la condición de tabú (*Tabu Tenure*). */
    private double porcentaje_estancamiento;    /** Umbral o porcentaje de empeoramientos permitidos antes de activar el mecanismo de estancamiento. */
    private double prob_oscilamiento;           /** Probabilidad de optar por intensificación (más visitados) frente a diversificación (menos visitados). */
    private boolean mejorpendiente;             /** Bandera que indica si hay una mejora pendiente por registrar como mejor solución. */
    private int limite_it;                      /* Límite máximo global de iteraciones del algoritmo. */


    /**
     * @brief Ejecuta el algoritmo principal de Búsqueda Tabú.
     * @param solucion Solución inicial de partida.
     * @param limiteIteraciones Límite de iteraciones que ejecutará el algoritmo.
     * @param tenenciaTabu Duración en iteraciones durante las cuales un movimiento es tabú.
     * @param por_os Porcentaje o probabilidad de oscilamiento para la reinicialización.
     * @param por_est Porcentaje o número límite de empeoramientos para detectar estancamiento.
     */
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
    /**
     * @brief Actualiza la memoria a corto plazo (Lista Tabú) y a largo plazo (Frecuencias).
     * @param solucion Estado actual de la ruta antes de aplicar el movimiento.
     * @param i Índice del primer nodo del intercambio.
     * @param j Índice del segundo nodo del intercambio.
     */
    private void actualizaMemorias(ArrayList<Integer>solucion, int i, int j) {

        // largo plazo: frecuencia de cada arista de la solución actual (diagonal inferior)
        for (int k = 0; k < n; k++) {
            int a = solucion.get(k), b = solucion.get((k + 1) % n);
            memoria[Math.max(a, b)][Math.min(a, b)]++;
        }

        memoria[Math.min(i, j)][Math.max(i, j)] = numIteraciones + tenencia; //actaulizamos segun la tenencia
        numIteraciones++;
    }
    /**
     * @brief Evalúa y aplica el movimiento seleccionado (sea mejorante o el 'menos malo').
     * @param solucion Vector de la solución actual sobre el cual se aplicará el cambio.
     */
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
            mejorpendiente = false;
        }

        Algoritmos.cambio(solucion, mov1, mov2);
        costeTotal += delta;

        if (costeTotal < mejorCosteGlobal ) {
            mejorCosteGlobal = costeTotal;
            mejorpendiente = true;
        }
        if (costeTotal < costemejorAnterior ) {   // mejor de la tanda actual
            costemejorAnterior = costeTotal;
            cantEmpeoramiento = 0; //reestablecemos el numero de empeoramientos
        } else {
            cantEmpeoramiento++;
        }
        actualizaMemorias(solucion, mov1, mov2);
    }

    /**
     * @brief Evalúa si el algoritmo ha entrado en estancamiento y aplica estrategias de oscilamiento.
     * @param solucion Solución actual que será reconstruida si existe estancamiento.
     */
    private void estancamiento(ArrayList<Integer> solucion ) {

        if (cantEmpeoramiento >= porcentaje_estancamiento && numIteraciones < limite_it) {
            cantEmpeoramiento = 0;

            if (mejorpendiente) {
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
    /**
     * @brief Evalúa si el algoritmo ha entrado en estancamiento y aplica estrategias de oscilamiento.
     * @param solucion Solución actual que será reconstruida si existe estancamiento.
     */
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

    /**
     * @brief Estrategia de Diversificación: Reconstruye la solución primando las aristas menos visitadas.
     * @param solucion Lista de la solución que será reemplazada.
     * @param n Número de ciudades/nodos.
     */
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

    /**
     * @brief Explora el vecindario aplicando el operador de intercambio 2-opt o similar y comprueba el estatus tabú.
     * @param solucion Solución actual desde donde se genera el vecindario.
     */
    private void exploracionEntorno(ArrayList<Integer> solucion) {
        mejoraGlobal = false;
        mv_actual_i = mv_actual_j = -1;
        pv_actual_i = pv_actual_j = -1;
        mp_coste = Double.MAX_VALUE;     // mejor variación entre los no mejorantes
        int j;
        boolean []vmascara = new boolean[n];
        int i =0;

        boolean mejora = false;
        while (i < n && !mejoraGlobal && numIteraciones < limite_it) {

            if (vmascara[i]) { //si la casilla ha sido marcada como que no tiene mejora continuamos con el siguiente
                i = (i + 1) % n;
                continue;
            }
            mejora = false;
            j = (i + 1) % n; //j = i+1
            while (i != j && !mejora) {
                if (i == 0 && j == n - 1) continue;     // adyacentes (cierre del ciclo)
                if (esTabu(memoria, i, j)) continue; //si la solucion es tabu se descarta

                double[] coste = Algoritmos.calculoCoste(i, j, solucion, n);
                double delta = coste[0] - coste[1];

                if (delta < 0) {                         // mejora: la aplicamos
                    mejoraGlobal = true;
                    mejora = true;
                    mv_actual_i = i;
                    mv_actual_j = j;
                    mp_coste = delta;
                    vmascara[i] = false;
                    vmascara[j] = false;
                    nuevo_inicio = i;
                } else if (delta < mp_coste) {           // mejor de los peores
                    mp_coste = delta;
                    pv_actual_i = i;
                    pv_actual_j = j;
                }

                if (!mejora) j = (j + 1) % n;
            }
            if (!mejora) {
                vmascara[i] = true;
            }
            i = (nuevo_inicio + i) % n; //iterado ciclico

        }
    }

    /**
     * @brief Comprueba si un movimiento entre las posiciones i y j se encuentra actualmente en estado tabú.
     * @param memoria Matriz con el estado de tenencia tabú en su diagonal superior.
     * @param i Primer índice del movimiento.
     * @param j Segundo índice del movimiento.
     * @return `true` si el movimiento está prohibido (es tabú); `false` en caso contrario.
     */
    private boolean esTabu(int[][] memoria, int i, int j) {

        //verificamos en la diagonal superior si el vecino generado es tabu
        int v1, v2 ;
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
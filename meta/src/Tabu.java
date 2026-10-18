import java.util.ArrayList;

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
    private int limite_it;


    public void tabu(ArrayList<Integer> solucion, int limiteIteraciones, double coste_Total,
                     int tenenciaTabu, double por_os, double por_est) {

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
        nuevo_inicio = -1;
        numIteraciones = 0;
        mp_coste = Double.MAX_VALUE;
        mejorCosteGlobal = costeTotal;
        costeTotal = coste_Total;
        costemejorAnterior = costeTotal;


        while (numIteraciones < limiteIteraciones) {
            exploracionEntorno(solucion);

            decisionMovimientoAplicar(solucion);


            estancamiento();

        }
    }

    private void actualizaMemorias(ArrayList<Integer>solucion, int i, int j) {

        int c1 = solucion.get(n - 1);
        int c2 = solucion.get(0);
        memoria[Math.max(c1, c2)][Math.min(c1, c2)]++;

        // aseguramos orden para la diagonal superior (MCP)
        int p1t = Math.min(i, j);
        int p2t = Math.max(i, j);

// prohibir el movimiento actual por X iteraciones
        memoria[p1t][p2t] = tenencia;

// dfecrementar la tenencia de los elementos antiguos
        for (int r = 0; r < n; r++) {
            for (int c = r + 1; c < n; c++) {
                if (memoria[r][c] > 0 && !(r == p1t && c == p2t)) {
                    memoria[r][c]--;
                }
            }
        }

        numIteraciones++;
    }

    private void decisionMovimientoAplicar(ArrayList<Integer> solucion) {
        int mov1 = -1;
        int mov2 = -1;
        if (mejoraGlobal) { // aplicamos los movimientos encontrados antes
            mov1 = mv_actual_i;
            mov2 = mv_actual_j;
        } else { // si no hubo mejora se elije el mejor de los peores
            mov1 = pv_actual_i;
            mov2 = pv_actual_j;
        }
        costeTotal += mp_coste;

        if (mov1 != -1) {
            Algoritmos.cambio(solucion, mov1, mov2);

            if (mp_coste < mejorCosteGlobal) {
                mejorCosteGlobal = mp_coste;
            }
            if (costemejorAnterior > mp_coste) {
                cantEmpeoramiento = 0;
                costemejorAnterior = mp_coste;
            } else {
                cantEmpeoramiento++;
            }
        }
        actualizaMemorias(solucion, mov1, mov2);
    }

    private void estancamiento() {
        if(cantEmpeoramiento >= porcentaje_estancamiento && numIteraciones < limite_it){
            cantEmpeoramiento =0;
            
        }
    }

    private void exploracionEntorno(ArrayList<Integer> solucion) {
        mejoraGlobal = false;
        boolean[] vmascara = new boolean[n];

        int j;
        double[] coste;
        boolean mejora = false;
        for (int i = nuevo_inicio, contador = 0; contador < n && !mejoraGlobal; i++, contador++) {

            if (vmascara[i]) { //si la casilla ha sido marcada como que no tiene mejora continuamos con el siguiente
                i = (i + 1) % n;
                continue;
            }
            j = (i + 1) % n; //j = i+1
            mejora = false;

            if (!esTabu(memoria, i, j)) { //si el movimiento no es tabu, lo evaluamos
                coste = Algoritmos.calculoCoste(i, j, solucion, n);
                if (Algoritmos.factorizacion(i, j, n, coste)) { // si hay mejora
                    mejora = true;
                    vmascara[i] = false;
                    vmascara[j] = false;
                    mv_actual_i = i;//almacenamos el mejor actual
                    mv_actual_j = j;
                    nuevo_inicio = i;
                } else { // si no hubo mejora. seleccionamos al mejor de los peores
                    if (coste[1] < mp_coste) { //guardo el nuevo mejor peor coste
                        mp_coste = coste[1];
                        pv_actual_j = j;
                        pv_actual_i = i;
                    }
                }
            }
            if (!mejora) {
                vmascara[i] = true;
            }
            if (i + 1 == n) i = 0;
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
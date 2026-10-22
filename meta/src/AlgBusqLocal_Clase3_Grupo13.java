import java.util.ArrayList;

public class AlgBusqLocal_Clase3_Grupo13 {
    /**
     * @brief Búsqueda Local con Primer Mejor (BLPM).
     * @details Modifica la solución aplicando el primer movimiento de intercambio que genere una mejora real,
     * deteniéndose tras alcanzar un límite de iteraciones o un óptimo local.
     * @param solucion Solución inicial a optimizar.
     * @param limitIteracc Límite máximo de iteraciones permitidas.
     * @param costeActual Coste actual de la solución.
     * @return Solución optimizada.
     */
    public static ArrayList<Integer> blpm(ArrayList<Integer> solucion, int limitIteracc, double costeActual) {

        MedidorTiempos.empezarContador();
        Log.print("Inicio BLPM (limite iteraciones=" + limitIteracc + ", coste inicial=" + costeActual + ")");
        int i = 0, j;

        int numIteraciones = 0;
        int n = solucion.size();
        boolean mejora;
        boolean[] vmascara = new boolean[n];
        double[] coste;
        boolean mejoraGlobal = false;

        while (numIteraciones < limitIteracc && !mejoraGlobal) { //condiciones basicas para que siga la busqueda
            if (vmascara[i]) { //si la casilla ha sido marcada como que no tiene mejora continuamos con el siguiente
                Log.print("Posicion " + i + " es fija ");
                i = (i + 1) % n;
                continue;
            }
            mejora = false;
            j = (i + 1) % n; //j = i+1
            while (i != j && !mejora) {
                coste = FuncionesAux.calculoCoste(i, j, solucion, n);
                if (FuncionesAux.factorizacion(i, j, n, coste)) { // si hay mejora
                    FuncionesAux.cambio(solucion, i, j);
                    mejora = true;
                    numIteraciones++;
                    vmascara[i] = false;
                    vmascara[j] = false;
                    mejoraGlobal = true;
                    costeActual += coste[1] - coste[0];
                    Log.print("Movimiento aplicado (i=" + i + ", j=" + j + "), nuevo coste=" + costeActual);

                }
                if (!mejora) j = (j + 1) % n;
            }
            if (!mejora) {
                vmascara[i] = true;
                Log.print("La posicion " + i + " ");
            }
            i = (i + 1) % n;

        }
        Log.print("=================================================");
        Log.print("=== BÚSQUEDA FINALIZADA: ÓPTIMO LOCAL ALCANZADO ===");

        Log.print("=================================================");

        Log.print("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        MedidorTiempos.finalizarYMostrar("BLPM");
        Log.print("Coste total " + FuncionesAux.calculoCosteTotal(solucion));


        return solucion;
    }
}

import java.util.ArrayList;

public class AlgDLB_Clase3_Grupo13 {
    /**
     * @brief Búsqueda Local aplicando la técnica "Don't Look Bits" (DLB) para acelerar la exploración de vecindarios.
     * @param solucion Solución de entrada que se mejorará mediante la búsqueda local.
     * @param costeAnterior Valor inicial del coste del recorrido.
     */
    public static void DontLookbits(ArrayList<Integer> solucion, double costeAnterior) {
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
                i++;
                continue; // descartamos aquellas que no tengan mejoras
            }

            mejora = true;
            j = (i + 1) % n;
            Log.print("Nodo activo (DLB=0). Iniciando exploración del vecindario con j = " + j + " para i = " + i);

            while (j != i && !haymejora) {
                costes = FuncionesAux.calculoCoste(i, j, solucion, n);
                if (FuncionesAux.factorizacion(i, j, n, costes)) { // si hay mejora hacemos cambio
                    Log.print("  [¡MEJORA ENCONTRADA!] Aplicando movimiento 2-opt entre i=" + i + " y j=" + j);
                    Log.print("  Reactivando bits en máscara (DLB=0) para i: " + i + " y j: " + j);

                    vmascara[i] = 0;
                    vmascara[j] = 0;
                    FuncionesAux.cambio(solucion, i, j);
                    haymejora = true;


                }
                j++;
                if (j == n) j = 0;
            }

            if (!haymejora) {
                vmascara[i] = 1;
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
}

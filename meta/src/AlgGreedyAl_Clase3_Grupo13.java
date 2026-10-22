import java.util.ArrayList;

public class AlgGreedyAl_Clase3_Grupo13 {
    /**
     * @brief Algoritmo Greedy Aleatorizado para introducir estocasticidad en la construcción de la solución.
     * @param semilla Semilla para la generación de números aleatorios.
     * @param k Tamaño del conjunto de candidatos restrictivo (LRC) a considerar en cada paso.
     * @param costeTotal Acumulador inicial para el coste total.
     * @return Ruta aleatorizada construida a partir de la solución Greedy.
     */
    public static ArrayList<Integer> greedyAleatorio(long semilla, int k, double costeTotal) {
        MedidorTiempos.empezarContador();
        Log.print("Inicio Greedy Aleatorio (semilla=" + semilla + ", k=" + k + ")");
        java.util.Random rand = new java.util.Random(semilla);

        ArrayList<Integer> vectorgreedy = AlgGreedy_Clase3_Grupo13.greedy();
        Log.print("Solucion greedy base obtenida, tamaño=" + vectorgreedy.size());

        ArrayList<Integer> vsolAlea = new ArrayList<>(); //vector solucion de greedyAleatorio (vsolAlea = vector solucion Aleatorio)

        int pos, i = 0;

        boolean primera = true;

        Log.print("Construyendo solucion aleatoria");
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
                costeTotal += FuncionesAux.distancia_euclidea(i, pos);
            } else {
                costeTotal += FuncionesAux.distancia_euclidea(i, pos);
                i = vectorgreedy.get(pos);
            }

            //se elimina la solucion del vector vsolucion
            Integer seleccionado = vectorgreedy.remove(pos);

            //el valor seleccionado del vector será nuestra solucion para agregar al vector
            vsolAlea.add(seleccionado);
        }

        Log.print("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        MedidorTiempos.finalizarYMostrar("Greedy Aleatorio");

        Log.print("\t Coste total: " + FuncionesAux.calculoCosteTotal(vsolAlea));


        //se devuelve la solucion
        return vsolAlea;
    }
}

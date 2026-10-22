import java.util.ArrayList;
import java.util.Collections;

public  class AlgGreedy_Clase3_Grupo13 extends FuncionesAux {
    /**
     * @brief Algoritmo Voraz (Greedy) para construir una ruta inicial.
     * @details Selecciona primero la ciudad con menor sumatoria total de distancias y,
     * a partir de ella, añade iterativamente la ciudad no visitada más cercana.
     * @return ArrayList con el orden de índices de las ciudades que forman la ruta.
     */
    public static ArrayList<Integer> greedy() {

        MedidorTiempos.empezarContador();

        int n = FuncionesAux.matriz.length;
        Log.print("Inicio Greedy (n=" + n + ")");

        ArrayList<FuncionesAux.Candidato> vectorSolucion = new ArrayList<>();

        double sumatorio;
        //suma de las distancias de las ciudades con respecto a la primer columna
        Log.print("Calculando sumatoria de distancias de cada ciudad");

        for (int i = 0; i < n; i++) {
            sumatorio = 0;
            for (int j = 0; j < n; j++) {
                sumatorio += FuncionesAux.distancia_euclidea(i, j);
            }
            // Guardamos el candidato con su sumatoria de distancias y su ciudad correspondiente en el vector de soluciones
            vectorSolucion.add(new FuncionesAux.Candidato(sumatorio, i));
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
        Log.print("Ciudad inicial (menor sumatoria): " + actual);
        double costeTotal = 0.0, minDistancia;
        int siguienteCiudad;

        Log.print("Construyendo ruta por vecino mas cercano");
        for (int i = 1; i < n; ++i) {
            minDistancia = Double.MAX_VALUE;
            siguienteCiudad = -1;

            // Ciudad más cercana a la ciudad actual que no haya sido visitada
            for (int j = 0; j < n; ++j) {
                if (!visitado[j] && (FuncionesAux.distancia_euclidea(actual, j) < minDistancia)) {
                    minDistancia = FuncionesAux.distancia_euclidea(actual, j);
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

        costeTotal += FuncionesAux.distancia_euclidea(actual, primeraCiudad);
        Log.print("Ruta completa, cierre del ciclo con ciudad " + primeraCiudad);
        MedidorTiempos.finalizarYMostrar("Greedy");

        // Devolvemos la ruta de las ciudades ordenadas
        //System.out.println("\t Coste total: " + costeTotal);
       Log.print("coste total " + FuncionesAux.calculoCosteTotal(ciudadesOrdenadas));


        return ciudadesOrdenadas;
    }
}

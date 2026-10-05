import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

public class Algoritmos {
    private double matriz[][];


    private class Candidato implements Comparable<Candidato> {
        double sumaDistancia;
        int ciudad;
        
        /**
         * Constructor de la clase Candidato
         * @param sumaDistancia Sumatoria de distancias
         * @param ciudad Ciudad
         */

        public Candidato(double sumaDistancia, int ciudad) {
            this.sumaDistancia = sumaDistancia;
            this.ciudad = ciudad;
        }
        
        /**        
         * Compara dos candidatos según su sumatoria de distancias
         * @param o Candidato a comparar
         * @return 1 si this es mayor que o
         * @return -1 si this es menor que o
         * @return 0 si son iguales
         */
        @Override
        public int compareTo(Candidato o) {
            return Double.compare(this.sumaDistancia, o.sumaDistancia);
        }
    }



    private double distancia_euclidea(int i, int j ){
        double dx = matriz[i][1] - matriz[j][1];
        double dy = matriz[i][2] - matriz[j][2];
        return sqrt(dx*dx + dy*dy);
    }


    public ArrayList<Integer> greedy(){

        MedidorTiempos.empezarContador();

        int n = matriz.length;

        ArrayList<Candidato> vectorSolucion = new ArrayList<>();

        double sumatorio = 0;
        //suma de las distancias de las ciudades con respecto a la primer columna
        for(int i = 0; i < n; i++){
            sumatorio = 0;
            for(int j = 0; j < n; j++){
                sumatorio += distancia_euclidea( i, j );
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
        double costeTotal = 0.0;
        double minDistancia = 0;
        int siguienteCiudad=0;

        for (int i = 1; i < n; ++i) {
            minDistancia = Double.MAX_VALUE;
            siguienteCiudad = -1;

            // Ciudad más cercana a la ciudad actual que no haya sido visitada
            for (int j = 0; j < n; ++j) {
                if (!visitado[j] && (distancia_euclidea( actual, j)  < minDistancia)) {
                    minDistancia = distancia_euclidea( actual, j);
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

        costeTotal += distancia_euclidea( actual, primeraCiudad);

        MedidorTiempos.finalizarYMostrar("Greedy");

        // Devolvemos la ruta de las ciudades ordenadas
        Log.print("\t Coste total: " + costeTotal);


        return ciudadesOrdenadas;
    }


    public ArrayList<Integer> greedyAleatorio( long semilla, int k, double costeTotal){
        MedidorTiempos.empezarContador();

        java.util.Random rand = new java.util.Random(semilla);

        ArrayList<Integer> vectorgreedy = greedy();
        ArrayList<Integer> vsolAlea = new ArrayList<>(); //vector solucion de greedyAleatorio (vsolAlea = vector solucion Aleatorio)

        int pos =0;
        int i =0;
        boolean primera =true;


        while(!vectorgreedy.isEmpty()) {     //empezamos a rellenar el vector solucion aleatoriamente
            // Para cuando quedan menos soluciones que k
            if(vectorgreedy.size() < k){
                k = vectorgreedy.size();
            }

            pos = rand.nextInt(k);

            //calculamos el coste

            if(primera){
                primera =false;
                i = vectorgreedy.get(pos);
            }
            else {
                costeTotal += distancia_euclidea( pos, i);
                i = vectorgreedy.get(pos);
            }

            //se elimina la solucion del vector vsolucion
            Integer seleccionado = vectorgreedy.remove(pos);

            //el valor seleccionado del vector será nuestra solucion para agregar al vector
            vsolAlea.add(seleccionado);
        }

        int primeraCiudad = vsolAlea.getFirst();

        costeTotal +=  distancia_euclidea( matriz.length-1, primeraCiudad);
        Log.print("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        MedidorTiempos.finalizarYMostrar("Greedy Aleatorio");

       Log.print("\t Coste total: " + costeTotal);


        //se devuelve la solucion
        return vsolAlea;
    }
    public void DontLookbits( ArrayList<Integer> solucion, double costeAnterior ){
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
        boolean haymejora = false;

        while(mejora){          // la condicion de parada es que no pueda mover mas (matriz entera en 1)
            mejora = false;
            haymejora = false;
            if(i == n) i = 0;

            Log.print("---------------------------------------");
            Log.print("[EVALUANDO NODO BASE i = " + i + "]");

            if(vmascara[i] == 1){
                Log.print("Máscara activada (DLB=1) en i = " + i + ". Nodo omitido.");
                i++;
                continue; // descartamos aquellas que no tengan mejoras
            }

            mejora = true;
            j = (i + 1) % n;
            Log.print("Nodo activo (DLB=0). Iniciando exploración del vecindario con j = " + j + " para i = " + i);

            while(j != i && !haymejora){
                Log.print("  -> Comprobando par (i=" + i + ", j=" + j + ")");

                if(factorizacion(solucion, i, j, n)){ // si hay mejora hacemos cambio
                    Log.print("  [¡MEJORA ENCONTRADA!] Aplicando movimiento 2-opt entre i=" + i + " y j=" + j);
                    Log.print("  Reactivando bits en máscara (DLB=0) para i: " + i + " y j: " + j);

                    vmascara[i] = 0;
                    vmascara[j] = 0;
                    cambio(solucion, i, j);
                    haymejora = true;

                    Log.print("  Nueva solución tras el cambio: " + solucion);
                }
                j++;
                if(j == n) j = 0;
                Log.print("---------------------------------------");
            }

            if(!haymejora){
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
     *
     * @param i
     * @param j
     * @param solucion
     * @param n
     * @return coste[1]  coste Actual , coste[0] costeAnterior
     */
        public double[] calculoCoste(int i , int j, ArrayList<Integer> solucion, int n ){

            double []costes = new double[2];
            double nuevoCoste =0;
            double costeAnterior=0;

            //calculamos el nuevo coste
            if(i>=1) nuevoCoste += distancia_euclidea(solucion.get(j), solucion.get(i-1));
            if(i==0) nuevoCoste += distancia_euclidea(solucion.get(j), solucion.get(n-1));

            if(i < (n-1)) nuevoCoste += distancia_euclidea(solucion.get(j), solucion.get(i+1));
            if(i == n-1) nuevoCoste += distancia_euclidea(solucion.get(j),solucion.getFirst());

            if(j>= 1) nuevoCoste += distancia_euclidea(solucion.get(i), solucion.get(j-1));
            if(j==0) nuevoCoste += distancia_euclidea(solucion.get(i), solucion.get(n-1));

            if(j<n-1) nuevoCoste += distancia_euclidea(solucion.get(i), solucion.get(j+1));
            if(j== n-1) nuevoCoste += distancia_euclidea(solucion.get(i),solucion.getFirst());

            /*********************************************************************************/


            if(i>=1) costeAnterior += distancia_euclidea(solucion.get(i),solucion.get(i-1));
            if(i==0) costeAnterior += distancia_euclidea(solucion.get(i),solucion.get(n-1));

            if(i<n-1) costeAnterior += distancia_euclidea(solucion.get(i),solucion.get(i+1));
            if(i==n-1) costeAnterior+= distancia_euclidea(solucion.get(i),solucion.getFirst());

            if(j>=1) costeAnterior += distancia_euclidea(solucion.get(j),solucion.get(j-1));
            if(j==0) costeAnterior += distancia_euclidea(solucion.get(j),solucion.get(n-1));

            if(j < n-1) costeAnterior += distancia_euclidea(solucion.get(j),solucion.get(j+1));
            if( j == n-1) costeAnterior += distancia_euclidea(solucion.get(j),solucion.getFirst());

            costes[0] = nuevoCoste;
            costes[1] = costeAnterior;

            return  costes;
        }
        private void cambio(ArrayList<Integer> vsolGA, int i, int j){
                int a = vsolGA.get(i);
                int b=  vsolGA.get(j);

                vsolGA.set(i, b);
                vsolGA.set(j, a);

        }
        public void setMatriz(double matriz[][]){
            this.matriz = matriz;
        }


    public ArrayList<Integer> pdlb( ArrayList<Integer> solucion, int limitIteracc, double costeActual) {
        MedidorTiempos.empezarContador();

        int tamS = solucion.size();

        boolean[] dlb = new boolean[tamS]; // Para marcar las casillas de las posiciones
        boolean mejorGlo = true;
        int iteraccActuales = 0;
        double mejoraActual = 0;
        double mejora = 0;

        Log.print("=================================================");
        Log.print("=== INICIO ALGORITMO 2-OPT CON DLB (PDLB) ===");
        Log.print("Tamaño solución (tamS): " + tamS + " | Límite iteraciones: " + limitIteracc);
        Log.print("Solución inicial: " + solucion);
        Log.print("=================================================");

        while (mejorGlo && iteraccActuales < limitIteracc) {
            mejorGlo = false;
            Log.print("\n-------------------------------------------------");
            Log.print("--- Inicio de iteración global. Iteraciones actuales: " + iteraccActuales + " ---");
            Log.print("-------------------------------------------------");

            boolean mejorLocalAc = false; // La utilizamos para saber si hay algún movimiento de mejora en i
            boolean haymejora = false; // La utilizamos para salir del bucle de j

            // Recorremos todas las posiciones de i, sin/hasta superar el limite de iter.
            for (int i = 0; i < tamS && iteraccActuales < limitIteracc && !mejorLocalAc ; ++i) {
                if (dlb[i]) { // Solo analizamos las que estén en false
                    Log.print("[i = " + i + "] Omitido -> Marcado en DLB (dlb[" + i + "] = true)");
                    continue;
                }

                Log.print("[EVALUANDO POSICIÓN BASE i = " + i + "] (DLB activo)");

                // Recorremos todas las posiciones de j, sin/hasta superar el limite de iter.
                for (int j = 0; j < tamS && iteraccActuales < limitIteracc && !haymejora; ++j) {
                    if (adyacentes(i, j, tamS)) {
                        Log.print("  -> Par (i=" + i + ", j=" + j + ") omitido por ser adyacentes");
                        continue;
                    }

                    Log.print("  -> Probando movimiento 2-OPT entre i=" + i + " y j=" + j);

                    // Aqui hay que hacer los cambios de ciudades
                    if (factorizacion(solucion, i, j, tamS)) {
                        cambio(solucion, i, j);

                        Log.print("  [¡MEJORA ENCONTRADA!] Entre i=" + i + " y j=" + j + ". Iteración global: " + (iteraccActuales + 1));
                        Log.print("  Nueva solución tras cambio: " + solucion);

                        // Encontramos mejora, asi que actualizamos todas las variables
                        //costeActual = nuevoCoste;
                        mejorLocalAc = true;
                        double []coste = calculoCoste(i, j, solucion, tamS);

                        Log.print("  Cálculo de coste -> Nuevo: " + coste[0] + " | Anterior: " + coste[1]);

                        if(coste[1] < coste[0]){
                            mejoraActual = coste[1];
                            Log.print("  Mejora actual registrada: " + mejoraActual);
                        }

                        iteraccActuales++;
                        Log.print("  Iteraciones acumuladas: " + iteraccActuales);
                        haymejora = true;
                    } else {
                        Log.print("  -> Sin mejora en par (i=" + i + ", j=" + j + ")");
                    }
                }

                if(mejoraActual < mejora){ //se ha encontrado una mejora global, se para la ejecucion
                    mejorGlo = true;
                    Log.print("[MEJORA GLOBAL] mejoraActual (" + mejoraActual + ") < mejora (" + mejora + "). Marcando mejorGlo = true");
                }

                if (!mejorLocalAc) {
                    dlb[i] = true; // Si no mejora ningun movimiento de i
                    Log.print("[DLB ACTUALIZADO] Sin mejora para i = " + i + ". Posición marcada en DLB (dlb[" + i + "] = true)");
                }
            }
        }

        Log.print("\n=================================================");
        Log.print("=== FIN DE BÚSQUEDA / ÓPTIMO LOCAL ALCANZADO ===");
        Log.print("Solución final: " + solucion);
        Log.print("=================================================");

        Log.print("Fin de ejecución del bucle principal. Motivo de salida -> mejorGlo: " + mejorGlo + ", Iteraciones alcanzadas: " + iteraccActuales + "/" + limitIteracc);

        Log.print("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        MedidorTiempos.finalizarYMostrar("PDLB");
        Log.print("Coste final: " + costeActual);
        Log.print("Iteraciones: " + iteraccActuales);

        return solucion;
    }

    private boolean adyacentes (int i, int j, int tam) {
        if (i == j) {
            return true;
        }

        if (((i + 1) % tam) == j) {
            return true;
        }

        if (((j + 1) % tam) == i) {
            return true;
        }

        return false;
    }private boolean factorizacion( ArrayList<Integer> solucion, int i, int j, int n ){

        Log.print("    [Evaluando Factorización] i=" + i + ", j=" + j);

        if (i == j || (i + 1) % n == j || (j + 1) % n == i){
            Log.print("    -> Los nodos son contiguos o iguales (i=" + i + ", j=" + j + "), no se realiza el 2-opt.");
            return false;
        }

        double []costes = calculoCoste(i, j, solucion, n);

        Log.print("    -> Coste anterior: " + costes[1] + " | Coste nuevo: " + costes[0] + " (i=" + i + ", j=" + j + ")");

        if (costes[1] <= costes[0]) {
            Log.print("    -> Resultado: FALSE (El coste no disminuye)");
            return false; // no hubo mejora
        }
        else{
            Log.print("    -> Resultado: TRUE (Mejora detectada)");
            return true;
        }
    }

}


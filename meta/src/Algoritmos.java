import java.awt.image.AreaAveragingScaleFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.*;

public class Algoritmos {
    public String nombre;

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



    private double distancia_euclidea(double [][]matriz, int i, int j ){
        double dx = matriz[i][0] - matriz[j][0];
        double dy = matriz[i][1] - matriz[j][1];
        return sqrt(dx*dx + dy*dy);
    }

    /**
     * Imprime la matriz de distancias euclideas
     * @param matriz Matriz de distancias euclideas a imprimir
     */

    private void printMatriz(int matriz[][]){
        for(int l = 0; l < matriz.length; l++){
            for(int m = 0; m < matriz.length; m++){
                System.out.print(" " + matriz[l][m]);
            }
            System.out.println("");
        }
    }


    public ArrayList<Integer> greedy(double matriz[][]){

        MedidorTiempos.empezarContador();

        int n = matriz.length;


        
        ArrayList<Candidato> vectorSolucion = new ArrayList<>();

        double sumatorio = 0;
        //suma de las distancias de las ciudades con respecto a la primer columna
        for(int i = 0; i < n; i++){
            sumatorio = 0;
            for(int j = 0; j < n; j++){
                sumatorio += distancia_euclidea(matriz, i, j );
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
        int actual = vectorSolucion.get(0).ciudad; 
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
                if (!visitado[j] && (distancia_euclidea(matriz, actual, j)  < minDistancia)) {
                    minDistancia = distancia_euclidea(matriz, actual, j);
                    //System.out.println("minDistancia " + minDistancia + " actual " + actual + " j: "+j );
                    siguienteCiudad = j;

                }

            }

            // La añadimos a la lista de ciudades ordenadas y marcamos como visitada
            if (siguienteCiudad != -1) {
                ciudadesOrdenadas.add(siguienteCiudad);
                visitado[siguienteCiudad] = true;
                costeTotal += minDistancia;
                //System.out.println(" min distancia " + minDistancia);
                actual = siguienteCiudad;
            }
        }
        
        int primeraCiudad = ciudadesOrdenadas.get(0);

        costeTotal += distancia_euclidea(matriz, actual, primeraCiudad);

        MedidorTiempos.finalizarYMostrar("Greedy");

        // Devolvemos la ruta de las ciudades ordenadas
        System.out.println("\t Coste total: " + costeTotal);


        return ciudadesOrdenadas;
    }


    public ArrayList<Integer> greedyAleatorio(double matriz[][], long semilla, int k ){
        MedidorTiempos.empezarContador();

        java.util.Random rand = new java.util.Random(semilla);

        ArrayList<Integer> vectorgreedy = greedy(matriz);

        ArrayList<Integer> vsolAlea = new ArrayList<>(); //vector solucion de greedyAleatorio (vsolAlea = vector solucion Aleatorio)


        double costeTotal = 0.0;
        int pos =0;

        //empezamos a rellenar el vector solucion aleatoriamente
        int i =0;
        boolean primera =true;
        while(!vectorgreedy.isEmpty()) {
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
                //System.out.println("coste total " + costeTotal);
                costeTotal += distancia_euclidea(matriz, pos, i);
                //System.out.println("ciudad 1: " + vectorgreedy.get(pos) + " -ciudad 2: " + i + " distancia " + matrizEuclidea[pos][i] + " coste total " + costeTotal);
                i = vectorgreedy.get(pos);
            }

            //se elimina la solucion del vector vsolucion
            Integer seleccionado = vectorgreedy.remove(pos);

            //el valor seleccionado del vector será nuestra solucion para agregar al vector
            vsolAlea.add(seleccionado);
        }

        int primeraCiudad = vsolAlea.getFirst();

        costeTotal +=  distancia_euclidea(matriz, matriz.length-1, primeraCiudad);
        
        MedidorTiempos.finalizarYMostrar("Greedy Aleatorio");

        System.out.println("\t Coste total: " + costeTotal);


        //se devuelve la solucion
        return vsolAlea;
    }
        public void DontLookbits(double [][] matriz, ArrayList<Integer> solucion ){
            //recibir solucion greedy aleatorio
            ArrayList<Integer> vsolGA = solucion;
            int n = vsolGA.size();

            //crear vector igual tamaño que solucion, vector mascara
            int vmascara[] = new int[n];

            for(int i =0; i < n; i++){
                vmascara[i] = 0; //ponemos todos los bits en 0
            }
            //0- puede cambiar 1-el valor es fijo. Comienza to_do en 0

            //verificamos en bucle los cambios
                // si ninguno provoca mejora v[i]=1
                //un movimiento genera solucion vecina con mejor coste v[i]=0

            double minimo=0;
            double distancia =0;
            int ultimo =-1;
            for (int i =0; i < n ; i++){
                minimo =0;
                for(int j = i +1; j != i && vmascara[i]==0; j++){
                    distancia = distancia_euclidea(matriz, i, j);
                    if(minimo > distancia){
                        minimo = distancia;
                        ultimo = j;
                    }
                    if(j +1 == n){ //volvemos al inicio
                        j=0;
                    }
                }
                if(minimo < distancia_euclidea(matriz, i, i+1)) { //si la distancia nueva encontrada supone una mejora
                    //hacemos trueque
                }
            }
        }

}


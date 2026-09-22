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



    private double distancia_euclidea(double x1, double y1, double x2, double y2){
        double dx = x1 - x2;
        double dy = y1 - y2;
        return sqrt(dx*dx + dy*dy);
    }

    /**
     * Calcula la matriz de distancias euclideas entre todas las ciudades
     * @param matrizEuclidea Matriz de distancias euclideas a llenar
     * @param matriz Matriz de coordenadas de las ciudades
     */

    private void calculoMatrizEuclidea(double matrizEuclidea[][], double  matriz[][]){
        int n = matriz.length;
        for(int i =0; i < n; i++){
            matrizEuclidea[i][i] = 0.0;

            for(int j = i + 1; j < n; j++){

                double distancia = distancia_euclidea(matriz[i][1], matriz[i][2], matriz[j][1], matriz[j][2]);
                
                // Casillas simetricas
                matrizEuclidea[i][j] = distancia;
                matrizEuclidea[j][i] = distancia;
            }

        }
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
        double matrizEuclidea[][] = new double[n][n];
        calculoMatrizEuclidea(matrizEuclidea,matriz);
        
        ArrayList<Candidato> vectorSolucion = new ArrayList<>();

        double sumatorio = 0;
        //suma de las distancias de las ciudades con respecto a la primer columna
        for(int i = 0; i < n; i++){
            sumatorio = 0;
            for(int j = 0; j < n; j++){
                sumatorio += matrizEuclidea[i][j];
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
                if (!visitado[j] && (matrizEuclidea[actual][j] < minDistancia)) {
                    minDistancia = matrizEuclidea[actual][j];
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
        costeTotal += matrizEuclidea[actual][primeraCiudad];

        MedidorTiempos.finalizarYMostrar("Greedy");

        // Devolvemos la ruta de las ciudades ordenadas
        System.out.println("\t Coste total: " + costeTotal);


        return ciudadesOrdenadas;
    }


    public ArrayList<Integer> greedyAleatorio(double matriz[][], long semilla, int k ){
        MedidorTiempos.empezarContador();

        java.util.Random rand = new java.util.Random(semilla);

        int n = matriz.length;
        double matrizEuclidea[][] = new double[n][n];
        calculoMatrizEuclidea(matrizEuclidea,matriz);

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
                costeTotal += matrizEuclidea[pos][i];
                //System.out.println("ciudad 1: " + vectorgreedy.get(pos) + " -ciudad 2: " + i + " distancia " + matrizEuclidea[pos][i] + " coste total " + costeTotal);
                i = vectorgreedy.get(pos);
            }

            //se elimina la solucion del vector vsolucion
            Integer seleccionado = vectorgreedy.remove(pos);

            //el valor seleccionado del vector será nuestra solucion para agregar al vector
            vsolAlea.add(seleccionado);


        }

        int primeraCiudad = vsolAlea.get(0);
       // System.out.println("primer ciudad " + primeraCiudad);
        costeTotal += matrizEuclidea[matrizEuclidea.length-1][primeraCiudad];
        
        MedidorTiempos.finalizarYMostrar("Greedy Aleatorio");

        System.out.println("\t Coste total: " + costeTotal);
        // System.out.println("Minimo encontrado: " + ciudadesOrdenadas.get(0));
      //  System.out.println("Total ciudades leídas: " + matriz.length);
        //System.out.println("Última ciudad (índice " + (matriz.length - 1) + ") -> X: " + matriz[matriz.length - 1][1] + " Y: " + matriz[matriz.length - 1][2]);
        //System.out.println(" minima distnacia " + minDistancia);

        /*System.out.println("vector solucion en greedy");
        for(int j =0; j < vsolAlea.size(); j++){
            System.out.print(" " + vsolAlea.get(j));
        }*/

        //se devuelve la solucion
        return vsolAlea;
    }
        public void DontLookbits(){
            //recibir solucion greedy aleatorio

            //crear vector igual tamaño que solucion, vector mascara
            //0- puede cambiar 1-el valor es fijo. Comienza to_do en 0

            //verificamos en bucle los cambios
                // si ninguno provoca mejora v[i]=1
                //un movimiento genera solucion vecina con mejor coste v[i]=0


            //
        }

}


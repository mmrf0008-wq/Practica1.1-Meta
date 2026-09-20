import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

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

    /**
     * Calcula la distancia euclidea entre dos ciudades dadas sus coordenadas
     * @param matriz x2 ciudad 1
     * @param matriz3 y2 ciudad 2
     * @return distancia euclidea
     */

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

    private void printMatriz(double matriz[][]){
        for(int l = 0; l < matriz.length; l++){
            for(int m = 0; m < matriz.length; m++){
                System.out.print(" " + matriz[l][m]);
            }
            System.out.println("");
        }
    }


    public ArrayList<Integer> greedy(double matriz[][]){
// Diagnóstico para ver los datos reales leídos
    System.out.println("[VERIFICACION] Ciudad 0 X: " + matriz[0][1] + " Y: " + matriz[0][2]);
    System.out.println("[VERIFICACION] Ciudad 1 X: " + matriz[1][1] + " Y: " + matriz[1][2]);
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
        for (int i = 1; i < n; ++i) {
            double minDistancia = Double.MAX_VALUE;
            int siguienteCiudad = -1;

            // Ciudad más cercana a la ciudad actual que no haya sido visitada
            for (int j = 0; j < n; ++j) {
                if (!visitado[j] && matrizEuclidea[actual][j] < minDistancia) {
                    minDistancia = matrizEuclidea[actual][j];
                    siguienteCiudad = j;

                }
            }

            // La añadimos a la lista de ciudades ordenadas y marcamos como visitada
            if (siguienteCiudad != -1) {
                ciudadesOrdenadas.add(siguienteCiudad);
                visitado[siguienteCiudad] = true;
                costeTotal = minDistancia;
                actual = siguienteCiudad;
            }
        }
        
        int primeraCiudad = ciudadesOrdenadas.get(0);
        costeTotal += matrizEuclidea[actual][primeraCiudad];

        MedidorTiempos.finalizarYMostrar("Greedy");
        
        // Devolvemos la ruta de las ciudades ordenadas
        System.out.println("Coste total: " + costeTotal);
        // System.out.println("Minimo encontrado: " + ciudadesOrdenadas.get(0));
        System.out.println("Total ciudades leídas: " + matriz.length);
System.out.println("Última ciudad (índice " + (matriz.length - 1) + ") -> X: " + matriz[matriz.length - 1][1] + " Y: " + matriz[matriz.length - 1][2]);
        return ciudadesOrdenadas;
    }


    public ArrayList<Integer> greedyAleatorio(double matriz[][], long semilla){
        MedidorTiempos.empezarContador();
        int n = matriz.length;
        double matrizEuclidea[][] = new double[n][n];
        calculoMatrizEuclidea(matrizEuclidea,matriz);

        // Creamos un vector solucion de la misma forma que antes
        ArrayList<Candidato> vSolucion = new ArrayList<>(); //vector solucion del greedy, partimos de el para sacar el vsolAlea
       
        double sumatorio = 0;
        //suma de las distancias de las ciudades con respecto a la primer columna
        for(int i = 0; i < n; i++){
            sumatorio = 0;
            for(int j = 0; j < n; j++){
                sumatorio += matrizEuclidea[i][j];
            }
            // Guardamos el candidato con su sumatoria de distancias y su ciudad correspondiente en el vector de soluciones
            vSolucion.add(new Candidato(sumatorio, i));
        }

        // Ordenamos menor mayor
        Collections.sort(vSolucion);

        ArrayList<Integer> vsolAlea = new ArrayList<>(); //vector solucion de greedyAleatorio (vsolAlea = vector solucion Aleatorio)
        //se elige aleatoriamente un numero del 0-5
        int k = 5;

        java.util.Random rand = new java.util.Random(semilla);

        //en bucle
        while(!vSolucion.isEmpty()){
            // Para cuando quedan menos soluciones que k
            if(vSolucion.size() < k) {
                k = vSolucion.size();
            }

            int pos = rand.nextInt(k);

            //se elimina la solucion del vector vsolucion
            Candidato seleccionado = vSolucion.remove(pos);

            //el valor seleccionado del vector será nuestra solucion para agregar al vector
            vsolAlea.add(seleccionado.ciudad);
        }
        
        MedidorTiempos.finalizarYMostrar("Greedy Aleatorio");

        //se devuelve la solucion
        return vsolAlea;
    }
}

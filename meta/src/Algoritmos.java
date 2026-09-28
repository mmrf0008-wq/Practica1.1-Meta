import java.awt.image.AreaAveragingScaleFilter;
import java.lang.reflect.Array;
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


    private void printMatriz(){
        for(int l = 0; l < matriz.length; l++){
            for(int m = 0; m < matriz.length; m++){
                System.out.print(" " + matriz[l][m]);
            }
            System.out.println("");
        }
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
        
        int primeraCiudad = ciudadesOrdenadas.getFirst();

        costeTotal += distancia_euclidea( actual, primeraCiudad);

        MedidorTiempos.finalizarYMostrar("Greedy");

        // Devolvemos la ruta de las ciudades ordenadas
        System.out.println("\t Coste total: " + costeTotal);


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
                //System.out.println("coste total " + costeTotal);
                costeTotal += distancia_euclidea( pos, i);
                //System.out.println("ciudad 1: " + vectorgreedy.get(pos) + " -ciudad 2: " + i + " distancia " + matrizEuclidea[pos][i] + " coste total " + costeTotal);
                i = vectorgreedy.get(pos);
            }

            //se elimina la solucion del vector vsolucion
            Integer seleccionado = vectorgreedy.remove(pos);

            //el valor seleccionado del vector será nuestra solucion para agregar al vector
            vsolAlea.add(seleccionado);
        }

        int primeraCiudad = vsolAlea.getFirst();

        costeTotal +=  distancia_euclidea( matriz.length-1, primeraCiudad);
        System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        MedidorTiempos.finalizarYMostrar("Greedy Aleatorio");

        System.out.println("\t Coste total: " + costeTotal);


        //se devuelve la solucion
        return vsolAlea;
    }
    public void DontLookbits( ArrayList<Integer> solucion, double costeAnterior ){
            MedidorTiempos.empezarContador();

            ArrayList<Integer> vsolGA = solucion;           //recibir solucion greedy aleatorio
            int n = vsolGA.size();
            double distancia=0.0;
            double actual=0.0;
            double minimo =0.0;
            int ultimo= -1;
            int j=0;
            double ganancia =0.0;


            int[] vmascara = new int[n];        //crear vector igual tamaño que solucion, vector mascara

           for(int iterador =0; iterador < n-1; iterador++){
               if(vmascara[iterador]==1 ){             //si la ciudad se descarto continuamos con la siguiente
                   continue;
               }

                minimo = distancia_euclidea(vsolGA.get(iterador), vsolGA.get(iterador+1));
                actual = minimo;
                j =iterador+1;
                ultimo =-1;


                while(j != iterador ){
                    distancia = distancia_euclidea( vsolGA.get(iterador), vsolGA.get(j));

                    if(distancia < minimo){
                      //  System.out.println("la ciudad " + j + " tiene distancia "+ distancia + " y la distancia de la ciudad en " + iterador + " es " + minimo);
                        minimo = distancia;
                        ultimo = j;
                    }
                    j++;
                    if(j==n){
                        j=0;
                    }
                }
                if(actual > minimo && ultimo != -1){ //comprobamos si hay mejoras
                    vmascara[iterador] =0;
                    vmascara[ultimo] =0;
                    cambio(vsolGA, iterador, ultimo);
                    calculoNuevoCoste();

                }
                else{
                    System.out.println("iterador " + iterador + " cambia a 1 ");
                    vmascara[iterador] = 1; //no hay mejoras

                    for(int i =0; i < n; i++){
                        System.out.print(" " +vmascara[i]);
                    }
                    System.out.println();
                }

            }
            System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
            MedidorTiempos.finalizarYMostrar("Don't look bit");

            System.out.println("\t Coste total: " + costeAnterior);
        }
        private void cambio(ArrayList<Integer> vsolGA, int iterador, int ultimo){
                int a = vsolGA.get(iterador);
                int b=  vsolGA.get(ultimo);

                vsolGA.set(iterador, b);
                vsolGA.set(ultimo, a);

        }
        private void calculoNuevoCoste(){

        }
        public void setMatriz(double matriz[][]){
            this.matriz = matriz;
        }


}


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

        //recibir solucion greedy aleatorio
        int n = solucion.size();

            int[] vmascara = new int[n];        //crear vector igual tamaño que solucion, vector mascara

        System.out.println();
        Log.print("las distnacias son");
        for(int i =0; i <solucion.size()-1; i++){
            System.out.println("i(" + i + ") i+1 (" + (i+1) + ")= " + distancia_euclidea(i,i+1) + " ID CIUDAD I " + solucion.get(i) + " ID CIUDAD J " + solucion.get((i+1)));
        }


            //la i y la j son ciclicas
            int i =0;
            int j =0;
            boolean mejora = true;              // mejora true: hay ciudades que pueden ser movidas.    mejora=false: vmascara =1 entera
        boolean haymejora=false;
           //la i es ciclica
        //la j es ciclica
        //la condicion de parada es que no pueda mover mas (matriz entera en 1)

        while(mejora){
            mejora=false;
            haymejora=false;
            if(i==n) i=0;
            Log.print("---------------------------------------");
            if(vmascara[i]==1){
                Log.print("mascara en " + i + " vmascara =  " + vmascara[i]+ " =1");
                i++;
                continue; //descartamos aquellas que no tengan mejoras
            }
            mejora=true;
            j= (i + 1) % n;
            Log.print("j = " + j + " i= "+ i);

            while(j != i ){
                if(factorizacion(solucion,i,j, n)){ //si hay mejora hacemos cambio
                    Log.print("mascara en i: " + i + " y en j: " + j + " =0 ");
                    vmascara[i]=0;
                    vmascara[j] =0;
                    cambio(solucion, i, j);
                    haymejora=true;

                    break;
                }
                j++;
                if(j==n) j=0;
                Log.print("---------------------------------------");
            }
            if(!haymejora){
                Log.print("no hubo mejora en i: " + i );
                vmascara[i]=1;
                Log.print("vmascara[" + i +"]=1");
            }
            i++;
        }


        System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
        MedidorTiempos.finalizarYMostrar("Don't look bit");

        System.out.println("\t Coste total: " + costeAnterior);
        }

        private boolean factorizacion( ArrayList<Integer>solucion,int i, int j, int n ){

            if (i == j || (i + 1) % n == j || (j + 1) % n == i){
                Log.print("los nodos son contiguos, no se realiza el 2-opt");
                return false;
            }
            double nuevoCoste = 0;
                    //calculamos el nuevo coste
            if(i>=1) nuevoCoste += distancia_euclidea(solucion.get(j), solucion.get(i-1));
            if(i==0) nuevoCoste += distancia_euclidea(solucion.get(j), solucion.get(n-1));

            if(i < (n-1)){
                nuevoCoste += distancia_euclidea(solucion.get(j), solucion.get(i+1));
               // System.out.println("coste" + distancia_euclidea(solucion.get(j), solucion.get(i+1)));
            }
            if(i == n-1) nuevoCoste += distancia_euclidea(solucion.get(j),solucion.getFirst());

            if(j>= 1) nuevoCoste += distancia_euclidea(solucion.get(i), solucion.get(j-1));
            if(j==0) nuevoCoste += distancia_euclidea(solucion.get(i), solucion.get(n-1));

            if(j<n-1) nuevoCoste += distancia_euclidea(solucion.get(i), solucion.get(j+1));
            if(j== n-1) nuevoCoste += distancia_euclidea(solucion.get(i),solucion.getFirst());

            /*********************************************************************************/

            double costeAnterior =0;
            if(i>=1) costeAnterior += distancia_euclidea(solucion.get(i),solucion.get(i-1));
            if(i==0) costeAnterior += distancia_euclidea(solucion.get(i),solucion.get(n-1));

            if(i<n-1) costeAnterior += distancia_euclidea(solucion.get(i),solucion.get(i+1));
            if(i==n-1) costeAnterior+= distancia_euclidea(solucion.get(i),solucion.getFirst());

            if(j>=1) costeAnterior += distancia_euclidea(solucion.get(j),solucion.get(j-1));
            if(j==0) costeAnterior += distancia_euclidea(solucion.get(j),solucion.get(n-1));

            if(j < n-1) costeAnterior += distancia_euclidea(solucion.get(j),solucion.get(j+1));
            if( j == n-1) costeAnterior += distancia_euclidea(solucion.get(j),solucion.getFirst());

            Log.print("coste anterior " + costeAnterior + " costenuevo " + nuevoCoste + " i:"+ i + " j: " + j);

            if (costeAnterior <= nuevoCoste) {
                Log.print("devuelve false");
                return false; // no hubo mejora
            }
            else{
                Log.print("DEVUELVE TRUE ");
                return true;
            }

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


}


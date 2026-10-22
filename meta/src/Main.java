import java.util.ArrayList;

public class Main {
    public static void main(String[] args){

        String rutaRelativa = "src/config.txt";
        Log.print("Ruta actual de ejecución: " + new java.io.File(".").getAbsolutePath());
        Configuracion config = new Configuracion(rutaRelativa);

        int algoritmo=config.parametros;
        FuncionesAux algoritmos= new FuncionesAux();
        double costeTotal=0.0;
        ArrayList<Integer> solucion;
        Log.inicializar();
        AlgGreedy_Clase3_Grupo13 g = new AlgGreedy_Clase3_Grupo13();
        AlgGreedyAl_Clase3_Grupo13 ga = new AlgGreedyAl_Clase3_Grupo13();
        AlgTabu_Clase3_Grupo13 ta = new AlgTabu_Clase3_Grupo13();
        AlgDLB_Clase3_Grupo13 dlb = new AlgDLB_Clase3_Grupo13();
        AlgBusqLocal_Clase3_Grupo13 bl = new AlgBusqLocal_Clase3_Grupo13();


        //comando terminal sacar logs  javac *.java && java Main >> log.txt
        switch(algoritmo) {
            case 0:

                for (int i = 0; i < config.archivos.size(); i++) {
                    ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(i));
                    Log.print("-------------------GREEDY -------------------");
                    Log.print("***************** Archivo  " + config.getArchivo(i) + "*****************");
                    FuncionesAux.setMatriz(archivosDatos.getMatriz1());
                    g.greedy();

                }
                break;
            case 1:

                for (int i = 0; i < config.archivos.size(); i++) {
                    ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(i));
                    Log.print("-------------------GREEDY ALEATORIO -------------------");
                    Log.print("***************** Archivo  " + config.getArchivo(i) + "*****************");

                    for (int j = 0; j < config.semillas.size(); j++) {
                        Log.print("********************* SEMILLA " + j + " *********************");
                        FuncionesAux.setMatriz(archivosDatos.getMatriz1());
                         ga.greedyAleatorio( config.getSemilla(j), config.getK(), costeTotal);
                    }
                }
                break;
            case 2: //dont look bit
                for (int i = 0; i < config.archivos.size(); i++) {
                //int i = 5;

                    ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(i));
                    Log.print("-------------------Dont look bit -------------------");
                    Log.print("***************** Archivo  " + config.getArchivo(i) + "*****************");

                    for (int j = 0; j < config.semillas.size(); j++) {
                        Log.print("********************* SEMILLA " + j + " *********************");
                        FuncionesAux.setMatriz(archivosDatos.getMatriz1());
                        solucion=  ga.greedyAleatorio( config.getSemilla(j), config.getK(), costeTotal);
                        dlb.DontLookbits(solucion, costeTotal );
                    }
                }
                break;
            case 3:
                for (int i = 0; i < config.archivos.size(); i++) {
               //int i = 5;
                    ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(i));
                    Log.print("-------------------Busqueda Local Primero Mejor -------------------");
                    Log.print("***************** Archivo  " + config.getArchivo(i) + "*****************");

                    for (int j = 0; j < config.semillas.size(); j++) {
                        Log.print("********************* SEMILLA " + j + " *********************");
                        FuncionesAux.setMatriz(archivosDatos.getMatriz1());
                        solucion = ga.greedyAleatorio(config.getSemilla(j), config.getK(), costeTotal);
                        solucion = bl.blpm(solucion, config.getLimitIteraciones(), costeTotal);

                    }
                }
            break;
            case 4: //tabu
                AlgTabu_Clase3_Grupo13 t = new AlgTabu_Clase3_Grupo13();

                for (int i = 0; i < config.archivos.size(); i++) {
                    //int i = 5;
                    ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(i));
                    Log.print("-------------------Busqueda Tabú -------------------");
                    Log.print("***************** Archivo  " + config.getArchivo(i) + "*****************");

                    for (int j = 0; j < config.semillas.size(); j++) {
                        Log.print("********************* SEMILLA " + j + " *********************");
                        FuncionesAux.setMatriz(archivosDatos.getMatriz1());
                        solucion = ga.greedyAleatorio(config.getSemilla(j), config.getK(), costeTotal);
                        t.tabu(solucion, config.limitIteraciones, config.tenencia_tabu, config.oscilacionEstragegica, config.porcentaje_estancamiento);

                    }
                }
                break;
        }
    }
}

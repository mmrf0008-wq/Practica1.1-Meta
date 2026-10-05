import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        /*String ruta = "C:\\Users\\Maitena\\Desktop\\Apuntes\\3º\\meta\\Practica1-Meta\\src\\config.txt";*/
        String rutaRelativa = "src/config.txt";
        System.out.println("Ruta actual de ejecución: " + new java.io.File(".").getAbsolutePath());
        Configuracion config = new Configuracion(rutaRelativa);

        int algoritmo=config.parametros;
        Algoritmos algoritmos= new Algoritmos();
        double costeTotal=0.0;
        ArrayList<Integer> solucion = new ArrayList<>() ;
        Log.inicializar();

        //comando terminal sacar logs  javac *.java && java Main >> log.txt
        switch(algoritmo) {
            case 0:

                for (int i = 0; i < config.archivos.size(); i++) {
                    ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(i));
                    System.out.println("-------------------GREEDY -------------------");
                    System.out.println("***************** Archivo  " + config.getArchivo(i) + "*****************");
                    algoritmos.setMatriz(archivosDatos.getMatriz1());
                    algoritmos.greedy();

                }
                break;
            case 1:

                for (int i = 0; i < config.archivos.size(); i++) {
                    ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(i));
                    System.out.println("-------------------GREEDY ALEATORIO -------------------");
                    System.out.println("***************** Archivo  " + config.getArchivo(i) + "*****************");

                    for (int j = 0; j < config.semillas.size(); j++) {
                        System.out.println("********************* SEMILLA " + j + " *********************");
                        algoritmos.setMatriz(archivosDatos.getMatriz1());
                         algoritmos.greedyAleatorio( config.getSemilla(j), config.getK(), costeTotal);
                    }
                }
                break;
            case 2: //dont look bit
                for (int i = 0; i < config.archivos.size(); i++) {
                //int i = 5;

                    ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(i));
                    System.out.println("-------------------Dont look bit -------------------");
                    System.out.println("***************** Archivo  " + config.getArchivo(i) + "*****************");

                    for (int j = 0; j < config.semillas.size(); j++) {
                        System.out.println("********************* SEMILLA " + j + " *********************");
                        algoritmos.setMatriz(archivosDatos.getMatriz1());
                        solucion=  algoritmos.greedyAleatorio( config.getSemilla(j), config.getK(), costeTotal);
                        algoritmos.DontLookbits(solucion, costeTotal );
                    }
                }
                break;
            case 3:
                //for (int i = 0; i < config.archivos.size(); i++) {
                int i = 0;
                    ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(i));
                    System.out.println("-------------------Dont look bit -------------------");
                    System.out.println("***************** Archivo  " + config.getArchivo(i) + "*****************");

                    for (int j = 0; j < config.semillas.size(); j++) {
                        System.out.println("********************* SEMILLA " + j + " *********************");
                        algoritmos.setMatriz(archivosDatos.getMatriz1());
                        solucion = algoritmos.greedyAleatorio(config.getSemilla(j), config.getK(), costeTotal);
                        algoritmos.pdlb(solucion, config.getLimitIteraciones(), costeTotal);
                    }
               // }
            break;
        }
    }
}

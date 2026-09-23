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
        ArrayList<Integer> solucion = new ArrayList<>() ;

        //comando terminal sacar logs  javac *.java && java Main >> log.txt
        switch(algoritmo) {
            case 0:

                for (int i = 0; i < config.archivos.size(); i++) {
                    ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(i));
                    System.out.println("-------------------GREEDY -------------------");
                    System.out.println("***************** Archivo  " + config.getArchivo(i) + "*****************");
                    algoritmos.greedy(archivosDatos.getMatriz1());

                }
                break;
            case 1:

                for (int i = 0; i < config.archivos.size(); i++) {
                    ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(i));
                    System.out.println("-------------------GREEDY ALEATORIO -------------------");
                    System.out.println("***************** Archivo  " + config.getArchivo(i) + "*****************");

                    for (int j = 0; j < config.semillas.size(); j++) {
                        System.out.println("********************* SEMILLA " + j + " *********************");
                         algoritmos.greedyAleatorio(archivosDatos.getMatriz1(), config.getSemilla(j), config.getK());
                    }
                }
                break;
            case 2: //dont look bit
                for (int i = 0; i < config.archivos.size(); i++) {
                    ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(i));
                    System.out.println("-------------------GREEDY ALEATORIO -------------------");
                    System.out.println("***************** Archivo  " + config.getArchivo(i) + "*****************");

                    for (int j = 0; j < config.semillas.size(); j++) {
                        System.out.println("********************* SEMILLA " + j + " *********************");
                        solucion=  algoritmos.greedyAleatorio(archivosDatos.getMatriz1(), config.getSemilla(j), config.getK());
                        algoritmos.DontLookbits(archivosDatos.getMatriz1(),solucion );
                    }
                }
                break;

        }
    }
}

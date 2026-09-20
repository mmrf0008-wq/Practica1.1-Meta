import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        int archivoLeer =4;
        int algoritmo=1;

        /*String ruta = "C:\\Users\\Maitena\\Desktop\\Apuntes\\3º\\meta\\Practica1-Meta\\src\\config.txt";*/
        String rutaRelativa = "src/config.txt";
        System.out.println("Ruta actual de ejecución: " + new java.io.File(".").getAbsolutePath());
        Configuracion config = new Configuracion(rutaRelativa);

        ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(archivoLeer));
        Algoritmos algoritmos= new Algoritmos();

        config.setIndiceSemilla(2);


        //comando terminal sacar logs  javac *.java && java Main >> log.txt
   
        switch(config.getAlgoritmos(algoritmo)){
            case "greedy":
                
                System.out.println("Nombre del archivo: " + config.getArchivo(archivoLeer));
                algoritmos.greedy(archivosDatos.getMatriz1());
                break;
            case "greedyAleatorio":
                System.out.println("Nombre del archivo: " + config.getArchivo(archivoLeer) + " Algoritmo greedyAleatorio . Semilla "+config.getSemilla() );
                algoritmos.greedyAleatorio(archivosDatos.getMatriz1(), config.getSemilla());
        }
    }
}

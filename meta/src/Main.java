import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
       
        /*String ruta = "C:\\Users\\Maitena\\Desktop\\Apuntes\\3º\\meta\\Practica1-Meta\\src\\config.txt";*/
        String rutaRelativa = "src/config.txt";
        System.out.println("Ruta actual de ejecución: " + new java.io.File(".").getAbsolutePath());
        Configuracion config = new Configuracion(rutaRelativa);

        ArchivoDatos archivosDatos = new ArchivoDatos("src/" + config.getArchivo(1));
        Algoritmos algoritmos= new Algoritmos();


        //comando terminal sacar logs  javac *.java && java Main >> log.txt
   
        switch(config.getAlgoritmos(0)){
            case "greedy":
                
                System.out.println("Nombre del archivo: " + config.getArchivo(1));
                algoritmos.greedy(archivosDatos.getMatriz1());
                break;
            case "greedyAleatorio":
                System.out.println("Nombre del archivo: " + config.getArchivo(0));
                algoritmos.greedyAleatorio(archivosDatos.getMatriz1(), config.getSemilla());
        }
    }
}

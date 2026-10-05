import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private static boolean imprimir = false;
    private static PrintStream fileStream;

    public static void inicializar(){
        if(!imprimir){
            try {
                String RUTA_CARPETA ="src/log_out";
                File carpeta = new File(RUTA_CARPETA);
                if (!carpeta.exists()) {
                    carpeta.mkdirs(); // Crea la carpeta y sus subcarpetas si no existen
                }

                // 2. Crear el nombre del archivo con timestamp dentro de la ruta
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File archivoLog = new File(carpeta, "ejecucion_" + timestamp + ".txt");

                // 3. Crear el PrintStream del archivo
                fileStream = new PrintStream(archivoLog);

            } catch (FileNotFoundException e) {
                System.err.println("Error al crear el archivo de salida: " + e.getMessage());
                return;
            }
        }
    }
    public static void  print(String mensaje) {
        if (fileStream != null) {
            fileStream.println(mensaje);
        }
        if(imprimir){
            System.out.println(mensaje);
        }

    }

    public static void setImprimir(boolean valor) {
        imprimir = valor;
    }
    public static void cerrar() {
        if (fileStream != null) {
            fileStream.close();
        }
    }

}

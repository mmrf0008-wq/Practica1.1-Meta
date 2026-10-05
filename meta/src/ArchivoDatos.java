import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArchivoDatos {
    private double matriz1[][];
    private String nombre; //nombre de ficheros de la matriz
    private int matriz2[][];

    public ArchivoDatos(String ruta) {
        String linea;
        FileReader file = null;

        try {
            file = new FileReader(ruta);
            BufferedReader buffer = new BufferedReader(file);

            int dimension=0;
            //sacamos la dimensión
            boolean parar = false;
            while( ((linea = buffer.readLine())!= null) && !parar){


                String[] split = linea.split(":");
                if (split[0].equals("DIMENSION") || split[0].equals("DIMENSION ")) {
                    String a[] = split[1].split(" ");
                    dimension = Integer.parseInt(a[1]);
                    parar = true;
                }
            }

            linea = buffer.readLine();

            //inicializamos las matrices al numero de filas y columnas indicado
            matriz1 = new double[dimension][3];

            //rellenamos las matrices con el contenido del doc
            for(int i =0; i < dimension; i++){
                linea = buffer.readLine();
                String split[] = linea.split(" ");
                int errores =0;
                for( int j =0; j < split.length; j++){
                    //como el archivo no tiene la misma cantidad de espacios en el doc para separar hacemos esto
                    try{
                        matriz1[i][j-errores] = Double.parseDouble(split[j]);
                    }catch (NumberFormatException e ){
                        errores++;
                    }
                }
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double[][] getMatriz1() {
        return matriz1;
    }
    public static void escrituraFichero(String contenido){
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formato);

        // 2. Construir el nombre dinámico del archivo
        String nombreArchivo = "mensaje_" + timestamp + ".txt";
        Path ruta = Paths.get(nombreArchivo);


        try {
            // Escribe el archivo. Al tener un nombre único, siempre se creará uno nuevo
            Files.writeString(ruta, contenido);
            System.out.println("Se ha creado un nuevo archivo: " + ruta.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al crear el archivo: " + e.getMessage());
        }
    }
}


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ArchivoDatos {

    private double[][] matriz1;
    private String nombre; //nombre de ficheros de la matriz

    public ArchivoDatos(String ruta) throws IOException {
        String linea;
        FileReader file = null;

        try (BufferedReader buffer = new BufferedReader(new FileReader(ruta))){
            
            int dimension = 0;
            //sacamos la dimensión
            boolean parar = false;

            while (((linea = buffer.readLine())!= null) && !parar){

                    String[] split = linea.split(":");
                    if (split[0].trim().equalsIgnoreCase("DIMENSION")) {
                        dimension = Integer.parseInt(split[1].trim());
                        parar = true;
                    }
            }

            parar = false;
            while ((linea = buffer.readLine()) != null  && !parar ) {
                if (linea.trim().toUpperCase().startsWith("NODE_COORD_SECTION")) {
                    parar = true;
                }
            }

            //inicializamos las matrices al numero de filas y columnas indicado
            matriz1 = new double[dimension][3];

            //rellenamos las matrices con el contenido del doc
            int i = 0;
            while( i < dimension && (linea = buffer.readLine()) != null){
                linea = linea.trim();
                if (linea.isEmpty() || linea.equalsIgnoreCase("EOF")) {
                    continue;
                }

                String[] split = linea.trim().split("\\s+");
                    
                if (split.length >= 3) {
                    matriz1[i][0] = (int) Double.parseDouble(split[0]);
                    matriz1[i][1] = (int) Double.parseDouble(split[1]);
                    matriz1[i][2] = (int) Double.parseDouble(split[2]);
                    i++;
                }
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo: " + e.getMessage());
        }
    }

    public double[][] getMatriz1() {
        return matriz1;
    }

}


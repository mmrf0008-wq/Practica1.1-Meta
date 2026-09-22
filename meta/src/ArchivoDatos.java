import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

}


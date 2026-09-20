import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Long.parseLong;

public class Configuracion {
    ArrayList<String> archivos;
    ArrayList<String> algoritmos;
    ArrayList<Long> semillas;
    Integer parametros;


    public Configuracion(String ruta){
        archivos= new ArrayList<>();
        algoritmos= new ArrayList<>();
        semillas=  new ArrayList<>();
        String linea;
        FileReader file = null;

        try{
            file = new FileReader(ruta);
            BufferedReader buffer = new BufferedReader(file);
            while((linea = buffer.readLine())!= null){
                String[] split = linea.split("=");
                switch(split[0]){
                    case "Archivos":
                        String[] v= split[1].split(" ");
                        for(int i =0; i < v.length; i++){
                            archivos.add(v[i]);
                        }
                        break;

                    case "Semilla":
                        String[] v1= split[1].split(" ");
                        for(int i =0; i < v1.length; i++){
                            semillas.add(parseLong(v1[i]));
                        }
                        break;
                    case "Algoritmos":
                        String[] v2= split[1].split(" ");
                        for(int i =0; i < v2.length; i++){
                            algoritmos.add(v2[i]);
                        }
                        break;
                    case "Parametros":
                        parametros = Integer.parseInt(split[1]);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAlgoritmos(int i) {
        return algoritmos.get(i);
    }
    public String getArchivo(int i) {
        return archivos.get(i);
    }
     public Long getSemilla() {
        return semillas.get(0);
    }
}

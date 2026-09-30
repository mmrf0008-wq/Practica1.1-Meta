public class Log {
    private static boolean imprimir = true;

    public static void  print(String mensaje) {
        if (imprimir) {
            System.out.println(mensaje);
        }
    }

    public static void setImprimir(boolean valor) {
        imprimir = valor;
    }
}

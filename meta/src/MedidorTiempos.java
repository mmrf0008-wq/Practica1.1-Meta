public  class MedidorTiempos {

  

    private static long tiempoInicio;

    /**
     * Captura el momento exacto de inicio en nanosegundos.
     */
    public static void empezarContador() {
        MedidorTiempos.tiempoInicio = System.nanoTime();
    }

    /**
     * Detiene la medición y devuelve el tiempo transcurrido en milisegundos (ms).
     */
    public static double finalizarContador() {
        long tiempoFin = System.nanoTime();
        long diferenciaNanos = tiempoFin - MedidorTiempos.tiempoInicio;
        
        // Convertimos a milisegundos dividiendo entre 1,000,000 (usando punto flotante)
        return diferenciaNanos / 1_000_000.0;
    }

    /**
     * Método opcional para detener la medición e imprimir directamente en consola.
     */
    public static void finalizarYMostrar(String nombreProceso) {
        double milisegundos = finalizarContador();
        System.out.printf("[%s] Tiempo de ejecución: %.4f ms%n", nombreProceso, milisegundos);
    }
}

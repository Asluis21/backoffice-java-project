import java.util.*;

/**
 * Tema01
 */
public class Tema01 {

    public static void main(String[] args) throws Exception{

        Object sht = (short) 3;

        try {    
            Integer numInt = (Integer) sht;
            System.out.println(numInt);

        } catch (ClassCastException e) {
            System.out.println("Cannot convert from short to Integer");
        }

        Number numNumber = (Number) sht;
        System.out.println("numNumber = " + numNumber);

        Map<String, Object> sampleMap = new HashMap<>();
        sampleMap.put("Short", (short) 7);
        sampleMap.put("Long", 9L);
        sampleMap.put("Null", null);
        sampleMap.put("Enersto", "cinco");

        try {
            int noExiste = (int) sampleMap.get("no_existe");
            System.out.println("no existe: " + noExiste);
        } catch (NullPointerException e) {
            System.out.println("No existe dentro del map");
        }

        /* 
        start  = System.nanoTime();

        sampleMap.keySet().forEach(e ->{
            System.out.println("=========== Start leer flag ===========");
            System.out.println(leerFlag(sampleMap, e));
            System.out.println("=========== Finish leer flag ===========");

        });

        long lambdaTime = System.nanoTime() - start;
        */

        for(String e : sampleMap.keySet()){
            System.out.println("=========== Start leer flag ===========");

            System.out.println(e);
            System.out.println(leerFlag(sampleMap, e));
            System.out.println("=========== Finish leer flag ===========");

        }

        Integer num_127 = 127;
        Integer num2_127 = 127;
        
        Integer num_128 = 128;
        Integer num2_128 = 128;


        // 127 == 127 es TRUE ya que se guarda una cache de valores de -128 hasta 127, dado que 127 es el ultimo valor, ambos integer 127 tienen el mismo valor dentro del cache de Integer
        System.out.println("127 == 127 scenario: " + (num_127 == num2_127)); // true

        // 128 sin embargo esta fuera del rango de la cache de integer, por lo tanto retorna false
        System.out.println("128 == 128 scenario: " + (num_128 == num2_128)); // false

        /**
         * En conclusion desde -128 hasta 127 son numeros integers que ya estan creados dentro del Heap que es el area de memoria donde se almacenan los objetos
         * Entonces cuando se compara integers entre ese rango ambos apuntan hacia el mismo objeto del heap, por esa razon son iguales 
         */
    }

    static int leerFlag(Map<String, Object> m, String clave){

        Object v = m.get(clave);
        return v instanceof Number ? ((Number) v).intValue() : 0;
    }
    
}
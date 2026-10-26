package lab.db_studio.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lab.db_studio.exceptions.InvalidResponseException;

public class ResultPrinter {
    
    private ResultPrinter(){}

    private static String printHeader(List<String> headers){

        return headers.stream()
            .map(e -> String.format("%-10s", "| " + e + " |"))
            .collect(Collectors.joining(""));
    }

    private static String separatorLine(List<String> headers) {
        
        return headers.stream()
            .map(e -> String.format("%-10s", "-".repeat(e.length() + 2)))
            .collect(Collectors.joining("+", "+", "+"));
    }

    public static void printInConsole(List<Map<String, Object>> result){

        if (result == null) throw new InvalidResponseException("Null result cannot be printed");

        List<String> columns =  new ArrayList<>(result.get(0).keySet());

        System.out.println(printHeader(columns));
        System.out.println(separatorLine(columns));

        for (int rowIndex = 0; rowIndex < result.size(); rowIndex++) {
            
            for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {

                Object value = result.get(rowIndex).get(columns.get(columnIndex));

                System.out.print(String.format("%-10s", "| " + (value == null ? "null" : value) + " |"));
            }

            System.out.println();
        }


        System.out.println();
        System.out.println(result.size() + " row(s)");
    }
}

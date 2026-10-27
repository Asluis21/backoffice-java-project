package lab.db_studio.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lab.db_studio.exceptions.InvalidResponseException;

public class ResultPrinter {
    
    private ResultPrinter(){}

    private static String printHeader(List<String> headers, int[] widths){

        return headers.stream()
            .map(e -> "| " + String.format("%-" +  e.length() + "s", e) + " ")
            .collect(Collectors.joining(""));
    }

    private static String separatorLine(List<String> headers, int[] widths) {
        
        return headers.stream()
            .map(e -> "-".repeat(e.length() + 2))
            .collect(Collectors.joining("+", "+", "+"));
    }

    public static void printInConsole(List<Map<String, Object>> result){

        if (result == null) throw new InvalidResponseException("Null result cannot be printed");

        List<String> columns =  new ArrayList<>(result.get(0).keySet());

        
        int[] widthHeaders = widthHeaders(columns);
        
        widthHeaders = widths(widthHeaders, columns, result);
        
        System.out.println(printHeader(columns, widthHeaders));
        
        System.out.println(separatorLine(columns, widthHeaders));

        for (int rowIndex = 0; rowIndex < result.size(); rowIndex++) {
            
            for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {

                Object value = result.get(rowIndex).get(columns.get(columnIndex));

                System.out.print("|" + String.format("%-" + widthHeaders[columnIndex] + "s", value));
            }

            System.out.println("|");
        }


        System.out.println();
        System.out.println(result.size() + " row(s)");
    }

    private static int[] widthHeaders(List<String> headers){
        int[] width = new int[headers.size()];

        for (int i = 0; i < width.length; i++) {
            width[i] = headers.get(i).length();
        }
        
        return  width;
    }

    private static int[] widths(int[] widthHeaders, List<String> columns, List<Map<String, Object>> result){
        
        for (int rowIndex = 0; rowIndex < result.size(); rowIndex++) {
            
            for (int columnIndex = 0; columnIndex < widthHeaders.length; columnIndex++) {

                Object value = result.get(rowIndex).get(columns.get(columnIndex));

                widthHeaders[columnIndex] = Math.max(widthHeaders[columnIndex], String.valueOf(value).length());
            }

            System.out.println("|");
        }

        return widthHeaders;
    }
}

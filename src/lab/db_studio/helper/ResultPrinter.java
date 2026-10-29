package lab.db_studio.helper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lab.db_studio.exceptions.InvalidResponseException;

public class ResultPrinter {

    private ResultPrinter(){}

    private static String printHeader(String[] headers, int[] widths){

        return IntStream
            .range(0, headers.length)
            .mapToObj(i -> "| " + String.format("%-" +  widths[i] + "s", headers[i]) + " ")
            .collect(Collectors.joining("", "", "|"));
    }

    private static String separatorLine(String[] headers, int[] widths) {
        
        return IntStream
            .range(0, headers.length)
            .mapToObj(i -> "-".repeat(widths[i] + 2))
            .collect(Collectors.joining("+", "+", "+"));
    }

    public static void printInConsole(String[] columns, List<Map<String, Object>> result){

        if (result == null) throw new InvalidResponseException("Null result cannot be printed");

        int[] widthHeaders = calculateWidths(columns);
        
        widthHeaders = widths(widthHeaders, columns, result);

        System.out.println(separatorLine(columns, widthHeaders));

        System.out.println(printHeader(columns, widthHeaders));
        
        System.out.println(separatorLine(columns, widthHeaders));

        for (int rowIndex = 0; rowIndex < result.size(); rowIndex++) {
            
            for (int columnIndex = 0; columnIndex < columns.length; columnIndex++) {

                Object value = result.get(rowIndex).get(columns[columnIndex]);

                System.out.print("| " + String.format("%-" + widthHeaders[columnIndex] + "s", value) + " ");
            }

            System.out.println("|");
        }

        System.out.println(separatorLine(columns, widthHeaders));
        System.out.println();

        System.out.println(result.size() + " row(s)");
    }

    private static int[] calculateWidths(String[] columns){
        int[] width = new int[columns.length];

        for (int i = 0; i < width.length; i++) {
            width[i] = columns[i].length();
        }
        
        return  width;
    }

    private static int[] widths(int[] widthHeaders, String[] columns, List<Map<String, Object>> result){
        
        for (int rowIndex = 0; rowIndex < result.size(); rowIndex++) {
            
            for (int columnIndex = 0; columnIndex < widthHeaders.length; columnIndex++) {

                Object value = result.get(rowIndex).get(columns[columnIndex]);

                widthHeaders[columnIndex] = Math.max(widthHeaders[columnIndex], String.valueOf(value).length());
            }
        }

        return widthHeaders;
    }
}

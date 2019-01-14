package edu.rit.cs.mmior.ResultSetTable;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableWriter {
  public static void writeResult(ResultSet result) throws SQLException {
    writeResult(System.out, result);
  }

  public static void writeResult(PrintStream out, ResultSet result) throws SQLException {
    ResultSetMetaData metadata = result.getMetaData();
    int numColumns = metadata.getColumnCount();
    List<String> header = new ArrayList<>(numColumns);
    List<Integer> maxWidths = new ArrayList<>(numColumns);
    for (int i = 0; i < numColumns; i++) {
      String columnName = metadata.getColumnLabel(i + 1);
      header.add(columnName);
      maxWidths.add(i, columnName.length());
    }

    // Consume all rows and calculate the widths needed
    List<List<String>> data = new ArrayList<>();
    while (result.next()) {
      List<String> row = new ArrayList<>(numColumns);
      for (int i = 0; i < numColumns; i++) {
        String column = result.getString(i + 1);
        row.add(column);

        // Check and update the maximum width if needed
        if (column.length() > maxWidths.get(i)) {
          maxWidths.set(i, column.length());
        }
      }
      data.add(row);
    }

    // Write the header, a separator and all the rows
    out.print(rowToString(header, maxWidths));
    int totalWidth = Math.max((header.size() -1) * 3 - 1, 0);
    for (Integer width : maxWidths) { totalWidth += width; }
    String separator = new String(new char[totalWidth]).replace("\0", "-");
    out.println(separator);
    for (List<String> row : data) {
      out.print(rowToString(row, maxWidths));
    }
  }

  private static String rowToString(List<String> columns, List<Integer> widths) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < columns.size(); i++) {
      sb.append(String.format("%-" + widths.get(i) + "." + widths.get(i) + "s", columns.get(i)));
      if (columns.size() > 1) {
        sb.append(" | ");
      }
    }
    sb.append("\n");
    return sb.toString();
  }
}

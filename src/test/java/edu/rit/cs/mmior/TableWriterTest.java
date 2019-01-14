package edu.rit.cs.mmior.ResultSetTable;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class TableWriterTest {
  @Test
  public void emptyResult() throws IOException, SQLException {
    Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");
    Statement stmt = connection.createStatement();
    ResultSet result = stmt.executeQuery("SELECT 1");

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    TableWriter.writeResult(new PrintStream(baos), result);
    baos.flush();
    String output = baos.toString();
    assertEquals(output, "1\n-\n1\n");
  }
}

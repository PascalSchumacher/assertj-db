/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.db.api;

import org.assertj.db.exception.AssertJDBException;
import org.assertj.db.type.Changes;
import org.assertj.db.type.Request;
import org.assertj.db.type.Source;
import org.assertj.db.type.Table;

import java.io.*;
import java.util.List;

/**
 * Entry point of all the assertions.
 * <p>
 *   The navigation methods are defined in <a href="navigation/package-summary.html">navigation package</a>.<br>
 *   The assertion methods are defined in <a href="assertions/package-summary.html">assertions package</a>.
 * </p>
 * <p>
 * Example with a {@link Source} and a {@link Table} with test on the content on the first row of the {@code movie}
 * table that the {@code title} column contains "Alien" like text and the next column contains 1979 like number :
 * </p>
 * 
 * <pre>
 * <code class='java'>
 * Source source = new Source(&quot;jdbc:h2:mem:test&quot;, &quot;sa&quot;, &quot;&quot;);
 * Table table = new Table(source, &quot;movie&quot;);
 * assertThat(table)
 *     .row()
 *        .value("title")
 *            .isEqualTo("Alien")
 *        .returnToRow()
 *        .value()
 *            .isEqualTo(1979);
 * </code>
 * </pre>
 * 
 * <p>
 * It is possible to chain assertion on a value :
 * </p>
 * 
 * <pre>
 * <code class='java'>
 * assertThat(table)
 *     .row()
 *         .value("title")
 *             .isText()
 *             .isEqualTo("Alien");
 * </code>
 * </pre>
 * 
 * <p>
 * It is not necessary to use the <code>returnToXxxx</code> methods. The next example is equivalent to the first :
 * </p>
 * 
 * <pre>
 * <code class='java'>
 * Source source = new Source(&quot;jdbc:h2:mem:test&quot;, &quot;sa&quot;, &quot;&quot;);
 * Table table = new Table(source, &quot;movie&quot;);
 * assertThat(table)
 *     .row()
 *        .value("title")
 *            .isEqualTo("Alien")
 *        .value()
 *            .isEqualTo(1979);
 * </code>
 * </pre>
 * 
 * <p>
 * It is possible to do the same thing with column and the row :
 * </p>
 * 
 * <pre>
 * <code class='java'>
 * assertThat(table)
 *     .row()
 *        .value("title")
 *            .isEqualTo("Alien")
 *     .row()
 *        .value()
 *            .isEqualTo("The Village")
 *     .column("year")
 *         .value(1)
 *            .equalTo(2004);
 * </code>
 * </pre>
 * 
 * @author Régis Pouiller
 * 
 */
public final class Assertions {

  /**
   * Private constructor of the entry point.
   */
  private Assertions() {
    // empty
  }

  /**
   * Creates a new instance of {@link TableAssert}.
   * 
   * @param table The table to assert on.
   * @return The created assertion object.
   */
  public static TableAssert assertThat(Table table) {
    return new TableAssert(table).as(table.getName() + " table");
  }

  /**
   * Creates a new instance of {@link RequestAssert}.
   * 
   * @param request The request to assert on.
   * @return The created assertion object.
   */
  public static RequestAssert assertThat(Request request) {
    String sql = request.getRequest();
    if (sql.length() > 30) {
      sql = sql.substring(0, 30) + "...";
    }
    return new RequestAssert(request).as("'" + sql + "' request");
  }

  /**
   * Creates a new instance of {@link ChangesAssert}.
   * 
   * @param changes The changes to assert on.
   * @return The created assertion object.
   */
  public static ChangesAssert assertThat(Changes changes) {
    StringBuilder stringBuilder = new StringBuilder();
    if (changes.getTablesList() != null) {
      List<Table> tablesList = changes.getTablesList();
      if (tablesList.size() == 1) {
        Table table = tablesList.get(0);
        stringBuilder.append("Changes on ").append(table.getName()).append(" table");
      } else {
        stringBuilder.append("Changes on tables");
      }
    } else {
      Request request = changes.getRequest();
      String sql = request.getRequest();
      if (sql.length() > 30) {
        sql = sql.substring(0, 30) + "...";
      }
      stringBuilder.append("Changes on '").append(sql).append("' request");
    }
    if (changes.getSource() != null) {
      Source source = changes.getSource();
      stringBuilder.append(" of '").append(source.getUser()).append("/").append(source.getUrl()).append("' source");
    } else {
      stringBuilder.append(" of a data source");
    }
    return new ChangesAssert(changes).as(stringBuilder.toString());
  }

  /**
   * Reads the bytes from an {@link InputStream}.
   * 
   * @param inputStream The {@link InputStream}
   * @return A array of {@code byte}
   * @throws AssertJDBException If triggered, this exception wrap a possible {@link IOException} during the loading.
   */
  private static byte[] read(InputStream inputStream) {
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

      int byteInt = inputStream.read();
      while (byteInt != -1) {
        byteArrayOutputStream.write(byteInt);
        byteInt = inputStream.read();
      }

      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      throw new AssertJDBException(e);
    }
  }

  /**
   * Reads the bytes from a file and returns them.
   * 
   * @param file The {@link File}
   * @return The bytes of the file.
   * @throws NullPointerException If the {@code file} field is {@code null}.
   * @throws AssertJDBException If triggered, this exception wrap a possible {@link IOException} during the loading.
   */
  public static byte[] bytesContentOf(File file) {
    if (file == null) {
      throw new NullPointerException("File must be not null");
    }

    try (InputStream inputStream = new FileInputStream(file)) {
      return read(inputStream);
    } catch (IOException e) {
      throw new AssertJDBException(e);
    }
  }

  /**
   * Reads the bytes from a file in the classpath and returns them.
   * 
   * @param resource The name of the file in the classpath.
   * @return The bytes of the file.
   * @throws NullPointerException If the {@code resource} field is {@code null}.
   */
  public static byte[] bytesContentFromClassPathOf(String resource) {
    if (resource == null) {
      throw new NullPointerException("Resource must be not null");
    }

    ClassLoader classLoader = Assertions.class.getClassLoader();
    try (InputStream inputStream = classLoader.getResourceAsStream(resource)) {
      if (inputStream == null) {
        throw new AssertJDBException("Resource %s not found in the classpath", resource);
      }

      return read(inputStream);
    } catch (IOException e) {
      throw new AssertJDBException(e);
    }
  }
}

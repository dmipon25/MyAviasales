package com.dmipon25;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class AddCommand implements Command {

    @Override
    public void execute(Connection db, FileWriter fw, String[] parameters) throws IOException {
        try (PreparedStatement insert =
                     db.prepareStatement("INSERT INTO ORDERS VALUES(default, ?, ?, ?, ?)")) {
            for (int i = 0; i < parameters.length; i++) {
                insert.setString(i + 1, parameters[i].trim());
            }
            insert.executeUpdate();
            try (PreparedStatement insert_output =
                         db.prepareStatement("SELECT * FROM ORDERS WHERE ID=(SELECT SCOPE_IDENTITY())")) {
                ResultSet rs = insert_output.executeQuery();
                while (rs.next()) {
                    fw.write(String.format("%s, %s, %s, %s, %s",
                            rs.getString("ID"),
                            rs.getString("LOCATION"),
                            rs.getString("FLIGHT"),
                            rs.getString("NAME"),
                            rs.getString("DATE")) + "\n");
                }
                fw.flush();
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("Убедитесь, что вводимые параметры верные" /*+ e.getMessage()*/);
        }
    }
}
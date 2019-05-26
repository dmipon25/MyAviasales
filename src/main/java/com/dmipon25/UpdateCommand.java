package com.dmipon25;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class UpdateCommand implements Command {

    @Override
    public void execute(Connection db, FileWriter fw, String[] parameters) throws IOException {
        String temp = parameters[0];
        System.arraycopy(parameters, 1, parameters, 0, parameters.length - 1);
        parameters[parameters.length - 1] = temp;
        try (PreparedStatement update =
                     db.prepareStatement("UPDATE ORDERS SET LOCATION=? " +
                             ",FLIGHT=? " +
                             ",NAME=? " +
                             ",DATE=? " +
                             "WHERE ID=?")) {
            for (int i = 0; i < parameters.length; i++) {
                update.setString(i + 1, parameters[i].trim());
            }
            update.executeUpdate();
            try (PreparedStatement update_output =
                         db.prepareStatement("SELECT * FROM ORDERS WHERE ID=?")) {

                update_output.setString(1, parameters[parameters.length - 1]);
                ResultSet rs = update_output.executeQuery();
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    fw.write(String.format("%s, %s, %s, %s, %s",
                            rs.getString("ID"),
                            rs.getString("LOCATION"),
                            rs.getString("FLIGHT"),
                            rs.getString("NAME"),
                            rs.getString("DATE")) + "\n");
                }
                if (!found)
                    fw.write("Not found\n");
                fw.flush();
                rs.close();
            }

        } catch (SQLException e) {
            System.out.println("Убедитесь, что вводимые параметры верные" /*+ e.getMessage()*/);
        }
    }
}
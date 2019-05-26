package com.dmipon25;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShowCommand implements Command {

    @Override
    public void execute(Connection db, FileWriter fw, String[] parameters) throws IOException {
        try (PreparedStatement select_output =
                     db.prepareStatement("SELECT * FROM ORDERS WHERE ID=?")) {
            for (int i = 0; i < parameters.length; i++) {
                select_output.setString(i + 1, parameters[i]);
            }
            ResultSet rs = select_output.executeQuery();
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
        } catch (SQLException e) {
            System.out.println("Убедитесь, что вводимые параметры верные" /*+ e.getMessage()*/);
        }
    }
}
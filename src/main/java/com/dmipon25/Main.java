package com.dmipon25;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String command = "";
        try (FileWriter fw = new FileWriter("output.txt");
             Connection db = DriverManager.getConnection("jdbc:h2:mem:")) {
            try (Statement dataQuery = db.createStatement()) {
                dataQuery.execute("CREATE TABLE ORDERS(ID VARCHAR2(255) AUTO_INCREMENT PRIMARY KEY, " +
                        "LOCATION VARCHAR2(255), " +
                        "FLIGHT VARCHAR2(255), " +
                        "NAME VARCHAR2(255), " +
                        "DATE VARCHAR2(55))");
                //dataQuery.execute("INSERT INTO ORDERS VALUES(default, 'Omsk', '001', 'Ivanov','2019-12-01')");
                //dataQuery.execute("INSERT INTO ORDERS VALUES(default, 'Tomsk', '002', 'Petrov','2019-12-02')");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!command.equals("exit")) {
                String query = reader.readLine();
                if (query.contains(" ")) {
                    command = query.substring(0, query.indexOf(" "));
                } else command = query;

                Command cmd = getCommand(command);
                if (cmd != null) {
                    String[] parameters = (query.length() > command.length() ? query.substring(command.length() + 1) : "")
                            .split(",", -1);
                    cmd.execute(db, fw, parameters);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Убедитесь, что вводимые параметры верные " + ex.getMessage());
        }
    }

    private static Command getCommand(String command) {
        switch (command) {
            case ("add"):
                return new AddCommand();
            case ("update"):
                return new UpdateCommand();
            case ("delete"):
                return new DeleteCommand();
            case ("show"):
                return new ShowCommand();
            case ("showAll"):
                return new ShowAllCommand();
            case ("exit"):
                return null;
            default:
                System.out.println("Введите корректную команду");
                return null;
        }
    }
}
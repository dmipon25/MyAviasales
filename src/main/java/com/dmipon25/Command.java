package com.dmipon25;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;

public interface Command {
    void execute(Connection db, FileWriter fw, String[] parameters) throws IOException;
}
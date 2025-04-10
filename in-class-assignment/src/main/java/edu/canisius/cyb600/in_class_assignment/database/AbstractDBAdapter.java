package edu.canisius.cyb600.in_class_assignment.database;

import edu.canisius.cyb600.lab4.dataobjects.Actor;
import edu.canisius.cyb600.lab4.dataobjects.Film;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDBAdapter {
    Connection conn;

    public AbstractDBAdapter(Connection conn) {
        this.conn = conn;
    }

    //SELECTS
    public abstract List<Film> getAllFilms();

    public abstract List<Actor> getActorsWithLastName(String lastName);

    //INSERTS
    public abstract Actor addActor(Actor actor);

    //JOIN
    public abstract List<Film> getFilmsForActor(Actor actor);

    public abstract List<Actor> getAllActors();

    public abstract List<Actor> getAllActorsWithLastNameWithCode(String lastName);
}



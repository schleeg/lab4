package edu.canisius.cyb600.in_class_assignment.database;

import edu.canisius.cyb600.lab4.dataobjects.Actor;
import edu.canisius.cyb600.lab4.dataobjects.Film;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresDBAdapter extends AbstractDBAdapter {

    public PostgresDBAdapter(Connection conn) {
        super(conn);
    }

    @Override
    public List<Film> getAllFilms() {
        //First, we are going to open up a new statement
        try (Statement statement = conn.createStatement()) {
            //This statement is easy
            //Select * from actor is saying "Return all Fields for all rows in films". Because there
            //is no "where clause", all rows are returned
            ResultSet results = statement.executeQuery("Select * from film");
            //Initialize an empty List to hold the return set of films.
            List<Film> films = new ArrayList<>();
            //Loop through all the results and create a new Film object to hold all its information
            while (results.next()) {
                Film film = new Film();
                film.setFilmId(results.getInt("FILM_ID"));
                film.setTitle(results.getString("TITLE"));
                film.setDescription(results.getString("DESCRIPTION"));
                film.setReleaseYear(results.getString("RELEASE_YEAR"));
                film.setLanguageId(results.getInt("LANGUAGE_ID"));
                film.setRentalDuration(results.getInt("RENTAL_DURATION"));
                film.setRentalRate(results.getDouble("RENTAL_RATE"));
                film.setLength(results.getInt("LENGTH"));
                film.setReplacementCost(results.getDouble("REPLACEMENT_COST"));
                film.setRating(results.getString("RATING"));
                film.setSpecialFeatures(results.getString("SPECIAL_FEATURES"));
                film.setLastUpdate(results.getDate("LAST_UPDATE"));
                //Add film to the array
                films.add(film);
            }
            //Return all the films.
            return films;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }
    @Override
    public List<Actor> getAllActors() {
        try (Statement statement = conn.createStatement()) {
            ResultSet results = statement.executeQuery("SELECT * FROM actor");
            List<Actor> actors = new ArrayList<>();
            while (results.next()) {
                Actor actor = new Actor();
                actor.setActorId(results.getInt("actor_id"));
                actor.setFirstName(results.getString("first_name"));
                actor.setLastName(results.getString("last_name"));
                actor.setLastUpdate(results.getDate("last_update"));
                actors.add(actor);
            }
            return actors;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }



    @Override
    public List<Actor> getActorsWithLastName(String lastName) {
        String sql = "SELECT * FROM actor WHERE last_name = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, lastName.toUpperCase());
            ResultSet results = statement.executeQuery();
            List<Actor> actors = new ArrayList<>();
            while (results.next()) {
                Actor actor = new Actor();
                actor.setActorId(results.getInt("actor_id"));
                actor.setFirstName(results.getString("first_name"));
                actor.setLastName(results.getString("last_name"));
                actor.setLastUpdate(results.getDate("last_update"));
                actors.add(actor);
            }
            return actors;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }


    @Override
    public List<Actor> getAllActorsWithLastNameWithCode(String lastName) { // Added parameter
        List<Actor> actors = getAllActors();
        List<Actor> targetActors = new ArrayList<Actor>();
        for (Actor actor : actors){
            if(actor.getLastName().toLowerCase().equals(lastName.toLowerCase())){
                targetActors.add(actor);
            }
        }
        return  targetActors;
    }
    @Override
    public Actor addActor(Actor actor) {
        String sql = "INSERT INTO ACTOR (first_name, last_name) VALUES (? , ? ) returning ACTOR_ID, LAST_UPDATE";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            int i = 1;
            statement.setString(i++, actor.getFirstName().toUpperCase());
            statement.setString(i++, actor.getLastName().toUpperCase());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                actor.setActorId(results.getInt("ACTOR_ID"));
                actor.setLastUpdate(results.getDate("LAST_UPDATE"));
            }
            return actor;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return actor;
    }

    @Override
    public List<Film> getFilmsForActor(Actor actor) {
        String sql = "select * from actor, film_actor, film where actor.actor_id = film_actor.actor_id and film.film_id = film_actor.film_id and actor.actor_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            int i = 1;
            statement.setInt(i, actor.getActorId());
            //This statement is easy
            //Select * from actor is saying "Return all Fields for all rows in films". Because there
            //is no "where clause", all rows are returned

            ResultSet results = statement.executeQuery();
            //Initialize an empty List to hold the return set of films.
            List<Film> films;
            films = new ArrayList<>();
            //Loop through all the results and create a new Film object to hold all its information
            while (results.next()) {
                Film film = new Film();
                film.setFilmId(results.getInt("FILM_ID"));
                film.setTitle(results.getString("TITLE"));
                actor.setActorId(results.getInt("actor_id"));
                actor.setFirstName(results.getString("first_name"));
                actor.setLastName(results.getString("last_name"));
                films.add(film);
            }
            //Return all the films.
            return films;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }
}

package edu.canisius.cyb600.lab4.database;

import edu.canisius.cyb600.lab4.dataobjects.Actor;
import edu.canisius.cyb600.lab4.dataobjects.Category;
import edu.canisius.cyb600.lab4.dataobjects.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresDBAdapter extends AbstractDBAdapter {

    public PostgresDBAdapter(Connection conn) {
        super(conn);
    }

    @Override
    public List<String> getAllDistinctCategoryNames() {
        try (PreparedStatement statement = conn.prepareStatement("SELECT DISTINCT name FROM category")) {
            ResultSet results = statement.executeQuery();
            List<String> categories = new ArrayList<>();
            while (results.next()) {
                categories.add(results.getString("name"));
            }
            return categories;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Film> getAllFilmsWithALengthLongerThanX(int length) {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM film WHERE length > ? ORDER BY film_id")) {
            statement.setInt(1, length);
            ResultSet results = statement.executeQuery();
            List<Film> films = new ArrayList<>();
            while (results.next()) {
                Film film = new Film();
                film.setFilmId(results.getInt("film_id"));
                film.setTitle(results.getString("title"));
                film.setDescription(results.getString("description"));
                film.setReleaseYear(String.valueOf(results.getInt("release_year")));
                film.setLanguageId(results.getInt("language_id"));
                film.setOriginalLanguageId(results.getInt("original_language_id"));
                film.setRentalDuration(results.getInt("rental_duration"));
                film.setRentalRate(results.getDouble("rental_rate"));
                film.setLength(results.getInt("length"));
                film.setReplacementCost(results.getDouble("replacement_cost"));
                film.setRating(results.getString("rating"));
                film.setSpecialFeatures(results.getString("special_features"));
                film.setLastUpdate(results.getDate("last_update"));
                films.add(film);
            }
            return films;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Actor> getActorsFirstNameStartingWithX(char firstLetter) {
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM actor WHERE first_name LIKE ?")) {
            statement.setString(1, firstLetter + "%");
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
    public List<Actor> insertAllActorsWithAnOddNumberLastName(List<Actor> actors) {
        List<Actor> insertedActors = new ArrayList<>();
        for (Actor actor : actors) {
            if (actor.getLastName() != null && actor.getLastName().length() % 2 != 0) {
                String sql = "INSERT INTO actor (first_name, last_name, last_update) VALUES (?, ?, CURRENT_TIMESTAMP) " +
                        "RETURNING actor_id, first_name, last_name, last_update";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    int i = 1;
                    statement.setString(i++, actor.getFirstName());
                    statement.setString(i++, actor.getLastName());
                    ResultSet results = statement.executeQuery();
                    if (results.next()) {
                        actor.setActorId(results.getInt("actor_id"));
                        actor.setLastUpdate(results.getDate("last_update"));
                    }
                    insertedActors.add(actor);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }
        return insertedActors;
    }

    @Override
    public List<Film> getFilmsInCategory(Category category) {
        String sql = "SELECT * FROM film f, film_category fc, category c " +
                "WHERE f.film_id = fc.film_id AND fc.category_id = c.category_id AND c.category_id = ? " +
                "ORDER BY f.film_id";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            int i = 1;
            statement.setInt(i, category.getCategoryId());
            ResultSet results = statement.executeQuery();
            List<Film> films = new ArrayList<>();
            while (results.next()) {
                Film film = new Film();
                film.setFilmId(results.getInt("film_id"));
                film.setTitle(results.getString("title"));
                film.setDescription(results.getString("description"));
                film.setReleaseYear(String.valueOf(results.getInt("release_year")));
                film.setLanguageId(results.getInt("language_id"));
                film.setOriginalLanguageId(results.getInt("original_language_id"));
                film.setRentalDuration(results.getInt("rental_duration"));
                film.setRentalRate(results.getDouble("rental_rate"));
                film.setLength(results.getInt("length"));
                film.setReplacementCost(results.getDouble("replacement_cost"));
                film.setRating(results.getString("rating"));
                film.setSpecialFeatures(results.getString("special_features"));
                film.setLastUpdate(results.getDate("last_update"));
                films.add(film);
            }
            return films;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new ArrayList<>();
    }
}
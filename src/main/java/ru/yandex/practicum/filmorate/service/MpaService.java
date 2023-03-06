package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExceptionAndLogs;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class MpaService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpaById(int id) {
        if(!checkMpa(id)) {
            log.warn(ExceptionAndLogs.ID_ERROR.getDescription());
            throw new FilmException(ExceptionAndLogs.ID_ERROR.getDescription());
        }
        String sqlRequest = "SELECT * FROM mpa WHERE id_mpa = ?";
        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeMpa(rs), id).stream().findFirst().get();
    }

    public List<Mpa> getMpa() {
        String sqlRequest = "SELECT * FROM mpa";
        return jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeMpa(rs));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("id_mpa"), rs.getString("name_mpa"));
    }

    private boolean checkMpa(int id) {
        for (Mpa mpa : getMpa()) {
            if(mpa.getId() == id) {
                return true;
            }
        }
        return false;
    }
}

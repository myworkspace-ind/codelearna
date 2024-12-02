package mks.myworkspace.learna.repository;

import java.lang.System.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import mks.myworkspace.learna.entity.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.LoggingCacheErrorHandler;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ParameterJdbcRepository {
	@Autowired
	private DataSource dataSource;
	
	public Parameter save(Parameter parameter) {
	    String sqlInsert = "INSERT INTO learna_parameter (param_key, param_value) VALUES (?, ?)";
	    String sqlUpdate = "UPDATE learna_parameter SET param_key = ?, param_value = ? WHERE id = ?";

	    try (Connection conn = dataSource.getConnection()) {
	        if (parameter.getId() != null) {
	            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
	                ps.setString(1, parameter.getParamKey());
	                ps.setString(2, parameter.getParamValue());
	                ps.setLong(3, parameter.getId());
	                int rowsUpdated = ps.executeUpdate();
	               
	                if (rowsUpdated == 0) {
	                    insertNewParameter(parameter, conn, sqlInsert);
	                }
	            }
	        } else {
	            insertNewParameter(parameter, conn, sqlInsert);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return parameter;
	}

	private void insertNewParameter(Parameter parameter, Connection conn, String sqlInsert) throws SQLException {
	    try (PreparedStatement ps = conn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
	        ps.setString(1, parameter.getParamKey());
	        ps.setString(2, parameter.getParamValue());
	        ps.executeUpdate();

	        try (ResultSet rs = ps.getGeneratedKeys()) {
	            if (rs.next()) {
	                parameter.setId(rs.getLong(1));
	            }
	        }
	    }
	}
	
	public void deleteById(Long id) {
        String sql = "DELETE FROM learna_parameter WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Không tìm thấy Parameter với id: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public List<String> getParamKeyDiff() {
		String sql = "SELECT DISTINCT param_key FROM learna.learna_parameter;";
		List<String> paramKeys = new ArrayList<>();

		paramKeys.add("All");
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				paramKeys.add(rs.getString("param_key"));
			}
		} catch (SQLException e) {
			log.error("Could not excute " + sql, e);
		} finally {
			close(rs);
			close(ps);
			close(conn);
		}

		return paramKeys;
	}

	private void close(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			// Do nothing
		}
	}

	private void close(PreparedStatement ps) {
		try {
			ps.close();
		} catch (SQLException e) {
			// Do nothing
		}
	}

	private void close(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			// Do nothing
		}
	}
	public List<String> 

}

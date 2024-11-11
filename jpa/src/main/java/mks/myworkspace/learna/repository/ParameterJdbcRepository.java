package mks.myworkspace.learna.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import mks.myworkspace.learna.entity.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ParameterJdbcRepository {
	@Autowired
	private DataSource dataSource;

	public Parameter save(Parameter parameter) {
		String sql = "INSERT INTO learna_parameter (param_key, param_value) VALUES (?, ?)";

		try (Connection conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, parameter.getParamKey());
			ps.setString(2, parameter.getParamValue());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					parameter.setId(rs.getLong(1));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parameter;
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

}

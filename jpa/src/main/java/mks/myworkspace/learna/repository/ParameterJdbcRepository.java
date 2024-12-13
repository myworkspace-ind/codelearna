package mks.myworkspace.learna.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import mks.myworkspace.learna.entity.Parameter;

@Repository
@Slf4j
public class ParameterJdbcRepository {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	@Autowired
	private DataSource dataSource;
	
	public Parameter save(Parameter parameter) {
		String sqlInsert = "INSERT INTO learna_parameter (param_key, param_value, status) VALUES (?, ?, ?)";
	    String sqlUpdate = "UPDATE learna_parameter SET param_key = ?, param_value = ?, status = ? WHERE id = ?";


	    try (Connection conn = dataSource.getConnection()) {
	        if (parameter.getId() != null) {
	            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
	                ps.setString(1, parameter.getParamKey());
	                ps.setString(2, parameter.getParamValue());
	                ps.setString(3, parameter.getStatus());
	                ps.setLong(4, parameter.getId());
	               
	                int rowsUpdated = ps.executeUpdate();
	               
	                if (rowsUpdated == 0) {
	                    insertNewParameter(parameter, conn, sqlInsert);
	                }
	            }
	        } else {
	            insertNewParameter(parameter, conn, sqlInsert);
	        }

	    } catch (SQLException sqlEx) {
	        log.error("Could not save parameter: " + parameter, sqlEx);
	    }
	    return parameter;
	}
	
	public Parameter save(Parameter parameter, Long categoryId) {
	    String sqlInsert = "INSERT INTO learna_parameter (param_key, param_value) VALUES (?, ?)";
	    String sqlUpdate = "UPDATE learna_parameter SET param_key = ?, param_value = ? WHERE id = ?";
	    String sqlInsertCategory = "INSERT INTO learna_category (parameter_id) VALUES (?)";
	    String sqlInsertSubcategory = "INSERT INTO learna_subcategory (parameter_id, category_id) VALUES (?, ?)";

        System.out.println("Thêm thành công vào bảng Category với ID: " + categoryId);
	    try (Connection conn = dataSource.getConnection()) {
	        // Nếu đã tồn tại ID Parameter, thực hiện cập nhật
	        if (parameter.getId() != null) {
	            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
	                ps.setString(1, parameter.getParamKey());
	                ps.setString(2, parameter.getParamValue());
	                ps.setLong(3, parameter.getId());
	                int rowsUpdated = ps.executeUpdate();

	                // Nếu không cập nhật được, thêm mới Parameter
	                if (rowsUpdated == 0) {
	                    insertNewParameter(parameter, conn, sqlInsert);
	                }
	            }
	        } else {
	            // Thêm mới Parameter
	            insertNewParameter(parameter, conn, sqlInsert);
	        }

	     // Nếu thêm mới thành công, tiếp tục Insert vào bảng Category và Subcategory
	        if (parameter.getId() != null && categoryId != 0) {
	            System.out.println("Bắt đầu thêm vào bảng Category với parameter ID: " + parameter.getId());
	            // Insert vào bảng Category
	            try (PreparedStatement psCategory = conn.prepareStatement(sqlInsertCategory)) {
	                psCategory.setLong(1, parameter.getId());
	                int categoryResult = psCategory.executeUpdate();
	                if (categoryResult > 0) {
	                    System.out.println("Thêm thành công vào bảng Category với ID: " + parameter.getId());
	                } else {
	                    System.out.println("Không thể thêm vào bảng Category với ID: " + parameter.getId());
	                }
	            } catch (SQLException e) {
	                System.err.println("Lỗi khi thêm vào bảng Category: " + e.getMessage());
	            }
	        }

	        // Nếu có categoryId, insert vào bảng Subcategory
	        if (categoryId != null) {
	            System.out.println("Bắt đầu thêm vào bảng Subcategory với parameter ID: " + parameter.getId() + " và category ID: " + categoryId);
	            try (PreparedStatement psSubcategory = conn.prepareStatement(sqlInsertSubcategory)) {
	                psSubcategory.setLong(1, parameter.getId());
	                psSubcategory.setLong(2, categoryId);
	                int subcategoryResult = psSubcategory.executeUpdate();
	                if (subcategoryResult > 0) {
	                    System.out.println("Thêm thành công vào bảng Subcategory với parameter ID: " + parameter.getId() + " và category ID: " + categoryId);
	                } else {
	                    System.out.println("Không thể thêm vào bảng Subcategory với parameter ID: " + parameter.getId() + " và category ID: " + categoryId);
	                }
	            } catch (SQLException e) {
	                System.err.println("Lỗi khi thêm vào bảng Subcategory: " + e.getMessage());
	            }
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
	        ps.setString(3, "ACTIVE");
	        ps.executeUpdate();

	        try (ResultSet rs = ps.getGeneratedKeys()) {
	            if (rs.next()) {
	                parameter.setId(rs.getLong(1));
	            }
	        }
	    }
	}
	
	public Parameter addNewValueToKey(Parameter parameter) {
	    String sqlInsert = "INSERT INTO learna_parameter (param_key, param_value) VALUES (?, ?)";

	    try (Connection conn = dataSource.getConnection()) {
	        if (parameter.getId() != null) {
	        	System.out.println("Đã tồn tại: ");
	        } else {
	            insertNewParameter(parameter, conn, sqlInsert);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return parameter;
	}

	
	public void deleteById(Long id) {
        String sql = "UPDATE learna_parameter SET status = 'DELETED' WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        
        if (rowsAffected == 0) {
            throw new RuntimeException("Lesson not found with id: " + id);
        }
    }
	
	public List<String> getParamKeyDiff() {
		String sql = "SELECT DISTINCT param_key FROM learna_parameter;";
		List<String> paramKeys = new ArrayList<>();

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
		} catch (SQLException sqlEx) {
			log.error("Could not excute " + sql, sqlEx);
		} finally {
			close(rs);
			close(ps);
			close(conn);
		}

		return paramKeys;
	}

	public List<Parameter> getListParamsByParamValueAndStatus(String paramKey, String status, String orderBy) {
	    String sql = "SELECT * FROM learna_parameter WHERE param_key = ? AND status = ? ORDER BY seqno " + orderBy;
	    List<Parameter> parameters = new ArrayList<>();
	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, paramKey);
	        ps.setString(2, status);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            Parameter parameter = new Parameter();
	            parameter.setParamValue(rs.getString("param_value"));
	            parameter.setSeqno(rs.getInt("seqno"));
	            parameters.add(parameter);
	        }
	    } catch (SQLException sqlEx) {
	    	log.error("Could not get parameter by sql " + sql, sqlEx);
	    }

	    return parameters;
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

}

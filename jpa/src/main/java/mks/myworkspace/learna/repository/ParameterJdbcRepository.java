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

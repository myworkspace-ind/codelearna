package mks.myworkspace.learna.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mks.myworkspace.learna.entity.Wallet;

@Repository
public class WalletJdbcRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Wallet wallet) {
        if (wallet.getId() == null) {
            String sql = "INSERT INTO wallet (user_eid, balance, created_dte, modified_dte) " +
                         "VALUES (?, ?, NOW(), NOW())";
            jdbcTemplate.update(sql, wallet.getUserEid(), 
                                wallet.getBalance());
        } else {
            String sql = "UPDATE wallet SET user_eid = ?, balance = ?, modified_dte = NOW() WHERE id = ?";
            jdbcTemplate.update(sql, wallet.getUserEid(), 
                                wallet.getBalance(), 
                                wallet.getId());
        }
    }
}	
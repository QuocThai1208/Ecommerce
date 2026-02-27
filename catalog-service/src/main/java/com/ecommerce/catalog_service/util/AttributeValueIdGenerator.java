package com.ecommerce.catalog_service.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AttributeValueIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String prefix = "ATT-VALUE-";

        try (Connection connection = session.getJdbcConnectionAccess().obtainConnection()) {
            // Sử dụng try-with-resources cho Statement và ResultSet để tự động đóng
            try (Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery("SELECT nextval('attributer_value_seq')")) {

                if (rs.next()) {
                    long id = rs.getLong(1);
                    return prefix + String.format("%09d", id);
                }
            }
        } catch (SQLException e) {
            throw new HibernateException("Không thể tạo ID cho Attribute Value: " + e.getMessage());
        }
        return null;
    }
}
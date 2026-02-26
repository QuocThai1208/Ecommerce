package com.ecommerce.inventory_service.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import java.io.Serializable;
import java.sql.*;

public class WarehouseIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String prefix = "WH-";

        try (Connection connection = session.getJdbcConnectionAccess().obtainConnection()) {
            // Sử dụng try-with-resources cho Statement và ResultSet để tự động đóng
            try (Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery("SELECT nextval('warehouse_seq')")) {

                if (rs.next()) {
                    long id = rs.getLong(1);
                    // Trả về định dạng WH-000001, ...
                    return prefix + String.format("%06d", id);
                }
            }
        } catch (SQLException e) {
            throw new HibernateException("Không thể tạo ID cho Warehouse: " + e.getMessage());
        }
        return null;
    }
}
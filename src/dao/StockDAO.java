package dao;

import database.DBConnection;
import model.Stock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDAO {

    /**
     * Insert new stock entry
     */
    public boolean insertStock(Stock stock) {
        String sql = "INSERT INTO stock (product_id, quantity, restock_level) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, stock.getProductId());
            ps.setInt(2, stock.getQuantity());
            ps.setInt(3, stock.getRestockLevel());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update existing stock
     */
    public boolean updateStock(Stock stock) {
        String sql = "UPDATE stock SET product_id=?, quantity=?, restock_level=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, stock.getProductId());
            ps.setInt(2, stock.getQuantity());
            ps.setInt(3, stock.getRestockLevel());
            ps.setInt(4, stock.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete stock entry by ID
     */
    public boolean deleteStock(int id) {
        String sql = "DELETE FROM stock WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get stock by ID
     */
    public Stock getStockById(int id) {
        String sql = "SELECT * FROM stock WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Stock(
                    rs.getInt("id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getInt("restock_level")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get stock by product ID
     */
    public Stock getStockByProductId(int productId) {
        String sql = "SELECT * FROM stock WHERE product_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Stock(
                    rs.getInt("id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getInt("restock_level")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all stock entries
     */
    public List<Stock> getAllStock() {
        List<Stock> list = new ArrayList<>();
        String sql = "SELECT * FROM stock";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Stock(
                    rs.getInt("id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getInt("restock_level")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get all stock items that need restocking (quantity <= restock_level)
     */
    public List<Stock> getLowStock() {
        List<Stock> list = new ArrayList<>();
        String sql = "SELECT * FROM stock WHERE quantity <= restock_level";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Stock(
                    rs.getInt("id"),
                    rs.getInt("product_id"),
                    rs.getInt("quantity"),
                    rs.getInt("restock_level")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Update stock quantity only
     */
    public boolean updateQuantity(int productId, int newQuantity) {
        String sql = "UPDATE stock SET quantity=? WHERE product_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


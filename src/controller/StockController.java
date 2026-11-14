package controller;

import dao.StockDAO;
import model.Stock;
import java.util.List;

public class StockController {

    private StockDAO stockDAO;

    public StockController() {
        stockDAO = new StockDAO();
    }

    public boolean addStock(Stock stock) {
        return stockDAO.insertStock(stock);
    }

    public boolean updateStock(Stock stock) {
        return stockDAO.updateStock(stock);
    }

    public boolean deleteStock(int stockId) {
        return stockDAO.deleteStock(stockId);
    }

    public List<Stock> getAllStock() {
        return stockDAO.getAllStock();
    }

    public Stock getStockById(int stockId) {
        return stockDAO.getStockById(stockId);
    }

    public Stock getStockByProductId(int productId) {
        return stockDAO.getStockByProductId(productId);
    }

    /**
     * Get all items that need restocking
     */
    public List<Stock> getLowStock() {
        return stockDAO.getLowStock();
    }

    /**
     * Update stock quantity for a product
     */
    public boolean updateQuantity(int productId, int newQuantity) {
        return stockDAO.updateQuantity(productId, newQuantity);
    }
}


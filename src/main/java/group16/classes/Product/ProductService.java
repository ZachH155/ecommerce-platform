package group16.classes.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void addProduct(String name, double price, int quantity, int sellerId) throws SQLException {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setSellerId(sellerId);
        productDAO.addProduct(product);
    }

    public List<Product> getProductsBySeller(int sellerId) throws SQLException {
        return productDAO.getProductsBySeller(sellerId);
    }

    public void updateProduct(int id, String name, double price, int quantity) throws SQLException {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        productDAO.updateProduct(product);
    }

    public void deleteProduct(int productId) throws SQLException {
        productDAO.deleteProduct(productId);
    }

    public List<Product> getAllProducts() throws SQLException {
        return productDAO.getAllProducts();
    }
}
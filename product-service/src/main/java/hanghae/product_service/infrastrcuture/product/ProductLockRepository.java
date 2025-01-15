package hanghae.product_service.infrastrcuture.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;
import javax.sql.DataSource;

public class ProductLockRepository<T> {

    private final DataSource dataSource;

    public ProductLockRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // 여러 키에 대해 락을 획득
    public T executeWithLocks(List<String> keys, Callable<T> callable) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                getLocks(connection, keys);
                return callable.call();
            } finally {
                releaseLocks(connection, keys);
            }
        } catch (Exception e) {
            throw new RuntimeException("락을 처리하는 중에 오류가 발생하였습니다.", e);
        }
    }

    private void getLocks(final Connection connection, final List<String> keys) {
        String sql = "SELECT GET_LOCK(?, ?)";

        for (String key : keys) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, key);
                stmt.setInt(2, 30); // 30초 대기
                stmt.executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException("락을 획득하는 중에 오류가 발생하였습니다. Key: " + key, e);
            }
        }
    }

    private void releaseLocks(final Connection connection, final List<String> keys) {
        String sql = "SELECT RELEASE_LOCK(?)";

        for (String key : keys) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, key);
                stmt.executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException("락을 해제하는 중에 오류가 발생하였습니다. Key: " + key, e);
            }
        }
    }
}

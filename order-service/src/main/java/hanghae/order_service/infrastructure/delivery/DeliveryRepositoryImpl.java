package hanghae.order_service.infrastructure.delivery;

import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.domain.order.DeliveryStatus;
import hanghae.order_service.service.port.DeliveryRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryRepositoryImpl implements DeliveryRepository {
    private final DeliveryJpaRepository jpaRepository;

    public DeliveryRepositoryImpl(DeliveryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Delivery save(Delivery delivery) {
        return jpaRepository.save(DeliveryEntity.fromModel(delivery)).toModel();
    }

    @Override
    public List<Delivery> findDeliveryStatusByDate(DeliveryStatus deliveryStatus, LocalDateTime dateWithMinusDay) {
        return jpaRepository.findAllByStatusAndDate(deliveryStatus, dateWithMinusDay).stream()
                .map(DeliveryEntity::toModel).toList();
    }

    @Override
    public void saveAll(List<Delivery> results) {
        List<DeliveryEntity> entities = results.stream().map(DeliveryEntity::fromModel).toList();
        jpaRepository.saveAll(entities);
    }
}

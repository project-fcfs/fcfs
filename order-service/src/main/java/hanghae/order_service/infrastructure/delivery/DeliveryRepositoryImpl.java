package hanghae.order_service.infrastructure.delivery;

import hanghae.order_service.domain.order.Delivery;
import hanghae.order_service.service.port.DeliveryRepository;
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
}

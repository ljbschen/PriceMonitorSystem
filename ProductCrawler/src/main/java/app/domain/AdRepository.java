package app.domain;

import org.springframework.data.repository.CrudRepository;

public interface AdRepository extends CrudRepository<Ad, String> {
    Ad getAdByAdId(String adId);
}

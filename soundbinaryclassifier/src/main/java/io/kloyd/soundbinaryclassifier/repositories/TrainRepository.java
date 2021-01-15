package io.kloyd.soundbinaryclassifier.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.kloyd.soundbinaryclassifier.models.Train;

@Repository
public interface TrainRepository extends CrudRepository<Train, String> {
	@Query("from Train item where item.orgid=?1")
	List<Train> findByOrgID(String orgid);
}

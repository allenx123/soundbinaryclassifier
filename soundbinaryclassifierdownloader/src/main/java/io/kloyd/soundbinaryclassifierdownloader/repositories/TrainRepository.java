package io.kloyd.soundbinaryclassifierdownloader.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.kloyd.soundbinaryclassifierdownloader.models.Train;

@Repository
public interface TrainRepository extends CrudRepository<Train, String> {
	@Query("from Train item where item.download=false")
	List<Train> findByDownload();	
}

package io.kloyd.soundbinaryclassifierdownloader.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.kloyd.soundbinaryclassifierdownloader.models.Organization;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, String> {
	
	

}

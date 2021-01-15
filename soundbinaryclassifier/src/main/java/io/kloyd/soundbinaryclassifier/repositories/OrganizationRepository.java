package io.kloyd.soundbinaryclassifier.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.kloyd.soundbinaryclassifier.models.Organization;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, String> {
	
	@Query("from Organization org where org.email=?1")
	Organization findByEmail(String email);

}

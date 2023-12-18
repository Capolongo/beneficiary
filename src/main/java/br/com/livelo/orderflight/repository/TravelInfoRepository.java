package br.com.livelo.orderflight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.livelo.orderflight.entities.TravelInfoEntity;

public interface TravelInfoRepository  extends JpaRepository<TravelInfoEntity, Integer>{
}

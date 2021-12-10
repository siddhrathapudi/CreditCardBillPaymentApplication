package com.creditcardpayment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.creditcardpayment.entity.TranscationEntity;

@Repository
public interface ITranscationRepository extends JpaRepository<TranscationEntity, Long>{

}

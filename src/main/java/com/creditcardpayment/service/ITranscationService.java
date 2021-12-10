package com.creditcardpayment.service;

import java.util.List;

import com.creditcardpayment.bean.Transaction;
import com.creditcardpayment.exception.CreditCardException;
import com.creditcardpayment.exception.TranscationException;

public interface ITranscationService {
	boolean existsById(Long transactionId) throws TranscationException;

	Transaction add(Transaction transaction) throws TranscationException;
	Transaction save(Transaction transaction) throws TranscationException;

	void deleteById(Long transactionId) throws TranscationException;

	Transaction findById(Long transactionId) throws TranscationException;

	List<Transaction> findAll();

	Transaction transaction(String cardNumber,Double amount,String discription) throws CreditCardException;
}
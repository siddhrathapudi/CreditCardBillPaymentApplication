package com.creditcardpayment.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creditcardpayment.bean.Transaction;
import com.creditcardpayment.bean.TranscationStatus;
import com.creditcardpayment.entity.CreditCardEntity;
import com.creditcardpayment.exception.CreditCardException;
import com.creditcardpayment.exception.TranscationException;
import com.creditcardpayment.repository.ICreditCardRepository;
import com.creditcardpayment.repository.ITranscationRepository;

@Service
public class TransactionServiceImpl implements ITranscationService {

	@Autowired
	private ITranscationRepository transactionRepo;

	@Autowired
	private ICreditCardRepository creditCardRepo;

	@Autowired
	private EMParse parser;

	public TransactionServiceImpl() {

	}

	public TransactionServiceImpl(ITranscationRepository transactionRepo) {
		super();
		this.transactionRepo = transactionRepo;
		this.parser = new EMParse();
	}



	public ITranscationRepository getTransactionRepo() {
		return transactionRepo;
	}


	public void setTransactionRepo(ITranscationRepository transactionRepo) {
		this.transactionRepo = transactionRepo;
	}


	public EMParse getParser() {
		return parser;
	}


	public void setParser(EMParse parser) {
		this.parser = parser;
	}


	@Override
	public Transaction add(Transaction transaction) throws TranscationException {
		if(transaction !=null) {
			if(transactionRepo.existsById(transaction.getTransactionId())) {
				throw new TranscationException("Transaction "+transaction.getTransactionId()+" is already Exists");
			}else {

				transaction=parser.parse(transactionRepo.save(parser.parse(transaction)));
			}
		}
		return transaction;
	}

	@Override
	public Transaction save(Transaction transaction) throws TranscationException {
		if(transaction==null) {
			throw new TranscationException("transaction details cannot be null");
		}
		return parser.parse(transactionRepo.save(parser.parse(transaction)));
	}

	@Override
	public List<Transaction> findAll() {
		return transactionRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}

	@Override
	public void deleteById(Long transactionId) throws TranscationException {
		if(transactionId==null) {
			throw new TranscationException("transaction Id cannot be Null");
		}else if(!transactionRepo.existsById(transactionId)) {
			throw new TranscationException("Transaction with Transaction Id "+transactionId+" Does not Exists");
		}
		transactionRepo.deleteById(transactionId);
	}

	@Override
	public Transaction findById(Long transactionId) throws TranscationException {
		if(transactionId==null) {
			throw new TranscationException("transaction Id cannot be Null");
		}else if(!transactionRepo.existsById(transactionId)) {
			throw new TranscationException("Transaction with Transaction Id "+transactionId+" Does not Exists");
		}
		return parser.parse(transactionRepo.findById(transactionId).orElse(null));
	}

	@Override
	public boolean existsById(Long transactionId) throws TranscationException {
		if(transactionId==null) {
			throw new TranscationException("transaction Id can't be Null");
		}
		return transactionRepo.existsById(transactionId);
	}


	@Override
	public Transaction transaction(String cardNumber,Double amount,String discription) throws CreditCardException {
		if(cardNumber==null) {
			throw new CreditCardException("Card Number cannot be Null");
		}
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		if(card==null) {
			throw new CreditCardException("Card Details should not be Null");
		}
		if(card.getExpiryDate().isBefore(LocalDate.now())) {
			throw new CreditCardException("card "+card.getCardNumber()+" is invalid");
		}
		Transaction transact=new Transaction();
		transact.setTransactionId(0L);
		transact.setCardNumber(cardNumber);
		transact.setTransactionDate(LocalDate.now());
		transact.setTransactionTime(LocalTime.now());
		transact.setDescription(discription);
		if(amount+card.getUsedLimit()<card.getCreditLimit()) {
			transact.setAmount(amount);
			card.setUsedLimit(amount+card.getUsedLimit());
			transact.setStatus(TranscationStatus.SUCCESSFUL);
		}else {
			transact.setAmount(0.0);
			card.setUsedLimit(card.getUsedLimit());
			transact.setStatus(TranscationStatus.FAILED);
		}
		transact=parser.parse(transactionRepo.save(parser.parse(transact)));
		return transact;
	}
}


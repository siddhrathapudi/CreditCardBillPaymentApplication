package com.creditcardpayment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.creditcardpayment.bean.Transaction;
import com.creditcardpayment.exception.CreditCardException;
import com.creditcardpayment.exception.TranscationException;
import com.creditcardpayment.service.ITranscationService;

@RestController
@RequestMapping("/transactions")
public class TranscationRestController {

	@Autowired
	private ITranscationService transactionService;

	@GetMapping("/getAllTransactions")
	public ResponseEntity<List<Transaction>> findAll() {
		return ResponseEntity.ok(transactionService.findAll());
	}

	@GetMapping("/getTransaction/{transactionId}")
	public ResponseEntity<Transaction> findById(@PathVariable("transactionId") Long transactionId) throws TranscationException{
		ResponseEntity<Transaction> response=null;
		if(!(transactionService.existsById(transactionId)) || transactionId==null) {
			response=new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			response=new ResponseEntity<>(transactionService.findById(transactionId),HttpStatus.FOUND);
		}
		return response;
	}

	@PostMapping("/addTransaction")
	public ResponseEntity<Transaction> add(@RequestBody Transaction transaction) throws TranscationException {
		transaction=transactionService.add(transaction);
		return ResponseEntity.ok(transaction);
	}

	@PutMapping("/updateTransaction")
	public ResponseEntity<Transaction> updateUser(@RequestBody Transaction transaction) throws TranscationException{
		transaction =transactionService.save(transaction);
		return ResponseEntity.ok(transaction);
	}

	@GetMapping("/transact/{cardNumber}/{amount}/{description}")
	public ResponseEntity<Transaction> transact(@PathVariable("cardNumber") String cardNumber,@PathVariable("amount") Double amount,@PathVariable("description") String description) throws CreditCardException {
		return ResponseEntity.ok(transactionService.transaction(cardNumber, amount, description));
	}

}

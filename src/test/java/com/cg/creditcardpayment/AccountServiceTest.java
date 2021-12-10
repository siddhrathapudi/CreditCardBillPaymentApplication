package com.cg.creditcardpayment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.creditcardpayment.bean.Account;
import com.creditcardpayment.bean.AccountType;
import com.creditcardpayment.entity.AccountEntity;
import com.creditcardpayment.exception.AccountException;
import com.creditcardpayment.repository.IAccountRepository;
import com.creditcardpayment.service.AccountServiceImpl;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	@Mock
	private IAccountRepository accountRepo;
	
	@InjectMocks
	private AccountServiceImpl service;

	@Test
	@DisplayName("AccountDetails should retrive")
	void testGetAll() {
		List<AccountEntity> testData=Arrays.asList(new AccountEntity[] {
				new AccountEntity("42356879562","Venkata sai",50000.0,AccountType.SAVINGS),
				new AccountEntity("42356879563","Venkata",40000.0,AccountType.SAVINGS),
				new AccountEntity("42356879564","Sai",90000.0,AccountType.CURRENT)
		});
		
		Mockito.when(accountRepo.findAll()).thenReturn(testData);
		
		List<Account> expected=Arrays.asList(new Account[] {
				new Account("42356879562","Venkata sai",50000.0,AccountType.SAVINGS),
				new Account("42356879563","Venkata",40000.0,AccountType.SAVINGS),
				new Account("42356879564","Sai",90000.0,AccountType.CURRENT)
		});
		
		List<Account> actual = service.findAll();
		
		assertEquals(expected,actual);

	}
	
	@Test
	@DisplayName("AccountDetails add")
	void testAdd() throws AccountException {
		AccountEntity account1=new AccountEntity("42356879562","Venkata sai",50000.0,AccountType.SAVINGS);
		
		Mockito.when(accountRepo.save(account1)).thenReturn(account1);

		Account expected=new Account("42356879562","Venkata sai",50000.0,AccountType.SAVINGS);
		
		Account actual = service.add(service.getParser().parse(account1));
		
		assertEquals(expected,actual);

	}
	
	@Test
	@DisplayName("AccountDetails should delete")
	void testDelete() throws AccountException {
		AccountEntity account1=new AccountEntity("42356879562","Venkata sai",50000.0,AccountType.SAVINGS);
		
		Mockito.when(accountRepo.save(account1)).thenReturn(account1);
		

		Account added = service.add(service.getParser().parse(account1));
		
		Mockito.doNothing().when(accountRepo).deleteById(added.getAccountNumber());
		Mockito.when(accountRepo.existsById(account1.getAccountNumber())).thenReturn(true);
		service.deleteById(added.getAccountNumber());
		boolean test=service.existsById(added.getAccountNumber());
		
		assertTrue(test);
		
	}
	
	@Test
	@DisplayName("get by Id ")
	void testGetById () throws AccountException {
		AccountEntity testdata=new AccountEntity("42356879564","Sai",90000.0,AccountType.SAVINGS);
		
		Account expected=new Account("42356879564","Sai",90000.0,AccountType.SAVINGS);
		
		
		Mockito.when(accountRepo.findById(testdata.getAccountNumber())).thenReturn(Optional.of(testdata));
	
		Account actual=service.findById(testdata.getAccountNumber());
		
		assertEquals(expected,actual);
	}
	
	@Test
	@DisplayName("get by id return null")
	void testGetByIdNull() throws AccountException {		
		
		Mockito.when(accountRepo.findById("425631257892")).thenReturn(Optional.empty());
		
		Account actual = service.findById("425631257892");
		assertNull(actual);
	}
	
}

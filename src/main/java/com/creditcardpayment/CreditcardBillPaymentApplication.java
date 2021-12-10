package com.creditcardpayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@RequestMapping("/api")
public class CreditcardBillPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditcardBillPaymentApplication.class, args);
		System.out.println("Connected DataBase");
	}

}

package com.harley.bank;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	//	@Bean
//	CommandLineRunner initDataBase(TransferRepository transferRepository) {
//		return args -> {
//			Transfer transfer = getTransfer();
//			transferRepository.deleteAll();
//			transferRepository.save(transfer);
//			transfer.setId(null);
//			transfer.setSchedulingDate(LocalDate.now());
//			transferRepository.save(transfer);
//		};
//	}
//
//	private Transfer getTransfer() {
//		LocalDate schedulingDate = LocalDate.now().plusDays(1);
//		return Transfer.builder()
//				.originAccount(123456)
//				.destinationAccount(654321)
//				.transferValue(100.0)
//				.schedulingDate(schedulingDate)
//				.transferDate(LocalDate.now()).build();
//	}

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}

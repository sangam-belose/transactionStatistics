package com.n26.statistics.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.n26.statistics.model.Transaction;
import com.n26.statistics.model.TransactionStatics;
import com.n26.statistics.service.TransactionService;

/**
 * Controller to represent the endpoints.
 * 
 * @author SBelose
 *
 */
@RestController("/")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@PostMapping(value = "/transactions", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createTransaction(@Valid @RequestBody Transaction transaction) throws URISyntaxException {

		long limitSecondsTime = Instant.now().atZone(ZoneOffset.UTC).minus(Duration.ofSeconds(60)).toEpochSecond();
		if (transaction.getTimestamp() > Instant.now().atZone(ZoneOffset.UTC).toEpochSecond()) {
			return ResponseEntity.noContent().build();
		}
		if (transaction.getTimestamp() < limitSecondsTime) {
			return ResponseEntity.noContent().build();
		} else {
			transactionService.save(transaction);
			URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
			return ResponseEntity.created(location).build();
		}

	}

	@GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TransactionStatics> getStatistics() throws URISyntaxException {
		return new ResponseEntity<TransactionStatics>(transactionService.getTransactionsStatistics(), HttpStatus.OK);

	}

}

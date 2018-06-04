package com.n26.statistics.service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.n26.statistics.model.Transaction;
import com.n26.statistics.model.TransactionStatics;

@Service
public class TransactionService {

	private Map<Long, Double> transactionStorage = new ConcurrentHashMap<>();

	public void save(Transaction transaction) {
		transactionStorage.put(transaction.getTimestamp(), transaction.getAmount());
	}

	public TransactionStatics getTransactionsStatistics() {
		DoubleSummaryStatistics stats = transactionStorage.entrySet().parallelStream()
				 .filter(TransactionService::checkTransactionWithinLimit)
				.collect(Collectors.summarizingDouble(e -> e.getValue()));

		return new TransactionStatics(stats.getSum(), stats.getAverage(), stats.getMax(), stats.getMin(),
				stats.getCount());
	}

	public static Boolean checkTransactionWithinLimit(Entry<Long, Double> timeStamp) {
		long limitSecondsTime = Instant.now().atZone(ZoneOffset.UTC).minus(Duration.ofSeconds(60)).toEpochSecond();
		return timeStamp.getKey() < limitSecondsTime;
	}
}

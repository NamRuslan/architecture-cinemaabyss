package cinema.abyss.events.controller;

import cinema.abyss.events.kafka.EventsConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventsController {
	Logger log = LoggerFactory.getLogger(EventsConsumer.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@GetMapping("/health")
	public Mono<ResponseEntity<String>> healthCheck() {
		return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"status\": true}"));
	}

	@PostMapping("/movie")
	public Mono<ResponseEntity<EventResponse>> createMovieEvent(@RequestBody String payload) {
		return publishEvent("movie-events", "movie", payload);
	}

	private Mono<ResponseEntity<EventResponse>> publishEvent(String topic, String eventType, String payload) {
		return Mono.fromFuture(kafkaTemplate.send(topic, payload))
			.map(sendResult ->
				new EventResponse(
					"success",
					sendResult.getRecordMetadata().partition(),
					sendResult.getRecordMetadata().offset(),
					new EventModel(
						eventType + "-created-" + UUID.randomUUID(),
						eventType,
						OffsetDateTime.now(),
						payload
					)
				)
			)
			.map(eventResponse -> ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON).body(eventResponse));
	}

	@PostMapping("/user")
	public Mono<ResponseEntity<EventResponse>> createUserEvent(@RequestBody String payload) {
		return publishEvent("user-events", "user", payload);
	}
	@PostMapping("/payment")
	public Mono<ResponseEntity<EventResponse>> createPaymentEvent(@RequestBody String payload) {
		return publishEvent("payment-events", "payment", payload);
	}

}

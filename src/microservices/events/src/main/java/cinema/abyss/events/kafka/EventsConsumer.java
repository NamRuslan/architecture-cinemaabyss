package cinema.abyss.events.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventsConsumer {
	Logger log = LoggerFactory.getLogger(EventsConsumer.class);

	@KafkaListener(topics = "movie-events", groupId = "events_group")
	public void movieEventListener(String message) {
		log.debug("got movie event: {}", message);
	}

	@KafkaListener(topics = "user-events", groupId = "events_group")
	public void userEventListener(String message) {
		log.debug("got user event: {}", message);
	}

	@KafkaListener(topics = "payment-events", groupId = "events_group")
	public void paymentEventListener(String message) {
		log.debug("got payment event: {}", message);
	}
}

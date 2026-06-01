package cinema.abyss.events.controller;

import java.time.OffsetDateTime;

public record EventModel(String id, String type, OffsetDateTime timestamp, String payload) {
}

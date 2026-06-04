package cinema.abyss.events.controller;

public record EventResponse(String status, int partition, long offset, EventModel event) {
}

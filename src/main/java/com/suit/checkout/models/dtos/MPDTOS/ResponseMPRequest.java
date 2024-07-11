package com.suit.checkout.models.dtos.MPDTOS;

public record ResponseMPRequest(
        String status,
        PointOfInteractionMP point_of_interaction
) {
}

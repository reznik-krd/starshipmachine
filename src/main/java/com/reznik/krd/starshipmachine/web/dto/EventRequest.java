package com.reznik.krd.starshipmachine.web.dto;

import com.reznik.krd.starshipmachine.entity.MeteorIntensity;

public record EventRequest(MeteorIntensity meteorIntensity, String evacuationMessage) {
}

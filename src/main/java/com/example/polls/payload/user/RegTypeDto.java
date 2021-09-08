package com.example.polls.payload.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegTypeDto {
    Boolean isVk;
    Boolean isDefault;
}

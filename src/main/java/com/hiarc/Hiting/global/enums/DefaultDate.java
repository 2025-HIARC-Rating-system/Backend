package com.hiarc.Hiting.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.of;

@Getter
@AllArgsConstructor
public enum DefaultDate {
    DEFAULT_START(LocalDateTime.of(1970, 1, 1, 0, 0, 0)),
    DEFAULT_END(LocalDateTime.of(1970, 1, 2, 0, 0, 0));

    private final LocalDateTime dateTime;

}

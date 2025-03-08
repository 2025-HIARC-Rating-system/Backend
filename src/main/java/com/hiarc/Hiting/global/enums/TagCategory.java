package com.hiarc.Hiting.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TagCategory {

    MATH("math", "수학"), IMPLEMENTATION("implementation", "구현");

    private final String engTagName;
    private final String korTagName;

    public static boolean isValidEngTag(String detailCategory) {
        return Arrays.stream(TagCategory.values())
                .anyMatch(tag -> tag.getEngTagName().equals(detailCategory));
    }

}

package com.hiarc.Hiting.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TierCategory {
    UNRATED(0, "Unrated", 1),
    BRONZE_5(1, "Bronze 5", 1),
    BRONZE_4(2, "Bronze 4", 2),
    BRONZE_3(3, "Bronze 3", 3),
    BRONZE_2(4, "Bronze 2", 4),
    BRONZE_1(5, "Bronze 1", 5),
    SILVER_5(6, "Silver 5", 6),
    SILVER_4(7, "Silver 4", 7),
    SILVER_3(8, "Silver 3", 8),
    SILVER_2(9, "Silver 2", 9),
    SILVER_1(10, "Silver 1", 10),
    GOLD_5(11, "Gold 5", 12),
    GOLD_4(12, "Gold 4", 14),
    GOLD_3(13, "Gold 3", 16),
    GOLD_2(14, "Gold 2", 18),
    GOLD_1(15, "Gold 1", 20),
    PLATINUM_5(16, "Platinum 5", 23),
    PLATINUM_4(17, "Platinum 4", 26),
    PLATINUM_3(18, "Platinum 3", 29),
    PLATINUM_2(19, "Platinum 2", 32),
    PLATINUM_1(20, "Platinum 1", 35),
    DIAMOND_5(21, "Diamond 5", 39),
    DIAMOND_4(22, "Diamond 4", 43),
    DIAMOND_3(23, "Diamond 3", 47),
    DIAMOND_2(24, "Diamond 2", 51),
    DIAMOND_1(25, "Diamond 1", 55),
    RUBY_5(26, "Ruby 5", 60),
    RUBY_4(27, "Ruby 4", 65),
    RUBY_3(28, "Ruby 3", 70),
    RUBY_2(29, "Ruby 2", 75),
    RUBY_1(30, "Ruby 1", 80);

    private final Integer level;
    private final String tier;
    private final Integer tierRating;

    //getLevel()이런거 getter있어서 사용 가능

    // 숫자로 Enum 찾기 (level → Enum 변환)
    public static TierCategory fromLeveltoEnum(Integer level) {
        for (TierCategory tier : values()) {
            if (tier.level.equals(level)) {
                return tier;
            }
        }
        throw new IllegalArgumentException("Unknown tier level: " + level); // 수정하기
    }

    public static int fromLeveltoTierRating(Integer level) {
        for (TierCategory tier : values()) {
            if (tier.level.equals(level)) {
                return tier.tierRating;
            }
        }
        throw new IllegalArgumentException("Unknown tier level: " + level); // 수정하기
    }

}

package com.example.polls.model.project;


import lombok.Getter;

import java.io.Console;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RiskTypeEnum {
    Analysis("Analysis"),
    Requirements("Requirements"),
    Software("Software"),
    Support("Support"),
    ProjectTeam("ProjectTeam"),
    Technical("Technical"),
    Customer("Customer"),
    Internal("Internal"),
    Functional("Functional"),
    Managerial("Managerial");

    @Getter
    private String value;

    RiskTypeEnum(String value) {
        this.value = value;
    }

    public static RiskTypeEnum of(String type){
        return Stream.of(RiskTypeEnum.values())
                .filter(riskTypeEnum -> riskTypeEnum.getValue().equals(type))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

package br.thiago.autenticacao.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {

    ACTION("Action"),
    SCIFI("Science Fiction"),
    ADVENTURE("Adventure"),
    HORROR("Horror"),
    SUSPENSE("Suspense"),
    ROMANCE("Romance"),
    MYSTERY("Mystery"),
    FANTASY("Fantasy"),
    HISTORICAL_FICTION("Historical Fiction"),
    BIOGRAPHY("Biography"),
    SELF_HELP("Self-Help"),
    COOKING("Cooking"),
    TRAVEL("Travel"),
    CHILDRENS("Children's"),
    YOUNG_ADULT("Young Adult"),
    SCIENCE("Science"),
    PHILOSOPHY("Philosophy"),
    PSYCHOLOGY("Psychology"),
    ART("Art"),
    MUSIC("Music"),
    SPORTS("Sports"),
    HISTORY("History"),
    RELIGION("Religion"),
    POLITICS("Politics");

    private final String name;

}

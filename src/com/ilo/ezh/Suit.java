package com.ilo.ezh;

public enum Suit {

    CLUB("c"),
    SPADE("s"),
    HEARTS("h"),
    DIAMONDS("d");

    private final String textRepresentation;

    Suit(String textRepresentation) {
        this.textRepresentation = textRepresentation;
    }

    @Override public String toString() {
        return textRepresentation;
    }

}

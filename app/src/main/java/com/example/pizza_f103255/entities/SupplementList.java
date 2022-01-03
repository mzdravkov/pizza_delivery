package com.example.pizza_f103255.entities;

import java.io.Serializable;
import java.util.List;

public class SupplementList implements Serializable {
    public final List<Supplement> supplements;

    public SupplementList(List<Supplement> supplements) {
        this.supplements = supplements;
    }
}

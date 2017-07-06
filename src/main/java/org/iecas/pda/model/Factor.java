package org.iecas.pda.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaufung on 22/06/2017.
 */
public  abstract class Factor {
    protected String name;
    protected List<Double> components;
    protected Integer size;

    public Factor(String name, List<Double> components){
        this.name = name;
        this.components = components;
        this.size = this.components.size();
    }
    public Factor(String name){
        this.name = name;
        this.components = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }
}

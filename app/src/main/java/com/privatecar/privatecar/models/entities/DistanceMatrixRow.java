package com.privatecar.privatecar.models.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basim Alamuddin on 27/03/2016.
 */
public class DistanceMatrixRow {
    public List<DistanceMatrixElement> elements = new ArrayList<>();

    public List<DistanceMatrixElement> getElements() {
        return elements;
    }

    public void setElements(List<DistanceMatrixElement> elements) {
        this.elements = elements;
    }
}

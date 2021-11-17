package ru.externalsort.externalsort;

import javafx.beans.property.SimpleLongProperty;

public class Node {
    //Создаём перемнные со специальным типом данных, которые могут отображаться в таблице
    public SimpleLongProperty size;
    public SimpleLongProperty simpleMerge;
    public SimpleLongProperty naturalMerge;
    public SimpleLongProperty absorptionMethod;
    //Создаём конструктор alt+insert
    public Node(long size, long simpleMerge, long naturalMerge, long absorptionMethod) {
        this.size = new SimpleLongProperty(size);
        this.simpleMerge = new SimpleLongProperty(simpleMerge);
        this.naturalMerge = new SimpleLongProperty(naturalMerge);
        this.absorptionMethod = new SimpleLongProperty(absorptionMethod);
    }
    //Создаём getter'ы и setter'ы используя alt+insert и выбрав все переменные. Также после создания стираем не нужные методы, позволяющие поставить property
    public long getSize() {
        return size.get();
    }

    public void setSize(long size) {
        this.size.set(size);
    }

    public long getSimpleMerge() {
        return simpleMerge.get();
    }
    public void setSimpleMerge(long simpleMerge) {
        this.simpleMerge.set(simpleMerge);
    }

    public long getNaturalMerge() {
        return naturalMerge.get();
    }

    public void setNaturalMerge(long naturalMerge) {
        this.naturalMerge.set(naturalMerge);
    }

    public long getAbsorptionMethod() {
        return absorptionMethod.get();
    }

    public void setAbsorptionMethod(long absorptionMethod) {
        this.absorptionMethod.set(absorptionMethod);
    }
}

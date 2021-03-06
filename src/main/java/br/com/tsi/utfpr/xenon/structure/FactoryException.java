package br.com.tsi.utfpr.xenon.structure;

public class FactoryException extends RuntimeException {

    public FactoryException(String dtoName, String reason) {
        super(String.format("Object %s cannot be created for this reason: %s", dtoName, reason));
    }
}

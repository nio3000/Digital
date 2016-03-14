package de.neemann.digital.core.basic;

import de.neemann.digital.core.Node;
import de.neemann.digital.core.NodeException;
import de.neemann.digital.core.ObservableValue;

/**
 * @author hneemann
 */
public class Not extends Node {

    private final ObservableValue input;
    private final ObservableValue output;
    private long value;

    public Not(ObservableValue input) throws NodeException {
        this.input = input;
        output = new ObservableValue(input.getBits());
        input.addListener(this);
    }

    @Override
    public void readInputs() throws NodeException {
        value = input.getValue();
    }

    @Override
    public void writeOutputs() throws NodeException {
        output.setValue(~value);
    }

    public ObservableValue getOutput() {
        return output;
    }
}
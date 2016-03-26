package de.neemann.digital.core.memory;

import de.neemann.digital.core.Node;
import de.neemann.digital.core.NodeException;
import de.neemann.digital.core.ObservableValue;
import de.neemann.digital.core.element.AttributeKey;
import de.neemann.digital.core.element.Element;
import de.neemann.digital.core.element.ElementAttributes;
import de.neemann.digital.core.element.ElementTypeDescription;

/**
 * @author hneemann
 */
public class LookUpTable extends Node implements Element {


    public static final ElementTypeDescription DESCRIPTION = new ElementTypeDescription(LookUpTable.class) {
        @Override
        public String[] getInputNames(ElementAttributes elementAttributes) {
            int size = elementAttributes.get(AttributeKey.InputCount);
            String[] names = new String[size];
            for (int i = 0; i < size; i++)
                names[i] = "in_" + i;
            return names;
        }
    }
            .addAttribute(AttributeKey.Bits)
            .addAttribute(AttributeKey.InputCount)
            .addAttribute(AttributeKey.Data)
            .setShortName("LUT");

    private final DataField data;
    private final int bits;
    private final ObservableValue output;
    private ObservableValue[] inputs;
    private int addr;

    public LookUpTable(ElementAttributes attr) {
        bits = attr.get(AttributeKey.Bits);
        output = new ObservableValue("out", bits);
        data = attr.get(AttributeKey.Data);
    }

    @Override
    public void setInputs(ObservableValue... inputs) throws NodeException {
        this.inputs = inputs;
        for (int i = 0; i < inputs.length; i++)
            inputs[i].checkBits(1, this).addObserver(this);
    }

    @Override
    public ObservableValue[] getOutputs() {
        return new ObservableValue[]{output};
    }

    @Override
    public void readInputs() throws NodeException {
        addr = 0;
        int mask = 1;
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i].getValue() > 0)
                addr = addr | mask;
            mask = mask * 2;
        }
    }

    @Override
    public void writeOutputs() throws NodeException {
        output.setValue(data.getData(addr));
    }

}
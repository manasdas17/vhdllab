package hr.fer.zemris.vhdllab.service.result;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

public final class CompilationMessage implements Serializable {

    /*
     * Please note that although this class implements java.io.Serializable and
     * claims to be immutable it does not implement readObject method as
     * specified by Joshua Bloch, "Effective Java: Programming Language Guide",
     * "Item 56: Write readObject methods defensively", page 166.
     * 
     * The reason for this is that this class is used to transfer data from
     * server to client (reverse is not true). So by altering byte stream
     * attacker can only hurt himself!
     */
    private static final long serialVersionUID = -6725777479857711759L;

    private final int row;
    private final int column;
    private final String entityName;
    private final String text;

    public CompilationMessage(String entityName, int row, int column,
            String text) {
        Validate.notNull(text, "Text can't be null");
        this.entityName = entityName;
        this.row = row;
        this.column = column;
        this.text = text;
    }

    public String getEntityName() {
        return entityName;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(text.length() + 20);
        if (!StringUtils.isEmpty(entityName)) {
            sb.append("[").append(entityName).append("]");
        }
        sb.append("[").append(row).append(",").append(column).append("]");
        sb.append(text);
        return sb.toString();
    }

}

package ch.interlis.ili2c.metamodel;

public class ValueRefThis extends Evaluable {
    private final Type type;

    public ValueRefThis(Type type) {
        this.type = type;
    }

    @Override
    public boolean isLogical() {
        return type.isBoolean();
    }

    @Override
    public Type getType() {
        return type;
    }
    public String getName()
    {
        return "THIS";
    }
}

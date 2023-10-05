package ch.interlis.ili2c.metamodel;

public class ClassTypeObjectPath extends Evaluable {
    private final ObjectPath[] objectPaths;

    public ClassTypeObjectPath(ObjectPath[] objectPaths) {
        this.objectPaths = objectPaths;
    }

    @Override
    public boolean isLogical() {
        for (ObjectPath objectPath : objectPaths) {
            if (!objectPath.isLogical()){
                return false;
            }
        }
        return true;
    }
}

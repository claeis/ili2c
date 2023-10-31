package ch.interlis.ili2c.metamodel;

import java.util.List;

public class ContextDef extends AbstractLeafElement{
    protected int nameIdx;
    private Domain generic=null;
    private Domain concretes[]=null;
    public ContextDef(int nameIdx,Domain generic,Domain concretes[]) {
        this.nameIdx = nameIdx;
        this.generic=generic;
        this.concretes=concretes;
    }
    @Override
    public String getName()
    {
        return "Context"+nameIdx;
    }
    public Domain getGeneric() {
        return generic;
    }
    public Domain[] getConcretes() {
        return concretes;
    }

    @Override
    public void checkIntegrity(List<Ili2cSemanticException> errs) throws IllegalStateException {
        super.checkIntegrity(errs);
        Type genericType = generic.getType();
        if (genericType instanceof AbstractCoordType) {
            AbstractCoordType genericCoord = (AbstractCoordType) genericType;
            if (!genericCoord.isGeneric()) {
                throw new IllegalStateException(formatMessage("err_contextdef_domain_not_generic", generic.getName(), this.getName()));
            }
        }

        for (Domain concrete : concretes) {
            concrete.getType().checkTypeExtension(genericType);
        }

        Model model = (Model) getContainer(Model.class);
        Domain[] concretesAllowedInImportedModel = model.resolveGenericDomainFromImportedModels(generic);
        if (concretesAllowedInImportedModel != null) {
            for (Domain concrete : concretes) {
                if (!matchesOrExtendsAllowedDomain(concrete, concretesAllowedInImportedModel)) {
                    throw new IllegalStateException(formatMessage("err_contextdef_domain_not_extending", concrete.getName(), generic.getName()));
                }
            }
        }
    }

    private boolean matchesOrExtendsAllowedDomain(Domain concrete, Domain[] allowedInImportedModel) {
        for (Domain allowed : allowedInImportedModel) {
            if (concrete == allowed || concrete.isExtendingIndirectly(allowed)) {
                return true;
            }
        }
        return false;
    }
}

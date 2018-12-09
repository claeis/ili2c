package ch.interlis.ilirepository.impl;

import java.util.ArrayList;
import java.util.List;

import ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata;

public class ModelLister implements VisitorAction {
    private List<ModelMetadata> result=new ArrayList<ModelMetadata>();
    private boolean ignoreDuplicates=true;
    public boolean processRepository(String uri, RepositoryAccess rep) throws RepositoryAccessException {
        List<ModelMetadata> iliModels=rep.readIlimodelsXml(uri);
        if(iliModels!=null){
            fixFiles(iliModels,uri);
            boolean stopVisiting=mergeModelMetadata(iliModels);
            if(stopVisiting) {
                return true;
            }
        }
        return false;
    }
    private static void fixFiles(List<ModelMetadata> models, String uri) {
        if(!uri.endsWith("/")) {
            uri=uri+"/";
        }
        for(ModelMetadata md:models) {
            md.setFile(uri+md.getFile());
        }
        
    }
    private boolean mergeModelMetadata(List<ModelMetadata> iliModels) {
        if(ignoreDuplicates) {
            for(ModelMetadata md:iliModels) {
                if(RepositoryAccess.findModelMetadata(result,md.getName(),md.getSchemaLanguage(),md.getVersion())==null) {
                    result.add(md);
                }
            }
        }else {
            result.addAll(iliModels);
        }
        return false;
    }

    public void logRepositoryScan(String uri) {
        // TODO Auto-generated method stub
        
    }

    public List<ModelMetadata> getResult() {
        return result;
    }

    public void setResult(List<ModelMetadata> result) {
        this.result = result;
    }

    public boolean ignoreDuplicates() {
        return ignoreDuplicates;
    }
    public void setIgnoreDuplicates(boolean ignoreDuplicates) {
        this.ignoreDuplicates = ignoreDuplicates;
    }


}

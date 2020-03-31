package ch.interlis.ilirepository.impl;

import java.util.ArrayList;
import java.util.List;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ilirepository.IliManager;

public class ModelLister implements VisitorAction {
    private List<ModelMetadata> result=new ArrayList<ModelMetadata>();
    private boolean ignoreDuplicates=true;
    public boolean processRepository(String uri, RepositoryAccess rep) throws RepositoryAccessException {
        
        List<ModelMetadata> iliModels=null;
        try {
            EhiLogger.traceState("read "+IliManager.ILIMODELS_XML+" from <"+uri+">...");
            iliModels=rep.readIlimodelsXml2(uri);
        }catch(RepositoryAccessException ex) {
            EhiLogger.logAdaption("repository <"+uri+"> ignored; "+RepositoryAccess.toString(ex));
        }
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
                if(RepositoryAccess.findModelMetadata2(result,md.getName(),md.getSchemaLanguage(),md.getVersion())==null) {
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

    @Deprecated
    public List<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> getResult() {
        List<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata> ret=new ArrayList<ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata>();
        for(ModelMetadata m:result) {
            ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata m09=RepositoryAccess.mapToIom09(m);
            if(m09!=null) {
                ret.add(m09);
            }
        }
        return ret;
    }
    public List<ModelMetadata> getResult2() {
        return result;
    }
    public void setResult(List<ModelMetadata> result) {
        throw new java.lang.NoSuchMethodError();
    }

    public boolean ignoreDuplicates() {
        return ignoreDuplicates;
    }
    public void setIgnoreDuplicates(boolean ignoreDuplicates) {
        this.ignoreDuplicates = ignoreDuplicates;
    }


}

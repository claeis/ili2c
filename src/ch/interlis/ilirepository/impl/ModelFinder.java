package ch.interlis.ilirepository.impl;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ilirepository.IliFiles;

public class ModelFinder implements VisitorAction {
    private IliFile result=null;
    private String modelName;
    private double iliVersion;
    private boolean dologging=true;
    public boolean processRepository(String uri, RepositoryAccess rep) throws RepositoryAccessException {
        logRepositoryScan(uri,modelName,iliVersion);
        result=getIliFileMetadataShallow(modelName, iliVersion, uri, rep);
        if(result!=null) {
            // model found
            return true;
        }
        return false;
    }
    private void logRepositoryScan(String uri,String model,double version) {
        if(dologging){
            if(version==0.0){
                EhiLogger.logState("lookup model <"+model+"> in repository <"+uri+">");
            }else{
                EhiLogger.logState("lookup model <"+model+"> "+version+" in repository <"+uri+">");
            }
        }
    }
    private IliFile getIliFileMetadataShallow(String modelName,double iliVersion, String uri,RepositoryAccess rep) {
        IliFiles iliFiles=rep.getIliFiles(uri);
        if(iliFiles==null){
            // site offline
        }else{
            IliFile iliFile=null;
            if(iliVersion==0.0){
                iliFile=iliFiles.getFileWithModel(modelName,2.4);
                if(iliFile==null){
                    iliFile=iliFiles.getFileWithModel(modelName,2.3);
                }
                if(iliFile==null){
                    iliFile=iliFiles.getFileWithModel(modelName,1.0);
                }
                if(iliFile==null){
                    iliFile=iliFiles.getFileWithModel(modelName,2.2);
                }
            }else{
                iliFile=iliFiles.getFileWithModel(modelName,iliVersion);
            }
            if(iliFile!=null){
                return iliFile;
            }
        }
        return null;
    }

    public IliFile getResult() {
        return result;
    }
    public void setCriteria(String modelName, double iliVersion) {
        this.modelName=modelName;
        this.iliVersion=iliVersion;
    }
    public void setDoLogging(boolean logLookup) {
        dologging=logLookup;
    }

}

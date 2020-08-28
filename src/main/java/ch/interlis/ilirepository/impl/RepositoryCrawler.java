package ch.interlis.ilirepository.impl;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.modelscan.IliFile;

public class RepositoryCrawler extends RepositoryVisitor {

    private ModelFinder finder;
    public RepositoryCrawler(RepositoryAccess rep) {
        super(rep, new ModelFinder());
        finder=(ModelFinder) action;
    }
    public IliFile getIliFileMetadataDeep(String modelName,double iliVersion,boolean logLookup)
    {
        finder.setDoLogging(logLookup);
        finder.setCriteria(modelName,iliVersion);
        try {
            visitRepositories();
        } catch (RepositoryAccessException e) {
            EhiLogger.logError(e);
        }
        return finder.getResult();
    }

}

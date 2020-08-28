package ch.interlis.ilirepository.impl;

import java.util.ArrayList;
import java.util.List;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.modelscan.IliFile;
import ch.interlis.ilirepository.Dataset;
import ch.interlis.ilirepository.IliFiles;
import ch.interlis.models.DatasetIdx16.DataIndex.BasketMetadata;
import ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata;

public class DataFinder implements VisitorAction {
    private List<Dataset> result=new ArrayList<Dataset>();
    private String bid=null;
    private java.util.Set<String> topics=null;
    private String topicLogTxt=null;
    private boolean dologging=true;
    public boolean processRepository(String uri, RepositoryAccess rep) throws RepositoryAccessException {
        logRepositoryScan(uri);
        List<DatasetMetadata> repoResult=getIliFileMetadataShallow(uri, rep);
        if(repoResult.size()>0) {
            for(DatasetMetadata md:repoResult) {
                result.add(new Dataset(uri,md));
            }
            if(bid!=null) {
                return true;
            }
        }
        return false;
    }
    private void logRepositoryScan(String uri) {
        if(dologging){
            if(bid!=null && topicLogTxt!=null) {
                EhiLogger.logState("search in repository <"+uri+"> for BID <"+bid+"> of "+topicLogTxt);
            }else if(bid!=null) {
                EhiLogger.logState("search in repository <"+uri+"> for BID <"+bid+">");
            }else if(topicLogTxt!=null) {
                EhiLogger.logState("search in repository <"+uri+"> for baskets of "+topicLogTxt);
            }
        }
    }
    private List<DatasetMetadata> getIliFileMetadataShallow(String uri,RepositoryAccess rep) {
        List<DatasetMetadata> result=new ArrayList<DatasetMetadata>();
        List<DatasetMetadata> iliFiles=rep.getIliData(uri);
        if(iliFiles==null){
            // site offline
        }else{
            for(DatasetMetadata iliFile:iliFiles) {
                if(bid==null && topics==null) {
                    result.add(iliFile);
                }else if(bid!=null && iliFile.getid().equals(bid)) {
                    result.add(iliFile);
                }else {
                    for(BasketMetadata basket:iliFile.getbaskets()) {
                        if((bid==null || bid.equals(basket.getid())) && (topics==null || topics.contains(basket.getmodel().getname()))) {
                            result.add(iliFile);
                            break; // add it only once
                        }
                    }
                }
            }
        }
        return result;
    }

    public List<Dataset> getResult() {
        return result;
    }
    public void setCriteria(String bid,String topics[]) {
        result=new ArrayList<Dataset>();
        this.bid=bid;
        if(topics==null || topics.length==0) {
            topicLogTxt=null;
            this.topics=null;
        }else {
            StringBuffer txt=new StringBuffer();
            String sep="";
            this.topics=new java.util.HashSet<String>();
            for(String topic:topics) {
                if(this.topics.add(topic)) {
                    txt.append(sep);
                    txt.append(topic);
                    sep=", ";
                }
            }
        }
    }
    public void setDoLogging(boolean logLookup) {
        dologging=logLookup;
    }

}

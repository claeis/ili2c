package ch.interlis.ilirepository;

import ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata;

public class Dataset {
    private String uri;
    private DatasetMetadata md;
    public Dataset(String uri, DatasetMetadata md) {
        this.uri=uri;
        this.md=md;
    }
    public String getUri() {
        return uri;
    }
    public DatasetMetadata getMetadata() {
        return md;
    }
}

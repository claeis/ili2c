package ch.interlis.ilirepository;

import java.io.File;
import java.util.List;

import ch.interlis.ili2c.Ili2cException;
import ch.interlis.ilirepository.impl.RepositoryAccessException;

public interface ReposManager {

    public List<Dataset> getDatasetIndex(String bid, String topics[]) throws RepositoryAccessException;

    public File[] getLocalFileOfRemoteDataset(Dataset dataset, String fileformat) throws Ili2cException;

}
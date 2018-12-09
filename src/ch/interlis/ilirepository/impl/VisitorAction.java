package ch.interlis.ilirepository.impl;

import java.util.List;

import ch.ehi.iox.ilisite.IliRepository09.RepositoryIndex.ModelMetadata;

public interface VisitorAction {
    public boolean processRepository(String uri, RepositoryAccess rep) throws RepositoryAccessException;
}

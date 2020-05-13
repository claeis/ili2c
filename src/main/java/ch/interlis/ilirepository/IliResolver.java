package ch.interlis.ilirepository;

public interface IliResolver {
	public boolean resolvesUri(String uri);
	public java.io.InputStream resolveIliFile (String uri, String filename) throws java.io.IOException;

}

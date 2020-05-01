package ch.interlis.ilirepository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelMetadata{
  static public final String ili1="ili1";
  static public final String ili2_2="ili2_2";
  static public final String ili2_3="ili2_3";
  static public final String ili2_4="ili2_4";
  static public final String all[]= {ili1,ili2_2,ili2_3,ili2_4};

    private String oid=null;
    private String name=null;
    private String schemaLanguage=null;
    private String file=null;
    private String version=null;
    private String versionComment=null;
    private String nameLanguage=null;
    private String publishingDate=null;
    private String original=null;
    private List<String> dependsOnModel=new ArrayList<String>();
    private String precursorVersion=null;
    private List<String> followupModel=new ArrayList<String>();
    private List<String> derivedModel=new ArrayList<String>();
    private String title=null;
    private String shortDescription=null;
    private String tags=null;
    private String issuer=null;
    private String technicalContact=null;
    private String furtherInformation=null;
    private String furtherMetadata=null;
    private List<String> knownWMS=new ArrayList<String>();
    private List<String> knownWFS=new ArrayList<String>();
    private List<String> knownPortal=new ArrayList<String>();
    private boolean browseOnly=false;
    private String md5=null;
    public String getOid() {
        return oid;
    }
    public void setOid(String oid) {
        this.oid = oid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSchemaLanguage() {
        return schemaLanguage;
    }
    public void setSchemaLanguage(String schemaLanguage) {
        this.schemaLanguage = schemaLanguage;
    }
    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getVersionComment() {
        return versionComment;
    }
    public void setVersionComment(String versionComment) {
        this.versionComment = versionComment;
    }
    public String getNameLanguage() {
        return nameLanguage;
    }
    public void setNameLanguage(String nameLanguage) {
        this.nameLanguage = nameLanguage;
    }
    public String getPublishingDate() {
        return publishingDate;
    }
    public void setPublishingDate(String publishingDate) {
        this.publishingDate = publishingDate;
    }
    public String getOriginal() {
        return original;
    }
    public void setOriginal(String original) {
        this.original = original;
    }
    public void setDependsOnModel(String deps[]) {
        if(deps==null) {
            dependsOnModel=new ArrayList<String>();
        }else {
            dependsOnModel=new ArrayList<String>(Arrays.asList(deps));
        }
    }
    public String[] getDependsOnModel() {
        return dependsOnModel.toArray(new String[dependsOnModel.size()]);
    }
    public void addDependsOnModel(String dependsOnModel) {
        this.dependsOnModel.add(dependsOnModel);
    }
    public String getPrecursorVersion() {
        return precursorVersion;
    }
    public void setPrecursorVersion(String precursorVersion) {
        this.precursorVersion = precursorVersion;
    }
    public String[] getFollowupModel() {
        return followupModel.toArray(new String[followupModel.size()]);
    }
    public void addFollowupModel(String followupModel) {
        this.followupModel.add(followupModel);
    }
    public String[] getDerivedModel() {
        return derivedModel.toArray(new String[derivedModel.size()]);
    }
    public void addDerivedModel(String derivedModel) {
        this.derivedModel.add(derivedModel);
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getShortDescription() {
        return shortDescription;
    }
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getIssuer() {
        return issuer;
    }
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
    public String getTechnicalContact() {
        return technicalContact;
    }
    public void setTechnicalContact(String technicalContact) {
        this.technicalContact = technicalContact;
    }
    public String getFurtherInformation() {
        return furtherInformation;
    }
    public void setFurtherInformation(String furtherInformation) {
        this.furtherInformation = furtherInformation;
    }
    public String getFurtherMetadata() {
        return furtherMetadata;
    }
    public void setFurtherMetadata(String furtherMetadata) {
        this.furtherMetadata = furtherMetadata;
    }
    public String[] getKnownWMS() {
        return knownWMS.toArray(new String[knownWMS.size()]);
    }
    public void addKnownWMS(String knownWMS) {
        this.knownWMS.add(knownWMS);
    }
    public String[] getKnownWFS() {
        return knownWFS.toArray(new String[knownWFS.size()]);
    }
    public void addKnownWFS(String knownWFS) {
        this.knownWFS.add(knownWFS);
    }
    public String[] getKnownPortal() {
        return knownPortal.toArray(new String[knownPortal.size()]);
    }
    public void addKnownPortal(String knownPortal) {
        this.knownPortal.add(knownPortal);
    }
    public boolean isBrowseOnly() {
        return browseOnly;
    }
    public void setBrowseOnly(boolean browseOnly) {
        this.browseOnly = browseOnly;
    }
    public String getMd5() {
        return md5;
    }
    public void setMd5(String md5) {
        this.md5 = md5;
    }
  
}

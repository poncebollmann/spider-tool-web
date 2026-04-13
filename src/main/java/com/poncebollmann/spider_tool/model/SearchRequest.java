package com.poncebollmann.spider_tool.model;

public class SearchRequest {

    private String protocolo;
    private String sample;
    private String phenomenon;
    private String design;
    private String evaluation;
    private String researchType;
    private String research;

    // Getters y setters
    public String getProtocolo()     { return protocolo; }
    public String getSample()        { return sample; }
    public String getPhenomenon()    { return phenomenon; }
    public String getDesign()        { return design; }
    public String getEvaluation()    { return evaluation; }
    public String getResearchType()  { return researchType; }
    public String getResearch()      { return research; }

    public void setProtocolo(String protocolo)        { this.protocolo = protocolo; }
    public void setSample(String sample)              { this.sample = sample; }
    public void setPhenomenon(String phenomenon)      { this.phenomenon = phenomenon; }
    public void setDesign(String design)              { this.design = design; }
    public void setEvaluation(String evaluation)      { this.evaluation = evaluation; }
    public void setResearchType(String researchType)  { this.researchType = researchType; }
    public void setResearch(String research)          { this.research = research; }
}
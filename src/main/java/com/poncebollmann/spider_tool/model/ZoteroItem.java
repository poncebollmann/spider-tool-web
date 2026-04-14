package com.poncebollmann.spider_tool.model;

public class ZoteroItem {

    private String key;
    private String titulo;
    private String autores;
    private int anio;
    private String revista;
    private String doi;
    private String decision;       // Include / Exclude / Maybe
    private String estado;         // Pending / In review / Final / Excluded
    private String motivoExclusion;

    public String getKey()              { return key; }
    public String getTitulo()           { return titulo; }
    public String getAutores()          { return autores; }
    public int getAnio()                { return anio; }
    public String getRevista()          { return revista; }
    public String getDoi()              { return doi; }
    public String getDecision()         { return decision; }
    public String getEstado()           { return estado; }
    public String getMotivoExclusion()  { return motivoExclusion; }

    public void setKey(String key)                      { this.key = key; }
    public void setTitulo(String titulo)                { this.titulo = titulo; }
    public void setAutores(String autores)              { this.autores = autores; }
    public void setAnio(int anio)                       { this.anio = anio; }
    public void setRevista(String revista)              { this.revista = revista; }
    public void setDoi(String doi)                      { this.doi = doi; }
    public void setDecision(String decision)            { this.decision = decision; }
    public void setEstado(String estado)                { this.estado = estado; }
    public void setMotivoExclusion(String motivo)       { this.motivoExclusion = motivo; }

    /**
     * Genera el study_id automáticamente desde el primer apellido y el año.
     * Ejemplo: LeGros2020
     */
    public String getStudyId() {
        String apellido = autores != null && !autores.isEmpty()
                ? autores.split(",")[0].trim().replaceAll("\\s+", "")
                : "Unknown";
        return apellido + anio;
    }

    /**
     * Genera la cita corta.
     * Ejemplo: Le Gros (2020)
     */
    public String getCitaCorta() {
        String apellido = autores != null && !autores.isEmpty()
                ? autores.split(",")[0].trim()
                : "Unknown";
        return apellido + " (" + anio + ")";
    }
}
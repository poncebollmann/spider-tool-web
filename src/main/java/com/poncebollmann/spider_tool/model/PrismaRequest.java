package com.poncebollmann.spider_tool.model;

public class PrismaRequest {

    // IDENTIFICATION
    private int registrosBD;
    private int registrosRegistros;
    private int duplicadosEliminados;
    private int excluidosAutomatizacion;
    private int excluidosOtrasRazones;

    // SCREENING
    private int registrosCribados;
    private int registrosExcluidos;
    private int informesBuscados;
    private int informesNoRecuperados;
    private int informesElegibilidad;

    // EXCLUSION REASONS (elegibilidad)
    private int excluidosDiseno;
    private int excluidosPoblacion;
    private int excluidosIntervencion;
    private int excluidosOutcome;
    private int excluidosTextoNoDisponible;
    private int excluidosDuplicadoTardio;
    private int excluidosOtros;

    // INCLUDED
    private int estudiosIncluidos;
    private int informesIncluidos;

    // Getters y setters
    public int getRegistrosBD()                  { return registrosBD; }
    public int getRegistrosRegistros()            { return registrosRegistros; }
    public int getDuplicadosEliminados()          { return duplicadosEliminados; }
    public int getExcluidosAutomatizacion()       { return excluidosAutomatizacion; }
    public int getExcluidosOtrasRazones()         { return excluidosOtrasRazones; }
    public int getRegistrosCribados()             { return registrosCribados; }
    public int getRegistrosExcluidos()            { return registrosExcluidos; }
    public int getInformesBuscados()              { return informesBuscados; }
    public int getInformesNoRecuperados()         { return informesNoRecuperados; }
    public int getInformesElegibilidad()          { return informesElegibilidad; }
    public int getExcluidosDiseno()               { return excluidosDiseno; }
    public int getExcluidosPoblacion()            { return excluidosPoblacion; }
    public int getExcluidosIntervencion()         { return excluidosIntervencion; }
    public int getExcluidosOutcome()              { return excluidosOutcome; }
    public int getExcluidosTextoNoDisponible()    { return excluidosTextoNoDisponible; }
    public int getExcluidosDuplicadoTardio()      { return excluidosDuplicadoTardio; }
    public int getExcluidosOtros()                { return excluidosOtros; }
    public int getEstudiosIncluidos()             { return estudiosIncluidos; }
    public int getInformesIncluidos()             { return informesIncluidos; }

    public void setRegistrosBD(int v)                 { this.registrosBD = v; }
    public void setRegistrosRegistros(int v)           { this.registrosRegistros = v; }
    public void setDuplicadosEliminados(int v)         { this.duplicadosEliminados = v; }
    public void setExcluidosAutomatizacion(int v)      { this.excluidosAutomatizacion = v; }
    public void setExcluidosOtrasRazones(int v)        { this.excluidosOtrasRazones = v; }
    public void setRegistrosCribados(int v)            { this.registrosCribados = v; }
    public void setRegistrosExcluidos(int v)           { this.registrosExcluidos = v; }
    public void setInformesBuscados(int v)             { this.informesBuscados = v; }
    public void setInformesNoRecuperados(int v)        { this.informesNoRecuperados = v; }
    public void setInformesElegibilidad(int v)         { this.informesElegibilidad = v; }
    public void setExcluidosDiseno(int v)              { this.excluidosDiseno = v; }
    public void setExcluidosPoblacion(int v)           { this.excluidosPoblacion = v; }
    public void setExcluidosIntervencion(int v)        { this.excluidosIntervencion = v; }
    public void setExcluidosOutcome(int v)             { this.excluidosOutcome = v; }
    public void setExcluidosTextoNoDisponible(int v)   { this.excluidosTextoNoDisponible = v; }
    public void setExcluidosDuplicadoTardio(int v)     { this.excluidosDuplicadoTardio = v; }
    public void setExcluidosOtros(int v)               { this.excluidosOtros = v; }
    public void setEstudiosIncluidos(int v)            { this.estudiosIncluidos = v; }
    public void setInformesIncluidos(int v)            { this.informesIncluidos = v; }
}
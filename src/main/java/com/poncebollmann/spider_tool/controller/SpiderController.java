package com.poncebollmann.spider_tool.controller;

import com.poncebollmann.spider_tool.model.SearchRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SpiderController {

    @GetMapping("/")
    public String landing() {
        return "landing";
    }

    @GetMapping("/spider")
    public String index(Model model) {
        model.addAttribute("searchRequest", new SearchRequest());
        return "index";
    }

    @PostMapping("/generar")
    public String generar(@ModelAttribute SearchRequest request, Model model) {

        // Construir la cadena booleana con los campos del formulario
        StringBuilder cadena = new StringBuilder();

        agregarTermino(cadena, request.getSample());
        agregarTermino(cadena, request.getPhenomenon());
        agregarTermino(cadena, request.getDesign());
        agregarTermino(cadena, request.getEvaluation());
        agregarTermino(cadena, request.getResearchType());

        if (request.getResearch() != null && !request.getResearch().isBlank()) {
            agregarTermino(cadena, request.getResearch());
        }

        model.addAttribute("cadena", cadena.toString());
        model.addAttribute("searchRequest", request);
        return "resultado";
    }

    private void agregarTermino(StringBuilder cadena, String termino) {
        if (termino != null && !termino.isBlank()) {
            if (cadena.length() > 0) cadena.append(" AND ");
            cadena.append(termino.trim());
        }
    }
}
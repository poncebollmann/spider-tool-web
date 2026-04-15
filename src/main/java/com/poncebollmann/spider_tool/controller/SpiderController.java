package com.poncebollmann.spider_tool.controller;

import com.poncebollmann.spider_tool.model.SearchRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        List<String> bloques = new ArrayList<>();

        agregarBloque(bloques, request.getSample());
        agregarBloque(bloques, request.getPhenomenon());
        agregarBloque(bloques, request.getDesign());
        agregarBloque(bloques, request.getEvaluation());
        agregarBloque(bloques, request.getResearchType());

        if (request.getResearch() != null && !request.getResearch().isBlank()) {
            agregarBloque(bloques, request.getResearch());
        }

        // Cadena genérica
        String cadenaGenerica = String.join(" AND ", bloques);

        // Cadena Scopus
        String cadenaScopus = bloques.stream()
                .map(b -> "TITLE-ABS-KEY(" + b + ")")
                .collect(Collectors.joining(" AND "));

        // Cadena Web of Science
        String cadenaWoS = bloques.stream()
                .map(b -> "TS=(" + b + ")")
                .collect(Collectors.joining(" AND "));

        // Cadena PubMed
        String cadenaPubMed = bloques.stream()
                .map(b -> "(" + b.replaceAll("([\\wáéíóúüñÁÉÍÓÚÜÑ]+)", "$1[tw]") + ")")
                .collect(Collectors.joining(" AND "));

        model.addAttribute("cadena",        cadenaGenerica);
        model.addAttribute("cadenaScopus",  cadenaScopus);
        model.addAttribute("cadenaWoS",     cadenaWoS);
        model.addAttribute("cadenaPubMed",  cadenaPubMed);
        model.addAttribute("searchRequest", request);
        return "resultado";
    }

    private void agregarBloque(List<String> bloques, String termino) {
        if (termino != null && !termino.isBlank()) {
            bloques.add(termino.trim());
        }
    }
}
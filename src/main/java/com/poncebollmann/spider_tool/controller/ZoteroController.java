package com.poncebollmann.spider_tool.controller;

import com.poncebollmann.spider_tool.model.ZoteroItem;
import com.poncebollmann.spider_tool.service.ZoteroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class ZoteroController {

    @Autowired
    private ZoteroService zoteroService;

    @GetMapping("/zotero")
    public String formulario() {
        return "zotero-form";
    }

    @PostMapping("/zotero/colecciones")
    public String colecciones(
            @RequestParam String userId,
            @RequestParam String apiKey,
            Model model) {

        try {
            List<ZoteroItem> colecciones = zoteroService.getColecciones(userId, apiKey);
            model.addAttribute("colecciones", colecciones);
            model.addAttribute("userId", userId);
            model.addAttribute("apiKey", apiKey);
            return "zotero-colecciones";
        } catch (IOException e) {
            model.addAttribute("error", "No se pudo conectar con Zotero: " + e.getMessage());
            return "zotero-form";
        }
    }

    @PostMapping("/zotero/items")
    public String items(
            @RequestParam String userId,
            @RequestParam String apiKey,
            @RequestParam String collectionKey,
            @RequestParam String collectionName,
            Model model) {

        try {
            List<ZoteroItem> items = zoteroService.getItems(userId, apiKey, collectionKey);
            model.addAttribute("items", items);
            model.addAttribute("userId", userId);
            model.addAttribute("apiKey", apiKey);
            model.addAttribute("collectionKey", collectionKey);
            model.addAttribute("collectionName", collectionName);
            return "zotero-items";
        } catch (IOException e) {
            model.addAttribute("error", "Error al cargar ítems: " + e.getMessage());
            return "zotero-form";
        }
    }

    @PostMapping("/zotero/exportar")
    public ResponseEntity<byte[]> exportar(
            @RequestParam List<String> keys,
            @RequestParam List<String> titulos,
            @RequestParam List<String> autores,
            @RequestParam List<Integer> anios,
            @RequestParam List<String> revistas,
            @RequestParam List<String> decisiones,
            @RequestParam List<String> estados,
            @RequestParam(required = false) List<String> motivos,
            @RequestParam(required = false) List<String> disenios,
            @RequestParam(required = false) List<String> revisores,
            @RequestParam(required = false) List<String> notas) {

        StringBuilder csv = new StringBuilder();

        // Cabecera PRISMA 2020 completa
        csv.append("study_id,citation_short,title,year,journal," +
                "study_design,full_text_decision,decision_status," +
                "exclusion_reason,reviewer,notes\n");

        for (int i = 0; i < keys.size(); i++) {
            String apellido  = autores.get(i).split(",")[0].trim()
                    .replaceAll("\\s+", "")
                    .replaceAll("\\.", "");
            String studyId   = apellido + anios.get(i);
            String citaCorta = autores.get(i).split(",")[0].trim() + " (" + anios.get(i) + ")";
            String motivo    = getValue(motivos, i);
            String disenio   = getValue(disenios, i);
            String revisor   = getValue(revisores, i);
            String nota      = getValue(notas, i);

            csv.append(escaparCSV(studyId)).append(",")
                    .append(escaparCSV(citaCorta)).append(",")
                    .append(escaparCSV(titulos.get(i))).append(",")
                    .append(anios.get(i)).append(",")
                    .append(escaparCSV(revistas.get(i))).append(",")
                    .append(escaparCSV(disenio)).append(",")
                    .append(escaparCSV(decisiones.get(i))).append(",")
                    .append(escaparCSV(estados.get(i))).append(",")
                    .append(escaparCSV(motivo)).append(",")
                    .append(escaparCSV(revisor)).append(",")
                    .append(escaparCSV(nota)).append("\n");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/csv"));
        headers.setContentDispositionFormData("attachment", "extraction_prisma2020.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csv.toString().getBytes());
    }

    private String getValue(List<String> lista, int index) {
        if (lista == null || index >= lista.size()) return "";
        String val = lista.get(index);
        return (val == null || val.equalsIgnoreCase("null")) ? "" : val;
    }

    private String escaparCSV(String valor) {
        if (valor == null || valor.equalsIgnoreCase("NaN") || valor.equalsIgnoreCase("null")) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            valor = valor.replace("\"", "\"\"");
            return "\"" + valor + "\"";
        }
        return valor;
    }
}
package com.poncebollmann.spider_tool.controller;

import com.poncebollmann.spider_tool.service.ClaudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.io.IOException;

/**
 * Controlador para el asistente IA basado en Claude.
 * Gestiona el chat del investigador con el asistente.
 */
@Controller
public class ClaudeController {

    @Autowired
    private ClaudeService claudeService;

    @GetMapping("/asistente")
    public String asistente() {
        return "asistente";
    }

    @PostMapping("/asistente/chat")
    @ResponseBody
    public String chat(
            @RequestParam String mensaje,
            @RequestParam(required = false) String contexto) {

        try {
            return claudeService.chat(mensaje, contexto);
        } catch (IOException e) {
            return "Error al conectar con el asistente: " + e.getMessage();
        }
    }
}
package com.poncebollmann.spider_tool.controller;

import com.poncebollmann.spider_tool.model.PrismaRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PrismaController {

    @GetMapping("/prisma")
    public String formulario(Model model) {
        model.addAttribute("prismaRequest", new PrismaRequest());
        return "prisma-form";
    }

    @PostMapping("/prisma/generar")
    public ResponseEntity<byte[]> generar(@ModelAttribute PrismaRequest r) {

        String svg = generarSVG(r);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("image/svg+xml"));
        headers.setContentDispositionFormData("attachment", "diagrama_prisma_2020.svg");

        return ResponseEntity.ok()
                .headers(headers)
                .body(svg.getBytes());
    }

    private String generarSVG(PrismaRequest r) {

        // Altura dinámica de la caja de exclusión según número de motivos con valor > 0
        int motivosConValor = 0;
        if (r.getExcluidosDiseno() > 0)            motivosConValor++;
        if (r.getExcluidosPoblacion() > 0)         motivosConValor++;
        if (r.getExcluidosIntervencion() > 0)      motivosConValor++;
        if (r.getExcluidosOutcome() > 0)           motivosConValor++;
        if (r.getExcluidosTextoNoDisponible() > 0) motivosConValor++;
        if (r.getExcluidosDuplicadoTardio() > 0)   motivosConValor++;
        if (r.getExcluidosOtros() > 0)             motivosConValor++;

        // Mínimo 1 motivo visible aunque todos sean 0
        if (motivosConValor == 0) motivosConValor = 1;

        int alturaExclusion = 40 + (motivosConValor * 20);
        int alturaSVG       = 750 + Math.max(0, alturaExclusion - 100);

        // Construir líneas de exclusión dinámicamente
        StringBuilder lineasExclusion = new StringBuilder();
        int lineaY = 502;
        if (r.getExcluidosDiseno() > 0) {
            lineasExclusion.append(String.format(
                    "<text x='515' y='%d'>Ineligible study design (n = %d)</text>",
                    lineaY, r.getExcluidosDiseno()));
            lineaY += 18;
        }
        if (r.getExcluidosPoblacion() > 0) {
            lineasExclusion.append(String.format(
                    "<text x='515' y='%d'>Ineligible population (n = %d)</text>",
                    lineaY, r.getExcluidosPoblacion()));
            lineaY += 18;
        }
        if (r.getExcluidosIntervencion() > 0) {
            lineasExclusion.append(String.format(
                    "<text x='515' y='%d'>Ineligible intervention (n = %d)</text>",
                    lineaY, r.getExcluidosIntervencion()));
            lineaY += 18;
        }
        if (r.getExcluidosOutcome() > 0) {
            lineasExclusion.append(String.format(
                    "<text x='515' y='%d'>Ineligible outcome (n = %d)</text>",
                    lineaY, r.getExcluidosOutcome()));
            lineaY += 18;
        }
        if (r.getExcluidosTextoNoDisponible() > 0) {
            lineasExclusion.append(String.format(
                    "<text x='515' y='%d'>Full text unavailable (n = %d)</text>",
                    lineaY, r.getExcluidosTextoNoDisponible()));
            lineaY += 18;
        }
        if (r.getExcluidosDuplicadoTardio() > 0) {
            lineasExclusion.append(String.format(
                    "<text x='515' y='%d'>Late duplicate (n = %d)</text>",
                    lineaY, r.getExcluidosDuplicadoTardio()));
            lineaY += 18;
        }
        if (r.getExcluidosOtros() > 0) {
            lineasExclusion.append(String.format(
                    "<text x='515' y='%d'>Other reasons (n = %d)</text>",
                    lineaY, r.getExcluidosOtros()));
            lineaY += 18;
        }

        // Posiciones dinámicas basadas en altura de exclusión
        int yIncluido     = 480 + alturaExclusion + 20;
        int yNotas        = yIncluido + 100;
        int yFlecha4      = 530 + alturaExclusion;

        return """
        <svg xmlns="http://www.w3.org/2000/svg" width="900" height="%d"
             font-family="Arial" font-size="12">

          <defs>
            <marker id="arrow" markerWidth="10" markerHeight="7"
                    refX="10" refY="3.5" orient="auto">
              <polygon points="0 0, 10 3.5, 0 7" fill="#333"/>
            </marker>
          </defs>

          <rect width="900" height="%d" fill="white"/>

          <text x="450" y="30" text-anchor="middle" font-size="13"
                font-weight="bold" fill="#333">
            PRISMA 2020 Flow Diagram — Databases and Registers only
          </text>

          <!-- Etiquetas laterales -->
          <rect x="10" y="55" width="70" height="160" rx="4" fill="#BBDEFB"/>
          <text x="45" y="140" text-anchor="middle" font-weight="bold"
                font-size="11" fill="#1565C0"
                transform="rotate(-90,45,140)">Identification</text>

          <rect x="10" y="230" width="70" height="360" rx="4" fill="#BBDEFB"/>
          <text x="45" y="415" text-anchor="middle" font-weight="bold"
                font-size="11" fill="#1565C0"
                transform="rotate(-90,45,415)">Screening</text>

          <rect x="10" y="605" width="70" height="%d" rx="4" fill="#BBDEFB"/>
          <text x="45" y="%d" text-anchor="middle" font-weight="bold"
                font-size="11" fill="#1565C0"
                transform="rotate(-90,45,%d)">Included</text>

          <!-- IDENTIFICATION -->
          <rect x="100" y="60" width="280" height="70" rx="4"
                fill="white" stroke="#333" stroke-width="1.2"/>
          <text x="115" y="82" font-weight="bold">Records identified from*:</text>
          <text x="125" y="100">Databases (n = %d)</text>
          <text x="125" y="118">Registers (n = %d)</text>

          <line x1="380" y1="95" x2="490" y2="95"
                stroke="#333" stroke-width="1.2" marker-end="url(#arrow)"/>

          <rect x="490" y="55" width="280" height="100" rx="4"
                fill="white" stroke="#333" stroke-width="1.2"/>
          <text x="505" y="76" font-style="italic">Records removed before screening:</text>
          <text x="515" y="95">Duplicate records removed (n = %d)</text>
          <text x="515" y="113">Ineligible by automation (n = %d)</text>
          <text x="515" y="131">Other reasons (n = %d)</text>

          <line x1="240" y1="130" x2="240" y2="235"
                stroke="#333" stroke-width="1.2" marker-end="url(#arrow)"/>

          <!-- SCREENING -->
          <rect x="100" y="235" width="280" height="55" rx="4"
                fill="white" stroke="#333" stroke-width="1.2"/>
          <text x="115" y="258">Records screened</text>
          <text x="115" y="278">(n = %d)</text>

          <line x1="380" y1="262" x2="490" y2="262"
                stroke="#333" stroke-width="1.2" marker-end="url(#arrow)"/>

          <rect x="490" y="235" width="280" height="55" rx="4"
                fill="white" stroke="#333" stroke-width="1.2"/>
          <text x="505" y="258">Records excluded**</text>
          <text x="505" y="278">(n = %d)</text>

          <line x1="240" y1="290" x2="240" y2="355"
                stroke="#333" stroke-width="1.2" marker-end="url(#arrow)"/>

          <rect x="100" y="355" width="280" height="55" rx="4"
                fill="white" stroke="#333" stroke-width="1.2"/>
          <text x="115" y="378">Reports sought for retrieval</text>
          <text x="115" y="398">(n = %d)</text>

          <line x1="380" y1="382" x2="490" y2="382"
                stroke="#333" stroke-width="1.2" marker-end="url(#arrow)"/>

          <rect x="490" y="355" width="280" height="55" rx="4"
                fill="white" stroke="#333" stroke-width="1.2"/>
          <text x="505" y="378">Reports not retrieved</text>
          <text x="505" y="398">(n = %d)</text>

          <line x1="240" y1="410" x2="240" y2="475"
                stroke="#333" stroke-width="1.2" marker-end="url(#arrow)"/>

          <rect x="100" y="475" width="280" height="55" rx="4"
                fill="white" stroke="#333" stroke-width="1.2"/>
          <text x="115" y="498">Reports assessed for eligibility</text>
          <text x="115" y="518">(n = %d)</text>

          <line x1="380" y1="502" x2="490" y2="502"
                stroke="#333" stroke-width="1.2" marker-end="url(#arrow)"/>

          <!-- Caja exclusión dinámica -->
          <rect x="490" y="460" width="280" height="%d" rx="4"
                fill="white" stroke="#333" stroke-width="1.2"/>
          <text x="505" y="482" font-weight="bold">Reports excluded:</text>
          %s

          <line x1="240" y1="530" x2="240" y2="%d"
                stroke="#333" stroke-width="1.2" marker-end="url(#arrow)"/>

          <!-- INCLUDED -->
          <rect x="100" y="%d" width="280" height="70" rx="4"
                fill="white" stroke="#333" stroke-width="1.2"/>
          <text x="115" y="%d">Studies included in review</text>
          <text x="115" y="%d">(n = %d)</text>
          <text x="115" y="%d">Reports of included studies (n = %d)</text>

          <!-- Notas -->
          <text x="100" y="%d" font-size="10" fill="#555">
            * Consider reporting the number of records identified from each database separately.
          </text>
          <text x="100" y="%d" font-size="10" fill="#555">
            ** If automation tools were used, indicate records excluded by humans vs. automation.
          </text>

        </svg>
        """.formatted(
                alturaSVG, alturaSVG,
                // etiqueta Included
                80 + alturaExclusion - 100,
                yIncluido + 35, yIncluido + 35,
                // identification
                r.getRegistrosBD(), r.getRegistrosRegistros(),
                r.getDuplicadosEliminados(), r.getExcluidosAutomatizacion(), r.getExcluidosOtrasRazones(),
                // screening
                r.getRegistrosCribados(), r.getRegistrosExcluidos(),
                r.getInformesBuscados(), r.getInformesNoRecuperados(),
                r.getInformesElegibilidad(),
                // exclusion box
                alturaExclusion, lineasExclusion.toString(),
                // flecha y caja included
                yIncluido,
                yIncluido,
                yIncluido + 22, yIncluido + 40,
                r.getEstudiosIncluidos(),
                yIncluido + 58, r.getInformesIncluidos(),
                // notas
                yNotas, yNotas + 15
        );
    }}
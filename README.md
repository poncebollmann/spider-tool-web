# SysReview Tool — Versión consola

Herramienta de línea de comandos en Java para construir estrategias de búsqueda booleana siguiendo los protocolos **SPIDER**, **PICO** y **PICOS**, utilizada como apoyo metodológico en revisiones sistemáticas de investigación cualitativa, cuantitativa y mixta.

> **Nota:** Este repositorio contiene la versión consola (v1.x). La versión web completa con Spring Boot, integración Zotero, diagrama PRISMA 2020, checklist y asistente IA está disponible en [spider-tool-web](https://github.com/poncebollmann/spider-tool-web).

---

## ¿Qué es una revisión sistemática?

Una revisión sistemática es una síntesis rigurosa y reproducible de la literatura científica sobre una pregunta de investigación específica. Sigue un protocolo predefinido que minimiza el sesgo y maximiza la transparencia. La herramienta apoya las fases iniciales del proceso: formulación de la pregunta, construcción de la estrategia de búsqueda y exportación de los resultados.

---

## Protocolos soportados

| Protocolo | Uso recomendado | Componentes |
|-----------|----------------|-------------|
| **SPIDER** | Investigación cualitativa y mixta | S · P · I · D · E · R |
| **PICO** | Investigación cuantitativa y clínica | P · I · C · O |
| **PICOS** | PICO + tipo de estudio | P · I · C · O · S |

### Componentes SPIDER

| Letra | Componente | Pregunta clave |
|-------|-----------|----------------|
| **S** | Sample | ¿Quién es la población de estudio? |
| **P** | Phenomenon of Interest | ¿Qué fenómeno se investiga? |
| **I** | Design | ¿Qué diseño de investigación se busca? |
| **D** | Evaluation | ¿Qué resultado o medida se considera? |
| **E** | Research type | ¿Cuali, cuanti o mixto? |
| **R** | Research | ¿Hay restricciones de contexto o periodo? |

---

## Funcionalidades

- Guía interactiva por cada componente del protocolo con preguntas orientadoras
- Soporte para sinónimos por componente — se unen automáticamente con `OR`
- Validación de campos obligatorios
- Construcción automática de la cadena booleana con operadores `AND` / `OR`
- Historial de búsquedas anteriores persistente en `historial.csv`
- Exportación a **CSV** compatible con Excel y Google Sheets
- Posibilidad de realizar varias búsquedas en la misma sesión

### Ejemplo de cadena generada

```
(adolescentes OR jóvenes OR youth) AND (burnout OR agotamiento laboral) AND entrevista AND bienestar AND cualitativa
```

Compatible con **Scopus**, **Web of Science**, **PubMed** y **PsycINFO**.

---

## Estructura del proyecto

```
spider-tool/
└── src/
    ├── Main.java               # Punto de entrada. Controla el flujo y el historial.
    ├── ProtocolTool.java       # Clase abstracta base para todos los protocolos.
    ├── SpiderTool.java         # Implementación del protocolo SPIDER.
    ├── PicoTool.java           # Implementación del protocolo PICO.
    ├── PicosTool.java          # Implementación del protocolo PICOS.
    ├── SpiderComponent.java    # Representa cada componente del protocolo.
    ├── HistorialManager.java   # Gestiona el historial de búsquedas en CSV.
    └── Exporter.java           # Exporta el resultado a CSV con timestamp.
```

### Conceptos Java aplicados

| Concepto | Dónde se aplica |
|----------|----------------|
| Clases y objetos | `SpiderComponent`, `SpiderTool` |
| Herencia y clases abstractas | `ProtocolTool` → `SpiderTool`, `PicoTool`, `PicosTool` |
| ArrayList y colecciones | Lista de componentes y sinónimos |
| Scanner y entrada por consola | Interacción con el usuario |
| FileWriter / BufferedReader | Exportación CSV e historial |
| `@Override` | Implementación de métodos abstractos |

---

## Cómo ejecutarlo

### Requisitos
- Java 21 o superior
- IntelliJ IDEA (recomendado)

### Pasos

```bash
git clone https://github.com/poncebollmann/spider-tool.git
```

Abre el proyecto en IntelliJ IDEA y ejecuta `Main.java`.

### Ejemplo de sesión

```
╔══════════════════════════════════════════════════════════╗
║         Systematic Review Protocol Tool  v1.2           ║
╚══════════════════════════════════════════════════════════╝

  ¿Ver historial de búsquedas anteriores? (s/n): n

  ¿Qué protocolo deseas usar?
    1. SPIDER  (investigación cualitativa y mixta)
    2. PICO    (investigación cuantitativa y clínica)
    3. PICOS   (PICO + tipo de estudio)

  Tu elección (1/2/3): 1

[ S ]  SAMPLE
  Tu término: adolescentes
  Sinónimo: jóvenes
  Sinónimo: fin
  ✓ Componente guardado.

  ...

  CADENA DE BÚSQUEDA BOOLEANA:

  (adolescentes OR jóvenes) AND burnout AND entrevista AND bienestar AND cualitativa

  ¿Exportar resultado a .csv? (s/n): s
  ✓ Exportado correctamente: spider_20260415_143022.csv
```

---

## Versiones

| Versión | Descripción |
|---------|-------------|
| v1.0 | Versión consola funcional con exportación CSV — protocolo SPIDER |
| v1.1 | Soporte para protocolos PICO y PICOS con herencia de clases |
| v1.2 | Historial de búsquedas persistente con `HistorialManager` |

---

## Versión web

La evolución completa de este proyecto está disponible como aplicación web en:

**Repositorio:** [spider-tool-web](https://github.com/poncebollmann/spider-tool-web)
**Demo en línea:** [spider-tool-web-production.up.railway.app](https://spider-tool-web-production.up.railway.app)

La versión web incluye:
- Formulario dinámico SPIDER / PICO / PICOS con labels adaptativos
- Cadenas de búsqueda específicas para Scopus, Web of Science y PubMed
- Diagrama PRISMA 2020 descargable en SVG
- Integración con Zotero API para extracción de estudios
- Checklist PRISMA 2020 con los 27 ítems exportable en CSV
- Asistente IA especializado en revisiones sistemáticas (Claude — Anthropic)
- Dashboard de revisión con flujo guiado paso a paso
- Manual de uso integrado

---

## Motivación

Este proyecto nació en el marco del **Curso de Especialización en Programación con Java** de **La Salle Barcelona**, como ejercicio de aplicación práctica del lenguaje a un problema real de investigación.

La construcción de cadenas de búsqueda booleana es uno de los pasos más críticos y propensos a errores en cualquier revisión sistemática. Esta herramienta busca sistematizar ese proceso, reducir errores y hacerlo reproducible, poniendo la programación al servicio del trabajo investigativo.

---

## Tecnologías

- Java 21
- IntelliJ IDEA
- Sin dependencias externas

---

## Roadmap general del proyecto

- [x] v1.0 — Versión consola SPIDER con exportación CSV
- [x] v1.1 — Soporte PICO y PICOS con herencia
- [x] v1.2 — Historial de búsquedas
- [x] v2.0 — Versión web con Spring Boot y Thymeleaf
- [x] v2.1 — Integración Zotero API y extracción de estudios
- [x] v2.2 — Diagrama PRISMA 2020 con cajas dinámicas
- [x] v2.3 — Checklist PRISMA 2020 con 27 ítems
- [x] v2.4 — Asistente IA con Claude (Anthropic)
- [x] v2.5 — Dashboard de revisión y flujo guiado
- [ ] v3.0 — Base de datos PostgreSQL, cuentas de usuario, doble revisor
- [ ] v3.1 — Importación RIS / BibTeX / CSV Scopus / WoS
- [ ] v3.2 — Deduplicación asistida por DOI y título
- [ ] v3.3 — Evaluación de riesgo de sesgo (CASP / RoB 2)
- [ ] v3.4 — Log metodológico auditable
- [ ] v3.5 — Reportes completos para manuscrito

---

## Autor

**Felipe Ponce Bollmann** — Investigador y desarrollador en formación
Sociólogo e investigador multidisciplinar · La Salle Barcelona
[poncebollmann@gmail.com](mailto:poncebollmann@gmail.com)
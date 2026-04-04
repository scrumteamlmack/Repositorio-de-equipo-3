"""
Utilidades compartidas para exportar tablas a PDF (horizontal + celdas con ajuste de texto).
"""
from __future__ import annotations

import re
from io import BytesIO
from xml.sax.saxutils import escape

from django.http import HttpResponse


def q_param(request) -> str:
    return (request.GET.get("q") or "").strip()


def maybe_int(value: str):
    if value and re.fullmatch(r"\d+", str(value).strip()):
        return int(value)
    return None


def safe_q_part(q: str) -> str:
    q = (q or "").strip()
    if not q:
        return "todo"
    q = re.sub(r"[^0-9A-Za-z_-]+", "_", q).strip("_")
    return q[:50] if q else "todo"


def landscape_pdf_response(
    title: str,
    q: str,
    headers: list[str],
    rows: list[list[str]],
    col_ratios: list[float],
    attachment_filename: str,
) -> HttpResponse:
    """
    Genera un PDF en A4 horizontal con encabezado, filtro y tabla usando Paragraph por celda.
    `rows` debe tener la misma cantidad de columnas que `headers`.
    `col_ratios` debe sumar 1.0 (se normaliza si hace falta).
    """
    if len(headers) != len(col_ratios):
        raise ValueError("headers y col_ratios deben tener la misma longitud")
    for r in rows:
        if len(r) != len(headers):
            raise ValueError("cada fila debe tener la misma longitud que headers")

    from reportlab.lib import colors
    from reportlab.lib.enums import TA_CENTER, TA_LEFT
    from reportlab.lib.pagesizes import A4, landscape
    from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
    from reportlab.platypus import Paragraph, SimpleDocTemplate, Spacer, Table, TableStyle

    styles = getSampleStyleSheet()
    page = landscape(A4)
    buf = BytesIO()
    side = 36
    slug = re.sub(r"\W+", "_", title)[:24] or "pdf"
    doc = SimpleDocTemplate(
        buf,
        pagesize=page,
        leftMargin=side,
        rightMargin=side,
        topMargin=48,
        bottomMargin=48,
    )
    avail_w = page[0] - doc.leftMargin - doc.rightMargin

    cell_style = ParagraphStyle(
        f"{slug}_cell",
        parent=styles["Normal"],
        fontSize=7,
        leading=9,
        alignment=TA_LEFT,
    )
    hdr_style = ParagraphStyle(
        f"{slug}_hdr",
        parent=styles["Normal"],
        fontName="Helvetica-Bold",
        fontSize=8,
        leading=10,
        textColor=colors.white,
        alignment=TA_CENTER,
    )

    def cell_paragraph(text, style=cell_style):
        t = escape(str(text or ""))
        t = t.replace("\n", "<br/>")
        return Paragraph(t, style)

    elements = [Paragraph(escape(title), styles["Title"]), Spacer(1, 12)]
    subtitle = f"Filtro: {q}" if q else "Sin filtro"
    elements.append(Paragraph(escape(subtitle), styles["BodyText"]))
    elements.append(Spacer(1, 12))

    data = [[cell_paragraph(h, hdr_style) for h in headers]]
    for row in rows:
        data.append([cell_paragraph(c) for c in row])

    ratio_sum = sum(col_ratios)
    norm = [r / ratio_sum for r in col_ratios]
    col_widths = [avail_w * r for r in norm]

    table = Table(data, repeatRows=1, colWidths=col_widths)
    table.setStyle(
        TableStyle(
            [
                ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#0f766e")),
                ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),
                ("GRID", (0, 0), (-1, -1), 0.25, colors.grey),
                ("VALIGN", (0, 0), (-1, -1), "TOP"),
                ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.whitesmoke]),
                ("LEFTPADDING", (0, 0), (-1, -1), 4),
                ("RIGHTPADDING", (0, 0), (-1, -1), 4),
                ("TOPPADDING", (0, 0), (-1, -1), 4),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 4),
            ]
        )
    )
    elements.append(table)
    doc.build(elements)

    resp = HttpResponse(buf.getvalue(), content_type="application/pdf")
    resp["Content-Disposition"] = f'attachment; filename="{attachment_filename}"'
    return resp

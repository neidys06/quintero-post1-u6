<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="/WEB-INF/views/idioma.jspf" %>
<!DOCTYPE html>
<html lang="${not empty cookie.idiomaPreferido ? cookie.idiomaPreferido.value : 'es'}">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="form.titulo"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <h1><fmt:message key="form.titulo"/></h1>

    <%-- Resumen de todos los errores; además cada campo muestra el suyo --%>
    <c:if test="${not empty errores}">
        <div class="alert-error">
            <ul><c:forEach var="e" items="${errores}"><li><c:out value="${e.value}"/></li></c:forEach></ul>
        </div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/app">
        <input type="hidden" name="comando" value="guardar">
        <label><fmt:message key="lista.titulo"/>:
            <input type="text" name="titulo" value="<c:out value='${titulo}'/>"
                   class="${not empty errores.titulo ? 'input-error' : ''}">
            <c:if test="${not empty errores.titulo}">
                <span class="campo-error">${errores.titulo}</span>
            </c:if>
        </label>
        <label><fmt:message key="lista.categoria"/>:
            <input type="text" name="categoria" value="<c:out value='${categoria}'/>"
                   class="${not empty errores.categoria ? 'input-error' : ''}">
            <c:if test="${not empty errores.categoria}">
                <span class="campo-error">${errores.categoria}</span>
            </c:if>
        </label>
        <label><fmt:message key="lista.prioridad"/>:
            <select name="prioridad" class="${not empty errores.prioridad ? 'input-error' : ''}">
                <option value="Alta"  ${prioridad == "Alta"  ? "selected" : ""}>Alta</option>
                <option value="Media" ${prioridad == "Media" or empty prioridad ? "selected" : ""}>Media</option>
                <option value="Baja"  ${prioridad == "Baja"  ? "selected" : ""}>Baja</option>
            </select>
            <c:if test="${not empty errores.prioridad}">
                <span class="campo-error">${errores.prioridad}</span>
            </c:if>
        </label>
        <label><fmt:message key="lista.fecha"/> (<fmt:message key="form.formato"/>):
            <input type="text" name="fechaLimite" value="<c:out value='${fechaLimite}'/>"
                   placeholder="2026-12-20"
                   class="${not empty errores.fechaLimite ? 'input-error' : ''}">
            <c:if test="${not empty errores.fechaLimite}">
                <span class="campo-error">${errores.fechaLimite}</span>
            </c:if>
        </label>
        <button type="submit"><fmt:message key="form.guardar"/></button>
        <a href="${pageContext.request.contextPath}/app"><fmt:message key="form.cancelar"/></a>
    </form>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="/WEB-INF/views/idioma.jspf" %>
<!DOCTYPE html>
<html lang="${not empty cookie.idiomaPreferido ? cookie.idiomaPreferido.value : 'es'}">
<head>
    <meta charset="UTF-8">
    <title>${applicationScope.nombreApp}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <div class="cabecera">
        <h1>${applicationScope.nombreApp}</h1>
        <p class="saludo">
            <fmt:message key="lista.bienvenida"/>, <c:out value="${sessionScope.usuarioActual.nombreCompleto}"/>
            (${sessionScope.usuarioActual.rol}) —
            <a href="${pageContext.request.contextPath}/app?comando=logout"><fmt:message key="lista.salir"/></a>
        </p>
        <p>
            <a href="${pageContext.request.contextPath}/app?comando=idioma&lang=es">Español</a> |
            <a href="${pageContext.request.contextPath}/app?comando=idioma&lang=en">English</a>
        </p>
    </div>

    <c:if test="${not empty error}">
        <p class="error"><c:out value="${error}"/></p>
    </c:if>

    <a href="${pageContext.request.contextPath}/app?comando=formulario"><fmt:message key="lista.nueva"/></a>

    <fmt:message key="lista.confirmar" var="textoConfirmar"/>
    <table>
        <thead><tr>
            <th><fmt:message key="lista.titulo"/></th>
            <th><fmt:message key="lista.categoria"/></th>
            <th><fmt:message key="lista.prioridad"/></th>
            <th><fmt:message key="lista.fecha"/></th>
            <th><fmt:message key="lista.estado"/></th>
            <th><fmt:message key="lista.acciones"/></th>
        </tr></thead>
        <tbody>
        <c:forEach var="t" items="${tareas}">
            <tr>
                <td class="${t.completada ? 'completada' : ''}"><c:out value="${t.titulo}"/></td>
                <td><c:out value="${t.categoria}"/></td>
                <td>${t.prioridad}</td>
                <td><fmt:formatDate value="${t.fechaLimite}" pattern="dd/MM/yyyy"/></td>
                <td><fmt:message key="${t.completada ? 'estado.completada' : 'estado.pendiente'}"/></td>
                <td>
                    <a href="${pageContext.request.contextPath}/app?comando=completar&id=${t.id}"><fmt:message key="lista.completar"/></a> |
                    <a href="${pageContext.request.contextPath}/app?comando=eliminar&id=${t.id}"
                       onclick="return confirm('${textoConfirmar}')"><fmt:message key="lista.eliminar"/></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</body>
</html>

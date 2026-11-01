<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="status" value="${requestScope['jakarta.servlet.error.status_code']}"/>
<c:set var="pageTitle" value="Something went wrong" scope="request"/>
<%@ include file="/WEB-INF/views/layout/header.jspf" %>
<div class="text-center py-5">
    <c:choose>
        <c:when test="${status == 403}">
            <h1 class="display-6">Access denied</h1>
            <p class="text-muted"><c:out value="${requestScope['jakarta.servlet.error.message']}"/></p>
        </c:when>
        <c:when test="${status == 404}">
            <h1 class="display-6">Page not found</h1>
            <p class="text-muted">We could not find what you were looking for.</p>
        </c:when>
        <c:otherwise>
            <h1 class="display-6">Something went wrong</h1>
            <p class="text-muted">The problem has been logged. Please try again in a moment.</p>
        </c:otherwise>
    </c:choose>
    <a class="btn btn-primary" href="${ctx}/app/dashboard">Back to the dashboard</a>
</div>
<%@ include file="/WEB-INF/views/layout/footer.jspf" %>
